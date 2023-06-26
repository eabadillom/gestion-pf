package mx.com.ferbo.controller;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
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
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.primefaces.PrimeFaces;
import org.primefaces.component.datepicker.DatePicker;

import mx.com.ferbo.dao.CamaraDAO;
import mx.com.ferbo.dao.ClienteDAO;
import mx.com.ferbo.dao.PlantaDAO;
import mx.com.ferbo.model.Camara;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.Planta;
import mx.com.ferbo.util.EntityManagerUtil;
import mx.com.ferbo.util.JasperReportUtil;
import mx.com.ferbo.util.conexion;
import mx.com.ferbo.utils.IOUtil;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.export.OutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;

@Named
@ViewScoped
public class ReporteAlmacenFechaBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(ReporteAlmacenFechaBean.class);

	private Date fecha;
	private Date maxDate;
	private Date date11;

	private Planta plantaSelect;
	private Camara camaraSelect;
	private Cliente clienteSelect;

	private List<Cliente> listaClientes;
	private List<Planta> listaPlanta;
	private List<Camara> listaCamara;
	private List<Date> dates;

	private ClienteDAO clienteDAO;
	private PlantaDAO plantaDAO;
	private CamaraDAO camaraDAO;

	public ReporteAlmacenFechaBean() {
		fecha = new Date();
		clienteDAO = new ClienteDAO();
		plantaDAO = new PlantaDAO();
		camaraDAO = new CamaraDAO();

		listaClientes = new ArrayList<Cliente>();
		listaPlanta = new ArrayList<Planta>();
		listaCamara = new ArrayList<Camara>();

	}
	@PostConstruct
	public void init() {
		plantaSelect = new Planta();
		camaraSelect = new Camara();
		clienteSelect = new Cliente();
		
		listaClientes = clienteDAO.buscarHabilitados(true);
		listaPlanta = plantaDAO.buscarTodos();
		listaCamara = camaraDAO.buscarTodos();
		Date today = new Date();
		long oneDay = 24 * 60 * 60 * 1000;

		maxDate = new Date(today.getTime() );
		
	}
	
	public void filtradoCamara() {
		listaCamara = camaraDAO.buscarPorPlanta(plantaSelect);
		plantaSelect.setCamaraList(listaCamara);
	}
	
	public void exportarPdf() throws JRException, IOException, SQLException{
		System.out.println("Exportando a pdf.....");
			String jasperPath = "/jasper/InventarioAlmacen.jrxml";
			String filename = "InventarioAlmacen" +fecha+".pdf";
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
			
				Integer clienteCve =null;
				if(clienteSelect == null) {
					clienteCve = null; 
				}else {
					clienteCve = clienteSelect.getCteCve();
				}
				
				Integer camaraCve = null;
				if(camaraSelect == null) {
					camaraCve = null;
				}else {
					camaraCve = camaraSelect.getCamaraCve();
				}
				Integer plantaCve = null;
				if(plantaSelect == null) {
					plantaCve = null;
				}else {
				plantaCve = plantaSelect.getPlantaCve();
				}
			
				connection = EntityManagerUtil.getConnection();
				parameters.put("REPORT_CONNECTION", connection);
				parameters.put("idCliente",clienteCve );
				parameters.put("Camara", camaraCve);
				parameters.put("Planta", plantaCve);
				parameters.put("Fecha",fecha );
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
	
	public void sleep() throws InterruptedException {
        TimeUnit.SECONDS.sleep(5);
    }
	public void exportarExcel() throws JRException, IOException, SQLException{
		System.out.println("Exportando a excel.....");
			String jasperPath = "/jasper/InventarioAlmacen.jrxml";
			String filename = "InventarioAlmacen" +fecha+".xlsx";
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
			
				Integer clienteCve =null;
				if(clienteSelect == null) {
					clienteCve = null; 
				}else {
					clienteCve = clienteSelect.getCteCve();
				}
				
				Integer camaraCve = null;
				if(camaraSelect == null) {
					camaraCve = null;
				}else {
					camaraCve = camaraSelect.getCamaraCve();
				}
				Integer plantaCve = null;
				if(plantaSelect == null) {
					plantaCve = null;
				}else {
				plantaCve = plantaSelect.getPlantaCve();
				}
			
				connection = EntityManagerUtil.getConnection();
				parameters.put("REPORT_CONNECTION", connection);
				parameters.put("idCliente",clienteCve );
				parameters.put("Camara", camaraCve);
				parameters.put("Planta", plantaCve);
				parameters.put("Fecha",fecha );
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
	public Planta getPlantaSelect() {
		return plantaSelect;
	}
	public void setPlantaSelect(Planta plantaSelect) {
		this.plantaSelect = plantaSelect;
	}
	public List<Planta> getListaPlanta() {
		return listaPlanta;
	}
	public void setListaPlanta(List<Planta> listaPlanta) {
		this.listaPlanta = listaPlanta;
	}
	public Camara getCamaraSelect() {
		return camaraSelect;
	}
	public void setCamaraSelect(Camara camaraSelect) {
		this.camaraSelect = camaraSelect;
	}
	public List<Camara> getListaCamara() {
		return listaCamara;
	}
	public void setListaCamara(List<Camara> listaCamara) {
		this.listaCamara = listaCamara;
	}

	public List<Date> getDates() {
		return dates;
	}
	public void setDates(List<Date> dates) {
		this.dates = dates;
	}
	public Date getMaxDate() {
		return maxDate;
	}
	public void setMaxDate(Date maxDate) {
		this.maxDate = maxDate;
	}
	public Date getDate11() {
		return date11;
	}
	public void setDate11(Date date11) {
		this.date11 = date11;
	}



}
