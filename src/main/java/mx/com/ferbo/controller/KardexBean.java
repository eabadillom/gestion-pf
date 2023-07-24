package mx.com.ferbo.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.PrimeFaces;

import mx.com.ferbo.dao.CamaraDAO;
import mx.com.ferbo.dao.ClienteDAO;
import mx.com.ferbo.dao.ConstanciaDeDepositoDAO;
import mx.com.ferbo.dao.PartidaDAO;
import mx.com.ferbo.dao.PlantaDAO;
import mx.com.ferbo.dao.ProductoDAO;
import mx.com.ferbo.dao.UnidadDeManejoDAO;
import mx.com.ferbo.dao.UnidadDeProductoDAO;
import mx.com.ferbo.model.Camara;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.ConstanciaDeDeposito;
import mx.com.ferbo.model.Partida;
import mx.com.ferbo.model.Planta;
import mx.com.ferbo.model.Producto;
import mx.com.ferbo.model.UnidadDeManejo;
import mx.com.ferbo.model.UnidadDeProducto;

@Named
@ViewScoped
public class KardexBean implements Serializable {

	/**
	 * @author Juan_Cervantes
	 */
	private static final long serialVersionUID = -4400856556349650679L;

	/**
	 * Objetos para Cliente
	 */

	private List<Cliente> lstClientes;
	private Cliente clienteSelected;
	private ClienteDAO clienteDAO;

	/**
	 * Objetos para Productos
	 */
	private List<Producto> listProducto;
	private Producto productoSelected;
	private ProductoDAO productoDAO;

	/**
	 * Objetos para Plantas
	 */
	private List<Planta> listPlanta;
	private Planta plantaSelected;
	private PlantaDAO plantaDAO;

	/**
	 * Objetos para Camaras
	 */
	private List<Camara> listCamara;
	private Camara camaraSelected;
	private CamaraDAO camaraDAO;

	/**
	 * Objetos para Constancia de deposito
	 */
	private List<ConstanciaDeDeposito> listConstanciaDeposito;
	private ConstanciaDeDeposito constanciaDepositoSelected;
	private ConstanciaDeDepositoDAO constanciaDeDepositoDAO;
	private List<ConstanciaDeDeposito> listConstanciaDepositoFiltered;

	/**
	 * Objetos para Partida
	 */
	private List<Partida> listPartida;
	private Partida partidaSelected;
	private PartidaDAO partidaDAO;

	/**
	 * Objetos para unidad de manejo
	 */
	private List<UnidadDeManejo> listUnidadManejo;
	private UnidadDeManejo unidadManejoSelected;
	private UnidadDeManejoDAO unidadDeManejoDAO;

	/**
	 * Objetos para unidad de producto
	 */
	private List<UnidadDeProducto> listUnidadProducto;
	private UnidadDeProducto unidadProductoSelected;
	private UnidadDeProductoDAO unidadDeProductoDAO;

	/**
	 * Auxiliares
	 */
	private String folioClienteSelected;

	/**
	 * Constructores
	 */
	public KardexBean() {
		this.clienteDAO = new ClienteDAO();
		this.constanciaDeDepositoDAO = new ConstanciaDeDepositoDAO();
		partidaDAO = new PartidaDAO();

	}

	@PostConstruct
	public void init() {
		lstClientes = clienteDAO.buscarTodos();
		listConstanciaDeposito = constanciaDeDepositoDAO.buscarTodos();
		listConstanciaDepositoFiltered = new ArrayList<>();
		listPartida = new ArrayList<>();
	}

	public void buscaDatos() {
		listConstanciaDepositoFiltered = new ArrayList<>();
		constanciaDepositoSelected = new ConstanciaDeDeposito();
		ConstanciaDeDeposito consAux = new ConstanciaDeDeposito();
		consAux.setFolio(Integer.parseInt(folioClienteSelected));
		listConstanciaDepositoFiltered = constanciaDeDepositoDAO.buscarPorFolio(consAux);
		if (!listConstanciaDepositoFiltered.isEmpty() && listConstanciaDepositoFiltered.size() > 0) {
			constanciaDepositoSelected = listConstanciaDepositoFiltered.get(0);
			clienteSelected = new Cliente();
			clienteSelected = clienteDAO.buscarPorId(constanciaDepositoSelected.getCteCve().getCteCve());
			listPartida = partidaDAO.buscarPorConstanciaDeposito(consAux);
		}
		System.out.println(constanciaDepositoSelected.getFolioCliente());
		
		PrimeFaces.current().ajax().update("form:dt-entradasKardex");
	}

	/**
	 * Getters & Setters
	 */

	public List<Cliente> getLstClientes() {
		return lstClientes;
	}

	public void setLstClientes(List<Cliente> lstClientes) {
		this.lstClientes = lstClientes;
	}

	public Cliente getClienteSelected() {
		return clienteSelected;
	}

	public void setClienteSelected(Cliente clienteSelected) {
		this.clienteSelected = clienteSelected;
	}

	public ClienteDAO getClienteDAO() {
		return clienteDAO;
	}

	public void setClienteDAO(ClienteDAO clienteDAO) {
		this.clienteDAO = clienteDAO;
	}

	public List<Producto> getListProducto() {
		return listProducto;
	}

	public void setListProducto(List<Producto> listProducto) {
		this.listProducto = listProducto;
	}

	public Producto getProductoSelected() {
		return productoSelected;
	}

	public void setProductoSelected(Producto productoSelected) {
		this.productoSelected = productoSelected;
	}

	public ProductoDAO getProductoDAO() {
		return productoDAO;
	}

	public void setProductoDAO(ProductoDAO productoDAO) {
		this.productoDAO = productoDAO;
	}

	public List<Planta> getListPlanta() {
		return listPlanta;
	}

	public void setListPlanta(List<Planta> listPlanta) {
		this.listPlanta = listPlanta;
	}

	public Planta getPlantaSelected() {
		return plantaSelected;
	}

	public void setPlantaSelected(Planta plantaSelected) {
		this.plantaSelected = plantaSelected;
	}

	public PlantaDAO getPlantaDAO() {
		return plantaDAO;
	}

	public void setPlantaDAO(PlantaDAO plantaDAO) {
		this.plantaDAO = plantaDAO;
	}

	public List<Camara> getListCamara() {
		return listCamara;
	}

	public void setListCamara(List<Camara> listCamara) {
		this.listCamara = listCamara;
	}

	public Camara getCamaraSelected() {
		return camaraSelected;
	}

	public void setCamaraSelected(Camara camaraSelected) {
		this.camaraSelected = camaraSelected;
	}

	public CamaraDAO getCamaraDAO() {
		return camaraDAO;
	}

	public void setCamaraDAO(CamaraDAO camaraDAO) {
		this.camaraDAO = camaraDAO;
	}

	public List<ConstanciaDeDeposito> getListConstanciaDeposito() {
		return listConstanciaDeposito;
	}

	public void setListConstanciaDeposito(List<ConstanciaDeDeposito> listConstanciaDeposito) {
		this.listConstanciaDeposito = listConstanciaDeposito;
	}

	public ConstanciaDeDeposito getConstanciaDepositoSelected() {
		return constanciaDepositoSelected;
	}

	public void setConstanciaDepositoSelected(ConstanciaDeDeposito constanciaDepositoSelected) {
		this.constanciaDepositoSelected = constanciaDepositoSelected;
	}

	public ConstanciaDeDepositoDAO getConstanciaDeDepositoDAO() {
		return constanciaDeDepositoDAO;
	}

	public void setConstanciaDeDepositoDAO(ConstanciaDeDepositoDAO constanciaDeDepositoDAO) {
		this.constanciaDeDepositoDAO = constanciaDeDepositoDAO;
	}

	public List<Partida> getListPartida() {
		return listPartida;
	}

	public void setListPartida(List<Partida> listPartida) {
		this.listPartida = listPartida;
	}

	public Partida getPartidaSelected() {
		return partidaSelected;
	}

	public void setPartidaSelected(Partida partidaSelected) {
		this.partidaSelected = partidaSelected;
	}

	public PartidaDAO getPartidaDAO() {
		return partidaDAO;
	}

	public void setPartidaDAO(PartidaDAO partidaDAO) {
		this.partidaDAO = partidaDAO;
	}

	public List<UnidadDeManejo> getListUnidadManejo() {
		return listUnidadManejo;
	}

	public void setListUnidadManejo(List<UnidadDeManejo> listUnidadManejo) {
		this.listUnidadManejo = listUnidadManejo;
	}

	public UnidadDeManejo getUnidadManejoSelected() {
		return unidadManejoSelected;
	}

	public void setUnidadManejoSelected(UnidadDeManejo unidadManejoSelected) {
		this.unidadManejoSelected = unidadManejoSelected;
	}

	public UnidadDeManejoDAO getUnidadDeManejoDAO() {
		return unidadDeManejoDAO;
	}

	public void setUnidadDeManejoDAO(UnidadDeManejoDAO unidadDeManejoDAO) {
		this.unidadDeManejoDAO = unidadDeManejoDAO;
	}

	public List<UnidadDeProducto> getListUnidadProducto() {
		return listUnidadProducto;
	}

	public void setListUnidadProducto(List<UnidadDeProducto> listUnidadProducto) {
		this.listUnidadProducto = listUnidadProducto;
	}

	public UnidadDeProducto getUnidadProductoSelected() {
		return unidadProductoSelected;
	}

	public void setUnidadProductoSelected(UnidadDeProducto unidadProductoSelected) {
		this.unidadProductoSelected = unidadProductoSelected;
	}

	public UnidadDeProductoDAO getUnidadDeProductoDAO() {
		return unidadDeProductoDAO;
	}

	public void setUnidadDeProductoDAO(UnidadDeProductoDAO unidadDeProductoDAO) {
		this.unidadDeProductoDAO = unidadDeProductoDAO;
	}

	public String getFolioClienteSelected() {
		return folioClienteSelected;
	}

	public void setFolioClienteSelected(String folioSelected) {
		this.folioClienteSelected = folioSelected;
	}

	public List<ConstanciaDeDeposito> getListConstanciaDepositoFiltered() {
		return listConstanciaDepositoFiltered;
	}

	public void setListConstanciaDepositoFiltered(List<ConstanciaDeDeposito> listConstanciaDepositoFiltered) {
		this.listConstanciaDepositoFiltered = listConstanciaDepositoFiltered;
	}

}
