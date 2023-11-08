package mx.com.ferbo.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import mx.com.ferbo.dao.ClienteDAO;
import mx.com.ferbo.dao.IngresoDAO;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.Ingreso;
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
	
	private Date fechaActual;
	
	public OrdenEntradaBean(){
		
		listaClientes = new ArrayList<Cliente>();
		clienteDAO = new ClienteDAO();
		cliente = new Cliente();
		
		listaIngreso = new ArrayList<Ingreso>();
		ingresoDAO = new IngresoDAO();
		ingreso = new Ingreso();
		
		fechaActual = new Date();
		
	}
	
	@PostConstruct
	public void init() {
		
		listaClientes = clienteDAO.buscarTodos();
		
	}
	
	public void ordenesEntrada() {
		
		FacesContext faceContext = null;
		HttpServletRequest request = null;
		HttpSession session = null;
		Usuario usuario = null;
		
		try {
			
			Date fechaActualIni = new Date(fechaActual.getTime());
			DateUtil.setTime(fechaActualIni, 0, 0, 0, 0);
			
			Date fechaActualFin = new Date(fechaActual.getTime());
			DateUtil.setTime(fechaActualFin, 23, 59, 59, 99);
			
			faceContext = FacesContext.getCurrentInstance();
	        request = (HttpServletRequest) faceContext.getExternalContext().getRequest();
	        session = request.getSession(false);
	        
	        usuario = (Usuario) session.getAttribute("usuario");
	        
	        listaIngreso = ingresoDAO.buscarPorFechaCtePlanta(fechaActualIni, fechaActualFin, cliente.getCteCve(),usuario.getIdPlanta());
			
		} catch (Exception e) {
			log.info("Error ...." + e.getMessage());
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
	

}
