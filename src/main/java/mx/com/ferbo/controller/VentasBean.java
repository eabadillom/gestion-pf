package mx.com.ferbo.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import mx.com.ferbo.dao.ClienteDAO;
import mx.com.ferbo.dao.EmisoresCFDISDAO;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.EmisoresCFDIS;

@Named
@ViewScoped
public class VentasBean implements Serializable{

	private static final long serialVersionUID = -1785488265380235016L;
	
	private String concepto;
	
	private Cliente clienteSelect;
	private ClienteDAO clienteDAO;
	private List<Cliente> listCliente;
	
	private EmisoresCFDIS emisorSelect;
	private EmisoresCFDISDAO emisorDAO;
	private List<EmisoresCFDIS> listEmisores;
	
	
	public VentasBean() {
		
		clienteDAO = new ClienteDAO();
		listCliente = new ArrayList<>();
		
		emisorDAO = new EmisoresCFDISDAO();
		listEmisores = new ArrayList<>();
		
	}
	
	
	@PostConstruct
	public void init() {
		
		listCliente = clienteDAO.findall();
		listEmisores = emisorDAO.findall(false);
		
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


	public Cliente getClienteSelect() {
		return clienteSelect;
	}


	public void setClienteSelect(Cliente clienteSelect) {
		this.clienteSelect = clienteSelect;
	}


	public EmisoresCFDIS getEmisorSelect() {
		return emisorSelect;
	}


	public void setEmisorSelect(EmisoresCFDIS emisorSelect) {
		this.emisorSelect = emisorSelect;
	}


	public String getConcepto() {
		return concepto;
	}


	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}
	
	
}
