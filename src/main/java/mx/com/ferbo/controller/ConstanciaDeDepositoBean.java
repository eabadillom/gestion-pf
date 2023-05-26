package mx.com.ferbo.controller;

import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.component.UISelectItems;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.primefaces.PrimeFaces;

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
import mx.com.ferbo.model.DetallePartidaPK;
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
import mx.com.ferbo.model.TipoMovimiento;
import mx.com.ferbo.model.UnidadDeManejo;
import mx.com.ferbo.model.UnidadDeProducto;
import mx.com.ferbo.util.EntityManagerUtil;
import mx.com.ferbo.util.InventarioException;
import mx.com.ferbo.util.JasperReportUtil;
import mx.com.ferbo.util.conexion;

@Named
@ViewScoped
public class ConstanciaDeDepositoBean implements Serializable{
	
	private static final long serialVersionUID = -1785488265380235016L;
	private static Logger log = Logger.getLogger(ConstanciaDeDepositoBean.class);
	
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
	
	private List<Cliente> listadoCliente;
	private List<Planta> listadoPlanta;
	private List<Camara> camaras;
	private List<Camara> camaraPorPlanta;
	private List<ConstanciaDeDeposito> listadoConstancia;
	private List<ProductoPorCliente> productoCliente;
	private List<Producto> listadoProducto;//nueva
	private List<Producto> productoC;
	private List<UnidadDeManejo> listadoUnidadDeManejo;
	private List<Posicion> listaPosiciones;
	private List<Posicion> posiciones;
	private List<Aviso> avisoPorCliente;
	private List<Partida> listadoPartida;//lista donde se guardan los objetos pero no se guardan en BD
	private List<DetallePartida> listadoDetallePartida;
	private List<Servicio> listadoServicio;
	private List<PrecioServicio> listadoPrecioServicio;//
	private List<PrecioServicio> listaServicioUnidad;
	private List<ConstanciaDepositoDetalle> listadoConstanciaDepositoDetalle;//se van agregar constanciadepositodetalle al dar añadir
	private List<Partida> selectedPartidas;
	
	private Planta plantaSelect;
	private Cliente clienteSelect;
	private ProductoPorCliente productoPorCliente;//nueva
	private Camara camaraSelect;
	private Producto producto;
	private Integer productoSelect;
	private UISelectItems productoItems;
	private UnidadDeManejo unidadDeManejoSelect;
	private Posicion posicionCamaraSelect;
	private Aviso avisoSelect;
	private PrecioServicio precioServicioSelect;
	private ConstanciaDepositoDetalle selectedConstanciasDD;
	private Partida selectedPartida;
	private Partida partida;
	private DetallePartida detalle;
	private TipoMovimiento tipoMovimiento;

	private String noConstanciaSelect;
	private BigDecimal unidadesPorTarima;
	private BigDecimal numTarimas;
	private BigDecimal cantidadTotal;
	private BigDecimal pesoTotal;
	private BigDecimal valorMercancia;
	private String pedimento,contenedor,lote,otro;
	private Boolean isCongelacion,isConservacion,isRefrigeracion,isManiobras;
	private int congelacion=2,conservacion=32,refrigeracion=33,maniobras=34;
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
	
	
	public ConstanciaDeDepositoBean() {
		clienteDAO = new ClienteDAO();
		plantaDAO = new PlantaDAO();
		camaraDAO = new CamaraDAO();
		avisoDAO = new AvisoDAO();
		constanciaDAO = new ConstanciaDeDepositoDAO();
		productoClienteDAO = new ProductoClienteDAO();//nueva
		productoDAO = new ProductoDAO();
		unidadDeManejoDAO = new UnidadDeManejoDAO();
		posicionCamaraDAO = new PosicionCamaraDAO();
		servicioDAO = new ServicioDAO();
		precioServicioDAO = new PrecioServicioDAO();
		unidadDeProductoDAO = new UnidadDeProductoDAO();
		tipoMovimientoDAO = new TipoMovimientoDAO();
		estadoInventarioDAO = new EstadoInventarioDAO();
		serieConstanciaDAO = new SerieConstanciaDAO();
		
		listadoPlanta = new ArrayList<>();
		listadoCliente = new ArrayList<>();
		camaraPorPlanta = new ArrayList<Camara>();
		productoC = new ArrayList<Producto>();
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
		estadoConstanciaDAO = new EstadoConstanciaDAO();
		
	}
	
	@PostConstruct
	public void init(){
		listadoCliente = clienteDAO.buscarHabilitados(true);
		listadoPlanta = plantaDAO.findall();
		this.listadoUnidadDeManejo = unidadDeManejoDAO.buscarTodos();
		tipoMovimiento = tipoMovimientoDAO.buscarPorId(1);
		estadoInventario = estadoInventarioDAO.buscarPorId(1);
		
		constanciaDeDeposito = new ConstanciaDeDeposito();
		partida = this.newPartida();
		detalle = this.newDetallePartida();
		
		cantidadTotal = null;
		
		isCongelacion=false;
		isConservacion=false;
		isRefrigeracion=false;
		isManiobras=false;
		fechaIngreso = new Date();
		
		showCaducidad = false;
		showLote = false;
		showOtro = false;
		showPedimento = false;
		showSAP = false;
	}
	
	public void generaFolioEntrada() {
		SerieConstanciaPK seriePK = null;
		SerieConstancia serie = null;
		
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		
		try {
			if(this.clienteSelect == null)
				throw new InventarioException("Debe seleccionar un cliente");
			
			if(this.plantaSelect == null)
				throw new InventarioException("Debe seleccionar una planta");
			
			seriePK = new SerieConstanciaPK();
			seriePK.setIdCliente(this.clienteSelect.getCteCve());
			seriePK.setTpSerie("I");
			serie = serieConstanciaDAO.buscarPorId(seriePK);
			
			if(serie == null) {
				this.noConstanciaSelect = "";
				throw new InventarioException("No se encontró información de los folios del cliente. Debe indicar manualmente un folio de constancia.");
			}
			
			this.noConstanciaSelect =
				String.format("%s%s%s-%d",
						seriePK.getTpSerie(),
						plantaSelect.getPlantaSufijo(),
						clienteSelect.getCodUnico(),
						serie.getNuSerie()
			);
			
			severity = FacesMessage.SEVERITY_INFO;
			mensaje = "Folio generado: " + this.noConstanciaSelect;
		} catch (InventarioException ex) {
			mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_WARN;
		} catch (Exception ex) {
			log.error("Problema para generar el folio de entrada...", ex);
			mensaje = "Ha ocurrido un error en el sistema. Intente nuevamente.\nSi el problema persiste, por favor comuniquese con su administrador del sistema.";
			severity = FacesMessage.SEVERITY_ERROR;
		} finally {
			message = new FacesMessage(severity, "Aviso", mensaje);
	        FacesContext.getCurrentInstance().addMessage(null, message);
	        PrimeFaces.current().ajax().update(":form:messages", ":form:numeroC");
		}
	}
	
	public void cargaInfoCliente() {
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		
		try {
			
			this.generaFolioEntrada();
			
			productoC.clear();
			productoPorCliente = new ProductoPorCliente(clienteSelect);
			productoCliente = productoClienteDAO.buscarPorCliente(clienteSelect.getCteCve(), true);
			for(ProductoPorCliente pc: productoCliente) {
				Producto p = pc.getProductoCve();
				productoC.add(p);
			}
			
			avisoPorCliente.clear();
			avisoPorCliente = avisoDAO.buscarPorCliente(clienteSelect.getCteCve());
			
			severity = FacesMessage.SEVERITY_INFO;
			mensaje = "Seleccione un aviso";
		//} catch (InventarioException ex) {
		//	mensaje = ex.getMessage();
		//	severity = FacesMessage.SEVERITY_ERROR;
		} catch (Exception ex) {
			mensaje = "Ha ocurrido un error en el sistema. Intente nuevamente.\nSi el problema persiste, por favor comuniquese con su administrador del sistema.";
			severity = FacesMessage.SEVERITY_ERROR;
		} finally {
			message = new FacesMessage(severity, "Aviso", mensaje);
	        FacesContext.getCurrentInstance().addMessage(null, message);
	        PrimeFaces.current().ajax().update(":form:messages");
		}
	}
	
	public void filtrarPosicion() {
		Posicion posicion = new Posicion();
		posicion.setPlanta(plantaSelect);
		posicion.setCamara(camaraSelect);
		posiciones = posicionCamaraDAO.buscarPorCriterios(posicion);
	}
	
	private Partida newPartida() {
		Partida partida = null;
		UnidadDeProducto udp = null;
		
		partida = new Partida();
		partida.setCantidadTotal(0);
		partida.setPesoTotal(new BigDecimal("0.000").setScale(3, BigDecimal.ROUND_HALF_UP));
		partida.setNoTarimas(new BigDecimal("0").setScale(1, BigDecimal.ROUND_HALF_UP));
		udp = new UnidadDeProducto();
		udp.setUnidadDeManejoCve(new UnidadDeManejo());
		udp.setProductoCve(new Producto());
		partida.setUnidadDeProductoCve(udp);
		
		return partida;
		
	}
	
	private DetallePartida newDetallePartida() {
		DetallePartida detalle = null;
		detalle = new DetallePartida();
		detalle.setTipoMovCve(tipoMovimiento);
		detalle.setEdoInvCve(estadoInventario);
		return detalle;
	}
	
	public void verDatosPartida() {
		log.info("Cantidad: " + partida.getCantidadTotal());
		log.info("ProductoCve: " + partida.getUnidadDeProductoCve().getProductoCve().getProductoCve());
		log.info("UnidadDeManejoCve: " + partida.getUnidadDeProductoCve().getUnidadDeManejoCve().getUnidadDeManejoCve());
		log.info("Peso: " + partida.getCantidadTotal());
		log.info("Num. Tarimas: "  + partida.getNoTarimas());
		
	}
	
	public void addProducto(Producto producto) {
		this.productoSelect = producto.getProductoCve();
	}
	
	public void verProducto() {
		log.info("ProductoCve: " + productoSelect);
	}

	public void calculo() {
		BigDecimal unidadT = new BigDecimal(partida.getCantidadTotal()).setScale(2);
		BigDecimal numTarimas = partida.getNoTarimas();
		if(numTarimas == null)
			return;
		
		BigDecimal tarimas = unidadT.divide(numTarimas ,2,RoundingMode.HALF_UP);		
		this.unidadesPorTarima = new BigDecimal(tarimas.intValue()).setScale(2);
	}
	
	public void addPartida(){
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		
		UnidadDeProducto udp = null;
		UnidadDeManejo udm = null;
		Integer idUnidadDeManejo = null;
		Producto prd = null;
		Integer idProducto = null;
		
		try {
			udp = partida.getUnidadDeProductoCve();
			
			if(udp == null)
				throw new InventarioException("La selección de producto o unidad de manejo es incorrecta.");
			
			udm = udp.getUnidadDeManejoCve();
			if(udm == null)
				throw new InventarioException("Debe seleccionar una unidad de manejo.");
			
			prd = udp.getProductoCve();
			if(prd == null)
				throw new InventarioException("Debe seleccionar un producto.");
			
			idUnidadDeManejo = partida.getUnidadDeProductoCve().getUnidadDeManejoCve().getUnidadDeManejoCve();
			idProducto = partida.getUnidadDeProductoCve().getProductoCve().getProductoCve();
			prd = productoDAO.buscarPorId(idProducto);
			udm = unidadDeManejoDAO.buscarPorId(idUnidadDeManejo);
			udp = unidadDeProductoDAO.buscarPorProductoUnidad(idProducto, idUnidadDeManejo);
			
			if(udp == null) {
				log.info(String.format("No se encontró la unidad de producto. Se registrará una nueva: producto(%d) - unidad(%d)",prd.getProductoCve(), udm.getUnidadDeManejoCve()));
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
			
			DetallePartidaPK detallePk = new DetallePartidaPK();
			detallePk.setDetPartCve(1);
			detallePk.setPartidaCve(partida);
			detalle.setDetallePartidaPK(detallePk);
			detalle.setCantidadUManejo(partida.getCantidadTotal());
			detalle.setCantidadUMedida(partida.getPesoTotal());
			
			detalle.setPartida(partida);
			partida.add(detalle);
			this.listadoPartida.add(partida);
			
			this.partida = this.newPartida();
			this.detalle = this.newDetallePartida();
			
			severity = FacesMessage.SEVERITY_INFO;
			mensaje = "Agregado correctamente";
		} catch (InventarioException ex) {
			mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_ERROR;
		} catch (Exception ex) {
			mensaje = "Ha ocurrido un error en el sistema. Intente nuevamente.\nSi el problema persiste, por favor comuniquese con su administrador del sistema.";
			severity = FacesMessage.SEVERITY_ERROR;
		} finally {
			message = new FacesMessage(severity, "Producto", mensaje);
	        FacesContext.getCurrentInstance().addMessage(null, message);
	        PrimeFaces.current().ajax().update(":form:messages", ":form:seleccion-mercancia");
		}
	}
	
	public void saveConstanciaDepositoDetalle(){
		ConstanciaDepositoDetalle constanciaDD = new ConstanciaDepositoDetalle();
		constanciaDD.setServicioCve(precioServicioSelect.getServicio());
		constanciaDD.setFolio(constanciaDeDeposito);
		constanciaDD.setServicioCantidad(cantidadServicio);
		listadoConstanciaDepositoDetalle.add(constanciaDD);
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Agregado","Se agrego el registro correctamente"));
		PrimeFaces.current().ajax().update("form:messages","form:dt-constanciaDD");
		
	}
	
	public void renderConstanciaDeDeposito(){
		
		log.info("AvisoSelect: " + avisoSelect);
		
		this.showPedimento = avisoSelect.getAvisoPedimento();
		this.showSAP = avisoSelect.getAvisoSap();
		this.showLote = avisoSelect.getAvisoLote();
		this.showCaducidad = avisoSelect.getAvisoCaducidad();
		this.showOtro = avisoSelect.getAvisoOtro();
		
		
		//-------------- PRECIO SERVICIO -----------------
		listaServicioUnidad = precioServicioDAO.buscarPorAviso(avisoSelect, clienteSelect);
		
		//Remover servicios (*)
		isCongelacion=false;isRefrigeracion=false;isConservacion=false;isManiobras=false;
		//isRefrigeracion = false;
		List<PrecioServicio> precioServicioTemp = new ArrayList<PrecioServicio>();
		precioServicioTemp.clear();
		for(PrecioServicio ps: listaServicioUnidad) {
			if(((ps.getServicio().getServicioCve()==congelacion)) 
					|| ((ps.getServicio().getServicioCve()==conservacion)) 
					|| ((ps.getServicio().getServicioCve()==refrigeracion))
					|| ((ps.getServicio().getServicioCve()==maniobras))) {
				precioServicioTemp.add(ps);
				
			}
			
		}
		
		if(!(precioServicioTemp.isEmpty())) {
			for(PrecioServicio pt: precioServicioTemp) {
				if(pt.getServicio().getServicioCve()==conservacion) {
					isConservacion=true;
				}else if(pt.getServicio().getServicioCve()==refrigeracion) {
					isRefrigeracion=true;
				}else if(pt.getServicio().getServicioCve()==maniobras) {
					isManiobras = true;
				}else if(pt.getServicio().getServicioCve()==congelacion) {
					isCongelacion = true;
				}
			}
		}
		
		listaServicioUnidad.removeAll(precioServicioTemp);
		
		PrimeFaces.current().ajax().update(":form:txtPedimento", ":form:txtSAP", ":form:txtLote", ":form:fechaCaducidad", ":form:txtOtro", ":form:precioServicio");
	}
	
	public void limpiar() {
		productoSelect = null;
		unidadDeManejoSelect = new UnidadDeManejo();
		posicionCamaraSelect = new Posicion() ;
		posicionCamaraSelect = new Posicion();
		
		cantidadTotal = null;
		pesoTotal = new BigDecimal("0.00").setScale(2);
		numTarimas = new BigDecimal("0.00").setScale(2);
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
	}
	
	public void saveConstanciaDeDeposito() {
		
		EstadoConstancia status = estadoConstanciaDAO.buscarPorId(1);
		
		constanciaDeDeposito.setNombreTransportista(nombreTransportista);
		constanciaDeDeposito.setPlacasTransporte(placas);
		constanciaDeDeposito.setObservaciones(observacion);
		constanciaDeDeposito.setTemperatura(temperatura);
		constanciaDeDeposito.setCteCve(clienteSelect);
		constanciaDeDeposito.setFechaIngreso(fechaIngreso);
		constanciaDeDeposito.setFolioCliente(noConstanciaSelect);
		constanciaDeDeposito.setAvisoCve(avisoSelect);
		constanciaDeDeposito.setConstanciaDepositoDetalleList(listadoConstanciaDepositoDetalle);
		constanciaDeDeposito.setPartidaList(listadoPartida);
		constanciaDeDeposito.setStatus(status);
		constanciaDAO.guardar(constanciaDeDeposito);
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Agregado","Se agrego el registro correctamente"));
		PrimeFaces.current().ajax().update("form:messages");
		
	}
	
	public void deleteSelectedPartidas() {
		
		for(Partida p: listadoPartida) {
			if(p==selectedPartida) {
				p.setPartidaCve(1);
				selectedPartida.setPartidaCve(1);
				System.out.println(p);
				System.out.println(selectedPartida);
			}
		}
		
		listadoPartida.remove(this.selectedPartida);//remueve todos los elementos de listadoPartida ERROR
		this.selectedPartidas = null;
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Eliminado","Se elimino el registro correctamente"));
		PrimeFaces.current().ajax().update("form:messages","form:dt-partidas");
	}
	
	public void deleteConstanciaDD() {
		
		for(ConstanciaDepositoDetalle cdd: listadoConstanciaDepositoDetalle) {
			if(cdd==selectedConstanciasDD) {
				cdd.setConstanciaDepositoDetalleCve(1);
				selectedConstanciasDD.setConstanciaDepositoDetalleCve(1);
				System.out.println(cdd);
				System.out.println(selectedConstanciaDD);
			}
		}
		
		listadoConstanciaDepositoDetalle.remove(this.selectedConstanciasDD);//lo remueve cuando la constanciadepositodetallecve tiene un Id, mientras no lo hace 
		this.selectedConstanciasDD = null;
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Eliminado","Se elimino el registro correctamente"));
		PrimeFaces.current().ajax().update("form:messages","form:dt-constanciaDD");
	}
	
	public void imprimir() {
		String jasperPath = "/jasper/GestionReport.jrxml";
		String filename = "ticket.pdf";
		String images = "/images/logo.jpeg";
		String message = null;
		Severity severity = null;
		ConstanciaDeDeposito constancia = null;
		 File reportFile = new File(jasperPath);
		 File imgfile = null;
		JasperReportUtil jasperReportUtil = new JasperReportUtil();
		Map<String, Object> parameters = new HashMap<String, Object>();
		Connection connection = null;
		parameters = new HashMap<String, Object>();
		try {
			URL resource = getClass().getResource(jasperPath);
			URL resourceimg = getClass().getResource(images);
			String file = resource.getFile();
			String img = resourceimg.getFile();
			reportFile = new File(file);
			imgfile = new File(img);
			constancia = new ConstanciaDeDeposito();
			constancia.setFolioCliente(this.noConstanciaSelect);
			noConstanciaSelect = String.valueOf(getNoConstanciaSelect());
			connection = EntityManagerUtil.getConnection();
			parameters.put("REPORT_CONNECTION", connection);
			parameters.put("FOLIO", noConstanciaSelect);
			parameters.put("LogoPath",imgfile.getPath());
			jasperReportUtil.createPdf(filename, parameters,reportFile.getPath());		
		} catch (Exception ex) {
			ex.printStackTrace();
			message = String.format("No se pudo imprimir el folio %s", this.noConstanciaSelect);
			severity = FacesMessage.SEVERITY_INFO;
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, "Error en impresion", message));
			PrimeFaces.current().ajax().update("form:messages");
		} finally {
			conexion.close((Connection) connection);
		}
		
	}
	
	// ----------- Otros Metodos ------------------


	public void filtraListado() {
		camaraPorPlanta = camaraDAO.buscarPorPlanta(plantaSelect);
		this.generaFolioEntrada();
	}
	
	public void validar() {
		 
		String constanciaE = noConstanciaSelect.trim();
		constanciaE.trim();
		ConstanciaDeDeposito constancia = null;
		
		constancia = constanciaDAO.buscarPorFolioCliente(constanciaE);
		
		if(constancia == null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Folio correcto" ,"Capture sus productos."));
			PrimeFaces.current().ajax().update("form:messages","form:numeroC");
			return;
		}
		
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Folio Existente" ,"Digite un nuevo folio"));
		this.noConstanciaSelect = null;
		PrimeFaces.current().ajax().update("form:messages","form:numeroC");
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
	
	public String getNoConstanciaSelect() {
		return noConstanciaSelect;
	}

	public void setNoConstanciaSelect(String noConstanciaSelect) {
		this.noConstanciaSelect = noConstanciaSelect;
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
	
	public List<Producto> getProductoC() {
		return productoC;
	}

	public void setProductoC(List<Producto> productoC) {
		this.productoC = productoC;
	}
	
	public Camara getCamaraSelect() {
		return camaraSelect;
	}

	public void setCamaraSelect(Camara camaraSelect) {
		this.camaraSelect = camaraSelect;
	}
	
	public BigDecimal getNumTarimas() {
		return numTarimas;
	}

	public void setNumTarimas(BigDecimal numTarimas) {
		this.numTarimas = numTarimas;
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
}
