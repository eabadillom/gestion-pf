package mx.com.ferbo.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Connection;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import mx.com.ferbo.dao.CamaraDAO;
import mx.com.ferbo.dao.ConstanciaSalidaDAO;
import mx.com.ferbo.dao.ConstanciaServicioDAO;
import mx.com.ferbo.dao.DetallePartidaDAO;
import mx.com.ferbo.dao.EstadoConstanciaDAO;
import mx.com.ferbo.dao.EstadoInventarioDAO;
import mx.com.ferbo.dao.OrdenSalidaDAO;
import mx.com.ferbo.dao.PartidaDAO;
import mx.com.ferbo.dao.PlantaDAO;
import mx.com.ferbo.dao.PreSalidaServicioDAO;
import mx.com.ferbo.dao.PrecioServicioDAO;
import mx.com.ferbo.dao.ProductoDAO;
import mx.com.ferbo.dao.SerieConstanciaDAO;
import mx.com.ferbo.dao.StatusConstanciaSalidaDAO;
import mx.com.ferbo.dao.UnidadDeManejoDAO;
import mx.com.ferbo.model.Camara;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.ConstanciaDeServicio;
import mx.com.ferbo.model.ConstanciaSalida;
import mx.com.ferbo.model.ConstanciaSalidaServicios;
import mx.com.ferbo.model.ConstanciaSalidaServiciosPK;
import mx.com.ferbo.model.ConstanciaServicioDetalle;
import mx.com.ferbo.model.DetalleConstanciaSalida;
import mx.com.ferbo.model.DetallePartida;
import mx.com.ferbo.model.EstadoConstancia;
import mx.com.ferbo.model.EstadoInventario;
import mx.com.ferbo.model.OrdenSalida;
import mx.com.ferbo.model.Partida;
import mx.com.ferbo.model.PartidaServicio;
import mx.com.ferbo.model.Planta;
import mx.com.ferbo.model.PreSalidaServicio;
import mx.com.ferbo.model.PrecioServicio;
import mx.com.ferbo.model.Producto;
import mx.com.ferbo.model.SerieConstancia;
import mx.com.ferbo.model.SerieConstanciaPK;
import mx.com.ferbo.model.StatusConstanciaSalida;
import mx.com.ferbo.model.UnidadDeManejo;
import mx.com.ferbo.model.Usuario;
import mx.com.ferbo.ui.OrdenDeSalidas;
import mx.com.ferbo.ui.PreSalidaUI;
import mx.com.ferbo.util.DateUtil;
import mx.com.ferbo.util.EntityManagerUtil;
import mx.com.ferbo.util.InventarioException;
import mx.com.ferbo.util.JasperReportUtil;
import mx.com.ferbo.util.conexion;

@Named
@ViewScoped
public class OrdenSalidaBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private static Logger log = LogManager.getLogger(OrdenSalidaBean.class);

	private List<Cliente> listaClientes;
	private List<OrdenSalida> listaOrdenSalida;
	private List<OrdenDeSalidas> ordenesDeSalida;
	private List<PrecioServicio> listaServicios;
	private List<PreSalidaServicio> listaPreSalidaServicio;
	private List<String> listaFolios;
	private List<OrdenSalida> listaSalidasporFolio;
	private List<PreSalidaUI> listaPreSalidaUI;
	
	private OrdenSalidaDAO ordenSalidaDAO;
	private PrecioServicioDAO precioServicioDAO;
	private PreSalidaServicioDAO presalidaservicioDAO;
	private PartidaDAO partidaDAO;
	private CamaraDAO camaraDAO;
	private ConstanciaSalidaDAO constanciaDAO;
	private DetallePartidaDAO dpDAO;
	private EstadoInventarioDAO estadoInventarioDAO;
	private EstadoConstanciaDAO estadoConstanciaDAO;
	private ProductoDAO productoDAO;
	private	UnidadDeManejoDAO unidadDAO ;
	private StatusConstanciaSalidaDAO statusConstanciaSalidaDAO;
	private ConstanciaServicioDAO constanciaServicioDAO;
	private PlantaDAO plantaDAO;
	private SerieConstanciaDAO serieConstanciaDAO;

	
	private boolean confirmacion;
	private boolean pdf;
	private boolean excel;
	private String folioSelected;
	private Date fecha;
	private Time tmSalida;
	private String detalleAnterior;
	private String detalleActual;
	private Integer cantidad;
	private BigDecimal peso;
	private String observaciones;
	private Integer cantidadServicio;
	private StreamedContent file;

	
	private Cliente clienteSelect;
	private Planta plantaSelect;
	private OrdenSalida ordensalida;
	private DetallePartida dp;
	private DetalleConstanciaSalida dcs;
	private OrdenDeSalidas ordenDeSalidas;
	private PrecioServicio servicioSelect;
	private PreSalidaServicio pss;
	private PreSalidaUI psu;
	private EstadoInventario estadoInventarioActual;
	private EstadoInventario estadoInventarioHistorico;
	private EstadoConstancia estadoConstancia;
	private SerieConstancia serie;
	
	private String folioSalida;
	private String folioServicio;
	private boolean isSalidaSaved = false;
	private boolean isServicioSaved = false;
	
	private FacesContext context;
    private HttpServletRequest request;
    private HttpSession session;
    private Usuario usuario;
    
    @Inject
    private SideBarBean sideBar;


	public OrdenSalidaBean() {
		ordenSalidaDAO = new OrdenSalidaDAO();
		presalidaservicioDAO = new PreSalidaServicioDAO();
		precioServicioDAO = new PrecioServicioDAO();
		estadoConstanciaDAO = new EstadoConstanciaDAO();
		partidaDAO = new PartidaDAO();
		camaraDAO = new CamaraDAO();
		productoDAO = new ProductoDAO();
		constanciaDAO = new ConstanciaSalidaDAO();
		unidadDAO = new UnidadDeManejoDAO();
		dpDAO = new DetallePartidaDAO();
		estadoInventarioDAO = new EstadoInventarioDAO();
		statusConstanciaSalidaDAO = new StatusConstanciaSalidaDAO();
		constanciaServicioDAO = new ConstanciaServicioDAO();
		plantaDAO = new PlantaDAO();
		serieConstanciaDAO = new SerieConstanciaDAO();

		
		listaOrdenSalida = new ArrayList<OrdenSalida>();
		listaClientes = new ArrayList<Cliente>();
		listaPreSalidaUI = new ArrayList<>();
		listaServicios = new ArrayList<PrecioServicio>();
		ordenesDeSalida = new ArrayList<OrdenDeSalidas>();
	}

	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() {
		try {
			context = FacesContext.getCurrentInstance();
			request = (HttpServletRequest) context.getExternalContext().getRequest();
			session = request.getSession(false);
			
			this.usuario = (Usuario) session.getAttribute("usuario");
			this.serie = new SerieConstancia();
			
			listaClientes = (List<Cliente>) request.getSession(false).getAttribute("clientesActivosList");
			fecha = new Date();
			DateUtil.setTime(fecha, 0, 0, 0, 0);
			estadoInventarioActual = estadoInventarioDAO.buscarPorId(1);
			estadoInventarioHistorico = estadoInventarioDAO.buscarPorId(2);
			estadoConstancia = estadoConstanciaDAO.buscarPorId(1);
			byte bytes[] = {};
			this.file = DefaultStreamedContent.builder()
					.contentType("application/pdf")
					.contentLength(bytes.length)
					.name("factura.pdf")
					.stream(() -> new ByteArrayInputStream(bytes) )
					.build();
			
		} catch(Exception ex) {
			
		} finally {
			
		}
	}

	public void filtrarCliente() {
		String message = null;
		Severity severity = null;
		
		Integer idPlanta = null;
		
		try {
			log.info("Entrando a filtrar cliente...");
			
			if(this.usuario.getPerfil() == 1 || this.usuario.getPerfil() == 4)
				idPlanta = new Integer(usuario.getIdPlanta());
			
			this.listaFolios = ordenSalidaDAO.buscaFolios(clienteSelect, fecha, idPlanta);
			this.listaServicios = precioServicioDAO.buscarPorCliente(clienteSelect.getCteCve(), true);
			
			message = "Seleccione el folio.";
			severity = FacesMessage.SEVERITY_INFO;
		} catch (Exception ex) {
			log.error("Problema para recuperar los datos del cliente.", ex);
			message = ex.getMessage();
			severity = FacesMessage.SEVERITY_ERROR;
		} finally {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, "Cliente", message));
			PrimeFaces.current().ajax().update("form:messages", "form:selServicio", "form:folio-som");
		}
		log.info("Informacion exitosamente filtrada.");
	}

	public void reload() throws IOException {
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
	}

	public void filtroPorPlanta() {
		Integer folio = null;
		Integer idPlanta = null;
		this.ordenesDeSalida = ordenSalidaDAO.buscarpoPlanta(folioSelected, fecha);
		this.listaPreSalidaUI = new ArrayList<PreSalidaUI>();
		try {
			for (OrdenDeSalidas orden : ordenesDeSalida) {
				Partida partida = partidaDAO.buscarPorIdConEntrada(orden.getPartidaCve());
				if(idPlanta == null) {
					idPlanta = partida.getCamaraCve().getPlantaCve().getPlantaCve();
					this.plantaSelect = this.plantaDAO.buscarPorId(idPlanta);
				}
				
				log.info("orden: {}", orden);
				PreSalidaUI preUI = new PreSalidaUI(
						orden.getFolioSalida(),
						orden.getStatus(),
						orden.getFechaSalida(),
						orden.getHoraSalida(),
						orden.getPartidaCve(),
						orden.getCantidad(),
						orden.getPeso(),
						orden.getCodigo(),
						orden.getLote(),
						orden.getFechaCaducidad(),
						orden.getSAP(),
						orden.getPedimento(),
						orden.getTemperatura(),
						orden.getUnidadManejo(),
						orden.getCodigoProducto(),
						orden.getNombreProducto(),
						partida.getCamaraCve().getPlantaCve().getPlantaAbrev(),
						partida.getCamaraCve().getCamaraAbrev(),
						orden.getFolioOrdenSalida(),
						orden.getProductoClave(),
						orden.getUnidadManejoCve());
						preUI.setSalidaSelected(false);
						
						
						Integer cantidadInicial = partida.getCantidadTotal();
						BigDecimal CantidadInicial = new BigDecimal(cantidadInicial);
						BigDecimal pesoInicial = partida.getPesoTotal();
						BigDecimal pesoPorUnidad = pesoInicial.divide(CantidadInicial, 3, BigDecimal.ROUND_HALF_UP);
						BigDecimal cantidadOrden = new BigDecimal(orden.getCantidad());
						preUI.setPeso(pesoPorUnidad.multiply(cantidadOrden));
						preUI.setFolioEntrada(partida.getFolio().getFolioCliente()
					);

				listaPreSalidaUI.add(preUI);
				
			}
			
			this.generaFolioSalida();
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		listaSalidasporFolio = ordenSalidaDAO.buscaFolios(folioSelected);
		listaPreSalidaServicio = presalidaservicioDAO.buscarPorFolios(folioSelected);
		folio = listaSalidasporFolio.size();
		if (folio != 0) {
			ordensalida = listaSalidasporFolio.get(0);
		}
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
			if (clienteSelect == null)
				throw new InventarioException("Debe seleccionar un cliente");

			if (plantaSelect == null)
				throw new InventarioException("Debe seleccionar una planta");
						
			seriePK = new SerieConstanciaPK();
			serieTemp = new SerieConstancia();
			
			seriePK.setCliente(this.clienteSelect);
			seriePK.setPlanta(this.plantaSelect);
			seriePK.setTpSerie("O");
			
			serieTemp.setSerieConstanciaPK(seriePK);
			
			
			serie = serieConstanciaDAO.buscarPorClienteAndPlanta(serieTemp);

			
			
			if (serie == null) {
				this.folioSalida = "";
				throw new InventarioException(
						"No se encontró información de los folios del cliente. Debe indicar manualmente un folio de constancia.");
			}

			this.folioSalida = String.format("%s%s%s%d", serie.getSerieConstanciaPK().getTpSerie(), plantaSelect.getPlantaSufijo(),
					clienteSelect.getCodUnico(), serie.getNuSerie());
			
			this.folioServicio = String.format("S%s", this.folioSalida);
			
			String folio = this.constanciaDAO.buscarPorNumero(this.folioSalida);
			
			if(this.folioSalida.equalsIgnoreCase(folio))
				throw new InventarioException("El folio ya existe");
			
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


	public void addMessage(AjaxBehaviorEvent event) {
		UIComponent component = event.getComponent();
		if (component instanceof UIInput) {
			UIInput inputComponent = (UIInput) component;
			boolean value = (boolean) inputComponent.getValue();
			String summary = value ? "Checked" : "Unchecked";
			value = psu.isSalidaSelected();
			log.info(psu.isSalidaSelected());

			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(summary));
		}
	}

	public void validarProducto() {
		String message = null;
		Severity severity = null;
		EntityManager manager = null;
		List<PreSalidaUI> listapsU = null;

		try {
			log.info("Filtrando Producto...");
			manager = EntityManagerUtil.getEntityManager();
			for (PreSalidaUI ps : listaPreSalidaUI) {
				
				
				if(ps.isSalidaSelected() == true) {
					listapsU = new ArrayList<>();
					ps.getPartidaCve();
					ps.getNombreProducto();
					ps.getCantidad();
					ps.getCodigoProducto();
					ps.getCodigo();
					listapsU.add(ps);
					message = "Agregado con exito";
					severity = FacesMessage.SEVERITY_INFO;
					
				}
				
			}
			
		} catch (Exception ex) {
			log.error("Problema para recuperar los datos del cliente.", ex);
			message = ex.getMessage();
			severity = FacesMessage.SEVERITY_ERROR;
		} finally {
			if (manager != null)
				manager.close();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, "Producto", message));
			PrimeFaces.current().ajax().update("form:messages", "form:selServicio", "form:folio-som");
		}
		log.info("Informacion filtrada con éxito.");
	}

	public void agregaServicios() {
		String message = null;
		Severity severity = null;
		PreSalidaServicio ps = null;
		
		try {
			if (this.servicioSelect == null)
				throw new InventarioException("Selecione almenos un servicio");
			
			if (this.clienteSelect == null)
				throw new InventarioException("Debe seleccionar el cliente");
			
			if (this.cantidadServicio == null)
				throw new InventarioException("Debe indicar la cantidad de servicios.");

			if (listaPreSalidaServicio == null)
				listaPreSalidaServicio = new ArrayList<>();
			
			ps = new PreSalidaServicio();
			ps.setCantidad(cantidadServicio);
			ps.setIdServicio(servicioSelect.getServicio());
			ps.setIdUnidadManejo(servicioSelect.getUnidad());
			ps.setObservacion(ps.getObservacion());

			int coincidencias = 0;
			for (PreSalidaServicio srv : listaPreSalidaServicio) {
				if (srv.getIdServicio().getServicioCve().equals(ps.getIdServicio().getServicioCve())) {
					coincidencias++;
				}
			}

			if (coincidencias == 1) {
				log.info("El servicio fue solicitado. Seleccione otro.");
				throw new InventarioException("Servicio solicitado.");
			}
			
			listaPreSalidaServicio.add(ps);
			
			this.servicioSelect = null;
			this.cantidadServicio = null;
			
			message = String.format("Servicio agregado: %s", ps.getIdServicio().getServicioDs());
			severity = FacesMessage.SEVERITY_INFO;

		} catch(InventarioException ex){
			message = ex.getMessage();
			severity = FacesMessage.SEVERITY_WARN;
		} catch (Exception e) {
			log.error("Problema para obtener el listado de servicios del cliente.", e);
			message = "Problema con la información de servicios.";
			severity = FacesMessage.SEVERITY_ERROR;
		} finally {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, "Servicios", message));
			PrimeFaces.current().ajax().update("form:messages", "form:dt-servicios");

		}
	}	
	
	public void validaTemperatura(PreSalidaUI p) {
		log.debug("PreSalida {}",p);
	}
	
	public void guardar() {
		String message = null;
		Severity severity = null;
		ConstanciaSalida constancia = null;
		StatusConstanciaSalida statusConstancia = null;
		DetallePartida detAnterior = null;
		DetallePartida detNUevo= null;
		List<DetalleConstanciaSalida> dcsList = null;
		List<ConstanciaSalidaServicios> listaConstanciaSalidaServicios = null;
		ConstanciaSalidaServicios css = null;
		
		//OBJETOS DE CONSTANCIA DE SERVICIO
		ConstanciaDeServicio cds = null;
		List<PartidaServicio> listaPartidaServicio = null;
		List<ConstanciaServicioDetalle> listaConstanciaSrv = null;
		
		Integer cantidadManejo = null;
		BigDecimal peso = null;
		
		String guardarSalida = null;
		String guardarServicio = null;
		
		boolean continuarSalida = false;
		
		try {
			if(this.isSalidaSaved)
				throw new InventarioException("La constancia ya se encuentra registrada.");
			
			for(PreSalidaUI preUI : this.listaPreSalidaUI) {
				if(preUI.isSalidaSelected())
					continuarSalida = true;
			}
			
			if(continuarSalida == false)
				throw new InventarioException("Debe confirmar al menos un producto");
			
			constancia = new ConstanciaSalida();
			constancia.setFecha(fecha);
			constancia.setPlacasTransporte(ordensalida.getNombrePlacas());
			constancia.setNombreTransportista(ordensalida.getNombreOperador());
			constancia.setNumero(this.folioSalida);
			constancia.setClienteCve(clienteSelect);
			constancia.setNombreCte(clienteSelect.getCteNombre());
			statusConstancia = statusConstanciaSalidaDAO.buscarPorId(1);
			constancia.setStatus(statusConstancia);
			constancia.setObservaciones(String.format("Orden salida: %s - %s",this.ordensalida.getFolioSalida(),  this.observaciones));
			dcsList = new ArrayList<>();
			constancia.setDetalleConstanciaSalidaList(dcsList);
			
			for (PreSalidaUI preS : listaPreSalidaUI) {
				if(preS.isSalidaSelected() == false)
					continue;
				
				DetalleConstanciaSalida dcs = new DetalleConstanciaSalida();
				Partida p = partidaDAO.buscarPorId(preS.getPartidaCve());
				
				
				
				
				
				List<DetallePartida> listadp = new ArrayList<>();
				listadp = dpDAO.buscarPorPartida(p.getPartidaCve());
				p.setDetallePartidaList(listadp);
				for(DetallePartida d : listadp) {
					detAnterior = d;
					detalleAnterior = detAnterior.toString();							
				}
				detNUevo = detAnterior.clone();
				detNUevo.setDetallePartidaPK(detNUevo.getDetallePartidaPK().clone());
				detAnterior.setEdoInvCve(estadoInventarioHistorico);
				detalleActual= detNUevo.toString();
				int i = detNUevo.getDetallePartidaPK().getDetPartCve() + 1;
				detNUevo.getDetallePartidaPK().setDetPartCve(i);
				detNUevo.setDetallePartida(detAnterior);
				detNUevo.setEdoInvCve(estadoInventarioActual);
				cantidadManejo = detNUevo.getCantidadUManejo();
				peso = detNUevo.getCantidadUMedida();
				cantidadManejo= cantidadManejo - preS.getCantidad();
				peso = peso.subtract(preS.getPeso());
				detNUevo.setCantidadUManejo(cantidadManejo);
				detNUevo.setCantidadUMedida(peso);
				listadp.add(detNUevo);
				dpDAO.actualizar(detAnterior);
				dpDAO.guardar(detNUevo);
				dcs.setPartidaCve(p);
				
				Camara c = camaraDAO.buscarPorId(p.getCamaraCve().getCamaraCve());
				dcs.setCamaraCve(c.getCamaraCve());
				dcs.setFolioEntrada(preS.getFolioEntrada());
				dcs.setCantidad(preS.getCantidad());
				dcs.setPeso(preS.getPeso());
				dcs.setUnidad(preS.getUnidadManejo());
				dcs.setProducto(preS.getNombreProducto());
				dcs.setTemperatura(preS.getTemperatura());
				dcs.setConstanciaCve(constancia);
				dcs.setCamaraCadena(preS.getNombreCamara());
				dcs.setDetPartCve(detNUevo.getDetallePartidaPK().getDetPartCve());
				dcsList.add(dcs);
			}
			
			listaConstanciaSalidaServicios = new ArrayList<>();
			for (PreSalidaServicio pss : listaPreSalidaServicio) {
				css = new ConstanciaSalidaServicios();
				ConstanciaSalidaServiciosPK ConstanciaSalidaServiciosPK = new ConstanciaSalidaServiciosPK();
				ConstanciaSalidaServiciosPK.setConstanciaSalidaCve(constancia);
				ConstanciaSalidaServiciosPK.setServicioCve(pss.getIdServicio());
				css.setConstanciaSalidaServiciosPK(ConstanciaSalidaServiciosPK);
				css.setIdConstancia(constancia);
				css.setServicioCve(pss.getIdServicio());
				css.setNumCantidad(BigDecimal.valueOf(pss.getCantidad()));
				constancia.setConstanciaSalidaServiciosList(listaConstanciaSalidaServicios);
				listaConstanciaSalidaServicios.add(css);
				log.debug(listaConstanciaSalidaServicios);
			}
			
			guardarSalida = constanciaDAO.guardar(constancia);
			
			if(guardarSalida != null && "".equalsIgnoreCase(guardarSalida.trim()) == false) {
				this.isSalidaSaved = false;
				throw new InventarioException("Ocurrió un problema al guardar la constancia de salida.");
			}
			this.isSalidaSaved = true;
			
			this.serie.setNuSerie(this.serie.getNuSerie() + 1);
			this.serieConstanciaDAO.actualizar(this.serie);
			
			if(listaPreSalidaServicio.size() <= 0) {
				message = "Constancia guardada correctamente.";
				severity = FacesMessage.SEVERITY_INFO;
				return;
			}
				
			cds = new ConstanciaDeServicio();
			cds.setFolioCliente(this.folioServicio);
			cds.setCteCve(clienteSelect);
			cds.setFecha(fecha);
			cds.setNombreTransportista(ordensalida.getNombreOperador());
			cds.setPlacasTransporte(ordensalida.getNombrePlacas());
			cds.setObservaciones(String.format("Salida: %s - %s", this.folioSalida, this.observaciones));
			BigDecimal valor = new BigDecimal(1);
			cds.setValorDeclarado(valor);
			cds.setStatus(estadoConstancia);
			
			listaPartidaServicio = new ArrayList<>();
			for(OrdenDeSalidas orden : ordenesDeSalida) {
				PartidaServicio ps = new PartidaServicio();
				Producto pr = new Producto();
				UnidadDeManejo udm = new UnidadDeManejo();
				Integer cantidad = orden.getCantidad();
				BigDecimal Cantidad = new BigDecimal(cantidad);
				BigDecimal pso = orden.getPeso();
				BigDecimal psoPorProducto = pso.divide(Cantidad);
				BigDecimal cantidadOrdenSalida = new BigDecimal(orden.getCantidad());

				pr = productoDAO.buscarPorId(orden.getProductoClave());
				udm = unidadDAO.buscarPorId(orden.getUnidadManejoCve());
				ps.setCantidadDeCobro(psoPorProducto.multiply(cantidadOrdenSalida));
				ps.setCantidadTotal(orden.getCantidad());
				ps.setFolio(cds);
				ps.setProductoCve(pr);
				ps.setUnidadDeCobro(udm);
				ps.setUnidadDeManejoCve(udm);
				listaPartidaServicio.add(ps);
			}
			cds.setPartidaServicioList(listaPartidaServicio);
		
			listaConstanciaSrv = new ArrayList<>();
			for(PreSalidaServicio preServ : listaPreSalidaServicio) {
				ConstanciaServicioDetalle consdetalle = new ConstanciaServicioDetalle();
				consdetalle.setFolio(cds);
				consdetalle.setServicioCantidad(BigDecimal.valueOf(preServ.getCantidad()));
				consdetalle.setServicioCve(preServ.getIdServicio());
				listaConstanciaSrv.add(consdetalle);
			}
			cds.setConstanciaServicioDetalleList(listaConstanciaSrv);
			
			guardarServicio = constanciaServicioDAO.guardar(cds);
			
			if(guardarServicio != null && "".equalsIgnoreCase(guardarServicio.trim()) == false) {
				this.isServicioSaved = false;
				throw new InventarioException("Ocurrió un problema al guardar la constancia de servicio.");
			}
			
			this.isServicioSaved = true;
			
			ordenesDeSalida = ordenSalidaDAO.buscarpoPlanta(folioSelected, fecha);
			
			for(OrdenDeSalidas orden : ordenesDeSalida) {
				orden.setStatus("C");
				ordenSalidaDAO.actualizar(orden);
			}
			
			if(sideBar.getNumeroEntradas() != null) {
				log.info("Actualizando número de ordenes de salida...");
				sideBar.setNumeroSalidas(sideBar.getNumeroSalidas() - 1);
				log.info("Numero de ordenes de salida actualizado.");
			}
			
			message = "Constancia guardada correctamente.";
			severity = FacesMessage.SEVERITY_INFO;
			
		} catch (InventarioException ex) {
			message = ex.getMessage();
			severity = FacesMessage.SEVERITY_WARN;
		} catch (Exception ex) {
			log.error("Problema para obtener el listado de la orden.", ex);
			ex.printStackTrace();
			message = "Problema con la información de servicios.";
			severity = FacesMessage.SEVERITY_ERROR;
		} finally {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, "Guardar orden", message));
			PrimeFaces.current().ajax().update("form:messages");
		}
	}	
	
	public void imprimirTicketSalida() throws Exception{
		
		String jasperPath = "/jasper/ConstanciaSalida.jrxml";
		String filename = String.format("ticketSalida-%s.pdf", this.folioSalida);
		String images = "/images/logoF.png";
		String message = null;
		Severity severity = null;
		
		File reportFile = new File(jasperPath);
		File imgFile = null;
		JasperReportUtil jasperReportUtil = new JasperReportUtil();
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		Connection connection = null;
		parameters = new HashMap<String, Object>();
		try {
			if(this.isSalidaSaved == false)
				throw new InventarioException("Debe guardar la salida.");
			
			URL resource = getClass().getResource(jasperPath);//verifica si el recurso esta disponible 
			URL resourceimg = getClass().getResource(images); 
			String file = resource.getFile();//retorna la ubicacion del archivo
			String img = resourceimg.getFile();
			reportFile = new File(file);//crea un archivo
			imgFile = new File(img);
			log.info(reportFile.getPath());
			connection = EntityManagerUtil.getConnection();
			parameters.put("REPORT_CONNECTION", connection);
			parameters.put("NUMERO", this.folioSalida);
			parameters.put("LogoPath", imgFile.getPath());
			byte[] bytes = jasperReportUtil.createPDF(parameters, reportFile.getPath());
			InputStream input = new ByteArrayInputStream(bytes);
			this.file = DefaultStreamedContent.builder()
					.contentType("application/pdf")
					.name(filename)
					.stream(() -> input )
					.build();
			log.info("Orden de salida generada {}...", filename);
		} catch (Exception e) {
			e.printStackTrace();
			message = String.format("No se pudo imprimir el folio %s", ordensalida.getFolioSalida());
			severity = FacesMessage.SEVERITY_INFO;
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity,"Error en impresion",message));
			PrimeFaces.current().ajax().update("form:messages");
			
		}finally {
			conexion.close((Connection) connection);
		}

	}
	
	public void imprimirTicketServicios() throws Exception{
		String jasperPath = "/jasper/ticketServicio.jrxml";
		String filename = String.format("ticketServicio-%s.pdf", this.folioServicio);
		String images = "/images/logoF.png";
		String message = null;
		Severity severity = null;
		File reportFile = new File(jasperPath);
		File imgFile = null;
		JasperReportUtil jasperReportUtil = new JasperReportUtil();
		Map<String, Object> parameters = new HashMap<String, Object>();
		Connection connection = null;
		parameters = new HashMap<String, Object>();
		try {
			if(this.isServicioSaved == false)
				throw new InventarioException("No hay constancia de servicios registrada.");
			
			URL resource = getClass().getResource(jasperPath);//verifica si el recurso esta disponible 
			URL resourceimg = getClass().getResource(images); 
			String file = resource.getFile();//retorna la ubicacion del archivo
			String img = resourceimg.getFile();
			reportFile = new File(file);//crea un archivo
			imgFile = new File(img);
			log.info(reportFile.getPath());
			connection = EntityManagerUtil.getConnection();
			parameters.put("REPORT_CONNECTION", connection);
			parameters.put("FOLIO", this.folioServicio);
			parameters.put("LogoPath", imgFile.getPath());
			byte[] bytes = jasperReportUtil.createPDF(parameters, reportFile.getPath());
			InputStream input = new ByteArrayInputStream(bytes);
			this.file = DefaultStreamedContent.builder()
					.contentType("application/pdf")
					.name(filename)
					.stream(() -> input )
					.build();
			log.info("Orden de servicio generada {}...", filename);
		} catch (Exception e) {
			e.printStackTrace();
			message = String.format("No se pudo imprimir el folio %s", folioSalida);
			severity = FacesMessage.SEVERITY_INFO;
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity,"Error en impresion",message));
			PrimeFaces.current().ajax().update("form:messages");
			
		}finally {
			conexion.close((Connection) connection);
		}

	}
	
	@SuppressWarnings("unlikely-arg-type")
	public void deleteServicio(PreSalidaServicio servicio) {
		this.listaPreSalidaServicio.remove(servicio);
		this.listaServicios.remove(servicio);
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

	public PrecioServicio getServicioSelect() {
		return servicioSelect;
	}

	public void setServicioSelect(PrecioServicio idServicio) {
		this.servicioSelect = idServicio;
	}

	public Integer getCantidadServicio() {
		return cantidadServicio;
	}

	public void setCantidadServicio(Integer cantidadServicio) {
		this.cantidadServicio = cantidadServicio;
	}

	public List<OrdenDeSalidas> getListaSalidasporPlantas() {
		return ordenesDeSalida;
	}

	public void setListaSalidasporPlantas(List<OrdenDeSalidas> listaSalidasporPlantas) {
		this.ordenesDeSalida = listaSalidasporPlantas;
	}

	public DetallePartida getDp() {
		return dp;
	}

	public void setDp(DetallePartida dp) {
		this.dp = dp;
	}

	public DetalleConstanciaSalida getDcs() {
		return dcs;
	}

	public void setDcs(DetalleConstanciaSalida dcs) {
		this.dcs = dcs;
	}

	public OrdenDeSalidas getOrdenDeSalidas() {
		return ordenDeSalidas;
	}

	public void setOrdenDeSalidas(OrdenDeSalidas ordenDeSalidas) {
		this.ordenDeSalidas = ordenDeSalidas;
	}

	public List<String> getListaFolios() {
		return listaFolios;
	}

	public void setListaFolios(List<String> listaFolios) {
		this.listaFolios = listaFolios;
	}

	public String getFolioSelected() {
		return folioSelected;
	}

	public void setFolioSelected(String folioSelected) {
		this.folioSelected = folioSelected;
	}

	public Time getTmSalida() {
		return tmSalida;
	}

	public void setTmSalida(Time tmSalida) {
		this.tmSalida = tmSalida;
	}

	public List<OrdenSalida> getListaSalidasporFolio() {
		return listaSalidasporFolio;
	}

	public void setListaSalidasporFolio(List<OrdenSalida> listaSalidasporFolio) {
		this.listaSalidasporFolio = listaSalidasporFolio;
	}

	public PreSalidaServicio getPss() {
		return pss;
	}

	public void setPss(PreSalidaServicio pss) {
		this.pss = pss;
	}

	public List<PreSalidaServicio> getListaPreSalidaServicio() {
		return listaPreSalidaServicio;
	}

	public void setListaPreSalidaServicio(List<PreSalidaServicio> listaPreSalidaServicio) {
		this.listaPreSalidaServicio = listaPreSalidaServicio;
	}

	public List<PreSalidaUI> getListaPreSalidaUI() {
		return listaPreSalidaUI;
	}

	public void setListaPreSalidaUI(List<PreSalidaUI> listaPreSalidaUI) {
		this.listaPreSalidaUI = listaPreSalidaUI;
	}

	public PreSalidaUI getPsu() {
		return psu;
	}

	public void setPsu(PreSalidaUI psu) {
		this.psu = psu;
	}

	public String getDetalleAnterior() {
		return detalleAnterior;
	}

	public void setDetalleAnterior(String detalleAnterior) {
		this.detalleAnterior = detalleAnterior;
	}

	public String getDetalleActual() {
		return detalleActual;
	}

	public void setDetalleActual(String detalleActual) {
		this.detalleActual = detalleActual;
	}

	public Integer getCantidad() {
		return cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

	public BigDecimal getPeso() {
		return peso;
	}

	public void setPeso(BigDecimal peso) {
		this.peso = peso;
	}

	public EstadoInventario getEstadoInventarioActual() {
		return estadoInventarioActual;
	}

	public void setEstadoInventarioActual(EstadoInventario estadoInventarioActual) {
		this.estadoInventarioActual = estadoInventarioActual;
	}

	public EstadoConstancia getEstadoConstancia() {
		return estadoConstancia;
	}

	public void setEstadoConstancia(EstadoConstancia estadoConstancia) {
		this.estadoConstancia = estadoConstancia;
	}

	public EstadoInventario getEstadoInventarioHistorico() {
		return estadoInventarioHistorico;
	}

	public void setEstadoInventarioHistorico(EstadoInventario estadoInventarioHistorico) {
		this.estadoInventarioHistorico = estadoInventarioHistorico;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public boolean isPdf() {
		return pdf;
	}

	public void setPdf(boolean pdf) {
		this.pdf = pdf;
	}

	public boolean isExcel() {
		return excel;
	}

	public void setExcel(boolean excel) {
		this.excel = excel;
	}

	public StreamedContent getFile() {
		return file;
	}

	public void setFile(StreamedContent file) {
		this.file = file;
	}

	public String getFolioSalida() {
		return folioSalida;
	}

	public void setFolioSalida(String folioSalida) {
		this.folioSalida = folioSalida;
	}

	public String getFolioServicio() {
		return folioServicio;
	}

	public void setFolioServicio(String folioServicio) {
		this.folioServicio = folioServicio;
	}

	public boolean isSalidaSaved() {
		return isSalidaSaved;
	}

	public void setSalidaSaved(boolean isSalidaSaved) {
		this.isSalidaSaved = isSalidaSaved;
	}

	public boolean isServicioSaved() {
		return isServicioSaved;
	}

	public void setServicioSaved(boolean isServicioSaved) {
		this.isServicioSaved = isServicioSaved;
	}

}
