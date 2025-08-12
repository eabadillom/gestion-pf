package mx.com.ferbo.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.component.UISelectItems;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import mx.com.ferbo.dao.AvisoDAO;
import mx.com.ferbo.dao.CamaraDAO;
import mx.com.ferbo.dao.ClienteDAO;
import mx.com.ferbo.dao.ConstanciaDeDepositoDAO;
import mx.com.ferbo.dao.EstadoConstanciaDAO;
import mx.com.ferbo.dao.EstadoInventarioDAO;
import mx.com.ferbo.dao.PlantaDAO;
import mx.com.ferbo.dao.PosicionCamaraDAO;
import mx.com.ferbo.dao.PrecioServicioDAO;
import mx.com.ferbo.dao.ProductoClienteDAO;
import mx.com.ferbo.dao.ProductoDAO;
import mx.com.ferbo.dao.SerieConstanciaDAO;
import mx.com.ferbo.dao.ServicioDAO;
import mx.com.ferbo.dao.TipoMovimientoDAO;
import mx.com.ferbo.dao.UnidadDeManejoDAO;
import mx.com.ferbo.dao.UnidadDeProductoDAO;
import mx.com.ferbo.model.Aviso;
import mx.com.ferbo.model.Camara;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.ConstanciaDeDeposito;
import mx.com.ferbo.model.ConstanciaDepositoDetalle;
import mx.com.ferbo.model.DetallePartida;
import mx.com.ferbo.model.EstadoConstancia;
import mx.com.ferbo.model.EstadoInventario;
import mx.com.ferbo.model.Partida;
import mx.com.ferbo.model.Planta;
import mx.com.ferbo.model.Posicion;
import mx.com.ferbo.model.PrecioServicio;
import mx.com.ferbo.model.Producto;
import mx.com.ferbo.model.ProductoPorCliente;
import mx.com.ferbo.model.SerieConstancia;
import mx.com.ferbo.model.SerieConstanciaPK;
import mx.com.ferbo.model.Servicio;
import mx.com.ferbo.model.Tarima;
import mx.com.ferbo.model.TipoMovimiento;
import mx.com.ferbo.model.UnidadDeManejo;
import mx.com.ferbo.model.UnidadDeProducto;
import mx.com.ferbo.model.Usuario;
import mx.com.ferbo.util.EntityManagerUtil;
import mx.com.ferbo.util.InventarioException;
import mx.com.ferbo.util.JasperReportUtil;
import net.sf.jasperreports.engine.JRException;

@Named(value = "entradaBean")
@ViewScoped
public class ConstanciaDeDepositoBean implements Serializable {

	private static final long serialVersionUID = -1785488265380235016L;
	private static Logger log = LogManager.getLogger(ConstanciaDeDepositoBean.class);
	
	private ClienteDAO clienteDAO;
	private PlantaDAO plantaDAO;
	private AvisoDAO avisoDAO;
	private CamaraDAO camaraDAO;
	private ConstanciaDeDepositoDAO constanciaDAO;
	private ProductoClienteDAO productoClienteDAO;
	private ProductoDAO productoDAO;
	private UnidadDeManejoDAO unidadDeManejoDAO;
	private PosicionCamaraDAO posicionCamaraDAO;
	private ServicioDAO servicioDAO;
	private PrecioServicioDAO precioServicioDAO;
	private UnidadDeProductoDAO unidadDeProductoDAO;
	private EstadoConstanciaDAO estadoConstanciaDAO;
	private TipoMovimientoDAO tipoMovimientoDAO;
	private EstadoInventario estadoInventario;
	private EstadoInventarioDAO estadoInventarioDAO;
	private SerieConstanciaDAO serieConstanciaDAO;
	private BigDecimal numTarimas;
	private Boolean restricted = null;
	private Boolean saved = null;
	private Integer idTarima = null;
	
	private List<Cliente> listadoCliente;
	private List<Planta> listadoPlanta;
	private List<Camara> camaras;
	private List<Camara> camaraPorPlanta;
	private List<ConstanciaDeDeposito> listadoConstancia;
	private List<ProductoPorCliente> productoCliente;
	private List<Producto> listadoProducto;// nueva
	private List<Producto> productos;
	private List<UnidadDeManejo> listadoUnidadDeManejo;
	private List<Posicion> listaPosiciones;
	private List<Posicion> posiciones;
	private List<Aviso> avisoPorCliente;
	private List<Aviso> avisos;
	private List<Partida> listadoPartida;
	private List<DetallePartida> listadoDetallePartida;
	private List<Servicio> listadoServicio;
	private List<PrecioServicio> listadoPrecioServicio;//
	private List<PrecioServicio> listaServicioUnidad;
	private List<ConstanciaDepositoDetalle> listadoConstanciaDepositoDetalle;
	private List<Partida> selectedPartidas;
	private List<Tarima> tarimas;

	private Tarima tarima;
	private Planta plantaSelect;
	private Cliente clienteSelect;
	private ProductoPorCliente productoPorCliente;// nueva
	private Camara camaraSelect;
	private Producto producto;
	private Integer productoSelect;
	private UISelectItems productoItems;
	private UnidadDeManejo unidadDeManejoSelect;
	private Posicion posicionCamaraSelect;
	private Aviso avisoSelect;
	private Aviso aviso;
	private PrecioServicio precioServicioSelect;
	private ConstanciaDepositoDetalle selectedConstanciasDD;
	private Partida selectedPartida;
	private Partida partida;
	private Partida partidaEdit;
	private DetallePartida detalle;
	private TipoMovimiento tipoMovimiento;
	
	private String folioCliente;
	private BigDecimal unidadesPorTarima;
	private BigDecimal cantidadTotal;
	private BigDecimal pesoTotal;
	private BigDecimal totalTarimas;
	private BigDecimal valorMercancia;
	private BigDecimal totalKilos;
	private BigDecimal totalCajas;
	private String pedimento, contenedor, lote, otro;
	private Boolean isCongelacion, isConservacion, isRefrigeracion, isManiobras;
	private int congelacion = 619, conservacion = 620, refrigeracion = 621, maniobras = 622 ;
	private boolean validaCarga;
	private boolean showCodigo;
	private boolean showPedimento;
	private boolean showSAP;
	private boolean showLote;
	private boolean showCaducidad;
	private boolean showOtro;
	private BigDecimal cantidadServicio;
	private Boolean respuesta;
	private String temperatura;
	private BigDecimal metroCamara;
	private String nombreTransportista;
	private String placas;
	private String observacion;
	private ConstanciaDeDeposito constanciaDeDeposito;
	private List<ConstanciaDepositoDetalle> selectedConstanciaDD;

	private Date fechaCaducidad;
	private Date fechaIngreso;
	private Date maxDate;
	private SerieConstancia serie;
	private Usuario usuario;
	
	private StreamedContent file;
	
	private FacesContext context;
    private HttpServletRequest request;
    
	@SuppressWarnings("unchecked")
	public ConstanciaDeDepositoBean() {
		log.info("Entrando a constructor...");
		clienteDAO = new ClienteDAO();
		plantaDAO = new PlantaDAO();
		camaraDAO = new CamaraDAO();
		avisoDAO = new AvisoDAO();
		constanciaDAO = new ConstanciaDeDepositoDAO();
		productoClienteDAO = new ProductoClienteDAO();// nueva
		productoDAO = new ProductoDAO();
		unidadDeManejoDAO = new UnidadDeManejoDAO();
		posicionCamaraDAO = new PosicionCamaraDAO();
		servicioDAO = new ServicioDAO();
		precioServicioDAO = new PrecioServicioDAO();
		unidadDeProductoDAO = new UnidadDeProductoDAO();
		tipoMovimientoDAO = new TipoMovimientoDAO();
		estadoInventarioDAO = new EstadoInventarioDAO();
		serieConstanciaDAO = new SerieConstanciaDAO();
		estadoConstanciaDAO = new EstadoConstanciaDAO();

		listadoPlanta = new ArrayList<>();
		
		camaraPorPlanta = new ArrayList<Camara>();
		productos = new ArrayList<Producto>();
		posiciones = new ArrayList<Posicion>();
		listadoPartida = new ArrayList<Partida>();
		listadoUnidadDeManejo = new ArrayList<>();
		avisoPorCliente = new ArrayList<Aviso>();
		listadoDetallePartida = new ArrayList<>();
		listadoPrecioServicio = new ArrayList<>();//
		listaServicioUnidad = new ArrayList<PrecioServicio>();//
		listadoConstanciaDepositoDetalle = new ArrayList<ConstanciaDepositoDetalle>();
		selectedPartidas = new ArrayList<Partida>();
		selectedConstanciaDD = new ArrayList<>();
		totalTarimas = new BigDecimal(0);
		tarimas = new ArrayList<>();
		
		
		Planta planta = null;
		byte bytes[] = {};
		
		try {
			this.context = FacesContext.getCurrentInstance();
			this.request = (HttpServletRequest) context.getExternalContext().getRequest();
			listadoCliente = (List<Cliente>) request.getSession(false).getAttribute("clientesActivosList");
			partidaEdit = new Partida();
			context = FacesContext.getCurrentInstance();
	        request = (HttpServletRequest) context.getExternalContext().getRequest();
	        this.usuario = (Usuario) request.getSession(true).getAttribute("usuario");
	        
	        this.setRestrictedAccess();
			
			if(restricted) {
				log.info("Estableciendo solo la planta para el usuario de almacen...");
				planta = plantaDAO.buscarPorId(usuario.getIdPlanta());
				listadoPlanta = new ArrayList<Planta>();
				listadoPlanta.add(planta);
				plantaSelect = planta;
				this.filtraCamaras();
			} else {
				log.info("Estableciendo todo el listado de plantas para usuarios de administración...");
				listadoPlanta = plantaDAO.findall();
			}
				 
			
			
			
			this.listadoUnidadDeManejo = unidadDeManejoDAO.buscarTodos();
			tipoMovimiento = tipoMovimientoDAO.buscarPorId(1);
			estadoInventario = estadoInventarioDAO.buscarPorId(1);

			constanciaDeDeposito = new ConstanciaDeDeposito();
			this.resetPartida();

			cantidadTotal = null;
			numTarimas = null;

			isCongelacion = false;
			isConservacion = false;
			isRefrigeracion = false;
			isManiobras = false;
			fechaIngreso = new Date();

			showCodigo = false;
			showCaducidad = false;
			showLote = false;
			showOtro = false;
			showPedimento = false;
			showSAP = false;
			
			saved = false;
			Date today = new Date();

			maxDate = new Date(today.getTime() );
			this.file = DefaultStreamedContent.builder().contentType("application/pdf").contentLength(bytes.length)
					.name("ticket.pdf").stream(() -> new ByteArrayInputStream(bytes)).build();
			
		} catch(Exception ex) {
			log.error("Problema al ejecutar el proceso PostConstruct...", ex);
		} finally {
			PrimeFaces.current().ajax().update("form:planta", "form:numeroC", "form:cmdCambiarFolio");
		}
		
		log.info("Terminando constructor.");
	}

	public void totalesTarimas() {
		log.info("Ejecutando totalTarimas...");
		try {
			Integer sumaTotalCajas = null;
			
			totalKilos = listadoPartida.stream()
					.map(item -> item.getPesoTotal())
					.reduce(BigDecimal.ZERO, BigDecimal::add)
					;
			
			sumaTotalCajas = listadoPartida.stream()
					.mapToInt(Partida::getCantidadTotal)
					.sum()
					;
			
			totalCajas = new BigDecimal(sumaTotalCajas).setScale(0, BigDecimal.ROUND_HALF_UP);
			log.info("Terminado totalTarimas.");
		} catch(Exception ex) {
			log.error("Problema al ejecutar totalTarimas...", ex);
		}
	}
	
	public BigDecimal TotalTarimas(List<Partida> lista) {
		BigDecimal subTotal = new BigDecimal(0);
		try {
			for(Partida p: listadoPartida) {
				subTotal = subTotal.add(p.getNoTarimas());
			}
			log.info("Total tarimas: {}", subTotal);
		} catch(Exception ex) {
			log.error("Problema para obtener el total de tarimas: {}", ex.getMessage());
		}
		
		return subTotal;
	}
	
	public BigDecimal TotalKilos(List<Partida> lista) {
		BigDecimal subTotal = new BigDecimal(0);
		try {
			for(Partida p: listadoPartida) {
				subTotal = subTotal.add(p.getPesoTotal());
			}
			log.info("Total kg: {}", subTotal);
		} catch(Exception ex) {
			log.error("Problema para obtener el total de kg: {}", ex.getMessage());
		}
		return subTotal;
	}
	
	public BigDecimal TotalCajas(List<Partida> lista) {
		BigDecimal subTotal = new BigDecimal(0);
		try {
			for(Partida p: listadoPartida) {
				subTotal = subTotal.add(new BigDecimal(p.getCantidadTotal()));
				
			}
			log.info("Total de unidades: {}", subTotal);
		} catch(Exception ex) {
			log.error("Problema para obtener el total de unidades: {}", ex.getMessage());
		}
		return subTotal;
	}
	
	
	
	private void setRestrictedAccess() {
		try {
			if(usuario.getPerfil() == 1 || usuario.getPerfil() == 4)
				restricted = true;
			else
				restricted = false;
		} catch(Exception ex) {
			log.error("Problema para obtener el estado restricted...", ex);
		}
	}
	
	public void cargaInfoCliente() {
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;

		try {
			log.info("Iniciando carga de información del cliente...");
			this.generaFolioEntrada();

			productos.clear();
			productoPorCliente = new ProductoPorCliente(clienteSelect);
			productoCliente = productoClienteDAO.buscarPorCliente(clienteSelect.getCteCve(), true);
			for (ProductoPorCliente pc : productoCliente) {
				Producto p = pc.getProductoCve();
				productos.add(p);
			}

			avisoPorCliente = avisoDAO.buscarPorCliente(clienteSelect.getCteCve());
			avisoSelect = null;
			
			
			avisos = avisoDAO.buscarPorCliente(clienteSelect.getCteCve());
			aviso = null;
			
			log.info("Terminando carga de información del cliente.");
			mensaje = "Seleccione un aviso";
			severity = FacesMessage.SEVERITY_INFO;
		} catch (Exception ex) {
			log.error("Problema al extraer información del cliente...", ex);
			mensaje = "Ha ocurrido un error en el sistema. Intente nuevamente.\nSi el problema persiste, por favor comuniquese con su administrador del sistema.";
			severity = FacesMessage.SEVERITY_ERROR;
		} finally {
			message = new FacesMessage(severity, "Aviso", mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
			PrimeFaces.current().ajax().update("form:messages");
		}
	}
	
	public void generaFolioEntrada()
	throws InventarioException {
		SerieConstanciaPK seriePK = null;
		SerieConstancia serie = null;
		SerieConstancia sConstancia = null;
//		FacesMessage message = null;
//		Severity severity = null;
//		String mensaje = null;

		log.info("Generando folio de entrada...");
		if (this.clienteSelect == null)
			return;
//			throw new InventarioException("Debe seleccionar un cliente");
		
		if (this.plantaSelect == null)
			return;
//			throw new InventarioException("Debe seleccionar una planta");
		sConstancia = new SerieConstancia();
		seriePK = new SerieConstanciaPK();
		seriePK.setCliente(this.clienteSelect);
		seriePK.setPlanta(plantaSelect);
		seriePK.setTpSerie("I");
		sConstancia.setSerieConstanciaPK(seriePK);
		serie = serieConstanciaDAO.buscarPorClienteAndPlanta(sConstancia);
		
		if (serie == null) {
			this.folioCliente = "";
			throw new InventarioException(
					"No se encontró información de los folios del cliente. Debe indicar manualmente un folio de constancia.");
		}
		
		this.folioCliente = String.format("%s%s%s%d", serie.getSerieConstanciaPK().getTpSerie(), plantaSelect.getPlantaSufijo(),
				clienteSelect.getCodUnico(), serie.getNuSerie());
		
		this.constanciaDeDeposito.setFolioCliente(this.folioCliente);
		this.serie = serie;
		log.info("Folio de entrada generado.");
//		try {
//		} catch (InventarioException ex) {
//			mensaje = ex.getMessage();
//			severity = FacesMessage.SEVERITY_WARN;
//			log.warn(mensaje);
//			message = new FacesMessage(severity, "Aviso", mensaje);
//			FacesContext.getCurrentInstance().addMessage(null, message);
//		} catch (Exception ex) {
//			log.error("Problema para generar el folio de entrada...", ex);
//			mensaje = "Ha ocurrido un error en el sistema. Intente nuevamente.\nSi el problema persiste, por favor comuniquese con su administrador del sistema.";
//			severity = FacesMessage.SEVERITY_ERROR;
//			
//			message = new FacesMessage(severity, "Aviso", mensaje);
//			FacesContext.getCurrentInstance().addMessage(null, message);
//		} finally {
//			PrimeFaces.current().ajax().update("form:messages", "form:numeroC");
//		}
	}

	

	public void filtrarPosicion() {
		try {
			log.info("Iniciando el filtrado de posiciones por camara...");
			
			Posicion posicion = new Posicion();
			posicion.setPlanta(plantaSelect);
			posicion.setCamara(camaraSelect);
			posiciones = posicionCamaraDAO.buscarPorCriterios(posicion);
			
			log.info("Termina el filtrado de posiciones por camara");
		} catch(Exception ex) {
			log.error("Problema con la funcion de filtrado de posiciones por camara...", ex);
		}
	}

	public Partida newPartida() {
		Partida partida = null;
		UnidadDeProducto udp = null;
		DetallePartida detalle = null;
		try {
			log.info("Generando nueva partida...");
			partida = new Partida();
			partida.setCantidadTotal(null);
			partida.setPesoTotal(null);
			partida.setNoTarimas(new BigDecimal("0").setScale(1, BigDecimal.ROUND_HALF_UP));
			udp = new UnidadDeProducto();
			udp.setUnidadDeManejoCve(new UnidadDeManejo());
			udp.setProductoCve(new Producto());
			partida.setUnidadDeProductoCve(udp);
			detalle = new DetallePartida(1, partida);
			detalle.setEdoInvCve(estadoInventario);
			detalle.setTipoMovCve(tipoMovimiento);
			partida.add(detalle);
			log.info("Nueva partida generada.");
		} catch(Exception ex) {
			log.error("Problema para generar una nueva partida...", ex);
		}

		return partida;

	}

	public void verDatosPartida() {
		log.info("Cantidad: " + partida.getCantidadTotal());
		log.info("ProductoCve: " + partida.getUnidadDeProductoCve().getProductoCve().getProductoCve());
		log.info("UnidadDeManejoCve: " + partida.getUnidadDeProductoCve().getUnidadDeManejoCve().getUnidadDeManejoCve());
		log.info("Peso: " + partida.getCantidadTotal());
		log.info("Num. Tarimas: " + partida.getNoTarimas());
		log.info("Folio: " + this.folioCliente);
	}

	public void addProducto(Producto producto) {
		log.info("Agregando producto...");
		this.productoSelect = producto.getProductoCve();
		log.info("Producto agregado.");
	}

	public void verProducto() {
		log.info("ProductoCve: " + productoSelect);
	}

	public void agregaServicio() {
		ConstanciaDepositoDetalle constanciaDD = new ConstanciaDepositoDetalle();
		
		try {
			log.info("Agregando un servicio a la entrada...");
			constanciaDD.setServicioCve(precioServicioSelect.getServicio());
			constanciaDD.setFolio(constanciaDeDeposito);
			constanciaDD.setServicioCantidad(cantidadServicio);
			listadoConstanciaDepositoDetalle.add(constanciaDD);
			
			this.precioServicioSelect = null;
			this.cantidadServicio = null;
			
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Agregado", "Se agrego el registro correctamente"));
			PrimeFaces.current().ajax().update("form:messages");
			log.info("Nuevo servicio agregado a la entrada.");
		} catch(Exception ex) {
			log.error("Problema para agregar un servicio a la entrada...", ex);
		}
	}
	
	public void loadAviso() {
		log.info("Aviso seleccionado: {}", this.aviso);
		
		this.showCodigo    = aviso.getAvisoCodigo();
		this.showPedimento = aviso.getAvisoPedimento();
		this.showSAP       = aviso.getAvisoSap();
		this.showLote      = aviso.getAvisoLote();
		this.showCaducidad = aviso.getAvisoCaducidad();
		this.showOtro      = aviso.getAvisoOtro();
		
		PrecioServicio psCongelacion = precioServicioDAO.buscar(this.clienteSelect.getCteCve(), this.aviso.getAvisoCve(), this.congelacion, true);;
		PrecioServicio psConservacion = precioServicioDAO.buscar(this.clienteSelect.getCteCve(), this.aviso.getAvisoCve(), this.conservacion, true);;
		PrecioServicio psRefrigeracion = precioServicioDAO.buscar(this.clienteSelect.getCteCve(), this.aviso.getAvisoCve(), this.refrigeracion, true);
		PrecioServicio psManiobras = precioServicioDAO.buscar(this.clienteSelect.getCteCve(), this.aviso.getAvisoCve(), this.maniobras, true);
		
		this.listaServicioUnidad = this.precioServicioDAO.buscarPorAviso(this.aviso, this.clienteSelect);
		
		if(psCongelacion == null)
			this.isCongelacion = false;
		else {
			this.isCongelacion = true;
			this.listaServicioUnidad.remove(psCongelacion);
		}
		
		if(psConservacion == null)
			this.isConservacion = false;
		else {
			this.isConservacion = true;
			this.listaServicioUnidad.remove(psConservacion);
		}
		
		if(psRefrigeracion == null)
			this.isRefrigeracion = false;
		else {
			this.isRefrigeracion = true;
			this.listaServicioUnidad.remove(psRefrigeracion);
		}
		
		if(psManiobras == null)
			this.isManiobras = false;
		else {
			this.isManiobras = true;
			this.listaServicioUnidad.remove(psManiobras);			
		}
		log.info("Aviso cargado.");
	}

	public void addTarima() {
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		
		Tarima tarima = null;
		
		try {
			log.info("Agregando tarima...");
			if(this.numTarimas.compareTo(BigDecimal.ONE) < 0)
				throw new InventarioException("El número de tarimas indicado es incorrecto.");
			
			if(   this.partida.getUnidadDeProductoCve().getProductoCve() != null
			   && this.partida.getUnidadDeProductoCve().getProductoCve().getProductoCve() != null) {
				this.addPartida();
				return;
			}
			
			if(this.idTarima == null)
				this.idTarima = new Integer(0);
			
			if(this.tarimas == null)
				this.tarimas = new ArrayList<Tarima>();
			
			for(int i = 0; i < this.numTarimas.intValue(); i++) {
				
				tarima = new Tarima();
				tarima.setId(idTarima++);
				tarima.setNombre(String.format("%s-%s", this.constanciaDeDeposito.getFolioCliente(), idTarima));
				tarima.setPartidas(new ArrayList<Partida>());
				this.tarimas.add(tarima);	
			}
			
			this.resetPartida();
			
			severity = FacesMessage.SEVERITY_INFO;
			mensaje = "Agregado correctamente";
			PrimeFaces.current().executeScript("PF('noTarimasDlg').hide()");
			
			message = new FacesMessage(severity, "Tarima", mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
			PrimeFaces.current().ajax().update("form:messages", "form:seleccion-mercancia", "form:numTarimas", "form:id-validaCarga", "form:totalTarimas","form:totalCajas","form:totalKilos", "form:dt-tarimas");
			log.info("Tarima agregada.");
		} catch (InventarioException ex) {
			mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_ERROR;
			log.warn(mensaje);
			
			message = new FacesMessage(severity, "Tarima", mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
			PrimeFaces.current().ajax().update("form:messages", "form:seleccion-mercancia", "form:numTarimas", "form:id-validaCarga", "form:totalTarimas","form:totalCajas","form:totalKilos", "form:dt-tarimas");
		} catch (Exception ex) {
			log.error("Problema al agregar una nueva partida...", ex);
			mensaje = "Ha ocurrido un error en el sistema. Intente nuevamente.\nSi el problema persiste, por favor comuniquese con su administrador del sistema.";
			severity = FacesMessage.SEVERITY_ERROR;
			
			message = new FacesMessage(severity, "Tarima", mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
			PrimeFaces.current().ajax().update("form:messages", "form:seleccion-mercancia", "form:numTarimas", "form:id-validaCarga", "form:totalTarimas","form:totalCajas","form:totalKilos", "form:dt-tarimas");
		}
	}
	
	public void addPartida() {
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;

		UnidadDeProducto udp = null;
		UnidadDeManejo udm = null;
		Integer idUnidadDeManejo = null;
		Producto prd = null;
		Integer idProducto = null;
		
		Partida p = null;
//		DetallePartida dp = null;
		Integer intNumTarimas = null;
		BigDecimal parcialTarima = null;
		
		Tarima tarima = null;

		try {
			log.info("Agregando una nueva partida...");
			udp = partida.getUnidadDeProductoCve();

			if (udp == null)
				throw new InventarioException("La selección de producto o unidad de manejo es incorrecta.");

			udm = udp.getUnidadDeManejoCve();
			if (udm == null)
				throw new InventarioException("Debe seleccionar una unidad de manejo.");
			
			if(udm.getUnidadDeManejoCve() == null)
				throw new InventarioException("Debe seleccionar una unidad de manejo.");

			prd = udp.getProductoCve();
			if (prd == null)
				throw new InventarioException("Debe seleccionar un producto.");
			
			if(prd.getProductoCve() == null)
				throw new InventarioException("Debe seleccionar un producto.");
			
			if(this.partida.getCantidadTotal() == null || this.partida.getCantidadTotal() == 0)
				throw new InventarioException("Debe indicar una cantidad.");
			
			if(this.partida.getPesoTotal() == null || this.partida.getPesoTotal().compareTo(BigDecimal.ZERO) == 0)
				throw new InventarioException("Debe indicar un peso.");

		
			idUnidadDeManejo = partida.getUnidadDeProductoCve().getUnidadDeManejoCve().getUnidadDeManejoCve();
			idProducto = partida.getUnidadDeProductoCve().getProductoCve().getProductoCve();
			prd = productoDAO.buscarPorId(idProducto);
			udm = unidadDeManejoDAO.buscarPorId(idUnidadDeManejo);
			udp = unidadDeProductoDAO.buscarPorProductoUnidad(idProducto, idUnidadDeManejo);

			if (udp == null) {
				log.info(String.format(
						"No se encontró la unidad de producto. Se registrará una nueva: producto(%d) - unidad(%d)",
						prd.getProductoCve(), udm.getUnidadDeManejoCve()));
				udp = new UnidadDeProducto();
				udp.setProductoCve(prd);
				udp.setUnidadDeManejoCve(udm);
				unidadDeProductoDAO.guardar(udp);
			}
			
			partida.setUnidadDeProductoCve(udp);
			partida.setFolio(constanciaDeDeposito);
			partida.setUnidadDeCobro(udm);
			partida.setCantidadDeCobro(partida.getPesoTotal());
			partida.setCamaraCve(this.camaraSelect);
			partida.setNoTarimas(null);
			partida.getDetallePartidaList().get(0).setCantidadUManejo(partida.getCantidadTotal());
			partida.getDetallePartidaList().get(0).setCantidadUMedida(partida.getPesoTotal());
			
			if(this.tarima != null) {
				p = (Partida) partida.clone();
				p.setNoTarimas(null);
				p.setTarima(this.tarima);
				
				this.tarima.getPartidas().add(p);
				this.listadoPartida.add(p);
				
				this.totalesTarimas();
				this.resetPartida();
				
				severity = FacesMessage.SEVERITY_INFO;
				mensaje = "Agregado a la tarima " + this.tarima.getNombre();
				log.info("Producto {} agregado a la tarima {}", p.getUnidadDeProductoCve().getProductoCve().getProductoDs(), this.tarima.getNombre());
				
				this.tarima = null;
				
				PrimeFaces.current().executeScript("PF('noTarimasDlg').hide()");
				PrimeFaces.current().executeScript("PF('dlgAddProducto').hide()");
				
				log.info("Partida agregada a la tarima {}", p.getTarima().getNombre());
				return;
			}
			
			//Obtenemos la parte entera del número de tarimas.
			//Por ejemplo. Si se indicó 3.5 tarimas, se toma 3 tarimas.
			intNumTarimas = this.numTarimas.intValue();
			parcialTarima = this.numTarimas.subtract(new BigDecimal(intNumTarimas).setScale(2, BigDecimal.ROUND_HALF_UP));
			
			//Se evalúa si se indicaron tarimas completas y además fracción de tarima
			//Por ejemplo, 3.5 tarimas. En caso afirmativo, se lanza una excepción, ya que
			//sólo se deberían indicar tarimas completas (1, 2, 3, etc) o bien, sólo una
			//fracción de tarima (0.1, 0.2, 0.3, ... , 0.9999...)
			if(intNumTarimas > 0 && parcialTarima.compareTo(BigDecimal.ZERO) > 0)
				throw new InventarioException("Debe indicar sólo tarimas completas.");
			
			//En caso contrario, se evalúa si se indicó sólo una fracción de tarima y se procede a agregarla
			//a la lista de tarimas.
			if(intNumTarimas == 0 && parcialTarima.compareTo(BigDecimal.ZERO) > 0) {
				p = (Partida) partida.clone();
				p.setNoTarimas(parcialTarima);
				this.listadoPartida.add(p);
				tarima = this.asignarTarima(p);
				this.tarimas.add(tarima);
			}
			
			for(int i = 0; i < intNumTarimas; i++) {
				log.info("Agregando partida: {}", p);
				p = (Partida) partida.clone();
				this.listadoPartida.add(p);
				log.info("Partida agregada: {}", p);
				tarima = this.asignarTarima(p);
				this.tarimas.add(tarima);
			}
			
			this.numTarimas = null;
			totalTarimas = new BigDecimal(0);
			totalCajas = new BigDecimal(0);
			totalKilos = new BigDecimal(0);
			
			this.totalesTarimas();
			
			this.resetPartida();
			severity = FacesMessage.SEVERITY_INFO;
			mensaje = "Agregado correctamente";
			PrimeFaces.current().executeScript("PF('noTarimasDlg').hide()");
			log.info("Partida agregada.");
		} catch (InventarioException ex) {
			mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_ERROR;
			log.warn(mensaje);
		} catch (Exception ex) {
			log.error("Problema al agregar una nueva partida...", ex);
			mensaje = "Ha ocurrido un error en el sistema. Intente nuevamente.\nSi el problema persiste, por favor comuniquese con su administrador del sistema.";
			severity = FacesMessage.SEVERITY_ERROR;
		} finally {
			message = new FacesMessage(severity, "Producto", mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
			PrimeFaces.current().ajax().update("form:messages", "form:seleccion-mercancia", "form:numTarimas", "form:totalTarimas","form:totalCajas","form:totalKilos", "form:dt-tarimas");
		}
	}
	
	private Tarima asignarTarima(Partida p) {
		Tarima tarima = null;
		log.info("Asignando tarima...");
		
		if(this.idTarima == null)
			this.idTarima = new Integer(0);
		
		tarima = new Tarima();
		tarima.setId(idTarima++);
		tarima.setNombre(String.format("%s-%s", this.constanciaDeDeposito.getFolioCliente(), idTarima));
		
		if(tarima.getPartidas() == null)
			tarima.setPartidas(new ArrayList<>());
		
		tarima.getPartidas().add(p);
		p.setTarima(tarima);
		log.info("Tarima asignada: {}", tarima);
		return tarima;
	}
	
	public void resetPartida() {
		
		try {
			log.info("Reiniciando el objeto partida...");
			this.partida = this.newPartida();
			this.numTarimas = null;
			this.validaCarga = false;
			
			PrimeFaces.current().ajax().update("form:dlg-add-producto");
			log.info("Partida reiniciada.");
		} catch(Exception ex) {
			log.error("Problema para actualizar la información de la nueva partida...", ex);
		}
		
	}
	
	public void clonarPartida(Partida partida) {
		Partida p = null;
		Tarima tarima = null;
		try {
			log.info("Clonando partida {}", partida);
			tarima = partida.getTarima();
			p = partida.clone();
			p.setNoTarimas(null);
			p.setTarima(tarima);
			tarima.getPartidas().add(p);
			this.listadoPartida.add(p);
			
			this.totalesTarimas();
			this.resetPartida();
			
			log.info("Producto {} agregado a la tarima {}", p.getUnidadDeProductoCve().getProductoCve().getProductoDs(), tarima.getNombre());
			
			this.tarima = null;
		} catch(Exception ex) {
			log.error("Problema para clonar la partida seleccionada...", ex);
		}
	}
	
	public void cargaDetalle(Partida partida) {
		log.info("Cargando detalle de la partida...");
		if(partida == null)
			return;
		
		if(partida.getDetallePartidaList() == null)
			return;
		
		if(partida.getDetallePartidaList().size() <= 0)
			return;
		
		this.partida = partida;
		this.detalle = partida.getDetallePartidaList().get(0);
		
		log.info("Detalle partida: {}", this.detalle);
	}
	
	public void quitaDetalle() {
		this.resetPartida();
	}
	
	public void partidaEditada() {
		log.info("Partida editada.");
	}
	
	public void logServiciosBasicos() {
		log.info("Cambio en los servicios basicos seleccionados...");
		
		if(this.isCongelacion)
			log.info("El cliente tiene preconfigurado el servicio de *CONGELACION");
		
		if(this.isConservacion)
			log.info("El cliente tiene preconfigurado el servicio de *CONSERVACION");
		
		if(this.isRefrigeracion)
			log.info("El cliente tiene preconfigurado el servicio de *REFRIGERACION");
		
		if(this.isManiobras)
			log.info("El cliente tiene preconfigurado el servicio de *MANIOBRAS");
	}

	public synchronized void saveConstanciaDeDeposito() {
		
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		
		String resultado = null;
		
		try {
			log.info("El usuario {} intenta guardar una constancia de depósito...", this.usuario.getUsuario());
			EstadoConstancia status = estadoConstanciaDAO.buscarPorId(1);
			String folioCliente = this.constanciaDeDeposito.getFolioCliente();
			if(folioCliente == null)
				folioCliente = this.folioCliente;
			
			ConstanciaDeDeposito constancia = constanciaDAO.buscarPorFolioCliente(folioCliente);
			if(this.constanciaDeDeposito.equals(constancia))
				throw new InventarioException(String.format("La constancia %s ya se encuentra registrada. Puede imprimirla.", folioCliente));
			
			
			if(constancia != null)
				throw new InventarioException(String.format("La constancia %s ya se encuentra registrada", folioCliente));
			
			if(this.isCongelacion) { //servicio_cve: 2
				Servicio congelacion = servicioDAO.buscarPorId(619);
				ConstanciaDepositoDetalle srvCongelacion = new ConstanciaDepositoDetalle();
				srvCongelacion.setServicioCve(congelacion);
				srvCongelacion.setServicioCantidad(new BigDecimal("1.00").setScale(0, BigDecimal.ROUND_HALF_UP));
				srvCongelacion.setFolio(constanciaDeDeposito);
				listadoConstanciaDepositoDetalle.add(srvCongelacion);
			}
			
			if(this.isConservacion) { //servicio_cve: 32
				Servicio conservacion = servicioDAO.buscarPorId(620);
				ConstanciaDepositoDetalle srvConservacion = new ConstanciaDepositoDetalle();
				srvConservacion.setServicioCve(conservacion);
				srvConservacion.setServicioCantidad(new BigDecimal("1.00").setScale(0, BigDecimal.ROUND_HALF_UP));
				srvConservacion.setFolio(constanciaDeDeposito);
				listadoConstanciaDepositoDetalle.add(srvConservacion);
			}
			
			if(this.isRefrigeracion) { //servicio_cve: 33
				Servicio refrigeracion = servicioDAO.buscarPorId(621);
				ConstanciaDepositoDetalle srvRefrigeracion = new ConstanciaDepositoDetalle();
				srvRefrigeracion.setServicioCve(refrigeracion);
				srvRefrigeracion.setServicioCantidad(new BigDecimal("1.00").setScale(0, BigDecimal.ROUND_HALF_UP));
				srvRefrigeracion.setFolio(constanciaDeDeposito);
				listadoConstanciaDepositoDetalle.add(srvRefrigeracion);
			}
			
			if(this.isManiobras) { //servicio_cve: 34
				Servicio maniobras = servicioDAO.buscarPorId(622);
				ConstanciaDepositoDetalle srvManiobras = new ConstanciaDepositoDetalle();
				srvManiobras.setServicioCve(maniobras);
				srvManiobras.setServicioCantidad(new BigDecimal("1.00").setScale(0, BigDecimal.ROUND_HALF_UP));
				srvManiobras.setFolio(constanciaDeDeposito);
				listadoConstanciaDepositoDetalle.add(srvManiobras);
			}
			
			if((avisoSelect==null && aviso == null) ) {
				throw new InventarioException("Debe seleccionar un aviso");
			}
			
			if(clienteSelect == null)
				throw new InventarioException("Debe seleccionar un cliente");
			
			if(fechaIngreso == null)
				throw new InventarioException("Debe indicar una fecha de ingreso");
			
			
			
			if(plantaSelect == null) {
				throw new InventarioException("Debe seleccionar una planta");
			}
			
			if(camaraSelect == null) {
				 throw new InventarioException("Debe seleccionar una camara");
			}
			
			if(constanciaDeDeposito.getFolioCliente() == null || "".equalsIgnoreCase(constanciaDeDeposito.getFolioCliente()) )
				this.constanciaDeDeposito.setFolioCliente(this.folioCliente);
			
			if(listadoPartida == null)
				throw new InventarioException("La lista de productos se encuentra vacía.");
			
			if(listadoPartida.size() == 0)
				throw new InventarioException("La lista de productos se encuentra vacía.");
			
			constanciaDeDeposito.setNombreTransportista(nombreTransportista);
			constanciaDeDeposito.setPlacasTransporte(placas);
			constanciaDeDeposito.setObservaciones(observacion);
			constanciaDeDeposito.setTemperatura(temperatura);
			constanciaDeDeposito.setCteCve(clienteSelect);
			constanciaDeDeposito.setFechaIngreso(fechaIngreso);
			constanciaDeDeposito.setAvisoCve(avisoSelect == null ? aviso : avisoSelect);
			constanciaDeDeposito.setConstanciaDepositoDetalleList(listadoConstanciaDepositoDetalle);
			
			for(Partida p: listadoPartida) {
				p.setCamaraCve(camaraSelect);
			}
			
			constanciaDeDeposito.setPartidaList(listadoPartida);
			constanciaDeDeposito.setStatus(status);
			
			for(Tarima t : this.tarimas) {
				t.setId(null);
			}
			
			resultado = constanciaDAO.guardar(constanciaDeDeposito);
			if(resultado != null)
				throw new InventarioException("Ocurrió un problema al guardar la constancia de depósito " + this.constanciaDeDeposito.getFolioCliente());
			
			log.info("Folio constancia: {}", this.constanciaDeDeposito.getFolio());
			
			Integer numeroSerie = this.serie.getNuSerie() + 1;
			this.serie.setNuSerie(numeroSerie);
			serieConstanciaDAO.actualizar(this.serie);
			
			saved = true;
			
			log.info("El usuario {} guardó la constancia de depósito {} correctamente.", this.usuario.getUsuario(), this.constanciaDeDeposito.getFolioCliente());
			severity = FacesMessage.SEVERITY_INFO;
			mensaje = String.format("La constancia de depósito %s se registró correctamente.", folioCliente);
		} catch (InventarioException ex) {
			mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_WARN;
		} catch (Exception ex) {
			mensaje = "Ha ocurrido un error en el sistema. Intente nuevamente.\nSi el problema persiste, por favor comuniquese con su administrador del sistema.";
			severity = FacesMessage.SEVERITY_ERROR;
		} finally {
			message = new FacesMessage(severity, "Producto", mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
			PrimeFaces.current().ajax().update("form:messages", "form:dt-tarimas", "form:dt-constanciaDD", "form:seleccion-mercancia", "form:seleccion-servicio", "form:dlg-add-producto");
		}
	}
	
	public void deleteConstanciaDD() {
		log.info("Eliiminando ConstanciaDD...");
		this.listadoConstanciaDepositoDetalle.remove(0);
		this.selectedConstanciaDD = null;
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Eliminado", "Se elimino el registro correctamente"));
		PrimeFaces.current().ajax().update("form:messages", "form:dt-constanciaDD");
		log.info("ConstanciaDD eliminada.");
	}
	
	public void deleteTarima(Tarima tarima) {
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		try {
			log.info("Eliminando tarima: {}", tarima);
			
			for(Partida p : tarima.getPartidas()) {
				this.listadoPartida.remove(p);
			}
			this.tarimas.remove(tarima);
			
			mensaje = "Tarima eliminada.";
			severity = FacesMessage.SEVERITY_INFO;
			log.info("Tarima eliminada.");
		} catch(Exception ex) {
			log.warn("Problema para eliminar la tarima...", ex);
			mensaje = "Hay un problema para eliminar la tarima.";
			severity = FacesMessage.SEVERITY_ERROR;
		} finally {
			message = new FacesMessage(severity, "Eliminado", mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
			PrimeFaces.current().ajax().update("form:messages", "form:dt-tarimas");
		}
	
	}
	
	public void deleteSelectedPartidas() {
		Tarima tarima = null;
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		
		try {
			for (Partida p : listadoPartida) {
				if(p != selectedPartida)
					continue;
				
				p.setPartidaCve(1);
				selectedPartida.setPartidaCve(1);
				log.info(p);
				log.info(selectedPartida);
			}
			tarima = this.selectedPartida.getTarima();
			tarima.getPartidas().remove(this.selectedPartida);
			
			listadoPartida.remove(this.selectedPartida);// remueve todos los elementos de listadoPartida ERROR
			this.selectedPartidas = null;
			
			this.resetPartida();
			
			mensaje = "Producto eliminado";
			severity = FacesMessage.SEVERITY_INFO;
		} catch(Exception ex) {
			log.warn("Problema para eliminar la partida...", ex);
			mensaje = "Hay un problema para eliminar el producto.";
			severity = FacesMessage.SEVERITY_ERROR;
		} finally {
			message = new FacesMessage(severity, "Eliminado", mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
			PrimeFaces.current().ajax().update("form:messages", "form:dt-tarimas");
		}
	}

	public void filtraCamaras() {
		try {
			log.info("Filtrando camaras para la planta seleccionada...");
			camaraPorPlanta = camaraDAO.buscarPorPlanta(plantaSelect);
			this.generaFolioEntrada();
			log.info("Termina el filtrado de camaras para la planta seleccionada.");
		} catch(Exception ex) {
			log.error("Problema en la funcion de filtrado de camaras...", ex);
		}
	}

	public void validar() {
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		String constanciaE = null;
		ConstanciaDeDeposito constancia = null;

		try {
			log.info("Validando folio del cliente...");
			constanciaE = folioCliente.trim();
			constanciaE.trim();
	
			constancia = constanciaDAO.buscarPorFolioCliente(constanciaE);
	
			if (constancia != null)
				throw new InventarioException("Digite un nuevo folio.");
			
			constanciaDeDeposito.setFolioCliente(constanciaE);
			mensaje = "Capture sus productos.";
			severity = FacesMessage.SEVERITY_INFO;
			log.info("Folio del cliente validado.");
		} catch (InventarioException ex) {
			this.folioCliente = null;
			mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_WARN;
			log.warn(mensaje);
		} catch (Exception ex) {
			mensaje = "Ha ocurrido un error en el sistema. Intente nuevamente.\nSi el problema persiste, por favor comuniquese con su administrador del sistema.";
			severity = FacesMessage.SEVERITY_ERROR;
			log.error("Problema al validar el folio del cliente...", ex);
		} finally {
			message = new FacesMessage(severity, "Folio", mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
			PrimeFaces.current().ajax().update("form:messages", "form:numeroC");
		}
	}
	
	public void jasper() throws JRException, IOException, SQLException {
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
			log.info("Iniciando impresion de ticket...");
			if(this.constanciaDeDeposito.getFolio() == null)
				throw new InventarioException("La constancia de depósito no está registrada.");
			
			jasperPath = "/jasper/GestionReport.jrxml";
			filename = String.format("Entrada_%s.pdf", constanciaDeDeposito.getFolioCliente());
			images = "/images/logo.jpeg";
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
			parameters.put("FOLIO", this.constanciaDeDeposito.getFolioCliente());
			parameters.put("LogoPath", imgfile.getPath());
			log.debug("Parametros: {}", parameters.toString());

			jasperReportUtil = new JasperReportUtil();
			byte[] bytes = jasperReportUtil.createPDF(parameters, reportFile.getPath());
			InputStream input = new ByteArrayInputStream(bytes);
			this.file = DefaultStreamedContent.builder().contentType("application/pdf").name(filename)
					.stream(() -> input).build();
			log.info("Ticket entrada generado {}...", filename);
		} catch (Exception ex) {
			log.error("Problema general...", ex);
			message = String.format("No se pudo imprimir el folio %s", this.folioCliente);
			severity = FacesMessage.SEVERITY_INFO;
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(severity, "Error en impresion", message));
			PrimeFaces.current().ajax().update("form:messages");
		} finally {
			EntityManagerUtil.close(conn);
		}

	}
	
	public void limpiar() {
		log.info("Limpiando objetos...");
		productoSelect = null;
		unidadDeManejoSelect = new UnidadDeManejo();
		posicionCamaraSelect = new Posicion();
		posicionCamaraSelect = new Posicion();

		cantidadTotal = null;
		pesoTotal = new BigDecimal("0.00").setScale(2);
		unidadesPorTarima = new BigDecimal("0.00").setScale(2);
		valorMercancia = new BigDecimal("0.00").setScale(2);
		pedimento = "";
		contenedor = "";
		lote = "";
		fechaCaducidad = new Date();
		otro = "";

		precioServicioSelect = new PrecioServicio();
		cantidadServicio = new BigDecimal("0.00").setScale(2);
		temperatura = "";
		metroCamara = new BigDecimal("0.00").setScale(2);
		nombreTransportista = "";
		placas = "";
		observacion = "";
		listadoPartida = new ArrayList<Partida>();
		listadoConstanciaDepositoDetalle = new ArrayList<ConstanciaDepositoDetalle>();
		constanciaDeDeposito = new ConstanciaDeDeposito();
		log.info("Objetos limpiados.");
	}
	
	public void reload() throws IOException {
	    ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
	    ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
	}

	public List<Cliente> getListadoCliente() {
		return listadoCliente;
	}

	public void setListadoCliente(List<Cliente> listadoCliente) {
		this.listadoCliente = listadoCliente;
	}

	public List<Planta> getListadoPlanta() {
		return listadoPlanta;
	}

	public void setListadoPlanta(List<Planta> listadoPlanta) {
		this.listadoPlanta = listadoPlanta;
	}

	public List<Camara> getCamaras() {
		return camaras;
	}

	public void setCamaras(List<Camara> camaras) {
		this.camaras = camaras;
	}

	public List<Camara> getCamaraPorPlanta() {
		return camaraPorPlanta;
	}

	public void setCamaraPorPlanta(List<Camara> camaraPorPlanta) {
		this.camaraPorPlanta = camaraPorPlanta;
	}

	public List<ConstanciaDeDeposito> getListadoConstancia() {
		return listadoConstancia;
	}

	public void setListadoConstancia(List<ConstanciaDeDeposito> listadoConstancia) {
		this.listadoConstancia = listadoConstancia;
	}

	public List<ProductoPorCliente> getProductoCliente() {
		return productoCliente;
	}

	public void setProductoCliente(List<ProductoPorCliente> productoCliente) {
		this.productoCliente = productoCliente;
	}

	public List<Producto> getListadoProducto() {
		return listadoProducto;
	}

	public void setListadoProducto(List<Producto> listadoProducto) {
		this.listadoProducto = listadoProducto;
	}

	public List<UnidadDeManejo> getListadoUnidadDeManejo() {
		return listadoUnidadDeManejo;
	}

	public void setListadoUnidadDeManejo(List<UnidadDeManejo> listadoUnidadDeManejo) {
		this.listadoUnidadDeManejo = listadoUnidadDeManejo;
	}

	public List<Posicion> getListaPosiciones() {
		return listaPosiciones;
	}

	public void setListaPosiciones(List<Posicion> listaPosiciones) {
		this.listaPosiciones = listaPosiciones;
	}

	public List<Posicion> getPosiciones() {
		return posiciones;
	}

	public void setPosiciones(List<Posicion> posiciones) {
		this.posiciones = posiciones;
	}

	public Cliente getClienteSelect() {
		return clienteSelect;
	}

	public void setClienteSelect(Cliente clienteSelect) {
		this.clienteSelect = clienteSelect;
	}

	public List<Partida> getListadoPartida() {
		return listadoPartida;
	}

	public void setListadoPartida(List<Partida> listadoPartida) {
		this.listadoPartida = listadoPartida;
	}

	public List<Aviso> getAvisoPorCliente() {
		return avisoPorCliente;
	}

	public void setAvisoPorCliente(List<Aviso> avisoPorCliente) {
		this.avisoPorCliente = avisoPorCliente;
	}

	public List<DetallePartida> getListadoDetallePartida() {
		return listadoDetallePartida;
	}

	public void setListadoDetallePartida(List<DetallePartida> listadoDetallePartida) {
		this.listadoDetallePartida = listadoDetallePartida;
	}

	public List<Servicio> getListadoServicio() {
		return listadoServicio;
	}

	public void setListadoServicio(List<Servicio> listadoServicio) {
		this.listadoServicio = listadoServicio;
	}

	public List<PrecioServicio> getListadoPrecioServicio() {
		return listadoPrecioServicio;
	}

	public void setListadoPrecioServicio(List<PrecioServicio> listadoPrecioServicio) {
		this.listadoPrecioServicio = listadoPrecioServicio;
	}

	public List<PrecioServicio> getListaServicioUnidad() {
		return listaServicioUnidad;
	}

	public void setListaServicioUnidad(List<PrecioServicio> listaServicioUnidad) {
		this.listaServicioUnidad = listaServicioUnidad;
	}

	public List<ConstanciaDepositoDetalle> getListadoConstanciaDepositoDetalle() {
		return listadoConstanciaDepositoDetalle;
	}

	public void setListadoConstanciaDepositoDetalle(List<ConstanciaDepositoDetalle> listadoConstanciaDepositoDetalle) {
		this.listadoConstanciaDepositoDetalle = listadoConstanciaDepositoDetalle;
	}

	public List<Partida> getSelectedPartidas() {
		return selectedPartidas;
	}

	public void setSelectedPartidas(List<Partida> selectedPartidas) {
		this.selectedPartidas = selectedPartidas;
	}

	public List<ConstanciaDepositoDetalle> getSelectedConstanciaDD() {
		return selectedConstanciaDD;
	}

	public void setSelectedConstanciaDD(List<ConstanciaDepositoDetalle> selectedConstanciaDD) {
		this.selectedConstanciaDD = selectedConstanciaDD;
	}

	public ClienteDAO getClienteDAO() {
		return clienteDAO;
	}

	public void setClienteDAO(ClienteDAO clienteDAO) {
		this.clienteDAO = clienteDAO;
	}

	public PlantaDAO getPlantaDAO() {
		return plantaDAO;
	}

	public void setPlantaDAO(PlantaDAO plantaDAO) {
		this.plantaDAO = plantaDAO;
	}

	public CamaraDAO getCamaraDAO() {
		return camaraDAO;
	}

	public void setCamaraDAO(CamaraDAO camaraDAO) {
		this.camaraDAO = camaraDAO;
	}

	public AvisoDAO getAvisoDAO() {
		return avisoDAO;
	}

	public void setAvisoDAO(AvisoDAO avisoDAO) {
		this.avisoDAO = avisoDAO;
	}

	public ConstanciaDeDepositoDAO getConstanciaDAO() {
		return constanciaDAO;
	}

	public void setConstanciaDAO(ConstanciaDeDepositoDAO constanciaDAO) {
		this.constanciaDAO = constanciaDAO;
	}

	public ProductoClienteDAO getProductoClienteDAO() {
		return productoClienteDAO;
	}

	public void setProductoClienteDAO(ProductoClienteDAO productoClienteDAO) {
		this.productoClienteDAO = productoClienteDAO;
	}

	public UnidadDeManejoDAO getUnidadDeManejoDAO() {
		return unidadDeManejoDAO;
	}

	public void setUnidadDeManejoDAO(UnidadDeManejoDAO unidadDeManejoDAO) {
		this.unidadDeManejoDAO = unidadDeManejoDAO;
	}

	public PosicionCamaraDAO getPosicionCamaraDAO() {
		return posicionCamaraDAO;
	}

	public void setPosicionCamaraDAO(PosicionCamaraDAO posicionCamaraDAO) {
		this.posicionCamaraDAO = posicionCamaraDAO;
	}

	public ServicioDAO getServicioDAO() {
		return servicioDAO;
	}

	public void setServicioDAO(ServicioDAO servicioDAO) {
		this.servicioDAO = servicioDAO;
	}

	public PrecioServicioDAO getPrecioServicioDAO() {
		return precioServicioDAO;
	}

	public void setPrecioServicioDAO(PrecioServicioDAO precioServicioDAO) {
		this.precioServicioDAO = precioServicioDAO;
	}

	public String getFolioCliente() {
		return folioCliente;
	}

	public void setFolioCliente(String folioCliente) {
		this.folioCliente = folioCliente;
	}

	public Planta getPlantaSelect() {
		return plantaSelect;
	}

	public void setPlantaSelect(Planta plantaSelect) {
		this.plantaSelect = plantaSelect;
	}

	public ProductoPorCliente getProductoPorCliente() {
		return productoPorCliente;
	}

	public void setProductoPorCliente(ProductoPorCliente productoPorCliente) {
		this.productoPorCliente = productoPorCliente;
	}

	public ProductoDAO getProductoDAO() {
		return productoDAO;
	}

	public void setProductoDAO(ProductoDAO productoDAO) {
		this.productoDAO = productoDAO;
	}

	public List<Producto> getProductos() {
		return productos;
	}

	public void setProductos(List<Producto> productos) {
		this.productos = productos;
	}

	public Camara getCamaraSelect() {
		return camaraSelect;
	}

	public void setCamaraSelect(Camara camaraSelect) {
		this.camaraSelect = camaraSelect;
	}

	public UnidadDeManejo getUnidadDeManejoSelect() {
		return unidadDeManejoSelect;
	}

	public void setUnidadDeManejoSelect(UnidadDeManejo unidadDeManejoSelect) {
		this.unidadDeManejoSelect = unidadDeManejoSelect;
	}

	public Posicion getPosicionCamaraSelect() {
		return posicionCamaraSelect;
	}

	public void setPosicionCamaraSelect(Posicion posicionCamaraSelect) {
		this.posicionCamaraSelect = posicionCamaraSelect;
	}

	public Aviso getAvisoSelect() {
		return avisoSelect;
	}

	public void setAvisoSelect(Aviso avisoSelect) {
		this.avisoSelect = avisoSelect;
	}

	public String getPedimento() {
		return pedimento;
	}

	public void setPedimento(String pedimento) {
		this.pedimento = pedimento;
	}

	public String getContenedor() {
		return contenedor;
	}

	public void setContenedor(String contenedor) {
		this.contenedor = contenedor;
	}

	public String getLote() {
		return lote;
	}

	public void setLote(String lote) {
		this.lote = lote;
	}

	public String getOtro() {
		return otro;
	}

	public void setOtro(String otro) {
		this.otro = otro;
	}

	public Date getFechaCaducidad() {
		return fechaCaducidad;
	}

	public void setFechaCaducidad(Date fechaCaducidad) {
		this.fechaCaducidad = fechaCaducidad;
	}

	public Date getFechaIngreso() {
		return fechaIngreso;
	}

	public void setFechaIngreso(Date fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}

	public PrecioServicio getPrecioServicioSelect() {
		return precioServicioSelect;
	}

	public void setPrecioServicioSelect(PrecioServicio precioServicioSelect) {
		this.precioServicioSelect = precioServicioSelect;
	}

	public Boolean getIsCongelacion() {
		return isCongelacion;
	}

	public void setIsCongelacion(Boolean isCongelacion) {
		this.isCongelacion = isCongelacion;
	}

	public Boolean getIsConservacion() {
		return isConservacion;
	}

	public void setIsConservacion(Boolean isConservacion) {
		this.isConservacion = isConservacion;
	}

	public Boolean getIsRefrigeracion() {
		return isRefrigeracion;
	}

	public void setIsRefrigeracion(Boolean isRefrigeracion) {
		this.isRefrigeracion = isRefrigeracion;
	}

	public Boolean getIsManiobras() {
		return isManiobras;
	}

	public void setIsManiobras(Boolean isManiobras) {
		this.isManiobras = isManiobras;
	}

	public BigDecimal getCantidadServicio() {
		return cantidadServicio;
	}

	public void setCantidadServicio(BigDecimal cantidadServicio) {
		this.cantidadServicio = cantidadServicio;
	}

	public ConstanciaDepositoDetalle getSelectedConstanciasDD() {
		return selectedConstanciasDD;
	}

	public void setSelectedConstanciasDD(ConstanciaDepositoDetalle selectedConstanciasDD) {
		this.selectedConstanciasDD = selectedConstanciasDD;
	}

	public Partida getSelectedPartida() {
		return selectedPartida;
	}

	public void setSelectedPartida(Partida selectedPartida) {
		this.selectedPartida = selectedPartida;
	}

	public BigDecimal getUnidadesPorTarima() {
		return unidadesPorTarima;
	}

	public void setUnidadesPorTarima(BigDecimal unidadesPorTarima) {
		this.unidadesPorTarima = unidadesPorTarima;
	}

	public BigDecimal getPesoTotal() {
		return pesoTotal;
	}

	public void setPesoTotal(BigDecimal pesoTotal) {
		this.pesoTotal = pesoTotal;
	}

	public BigDecimal getValorMercancia() {
		return valorMercancia;
	}

	public void setValorMercancia(BigDecimal valorMercancia) {
		this.valorMercancia = valorMercancia;
	}

	public Boolean getRespuesta() {
		return respuesta;
	}

	public void setRespuesta(Boolean respuesta) {
		this.respuesta = respuesta;
	}

	public String getTemperatura() {
		return temperatura;
	}

	public void setTemperatura(String temperatura) {
		this.temperatura = temperatura;
	}

	public BigDecimal getMetroCamara() {
		return metroCamara;
	}

	public void setMetroCamara(BigDecimal metroCamara) {
		this.metroCamara = metroCamara;
	}

	public String getNombreTransportista() {
		return nombreTransportista;
	}

	public void setNombreTransportista(String nombreTransportista) {
		this.nombreTransportista = nombreTransportista;
	}

	public String getPlacas() {
		return placas;
	}

	public void setPlacas(String placas) {
		this.placas = placas;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public boolean isShowCodigo() {
		return showCodigo;
	}

	public void setShowCodigo(boolean showCodigo) {
		this.showCodigo = showCodigo;
	}

	public boolean isShowPedimento() {
		return showPedimento;
	}

	public void setShowPedimento(boolean showPedimento) {
		this.showPedimento = showPedimento;
	}

	public boolean isShowSAP() {
		return showSAP;
	}

	public void setShowSAP(boolean showSAP) {
		this.showSAP = showSAP;
	}

	public boolean isShowLote() {
		return showLote;
	}

	public void setShowLote(boolean showLote) {
		this.showLote = showLote;
	}

	public boolean isShowOtro() {
		return showOtro;
	}

	public void setShowOtro(boolean showOtro) {
		this.showOtro = showOtro;
	}

	public boolean isShowCaducidad() {
		return showCaducidad;
	}

	public void setShowCaducidad(boolean showCaducidad) {
		this.showCaducidad = showCaducidad;
	}

	public BigDecimal getCantidadTotal() {
		return cantidadTotal;
	}

	public void setCantidadTotal(BigDecimal cantidadTotal) {
		this.cantidadTotal = cantidadTotal;
	}

	public Partida getPartida() {
		return partida;
	}

	public void setPartida(Partida partida) {
		this.partida = partida;
	}

	public TipoMovimiento getTipoMovimiento() {
		return tipoMovimiento;
	}

	public void setTipoMovimiento(TipoMovimiento tipoMovimiento) {
		this.tipoMovimiento = tipoMovimiento;
	}

	public DetallePartida getDetalle() {
		return detalle;
	}

	public void setDetalle(DetallePartida detalle) {
		this.detalle = detalle;
	}

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public Integer getProductoSelect() {
		return productoSelect;
	}

	public void setProductoSelect(Integer productoSelect) {
		this.productoSelect = productoSelect;
	}

	public UISelectItems getProductoItems() {
		return productoItems;
	}

	public void setProductoItems(UISelectItems productoItems) {
		this.productoItems = productoItems;
	}
	
	public BigDecimal getNumTarimas() {
		return numTarimas;
	}

	public void setNumTarimas(BigDecimal numTarimas) {
		this.numTarimas = numTarimas;
	}

	public Boolean getRestricted() {
		return restricted;
	}

	public void setRestricted(Boolean restricted) {
		this.restricted = restricted;
	}

	public Boolean getSaved() {
		return saved;
	}

	public void setSaved(Boolean saved) {
		this.saved = saved;
	}

	public Date getMaxDate() {
		return maxDate;
	}

	public void setMaxDate(Date maxDate) {
		this.maxDate = maxDate;
	}

	public boolean isValidaCarga() {
		return validaCarga;
	}

	public void setValidaCarga(boolean validaCarga) {
		this.validaCarga = validaCarga;
	}

	public Partida getPartidaEdit() {
		return partidaEdit;
	}

	public void setPartidaEdit(Partida partidaEdit) {
		this.partidaEdit = partidaEdit;
	}

	public BigDecimal getTotalTarimas() {
		return totalTarimas;
	}

	public void setTotalTarimas(BigDecimal totalTarimas) {
		this.totalTarimas = totalTarimas;
	}

	public BigDecimal getTotalKilos() {
		return totalKilos;
	}

	public void setTotalKilos(BigDecimal totalKilos) {
		this.totalKilos = totalKilos;
	}

	public BigDecimal getTotalCajas() {
		return totalCajas;
	}

	public void setTotalCajas(BigDecimal totalCajas) {
		this.totalCajas = totalCajas;
	}

	public ConstanciaDeDeposito getConstanciaDeDeposito() {
		return constanciaDeDeposito;
	}

	public void setConstanciaDeDeposito(ConstanciaDeDeposito constanciaDeDeposito) {
		this.constanciaDeDeposito = constanciaDeDeposito;
	}

	public StreamedContent getFile() {
		return file;
	}

	public void setFile(StreamedContent file) {
		this.file = file;
	}

	public List<Tarima> getTarimas() {
		return tarimas;
	}

	public void setTarimas(List<Tarima> tarimas) {
		this.tarimas = tarimas;
	}
	
	public Tarima getTarima() {
		return tarima;
	}

	public void setTarima(Tarima tarima) {
		this.tarima = tarima;
	}
	
	public Aviso getAviso() {
		return aviso;
	}

	public void setAviso(Aviso aviso) {
		this.aviso = aviso;
	}

	public List<Aviso> getAvisos() {
		return avisos;
	}

	public void setAvisos(List<Aviso> avisos) {
		this.avisos = avisos;
	}
}
