package mx.com.ferbo.controller;

import java.io.File;
import java.io.Serializable;
import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;

import mx.com.ferbo.dao.ClienteDAO;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.util.EntityManagerUtil;
import mx.com.ferbo.util.JasperReportUtil;
import mx.com.ferbo.util.conexion;

@Named
@ViewScoped

public class ReporteNotaCreditoBean implements Serializable{
	
	
	
	
	private static final long serialVersionUID = 1L;
	private static Logger log = LogManager.getLogger(ReporteNotaCreditoBean.class);
	
	private Cliente clienteSelect;
	private ClienteDAO clienteDAO;
	private List<Cliente> listCliente;
	
	private Date fechaInicio;
	private Date fechaFin;
	private Date fechaActual;
	private Date maxDate;
	
	public ReporteNotaCreditoBean(){
		listCliente = new ArrayList<Cliente>();
		clienteSelect = new Cliente();
		clienteDAO = new ClienteDAO();
	}
	
	@PostConstruct
	public void init() {
		
		listCliente = clienteDAO.buscarTodos();
		
		fechaInicio = new Date();
		fechaFin = new Date();
		fechaActual = new Date();
		Date today = new Date();
		long oneDay = 24 * 60 * 60 * 1000;

		maxDate = new Date(today.getTime() );
	}

	public Cliente getClienteSelect() {
		return clienteSelect;
	}

	public void setClienteSelect(Cliente clienteSelect) {
		this.clienteSelect = clienteSelect;
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

	public Date getFechaActual() {
		return fechaActual;
	}

	public void setFechaActual(Date fechaActual) {
		this.fechaActual = fechaActual;
	}

	public List<Cliente> getListCliente() {
		return listCliente;
	}

	public void setListCliente(List<Cliente> listCliente) {
		this.listCliente = listCliente;
	}
	
	public void exportarPdf() {
		
		log.info("exṕrtando a PDF");
		String jasperPath = "/jasper/NotasCredito.jrxml";
		String filename = "reporteNotasCredito"+fechaActual+".pdf";
		String images = "/images/logo.jpeg";
		String message = null;
		Severity severity = null;
		File reportFile = new File(jasperPath);
		File imgfile = null;
		JasperReportUtil jasperReportUtil = new JasperReportUtil();
		Map<String, Object> parameters = new HashMap<String, Object>();
		Connection connection = null;
		parameters = new HashMap<String, Object>();
		
		try {
		
			URL resource = getClass().getResource(jasperPath);
			URL resourceimg = getClass().getResource(images);
			String file = resource.getFile();
			String img = resourceimg.getFile();
			reportFile = new File(file);
			imgfile = new File(img);
			log.info(reportFile.getPath());
		
			Integer clienteCve = null;
			if(clienteSelect == null) {
				clienteCve = null;
			}else {
				clienteCve = clienteSelect.getCteCve();
			}
			
		
			connection = EntityManagerUtil.getConnection();
			parameters.put("REPORT_CONNECTION", connection);
			parameters.put("idCliente",clienteCve );
			parameters.put("fechaInicio", fechaInicio);
			parameters.put("fechaFin", fechaFin);
			parameters.put("imagen", imgfile.getPath());
			log.info("Parametros: " + parameters.toString());
			jasperReportUtil.createPdf(filename, parameters, reportFile.getPath());
		} catch (Exception ex) {
			log.error("Problema general...", ex);
			message = String.format("No se pudo imprimir el reporte");
			severity = FacesMessage.SEVERITY_INFO;
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, "Error en impresion", message));
			PrimeFaces.current().ajax().update("form:messages");
		} finally {
			conexion.close((Connection) connection);
		}
		
		
	}
	
	public void sleep() throws InterruptedException {
        TimeUnit.SECONDS.sleep(5);
    }
	
	public void exportarExcel() {
		log.info("exṕrtando a PDF");
		String jasperPath = "/jasper/NotasCredito.jrxml";
		String filename = "reporteNotasCredito"+fechaActual+".xlsx";
		String images = "/images/logo.jpeg";
		String message = null;
		Severity severity = null;
		File reportFile = new File(jasperPath);
		File imgfile = null;
		JasperReportUtil jasperReportUtil = new JasperReportUtil();
		Map<String, Object> parameters = new HashMap<String, Object>();
		Connection connection = null;
		parameters = new HashMap<String, Object>();
		
		try {
		
			URL resource = getClass().getResource(jasperPath);
			URL resourceimg = getClass().getResource(images);
			String file = resource.getFile();
			String img = resourceimg.getFile();
			reportFile = new File(file);
			imgfile = new File(img);
			log.info(reportFile.getPath());
		
			Integer clienteCve = null;
			if(clienteSelect == null) {
				clienteCve = null;
			}else {
				clienteCve = clienteSelect.getCteCve();
			}
			
		
			connection = EntityManagerUtil.getConnection();
			parameters.put("REPORT_CONNECTION", connection);
			parameters.put("idCliente",clienteCve );
			parameters.put("fechaInicio", fechaInicio);
			parameters.put("fechaFin", fechaFin);
			parameters.put("imagen", imgfile.getPath());
			log.info("Parametros: " + parameters.toString());
			jasperReportUtil.createXlsx(filename, parameters, reportFile.getPath());
		} catch (Exception ex) {
			log.error("Problema general...", ex);
			message = String.format("No se pudo imprimir el reporte");
			severity = FacesMessage.SEVERITY_INFO;
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, "Error en impresion", message));
			PrimeFaces.current().ajax().update("form:messages");
		} finally {
			conexion.close((Connection) connection);
		}
		
	}

	public Date getMaxDate() {
		return maxDate;
	}

	public void setMaxDate(Date maxDate) {
		this.maxDate = maxDate;
	}
	
	

}
