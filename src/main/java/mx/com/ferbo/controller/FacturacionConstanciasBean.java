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
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
import mx.com.ferbo.dao.SerieFacturaDAO;
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
import mx.com.ferbo.model.Planta;
import mx.com.ferbo.model.SerieFactura;
import mx.com.ferbo.util.DateUtil;


@Named
@SessionScoped
public class FacturacionConstanciasBean implements Serializable{
	
	private static final long serialVersionUID = -1785488265380235016L;
	
	private Cliente clienteSelect;
	private Domicilios domicilioSelect;
	private Planta plantaSelect;
	private SerieFactura serieFacturaSelect;
	private MetodoPago metodoPagoSelect;
	private MedioPago medioPagoSelect;
	private Parametro iva,retencion;
	
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
	private List<ConstanciaDeDeposito> listaEntradas;
	private List<ConstanciaFactura> listaVigencias;
	private List<ConstanciaFacturaDs> listaServicios;
	
	private List<ConstanciaDeDeposito> selectedEntradas;
	private List<ConstanciaFactura> selectedVigencias;
	private List<ConstanciaFacturaDs> selectedServicios;
	
	private Date fechaFactura;
	private Date fechaCorte;
	
	private String moneda = "MX$";
	private int plazoSelect;
	private BigDecimal resIva = new BigDecimal(0);
	private BigDecimal resRetencion = new BigDecimal(0);
	
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
		
	}
	
	@PostConstruct
	public void init() {
		
		listaCliente = clienteDAO.buscarTodos();
		listaClienteDom = clienteDomicilioDAO.buscarTodos();
		listaPlanta = plantaDAO.findall();
		listaSerieF = serieFacturaDAO.findAll();
		listaA = avisoDAO.buscarTodos();
		listaMetodoPago = metodoPagoDAO.buscarTodos();
		listaMedioPago = medioPagoDAO.buscarTodos();
		
		
		//iva = parametroDAO.buscarPorNombre("IVA");
		//retencion = parametroDAO.buscarPorNombre("RETENCION");
		
		fechaFactura = new Date();
		fechaCorte = new Date();
		
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

	public List<ConstanciaDeDeposito> getListaEntradas() {
		return listaEntradas;
	}

	public void setListaEntradas(List<ConstanciaDeDeposito> listaEntradas) {
		this.listaEntradas = listaEntradas;
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

	public List<ConstanciaDeDeposito> getSelectedEntradas() {
		return selectedEntradas;
	}

	public void setSelectedEntradas(List<ConstanciaDeDeposito> selectedEntradas) {
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

	public void domicilioAvisoPorCliente() {
		
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
		this.facturacionEntradas();
		this.facturacionVigencias();
		this.facturacionServicios();
		
		PrimeFaces.current().ajax().update("form:dt-constanciasE","form:dt-vigencias","form:dt-servicios");
		
	}
	
	public void facturacionEntradas(){
	
		if(clienteSelect == null){
			return;
		}
		
		if(plantaSelect == null){
			return;
		}
		
		listaEntradas = facturacionConstanciasDAO.buscarNoFacturados(clienteSelect.getCteCve(), plantaSelect.getPlantaCve());		
		
		if(listaEntradas.isEmpty()){
			listaEntradas = new ArrayList<>();
		}
		
	}
	
	public void facturacionVigencias(){
		
		if(clienteSelect == null){
			return;			
		}
		
		if(plantaSelect == null){
			return;			
		}
		
		System.out.println("fecha corte antes:" + fechaCorte);
		
		DateUtil.setTime(fechaCorte, 0, 0, 0, 0);
		
		System.out.println("fecha corte despues:" + fechaCorte);
		
		listaVigencias = facturacionVigenciasDAO.buscarNoFacturados(clienteSelect.getCteCve(), fechaCorte, plantaSelect.getPlantaCve());
		
		if(listaVigencias.isEmpty()){
			listaVigencias = new ArrayList<>();
		}
		
	}
	
	public void facturacionServicios(){
		
		if(clienteSelect == null) {
			return;
		}
		
		if(plantaSelect == null) {
			return;
		}
		
		listaServicios = facturacionServiciosDAO.buscarNoFacturados(clienteSelect.getCteCve());
		
		
	}
	
	public String paginaCalculoPrevio() throws IOException {
		
		if(clienteSelect!=null & plantaSelect!=null) {
			return "calculoPrevio.xhtml?faces-redirect=true";
		} 
		
		return "";
		
	}
	
	public String inyeccionBean(){
		
		
		try {
			
			faceContext = FacesContext.getCurrentInstance();
            request = (HttpServletRequest) faceContext.getExternalContext().getRequest();
            session = request.getSession(true);
			session.setAttribute("entradas", selectedEntradas);
			session.setAttribute("vigencias", selectedVigencias);
			
		}catch(Exception e) {
			System.out.println("ERROR:" + e.getMessage());
		}
		
		return "calculoPrevio.xhtml?faces-redirect=true";
		
	}
	
	
	// funcion para comprobacion
	/*public void verVigencias() {
		System.out.println("entradas" + selectedEntradas.get(0));
		System.out.println("vigencias" + selectedVigencias.get(0));
		System.out.println("servicios" + selectedServicios.get(0));
		//"index?faces-redirect=true"
		//calculoPrevio.xhtml?faces-redirect=true
	}*/
	

}
