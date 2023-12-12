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
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;

import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.util.EntityManagerUtil;
import mx.com.ferbo.util.JasperReportUtil;
import mx.com.ferbo.util.conexion;

@Named
@ViewScoped

public class ReporteCarteraClienteBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private static Logger log = LogManager.getLogger(ReporteAlmacenFechaBean.class);
	
	private Cliente clienteSelect;
	private List<Cliente> listCliente;
	
	private Date fecha;
	private Date maxDate;
	private String consulta = null;
	
	private FacesContext faceContext;
    private HttpServletRequest request;
	
	public ReporteCarteraClienteBean() {
		
		clienteSelect = new Cliente();
		listCliente = new ArrayList<Cliente>();
		
	}
	
	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init(){
		faceContext = FacesContext.getCurrentInstance();
        request = (HttpServletRequest) faceContext.getExternalContext().getRequest();
		
		listCliente = (List<Cliente>) request.getSession(false).getAttribute("clientesActivosList");
		
		fecha = new Date();
		Date today = new Date();

		maxDate = new Date(today.getTime() );
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getConsulta() {
		return consulta;
	}

	public void setConsulta(String consulta) {
		this.consulta = consulta;
	}

	public Cliente getClienteSelect() {
		return clienteSelect;
	}

	public void setClienteSelect(Cliente clienteSelect) {
		this.clienteSelect = clienteSelect;
	}

	public List<Cliente> getListCliente() {
		return listCliente;
	}

	public void setListCliente(List<Cliente> listCliente) {
		this.listCliente = listCliente;
	}

	public void exportarPdf() {
		
		System.out.println("Exportando a pdf.....");
		String jasperPath = null;
		String filename = null;
		String images = null;
		String message = null;
		Severity severity = null;
		File reportFile = null;
		File imgfile = null;
		JasperReportUtil jasperReportUtil = new JasperReportUtil();
		Map<String, Object> parameters = null;
		Connection connection = null;
		parameters = new HashMap<String, Object>();
		
		if (consulta.equals("Concentrada")) {
			
			log.info("Exportando Cartera de clientes concentrada a pdf...");
			jasperPath = "/jasper/Concentrada.jrxml";
			filename = "Concentrada" +fecha+".pdf";
			images = "/images/logo.jpeg";
			message = null;
			severity = null;
			reportFile = new File(jasperPath);
			imgfile = null;
			jasperReportUtil = new JasperReportUtil();					
			parameters = new HashMap<String, Object>();
			
			try {
			
				URL resource = getClass().getResource(jasperPath);
				URL resourceimg = getClass().getResource(images);
				String file = resource.getFile();
				String img = resourceimg.getFile();
				reportFile = new File(file);
				imgfile = new File(img);
				log.info(reportFile.getPath());
			
				Integer clienteCve =null;
				if(clienteSelect == null) {
					clienteCve = null; 
				}else {
					clienteCve = clienteSelect.getCteCve();
				}
				
			
				connection = EntityManagerUtil.getConnection();
				parameters.put("REPORT_CONNECTION", connection);
				parameters.put("idCliente",clienteCve );
				parameters.put("fecha",fecha );
				parameters.put("imagen", imgfile.getPath());
				log.info("Parametros: " + parameters.toString());
				jasperReportUtil.createPdf(filename, parameters, reportFile.getPath());
			} catch (Exception ex) {
				log.error("Problema general...", ex);
				message = String.format("No se pudo imprimir el reporte");
				severity = FacesMessage.SEVERITY_INFO;
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, "Error en impresion", message));
				PrimeFaces.current().ajax().update("form:messages", "form:dt-facturacionServicios");
			} finally {
				conexion.close((Connection) connection);
			}
			
		}else {
			
			log.info("Exportando Cartera de clientes desglosada a pdf...");
			jasperPath = "/jasper/Desglosada.jrxml";
			filename = "Desglosada" +fecha+".pdf";
			images = "/images/logo.jpeg";
			message = null;
			severity = null;
			reportFile = new File(jasperPath);
			imgfile = null;
			jasperReportUtil = new JasperReportUtil();					
			parameters = new HashMap<String, Object>();
			
			try {
			
				URL resource = getClass().getResource(jasperPath);
				URL resourceimg = getClass().getResource(images);
				String file = resource.getFile();
				String img = resourceimg.getFile();
				reportFile = new File(file);
				imgfile = new File(img);
				log.info(reportFile.getPath());
			
				Integer clienteCve =null;
				if(clienteSelect == null) {
					clienteCve = null; 
				}else {
					clienteCve = clienteSelect.getCteCve();
				}
				
			
				connection = EntityManagerUtil.getConnection();
				parameters.put("REPORT_CONNECTION", connection);
				parameters.put("idCliente",clienteCve );
				parameters.put("fecha",fecha );
				parameters.put("imagen", imgfile.getPath());
				log.info("Parametros: " + parameters.toString());
				jasperReportUtil.createPdf(filename, parameters, reportFile.getPath());
			} catch (Exception ex) {
				log.error("Problema general...", ex);
				message = String.format("No se pudo imprimir el reporte");
				severity = FacesMessage.SEVERITY_INFO;
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, "Error en impresion", message));
				PrimeFaces.current().ajax().update("form:messages", "form:dt-facturacionServicios");
			} finally {
				conexion.close((Connection) connection);
			}
			
		}	
	}
	
	public void sleep() throws InterruptedException {
        TimeUnit.SECONDS.sleep(5);
    }
	
	public void exportarExcel() {
			
			log.info("Exportando cartera de clientes concentrada a excel...");
			String jasperPath = null;
			String filename = null;
			String images = null;
			String message = null;
			Severity severity = null;
			File reportFile = null;
			File imgfile = null;
			JasperReportUtil jasperReportUtil = new JasperReportUtil();
			Map<String, Object> parameters = null;
			Connection connection = null;
			parameters = new HashMap<String, Object>();
			
			if (consulta.equals("Concentrada")) {
				
				System.out.println("Exportando a excel.....");
				jasperPath = "/jasper/Concentrada.jrxml";
				filename = "Concentrada" +fecha+".xlsx";
				images = "/images/logo.jpeg";
				message = null;
				severity = null;
				reportFile = new File(jasperPath);
				imgfile = null;
				jasperReportUtil = new JasperReportUtil();					
				parameters = new HashMap<String, Object>();
				
				try {
				
					URL resource = getClass().getResource(jasperPath);
					URL resourceimg = getClass().getResource(images);
					String file = resource.getFile();
					String img = resourceimg.getFile();
					reportFile = new File(file);
					imgfile = new File(img);
					log.info(reportFile.getPath());
				
					Integer clienteCve =null;
					if(clienteSelect == null) {
						clienteCve = null; 
					}else {
						clienteCve = clienteSelect.getCteCve();
					}
					
				
					connection = EntityManagerUtil.getConnection();
					parameters.put("REPORT_CONNECTION", connection);
					parameters.put("idCliente",clienteCve );
					parameters.put("fecha",fecha );
					parameters.put("imagen", imgfile.getPath());
					log.info("Parametros: " + parameters.toString());
					jasperReportUtil.createXlsx(filename, parameters, reportFile.getPath());
				} catch (Exception ex) {
					log.error("Problema general...", ex);
					message = String.format("No se pudo imprimir el reporte");
					severity = FacesMessage.SEVERITY_INFO;
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, "Error en impresion", message));
					PrimeFaces.current().ajax().update("form:messages", "form:dt-facturacionServicios");
				} finally {
					conexion.close((Connection) connection);
				}
				
			}else {
				
				log.info("Exportando carterad e clientes desglosada a excel...");
				jasperPath = "/jasper/Desglosada.jrxml";
				filename = "Desglosada" +fecha+".xlsx";
				images = "/images/logo.jpeg";
				message = null;
				severity = null;
				reportFile = new File(jasperPath);
				imgfile = null;
				jasperReportUtil = new JasperReportUtil();					
				parameters = new HashMap<String, Object>();
				
				try {
				
					URL resource = getClass().getResource(jasperPath);
					URL resourceimg = getClass().getResource(images);
					String file = resource.getFile();
					String img = resourceimg.getFile();
					reportFile = new File(file);
					imgfile = new File(img);
					log.info(reportFile.getPath());
				
					Integer clienteCve =null;
					if(clienteSelect == null) {
						clienteCve = null; 
					}else {
						clienteCve = clienteSelect.getCteCve();
					}
					
				
					connection = EntityManagerUtil.getConnection();
					parameters.put("REPORT_CONNECTION", connection);
					parameters.put("idCliente",clienteCve );
					parameters.put("fecha",fecha );
					parameters.put("imagen", imgfile.getPath());
					log.info("Parametros: " + parameters.toString());
					jasperReportUtil.createXlsx(filename, parameters, reportFile.getPath());
				} catch (Exception ex) {
					log.error("Problema general...", ex);
					message = String.format("No se pudo imprimir el reporte");
					severity = FacesMessage.SEVERITY_INFO;
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, "Error en impresion", message));
					PrimeFaces.current().ajax().update("form:messages", "form:dt-facturacionServicios");
				} finally {
					conexion.close((Connection) connection);
				}
				
			}
			
			
		}

	public Date getMaxDate() {
		return maxDate;
	}

	public void setMaxDate(Date maxDate) {
		this.maxDate = maxDate;
	}
	
	
}
