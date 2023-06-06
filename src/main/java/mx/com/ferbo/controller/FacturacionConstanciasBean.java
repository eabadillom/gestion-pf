package mx.com.ferbo.controller;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.primefaces.PrimeFaces;

import mx.com.ferbo.dao.AsentamientoHumandoDAO;
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
import mx.com.ferbo.dao.SerieFacturaDAO;
import mx.com.ferbo.dao.StatusFacturaDAO;
import mx.com.ferbo.dao.TipoFacturacionDAO;
import mx.com.ferbo.model.AsentamientoHumano;
import mx.com.ferbo.model.AsentamientoHumanoPK;
import mx.com.ferbo.model.Aviso;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.ClienteDomicilios;
import mx.com.ferbo.model.ConstanciaFactura;
import mx.com.ferbo.model.ConstanciaFacturaDs;
import mx.com.ferbo.model.Domicilios;
import mx.com.ferbo.model.Factura;
import mx.com.ferbo.model.FacturaMedioPago;
import mx.com.ferbo.model.FacturaMedioPagoPK;
import mx.com.ferbo.model.MedioPago;
import mx.com.ferbo.model.MetodoPago;
import mx.com.ferbo.model.Parametro;
import mx.com.ferbo.model.Planta;
import mx.com.ferbo.model.SerieFactura;
import mx.com.ferbo.model.StatusFactura;
import mx.com.ferbo.model.TipoFacturacion;
import mx.com.ferbo.util.DateUtil;
import mx.com.ferbo.util.EntityManagerUtil;


@Named
@SessionScoped
public class FacturacionConstanciasBean implements Serializable{
	
	private static final long serialVersionUID = -1785488265380235016L;
	
	private static Logger log = Logger.getLogger(FacturacionConstanciasBean.class);
	
	private Cliente clienteSelect;
	private Domicilios domicilioSelect;
	private Planta plantaSelect;
	private SerieFactura serieFacturaSelect;
	private MetodoPago metodoPagoSelect;
	private MedioPago medioPagoSelect;
	private Parametro iva,retencion;
	private Factura factura;
	
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
	
	
	private List<Cliente> listaCliente;
	private List<ClienteDomicilios> listaClienteDom;//recupera datos de la tabla cliente-domicilio
	private List<ClienteDomicilios> listaClienteDomicilio;
	private List<Planta> listaPlanta;
	private List<SerieFactura> listaSerieF;//recupera todos los registros de serie factura
	private List<SerieFactura> listaSerieFactura;
	private List<Aviso> listaA;
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
	
	private String moneda, observacion;
	private String referencia;
	private int plazoSelect;
	private BigDecimal resIva;
	private BigDecimal resRetencion, valorDeclarado;
	
    private FacesContext faceContext;
    private HttpServletRequest request;
    private HttpSession session;
	
	
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
		
		listaCliente = new ArrayList<>();
		listaClienteDom = new ArrayList<>();
		listaClienteDomicilio = new ArrayList<>();
		listaPlanta = new ArrayList<>();
		listaSerieF = new ArrayList<>();
		listaSerieFactura = new ArrayList<>();
		listaA = new ArrayList<>();
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
		moneda = "MX$";

		
		
	}
	
	@PostConstruct
	public void init(){
		
		listaCliente = clienteDAO.buscarTodos();
		listaClienteDom = clienteDomicilioDAO.buscarTodos();
		listaPlanta = plantaDAO.findall();
		listaSerieF = serieFacturaDAO.findAll();
		listaA = avisoDAO.buscarTodos();
		listaMetodoPago = metodoPagoDAO.buscarTodos();
		listaMedioPago = medioPagoDAO.buscarTodos();
		
		fechaCorte = new Date();
		fechaFactura = new Date();
		
		log.info("Entrando al init");
		
		faceContext = FacesContext.getCurrentInstance();
        request = (HttpServletRequest) faceContext.getExternalContext().getRequest();
        session = request.getSession(false);
        
        //variables a inicializar en null al regreso de la pantalla

        selectedEntradas = new ArrayList<>();
		selectedVigencias = new ArrayList<>();
		selectedServicios = new ArrayList<>();
		
		if((clienteSelect!=null)&&(plantaSelect!=null)) {
			facturacionEntradas();
			facturacionServicios();
			facturacionVigencias();
			
			PrimeFaces.current().ajax().update("form:dt-constanciasE","form:dt-vigencias","form:dt-servicios");
		}
		
		
		
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

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
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

	public void domicilioAvisoPorCliente() {
		
		clienteSelect = clienteDAO.buscarPorId(clienteSelect.getCteCve(), false);
		
		iva = parametroDAO.buscarPorNombre("IVA");//
		resIva = new BigDecimal(iva.getValor()); 
		resIva = resIva.multiply(new BigDecimal(100));//necesario ya que manda error al multiplicar desde el fron-end+++
		retencion = parametroDAO.buscarPorNombre("RETENCION");//***
		resRetencion = new BigDecimal(retencion.getValor());
		resRetencion = resRetencion.multiply(new BigDecimal(100));//+++
		//Domicilio
		listaClienteDomicilio.clear();
		listaClienteDomicilio = listaClienteDom.stream()
								.filter(cd -> clienteSelect != null
								?(cd.getCteCve().getCteCve().intValue()==clienteSelect.getCteCve().intValue())
								:false)
								.collect(Collectors.toList());
		if(listaClienteDomicilio.size()>0) {
			domicilioSelect = listaClienteDomicilio.get(0).getDomicilios();
			
		}
		
		//llenado de select plazo de pago
		AvisoCliente();
		
		//carga de constancias si existe un cambio de cliente
		cargarConstancias();
	}
	
	public void AvisoCliente() {
		
		// -------------- comienza recuperado de Aviso ----------------
		
				listaAviso.clear();
				listaAviso = listaA.stream()
							 .filter(av -> clienteSelect != null
							 ?(av.getCteCve().getCteCve().intValue()==clienteSelect.getCteCve().intValue())
							 :false).collect(Collectors.toList());
				
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
		listaSerieFactura = listaSerieF.stream()
							.filter(s -> plantaSelect != null
							?(s.getIdPlanta().getPlantaCve().intValue()==plantaSelect.getPlantaCve().intValue())
							:false)
							.collect(Collectors.toList());
		
		if(listaSerieFactura.size()>0) {
			serieFacturaSelect = listaSerieFactura.get(0);
		}
		
		//Carga de constancias si existe cambio en planta 
		cargarConstancias();
	}

	public void cargarConstancias(){
		
		if(clienteSelect == null){
			return;
		}
		
		if(plantaSelect == null){
			return;
		}
		
		this.facturacionEntradas();
		this.facturacionVigencias();
		this.facturacionServicios();
		
		PrimeFaces.current().ajax().update("form:dt-constanciasE","form:dt-vigencias","form:dt-servicios");
		
	}
	
	public void facturacionEntradas(){
		
		EntityManager em = EntityManagerUtil.getEntityManager();
		EntityTransaction trans = em.getTransaction();
		trans.begin();
		facturacionConstanciasDAO.setEm(em);
		
		listaEntradas = facturacionConstanciasDAO.buscarNoFacturados(clienteSelect.getCteCve(), plantaSelect.getPlantaCve());		
		
		if(listaEntradas.isEmpty()){
			listaEntradas = new ArrayList<>();
		}
		
		em.close();
		
	}
	
	public void facturacionVigencias(){
		
		EntityManager em = EntityManagerUtil.getEntityManager();
		EntityTransaction t = em.getTransaction();
		t.begin();
		facturacionVigenciasDAO.setEm(em);
		
		System.out.println(fechaFactura);
		DateUtil.setTime(fechaCorte, 0, 0, 0, 0);
		
		listaVigencias = facturacionVigenciasDAO.buscarNoFacturados(clienteSelect.getCteCve(), fechaCorte, plantaSelect.getPlantaCve());
		
		if(listaVigencias.isEmpty()){
			listaVigencias = new ArrayList<>();
		}
		
		em.close();
		
	}
	
	public void facturacionServicios(){
		
		EntityManager em = EntityManagerUtil.getEntityManager();
		EntityTransaction trans = em.getTransaction();
		trans.begin();
		facturacionServiciosDAO.setEm(em);
		
		listaServicios = facturacionServiciosDAO.buscarNoFacturados(clienteSelect.getCteCve());
		
		
		if(listaServicios.isEmpty()) {
			listaServicios = new ArrayList<>();
		}
		
		em.close();//agregado 
		
	}
	
	public String paginaCalculoPrevio() throws IOException {
		
		if(clienteSelect!=null & plantaSelect!=null) {
			return "calculoPrevio.xhtml?faces-redirect=true";
		} 
		
		return "";
		
	}
	
	public String inyeccionBean(){
		clienteSelect = clienteDAO.buscarPorId(clienteSelect.getCteCve(), true);
		factura = new Factura();
		
		factura.setCliente(clienteSelect);
		factura.setNumero(String.valueOf(serieFacturaSelect.getNumeroActual() + 1));
		factura.setMoneda(moneda);
		factura.setRfc(clienteSelect.getCteRfc());
		factura.setNombreCliente(clienteSelect.getCteNombre());
		factura.setFecha(fechaCorte);
		factura.setObservacion(observacion);
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
		//factura.setFax(null);//duda*
		factura.setPorcentajeIva(resIva);
		factura.setNumeroCliente(clienteSelect.getNumeroCte());
		factura.setValorDeclarado(valorDeclarado);
		factura.setInicioServicios(fechaCorte);
		factura.setFinServicios(fechaCorte);
		factura.setMontoLetra(null);//duda*
		//verificar- ------------
		factura.setFacturaMedioPagoList(new ArrayList<FacturaMedioPago>());
		FacturaMedioPagoPK facturaPK = new FacturaMedioPagoPK();
		facturaPK.setFacturaId(factura);
		facturaPK.setFmpId(0);
		FacturaMedioPago fmp = new FacturaMedioPago();
		fmp.setFacturaMedioPagoPK(facturaPK);
		MedioPago medioP = medioPagoDAO.buscarPorFormaPago(medioPagoSelect.getFormaPago());
		fmp.setMpId(medioP);
		fmp.setFactura(factura);
		fmp.setFmpPorcentaje(100);
		fmp.setMpDescripcion(medioP.getMpDescripcion());
		fmp.setFmpReferencia(referencia);
		factura.getFacturaMedioPagoList().add(fmp);
		//---------------------
		StatusFactura statusF = statusFacturaDAO.buscarPorId(1);
		
		factura.setStatus(statusF);//duda*			
		
		TipoFacturacion tipoFacturacion = tipoFacturacionDAO.buscarPorId(1);
		
		factura.setTipoFacturacion(tipoFacturacion);//1
		factura.setPlanta(plantaSelect);
		factura.setPlazo(plazoSelect);
		factura.setRetencion(BigDecimal.ZERO);
		factura.setNomSerie(serieFacturaSelect.getNomSerie());//duda*nombre de serie_factura ya tengo un objeto serieFactura
		factura.setMetodoPago(metodoPagoSelect.getCdMetodoPago());
		factura.setTipoPersona(clienteSelect.getTipoPersona());
		factura.setCdRegimen(clienteSelect.getRegimenFiscal().getCd_regimen());
		factura.setCdUsoCfdi(clienteSelect.getUsoCfdi().getCdUsoCfdi());
		factura.setUuid(null);//se coloca al timbrar
		factura.setEmisorNombre(plantaSelect.getIdEmisoresCFDIS().getNb_emisor());
		factura.setEmisorRFC(plantaSelect.getIdEmisoresCFDIS().getNb_rfc());
		factura.setEmisorCdRegimen(plantaSelect.getIdEmisoresCFDIS().getCd_regimen().getCd_regimen());
		
		
		try {
			
			session.setAttribute("entradas", selectedEntradas);
			session.setAttribute("vigencias",selectedVigencias);
			session.setAttribute("servicios", selectedServicios);
			session.setAttribute("cliente", clienteSelect);
			session.setAttribute("plantaSelect", plantaSelect);
			session.setAttribute("factura", factura);
			session.setAttribute("fechaEmision", fechaCorte);
			session.setAttribute("iva", iva);
			session.setAttribute("medioPago", medioPagoSelect);
			session.setAttribute("metodoPago", metodoPagoSelect);
			session.setAttribute("domicilioSelect",domicilioSelect);
			session.setAttribute("serieFacturaSelect",serieFacturaSelect);
			
			
		}catch(Exception e) {
			System.out.println("ERROR:" + e.getMessage());
		}
		
		return "calculoPrevio.xhtml?faces-redirect=true";
		
	}
	
	
}
