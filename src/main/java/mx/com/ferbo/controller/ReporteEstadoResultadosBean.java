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
import mx.com.ferbo.dao.EmisoresCFDISDAO;
import mx.com.ferbo.model.EmisoresCFDIS;
import mx.com.ferbo.util.EntityManagerUtil;
import mx.com.ferbo.util.JasperReportUtil;
import mx.com.ferbo.util.conexion;

@Named
@ViewScoped
public class ReporteEstadoResultadosBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private static Logger log = LogManager.getLogger(ReporteAlmacenFechaBean.class);
	
	private List<EmisoresCFDIS> listaEmisores;
	
	private EmisoresCFDIS emisor;
	
	private ClienteDAO clienteDAO;
	private EmisoresCFDISDAO emisorDAO;
	
	private Date fechaInicio;
	private Date fechaFin;
	private Date maxDate;
	private Date mesActual;
	
	public ReporteEstadoResultadosBean() {
		
		listaEmisores = new ArrayList<EmisoresCFDIS>();
		
		clienteDAO = new ClienteDAO();
		emisorDAO = new EmisoresCFDISDAO();
		emisor = new EmisoresCFDIS();
		
	}
	
	@PostConstruct
	public void init() {
		
		fechaInicio = new Date();
		fechaFin = new Date();
		mesActual = new Date();
		
		listaEmisores =emisorDAO.buscarTodos();
		
		Date today = new Date();
		long oneDay = 24 * 60 * 60 * 1000;

		maxDate = new Date(today.getTime() );
	}


	public void exportarPdf() {
		
		log.info("exṕrtando a PDF");
		String jasperPath = "/jasper/Ingresos.jrxml";
		String filename = "reporteEstadoCuenta"+mesActual+".pdf";
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
		
			String emi = null;
			if(emisor.getNb_emisor() == null) {
				emi = null;
			}else {
				emi = emisor.getNb_emisor();
			}
		
			connection = EntityManagerUtil.getConnection();
			parameters.put("REPORT_CONNECTION", connection);
			parameters.put("emisor",emisor.getNb_emisor());
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
	
	
	public void exportarExcel() {
		
		log.info("exṕrtando a PDF");
		String jasperPath = "/jasper/Ingresos.jrxml";
		String filename = "reporteIngresos"+mesActual+".xlsx";
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
		
			String emi = null;
			if(emisor.getNb_emisor() == null) {
				emi = null;
			}else {
				emi = emisor.getNb_emisor();
			}
			
		
			connection = EntityManagerUtil.getConnection();
			parameters.put("REPORT_CONNECTION", connection);
			parameters.put("emisor",emisor.getNb_emisor());
			parameters.put("fechaInicio", fechaInicio);
			parameters.put("fechaFin", fechaFin);
			parameters.put("Imagen", imgfile.getPath());
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

	public Date getMesActual() {
		return mesActual;
	}

	public void setMesActual(Date mesActual) {
		this.mesActual = mesActual;
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

	public EmisoresCFDIS getEmisor() {
		return emisor;
	}

	public void setEmisor(EmisoresCFDIS emisor) {
		this.emisor = emisor;
	}

	public EmisoresCFDISDAO getEmisorDAO() {
		return emisorDAO;
	}

	public void setEmisorDAO(EmisoresCFDISDAO emisorDAO) {
		this.emisorDAO = emisorDAO;
	}

	public List<EmisoresCFDIS> getListaEmisores() {
		return listaEmisores;
	}

	public void setListaEmisores(List<EmisoresCFDIS> listaEmisores) {
		this.listaEmisores = listaEmisores;
	}
	
}
