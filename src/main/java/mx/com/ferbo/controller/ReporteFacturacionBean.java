package mx.com.ferbo.controller;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
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
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;

import mx.com.ferbo.dao.StatusFacturaDAO;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.StatusFactura;
import mx.com.ferbo.util.EntityManagerUtil;
import mx.com.ferbo.util.JasperReportUtil;
import mx.com.ferbo.util.conexion;
import net.sf.jasperreports.engine.JRException;

@Named
@ViewScoped
public class ReporteFacturacionBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private static Logger log = LogManager.getLogger(ReporteFacturacionBean.class);
   
	private Date fecha;
	private Date fecha_ini;
	private Date fecha_fin;
	private Date maxDate;

	private Cliente clienteSelect;
	private StatusFactura statusFacturaSelect;

	private List<Cliente> listaClientes;
	private List<StatusFactura> listaStatusFactura;


	private StatusFacturaDAO statusFacturaDAO;
	
	private FacesContext faceContext;
    private HttpServletRequest request;

	public ReporteFacturacionBean() {
		fecha = new Date();
		
		statusFacturaDAO = new StatusFacturaDAO();
		listaClientes = new ArrayList<Cliente>();
		listaStatusFactura =new ArrayList<StatusFactura>();

	}
	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() {
		
		faceContext = FacesContext.getCurrentInstance();
        request = (HttpServletRequest) faceContext.getExternalContext().getRequest();
		
		listaClientes = (List<Cliente>) request.getSession(false).getAttribute("clientesActivosList");
		clienteSelect = new Cliente();
		statusFacturaSelect  = new StatusFactura();
		listaStatusFactura = statusFacturaDAO.buscarTodos();
		Date today = new Date();
		maxDate = new Date(today.getTime() );
		this.fecha_ini = new Date();
		this.fecha_fin = new Date();
	}
	

	
	public void exportarPdf() throws JRException, IOException, SQLException{
		System.out.println("Exportando a pdf.....");
			String jasperPath = "/jasper/ReporteFacturacion.jrxml";
			String filename = "ReporteFacturacionporFecha"+fecha+".pdf";
			String images = "/images/logoF.png";
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
				Integer statusCve = null;
				if(statusFacturaSelect == null) {
					statusCve =null;
				}else {
					statusCve  = statusFacturaSelect.getId();
				}
			
				connection = EntityManagerUtil.getConnection();
				parameters.put("REPORT_CONNECTION", connection);
				parameters.put("idCliente",clienteCve );
				parameters.put("fechaInicio", fecha_ini);
				parameters.put("fechaFin", fecha_fin);
				parameters.put("status", statusCve);
				parameters.put("imagen", imgfile.getPath());
				log.info("Parametros: " + parameters.toString());
				jasperReportUtil.createPdf(filename, parameters, reportFile.getPath());
			} catch (Exception ex) {
				log.error("Problema general...", ex);
				message = String.format("No se pudo imprimir el reporte");
				severity = FacesMessage.SEVERITY_INFO;
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, "Error en impresion", message));
				PrimeFaces.current().ajax().update("form:messages", "form:dt-inventarioEntradas");
			} finally {
				conexion.close((Connection) connection);
			}

		}
	
	public void sleep() throws InterruptedException {
        TimeUnit.SECONDS.sleep(1);
    }
	public void exportarExcel() throws JRException, IOException, SQLException{
		System.out.println("Exportando a excel.....");
			String jasperPath = "/jasper/ReporteFacturacion.jrxml";
			String filename = "ReporteFacturacionPorFecha" +fecha+".xlsx";
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
				
				Integer statusCve = null;
				if(statusFacturaSelect == null) {
					statusCve =null;
				}else {
					statusCve  = statusFacturaSelect.getId();
				}


			
				connection = EntityManagerUtil.getConnection();
				parameters.put("REPORT_CONNECTION", connection);
				parameters.put("idCliente",clienteCve );
				parameters.put("fechaInicio", fecha_ini);
				parameters.put("fechaFin", fecha_fin);
				parameters.put("status", statusCve);
				parameters.put("imagen", imgfile.getPath());
				log.info("Parametros: " + parameters.toString());
				jasperReportUtil.createXlsx(filename, parameters, reportFile.getPath());
			} catch (Exception ex) {
				log.error("Problema general...", ex);
				message = String.format("No se pudo imprimir el reporte");
				severity = FacesMessage.SEVERITY_INFO;
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, "Error en impresion", message));
				PrimeFaces.current().ajax().update("form:messages", "form:dt-inventarioEntradas");
			} finally {
				conexion.close((Connection) connection);
			}

		}
	
		
	
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
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
	public Date getFecha_ini() {
		return fecha_ini;
	}
	public void setFecha_ini(Date fecha_ini) {
		this.fecha_ini = fecha_ini;
	}
	public Date getFecha_fin() {
		return fecha_fin;
	}
	public void setFecha_fin(Date fecha_fin) {
		this.fecha_fin = fecha_fin;
	}
	public StatusFactura getStatusFacturaSelect() {
		return statusFacturaSelect;
	}
	public void setStatusFacturaSelect(StatusFactura statusFacturaSelect) {
		this.statusFacturaSelect = statusFacturaSelect;
	}
	public List<StatusFactura> getListaStatusFactura() {
		return listaStatusFactura;
	}
	public void setListaStatusFactura(List<StatusFactura> listaStatusFactura) {
		this.listaStatusFactura = listaStatusFactura;
	}
	public Date getMaxDate() {
		return maxDate;
	}
	public void setMaxDate(Date maxDate) {
		this.maxDate = maxDate;
	}



}
