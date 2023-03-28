package mx.com.ferbo.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import mx.com.ferbo.dao.ClienteDAO;
import mx.com.ferbo.model.Cliente;

@Named
@ViewScoped
public class FacturacionConstanciasBean implements Serializable{
	
	private static final long serialVersionUID = -1785488265380235016L;
	
	private Cliente clienteSelect;
	
	private ClienteDAO clienteDAO;
	
	private List<Cliente> listaCliente;
	
	
	public FacturacionConstanciasBean() {
		
		clienteDAO = new ClienteDAO();
		
		listaCliente = new ArrayList<>();
		
		
	}
	
	@PostConstruct
	public void init() {
		
		listaCliente = clienteDAO.buscarTodos();
	}

	public Cliente getClienteSelect() {
		return clienteSelect;
	}

	public void setClienteSelect(Cliente clienteSelect) {
		this.clienteSelect = clienteSelect;
	}

	public List<Cliente> getListaCliente() {
		return listaCliente;
	}

	public void setListaCliente(List<Cliente> listaCliente) {
		this.listaCliente = listaCliente;
	}
	

	public void domicilioCliente() {
		
	}
	
	

}
