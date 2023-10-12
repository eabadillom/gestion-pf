package mx.com.ferbo.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.dao.ClienteDAO;
import mx.com.ferbo.dao.OrdenSalidaDAO;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.OrdenSalida;


@Named
@ViewScoped
public class OrdenSalidaBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private static Logger log = LogManager.getLogger(OrdenSalidaBean.class);
	
	private ClienteDAO clienteDAO;
	private OrdenSalidaDAO ordenSalidaDAO;
	private List<Cliente> listaClientes;
	private List<OrdenSalida> listaOrdenSalida;
	private boolean confirmacion;
	private Cliente clienteSelect;
	private OrdenSalida ordensalida;
	
	public OrdenSalidaBean() {
		clienteDAO = new ClienteDAO();
		ordenSalidaDAO = new OrdenSalidaDAO();
		
		listaOrdenSalida = new ArrayList<OrdenSalida>();
		listaClientes = new ArrayList<Cliente>();
		
	}
	@PostConstruct
	public void init() {
		listaClientes = clienteDAO.buscarHabilitados(true);
		listaOrdenSalida = ordenSalidaDAO.buscarTodos();
	}
	
	public void guardar() {
		
	}
	
	public void reload() throws IOException {
	    ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
	    ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
	}
	
	public Cliente getClienteSelect() {
		return clienteSelect;
	}
	public void setClienteSelect(Cliente clienteSelect) {
		this.clienteSelect = clienteSelect;
	}
	public List<Cliente> getListaClientes() {
		return listaClientes;
	}
	public void setListaClientes(List<Cliente> listaClientes) {
		this.listaClientes = listaClientes;
	}
	public OrdenSalida getOrdensalida() {
		return ordensalida;
	}
	public void setOrdensalida(OrdenSalida ordensalida) {
		this.ordensalida = ordensalida;
	}
	public List<OrdenSalida> getListaOrdenSalida() {
		return listaOrdenSalida;
	}
	public void setListaOrdenSalida(List<OrdenSalida> listaOrdenSalida) {
		this.listaOrdenSalida = listaOrdenSalida;
	}
	public boolean isConfirmacion() {
		return confirmacion;
	}
	public void setConfirmacion(boolean confirmacion) {
		this.confirmacion = confirmacion;
	}
	
}
