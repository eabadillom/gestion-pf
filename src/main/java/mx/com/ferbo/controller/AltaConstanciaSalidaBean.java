package mx.com.ferbo.controller;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;

import mx.com.ferbo.dao.CandadoSalidaDAO;
import mx.com.ferbo.dao.ConstanciaSalidaDAO;
import mx.com.ferbo.dao.ConstanciaServicioDAO;
import mx.com.ferbo.dao.DetallePartidaDAO;
import mx.com.ferbo.dao.EstadoInventarioDAO;
import mx.com.ferbo.dao.InventarioDAO;
import mx.com.ferbo.dao.PartidaDAO;
import mx.com.ferbo.dao.PlantaDAO;
import mx.com.ferbo.dao.PrecioServicioDAO;
import mx.com.ferbo.dao.SaldoDAO;
import mx.com.ferbo.dao.SerieConstanciaDAO;
import mx.com.ferbo.dao.StatusConstanciaSalidaDAO;
import mx.com.ferbo.dao.TipoMovimientoDAO;
import mx.com.ferbo.model.CandadoSalida;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.ConstanciaDeDeposito;
import mx.com.ferbo.model.ConstanciaDeServicio;
import mx.com.ferbo.model.ConstanciaSalida;
import mx.com.ferbo.model.ConstanciaSalidaServicios;
import mx.com.ferbo.model.ConstanciaSalidaServiciosPK;
import mx.com.ferbo.model.ConstanciaServicioDetalle;
import mx.com.ferbo.model.DetalleConstanciaSalida;
import mx.com.ferbo.model.DetallePartida;
import mx.com.ferbo.model.DetallePartidaPK;
import mx.com.ferbo.model.EstadoConstancia;
import mx.com.ferbo.model.EstadoInventario;
import mx.com.ferbo.model.Inventario;
import mx.com.ferbo.model.Partida;
import mx.com.ferbo.model.PartidaServicio;
import mx.com.ferbo.model.Planta;
import mx.com.ferbo.model.PrecioServicio;
import mx.com.ferbo.model.Saldo;
import mx.com.ferbo.model.SerieConstancia;
import mx.com.ferbo.model.SerieConstanciaPK;
import mx.com.ferbo.model.Servicio;
import mx.com.ferbo.model.StatusConstanciaSalida;
import mx.com.ferbo.model.TipoMovimiento;
import mx.com.ferbo.model.Usuario;
import mx.com.ferbo.util.EntityManagerUtil;
import mx.com.ferbo.util.InventarioException;
import mx.com.ferbo.util.JasperReportUtil;
import mx.com.ferbo.util.conexion;

@Named
@ViewScoped

public class AltaConstanciaSalidaBean implements Serializable{
	
	private static final long serialVersionUID = -1785488265380235016L;
	
	private static Logger log = LogManager.getLogger(AltaConstanciaSalidaBean.class);
	
	private List<Cliente> listadoClientes;
	private Cliente clienteSelect;
	
	private List<Planta> listadoPlantas;
	private PlantaDAO plantaDAO;
	private Planta plantaSelect;
	
	private List<ConstanciaSalida> listadoConstanciasSalidas;
	private ConstanciaSalidaDAO constanciaSalidaDAO;
	
	private List<ConstanciaDeDeposito> listadoConstanciaDD;
	
	private List<PrecioServicio> listadoPrecioServicios;
	private PrecioServicioDAO preciosServicioDAO;
	private List<PrecioServicio> serviciosCliente;
	private PrecioServicio servicioClienteSelect;
	
	private List<ConstanciaSalidaServicios> listadoConstanciaSalidaServicios;//listadoConstanciaDepositoDetalle
	
	private Partida partidaSelect;
	private List<Partida> listadoPartida;
	
	private List<DetalleConstanciaSalida> listadoDetalleConstanciaSalida;
	private List<DetalleConstanciaSalida> listadoTemp;
	
	private DetallePartida detallePartida;
	private List<DetallePartida> detallePartidaLista;
	
	private DetallePartidaDAO detallePartidaDAO;
	
	private List<Inventario> listaInventario;
	private List<Inventario> listaInventarioCopy;
	private List<Inventario> filteredInventario;
	private List<String> listaEntradas;
	private List<Date> listaIngresos;

	private InventarioDAO inventarioDAO;
	private Inventario inventarioSelected;
	
	private ConstanciaDeServicio constanciaDeServicio;
	private ConstanciaServicioDAO constanciaServicioDAO;
	
	private String numFolio;
	private String nombreTransportista;
	private String placas;
	private String observaciones;
	private String temperatura;
	private Boolean statusTermo;
	private BigDecimal temperaturaTransporte;
	private BigDecimal cantidadServicio;
	private Date fechaSalida;
	private int cantidadTotal;
	private BigDecimal pesoTotal;
	
	private DetalleConstanciaSalida detalleSalida;
	private int tmpIdDetalleSalida = 0;
	private ConstanciaSalidaServicios constanciaSalidaServicio;
	private SerieConstanciaDAO serieConstanciaDAO;
	private SerieConstancia serie;
	
	private StatusConstanciaSalida status;
	private StatusConstanciaSalidaDAO statusDAO;
	private PartidaDAO partidaDAO;
	
	private TipoMovimientoDAO tipoMovimientoDAO;
	private TipoMovimiento tpMovimientoSalida;
	
	private EstadoInventarioDAO estadoInventarioDAO;
	private EstadoInventario edoInventarioActual;
	private EstadoInventario edoInventarioHistorico;
	private List<Partida> partidaList;
	
	private SaldoDAO saldoDAO;
	private Saldo saldo;
	private CandadoSalidaDAO candadoDAO = null;
	private CandadoSalida candadoSalida = null;
	private BigDecimal cantidadInventario = null;
	private boolean saldoVencido = false;
	
	private boolean saved = false;
	
	private Usuario usuario;
	private FacesContext faceContext;
	private HttpServletRequest httpServletRequest;
	
	public String formatDate(Date date) {
	     DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");   
	     return dateFormat.format(date);
	}
	
	public AltaConstanciaSalidaBean() {
		constanciaDeServicio = new ConstanciaDeServicio();
		constanciaServicioDAO = new ConstanciaServicioDAO();
		
		listadoClientes = new ArrayList<>();
		
		plantaDAO = new PlantaDAO();
		listadoPlantas = new ArrayList<>();
		
		constanciaSalidaDAO = new ConstanciaSalidaDAO();
		listadoConstanciasSalidas = new ArrayList<>();
		
		listadoConstanciaDD = new ArrayList<>();
		listadoPrecioServicios = new ArrayList<>();
		serviciosCliente = new ArrayList<>();
		preciosServicioDAO = new PrecioServicioDAO();
		
		inventarioDAO = new InventarioDAO();
		listaInventario = new ArrayList<Inventario>();
		listaInventarioCopy = new ArrayList<>();
		
		detallePartidaDAO = new DetallePartidaDAO();
		serieConstanciaDAO = new SerieConstanciaDAO();
		statusDAO = new StatusConstanciaSalidaDAO();
		partidaDAO = new PartidaDAO();
		tipoMovimientoDAO = new TipoMovimientoDAO();
		estadoInventarioDAO = new EstadoInventarioDAO();
		
		listadoConstanciaSalidaServicios = new ArrayList<>();

		listadoPartida = new ArrayList<Partida>();
		listadoDetalleConstanciaSalida = new ArrayList<>();
		listadoTemp = new ArrayList<>();
		detallePartidaLista = new ArrayList<>();
		
		saldoDAO = new SaldoDAO();
		candadoDAO = new CandadoSalidaDAO();
	}
	
	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() {
		
		faceContext = FacesContext.getCurrentInstance();
		httpServletRequest = (HttpServletRequest) faceContext.getExternalContext().getRequest();
		usuario = (Usuario) httpServletRequest.getSession(false).getAttribute("usuario");
		
		listadoClientes = (List<Cliente>) httpServletRequest.getSession(false).getAttribute("clientesActivosList");
		//Por defecto, el catálogo de status constancia salida tiene el valor 1 para una constancia vigente.
		status = statusDAO.buscarPorId(1);
		tpMovimientoSalida = tipoMovimientoDAO.buscarPorId(2);
		edoInventarioActual = estadoInventarioDAO.buscarPorId(1);
		edoInventarioHistorico = estadoInventarioDAO.buscarPorId(2);
		
		if((usuario.getPerfil() == 1)||(usuario.getPerfil() == 4)) {
			listadoPlantas.add(plantaDAO.buscarPorId(usuario.getIdPlanta()));
		}else {
			listadoPlantas = plantaDAO.findall(false);
		}
		plantaSelect = listadoPlantas.get(0);
		fechaSalida = new Date();
		
		this.cantidadTotal = 0;
		this.pesoTotal = new BigDecimal("0.000").setScale(3, BigDecimal.ROUND_HALF_UP);
		
		this.statusTermo = new Boolean(true);
		
		log.info("El usuario {} entra a Inventarios / Salidas / Alta.", this.usuario.getUsuario());
	}
	
	public synchronized void validar() throws InventarioException {
		
		int contador = 0;
		SerieConstancia serie = new SerieConstancia();
		listadoConstanciasSalidas = constanciaSalidaDAO.buscarTodos();
		for(ConstanciaSalida cs: listadoConstanciasSalidas) {
			if(cs.getNumero().equals(numFolio)){
				FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,"ERROR FOLIO","El folio no esta disponible"));
				this.numFolio = null;
				PrimeFaces.current().ajax().update("form:folio");
				break;
			}
			contador = contador + 1;
			if(contador == listadoConstanciasSalidas.size()) {
				SerieConstanciaPK seriePK = new SerieConstanciaPK();
				
				seriePK.setCliente(this.clienteSelect);
				seriePK.setTpSerie("O");
				serie = serieConstanciaDAO.buscarPorId(seriePK);

				if (serie == null) {
					this.numFolio = "";
					throw new InventarioException("No se encontró información de los folios del cliente. Debe indicar manualmente un folio de constancia.");
				}
				this.serie = serie;
				FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO,"FOLIO DISPONIBLE","El folio esta disponible"));
			}
		}
		PrimeFaces.current().ajax().update("form:messages");
		
	}
	
	public synchronized void cargaInfoCliente() {
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		String titulo = "Constancia salida";
		
		try {
			this.validaSaldo();
			this.generaFolioSalida();//si el folio existe no ejecutar lo demas y mandar a llamar a validar 
			serviciosCliente = preciosServicioDAO.buscarPorCliente(clienteSelect.getCteCve(), true);
			listaInventario = inventarioDAO.buscar(clienteSelect, plantaSelect);
			listadoTemp = new ArrayList<DetalleConstanciaSalida>();
			listadoConstanciaSalidaServicios = new ArrayList<ConstanciaSalidaServicios>();
			partidaList = new ArrayList<Partida>();
			listaInventarioCopy = inventarioDAO.buscar(clienteSelect, plantaSelect);
			
			listaEntradas = new ArrayList<String>();
			for(Inventario i : listaInventario) {
				if(listaEntradas.contains(i.getFolioCliente()))
					continue;
				listaEntradas.add(i.getFolioCliente());
			}
			log.debug("Lista entradas: {}",listaEntradas);
			
			listaIngresos = new ArrayList<Date>();
			for(Inventario i : listaInventario) {
				if(listaIngresos.contains(i.getFechaIngreso()))
					continue;
				listaIngresos.add(i.getFechaIngreso());
			}
			log.debug("Lista fechas de ingreso: {}", listaIngresos);
		} catch (InventarioException ex) {
			mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_ERROR;
			message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
		} catch (Exception ex) {
			log.error("Problema para cargar la información del cliente...", ex);
		} finally {
			PrimeFaces.current().ajax().update(":form:messages", ":form:folio");	
		}
	}
	
	private void validaSaldo()
	throws InventarioException {
		boolean isHabilitarSalida = false;
		BigDecimal saldoVencido = null;
		
		saldo = saldoDAO.getSaldo(clienteSelect, fechaSalida, null);
		cantidadInventario = inventarioDAO.getCantidad(clienteSelect.getCteCve(), fechaSalida);
		candadoSalida = candadoDAO.buscarPorCliente(this.clienteSelect.getCteCve());
		
		if(saldo == null) {
			log.info("El cliente NO tiene saldos pendientes, puede retirar su mercancia.");
			return;
		}
		
		log.info("Saldo: en plazo = {}, 15 días = {}, 30 días = {}, 60 días = {}, más de 60 días = {}", saldo.getEnPlazo(), saldo.getAtraso15dias(), saldo.getAtraso30dias(), saldo.getAtraso60dias(), saldo.getAtrasoMayor60dias());
		log.info("Cantidad en inventario: {} piezas.", cantidadInventario);
		
		saldoVencido = saldo.getAtraso8dias().add(saldo.getAtraso15dias()).add(saldo.getAtraso30dias()).add(saldo.getAtraso60dias()).add(saldo.getAtrasoMayor60dias());
		
		if(saldoVencido.compareTo(BigDecimal.ZERO) > 0) {
			this.saldoVencido = true;
			log.info("El cliente {} presenta un saldo vencido: {}", this.clienteSelect.getNombre(), this.saldo.getSaldo());
		}
		
		isHabilitarSalida = candadoSalida.getHabilitado();
		
//		if(candadoSalida.getNumSalidas() <= 0)
//			throw new InventarioException("El cliente no tiene permitida la salida de mercancía. Por favor, comuníquese con el área de Facturación.");
		
		if(this.saldoVencido && isHabilitarSalida && candadoSalida.getNumSalidas() > 0) {
			log.info("El cliente tiene un saldo vencido, pero tiene {} salidas permitidas.", candadoSalida.getNumSalidas());
			return;
		}
		
		if(this.saldoVencido && isHabilitarSalida && candadoSalida.getNumSalidas() <= 0) {
			throw new InventarioException("El cliente tiene adeudos vencidos. Favor de contactar con el área de facturacíon");
		}
		
		if(this.saldoVencido && isHabilitarSalida == false) {
			throw new InventarioException("El cliente tiene adeudos vencidos. Favor de contactar con el área de facturación.");
		}
		
		if(this.saldoVencido == false) {
			//retorna el control sin acciones, debido a que SI tiene permitidas las salidas.
			log.info("El cliente NO tiene saldos vencidos, puede retirar su mercancia.");
			return;
		}
		
		if(isHabilitarSalida && candadoSalida.getNumSalidas() > 0) {
			log.info("El cliente {} presenta un saldo vencido ({}), pero tiene permitidas las salidas", this.clienteSelect.getNombre(), this.saldo.getSaldo());
			return;
		}
		
		throw new InventarioException("El cliente no tiene  permitida la salida de mercancia porque presenta un adeudo. Favor de contactar al área de cobranza.");
	}

	public synchronized void generaFolioSalida() {
		SerieConstanciaPK seriePK = null;
		SerieConstancia serie = null;
		SerieConstancia serieTemp = null;

		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		String titulo = "Folio";

		try {
			if (this.clienteSelect == null)
				throw new InventarioException("Debe seleccionar un cliente");

			if (this.plantaSelect == null)
				throw new InventarioException("Debe seleccionar una planta");
						
			seriePK = new SerieConstanciaPK();
			serieTemp = new SerieConstancia();
			
			seriePK.setCliente(this.clienteSelect);
			seriePK.setPlanta(plantaSelect);
			seriePK.setTpSerie("O");
			
			serieTemp.setSerieConstanciaPK(seriePK);
			
			
			serie = serieConstanciaDAO.buscarPorClienteAndPlanta(serieTemp);

			
			
			if (serie == null) {
				this.numFolio = "";
				throw new InventarioException(
						"No se encontró información de los folios del cliente. Debe indicar manualmente un folio de constancia.");
			}

			this.numFolio = String.format("%s%s%s%d", serie.getSerieConstanciaPK().getTpSerie(), plantaSelect.getPlantaSufijo(),
					clienteSelect.getCodUnico(), serie.getNuSerie());
			
			String folio = constanciaSalidaDAO.buscarPorNumero(numFolio);
			
			if(numFolio.equalsIgnoreCase(folio)) {
								
				throw new InventarioException("El folio ya existe");
			}
			
			this.serie = serie;

		} catch (InventarioException ex) {
			mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_WARN;
			
			message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
		} catch (Exception ex) {
			log.error("Problema para generar el folio de entrada...", ex);
			mensaje = "Ha ocurrido un error en el sistema. Intente nuevamente.\nSi el problema persiste, por favor comuniquese con su administrador del sistema.";
			severity = FacesMessage.SEVERITY_ERROR;
			
			message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
		} finally {
			PrimeFaces.current().ajax().update(":form:messages", ":form:folio");
		}
	}
	
	public void agregaServicio() {
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		String titulo = "Servicio";
		
		ConstanciaSalidaServicios constanciaSalidaServicios = null;
		ConstanciaSalidaServiciosPK constanciaSalidaServiciosPK = null;
		
		try {
			constanciaSalidaServicios = new ConstanciaSalidaServicios();
			constanciaSalidaServiciosPK = new ConstanciaSalidaServiciosPK();
			
			if(servicioClienteSelect == null)
				throw new InventarioException("Debe indicar un servicio.");
			
			if(cantidadServicio == null)
				throw new InventarioException("Debe indicar la cantidad del servicio solicitado.");
			
			if(cantidadServicio.compareTo(BigDecimal.ZERO) <= 0)
				throw new InventarioException("La cantidad del servicio indicada es incorrecta.");
			
			constanciaSalidaServiciosPK.setServicioCve(servicioClienteSelect.getServicio());
			constanciaSalidaServicios.setConstanciaSalidaServiciosPK(constanciaSalidaServiciosPK);
			constanciaSalidaServicios.setNumCantidad(cantidadServicio);
			
			listadoConstanciaSalidaServicios.add(constanciaSalidaServicios);
			log.debug(listadoConstanciaSalidaServicios);
			
		} catch (InventarioException ex) {
			mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_WARN;
			
			message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
		} catch (Exception ex) {
			log.error("Problema para generar el folio de entrada...", ex);
			mensaje = "Ha ocurrido un error en el sistema. Intente nuevamente.\nSi el problema persiste, por favor comuniquese con su administrador del sistema.";
			severity = FacesMessage.SEVERITY_ERROR;
			
			message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
		} finally {
			PrimeFaces.current().ajax().update(":form:messages");
		}
		
	}
	
	public void newDetalleSalida(Inventario inventario){
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		String titulo = "Agregar producto";
		
		Partida partida = null;
		List<DetallePartida> detallePartidaList = null;
		DetallePartida detallePartidaAnterior = null;
		DetallePartidaPK dpPK = null;
		DetallePartida detallePartida = null;
		Integer detPartCve = null;
		
		try {
			this.inventarioSelected = inventario;
			List<DetalleConstanciaSalida> lstDcsRegistrada = this.listadoTemp.stream()
					.filter(d -> d.getPartidaCve().getPartidaCve() == inventario.getPartidaCve())
					.collect(Collectors.toList())
					;
			DetalleConstanciaSalida dcsRegistrada = null;
			if(lstDcsRegistrada != null && lstDcsRegistrada.size() > 0)
				dcsRegistrada = lstDcsRegistrada.get(0);
			
			if(dcsRegistrada != null)
				throw new InventarioException("El producto seleccionado ya está registrado.");
			
			partida = partidaDAO.buscarPorId(inventario.getPartidaCve(), true);
			detallePartidaList = partida.getDetallePartidaList();
			detallePartidaAnterior = detallePartidaList.get(detallePartidaList.size() - 1);
			detPartCve = detallePartidaAnterior.getDetallePartidaPK().getDetPartCve() + 1;

			detallePartida = detallePartidaAnterior.clone();
			detallePartidaAnterior.setEdoInvCve(edoInventarioHistorico);
			
			dpPK = detallePartidaAnterior.getDetallePartidaPK().clone();
			dpPK.setDetPartCve(detPartCve);
			detallePartida.setDetallePartidaPK(dpPK);
			detallePartida.getDetallePartidaPK().setDetPartCve(detPartCve);
			detallePartida.setDetallePartida(detallePartidaAnterior);
			detallePartida.setTipoMovCve(tpMovimientoSalida);
			detallePartida.setEdoInvCve(edoInventarioActual);
			
			partida.add(detallePartida);
			
			this.detalleSalida = new DetalleConstanciaSalida();                                                                                 
			this.detalleSalida.setPartidaCve(partida);
			this.detalleSalida.setDetPartCve(detallePartida.getDetallePartidaPK().getDetPartCve());
			//this.detalleSalida.setDetallePartida(detallePartida);
			this.detalleSalida.setCantidad(inventario.getCantidad());
			this.detalleSalida.setPeso(inventario.getPeso());
			this.detalleSalida.setUnidad(inventario.getUnidadManejo().getUnidadDeManejoDs());
			this.detalleSalida.setCamaraCve(inventario.getCamara().getCamaraCve());
			this.detalleSalida.setProducto(inventario.getProducto().getProductoDs());
			this.detalleSalida.setFolioEntrada(inventario.getFolioCliente());
			this.detalleSalida.setCamaraCadena(inventario.getCamara().getCamaraDs());
			this.detalleSalida.setId(this.tmpIdDetalleSalida++);
			partida.add(detalleSalida);
			
			this.partidaList.add(partida);
			mensaje = "Indique la cantidad y temperatura.";
			severity = FacesMessage.SEVERITY_INFO;
			
		} catch (InventarioException ex) {
			
			mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_WARN;
			
			PrimeFaces.current().executeScript("PF('dg-cantidad-producto').hide()");
		} catch (Exception ex) {
			log.error("Problema para generar el folio de entrada...", ex);
			
			mensaje = "Ha ocurrido un error en el sistema. Intente nuevamente.\nSi el problema persiste, por favor comuniquese con su administrador del sistema.";
			severity = FacesMessage.SEVERITY_ERROR;
			
			PrimeFaces.current().executeScript("PF('dg-cantidad-producto').hide()");
		} finally {
			message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
			
			PrimeFaces.current().ajax().update("form:messages", "form:pnl-cantidad-producto", "form:det-cantidad", "form:det-peso", "form:-det-temperatura");
		}
	}
	
	public void cancelaDetalleSalida() {
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		String titulo = "Producto";
		
		try {
			log.debug("Cancela la captura del detalle de salida.");
			this.detalleSalida = new DetalleConstanciaSalida();
			this.detalleSalida.setCantidad(0);
			this.detalleSalida.setPeso(BigDecimal.ZERO);
			
			PrimeFaces.current().executeScript("PF('dg-cantidad-producto').hide()");
		} catch (Exception ex) {
			log.error("Problema para generar el folio de entrada...", ex);
			mensaje = "Ha ocurrido un error en el sistema. Intente nuevamente.\nSi el problema persiste, por favor comuniquese con su administrador del sistema.";
			
			severity = FacesMessage.SEVERITY_ERROR;
			message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
		} finally {
			PrimeFaces.current().ajax().update("form:messages");
		}
		
	}
	
	public void addDetalleSalida() {
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		String titulo = "Producto";
		List<DetalleConstanciaSalida> lista = null;
		
		
		try {
			if(this.detalleSalida.getTemperatura() == null)
				throw new InventarioException("Debe indicar una temperatura.");
			
			if("".equalsIgnoreCase(this.detalleSalida.getTemperatura()))
				throw new InventarioException("Debe indicar una temperatura.");
			
			if(this.detalleSalida.getCantidad() <= 0)
				throw new InventarioException("Debe indicar una cantidad correcta.");
			
			if(this.detalleSalida.getCantidad() > this.inventarioSelected.getCantidad())
				throw new InventarioException("La cantidad indicada es mayor al inventario disponible.");
			
			if(listadoTemp == null)
				this.listadoTemp = new ArrayList<DetalleConstanciaSalida>();
			
			lista = listadoTemp.stream().filter(d -> d.getPartidaCve().getPartidaCve() == detalleSalida.getPartidaCve().getPartidaCve())
					.collect(Collectors.toList());
			
			if(lista.size() > 0)
				throw new InventarioException("El producto seleccionado ya está registrado");
			
			listadoTemp.add(this.detalleSalida);
			
			
			this.cantidadTotal = 0;
			this.pesoTotal = new BigDecimal("0.000").setScale(3, BigDecimal.ROUND_HALF_UP);
			for(DetalleConstanciaSalida dcs : listadoTemp ) {
				this.cantidadTotal += dcs.getCantidad();
				this.pesoTotal = this.pesoTotal.add(dcs.getPeso());
			}
			
			PrimeFaces.current().executeScript("PF('dg-cantidad-producto').hide()");
			mensaje = "El producto se registró correctamente.";
			severity = FacesMessage.SEVERITY_INFO;
			
			listaInventario.remove(inventarioSelected);
			
		} catch (InventarioException ex) {
			mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_WARN;
		} catch (Exception ex) {
			log.error("Problema para generar el folio de entrada...", ex);
			mensaje = "Ha ocurrido un error en el sistema. Intente nuevamente.\nSi el problema persiste, por favor comuniquese con su administrador del sistema.";
			severity = FacesMessage.SEVERITY_ERROR;
		} finally {
			
			message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
			PrimeFaces.current().ajax().update("form:messages","form:dt-salidas");
		}
	}
	
	
	public void calculoPesoSalida() {
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		
		BigDecimal peso = null;
		BigDecimal cantidad = null;
		BigDecimal pesoUnitario = null;
		BigDecimal pesoSalida = null;
		
		try {
			if(this.detalleSalida.getCantidad() > this.inventarioSelected.getCantidad())
				throw new InventarioException("La cantidad indicada es mayor a la disponible en inventario.");
			
			log.debug("Calculando peso...");
			peso = this.inventarioSelected.getPeso();
			
			cantidad = new BigDecimal(this.inventarioSelected.getCantidad()).setScale(3, BigDecimal.ROUND_HALF_UP);
			pesoUnitario = peso.divide(cantidad, BigDecimal.ROUND_HALF_UP);
			pesoSalida = pesoUnitario
					.multiply(new BigDecimal(this.detalleSalida.getCantidad()).setScale(3, BigDecimal.ROUND_HALF_UP))
					.setScale(3, BigDecimal.ROUND_HALF_UP);
		} catch (InventarioException ex) {
			mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_WARN;
			
			message = new FacesMessage(severity, "Aviso", mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
		} catch(Exception ex) {
			pesoSalida = new BigDecimal("0.000").setScale(3, BigDecimal.ROUND_HALF_UP);
		} finally {
			log.debug("Peso calculado: {}", pesoSalida);
			if(this.detalleSalida != null)
				this.detalleSalida.setPeso(pesoSalida);
			PrimeFaces.current().ajax().update("form:det-peso", ":form:messages");
		}
	}
	
	public void deleteDetalleConstanciaSalida(DetalleConstanciaSalida detalle) {
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		
		try {
			if(this.detalleSalida == null)
				throw new InventarioException("Debe indicar un producto.");
			
			this.listadoTemp.remove(detalleSalida);
			
			this.cantidadTotal = 0;
			this.pesoTotal = new BigDecimal("0.000").setScale(3, BigDecimal.ROUND_HALF_UP);
			for(DetalleConstanciaSalida dcs : listadoTemp ) {
				this.cantidadTotal += dcs.getCantidad();
				this.pesoTotal = this.pesoTotal.add(dcs.getPeso());
			}
		
			detalleSalida.getProducto();
			detalleSalida.getFolioEntrada();
			
			Inventario inventarioTemp = new Inventario();
			
			for(Inventario i: listaInventarioCopy) {				
				if((i.getProducto().getProductoDs().equals(detalleSalida.getProducto())) && (i.getFolioCliente().equals(detalleSalida.getFolioEntrada()))   ) {
					inventarioTemp = i;
					break;
				}				
			}
			
			listaInventario.add(inventarioTemp);
			
		} catch (InventarioException ex) {
			mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_WARN;
			
			message = new FacesMessage(severity, "Productos", mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
		} catch (Exception ex) {
			log.error("Problema para eliminar el producto de la salida...", ex);
			mensaje = "Ha ocurrido un error en el sistema. Intente nuevamente.\nSi el problema persiste, por favor comuniquese con su administrador del sistema.";
			severity = FacesMessage.SEVERITY_ERROR;
			
			message = new FacesMessage(severity, "Productos", mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
		} finally {
			PrimeFaces.current().ajax().update("form:messages","form:dt-salidas");
		}
	}
	
	public void deleteConstanciaSalidaServicio(ConstanciaSalidaServicios csServicio) {
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		
		try {
			if(this.detalleSalida == null)
				throw new InventarioException("Debe indicar un servicio.");
			
			this.listadoConstanciaSalidaServicios.remove(csServicio);
		} catch (InventarioException ex) {
			mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_WARN;
			
			message = new FacesMessage(severity, "Servicio", mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
		} catch (Exception ex) {
			log.error("Problema para eliminar el servicio de la salida...", ex);
			mensaje = "Ha ocurrido un error en el sistema. Intente nuevamente.\nSi el problema persiste, por favor comuniquese con su administrador del sistema.";
			severity = FacesMessage.SEVERITY_ERROR;
			
			message = new FacesMessage(severity, "Productos", mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
		} finally {
			PrimeFaces.current().ajax().update("form:messages","form:dt-salidas");
		}
	}
	
	public void resetTemperaturaTransporte() {
		this.temperaturaTransporte = null;
	}
	
	public void resetCantidadServicio() {
		this.cantidadServicio = new BigDecimal("0.00").setScale(2, BigDecimal.ROUND_HALF_UP);
		log.debug("Reiniciando el valor de cantidad total (servicio)");
	}
	
	public synchronized void saveConstanciaSalida() {
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		String titulo = "Guardar constancia";
		BigDecimal saldoTotal = null;
		
		ConstanciaSalida constancia = new ConstanciaSalida();
		String folioCliente = null;
		
		String respuesta = null;
		
		try {
			log.info("El usuario {} intenta guardar una constancia de salida...", this.usuario.getUsuario());
			if(saved)
				throw new InventarioException("La constancia ya está registrada.");
			
			folioCliente = constanciaSalidaDAO.buscarPorNumero(this.numFolio);
			if(folioCliente != null && folioCliente.length() > 0 )
				throw new InventarioException(String.format("La constancia ya está registrada: %s", folioCliente));
			
			if(this.numFolio == null || "".equalsIgnoreCase(this.numFolio.trim()))
				throw new InventarioException("No se ha indicado un folio para la constancia de salida.");
			
			saldoTotal = (this.saldo == null ? new BigDecimal("0.00").setScale(2, BigDecimal.ROUND_HALF_UP) : this.saldo.getSaldo());
			
			log.info("Peso: {} kg, Cantidad: {} unidades.", this.pesoTotal, this.cantidadTotal);
			log.info("En inventario: {} unidades", this.cantidadInventario);
			log.info("Saldo: {}", saldoTotal);
			
			
			if(this.cantidadInventario.compareTo(new BigDecimal(this.cantidadTotal).setScale(2, BigDecimal.ROUND_HALF_UP)) <= 0
				&& this.candadoSalida.isSalidaTotal() == false && saldoTotal.compareTo(BigDecimal.ZERO) > 0)
				throw new InventarioException("El cliente no puede sacar toda su mercancía hasta liquidar sus adeudos.");
			
			if(placas == null)
				throw new InventarioException("Debe indicar las placas del vehículo");
			
			if(placas.trim().equalsIgnoreCase(""))
				throw new InventarioException("Debe indicar las placas del vehículo.");
			
			if(nombreTransportista == null)
				throw new InventarioException("Debe indicar el nombre del transportista.");
			
			if(nombreTransportista.trim().equalsIgnoreCase(""))
				throw new InventarioException("Debe indicar el nombre del transportista.");
			
			if(statusTermo == null)
				throw new InventarioException("Debe indicar si el transporte cuenta con equipo térmico.");
			
			if(statusTermo == true && temperaturaTransporte == null)
				throw new InventarioException("Debe indicar la temperatura del transporte.");
			
			constancia.setFecha(fechaSalida);
			constancia.setNumero(numFolio);
			constancia.setClienteCve(clienteSelect);
			constancia.setNombreCte(clienteSelect.getNombre());
			constancia.setStatus(status);
			constancia.setObservaciones(observaciones);
			constancia.setNombreTransportista(nombreTransportista);
			constancia.setPlacasTransporte(placas);
			constancia.setStatusTermo(statusTermo);
			constancia.setTemperaturaTransporte(temperaturaTransporte);
			constancia.setConstanciaSalidaServiciosList(listadoConstanciaSalidaServicios);
			constancia.setDetalleConstanciaSalidaList(listadoTemp);
			log.info("Constancia salida: (numero = {}), (fecha = {}), (cliente = {})", constancia.getNumero(), constancia.getFecha(), constancia.getNombreCte());
			
	 		for(ConstanciaSalidaServicios c: listadoConstanciaSalidaServicios) {
	 			Servicio servicioCve = c.getConstanciaSalidaServiciosPK().getServicioCve();
	 			c.getConstanciaSalidaServiciosPK().setConstanciaSalidaCve(constancia);
	 			c.setIdConstancia(constancia);
	 			c.setServicioCve(servicioCve);
	 			log.debug("ConstanciaSalidaServicio: {}", c);
	 			log.info("Constancia salida servicio: (cantidad = {}), (servicio = {})", c.getNumCantidad(), servicioCve.getServicioDs());
			}
	 		
	 		for(DetalleConstanciaSalida d: listadoTemp) {
	 			log.debug("Partida: {}", d.getPartidaCve());
	 		}
	 		
	 		for(DetalleConstanciaSalida d: listadoTemp) {
	 			d.setId(null);
	 			d.setConstanciaCve(constancia);
	 			List<DetallePartida> detallePartidaList = d.getPartidaCve().getDetallePartidaList();
	 			DetallePartida detallePartida = detallePartidaList.get(detallePartidaList.size() - 1);
	 			detallePartida.setCantidadUManejo(detallePartida.getCantidadUManejo() - d.getCantidad());
	 			detallePartida.setCantidadUMedida(detallePartida.getCantidadUMedida().subtract(d.getPeso()));
	 			detallePartidaDAO.guardar(detallePartida);
	 			log.info("Detalle constancia salida: (cantidad = {}), (peso = {}), (producto = {})", d.getCantidad(), d.getPeso(), d.getProducto());
	 		}
	 		respuesta = constanciaSalidaDAO.guardar(constancia); //REGISTRO LA CONSTANCIA SALIDA
	 		if(respuesta != null)
	 			throw new InventarioException("Existe un problema para guardar la constancia de salida.");
	 		
	 		saved = true;
	 		
	 		Integer numeroSerie = this.serie.getNuSerie() + 1;
			this.serie.setNuSerie(numeroSerie);
			respuesta = serieConstanciaDAO.actualizar(this.serie);
			if(respuesta != null)
				throw new InventarioException("Existe un problema para actualizar el folio de la serie de salida.");
	 		
	 		if(!(constancia.getConstanciaSalidaServiciosList().isEmpty())) {
	 			
	 			EstadoConstancia estadoConstancia = new EstadoConstancia();
	 			estadoConstancia.setEdoCve(1);
	 			estadoConstancia.setDescripcion("NUEVA");
	 			
	 			constanciaDeServicio.setCteCve(clienteSelect);
	 			constanciaDeServicio.setFecha(fechaSalida);
	 			constanciaDeServicio.setNombreTransportista(nombreTransportista);
	 			constanciaDeServicio.setPlacasTransporte(placas);
	 			constanciaDeServicio.setObservaciones(String.format("Salida %s - %s",this.numFolio , observaciones));
	 			constanciaDeServicio.setFolioCliente("S"+constancia.getNumero());
	 			constanciaDeServicio.setValorDeclarado(new BigDecimal(1));
	 			constanciaDeServicio.setStatus(estadoConstancia);
	 			
	 			List<ConstanciaServicioDetalle> constanciaServicioDetalles = new ArrayList<ConstanciaServicioDetalle>(); 
	 			List<PartidaServicio> partidaServicios = new ArrayList<PartidaServicio>(); 
	 			
	 			for(ConstanciaSalidaServicios css:listadoConstanciaSalidaServicios) {
	 				ConstanciaServicioDetalle constanciaServicioDetalle = new ConstanciaServicioDetalle();
	 				constanciaServicioDetalle.setServicioCve(css.getConstanciaSalidaServiciosPK().getServicioCve());
	 				constanciaServicioDetalle.setFolio(constanciaDeServicio);
	 				constanciaServicioDetalle.setServicioCantidad(css.getNumCantidad());
	 				constanciaServicioDetalles.add(constanciaServicioDetalle);
	 			}
	 			constanciaDeServicio.setConstanciaServicioDetalleList(new ArrayList<>());
	 			constanciaDeServicio.setConstanciaServicioDetalleList(constanciaServicioDetalles);
	 			
	 			for(DetalleConstanciaSalida dcs: listadoTemp) {
	 				
	 				PartidaServicio partidaS = new PartidaServicio();
	 				partidaS.setFolio(constanciaDeServicio);
	 				partidaS.setCantidadDeCobro(dcs.getPeso());
	 				partidaS.setCantidadTotal(dcs.getCantidad());
	 				partidaS.setProductoCve(dcs.getPartidaCve().getUnidadDeProductoCve().getProductoCve());
	 				partidaS.setUnidadDeManejoCve(dcs.getPartidaCve().getUnidadDeProductoCve().getUnidadDeManejoCve());
	 				partidaS.setUnidadDeCobro(dcs.getPartidaCve().getUnidadDeProductoCve().getUnidadDeManejoCve());
	 				partidaServicios.add(partidaS);
	 			}
	 			
	 			constanciaDeServicio.setPartidaServicioList(new ArrayList<>());
	 			constanciaDeServicio.setPartidaServicioList(partidaServicios);
	 			respuesta = constanciaServicioDAO.guardar(constanciaDeServicio);
	 			if(respuesta != null) {
	 				throw new InventarioException("Existe un problema para guardar la constancia de servicio.");
	 			}
	 		}
	 		int numSalidas = this.candadoSalida.getNumSalidas() > 0 ? (this.candadoSalida.getNumSalidas() - 1) : 0;
	 		boolean isHabilitado = numSalidas > 0 ? true : false;
	 		
	 		this.candadoSalida.setNumSalidas(numSalidas);
	 		this.candadoSalida.setHabilitado(isHabilitado);
	 		this.candadoSalida.setSalidaTotal(false);
	 		respuesta = this.candadoDAO.actualizar(candadoSalida);
	 		
	 		if(respuesta != null )
	 			throw new InventarioException("Ocurrió un problema para actualizar el candado de salida del cliente.");
	 		
	 		log.info("El usuario {} guardó correctamente la constancia de salida {}", this.usuario.getUsuario(), this.numFolio);
	 		mensaje = "La constancia de salida se guardó correctamente.";
			severity = FacesMessage.SEVERITY_INFO;
		} catch (InventarioException ex) {
			mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_WARN;
		} catch (Exception ex) {
			log.error("Problema para eliminar el servicio de la salida...", ex);
			mensaje = "Ha ocurrido un error en el sistema. Intente nuevamente.\nSi el problema persiste, por favor comuniquese con su administrador del sistema.";
			severity = FacesMessage.SEVERITY_ERROR;
		} finally {
			message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
			
			PrimeFaces.current().ajax().update("form:messages");
		}
	}
	
	public void nuevoRegistro() {
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
	    try {
			ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
		} catch (IOException e) {
			log.error("Problema para crear una nueva constancia de salida...",  e);
		}
		
	}
	
	public void imprimirTicket() {
		
		String jasperPath = "/jasper/ConstanciaSalida.jrxml";
		String filename = String.format("ticket-salida-%s.pdf", this.numFolio);
		String images = "/images/logoF.png";
		String message = null;
		Severity severity = null;
		ConstanciaSalida constancia = null;
		File reportFile = new File(jasperPath);
		File imgFile = null;
		JasperReportUtil jasperReportUtil = new JasperReportUtil();
		Map<String, Object> parameters = new HashMap<String, Object>();
		Connection connection = null;
		parameters = new HashMap<String, Object>();
		try {
			
			URL resource = getClass().getResource(jasperPath);//verifica si el recurso esta disponible 
			URL resourceimg = getClass().getResource(images); 
			String file = resource.getFile();//retorna la ubicacion del archivo
			String img = resourceimg.getFile();
			reportFile = new File(file);//crea un archivo
			imgFile = new File(img);
			constancia = new ConstanciaSalida();
			constancia.setNumero(this.numFolio);
			numFolio = String.valueOf(getNumFolio());
			connection = EntityManagerUtil.getConnection();
			parameters.put("REPORT_CONNECTION", connection);
			parameters.put("NUMERO", numFolio);
			parameters.put("LogoPath", imgFile.getPath());
			jasperReportUtil.createPdf(filename, parameters, reportFile.getPath());
			   
		} catch (Exception e) {
			log.error("Ocurrió un problema al imprimir el ticket de la constancia de salida...", e);
			message = String.format("No se pudo imprimir el folio %s", this.numFolio);
			severity = FacesMessage.SEVERITY_INFO;
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity,"Error en impresion",message));
			PrimeFaces.current().ajax().update("form:messages");
		}finally {
			conexion.close((Connection) connection);
		}
		
		
	}

	public List<Cliente> getListadoClientes() {
		return listadoClientes;
	}

	public void setListadoClientes(List<Cliente> listadoClientes) {
		this.listadoClientes = listadoClientes;
	}

	public Cliente getClienteSelect() {
		return clienteSelect;
	}

	public void setClienteSelect(Cliente clienteSelect) {
		this.clienteSelect = clienteSelect;
	}

	public List<Planta> getListadoPlantas() {
		return listadoPlantas;
	}

	public void setListadoPlantas(List<Planta> listadoPlantas) {
		this.listadoPlantas = listadoPlantas;
	}

	public PlantaDAO getPlantaDAO() {
		return plantaDAO;
	}

	public void setPlantaDAO(PlantaDAO plantaDAO) {
		this.plantaDAO = plantaDAO;
	}

	public Planta getPlantaSelect() {
		return plantaSelect;
	}

	public void setPlantaSelect(Planta plantaSelect) {
		this.plantaSelect = plantaSelect;
	}

	public Date getFechaSalida() {
		return fechaSalida;
	}

	public void setFechaSalida(Date fechaSalida) {
		this.fechaSalida = fechaSalida;
	}

	public String getNumFolio() {
		return numFolio;
	}

	public void setNumFolio(String numFolio) {
		this.numFolio = numFolio;
	}
	
	public List<ConstanciaSalida> getListadoConstanciasSalidas() {
		return listadoConstanciasSalidas;
	}

	public void setListadoConstanciasSalidas(List<ConstanciaSalida> listadoConstanciasSalidas) {
		this.listadoConstanciasSalidas = listadoConstanciasSalidas;
	}

	public List<ConstanciaDeDeposito> getListadoConstanciaDD() {
		return listadoConstanciaDD;
	}

	public void setListadoConstanciaDD(List<ConstanciaDeDeposito> listadoConstanciaDD) {
		this.listadoConstanciaDD = listadoConstanciaDD;
	}

	public List<PrecioServicio> getListadoPrecioServicios() {
		return listadoPrecioServicios;
	}

	public void setListadoPrecioServicios(List<PrecioServicio> listadoPrecioServicios) {
		this.listadoPrecioServicios = listadoPrecioServicios;
	}

	public PrecioServicioDAO getPreciosServicioDAO() {
		return preciosServicioDAO;
	}

	public void setPreciosServicioDAO(PrecioServicioDAO preciosServicioDAO) {
		this.preciosServicioDAO = preciosServicioDAO;
	}

	public List<PrecioServicio> getServiciosCliente() {
		return serviciosCliente;
	}

	public void setServiciosCliente(List<PrecioServicio> serviciosCliente) {
		this.serviciosCliente = serviciosCliente;
	}

	public PrecioServicio getServicioClienteSelect() {
		return servicioClienteSelect;
	}

	public void setServicioClienteSelect(PrecioServicio servicioClienteSelect) {
		this.servicioClienteSelect = servicioClienteSelect;
	}

	public List<ConstanciaSalidaServicios> getListadoConstanciaSalidaServicios() {
		return listadoConstanciaSalidaServicios;
	}

	public void setListadoConstanciaSalidaServicios(List<ConstanciaSalidaServicios> listadoConstanciaSalidaServicios) {
		this.listadoConstanciaSalidaServicios = listadoConstanciaSalidaServicios;
	}

	public BigDecimal getCantidadServicio() {
		return cantidadServicio;
	}

	public void setCantidadServicio(BigDecimal cantidadServicio) {
		this.cantidadServicio = cantidadServicio;
	}

	public Partida getPartidaSelect() {
		return partidaSelect;
	}

	public void setPartidaSelect(Partida partidaSelect) {
		this.partidaSelect = partidaSelect;
	}

	public List<Partida> getListadoPartida() {
		return listadoPartida;
	}

	public void setListadoPartida(List<Partida> listadoPartida) {
		this.listadoPartida = listadoPartida;
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

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public String getTemperatura() {
		return temperatura;
	}

	public void setTemperatura(String temperatura) {
		this.temperatura = temperatura;
	}

	public List<DetalleConstanciaSalida> getListadoDetalleConstanciaSalida() {
		return listadoDetalleConstanciaSalida;
	}

	public void setListadoDetalleConstanciaSalida(List<DetalleConstanciaSalida> listadoDetalleConstanciaSalida) {
		this.listadoDetalleConstanciaSalida = listadoDetalleConstanciaSalida;
	}
	
	public int getCantidadTotal() {
		return cantidadTotal;
	}

	public void setCantidadTotal(int cantidadTotal) {
		this.cantidadTotal = cantidadTotal;
	}

	public BigDecimal getPesoTotal() {
		return pesoTotal;
	}

	public void setPesoTotal(BigDecimal pesoTotal) {
		this.pesoTotal = pesoTotal;
	}

	public List<DetalleConstanciaSalida> getListadoTemp() {
		return listadoTemp;
	}

	public void setListadoTemp(List<DetalleConstanciaSalida> listadoTemp) {
		this.listadoTemp = listadoTemp;
	}
	
	public DetallePartida getDetallePartida() {
		return detallePartida;
	}

	public void setDetallePartida(DetallePartida detallePartida) {
		this.detallePartida = detallePartida;
	}

	public List<DetallePartida> getDetallePartidaLista() {
		return detallePartidaLista;
	}

	public void setDetallePartidaLista(List<DetallePartida> detallePartidaLista) {
		this.detallePartidaLista = detallePartidaLista;
	}
	
	public List<Inventario> getListaInventario() {
		return listaInventario;
	}

	public void setListaInventario(List<Inventario> listaInventario) {
		this.listaInventario = listaInventario;
	}

	public InventarioDAO getInventarioDAO() {
		return inventarioDAO;
	}

	public void setInventarioDAO(InventarioDAO inventarioDAO) {
		this.inventarioDAO = inventarioDAO;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public DetalleConstanciaSalida getDetalleSalida() {
		return detalleSalida;
	}

	public void setDetalleSalida(DetalleConstanciaSalida detalleSalida) {
		this.detalleSalida = detalleSalida;
	}

	public Inventario getInventarioSelected() {
		return inventarioSelected;
	}

	public void setInventarioSelected(Inventario inventarioSelected) {
		this.inventarioSelected = inventarioSelected;
	}

	public ConstanciaSalidaServicios getConstanciaSalidaServicio() {
		return constanciaSalidaServicio;
	}

	public void setConstanciaSalidaServicio(ConstanciaSalidaServicios constanciaSalidaServicio) {
		this.constanciaSalidaServicio = constanciaSalidaServicio;
	}

	public boolean isSaved() {
		return saved;
	}

	public void setSaved(boolean saved) {
		this.saved = saved;
	}

	public List<Inventario> getFilteredInventario() {
		return filteredInventario;
	}

	public void setFilteredInventario(List<Inventario> filteredInventario) {
		this.filteredInventario = filteredInventario;
	}

	public List<String> getListaEntradas() {
		return listaEntradas;
	}

	public void setListaEntradas(List<String> listaEntradas) {
		this.listaEntradas = listaEntradas;
	}

	public List<Date> getListaIngresos() {
		return listaIngresos;
	}

	public void setListaIngresos(List<Date> listaIngresos) {
		this.listaIngresos = listaIngresos;
	}

	public List<Inventario> getListaInventarioCopy() {
		return listaInventarioCopy;
	}

	public void setListaInventarioCopy(List<Inventario> listaInventarioCopy) {
		this.listaInventarioCopy = listaInventarioCopy;
	}

	public boolean isSaldoVencido() {
		return saldoVencido;
	}

	public void setSaldoVencido(boolean saldoVencido) {
		this.saldoVencido = saldoVencido;
	}

	public Boolean getStatusTermo() {
		return statusTermo;
	}

	public void setStatusTermo(Boolean statusTermo) {
		this.statusTermo = statusTermo;
	}

	public BigDecimal getTemperaturaTransporte() {
		return temperaturaTransporte;
	}

	public void setTemperaturaTransporte(BigDecimal temperaturaTransporte) {
		this.temperaturaTransporte = temperaturaTransporte;
	}
}