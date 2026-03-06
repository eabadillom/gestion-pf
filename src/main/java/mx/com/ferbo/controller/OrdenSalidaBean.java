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
import javax.annotation.PreDestroy;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import mx.com.ferbo.business.constancias.ConstanciaSalidaBL;
import mx.com.ferbo.business.constancias.ConstanciaServicioBL;
import mx.com.ferbo.business.n.PartidaBL;
import mx.com.ferbo.business.n.PlantaBL;
import mx.com.ferbo.business.n.PrecioServicioBL;
import mx.com.ferbo.business.n.UnidadManejoBL;
import mx.com.ferbo.business.salidas.SalidasBL;
import mx.com.ferbo.dao.CamaraDAO;
import mx.com.ferbo.dao.ConstanciaServicioDAO;
import mx.com.ferbo.dao.EstadoConstanciaDAO;
import mx.com.ferbo.dao.EstadoInventarioDAO;
import mx.com.ferbo.dao.ProductoDAO;
import mx.com.ferbo.dao.SerieConstanciaDAO;
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
import mx.com.ferbo.model.Partida;
import mx.com.ferbo.model.PartidaServicio;
import mx.com.ferbo.model.Planta;
import mx.com.ferbo.model.PrecioServicio;
import mx.com.ferbo.model.Producto;
import mx.com.ferbo.model.Salida;
import mx.com.ferbo.model.SerieConstancia;
import mx.com.ferbo.model.SerieConstanciaPK;
import mx.com.ferbo.model.ServiciosSalida;
import mx.com.ferbo.model.StatusConstanciaSalida;
import mx.com.ferbo.model.StatusSalida;
import mx.com.ferbo.model.UnidadDeManejo;
import mx.com.ferbo.model.Usuario;
import mx.com.ferbo.ui.OrdenDeSalidas;
import mx.com.ferbo.ui.SalidaUI;
import mx.com.ferbo.util.DateUtil;
import mx.com.ferbo.util.EntityManagerUtil;
import mx.com.ferbo.util.FacesUtils;
import mx.com.ferbo.util.InventarioException;
import mx.com.ferbo.util.JasperReportUtil;
import mx.com.ferbo.util.conexion;

@Named
@ViewScoped
public class OrdenSalidaBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private static Logger log = LogManager.getLogger(OrdenSalidaBean.class);
        
        @Inject
	private SalidasBL salidasBL;
        
        @Inject
        private ConstanciaSalidaBL constanciaSalidaBL;
        
        @Inject
        private ConstanciaServicioBL constanciaServicioBL; 
        
        @Inject
        private PrecioServicioBL precioServicioBL;
        
        @Inject
        private PartidaBL partidaBL;
        
        @Inject
        private PlantaBL plantaBL;
        
        @Inject
        private UnidadManejoBL unidadDeManejoBL;
        
	private List<Cliente> listaClientes;
	private List<OrdenDeSalidas> ordenesDeSalida;
	private List<PrecioServicio> listaServicios;
	private List<ServiciosSalida> listaServiciosSalida;
	private List<String> listaFolios;
	private List<Salida> listaSalidasPorFolio;
	private List<SalidaUI> listaSalidaUI;
        private List<SalidaUI> listSolicitadosSalidaUI;
	
	private CamaraDAO camaraDAO;
	private EstadoInventarioDAO estadoInventarioDAO;
	private EstadoConstanciaDAO estadoConstanciaDAO;
	private ProductoDAO productoDAO;
	private ConstanciaServicioDAO constanciaServicioDAO;
	private SerieConstanciaDAO serieConstanciaDAO;
	
	private boolean confirmacion = false;
	private boolean pdf = false;
	private boolean excel = false;
	private String folioSelected;
	private Date fecha;
	private Time tmSalida;
	private String detalleAnterior;
	private String detalleActual;
	private String observaciones;
	private Integer cantidadServicio;
	private StreamedContent file;
	
	private Cliente clienteSelect;
	private Planta plantaSelect;
	private Salida ordenSalida;
        private ServiciosSalida serviciosSalida;
	private DetallePartida dp;
	private DetalleConstanciaSalida dcs;
	private OrdenDeSalidas ordenDeSalidas;
	private PrecioServicio servicioSelect;
	private SalidaUI psu;
	private EstadoInventario estadoInventarioActual;
	private EstadoInventario estadoInventarioHistorico;
	private EstadoConstancia estadoConstancia;
	private SerieConstancia serie;
	
	private String folioSalida;
	private String folioServicio;
	private boolean isSalidaSaved = false;
	private boolean isServicioSaved = false;
        
        private Integer totalCajas;
        private BigDecimal pesoTotal = BigDecimal.ZERO;
	
	private FacesContext context;
    private HttpServletRequest request;
    private HttpSession session;
    private Usuario usuario;
    
    @Inject
    private SideBarBean sideBar;

	public OrdenSalidaBean() {
		estadoConstanciaDAO = new EstadoConstanciaDAO();
		camaraDAO = new CamaraDAO();
		productoDAO = new ProductoDAO();
		estadoInventarioDAO = new EstadoInventarioDAO();
		constanciaServicioDAO = new ConstanciaServicioDAO();
		serieConstanciaDAO = new SerieConstanciaDAO();

		
		listaClientes = new ArrayList<Cliente>();
		listaSalidaUI = new ArrayList<>();
                listSolicitadosSalidaUI = new ArrayList<>();
		listaServicios = new ArrayList<PrecioServicio>();
		ordenesDeSalida = new ArrayList<OrdenDeSalidas>();
		listaServiciosSalida = new ArrayList<>();
		listaFolios = new ArrayList<>();
		listaSalidasPorFolio = new ArrayList<>();
                totalCajas = 0;
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
			log.info("cargando lista de clientes en orden de salidas...");
			listaClientes = sideBar.getListaClientesActivos();
			log.info("lista de clientes cargada en orden de salidas.");
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
	
	@PreDestroy
	public void destroy() {
		log.info("Saliendo del alta de constancias de depósito.");
	}

	public void filtrarCliente() {
		Integer idPlanta = null;
		
		try {
			log.info("Entrando a filtrar cliente...");
			
			if(this.usuario.getPerfil() == 1 || this.usuario.getPerfil() == 4)
				idPlanta = new Integer(usuario.getIdPlanta());
			
			this.listaFolios = salidasBL.obtenerFolios(clienteSelect, fecha, idPlanta);
			this.listaServicios = precioServicioBL.buscarPorCliente(clienteSelect.getCteCve(), true);
			
			FacesUtils.addMessage(FacesMessage.SEVERITY_INFO, "Orden de Salidas", "Seleccione el folio");
		} catch(InventarioException ex) {
                        log.error("Problema para recuperar los datos del cliente.", ex);
			FacesUtils.addMessage(FacesMessage.SEVERITY_WARN, "Orden de Salidas", ex.getMessage());
                } catch(Exception ex) {
			log.error("Problema para recuperar los datos del cliente.", ex);
			FacesUtils.addMessage(FacesMessage.SEVERITY_ERROR, "Orden de Salidas", "Error al seleccionar al cliente");
		} finally {
			PrimeFaces.current().ajax().update("form:messages", "form:selServicio", "form:folio-som");
		}
		log.info("Informacion exitosamente filtrada.");
	}

	public void reload() throws IOException {
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
	}

	public void filtroPorPlanta() {
		Integer idPlanta = null;
                Salida salidaFolio = null;
		this.ordenesDeSalida = salidasBL.obtenerInventario(folioSelected, fecha);
		this.listaSalidaUI = new ArrayList<SalidaUI>();
		try {
			for (OrdenDeSalidas orden : ordenesDeSalida) {
				Partida partida = partidaBL.buscarPartidaConEntrada(orden.getPartidaCve());
				if(idPlanta == null) {
					idPlanta = partida.getCamaraCve().getPlantaCve().getPlantaCve();
					this.plantaSelect = plantaBL.buscarPlanta(idPlanta);
				}
				
				Integer cantidadInicial = partida.getCantidadTotal();
                                BigDecimal CantidadInicial = new BigDecimal(cantidadInicial);
                                BigDecimal pesoInicial = partida.getPesoTotal();
                                BigDecimal pesoPorUnidad = pesoInicial.divide(CantidadInicial, 3, BigDecimal.ROUND_HALF_UP);
                                BigDecimal cantidadOrden = new BigDecimal(orden.getCantidad());
                                
				SalidaUI preUI = new SalidaUI(
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
						preUI.setPeso(pesoPorUnidad.multiply(cantidadOrden));
						preUI.setFolioEntrada(partida.getFolio().getFolioCliente()
					);
                                        
				listaSalidaUI.add(preUI);
				
			}
			
			this.generaFolioSalida();
                        
                        salidaFolio = salidasBL.obtenerSalidaPorFolio(folioSelected);
                        listaServiciosSalida = salidaFolio.getListServiciosSalida();
                        
                        if(salidaFolio == null)
                            throw new InventarioException("No se encontro la salida con el folio");
                        
                        ordenSalida = salidaFolio;
		} catch(InventarioException ex) {
                        log.error("Problema para recuperar los datos de la salida...", ex);
			FacesUtils.addMessage(FacesMessage.SEVERITY_WARN, "Orden de Salidas", ex.getMessage());
                } catch (Exception e) {
                        log.info("Problema para recuperar los datos de la salida...", e);
                        FacesUtils.addMessage(FacesMessage.SEVERITY_ERROR, "Orden de Salidas", "Ha ocurrido un error en el sistema. Intente nuevamente.\nSi el problema persiste, por favor comuniquese con su administrador del sistema.");
		}
                
	}
	
	public synchronized void generaFolioSalida() {
		SerieConstanciaPK seriePK = null;
		SerieConstancia serie = null;
		SerieConstancia serieTemp = null;
		
		try {
                        FacesUtils.requireNonNull(clienteSelect, "Debe seleccionar un cliente");
			FacesUtils.requireNonNull(plantaSelect, "Debe seleccionar una planta");
						
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
			
			String folio = constanciaSalidaBL.buscarPorFolioSalida(this.folioSalida);
			
			if(this.folioSalida.equalsIgnoreCase(folio))
                            throw new InventarioException("El folio ya existe");
			
			this.serie = serie;

		} catch (InventarioException ex) {
			log.warn("Error al generar el folio de salida...", ex);
                        FacesUtils.addMessage(FacesMessage.SEVERITY_WARN, "Folio", ex.getMessage());
		} catch (Exception ex) {
			log.error("Problema para generar el folio de entrada...", ex);
			FacesUtils.addMessage(FacesMessage.SEVERITY_ERROR, "Folio", "Ha ocurrido un error en el sistema. Intente nuevamente.\nSi el problema persiste, por favor comuniquese con su administrador del sistema.");
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

	public void validarProducto(SalidaUI salida) {
		log.info("Filtrando Producto...");
                try {
                    
                    if(salida.isSalidaSelected())
                    {
                        this.listSolicitadosSalidaUI.add(salida);
                        totalCajas += salida.getCantidad();
                        pesoTotal = pesoTotal.add(salida.getPeso());
                    } else {
                        this.listSolicitadosSalidaUI.remove(salida);
                        totalCajas -= salida.getCantidad();
                        pesoTotal = pesoTotal.subtract(salida.getPeso());
                    }
                    
                    String mensaje = salida.isSalidaSelected() ? "Producto agregado con éxito" : "Producto eliminado con éxito";
                    log.info("Elementos seleccionados: {}", this.listSolicitadosSalidaUI.size());
                    FacesUtils.addMessage(FacesMessage.SEVERITY_INFO, "Productos", mensaje);
		} catch (Exception ex) {
                    log.error("Problema para recuperar los datos del cliente.", ex);
                    FacesUtils.addMessage(FacesMessage.SEVERITY_ERROR, "Productos", "Ha ocurrido un error en el sistema. Intente nuevamente.\nSi el problema persiste, por favor comuniquese con su administrador del sistema.");
		} finally {
                    PrimeFaces.current().ajax().update("form:messages", "form:selServicio", "form:folio-som");
		}
		log.info("Producto filtrada con éxito.");
	}

	public void agregaServicios() {
		ServiciosSalida ps = null;
		
		try {
                        FacesUtils.requireNonNull(this.servicioSelect, "Selecione almenos un servicio");
			FacesUtils.requireNonNull(this.clienteSelect, "Debe seleccionar el cliente");
			
                        if(this.cantidadServicio == null)
                            throw new InventarioException("Debe indicar la cantidad de servicios.");
			
                        if(this.listaServiciosSalida.isEmpty() || listaServiciosSalida == null)
                            listaServiciosSalida = new ArrayList<>();
			
			ps = new ServiciosSalida();
			ps.setCantidad(cantidadServicio);
			ps.setServicio(servicioSelect.getServicio());
			ps.setUnidadDeManejo(servicioSelect.getUnidad());
                        ps.setSalida(ordenSalida);

			int coincidencias = 0;
			for (ServiciosSalida srv : listaServiciosSalida) {
				if (srv.getServicio().getServicioCve().equals(ps.getServicio().getServicioCve())) {
					coincidencias++;
				}
			}

			if (coincidencias == 1) {
				log.info("El servicio ya fue solicitado. Seleccione otro.");
				throw new InventarioException("El servicio ya se encuentra agregado.");
			}
			
			listaServiciosSalida.add(ps);
			
			this.servicioSelect = null;
			this.cantidadServicio = null;
			
                        log.info(String.format("Servicio agregado: %s", ps.getServicio().getServicioDs()));
			FacesUtils.addMessage(FacesMessage.SEVERITY_INFO, "Servicios", String.format("Servicio agregado: %s", ps.getServicio().getServicioDs()));
		} catch(InventarioException ex){
                        log.warn("Problema para obtener el listado de servicios del cliente...", ex);
			FacesUtils.addMessage(FacesMessage.SEVERITY_WARN, "Servicios", ex.getMessage());
		} catch (Exception e) {
			log.error("Problema para obtener el listado de servicios del cliente.", e);
			FacesUtils.addMessage(FacesMessage.SEVERITY_ERROR, "Servicios", "Ha ocurrido un error en el sistema. Intente nuevamente.\nSi el problema persiste, por favor comuniquese con su administrador del sistema.");
		} finally {
			PrimeFaces.current().ajax().update("form:messages");
		}
	}	
	
	public void validarTemperatura(SalidaUI p) {
            log.debug("Salida {}", p);
	}
	
	public void guardar() {
		ConstanciaSalida constancia = null;
		StatusConstanciaSalida statusConstancia = null;
		DetallePartida detAnterior = null;
		DetallePartida detPNuevo= null;
		List<DetalleConstanciaSalida> dcsList = null;
		List<ConstanciaSalidaServicios> listaConstanciaSalidaServicios = null;
		ConstanciaSalidaServicios css = null;
		
		//OBJETOS DE CONSTANCIA DE SERVICIO
		ConstanciaDeServicio cds = null;
		List<PartidaServicio> listaPartidaServicio = null;
		List<ConstanciaServicioDetalle> listaConstanciaSrv = null;
		
		Integer cantidadManejo = null;
		BigDecimal peso = null;
		
		boolean continuarSalida = false;
		
		try {
			if(this.isSalidaSaved)
				throw new InventarioException("La constancia ya se encuentra registrada.");
			
			if(continuarSalida == false)
				throw new InventarioException("Debe confirmar al menos un producto");
			
			constancia = new ConstanciaSalida();
			constancia.setFecha(fecha);
			constancia.setPlacasTransporte(ordenSalida.getPlacasTransporte());
			constancia.setNombreTransportista(ordenSalida.getNombreTransportista());
			constancia.setNumero(this.folioSalida);
			constancia.setClienteCve(clienteSelect);
			constancia.setNombreCte(clienteSelect.getNombre());
			statusConstancia = constanciaSalidaBL.buscarStatusConstancia();
			constancia.setStatus(statusConstancia);
			constancia.setObservaciones(String.format("Orden salida: %s - %s",this.ordenSalida.getFolioSalida(),  this.observaciones));
			dcsList = new ArrayList<>();
			constancia.setDetalleConstanciaSalidaList(dcsList);
			
			for (SalidaUI preS : this.listSolicitadosSalidaUI) {
				DetalleConstanciaSalida dcs = new DetalleConstanciaSalida();
				Partida p = partidaBL.buscarPartidaPorId(preS.getPartidaCve());
				
				List<DetallePartida> listadp = new ArrayList<>();
				listadp = partidaBL.buscarPorId(p.getPartidaCve());
				p.setDetallePartidaList(listadp);
				for(DetallePartida d : listadp) {
					detAnterior = d;
					detalleAnterior = detAnterior.toString();							
				}
				detPNuevo = detAnterior.clone();
				detPNuevo.setDetallePartidaPK(detPNuevo.getDetallePartidaPK().clone());
				detAnterior.setEdoInvCve(estadoInventarioHistorico);
				detalleActual= detPNuevo.toString();
				int i = detPNuevo.getDetallePartidaPK().getDetPartCve() + 1;
				detPNuevo.getDetallePartidaPK().setDetPartCve(i);
				detPNuevo.setDetallePartida(detAnterior);
				detPNuevo.setEdoInvCve(estadoInventarioActual);
				cantidadManejo = detPNuevo.getCantidadUManejo();
				peso = detPNuevo.getCantidadUMedida();
				cantidadManejo= cantidadManejo - preS.getCantidad();
				peso = peso.subtract(preS.getPeso());
				detPNuevo.setCantidadUManejo(cantidadManejo);
				detPNuevo.setCantidadUMedida(peso);
				listadp.add(detPNuevo);
				partidaBL.actualizarDetallePartida(detAnterior);
				partidaBL.guardarDetallePartida(detPNuevo);
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
				dcs.setDetPartCve(detPNuevo.getDetallePartidaPK().getDetPartCve());
				dcsList.add(dcs);
			}
			
			listaConstanciaSalidaServicios = new ArrayList<>();
			for (ServiciosSalida ss : listaServiciosSalida) {
				css = new ConstanciaSalidaServicios();
				ConstanciaSalidaServiciosPK ConstanciaSalidaServiciosPK = new ConstanciaSalidaServiciosPK();
				ConstanciaSalidaServiciosPK.setConstanciaSalidaCve(constancia);
				ConstanciaSalidaServiciosPK.setServicioCve(ss.getServicio());
				css.setConstanciaSalidaServiciosPK(ConstanciaSalidaServiciosPK);
				css.setIdConstancia(constancia);
				css.setServicioCve(ss.getServicio());
				css.setNumCantidad(BigDecimal.valueOf(ss.getCantidad()));
				constancia.setConstanciaSalidaServiciosList(listaConstanciaSalidaServicios);
				listaConstanciaSalidaServicios.add(css);
				log.debug(listaConstanciaSalidaServicios);
			}
			
			constanciaSalidaBL.guardar(constancia);
			
			this.isSalidaSaved = true;
			
			this.serie.setNuSerie(this.serie.getNuSerie() + 1);
			this.serieConstanciaDAO.actualizar(this.serie);
			
			if(listaServiciosSalida.size() <= 0) {
				FacesUtils.addMessage(FacesMessage.SEVERITY_INFO, "Guardar orden", "Constancia guardada correctamente.");
				return;
			}
				
			cds = new ConstanciaDeServicio();
			cds.setFolioCliente(this.folioServicio);
			cds.setCteCve(clienteSelect);
			cds.setFecha(fecha);
			cds.setNombreTransportista(ordenSalida.getNombreTransportista());
			cds.setPlacasTransporte(ordenSalida.getPlacasTransporte());
			cds.setObservaciones(String.format("Salida: %s - %s", this.folioSalida, this.observaciones));
			BigDecimal valor = new BigDecimal(1);
			cds.setValorDeclarado(valor);
			cds.setStatus(estadoConstancia);
			
			listaPartidaServicio = new ArrayList<>();
			for(SalidaUI orden : listSolicitadosSalidaUI) {
				PartidaServicio ps = new PartidaServicio();
				Producto pr = new Producto();
				UnidadDeManejo udm = new UnidadDeManejo();
				Integer cantidad = orden.getCantidad();
				BigDecimal Cantidad = new BigDecimal(cantidad);
				BigDecimal pso = orden.getPeso();
				BigDecimal psoPorProducto = pso.divide(Cantidad, 3, BigDecimal.ROUND_HALF_UP);
				BigDecimal cantidadOrdenSalida = new BigDecimal(orden.getCantidad());

				pr = productoDAO.buscarPorId(orden.getProductoClave());
				udm = unidadDeManejoBL.obtenerUDMPorId(orden.getUnidadManejoCve());
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
			for(ServiciosSalida servSalida : listaServiciosSalida) {
				ConstanciaServicioDetalle consdetalle = new ConstanciaServicioDetalle();
				consdetalle.setFolio(cds);
				consdetalle.setServicioCantidad(BigDecimal.valueOf(servSalida.getCantidad()));
				consdetalle.setServicioCve(servSalida.getServicio());
				listaConstanciaSrv.add(consdetalle);
			}
			cds.setConstanciaServicioDetalleList(listaConstanciaSrv);
			
			constanciaServicioBL.guardarConstancia(cds);
			
			this.isServicioSaved = true;
			
                        StatusSalida auxStatusSalida = salidasBL.obtenerStatusAceptado();
			Salida auxSalida = salidasBL.obtenerSalidaPorFolio(folioSelected);
			auxSalida.setStatus(auxStatusSalida);
                        auxSalida.setFechaModificacion(new Date());
                        salidasBL.actualizarSalida(auxSalida);
			
			if(sideBar.getNumeroEntradas() != null) {
				log.info("Actualizando número de ordenes de salida...");
				sideBar.setNumeroSalidas(sideBar.getNumeroSalidas() - 1);
				log.info("Numero de ordenes de salida actualizado.");
			}
			
			FacesUtils.addMessage(FacesMessage.SEVERITY_INFO, "Orden", "Constancia guardada correctamente.");
		} catch (InventarioException ex) {
                        this.isSalidaSaved = false;
                        log.warn("Problema para obtener el listado de la orden...", ex);
			FacesUtils.addMessage(FacesMessage.SEVERITY_WARN, "Orden", ex.getMessage());
		} catch (Exception ex) {
			log.error("Problema para obtener el listado de la orden...", ex);
			FacesUtils.addMessage(FacesMessage.SEVERITY_ERROR, "Orden", "Ha ocurrido un error en el sistema. Intente nuevamente.\nSi el problema persiste, por favor comuniquese con su administrador del sistema.");
		} finally {
			PrimeFaces.current().ajax().update("form:messages");
		}
	}	
	
	public void imprimirTicketSalida() throws Exception{
		
		String jasperPath = "/jasper/ConstanciaSalida.jrxml";
		String filename = String.format("ticketSalida-%s.pdf", this.folioSalida);
		String images = "/images/logoF.png";
		
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
                        log.error("Problema para para imrprimir el folio...", e);
			FacesUtils.addMessage(FacesMessage.SEVERITY_ERROR, "Error en impresion", String.format("No se pudo imprimir el folio %s", ordenSalida.getFolioSalida()));
                        PrimeFaces.current().ajax().update("form:messages");
			
		}finally {
			conexion.close((Connection) connection);
		}

	}
	
	public void imprimirTicketServicios() throws Exception{
		String jasperPath = "/jasper/ticketServicio.jrxml";
		String filename = String.format("ticketServicio-%s.pdf", this.folioServicio);
		String images = "/images/logoF.png";
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
			log.error("Problema para el ticket de servicios...", e);
			FacesUtils.addMessage(FacesMessage.SEVERITY_ERROR, "Error en impresion", String.format("No se pudo imprimir el folio %s", folioSalida));
                        PrimeFaces.current().ajax().update("form:messages");
			
		}finally {
			conexion.close((Connection) connection);
		}

	}
	
	@SuppressWarnings("unlikely-arg-type")
	public void deleteServicio(ServiciosSalida servicio) {
                log.info("Servicio eliminado: {}", servicio.getServicio().getServicioNombre());
		this.listaServiciosSalida.remove(servicio);
		//this.listaServicios.remove(servicio);
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

	public Salida getSalida() {
		return ordenSalida;
	}

	public void setSalida(Salida ordenSalida) {
		this.ordenSalida = ordenSalida;
	}

        public ServiciosSalida getServiciosSalida() {
            return serviciosSalida;
        }

        public void setServiciosSalida(ServiciosSalida serviciosSalida) {
            this.serviciosSalida = serviciosSalida;
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

	public List<Salida> getListaSalidasPorFolio() {
		return listaSalidasPorFolio;
	}

	public void setListaSalidasPorFolio(List<Salida> listaSalidasPorFolio) {
		this.listaSalidasPorFolio = listaSalidasPorFolio;
	}

	public List<ServiciosSalida> getListaServiciosSalida() {
		return listaServiciosSalida;
	}

	public void setListaServiciosSalida(List<ServiciosSalida> listaServiciosSalida) {
		this.listaServiciosSalida = listaServiciosSalida;
	}

	public List<SalidaUI> getListaPreSalidaUI() {
		return listaSalidaUI;
	}

	public void setListaPreSalidaUI(List<SalidaUI> listaSalidaUI) {
		this.listaSalidaUI = listaSalidaUI;
	}

	public SalidaUI getPsu() {
		return psu;
	}

	public void setPsu(SalidaUI psu) {
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

        public Integer getTotalCajas() {
            return totalCajas;
        }

        public void setTotalCajas(Integer totalCajas) {
            this.totalCajas = totalCajas;
        }

        public BigDecimal getPesoTotal() {
            return pesoTotal;
        }

        public void setPesoTotal(BigDecimal pesoTotal) {
            this.pesoTotal = pesoTotal;
        }

}
