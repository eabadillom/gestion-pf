package mx.com.ferbo.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import mx.com.ferbo.dao.ClienteDAO;
import mx.com.ferbo.dao.ClienteDomiciliosDAO;
import mx.com.ferbo.dao.PlantaDAO;
import mx.com.ferbo.dao.SerieFacturaDAO;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.ClienteDomicilios;
import mx.com.ferbo.model.Domicilios;
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
	
	private ClienteDAO clienteDAO;
	private ClienteDomiciliosDAO clienteDomicilioDAO;
	private PlantaDAO plantaDAO;
	private SerieFacturaDAO serieFacturaDAO;
	
	private List<Cliente> listaCliente;
	private List<ClienteDomicilios> listaClienteDom;//recupera datos de la tabla cliente-domicilio
	private List<ClienteDomicilios> listaClienteDomicilio;
	private List<Planta> listaPlanta;
	private List<SerieFactura> listaSerieF;//recupera todos los registros de serie factura
	private List<SerieFactura> listaSerieFactura;
	
	
	public FacturacionConstanciasBean() {
		
		clienteDAO = new ClienteDAO();
		clienteDomicilioDAO = new ClienteDomiciliosDAO();
		plantaDAO = new PlantaDAO();
		serieFacturaDAO = new SerieFacturaDAO();
		
		listaCliente = new ArrayList<>();
		listaClienteDom = new ArrayList<>();
		listaClienteDomicilio = new ArrayList<>();
		listaPlanta = new ArrayList<>();
		listaSerieF = new ArrayList<>();
		listaSerieFactura = new ArrayList<>();
		
	}
	
	@PostConstruct
	public void init() {
		
		listaCliente = clienteDAO.buscarTodos();
		listaClienteDom = clienteDomicilioDAO.buscarTodos();
		listaPlanta = plantaDAO.findall();
		listaSerieF = serieFacturaDAO.findAll();
		
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

	public void domicilioCliente() {
		
		listaClienteDomicilio.clear();
		listaClienteDomicilio = listaClienteDom.stream()
								.filter(cd -> clienteSelect != null
								?(cd.getCteCve().getCteCve().intValue()==clienteSelect.getCteCve().intValue())
								:false)
								.collect(Collectors.toList());
		if(listaClienteDomicilio.size()>0) {
			domicilioSelect = listaClienteDomicilio.get(0).getDomicilios();
			
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
		
	}
	
	
	

}
