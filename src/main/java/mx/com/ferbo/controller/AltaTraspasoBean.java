package mx.com.ferbo.controller;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.primefaces.PrimeFaces;

import mx.com.ferbo.dao.AvisoDAO;
import mx.com.ferbo.dao.ClienteDAO;
import mx.com.ferbo.dao.ConstanciaDeDepositoDAO;
import mx.com.ferbo.dao.ConstanciaServicioDAO;
import mx.com.ferbo.dao.EstadoConstanciaDAO;
import mx.com.ferbo.dao.InventarioDAO;
import mx.com.ferbo.dao.PartidaDAO;
import mx.com.ferbo.dao.PartidaServicioDAO;
import mx.com.ferbo.dao.PlantaDAO;
import mx.com.ferbo.dao.UnidadDeManejoDAO;
import mx.com.ferbo.model.Aviso;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.ConstanciaDeDeposito;
import mx.com.ferbo.model.ConstanciaDeServicio;
import mx.com.ferbo.model.ConstanciaServicioDetalle;
import mx.com.ferbo.model.DetallePartida;
import mx.com.ferbo.model.EstadoConstancia;
import mx.com.ferbo.model.Inventario;
import mx.com.ferbo.model.Partida;
import mx.com.ferbo.model.PartidaServicio;
import mx.com.ferbo.model.Planta;
import mx.com.ferbo.model.PrecioServicio;
import mx.com.ferbo.model.Producto;
import mx.com.ferbo.model.ProductoPorCliente;
import mx.com.ferbo.model.UnidadDeManejo;
import mx.com.ferbo.util.EntityManagerUtil;
import mx.com.ferbo.util.InventarioException;
import mx.com.ferbo.util.JasperReportUtil;
import mx.com.ferbo.util.conexion;
import net.sf.jasperreports.engine.JRException;

@Named
@ViewScoped
public class AltaTraspasoBean implements Serializable {

	private static final long serialVersionUID = -3109002730694247052L;
	private static Logger log = Logger.getLogger(AltaConstanciaServicioBean.class);

	private List<Cliente> clientes;
	private List<PartidaServicio> alPartidas;
	private List<ConstanciaServicioDetalle> alServiciosDetalle;
	private List<PrecioServicio> alServicios;
	private List<ProductoPorCliente> alProductosFiltered;
	private List<UnidadDeManejo> alUnidades;
	private List<EstadoConstancia> estados = null;
	private List<ConstanciaDeDeposito> listaconstanciadepo;
	private List<Partida> partida;
	private List<DetallePartida> ldpartida;
	private List<Inventario> inventario;
	
	private Date fecha;
	private String numero;
	private Integer cantidad;
	private Integer idUnidadManejo;
	private Integer idProducto;
	private BigDecimal peso;
	private BigDecimal valorDeclarado;
	private String observaciones;
	private String nombreTransportista;
	private String placasVehiculo;
	private String unidadcobro;
	private UnidadDeManejo selUnidadManejo;
	private Integer idPrecioServicio;
	private BigDecimal cantidadServicio;
	private Integer idCliente;
	private Integer idclavectedeposito;

	private Cliente selCliente;
	private ConstanciaDeDeposito ctecve;
	private PartidaServicio selPartida;
	private ConstanciaServicioDetalle selServicio;

	private UnidadDeManejoDAO udmDAO;
	private ConstanciaServicioDAO csDAO;
	private EstadoConstanciaDAO edoDAO;
	private ClienteDAO clienteDAO;
	private PartidaServicioDAO partidaservicioDAO;
	private ConstanciaDeDepositoDAO constanciadepDAO;
	private PartidaDAO partidaDAO;
	private InventarioDAO inventarioDAO;
	private boolean isSaved = false;
	private boolean habilitareporte = false;

	public AltaTraspasoBean() {
		log.info("Entrando al constructor del controller...");
		partidaservicioDAO = new PartidaServicioDAO();
		clienteDAO = new ClienteDAO();
		udmDAO = new UnidadDeManejoDAO();
		csDAO = new ConstanciaServicioDAO();
		edoDAO = new EstadoConstanciaDAO();
		partidaDAO = new PartidaDAO();
		inventarioDAO = new InventarioDAO();
		constanciadepDAO = new ConstanciaDeDepositoDAO();
		clientes = new ArrayList<Cliente>();
		partida = new ArrayList<Partida>();
		ldpartida = new ArrayList<DetallePartida>();
		inventario = new ArrayList<Inventario>();
		// alPartidas = new ArrayList<PartidaServicio>();
		alServiciosDetalle = new ArrayList<ConstanciaServicioDetalle>();
		alServicios = new ArrayList<PrecioServicio>();
		alUnidades = new ArrayList<UnidadDeManejo>();
		alProductosFiltered = new ArrayList<ProductoPorCliente>();
		listaconstanciadepo = new ArrayList<ConstanciaDeDeposito>();
		selCliente = new Cliente();
		alPartidas = partidaservicioDAO.findall();
		clientes = clienteDAO.findall();
		partida = partidaDAO.findall();
		
	}

	@PostConstruct
	public void init() {
		log.info("Entrando a Init...");
		clientes = clienteDAO.buscarTodos();
		fecha = new Date();
		alUnidades = udmDAO.buscarTodos();
		if (alProductosFiltered == null)
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
		selCliente = clienteDAO.buscarPorId(idCliente);
		inventario = inventarioDAO.buscarPorCliente(selCliente);
		try {
			log.info("Entrando a filtrar cliente...");
			// selCliente = clientes.stream()
			manager = EntityManagerUtil.getEntityManager();
			cliente = manager.createNamedQuery("Cliente.findByCteCve", Cliente.class)
					.setParameter("cteCve", this.idCliente).getSingleResult();
			log.info("Productos cargados: " + cliente.getProductoPorClienteList().size());
			alProductosFiltered.clear();
			alProductosFiltered.addAll(cliente.getProductoPorClienteList());
			for (ProductoPorCliente ppc : alProductosFiltered) {
				log.info("Producto: " + ppc.getProductoCve());
			}
			precioServicioList = cliente.getPrecioServicioList();
			Integer idAviso = new Integer(-1);
			for (PrecioServicio ps : precioServicioList) {
				Integer avisoCve = ps.getAvisoCve().getAvisoCve();
				if (avisoCve > idAviso)
					idAviso = new Integer(avisoCve);
				List<PrecioServicio> list = mpPrecioServicio.get(avisoCve);
				if (list == null) {
					list = new ArrayList<PrecioServicio>();
					mpPrecioServicio.put(avisoCve, list);
				}
				list.add(ps);
			}
			mpPrecioServicio.get(idAviso);
			alServicios.clear();
			alServicios = mpPrecioServicio.get(idAviso);
			for (PrecioServicio ps : alServicios) {
				log.info(ps.getServicio().getServicioDs());
				log.info(ps.getUnidad().getUnidadDeManejoDs());
			}
			message = "Agregue sus productos y servicios.";
			severity = FacesMessage.SEVERITY_INFO;
		} catch (Exception ex) {
			log.error("Problema para recuperar los datos del cliente.", ex);
			message = ex.getMessage();
			severity = FacesMessage.SEVERITY_ERROR;
		} finally {
			if (manager != null)
				manager.close();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, "Cliente", message));
			PrimeFaces.current().ajax().update("form:messages", "form:dt-partidas");
		}
		log.info("Productos y/o servicios del cliente filtrados.");
	}

	public void agregarProducto() {
		String message = null;
		Severity severity = null;
		try {
			log.info("AGREGANDO PRODUCTO...");

			if (this.idCliente == null || this.idCliente == 0)
				throw new InventarioException("Debe seleccionar el cliente");

			if (this.cantidad == null || this.cantidad <= 0)
				throw new InventarioException("Debe indicar la cantidad de piezas");

			if (this.peso == null || this.peso.compareTo(BigDecimal.ZERO) <= 0)
				throw new InventarioException("Debe indicar el peso del producto.");

			if (this.idUnidadManejo == null)
				throw new InventarioException("Debe seleccionar una unidad de manejo");

			if (this.idProducto == null)
				throw new InventarioException("Debe seleccionar un producto");

			if (this.idclavectedeposito == null)
				throw new InventarioException("Debe seleccionar algo");

			if (alPartidas == null)
				alPartidas = new ArrayList<PartidaServicio>();

			UnidadDeManejo udm = alUnidades.stream().filter(u -> this.idUnidadManejo == u.getUnidadDeManejoCve())
					.collect(Collectors.toList()).get(0);

			if (udm == null)
				throw new InventarioException("Debe seleccionar una unidad de producto.");
			ProductoPorCliente prd = alProductosFiltered.stream()
					.filter(p -> this.idProducto.equals(p.getProductoCve().getProductoCve()))
					.collect(Collectors.toList()).get(0);
			ConstanciaDeDeposito cdd = listaconstanciadepo.stream()
					.filter(p -> this.idclavectedeposito.equals(p.getCteCve().getCteCve())).collect(Collectors.toList())
					.get(0);

			if (prd == null)
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
		} catch (InventarioException ex) {
			log.error("Problema para obtener la información de los productos...", ex);
			message = ex.getMessage();
			severity = FacesMessage.SEVERITY_ERROR;
		} catch (Exception ex) {
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
			if (this.idCliente == null || this.idCliente == 0)
				throw new InventarioException("Debe seleccionar el cliente");

			if (this.cantidadServicio == null || this.cantidadServicio.compareTo(BigDecimal.ZERO) <= 0)
				throw new InventarioException("Debe indicar la cantidad de servicios.");

			if (this.idPrecioServicio == null)
				throw new InventarioException("Debe seleccionar un servicio.");

			precioServicio = this.alServicios.stream().filter(ps -> this.idPrecioServicio.equals(ps.getId()))
					.collect(Collectors.toList()).get(0);
			if (alServiciosDetalle == null)
				alServiciosDetalle = new ArrayList<ConstanciaServicioDetalle>();

			servicio = new ConstanciaServicioDetalle();
			servicio.setServicioCantidad(this.cantidadServicio);
			servicio.setServicioCve(precioServicio.getServicio());
			alServiciosDetalle.add(servicio);
			message = "Producto agregado correctamente.";
			severity = FacesMessage.SEVERITY_INFO;
		} catch (InventarioException ex) {
			log.error("Problema para obtener la información de los productos...", ex);
			message = ex.getMessage();
			severity = FacesMessage.SEVERITY_ERROR;
		} catch (Exception ex) {
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
			if (this.isSaved)
				throw new InventarioException("La constancia ya se encuentra registrada.");

			if (this.numero == null || "".equalsIgnoreCase(this.numero.trim()))
				throw new InventarioException("Debe indicar el folio de la constancia.");

			if (this.alServiciosDetalle == null || this.alServiciosDetalle.size() == 0)
				throw new InventarioException("Debe seleccionar al menos un servicio");

			alConstancias = csDAO.buscarPorFolioCliente(this.numero);

			if (alConstancias != null && alConstancias.size() > 0)
				throw new InventarioException(String.format("El folio %s ya se encuentra registrado.", this.numero));

			estado = estados.stream().filter(e -> e.getEdoCve() == 1).collect(Collectors.toList()).get(0);
			constancia = new ConstanciaDeServicio();
			constancia.setFecha(this.fecha);
			constancia.setFolioCliente(this.numero);
			constancia.setCteCve(this.selCliente);
			constancia.setObservaciones(this.observaciones);
			constancia.setValorDeclarado(this.valorDeclarado);
			constancia.setNombreTransportista(this.nombreTransportista);
			constancia.setPlacasTransporte(this.placasVehiculo);
			constancia.setPartidaServicioList(this.alPartidas);
			constancia.setConstanciaServicioDetalleList(this.alServiciosDetalle);
			constancia.setStatus(estado);
			for (PartidaServicio partida : this.alPartidas) {
				partida.setFolio(constancia);
			}
			for (ConstanciaServicioDetalle servicio : this.alServiciosDetalle) {
				servicio.setFolio(constancia);
			}
			csDAO.actualizar(constancia);
			this.isSaved = true;
			this.habilitareporte = true;
			message = String.format("Constancia guardada correctamente con el folio %s", this.numero);
			severity = FacesMessage.SEVERITY_INFO;

		} catch (InventarioException ex) {
			log.error("Problema para obtener la información de los productos...", ex);
			message = ex.getMessage();
			severity = FacesMessage.SEVERITY_ERROR;
		} catch (Exception ex) {
			log.error("Problema para obtener el listado de servicios del cliente.", ex);
			ex.printStackTrace();
			message = "Problema con la información de servicios.";
			severity = FacesMessage.SEVERITY_ERROR;
		} finally {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, "Agregar servicio", message));
			PrimeFaces.current().ajax().update("form:messages", "form:dt-constanciaServicios");
		}
	}

	public void jasper() throws JRException, IOException, SQLException {
		String jasperPath = "/jasper/ejemplo1.jrxml";
		String filename = "Constancia_de_servicio.pdf";
		String images = "/images/logo.jpeg";
		String message = null;
		Severity severity = null;
		ConstanciaDeServicio constancia = null;
		List<ConstanciaDeServicio> alConstancias = null;
		alConstancias = csDAO.buscarPorFolioCliente(this.numero);
		File reportFile = new File(jasperPath);
		File imgfile = null;
		JasperReportUtil jasperReportUtil = new JasperReportUtil();
		ConstanciaDeServicio cds = new ConstanciaDeServicio();
		Map<String, Object> parameters = new HashMap<String, Object>();
		Connection connection = null;
		parameters = new HashMap<String, Object>();
		try {
			if (habilitareporte == false) {
				throw new Exception("Favor de guardar constancia");
			}
			URL resource = getClass().getResource(jasperPath);
			URL resourceimg = getClass().getResource(images);
			String file = resource.getFile();
			String img = resourceimg.getFile();
			reportFile = new File(file);
			imgfile = new File(img);
			log.info(reportFile.getPath());
			constancia = new ConstanciaDeServicio();
			constancia.setFolioCliente(this.numero);
			numero = String.valueOf(getNumero());
			connection = EntityManagerUtil.getConnection();
			parameters.put("REPORT_CONNECTION", connection);
			parameters.put("FOLIO", numero);
			parameters.put("LogoPath", imgfile.getPath());
			log.info("Parametros: " + parameters.toString());
			jasperReportUtil.createPdf(filename, parameters, reportFile.getPath());
		} catch (Exception ex) {
			ex.fillInStackTrace();
			log.error("Problema general...", ex);
			message = String.format("No se pudo imprimir el folio %s", this.numero);
			severity = FacesMessage.SEVERITY_INFO;
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(severity, "Error en impresion", message));
			PrimeFaces.current().ajax().update("form:messages", "form:dt-constanciaServicios");
		} finally {
			conexion.close((Connection) connection);
		}
	}

	public void reload() throws IOException {
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
	}

	public String getUnidadcobro() {
		return unidadcobro;
	}

	public void setUnidadcobro(String unidadcobro) {
		this.unidadcobro = unidadcobro;
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

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
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

	public boolean isHabilitareporte() {
		return habilitareporte;
	}

	public void setHabilitareporte(boolean habilitareporte) {
		this.habilitareporte = habilitareporte;
	}

	public List<EstadoConstancia> getEstados() {
		return estados;
	}

	public void setEstados(List<EstadoConstancia> estados) {
		this.estados = estados;
	}

	public List<ConstanciaDeDeposito> getListaconstanciadepo() {
		return listaconstanciadepo;
	}

	public void setListaconstanciadepo(List<ConstanciaDeDeposito> listaconstanciadepo) {
		this.listaconstanciadepo = listaconstanciadepo;
	}

	public ConstanciaDeDeposito getCtecve() {
		return ctecve;
	}

	public void setCtecve(ConstanciaDeDeposito ctecve) {
		this.ctecve = ctecve;
	}

	public Integer getIdclavectedeposito() {
		return idclavectedeposito;
	}

	public void setIdclavectedeposito(Integer idclavectedeposito) {
		this.idclavectedeposito = idclavectedeposito;
	}

	public List<Partida> getPartida() {
		return partida;
	}

	public void setPartida(List<Partida> partida) {
		this.partida = partida;
	}

	public List<DetallePartida> getLdpartida() {
		return ldpartida;
	}

	public void setLdpartida(List<DetallePartida> ldpartida) {
		this.ldpartida = ldpartida;
	}

	public List<Inventario> getInventario() {
		return inventario;
	}

	public void setInventario(List<Inventario> inventario) {
		this.inventario = inventario;
	}

}
