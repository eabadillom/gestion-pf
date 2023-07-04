package mx.com.ferbo.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.apache.log4j.Logger;

import mx.com.ferbo.dao.ClienteDAO;
import mx.com.ferbo.model.Cliente;

@Named
@ViewScoped
public class ReporteIngresosBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(ReporteAlmacenFechaBean.class);
	
	private List<Cliente> listCliente;
	
	private Cliente clienteSelect;
	
	private ClienteDAO clienteDAO;
	
	private Date fechaInicio;
	private Date fechaFin;
	
	public ReporteIngresosBean() {
		
		listCliente = new ArrayList<Cliente>();
		
		clienteDAO = new ClienteDAO();
		
		clienteSelect = new Cliente();
		
	}
	
	@PostConstruct
	public void init() {
		
		fechaInicio = new Date();
		fechaFin = new Date();
		listCliente = clienteDAO.buscarTodos();
		
	}

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Date getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	public List<Cliente> getListCliente() {
		return listCliente;
	}

	public void setListCliente(List<Cliente> listCliente) {
		this.listCliente = listCliente;
	}

	public Cliente getClienteSelect() {
		return clienteSelect;
	}

	public void setClienteSelect(Cliente clienteSelect) {
		this.clienteSelect = clienteSelect;
	}
	
	
	public void exportarPdf() {
		
		log.info("exṕrtando a PDF");
		
	}
	
	public void sleep() throws InterruptedException {
        TimeUnit.SECONDS.sleep(5);
    }
	
	public void exportarExcel() {
		
	}

	
}
