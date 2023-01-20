package mx.com.ferbo.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import mx.com.ferbo.dao.AvisoDAO;
import mx.com.ferbo.dao.CamaraDAO;
import mx.com.ferbo.dao.ClienteDAO;
import mx.com.ferbo.dao.PlantaDAO;
import mx.com.ferbo.model.Aviso;
import mx.com.ferbo.model.Camara;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.ConstanciaDeDeposito;
import mx.com.ferbo.model.Planta;

@Named
@ViewScoped
public class ConstanciaDeDepositoBean implements Serializable{
	
	private static final long serialVersionUID = -1785488265380235016L;
	
	private ClienteDAO clienteDAO;
	private PlantaDAO plantaDAO;
	private AvisoDAO avisoDAO;
	private CamaraDAO camaraDAO;
	
	private ConstanciaDeDeposito constancia;
	
	private List<Cliente> listadoCliente;
	private List<Planta> listadoPlanta;
	private List<Camara> camaras;
	private List<Camara> camaraPorPlanta;
	private List<Aviso> listadoAviso;
	
	private Planta plantaSelect;
	
	public ConstanciaDeDepositoBean() {
		clienteDAO = new ClienteDAO();
		plantaDAO = new PlantaDAO();
		camaraDAO = new CamaraDAO();
		avisoDAO = new AvisoDAO();
		listadoPlanta = new ArrayList<>();
		camaraPorPlanta = new ArrayList<Camara>();
	}
	
	@PostConstruct
	public void init(){
		listadoCliente = clienteDAO.buscarTodos();		
		listadoPlanta = plantaDAO.findall();
		camaras = camaraDAO.buscarTodos();
		listadoAviso = avisoDAO.buscarTodos();
	}

	//---------- Metodos de listas --------------
	
	public List<Cliente> getListadoCliente() {
		return listadoCliente;
	}

	public void setListadoCliente(List<Cliente> listadoCliente) {
		this.listadoCliente = listadoCliente;
	}
	
	public List<Planta> getListadoPlanta() {
		return listadoPlanta;
	}

	public void setListadoPlanta(List<Planta> listadoPlanta) {
		this.listadoPlanta = listadoPlanta;
	}
	
	
	public List<Aviso> getListadoAviso() {
		return listadoAviso;
	}

	public void setListadoAviso(List<Aviso> listadoAviso) {
		this.listadoAviso = listadoAviso;
	}
	
	public List<Camara> getCamaras() {
		return camaras;
	}

	public void setCamaras(List<Camara> camaras) {
		this.camaras = camaras;
	}

	public List<Camara> getCamaraPorPlanta() {
		return camaraPorPlanta;
	}

	public void setCamaraPorPlanta(List<Camara> camaraPorPlanta) {
		this.camaraPorPlanta = camaraPorPlanta;
	}
	
	// ------------ Metodos DAO ------------------


	public ClienteDAO getClienteDAO() {
		return clienteDAO;
	}

	public void setClienteDAO(ClienteDAO clienteDAO) {
		this.clienteDAO = clienteDAO;
	}
	
	public PlantaDAO getPlantaDAO() {
		return plantaDAO;
	}

	public void setPlantaDAO(PlantaDAO plantaDAO) {
		this.plantaDAO = plantaDAO;
	}
	
	public CamaraDAO getCamaraDAO() {
		return camaraDAO;
	}

	public void setCamaraDAO(CamaraDAO camaraDAO) {
		this.camaraDAO = camaraDAO;
	}
	
	public AvisoDAO getAvisoDAO() {
		return avisoDAO;
	}

	public void setAvisoDAO(AvisoDAO avisoDAO) {
		this.avisoDAO = avisoDAO;
	}
	
	// ------------ Metodos de Modelo --------------

	public ConstanciaDeDeposito getConstancia() {
		return constancia;
	}

	public void setConstancia(ConstanciaDeDeposito selectedConstancia) {
		this.constancia = selectedConstancia;
	}

	public Planta getPlantaSelect() {
		return plantaSelect;
	}

	public void setPlantaSelect(Planta plantaSelect) {
		this.plantaSelect = plantaSelect;
	}

	
	
	// ----------- Otros Metodos ------------------

	public void filtraListado() {
		camaraPorPlanta.clear();//limpia la lista
		camaraPorPlanta = camaras.stream()
				.filter(ps -> plantaSelect != null
						? (ps.getPlantaCve().getPlantaCve().intValue() == plantaSelect.getPlantaCve().intValue())
						: false)
				.collect(Collectors.toList());
		System.out.println("Productos Cliente Filtrados:" + camaraPorPlanta.toString() + "---------------------------------------------------------------------------------------");
	}
	
	
	
	
	
	
	
	
	

}
