package mx.com.ferbo.controller;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;

import com.ferbo.facturama.tools.FacturamaException;

import mx.com.ferbo.business.FacturamaBL;
import mx.com.ferbo.dao.AsentamientoHumandoDAO;
import mx.com.ferbo.dao.AvisoDAO;
import mx.com.ferbo.dao.ClaveUnidadDAO;
import mx.com.ferbo.dao.ClienteDAO;
import mx.com.ferbo.dao.ClienteDomiciliosDAO;
import mx.com.ferbo.dao.ConceptoDAO;
import mx.com.ferbo.dao.EmisoresCFDISDAO;
import mx.com.ferbo.dao.FacturaDAO;
import mx.com.ferbo.dao.MedioPagoDAO;
import mx.com.ferbo.dao.MetodoPagoDAO;
import mx.com.ferbo.dao.ParametroDAO;
import mx.com.ferbo.dao.PlantaDAO;
import mx.com.ferbo.dao.PrecioServicioDAO;
import mx.com.ferbo.dao.SerieFacturaDAO;
import mx.com.ferbo.dao.StatusFacturaDAO;
import mx.com.ferbo.dao.TipoCobroDAO;
import mx.com.ferbo.dao.TipoFacturacionDAO;
import mx.com.ferbo.dao.UnidadDeManejoDAO;
import mx.com.ferbo.model.AsentamientoHumano;
import mx.com.ferbo.model.Aviso;
import mx.com.ferbo.model.ClaveUnidad;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.ClienteDomicilios;
import mx.com.ferbo.model.Concepto;
import mx.com.ferbo.model.ConstanciaDeDeposito;
import mx.com.ferbo.model.ConstanciaFactura;
import mx.com.ferbo.model.ConstanciaFacturaDs;
import mx.com.ferbo.model.Domicilios;
import mx.com.ferbo.model.EmisoresCFDIS;
import mx.com.ferbo.model.Factura;
import mx.com.ferbo.model.FacturaMedioPago;
import mx.com.ferbo.model.FacturaMedioPagoPK;
import mx.com.ferbo.model.MedioPago;
import mx.com.ferbo.model.MetodoPago;
import mx.com.ferbo.model.Parametro;
import mx.com.ferbo.model.PartidaServicio;
import mx.com.ferbo.model.Planta;
import mx.com.ferbo.model.PrecioServicio;
import mx.com.ferbo.model.SerieFactura;
import mx.com.ferbo.model.ServicioFactura;
import mx.com.ferbo.model.StatusFactura;
import mx.com.ferbo.model.TipoCobro;
import mx.com.ferbo.model.TipoFacturacion;
import mx.com.ferbo.model.UnidadDeManejo;
import mx.com.ferbo.model.Usuario;
import mx.com.ferbo.util.DateUtil;
import mx.com.ferbo.util.EntityManagerUtil;
import mx.com.ferbo.util.FormatUtil;
import mx.com.ferbo.util.InventarioException;
import mx.com.ferbo.util.JasperReportUtil;
import mx.com.ferbo.util.conexion;
import net.sf.jasperreports.engine.JRException;

@Named
@ViewScoped
public class FacturaServiciosBean implements Serializable {

	private static final long serialVersionUID = 3500528042991256313L;
	private static Logger log = LogManager.getLogger(FacturaServiciosBean.class);
	
	private FacesContext context;
	private HttpServletRequest request;
	private HttpSession session;
	
	private List<Cliente> clientes;
	private List<ClienteDomicilios> listaClienteDom;    
	private List<ClienteDomicilios> listaClienteDomicilio;   
	private List<Planta> listaPlanta;
	private List<Aviso> listaA;
	private List<Aviso> listaAviso;
	private List<MetodoPago> listaMetodoPago;
	private List<MedioPago> listaMedioPago;
	private List<PrecioServicio> alServicios;
	private List<UnidadDeManejo> alUnidades;
	private List<PartidaServicio> alPartidas;
	private List<ConstanciaDeDeposito> listaEntradas;
	private List<ConstanciaFactura> listaVigencias;
	private List<ConstanciaFacturaDs> listaServicios;
	private List<SerieFactura> listaSerieF;
	private List<SerieFactura> listaSerieFactura;
	private List<ServicioFactura> alServiciosDetalle;
	private List<FacturaMedioPago> listaFacturaMedioPago;
	private List<EmisoresCFDIS> emisores;

	private Cliente clienteSelect;
	private Domicilios domicilioSelect;
	private Planta plantaSelect;
	private MetodoPago metodoPagoSelect;
	private String medioPagoSelect;
	private Parametro iva, retencion;
	private SerieFactura serieFacturaSelect;
	private ServicioFactura selServicio;
	private ServicioFactura otroServicio;
	private PrecioServicio precioServicio;
	private Factura factura;
	private EmisoresCFDIS emisor;
	private TipoCobro tipoCobroPrecioFijo;
	private Concepto concepto;
	private ClaveUnidad claveUnidad;

	private ClienteDAO clienteDAO;
	private ClienteDomiciliosDAO clienteDomicilioDAO;
	private PlantaDAO plantaDAO;
	private AvisoDAO avisoDAO;
	private MetodoPagoDAO metodoPagoDAO;
	private MedioPagoDAO medioPagoDAO;
	private ParametroDAO parametroDAO;
	private SerieFacturaDAO serieFacturaDAO;
	private FacturaDAO facturaDAO;
	private AsentamientoHumandoDAO asnDAO;
	private StatusFacturaDAO statusDAO;
	private TipoFacturacionDAO tipoDAO;
	private PrecioServicioDAO psDAO;
	private EmisoresCFDISDAO emisorDAO;
	private ConceptoDAO conceptoDAO;
	private ClaveUnidadDAO cveUnidadDAO;
	private UnidadDeManejoDAO unidadManejoDAO;
	private TipoCobroDAO tipoCobroDAO;

	private int plazoSelect;
	private Integer idPrecioServicio;
	private Integer  idFactura;
	private Date fechaFactura;
	private Date fechaCorte;
	private String moneda = "MXN";
	private String montoLetra;
	private String Obervaciones;
	private BigDecimal cantidadServicio;
	private BigDecimal subtotal;
	private BigDecimal bdIva;
	private BigDecimal total;
	private BigDecimal tasaIva;
	private String referencia;
	
	private int idServicioFacturaTmp = 0;
	
	private Usuario usuario;

	public FacturaServiciosBean() {
		
		alServicios = new ArrayList<PrecioServicio>();
		listaClienteDom = new ArrayList<ClienteDomicilios>();
		listaClienteDomicilio = new ArrayList<ClienteDomicilios>();
		listaPlanta = new ArrayList<Planta>();
		listaSerieF = new ArrayList<SerieFactura>();
		listaSerieFactura = new ArrayList<SerieFactura>();
		listaA = new ArrayList<Aviso>();
		listaAviso = new ArrayList<Aviso>();
		listaMetodoPago = new ArrayList<MetodoPago>();
		alServiciosDetalle = new ArrayList<ServicioFactura>();
		listaEntradas = new ArrayList<ConstanciaDeDeposito>();
		listaVigencias = new ArrayList<ConstanciaFactura>();
		listaServicios = new ArrayList<ConstanciaFacturaDs>();
		listaFacturaMedioPago = new ArrayList<FacturaMedioPago>();
		
		subtotal = new BigDecimal(0);
		bdIva = new BigDecimal(0);
		total = new BigDecimal(0);
		fechaFactura = new Date();
		clienteSelect = new Cliente();
		domicilioSelect = new Domicilios();
		metodoPagoSelect = null;
		medioPagoSelect = null;
		
		clienteDAO = new ClienteDAO();
		clienteDomicilioDAO = new ClienteDomiciliosDAO();
		plantaDAO = new PlantaDAO();
		avisoDAO = new AvisoDAO();
		metodoPagoDAO = new MetodoPagoDAO();
		medioPagoDAO = new MedioPagoDAO();
		parametroDAO = new ParametroDAO();
		serieFacturaDAO = new SerieFacturaDAO();
		asnDAO = new AsentamientoHumandoDAO();
		statusDAO = new StatusFacturaDAO();
		tipoDAO = new TipoFacturacionDAO();
		facturaDAO = new FacturaDAO();
		psDAO = new PrecioServicioDAO();
		emisorDAO = new EmisoresCFDISDAO();
		conceptoDAO = new ConceptoDAO();
		cveUnidadDAO = new ClaveUnidadDAO();
		unidadManejoDAO = new UnidadDeManejoDAO();
		tipoCobroDAO = new TipoCobroDAO();
	}

	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() {
		context = FacesContext.getCurrentInstance();
        request = (HttpServletRequest) context.getExternalContext().getRequest();
        session = request.getSession(false);
        
		clientes = (List<Cliente>) request.getSession(false).getAttribute("clientesActivosList");
		listaPlanta = plantaDAO.findall();
		listaMetodoPago = metodoPagoDAO.buscarTodos();
		listaMedioPago = medioPagoDAO.buscarVigentes(new Date());
		listaSerieF = serieFacturaDAO.findAll();
		
		context = FacesContext.getCurrentInstance();
		request = (HttpServletRequest) context.getExternalContext().getRequest();
		session = request.getSession(false);
		this.usuario = (Usuario) session.getAttribute("usuario");
		
		this.factura = new Factura();
		this.factura.setInicioServicios(new Date());
		Date fechaInicioDefault = new Date();
		Date fechaFinDefault = new Date();
		DateUtil.setTime(fechaInicioDefault, 0, 0, 0, 0);
		DateUtil.setTime(fechaFinDefault, 0, 0, 0, 0);
		
		this.factura.setInicioServicios(fechaInicioDefault);
		this.factura.setFinServicios(fechaFinDefault);
		
		this.emisores = emisorDAO.buscarTodos(true);
		this.alUnidades = unidadManejoDAO.buscarTodos();
		this.tipoCobroPrecioFijo = tipoCobroDAO.buscarPorId(1);//TIPO COBRO PRECIO FIJO
		
		iva = parametroDAO.buscarPorNombre("IVA");
		
		if(iva == null)
			tasaIva = new BigDecimal("0.00").setScale(2, BigDecimal.ROUND_HALF_UP);
		else
			tasaIva = new BigDecimal(iva.getValor()).setScale(2, BigDecimal.ROUND_HALF_UP);
		
		retencion = parametroDAO.buscarPorNombre("RETENCION");
		
		this.otroServicio = new ServicioFactura();
	}
	
	public void filtraSeries() {
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		String titulo = "Cambio de emisor";
		
		try {
			if(this.emisor == null)
				throw new InventarioException("Debe seleccionar un emisor");
			log.info("Emisor: {}, Serie: {}", this.emisor.getNb_emisor(), this.listaSerieFactura);
			this.listaSerieFactura = serieFacturaDAO.buscarPorEmisor(this.emisor, true);
			
			mensaje = "Debe seleccionar una serie";
			severity = FacesMessage.SEVERITY_INFO;
		} catch (InventarioException ex) {
			mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_WARN;
		} catch (Exception ex) {
			log.error("Problema para cargar la información del emisor y sus series...", ex);
			mensaje = "Hay un problema para cargar la información del emisor y sus series de facturación.";
			severity = FacesMessage.SEVERITY_ERROR;
		} finally {
			message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
			PrimeFaces.current().ajax().update(":form:messages");	
		}
	}
	
	public void serieFactura() {
		if(this.plantaSelect == null)
			return;
		this.emisor = this.plantaSelect.getIdEmisoresCFDIS();
		this.listaSerieFactura = this.serieFacturaDAO.buscarPorEmisor(this.emisor, true);
		this.serieFacturaSelect = this.plantaSelect.getSerieFacturaDefault();
	}
	
	public void cargaInfoCliente() {
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		
		try {
			
			this.clienteSelect = clienteDAO.buscarPorId(clienteSelect.getCteCve(), true);
			
			Map<Integer, List<PrecioServicio>> mpPrecioServicio = new HashMap<Integer, List<PrecioServicio>>();
			List<PrecioServicio> precioServicioList = null;
			listaClienteDomicilio.clear();
			listaClienteDomicilio = clienteDomicilioDAO.buscarPorCliente(clienteSelect.getCteCve());
			if(this.clienteSelect.getMetodoPago() == null)
				throw new InventarioException("El cliente no tiene un método de pago configurado.");
			this.metodoPagoSelect = this.clienteSelect.getMetodoPago();
			
			if (listaClienteDomicilio.size() > 0) {
				domicilioSelect = listaClienteDomicilio.get(0).getDomicilios();
			}
			
			precioServicioList = psDAO.buscarPorCliente(clienteSelect.getCteCve(), true);
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
				log.debug("Servicio: {} - Unidad: {}", ps.getServicio().getServicioDs(), ps.getUnidad().getUnidadDeManejoDs());
			}
			
			AvisoCliente();
			setMedioPago();
			setMetodoPago();
			
			mensaje = "Agregue sus servicios.";
			severity = FacesMessage.SEVERITY_INFO;
			
		} catch (InventarioException ex) {
			mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_ERROR;
		} catch (Exception ex) {
			mensaje = "Ha ocurrido un error en el sistema. Intente nuevamente.\nSi el problema persiste, por favor comuniquese con su administrador del sistema.";
			severity = FacesMessage.SEVERITY_ERROR;
		} finally {
			message = new FacesMessage(severity, "Catálogo de clientes", mensaje);
	        FacesContext.getCurrentInstance().addMessage(null, message);
	        PrimeFaces.current().ajax().update("receptor", "factura", "servicioSelect", "metodoPago", "medioPago");
		}
	}
	
	public void AvisoCliente() {
		listaAviso.clear();
		listaAviso = avisoDAO.buscarPorCliente(clienteSelect.getCteCve());
		Aviso aviso;
		if (listaAviso.size() > 1) {
			Optional<Aviso> numeroMax = listaAviso.stream().max(Comparator.comparing(Aviso::getAvisoPlazo));
			aviso = numeroMax.get();
			this.plazoSelect = aviso.getAvisoPlazo();
		} else {
			if (listaAviso.size() == 1) {
				aviso = listaAviso.get(0);
				this.plazoSelect = aviso.getAvisoPlazo();
			} else {
				System.out.println("el cliente no tiene aviso");
			}
		}
	}
	
	public void agregarServicio() {
		String message = null;
		Severity severity = null;
		ServicioFactura servicio = null;
		Concepto concepto = null;
		ClaveUnidad claveUnidad = null;
		
		try {
			if (this.cantidadServicio == null || this.cantidadServicio.compareTo(BigDecimal.ZERO) <= 0)
				throw new InventarioException("Debe indicar la cantidad de servicios.");
			if (this.precioServicio == null)
				throw new InventarioException("Debe seleccionar un servicio.");
			if (alServiciosDetalle == null)
				alServiciosDetalle = new ArrayList<ServicioFactura>();
			
			concepto = conceptoDAO.buscarPorId(precioServicio.getServicio().getServicioCod());
			claveUnidad = cveUnidadDAO.buscarPorId(precioServicio.getServicio().getCdUnidad());

			servicio = new ServicioFactura();
			servicio.setId(this.idServicioFacturaTmp++);
			servicio.setDescripcion(precioServicio.getServicio().getServicioDs());
			servicio.setCantidad(cantidadServicio);
			servicio.setUnidad(precioServicio.getUnidad().getUnidadDeManejoDs());
			servicio.setTarifa(precioServicio.getPrecio().setScale(2, BigDecimal.ROUND_HALF_UP));
			servicio.setCosto(cantidadServicio.multiply(precioServicio.getPrecio().setScale(2, BigDecimal.ROUND_HALF_UP)));
			servicio.setTipoCobro(precioServicio.getServicio().getCobro());
			servicio.setUdCobro("Servicio");
			servicio.setCodigo(concepto);
			servicio.setCdUnidad(claveUnidad);
			subtotal = BigDecimal.ZERO;
			int coincidencias = 0, diferentes = 0;
			if (alServiciosDetalle.size() == 0) {
				alServiciosDetalle.add(servicio);
			} else {
				for (ServicioFactura srv : alServiciosDetalle) {
					if (srv.getDescripcion() == servicio.getDescripcion()) {
						// alServiciosDetalle.add(servicio);+
						coincidencias++;
					} else {
						diferentes++;
					}
				}

				if (coincidencias == 1) {
					throw new InventarioException("El servicio ya se encuentra registrado en su factura.");
				} else if (diferentes > 0) {
					alServiciosDetalle.add(servicio);
				}
			}
			for (ServicioFactura sf : alServiciosDetalle) {
				subtotal = subtotal.add(sf.getCosto().setScale(2, BigDecimal.ROUND_HALF_UP));
			}
			bdIva = subtotal.multiply(tasaIva).setScale(2, BigDecimal.ROUND_HALF_UP);
			total = bdIva.add(subtotal).setScale(2, BigDecimal.ROUND_HALF_UP);
			FormatUtil format = new FormatUtil();
			montoLetra = format.getMontoConLetra(total.floatValue());
			this.cantidadServicio = new BigDecimal("0.00").setScale(2, BigDecimal.ROUND_HALF_UP);
			message = "Servicio(s) agregado(s) correctamente.";
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
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, "Servicio", message));
			PrimeFaces.current().ajax().update("form:messages", "form:dt-facturacionServicios", "form:montoLetra", "form:txtCantidadSrv");
		}
	}
	
	public void crearOtroServicio() {
		this.otroServicio = new ServicioFactura();
		this.otroServicio.setUdCobro("Servicio");
		this.otroServicio.setTipoCobro(this.tipoCobroPrecioFijo);
		this.concepto = new Concepto();
		this.claveUnidad = new ClaveUnidad();
		log.info("Creando un nuevo servicio fuera de catálogo...");
		PrimeFaces.current().ajax().update("dg-otro-servicio");
	}
	
	public List<Concepto> buscaConcepto(String query) {
		List<Concepto> conceptos = null;
		try {
			conceptos = conceptoDAO.buscarPorClaveNombre(query, query);
			log.info("Cantidad de conceptos consultados: {}", conceptos.size());
		} catch(Exception ex) {
			log.error("Problema para buscar en el catálogo de conceptos...", ex);
		}
		return conceptos;
	}
	
	public List<ClaveUnidad> buscaUnidad(String query) {
		List<ClaveUnidad> unidadesSAT = null;
		try {
			unidadesSAT = cveUnidadDAO.buscarPorClaveNombre(query, query);
			log.info("Cantidad de unidades SAT consultados: {}", unidadesSAT.size());
		} catch(Exception ex) {
			log.error("Problema para buscar en el catálogo de claves de unidad SAT...", ex);
		}
		return unidadesSAT;
	}
	
	public void subtotalOtroServicio() {
		BigDecimal subtotal = null;
		try {
			if(this.otroServicio.getCantidad() == null)
				throw new InventarioException("No se ha indicado una cantidad.");
			
			if(this.otroServicio.getTarifa() == null)
				throw new InventarioException("No se ha indicado un precio unitario.");
			
			subtotal = this.otroServicio.getCantidad()
					.multiply(this.otroServicio.getTarifa())
					.setScale(2, BigDecimal.ROUND_HALF_UP)
					;
			
			this.otroServicio.setCosto(subtotal);
		} catch(Exception ex) {
			log.warn("Problema para obtener el subtotal del servicio...", ex.getMessage());
			this.otroServicio.setCosto(BigDecimal.ZERO);
		}
	}
	
	public void agregaOtroServicio() {
		String message = null;
		Severity severity = null;
		
		int coincidencias = 0, diferentes = 0;
		
		try {
			if(this.otroServicio.getCantidad() == null || this.otroServicio.getCantidad().compareTo(BigDecimal.ZERO) <= 0)
				throw new InventarioException("Debe indicar la cantidad de servicios");
			
			if(this.otroServicio.getTarifa() == null || this.otroServicio.getTarifa().compareTo(BigDecimal.ZERO) <= 0)
				throw new InventarioException("Debe indicar un precio unitario.");
			
			if(this.otroServicio.getCosto() == null || this.otroServicio.getCosto().compareTo(BigDecimal.ZERO) <= 0)
				throw new InventarioException("El subtotal es incorrecto.");
			
			if(this.concepto == null)
				throw new InventarioException("Debe indicar un concepto del catálogo del SAT.");
			
			if(this.claveUnidad == null)
				throw new InventarioException("Debe indicar una clave de unidad del SAT.");
			
			this.otroServicio.setCodigo(this.concepto);
			this.otroServicio.setCdUnidad(this.claveUnidad);
			this.otroServicio.setTipoCobro(tipoCobroPrecioFijo);
			
			if (alServiciosDetalle == null)
				alServiciosDetalle = new ArrayList<ServicioFactura>();
			
			this.otroServicio.setId(this.idServicioFacturaTmp++);
			subtotal = BigDecimal.ZERO;
			
			if (alServiciosDetalle.size() == 0) {
				alServiciosDetalle.add(this.otroServicio);
			} else {
				for (ServicioFactura srv : alServiciosDetalle) {
					if (srv.getDescripcion() == this.otroServicio.getDescripcion()) {
						coincidencias++;
					} else {
						diferentes++;
					}
				}

				if (coincidencias == 1) {
					throw new InventarioException("El servicio ya se encuentra registrado en su factura.");
				} else if (diferentes > 0) {
					alServiciosDetalle.add(this.otroServicio);
				}
			}
			for (ServicioFactura sf : alServiciosDetalle) {
				subtotal = subtotal.add(sf.getCosto().setScale(2, BigDecimal.ROUND_HALF_UP));
			}
			bdIva = subtotal.multiply(tasaIva).setScale(2, BigDecimal.ROUND_HALF_UP);
			total = bdIva.add(subtotal).setScale(2, BigDecimal.ROUND_HALF_UP);
			FormatUtil format = new FormatUtil();
			montoLetra = format.getMontoConLetra(total.floatValue());
			this.cantidadServicio = new BigDecimal("0.00").setScale(2, BigDecimal.ROUND_HALF_UP);
			
			PrimeFaces.current().executeScript("PF('dgOtroServicio').hide()");
			
			message = "Servicio agregado correctamente.";
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
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, "Servicio", message));
			PrimeFaces.current().ajax().update("form:messages", "form:dt-facturacionServicios", "form:montoLetra", "form:txtCantidadSrv");
		}
	}

	public synchronized void saveFactura() {
		String message = null;
		Severity severity = null;
		Cliente cliente = null;
		List<Factura> buscaFacturasList = null;
		List<SerieFactura> listaSerieFacturaBkp = null;
				
		try {
			listaSerieFacturaBkp = new ArrayList<SerieFactura>();
			listaSerieFacturaBkp.addAll(listaSerieFactura);
			
			listaSerieFacturaBkp = serieFacturaDAO.buscarPorEmisor(this.emisor, true);
			
			if(factura.getId() != null)
				throw new InventarioException("La factura ya se encuentra registrada.");
			
			if (this.alServiciosDetalle == null || this.alServiciosDetalle.size() == 0)
				throw new InventarioException("Debe indicar al menos un servicio");
			
			if(BigDecimal.ZERO.compareTo(total) == 0) {
				throw new InventarioException("El total de la factura no puede ser cero.");
			}
			
			// Datos receptor
			cliente = clienteDAO.buscarPorId(clienteSelect.getCteCve(), true);
			
			buscaFacturasList = facturaDAO.buscarActivasPorSerieNumero(serieFacturaSelect.getNomSerie(), String.valueOf(serieFacturaSelect.getNumeroActual() + 1));
			
			if(buscaFacturasList.size() > 0)
				throw new InventarioException("La factura ya se encuentra registrada.");
			
			int serie = (serieFacturaSelect.getNumeroActual()+1);
			serieFacturaSelect.setNumeroActual(serie);
			
			List<Factura> alFacturas = new ArrayList<>();
			factura.setId(idFactura);
			alFacturas.add(factura);
			cliente.setFacturaList(alFacturas);
			factura.setCliente(cliente);
			factura.setNumero(String.valueOf(serieFacturaSelect.getNumeroActual()));
			factura.setMoneda(this.moneda);
			factura.setRfc(cliente.getCteRfc());
			factura.setNombreCliente(cliente.getNombre());
			factura.setFecha(this.fechaFactura);
			factura.setObservacion(this.Obervaciones == null ? "" : this.Obervaciones);
			factura.setSubtotal(subtotal);
			factura.setIva(bdIva);
			factura.setTotal(total);
			domicilioSelect = listaClienteDomicilio.get(0).getDomicilios();
			factura.setPais(domicilioSelect.getPaisCved().getPaisDesc());
			factura.setEstado(domicilioSelect.getCiudades().getMunicipios().getEstados().getEstadoDesc());
			factura.setMunicipio(domicilioSelect.getCiudades().getMunicipios().getMunicipioDs());
			factura.setCiudad(domicilioSelect.getCiudades().getCiudadDs());
			AsentamientoHumano asentamiento = asnDAO.buscarPorAsentamiento(domicilioSelect.getPaisCved().getPaisCve(),
					domicilioSelect.getCiudades().getMunicipios().getEstados().getEstadosPK().getEstadoCve(),
					domicilioSelect.getCiudades().getMunicipios().getMunicipiosPK().getMunicipioCve(),
					domicilioSelect.getCiudades().getCiudadesPK().getCiudadCve(),
					domicilioSelect.getDomicilioColonia());
			factura.setColonia(asentamiento.getAsentamientoDs());
			factura.setCp(domicilioSelect.getDomicilioCp());
			factura.setCalle(domicilioSelect.getDomicilioCalle());
			factura.setNumExt(domicilioSelect.getDomicilioNumExt());
			factura.setNumInt(domicilioSelect.getDomicilioNumInt());
			factura.setTelefono(domicilioSelect.getDomicilioTel1());
			factura.setFax(domicilioSelect.getDomicilioFax());
			factura.setPorcentajeIva(tasaIva.multiply(new BigDecimal(100).setScale(2, BigDecimal.ROUND_HALF_UP)));
			factura.setNumeroCliente(cliente.getNumeroCte());
			factura.setValorDeclarado(BigDecimal.ZERO);
			factura.setMontoLetra(FormatUtil.numeroPalabras(total.doubleValue()));
			factura.setFacturaMedioPagoList(new ArrayList<FacturaMedioPago>());
			FacturaMedioPagoPK facturaPK = new FacturaMedioPagoPK();
			facturaPK.setFacturaId(factura);
			facturaPK.setFmpId(0);
			FacturaMedioPago fmp = new FacturaMedioPago();
			fmp.setFacturaMedioPagoPK(facturaPK);
			MedioPago medioP = medioPagoDAO.buscarPorFormaPago(medioPagoSelect);
			fmp.setMpId(medioP);
			fmp.setFactura(factura);
			fmp.setFmpPorcentaje(100);
			fmp.setMpDescripcion(medioP.getMpDescripcion());
			fmp.setFmpReferencia(referencia);
			factura.getFacturaMedioPagoList().add(fmp);
			StatusFactura status = statusDAO.buscarPorId(1);
			factura.setStatus(status);
			TipoFacturacion tipo = tipoDAO.buscarPorId(1);
			factura.setTipoFacturacion(tipo);
			// datos Emisor
			factura.setPlanta(plantaSelect);// sucursal
			factura.setPlazo(this.plazoSelect);
			factura.setRetencion(BigDecimal.ZERO);
			factura.setNomSerie(serieFacturaSelect.getNomSerie());
			MetodoPago metodoP = this.metodoPagoSelect;
			factura.setMetodoPago(metodoP.getCdMetodoPago());
			factura.setTipoPersona(cliente.getTipoPersona());
			factura.setCdRegimen(cliente.getRegimenFiscal().getCd_regimen());
			factura.setCdUsoCfdi(cliente.getUsoCfdi().getCdUsoCfdi());
			factura.setUuid(null);
			factura.setEmisorNombre(this.emisor.getNb_emisor());
			factura.setEmisorRFC(this.emisor.getNb_rfc());
			factura.setEmisorCdRegimen(this.emisor.getCd_regimen().getCd_regimen());
			factura.setLugarExpedicion(this.emisor.getCodigoPostal());
			factura.setServicioFacturaList(alServiciosDetalle);
			
			for (ServicioFactura sef : alServiciosDetalle) {
				sef.setId(null);
				sef.setFactura(factura);
			}
			
			facturaDAO.save(factura);
			
			serieFacturaDAO.actualizar(serieFacturaSelect);
			
			listaSerieFactura.clear();
			listaSerieFactura.add(serieFacturaSelect);
			serieFacturaSelect = listaSerieFactura.get(0);
			
			listaSerieFactura = listaSerieFacturaBkp;
			
			severity = FacesMessage.SEVERITY_INFO;
			message = String.format("La factura %s-%s se guardo correctamente", this.factura.getNomSerie(), this.factura.getNumero());
		} catch (InventarioException ex) {
			log.error("Problema para guardar la factura...", ex);
			message = ex.getMessage();
			severity = FacesMessage.SEVERITY_WARN;
		} catch (Exception ex) {
			log.error("Problema para obtener los servicios del cliente.", ex);
			message = "Existe un problema al guardar la factura.";
			severity = FacesMessage.SEVERITY_ERROR;
		} finally {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, "Factura" , message));
			PrimeFaces.current().ajax().update("form:messages", "form:dt-facturacionServicios", "form:serieFactura");
		}
	}

	public void deleteServicio(ServicioFactura servicio) {
		this.alServiciosDetalle.remove(servicio);
		subtotal = BigDecimal.ZERO;
		for (ServicioFactura sf : alServiciosDetalle) {
			subtotal = subtotal.add(sf.getCosto().setScale(2, BigDecimal.ROUND_HALF_UP));
		}
		bdIva = subtotal.multiply(tasaIva).setScale(2, BigDecimal.ROUND_HALF_UP);
		total = bdIva.add(subtotal).setScale(2, BigDecimal.ROUND_HALF_UP);
		montoLetra = FormatUtil.numeroPalabras(total.doubleValue());
		PrimeFaces.current().ajax().update("form:subtotal", "form:iva", "form:total", "form:montoLetra");
	}

	public void jasper() throws JRException, IOException, SQLException {
		String jasperPath = "/jasper/Factura.jrxml";
		String filename = String.format("Factura_Folio_%s-%s.pdf", this.factura.getNomSerie(), this.factura.getNumero());
		String images = "/images/logo.jpeg";
		String message = null;
		Severity severity = null;
		File reportFile = new File(jasperPath);
		File imgfile = null;
		JasperReportUtil jasperReportUtil = new JasperReportUtil();
		Map<String, Object> parameters = new HashMap<String, Object>();
		Connection connection = null;
		parameters = new HashMap<String, Object>();
		try {
			if (this.alServiciosDetalle == null || this.alServiciosDetalle.size() == 0)
				throw new InventarioException("Debe seleccionar almenos un servicio");
			if (this.clienteSelect == null)
				throw new InventarioException("Debe seleccionar el cliente");

			URL resource = getClass().getResource(jasperPath);
			URL resourceimg = getClass().getResource(images);
			String file = resource.getFile();
			String img = resourceimg.getFile();
			reportFile = new File(file);
			imgfile = new File(img);
			log.debug("Ruta del reporte: {}", reportFile.getPath());
			Integer num = factura.getId();
			connection = EntityManagerUtil.getConnection();
			parameters.put("REPORT_CONNECTION", connection);
			parameters.put("idFactura", num);
			parameters.put("imagen", imgfile.getPath());
			log.debug("Parametros: {}", parameters.toString());
			jasperReportUtil.createPdf(filename, parameters, reportFile.getPath());
		} catch (Exception ex) {
			log.error("Problema general...", ex);
			message = String.format("No se pudo imprimir el folio %s", this.selServicio);
			severity = FacesMessage.SEVERITY_INFO;
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(severity, "Error en impresion", message));
			PrimeFaces.current().ajax().update("form:messages", "form:dt-facturacionServicios");
		} finally {
			conexion.close((Connection) connection);
		}

	}

	public void reload() throws IOException {
		ExternalContext ec = null;
	    try {
	    	ec = FacesContext.getCurrentInstance().getExternalContext();
			ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
		} catch (IOException e) {
			log.error("Problema para crear una nueva factura...",  e);
		}
	}

	public void timbrado() throws InventarioException {
		String message = null;
		Severity severity = null;
		FacturamaBL facturamaBO = new FacturamaBL(factura.getId(), this.usuario);
		
		try {
			log.info("Timbrando factura: {}...", factura);
			facturamaBO.timbrar();
			facturamaBO.sendMail();
			log.info("Timbrado completado correctamente.");
			severity = FacesMessage.SEVERITY_INFO;
			message = "El timbrado se genero correctamente";
		} catch (FacturamaException e) {
			message = String.format("Mensaje de Facturama: %s", e.getMessage());
			severity = FacesMessage.SEVERITY_ERROR;
		}catch (Exception ex) {
			log.error("Problema para obtener los servicios del cliente.", ex);
			message = "Problema con la información de servicios.";
			severity = FacesMessage.SEVERITY_ERROR;
		} finally {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, "Timbrado CFDI", message));
			PrimeFaces.current().ajax().update("form:messages", "form:dt-facturacionServicios");
		}
		
	}

	public Cliente getClienteSelect() {
		return clienteSelect;
	}

	public void setClienteSelect(Cliente clienteSelect) {
		this.clienteSelect = clienteSelect;
	}

	public Domicilios getDomicilioSelect() {
		return domicilioSelect;
	}

	public void setDomicilioSelect(Domicilios domicilioSelect) {
		this.domicilioSelect = domicilioSelect;
	}

	public Planta getPlantaSelect() {
		return plantaSelect;
	}

	public void setPlantaSelect(Planta plantaSelect) {
		this.plantaSelect = plantaSelect;
	}

	public MetodoPago getMetodoPagoSelect() {
		return this.metodoPagoSelect;
	}
	
	public void setMetodoPagoSelect(MetodoPago metodoPago) {
		this.metodoPagoSelect = metodoPago;
	}

	public String getMedioPagoSelect() {
		return medioPagoSelect;
	}

	public void setMedioPagoSelect(String medioPagoSelect) {
		this.medioPagoSelect = medioPagoSelect;
	}

	public Parametro getIva() {
		return iva;
	}

	public void setIva(Parametro iva) {
		this.iva = iva;
	}

	public Parametro getRetencion() {
		return retencion;
	}

	public void setRetencion(Parametro retencion) {
		this.retencion = retencion;
	}

	public Date getFechaFactura() {
		return fechaFactura;
	}

	public void setFechaFactura(Date fechaFactura) {
		this.fechaFactura = fechaFactura;
	}

	public Date getFechaCorte() {
		return fechaCorte;
	}

	public void setFechaCorte(Date fechaCorte) {
		this.fechaCorte = fechaCorte;
	}

	public String getMoneda() {
		return moneda;
	}

	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}

	public int getPlazoSelect() {
		return plazoSelect;
	}

	public void setPlazoSelect(int plazoSelect) {
		this.plazoSelect = plazoSelect;
	}

	public List<Cliente> getListaCliente() {
		return clientes;
	}

	public void setListaCliente(List<Cliente> listaCliente) {
		this.clientes = listaCliente;
	}

	public List<ClienteDomicilios> getListaClienteDom() {
		return listaClienteDom;
	}

	public void setListaClienteDom(List<ClienteDomicilios> listaClienteDom) {
		this.listaClienteDom = listaClienteDom;
	}

	public List<ClienteDomicilios> getListaClienteDomicilio() {
		return listaClienteDomicilio;
	}

	public void setListaClienteDomicilio(List<ClienteDomicilios> listaClienteDomicilio) {
		this.listaClienteDomicilio = listaClienteDomicilio;
	}

	public List<Planta> getListaPlanta() {
		return listaPlanta;
	}

	public void setListaPlanta(List<Planta> listaPlanta) {
		this.listaPlanta = listaPlanta;
	}

	public List<Aviso> getListaA() {
		return listaA;
	}

	public void setListaA(List<Aviso> listaA) {
		this.listaA = listaA;
	}

	public List<Aviso> getListaAviso() {
		return listaAviso;
	}

	public void setListaAviso(List<Aviso> listaAviso) {
		this.listaAviso = listaAviso;
	}

	public List<MetodoPago> getListaMetodoPago() {
		return listaMetodoPago;
	}

	public void setListaMetodoPago(List<MetodoPago> listaMetodoPago) {
		this.listaMetodoPago = listaMetodoPago;
	}

	public List<MedioPago> getListaMedioPago() {
		return listaMedioPago;
	}

	public void setListaMedioPago(List<MedioPago> listaMedioPago) {
		this.listaMedioPago = listaMedioPago;
	}

	public List<PrecioServicio> getAlServicios() {
		return alServicios;
	}

	public void setAlServicios(List<PrecioServicio> alServicios) {
		this.alServicios = alServicios;
	}

	public List<Cliente> getClientes() {
		return clientes;
	}

	public void setClientes(List<Cliente> clientes) {
		this.clientes = clientes;
	}

	public List<PartidaServicio> getAlPartidas() {
		return alPartidas;
	}

	public void setAlPartidas(List<PartidaServicio> alPartidas) {
		this.alPartidas = alPartidas;
	}

	public List<UnidadDeManejo> getAlUnidades() {
		return alUnidades;
	}

	public void setAlUnidades(List<UnidadDeManejo> alUnidades) {
		this.alUnidades = alUnidades;
	}

	public List<ConstanciaDeDeposito> getListaEntradas() {
		return listaEntradas;
	}

	public void setListaEntradas(List<ConstanciaDeDeposito> listaEntradas) {
		this.listaEntradas = listaEntradas;
	}

	public List<ConstanciaFactura> getListaVigencias() {
		return listaVigencias;
	}

	public void setListaVigencias(List<ConstanciaFactura> listaVigencias) {
		this.listaVigencias = listaVigencias;
	}

	public List<ConstanciaFacturaDs> getListaServicios() {
		return listaServicios;
	}

	public void setListaServicios(List<ConstanciaFacturaDs> listaServicios) {
		this.listaServicios = listaServicios;
	}

	public List<SerieFactura> getListaSerieF() {
		return listaSerieF;
	}

	public void setListaSerieF(List<SerieFactura> listaSerieF) {
		this.listaSerieF = listaSerieF;
	}

	public List<SerieFactura> getListaSerieFactura() {
		return listaSerieFactura;
	}

	public void setListaSerieFactura(List<SerieFactura> listaSerieFactura) {
		this.listaSerieFactura = listaSerieFactura;
	}

	public SerieFactura getSerieFacturaSelect() {
		return serieFacturaSelect;
	}

	public void setSerieFacturaSelect(SerieFactura serieFacturaSelect) {
		this.serieFacturaSelect = serieFacturaSelect;
	}

	public Integer getIdPrecioServicio() {
		return idPrecioServicio;
	}

	public void setIdPrecioServicio(Integer idPrecioServicio) {
		this.idPrecioServicio = idPrecioServicio;
	}

	public List<ServicioFactura> getAlServiciosDetalle() {
		return alServiciosDetalle;
	}

	public void setAlServiciosDetalle(List<ServicioFactura> alServiciosDetalle) {
		this.alServiciosDetalle = alServiciosDetalle;
	}

	public ServicioFactura getSelServicio() {
		return selServicio;
	}

	public void setSelServicio(ServicioFactura selServicio) {
		this.selServicio = selServicio;
	}

	public BigDecimal getCantidadServicio() {
		return cantidadServicio;
	}

	public void setCantidadServicio(BigDecimal cantidadServicio) {
		this.cantidadServicio = cantidadServicio;
	}

	public String getObervaciones() {
		return Obervaciones;
	}

	public void setObervaciones(String obervaciones) {
		Obervaciones = obervaciones;
	}

	public BigDecimal getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(BigDecimal subtotal) {
		this.subtotal = subtotal;
	}

	public BigDecimal getBdIva() {
		return bdIva;
	}

	public void setBdIva(BigDecimal bdIva) {
		this.bdIva = bdIva;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public String getMontoLetra() {
		return montoLetra;
	}

	public void setMontoLetra(String montoLetra) {
		this.montoLetra = montoLetra;
	}

	public PrecioServicio getPrecioServicio() {
		return precioServicio;
	}

	public void setPrecioServicio(PrecioServicio precioServicio) {
		this.precioServicio = precioServicio;
	}


	public void setIdFactura(Integer idFactura) {
		this.idFactura = idFactura;
	}


	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public BigDecimal getTasaIva() {
		return tasaIva;
	}

	public void setTasaIva(BigDecimal tasaIva) {
		this.tasaIva = tasaIva;
	}

	public String getReferencia() {
		return referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	public Factura getFactura() {
		return factura;
	}

	public void setFactura(Factura factura) {
		this.factura = factura;
	}

	public List<FacturaMedioPago> getListaFacturaMedioPago() {
		return listaFacturaMedioPago;
	}

	public void setListaFacturaMedioPago(List<FacturaMedioPago> listaFacturaMedioPago) {
		this.listaFacturaMedioPago = listaFacturaMedioPago;
	}

	public EmisoresCFDIS getEmisor() {
		return emisor;
	}

	public void setEmisor(EmisoresCFDIS emisor) {
		this.emisor = emisor;
	}

	public List<EmisoresCFDIS> getEmisores() {
		return emisores;
	}

	public void setEmisores(List<EmisoresCFDIS> emisores) {
		this.emisores = emisores;
	}
	
	public void setMedioPago() {
		medioPagoSelect =  clienteSelect.getFormaPago();
	}
		
	public void setMetodoPago() {
		this.metodoPagoSelect = clienteSelect.getMetodoPago();
	}

	public ServicioFactura getOtroServicio() {
		return otroServicio;
	}

	public void setOtroServicio(ServicioFactura otroServicio) {
		this.otroServicio = otroServicio;
	}

	public Concepto getConcepto() {
		return concepto;
	}

	public void setConcepto(Concepto concepto) {
		this.concepto = concepto;
	}

	public ClaveUnidad getClaveUnidad() {
		return claveUnidad;
	}

	public void setClaveUnidad(ClaveUnidad claveUnidad) {
		this.claveUnidad = claveUnidad;
	}
}
