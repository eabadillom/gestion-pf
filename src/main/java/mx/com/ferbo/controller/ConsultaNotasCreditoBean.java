package mx.com.ferbo.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;

import mx.com.ferbo.dao.ClienteDAO;
import mx.com.ferbo.dao.NotaCreditoDAO;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.NotaCredito;

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
	
	
	
	public ConsultaNotasCreditoBean() {
		
		listaCliente = new ArrayList<Cliente>();
		listaNotaCredito = new ArrayList<NotaCredito>();
		
		clienteDAO = new ClienteDAO();
		notaCreditoDAO = new NotaCreditoDAO();
		notaCreditoSelect = new NotaCredito();
		
		clienteSelect = new Cliente();
		
		
		
	}	
	
	@PostConstruct
	public void init() {
		
		listaCliente = clienteDAO.buscarTodos();
	
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
}
