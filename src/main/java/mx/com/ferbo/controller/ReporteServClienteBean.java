package mx.com.ferbo.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.Usuario;
import mx.com.ferbo.util.conexion;
import mx.com.ferbo.util.EntityManagerUtil;
import mx.com.ferbo.util.FacesUtils;
import mx.com.ferbo.util.JasperReportUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import net.sf.jasperreports.engine.JRException;

@Named
@ViewScoped
public class ReporteServClienteBean implements Serializable 
{	
	private static final long serialVersionUID = 1L;
	private static Logger log = LogManager.getLogger(ReporteServClienteBean.class);

        private Cliente clienteSelected;
        private List<Cliente> lstClientes;
        
	private Date fecha;
        private Locale localeMx = new Locale("es", "MX");
        private StreamedContent file;
        
        private Usuario usuario;
        private FacesContext context;
        private HttpServletRequest request;

	public ReporteServClienteBean() {
		fecha = new Date();
	}
	
	@PostConstruct
	public void init() {
            context = FacesContext.getCurrentInstance();
            request = (HttpServletRequest) context.getExternalContext().getRequest();
            this.usuario = (Usuario) request.getSession(false).getAttribute("usuario");
            log.info("El usuario {} ingresa al reporte de servicios por cliente.", usuario.getUsuario());
            lstClientes = (List<Cliente>) request.getSession(false).getAttribute("clientesActivosList");
	}
        
        public void infoCliente() {
            if(this.clienteSelected == null){
                log.info("El usuario {} no ha seleccionado a ningun cliente", this.usuario.getNombre());
            } else{
                log.info("El usuario {} ha seleccionado al cliente {}", this.usuario.getUsuario(), this.clienteSelected.getNombre());
            }
        }
	
	public void exportarPdf() throws JRException, IOException, SQLException{
		log.info("Exportando a pdf.....");
			String jasperPath = "/jasper/reporteServicioPorCliente.jrxml";
			String filename = "reporteServicioPorCliente" +fecha+".pdf";
			String images = "/images/logo.jpeg";
			File reportFile = new File(jasperPath);
			File imgfile = null;
			JasperReportUtil jasperReportUtil = new JasperReportUtil();
			Map<String, Object> parameters = new HashMap<String, Object>();
			Connection connection = null;
			parameters = new HashMap<String, Object>();
			
			try {
                            Integer idCliente = null;
                            if(this.clienteSelected == null){
                                idCliente = null;
                            } else {
                                idCliente = this.clienteSelected.getCteCve();
                            }

                            URL resource = getClass().getResource(jasperPath);
                            URL resourceimg = getClass().getResource(images);
                            String file = resource.getFile();
                            String img = resourceimg.getFile();
                            reportFile = new File(file);
                            imgfile = new File(img);
                            log.info(reportFile.getPath());

                            connection = EntityManagerUtil.getConnection();
                            parameters.put("REPORT_CONNECTION", connection);
                            parameters.put("REPORT_LOCALE", localeMx);
                            parameters.put("idCliente", idCliente);
                            parameters.put("imagen", imgfile.getPath());
                            log.info("Parametros: " + parameters.toString());
                            byte[] bytes = jasperReportUtil.createPDF(parameters, reportFile.getPath());
                            InputStream input = new ByteArrayInputStream(bytes);
                            this.file = DefaultStreamedContent.builder().contentType("application/pdf").name(filename)
                                .stream(() -> input).build();
                            log.info("El usuario {} descargo el reporte de servicios por cliente {}", this.usuario.getUsuario(), filename);
			} catch (Exception ex) {
                            log.error("Problema general...", ex);
                            FacesUtils.addMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se puede descargar el reporte, consulte con su administrador de sistemas");
                            PrimeFaces.current().ajax().update("form:messages");
			} finally {
                            conexion.close((Connection) connection);
			}
		}

	public void exportarExcel() throws JRException, IOException, SQLException{
		log.info("Exportando a excel.....");
			String jasperPath = "/jasper/reporteServicioPorCliente.jrxml";
			String filename = "reporteServicioPorCliente" +fecha+".xlsx";
			String images = "/images/logo.jpeg";
			File reportFile = new File(jasperPath);
			File imgfile = null;
			JasperReportUtil jasperReportUtil = new JasperReportUtil();
			Map<String, Object> parameters = new HashMap<String, Object>();
			Connection connection = null;
			parameters = new HashMap<String, Object>();
			
			try {
                            Integer idCliente = null;
                            if(this.clienteSelected == null){
                                idCliente = null;
                            } else {
                                idCliente = this.clienteSelected.getCteCve();
                            }
                            
                            URL resource = getClass().getResource(jasperPath);
                            URL resourceimg = getClass().getResource(images);
                            String file = resource.getFile();
                            String img = resourceimg.getFile();
                            reportFile = new File(file);
                            imgfile = new File(img);
                            log.info(reportFile.getPath());

                            connection = EntityManagerUtil.getConnection();
                            parameters.put("REPORT_CONNECTION", connection);
                            parameters.put("REPORT_LOCALE", localeMx);
                            parameters.put("idCliente", idCliente);
                            parameters.put("imagen", imgfile.getPath());
                            log.info("Parametros: " + parameters.toString());
                            byte[] bytes = jasperReportUtil.createXLSX(parameters, reportFile.getPath());
                            InputStream input = new ByteArrayInputStream(bytes);
                            this.file = DefaultStreamedContent.builder().contentType("application/vnd.ms-excel").name(filename)
                                .stream(() -> input).build();
                            log.info("El usuario {} descargo el reporte de servicios por cliente {}", this.usuario.getUsuario(), filename);
			} catch (Exception ex) {
                            log.error("Problema general...", ex);
                            FacesUtils.addMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se puede descargar el reporte, consulte con su administrador de sistemas");
                            PrimeFaces.current().ajax().update("form:messages");
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
        
        public Cliente getClienteSelected() {
            return clienteSelected;
        }

        public void setClienteSelected(Cliente clienteSelected) {
            this.clienteSelected = clienteSelected;
        }

        public List<Cliente> getLstClientes() {
            return lstClientes;
        }

        public void setLstClientes(List<Cliente> lstClientes) {
            this.lstClientes = lstClientes;
        }

        public StreamedContent getFile() {
            return file;
        }

        public void setFile(StreamedContent file) {
            this.file = file;
        }

}
