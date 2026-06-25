package mx.com.ferbo.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
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
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;

import mx.com.ferbo.dao.RepServiciosDAO;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.Usuario;
import mx.com.ferbo.ui.RepServicios;
import mx.com.ferbo.util.EntityManagerUtil;
import mx.com.ferbo.util.InventarioException;
import mx.com.ferbo.util.JasperReportUtil;
import mx.com.ferbo.util.conexion;
import net.sf.jasperreports.engine.JRException;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@Named
@ViewScoped
public class ReporteServiciosBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private static Logger log = LogManager.getLogger(ReporteServiciosBean.class);

	private Date fecha;
	private Date fecha_ini;
	private Date fecha_fin;
	private Date maxDate;
	private Cliente clienteSelect;
	private List<Cliente> listaClientes;

	private List<RepServicios> reporte = null;
	
	private FacesContext faceContext;
    private HttpServletRequest httpServletRequest;
        private Usuario usuario;
        private StreamedContent file;

	public ReporteServiciosBean() {
		fecha = new Date();
		listaClientes = new ArrayList<Cliente>();
	}
	
	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() {
		faceContext = FacesContext.getCurrentInstance();
        httpServletRequest = (HttpServletRequest) faceContext.getExternalContext().getRequest();
        usuario = (Usuario) httpServletRequest.getSession(false).getAttribute("usuario");
		clienteSelect = new Cliente();
//		listaClientes = clienteDAO.buscarHabilitados(true);
		listaClientes = (List<Cliente>) httpServletRequest.getSession(false).getAttribute("clientesActivosList");
		Date today = new Date();
		setMaxDate(new Date(today.getTime() ));
		this.fecha_ini = new Date();
		this.fecha_fin = new Date();
                log.info("El usuario {} esta ingresando a reporte de servicios", usuario.getUsuario());
	}
	
	public void exportarPdf() throws JRException, IOException, SQLException{
		log.info("Exportando a pdf...");
		String jasperPath = "/jasper/InventarioServicios.jrxml";
		String filename = "InventarioServicios"+fecha+".pdf";
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
			parameters.put("idCliente",clienteCve);
			parameters.put("fechaInicio", fecha_ini);
			parameters.put("fechaFin", fecha_fin);
			parameters.put("imagen", imgfile.getPath());
			log.info("Parametros: " + parameters.toString());
			
                        byte[] bytes = jasperReportUtil.createPDF(parameters, reportFile.getPath());
			InputStream input = new ByteArrayInputStream(bytes);
                                this.file = DefaultStreamedContent.builder().contentType("application/pdf")
                                    .name(filename)
                                    .stream(() -> input)
                                    .build();
                                log.info("El usuario {} descargo el reporte de inventario de servicios {}", this.usuario.getUsuario(), filename);
		} catch (Exception ex) {
			log.error("Problema general...", ex);
			message = String.format("No se pudo imprimir el reporte");
			severity = FacesMessage.SEVERITY_INFO;
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, "Error en impresion", message));
			PrimeFaces.current().ajax().update("form:messages", "form:dt-InventarioServicios");
		} finally {
			conexion.close((Connection) connection);
		}
		
	}
	
	public void exportarExcel() throws JRException, IOException, SQLException{
		log.info("Exportando a excel...");
		String jasperPath = "/jasper/InventarioServicios.jrxml";
		String filename = "InventarioServicios" +fecha+".xlsx";
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
			parameters.put("fechaInicio",fecha_ini);
			parameters.put("fechaFin", fecha_fin);
			parameters.put("imagen", imgfile.getPath());
			log.info("Parametros: " + parameters.toString());
                        
			byte[] bytes = jasperReportUtil.createXLSX(parameters, reportFile.getPath());
                        InputStream input = new ByteArrayInputStream(bytes);
                        this.file = DefaultStreamedContent.builder().contentType("application/vnd.ms-excel")
                            .name(filename)
                            .stream(() -> input)
                            .build();
                        log.info("El usuario {} descargo el reporte de inventario de servicios {}", this.usuario.getUsuario(), filename);
		} catch (Exception ex) {
			log.error("Problema general...", ex);
			message = String.format("No se pudo imprimir el reporte");
			severity = FacesMessage.SEVERITY_INFO;
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, "Error en impresion", message));
			PrimeFaces.current().ajax().update("form:messages", "form:dt-InventarioServicios");
		} finally {
			conexion.close((Connection) connection);
		}
	}
	
	public void generaReporte() {
		RepServiciosDAO reporteDAO = null;
		Integer clienteCve = null;
		
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		String titulo = "Reporte de Servicios";
		
		try {
			
			if(clienteSelect == null) {
				throw new InventarioException("Debe seleccionar un cliente.");
			} else {
				clienteCve =clienteSelect.getCteCve();
			}
			
			if(clienteCve == null)
				throw new InventarioException("Debe seleccionar un cliente.");
			
			reporteDAO = new RepServiciosDAO();
			reporte = reporteDAO.buscar(fecha_ini, fecha_fin, clienteCve);
			log.debug("Registros del reporte: {}", reporte.size());
                        mensaje = "La consulta se generó correctamente";
                        severity = FacesMessage.SEVERITY_INFO;
		} catch(InventarioException ex) {
			log.error("Problema para consultar el reporte de salidas...", ex);
			mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_WARN;
		} catch(Exception ex) {
			log.error("Problema para consultar el reporte de salidas...", ex);
			mensaje = "Ha ocurrido un error en el sistema. Intente nuevamente.\nSi el problema persiste, por favor comuniquese con su administrador del sistema.";
			severity = FacesMessage.SEVERITY_ERROR;
		} finally {
			message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
                        PrimeFaces.current().ajax().update("form:dt-reporte", "form:dtReporte", "form:messages");
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
	
        public Date getMaxDate() {
		return maxDate;
	}
	
        public void setMaxDate(Date maxDate) {
		this.maxDate = maxDate;
	}
	
        public List<RepServicios> getReporte() {
		return reporte;
	}
	
        public void setReporte(List<RepServicios> reporte) {
		this.reporte = reporte;
	}

        public StreamedContent getFile() {
            return file;
        }

        public void setFile(StreamedContent file) {
            this.file = file;
        }

}
