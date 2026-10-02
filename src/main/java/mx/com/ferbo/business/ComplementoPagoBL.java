package mx.com.ferbo.business;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ferbo.facturama.business.CfdiBL;
import com.ferbo.facturama.request.CFDIInfo;
import com.ferbo.facturama.request.Complement;
import com.ferbo.facturama.request.IssuerBindingModel;
import com.ferbo.facturama.request.PaymentBindingModel;
import com.ferbo.facturama.request.ReceiverBindingModel;
import com.ferbo.facturama.request.RelatedDocument;
import com.ferbo.facturama.request.Tax;
import com.ferbo.facturama.response.CfdiInfoModel;
import com.ferbo.facturama.response.FileViewModel;
import com.ferbo.facturama.tools.FacturamaException;
import com.ferbo.mail.beans.Adjunto;

import mx.com.ferbo.business.complemento.ComplementoBL;
import mx.com.ferbo.dao.n.ComplementoPagoDAO;
import mx.com.ferbo.model.ComplementoPago;
import mx.com.ferbo.model.Pago;
import mx.com.ferbo.model.Usuario;
import mx.com.ferbo.ui.DatosPago;
import mx.com.ferbo.util.DateUtil;
import mx.com.ferbo.util.InventarioException;

public class ComplementoPagoBL
{
    private Logger log = LogManager.getLogger(ComplementoPagoBL.class);
    
    public static final Integer NAME_ID = 14;
    public static final String  TIPO_CFDI = "P";
    public static final String  USO_CFDI = "CP01";
    public static final String  CURRENCY = "MXN";
    public static final String  PAYMENT_METHOD = "PPD";
    public static final String  TAX_NAME = "IVA";
    public static final String  TAX_OBJECT = "02";
    
    private ComplementoPago complementoPago;
    private ComplementoPagoDAO complementoPagoDAO;
    private ComplementoBL complementoPagoBL;
    private CfdiBL cfdiBL = null;
    
    private Usuario usuario = null;
    private String formaPago;
    
    public ComplementoPagoBL() {
    	complementoPagoDAO = new ComplementoPagoDAO();
    	complementoPagoBL = new ComplementoBL();
    	cfdiBL = new CfdiBL();
    }
    
    public ComplementoPagoBL(ComplementoPago complemento)
    throws InventarioException {
    	this();
    	
    	this.complementoPago = complementoPagoDAO
    			.cargar(complemento.getId())
    			.orElseThrow(() -> new InventarioException("Problema al cargar el complemento de pago."));
    }
    
    public void timbrar() throws InventarioException, FacturamaException {
    	CFDIInfo cfdi = new CFDIInfo();
    	
    	cfdi.setCfdiType(TIPO_CFDI);
        cfdi.setNameId(NAME_ID); 
        cfdi.setFolio(complementoPago.getNumero());
        cfdi.setSerie(complementoPago.getSerie());
        cfdi.setExpeditionPlace(complementoPago.getLugarExpedicion());
        
        IssuerBindingModel emisor = new IssuerBindingModel();
        emisor.setRfc(complementoPago.getEmisorRFC());
        emisor.setName(complementoPago.getEmisorNombre());
        emisor.setFiscalRegime(complementoPago.getEmisorRegimenFiscal());
        cfdi.setIssuer(emisor);
        
        ReceiverBindingModel receptor = new ReceiverBindingModel(); 
        receptor.setRfc(complementoPago.getReceptorRFC());
        receptor.setCfdiUse(complementoPago.getUsoCFDI());
        receptor.setName(complementoPago.getReceptorNombre());
        receptor.setFiscalRegime(complementoPago.getReceptorRegimenFiscal());
        receptor.setTaxZipCode(complementoPago.getReceptorCodigoPostal());
        cfdi.setReceiver(receptor);
        
        Complement complemento = new Complement();
        List<PaymentBindingModel> pagos = new ArrayList<PaymentBindingModel>();
        List<RelatedDocument> documentosRelacionados;
        List<Tax> impuestos;
        for(Pago ingreso : this.complementoPago.getListPagos()) {
        	if(ingreso.getFactura().getCfdi() == null) {
        		String message = String.format("El complemento de pago no se puede procesar porque la factura %s-%s no está certificada ante el SAT (sin timbrar).", ingreso.getFactura().getNomSerie(), ingreso.getFactura().getNumero());
        		throw new InventarioException(message);
        	}
        	
            documentosRelacionados = new ArrayList<RelatedDocument>();
            impuestos = new ArrayList<Tax>();
            
            PaymentBindingModel pago = new PaymentBindingModel();
            Date fechaHora = ingreso.getFecha();
            DateUtil.setTime(fechaHora, ingreso.getHora().getHour(), ingreso.getHora().getMinute(), 0);
            pago.setDate(fechaHora);
            pago.setPaymentForm(ingreso.getFormaPago());
            pago.setAmount(ingreso.getMonto());
            pago.setCurrency(CURRENCY);

            DatosPago saldosPago = complementoPagoBL.obtenerSaldos(ingreso);
            RelatedDocument documentoRelacionado = new RelatedDocument();
            documentoRelacionado.setTaxObject(TAX_OBJECT);
            documentoRelacionado.setUuid(ingreso.getFactura().getCfdi().getUuid());
            documentoRelacionado.setPartialityNumber(ingreso.getParcialidad());
            documentoRelacionado.setSerie(ingreso.getFactura().getNomSerie());
            documentoRelacionado.setFolio(ingreso.getFactura().getNumero());
            documentoRelacionado.setCurrency(CURRENCY);
            documentoRelacionado.setPaymentMethod(formaPago);
            documentoRelacionado.setPreviousBalanceAmount(saldosPago.getSaldoAnterior());
            documentoRelacionado.setAmountPaid(saldosPago.getMonto());
            documentoRelacionado.setImpSaldoInsoluto(saldosPago.getSaldoRestante());

            
            Tax impuesto = this.procesaIVA(ingreso);
            impuestos.add(impuesto);

            documentoRelacionado.setTaxes(impuestos);
            documentosRelacionados.add(documentoRelacionado);
            pago.setRelatedDocuments(documentosRelacionados);
            pagos.add(pago);
        }
        complemento.setPayments(pagos);
        cfdi.setComplemento(complemento);
        
        CfdiInfoModel registra = cfdiBL.registra(cfdi);
        
        String idPac             = registra.getId();
        String uuid              = registra.getComplement().getTaxStamp().getUuid();
        Date   fecha             = DateUtil.getDate(registra.getComplement().getTaxStamp().getDate(),DateUtil.FORMATO_ISO_8601);
        String numCertificadoSAT = registra.getComplement().getTaxStamp().getSatCertNumber();
        
        complementoPago.setTimbrado(fecha);
        complementoPago.setIdPac(idPac);
        complementoPago.setUuid(uuid);
        complementoPago.setCertificadoSAT(numCertificadoSAT);
        
        complementoPagoDAO.actualizar(complementoPago);
    }
    
    private Tax procesaIVA(Pago pago) {
    	Tax tax;
    	BigDecimal importeIVA;
    	com.ferbo.tools.value.money.Tax iva = com.ferbo.tools.value.money.Tax.ofPercentage(pago.getFactura().getPorcentajeIva());
    	
    	BigDecimal total = pago.getMonto();
    	
    	
        BigDecimal factorIVA = BigDecimal.ONE.add(iva.getRate()).setScale(2, RoundingMode.HALF_UP);
        BigDecimal subtotal = total.divide(factorIVA, 2, RoundingMode.HALF_UP);
        importeIVA = total.subtract(subtotal);
        
        tax = new Tax();
        tax.setName(TAX_NAME);
        tax.setRate(iva.getRate().setScale(2, RoundingMode.HALF_UP));
        tax.setTotal(importeIVA);
        tax.setBase(subtotal);
        tax.setIsRetention(false);
    	
    	return tax;
    }
    
//    @Deprecated
//    public void timbrar() throws InventarioException, DAOException, JsonProcessingException, FacturamaException {
//        CFDIInfo cfdi = new CFDIInfo();
//        
//        complementoPagoBL = new ComplementoBL();
//        
//        Cliente cliente = clienteDAO.obtenerPorId(idReceptor, true);
//        EmisoresCFDIS emisor = emisoresDAO.buscarPorId(idEmisor).orElseThrow(() -> new InventarioException("Emisor no encontrado."));
//        
//        complementoPago = listPagos.stream()
//            .map(Pago::getComplementoPago)
//            .filter(Objects::nonNull)
//            .findFirst()
//            .orElse(null);
//        
//        if(complementoPago == null) 
//            throw new InventarioException("El folio y serie del complemento de pago no estan asignados.");
//        
//        cfdi.setCfdiType(TIPO_CFDI);
//        cfdi.setNameId(NAME_ID); 
//        cfdi.setFolio(complementoPago.getNumero());
//        cfdi.setSerie(complementoPago.getSerie());
//        cfdi.setExpeditionPlace(emisor.getCodigoPostal());
//        
//        IssuerBindingModel issuerBindingModel = new IssuerBindingModel();
//        issuerBindingModel.setRfc(emisor.getNb_rfc());
//        issuerBindingModel.setName(emisor.getNb_emisor());
//        issuerBindingModel.setFiscalRegime(emisor.getCd_regimen().getCd_regimen());
//        cfdi.setIssuer(issuerBindingModel);
//        
//        String cp = complementoPagoBL.obtenerCP(cliente);
//        ReceiverBindingModel receptor = new ReceiverBindingModel(); 
//        receptor.setRfc(cliente.getCteRfc());
//        receptor.setCfdiUse(USO_CFDI);
//        receptor.setName(cliente.getNombre());
//        receptor.setFiscalRegime(cliente.getRegimenFiscal().getCd_regimen());
//        receptor.setTaxZipCode(cp);
//        cfdi.setReceiver(receptor);
//        
//        Complement complements = new Complement();
//        List<PaymentBindingModel> listPayments = new ArrayList<PaymentBindingModel>();
//        List<RelatedDocument> listRelatedDocuments;
//        List<Tax> listTaxes;
//        for(Pago pago : this.listPagos) {
//            listRelatedDocuments = new ArrayList<RelatedDocument>();
//            listTaxes = new ArrayList<Tax>();
//            PaymentBindingModel payment = new PaymentBindingModel();
//            Date fechaHora = pago.getFecha();
//            DateUtil.setTime(fechaHora, pago.getHora().getHour(), pago.getHora().getMinute(), 0);
//            payment.setDate(fechaHora);
//            payment.setPaymentForm(pago.getFormaPago());
//            payment.setAmount(pago.getMonto());
//            payment.setCurrency(CURRENCY);
//
//            DatosPago saldosPago = complementoPagoBL.obtenerSaldos(pago);
//            RelatedDocument relatedDocument = new RelatedDocument();
//            relatedDocument.setTaxObject(TAX_OBJECT);
//            relatedDocument.setUuid(pago.getFactura().getCfdi().getUuid());
//            relatedDocument.setPartialityNumber(pago.getParcialidad());
//            relatedDocument.setSerie(pago.getFactura().getNomSerie());
//            relatedDocument.setFolio(pago.getFactura().getNumero());
//            relatedDocument.setCurrency(CURRENCY);
//            relatedDocument.setPaymentMethod(formaPago);
//            relatedDocument.setPreviousBalanceAmount(saldosPago.getSaldoAnterior());
//            relatedDocument.setAmountPaid(saldosPago.getMonto());
//            relatedDocument.setImpSaldoInsoluto(saldosPago.getSaldoRestante());
//
//            BigDecimal total = pago.getMonto();
//            BigDecimal factorIVA = new BigDecimal("1.16");
//            BigDecimal subtotal = total.divide(factorIVA, 2, RoundingMode.HALF_UP);
//            BigDecimal iva = total.subtract(subtotal);
//            BigDecimal rate = new BigDecimal("0.16");
//            Tax tax = new Tax();
//            tax.setName(TAX_NAME);
//            tax.setRate(rate.setScale(2, RoundingMode.HALF_UP));
//            tax.setTotal(iva);
//            tax.setBase(subtotal);
//            tax.setIsRetention(false);
//            listTaxes.add(tax);
//
//            relatedDocument.setTaxes(listTaxes);
//            listRelatedDocuments.add(relatedDocument);
//            payment.setRelatedDocuments(listRelatedDocuments);
//            listPayments.add(payment);
//        }
//        complements.setPayments(listPayments);
//        cfdi.setComplemento(complements);
//        
//        CfdiInfoModel registra = cfdiBL.registra(cfdi);
//        
//        String idPac             = registra.getId();
//        String uuid              = registra.getComplement().getTaxStamp().getUuid();
//        Date   fecha             = DateUtil.getDate(registra.getComplement().getTaxStamp().getDate(),DateUtil.FORMATO_ISO_8601);
//        String numCertificadoSAT = registra.getComplement().getTaxStamp().getSatCertNumber();
//        
//        complementoPago.setTimbrado(fecha);
//        complementoPago.setIdPac(idPac);
//        complementoPago.setUuid(uuid);
//        complementoPago.setCertificadoSAT(numCertificadoSAT);
//        
//        complementoPagoDAO.actualizar(complementoPago);
//    }
    
    public void sendMail() throws FacturamaException {
        SendMailComplementoPagoBL sendMailBO = null;
        String sContent = null;
        byte[] content = null;
        
        Adjunto adjunto = null;
        List<Adjunto> alAdjuntos = null;
        try {
            alAdjuntos = new ArrayList<Adjunto>();
            
            if(complementoPago == null)
                throw new InventarioException("No se estableció un complemento de pago para envío por correo electrónico.");
            
            FileViewModel fileXML = cfdiBL.getFile("xml", "issuedLite", complementoPago.getIdPac());
            sContent = fileXML.getContent();
            content = Base64.getDecoder().decode(sContent);
            adjunto = new Adjunto("ComplementoPago_" + complementoPago.getSerie() + "-" + complementoPago.getNumero() + ".xml", Adjunto.TP_ARCHIVO_XML, content);
            alAdjuntos.add(adjunto);

            FileViewModel filePDF = cfdiBL.getFile("pdf", "issuedLite", complementoPago.getIdPac());
            sContent = filePDF.getContent();
            content = Base64.getDecoder().decode(sContent);
            adjunto = new Adjunto("ComplementoPago_" + complementoPago.getSerie()+ "-" + complementoPago.getNumero() + ".pdf", Adjunto.TP_ARCHIVO_PDF, content);
            alAdjuntos.add(adjunto);
            
            sendMailBO = new SendMailComplementoPagoBL(this.complementoPago.getReceptor().getCteCve());
            sendMailBO.setSerie(complementoPago.getSerie());
            sendMailBO.setFolio(complementoPago.getNumero());
            sendMailBO.setAlFiles(alAdjuntos);
            sendMailBO.setLoggedUser(usuario);
            sendMailBO.send();
        } catch(InventarioException ex) {
            log.error("Problema en el envío de correo electrónico de los documentos CFDI...", ex);
        }
    }
    
}
