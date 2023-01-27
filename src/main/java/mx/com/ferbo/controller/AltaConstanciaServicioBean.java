package mx.com.ferbo.controller;

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
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.apache.log4j.Logger;
import org.primefaces.PrimeFaces;

import mx.com.ferbo.dao.ClienteDAO;
import mx.com.ferbo.dao.ConstanciaServicioDAO;
import mx.com.ferbo.dao.EstadoConstanciaDAO;
import mx.com.ferbo.dao.UnidadDeManejoDAO;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.ConstanciaDeServicio;
import mx.com.ferbo.model.ConstanciaServicioDetalle;
import mx.com.ferbo.model.EstadoConstancia;
import mx.com.ferbo.model.PartidaServicio;
import mx.com.ferbo.model.PrecioServicio;
import mx.com.ferbo.model.Producto;
import mx.com.ferbo.model.ProductoPorCliente;
import mx.com.ferbo.model.UnidadDeManejo;
import mx.com.ferbo.util.EntityManagerUtil;
import mx.com.ferbo.util.InventarioException;

@Named
@ViewScoped
public class AltaConstanciaServicioBean implements Serializable {

	private static final long serialVersionUID = -3109002730694247052L;
	
	private static Logger log = Logger.getLogger(AltaConstanciaServicioBean.class);
	
	private List<Cliente> clientes;
	private List<PartidaServicio> alPartidas;
	private List<ConstanciaServicioDetalle> alServiciosDetalle;
	private List<PrecioServicio> alServicios;
	
	private List<ProductoPorCliente> alProductosFiltered;
	private List<UnidadDeManejo> alUnidades;
	
	private Date fecha;
	private String folio;
	private Integer cantidad;
	private Integer idUnidadManejo;
	private Integer idProducto;
	private BigDecimal peso;
	private BigDecimal valorDeclarado;
	private String observaciones;
	private String nombreTransportista;
	private String placasVehiculo;
	private Cliente selCliente;
	private Integer idCliente;
	private UnidadDeManejo selUnidadManejo;
	private Integer idPrecioServicio;
	private BigDecimal cantidadServicio;
	
	private ClienteDAO clienteDAO;
	private UnidadDeManejoDAO udmDAO;
	private ConstanciaServicioDAO csDAO;
	private EstadoConstanciaDAO edoDAO;
	
	private PartidaServicio selPartida;
	private ConstanciaServicioDetalle selServicio;
	private boolean isSaved = false;
	private List<EstadoConstancia> estados = null;
	
	
	
	public AltaConstanciaServicioBean() {
		log.info("Entrando al constructor del controller...");
		clientes = new ArrayList<Cliente>();
		alPartidas = new ArrayList<PartidaServicio>();
		alServiciosDetalle = new ArrayList<ConstanciaServicioDetalle>();
		alServicios = new ArrayList<PrecioServicio>();
		alUnidades = new ArrayList<UnidadDeManejo>();
		clienteDAO = new ClienteDAO();
		udmDAO = new UnidadDeManejoDAO();
		csDAO = new ConstanciaServicioDAO();
		alProductosFiltered = new ArrayList<ProductoPorCliente>();
		edoDAO = new EstadoConstanciaDAO();
		selCliente = new Cliente();
	}
	
	@PostConstruct
	public void init() {
		log.info("Entrando a Init...");
		clientes = clienteDAO.buscarTodos();
		fecha = new Date();
		alUnidades = udmDAO.buscarTodos();
		if(alProductosFiltered == null)
			alProductosFiltered = new ArrayList<ProductoPorCliente>();
		estados = edoDAO.buscarTodos();
	}
	
	public void filtrarCliente() {
		
		String message = null;
		Severity severity = null;
		EntityManager manager = null;
		Cliente cliente = null;
		Map<Integer, List<PrecioServicio>> mpPrecioServicio = new HashMap<Integer, List<PrecioServicio>>();
		List<PrecioServicio> precioServicioList = null;
		selCliente.setCteCve(this.idCliente);
		try {
			log.info("Entrando a filtrar cliente...");
			//selCliente = clientes.stream()
			manager = EntityManagerUtil.getEntityManager();
			cliente = manager.createNamedQuery("Cliente.findByCteCve", Cliente.class).setParameter("cteCve", this.idCliente).getSingleResult();
			log.info("Productos cargados: "+ cliente.getProductoPorClienteList().size());
			alProductosFiltered.clear();
			alProductosFiltered.addAll(cliente.getProductoPorClienteList());
			for(ProductoPorCliente ppc : alProductosFiltered) {
				log.info("Producto: "+ ppc.getProductoCve()); 
			}
			precioServicioList = cliente.getPrecioServicioList();
			Integer idAviso = new Integer(-1);
			for(PrecioServicio ps : precioServicioList) {
				Integer avisoCve = ps.getAvisoCve().getAvisoCve();
				if(avisoCve > idAviso)
					idAviso = new Integer(avisoCve);
				List<PrecioServicio> list = mpPrecioServicio.get(avisoCve);
				if(list == null) {
					list = new ArrayList<PrecioServicio>();
					mpPrecioServicio.put(avisoCve, list);
				}list.add(ps);
			}
			mpPrecioServicio.get(idAviso);			
			alServicios.clear();
			alServicios = mpPrecioServicio.get(idAviso);
			for(PrecioServicio ps : alServicios) {
				log.info(ps.getServicio().getServicioDs());
				log.info(ps.getUnidad().getUnidadDeManejoDs());
			}
			message = "Agregue sus productos y servicios.";
			severity = FacesMessage.SEVERITY_INFO;
		} catch(Exception ex) {
			log.error("Problema para recuperar los datos del cliente.", ex);
			message = ex.getMessage();
			severity = FacesMessage.SEVERITY_ERROR;
		} finally {
			if(manager != null)
				manager.close();
			FacesContext.getCurrentInstance().addMessage(null,  new FacesMessage(severity, "Cliente", message));
			PrimeFaces.current().ajax().update("form:messages", "form:dt-partidas");
		}
		log.info("Productos y/o servicios del cliente filtrados.");
	}
	
	public void agregarProducto() {
		String message = null;
		Severity severity = null;
		try {
			log.info("AGREGANDO PRODUCTO...");
			
			if(this.idCliente == null || this.idCliente == 0)
				throw new InventarioException("Debe seleccionar el cliente");
			
			if(this.cantidad == null || this.cantidad <= 0)
				throw new InventarioException("Debe indicar la cantidad de piezas");
			
			if(this.peso == null || this.peso.compareTo(BigDecimal.ZERO) <= 0)
				throw new InventarioException("Debe indicar el peso del producto.");
			
			if(this.idUnidadManejo == null)
				throw new InventarioException("Debe seleccionar una unidad de manejo");
			
			if(this.idProducto == null)
				throw new InventarioException("Debe seleccionar un producto");
			
			if(alPartidas == null)
				alPartidas = new ArrayList<PartidaServicio>();	
				
				UnidadDeManejo udm = alUnidades.stream().filter(u -> this.idUnidadManejo == u.getUnidadDeManejoCve()).collect(Collectors.toList()).get(0);
			
			if(udm == null)
				throw new InventarioException("Debe seleccionar una unidad de producto.");
				ProductoPorCliente prd = alProductosFiltered.stream().filter(p -> this.idProducto.equals(p.getProductoCve().getProductoCve())).collect(Collectors.toList()).get(0);
				
			if(prd == null)
				throw new InventarioException("Debe seleccionar un producto.");
				Producto p = prd.getProductoCve();
				PartidaServicio partida = new PartidaServicio();
				partida.setCantidadDeCobro(this.peso);
				partida.setCantidadTotal(this.cantidad);
				partida.setUnidadDeCobro(udm);
				partida.setUnidadDeManejoCve(udm);
				partida.setProductoCve(p);
				alPartidas.add(partida);
				message = "Producto agregado correctamente.";
				severity = FacesMessage.SEVERITY_INFO;
		} catch(InventarioException ex) {
			log.error("Problema para obtener la información de los productos...", ex);
			message = ex.getMessage();
			severity = FacesMessage.SEVERITY_ERROR;
		} catch(Exception ex) {
			log.error("Problema para obtener la informaciòn de los productos...", ex);
			message = "Problema al agregar sus productos.";
			severity = FacesMessage.SEVERITY_ERROR;
		} finally {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, "Agregar producto", message));
			PrimeFaces.current().ajax().update("form:messages", "form:form:dt-partidas");
		}	
		log.info("Id Producto: " + this.idProducto);
	}
	
	public void agregarServicio() {
		String message = null;
		Severity severity = null;		
		PrecioServicio precioServicio = null;
		ConstanciaServicioDetalle servicio = null;
		
		try {
			if(this.idCliente == null || this.idCliente == 0)
				throw new InventarioException("Debe seleccionar el cliente");
			
			if(this.cantidadServicio == null || this.cantidadServicio.compareTo(BigDecimal.ZERO) <= 0 )
				throw new InventarioException("Debe indicar la cantidad de servicios.");
			
			if(this.idPrecioServicio == null)
				throw new InventarioException("Debe seleccionar un servicio.");
			
			precioServicio = this.alServicios.stream().filter(ps -> this.idPrecioServicio.equals(ps.getId())).collect(Collectors.toList()).get(0);
			if(alServiciosDetalle == null)
				alServiciosDetalle = new ArrayList<ConstanciaServicioDetalle>();	
			
			servicio = new ConstanciaServicioDetalle();
			servicio.setServicioCantidad(this.cantidadServicio);
			servicio.setServicioCve(precioServicio.getServicio());
			alServiciosDetalle.add(servicio);
			message = "Producto agregado correctamente.";
			severity = FacesMessage.SEVERITY_INFO;
		} catch(InventarioException ex) {
			log.error("Problema para obtener la información de los productos...", ex);
			message = ex.getMessage();
			severity = FacesMessage.SEVERITY_ERROR;
		} catch(Exception ex) {
			log.error("Problema para obtener el listado de servicios del cliente.", ex);
			message = "Problema con la información de servicios.";
			severity = FacesMessage.SEVERITY_ERROR;
		} finally {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, "Agregar servicio", message));
			PrimeFaces.current().ajax().update("form:messages", "form:dt-constanciaServicios");
		}
	}
	
	public synchronized void guardar() {
		String message = null;
		Severity severity = null;
		
		ConstanciaDeServicio constancia = null;
		List<ConstanciaDeServicio> alConstancias = null;
		EstadoConstancia estado = null;
		
		try {
			if(this.isSaved)
				throw new InventarioException("La constancia ya se encuentra registrada.");
			
			if(this.folio == null || "".equalsIgnoreCase(this.folio.trim()))
				throw new InventarioException("Debe indicar el folio de la constancia.");
			
			if(this.alServiciosDetalle == null || this.alServiciosDetalle.size() == 0)
				throw new InventarioException("Debe seleccionar al menos un servicio");
			
			alConstancias = csDAO.buscarPorFolioCliente(this.folio);
			
			if(alConstancias != null && alConstancias.size() > 0)
				throw new InventarioException(String.format("El folio %s ya se encuentra registrado.", this.folio));
			
			estado = estados.stream().filter(e -> e.getEdoCve() == 1).collect(Collectors.toList()).get(0);
			
			constancia = new ConstanciaDeServicio();
			constancia.setFolioCliente(this.folio);
			constancia.setCteCve(this.selCliente);
			constancia.setFecha(this.fecha);
			constancia.setObservaciones(this.observaciones);
			constancia.setNombreTransportista(this.nombreTransportista);
			constancia.setPlacasTransporte(this.placasVehiculo);
			constancia.setPartidaServicioList(this.alPartidas);
			constancia.setConstanciaServicioDetalleList(this.alServiciosDetalle);
			constancia.setStatus(estado);
			
			for(PartidaServicio partida : this.alPartidas) {
				partida.setFolio(constancia);
			}
			
			for(ConstanciaServicioDetalle servicio : this.alServiciosDetalle) {
				servicio.setFolio(constancia);
			}
			
			csDAO.actualizar(constancia);
			
			this.isSaved = true;
			message = String.format("Constancia guardada correctamente con el folio %s", this.folio);
			severity = FacesMessage.SEVERITY_INFO;
		} catch(InventarioException ex) {
			log.error("Problema para obtener la información de los productos...", ex);
			message = ex.getMessage();
			severity = FacesMessage.SEVERITY_ERROR;
		} catch(Exception ex) {
			log.error("Problema para obtener el listado de servicios del cliente.", ex);
			ex.printStackTrace();

			message = "Problema con la información de servicios.";
			severity = FacesMessage.SEVERITY_ERROR;
		} finally {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, "Agregar servicio", message));
			PrimeFaces.current().ajax().update("form:messages", "form:dt-constanciaServicios");
		}
	}
	
	public void deletePartida(PartidaServicio partida) {
		this.alPartidas.remove(partida);
	}
	
	public void deleteServicio(ConstanciaServicioDetalle servicio) {
		this.alServiciosDetalle.remove(servicio);
	}
	
	public Cliente getSelCliente() {
		return selCliente;
	}
	public void setSelCliente(Cliente selCliente) {
		this.selCliente = selCliente;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public String getFolio() {
		return folio;
	}
	public void setFolio(String folio) {
		this.folio = folio;
	}
	public BigDecimal getValorDeclarado() {
		return valorDeclarado;
	}
	public void setValorDeclarado(BigDecimal valorDeclarado) {
		this.valorDeclarado = valorDeclarado;
	}
	public String getObservaciones() {
		return observaciones;
	}
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	public String getNombreTransportista() {
		return nombreTransportista;
	}
	public void setNombreTransportista(String nombreTransportista) {
		this.nombreTransportista = nombreTransportista;
	}
	public String getPlacasVehiculo() {
		return placasVehiculo;
	}
	public void setPlacasVehiculo(String placasVehiculo) {
		this.placasVehiculo = placasVehiculo;
	}

	public List<Cliente> getClientes() {
		return clientes;
	}

	public void setClientes(List<Cliente> clientes) {
		this.clientes = clientes;
	}

	public Integer getCantidad() {
		return cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

	public Integer getIdUnidadManejo() {
		return idUnidadManejo;
	}

	public void setIdUnidadManejo(Integer idUnidadManejo) {
		this.idUnidadManejo = idUnidadManejo;
	}

	public List<UnidadDeManejo> getAlUnidades() {
		return alUnidades;
	}

	public void setAlUnidades(List<UnidadDeManejo> alUnidades) {
		this.alUnidades = alUnidades;
	}

	public Integer getIdProducto() {
		return idProducto;
	}

	public void setIdProducto(Integer idProducto) {
		this.idProducto = idProducto;
	}

	public List<ProductoPorCliente> getAlProductosFiltered() {
		return alProductosFiltered;
	}

	public void setAlProductosFiltered(List<ProductoPorCliente> alProductosFiltered) {
		this.alProductosFiltered = alProductosFiltered;
	}

	public UnidadDeManejo getSelUnidadManejo() {
		return selUnidadManejo;
	}

	public void setSelUnidadManejo(UnidadDeManejo selUnidadManejo) {
		this.selUnidadManejo = selUnidadManejo;
	}

	public Integer getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(Integer idCliente) {
		this.idCliente = idCliente;
	}

	public BigDecimal getPeso() {
		return peso;
	}

	public void setPeso(BigDecimal peso) {
		this.peso = peso;
	}

	public PartidaServicio getSelPartida() {
		return selPartida;
	}

	public void setSelPartida(PartidaServicio selPartida) {
		this.selPartida = selPartida;
	}

	public List<PartidaServicio> getAlPartidas() {
		return alPartidas;
	}

	public void setAlPartidas(List<PartidaServicio> alPartidas) {
		this.alPartidas = alPartidas;
	}

	public List<ConstanciaServicioDetalle> getAlServiciosDetalle() {
		return alServiciosDetalle;
	}

	public void setAlServiciosDetalle(List<ConstanciaServicioDetalle> alServiciosDetalle) {
		this.alServiciosDetalle = alServiciosDetalle;
	}

	public List<PrecioServicio> getAlServicios() {
		return alServicios;
	}

	public void setAlServicios(List<PrecioServicio> alServicios) {
		this.alServicios = alServicios;
	}

	public Integer getIdPrecioServicio() {
		return idPrecioServicio;
	}

	public void setIdPrecioServicio(Integer idPrecioServicio) {
		this.idPrecioServicio = idPrecioServicio;
	}

	public ConstanciaServicioDetalle getSelServicio() {
		return selServicio;
	}

	public void setSelServicio(ConstanciaServicioDetalle selServicio) {
		this.selServicio = selServicio;
	}

	public BigDecimal getCantidadServicio() {
		return cantidadServicio;
	}

	public void setCantidadServicio(BigDecimal cantidadServicio) {
		this.cantidadServicio = cantidadServicio;
	}
}
