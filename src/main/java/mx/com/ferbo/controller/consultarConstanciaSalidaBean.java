package mx.com.ferbo.controller;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import mx.com.ferbo.dao.ClienteDAO;
import mx.com.ferbo.dao.ConstanciaSalidaDAO;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.ConstanciaSalida;
import mx.com.ferbo.model.ConstanciaSalidaServicios;
import mx.com.ferbo.model.DetalleConstanciaSalida;
import mx.com.ferbo.util.EntityManagerUtil;

@Named
@ViewScoped

public class consultarConstanciaSalidaBean implements Serializable{
	
	private static final long serialVersionUID = -3109002730694247052L;
	
	private ConstanciaSalidaDAO constanciaSalidaDAO;
	private List<ConstanciaSalida> listadoConstanciaSalida;
	
	private Date fechaInicial;
	private Date fechaFinal;
	private String folio;
	
	private List<Cliente> listadoClientes;
	private ClienteDAO clienteDAO;
	private Cliente cliente;
	
	private ConstanciaSalida constanciaSelect;
	
	public consultarConstanciaSalidaBean() {
		constanciaSalidaDAO = new ConstanciaSalidaDAO();
		listadoConstanciaSalida = new ArrayList<>();
		
		listadoClientes = new ArrayList<Cliente>();
		clienteDAO = new ClienteDAO();
		
	}
	
	
	@PostConstruct
	public void init() {
		
		listadoClientes = clienteDAO.buscarTodos();
		
		fechaInicial = new Date();
		fechaFinal = new Date();
	}
	
	public void buscarConstanciaSalida() {
		EntityManager em = EntityManagerUtil.getEntityManager();
		EntityTransaction transaction = em.getTransaction();
		transaction.begin();
		constanciaSalidaDAO.setEm(em);
		
		System.out.println( getFechaInicial() + "" + getFechaFinal());
		System.out.println( fechaFinal + "" + fechaInicial);//si trae las fechas dadas por el usuario
		
		listadoConstanciaSalida = constanciaSalidaDAO.buscarPorCriterios(folio,fechaInicial, fechaFinal, cliente.getCteCve());
		
		for (ConstanciaSalida constanciaSalida : listadoConstanciaSalida) {
			List<ConstanciaSalidaServicios> alConstanciaSalidaS = constanciaSalida.getConstanciaSalidaServiciosList();
			alConstanciaSalidaS.size();
			List<DetalleConstanciaSalida> alConstanciaDD = constanciaSalida.getDetalleConstanciaSalidaList();
			alConstanciaDD.size();
		}
		
		transaction.commit();
		em.close();
	}



	//metodo get y setter
	
	public List<ConstanciaSalida> getListadoConstanciaSalida() {
		return listadoConstanciaSalida;
	}


	public void setListadoConstanciaSalida(List<ConstanciaSalida> listadoConstanciaSalida) {
		this.listadoConstanciaSalida = listadoConstanciaSalida;
	}

	public Date getFechaInicial() {
		return fechaInicial;
	}


	public void setFechaInicial(Date fechaInicial) {
		this.fechaInicial = fechaInicial;
	}


	public Date getFechaFinal() {
		return fechaFinal;
	}


	public void setFechaFinal(Date fechaFinal) {
		this.fechaFinal = fechaFinal;
	}


	public String getFolio() {
		return folio;
	}


	public void setFolio(String folio) {
		this.folio = folio;
	}


	public List<Cliente> getListadoClientes() {
		return listadoClientes;
	}


	public void setListadoClientes(List<Cliente> listadoClientes) {
		this.listadoClientes = listadoClientes;
	}


	public Cliente getCliente() {
		return cliente;
	}


	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}


	public ConstanciaSalida getConstanciaSelect() {
		return constanciaSelect;
	}


	public void setConstanciaSelect(ConstanciaSalida constanciaSelect) {
		this.constanciaSelect = constanciaSelect;
	}
	
	
	
	
}
