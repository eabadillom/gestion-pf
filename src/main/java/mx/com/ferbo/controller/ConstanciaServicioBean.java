package mx.com.ferbo.controller;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import mx.com.ferbo.dao.ClienteDAO;
import mx.com.ferbo.dao.ConstanciaServicioDAO;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.ConstanciaDeServicio;

@Named
@ViewScoped
public class ConstanciaServicioBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private List<Cliente> listaClientes;
	private List<ConstanciaDeServicio> listaConstanciaServicios;
	
	private ClienteDAO clienteDao;
	private ConstanciaServicioDAO constanciaServicioDao;
	
	private ConstanciaDeServicio selectConstanciaDeServicio;
	
	private int idCliente;
	private Date fechaInicio;
	private Date fechaFinal;
	private int folio;
	
	public ConstanciaServicioBean() {
		clienteDao = new ClienteDAO();
		constanciaServicioDao = new ConstanciaServicioDAO();
		listaClientes = new ArrayList<>();
		listaConstanciaServicios = new ArrayList<>();
	}
	
	@PostConstruct
	public void init() {
		listaClientes = clienteDao.buscarTodos();
		idCliente = 0;
		fechaInicio = new Date();
		fechaFinal = new Date();
		folio = 0;
	}
	
	public void buscarConstancia() {
		System.out.println(getIdCliente() + " " + getFechaInicio() + " " + getFechaFinal() + " " + getFolio());
		System.out.println(folio + " " + fechaInicio + " " + fechaFinal + " " + idCliente);
		listaConstanciaServicios = constanciaServicioDao.buscarPorCriterio(folio, fechaInicio, fechaFinal, idCliente);
		for (ConstanciaDeServicio constanciaDeServicio : listaConstanciaServicios) {
			System.out.println(constanciaDeServicio);
		}
	}

	public List<Cliente> getListaClientes() {
		return listaClientes;
	}

	public void setListaClientes(List<Cliente> listaClientes) {
		this.listaClientes = listaClientes;
	}

	public List<ConstanciaDeServicio> getListaConstanciaServicios() {
		return listaConstanciaServicios;
	}

	public void setListaConstanciaServicios(List<ConstanciaDeServicio> listaConstanciaServicios) {
		this.listaConstanciaServicios = listaConstanciaServicios;
	}

	public ClienteDAO getClienteDao() {
		return clienteDao;
	}

	public void setClienteDao(ClienteDAO clienteDao) {
		this.clienteDao = clienteDao;
	}

	public ConstanciaServicioDAO getConstanciaServicioDao() {
		return constanciaServicioDao;
	}

	public void setConstanciaServicioDao(ConstanciaServicioDAO constanciaServicioDao) {
		this.constanciaServicioDao = constanciaServicioDao;
	}

	public ConstanciaDeServicio getSelectConstanciaDeServicio() {
		return selectConstanciaDeServicio;
	}

	public void setSelectConstanciaDeServicio(ConstanciaDeServicio selectConstanciaDeServicio) {
		this.selectConstanciaDeServicio = selectConstanciaDeServicio;
	}

	public int getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(int idCliente) {
		this.idCliente = idCliente;
	}

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Date getFechaFinal() {
		return fechaFinal;
	}

	public void setFechaFinal(Date fechaFinal) {
		this.fechaFinal = fechaFinal;
	}

	public int getFolio() {
		return folio;
	}

	public void setFolio(int folio) {
		this.folio = folio;
	}
	
}
