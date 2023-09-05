package mx.com.ferbo.controller;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.PrimeFaces;

import mx.com.ferbo.dao.AsentamientoHumandoDAO;
import mx.com.ferbo.dao.ClienteDAO;
import mx.com.ferbo.dao.ClienteDomiciliosDAO;
import mx.com.ferbo.dao.FacturaDAO;
import mx.com.ferbo.dao.SerieNotaDAO;
import mx.com.ferbo.dao.notaCreditoDAO;
import mx.com.ferbo.model.AsentamientoHumano;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.ClienteDomicilios;
import mx.com.ferbo.model.Domicilios;
import mx.com.ferbo.model.Factura;
import mx.com.ferbo.model.NotaCredito;
import mx.com.ferbo.model.NotaPorFactura;
import mx.com.ferbo.model.NotaPorFacturaPK;
import mx.com.ferbo.model.SerieNota;
import mx.com.ferbo.model.StatusFactura;
import mx.com.ferbo.model.StatusNotaCredito;
import mx.com.ferbo.util.FormatUtil;

@Named
@ViewScoped

public class AltaNotasCredito implements Serializable{
	
	private static final long serialVersionUID = -626048119540963939L;
	
	private List<Cliente> listaClientes;
	private List<Factura> listaFactura;
	private List<ClienteDomicilios> listaClienteDom;
	private List<ClienteDomicilios> listaClienteDomicilio;
	private List<NotaPorFactura> listaNotaXFactura;
	private List<SerieNota> listaSerieNota;
	
	private ClienteDAO clienteDAO;
	private FacturaDAO facturaDAO;
	private ClienteDomiciliosDAO clienteDomicilioDAO;
	private AsentamientoHumandoDAO asentamientoHumanoDAO;
	private notaCreditoDAO notaCreditoDAO;
	private SerieNotaDAO serieNotaDAO;
	//private NotaPorFacturaDAO notaFactDAO;
	
	private Cliente clienteSelect;
	private Domicilios domicilioSelect;
	private Factura facturaSelect;
	private NotaPorFactura notaPorFactura;
	private AsentamientoHumano asentamientoCliente;
	private NotaCredito notaCredito;
	private SerieNota serieNotaSelect;
	
	private Boolean pagoParcial;
	private Boolean porCobrar;
	private Boolean pagada;
	private BigDecimal cantidad;
	private BigDecimal totalCantidad;
	private String montoLetra;
	private BigDecimal sumaSubtotal,ivaSubtotal,total;
	
	
	
	
	public AltaNotasCredito() {
		
		listaClientes = new ArrayList<Cliente>();
		listaFactura = new ArrayList<Factura>();
		listaClienteDom = new ArrayList<ClienteDomicilios>();
		listaClienteDomicilio = new ArrayList<ClienteDomicilios>();
		listaNotaXFactura = new ArrayList<NotaPorFactura>();
		listaSerieNota = new ArrayList<SerieNota>();
		
		clienteDAO = new ClienteDAO();
		facturaDAO = new FacturaDAO();
		clienteDomicilioDAO = new ClienteDomiciliosDAO();
		asentamientoHumanoDAO = new AsentamientoHumandoDAO();
		notaCreditoDAO = new notaCreditoDAO();
		serieNotaDAO = new SerieNotaDAO();
		//notaFactDAO = new NotaPorFacturaDAO();
		
		clienteSelect = new Cliente();
		domicilioSelect = new Domicilios();
		facturaSelect = new Factura();
		serieNotaSelect = new SerieNota();
		notaPorFactura = new NotaPorFactura();
		notaCredito = new NotaCredito();
		
		
		
	}
	
	@PostConstruct
	public void init(){
		
		listaClientes = clienteDAO.buscarTodos();
		listaClienteDom = clienteDomicilioDAO.buscarTodos();
		listaSerieNota = serieNotaDAO.findAll();
		
		porCobrar = false;
		pagada = false;
		pagoParcial = false;
		cantidad = new BigDecimal(0).setScale(2);
		totalCantidad = new BigDecimal(0).setScale(2);
		sumaSubtotal = new BigDecimal(0).setScale(2);
		ivaSubtotal = new BigDecimal(0).setScale(2);
		total = new BigDecimal(0).setScale(2);
		
	}
	
	

	public List<Cliente> getListaClientes() {
		return listaClientes;
	}

	public void setListaClientes(List<Cliente> listaClientes) {
		this.listaClientes = listaClientes;
	}

	public Cliente getClienteSelect() {
		return clienteSelect;
	}

	public void setClienteSelect(Cliente clienteSelect) {
		this.clienteSelect = clienteSelect;
	}

	public Boolean getPagoParcial() {
		return pagoParcial;
	}

	public void setPagoParcial(Boolean pagoParcial) {
		this.pagoParcial = pagoParcial;
	}

	public Boolean getPorCobrar() {
		return porCobrar;
	}

	public void setPorCobrar(Boolean porCobrar) {
		this.porCobrar = porCobrar;
	}

	public Boolean getPagada() {
		return pagada;
	}

	public void setPagada(Boolean pagada) {
		this.pagada = pagada;
	}

	public List<Factura> getListaFactura() {
		return listaFactura;
	}

	public void setListaFactura(List<Factura> listaFactura) {
		this.listaFactura = listaFactura;
	}
	
	public Domicilios getDomicilioSelect() {
		return domicilioSelect;
	}

	public void setDomicilioSelect(Domicilios domicilioSelect) {
		this.domicilioSelect = domicilioSelect;
	}
	
	public List<ClienteDomicilios> getListaClienteDomicilio() {
		return listaClienteDomicilio;
	}

	public void setListaClienteDomicilio(List<ClienteDomicilios> listaClienteDomicilio) {
		this.listaClienteDomicilio = listaClienteDomicilio;
	}

	public Factura getFacturaSelect() {
		return facturaSelect;
	}

	public void setFacturaSelect(Factura facturaSelect) {
		this.facturaSelect = facturaSelect;
	}

	public BigDecimal getCantidad() {
		return cantidad;
	}

	public void setCantidad(BigDecimal cantidad) {
		this.cantidad = cantidad;
	}

	public List<NotaPorFactura> getListaNotaXFactura() {
		return listaNotaXFactura;
	}

	public void setListaNotaXFactura(List<NotaPorFactura> listaNotaXFactura) {
		this.listaNotaXFactura = listaNotaXFactura;
	}

	public BigDecimal getTotalCantidad() {
		return totalCantidad;
	}

	public void setTotalCantidad(BigDecimal totalCantidad) {
		this.totalCantidad = totalCantidad;
	}

	public String getMontoLetra() {
		return montoLetra;
	}

	public void setMontoLetra(String montoLetra) {
		this.montoLetra = montoLetra;
	}

	public AsentamientoHumano getAsentamientoCliente() {
		return asentamientoCliente;
	}

	public void setAsentamientoCliente(AsentamientoHumano asentamientoCliente) {
		this.asentamientoCliente = asentamientoCliente;
	}
	
	public NotaCredito getNotaCredito() {
		return notaCredito;
	}

	public void setNotaCredito(NotaCredito notaCredito) {
		this.notaCredito = notaCredito;
	}

	public BigDecimal getSumaSubtotal() {
		return sumaSubtotal;
	}

	public void setSumaSubtotal(BigDecimal sumaSubtotal) {
		this.sumaSubtotal = sumaSubtotal;
	}

	public BigDecimal getIvaSubtotal() {
		return ivaSubtotal;
	}

	public void setIvaSubtotal(BigDecimal ivaSubtotal) {
		this.ivaSubtotal = ivaSubtotal;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public List<SerieNota> getListaSerieNota() {
		return listaSerieNota;
	}

	public void setListaSerieNota(List<SerieNota> listaSerieNota) {
		this.listaSerieNota = listaSerieNota;
	}

	public SerieNota getSerieNotaSelect() {
		return serieNotaSelect;
	}

	public void setSerieNotaSelect(SerieNota serieNotaSelect) {
		this.serieNotaSelect = serieNotaSelect;
	}

	public void filtroFactura() {
		
		String message = null;
		Severity severity = null;
		StatusFactura sf = new StatusFactura();
		listaFactura.clear();
		listaNotaXFactura.clear();
		
		try {
			
			if(porCobrar==true) {
				sf.setId(1);
				listaFactura.addAll(facturaDAO.buscarPorCteStatus(sf, clienteSelect));
				
			}
			
			if(pagada==true) {				
				sf.setId(3);
				listaFactura.addAll(facturaDAO.buscarPorCteStatus(sf, clienteSelect));
			}
			
			if(pagoParcial==true) {
				sf.setId(4);
				listaFactura.addAll(facturaDAO.buscarPorCteStatus(sf, clienteSelect));
			}
			
			if((porCobrar==false)&&(pagada==false)&&(pagoParcial==false)) {			
				message = "Seleccione el tipo de pago.";
				severity = FacesMessage.SEVERITY_INFO;
			}
			
			if((porCobrar==true)||(pagada==true)||(pagoParcial==true)) {			
				message = "Tipo Pago Seleccionado";
				severity = FacesMessage.SEVERITY_INFO;
			}
			
			domicilioCliente(clienteSelect);
			
		}catch (Exception e) {
			message = "Selecciona un cliente";
			severity = FacesMessage.SEVERITY_ERROR;
		}finally {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity,"Cliente", message));
			PrimeFaces.current().ajax().update("form:dt-factura","form:messages","form:domicilio");
		}
		
	}
	
	public void domicilioCliente(Cliente clienteSelect) {
		
		listaClienteDomicilio.clear();
		listaClienteDomicilio = listaClienteDom.stream()
								.filter(cd -> clienteSelect != null
								?(cd.getCteCve().getCteCve().intValue()==clienteSelect.getCteCve().intValue())
								:false)
								.collect(Collectors.toList());
		if(listaClienteDomicilio.size()>0) {
			domicilioSelect = listaClienteDomicilio.get(0).getDomicilios();
		
		}
	}
	
	public void facturasSeleccionadas() {//AQUI VAN LAS SUMAS 
		
		//SUMAS GENERALES
		BigDecimal iva = new BigDecimal(.16).setScale(2,BigDecimal.ROUND_HALF_UP);
		
		sumaSubtotal = sumaSubtotal.add(facturaSelect.getSubtotal()).setScale(2,BigDecimal.ROUND_HALF_UP);
		ivaSubtotal = sumaSubtotal.multiply(iva).setScale(2,BigDecimal.ROUND_HALF_UP);
		total = sumaSubtotal.add(ivaSubtotal);
		
		totalCantidad = totalCantidad.add(cantidad).setScale(2,BigDecimal.ROUND_HALF_UP);
		
		notaPorFactura = new NotaPorFactura();
		notaPorFactura.setFactura(facturaSelect);
		notaPorFactura.setCantidad(cantidad);
		
		listaNotaXFactura.add(notaPorFactura);		
		
		FormatUtil formato = new FormatUtil();
		
		montoLetra = formato.getMontoConLetra(totalCantidad.doubleValue());
		
		cantidad = new BigDecimal(0).setScale(2);
		
		PrimeFaces.current().ajax().update("form:dt-NotasPorFactura","form:cantidad","form:montoLetra");
		
	}
	
	
	
	public void save() {
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		asentamientoCliente = asentamientoHumanoDAO.buscarPorAsentamiento(domicilioSelect.getPaisCved().getPaisCve(), domicilioSelect.getCiudades().getMunicipios().getEstados().getEstadosPK().getEstadoCve(),domicilioSelect.getCiudades().getMunicipios().getMunicipiosPK().getMunicipioCve() , domicilioSelect.getCiudades().getCiudadesPK().getCiudadCve(), domicilioSelect.getDomicilioColonia());
		String domicilio = domicilioSelect.getDomicilioCalle() + " " + domicilioSelect.getDomicilioNumExt() + " " + domicilioSelect.getDomicilioNumInt() + " " + asentamientoCliente.getAsentamientoDs();
		
		StatusNotaCredito statusNotaCredito = new StatusNotaCredito();
		statusNotaCredito.setId(1);
		
		
		try {
			
			serieNotaSelect.setNumeroActual(serieNotaSelect.getNumeroActual()+1);
			serieNotaDAO.update(serieNotaSelect);
			notaCredito.setNumero(String.valueOf(serieNotaSelect.getNumeroActual()));//DUDA
			notaCredito.setIdcliente(clienteSelect.getCteCve());
			notaCredito.setCliente(clienteSelect.getCteNombre());
			notaCredito.setDomicilio(domicilio);
			notaCredito.setRfc(clienteSelect.getCteRfc());
			notaCredito.setSubtotal(sumaSubtotal);
			notaCredito.setIva(ivaSubtotal);
			notaCredito.setTotal(total);
			notaCredito.setTotalLetra(montoLetra);
			notaCredito.setStatus(statusNotaCredito);
			notaCredito.setNotaFacturaList(new ArrayList<NotaPorFactura>());
			notaCredito.getNotaFacturaList().addAll(listaNotaXFactura);
			
			for(NotaPorFactura ntf: listaNotaXFactura) {
				
				NotaPorFacturaPK notaFacturaPK = new NotaPorFacturaPK();
				notaFacturaPK.setNota(notaCredito);
				notaFacturaPK.setFactura(ntf.getFactura());
				
				ntf.setNotaPorFacturaPK(notaFacturaPK);
				ntf.setNota(notaCredito);
				
				//Actualizacion de factura
				StatusFactura statusF = new StatusFactura();
				
				//si cantidad es igual a total de factura
				if((ntf.getCantidad().compareTo(ntf.getFactura().getTotal())==0)) {
					statusF.setId(3);
				}
				
				
				//si cantidad es menor que el total de factura
				if((ntf.getCantidad().compareTo(ntf.getFactura().getTotal())==-1)) {
					statusF.setId(4);
				}
				
				ntf.getFactura().setStatus(statusF);
				//ntf.getFactura().setNotaFacturaList(new ArrayList<>());
				//ntf.getFactura().getNotaFacturaList().add(ntf);
				
				facturaDAO.actualizar(ntf.getFactura());
				
				
			}
			
			notaCreditoDAO.guardar(notaCredito);
			//-----Actualizar Tablas---
			//filtroFactura();
			severity = FacesMessage.SEVERITY_INFO;
			mensaje = "Nota agregada correctamente";
			
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}finally {
			message = new FacesMessage(severity, "Nota de Credito", mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
			PrimeFaces.current().ajax().update(":form:messages");
		}
			
	}
	
	public void reload() throws IOException {
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
		
	}

	
	
	

}
