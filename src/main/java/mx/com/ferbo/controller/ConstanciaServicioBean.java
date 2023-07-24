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
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.PrimeFaces;

import mx.com.ferbo.dao.ClienteDAO;
import mx.com.ferbo.dao.ConstanciaServicioDAO;
import mx.com.ferbo.dao.ControlFacturaConstanciaDAO;
import mx.com.ferbo.dao.EstadoConstanciaDAO;
import mx.com.ferbo.dao.PartidaServicioDAO;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.ConstanciaDeServicio;
import mx.com.ferbo.model.ConstanciaServicioDetalle;
import mx.com.ferbo.model.ControlFacturaConstanciaDS;
import mx.com.ferbo.model.EstadoConstancia;
import mx.com.ferbo.model.PartidaServicio;
import mx.com.ferbo.model.Usuario;
import mx.com.ferbo.util.DateUtil;
import mx.com.ferbo.util.EntityManagerUtil;
import mx.com.ferbo.util.InventarioException;

@Named
@ViewScoped
public class ConstanciaServicioBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private List<Cliente> listaClientes;
	private List<ConstanciaDeServicio> listaConstanciaServicios;
	private List<EstadoConstancia> listaEstadosConstancias;
	
	private ClienteDAO clienteDao;
	private ConstanciaServicioDAO constanciaServicioDao;
	private ControlFacturaConstanciaDAO cfcDAO;
	private EstadoConstanciaDAO ecDAO;
	//TODO agregar DAO para PARTIDA_SERVICIO y CONSTANCIA_SERVICIO_DETALLE.
	
	private Usuario usuario;
	private FacesContext faceContext;
	private HttpServletRequest httpServletRequest;
	
	private int idCliente;
	private Date fechaInicio;
	private Date fechaFinal;
	private String folio;
	private ConstanciaDeServicio seleccion;
	
	public ConstanciaServicioBean() {
		clienteDao = new ClienteDAO();
		constanciaServicioDao = new ConstanciaServicioDAO();
		//psDAO = new PartidaServicioDAO();
		//csdDAO = new ConstanciaServicioDetalleDAO();
		cfcDAO = new ControlFacturaConstanciaDAO();
		ecDAO = new EstadoConstanciaDAO();
		listaClientes = new ArrayList<>();
		listaConstanciaServicios = new ArrayList<>();
	}
	
	@PostConstruct
	public void init() {
		
		faceContext = FacesContext.getCurrentInstance();
		httpServletRequest = (HttpServletRequest) faceContext.getExternalContext().getRequest();
		usuario = (Usuario) httpServletRequest.getSession(false).getAttribute("usuario");
		
		listaClientes = clienteDao.buscarTodos();
		listaEstadosConstancias = ecDAO.buscarTodos();
		idCliente = 0;
		fechaInicio = new Date();
		fechaFinal = new Date();
		folio = "";
	}
	
	public void buscarConstancia() {
		EntityManager em = EntityManagerUtil.getEntityManager();
		EntityTransaction transaction = em.getTransaction();
		transaction.begin();
		constanciaServicioDao.setEntityManager(em);
		
		System.out.println(getIdCliente() + " " + getFechaInicio() + " " + getFechaFinal() + " " + getFolio());
		System.out.println(folio + " " + fechaInicio + " " + fechaFinal + " " + idCliente);
		listaConstanciaServicios = constanciaServicioDao.buscarPorCriterio(folio, fechaInicio, fechaFinal, idCliente);
		for (ConstanciaDeServicio constanciaDeServicio : listaConstanciaServicios) {
			List<PartidaServicio> alPartidas = constanciaDeServicio.getPartidaServicioList();
			List<ConstanciaServicioDetalle> alServicios = constanciaDeServicio.getConstanciaServicioDetalleList();
			EstadoConstancia estado = constanciaDeServicio.getStatus();
			System.out.println(estado);
			System.out.println(alPartidas);
			System.out.println(alServicios);
		}
		transaction.commit();
		em.close();
	}
	
	public void cancelar() {
		PrimeFaces.current().executeScript("PF('dlg-constancia').hide()");
		ConstanciaDeServicio constancia = this.seleccion;
		String message = null;
		Severity severity = null;
		
		EntityManager manager = null;
		
		List<ControlFacturaConstanciaDS> alControlFacturas = null;
		EstadoConstancia stCancelada = null;
		String observaciones = null;
		String sFechaHoy = null;
		Date fechaHoy = null;
		
		try {
			System.out.println("Cancelando constancia...");
			if(constancia.getStatus().getEdoCve() == 4)
				throw new InventarioException("La constancia ya se encuentra cancelada.");
			
			manager = EntityManagerUtil.getEntityManager();
			
			//Validar si la constancia de servicio ya se encuentra facturada.
			alControlFacturas = cfcDAO.buscarPorConstancia(constancia.getFolio());
			
			if(alControlFacturas != null && alControlFacturas.size() > 0) {
				throw new InventarioException("La constancia se encuentra facturada, no es posible cancelar.");
			}
			
			stCancelada = listaEstadosConstancias.stream()
					.filter(estado -> estado.getEdoCve().equals(4))
					.collect(Collectors.toList())
					.get(0);
			
			manager.getTransaction().begin();
			manager.merge(constancia);
			
			List<PartidaServicio> partidas = constancia.getPartidaServicioList();
			
			if(partidas.removeAll(partidas)) {
				constancia.setPartidaServicioList(partidas);
				System.out.println("Partidas eliminadas.");
			}
			
			List<ConstanciaServicioDetalle> servicios = constancia.getConstanciaServicioDetalleList();
			for(int i = 0; i < servicios.size(); i++) {
				servicios.remove(0);
				manager.merge(constancia);
			}
			
			if(servicios.removeAll(servicios)) {
				constancia.setConstanciaServicioDetalleList(servicios);
				System.out.println("Servicios eliminados.");
			}
			
			constancia.setStatus(stCancelada);
			fechaHoy = new Date();
			sFechaHoy = DateUtil.getString(fechaHoy, DateUtil.FORMATO_DD_MM_YYYY);
			observaciones = constancia.getObservaciones();
			observaciones = String.format("CANCELADA EL %S: %S", sFechaHoy, observaciones);
			constancia.setObservaciones(observaciones);
			
			manager.merge(constancia);
			
			manager.getTransaction().commit();
			message = "Constancia cancelada correctamente.";
			severity = FacesMessage.SEVERITY_INFO;
			
			System.out.println("Constancia cancelada.");
		} catch(InventarioException ex) {
			manager.getTransaction().rollback();
			ex.printStackTrace();
			message = ex.getMessage();
			severity = FacesMessage.SEVERITY_ERROR;
		} catch(Exception ex) {
			manager.getTransaction().rollback();
			ex.printStackTrace();
			message = "Problema con la actualización de la constancia.";
			severity = FacesMessage.SEVERITY_ERROR;
		} finally {
			manager.close();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, "Cancelación", message));
			PrimeFaces.current().ajax().update("form:messages", "form:dt-constanciaServicios");
		}
	}
	
	public List<Cliente> getListaClientes() {
		return listaClientes;
	}

	public void setListaClientes(List<Cliente> listaClientes) {
		this.listaClientes = listaClientes;
	}

	public List<ConstanciaDeServicio> getListaConstanciaServicios() {
		return listaConstanciaServicios;
	}

	public void setListaConstanciaServicios(List<ConstanciaDeServicio> listaConstanciaServicios) {
		this.listaConstanciaServicios = listaConstanciaServicios;
	}

	public ClienteDAO getClienteDao() {
		return clienteDao;
	}

	public void setClienteDao(ClienteDAO clienteDao) {
		this.clienteDao = clienteDao;
	}

	public ConstanciaServicioDAO getConstanciaServicioDao() {
		return constanciaServicioDao;
	}

	public void setConstanciaServicioDao(ConstanciaServicioDAO constanciaServicioDao) {
		this.constanciaServicioDao = constanciaServicioDao;
	}

	public int getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(int idCliente) {
		this.idCliente = idCliente;
	}

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Date getFechaFinal() {
		return fechaFinal;
	}

	public void setFechaFinal(Date fechaFinal) {
		this.fechaFinal = fechaFinal;
	}

	public String getFolio() {
		return folio;
	}

	public void setFolio(String folioCliente) {
		this.folio = folioCliente;
	}

	public ConstanciaDeServicio getSeleccion() {
		return seleccion;
	}

	public void setSeleccion(ConstanciaDeServicio seleccion) {
		this.seleccion = seleccion;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}
