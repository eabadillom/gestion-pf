package mx.com.ferbo.controller;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;

import mx.com.ferbo.dao.AsentamientoHumanoDAO;
import mx.com.ferbo.dao.ClienteDAO;
import mx.com.ferbo.dao.ClienteDomiciliosDAO;
import mx.com.ferbo.dao.FacturaDAO;
import mx.com.ferbo.dao.NotaCreditoDAO;
import mx.com.ferbo.dao.PagoDAO;
import mx.com.ferbo.dao.ParametroDAO;
import mx.com.ferbo.dao.SerieNotaDAO;
import mx.com.ferbo.dao.StatusFacturaDAO;
import mx.com.ferbo.dao.StatusNotaCreditoDAO;
import mx.com.ferbo.dao.TipoPagoDAO;
import mx.com.ferbo.model.AsentamientoHumano;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.ClienteDomicilios;
import mx.com.ferbo.model.Domicilios;
import mx.com.ferbo.model.Factura;
import mx.com.ferbo.model.NotaCredito;
import mx.com.ferbo.model.NotaPorFactura;
import mx.com.ferbo.model.NotaPorFacturaPK;
import mx.com.ferbo.model.Pago;
import mx.com.ferbo.model.Parametro;
import mx.com.ferbo.model.SerieNota;
import mx.com.ferbo.model.StatusFactura;
import mx.com.ferbo.model.StatusNotaCredito;
import mx.com.ferbo.model.TipoPago;
import mx.com.ferbo.model.Usuario;
import mx.com.ferbo.util.FormatUtil;
import mx.com.ferbo.util.InventarioException;

@Named
@ViewScoped

public class AltaNotasCredito implements Serializable{
	
	private static final long serialVersionUID = -626048119540963939L;
	private static Logger log = LogManager.getLogger(AltaNotasCredito.class);
	
	private List<Cliente> listaClientes;
	private List<Factura> listaFactura;
	private List<ClienteDomicilios> listaClienteDom;
	private List<ClienteDomicilios> listaClienteDomicilio;
	private List<NotaPorFactura> listaNotaXFactura;
	private List<SerieNota> listaSerieNota;
	
	private ClienteDAO clienteDAO;
	private FacturaDAO facturaDAO;
	private ClienteDomiciliosDAO clienteDomicilioDAO;
	private AsentamientoHumanoDAO asentamientoHumanoDAO;
	private NotaCreditoDAO notaCreditoDAO;
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
	private BigDecimal sumaSubtotal, ivaSubtotal, total;
	private TipoPago tipoPagoNotaCredito;
	private TipoPagoDAO tipoPagoDAO;
	private StatusFactura statusFacturaPagada;
	private StatusFactura statusFacturaPagoParcial;
	private StatusFacturaDAO statusFacturaDAO;
	private StatusNotaCredito statusNotaNueva;
	private StatusNotaCreditoDAO statusNotaCreditoDAO;
	private PagoDAO pagoDAO;
	private BigDecimal saldoSelected;
	
	private Usuario usuario;
	private FacesContext faceContext;
	private HttpServletRequest httpServletRequest;
	
	private Date fechaInicio;
	private Date fechaFin;
	private Parametro pIVA = null;
	private ParametroDAO parametroDAO = null;
	
	
	public AltaNotasCredito() {
		
		faceContext = FacesContext.getCurrentInstance();
		httpServletRequest = (HttpServletRequest) faceContext.getExternalContext().getRequest();
		usuario = (Usuario) httpServletRequest.getSession(false).getAttribute("usuario");
		
		listaClientes = new ArrayList<Cliente>();
		listaFactura = new ArrayList<Factura>();
		listaClienteDom = new ArrayList<ClienteDomicilios>();
		listaClienteDomicilio = new ArrayList<ClienteDomicilios>();
		listaNotaXFactura = new ArrayList<NotaPorFactura>();
		listaSerieNota = new ArrayList<SerieNota>();
		
		clienteSelect = new Cliente();
		domicilioSelect = new Domicilios();
		facturaSelect = new Factura();
		serieNotaSelect = new SerieNota();
		notaPorFactura = new NotaPorFactura();
		notaCredito = new NotaCredito();
		parametroDAO = new ParametroDAO();
	}
	
	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init(){
		this.clienteDAO = new ClienteDAO();
		this.facturaDAO = new FacturaDAO();
		this.clienteDomicilioDAO = new ClienteDomiciliosDAO();
		this.asentamientoHumanoDAO = new AsentamientoHumanoDAO();
		this.notaCreditoDAO = new NotaCreditoDAO();
		this.serieNotaDAO = new SerieNotaDAO();
		this.tipoPagoDAO = new TipoPagoDAO();
		this.statusFacturaDAO = new StatusFacturaDAO();
		this.statusNotaCreditoDAO = new StatusNotaCreditoDAO();
		this.pagoDAO = new PagoDAO();
		
		this.listaClientes = (List<Cliente>) httpServletRequest.getSession(false).getAttribute("clientesActivosList");
		//TODO timbrado CFDI para las notas de crédito. Se prepara parámetro para indicar planta (razón social) a la que pertenece la serie.
		this.listaSerieNota = serieNotaDAO.buscarActivas(null);
		this.tipoPagoNotaCredito = tipoPagoDAO.buscarPorId(TipoPago.TIPO_PAGO_NOTA_CREDITO);
		
		
		this.porCobrar = false;
		this.pagada = false;
		this.pagoParcial = false;
		this.cantidad = new BigDecimal(0).setScale(2);
		this.totalCantidad = new BigDecimal(0).setScale(2);
		this.sumaSubtotal = new BigDecimal(0).setScale(2);
		this.ivaSubtotal = new BigDecimal(0).setScale(2);
		this.total = new BigDecimal(0).setScale(2);
		this.notaCredito.setFecha(new Date());
		this.statusFacturaPagada = statusFacturaDAO.buscarPorId(StatusFactura.STATUS_PAGADA);
		this.statusFacturaPagoParcial = statusFacturaDAO.buscarPorId(StatusFactura.STATUS_PAGO_PARCIAL);
		this.statusNotaNueva = statusNotaCreditoDAO.buscarPorId(StatusNotaCredito.STATUS_NOTA_CREDITO_NUEVA);
		String cajero = String.format("%s %s %s",
				this.usuario.getNombre() == null ? "" : this.usuario.getNombre(),
				this.usuario.getApellido1() == null ? "" : this.usuario.getApellido1(),
				this.usuario.getApellido2() == null ? "" : this.usuario.getApellido2()
		);
		this.notaCredito.setCajero(cajero);
		
		this.fechaInicio = new Date();
		this.fechaFin = new Date();
		this.pIVA = parametroDAO.buscarPorNombre("IVA");
	}
	
	public void filtroFactura() {
		
		String message = null;
		Severity severity = null;
		StatusFactura sf = new StatusFactura();
		this.listaFactura.clear();
		this.listaNotaXFactura.clear();
		
		try {
			
			if(porCobrar==true) {
				sf.setId(1);
				listaFactura.addAll(facturaDAO.buscarPorCteStatusClientePeriodo(sf, clienteSelect, fechaInicio, fechaFin));
			}
			
			if(pagada==true) {				
				sf.setId(3);
				listaFactura.addAll(facturaDAO.buscarPorCteStatusClientePeriodo(sf, clienteSelect, fechaInicio, fechaFin));
			}
			
			if(pagoParcial==true) {
				sf.setId(4);
				listaFactura.addAll(facturaDAO.buscarPorCteStatusClientePeriodo(sf, clienteSelect, fechaInicio, fechaFin));
			}
			
			if(clienteSelect == null) {
				throw new InventarioException("Seleccione un cliente.");
			}
			
			if(clienteSelect.getCteCve() == null) {
				throw new InventarioException("Seleccione un cliente.");
			}
			
			domicilioCliente(clienteSelect);
			
			if((porCobrar==false)&&(pagada==false)&&(pagoParcial==false)) {			
				throw new InventarioException("Seleccione el status de sus facturas.");
			}
			
			message = "Agregue una factura a su nota de crédito.";
			severity = FacesMessage.SEVERITY_INFO;
			
		} catch (InventarioException ex) {
			message = ex.getMessage();
			severity = FacesMessage.SEVERITY_WARN;
		} catch (Exception e) {
			message = "Ocurrió un problema en la consulta de notas de crédito.";
			severity = FacesMessage.SEVERITY_ERROR;
		}finally {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity,"Cliente", message));
			PrimeFaces.current().ajax().update("form:dt-factura","form:messages","form:domicilio");
		}
		
	}
	
	public void domicilioCliente(Cliente clienteSelect) {
		
		listaClienteDom = clienteDomicilioDAO.buscarDomicilioFiscalPorCliente(clienteSelect.getCteCve(), true);
		
		listaClienteDomicilio.clear();
		listaClienteDomicilio = listaClienteDom.stream()
			.filter(cd -> clienteSelect != null ? (cd.getCteCve().getCteCve().intValue()==clienteSelect.getCteCve().intValue()) :false)
			.collect(Collectors.toList());
		if(listaClienteDomicilio.size() > 0) {
			this.domicilioSelect = listaClienteDomicilio.get(0).getDomicilios();
			log.debug("Domicilio Selected: {}",this.domicilioSelect);
		}
	}
	
	public void agregaFactura() {
		log.debug("Factura seleccionada: {}", this.facturaSelect);
		
		if(facturaSelect == null)
			return;
		
		if(facturaSelect.getId() == null)
			return;
		
		facturaSelect = facturaDAO.buscarPorId(facturaSelect.getId(), true);
		
		saldoSelected = facturaSelect.getTotal();
		log.debug("Total factura: {}", facturaSelect.getTotal());
		for(Pago p : facturaSelect.getPagoList()) {
			saldoSelected = saldoSelected.subtract(p.getMonto());
		}
		log.debug("Saldo: {}", saldoSelected);
	}
	
	public void facturasSeleccionadas() { 
		
		BigDecimal iva = new BigDecimal(pIVA.getValor()).setScale(2,BigDecimal.ROUND_HALF_UP);
		
		log.info("Nota de credito - Subtotal: {}, IVA: {}, Total: {}", sumaSubtotal, ivaSubtotal, total);
		
		notaPorFactura = new NotaPorFactura();
		notaPorFactura.setNotaPorFacturaPK(new NotaPorFacturaPK());
		notaPorFactura.getNotaPorFacturaPK().setFactura(facturaSelect);
		notaPorFactura.setCantidad(cantidad);
		
		listaNotaXFactura.add(notaPorFactura);
		
		totalCantidad = new BigDecimal("0.00").setScale(2, BigDecimal.ROUND_HALF_UP);
		for(NotaPorFactura npf : this.listaNotaXFactura) {
			log.debug("NotaXFactura - Cantidad: {}", npf.getCantidad());
			totalCantidad = totalCantidad.add(npf.getCantidad());
		}
		
		sumaSubtotal = totalCantidad.divide(new BigDecimal("1.00").setScale(2, BigDecimal.ROUND_HALF_UP).add(iva), BigDecimal.ROUND_HALF_UP);
		ivaSubtotal = sumaSubtotal.multiply(iva);
		
		FormatUtil formato = new FormatUtil();
		
		montoLetra = formato.getMontoConLetra(totalCantidad.doubleValue());
		
		cantidad = new BigDecimal(0).setScale(2);
		
		PrimeFaces.current().ajax().update("form:dt-NotasPorFactura","form:cantidad","form:montoLetra");
		
	}
	
	public void save() {
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		
		StatusFactura statusF = null;
		String domicilio = null;
		
		try {
			domicilioCliente(clienteSelect);
			
			asentamientoCliente = asentamientoHumanoDAO.buscarPorAsentamiento(
					domicilioSelect.getAsentamiento().getAsentamientoHumanoPK().getCiudades().getMunicipios().getEstados().getPaises().getPaisCve(),
					domicilioSelect.getAsentamiento().getAsentamientoHumanoPK().getCiudades().getMunicipios().getEstados().getEstadosPK().getEstadoCve(),
					domicilioSelect.getAsentamiento().getAsentamientoHumanoPK().getCiudades().getMunicipios().getMunicipiosPK().getMunicipioCve() ,
					domicilioSelect.getAsentamiento().getAsentamientoHumanoPK().getCiudades().getCiudadesPK().getCiudadCve(),
					domicilioSelect.getAsentamiento().getAsentamientoHumanoPK().getAsentamientoCve()
				);
			domicilio = domicilioSelect.getDomicilioCalle() + " " + domicilioSelect.getDomicilioNumExt() + " " + domicilioSelect.getDomicilioNumInt() + " " + asentamientoCliente.getAsentamientoDs();
			
			
			notaCredito.setNumero(String.valueOf(serieNotaSelect.getNumeroActual() + 1));
			notaCredito.setIdcliente(clienteSelect.getCteCve());
			notaCredito.setCliente(clienteSelect.getNombre());
			notaCredito.setDomicilio(domicilio);
			notaCredito.setRfc(clienteSelect.getCteRfc());
			notaCredito.setSubtotal(sumaSubtotal);
			notaCredito.setIva(ivaSubtotal);
			notaCredito.setTotal(totalCantidad);
			notaCredito.setTotalLetra(montoLetra);
			notaCredito.setStatus(this.statusNotaNueva);
			notaCredito.setNotaFacturaList(new ArrayList<NotaPorFactura>());
			notaCredito.getNotaFacturaList().addAll(listaNotaXFactura);
			
			for(NotaPorFactura ntf: listaNotaXFactura) {
				
				Factura factura = ntf.getNotaPorFacturaPK().getFactura();
				Pago pago = new Pago();
				
				
				NotaPorFacturaPK notaFacturaPK = new NotaPorFacturaPK();
				notaFacturaPK.setNota(notaCredito);
				notaFacturaPK.setFactura(factura);
				
				pago.setFactura(factura);
				pago.setTipo(tipoPagoNotaCredito);
				pago.setMonto(ntf.getCantidad());
				pago.setFecha(notaCredito.getFecha());
				pago.setReferencia(String.format("Nota Credito No %s", notaCredito.getNumero()));
				pago.setCheque("");
				pago.setChequeDevuelto(false);
				
				factura.getPagoList().add(pago);
				
				ntf.setNotaPorFacturaPK(notaFacturaPK);
//				ntf. setNota(notaCredito);
				
				if(factura.getNotaFacturaList() == null)
					factura.setNotaFacturaList(new ArrayList<NotaPorFactura>());
				
				factura.getNotaFacturaList().add(ntf);
				
				//Actualizacion de factura
				
				//si cantidad es igual a total de factura
				if((ntf.getCantidad().compareTo(ntf.getNotaPorFacturaPK().getFactura().getTotal())==0)) {
					statusF = this.statusFacturaPagada;
				}
				
				//si cantidad es menor que el total de factura
				if((ntf.getCantidad().compareTo(ntf.getNotaPorFacturaPK().getFactura().getTotal())==-1)) {
					statusF = this.statusFacturaPagoParcial;
				}
				
				ntf.getNotaPorFacturaPK().getFactura().setStatus(statusF);
			}
			
			notaCreditoDAO.guardar(notaCredito);
			
			for(NotaPorFactura ntf: listaNotaXFactura) {
				int index = ntf.getNotaPorFacturaPK().getFactura().getPagoList().size();
				pagoDAO.guardar(ntf.getNotaPorFacturaPK().getFactura().getPagoList().get(index - 1));
			}
			
			for(NotaPorFactura ntf: listaNotaXFactura) {
				
				Factura factura = facturaDAO.buscarPorId(ntf.getNotaPorFacturaPK().getFactura().getId(), false);
				List<Pago> pagosList = pagoDAO.buscarPorFactura(factura.getId());
				
				BigDecimal saldo = factura.getTotal();
				
				for(Pago pago : pagosList) {
					saldo = saldo.subtract(pago.getMonto());
				}
				
				if(saldo.compareTo(BigDecimal.ZERO) > 0)
					factura.setStatus(statusFacturaPagoParcial);
				else
					factura.setStatus(statusFacturaPagada);
				
				facturaDAO.actualizaStatus(factura);
			}
			
			serieNotaSelect.setNumeroActual(serieNotaSelect.getNumeroActual()+1);
			serieNotaDAO.update(serieNotaSelect);
			
			
			severity = FacesMessage.SEVERITY_INFO;
			mensaje = "Nota agregada correctamente";
			
			
		} catch(Exception e) {
			log.error("Ocurrió un problema al guardar la nota de crédito...", e);
			mensaje = "Ocurrió un problema al guardar la nota de crédito.";
			severity = FacesMessage.SEVERITY_ERROR;
		} finally {
			message = new FacesMessage(severity, "Nota de Credito", mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
			PrimeFaces.current().ajax().update(":form:messages");
		}
			
	}
	
	public void reload() throws IOException {
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
		
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

	public BigDecimal getSaldoSelected() {
		return saldoSelected;
	}

	public void setSaldoSelected(BigDecimal saldoSelected) {
		this.saldoSelected = saldoSelected;
	}

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Date getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}
}
