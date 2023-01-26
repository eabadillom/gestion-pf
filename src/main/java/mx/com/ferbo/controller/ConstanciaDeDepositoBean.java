package mx.com.ferbo.controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.PrimeFaces;

import mx.com.ferbo.dao.AvisoDAO;
import mx.com.ferbo.dao.CamaraDAO;
import mx.com.ferbo.dao.ClienteDAO;
import mx.com.ferbo.dao.ConstanciaDeDepositoDAO;
import mx.com.ferbo.dao.PlantaDAO;
import mx.com.ferbo.dao.PosicionCamaraDAO;
import mx.com.ferbo.dao.ProductoClienteDAO;
import mx.com.ferbo.dao.ProductoDAO;
import mx.com.ferbo.dao.UnidadDeManejoDAO;
import mx.com.ferbo.model.Aviso;
import mx.com.ferbo.model.Camara;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.ConstanciaDeDeposito;
import mx.com.ferbo.model.Partida;
import mx.com.ferbo.model.Planta;
import mx.com.ferbo.model.Posicion;
import mx.com.ferbo.model.Producto;
import mx.com.ferbo.model.ProductoPorCliente;
import mx.com.ferbo.model.UnidadDeManejo;
import mx.com.ferbo.model.UnidadDeProducto;

@Named
@ViewScoped
public class ConstanciaDeDepositoBean implements Serializable{
	
	private static final long serialVersionUID = -1785488265380235016L;
	
	private ClienteDAO clienteDAO;
	private PlantaDAO plantaDAO;
	private AvisoDAO avisoDAO;
	private CamaraDAO camaraDAO;
	private ConstanciaDeDepositoDAO constanciaDAO;
	private ProductoClienteDAO productoClienteDAO;
	private ProductoDAO productoDAO;
	private UnidadDeManejoDAO unidadManejoDAO;
	private PosicionCamaraDAO posicionCamaraDAO;
	
	private List<Cliente> listadoCliente;
	private List<Planta> listadoPlanta;
	private List<Camara> camaras;
	private List<Camara> camaraPorPlanta;
	private List<Aviso> listadoAviso;
	private List<ConstanciaDeDeposito> listadoConstancia;
	private List<ProductoPorCliente> productoCliente;
	private List<Producto> listadoProducto;//nueva
	private List<Producto> productoC;
	private List<UnidadDeManejo> listadoUnidadDeManejo;
	private List<Posicion> listaPosiciones;
	private List<Posicion> posiciones;
	private List<Partida> listadoPartida;
	
	private Planta plantaSelect;
	private Cliente clienteSelect;
	private ProductoPorCliente productoPorCliente;//nueva
	private Camara camaraSelect;
	private Producto productoSelect;
	private UnidadDeManejo unidadManejoSelect;
	private Posicion posicionSelect;

	private String noConstanciaSelect;
	private BigDecimal unidadesPorTarima;
	private BigDecimal numTarimas;
	private int cantidadTotal;
	private BigDecimal pesoTotal;
	private BigDecimal valorMercancia;
	
	public ConstanciaDeDepositoBean() {
		clienteDAO = new ClienteDAO();
		plantaDAO = new PlantaDAO();
		camaraDAO = new CamaraDAO();
		avisoDAO = new AvisoDAO();
		constanciaDAO = new ConstanciaDeDepositoDAO();
		productoClienteDAO = new ProductoClienteDAO();//nueva
		productoDAO = new ProductoDAO();
		unidadManejoDAO = new UnidadDeManejoDAO();
		posicionCamaraDAO = new PosicionCamaraDAO();
		listadoPlanta = new ArrayList<>();
		camaraPorPlanta = new ArrayList<Camara>();
		productoC = new ArrayList<Producto>();
		posiciones = new ArrayList<Posicion>();
		listadoPartida = new ArrayList<Partida>();
		
	}
	
	@PostConstruct
	public void init(){
		listadoCliente = clienteDAO.buscarTodos();		
		listadoPlanta = plantaDAO.findall();
		camaras = camaraDAO.buscarTodos();
		listadoProducto = productoDAO.buscarTodos();//nueva
		listadoAviso = avisoDAO.buscarTodos();
		listadoConstancia = constanciaDAO.buscarTodos();
		listadoUnidadDeManejo = unidadManejoDAO.buscarTodos();
		this.listaPosiciones = posicionCamaraDAO.findAll();
		
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
	
	public List<ConstanciaDeDeposito> getListadoConstancia() {
		return listadoConstancia;
	}

	public void setListadoConstancia(List<ConstanciaDeDeposito> listadoConstancia) {
		this.listadoConstancia = listadoConstancia;
	}
	
	public List<ProductoPorCliente> getProductoCliente() {
		return productoCliente;
	}

	public void setProductoCliente(List<ProductoPorCliente> productoCliente) {
		this.productoCliente = productoCliente;
	}
	
	public List<Producto> getListadoProducto() {
		return listadoProducto;
	}

	public void setListadoProducto(List<Producto> listadoProducto) {
		this.listadoProducto = listadoProducto;
	}
	
	public List<UnidadDeManejo> getListadoUnidadDeManejo() {
		return listadoUnidadDeManejo;
	}

	public void setListadoUnidadDeManejo(List<UnidadDeManejo> listadoUnidadDeManejo) {
		this.listadoUnidadDeManejo = listadoUnidadDeManejo;
	}
	
	public List<Posicion> getListaPosiciones() {
		return listaPosiciones;
	}

	public void setListaPosiciones(List<Posicion> listaPosiciones) {
		this.listaPosiciones = listaPosiciones;
	}
	
	public List<Posicion> getPosiciones() {
		return posiciones;
	}

	public void setPosiciones(List<Posicion> posiciones) {
		this.posiciones = posiciones;
	}
	
	public Cliente getClienteSelect() {
		return clienteSelect;
	}

	public void setClienteSelect(Cliente clienteSelect) {
		this.clienteSelect = clienteSelect;
	}
	
	public List<Partida> getListadoPartida() {
		return listadoPartida;
	}

	public void setListadoPartida(List<Partida> listadoPartida) {
		this.listadoPartida = listadoPartida;
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
	
	public ConstanciaDeDepositoDAO getConstanciaDAO() {
		return constanciaDAO;
	}

	public void setConstanciaDAO(ConstanciaDeDepositoDAO constanciaDAO) {
		this.constanciaDAO = constanciaDAO;
	}
	
	public ProductoClienteDAO getProductoClienteDAO() {
		return productoClienteDAO;
	}

	public void setProductoClienteDAO(ProductoClienteDAO productoClienteDAO) {
		this.productoClienteDAO = productoClienteDAO;
	}
	
	public UnidadDeManejoDAO getUnidadManejoDAO() {
		return unidadManejoDAO;
	}

	public void setUnidadManejoDAO(UnidadDeManejoDAO unidadManejoDAO) {
		this.unidadManejoDAO = unidadManejoDAO;
	}
	
	public PosicionCamaraDAO getPosicionCamaraDAO() {
		return posicionCamaraDAO;
	}

	public void setPosicionCamaraDAO(PosicionCamaraDAO posicionCamaraDAO) {
		this.posicionCamaraDAO = posicionCamaraDAO;
	}
	
	// ------------ Metodos de Modelo --------------

	public String getNoConstanciaSelect() {
		return noConstanciaSelect;
	}

	public void setNoConstanciaSelect(String noConstanciaSelect) {
		this.noConstanciaSelect = noConstanciaSelect;
	}

	public Planta getPlantaSelect() {
		return plantaSelect;
	}

	public void setPlantaSelect(Planta plantaSelect) {
		this.plantaSelect = plantaSelect;
	}
	
	public ProductoPorCliente getProductoPorCliente() {
		return productoPorCliente;
	}

	public void setProductoPorCliente(ProductoPorCliente productoPorCliente) {
		this.productoPorCliente = productoPorCliente;
	}
	
	public ProductoDAO getProductoDAO() {
		return productoDAO;
	}

	public void setProductoDAO(ProductoDAO productoDAO) {
		this.productoDAO = productoDAO;
	}
	
	public List<Producto> getProductoC() {
		return productoC;
	}

	public void setProductoC(List<Producto> productoC) {
		this.productoC = productoC;
	}
	
	public Camara getCamaraSelect() {
		return camaraSelect;
	}

	public void setCamaraSelect(Camara camaraSelect) {
		this.camaraSelect = camaraSelect;
	}
	
	public BigDecimal getNumTarimas() {
		return numTarimas;
	}

	public void setNumTarimas(BigDecimal numTarimas) {
		this.numTarimas = numTarimas;
	}
	
	public int getCantidadTotal() {
		return cantidadTotal;
	}

	public void setCantidadTotal(int cantidadTotal) {
		this.cantidadTotal = cantidadTotal;
	}
	
	public Producto getProductoSelect() {
		return productoSelect;
	}

	public void setProductoSelect(Producto productoSelect) {
		this.productoSelect = productoSelect;
	}
	
	public UnidadDeManejo getUnidadManejoSelect() {
		return unidadManejoSelect;
	}

	public void setUnidadManejoSelect(UnidadDeManejo unidadManejoSelect) {
		this.unidadManejoSelect = unidadManejoSelect;
	}
	
	public Posicion getPosicionSelect() {
		return posicionSelect;
	}

	public void setPosicionSelect(Posicion posicionSelect) {
		this.posicionSelect = posicionSelect;
	}
	
	// ----------- Otros Metodos ------------------

	public void filtraListado() {
		camaraPorPlanta.clear();//limpia la lista
		camaraPorPlanta = camaras.stream()
				.filter(ps -> plantaSelect != null
						? (ps.getPlantaCve().getPlantaCve().intValue() == plantaSelect.getPlantaCve().intValue())
						: false)
				.collect(Collectors.toList());
		//System.out.println("Productos Cliente Filtrados:" + camaraPorPlanta.toString() + "---------------------------------------------------------------------------------------");
	}
	
	public void validar() {
		 
		String constanciaE = noConstanciaSelect.trim();
		constanciaE.trim();
		
		for(ConstanciaDeDeposito cd: listadoConstancia) {
			if(constanciaE.equals(cd.getFolioCliente())) {				
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Folio Existente" ,"Digite un nuevo folio"));
				PrimeFaces.current().ajax().update("form:messages");
			}
		}
		
		
		this.noConstanciaSelect = constanciaE;
		
	}
	
	public void productoPorCliente() {//nuevo
		
		productoC.clear();
		productoPorCliente = new ProductoPorCliente(clienteSelect); //seteo el cliente seleccionado del componente
		productoCliente = productoClienteDAO.buscarPorCriterios(productoPorCliente);//retorno la lista de objeto productoporcliente (lo busco por cteCve)
		for(ProductoPorCliente pc: productoCliente) {//recorro la lista de productocliente para tomar para tomar el valor de productoCve
			for(Producto p: listadoProducto) {//anido el recorrido de la lista de mis productos
				if(pc.getProductoCve().getProductoCve().intValue()==p.getProductoCve().intValue()) {//comparo que si el id recuperado es igual al id de algun registro de producto
					productoC.add(p);//añado dicho registro a la lista 
				}
			}
		}
		
		System.out.println("Productos de cliente:" + productoC);
		
	}
	
	public void filtrarPosicion() {
		//posiciones.clear();
		//System.out.println("--------------------------------------------"+listaPosiciones);
		Posicion posicion = new Posicion();
		posicion.setPlanta(plantaSelect);
		posicion.setCamara(camaraSelect);
		posiciones = posicionCamaraDAO.buscarPorCriterios(posicion);
		posiciones = listaPosiciones.stream()
				.filter(pc -> plantaSelect != null
						? (pc.getPlanta().getPlantaCve() == camaraSelect.getPlantaCve().getPlantaCve())
						: false)
				.collect(Collectors.toList());
		//System.out.println("posicion planta -----------------------------------------------------" + posiciones);
		posiciones = listaPosiciones.stream()
				.filter(cs -> camaraSelect != null
						? (cs.getCamara().getCamaraCve() == camaraSelect.getCamaraCve().intValue())
						: false)
				.collect(Collectors.toList());
		
		System.out.println("camaras-----------------------------------------------------------" + posiciones);

	}

	public BigDecimal getUnidadesPorTarima() {
		return unidadesPorTarima;
	}

	public void setUnidadesPorTarima(BigDecimal unidadesPorTarima) {
		this.unidadesPorTarima = unidadesPorTarima;
	}

	public BigDecimal getPesoTotal() {
		return pesoTotal;
	}

	public void setPesoTotal(BigDecimal pesoTotal) {
		this.pesoTotal = pesoTotal;
	}

	public BigDecimal getValorMercancia() {
		return valorMercancia;
	}

	public void setValorMercancia(BigDecimal valorMercancia) {
		this.valorMercancia = valorMercancia;
	}

	public void calculo() {
		BigDecimal unidadT = new BigDecimal(cantidadTotal).setScale(2);
		BigDecimal tarimas = unidadT.divide(numTarimas,2,RoundingMode.HALF_UP);		
		this.unidadesPorTarima = new BigDecimal(tarimas.intValue()).setScale(2);
	}
	
	public void savePartida() {
		
		ConstanciaDeDeposito constanciaDeDeposito = new ConstanciaDeDeposito();
		constanciaDeDeposito.setFolioCliente(noConstanciaSelect);
		
		UnidadDeProducto unidadDeProducto = new UnidadDeProducto();
		unidadDeProducto.setProductoCve(productoSelect);
		unidadDeProducto.setUnidadDeManejoCve(unidadManejoSelect);
		
		Partida partida = new Partida();
		partida.setCamaraCve(camaraSelect);//revisar se le pasaba camaraSelect
		partida.setFolio(constanciaDeDeposito);
		partida.setPesoTotal(pesoTotal);
		partida.setCantidadTotal(cantidadTotal);
		partida.setUnidadDeProductoCve(unidadDeProducto);
		partida.setValorMercancia(valorMercancia);
		partida.setNoTarimas(numTarimas);
		
		this.listadoPartida.add(partida);
		
	}
	

}
