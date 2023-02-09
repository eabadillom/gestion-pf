package mx.com.ferbo.controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
import mx.com.ferbo.dao.ConstanciaDepositoDetalleDAO;
import mx.com.ferbo.dao.PartidaDAO;
import mx.com.ferbo.dao.PlantaDAO;
import mx.com.ferbo.dao.PosicionCamaraDAO;
import mx.com.ferbo.dao.PrecioServicioDAO;
import mx.com.ferbo.dao.ProductoClienteDAO;
import mx.com.ferbo.dao.ProductoDAO;
import mx.com.ferbo.dao.ServicioDAO;
import mx.com.ferbo.dao.UnidadDeManejoDAO;
import mx.com.ferbo.dao.UnidadDeProductoDAO;
import mx.com.ferbo.model.Aviso;
import mx.com.ferbo.model.Camara;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.ConstanciaDeDeposito;
import mx.com.ferbo.model.ConstanciaDepositoDetalle;
import mx.com.ferbo.model.DetallePartida;
import mx.com.ferbo.model.DetallePartidaPK;
import mx.com.ferbo.model.Partida;
import mx.com.ferbo.model.Planta;
import mx.com.ferbo.model.Posicion;
import mx.com.ferbo.model.PrecioServicio;
import mx.com.ferbo.model.Producto;
import mx.com.ferbo.model.ProductoPorCliente;
import mx.com.ferbo.model.Servicio;
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
	private UnidadDeManejoDAO unidadDeManejoDAO;
	private PosicionCamaraDAO posicionCamaraDAO;
	private ServicioDAO servicioDAO;	
	private PrecioServicioDAO precioServicioDAO;
	private UnidadDeProductoDAO unidadDeProductoDAO;
	
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
	private List<Aviso> avisoPorCliente;
	private List<Partida> listadoPartida;//lista donde se guardan los objetos pero no se guardan en BD
	private List<DetallePartida> listadoDetallePartida;
	private List<Servicio> listadoServicio;
	private List<PrecioServicio> listadoPrecioServicio;//
	private List<PrecioServicio> listaServicioUnidad;
	private List<ConstanciaDepositoDetalle> listadoConstanciaDepositoDetalle;//se van agregar constanciadepositodetalle al dar añadir
	private List<Partida> selectedPartidas;
	
	private Planta plantaSelect;
	private Cliente clienteSelect;
	private ProductoPorCliente productoPorCliente;//nueva
	private Camara camaraSelect;
	private Producto productoSelect;
	private UnidadDeManejo unidadDeManejoSelect;
	private Posicion posicionCamaraSelect;
	private Aviso avisoSelect;
	private PrecioServicio precioServicioSelect;
	private ConstanciaDepositoDetalle selectedConstanciasDD;
	private Partida selectedPartida;

	private String noConstanciaSelect;
	private BigDecimal unidadesPorTarima;
	private BigDecimal numTarimas;
	private int cantidadTotal;
	private BigDecimal pesoTotal;
	private BigDecimal valorMercancia;
	private String pedimento,contenedor,lote,otro;
	private Boolean isCongelacion,isConservacion,isRefrigeracion,isManiobras;
	private int congelacion=2,conservacion=32,refrigeracion=33,maniobras=34;
	private BigDecimal cantidadServicio;
	private Boolean respuesta;
	private String temperatura;
	private BigDecimal metroCamara;
	private String nombreTransportista;
	private String placas;
	private String observacion;
	private ConstanciaDeDeposito constanciaDeDeposito;
	private ConstanciaDepositoDetalle constanciaDD;
	private List<ConstanciaDepositoDetalle> selectedConstanciaDD;
	
	private Date fechaCaducidad;
	private Date fechaIngreso;
	
	
	public ConstanciaDeDepositoBean() {
		clienteDAO = new ClienteDAO();
		plantaDAO = new PlantaDAO();
		camaraDAO = new CamaraDAO();
		avisoDAO = new AvisoDAO();
		constanciaDAO = new ConstanciaDeDepositoDAO();
		productoClienteDAO = new ProductoClienteDAO();//nueva
		productoDAO = new ProductoDAO();
		unidadDeManejoDAO = new UnidadDeManejoDAO();
		posicionCamaraDAO = new PosicionCamaraDAO();
		servicioDAO = new ServicioDAO();
		precioServicioDAO = new PrecioServicioDAO();
		unidadDeProductoDAO = new UnidadDeProductoDAO();
		
		listadoPlanta = new ArrayList<>();
		listadoCliente = new ArrayList<>();
		camaraPorPlanta = new ArrayList<Camara>();
		productoC = new ArrayList<Producto>();
		posiciones = new ArrayList<Posicion>();
		listadoPartida = new ArrayList<Partida>();
		listadoUnidadDeManejo = new ArrayList<>();
		avisoPorCliente = new ArrayList<Aviso>();
		listadoDetallePartida = new ArrayList<>();
		listadoPrecioServicio = new ArrayList<>();//
		listaServicioUnidad = new ArrayList<PrecioServicio>();//
		listadoConstanciaDepositoDetalle = new ArrayList<ConstanciaDepositoDetalle>();
		selectedPartidas = new ArrayList<Partida>();
		selectedConstanciaDD = new ArrayList<>();
		
	}
	
	@PostConstruct
	public void init(){
		listadoCliente = clienteDAO.buscarTodos();		
		listadoPlanta = plantaDAO.findall();
		camaras = camaraDAO.buscarTodos();
		listadoProducto = productoDAO.buscarTodos();//nueva
		this.listadoAviso = avisoDAO.buscarTodos();
		listadoConstancia = constanciaDAO.buscarTodos();
		this.listadoUnidadDeManejo = unidadDeManejoDAO.buscarTodos();
		this.listaPosiciones = posicionCamaraDAO.findAll();
		listadoServicio = servicioDAO.buscarTodos();
		listadoPrecioServicio = precioServicioDAO.buscarTodos();
		
		constanciaDeDeposito = new ConstanciaDeDeposito();
		
		isCongelacion=false;
		isConservacion=false;
		isRefrigeracion=false;
		isManiobras=false;
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
	
	public List<Aviso> getAvisoPorCliente() {
		return avisoPorCliente;
	}

	public void setAvisoPorCliente(List<Aviso> avisoPorCliente) {
		this.avisoPorCliente = avisoPorCliente;
	}
	
	public List<DetallePartida> getListadoDetallePartida() {
		return listadoDetallePartida;
	}

	public void setListadoDetallePartida(List<DetallePartida> listadoDetallePartida) {
		this.listadoDetallePartida = listadoDetallePartida;
	}
	
	public List<Servicio> getListadoServicio() {
		return listadoServicio;
	}

	public void setListadoServicio(List<Servicio> listadoServicio) {
		this.listadoServicio = listadoServicio;
	}
	
	public List<PrecioServicio> getListadoPrecioServicio() {
		return listadoPrecioServicio;
	}

	public void setListadoPrecioServicio(List<PrecioServicio> listadoPrecioServicio) {
		this.listadoPrecioServicio = listadoPrecioServicio;
	}

	public List<PrecioServicio> getListaServicioUnidad() {
		return listaServicioUnidad;
	}

	public void setListaServicioUnidad(List<PrecioServicio> listaServicioUnidad) {
		this.listaServicioUnidad = listaServicioUnidad;
	}
	
	public List<ConstanciaDepositoDetalle> getListadoConstanciaDepositoDetalle() {
		return listadoConstanciaDepositoDetalle;
	}

	public void setListadoConstanciaDepositoDetalle(List<ConstanciaDepositoDetalle> listadoConstanciaDepositoDetalle) {
		this.listadoConstanciaDepositoDetalle = listadoConstanciaDepositoDetalle;
	}
	
	public List<Partida> getSelectedPartidas() {
		return selectedPartidas;
	}

	public void setSelectedPartidas(List<Partida> selectedPartidas) {
		this.selectedPartidas = selectedPartidas;
	}
	
	public List<ConstanciaDepositoDetalle> getSelectedConstanciaDD() {
		return selectedConstanciaDD;
	}

	public void setSelectedConstanciaDD(List<ConstanciaDepositoDetalle> selectedConstanciaDD) {
		this.selectedConstanciaDD = selectedConstanciaDD;
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
	
	public UnidadDeManejoDAO getUnidadDeManejoDAO() {
		return unidadDeManejoDAO;
	}

	public void setUnidadDeManejoDAO(UnidadDeManejoDAO unidadDeManejoDAO) {
		this.unidadDeManejoDAO = unidadDeManejoDAO;
	}

	public PosicionCamaraDAO getPosicionCamaraDAO() {
		return posicionCamaraDAO;
	}

	public void setPosicionCamaraDAO(PosicionCamaraDAO posicionCamaraDAO) {
		this.posicionCamaraDAO = posicionCamaraDAO;
	}
	
	public ServicioDAO getServicioDAO() {
		return servicioDAO;
	}

	public void setServicioDAO(ServicioDAO servicioDAO) {
		this.servicioDAO = servicioDAO;
	}
	
	public PrecioServicioDAO getPrecioServicioDAO() {
		return precioServicioDAO;
	}

	public void setPrecioServicioDAO(PrecioServicioDAO precioServicioDAO) {
		this.precioServicioDAO = precioServicioDAO;
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

	public UnidadDeManejo getUnidadDeManejoSelect() {
		return unidadDeManejoSelect;
	}

	public void setUnidadDeManejoSelect(UnidadDeManejo unidadDeManejoSelect) {
		this.unidadDeManejoSelect = unidadDeManejoSelect;
	}
	
	public Posicion getPosicionCamaraSelect() {
		return posicionCamaraSelect;
	}

	public void setPosicionCamaraSelect(Posicion posicionCamaraSelect) {
		this.posicionCamaraSelect = posicionCamaraSelect;
	}
	
	public Aviso getAvisoSelect() {
		return avisoSelect;
	}

	public void setAvisoSelect(Aviso avisoSelect) {
		this.avisoSelect = avisoSelect;
	}
	
	public String getPedimento() {
		return pedimento;
	}

	public void setPedimento(String pedimento) {
		this.pedimento = pedimento;
	}
	
	public String getContenedor() {
		return contenedor;
	}

	public void setContenedor(String contenedor) {
		this.contenedor = contenedor;
	}

	public String getLote() {
		return lote;
	}

	public void setLote(String lote) {
		this.lote = lote;
	}

	public String getOtro() {
		return otro;
	}

	public void setOtro(String otro) {
		this.otro = otro;
	}
	
	public Date getFechaCaducidad() {
		return fechaCaducidad;
	}

	public void setFechaCaducidad(Date fechaCaducidad) {
		this.fechaCaducidad = fechaCaducidad;
	}
	
	public Date getFechaIngreso() {
		return fechaIngreso;
	}

	public void setFechaIngreso(Date fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}

	public PrecioServicio getPrecioServicioSelect() {
		return precioServicioSelect;
	}

	public void setPrecioServicioSelect(PrecioServicio precioServicioSelect) {
		this.precioServicioSelect = precioServicioSelect;
	}
	
	public Boolean getIsCongelacion() {
		return isCongelacion;
	}

	public void setIsCongelacion(Boolean isCongelacion) {
		this.isCongelacion = isCongelacion;
	}

	public Boolean getIsConservacion() {
		return isConservacion;
	}

	public void setIsConservacion(Boolean isConservacion) {
		this.isConservacion = isConservacion;
	}
	
	public Boolean getIsRefrigeracion() {
		return isRefrigeracion;
	}

	public void setIsRefrigeracion(Boolean isRefrigeracion) {
		this.isRefrigeracion = isRefrigeracion;
	}
	
	public Boolean getIsManiobras() {
		return isManiobras;
	}

	public void setIsManiobras(Boolean isManiobras) {
		this.isManiobras = isManiobras;
	}
	
	public BigDecimal getCantidadServicio() {
		return cantidadServicio;
	}

	public void setCantidadServicio(BigDecimal cantidadServicio) {
		this.cantidadServicio = cantidadServicio;
	}
	
	public ConstanciaDepositoDetalle getSelectedConstanciasDD() {
		return selectedConstanciasDD;
	}

	public void setSelectedConstanciasDD(ConstanciaDepositoDetalle selectedConstanciasDD) {
		this.selectedConstanciasDD = selectedConstanciasDD;
	}
	
	public Partida getSelectedPartida() {
		return selectedPartida;
	}

	public void setSelectedPartida(Partida selectedPartida) {
		this.selectedPartida = selectedPartida;
	}
	
	// ----------- Otros Metodos ------------------


	public void filtraListado() {
		camaraPorPlanta.clear();
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
				this.noConstanciaSelect = null;
				PrimeFaces.current().ajax().update("form:messages","form:numeroC");
				break;
			}
			
		}
		
		
	}
	
	public void productoPorCliente() {//nuevo
		
		//--------------- PRODUCTO POR CLIENTE ------------
		
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
		
		//System.out.println("Productos de cliente:" + productoC);
		
		//------------ AVISOS CLIENTE -------------
		avisoPorCliente.clear();
		avisoPorCliente = listadoAviso.stream()
				.filter(av -> clienteSelect != null
				?(av.getCteCve().getCteCve().intValue() == clienteSelect.getCteCve().intValue())//compara el id del cliente que esta en la lista con el de clienteSelect si son iguales se trae el dato
				:false).collect(Collectors.toList());
				
		//System.out.println("Avisos por cliente: " + avisoPorCliente);
		
		//-------------- PRECIO SERVICIO -----------------
		listaServicioUnidad.clear();
		PrecioServicio precioServicio = new PrecioServicio();
		precioServicio.setCliente(clienteSelect);
		listaServicioUnidad = listadoPrecioServicio.stream()
								.filter(ps -> clienteSelect != null
								?(ps.getCliente().getCteCve().intValue()==clienteSelect.getCteCve().intValue())
								:false)
								.collect(Collectors.toList());
		//Remover servicios (*)
		isCongelacion=false;isRefrigeracion=false;isConservacion=false;isManiobras=false;
		//isRefrigeracion = false;
		List<PrecioServicio> precioServicioTemp = new ArrayList<PrecioServicio>();
		precioServicioTemp.clear();
		for(PrecioServicio ps: listaServicioUnidad) {
			if(((ps.getServicio().getServicioCve()==congelacion)) 
					|| ((ps.getServicio().getServicioCve()==conservacion)) 
					|| ((ps.getServicio().getServicioCve()==refrigeracion))
					|| ((ps.getServicio().getServicioCve()==maniobras))) {
				precioServicioTemp.add(ps);
				
			}
			
		}
		
		if(!(precioServicioTemp.isEmpty())) {
			for(PrecioServicio pt: precioServicioTemp) {
				if(pt.getServicio().getServicioCve()==conservacion) {
					isConservacion=true;
				}else if(pt.getServicio().getServicioCve()==refrigeracion) {
					isRefrigeracion=true;
				}else if(pt.getServicio().getServicioCve()==maniobras) {
					isManiobras = true;
				}else if(pt.getServicio().getServicioCve()==congelacion) {
					isCongelacion = true;
				}
			}
		}
		//System.out.println("lista de objetos a remover: "+precioServicioTemp);
		
		listaServicioUnidad.removeAll(precioServicioTemp);
		
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
		
		//System.out.println("camaras-----------------------------------------------------------" + posiciones);

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
	
	public Boolean getRespuesta() {
		return respuesta;
	}

	public void setRespuesta(Boolean respuesta) {
		this.respuesta = respuesta;
	}

	public String getTemperatura() {
		return temperatura;
	}

	public void setTemperatura(String temperatura) {
		this.temperatura = temperatura;
	}

	public BigDecimal getMetroCamara() {
		return metroCamara;
	}

	public void setMetroCamara(BigDecimal metroCamara) {
		this.metroCamara = metroCamara;
	}
	
	public String getNombreTransportista() {
		return nombreTransportista;
	}

	public void setNombreTransportista(String nombreTransportista) {
		this.nombreTransportista = nombreTransportista;
	}

	public String getPlacas() {
		return placas;
	}

	public void setPlacas(String placas) {
		this.placas = placas;
	}
	
	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public void calculo() {
		BigDecimal unidadT = new BigDecimal(cantidadTotal).setScale(2);
		BigDecimal tarimas = unidadT.divide(numTarimas,2,RoundingMode.HALF_UP);		
		this.unidadesPorTarima = new BigDecimal(tarimas.intValue()).setScale(2);
	}
	
	public void savePartida(){
		
		
		UnidadDeProducto unidadDeProducto = new UnidadDeProducto();
		unidadDeProducto.setProductoCve(productoSelect);
		unidadDeProducto.setUnidadDeManejoCve(unidadDeManejoSelect);
		unidadDeProductoDAO.guardar(unidadDeProducto);//guardo el registro
		
		ConstanciaDeDeposito constanciaDeDeposito = new ConstanciaDeDeposito();
		constanciaDeDeposito.setFolioCliente(noConstanciaSelect);
		
		
		Partida partida = new Partida();
		
		partida.setCamaraCve(posicionCamaraSelect.getCamara());
		partida.setFolio(constanciaDeDeposito);
		partida.setPesoTotal(pesoTotal);
		partida.setCantidadTotal(cantidadTotal);
		partida.setUnidadDeProductoCve(unidadDeProducto);		
		//partida.setCantidadDeCobro(null);
		partida.setUnidadDeCobro(unidadDeManejoSelect);
		//partida.setPartidaSeq(null);
		partida.setValorMercancia(valorMercancia);
		partida.setRendimiento(metroCamara);
		partida.setNoTarimas(numTarimas);
		
		partida.setDetallePartidaList(new ArrayList<DetallePartida>());
		
		DetallePartida detallePartida = new DetallePartida();
		DetallePartidaPK detallePk = new DetallePartidaPK();
		detallePk.setDetPartCve(1);
		detallePk.setPartidaCve(partida);
		
		detallePartida.setDetallePartidaPK(detallePk);
		detallePartida.setDtpPedimento(pedimento);
		detallePartida.setDtpSAP(contenedor);
		detallePartida.setDtpLote(lote);
		detallePartida.setDtpPO(otro);      
		detallePartida.setDtpCaducidad(fechaCaducidad);
		detallePartida.setPartida(partida);
		
		partida.getDetallePartidaList().add(detallePartida);
		
		
		this.listadoPartida.add(partida);
		
		
		//FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Agregado","Se agrego el registro correctamente"));
		//PrimeFaces.current().ajax().update("form:messages");
	}
	
	public void saveConstanciaDepositoDetalle(){
		
		
		//---------------- FOLIO ----------------
		constanciaDeDeposito.setCteCve(clienteSelect);
		constanciaDeDeposito.setFechaIngreso(fechaIngreso);
		constanciaDeDeposito.setFolioCliente(noConstanciaSelect);
		constanciaDeDeposito.setAvisoCve(avisoSelect);
		
		ConstanciaDeDeposito constanciaDeDeposito = new ConstanciaDeDeposito();
		constanciaDeDeposito.setFechaIngreso(fechaIngreso);
		
		ConstanciaDepositoDetalle constanciaDD = new ConstanciaDepositoDetalle();
		constanciaDD.setServicioCve(precioServicioSelect.getServicio());
		constanciaDD.setFolio(constanciaDeDeposito);
		constanciaDD.setServicioCantidad(cantidadServicio);
		listadoConstanciaDepositoDetalle.add(constanciaDD);
		
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Agregado","Se agrego el registro correctamente"));
		PrimeFaces.current().ajax().update("form:messages","form:dt.constanciaDD");
		
	}
	
	public void renderConstanciaDeDeposito(){
		
		if(avisoSelect != null ) {
			if(avisoSelect.getAvisoCaducidad() == false ) {
				respuesta = false;
				this.fechaCaducidad= null;
				this.pedimento = null;
				this.contenedor = null;
				this.otro = null;
				this.lote = null;
			}else {
				respuesta = true;
			}
			
		}
		
	}
	
	public void limpiar() {
		productoSelect = new Producto();
		unidadDeManejoSelect = new UnidadDeManejo();
		posicionCamaraSelect = new Posicion() ;
		posicionCamaraSelect = new Posicion();
		
		cantidadTotal = 0;
		pesoTotal = new BigDecimal("0.00").setScale(2);
		numTarimas = new BigDecimal("0.00").setScale(2);
		unidadesPorTarima = new BigDecimal("0.00").setScale(2);
		valorMercancia = new BigDecimal("0.00").setScale(2);
		pedimento = "";
		contenedor = "";
		lote = "";
		fechaCaducidad = new Date();
		otro = "";
		
		precioServicioSelect = new PrecioServicio();
		cantidadServicio = new BigDecimal("0.00").setScale(2);
		temperatura = "";
		metroCamara = new BigDecimal("0.00").setScale(2);
		nombreTransportista = "";
		placas = "";
		observacion = "";
		listadoPartida = new ArrayList<Partida>();
		listadoConstanciaDepositoDetalle = new ArrayList<ConstanciaDepositoDetalle>();
		
	}
	
	public void saveConstanciaDeDeposito() {
		
	
		constanciaDeDeposito.setNombreTransportista(nombreTransportista);
		constanciaDeDeposito.setPlacasTransporte(placas);
		constanciaDeDeposito.setObservaciones(observacion);
		constanciaDeDeposito.setTemperatura(temperatura);
		constanciaDeDeposito.setConstanciaDepositoDetalleList(listadoConstanciaDepositoDetalle);
		constanciaDeDeposito.setPartidaList(listadoPartida);
		
		constanciaDAO.guardar(constanciaDeDeposito);
		
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Agregado","Se agrego el registro correctamente"));
		PrimeFaces.current().ajax().update("form:messages");
		
	}
	
	public void deleteSelectedPartidas() {
		
		for(Partida p: listadoPartida) {
			if(p==selectedPartida) {
				p.setPartidaCve(1);
				selectedPartida.setPartidaCve(1);
				System.out.println(p);
				System.out.println(selectedPartida);
			}
		}
		
		listadoPartida.remove(this.selectedPartida);//remueve todos los elementos de listadoPartida ERROR
		this.selectedPartidas = null;
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Eliminado","Se elimino el registro correctamente"));
		PrimeFaces.current().ajax().update("form:messages","form:dt-partidas");
	}
	
	public void deleteConstanciaDD() {
		
		for(ConstanciaDepositoDetalle cdd: listadoConstanciaDepositoDetalle) {
			if(cdd==selectedConstanciasDD) {
				cdd.setConstanciaDepositoDetalleCve(1);
				selectedConstanciasDD.setConstanciaDepositoDetalleCve(1);
				System.out.println(cdd);
				System.out.println(selectedConstanciaDD);
			}
		}
		
		listadoConstanciaDepositoDetalle.remove(this.selectedConstanciasDD);//lo remueve cuando la constanciadepositodetallecve tiene un Id, mientras no lo hace 
		this.selectedConstanciasDD = null;
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Eliminado","Se elimino el registro correctamente"));
		PrimeFaces.current().ajax().update("form:messages","form:dt-constanciaDD");
	}
	
	

}
