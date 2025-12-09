package mx.com.ferbo.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

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
import mx.com.ferbo.util.JasperReportUtil;
import mx.com.ferbo.util.conexion;

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
	private StreamedContent file;
	
	public ConstanciaServicioBean() {
		listaClientes = new ArrayList<>();
		listaConstanciaServicios = new ArrayList<>();
	}
	
	@SuppressWarnings("unchecked")
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
		
                log.info("El usuario {} entrando a consultar constancia de servicio", usuario.getUsuario());
                
		listaClientes = (List<Cliente>) httpServletRequest.getSession(false).getAttribute("clientesActivosList");
		if(listaClientes.size() == 1)
			this.idCliente = listaClientes.get(0).getCteCve();
		
		listaEstadosConstancias = ecDAO.buscarTodos();
		folio = "";
		seleccion = new ConstanciaDeServicio();
		this.fechaInicio = null;
		this.fechaFinal = null;
	}
	
	public void buscarConstancia() {
		if(folio != null && "".equalsIgnoreCase(folio.trim()))
			this.folio = null;
		
		if("".equalsIgnoreCase(this.folio))
			this.folio = null;
		
		if(listaClientes.size() == 1)
			this.idCliente = listaClientes.get(0).getCteCve();
		
		this.actualizarConstancias();
	}
        
        public void actualizarConstancias(){
            this.listaConstanciaServicios = constanciaServicioDAO.buscar(fechaInicio, fechaFinal, idCliente, folio);
        }
	
	public void cargaDetalle() {
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		String titulo = "Carga de información...";
		
		try {
                        log.info("Cargando el detalle de la constancia: {}", this.seleccion.getFolioCliente());
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
	
	public void guardaDetalle() {
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		String titulo = "Constancia de servicio...";
		
		String response = null;
		
		try {
                        log.info("Actualizando la informacion de la constancia: {}", this.seleccion.getFolioCliente());
			response = constanciaServicioDAO.actualizar(this.seleccion);
			log.debug("Respuesta del DAO: {}", response);
			if(response != null )
				throw new InventarioException("Problema para guardar la constancia de servicio...");
			
			mensaje = "La constancia se actualizó correctamente.";
			severity = FacesMessage.SEVERITY_INFO;
			
			PrimeFaces.current().executeScript("PF('dlg-constancia').hide()");
		} catch (Exception ex) {
			log.error("Problema para cargar la información de la constancia...", ex);
			mensaje = "Ha ocurrido un error en el sistema. Intente nuevamente.\nSi el problema persiste, por favor comuniquese con su administrador del sistema.";
			severity = FacesMessage.SEVERITY_ERROR;
		} finally {
			message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
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
			
                        log.info("Agregando el servicio a la constancia: {}", this.seleccion.getFolio());
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
                        log.info("Actualizando la constancia de servicio: {}", constancia.getFolioCliente());
			
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
                        log.info("Eliminando servicio de la constancia: {}", this.seleccion.getFolioCliente());
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
		ConstanciaDeServicio constancia = this.seleccion;
		String message = null;
		Severity severity = null;
		
		List<ControlFacturaConstanciaDS> alControlFacturas = null;
		EstadoConstancia stCancelada = null;
		String observaciones = null;
		String sFechaHoy = null;
		Date fechaHoy = null;
		
		try {
			log.info("Cancelando constancia: {}", constancia.getFolioCliente());
			if(constancia.getStatus().getEdoCve() == 4)
				throw new InventarioException("La constancia ya se encuentra cancelada.");
			
			//Validar si la constancia de servicio ya se encuentra facturada.
			alControlFacturas = cfcDAO.buscarPorConstancia(constancia.getFolio());
			
			if(alControlFacturas != null && alControlFacturas.size() > 0) {
				throw new InventarioException("La constancia se encuentra facturada, no es posible cancelar.");
			}
			
			stCancelada = listaEstadosConstancias.stream()
					.filter(estado -> estado.getEdoCve().equals(4))
					.collect(Collectors.toList())
					.get(0);
			
			constancia.setStatus(stCancelada);
			fechaHoy = new Date();
			sFechaHoy = DateUtil.getString(fechaHoy, DateUtil.FORMATO_DD_MM_YYYY);
			observaciones = constancia.getObservaciones();
			observaciones = String.format("CANCELADA EL %S: %S", sFechaHoy, observaciones);
			constancia.setObservaciones(observaciones);
			
			this.constanciaServicioDAO.actualizar(constancia);
			message = "Constancia cancelada correctamente.";
			severity = FacesMessage.SEVERITY_INFO;
			
			log.info("Constancia cancelada.");
		} catch(InventarioException ex) {
			log.error("Error al cancelar: ", ex);
                        message = ex.getMessage();
			severity = FacesMessage.SEVERITY_ERROR;
		} catch(Exception ex) {
			log.error("Error al cancelar: ", ex);
			message = "Problema con la actualización de la constancia.";
			severity = FacesMessage.SEVERITY_ERROR;
		} finally {
			this.actualizarConstancias();
                        PrimeFaces.current().executeScript("PF('dlg-constancia').hide();");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, "Cancelación", message));
			PrimeFaces.current().ajax().update("form:messages", "form:dtconstanciaServicios");
		}
	}
	
	public void jasper() {
		String jasperPath = null;
		String filename = null;
		String images = null;
		String message = null;
		Severity severity = null;
		File reportFile = null;
		File imgfile = null;
		JasperReportUtil jasperReportUtil = null;
		Map<String, Object> parameters = null;
		Connection conn = null;
		
		try {
			jasperPath = "/jasper/ticketServicio.jrxml";
			filename = String.format("ticket-servicio_%s.pdf", this.seleccion.getFolioCliente());
			images = "/images/logoF.png";
			reportFile = new File(jasperPath);
			URL resource = getClass().getResource(jasperPath);
			URL resourceimg = getClass().getResource(images);
			String file = resource.getFile();
			String img = resourceimg.getFile();
			reportFile = new File(file);
			imgfile = new File(img);
			log.debug("Ruta del reporte: {}", reportFile.getPath());
			
			
			conn = EntityManagerUtil.getConnection();
			parameters = new HashMap<String, Object>();
			parameters.put("REPORT_CONNECTION", conn);
			parameters.put("FOLIO", this.seleccion.getFolioCliente());
			parameters.put("LogoPath", imgfile.getPath());
			log.debug("Parametros: {}", parameters.toString());
			
			jasperReportUtil = new JasperReportUtil();
			byte[] bytes = jasperReportUtil.createPDF(parameters, reportFile.getPath());
			InputStream input = new ByteArrayInputStream(bytes);
			this.file = DefaultStreamedContent.builder()
					.contentType("application/pdf")
					.name(filename)
					.stream(() -> input )
					.build();
			log.info("Factura generada {}...", filename);
		} catch (Exception ex) {
			log.error("Problema general...", ex);
			message = String.format("Problema para imprimir el folio %s", this.seleccion.getFolioCliente());
			severity = FacesMessage.SEVERITY_INFO;
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, "Error en impresion", message));
			PrimeFaces.current().ajax().update("form:messages", "form:dt-facturacionServicios");
		} finally {
			conexion.close((Connection) conn);
			PrimeFaces.current().ajax().update("frmFactura:file-factura");
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

	public StreamedContent getFile() {
		return file;
	}

	public void setFile(StreamedContent file) {
		this.file = file;
	}

}
