package mx.com.ferbo.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import mx.com.ferbo.dao.AvisoDAO;
import mx.com.ferbo.dao.ClienteDAO;
import mx.com.ferbo.dao.ClienteDomiciliosDAO;
import mx.com.ferbo.dao.MedioPagoDAO;
import mx.com.ferbo.dao.MetodoPagoDAO;
import mx.com.ferbo.dao.PlantaDAO;
import mx.com.ferbo.dao.SerieFacturaDAO;
import mx.com.ferbo.model.Aviso;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.ClienteDomicilios;
import mx.com.ferbo.model.Domicilios;
import mx.com.ferbo.model.MedioPago;
import mx.com.ferbo.model.MetodoPago;
import mx.com.ferbo.model.Planta;
import mx.com.ferbo.model.SerieFactura;


@Named
@ViewScoped
public class FacturacionConstanciasBean implements Serializable{
	
	private static final long serialVersionUID = -1785488265380235016L;
	
	private Cliente clienteSelect;
	private Domicilios domicilioSelect;
	private Planta plantaSelect;
	private SerieFactura serieFacturaSelect;
	private MetodoPago metodoPagoSelect;
	private MedioPago medioPagoSelect;
	
	private ClienteDAO clienteDAO;
	private ClienteDomiciliosDAO clienteDomicilioDAO;
	private PlantaDAO plantaDAO;
	private SerieFacturaDAO serieFacturaDAO;
	private AvisoDAO avisoDAO;
	private MetodoPagoDAO metodoPagoDAO;
	private MedioPagoDAO medioPagoDAO;	
	
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
	
	private Date fechaFactura;
	
	private String moneda = "MX$";
	private int plazoSelect;
	
	
	public FacturacionConstanciasBean() {
		
		clienteDAO = new ClienteDAO();
		clienteDomicilioDAO = new ClienteDomiciliosDAO();
		plantaDAO = new PlantaDAO();
		serieFacturaDAO = new SerieFacturaDAO();
		avisoDAO = new AvisoDAO();
		metodoPagoDAO = new MetodoPagoDAO();
		medioPagoDAO = new MedioPagoDAO();
		
		
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
		
		fechaFactura = new Date();
		
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

	public void domicilioAvisoPorCliente() {
		
		
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
			aviso = listaAviso.get(0);//error en elegir empresa s. a. viene vacia la lista aviso
			this.plazoSelect = aviso.getAvisoPlazo();
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
		
	}
	
	/*public void avisoCliente() {
		
		listaAviso.clear();
		listaAviso = listaA.stream()
					 .filter(av -> clienteSelect != null
					 ?(av.getCteCve().getCteCve().intValue()==clienteSelect.getCteCve().intValue())
					 :false).collect(Collectors.toList());
		
	}*/
	
	

}
