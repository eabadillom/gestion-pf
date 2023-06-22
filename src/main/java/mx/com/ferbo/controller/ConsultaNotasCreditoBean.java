package mx.com.ferbo.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.primefaces.PrimeFaces;

import mx.com.ferbo.dao.ClienteDAO;
import mx.com.ferbo.dao.notaCreditoDAO;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.NotaCredito;
import mx.com.ferbo.util.EntityManagerUtil;

@Named
@ViewScoped

public class ConsultaNotasCreditoBean implements Serializable{
	
	private static final long serialVersionUID = -626048119540963939L;
	
	private List<Cliente> listaCliente;
	private List<NotaCredito> listaNotaCredito;
	
	private ClienteDAO clienteDAO;
	private notaCreditoDAO notaCreditoDAO;
	
	private Cliente clienteSelect;
	private NotaCredito notaCreditoSelect;
	
	private Date fechaInicio;
	private Date fechaFin;
	
	
	
	public ConsultaNotasCreditoBean() {
		
		listaCliente = new ArrayList<Cliente>();
		listaNotaCredito = new ArrayList<NotaCredito>();
		
		clienteDAO = new ClienteDAO();
		notaCreditoDAO = new notaCreditoDAO();
		notaCreditoSelect = new NotaCredito();
		
		clienteSelect = new Cliente();
		
		
		
	}	
	
	@PostConstruct
	public void init() {
		
		listaCliente = clienteDAO.buscarTodos();
	
		fechaInicio = new Date();
		fechaFin = new Date();
		
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

	public void consultarNotaCreditoCte() {
		
		EntityManager em = EntityManagerUtil.getEntityManager();
		em.getTransaction().begin();
		notaCreditoDAO.setEm(em);		
		
		listaNotaCredito = notaCreditoDAO.buscarPorCriterios(fechaInicio, fechaFin, clienteSelect.getCteCve().intValue());
		
		for(NotaCredito nc: listaNotaCredito) {
			
			System.out.println(nc.getNotaFacturaList().size());
			
		}
		
		em.getTransaction().commit();
		em.close();
		
		PrimeFaces.current().ajax().update("form:dt-notaCredito");
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
