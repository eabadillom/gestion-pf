package mx.com.ferbo.controller;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
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
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.primefaces.PrimeFaces;

import mx.com.ferbo.dao.CamaraDAO;
import mx.com.ferbo.dao.ClienteDAO;
import mx.com.ferbo.dao.ConstanciaServicioDAO;
import mx.com.ferbo.dao.ConstanciaTraspasoDAO;
import mx.com.ferbo.dao.EstadoConstanciaDAO;
import mx.com.ferbo.dao.InventarioDAO;
import mx.com.ferbo.dao.PartidaDAO;
import mx.com.ferbo.dao.PartidaServicioDAO;
import mx.com.ferbo.dao.PlantaDAO;
import mx.com.ferbo.dao.PosicionCamaraDAO;
import mx.com.ferbo.dao.ProductoDAO;
import mx.com.ferbo.dao.TraspasoPartidaDAO;
import mx.com.ferbo.dao.UnidadDeManejoDAO;
import mx.com.ferbo.dao.UnidadDeProductoDAO;
import mx.com.ferbo.dao.partidasAfectadasDAO;
import mx.com.ferbo.model.Camara;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.ConstanciaDeDeposito;
import mx.com.ferbo.model.ConstanciaServicioDetalle;
import mx.com.ferbo.model.ConstanciaTraspaso;
import mx.com.ferbo.model.DetallePartida;
import mx.com.ferbo.model.EstadoConstancia;
import mx.com.ferbo.model.Inventario;
import mx.com.ferbo.model.InventarioDetalle;
import mx.com.ferbo.model.Partida;
import mx.com.ferbo.model.PartidaServicio;
import mx.com.ferbo.model.PartidasAfectadas;
import mx.com.ferbo.model.Planta;
import mx.com.ferbo.model.Posicion;
import mx.com.ferbo.model.PrecioServicio;
import mx.com.ferbo.model.Producto;
import mx.com.ferbo.model.ProductoPorCliente;
import mx.com.ferbo.model.TraspasoPartida;
import mx.com.ferbo.model.TraspasoServicio;
import mx.com.ferbo.model.UnidadDeManejo;
import mx.com.ferbo.model.UnidadDeProducto;
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
	private List<TraspasoServicio> alServiciosDetalle;
	private List<PrecioServicio> alServicios;
	private List<ProductoPorCliente> alProductosFiltered;
	private List<UnidadDeManejo> alUnidades;
	private List<EstadoConstancia> estados = null;
	private List<ConstanciaDeDeposito> listaconstanciadepo;
	private List<Partida> partida;
	private List<DetallePartida> ldpartida;
	private List<InventarioDetalle> inventario;
	private List<InventarioDetalle> destino;
	private List<Planta> listaplanta;
	private List<Camara> listacamara;
	private List<Posicion> listaposicion;
	private List<TraspasoPartida> listaTraspasoPartida;

	
	private Date fecha;
	private String numero;
	private Integer cantidad;
	private Integer idUnidadManejo;
	private Integer idProducto;
	private BigDecimal peso;
	private BigDecimal valorDeclarado;
	private String observaciones;
	private String unidadcobro;
	private UnidadDeManejo selUnidadManejo;
	private Integer idPrecioServicio;
	private BigDecimal cantidadServicio;
	private Integer idCliente;
	private Integer idclavectedeposito;
	private InventarioDetalle selectedInventario;
	private Integer planta;
	private Integer camara;
	private Cliente selCliente;
	private ConstanciaDeDeposito ctecve;
	private PartidaServicio selPartida;
	private ConstanciaServicioDetalle selServicio;
	private TraspasoPartida tp;
	
	private UnidadDeManejoDAO udmDAO;
	private EstadoConstanciaDAO edoDAO;
	private ClienteDAO clienteDAO;
	private PartidaServicioDAO partidaservicioDAO;
	private PartidaDAO partidaDAO;
	private InventarioDAO inventarioDAO;
	private PlantaDAO plantaDAO;
	private CamaraDAO camaraDAO;
	private PosicionCamaraDAO posicionDAO;
	private TraspasoPartidaDAO tpDAO;
	private ConstanciaTraspasoDAO constanciaTDAO;
	private UnidadDeProductoDAO udpDAO;
	private ProductoDAO prDAO;
	private partidasAfectadasDAO partidasAfectadasDAO;
	
	private boolean isSaved = false;
	private boolean habilitareporte = false;

	public AltaTraspasoBean() {
		log.info("Entrando al constructor del controller...");
		partidaservicioDAO = new PartidaServicioDAO();
		clienteDAO = new ClienteDAO();
		udmDAO = new UnidadDeManejoDAO();
		edoDAO = new EstadoConstanciaDAO();
		partidaDAO = new PartidaDAO();
		inventarioDAO = new InventarioDAO();
		plantaDAO = new PlantaDAO();
		camaraDAO = new CamaraDAO();
		constanciaTDAO = new ConstanciaTraspasoDAO();
		posicionDAO = new PosicionCamaraDAO();
		tpDAO = new TraspasoPartidaDAO();
		udpDAO = new UnidadDeProductoDAO();
		prDAO = new ProductoDAO();
		partidasAfectadasDAO = new partidasAfectadasDAO();
		clientes = new ArrayList<Cliente>();
		partida = new ArrayList<Partida>();
		ldpartida = new ArrayList<DetallePartida>();
		inventario = new ArrayList<InventarioDetalle>();
		alServiciosDetalle = new ArrayList<TraspasoServicio>();
		alServicios = new ArrayList<PrecioServicio>();
		alUnidades = new ArrayList<UnidadDeManejo>();
		alProductosFiltered = new ArrayList<ProductoPorCliente>();
		listaconstanciadepo = new ArrayList<ConstanciaDeDeposito>();
		listaTraspasoPartida = new ArrayList<TraspasoPartida>();
		destino = new ArrayList<InventarioDetalle>();
		selCliente = new Cliente();
		alPartidas = partidaservicioDAO.findall();
		clientes = clienteDAO.findall();
		partida = partidaDAO.findall();
		listaplanta = plantaDAO.findall();
		listacamara = camaraDAO.findall();
		listaposicion = posicionDAO.findAll();
		listaTraspasoPartida = tpDAO.findall();
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
		tp = new TraspasoPartida();
		
	}

	public void filtrarCliente() {
		String message = null;
		Severity severity = null;
		EntityManager manager = null;
		Cliente cliente = null;
		
		Map<Integer, List<PrecioServicio>> mpPrecioServicio = new HashMap<Integer, List<PrecioServicio>>();
		List<PrecioServicio> precioServicioList = null;
		this.selCliente = clienteDAO.buscarPorId(idCliente);
		List<Inventario> inventario_antes = inventarioDAO.buscarPorCliente(selCliente);
	try {
			for( Inventario i : inventario_antes) {
				InventarioDetalle invdet = new InventarioDetalle(i);
				invdet.setListaplanta(listaplanta);
				invdet.setListacamara(listacamara);
				invdet.setListaposicion(listaposicion);
				inventario.add(invdet);
			}
			log.info("Entrando a filtrar cliente...");
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
			message = "Agregue los servicios requeridos.";
			severity = FacesMessage.SEVERITY_INFO;
		} catch (Exception ex) {
			log.error("Problema para recuperar los datos del cliente.", ex);
			message = ex.getMessage();
			severity = FacesMessage.SEVERITY_ERROR;
		} finally {
			if (manager != null)
				manager.close();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, "Cliente", message));
			PrimeFaces.current().ajax().update("form:messages", "form:destino");
		}
		log.info("Servicios del cliente filtrados.");
	}
		
		public void agrega() {
			System.out.println(this.selectedInventario);
			TraspasoPartida traspasoP = new TraspasoPartida();
			Inventario inventario = new Inventario();
			
			if(tp != null) {
				traspasoP.setCantidad(this.selectedInventario.getCantidad());
				traspasoP.setDescripcion(this.selectedInventario.getProducto().getProductoDs());
				traspasoP.setOrigen(this.selectedInventario.getPlanta().getPlantaDs() + " " + this.selectedInventario.getCamara().getCamaraDs());		
				traspasoP.setDestino(this.selectedInventario.getPlantaDestino().getPlantaDs() + " " + this.selectedInventario.getCamaraDestino().getCamaraDs());
				inventario.setProducto(this.selectedInventario.getProducto());
				inventario.setCamara(this.selectedInventario.getCamara());
				destino.add(selectedInventario);
				listaTraspasoPartida.add(tp);
				tp = new TraspasoPartida();
			}
				
		}
		

	public void agregarServicio() {
		String message = null;
		Severity severity = null;
		PrecioServicio precioServicio = null;
		TraspasoServicio servicio = null;

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
				alServiciosDetalle = new ArrayList<TraspasoServicio>();

			servicio = new TraspasoServicio();
			servicio.setCantidad(this.cantidadServicio);
			servicio.setServicio(precioServicio.getServicio().getServicioDs());
			servicio.setPrecio(precioServicio.getPrecio());
			servicio.setSubtotal(precioServicio.getPrecio());
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
			PrimeFaces.current().ajax().update("form:messages", "form:dt-altaTraspaso");
		}
	}
	
	public void plantaselect() {
		Planta plantaDestino = this.selectedInventario.getPlantaDestino();
		List<Camara> listaCamarasDestino = camaraDAO.buscarPorPlanta(plantaDestino);
		selectedInventario.setListacamara(listaCamarasDestino);
	}
	public void camaraselect() {
			Camara camD = this.selectedInventario.getCamaraDestino();
			List<Posicion> listaposicionDestino = posicionDAO.buscarPorCamara(camD);
			selectedInventario.setListaposicion(listaposicionDestino);
		}

	public synchronized void guardar() {
		String message = null;
		Severity severity = null;
		ConstanciaTraspaso constancia = null;
		List<ConstanciaTraspaso> alConstancias = null;
		EstadoConstancia estado = null;
		
		try {
			if (this.isSaved)
				throw new InventarioException("La constancia ya se encuentra registrada.");

			if (this.numero == null || "".equalsIgnoreCase(this.numero.trim()))
				throw new InventarioException("Debe indicar el folio de la constancia.");

			if (this.destino == null || this.destino.size() == 0)
				throw new InventarioException("Debe traspasar almenos un producto");

			alConstancias = constanciaTDAO.buscarporNumero(this.numero);

			if (alConstancias != null && alConstancias.size() > 0)
				throw new InventarioException(String.format("El folio %s ya se encuentra registrado.", this.numero));

			estado = estados.stream().filter(e -> e.getEdoCve() == 1).collect(Collectors.toList()).get(0);
			constancia = new ConstanciaTraspaso();
			constancia.setFecha(this.fecha);
			constancia.setNumero(this.numero);
			constancia.setCliente(this.selCliente);
			constancia.setObservacion(this.observaciones);
			constancia.setNombreCliente(this.selCliente.getCteNombre());
			List<TraspasoPartida> listaTraspasoPartida = new ArrayList<TraspasoPartida>();
			List<PartidasAfectadas> listaPartidasAfectadas = new ArrayList<PartidasAfectadas>();

			
			for(InventarioDetalle i : destino ) {
				System.out.println(i);
				TraspasoPartida partida = new TraspasoPartida();
				partida.setTraspaso(constancia);
				partida.setConstancia(i.getFolioCliente());
				Partida p = partidaDAO.buscarPorId(i.getPartidaCve());
				partida.setPartida(p);
				UnidadDeProducto udp = p.getUnidadDeProductoCve();
				Producto pr = udp.getProductoCve();
				partida.setDescripcion(pr.getProductoDs());
				partida.setCantidad(i.getCantidad());
				partida.setOrigen(i.getCamara().getCamaraDs());
				partida.setDestino(i.getCamaraDestino().getCamaraDs());
				p.setCamaraCve(i.getCamaraDestino());
				listaTraspasoPartida.add(partida);
				PartidasAfectadas pa = new PartidasAfectadas();
				pa.setPartidatraspaso(partida);
				pa.setPartida(p);
				listaPartidasAfectadas.add(pa);
			}
			for(TraspasoServicio servicio : alServiciosDetalle) {
				servicio.setTraspaso(constancia);
			}
			constancia.setTraspasoServicioList(alServiciosDetalle);
			constancia.setTraspasoPartidaList(listaTraspasoPartida);
			constanciaTDAO.actualizar(constancia);
			for(PartidasAfectadas pa : listaPartidasAfectadas ) {
				partidasAfectadasDAO.actualizar(pa);
			}
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
			PrimeFaces.current().ajax().update("form:messages", "form:dt-altaTraspaso");
		}
	}

	public void jasper() throws JRException, IOException, SQLException {
		String jasperPath = "/jasper/ReporteTraspaso.jrxml";
		String filename = "Constancia_de_traspaso.pdf";
		String images = "/images/logo.jpeg";
		String message = null;
		Severity severity = null;
		ConstanciaTraspaso constancia = null;
		List<ConstanciaTraspaso> alConstancias = null;
		alConstancias = constanciaTDAO.buscarporNumero(this.numero);
		File reportFile = new File(jasperPath);
		File imgfile = null;
		JasperReportUtil jasperReportUtil = new JasperReportUtil();
		ConstanciaTraspaso cds = new ConstanciaTraspaso();
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
			constancia = new ConstanciaTraspaso();
			constancia.setNumero(this.numero);
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

		public void deleteServicio(TraspasoServicio servicio) {
		this.alServiciosDetalle.remove(servicio);
	}
		public void deletePartida(InventarioDetalle partida) {
			this.destino.remove(partida);
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

	public List<TraspasoServicio> getAlServiciosDetalle() {
		return alServiciosDetalle;
	}

	public void setAlServiciosDetalle(List<TraspasoServicio> alServiciosDetalle) {
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

	public List<InventarioDetalle> getInventario() {
		return inventario;
	}

	public void setInventario(List<InventarioDetalle> inventario) {
		this.inventario = inventario;
	}

	public List<Planta> getListaplanta() {
		return listaplanta;
	}

	public void setListaplanta(List<Planta> listaplanta) {
		this.listaplanta = listaplanta;
	}

	public List<Camara> getListacamara() {
		return listacamara;
	}

	public void setListacamara(List<Camara> listacamara) {
		this.listacamara = listacamara;
	}

	public List<Posicion> getListaposicion() {
		return listaposicion;
	}

	public void setListaposicion(List<Posicion> listaposicion) {
		this.listaposicion = listaposicion;
	}

	public InventarioDetalle getSelectedInventario() {
		return selectedInventario;
	}

	public void setSelectedInventario(InventarioDetalle selectedInventario) {
		this.selectedInventario = selectedInventario;
	}

	public Integer getPlanta() {
		return planta;
	}

	public void setPlanta(Integer planta) {
		this.planta = planta;
	}

	public Integer getCamara() {
		return camara;
	}

	public void setCamara(Integer camara) {
		this.camara = camara;
	}


	public TraspasoPartida getTp() {
		return tp;
	}

	public void setTp(TraspasoPartida tp) {
		this.tp = tp;
	}

	public List<InventarioDetalle> getDestino() {
		return destino;
	}

	public void setDestino(List<InventarioDetalle> destino) {
		this.destino = destino;
	}

	


}
