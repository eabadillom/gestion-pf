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

import org.apache.log4j.Logger;
import org.primefaces.PrimeFaces;

import com.ferbo.facturama.business.CfdiBL;
import com.ferbo.facturama.request.CFDIInfo;
import com.ferbo.facturama.request.IssuerBindingModel;
import com.ferbo.facturama.request.ItemFullBindingModel;
import com.ferbo.facturama.request.ReceiverBindingModel;
import com.ferbo.facturama.request.Tax;
import com.ferbo.facturama.response.CfdiInfoModel;
import com.ferbo.facturama.tools.FacturamaException;

import mx.com.ferbo.dao.AsentamientoHumandoDAO;
import mx.com.ferbo.dao.AvisoDAO;
import mx.com.ferbo.dao.ClaveUnidadDAO;
import mx.com.ferbo.dao.ClienteDAO;
import mx.com.ferbo.dao.ClienteDomiciliosDAO;
import mx.com.ferbo.dao.FacturaDAO;
import mx.com.ferbo.dao.FacturacionDepositosDAO;
import mx.com.ferbo.dao.MedioPagoDAO;
import mx.com.ferbo.dao.MetodoPagoDAO;
import mx.com.ferbo.dao.ParametroDAO;
import mx.com.ferbo.dao.PlantaDAO;
import mx.com.ferbo.dao.SerieFacturaDAO;
import mx.com.ferbo.dao.StatusFacturaDAO;
import mx.com.ferbo.dao.TipoFacturacionDAO;
import mx.com.ferbo.model.AsentamientoHumano;
import mx.com.ferbo.model.Aviso;
import mx.com.ferbo.model.ClaveUnidad;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.ClienteDomicilios;
import mx.com.ferbo.model.ConstanciaDeDeposito;
import mx.com.ferbo.model.ConstanciaFactura;
import mx.com.ferbo.model.ConstanciaFacturaDs;
import mx.com.ferbo.model.Domicilios;
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
import mx.com.ferbo.model.TipoFacturacion;
import mx.com.ferbo.model.UnidadDeManejo;
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
	private static Logger log = Logger.getLogger(FacturaServiciosBean.class);

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

	private Cliente clienteSelect;
	private Domicilios domicilioSelect;
	private Planta plantaSelect;
	private String metodoPagoSelect;
	private String medioPagoSelect;
	private Parametro iva, retencion;
	private SerieFactura serieFacturaSelect;
	private ServicioFactura selServicio;
	private PrecioServicio precioServicio;
	private Factura factura;

	private ClienteDAO clienteDAO;
	private ClienteDomiciliosDAO clienteDomicilioDAO;
	private PlantaDAO plantaDAO;
	private AvisoDAO avisoDAO;
	private MetodoPagoDAO metodoPagoDAO;
	private MedioPagoDAO medioPagoDAO;
	private ParametroDAO parametroDAO;
	private SerieFacturaDAO seriefacturaDAO;
	private FacturacionDepositosDAO facturacionConstanciasDAO;
	private FacturaDAO facturaDAO;
	private AsentamientoHumandoDAO asnDAO;
	private StatusFacturaDAO statusDAO;
	private TipoFacturacionDAO tipoDAO;
	private ClaveUnidadDAO claveDAO;

	private int plazoSelect;
	private Integer idPrecioServicio;
	private Integer idFactura;
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

	public FacturaServiciosBean() {
		
		clientes = new ArrayList<Cliente>();
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
		metodoPagoSelect = null;
		medioPagoSelect = null;
		
		clienteDAO = new ClienteDAO();
		clienteDomicilioDAO = new ClienteDomiciliosDAO();
		plantaDAO = new PlantaDAO();
		avisoDAO = new AvisoDAO();
		metodoPagoDAO = new MetodoPagoDAO();
		medioPagoDAO = new MedioPagoDAO();
		parametroDAO = new ParametroDAO();
		seriefacturaDAO = new SerieFacturaDAO();
		facturacionConstanciasDAO = new FacturacionDepositosDAO();
		asnDAO = new AsentamientoHumandoDAO();
		statusDAO = new StatusFacturaDAO();
		tipoDAO = new TipoFacturacionDAO();
		facturaDAO = new FacturaDAO();
		claveDAO = new ClaveUnidadDAO();
	}

	@PostConstruct
	public void init() {
		clientes = clienteDAO.buscarTodos();
		listaClienteDom = clienteDomicilioDAO.buscarTodos();
		listaPlanta = plantaDAO.findall();
		listaA = avisoDAO.buscarTodos();
		listaMetodoPago = metodoPagoDAO.buscarTodos();
		listaMedioPago = medioPagoDAO.buscarTodos();
		listaSerieF = seriefacturaDAO.findAll();

	}

	public void domicilioAvisoPorCliente() {
		String message = null;
		Severity severity = null;
		iva = parametroDAO.buscarPorNombre("IVA");
		tasaIva = new BigDecimal(iva.getValor()).setScale(2, BigDecimal.ROUND_HALF_UP);
		retencion = parametroDAO.buscarPorNombre("RETENCION");
		Map<Integer, List<PrecioServicio>> mpPrecioServicio = new HashMap<Integer, List<PrecioServicio>>();
		List<PrecioServicio> precioServicioList = null;
		listaClienteDomicilio.clear();
		listaClienteDomicilio = listaClienteDom.stream()
				.filter(cd -> clienteSelect != null
						? (cd.getCteCve().getCteCve().intValue() == clienteSelect.getCteCve().intValue())
						: false)
				.collect(Collectors.toList());
		if (listaClienteDomicilio.size() > 0) {
			domicilioSelect = listaClienteDomicilio.get(0).getDomicilios();
		}
		
		precioServicioList = clienteSelect.getPrecioServicioList();
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
		//
		message = "Agregue sus servicios.";
		severity = FacesMessage.SEVERITY_INFO;
		AvisoCliente();
		constancias();
		setMedioPago();
		setMetodoPago();
	}
	
		public void setMedioPago() {
			medioPagoSelect =  clienteSelect.getFormaPago();
		}
		
		public void setMetodoPago() {
			metodoPagoSelect = clienteSelect.getMetodoPago().getCdMetodoPago();
		}


	public void AvisoCliente() {
		listaAviso.clear();
		listaAviso = listaA.stream().filter(av -> clienteSelect != null
						? (av.getCteCve().getCteCve().intValue() == clienteSelect.getCteCve().intValue())
						: false).collect(Collectors.toList());
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

	public void serieFactura() {
		listaSerieFactura.clear();
		listaSerieFactura = listaSerieF.stream()
				.filter(s -> plantaSelect != null
						? (s.getIdPlanta().getPlantaCve().intValue() == plantaSelect.getPlantaCve().intValue())
						: false)
				.collect(Collectors.toList());
		if (listaSerieFactura.size() > 0) {
			serieFacturaSelect = listaSerieFactura.get(0);
		}
		constancias();
	}

	public void constancias() {
		this.facturacionEntradas();
		this.facturacionVigencias();
		PrimeFaces.current().ajax().update("form:dt-constanciasE", "form:dt-vigencias", "form:dt-servicios");
	}

	public void facturacionEntradas() {

		if (clienteSelect == null) {
			return;
		}

		if (plantaSelect == null) {
			return;
		}

			listaEntradas = facturacionConstanciasDAO.buscarNoFacturados(clienteSelect.getCteCve(),
																			plantaSelect.getPlantaCve());

		if (listaEntradas.isEmpty()) {
			listaEntradas = new ArrayList<>();
		}

	}

	public void facturacionVigencias() {
		if (clienteSelect == null) {
			return;
		}

		if (plantaSelect == null) {
			return;
		}

	}

	public void agregarServicio() {
		String message = null;
		Severity severity = null;
		ServicioFactura servicio = null;
		try {
			if (this.cantidadServicio == null || this.cantidadServicio.compareTo(BigDecimal.ZERO) <= 0)
				throw new InventarioException("Debe indicar la cantidad de servicios.");
			if (this.precioServicio == null)
				throw new InventarioException("Debe seleccionar un servicio.");

			if (alServiciosDetalle == null)
				alServiciosDetalle = new ArrayList<ServicioFactura>();

			servicio = new ServicioFactura();
			servicio.setDescripcion(precioServicio.getServicio().getServicioDs());
			servicio.setCantidad(cantidadServicio);
			servicio.setUnidad(precioServicio.getUnidad().getUnidadDeManejoDs());
			servicio.setTarifa(precioServicio.getPrecio().setScale(2, BigDecimal.ROUND_HALF_UP));
			servicio.setCosto(
					cantidadServicio.multiply(precioServicio.getPrecio().setScale(2, BigDecimal.ROUND_HALF_UP)));
			servicio.setTipoCobro(precioServicio.getServicio().getCobro());
			servicio.setUdCobro("Servicio");
			servicio.setCodigo(precioServicio.getServicio().getServicioCod());
			servicio.setCdUnidad(precioServicio.getServicio().getCdUnidad());
			// alServiciosDetalle.add(servicio);
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
					System.out.println("ya existe el servicio");
				} else if (diferentes > 0) {
					alServiciosDetalle.add(servicio);
				}
			}
			for (ServicioFactura sf : alServiciosDetalle) {
				subtotal = subtotal.add(sf.getCosto().setScale(2, BigDecimal.ROUND_HALF_UP));
			}
			bdIva = subtotal.multiply(tasaIva).setScale(2, BigDecimal.ROUND_HALF_UP);
			total = bdIva.add(subtotal).setScale(2, BigDecimal.ROUND_HALF_UP);
			montoLetra = FormatUtil.numeroPalabras(total.doubleValue());
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
			PrimeFaces.current().ajax().update("form:messages", "form:dt-facturacionServicios", "form:montoLetra");
		}
	}

	public synchronized void SaveFactura() {
		String message = null;
		Severity severity = null;
		try {
			if (this.alServiciosDetalle == null || this.alServiciosDetalle.size() == 0)
				throw new InventarioException("Debe traspasar almenos un producto");

			// Datos receptor
			factura = new Factura();
			List<Factura> alFacturas = new ArrayList<>();
			alFacturas.add(factura);
			clienteSelect.setFacturaList(alFacturas);
			factura.setCliente(clienteSelect);
			factura.setNumero(String.valueOf(serieFacturaSelect.getNumeroActual()+1));
			factura.setMoneda(this.moneda);
			factura.setRfc(clienteSelect.getCteRfc());
			factura.setNombreCliente(clienteSelect.getCteNombre());
			factura.setFecha(this.fechaFactura);
			factura.setObservacion(this.Obervaciones);
			factura.setSubtotal(subtotal);
			factura.setIva(bdIva);
			factura.setTotal(total);
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
			factura.setNumeroCliente(clienteSelect.getNumeroCte());
			factura.setValorDeclarado(BigDecimal.ZERO);
			factura.setInicioServicios(this.fechaFactura);
			factura.setFinServicios(this.fechaFactura);
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
			MetodoPago metodoP = metodoPagoDAO.buscarPorMetodoPago(metodoPagoSelect);
			factura.setMetodoPago(metodoP.getCdMetodoPago());
			factura.setTipoPersona(clienteSelect.getTipoPersona());
			factura.setCdRegimen(clienteSelect.getRegimenFiscal().getCd_regimen());
			factura.setCdUsoCfdi(clienteSelect.getUsoCfdi().getCdUsoCfdi());
			factura.setUuid(null);
			factura.setEmisorNombre(plantaSelect.getIdEmisoresCFDIS().getNb_emisor());
			factura.setEmisorRFC(plantaSelect.getIdEmisoresCFDIS().getNb_rfc());
			factura.setEmisorCdRegimen(plantaSelect.getIdEmisoresCFDIS().getCd_regimen().getCd_regimen());
			factura.setServicioFacturaList(alServiciosDetalle);
			for (ServicioFactura sef : alServiciosDetalle) {
				sef.setFactura(factura);
			}
			facturaDAO.guardar(factura);

			severity = FacesMessage.SEVERITY_INFO;
			message = "La factura se guardo correctamente";
		} catch (InventarioException ex) {
			log.error("Problema para obtener la información de los productos...", ex);
			message = ex.getMessage();
			severity = FacesMessage.SEVERITY_ERROR;
		} catch (Exception ex) {
			log.error("Problema para obtener los servicios del cliente.", ex);
			ex.printStackTrace();
			message = "Problema con la información de servicios.";
			severity = FacesMessage.SEVERITY_ERROR;
		} finally {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, "Factura guardada", message));
			PrimeFaces.current().ajax().update("form:messages", "form:dt-facturacionServicios");
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
		String filename = "Factura " + fechaFactura + ".pdf";
		String images = "/images/logo.jpeg";
		String message = null;
		Severity severity = null;
		Factura sf = null;
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
			log.info(reportFile.getPath());
			sf = new Factura();
			Integer num = factura.getId();
			connection = EntityManagerUtil.getConnection();
			parameters.put("REPORT_CONNECTION", connection);
			parameters.put("idFactura", num);
			parameters.put("imagen", imgfile.getPath());
			log.info("Parametros: " + parameters.toString());
			jasperReportUtil.createPdf(filename, parameters, reportFile.getPath());
		} catch (Exception ex) {
			ex.fillInStackTrace();
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
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
	}

	public void timbrado() {
		CFDIInfo cfdi = new CFDIInfo();
		CfdiBL cfdiBL = new CfdiBL();
		try {
			// Datos de emisor
			IssuerBindingModel is = new IssuerBindingModel();
			is.setName(plantaSelect.getIdEmisoresCFDIS().getNb_emisor());
			is.setFiscalRegime(plantaSelect.getIdEmisoresCFDIS().getCd_regimen().getCd_regimen());
			is.setRfc(plantaSelect.getIdEmisoresCFDIS().getNb_rfc());
			cfdi.setIssuer(is);

			// Datos de receptor
			ReceiverBindingModel receptor = new ReceiverBindingModel();
			receptor.setRfc(factura.getRfc());
			receptor.setCfdiUse(factura.getCdUsoCfdi());
			receptor.setName(factura.getNombreCliente());
			receptor.setFiscalRegime(factura.getCdRegimen());
			receptor.setTaxZipCode(factura.getCp());
			cfdi.setReceiver(receptor);
			// Datos generales de la factura
			cfdi.setDate(factura.getFecha());
			cfdi.setFolio(factura.getNomSerie());
			cfdi.setSerie(factura.getNumero());
			//TODO
			//cfdi.setPaymentForm(medioPagoSelect.getFormaPago());
			cfdi.setCurrency(moneda);
			cfdi.setCfdiType("I");
			cfdi.setExpeditionPlace(plantaSelect.getCodigopostal().toString());
			cfdi.setPaymentMethod(factura.getMetodoPago());
			cfdi.setObservations(Obervaciones);

			// Productos/Servicios a facturar
			List<ItemFullBindingModel> listaItems = new ArrayList<ItemFullBindingModel>();
			for (ServicioFactura sf : alServiciosDetalle) {
				ItemFullBindingModel item = new ItemFullBindingModel();
				item.setProductCode(sf.getCodigo());
				item.setDescription(sf.getDescripcion());
				item.setUnitCode(sf.getCdUnidad());
				ClaveUnidad claveUnidad = claveDAO.buscarPorId(sf.getCodigo());
				// item.setUnit(claveUnidad.getNombre());
				item.setQuantity(sf.getCantidad());
				item.setUnitPrice(sf.getTarifa().setScale(2, BigDecimal.ROUND_HALF_UP));
				item.setSubtotal(sf.getCosto()); // importe
				item.setTaxObject("02");

				Tax tx = new Tax();
				tx.setBase(sf.getCosto());
				BigDecimal ivaServicio = sf.getCosto().multiply(bdIva);
				tx.setTotal(ivaServicio);
				tx.setName("IVA");
				tx.setRate(tasaIva);
				tx.setIsRetention(false);
				item.setTaxes(new ArrayList<Tax>());
				item.setTotal(sf.getCosto().add(ivaServicio));
				item.getTaxes().add(tx);
				listaItems.add(item);
			}
			cfdi.setItems(listaItems);
			CfdiInfoModel registra = cfdiBL.registra(cfdi);
			factura.setUuid(registra.getId());
			facturaDAO.actualizar(factura);
		} catch (FacturamaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

	/* Getters y Setters */
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

	public String getMetodoPagoSelect() {
		return metodoPagoSelect;
	}

	public void setMetodoPagoSelect(String metodoPagoSelect) {
		this.metodoPagoSelect = metodoPagoSelect;
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

	public Integer getIdFactura() {
		return idFactura;
	}

	public void setIdFactura(Integer idFactura) {
		this.idFactura = idFactura;
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

}
