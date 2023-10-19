package mx.com.ferbo.controller;

import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;

import mx.com.ferbo.dao.ClienteDAO;
import mx.com.ferbo.dao.ConstanciaServicioDAO;
import mx.com.ferbo.dao.ControlFacturaConstanciaDAO;
import mx.com.ferbo.dao.EstadoConstanciaDAO;
import mx.com.ferbo.dao.PrecioServicioDAO;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.ConstanciaDeServicio;
import mx.com.ferbo.model.ConstanciaServicioDetalle;
import mx.com.ferbo.model.ControlFacturaConstanciaDS;
import mx.com.ferbo.model.EstadoConstancia;
import mx.com.ferbo.model.PartidaServicio;
import mx.com.ferbo.model.PrecioServicio;
import mx.com.ferbo.model.Servicio;
import mx.com.ferbo.model.Usuario;
import mx.com.ferbo.util.DateUtil;
import mx.com.ferbo.util.EntityManagerUtil;
import mx.com.ferbo.util.InventarioException;

@Named
@ViewScoped
public class ConstanciaServicioBean implements Serializable{

	private static final long serialVersionUID = 1L;
	private static Logger log = LogManager.getLogger(ConstanciaServicioBean.class);
	
	private List<Cliente> listaClientes;
	private List<ConstanciaDeServicio> listaConstanciaServicios;
	private List<EstadoConstancia> listaEstadosConstancias;
	
	private ClienteDAO clienteDao;
	private ConstanciaServicioDAO constanciaServicioDAO;
	private ControlFacturaConstanciaDAO cfcDAO;
	private EstadoConstanciaDAO ecDAO;
	
	private Usuario usuario;
	private FacesContext faceContext;
	private HttpServletRequest httpServletRequest;
	
	private Integer idCliente;
	private Date fechaInicio;
	private Date fechaFinal;
	private String folio;
	private ConstanciaDeServicio seleccion;
	private Servicio servicioSelected;
	private BigDecimal cantidadServicio;
	private List<PrecioServicio> precioServicioList;
	private PrecioServicioDAO precioServicioDAO;
	private ConstanciaServicioDetalle selectedConstanciaServicioDetalle;
	private ConstanciaServicioDetalleDAO csdDAO;
	
	public ConstanciaServicioBean() {
		listaClientes = new ArrayList<>();
		listaConstanciaServicios = new ArrayList<>();
	}
	
	@PostConstruct
	public void init() {
		
		clienteDao = new ClienteDAO();
		constanciaServicioDAO = new ConstanciaServicioDAO();
		cfcDAO = new ControlFacturaConstanciaDAO();
		ecDAO = new EstadoConstanciaDAO();
		precioServicioDAO = new PrecioServicioDAO();
		csdDAO = new ConstanciaServicioDetalleDAO();
		
		faceContext = FacesContext.getCurrentInstance();
		httpServletRequest = (HttpServletRequest) faceContext.getExternalContext().getRequest();
		usuario = (Usuario) httpServletRequest.getSession(false).getAttribute("usuario");
		
		listaClientes = clienteDao.buscarTodos();
		listaEstadosConstancias = ecDAO.buscarTodos();
		fechaInicio = new Date();
		fechaFinal = new Date();
		folio = "";
		seleccion = new ConstanciaDeServicio();
	}
	
	public void buscarConstancia() {
		if(folio != null && "".equalsIgnoreCase(folio.trim()))
			this.folio = null;
		
		if("".equalsIgnoreCase(this.folio))
			this.folio = null;
		
		listaConstanciaServicios = constanciaServicioDAO.buscar(fechaInicio, fechaFinal, idCliente, folio);
	}
	
	public void cargaDetalle() {
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		String titulo = "Carga de información...";
		
		try {
			this.seleccion = constanciaServicioDAO.buscarPorId(this.seleccion.getFolio(), true);
			this.precioServicioList = precioServicioDAO.buscarPorCliente(seleccion.getCteCve().getCteCve(), true);
			this.servicioSelected = new Servicio();
			this.cantidadServicio = null;
		} catch (Exception ex) {
			log.error("Problema para cargar la información de la constancia...", ex);
			mensaje = "Ha ocurrido un error en el sistema. Intente nuevamente.\nSi el problema persiste, por favor comuniquese con su administrador del sistema.";
			severity = FacesMessage.SEVERITY_ERROR;
			
			message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
		} finally {
			PrimeFaces.current().ajax().update("form:messages","form:dlg-constancia", "form:dlg-partidas", "form:dlg-servicios");
		}
	}
	
	public void agregaServicio() {
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		String titulo = "Agregar servicio...";
		
		ConstanciaServicioDetalle servicio = null;
		ConstanciaDeServicio constancia = null;
		
		try {
			if(this.servicioSelected == null)
				throw new InventarioException("Debe seleccionar un servicio.");
			
			if(this.servicioSelected.getServicioCve() == null)
				throw new InventarioException("Debe seleccionar un servicio.");
			
			if(this.cantidadServicio == null)
				throw new InventarioException("Debe indicar una cantidad.");
			
			if(this.cantidadServicio.intValue() <= 0)
				throw new InventarioException("Debe indicar una cantidad correcta.");
			
			constancia = this.seleccion;
			
			servicio = new ConstanciaServicioDetalle();
			servicio.setFolio(constancia);
			servicio.setServicioCve(servicioSelected);
			servicio.setServicioCantidad(cantidadServicio);
			
			if(constancia.getConstanciaServicioDetalleList() == null)
				constancia.setConstanciaServicioDetalleList(new ArrayList<ConstanciaServicioDetalle>());
			
			constancia.getConstanciaServicioDetalleList().add(servicio);
			
			constanciaServicioDAO.actualizar(constancia);
			this.seleccion = constanciaServicioDAO.buscarPorId(this.seleccion.getFolio(), true);
			
			this.servicioSelected = new Servicio();
			this.cantidadServicio = null;
			
		} catch(InventarioException ex) {
			log.error("Problema para cargar la información de la constancia...", ex);
			mensaje = "Ha ocurrido un error en el sistema. Intente nuevamente.\nSi el problema persiste, por favor comuniquese con su administrador del sistema.";
			severity = FacesMessage.SEVERITY_ERROR;
			
			message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
		
		} catch (Exception ex) {
			log.error("Problema para cargar la información de la constancia...", ex);
			mensaje = "Ha ocurrido un error en el sistema. Intente nuevamente.\nSi el problema persiste, por favor comuniquese con su administrador del sistema.";
			severity = FacesMessage.SEVERITY_ERROR;
			
			message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
		} finally {
			PrimeFaces.current().ajax().update("form:messages","form:dlg-servicios");
		}
		
	}
	
	public void actualizaServicio() {
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		String titulo = "Agregar servicio...";
		ConstanciaDeServicio constancia = null;
		
		try {
			if(this.seleccion == null)
				throw new InventarioException("Debe indicar una constancia de servicio.");
			log.debug("Constancia de Servicio: {}", this.seleccion);
			
			constancia = this.seleccion;
			
			log.debug("Constancia Servicio Detalle: {}", this.selectedConstanciaServicioDetalle);
			
			csdDAO.actualizar(selectedConstanciaServicioDetalle);
			
			constanciaServicioDAO.actualizar(constancia);
			this.seleccion = constanciaServicioDAO.buscarPorId(this.seleccion.getFolio(), true);
			
			this.servicioSelected = new Servicio();
			this.cantidadServicio = null;
			
		} catch(InventarioException ex) {
			log.error("Problema para cargar la información de la constancia...", ex);
			mensaje = "Ha ocurrido un error en el sistema. Intente nuevamente.\nSi el problema persiste, por favor comuniquese con su administrador del sistema.";
			severity = FacesMessage.SEVERITY_ERROR;
			
			message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
		
		} catch (Exception ex) {
			log.error("Problema para cargar la información de la constancia...", ex);
			mensaje = "Ha ocurrido un error en el sistema. Intente nuevamente.\nSi el problema persiste, por favor comuniquese con su administrador del sistema.";
			severity = FacesMessage.SEVERITY_ERROR;
			
			message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
		} finally {
			PrimeFaces.current().ajax().update("form:messages","form:dlg-servicios");
		}
		
		
	}
	
	public void eliminaServicio() {
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		String titulo = "Agregar servicio...";
		
		try {
			log.debug("Constancia De Servicio: {}", this.seleccion);
			log.debug("Constancia Servicio Detalle (eliminar) {}", this.selectedConstanciaServicioDetalle);
			
			if(this.seleccion == null)
				throw new InventarioException("Debe seleccionar una constancia.");
			
			if(this.selectedConstanciaServicioDetalle == null)
				throw new InventarioException("Debe indicar un servicio.");
			
			//this.selectedConstanciaServicioDetalle = csdDAO.buscarPorId(selectedConstanciaServicioDetalle.getConstanciaServicioDetalleCve());
			this.seleccion.getConstanciaServicioDetalleList().remove(this.selectedConstanciaServicioDetalle);
			
			constanciaServicioDAO.actualizar(seleccion);
			this.seleccion = constanciaServicioDAO.buscarPorId(this.seleccion.getFolio(), true);
			
			PrimeFaces.current().ajax().update("form:messages","form:dlg-servicios");
		} catch(InventarioException ex) {
			log.error("Problema para eliminar el servicio de la constancia...", ex);
			mensaje = "Ha ocurrido un error en el sistema. Intente nuevamente.\nSi el problema persiste, por favor comuniquese con su administrador del sistema.";
			severity = FacesMessage.SEVERITY_ERROR;
			
			message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
		
		} catch (Exception ex) {
			log.error("Problema para eliminar el servicio de la constancia...", ex);
			mensaje = "Ha ocurrido un error en el sistema. Intente nuevamente.\nSi el problema persiste, por favor comuniquese con su administrador del sistema.";
			severity = FacesMessage.SEVERITY_ERROR;
			
			message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
		} finally {
			PrimeFaces.current().ajax().update("form:messages","form:dlg-servicios");
		}
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
			//manager.merge(constancia);
			
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
		return constanciaServicioDAO;
	}

	public void setConstanciaServicioDao(ConstanciaServicioDAO constanciaServicioDao) {
		this.constanciaServicioDAO = constanciaServicioDao;
	}

	public Integer getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(Integer idCliente) {
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

	public Servicio getServicioSelected() {
		return servicioSelected;
	}

	public void setServicioSelected(Servicio servicioSelected) {
		this.servicioSelected = servicioSelected;
	}

	public BigDecimal getCantidadServicio() {
		return cantidadServicio;
	}

	public void setCantidadServicio(BigDecimal cantidadServicio) {
		this.cantidadServicio = cantidadServicio;
	}

	public List<PrecioServicio> getPrecioServicioList() {
		return precioServicioList;
	}

	public void setPrecioServicioList(List<PrecioServicio> precioServicioList) {
		this.precioServicioList = precioServicioList;
	}

	public ConstanciaServicioDetalle getSelectedConstanciaServicioDetalle() {
		return selectedConstanciaServicioDetalle;
	}

	public void setSelectedConstanciaServicioDetalle(ConstanciaServicioDetalle selectedConstanciaServicioDetalle) {
		this.selectedConstanciaServicioDetalle = selectedConstanciaServicioDetalle;
	}

}
