package mx.com.ferbo.controller;

import java.io.Serializable;
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
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.apache.log4j.Logger;
import org.primefaces.PrimeFaces;

import mx.com.ferbo.dao.AvisoDAO;
import mx.com.ferbo.dao.ClienteDAO;
import mx.com.ferbo.dao.ClienteDomiciliosDAO;
import mx.com.ferbo.dao.FacturacionDepositosDAO;
import mx.com.ferbo.dao.FacturacionServiciosDAO;
import mx.com.ferbo.dao.FacturacionVigenciasDAO;
import mx.com.ferbo.dao.MedioPagoDAO;
import mx.com.ferbo.dao.MetodoPagoDAO;
import mx.com.ferbo.dao.ParametroDAO;
import mx.com.ferbo.dao.PlantaDAO;
import mx.com.ferbo.model.Aviso;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.ClienteDomicilios;
import mx.com.ferbo.model.ConstanciaDeDeposito;
import mx.com.ferbo.model.ConstanciaFactura;
import mx.com.ferbo.model.ConstanciaFacturaDs;
import mx.com.ferbo.model.Domicilios;
import mx.com.ferbo.model.MedioPago;
import mx.com.ferbo.model.MetodoPago;
import mx.com.ferbo.model.Parametro;
import mx.com.ferbo.model.PartidaServicio;
import mx.com.ferbo.model.Planta;
import mx.com.ferbo.model.PrecioServicio;
import mx.com.ferbo.model.ProductoPorCliente;
import mx.com.ferbo.model.SerieFactura;
import mx.com.ferbo.model.UnidadDeManejo;
import mx.com.ferbo.util.DateUtil;
import mx.com.ferbo.util.EntityManagerUtil;

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

	private Cliente clienteSelect;
	private Domicilios domicilioSelect;
	private Planta plantaSelect;
	private MetodoPago metodoPagoSelect;
	private MedioPago medioPagoSelect;
	private Parametro iva, retencion;

	private ClienteDAO clienteDAO;
	private ClienteDomiciliosDAO clienteDomicilioDAO;
	private PlantaDAO plantaDAO;
	private AvisoDAO avisoDAO;
	private MetodoPagoDAO metodoPagoDAO;
	private MedioPagoDAO medioPagoDAO;
	private ParametroDAO parametroDAO;
	private FacturacionDepositosDAO facturacionConstanciasDAO;
	private FacturacionVigenciasDAO facturacionVigenciasDAO;
	private FacturacionServiciosDAO facturacionServiciosDAO;

	private Date fechaFactura;
	private Date fechaCorte;
	private Date fecha;
	private String moneda = "MX$";
	private int plazoSelect;
	private Integer idCliente;

	public FacturaServiciosBean() {
		clientes = new ArrayList<Cliente>();
		alServicios = new ArrayList<PrecioServicio>();
		listaClienteDom = new ArrayList<ClienteDomicilios>();
		listaClienteDomicilio = new ArrayList<ClienteDomicilios>();
		listaPlanta = new ArrayList<Planta>();
		listaSerieF = new ArrayList<SerieFactura>();
		listaA = new ArrayList<Aviso>();
		listaAviso = new ArrayList<Aviso>();
		listaMetodoPago = new ArrayList<MetodoPago>();
		listaEntradas = new ArrayList<>();
		listaVigencias = new ArrayList<>();
		listaServicios = new ArrayList<>();
		clienteSelect = new Cliente();
		clienteDAO = new ClienteDAO();
		clienteDomicilioDAO = new ClienteDomiciliosDAO();
		plantaDAO = new PlantaDAO();
		avisoDAO = new AvisoDAO();
		metodoPagoDAO = new MetodoPagoDAO();
		medioPagoDAO = new MedioPagoDAO();
		parametroDAO = new ParametroDAO();

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

	}

	public void domicilioAvisoPorCliente() {
		iva = parametroDAO.buscarPorNombre("IVA");//
		retencion = parametroDAO.buscarPorNombre("RETENCION");
		
		listaClienteDomicilio.clear();
		listaClienteDomicilio = listaClienteDom.stream()
				.filter(cd -> clienteSelect != null
						? (cd.getCteCve().getCteCve().intValue()==clienteSelect.getCteCve().intValue())
						:false)
				.collect(Collectors.toList());
		if (listaClienteDomicilio.size() > 0) {
			domicilioSelect = listaClienteDomicilio.get(0).getDomicilios();
		}
		AvisoCliente();
		constancias();
	}

	public void constancias() {
		this.facturacionEntradas();
		this.facturacionVigencias();
		this.facturacionServicios();

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

		System.out.println("fecha corte antes:" + fechaCorte);
		DateUtil.setTime(fechaCorte, 0, 0, 0, 0);
		System.out.println("fecha corte despues:" + fechaCorte);
		listaVigencias = facturacionVigenciasDAO.buscarNoFacturados(clienteSelect.getCteCve(), fechaCorte,
				plantaSelect.getPlantaCve());

		if (listaVigencias.isEmpty()) {
			listaVigencias = new ArrayList<>();
		}

	}

	public void facturacionServicios() {

		if (clienteSelect == null) {
			return;
		}

		if (plantaSelect == null) {
			return;
		}

		listaServicios = facturacionServiciosDAO.buscarNoFacturados(clienteSelect.getCteCve());

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
	
}
