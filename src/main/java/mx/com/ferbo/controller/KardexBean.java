package mx.com.ferbo.controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.PrimeFaces;

import mx.com.ferbo.dao.CamaraDAO;
import mx.com.ferbo.dao.ClienteDAO;
import mx.com.ferbo.dao.ConstanciaDeDepositoDAO;
import mx.com.ferbo.dao.ConstanciaSalidaDAO;
import mx.com.ferbo.dao.DetalleConstanciaSalidaDAO;
import mx.com.ferbo.dao.PartidaDAO;
import mx.com.ferbo.dao.PlantaDAO;
import mx.com.ferbo.dao.ProductoDAO;
import mx.com.ferbo.dao.UnidadDeManejoDAO;
import mx.com.ferbo.dao.UnidadDeProductoDAO;
import mx.com.ferbo.model.Camara;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.ConstanciaDeDeposito;
import mx.com.ferbo.model.ConstanciaSalida;
import mx.com.ferbo.model.DetalleConstanciaSalida;
import mx.com.ferbo.model.Partida;
import mx.com.ferbo.model.Planta;
import mx.com.ferbo.model.Producto;
import mx.com.ferbo.model.UnidadDeManejo;
import mx.com.ferbo.model.UnidadDeProducto;
import mx.com.ferbo.util.KardexTotalsBean;

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
	 * Objetos para Detalle de Constancia de Salida
	 */
	private List<DetalleConstanciaSalida> listDetalleSalida;
	private DetalleConstanciaSalida detalleSalidaSelected;
	private DetalleConstanciaSalidaDAO detalleSalidaDAO;
	
	/**
	 * Objetos auxiliares para Totales de Salidas
	 */
	private List<KardexTotalsBean> totalSalidaKardex;

	/**
	 * Objetos para traspasos
	 */

	/**
	 * Auxiliares
	 */
	private String folioClienteSelected;
	private Integer cantidadSalida;
	private BigDecimal pesoSalida;
	private Integer cantidadTotal;
	private BigDecimal pesoTotal;

	/**
	 * Constructores
	 */
	public KardexBean() {
		this.clienteDAO = new ClienteDAO();
		this.constanciaDeDepositoDAO = new ConstanciaDeDepositoDAO();
		this.partidaDAO = new PartidaDAO();
		this.detalleSalidaDAO = new DetalleConstanciaSalidaDAO();
		this.cantidadSalida = 0;
		this.cantidadTotal = 0;
		this.pesoSalida = new BigDecimal(0);
		this.pesoTotal = new BigDecimal(0);
	}

	@PostConstruct
	public void init() {
		lstClientes = clienteDAO.buscarTodos();
		listConstanciaDepositoFiltered = new ArrayList<>();
		listDetalleSalida = new ArrayList<>();
		listPartida = new ArrayList<>();
	}

	public void buscaDatos() {
		if (this.folioClienteSelected == null || this.folioClienteSelected != "") {
			// Kardex Entradas
			manejaEntradas();
			System.out.println(constanciaDepositoSelected.getFolioCliente());
			PrimeFaces.current().ajax().update("form:dt-entradasKardex");
		}
	}

	private void manejaEntradas() {
		this.cantidadSalida =0;
		this.setPesoSalida(new BigDecimal(0));
		listConstanciaDepositoFiltered = new ArrayList<>();
		ConstanciaDeDeposito consAux = new ConstanciaDeDeposito();
		consAux.setFolio(Integer.parseInt(folioClienteSelected));
		listConstanciaDepositoFiltered = constanciaDeDepositoDAO.buscarPorFolio(consAux);
		if (!listConstanciaDepositoFiltered.isEmpty() && listConstanciaDepositoFiltered.size() > 0) {
			constanciaDepositoSelected = listConstanciaDepositoFiltered.get(0);
			clienteSelected = new Cliente();
			clienteSelected = clienteDAO.buscarPorId(constanciaDepositoSelected.getCteCve().getCteCve());
			listPartida = partidaDAO.buscarPorConstanciaDeposito(consAux);
			listDetalleSalida = new ArrayList<>();
			if (listPartida != null && listPartida.size() > 0) {
				for (Partida p : listPartida) {
					manejaSalida(p);
				}
			}
		}
	}

	private void manejaSalida(Partida p) {
		detalleSalidaSelected = new DetalleConstanciaSalida();
		if (p != null) {
			List<DetalleConstanciaSalida> detalleAux = new ArrayList<>();
			detalleAux = detalleSalidaDAO.buscarPorParams(p, constanciaDepositoSelected);
			this.cantidadTotal = p.getCantidadTotal();
			this.pesoTotal = p.getPesoTotal();
			if (!detalleAux.isEmpty()) {
				Integer auxCantidadSalida = 0;
				BigDecimal auxPesoSalida = new BigDecimal(0);
				String unidadAux = "";
				for (DetalleConstanciaSalida ds : detalleAux) {					
					listDetalleSalida.add(ds);
					auxCantidadSalida += ds.getCantidad();
					auxPesoSalida = auxPesoSalida.add(ds.getPeso());
					unidadAux = ds.getUnidad();
				}
				DetalleConstanciaSalida dsAux = new DetalleConstanciaSalida();
				dsAux.setId(0);
				dsAux.setCantidad(auxCantidadSalida);
				dsAux.setProducto("Totales Salidas");
				dsAux.setUnidad(unidadAux);
				dsAux.setPeso(auxPesoSalida);
				listDetalleSalida.add(dsAux);
				this.cantidadTotal -=  this.cantidadSalida;
				this.pesoTotal = this.pesoTotal.subtract(pesoSalida);
			}
		}
	}
	
	public DetalleConstanciaSalida obtenTotales() {
		return listDetalleSalida.get(listDetalleSalida.size()-1);
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

	public List<DetalleConstanciaSalida> getListDetalleSalida() {
		return listDetalleSalida;
	}

	public void setListDetalleSalida(List<DetalleConstanciaSalida> listDetalleSalida) {
		this.listDetalleSalida = listDetalleSalida;
	}

	public DetalleConstanciaSalida getDetalleSalidaSelected() {
		return detalleSalidaSelected;
	}

	public void setDetalleSalidaSelected(DetalleConstanciaSalida detalleSalidaSelected) {
		this.detalleSalidaSelected = detalleSalidaSelected;
	}

	public DetalleConstanciaSalidaDAO getDetalleSalidaDAO() {
		return detalleSalidaDAO;
	}

	public void setDetalleSalidaDAO(DetalleConstanciaSalidaDAO detalleSalidaDAO) {
		this.detalleSalidaDAO = detalleSalidaDAO;
	}

	public Integer getCantidadSalida() {
		return cantidadSalida;
	}

	public void setCantidadSalida(Integer cantidadSalida) {
		this.cantidadSalida = cantidadSalida;
	}

	public BigDecimal getPesoSalida() {
		return pesoSalida;
	}

	public void setPesoSalida(BigDecimal pesoSalida) {
		this.pesoSalida = pesoSalida;
	}

	public Integer getCantidadTotal() {
		return cantidadTotal;
	}

	public void setCantidadTotal(Integer cantidadTotal) {
		this.cantidadTotal = cantidadTotal;
	}

	public BigDecimal getPesoTotal() {
		return pesoTotal;
	}

	public void setPesoTotal(BigDecimal pesoTotal) {
		this.pesoTotal = pesoTotal;
	}

	public List<KardexTotalsBean> getTotalSalidaKardex() {
		return totalSalidaKardex;
	}

	public void setTotalSalidaKardex(List<KardexTotalsBean> totalSalidaKardex) {
		this.totalSalidaKardex = totalSalidaKardex;
	}
	
}
