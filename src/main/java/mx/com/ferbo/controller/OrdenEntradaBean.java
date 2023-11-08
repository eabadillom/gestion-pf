package mx.com.ferbo.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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

import mx.com.ferbo.dao.ClienteDAO;
import mx.com.ferbo.dao.IngresoDAO;
import mx.com.ferbo.dao.ProductoClienteDAO;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.Ingreso;
import mx.com.ferbo.model.Producto;
import mx.com.ferbo.model.ProductoPorCliente;
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
	
	private Date fechaActual;
	
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
				
		fechaActual = new Date();
		
	}
	
	@PostConstruct
	public void init() {
		
		faceContext = FacesContext.getCurrentInstance();
        request = (HttpServletRequest) faceContext.getExternalContext().getRequest();
        session = request.getSession(false);
        
        usuario = (Usuario) session.getAttribute("usuario");
		
		listaClientes = clienteDAO.buscarTodos();
		
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
	

}
