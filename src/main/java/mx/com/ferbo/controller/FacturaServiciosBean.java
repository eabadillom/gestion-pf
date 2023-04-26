package mx.com.ferbo.controller;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.primefaces.PrimeFaces;

import mx.com.ferbo.dao.AsentamientoHumandoDAO;
import mx.com.ferbo.dao.AvisoDAO;
import mx.com.ferbo.dao.ClienteDAO;
import mx.com.ferbo.dao.ClienteDomiciliosDAO;
import mx.com.ferbo.dao.FacturaDAO;
import mx.com.ferbo.dao.FacturacionDepositosDAO;
import mx.com.ferbo.dao.FacturacionServiciosDAO;
import mx.com.ferbo.dao.FacturacionVigenciasDAO;
import mx.com.ferbo.dao.MedioPagoDAO;
import mx.com.ferbo.dao.MetodoPagoDAO;
import mx.com.ferbo.dao.ParametroDAO;
import mx.com.ferbo.dao.PlantaDAO;
import mx.com.ferbo.dao.SerieFacturaDAO;
import mx.com.ferbo.dao.StatusFacturaDAO;
import mx.com.ferbo.dao.TipoFacturacionDAO;
import mx.com.ferbo.model.AsentamientoHumano;
import mx.com.ferbo.model.Aviso;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.ClienteDomicilios;
import mx.com.ferbo.model.ConstanciaDeDeposito;
import mx.com.ferbo.model.ConstanciaFactura;
import mx.com.ferbo.model.ConstanciaFacturaDs;
import mx.com.ferbo.model.ConstanciaServicioDetalle;
import mx.com.ferbo.model.Domicilios;
import mx.com.ferbo.model.EmisoresCFDIS;
import mx.com.ferbo.model.Factura;
import mx.com.ferbo.model.MedioPago;
import mx.com.ferbo.model.MetodoPago;
import mx.com.ferbo.model.Parametro;
import mx.com.ferbo.model.PartidaServicio;
import mx.com.ferbo.model.Planta;
import mx.com.ferbo.model.PrecioServicio;
import mx.com.ferbo.model.ProductoPorCliente;
import mx.com.ferbo.model.SerieFactura;
import mx.com.ferbo.model.ServicioFactura;
import mx.com.ferbo.model.StatusFactura;
import mx.com.ferbo.model.TipoFacturacion;
import mx.com.ferbo.model.UnidadDeManejo;
import mx.com.ferbo.util.DateUtil;
import mx.com.ferbo.util.EntityManagerUtil;
import mx.com.ferbo.util.InventarioException;

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

	private Cliente clienteSelect;
	private Domicilios domicilioSelect;
	private Planta plantaSelect;
	private MetodoPago metodoPagoSelect;
	private MedioPago medioPagoSelect;
	private Parametro iva, retencion;
	private SerieFactura serieFacturaSelect;
	private ServicioFactura selServicio;
	private BigDecimal subtotal;
	private BigDecimal bdIva;
	private BigDecimal total;
	private BigDecimal tasaIva;

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

	private Date fechaFactura;
	private Date fechaCorte;
	private Date fecha;
	private String moneda = "MX$";
	private int plazoSelect;
	private Integer idCliente;
	private Integer idPrecioServicio;
	private BigDecimal cantidadServicio;
	private String Obervaciones;

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
		listaEntradas = new ArrayList<>();
		listaVigencias = new ArrayList<>();
		listaServicios = new ArrayList<>();
		subtotal = new BigDecimal(0);
		bdIva = new BigDecimal(0);
		total  = new BigDecimal(0);
		
		clienteDAO = new ClienteDAO();
		clienteDomicilioDAO = new ClienteDomiciliosDAO();
		plantaDAO = new PlantaDAO();
		avisoDAO = new AvisoDAO();
		metodoPagoDAO = new MetodoPagoDAO();
		medioPagoDAO = new MedioPagoDAO();
		parametroDAO = new ParametroDAO();
		seriefacturaDAO = new SerieFacturaDAO();
		facturacionConstanciasDAO = new FacturacionDepositosDAO();
		clienteSelect = new Cliente();
		asnDAO = new AsentamientoHumandoDAO();
		statusDAO = new StatusFacturaDAO();
		tipoDAO = new TipoFacturacionDAO();
	}

	@PostConstruct
	public void init() {
		fecha = new Date();
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
		iva = parametroDAO.buscarPorNombre("IVA");//
		tasaIva = new BigDecimal(iva.getValor()).setScale(2, BigDecimal.ROUND_HALF_UP);
		retencion = parametroDAO.buscarPorNombre("RETENCION");
		Map<Integer, List<PrecioServicio>> mpPrecioServicio = new HashMap<Integer, List<PrecioServicio>>();
		List<PrecioServicio> precioServicioList = null;
		listaClienteDomicilio.clear();
		listaClienteDomicilio = listaClienteDom.stream()
				.filter(cd -> clienteSelect != null
						? (cd.getCteCve().getCteCve().intValue()==clienteSelect.getCteCve().intValue())
						:false)
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
		message = "Agregue sus servicios.";
		severity = FacesMessage.SEVERITY_INFO;
		AvisoCliente();
		constancias();
	}

	public void AvisoCliente() {
		listaAviso.clear();
		listaAviso = listaA.stream()
				.filter(av -> clienteSelect != null
				? (av.getCteCve().getCteCve().intValue() == clienteSelect.getCteCve().intValue())
						: false)
				.collect(Collectors.toList());
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
							?(s.getIdPlanta().getPlantaCve().intValue()==plantaSelect.getPlantaCve().intValue())
									:false).collect(Collectors.toList());
		if(listaSerieFactura.size()>0) {
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
		PrecioServicio precioServicio = null;
		ServicioFactura servicio = null;

		try {
			if (this.clienteSelect == null )
				throw new InventarioException("Debe seleccionar el cliente");

			if (this.cantidadServicio == null || this.cantidadServicio.compareTo(BigDecimal.ZERO) <= 0)
				throw new InventarioException("Debe indicar la cantidad de servicios.");

			if (this.idPrecioServicio == null)
				throw new InventarioException("Debe seleccionar un servicio.");

			precioServicio = this.alServicios.stream().filter(ps -> this.idPrecioServicio.equals(ps.getId())).collect(Collectors.toList()).get(0);
			if (alServiciosDetalle == null)
				alServiciosDetalle = new ArrayList<ServicioFactura>();

			servicio = new ServicioFactura();
			servicio.setDescripcion(precioServicio.getServicio().getServicioDs());
			servicio.setCantidad(cantidadServicio);
			servicio.setUdCobro(precioServicio.getUnidad().getUnidadDeManejoDs());
			servicio.setUnidad(precioServicio.getUnidad().getUnidadDeManejoDs());
			servicio.setTarifa(precioServicio.getPrecio().setScale(2, BigDecimal.ROUND_HALF_UP));
			servicio.setCosto(cantidadServicio.multiply(precioServicio.getPrecio().setScale(2, BigDecimal.ROUND_HALF_UP)));
			alServiciosDetalle.add(servicio);
			subtotal = subtotal.add(servicio.getCosto());
			subtotal = subtotal.setScale(2, BigDecimal.ROUND_HALF_UP);
			bdIva =bdIva.add(servicio.getCosto().multiply(tasaIva));
			bdIva = bdIva.setScale(2, BigDecimal.ROUND_HALF_UP);
			total = total.add(bdIva.add(subtotal));
			total = total.setScale(2, BigDecimal.ROUND_HALF_UP);
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
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, "Servicio",message));
			PrimeFaces.current().ajax().update("form:messages", "form:dt-facturacionServicios");
		}
	}


	/*
	 * public void filtrarCliente() { String message = null; Severity severity =
	 * null; EntityManager manager = null; Cliente cliente; Map<Integer,
	 * List<PrecioServicio>> mpPrecioServicio = new HashMap<Integer,
	 * List<PrecioServicio>>(); List<PrecioServicio> precioServicioList = null;
	 * clienteSelect.setCteCve(this.idCliente);
	 * 
	 * try { log.info("Entrando a filtrar cliente..."); manager =
	 * EntityManagerUtil.getEntityManager(); cliente =
	 * manager.createNamedQuery("Cliente.findByCteCve",
	 * Cliente.class).setParameter("cteCve", this.idCliente).getSingleResult();
	 * precioServicioList = cliente.getPrecioServicioList(); Integer idAviso = new
	 * Integer(-1); for (PrecioServicio ps : precioServicioList) { Integer avisoCve
	 * = ps.getAvisoCve().getAvisoCve(); if (avisoCve > idAviso) idAviso = new
	 * Integer(avisoCve); List<PrecioServicio> list =
	 * mpPrecioServicio.get(avisoCve); if (list == null) { list = new
	 * ArrayList<PrecioServicio>(); mpPrecioServicio.put(avisoCve, list); }
	 * list.add(ps); } mpPrecioServicio.get(idAviso); alServicios.clear();
	 * alServicios = mpPrecioServicio.get(idAviso); for (PrecioServicio ps :
	 * alServicios) { log.info(ps.getServicio().getServicioDs());
	 * log.info(ps.getUnidad().getUnidadDeManejoDs()); } message =
	 * "Agregue sus servicios."; severity = FacesMessage.SEVERITY_INFO; } catch
	 * (Exception ex) { log.error("Problema para recuperar los datos del cliente.",
	 * ex); message = ex.getMessage(); severity = FacesMessage.SEVERITY_ERROR; }
	 * finally { if (manager != null) manager.close();
	 * FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity,
	 * "Cliente", message)); PrimeFaces.current().ajax().update("form:messages",
	 * "form:receptor"); } log.info("servicios del cliente filtrados.");
	 * 
	 * }
	 */
		public void SaveFactura() {
			String message = null;
			Severity severity = null;
			Factura factura = null;
			Cliente cliente = null;
			ServicioFactura servicio = null;
			Planta planta=  null;
			SerieFactura serieFactura = null;
			ClienteDomicilios domicilioCliente = null;
			EmisoresCFDIS emisor = null;
			List<Factura> listaFactura = null;
			
			try {
				if(this.alServiciosDetalle == null || this.alServiciosDetalle.size() == 0)
					throw new InventarioException("Debe seleccionar almenos un servicio");
				if (this.clienteSelect == null)
					throw new InventarioException("Debe seleccionar el cliente");
				if (this.iva == null)
					throw new InventarioException("Debe indicar la cantidad de servicios.");
				//Datos receptor
				factura.setCliente(cliente);
				factura.setNumero(String.valueOf(serieFacturaSelect.getNumeroActual()+1));
				factura.setMoneda(this.moneda);
				factura.setRfc(cliente.getCteRfc());
				factura.setNombreCliente(cliente.getCteNombre());
				factura.setFecha(this.fechaFactura);
				factura.setObservacion(this.Obervaciones);
				factura.setSubtotal(subtotal);
				factura.setIva(bdIva);
				factura.setTotal(total);
				factura.setPais(domicilioCliente.getDomicilios().getPaisCved().getPaisDesc());
				factura.setEstado(domicilioCliente.getDomicilios().getCiudades().getMunicipios().getEstados().getEstadoDesc());
				factura.setMunicipio(domicilioCliente.getDomicilios().getCiudades().getMunicipios().getMunicipioDs());
				factura.setCiudad(domicilioCliente.getDomicilios().getCiudades().getCiudadDs());
				AsentamientoHumano asentamiento = asnDAO.buscarPorId(domicilioCliente.getDomicilios().getDomicilioColonia());
				factura.setColonia(asentamiento.getAsentamientoDs());
				factura.setCp(domicilioCliente.getDomicilios().getDomicilioCp());
				factura.setCalle(domicilioCliente.getDomicilios().getDomicilioCalle());
				factura.setNumExt(domicilioCliente.getDomicilios().getDomicilioNumExt());
				factura.setNumInt(domicilioCliente.getDomicilios().getDomicilioNumInt());
				factura.setTelefono(domicilioCliente.getDomicilios().getDomicilioTel1());
				factura.setFax(domicilioCliente.getDomicilios().getDomicilioFax());
				factura.setPorcentajeIva(tasaIva.multiply(new BigDecimal(100).setScale(2, BigDecimal.ROUND_HALF_UP)));
				factura.setNumeroCliente(cliente.getNumeroCte());
				factura.setValorDeclarado(BigDecimal.ZERO);
				factura.setInicioServicios(this.fechaFactura);
				factura.setFinServicios(this.fechaFactura);
				//TODO PENDIENTE MONTO CON LETRA
				factura.setMontoLetra(null); 
				StatusFactura status = statusDAO.buscarPorId(1);
				factura.setStatus(status);
				TipoFacturacion tipo = tipoDAO.buscarPorId(5);
				factura.setTipoFacturacion(tipo);
				//datos Emisor
				factura.setPlanta(plantaSelect);//sucursal
				factura.setPlazo(this.plazoSelect);
				factura.setRetencion(BigDecimal.ZERO);
				serieFactura.setNomSerie(serieFacturaSelect.getNomSerie());
				factura.setMetodoPago(this.metodoPagoSelect.getNbMetodoPago());
				//TODO
				factura.setTipoPersona(null);
				factura.setCdRegimen(null);
				factura.setCdUsoCfdi(null);
				factura.setUuid(null);
				factura.setEmisorNombre(plantaSelect.getIdEmisoresCFDIS().getNb_emisor());
				factura.setEmisorRFC(plantaSelect.getIdEmisoresCFDIS().getNb_rfc());
				factura.setCdRegimen(plantaSelect.getIdEmisoresCFDIS().getCd_regimen().getCd_regimen());
				//datos de facturacion
				
				//servicio.setUdCobro(precioServicio.getUnidad().getUnidadDeManejoDs());
				
			}catch (Exception ex) {
				
			}
		}
		
	public void deleteServicio(ServicioFactura servicio) {
		this.alServiciosDetalle.remove(servicio);
		subtotal = subtotal.subtract(servicio.getCosto());
		subtotal = subtotal.setScale(2, BigDecimal.ROUND_HALF_UP);
		bdIva =bdIva.subtract(servicio.getCosto().multiply(tasaIva));
		bdIva = bdIva.setScale(2, BigDecimal.ROUND_HALF_UP);
		total = total.subtract(bdIva.add(subtotal));
		
		PrimeFaces.current().ajax().update("form:subtotal","form:iva","form:total");
	}

	public void reload() throws IOException {
	    ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
	    ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
	}
	
	/*Getters y Setters*/
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
		return metodoPagoSelect;
	}

	public void setMetodoPagoSelect(MetodoPago metodoPagoSelect) {
		this.metodoPagoSelect = metodoPagoSelect;
	}

	public MedioPago getMedioPagoSelect() {
		return medioPagoSelect;
	}

	public void setMedioPagoSelect(MedioPago medioPagoSelect) {
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

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Integer getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(Integer idCliente) {
		this.idCliente = idCliente;
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
	
}
