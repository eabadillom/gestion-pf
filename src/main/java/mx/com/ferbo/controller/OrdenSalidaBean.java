package mx.com.ferbo.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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
import javax.annotation.PreDestroy;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
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
import org.primefaces.model.file.UploadedFile;

import com.ferbo.mail.beans.Adjunto;

import mx.com.ferbo.business.SendMailTicketSalida;
import mx.com.ferbo.business.almacen.EstadoInventarioBL;
import mx.com.ferbo.business.almacen.PlantaBL;
import mx.com.ferbo.business.constancias.ConstanciaSalidaBL;
import mx.com.ferbo.business.constancias.ConstanciaServicioBL;
import mx.com.ferbo.business.n.CandadoBL;
import mx.com.ferbo.business.n.PartidaBL;
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
import mx.com.ferbo.model.TipoMovimiento;
import mx.com.ferbo.model.Usuario;
import mx.com.ferbo.ui.OrdenDeSalidas;
import mx.com.ferbo.ui.SalidaDetalleUI;
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
        
	@Inject private OrdenSalidasBL ordenSalidaBO;
	@Inject private ConstanciaSalidaBL constanciaSalidaBO;
	@Inject private ConstanciaServicioBL constanciaServicioBO;
	@Inject private CandadoBL candadoBO;
	@Inject private SerieConstanciaBL servicioConstanciaBO;
	@Inject private PrecioServicioBL precioServicioBO;
	@Inject private PartidaBL partidaBO;
	@Inject private SaldosBL saldoBO;
	@Inject private EstadoInventarioBL estadoInventarioBO;
	@Inject private SideBarBean sideBar;
	@Inject private PlantaBL plantaBO;
	@Inject private SalidasBL salidaBO;
	@Inject	private ConstanciaSalidaBL constanciaBO;
	
	private ConstanciaSalida constancia;
	private List<Cliente> listaClientes;
	
	private List<SalidaDetalleUI> listaDetalles = null;
	private List<SalidaDetalleUI> listaDetallesSelected = null;
	private List<PrecioServicio> listaServicios;
	private List<ServiciosSalida> listaServiciosSalida;
	private List<String> listaFolios;
	
	private String folioSelected;
	private Date fecha;
	private String observaciones;
	private Integer cantidadServicio;
	private StreamedContent file;
	
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

	private Integer totalCajas;
	private BigDecimal pesoTotal = BigDecimal.ZERO;

	private FacesContext context;
	private HttpServletRequest request;
	private HttpSession session;
	private Usuario usuario;
	private Planta planta;
	
	private UploadedFile attachmentFile;
	private List<Adjunto> archivosList;
	private Adjunto selectedAttachment;
	private BigDecimal tamanioTotal = null;
	private BigDecimal limite = new BigDecimal("10485760").setScale(2, BigDecimal.ROUND_HALF_UP);
	private BigDecimal megabyte = new BigDecimal("1048576").setScale(2, BigDecimal.ROUND_HALF_UP);
    
	public OrdenSalidaBean() {
		this.listaClientes = new ArrayList<Cliente>();
		this.listPlantas = new ArrayList<>();
		this.listaServicios = new ArrayList<PrecioServicio>();
		this.listaServiciosSalida = new ArrayList<>();
		this.listaFolios = new ArrayList<>();
		this.listaDetallesSelected = new ArrayList<SalidaDetalleUI>();
		this.totalCajas = 0;
		
		this.archivosList = new ArrayList<Adjunto>();
        this.tamanioTotal = new BigDecimal("0.00").setScale(2, BigDecimal.ROUND_HALF_UP);
	}
	
	@PostConstruct
	public synchronized void init() {
		try {
        	this.context = FacesContext.getCurrentInstance();
        	this.request = (HttpServletRequest) this.context.getExternalContext().getRequest();
        	this.session = this.request.getSession(false);
        	
        	this.usuario = (Usuario) session.getAttribute("usuario");
			log.info("El usuario {} ha entrado a Orden de Salidas", this.usuario.getUsuario());
                        this.serie = new SerieConstancia();
			log.info("Cargando lista de clientes en orden de salidas...");
        	
			this.planta = plantaBO.buscar(this.usuario.getIdPlanta());
        	
        	this.listaClientes = this.salidaBO.getListaClientesPendientes(this.planta);
			this.fecha = new Date();
			DateUtil.setTime(fecha, 0, 0, 0, 0);
			this.estadoInventarioActual = this.estadoInventarioBO.actual();
			this.estadoInventarioHistorico = this.estadoInventarioBO.historico();
			this.tpMovimientoSalida = this.partidaBO.buscarTMPorId(2);
			
			byte bytes[] = {};
			this.file = FacesUtils.toPDF(bytes, "TicketSalida.pdf");
			
			this.ordenSalida = ordenSalidaBO.create();
			this.constancia = constanciaSalidaBO.create();
        	
        } catch(Exception ex) {
        	log.error("Problema con el inicio del controller...", ex);
        }
	}

	@PreDestroy
	public void destroy() {
		log.info("Saliendo del alta de constancias de depósito.");
	}

	public void filtrarCliente() {
		try {
			log.info("Cargando la información del cliente...");
			if(this.constancia.getClienteCve() == null)
				throw new InventarioException("Debe seleccionar un cliente.");
			
			log.info("El usuario {}  ha seleccionado al cliente {}", this.usuario.getUsuario(), this.constancia.getClienteCve().getNombre());
			this.saldoBO.validaSaldo(this.constancia.getClienteCve(), fecha);
			this.candadoSalida = candadoBO.obtenerCandadoSalidaPorCliente(this.constancia.getClienteCve());
                        
			if(this.usuario.getPerfil() == 1 || this.usuario.getPerfil() == 4) {
				listPlantas.add(plantaBO.buscar(usuario.getIdPlanta()));
				plantaSelect = listPlantas.get(0);
			} else {
				listPlantas = plantaBO.buscarTodos(false);
			}
			
			if(plantaSelect == null)
				throw new InventarioException("Seleccione una planta");
			
			this.listaFolios = salidaBO.obtenerFolios(this.constancia.getClienteCve(), fecha, plantaSelect.getPlantaCve());
			this.listaServicios = precioServicioBO.buscarPorCliente(this.constancia.getClienteCve().getCteCve(), true);
			
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

	public void cargarOrden() {
		try {
			this.saldoBO.validarSalidaMercancia(this.constancia.getClienteCve(), fecha);
			this.generaFolioSalida();
			this.ordenSalida = this.salidaBO.buscar(this.folioSelected);
			this.listaDetalles = this.salidaBO.toUI(this.ordenSalida.getListSalidaDetalle());
			this.listaServiciosSalida = this.ordenSalida.getListServiciosSalida();
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
			FacesUtils.requireNonNull(this.constancia.getClienteCve(), "Debe seleccionar un cliente");
			FacesUtils.requireNonNull(plantaSelect, "Debe seleccionar una planta");
			
			serie = servicioConstanciaBO.buscarSerie(this.constancia.getClienteCve(), plantaSelect);
                        
			if (serie == null) {
				this.folioSalida = "";
				throw new InventarioException("No se encontró información de los folios del cliente. Debe indicar manualmente un folio de constancia.");
			}
                        
			this.folioSalida = servicioConstanciaBO.generarFolioSalida(serie, this.constancia.getClienteCve(), plantaSelect);
			this.folioServicio = String.format("S%s", this.folioSalida);
			String folio = constanciaSalidaBO.buscarPorFolioSalida(this.folioSalida);
			
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

	public void validarProducto(SalidaDetalleUI salida) {
		try {
			if(salida.getSelected())
				log.info("Partida: {} - CONFIRMADO", salida.getPartida().getPartidaCve());
			else
				log.info("Partida: {} - SIN CONFIRMAR", salida.getPartida().getPartidaCve());
                    
			if(salida.getSelected())
				this.listaDetallesSelected.add(salida);
			else
				this.listaDetallesSelected.remove(salida);
			
			this.totalCajas = this.listaDetallesSelected.stream()
					.mapToInt(SalidaDetalleUI::getCantidad)
					.sum();
			
			this.pesoTotal = this.listaDetallesSelected.stream()
					.map(SalidaDetalleUI::getPesoAprox)
					.reduce(BigDecimal.ZERO, BigDecimal::add)
					.setScale(3, RoundingMode.HALF_UP);
                    
		} catch (Exception ex) {
			log.error("Problema para recuperar los datos del cliente.", ex);
			FacesUtils.addMessage(FacesMessage.SEVERITY_ERROR, "Productos", "Ha ocurrido un error en el sistema. Intente nuevamente.\nSi el problema persiste, por favor comuniquese con su administrador del sistema.");
		} finally {
			PrimeFaces.current().ajax().update("form:messages", "form:selServicio", "form:folio-som");
		}
	}
	
	public DetallePartida detallePartida(Partida p) {
		return p.getDetallePartidaList().get(p.getDetallePartidaList().size() - 1);
	}

	public void agregaServicios() {
		ServiciosSalida ps = null;
		
		try {
			log.info("El usuario {} esta agregando servicios", this.usuario.getUsuario());
			FacesUtils.requireNonNull(this.servicioSelect, "Debe seleccionar al menos un servicio");
			FacesUtils.requireNonNull(this.constancia.getClienteCve(), "Debe seleccionar un cliente");
			
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
	
	public void validarTemperatura(SalidaDetalleUI detalle) {
            log.debug("Salida {}", detalle.getTemperatura());
	}
	
	public void deleteServicio(ServiciosSalida servicio) {
		log.info("Servicio eliminado: {}", servicio.getServicio().getServicioNombre());
		this.listaServiciosSalida.remove(servicio);
	}
	
	public void validarOrden() {
		
		if(this.constancia == null || this.constancia.getId() == null)
			PrimeFaces.current().executeScript("PF('dialogCargaError').show();");
		else
			PrimeFaces.current().executeScript("PF('dialogCarga').show();");
	}
	
	public synchronized void guardar() {
		DetallePartida detPAnterior = null;
		DetallePartida detPNuevo= null;
		DetallePartidaPK dpPK = null;
		Integer detPartCve = null;
		List<DetalleConstanciaSalida> dcsList = null;
		List<ConstanciaSalidaServicios> listaConstanciaSalidaServicios = null;
		
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
			
			if(observaciones == null || observaciones.equals(""))
				throw new InventarioException("Debe indicar las observaciones");
			
			log.info("Elementos confirmados: {}", this.listaDetallesSelected.size());
			if(this.listaDetallesSelected.size() <= 0)
				throw new InventarioException("Debe confirmar al menos un producto");
			
			for(SalidaDetalleUI detalle : this.listaDetallesSelected) {
				if(detalle.getTemperatura() == null)
					throw new InventarioException("Debe indicar la temperatura de salida de sus productos.");
			}
			dcsList = this.constancia.getDetalleConstanciaSalidaList();
			
			this.constancia = ordenSalidaBO.toConstanciaSalida(ordenSalida);
			this.constancia.setNumero(this.folioSalida);
			this.constancia.setObservaciones(this.constancia.getObservaciones().concat(this.observaciones));
			
			for (SalidaDetalleUI detalle : this.listaDetallesSelected) {
				
				DetalleConstanciaSalida dcs = new DetalleConstanciaSalida();
				
				List<DetallePartida> detallesPartida = detalle.getPartida().getDetallePartidaList();
				
				detPAnterior = detallesPartida.get(detallesPartida.size() - 1);
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
				
				cantidadManejo = detPNuevo.getCantidadUManejo() - detalle.getCantidad();
				peso = detPNuevo.getCantidadUMedida();
				peso = peso.subtract(detalle.getPesoAprox());
				detPNuevo.setCantidadUManejo(cantidadManejo);
				detPNuevo.setCantidadUMedida(peso);
				detPNuevo.setTipoMovCve(tpMovimientoSalida);
				
				partidaBO.actualizarDetallePartida(detPAnterior);
				partidaBO.guardarDetallePartida(detPNuevo);
				detalle.getPartida().add(detPNuevo);
				
				Camara c = plantaBO.buscarCamaraPorId(detalle.getPartida().getCamaraCve().getCamaraCve());
				dcs.setPartidaCve(detalle.getPartida());
				dcs.setCamaraCve(c.getCamaraCve());
				dcs.setFolioEntrada(detalle.getPartida().getFolio().getFolioCliente());
				dcs.setCantidad(detalle.getCantidad());
				dcs.setPeso(detalle.getPesoAprox());
				dcs.setUnidad(detalle.getPartida().getUnidadDeProductoCve().getUnidadDeManejoCve().getUnidadDeManejoDs());
				dcs.setProducto(detalle.getPartida().getUnidadDeProductoCve().getProductoCve().getProductoDs());
				dcs.setTemperatura(detalle.getTemperatura().toString());
				dcs.setConstanciaCve(constancia);
				dcs.setCamaraCadena(detalle.getPartida().getCamaraCve().getCamaraDs());
				dcs.setDetPartCve(detPNuevo.getDetallePartidaPK().getDetPartCve());
				dcsList.add(dcs);
			}
			this.constancia.setDetalleConstanciaSalidaList(dcsList);
			listaConstanciaSalidaServicios = ordenSalidaBO.addConstanciaSalidaServicios(listaServiciosSalida, constancia);
			constancia.setConstanciaSalidaServiciosList(listaConstanciaSalidaServicios);
			
			constanciaSalidaBO.guardar(constancia);
			
			this.isSalidaSaved = true;
			
			servicioConstanciaBO.guardarSerieConstancia(serie);
			
			salidaBO.actualizarSalida(ordenSalida);
			
			if(sideBar.getNumeroEntradas() != null) {
				log.info("Actualizando número de ordenes de salida...");
				sideBar.setNumeroSalidas(sideBar.getNumeroSalidas() - 1);
				log.info("Numero de ordenes de salida actualizado.");
			}
                        
			candadoBO.actualizaCandadoSalida(candadoSalida);
			
			sideBar.cargaOrdenesDeSalida();
			PrimeFaces.current().ajax().update("topbarForm", "menuform:frb_orden_retiro");
                        
			if(listaServiciosSalida.isEmpty()) {
				FacesUtils.addMessage(FacesMessage.SEVERITY_INFO, "Orden Salida", "Constancia guardada correctamente.");
				return;
			}
			
			estadoConstancia = constanciaServicioBO.buscarEstadoConstancia(1);
			cds = new ConstanciaDeServicio();
			cds.setFolioCliente(this.folioServicio);
			cds.setCteCve(this.constancia.getClienteCve());
			cds.setFecha(fecha);
			cds.setNombreTransportista(ordenSalida.getNombreTransportista());
			cds.setPlacasTransporte(ordenSalida.getPlacasTransporte());
			cds.setObservaciones(String.format("Salida: %s - %s", this.folioSalida, this.observaciones));
			cds.setValorDeclarado(new BigDecimal(1));
			cds.setStatus(estadoConstancia);
			
			listaPartidaServicio = ordenSalidaBO.addPartidasServicios(this.listaDetallesSelected, cds);
			cds.setPartidaServicioList(listaPartidaServicio);
		
			listaConstanciaSrv = ordenSalidaBO.addConstSrvDet(listaServiciosSalida, cds);
			cds.setConstanciaServicioDetalleList(listaConstanciaSrv);
			
			constanciaServicioBO.guardarConstancia(cds);
			
			this.isServicioSaved = true;
			FacesUtils.addMessage(FacesMessage.SEVERITY_INFO, "Orden Salida", "Constancia guardada correctamente.");
		} catch (InventarioException ex) {
			this.isSalidaSaved = false;
			log.warn("Problema para obtener el listado de la orden...", ex.getMessage());
			FacesUtils.addMessage(FacesMessage.SEVERITY_WARN, "Orden", ex.getMessage());
		} catch (Exception ex) {
			log.error("Problema para obtener el listado de la orden...", ex);
			FacesUtils.addMessage(FacesMessage.SEVERITY_ERROR, "Orden", "Ha ocurrido un error en el sistema. Intente nuevamente.\nSi el problema persiste, por favor comuniquese con su administrador del sistema.");
		} finally {
			PrimeFaces.current().ajax().update("form:messages form:numeroSalidas");
		}
	}	
	
	public void imprimirTicketSalida() {
		
		try {
			String filename = String.format("Salida-%s.pdf", this.constancia.getNumero());
			byte[] bytes = constanciaBO.exportToPDF(this.constancia.getNumero())
					.orElseThrow(() -> new InventarioException("Problema para generar el ticket de salida"));
			this.file = FacesUtils.toPDF(bytes, filename);
		} catch(Exception ex) {
			log.error("Problema para generar el ticket de la constancia de salida...", ex);
		}
		
//		String jasperPath = "/jasper/ConstanciaSalida.jrxml";
//		String filename = String.format("ticketSalida-%s.pdf", this.folioSalida);
//		String logo = "/images/logoF.png";
//		
//		File reportFile = new File(jasperPath);
//		File imgFile = null;
//		JasperReportUtil jasperReportUtil = new JasperReportUtil();
//		
//		Map<String, Object> parameters = new HashMap<String, Object>();
//		Connection connection = null;
//		parameters = new HashMap<String, Object>();
//		
//		try {
//			if(!this.isSalidaSaved)
//				throw new InventarioException("Debe guardar la salida.");
//			
//			URL resource = getClass().getResource(jasperPath);//verifica si el recurso esta disponible 
//			URL logoUrl = getClass().getResource(logo); 
//			String file = resource.getFile();//retorna la ubicacion del archivo
//			String logoPath = logoUrl.getFile();
//			reportFile = new File(file);//crea un archivo
//			imgFile = new File(logoPath);
//			log.info(reportFile.getPath());
//			connection = EntityManagerUtil.getConnection();
//			parameters.put("REPORT_CONNECTION", connection);
//			parameters.put("NUMERO", this.folioSalida);
//			parameters.put("LogoPath", imgFile.getPath());
//			log.info("Parametros: {}", parameters);
//			byte[] bytes = jasperReportUtil.createPDF(parameters, reportFile.getPath());
//			InputStream input = new ByteArrayInputStream(bytes);
//			this.file = DefaultStreamedContent.builder()
//					.contentType("application/pdf")
//					.name(filename)
//					.stream(() -> input )
//					.build();
//			log.info("Orden de salida generada {}...", filename);
//		} catch (Exception e) {
//			log.error("Problema para para imrprimir el folio...", e);
//			FacesUtils.addMessage(FacesMessage.SEVERITY_ERROR, "Error en impresion", String.format("No se pudo imprimir el folio %s", ordenSalida.getFolioSalida()));
//			PrimeFaces.current().ajax().update("form:messages");
//		}finally {
//			conexion.close((Connection) connection);
//		}

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
	
	public void cargarArchivo() {
		Adjunto archivo = null;
		BigDecimal tamanio = null;
		
		try {
			
			this.tamanioTotal = new BigDecimal("0.00").setScale(2, BigDecimal.ROUND_HALF_UP);
			tamanio = new BigDecimal("0.00").setScale(2, BigDecimal.ROUND_HALF_UP);
			
			if(this.attachmentFile == null)
				throw new InventarioException("Debe seleccionar un archivo");
			
			if(this.attachmentFile.getSize() == 0)
				throw new InventarioException("El archivo no debe estar vacío.");
			
			for(Adjunto a : archivosList) {
				tamanio = tamanio.add(new BigDecimal(a.getTamanio()));
			}
			tamanioTotal = tamanioTotal.add(tamanio);
			
			archivo = new Adjunto(attachmentFile.getFileName(), Adjunto.TP_ARCHIVO_GENERICO, attachmentFile.getContent());
			tamanio = tamanio.add(new BigDecimal(archivo.getTamanio()));
			
			if(tamanio.compareTo(limite) > 0) //El tamaño debe ser menor o igual a 10 MB.
				throw new InventarioException("El tamaño de todos los archivos no debe superar los 10 MB.");
			tamanioTotal = tamanio;
			
			log.info("Tamaño total de archivos adjuntos (MB): " + tamanioTotal);
			this.archivosList.add(archivo);
			this.attachmentFile = null;
			
			FacesUtils.addMessage(FacesMessage.SEVERITY_INFO, "Emisión de salida", "El archivo se cargó correctamente.");
		} catch(InventarioException ex) {
			log.error("Problema con la emisión de salidas...", ex);
                        FacesUtils.addMessage(FacesMessage.SEVERITY_WARN, "Emisión de salida", ex.getMessage());
		} catch (Exception ex) {
			log.error("Problema con la emisión de salidas...", ex);
			FacesUtils.addMessage(FacesMessage.SEVERITY_ERROR, "Emisión de salida", "Su solicitud no se pudo generar.\nFavor de comunicarse con el administrador del sistema.");
		} finally {
			tamanioTotal = tamanioTotal.divide(megabyte, BigDecimal.ROUND_HALF_UP);
			
			PrimeFaces.current().ajax().update("form:messages");
		}
	}
	
	public void eliminarAdjunto() {
		BigDecimal tamanio = new BigDecimal("0.00").setScale(2, BigDecimal.ROUND_HALF_UP);
		this.archivosList.remove(this.selectedAttachment);
		for(Adjunto a : archivosList) {
			tamanio = tamanio.add(new BigDecimal(a.getTamanio()));
		}
		this.tamanioTotal = tamanio.divide(megabyte, BigDecimal.ROUND_HALF_UP);
	}
	
	public void notificar() {
 		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		String titulo = "Confirmación";
		
		SendMailTicketSalida sendBO = null;
		
		try {
			if(this.constancia == null || this.constancia.getId() == null)
				throw new InventarioException("Debe confirmar una orden de retiro.");
			
			if(this.archivosList == null || this.archivosList.size() <= 0)
				throw new InventarioException("Debe agregar al menos un archivo a la notificación.");
		
			log.info("Confirmando la orden de retiro {}", this.folioSelected);
			sendBO = new SendMailTicketSalida(this.constancia.getClienteCve().getCteCve());
			sendBO.setFolio(this.folioSalida);
			sendBO.setLoggedUser(this.usuario);
			for(Adjunto a : this.archivosList ) {
				sendBO.addAttachment(a);
			}
			sendBO.send();
			
			mensaje = "El mensaje se envió correctamente.";
			severity = FacesMessage.SEVERITY_INFO;
			
			PrimeFaces.current().executeScript("PF('dialogCarga').hide()");
		} catch(InventarioException ex) {
			log.warn("Problema para enviar el correo electrónico...", ex.getMessage());
			mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_WARN;
		} catch(Exception ex) {
			log.error("Problema para enviar el correo electrónico...", ex);
			mensaje = "Ha ocurrido un error en el sistema. Intente nuevamente.\nSi el problema persiste, por favor comuniquese con su administrador del sistema.";
			severity = FacesMessage.SEVERITY_ERROR;
		} finally {
			message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
			PrimeFaces.current().ajax().update("form:messages");
		}
		
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

	public List<ServiciosSalida> getListaServiciosSalida() {
		return listaServiciosSalida;
	}

	public void setListaServiciosSalida(List<ServiciosSalida> listaServiciosSalida) {
		this.listaServiciosSalida = listaServiciosSalida;
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
	
	public UploadedFile getAttachmentFile() {
		return attachmentFile;
	}

	public void setAttachmentFile(UploadedFile attachmentFile) {
		this.attachmentFile = attachmentFile;
	}

	public List<Adjunto> getArchivosList() {
		return archivosList;
	}

	public void setArchivosList(List<Adjunto> archivosList) {
		this.archivosList = archivosList;
	}

	public BigDecimal getTamanioTotal() {
		return tamanioTotal;
	}

	public void setTamanioTotal(BigDecimal tamanioTotal) {
		this.tamanioTotal = tamanioTotal;
	}

	public BigDecimal getLimite() {
		return limite;
	}

	public void setLimite(BigDecimal limite) {
		this.limite = limite;
	}

	public BigDecimal getMegabyte() {
		return megabyte;
	}

	public void setMegabyte(BigDecimal megabyte) {
		this.megabyte = megabyte;
	}

	public Adjunto getSelectedAttachment() {
		return selectedAttachment;
	}

	public void setSelectedAttachment(Adjunto selectedAttachment) {
		this.selectedAttachment = selectedAttachment;
	}

	public ConstanciaSalida getConstancia() {
		return constancia;
	}

	public void setConstancia(ConstanciaSalida constancia) {
		this.constancia = constancia;
	}

	public Salida getOrdenSalida() {
		return ordenSalida;
	}

	public void setOrdenSalida(Salida ordenSalida) {
		this.ordenSalida = ordenSalida;
	}

	public List<SalidaDetalleUI> getListaDetalles() {
		return listaDetalles;
	}

	public void setListaDetalles(List<SalidaDetalleUI> listaDetalles) {
		this.listaDetalles = listaDetalles;
	}
}
