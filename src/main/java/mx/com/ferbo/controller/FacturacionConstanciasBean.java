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
import java.util.stream.Collectors;

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
import mx.com.ferbo.dao.ClienteDAO;
import mx.com.ferbo.dao.ClienteDomiciliosDAO;
import mx.com.ferbo.dao.EmisoresCFDISDAO;
import mx.com.ferbo.dao.FacturaDAO;
import mx.com.ferbo.dao.FacturacionDepositosDAO;
import mx.com.ferbo.dao.FacturacionServiciosDAO;
import mx.com.ferbo.dao.FacturacionVigenciasDAO;
import mx.com.ferbo.dao.MedioPagoDAO;
import mx.com.ferbo.dao.MetodoPagoDAO;
import mx.com.ferbo.dao.ParametroDAO;
import mx.com.ferbo.dao.PlantaDAO;
import mx.com.ferbo.dao.PrecioServicioDAO;
import mx.com.ferbo.dao.SerieFacturaDAO;
import mx.com.ferbo.dao.StatusFacturaDAO;
import mx.com.ferbo.dao.TipoFacturacionDAO;
import mx.com.ferbo.model.AsentamientoHumano;
import mx.com.ferbo.model.Aviso;
import mx.com.ferbo.model.Camara;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.ClienteDomicilios;
import mx.com.ferbo.model.ConstanciaDeDeposito;
import mx.com.ferbo.model.ConstanciaDepositoDetalle;
import mx.com.ferbo.model.ConstanciaFactura;
import mx.com.ferbo.model.ConstanciaFacturaDs;
import mx.com.ferbo.model.ConstanciaServicioDetalle;
import mx.com.ferbo.model.Domicilios;
import mx.com.ferbo.model.EmisoresCFDIS;
import mx.com.ferbo.model.Factura;
import mx.com.ferbo.model.FacturaMedioPago;
import mx.com.ferbo.model.FacturaMedioPagoPK;
import mx.com.ferbo.model.MedioPago;
import mx.com.ferbo.model.MetodoPago;
import mx.com.ferbo.model.Parametro;
import mx.com.ferbo.model.Partida;
import mx.com.ferbo.model.PartidaServicio;
import mx.com.ferbo.model.Planta;
import mx.com.ferbo.model.PrecioServicio;
import mx.com.ferbo.model.ProductoConstanciaDs;
import mx.com.ferbo.model.SerieFactura;
import mx.com.ferbo.model.Servicio;
import mx.com.ferbo.model.ServicioConstancia;
import mx.com.ferbo.model.ServicioConstanciaDs;
import mx.com.ferbo.model.StatusFactura;
import mx.com.ferbo.model.TipoCobro;
import mx.com.ferbo.model.TipoFacturacion;
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
public class FacturacionConstanciasBean implements Serializable{
	
	private static final long serialVersionUID = -1785488265380235016L;
	
	private static Logger log = LogManager.getLogger(FacturacionConstanciasBean.class);
	
	private Cliente clienteSelect;
	private Domicilios domicilioSelect;
	private Planta plantaSelect;
	private SerieFactura serieFacturaSelect;
	private MetodoPago metodoPagoSelect;
	private MetodoPago MPagoSelect;
	private MedioPago medioPagoSelect;
	private MedioPago formaPagoCliente;
	private Parametro iva,retencion;
	private Factura factura;
	private ServicioConstancia selectServicioE;
	private ServicioConstancia selectServicioV;
	private ConstanciaFactura entradaSelect;
	private ConstanciaFactura vigenciaSelect;
	private ConstanciaFacturaDs servicioConstanciaSelect;
	private ServicioConstanciaDs selectServicioDs;
	private Usuario usuario;
	private EmisoresCFDIS emisor;
	private List<EmisoresCFDIS> emisores;
	
	private ClienteDAO clienteDAO;
	private ClienteDomiciliosDAO clienteDomicilioDAO;
	private PlantaDAO plantaDAO;
	private SerieFacturaDAO serieFacturaDAO;
	private AvisoDAO avisoDAO;
	private MetodoPagoDAO metodoPagoDAO;
	private MedioPagoDAO medioPagoDAO;	
	private ParametroDAO parametroDAO;
	private FacturacionDepositosDAO facturacionConstanciasDAO;
	private FacturacionVigenciasDAO facturacionVigenciasDAO;
	private FacturacionServiciosDAO facturacionServiciosDAO;
	private StatusFacturaDAO statusFacturaDAO;
	private TipoFacturacionDAO tipoFacturacionDAO;
	private PrecioServicioDAO precioServicioDAO;
	private FacturaDAO facturaDAO;
	private EmisoresCFDISDAO emisorDAO;
	
	private List<Cliente> listaCliente;
	private List<ClienteDomicilios> listaClienteDomicilio;
	private List<Planta> listaPlanta;
	private List<SerieFactura> listaSerieFactura;
	private List<Aviso> listaAviso;
	private List<MetodoPago> listaMetodoPago;
	private List<MedioPago> listaMedioPago;
	private List<ConstanciaFactura> listaEntradas;
	private List<ConstanciaFactura> listaVigencias;
	private List<ConstanciaFacturaDs> listaServicios;
	
	private List<ConstanciaFactura> selectedEntradas;
	private List<ConstanciaFactura> selectedVigencias;
	private List<ConstanciaFacturaDs> selectedServicios;
	
	private Date fechaFactura;
	private Date fechaCorte;
	private BigDecimal cantidad;
	private String moneda;
	private String referencia;
	private int plazoSelect;
	private BigDecimal resIva;
	private BigDecimal resRetencion, valorDeclarado;
	private BigDecimal subTotalEntrada;
	private BigDecimal subTotalVigencias;
	private BigDecimal subTotalServicios;
	private BigDecimal subTotalGeneral;
	private BigDecimal ivaTotal;
	private BigDecimal total;
	private String montoLetra;
	private FacesContext context;
	private HttpSession session;
	private String observaciones;
	
	
    private FacesContext faceContext;
    private HttpServletRequest request;
    
	
	public FacturacionConstanciasBean() {
		clienteDAO = new ClienteDAO();
		clienteDomicilioDAO = new ClienteDomiciliosDAO();
		plantaDAO = new PlantaDAO();
		serieFacturaDAO = new SerieFacturaDAO();
		avisoDAO = new AvisoDAO();
		metodoPagoDAO = new MetodoPagoDAO();
		medioPagoDAO = new MedioPagoDAO();
		parametroDAO = new ParametroDAO();
		facturacionConstanciasDAO = new FacturacionDepositosDAO();
		facturacionVigenciasDAO = new FacturacionVigenciasDAO();
		facturacionServiciosDAO = new FacturacionServiciosDAO();
		statusFacturaDAO = new StatusFacturaDAO();
		tipoFacturacionDAO = new TipoFacturacionDAO();
		precioServicioDAO = new PrecioServicioDAO();
		facturaDAO = new FacturaDAO();
		emisorDAO = new EmisoresCFDISDAO();
		
		listaCliente = new ArrayList<>();
		listaClienteDomicilio = new ArrayList<>();
		listaPlanta = new ArrayList<>();
		listaSerieFactura = new ArrayList<>();
		listaAviso = new ArrayList<>();
		listaMetodoPago = new ArrayList<>();
		listaMedioPago = new ArrayList<>();
		listaEntradas = new ArrayList<>();
		listaVigencias = new ArrayList<>();
		listaServicios = new ArrayList<>();
		
		selectedEntradas = new ArrayList<>();
		selectedVigencias = new ArrayList<>();
		selectedServicios = new ArrayList<>();
		
		valorDeclarado = new BigDecimal(0);
		resRetencion = new BigDecimal(0);
		resIva = new BigDecimal(0);
		moneda = "MXN";
	}
	
	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init(){
		faceContext = FacesContext.getCurrentInstance();
		request = (HttpServletRequest) faceContext.getExternalContext().getRequest();
		usuario = (Usuario) request.getSession(false).getAttribute("usuario");
		
		log.info("El usuario {} ingresa a la facturación de constancias", this.usuario.getUsuario());
		
		subTotalServicios = new BigDecimal(0);
		subTotalEntrada = new BigDecimal(0);
		subTotalVigencias = new BigDecimal(0);
		subTotalGeneral = new BigDecimal(0);
		ivaTotal = new BigDecimal(0);
		total = new BigDecimal(0);
		
        
		listaCliente = (List<Cliente>) request.getSession(false).getAttribute("clientesActivosList");
		log.debug("Lista de clientes cargada.");
		
		listaPlanta = plantaDAO.findall(true);
		log.debug("Lista de plantas cargada.");
		
		log.debug("Lista de series de facturas cargada.");
		
		listaMetodoPago = metodoPagoDAO.buscarTodos();
		log.debug("Lista de Métodos de pago cargada.");
		
		listaMedioPago = medioPagoDAO.buscarVigentes(new Date());
		log.debug("Lista de Formas de pago cargada.");
		
		fechaCorte = new Date();
		fechaFactura = new Date();
		
		this.emisores = emisorDAO.buscarTodos(true);
        
        //variables a inicializar en null al regreso de la pantalla
        selectedEntradas = new ArrayList<>();
		selectedVigencias = new ArrayList<>();
		selectedServicios = new ArrayList<>();
		clienteSelect = new Cliente();
		plantaSelect = new Planta();
		formaPagoCliente = new MedioPago();
		medioPagoSelect = new MedioPago();
		metodoPagoSelect = new MetodoPago();
		MPagoSelect = new MetodoPago();
		if((clienteSelect!=null)&&(plantaSelect!=null)) {
			facturacionEntradas();
			facturacionServicios();
			facturacionVigencias();
		}
		
		PrimeFaces.current().ajax().update("form:dt-constanciasE","form:dt-vigencias","form:dt-servicios","form:medioPago");
	}

	
	
	public void cargaInfoCliente() {
		
		clienteSelect = clienteDAO.buscarPorId(clienteSelect.getCteCve(), false);
		formaPagoCliente = medioPagoDAO.buscarPorFormaPago(clienteSelect.getFormaPago()); 
		MPagoSelect = metodoPagoDAO.buscarPorMetodoPago(clienteSelect.getMetodoPago().getCdMetodoPago());
		iva = parametroDAO.buscarPorNombre("IVA");//
		resIva = new BigDecimal(iva.getValor()); 
		resIva = resIva.multiply(new BigDecimal(100));//necesario ya que manda error al multiplicar desde el fron-end+++
		retencion = parametroDAO.buscarPorNombre("RETENCION");//***
		resRetencion = new BigDecimal(retencion.getValor());
		resRetencion = resRetencion.multiply(new BigDecimal(100));//+++
		//Domicilio
		listaClienteDomicilio.clear();
		listaClienteDomicilio = clienteDomicilioDAO.buscarPorCliente(clienteSelect.getCteCve());
		
		
		if(listaClienteDomicilio.size() > 0) {
			domicilioSelect = listaClienteDomicilio.get(0).getDomicilios();
		}
		
		//llenado de select plazo de pago
		this.avisoCliente();
		
		//carga de constancias si existe un cambio de cliente
		this.cargarConstancias();
		
		//PrimeFaces.current().ajax().update("form:medioPago","form:metodoPago");
	}
	
	
	public void avisoCliente() {
		
		// -------------- comienza recuperado de Aviso ----------------
		
		listaAviso.clear();
		listaAviso = avisoDAO.buscarPorCliente(clienteSelect.getCteCve());
		
		//obtengo de la listaAviso el registro con el plazo maximo
		Aviso aviso;
		if(listaAviso.size()>1) {//debe traer dos registros la lista o mas para poder comparar cul es el maximo
			Optional<Aviso> numeroMax =  listaAviso.stream().max(Comparator.comparing(Aviso::getAvisoPlazo));
			
			aviso = numeroMax.get();
			this.plazoSelect = aviso.getAvisoPlazo();
			
		}else {
			
			if(listaAviso.size()==1) {
				aviso = listaAviso.get(0);
				this.plazoSelect = aviso.getAvisoPlazo();
			}else {
				System.out.println("el cliente no tiene aviso");
			}
		}
				
				// ------------------- termina recuperacion de aviso -----------------------
		
	}
	
	public void serieFactura() {
		
		listaSerieFactura.clear();
		
		if(this.plantaSelect == null)
			return;
		
		this.emisor = this.plantaSelect.getIdEmisoresCFDIS();
		this.listaSerieFactura = this.serieFacturaDAO.buscarPorEmisor(this.emisor, true);
		this.serieFacturaSelect = this.plantaSelect.getSerieFacturaDefault();
		
		log.info("Planta seleccionada: {}", this.plantaSelect);
		log.info("Emisor seleccionado: {}", this.emisor);
		log.info("Serie factura seleccionada: {}", this.serieFacturaSelect);
		
		//Carga de constancias si existe cambio en planta 
		cargarConstancias();
	}
	
	public void filtraSeries() {
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		String titulo = "Cambio de emisor";
		
		try {
			if(this.emisor == null)
				throw new InventarioException("Debe seleccionar un emisor");
			this.listaSerieFactura = serieFacturaDAO.buscarPorEmisor(this.emisor, true);
			
			log.info("Planta seleccionada: {}", this.plantaSelect);
			log.info("Emisor seleccionado: {}", this.emisor);
			log.info("Serie factura seleccionada: {}", this.serieFacturaSelect);
			
			mensaje = "Debe seleccionar una serie";
			severity = FacesMessage.SEVERITY_WARN;
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
	
	public void seleccionaSerie() {
		log.info("Serie seleccionada: {} - {}", this.serieFacturaSelect.getNomSerie(), (this.serieFacturaSelect.getNumeroActual() + 1));
	}

	public void recargaDatos() {
		log.info("Recargando datos...");
	}
	
	public void cargarConstancias() {
		
		if(clienteSelect == null) {
			return;
		}
		
		if(clienteSelect.getCteCve() == null)
			return;
		
		if(plantaSelect == null) {
			return;
		}
		
		if(plantaSelect.getPlantaCve() == null)
			return;
		
		this.facturacionEntradas();
		procesarEntradas();
		this.facturarVigencias();
		this.facturacionServicios();
		procesarServicios();
		
		PrimeFaces.current().ajax().update("form:dt-constanciasE","form:dt-vigencias","form:dt-servicios");
		
	}
	
	public void facturarVigencias() {
		this.facturacionVigencias();
		this.procesarVigencias();
	}
	
	public void calcula(ServicioConstancia sc) {
		String message = null;
		Severity severity = null;
		
		BigDecimal subtotal = null;
		
		try {
			subtotal = sc.getBaseCargo().multiply(sc.getTarifa()).setScale(2, BigDecimal.ROUND_HALF_UP);
			sc.setCosto(subtotal);
			
			log.info("Importe actualizado en servicio constancia: cantidad = {}, costo unitario = {}, subtotal = {}", sc.getBaseCargo(), sc.getTarifa(), sc.getCosto());
		} catch(NullPointerException ex) {
			log.warn("Problema al recalcular los servicios: {}", ex.getMessage());
			message = "NO puede dejar datos vacíos.";
			severity = FacesMessage.SEVERITY_ERROR;
			
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, "Actualizar importe", message));
			PrimeFaces.current().ajax().update("form:messages");
		} catch(Exception ex) {
			log.warn("Problema al recalcular los servicios: {}", ex.getMessage());
			message = "Problema con la información de servicios.";
			severity = FacesMessage.SEVERITY_ERROR;
			
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, "Actualizar importe", message));
			PrimeFaces.current().ajax().update("form:messages");
		} finally {
			this.recalculoEntradas();
			this.recalculoVigencias();
			
			
		}
	}
	
	public void sumaGeneral() {
		
		subTotalGeneral = subTotalEntrada.add(subTotalServicios.add(subTotalVigencias)).setScale(2,BigDecimal.ROUND_HALF_UP);
		
		ivaTotal = subTotalGeneral.multiply(new BigDecimal(iva.getValor())).setScale(2, BigDecimal.ROUND_HALF_UP);
		
		total = subTotalGeneral.add(ivaTotal).setScale(2,BigDecimal.ROUND_HALF_UP);
		
		FormatUtil formato = new FormatUtil();
		
		montoLetra = formato.getMontoConLetra(total.doubleValue());
		
		factura.setSubtotal(subTotalGeneral);
		factura.setIva(ivaTotal);
		factura.setTotal(total);
		factura.setMontoLetra(montoLetra);
		factura.setConstanciaFacturaList(new ArrayList<>());
		factura.setConstanciaFacturaDsList(new ArrayList<>());
		
		factura.getConstanciaFacturaList().addAll(selectedEntradas);
		
		factura.getConstanciaFacturaList().addAll(selectedVigencias);
		
		factura.setConstanciaFacturaDsList(selectedServicios);
		
		PrimeFaces.current().ajax().update("form:subTotalGeneral","form:iva","form:total","form:montoLetra");
			
	}
	
	//--------------------------------------- CONSTANCIAS DE FACTURACION (ENTRADAS) -------------------------------------------------------
	
	public void eliminarServicioEntrada() {
		
		for (ConstanciaFactura cf : listaEntradas) {			
			if(cf.getFolio().getFolioCliente().equals(selectServicioE.getConstancia().getFolio().getFolioCliente())) {				
				for (ServicioConstancia sc : cf.getServicioConstanciaList()) {					
					if (sc.getDescripcion().equals(selectServicioE.getDescripcion())) {
						cf.getServicioConstanciaList().remove(selectServicioE);
						break;
					}					
				}
			}			
		}		
	}
	
	public void facturacionEntradas(){
		
		try {
			listaEntradas = facturacionConstanciasDAO.buscarNoFacturados(clienteSelect.getCteCve(), plantaSelect.getPlantaCve());		
			
			if(listaEntradas.isEmpty()){				
				listaEntradas = new ArrayList<>();				
			}
		}catch (Exception e) {
			log.error(e);
		}
	}
	
	public void recalculoEntradas() {
		
		subTotalEntrada = new BigDecimal(0);
		BigDecimal sum;
		
		for(ConstanciaFactura cf: selectedEntradas) {
			
			sum = subTotalEntradaVigencias(cf.getServicioConstanciaList());
			subTotalEntrada = subTotalEntrada.add(sum);
			
		}
		sumaGeneral();
	}
	
	public void procesarEntradas() {
		Integer id = 0;		
		
		if(clienteSelect.getCteCve()!=null) {
			creacionFactura();
		}
		
		for(ConstanciaFactura cf: listaEntradas) {
				

			ConstanciaDeDeposito cdd = cf.getFolio();
			Aviso aviso = cdd.getAvisoCve();
			Camara camara = cdd.getPartidaList().get(0).getCamaraCve();
			String tipoFacturacion = aviso.getAvisoTpFacturacion();
			cf.setServicioConstanciaList(new ArrayList<>());

			for (ConstanciaDepositoDetalle cs : cdd.getConstanciaDepositoDetalleList()) {

				Servicio servicio = cs.getServicioCve();
				TipoCobro tipoCobro = servicio.getCobro();
				ServicioConstancia sc = new ServicioConstancia();

				BigDecimal importe = new BigDecimal(0);
				BigDecimal cantidad = null;

				List <PrecioServicio> listaPrecioServicio = precioServicioDAO.busquedaServicio(cdd.getAvisoCve().getAvisoCve(),
						clienteSelect.getCteCve(), servicio.getServicioCve());
				PrecioServicio precioServicio = null;
				if(!listaPrecioServicio.isEmpty()) {
				precioServicio = listaPrecioServicio.get(0);
				
				switch (tipoCobro.getId()) {

				case 1:
				case 2:

					cantidad = cs.getServicioCantidad();
					importe = cantidad.multiply(precioServicio.getPrecio());
					sc.setCosto(importe.setScale(2, BigDecimal.ROUND_HALF_UP));
					sc.setUnidadMedida("SRV");
					log.debug("El tipo cobro es 1 o 2 y su importe es: " + importe);
					break;

				case 3:
				case 4:

					cantidad = getCantidadPartidas(cdd.getPartidaList(), tipoFacturacion);

					importe = cantidad.multiply(precioServicio.getPrecio());
					sc.setCosto(importe.setScale(2, BigDecimal.ROUND_HALF_UP));
					log.debug("El tipo de cobro es 3 o 4 y su importe es entradas: " + importe);

					if (aviso.getAvisoTpFacturacion().equals("T")) {
						sc.setUnidadMedida("TRM");
					} else {
						sc.setUnidadMedida("KG");
					}

					break;

				default:

					log.debug("No existe el tipo de cobro");
					break;
				}

				sc.setId(id);
				sc.setConstancia(cf);
				sc.setTarifa(precioServicio.getPrecio().setScale(2, BigDecimal.ROUND_HALF_UP));
				sc.setBaseCargo(cantidad.setScale(2, BigDecimal.ROUND_HALF_UP));
				sc.setDescripcion(cs.getServicioCve().getServicioDs());
				sc.setPlantaCve(plantaSelect.getPlantaCve());
				sc.setPlantaDs(plantaSelect.getPlantaDs());
				sc.setPlantaAbrev(plantaSelect.getPlantaAbrev());
				sc.setPlantaCod(plantaSelect.getPlantaCod());

				
				sc.setCamaraCve(camara.getCamaraCve());
				sc.setCamaraDs(camara.getCamaraDs());
				sc.setCamaraAbrev(camara.getCamaraAbrev());
				sc.setCodigo(precioServicio.getServicio().getServicioCod());
				sc.setCdUnidad(precioServicio.getServicio().getCdUnidad());
				cf.getServicioConstanciaList().add(sc); // datos a mostrar row ex--
				id++;

			}
			}
			
			cf.setFactura(factura);
			cf.setPlantaCve(plantaSelect.getPlantaCve());
			cf.setPlantaDs(plantaSelect.getPlantaDs());
			cf.setPlantaAbrev(plantaSelect.getPlantaAbrev());
			cf.setCamaraCve(camara.getCamaraCve());
			cf.setCamaraDs(camara.getCamaraDs());
			cf.setCamaraAbrev(camara.getCamaraAbrev());
		}
		
		PrimeFaces.current().ajax().update("form:dt-serviciosEntrada");
		
	}
	
	public void relacionConstanciaFacturaEntradas() {
		
		BigDecimal sumTmp = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);
		subTotalEntrada = new BigDecimal(0);
		
		for(ConstanciaFactura cf: selectedEntradas) {
			
			sumTmp = subTotalEntradaVigencias(cf.getServicioConstanciaList()).setScale(2, BigDecimal.ROUND_HALF_UP);
			subTotalEntrada = subTotalEntrada.add(sumTmp);
			
			ConstanciaDeDeposito cdd = cf.getFolio();
			
			cdd.setConstanciaFacturaList(new ArrayList<>());
			cdd.getConstanciaFacturaList().addAll(selectedEntradas);
			
		}
		
		sumaGeneral();
		
	}
	
	//---------------------------------------------------- CONSTANCIAS DE FACTURACION VIGENCIAS -----------------------------------------
	public void procesarVigencias() {
		
		Integer id = 0;
		BigDecimal importe = new BigDecimal(0);
		
		for(ConstanciaFactura cf :listaVigencias) {
			
		
		
		ConstanciaDeDeposito cdd = cf.getFolio();
		Aviso aviso = cdd.getAvisoCve();
		Camara camara = cdd.getPartidaList().get(0).getCamaraCve();
		String tipoFacturacion = aviso.getAvisoTpFacturacion();
		cf.setServicioConstanciaList(new ArrayList<>());
		
		for (ConstanciaDepositoDetalle cs : cdd.getConstanciaDepositoDetalleList()) {
			
			Servicio servicio = cs.getServicioCve();
			TipoCobro tipoCobro = servicio.getCobro();
			ServicioConstancia sc = new ServicioConstancia();
			
			BigDecimal cantidad = null;
			
			List<PrecioServicio> listaPrecioServicio = precioServicioDAO.busquedaServicio(cdd.getAvisoCve().getAvisoCve(),
					clienteSelect.getCteCve(), servicio.getServicioCve());
			
			if(!listaPrecioServicio.isEmpty()) {
				PrecioServicio precioServicio = listaPrecioServicio.get(0);
				
				switch (tipoCobro.getId()) {
				
				case 1:
				case 2:
				case 3:
					break;
				case 4:
					
					cantidad = getCantidadPartidas(cdd.getPartidaList(), tipoFacturacion);
					importe = cantidad.multiply(precioServicio.getPrecio()).setScale(2, BigDecimal.ROUND_HALF_UP);
					log.debug("El tipo de cobro es 3 o 4 y su importe es: " + importe);
					sc.setCosto(importe);
					
					
					if (aviso.getAvisoTpFacturacion().equals("T")) {
						sc.setUnidadMedida("TRM");
					} else {
						sc.setUnidadMedida("KG");
					}
					
					break;
					
				default:
					
					log.info("No existe el tipo de cobro");
					break;
				}
				sc.setId(id);
				sc.setConstancia(cf);
				sc.setTarifa(precioServicio.getPrecio().setScale(2, BigDecimal.ROUND_HALF_UP));
				if(cantidad!=null) {
					sc.setBaseCargo(cantidad.setScale(2, BigDecimal.ROUND_HALF_UP));
				}
				sc.setDescripcion(cs.getServicioCve().getServicioDs());
				sc.setPlantaCve(plantaSelect.getPlantaCve());
				sc.setPlantaDs(plantaSelect.getPlantaDs());
				sc.setPlantaAbrev(plantaSelect.getPlantaAbrev());
				sc.setPlantaCod(plantaSelect.getPlantaCod());
				
				
				sc.setCamaraCve(camara.getCamaraCve());
				sc.setCamaraDs(camara.getCamaraDs());
				sc.setCamaraAbrev(camara.getCamaraAbrev());				
				sc.setCodigo(precioServicio.getServicio().getServicioCod());
				sc.setCdUnidad(precioServicio.getServicio().getCdUnidad());
				
				if (tipoCobro.getId() == 4) {
					cf.getServicioConstanciaList().add(sc);					
					id++;
				}
			}
		}
		
		cf.setFactura(factura);
		cf.setPlantaCve(plantaSelect.getPlantaCve());
		cf.setPlantaDs(plantaSelect.getPlantaDs());
		cf.setPlantaAbrev(plantaSelect.getPlantaAbrev());
		cf.setCamaraCve(camara.getCamaraCve());
		cf.setCamaraDs(camara.getCamaraDs());
		cf.setCamaraAbrev(camara.getCamaraAbrev());
		
		cdd.setConstanciaFacturaList(new ArrayList<>());
		cdd.getConstanciaFacturaList().addAll(listaEntradas);
		cdd.getConstanciaFacturaList().addAll(listaVigencias);
		}
		
		PrimeFaces.current().ajax().update("form:dt-serviciosVigencia");
	}
	
	public void relacionConstanciaFacturaVigencias() {
		
		BigDecimal sumTmp = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);
		subTotalVigencias = new BigDecimal(0);
		
		for(ConstanciaFactura cf: selectedVigencias) {
			
			sumTmp = subTotalEntradaVigencias(cf.getServicioConstanciaList()).setScale(2, BigDecimal.ROUND_HALF_UP);
			subTotalVigencias= subTotalVigencias.add(sumTmp);
			
			ConstanciaDeDeposito cdd = cf.getFolio();
			
			cdd.setConstanciaFacturaList(new ArrayList<>());
			cdd.getConstanciaFacturaList().addAll(selectedVigencias);
			
		}
		sumaGeneral();
	}
	
	public void facturacionVigencias(){
		
		try {
			log.debug("Fecha de corte: ", fechaCorte);
			DateUtil.setTime(fechaCorte, 0, 0, 0, 0);
			
			listaVigencias = facturacionVigenciasDAO.buscarNoFacturados(clienteSelect.getCteCve(), fechaCorte, plantaSelect.getPlantaCve());//Marca error cuando devuelve la lista vacia
			
			if(listaVigencias.isEmpty()){
				listaVigencias = new ArrayList<>();
			}
			
			
		}catch (Exception e) {
			log.error(e);
		}
	}
	
	
	public void eliminarServicioVigencia() {
		
		for (ConstanciaFactura cf : listaVigencias) {
			
			if(cf.getFolio().getFolioCliente().equals(selectServicioV.getConstancia().getFolio().getFolioCliente())) {
				for (ServicioConstancia sc : cf.getServicioConstanciaList()) {
					if (sc.getDescripcion().equals(selectServicioV.getDescripcion())) {
						cf.getServicioConstanciaList().remove(selectServicioV);
						break;
					}
				}
			}
			
		}
		
		recalculoVigencias();
		
		PrimeFaces.current().ajax().update("form:dt-selectVigencias:dt-serviciosVigencia");
		
	}
	
	public void recalculoVigencias() {
		
		subTotalVigencias = new BigDecimal(0);
		
		BigDecimal sum;
		
		for(ConstanciaFactura cf: selectedVigencias) {
			
			sum = subTotalEntradaVigencias(cf.getServicioConstanciaList());
			subTotalVigencias = subTotalVigencias.add(sum);
			
		}
		
		sumaGeneral();
		
	}
	
	public BigDecimal subTotalEntradaVigencias(List<ServicioConstancia> lista) {
		
		BigDecimal subTotal = new BigDecimal(0);
		
		for(ServicioConstancia sc: lista) {
			subTotal = subTotal.add(sc.getCosto());
		}
		
		return subTotal;
	}
	
	//------------------------------------------------- CONSTANCIA DE FACTURA DS (SERVICIOS) -----------------------------------------------------
	
	
	public BigDecimal getCantidadPartidas(List<Partida> listaPartidas, String tipoFacturacion) {

		BigDecimal cantidad = new BigDecimal(0).setScale(3, BigDecimal.ROUND_HALF_UP);
		
		if(tipoFacturacion.equals("T")) {
			BigDecimal fraccionTarimas = null;
			cantidad = listaPartidas.stream()
					.filter(p -> p.getNoTarimas().compareTo(BigDecimal.ONE.setScale(3, BigDecimal.ROUND_HALF_UP)) >= 0 )
					.map(item -> item.getNoTarimas())
					.reduce(BigDecimal.ZERO.setScale(3, BigDecimal.ROUND_HALF_UP), BigDecimal::add)
					;
			
			fraccionTarimas = listaPartidas.stream()
					.filter(p -> p.getNoTarimas().compareTo(BigDecimal.ONE.setScale(3, BigDecimal.ROUND_HALF_UP)) < 0 )
					.map(item -> item.getNoTarimas())
					.reduce(BigDecimal.ZERO.setScale(3, BigDecimal.ROUND_HALF_UP), BigDecimal::add)
					;
			fraccionTarimas = fraccionTarimas.setScale(0, BigDecimal.ROUND_CEILING);
			cantidad = cantidad.add(fraccionTarimas);
			
		} else {
			cantidad = listaPartidas.stream()
					.map(item -> item.getPesoTotal())
					.reduce(BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP), BigDecimal::add)
					;
		}
		log.info("Cantidad: {} {}", cantidad, tipoFacturacion);

		for (Partida p : listaPartidas) {

			if (tipoFacturacion.equals("T")) {
				cantidad = cantidad.add(p.getNoTarimas());
			} else {
				cantidad = cantidad.add(p.getPesoTotal());
				log.debug("Peso total: {}" + p.getPesoTotal());
				log.debug("Cantidad: " + cantidad);
			}

		}
		
		log.info("Cantidad: {} {}", cantidad, tipoFacturacion);

		return cantidad;
	}

	public void procesarServicios() {

		BigDecimal importe = new BigDecimal(0);
		Integer idS = 0,idP = 0;
		
		for (ConstanciaFacturaDs cfd : listaServicios) {

			List<ConstanciaServicioDetalle> listaConstanciaSD = cfd.getConstanciaDeServicio()
					.getConstanciaServicioDetalleList();
			List<PartidaServicio> listaPartidaServicio = cfd.getConstanciaDeServicio().getPartidaServicioList();

			cfd.setServicioConstanciaDsList(new ArrayList<>());
			cfd.setProductoConstanciaDsList(new ArrayList<>());

			for (ConstanciaServicioDetalle csd : listaConstanciaSD) {

				cantidad = new BigDecimal(0);

				List<PrecioServicio> listaPrecioS = csd.getServicioCve().getPrecioServicioList();
				PrecioServicio precioServicio = getPrecioServicio(clienteSelect.getCteCve(), listaPrecioS);

				ServicioConstanciaDs servicioConstanciaDs = new ServicioConstanciaDs();
				
				servicioConstanciaDs.setId(idS);
				servicioConstanciaDs.setDescripcion(csd.getServicioCve().getServicioDs());
				servicioConstanciaDs.setConstancia(cfd);

				if (precioServicio.getPrecio() != null) {// modificado por error null al llamar al metodo
															// getPrecioServicio
					cantidad = precioServicio.getPrecio().setScale(2, BigDecimal.ROUND_HALF_UP);// variable agregada
					importe = csd.getServicioCantidad().multiply(cantidad);

					servicioConstanciaDs.setCosto(importe.setScale(2, BigDecimal.ROUND_HALF_UP));
					servicioConstanciaDs.setTarifa(precioServicio.getPrecio().setScale(2, BigDecimal.ROUND_HALF_UP));
				}
				servicioConstanciaDs.setCodigo(csd.getServicioCve().getServicioCod());
				servicioConstanciaDs.setUdCobro("SRV");
				servicioConstanciaDs.setCdUnidad(csd.getServicioCve().getCdUnidad());
				servicioConstanciaDs.setCantidad(csd.getServicioCantidad());

				cfd.getServicioConstanciaDsList().add(servicioConstanciaDs);
				idS++;
			}

			for (PartidaServicio ps : listaPartidaServicio) {

				ProductoConstanciaDs productoConstanciaDs = new ProductoConstanciaDs();

				productoConstanciaDs.setId(idP);
				productoConstanciaDs.setDescripcion(ps.getProductoCve().getProductoDs());
				productoConstanciaDs.setConstancia(cfd);
				productoConstanciaDs.setCatidadCobro(ps.getCantidadDeCobro());
				productoConstanciaDs.setUnidadCobro(ps.getUnidadDeCobro().getUnidadDeManejoDs());
				productoConstanciaDs.setCantidadManejo(new BigDecimal(ps.getCantidadTotal()).setScale(2, BigDecimal.ROUND_HALF_UP));
				productoConstanciaDs.setUnidadManejo(ps.getUnidadDeManejoCve().getUnidadDeManejoDs());
				
				cfd.getProductoConstanciaDsList().add(productoConstanciaDs);
				idP++;
			}
			
			cfd.setFactura(factura);
		}

	}
	
	private PrecioServicio getPrecioServicio(Integer idCliente, List<PrecioServicio> listaPrecioS) {

		List<PrecioServicio> listaPrecioSTemp = null;
		PrecioServicio precioServicio = new PrecioServicio();

		listaPrecioSTemp = listaPrecioS.stream().filter(ps -> ps.getCliente().getCteCve().intValue() == idCliente)
				.collect(Collectors.toList());

		if (!(listaPrecioSTemp.isEmpty())) { // modificado por error de retornar objeto precioServicio en null
			Optional<PrecioServicio> precioServicioMax = listaPrecioSTemp.stream()
					.max((i, j) -> i.getAvisoCve().getAvisoCve().compareTo(j.getAvisoCve().getAvisoCve()));
			precioServicio = precioServicioMax.get();// colocar condicional, si y solo si precioServicioMax != null
		}

		return precioServicio;
	}
	
	public void eliminarServicioDs() {
		
		for(ConstanciaFacturaDs cfd: listaServicios) {
			
			if(cfd.getFolioCliente().equals(selectServicioDs.getConstancia().getFolioCliente())) {
			
				for(ServicioConstanciaDs sc: cfd.getServicioConstanciaDsList()) {
					
					if(sc.getDescripcion().equals(selectServicioDs.getDescripcion())) {
						cfd.getServicioConstanciaDsList().remove(selectServicioDs);
						break;
					}
				}
			}
			
		}
		
		recalculoServicioDs(); 
		
		PrimeFaces.current().ajax().update("form:dt-constanciaFacturaDs:dt-serviciosDs");
		
	}

	public void recalculoServicioDs() {//calculo el subtotal con la lista actualizada despues de eliminar un servicio 
		
		subTotalServicios = new BigDecimal(0);
		BigDecimal sum;
		
		for(ConstanciaFacturaDs cf: selectedServicios) {
			
			sum = subTotalServicios(cf.getServicioConstanciaDsList());	
			subTotalServicios = subTotalServicios.add(sum);
		}
		
		sumaGeneral();
		
	}
		
	public void facturacionServicios(){
		
		try {
			listaServicios = facturacionServiciosDAO.buscarNoFacturados(clienteSelect.getCteCve());
			
			if(listaServicios.isEmpty()) {
				listaServicios = new ArrayList<>();
			}
		} catch (Exception e) {
			log.error(e);
		}
	}
	
	public BigDecimal subTotalServicios(List<ServicioConstanciaDs> lista) {
		BigDecimal subTotal = new BigDecimal(0);
		
		for(ServicioConstanciaDs scd: lista) {
			subTotal = subTotal.add(scd.getCosto());
		}
		
		return subTotal;
		
	}
	
	
	public void creacionFactura(){
		clienteSelect = clienteDAO.buscarPorId(clienteSelect.getCteCve(), true);
		factura = new Factura();
		
		factura.setCliente(clienteSelect);
		factura.setMoneda(moneda);
		factura.setRfc(clienteSelect.getCteRfc());
		factura.setNombreCliente(clienteSelect.getNombre());
		factura.setFecha(fechaCorte);
		if(observaciones == null)
			factura.setObservacion("");
		else
			factura.setObservacion(observaciones);
		factura.setSubtotal(null);//duda*
		factura.setIva(null);//duda*
		factura.setTotal(null);//duda*
		factura.setPais(domicilioSelect.getPaisCved().getPaisDesc());
		factura.setEstado(domicilioSelect.getCiudades().getMunicipios().getEstados().getEstadoDesc());
		factura.setMunicipio(domicilioSelect.getCiudades().getMunicipios().getMunicipioDs());
		factura.setCiudad(domicilioSelect.getCiudades().getCiudadDs());
		
		AsentamientoHumandoDAO asentamientoDAO = new AsentamientoHumandoDAO();
		
		AsentamientoHumano asentamiento = asentamientoDAO.buscarPorAsentamiento(domicilioSelect.getPaisCved().getPaisCve(),
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
		factura.setPorcentajeIva(resIva);
		factura.setNumeroCliente(clienteSelect.getNumeroCte());
		factura.setValorDeclarado(valorDeclarado);
		factura.setInicioServicios(fechaCorte);
		factura.setFinServicios(fechaCorte);
		factura.setMontoLetra(null);
		factura.setFacturaMedioPagoList(new ArrayList<FacturaMedioPago>());
		FacturaMedioPagoPK facturaPK = new FacturaMedioPagoPK();
		facturaPK.setFacturaId(factura);
		facturaPK.setFmpId(0);
		FacturaMedioPago fmp = new FacturaMedioPago();
		fmp.setFacturaMedioPagoPK(facturaPK);
		
		MedioPago medioP = new MedioPago();
		if((medioPagoSelect.getFormaPago()!=null)&&(medioPagoSelect.getFormaPago().equals(formaPagoCliente.getFormaPago()))) { //se queda con el medioPagoSelect anterior por tanto como en el segundo no es nulo entra a buscar el mismo mediopagoSelect
			medioP = medioPagoDAO.buscarPorFormaPago(medioPagoSelect.getFormaPago());      //ERROR ,EDIO PAGO SELECT		
		}else {
			medioP = medioPagoDAO.buscarPorFormaPago(formaPagoCliente.getFormaPago());      //ERROR ,EDIO PAGO SELECT
			medioPagoSelect = medioP;
		}
		fmp.setMpId(medioP);
		fmp.setFactura(factura);
		fmp.setFmpPorcentaje(100);
		fmp.setMpDescripcion(medioP.getMpDescripcion());
		fmp.setFmpReferencia(referencia);
		factura.getFacturaMedioPagoList().add(fmp); 
		//---------------------
		StatusFactura statusF = statusFacturaDAO.buscarPorId(1);
		   
		factura.setStatus(statusF);
		
		TipoFacturacion tipoFacturacion = tipoFacturacionDAO.buscarPorId(1);
		
		factura.setTipoFacturacion(tipoFacturacion);
		factura.setPlanta(plantaSelect);
		factura.setPlazo(plazoSelect);
		factura.setRetencion(BigDecimal.ZERO);
		factura.setNomSerie(serieFacturaSelect.getNomSerie());
		
		MetodoPago mp = new MetodoPago();
		if(metodoPagoSelect.getCdMetodoPago()!=null) {
			mp = MPagoSelect;
			factura.setMetodoPago(mp.getCdMetodoPago());
		}else {
			mp = clienteSelect.getMetodoPago();
			factura.setMetodoPago(mp.getCdMetodoPago());
			MPagoSelect = mp;
		}
		
	
		
		factura.setTipoPersona(clienteSelect.getTipoPersona());
		factura.setCdRegimen(clienteSelect.getRegimenFiscal().getCd_regimen());
		factura.setCdUsoCfdi(clienteSelect.getUsoCfdi().getCdUsoCfdi());
		factura.setUuid(null);
		if(this.emisor == null) {
			factura.setEmisorNombre(plantaSelect.getIdEmisoresCFDIS().getNb_emisor());
			factura.setEmisorRFC(plantaSelect.getIdEmisoresCFDIS().getNb_rfc());
			factura.setEmisorCdRegimen(plantaSelect.getIdEmisoresCFDIS().getCd_regimen().getCd_regimen());
		} else {
			factura.setEmisorNombre(this.emisor.getNb_emisor());
			factura.setEmisorRFC(this.emisor.getNb_rfc());
			factura.setEmisorCdRegimen(this.emisor.getCd_regimen().getCd_regimen());
		}
		
		PrimeFaces.current().ajax().update("form:metodoPago");
	}
	
	public void saveFactura() throws InventarioException {
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		String titulo = "Guardar factura";
		
		List<Factura> buscaFacturasList = null;
		SerieFactura serieTmp = null;
		List<SerieFactura> listaSerieFacturaBkp = null;
		String resultado = null;
		
		try {
			listaSerieFacturaBkp = new ArrayList<SerieFactura>();
			listaSerieFacturaBkp.addAll(listaSerieFactura);
			
			listaSerieFacturaBkp = serieFacturaDAO.buscarPorEmisor(this.emisor, true);
			
			//haciendo null id de los servicios constancias de constancias de deposito 
			for(ConstanciaFactura cf: selectedEntradas) {
				for(ServicioConstancia sc: cf.getServicioConstanciaList()) {
					sc.setId(null);
				}
			}
			
			for(ConstanciaFactura cf: selectedVigencias) {
				for(ServicioConstancia sc: cf.getServicioConstanciaList()) {
					sc.setId(null);
				}
			}
			
			//haciendo null id de serviciosDs  y productosDs de constancia servicios 
			for(ConstanciaFacturaDs cfd: selectedServicios) {
				for(ServicioConstanciaDs scd: cfd.getServicioConstanciaDsList()) {
					scd.setId(null);
				}
				
				for(ProductoConstanciaDs pcd: cfd.getProductoConstanciaDsList()) {
					pcd.setId(null);
				}	
				
			}
			
			if(factura.getId() != null)
				throw new InventarioException("La factura ya se encuentra registrada");
			
			if(BigDecimal.ZERO.compareTo(subTotalGeneral) == 0)
				throw new InventarioException("Debe registrar un importe mayor a cero.");
			
			log.info("Emisor: {}", this.emisor);
			log.info("Serie-número: {}-{}", this.serieFacturaSelect.getNomSerie(), (this.serieFacturaSelect.getNumeroActual() + 1));
			
			serieTmp = serieFacturaDAO.findById(serieFacturaSelect.getId());
			buscaFacturasList = facturaDAO.buscarActivasPorSerieNumero(serieFacturaSelect.getNomSerie(), String.valueOf(serieFacturaSelect.getNumeroActual() + 1));
			
			if(buscaFacturasList.size() > 0)
				throw new InventarioException("La factura ya se encuentra registrada.");
			
			factura.setEmisorNombre(this.emisor.getNb_emisor());
			factura.setEmisorRFC(this.emisor.getNb_rfc());
			factura.setEmisorCdRegimen(this.emisor.getCd_regimen().getCd_regimen());
			factura.setNumero(String.valueOf(serieFacturaSelect.getNumeroActual() + 1));
			factura.setNomSerie(this.serieFacturaSelect.getNomSerie());
			resultado = facturaDAO.guardar(factura);
			
			if(resultado != null && "".equalsIgnoreCase(resultado.trim()))
				throw new InventarioException("Ocurrió un problema al guardar la factura.");
			
			this.serieFacturaSelect.setNumeroActual(serieFacturaSelect.getNumeroActual() + 1);
			serieFacturaDAO.update(this.serieFacturaSelect);
			
			this.listaSerieFactura = listaSerieFacturaBkp;
			this.serieFacturaSelect = serieTmp;
			
			mensaje = String.format("La factura %s-%s se guardo correctamente", this.serieFacturaSelect.getNomSerie(), this.serieFacturaSelect.getNumeroActual() + 1);
			severity = FacesMessage.SEVERITY_INFO;
		} catch(InventarioException ex) {
			log.error("Problema para guardar la factura...", ex);
			mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_WARN;
		} catch (Exception ex) {
			log.error("Problema para guardar la factura...", ex);
			mensaje = "Existe un problema al guardar la factura";
			severity = FacesMessage.SEVERITY_ERROR;
		}finally {
			message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
			PrimeFaces.current().ajax().update("form:messages", "form:serieFactura");
		}
	}
	
	public void jasper() throws JRException, IOException, SQLException {
		String jasperPath = "/jasper/Factura.jrxml";
		String filename = null;
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
			
			if (this.factura.getId() == null)
				throw new InventarioException("Debe Guardar la Factura primero");
			
			filename = String.format("Factura_%s-%s.pdf", factura.getNomSerie(), factura.getNumero());
			
			URL resource = getClass().getResource(jasperPath);
			URL resourceimg = getClass().getResource(images);
			String file = resource.getFile();
			String img = resourceimg.getFile();
			reportFile = new File(file);
			imgfile = new File(img);
			Integer num = factura.getId();
			connection = EntityManagerUtil.getConnection();
			parameters.put("REPORT_CONNECTION", connection);
			parameters.put("idFactura", num);
			parameters.put("imagen", imgfile.getPath());
			jasperReportUtil.createPdf(filename, parameters, reportFile.getPath());
		} catch (InventarioException ex) {
			ex.fillInStackTrace();
			//log.error("Problema general...", ex);
			message = String.format("Guarda primero la factura para poder realizar la impresion");
			severity = FacesMessage.SEVERITY_ERROR;
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(severity, "Error en impresion", message));
			//PrimeFaces.current().ajax().update("form:messages");
		} finally {
			conexion.close((Connection) connection);
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
			message = "El timbrado se generó correctamente";
		} catch (FacturamaException e) {
			severity = FacesMessage.SEVERITY_ERROR;
			message = e.getMessage();
			e.printStackTrace();
		}catch (Exception ex) {
			//log.error("Problema para obtener los servicios del cliente.", ex);
			ex.printStackTrace();
			message = "Problema con la información de servicios.";
			severity = FacesMessage.SEVERITY_ERROR;
		} finally {
			if(severity == null)
				severity = FacesMessage.SEVERITY_FATAL;
			if(message == null)
				message = "Ocurrió un error con la actualización de la factura.";
			
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, "Timbrado CFDI", message));
			PrimeFaces.current().ajax().update("form:messages");
		}
		
	}
	
public String paginaFactura() {
	
		session.removeAttribute("plantaSelect");
		session.removeAttribute("cliente");
		session.removeAttribute("entrada");
		session.removeAttribute("vigencia");
		session.removeAttribute("servicios");
		session.removeAttribute("factura");
		session.removeAttribute("fechaEmision");
		session.removeAttribute("iva");
		session.removeAttribute("medioPago");
		session.removeAttribute("metodoPago");
		session.removeAttribute("domicilioSelect");
		session.removeAttribute("serieFacturaSelect");
		
		if(factura.getId()!=null) {
			FacturacionConstanciasBean fcb = new FacturacionConstanciasBean();
			
			fcb.init();
		}
		
		return "facturacionConstancias.xhtml?faces-redirect=true";
		
	}

	public String reload() throws IOException {
	
		return "facturacionConstancias.xhtml?faces-redirect=true";
	}

	public void  reset() {
		ExternalContext ec = null;
	    try {
	    	ec = FacesContext.getCurrentInstance().getExternalContext();
			ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
		} catch (IOException e) {
			log.error("Problema para crear una nueva factura...",  e);
		}
	}

	public ServicioConstancia getSelectServicioE() {
		return selectServicioE;
	}

	public void setSelectServicioE(ServicioConstancia selectServicioE) {
		this.selectServicioE = selectServicioE;
	}

	public Cliente getClienteSelect() {
		return clienteSelect;
	}

	public void setClienteSelect(Cliente clienteSelect) {
		this.clienteSelect = clienteSelect;
	}

	public List<Cliente> getListaCliente() {
		return listaCliente;
	}

	public void setListaCliente(List<Cliente> listaCliente) {
		this.listaCliente = listaCliente;
	}

	public Domicilios getDomicilioSelect() {
		return domicilioSelect;
	}

	public void setDomicilioSelect(Domicilios domicilioSelect) {
		this.domicilioSelect = domicilioSelect;
	}

	public List<ClienteDomicilios> getListaClienteDomicilio() {
		return listaClienteDomicilio;
	}

	public void setListaClienteDomicilio(List<ClienteDomicilios> listaClienteDomicilio) {
		this.listaClienteDomicilio = listaClienteDomicilio;
	}

	public Planta getPlantaSelect() {
		return plantaSelect;
	}

	public void setPlantaSelect(Planta plantaSelect) {
		this.plantaSelect = plantaSelect;
	}
	
	public List<Planta> getListaPlanta() {
		return listaPlanta;
	}

	public void setListaPlanta(List<Planta> listaPlanta) {
		this.listaPlanta = listaPlanta;
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

	public Date getFechaFactura() {
		return fechaFactura;
	}

	public void setFechaFactura(Date fechaFactura) {
		this.fechaFactura = fechaFactura;
	}

	public String getMoneda() {
		return moneda;
	}

	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}

	public List<Aviso> getListaAviso() {
		return listaAviso;
	}

	public void setListaAviso(List<Aviso> listaAviso) {
		this.listaAviso = listaAviso;
	}

	public int getPlazoSelect() {
		return plazoSelect;
	}

	public void setPlazoSelect(int plazoSelect) {
		this.plazoSelect = plazoSelect;
	}	
	
	public MetodoPago getMetodoPagoSelect() {
		return metodoPagoSelect;
	}

	public void setMetodoPagoSelect(MetodoPago metodoPagoSelect) {
		this.metodoPagoSelect = metodoPagoSelect;
	}

	public MetodoPagoDAO getMetodoPagoDAO() {
		return metodoPagoDAO;
	}

	public void setMetodoPagoDAO(MetodoPagoDAO metodoPagoDAO) {
		this.metodoPagoDAO = metodoPagoDAO;
	}

	public List<MetodoPago> getListaMetodoPago() {
		return listaMetodoPago;
	}

	public void setListaMetodoPago(List<MetodoPago> listaMetodoPago) {
		this.listaMetodoPago = listaMetodoPago;
	}

	public List<ConstanciaFactura> getListaEntradas() {
		return listaEntradas;
	}

	public void setListaEntradas(List<ConstanciaFactura> listaEntradas) {
		this.listaEntradas = listaEntradas;
	}

	public MedioPago getMedioPagoSelect() {
		return medioPagoSelect;
	}

	public void setMedioPagoSelect(MedioPago medioPagoSelect) {
		this.medioPagoSelect = medioPagoSelect;
	}

	public List<MedioPago> getListaMedioPago() {
		return listaMedioPago;
	}

	public void setListaMedioPago(List<MedioPago> listaMedioPago) {
		this.listaMedioPago = listaMedioPago;
	}	

	public Date getFechaCorte() {
		return fechaCorte;
	}

	public void setFechaCorte(Date fechaCorte) {
		this.fechaCorte = fechaCorte;
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

	public List<ConstanciaFactura> getSelectedVigencias() {
		return selectedVigencias;
	}

	public void setSelectedVigencias(List<ConstanciaFactura> selectedVigencias) {
		this.selectedVigencias = selectedVigencias;
	}

	public List<ConstanciaFactura> getSelectedEntradas() {
		return selectedEntradas;
	}

	public void setSelectedEntradas(List<ConstanciaFactura> selectedEntradas) {
		this.selectedEntradas = selectedEntradas;
	}

	public List<ConstanciaFacturaDs> getSelectedServicios() {
		return selectedServicios;
	}

	public void setSelectedServicios(List<ConstanciaFacturaDs> selectedServicios) {
		this.selectedServicios = selectedServicios;
	}

	public BigDecimal getResIva() {
		return resIva;
	}

	public void setResIva(BigDecimal resIva) {
		this.resIva = resIva;
	}

	public BigDecimal getResRetencion() {
		return resRetencion;
	}

	public void setResRetencion(BigDecimal resRetencion) {
		this.resRetencion = resRetencion;
	}

	public Factura getFactura() {
		return factura;
	}

	public void setFactura(Factura factura) {
		this.factura = factura;
	}


	public BigDecimal getValorDeclarado() {
		return valorDeclarado;
	}

	public void setValorDeclarado(BigDecimal valorDeclarado) {
		this.valorDeclarado = valorDeclarado;
	}

	public String getReferencia() {
		return referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	public Parametro getIva() {
		return iva;
	}

	public void setIva(Parametro iva) {
		this.iva = iva;
	}

	public MedioPago getFormaPagoCliente() {
		return formaPagoCliente;
	}

	public void setFormaPagoCliente(MedioPago formaPagoCliente) {
		this.formaPagoCliente = formaPagoCliente;
	}

	public BigDecimal getCantidad() {
		return cantidad;
	}

	public void setCantidad(BigDecimal cantidad) {
		this.cantidad = cantidad;
	}

	public BigDecimal getSubTotalServicios() {
		return subTotalServicios;
	}

	public void setSubTotalServicios(BigDecimal subTotalServicios) {
		this.subTotalServicios = subTotalServicios;
	}

	public ConstanciaFactura getEntradaSelect() {
		return entradaSelect;
	}

	public void setEntradaSelect(ConstanciaFactura entradaSelect) {
		this.entradaSelect = entradaSelect;
	}

	public ConstanciaFactura getVigenciaSelect() {
		return vigenciaSelect;
	}

	public void setVigenciaSelect(ConstanciaFactura vigenciaSelect) {
		this.vigenciaSelect = vigenciaSelect;
	}

	public ServicioConstancia getSelectServicioV() {
		return selectServicioV;
	}

	public void setSelectServicioV(ServicioConstancia selectServicioV) {
		this.selectServicioV = selectServicioV;
	}

	public BigDecimal getSubTotalVigencias() {
		return subTotalVigencias;
	}

	public void setSubTotalVigencias(BigDecimal subTotalVigencias) {
		this.subTotalVigencias = subTotalVigencias;
	}

	public BigDecimal getIvaTotal() {
		return ivaTotal;
	}

	public void setIvaTotal(BigDecimal ivaTotal) {
		this.ivaTotal = ivaTotal;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public Parametro getRetencion() {
		return retencion;
	}

	public void setRetencion(Parametro retencion) {
		this.retencion = retencion;
	}

	public BigDecimal getSubTotalEntrada() {
		return subTotalEntrada;
	}

	public void setSubTotalEntrada(BigDecimal subTotalEntrada) {
		this.subTotalEntrada = subTotalEntrada;
	}

	public BigDecimal getSubTotalGeneral() {
		return subTotalGeneral;
	}

	public void setSubTotalGeneral(BigDecimal subTotalGeneral) {
		this.subTotalGeneral = subTotalGeneral;
	}

	public ServicioConstanciaDs getSelectServicioDs() {
		return selectServicioDs;
	}

	public void setSelectServicioDs(ServicioConstanciaDs selectServicioDs) {
		this.selectServicioDs = selectServicioDs;
	}

	public ConstanciaFacturaDs getServicioConstanciaSelect() {
		return servicioConstanciaSelect;
	}

	public void setServicioConstanciaSelect(ConstanciaFacturaDs servicioConstanciaSelect) {
		this.servicioConstanciaSelect = servicioConstanciaSelect;
	}

	public String getMontoLetra() {
		return montoLetra;
	}

	public void setMontoLetra(String montoLetra) {
		this.montoLetra = montoLetra;
	}

	public FacesContext getContext() {
		return context;
	}

	public void setContext(FacesContext context) {
		this.context = context;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public MetodoPago getMPagoSelect() {
		return MPagoSelect;
	}

	public void setMPagoSelect(MetodoPago mPagoSelect) {
		MPagoSelect = mPagoSelect;
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
	
	
}
