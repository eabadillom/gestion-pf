package mx.com.ferbo.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import mx.com.ferbo.dao.ClienteDAO;
import mx.com.ferbo.dao.EmisoresCFDISDAO;
import mx.com.ferbo.dao.VentaDAO;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.EmisoresCFDIS;
import mx.com.ferbo.model.Ventas;

@Named
@ViewScoped
public class VentasBean implements Serializable{

	private static final long serialVersionUID = -1785488265380235016L;
	
	private VentaDAO ventaDAO;
	private List<Ventas> listVenta;
	private Ventas venta;
	
	private ClienteDAO clienteDAO;
	private List<Cliente> listCliente;
	
	private EmisoresCFDISDAO emisorDAO;
	private List<EmisoresCFDIS> listEmisores;
	
	
	public VentasBean() {		
		
		clienteDAO = new ClienteDAO();
		listCliente = new ArrayList<>();
		
		emisorDAO = new EmisoresCFDISDAO();
		listEmisores = new ArrayList<>();
		
		ventaDAO = new VentaDAO();		
		listVenta = new ArrayList<>();
		venta = new Ventas();
		
	}
	
	
	@PostConstruct
	public void init() {
		
		listCliente = clienteDAO.findall();
		listEmisores = emisorDAO.findall(false);
		listVenta = ventaDAO.buscarTodos();
		
	}


	public List<Ventas> getListVenta() {
		return listVenta;
	}


	public void setListVenta(List<Ventas> listVenta) {
		this.listVenta = listVenta;
	}


	public List<Cliente> getListCliente() {
		return listCliente;
	}


	public void setListCliente(List<Cliente> listCliente) {
		this.listCliente = listCliente;
	}


	public List<EmisoresCFDIS> getListEmisores() {
		return listEmisores;
	}


	public void setListEmisores(List<EmisoresCFDIS> listEmisores) {
		this.listEmisores = listEmisores;
	}


	public Ventas getVenta() {
		return venta;
	}


	public void setVenta(Ventas venta) {
		this.venta = venta;
	}
	
	
}
