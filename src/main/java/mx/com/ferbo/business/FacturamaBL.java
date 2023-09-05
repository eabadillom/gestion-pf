package mx.com.ferbo.business;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ferbo.facturama.business.CfdiBL;
import com.ferbo.facturama.request.CFDIInfo;
import com.ferbo.facturama.request.IssuerBindingModel;
import com.ferbo.facturama.request.ItemFullBindingModel;
import com.ferbo.facturama.request.ReceiverBindingModel;
import com.ferbo.facturama.request.Tax;
import com.ferbo.facturama.response.CfdiInfoModel;
import com.ferbo.facturama.response.FileViewModel;
import com.ferbo.facturama.tools.FacturamaException;
import com.ferbo.mail.beans.Adjunto;

import mx.com.ferbo.dao.ClaveUnidadDAO;
import mx.com.ferbo.dao.FacturaDAO;
import mx.com.ferbo.model.ClaveUnidad;
import mx.com.ferbo.model.ConstanciaFactura;
import mx.com.ferbo.model.ConstanciaFacturaDs;
import mx.com.ferbo.model.Factura;
import mx.com.ferbo.model.FacturaMedioPago;
import mx.com.ferbo.model.ServicioConstancia;
import mx.com.ferbo.model.ServicioConstanciaDs;
import mx.com.ferbo.model.ServicioFactura;
import mx.com.ferbo.model.Usuario;
import mx.com.ferbo.util.InventarioException;

public class FacturamaBL {
	
	private Logger log = LogManager.getLogger(FacturamaBL.class);
	
	private FacturaDAO facturaDAO = null;
	private ClaveUnidadDAO claveDAO = null;
	private CfdiBL cfdiBL = null;
	private Factura factura = null;
	private Integer idFactura = null;
	private Usuario usuario = null;
	
	public FacturamaBL(Integer idFactura, Usuario usuario) {
		facturaDAO = new FacturaDAO();
		claveDAO = new ClaveUnidadDAO();
		cfdiBL = new CfdiBL();
		this.idFactura = idFactura;
		this.usuario = usuario;
	}
	
	public void timbrar() throws FacturamaException, InventarioException {
        CFDIInfo cfdi = null;
		BigDecimal tasaIva = null;
		
		cfdiBL = new CfdiBL();
		cfdi = new CFDIInfo();
		
		factura = facturaDAO.buscarPorId(idFactura, true);
		
		if(factura == null)
			throw new InventarioException("No se encontró la factura: " + idFactura);
		
		if(factura.getStatus().getId() == 3) 
			throw new InventarioException("La factura ya se encuentra cancelada, no es posible timbrar.");
		
		if(factura.getUuid() != null && factura.getUuid().trim().length() > 0) {
			log.info("La factura con id: {} ya se encuentra timbrada (id Facturama {}).", factura.getId(), factura.getUuid());
			return;
		}
		
		tasaIva = factura.getPorcentajeIva().divide(new BigDecimal("100.00"), BigDecimal.ROUND_HALF_UP);
		
		// Datos de emisor
		IssuerBindingModel is = new IssuerBindingModel();
		is.setName(factura.getEmisorNombre());
		is.setFiscalRegime(factura.getEmisorCdRegimen());
		is.setRfc(factura.getEmisorRFC());
		cfdi.setIssuer(is);
		
		// Datos de receptor
		ReceiverBindingModel receptor = new ReceiverBindingModel();
		receptor.setRfc(factura.getRfc());
		receptor.setCfdiUse(factura.getCdUsoCfdi());
		receptor.setName(factura.getNombreCliente());
		receptor.setFiscalRegime(factura.getCdRegimen());
		receptor.setTaxZipCode(factura.getCp());
		cfdi.setReceiver(receptor);
		// Datos generales de la factura
		cfdi.setDate(factura.getFecha());
		cfdi.setFolio(factura.getNumero());
		cfdi.setSerie(factura.getNomSerie());
		cfdi.setCurrency(factura.getMoneda());
		cfdi.setCfdiType("I");
		FacturaMedioPago facturaMedioPago = factura.getFacturaMedioPagoList().get(0);
		cfdi.setPaymentForm(facturaMedioPago.getMpId().getFormaPago());
		cfdi.setExpeditionPlace(factura.getPlanta().getCodigopostal().toString());
		cfdi.setPaymentMethod(factura.getMetodoPago());
		cfdi.setObservations(factura.getObservacion());
		
		// Productos/Servicios a facturar
		List<ItemFullBindingModel> listaItems = new ArrayList<ItemFullBindingModel>();
		List<ServicioFactura> alServiciosDetalle = factura.getServicioFacturaList();
		for (ServicioFactura sf : alServiciosDetalle) {
			ItemFullBindingModel item = new ItemFullBindingModel();
			item.setProductCode(sf.getCodigo());
			item.setDescription(sf.getDescripcion());
			item.setUnitCode(sf.getCdUnidad());
			ClaveUnidad claveUnidad = claveDAO.buscarPorId(sf.getCdUnidad());
			item.setUnit(claveUnidad.getNbUnidad());
			// item.setUnit(claveUnidad.getNombre());
			item.setQuantity(sf.getCantidad());
			item.setUnitPrice(sf.getTarifa().setScale(2, BigDecimal.ROUND_HALF_UP));
			item.setSubtotal(sf.getCosto()); // importe
			item.setTaxObject("02");
			
			Tax tx = new Tax();
			tx.setBase(sf.getCosto());
			BigDecimal ivaServicio = sf.getCosto().multiply(tasaIva);
			tx.setTotal(ivaServicio);
			tx.setName("IVA");
			tx.setRate(tasaIva);
			tx.setIsRetention(false);
			item.setTaxes(new ArrayList<Tax>());
			item.setTotal(sf.getCosto().add(ivaServicio));
			item.getTaxes().add(tx);
			listaItems.add(item);
		}
		
		
		for(ConstanciaFactura cf: factura.getConstanciaFacturaList()) {
			for(ServicioConstancia sc: cf.getServicioConstanciaList()) {
				ItemFullBindingModel item = new ItemFullBindingModel();
				
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");//formateo de vigenciafin
				String fechaVigencia = format.format(cf.getVigenciaFin());
				
				String descripcion = sc.getDescripcion() + " - " + "CONSTANCIA " + cf.getId() + " - " + "VIGENCIA " + cf.getVigenciaInicio() + " AL " + fechaVigencia + " - " + "Tipo de cobro: " + sc.getUnidadMedida();
				
				item.setProductCode(sc.getCodigo());
				item.setDescription(descripcion);//modificar leyenda 
				ClaveUnidad claveUnidad = claveDAO.buscarPorId(sc.getCdUnidad());
				item.setUnit(claveUnidad.getNbUnidad());
				item.setUnitCode(sc.getCdUnidad());
				item.setUnitPrice(sc.getTarifa().setScale(2, BigDecimal.ROUND_HALF_UP));
				item.setQuantity(sc.getBaseCargo());
				item.setSubtotal(sc.getCosto().setScale(2, BigDecimal.ROUND_HALF_UP));
				item.setTaxObject("02");
				
				Tax tx = new Tax();
				
				BigDecimal ivaTotal = sc.getCosto().multiply(tasaIva);
				tx.setTotal(ivaTotal.setScale(2, BigDecimal.ROUND_HALF_UP));
				tx.setName("IVA");
				tx.setBase(sc.getCosto().setScale(2, BigDecimal.ROUND_HALF_UP));
				tx.setRate(tasaIva);
				tx.setIsRetention(false);
				item.setTaxes(new ArrayList<Tax>());
				item.setTotal(sc.getCosto().add(ivaTotal).setScale(2, BigDecimal.ROUND_HALF_UP));
				item.getTaxes().add(tx);
				listaItems.add(item);
			}
		}
		
		for(ConstanciaFacturaDs cf: factura.getConstanciaFacturaDsList()) {
			for(ServicioConstanciaDs sc: cf.getServicioConstanciaDsList()) {
				ItemFullBindingModel item = new ItemFullBindingModel();
				
				item.setProductCode(sc.getCodigo());
				item.setDescription(sc.getDescripcion());
				ClaveUnidad claveUnidad = claveDAO.buscarPorId(sc.getCdUnidad());
				item.setUnit(claveUnidad.getNbUnidad());
				item.setUnitCode(sc.getCdUnidad());
				item.setUnitPrice(sc.getTarifa());
				item.setQuantity(sc.getCantidad());
				item.setSubtotal(sc.getCosto().setScale(2, BigDecimal.ROUND_HALF_UP));
				item.setTaxObject("02");
				
				Tax tx = new Tax();
				
				BigDecimal ivaTotal = sc.getCosto().multiply(tasaIva);
				tx.setTotal(ivaTotal.setScale(2, BigDecimal.ROUND_HALF_UP));
				tx.setName("IVA");
				tx.setBase(sc.getCosto().setScale(2, BigDecimal.ROUND_HALF_UP));
				tx.setRate(tasaIva.setScale(2, BigDecimal.ROUND_HALF_UP));
				tx.setIsRetention(false);
				item.setTaxes(new ArrayList<Tax>());
				BigDecimal bdTotalItem = sc.getCosto().add(ivaTotal);
				item.setTotal(bdTotalItem.setScale(2, BigDecimal.ROUND_HALF_UP));
				item.getTaxes().add(tx);
				listaItems.add(item);
			}
		}
		
		cfdi.setItems(listaItems);
		
		CfdiInfoModel registra = cfdiBL.registra(cfdi);
		factura.setUuid(registra.getId());
		facturaDAO.actualizar(factura);
	}
	
	public void sendMail() throws FacturamaException {
		
		SendMailFacturaBL sendMailBO = null;
        String sContent = null;
        byte[] content = null;
        
        Adjunto adjunto = null;
        List<Adjunto> alAdjuntos = null;
        
        try {
        	alAdjuntos = new ArrayList<Adjunto>();
        	
        	if(factura == null)
        		throw new InventarioException("No se estableció una factura para envío por correo electrónico.");
        	
        	FileViewModel fileXML = cfdiBL.getFile("xml", "issuedLite", factura.getUuid());
        	sContent = fileXML.getContent();
        	content = Base64.getDecoder().decode(sContent);
        	adjunto = new Adjunto("Factura_" + factura.getNomSerie() + "-" + factura.getNumero() + ".xml", Adjunto.TP_ARCHIVO_XML, content);
        	alAdjuntos.add(adjunto);
        	
        	FileViewModel filePDF = cfdiBL.getFile("pdf", "issuedLite", factura.getUuid());
        	sContent = filePDF.getContent();
        	content = Base64.getDecoder().decode(sContent);
        	adjunto = new Adjunto("Factura_" + factura.getNomSerie()+ "-" + factura.getNumero() + ".pdf", Adjunto.TP_ARCHIVO_PDF, content);
        	alAdjuntos.add(adjunto);
        	
        	sendMailBO = new SendMailFacturaBL(factura.getCliente().getCteCve());
        	sendMailBO.setSerie(factura.getNomSerie());
        	sendMailBO.setFolio(factura.getNumero());
        	sendMailBO.setAlFiles(alAdjuntos);
        	sendMailBO.setLoggedUser(usuario);
        	sendMailBO.send();
        	
        } catch(InventarioException ex) {
        	log.error("Problema en el envío de correo electrónico de los documentos CFDI...", ex);
        }
	}
	
	

}
