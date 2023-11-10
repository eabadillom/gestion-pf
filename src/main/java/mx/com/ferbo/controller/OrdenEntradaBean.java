package mx.com.ferbo.controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;

import mx.com.ferbo.dao.CamaraDAO;
import mx.com.ferbo.dao.ClienteDAO;
import mx.com.ferbo.dao.IngresoDAO;
import mx.com.ferbo.dao.IngresoProductoDAO;
import mx.com.ferbo.dao.PlantaDAO;
import mx.com.ferbo.dao.ProductoClienteDAO;
import mx.com.ferbo.dao.UnidadDeManejoDAO;
import mx.com.ferbo.model.Camara;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.Ingreso;
import mx.com.ferbo.model.IngresoProducto;
import mx.com.ferbo.model.Planta;
import mx.com.ferbo.model.Producto;
import mx.com.ferbo.model.ProductoPorCliente;
import mx.com.ferbo.model.UnidadDeManejo;
import mx.com.ferbo.model.Usuario;
import mx.com.ferbo.util.DateUtil;

@Named
@ViewScoped
public class OrdenEntradaBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private static Logger log = LogManager.getLogger(OrdenEntradaBean.class);
	
	private List<Cliente> listaClientes;
	private ClienteDAO clienteDAO;
	private Cliente cliente;
	
	private List<Ingreso> listaIngreso;
	private IngresoDAO ingresoDAO;
	private Ingreso ingreso;
	
	private List<ProductoPorCliente> listaProductoPorCte;
	private ProductoClienteDAO productoClienteDAO;
	
	private List<Producto> listaProductos;
	private Producto producto;
	
	private List<Planta> listaPlantas;
	private PlantaDAO plantaDAO;
	private Planta planta;
	
	private List<Camara> listaCamara;
	private CamaraDAO camaraDAO;
	private Camara camara;
	
	private List<UnidadDeManejo> listaUnidadDeManejo;
	private UnidadDeManejoDAO unidadDeManejoDAO;
	private UnidadDeManejo unidadDeManejo;
	
	private List<IngresoProducto> listaIngresoProducto;
	private IngresoProducto ingresoProducto;
	private IngresoProductoDAO ingresoProductoDAO;
	
	private Date fechaActual;
	private Integer tarima;
	private BigDecimal sumaTarimas;
	private BigDecimal sumaCantidad;
	private BigDecimal sumaPeso;
	
	FacesContext faceContext = null;
	HttpServletRequest request = null;
	HttpSession session = null;
	Usuario usuario = null;
	
	public OrdenEntradaBean(){
		
		listaClientes = new ArrayList<Cliente>();
		clienteDAO = new ClienteDAO();
		cliente = new Cliente();
		
		listaIngreso = new ArrayList<Ingreso>();
		ingresoDAO = new IngresoDAO();
		ingreso = new Ingreso();
		
		listaProductoPorCte = new ArrayList<ProductoPorCliente>();
		productoClienteDAO = new ProductoClienteDAO();
		
		listaProductos = new ArrayList<Producto>();
		producto = new Producto();
		
		listaPlantas = new ArrayList<Planta>();
		plantaDAO = new PlantaDAO();
		planta = new Planta();
		
		listaCamara = new ArrayList<Camara>();
		camaraDAO = new CamaraDAO();
		camara = new Camara();
		
		listaUnidadDeManejo = new ArrayList<UnidadDeManejo>();
		unidadDeManejoDAO = new UnidadDeManejoDAO();
		unidadDeManejo = new UnidadDeManejo();
		
		listaIngresoProducto = new ArrayList<IngresoProducto>();
		ingresoProductoDAO = new IngresoProductoDAO();
		ingresoProducto = new IngresoProducto();
				
		fechaActual = new Date();
		ingresoProducto.setNoTarimas(new BigDecimal(1));
		sumaTarimas = new BigDecimal(0);
		sumaCantidad = new BigDecimal(0);
		sumaPeso = new BigDecimal(0);
		
	}
	
	@PostConstruct
	public void init() {
		
		faceContext = FacesContext.getCurrentInstance();
        request = (HttpServletRequest) faceContext.getExternalContext().getRequest();
        session = request.getSession(false);
        
        usuario = (Usuario) session.getAttribute("usuario");
		
		listaClientes = clienteDAO.buscarTodos();
		listaPlantas = plantaDAO.buscarTodos();
		listaUnidadDeManejo = unidadDeManejoDAO.buscarTodos();
		
	}	
	
	public void cargaInfoCliente() {
		
		ordenesEntrada();
		productoCte();		
	}
	
	public void ordenesEntrada() {		
		
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		
		
		try {
			
			Date fechaActualIni = new Date(fechaActual.getTime());
			DateUtil.setTime(fechaActualIni, 0, 0, 0, 0);
			
			Date fechaActualFin = new Date(fechaActual.getTime());
			DateUtil.setTime(fechaActualFin, 23, 59, 59, 99);	        	       
	        
	        listaIngreso = ingresoDAO.buscarPorFechaCtePlanta(fechaActualIni, fechaActualFin, cliente.getCteCve(),usuario.getIdPlanta());
			
	        severity = FacesMessage.SEVERITY_INFO;
	        mensaje = "Selecciona Folio de Orden de Entrada";
		} catch (Exception e) {
			log.info("Error ...." + e.getMessage());
			
			severity = FacesMessage.SEVERITY_INFO;
	        mensaje = "Error al encontrar ordenes de entrada";
			
		}finally {
			message = new FacesMessage(severity, "Orden Entrada", mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
			PrimeFaces.current().ajax().update("form:messages");
		}
		
	}
	
	public void productoCte() {
		
		listaProductos.clear();
		listaProductoPorCte = productoClienteDAO.buscarPorCliente(cliente.getCteCve(), false);
		
		for(ProductoPorCliente pc: listaProductoPorCte) {			
			Producto prod = pc.getProductoCve();
			listaProductos.add(prod);
		}
		
	}
	
	public void camaraDisponible() {
		
		listaCamara = camaraDAO.buscarPorPlanta(planta);
		
	}
	
	public void addIngresoProducto() {
				
		IngresoProducto ingresoProducto = null;
		int tarima = this.ingresoProducto.getNoTarimas().intValue();
		
		
		try {
			
			for(int i=0;i<tarima;i++) {
				this.ingresoProducto.setNoTarimas(new BigDecimal(1));
				sumaTarimas = sumaTarimas.add(this.ingresoProducto.getNoTarimas());
				sumaCantidad = sumaCantidad.add(new BigDecimal(this.ingresoProducto.getCantidad()));
				sumaPeso = sumaPeso.add(this.ingresoProducto.getPeso());
				ingresoProducto = (IngresoProducto) this.ingresoProducto.clone();
				listaIngresoProducto.add(ingresoProducto);				
			}
			
			this.ingresoProducto = new IngresoProducto();
			
		} catch (Exception e) {
			log.info("Error al agregar ingreso Producto" + e.getMessage());
		}
		
	}
	
	public void productoIngresoOrden() {
		
		listaIngresoProducto.clear();
		sumaTarimas = new BigDecimal(0);
		sumaCantidad = new BigDecimal(0);
		sumaPeso = new BigDecimal(0);
		
		List<IngresoProducto> listaTmpIngresoP = ingresoProductoDAO.buscarIngresosProductoPorId(ingreso.getIdIngreso());
		
		for(IngresoProducto ip: listaTmpIngresoP){
			
			sumaTarimas = sumaTarimas.add(ip.getNoTarimas());
			sumaCantidad = sumaCantidad.add(new BigDecimal(ip.getCantidad()));
			sumaPeso = sumaPeso.add(ip.getPeso());
			
			listaIngresoProducto.add(ip);
			
		}
		
	}
	
	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public List<Cliente> getListaClientes() {
		return listaClientes;
	}

	public void setListaClientes(List<Cliente> listaClientes) {
		this.listaClientes = listaClientes;
	}

	public List<Ingreso> getListaIngreso() {
		return listaIngreso;
	}

	public void setListaIngreso(List<Ingreso> listaIngreso) {
		this.listaIngreso = listaIngreso;
	}

	public Ingreso getIngreso() {
		return ingreso;
	}

	public void setIngreso(Ingreso ingreso) {
		this.ingreso = ingreso;
	}

	public List<ProductoPorCliente> getListaProductoPorCte() {
		return listaProductoPorCte;
	}

	public void setListaProductoPorCte(List<ProductoPorCliente> listaProductoPorCte) {
		this.listaProductoPorCte = listaProductoPorCte;
	}

	public List<Producto> getListaProductos() {
		return listaProductos;
	}

	public void setListaProductos(List<Producto> listaProductos) {
		this.listaProductos = listaProductos;
	}

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public Planta getPlanta() {
		return planta;
	}

	public void setPlanta(Planta planta) {
		this.planta = planta;
	}

	public List<Planta> getListaPlantas() {
		return listaPlantas;
	}

	public void setListaPlantas(List<Planta> listaPlantas) {
		this.listaPlantas = listaPlantas;
	}

	public List<Camara> getListaCamara() {
		return listaCamara;
	}

	public void setListaCamara(List<Camara> listaCamara) {
		this.listaCamara = listaCamara;
	}

	public Camara getCamara() {
		return camara;
	}

	public void setCamara(Camara camara) {
		this.camara = camara;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public IngresoProducto getIngresoProducto() {
		return ingresoProducto;
	}

	public void setIngresoProducto(IngresoProducto ingresoProducto) {
		this.ingresoProducto = ingresoProducto;
	}

	public List<UnidadDeManejo> getListaUnidadDeManejo() {
		return listaUnidadDeManejo;
	}

	public void setListaUnidadDeManejo(List<UnidadDeManejo> listaUnidadDeManejo) {
		this.listaUnidadDeManejo = listaUnidadDeManejo;
	}

	public UnidadDeManejo getUnidadDeManejo() {
		return unidadDeManejo;
	}

	public void setUnidadDeManejo(UnidadDeManejo unidadDeManejo) {
		this.unidadDeManejo = unidadDeManejo;
	}

	public Integer getTarima() {
		return tarima;
	}

	public void setTarima(Integer tarima) {
		this.tarima = tarima;
	}

	public List<IngresoProducto> getListaIngresoProducto() {
		return listaIngresoProducto;
	}

	public void setListaIngresoProducto(List<IngresoProducto> listaIngresoProducto) {
		this.listaIngresoProducto = listaIngresoProducto;
	}

	public BigDecimal getSumaTarimas() {
		return sumaTarimas;
	}

	public void setSumaTarimas(BigDecimal sumaTarimas) {
		this.sumaTarimas = sumaTarimas;
	}

	public BigDecimal getSumaCantidad() {
		return sumaCantidad;
	}

	public void setSumaCantidad(BigDecimal sumaCantidad) {
		this.sumaCantidad = sumaCantidad;
	}

	public BigDecimal getSumaPeso() {
		return sumaPeso;
	}

	public void setSumaPeso(BigDecimal sumaPeso) {
		this.sumaPeso = sumaPeso;
	}
	

}
