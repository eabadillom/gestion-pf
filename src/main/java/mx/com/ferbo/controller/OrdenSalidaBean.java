package mx.com.ferbo.controller;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;


import mx.com.ferbo.dao.ClienteDAO;
import mx.com.ferbo.dao.OrdenSalidaDAO;
import mx.com.ferbo.dao.PrecioServicioDAO;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.ConstanciaDeServicio;
import mx.com.ferbo.model.ConstanciaServicioDetalle;
import mx.com.ferbo.model.OrdenSalida;
import mx.com.ferbo.model.PartidaServicio;
import mx.com.ferbo.model.PrecioServicio;
import mx.com.ferbo.model.ProductoPorCliente;
import mx.com.ferbo.util.EntityManagerUtil;
import mx.com.ferbo.util.InventarioException;


@Named
@ViewScoped
public class OrdenSalidaBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private static Logger log = LogManager.getLogger(OrdenSalidaBean.class);
	
	private ClienteDAO clienteDAO;
	private OrdenSalidaDAO ordenSalidaDAO;
	private PrecioServicioDAO precioServicioDAO;
	private List<Cliente> listaClientes;
	private List<OrdenSalida> listaOrdenSalida;
	private List<PrecioServicio> listaServicios;
	private List<ConstanciaServicioDetalle> alServiciosDetalle;

	
	private boolean confirmacion;
	
	private Cliente clienteSelect;
	private OrdenSalida ordensalida;
	private ConstanciaServicioDetalle selServicio;

	
	private Date fecha;
	private String folio;
	private String status;
	private Integer idServicio;
	private BigDecimal cantidadServicio;

	
	public OrdenSalidaBean() {
		clienteDAO = new ClienteDAO();
		ordenSalidaDAO = new OrdenSalidaDAO();
		precioServicioDAO = new PrecioServicioDAO();
		
		listaOrdenSalida = new ArrayList<OrdenSalida>();
		listaClientes = new ArrayList<Cliente>();
		alServiciosDetalle = new ArrayList<ConstanciaServicioDetalle>();
		listaServicios = new ArrayList<PrecioServicio>();
	}
	@PostConstruct
	public void init() {
		listaClientes = clienteDAO.buscarHabilitados(true);
		//listaOrdenSalida = ordenSalidaDAO.buscarTodos();
		fecha= new Date();
		
	}
	
	public void guardar() {
		
	}
	
	public void buscarFoliosporFecha() {
		EntityManager em = EntityManagerUtil.getEntityManager();
		EntityTransaction transaction = em.getTransaction();
		transaction.begin();
		ordenSalidaDAO.setEntityManager(em);
		listaOrdenSalida = ordenSalidaDAO.buscarFolioPorCliente(clienteSelect, fecha);
		
		transaction.commit();
		em.close();
	}
	public void reload() throws IOException {
	    ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
	    ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
	}
	
	public void filtrarCliente() {
		String message = null;
		Severity severity = null;
		EntityManager manager = null;

		try {
			log.info("Entrando a filtrar cliente...");
			
			manager = EntityManagerUtil.getEntityManager();
			
			listaServicios = precioServicioDAO.buscarPorCliente(clienteSelect.getCteCve(), true);
			listaOrdenSalida = ordenSalidaDAO.buscarFolioPorCliente(clienteSelect, fecha);
			
			message = "Seleccione el folio.";
			severity = FacesMessage.SEVERITY_INFO;
		} catch (Exception ex) {
			log.error("Problema para recuperar los datos del cliente.", ex);
			message = ex.getMessage();
			severity = FacesMessage.SEVERITY_ERROR;
		} finally {
			if (manager != null)
				manager.close();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, "Cliente", message));
			PrimeFaces.current().ajax().update("form:messages", "form:selServicio", "form:folio-som");
		}
		log.info("Informacion exitosamente filtrada.");
	}
	
	public void agregaServicios() {
		String message = null;
		PrecioServicio precioServicio = null;
		Severity severity = null;
		ConstanciaServicioDetalle servicio = null;

		try {
			if(this.idServicio == null) 
					throw new InventarioException("Selecione almenos un servicio");
			if (this.clienteSelect == null )
				throw new InventarioException("Debe seleccionar el cliente");
			if (this.cantidadServicio == null || this.cantidadServicio.compareTo(BigDecimal.ZERO) <= 0)
				throw new InventarioException("Debe indicar la cantidad de servicios.");
			
			precioServicio = this.listaServicios.stream().filter(ps -> this.idServicio.equals(ps.getId()))
					.collect(Collectors.toList()).get(0);
			
			if (alServiciosDetalle == null)
				alServiciosDetalle = new ArrayList<ConstanciaServicioDetalle>();
			
			servicio = new ConstanciaServicioDetalle();
			servicio.setServicioCantidad(this.cantidadServicio);
			servicio.setServicioCve(precioServicio.getServicio());
			alServiciosDetalle.add(servicio);
			message = "Producto agregado correctamente.";
			severity = FacesMessage.SEVERITY_INFO;
		} catch (Exception e) {
			log.error("Problema para obtener el listado de servicios del cliente.", e);
			message = "Problema con la información de servicios.";
			severity = FacesMessage.SEVERITY_ERROR;
			
		}finally {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, "Agregar servicio", message));
			PrimeFaces.current().ajax().update("form:messages", "form:dt-dt-servicios");
	
		}
	}
	public Cliente getClienteSelect() {
		return clienteSelect;
	}
	public void setClienteSelect(Cliente clienteSelect) {
		this.clienteSelect = clienteSelect;
	}
	public List<Cliente> getListaClientes() {
		return listaClientes;
	}
	public void setListaClientes(List<Cliente> listaClientes) {
		this.listaClientes = listaClientes;
	}
	public OrdenSalida getOrdensalida() {
		return ordensalida;
	}
	public void setOrdensalida(OrdenSalida ordensalida) {
		this.ordensalida = ordensalida;
	}
	public List<OrdenSalida> getListaOrdenSalida() {
		return listaOrdenSalida;
	}
	public void setListaOrdenSalida(List<OrdenSalida> listaOrdenSalida) {
		this.listaOrdenSalida = listaOrdenSalida;
	}
	public boolean isConfirmacion() {
		return confirmacion;
	}
	public void setConfirmacion(boolean confirmacion) {
		this.confirmacion = confirmacion;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public List<PrecioServicio> getListaServicios() {
		return listaServicios;
	}
	public void setListaServicios(List<PrecioServicio> listaServicios) {
		this.listaServicios = listaServicios;
	}
	public ConstanciaServicioDetalle getSelServicio() {
		return selServicio;
	}
	public void setSelServicio(ConstanciaServicioDetalle selServicio) {
		this.selServicio = selServicio;
	}
	public Integer getIdServicio() {
		return idServicio;
	}
	public void setIdServicio(Integer idServicio) {
		this.idServicio = idServicio;
	}
	public List<ConstanciaServicioDetalle> getAlServiciosDetalle() {
		return alServiciosDetalle;
	}
	public void setAlServiciosDetalle(List<ConstanciaServicioDetalle> alServiciosDetalle) {
		this.alServiciosDetalle = alServiciosDetalle;
	}
	public BigDecimal getCantidadServicio() {
		return cantidadServicio;
	}
	public void setCantidadServicio(BigDecimal cantidadServicio) {
		this.cantidadServicio = cantidadServicio;
	}
	
}
