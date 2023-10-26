package mx.com.ferbo.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;

import mx.com.ferbo.dao.ClienteDAO;
import mx.com.ferbo.dao.FacturaDAO;
import mx.com.ferbo.dao.NotaCreditoDAO;
import mx.com.ferbo.dao.PagoDAO;
import mx.com.ferbo.dao.StatusNotaCreditoDAO;
import mx.com.ferbo.model.CancelaNotaCredito;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.Factura;
import mx.com.ferbo.model.NotaCredito;
import mx.com.ferbo.model.NotaPorFactura;
import mx.com.ferbo.model.Pago;
import mx.com.ferbo.model.StatusNotaCredito;
import mx.com.ferbo.model.TipoPago;
import mx.com.ferbo.util.InventarioException;

@Named
@ViewScoped

public class ConsultaNotasCreditoBean implements Serializable{
	
	private static final long serialVersionUID = -626048119540963939L;
	private static Logger log = LogManager.getLogger(ConsultaNotasCreditoBean.class);
	
	private List<Cliente> listaCliente;
	private List<NotaCredito> listaNotaCredito;
	
	private ClienteDAO clienteDAO;
	private NotaCreditoDAO notaCreditoDAO;
	
	private Cliente clienteSelect;
	private NotaCredito notaCreditoSelect;
	
	private Date fechaInicio;
	private Date fechaFin;
	
	private StatusNotaCreditoDAO statusNotaCreditoDAO;
	private StatusNotaCredito statusCancelada;
	private FacturaDAO facturaDAO;
	private PagoDAO pagoDAO;
	private String motivoCancelacion;
	
	
	
	public ConsultaNotasCreditoBean() {
		
		listaCliente = new ArrayList<Cliente>();
		listaNotaCredito = new ArrayList<NotaCredito>();
		
		clienteDAO = new ClienteDAO();
		notaCreditoDAO = new NotaCreditoDAO();
		notaCreditoSelect = new NotaCredito();
		statusNotaCreditoDAO = new StatusNotaCreditoDAO();
		facturaDAO = new FacturaDAO();
		pagoDAO = new PagoDAO();
		
		clienteSelect = new Cliente();
		
		
		
	}	
	
	@PostConstruct
	public void init() {
		
		listaCliente = clienteDAO.buscarTodos();
		statusCancelada = statusNotaCreditoDAO.buscarPorId(StatusNotaCredito.STATUS_NOTA_CREDITO_CANCELADA);
	
		fechaInicio = new Date();
		fechaFin = new Date();
		
	}
	
	public void consultarNotaCreditoCte() {
		Integer idCliente = null;
		
		if(this.clienteSelect == null)
			idCliente = null;
		
		else if(clienteSelect.getCteCve() == null)
			idCliente = null;
		else
			idCliente = clienteSelect.getCteCve();
		
		log.debug("Fecha inicio: {}",fechaInicio);
		log.debug("Fecha Fin: {}",fechaFin);
		
		listaNotaCredito = notaCreditoDAO.buscarPor(fechaInicio, fechaFin, idCliente);
		log.debug("ListanotaCredito.size(): {}", listaNotaCredito.size());
		
		PrimeFaces.current().ajax().update("form:dt-notaCredito");
	}
	
	public void cargaInfoNota(NotaCredito nota) {
		log.info("Nota de credito: {}", this.notaCreditoSelect.getId());
		this.notaCreditoSelect = notaCreditoDAO.buscarPor(nota.getId(), true);
	}
	
	public void actualizar() {
		notaCreditoDAO.actualizar(notaCreditoSelect);
		this.consultarNotaCreditoCte();
	}
	
	public void cancelar() {
		String message = null;
		Severity severity = null;
		String titulo = "Cancelar nota de crédito...";
		
		List<NotaPorFactura> npfList = null;
		CancelaNotaCredito cancela = null;
		String resultado = null;
		
		try {
			log.debug("Cancelar nota de crédito: {}", this.notaCreditoSelect);
			this.notaCreditoSelect = notaCreditoDAO.buscarPor(this.notaCreditoSelect.getId(), true);
			
			if(this.notaCreditoSelect.getStatus().getId().equals(statusCancelada.getId()))
				throw new InventarioException("La nota de crédito ya está cancelada.");
			
			this.notaCreditoSelect.setStatus(statusCancelada);
			npfList = this.notaCreditoSelect.getNotaFacturaList();
			
			for(NotaPorFactura npf : npfList) {
				Factura factura = npf.getNotaPorFacturaPK().getFactura();
				factura = facturaDAO.buscarPorId(factura.getId(), true);
				this.eliminaPago(this.notaCreditoSelect, factura.getPagoList());
			}
			
			cancela = new CancelaNotaCredito();
			cancela.setNota(notaCreditoSelect);
			cancela.setDescripcion(motivoCancelacion);
			this.notaCreditoSelect.getCancelaNotaCreditoList().add(cancela);
			
			resultado = notaCreditoDAO.actualizar(notaCreditoSelect);
			log.debug("Resultado de la actualización de la nota de credito: {}", resultado);
			
			this.motivoCancelacion = null;
			
			message = "La nota de crédito se canceló correctamente.";
			severity = FacesMessage.SEVERITY_INFO;
			
		} catch (InventarioException ex) {
			message = ex.getMessage();
			severity = FacesMessage.SEVERITY_WARN;
		} catch (Exception e) {
			message = "Ocurrió un problema para cancelar la nota de crédito.";
			severity = FacesMessage.SEVERITY_ERROR;
		}finally {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity,titulo, message));
		}
	}
	
	private void eliminaPago(NotaCredito notaCredito, List<Pago> pagoList)
	throws InventarioException {
		final String referencia = String.format("Nota Credito No %s", notaCredito.getNumero());
		List<Pago> tmpPagoList = pagoList.stream()
				.filter(p -> p.getReferencia().equals(referencia) && p.getTipo().getId() == TipoPago.TIPO_PAGO_NOTA_CREDITO)
				.collect(Collectors.toList())
				;
		
		Pago pago = null;
		
		if(tmpPagoList.size() > 0)
			pago = tmpPagoList.get(0);
		else
			return;
		
		String resultado = pagoDAO.eliminar(pago);
		
		if(resultado != null && "".equalsIgnoreCase(resultado.trim()) == false)
			throw new InventarioException(resultado);
	}

	public List<Cliente> getListaCliente() {
		return listaCliente;
	}
	public void setListaCliente(List<Cliente> listaCliente) {
		this.listaCliente = listaCliente;
	}
	public Cliente getClienteSelect() {
		return clienteSelect;
	}
	public void setClienteSelect(Cliente clienteSelect) {
		this.clienteSelect = clienteSelect;
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
	
	public List<NotaCredito> getListaNotaCredito() {
		return listaNotaCredito;
	}

	public void setListaNotaCredito(List<NotaCredito> listaNotaCredito) {
		this.listaNotaCredito = listaNotaCredito;
	}

	public NotaCredito getNotaCreditoSelect() {
		return notaCreditoSelect;
	}

	public void setNotaCreditoSelect(NotaCredito notaCreditoSelect) {
		this.notaCreditoSelect = notaCreditoSelect;
	}

	public String getMotivoCancelacion() {
		return motivoCancelacion;
	}

	public void setMotivoCancelacion(String motivoCancelacion) {
		this.motivoCancelacion = motivoCancelacion;
	}
}
