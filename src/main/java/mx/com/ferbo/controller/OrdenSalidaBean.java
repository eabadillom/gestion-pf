package mx.com.ferbo.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
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

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
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
import mx.com.ferbo.business.n.CandadoBL;
import mx.com.ferbo.business.n.EstadoInventarioBL;
import mx.com.ferbo.business.n.PartidaBL;
import mx.com.ferbo.business.n.PlantaBL;
import mx.com.ferbo.business.n.PrecioServicioBL;
import mx.com.ferbo.business.n.SaldosBL;
import mx.com.ferbo.business.n.SerieConstanciaBL;
import mx.com.ferbo.business.salidas.OrdenSalidasBL;
import mx.com.ferbo.business.salidas.SalidasBL;
import mx.com.ferbo.model.Camara;
import mx.com.ferbo.model.CandadoSalida;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.ConstanciaDeServicio;
import mx.com.ferbo.model.ConstanciaSalida;
import mx.com.ferbo.model.ConstanciaSalidaServicios;
import mx.com.ferbo.model.ConstanciaServicioDetalle;
import mx.com.ferbo.model.DetalleConstanciaSalida;
import mx.com.ferbo.model.DetallePartida;
import mx.com.ferbo.model.DetallePartidaPK;
import mx.com.ferbo.model.EstadoConstancia;
import mx.com.ferbo.model.EstadoInventario;
import mx.com.ferbo.model.Partida;
import mx.com.ferbo.model.PartidaServicio;
import mx.com.ferbo.model.Planta;
import mx.com.ferbo.model.PrecioServicio;
import mx.com.ferbo.model.Salida;
import mx.com.ferbo.model.SerieConstancia;
import mx.com.ferbo.model.ServiciosSalida;
import mx.com.ferbo.model.StatusConstanciaSalida;
import mx.com.ferbo.model.TipoMovimiento;
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
        private OrdenSalidasBL ordenSalidasBL;
        
        @Inject
	private SalidasBL salidasBL;
        
        @Inject
        private ConstanciaSalidaBL constanciaSalidaBL;
        
        @Inject
        private ConstanciaServicioBL constanciaServicioBL; 
        
        @Inject
        private CandadoBL candadoBL;
        
        @Inject
        private SerieConstanciaBL serieConstanciaBL;
        
        @Inject
        private PrecioServicioBL precioServicioBL;
        
        @Inject
        private PartidaBL partidaBL;
        
        @Inject
        private PlantaBL plantaBL;
        
        @Inject
        private SaldosBL saldoBL;
        
        @Inject
        private EstadoInventarioBL estadoInventarioBL;
        
	private List<Cliente> listaClientes;
	private List<OrdenDeSalidas> ordenesDeSalida;
	private List<PrecioServicio> listaServicios;
	private List<ServiciosSalida> listaServiciosSalida;
	private List<String> listaFolios;
	private List<Salida> listaSalidasPorFolio;
	private List<SalidaUI> listaSalidaUI;
        private List<SalidaUI> listSolicitadosSalidaUI;
	
	private String folioSelected;
	private Date fecha;
	private String observaciones;
	private Integer cantidadServicio;
	private StreamedContent file;
	
	private Cliente clienteSelect;
	private Planta plantaSelect;
        private List<Planta> listPlantas;
	private Salida ordenSalida;
        private ServiciosSalida serviciosSalida;
        private TipoMovimiento tpMovimientoSalida;
	private DetallePartida dp;
	private DetalleConstanciaSalida dcs;
	private OrdenDeSalidas ordenDeSalidas;
	private PrecioServicio servicioSelect;
	private EstadoInventario estadoInventarioActual;
	private EstadoInventario estadoInventarioHistorico;
	private EstadoConstancia estadoConstancia;
	private SerieConstancia serie;
	
	private String folioSalida;
	private String folioServicio;
	private boolean isSalidaSaved = false;
	private boolean isServicioSaved = false;
        
        private CandadoSalida candadoSalida;
        private Integer cantidadTotal;
        
        private Integer totalCajas;
        private BigDecimal pesoTotal = BigDecimal.ZERO;
	
	private FacesContext context;
    private HttpServletRequest request;
    private HttpSession session;
    private Usuario usuario;
    
    @Inject
    private SideBarBean sideBar;

	public OrdenSalidaBean() {
		listaClientes = new ArrayList<Cliente>();
                listPlantas = new ArrayList<>(); 
		listaSalidaUI = new ArrayList<>();
                listSolicitadosSalidaUI = new ArrayList<>();
		listaServicios = new ArrayList<PrecioServicio>();
		ordenesDeSalida = new ArrayList<OrdenDeSalidas>();
		listaServiciosSalida = new ArrayList<>();
		listaFolios = new ArrayList<>();
		listaSalidasPorFolio = new ArrayList<>();
                totalCajas = 0;
                cantidadTotal = 0;
	}

	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() {
		try {
			context = FacesContext.getCurrentInstance();
			request = (HttpServletRequest) context.getExternalContext().getRequest();
			session = request.getSession(false);
			
			this.usuario = (Usuario) session.getAttribute("usuario");
			log.info("El usuario {} ha entrado a Orden de Salidas", this.usuario.getUsuario());
                        this.serie = new SerieConstancia();
			log.info("Cargando lista de clientes en orden de salidas...");
			listaClientes = sideBar.getListaClientesActivos();
			fecha = new Date();
			DateUtil.setTime(fecha, 0, 0, 0, 0);
			estadoInventarioActual = estadoInventarioBL.buscarPorId(1);
			estadoInventarioHistorico = estadoInventarioBL.buscarPorId(2);
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
		log.info("Entrando a filtrar cliente...");
		
		try {
			log.info("El usuario {}  ha seleccionado al cliente {}", this.usuario.getUsuario(), this.clienteSelect.getNombre());
			saldoBL.validaSaldo(clienteSelect, fecha);
                        candadoSalida = candadoBL.obtenerCandadoSalidaPorCliente(clienteSelect);
                        
                        if(this.usuario.getPerfil() == 1 || this.usuario.getPerfil() == 4) {
                            listPlantas.add(plantaBL.buscarPlanta(usuario.getIdPlanta()));
                            plantaSelect = listPlantas.get(0);
                        } else {
                            listPlantas = plantaBL.obtenerPlantas(false);
                        }
			
                        if(plantaSelect == null)
                            throw new InventarioException("No se ha seleccionado la planta");

                        this.listaFolios = salidasBL.obtenerFolios(clienteSelect, fecha, plantaSelect.getPlantaCve());
			this.listaServicios = precioServicioBL.buscarPorCliente(clienteSelect.getCteCve(), true);
			
			FacesUtils.addMessage(FacesMessage.SEVERITY_INFO, "Orden de Salidas", "Seleccione el folio");
		} catch(InventarioException ex) {
                        log.error("Problema para recuperar los datos del cliente: {}", ex.getMessage());
			FacesUtils.addMessage(FacesMessage.SEVERITY_WARN, "Orden de Salidas", ex.getMessage());
                } catch(Exception ex) {
			log.error("Problema para recuperar los datos del cliente.", ex);
			FacesUtils.addMessage(FacesMessage.SEVERITY_ERROR, "Orden de Salidas", "Error al seleccionar al cliente");
		} finally {
			PrimeFaces.current().ajax().update("form:messages", "form:selServicio", "form:folio-som");
		}
	}

	public void reload() throws IOException {
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
	}

	public void filtroPorOrdenesSalida() {
		Salida salidaFolio = null;
		this.listaSalidaUI = new ArrayList<SalidaUI>();
		
                try {
                        this.ordenesDeSalida = salidasBL.obtenerInventario(folioSelected, fecha);
                        if(ordenesDeSalida.isEmpty()) 
                            throw new InventarioException("No se encontrarón ordenes de salida con el folio dado");
                        
			listaSalidaUI = ordenSalidasBL.procesarOrdenesSalida(ordenesDeSalida);
			
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
		SerieConstancia serie = null;
		
		try {
                        FacesUtils.requireNonNull(clienteSelect, "Debe seleccionar un cliente");
			FacesUtils.requireNonNull(plantaSelect, "Debe seleccionar una planta");
			
			serie = serieConstanciaBL.buscarSerie(clienteSelect, plantaSelect);
                        
                        if (serie == null) {
                            this.folioSalida = "";
                            throw new InventarioException("No se encontró información de los folios del cliente. Debe indicar manualmente un folio de constancia.");
			}
                        
                        this.folioSalida = serieConstanciaBL.generarFolioSalida(serie, clienteSelect, plantaSelect);
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

	public void validarProducto(SalidaUI salida) {
		log.info("Filtrando Producto...");
                try {
                    
                    if(salida.isSalidaSelected())
                    {
                        this.listSolicitadosSalidaUI.add(salida);
                        totalCajas += salida.getCantidad();
                        pesoTotal = pesoTotal.add(salida.getPeso());
                        cantidadTotal += salida.getCantidad();
                    } else {
                        this.listSolicitadosSalidaUI.remove(salida);
                        totalCajas -= salida.getCantidad();
                        pesoTotal = pesoTotal.subtract(salida.getPeso());
                        cantidadTotal -= salida.getCantidad();
                    }
                    
                    String mensaje = salida.isSalidaSelected() ? "Producto agregado con éxito" : "Producto eliminado con éxito";
                    FacesUtils.addMessage(FacesMessage.SEVERITY_INFO, "Productos", mensaje);
		} catch (Exception ex) {
                    log.error("Problema para recuperar los datos del cliente.", ex);
                    FacesUtils.addMessage(FacesMessage.SEVERITY_ERROR, "Productos", "Ha ocurrido un error en el sistema. Intente nuevamente.\nSi el problema persiste, por favor comuniquese con su administrador del sistema.");
		} finally {
                    PrimeFaces.current().ajax().update("form:messages", "form:selServicio", "form:folio-som");
		}
	}

	public void agregaServicios() {
		ServiciosSalida ps = null;
		
		try {
                        log.info("El usuario {} esta agregando servicios", this.usuario.getUsuario());
                        FacesUtils.requireNonNull(this.servicioSelect, "Debe seleccionar al menos un servicio");
			FacesUtils.requireNonNull(this.clienteSelect, "Debe seleccionar un cliente");
			
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
	
	public synchronized void guardar() {
		ConstanciaSalida constancia = null;
		StatusConstanciaSalida statusConstancia = null;
		DetallePartida detPAnterior = null;
                DetallePartida detPNuevo= null;
                DetallePartidaPK dpPK = null;
                Integer detPartCve = null;
                List<DetalleConstanciaSalida> dcsList = null;
		List<ConstanciaSalidaServicios> listaConstanciaSalidaServicios = null;
		ConstanciaSalidaServicios css = null;
		
		//OBJETOS DE CONSTANCIA DE SERVICIO
		ConstanciaDeServicio cds = null;
		List<PartidaServicio> listaPartidaServicio = null;
		List<ConstanciaServicioDetalle> listaConstanciaSrv = null;
		
		Integer cantidadManejo = null;
		BigDecimal peso = null;
		
		try {
                        log.info("El usuario {} esta iniciando el guardado de la constancia de salida", this.usuario.getUsuario());
                        
			if(this.isSalidaSaved)
				throw new InventarioException("La constancia ya se encuentra registrada.");
			
			if(this.listSolicitadosSalidaUI.isEmpty())
				throw new InventarioException("Debe confirmar al menos un producto para la salida");
                        
                        if(observaciones == null || observaciones.equals(""))
                                throw new InventarioException("Debe observaciones para la salida");
                        
                        dcsList = new ArrayList<>();
                        saldoBL.validarSalidaMercancia(clienteSelect, fecha, cantidadTotal);
                        statusConstancia = constanciaSalidaBL.buscarStatusConstancia();
                        estadoConstancia = constanciaServicioBL.buscarEstadoConstancia(1);
                        
                        constancia = new ConstanciaSalida();
			constancia.setFecha(fecha);
			constancia.setPlacasTransporte(ordenSalida.getPlacasTransporte());
			constancia.setNombreTransportista(ordenSalida.getNombreTransportista());
			constancia.setNumero(this.folioSalida);
			constancia.setClienteCve(clienteSelect);
			constancia.setNombreCte(clienteSelect.getNombre());
			constancia.setStatus(statusConstancia);
			constancia.setObservaciones(String.format("Orden salida: %s - %s",this.ordenSalida.getFolioSalida(),  this.observaciones));
			constancia.setDetalleConstanciaSalidaList(dcsList);
			
                        tpMovimientoSalida = partidaBL.buscarTMPorId(2);
                        for (SalidaUI preS : this.listSolicitadosSalidaUI) {
				DetalleConstanciaSalida dcs = new DetalleConstanciaSalida();
				Partida p = partidaBL.buscarPartidaPorId(preS.getPartidaCve());
				
				List<DetallePartida> listadp = partidaBL.buscarPorId(p.getPartidaCve());
				p.setDetallePartidaList(listadp);
				
                                detPAnterior = listadp.get(listadp.size() - 1);
                                detPartCve = detPAnterior.getDetallePartidaPK().getDetPartCve() + 1;
                                
                                dpPK = detPAnterior.getDetallePartidaPK().clone();
				dpPK.setPartidaCve(detPAnterior.getDetallePartidaPK().getPartidaCve());
                                dpPK.setDetPartCve(detPartCve);
                        
                                detPNuevo = detPAnterior.clone();
				detPNuevo.setDetallePartidaPK(dpPK);
                                detPAnterior.setEdoInvCve(estadoInventarioHistorico);
				
                                detPNuevo.getDetallePartidaPK().setDetPartCve(detPartCve);
				detPNuevo.setDetallePartida(detPAnterior);
				detPNuevo.setEdoInvCve(estadoInventarioActual);
				
                                cantidadManejo = detPNuevo.getCantidadUManejo() - preS.getCantidad();
				peso = detPNuevo.getCantidadUMedida();
				peso = peso.subtract(preS.getPeso());
				detPNuevo.setCantidadUManejo(cantidadManejo);
				detPNuevo.setCantidadUMedida(peso);
                                detPNuevo.setTipoMovCve(tpMovimientoSalida);
				
                                partidaBL.actualizarDetallePartida(detPAnterior);
				partidaBL.guardarDetallePartida(detPNuevo);
                                p.add(detPNuevo);
				
				Camara c = plantaBL.buscarCamaraPorId(p.getCamaraCve().getCamaraCve());
				dcs.setPartidaCve(p);
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
			
			listaConstanciaSalidaServicios = ordenSalidasBL.addConstanciaSalidaServicios(listaServiciosSalida, constancia);
			constancia.setConstanciaSalidaServiciosList(listaConstanciaSalidaServicios);
			
			constanciaSalidaBL.guardar(constancia);
			
			this.isSalidaSaved = true;
			
			serieConstanciaBL.guardarSerieConstancia(serie);
			
                        salidasBL.actualizarSalida(ordenSalida);
			
			if(sideBar.getNumeroEntradas() != null) {
                            log.info("Actualizando número de ordenes de salida...");
                            sideBar.setNumeroSalidas(sideBar.getNumeroSalidas() - 1);
                            log.info("Numero de ordenes de salida actualizado.");
			}
                        
                        candadoBL.actualizaCandadoSalida(candadoSalida);
                        
			if(listaServiciosSalida.isEmpty()) {
                            FacesUtils.addMessage(FacesMessage.SEVERITY_INFO, "Orden Salida", "Constancia guardada correctamente.");
                            return;
			}
				
			cds = new ConstanciaDeServicio();
			cds.setFolioCliente(this.folioServicio);
			cds.setCteCve(clienteSelect);
			cds.setFecha(fecha);
			cds.setNombreTransportista(ordenSalida.getNombreTransportista());
			cds.setPlacasTransporte(ordenSalida.getPlacasTransporte());
			cds.setObservaciones(String.format("Salida: %s - %s", this.folioSalida, this.observaciones));
			cds.setValorDeclarado(new BigDecimal(1));
			cds.setStatus(estadoConstancia);
			
			listaPartidaServicio = ordenSalidasBL.addPartidasServicios(listaSalidaUI, cds);
                        cds.setPartidaServicioList(listaPartidaServicio);
		
			listaConstanciaSrv = ordenSalidasBL.addConstSrvDet(listaServiciosSalida, cds);
			cds.setConstanciaServicioDetalleList(listaConstanciaSrv);
			
			constanciaServicioBL.guardarConstancia(cds);
			
			this.isServicioSaved = true;
			FacesUtils.addMessage(FacesMessage.SEVERITY_INFO, "Orden Salida", "Constancia guardada correctamente.");
		} catch (InventarioException ex) {
                        this.isSalidaSaved = false;
                        log.warn("Problema para obtener el listado de la orden...", ex);
			FacesUtils.addMessage(FacesMessage.SEVERITY_WARN, "Orden", ex.getMessage());
		} catch (Exception ex) {
			log.error("Problema para obtener el listado de la orden...", ex);
			FacesUtils.addMessage(FacesMessage.SEVERITY_ERROR, "Orden", "Ha ocurrido un error en el sistema. Intente nuevamente.\nSi el problema persiste, por favor comuniquese con su administrador del sistema.");
		} finally {
			PrimeFaces.current().ajax().update("form:messages form:numeroSalidas");
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
			if(!this.isSalidaSaved)
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
			if(!this.isServicioSaved)
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

        public Planta getPlantaSelect() {
            return plantaSelect;
        }

        public void setPlantaSelect(Planta plantaSelect) {
            this.plantaSelect = plantaSelect;
        }

        public List<Planta> getListPlantas() {
            return listPlantas;
        }

        public void setListPlantas(List<Planta> listPlantas) {
            this.listPlantas = listPlantas;
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

	public List<SalidaUI> getListaSalidaUI() {
		return listaSalidaUI;
	}

	public void setListaSalidaUI(List<SalidaUI> listaSalidaUI) {
		this.listaSalidaUI = listaSalidaUI;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
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
