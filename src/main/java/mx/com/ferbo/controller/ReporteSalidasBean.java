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
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import mx.com.ferbo.dao.CamaraDAO;
import mx.com.ferbo.dao.PlantaDAO;
import mx.com.ferbo.dao.RepSalidasDAO;
import mx.com.ferbo.model.Camara;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.Planta;
import mx.com.ferbo.model.Usuario;
import mx.com.ferbo.ui.RepSalidas;
import mx.com.ferbo.util.EntityManagerUtil;
import mx.com.ferbo.util.InventarioException;
import mx.com.ferbo.util.JasperReportUtil;
import mx.com.ferbo.util.conexion;
import net.sf.jasperreports.engine.JRException;

@Named
@ViewScoped
public class ReporteSalidasBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private static Logger log = LogManager.getLogger(ReporteSalidasBean.class);

	private Date fecha;
	private Date fecha_ini;
	private Date fecha_fin;
	private Date maxDate;
	private Integer plantaN = null; 
	private Integer camaraN = null;
	private Integer clienteN = null;

	private Planta plantaSelect;
	private Camara camaraSelect;
	private Cliente clienteSelect;

	private List<Cliente> listaClientes;
	private List<Planta> listaPlanta;
	private List<Camara> listaCamara;

	private PlantaDAO plantaDAO;
	private CamaraDAO camaraDAO;
	
	private Usuario usuario;
	private FacesContext faceContext;
	private HttpServletRequest httpServletRequest;
	
	private List<RepSalidas> reporte;
	private StreamedContent file;

	public ReporteSalidasBean() {
		fecha = new Date();

		plantaDAO = new PlantaDAO();
		camaraDAO = new CamaraDAO();

		listaClientes = new ArrayList<Cliente>();
		listaPlanta = new ArrayList<Planta>();
		listaCamara = new ArrayList<Camara>();

	}
	
	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() {
		
		faceContext = FacesContext.getCurrentInstance();
		httpServletRequest = (HttpServletRequest) faceContext.getExternalContext().getRequest();
		usuario = (Usuario) httpServletRequest.getSession(false).getAttribute("usuario");
		
		plantaSelect = new Planta();
		camaraSelect = new Camara();
		clienteSelect = new Cliente();
		
		listaClientes = (List<Cliente>) httpServletRequest.getSession(false).getAttribute("clientesActivosList");
		
		if((usuario.getPerfil()==1)||(usuario.getPerfil()==4)) {
			listaPlanta.add(plantaDAO.buscarPorId(usuario.getIdPlanta()));
		}else {
			listaPlanta = plantaDAO.buscarTodos();
		}
		
		filtradoCamara();
		Date today = new Date();

		maxDate = new Date(today.getTime() );
		this.fecha_ini = new Date();
		this.fecha_fin = new Date();
		
		byte bytes[] = {};
		this.file = DefaultStreamedContent.builder()
				.contentType("application/pdf")
				.contentLength(bytes.length)
				.name("InventarioSalidas.pdf")
				.stream(() -> new ByteArrayInputStream(bytes))
				.build();
	}
	
	public void filtradoCamara() {
		listaCamara = camaraDAO.buscarPorPlanta(plantaSelect);
		plantaSelect.setCamaraList(listaCamara);
	}
	
	public void exportarPdf() throws JRException, IOException, SQLException{
		log.info("Exportando reporte de salidas a PDF...");
		String jasperPath = "/jasper/InventarioSalidas.jrxml";
		String filename = "InventarioSalidas"+fecha+".pdf";
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
			parameters.put("idCliente",clienteCve);
			parameters.put("fechaInicio", fecha_ini);
			parameters.put("fechaFin", fecha_fin);
			parameters.put("imagen", imgfile.getPath());
			parameters.put("planta", plantaCve);
			parameters.put("camara", camaraCve);
			log.info("Parametros: " + parameters.toString());
			byte[] bytes =jasperReportUtil.createPDF(parameters, reportFile.getPath());
			
			InputStream input = new ByteArrayInputStream(bytes);
			this.file = DefaultStreamedContent.builder()
					.contentEncoding("application/pdf")
					.contentLength(bytes.length)
					.name(filename)
					.stream(() -> input)
					.build();
			log.info("El usuario {} descargo el reporte de salidas {}", this.usuario.getUsuario(), filename);
		} catch (Exception ex) {
			log.error("Problema general...", ex);
			message = String.format("No se pudo imprimir el reporte");
			severity = FacesMessage.SEVERITY_INFO;
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, "Error en impresion", message));
			PrimeFaces.current().ajax().update("form:messages", "form:dt-inventarioSalidas");
		} finally {
			conexion.close((Connection) connection);
		}
		
	}
	
	public void exportarExcel() throws JRException, IOException, SQLException{
		log.info("Exportando reporte de salidas a excel...");
		String jasperPath = "/jasper/InventarioSalidas.jrxml";
		String filename = "InventarioSalidas" +fecha+".xlsx";
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
			parameters.put("fechaInicio",fecha_ini);
			parameters.put("fechaFin", fecha_fin);
			parameters.put("imagen", imgfile.getPath());
			parameters.put("planta", plantaCve);
			parameters.put("camara", camaraCve);
			log.info("Parametros: " + parameters.toString());
			byte[] bytes = jasperReportUtil.createXLSX(parameters, reportFile.getPath());
			InputStream input = new ByteArrayInputStream(bytes);
			this.file = DefaultStreamedContent.builder()
					.contentType("application/vnd.ms-excel")
					.contentLength(bytes.length)
					.name(filename)
					.stream(() -> input)
					.build();
			log.info("El usuario {} descargo el reporte de salidas {}", this.usuario.getUsuario(), filename); 
		} catch (Exception ex) {
			log.error("Problema general...", ex);
			message = String.format("No se pudo imprimir el reporte");
			severity = FacesMessage.SEVERITY_INFO;
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, "Error en impresion", message));
			PrimeFaces.current().ajax().update("form:messages", "form:dt-inventarioSalidas");
		} finally {
			conexion.close((Connection) connection);
		}
	}
	
	public void generaReporte() {
		RepSalidasDAO reporteDAO = null;
		Integer clienteCve = null;
		Integer plantaCve = null;
		Integer camaraCve = null;
		
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		String titulo = "Reporte salidas";
		
		try {
			
			if(clienteSelect == null)
				throw new InventarioException("Debe seleccionar un cliente.");
			
			clienteCve =clienteSelect.getCteCve();
			
			if(clienteCve == null)
				throw new InventarioException("Debe seleccionar un cliente.");
			
			if(plantaSelect == null)
				throw new InventarioException("Debe seleccionar una planta.");
			
			plantaCve= plantaSelect.getPlantaCve();
			
			if(plantaCve == null)
				throw new InventarioException("Debe seleccionar una planta.");
			
			if(camaraSelect == null)
				camaraCve = null;
			
			camaraCve= camaraSelect.getCamaraCve();
			
			reporteDAO = new RepSalidasDAO();
			reporte = reporteDAO.buscar(fecha_ini, fecha_fin, clienteCve, plantaCve, camaraCve);
			log.debug("Registros del reporte: {}", reporte.size());
		
		} catch(InventarioException ex) {
			log.error("Problema para consultar el reporte de salidas...", ex);
			mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_WARN;
			
			message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
		} catch(Exception ex) {
			log.error("Problema para consultar el reporte de salidas...", ex);
			mensaje = "Ha ocurrido un error en el sistema. Intente nuevamente.\nSi el problema persiste, por favor comuniquese con su administrador del sistema.";
			severity = FacesMessage.SEVERITY_ERROR;
			
			message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
		} finally {
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

	public Integer getPlantaN() {
		return plantaN;
	}
	public void setPlantaN(Integer plantaN) {
		this.plantaN = plantaN;
	}
	public Integer getCamaraN() {
		return camaraN;
	}
	public void setCamaraN(Integer camaraN) {
		this.camaraN = camaraN;
	}
	public Integer getClienteN() {
		return clienteN;
	}
	public void setClienteN(Integer clienteN) {
		this.clienteN = clienteN;
	}
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	public Date getMaxDate() {
		return maxDate;
	}
	public void setMaxDate(Date maxDate) {
		this.maxDate = maxDate;
	}
	public List<RepSalidas> getReporte() {
		return reporte;
	}
	public void setReporte(List<RepSalidas> reporte) {
		this.reporte = reporte;
	}

	public StreamedContent getFile() {
		return file;
	}

	public void setFile(StreamedContent file) {
		this.file = file;
	}

}
