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
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import mx.com.ferbo.dao.CamaraDAO;
import mx.com.ferbo.dao.PlantaDAO;
import mx.com.ferbo.dao.RepInventarioDAO;
import mx.com.ferbo.model.Camara;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.Planta;
import mx.com.ferbo.model.Usuario;
import mx.com.ferbo.ui.RepInventario;
import mx.com.ferbo.util.EntityManagerUtil;
import mx.com.ferbo.util.InventarioException;
import mx.com.ferbo.util.JasperReportUtil;
import mx.com.ferbo.util.conexion;
import net.sf.jasperreports.engine.JRException;

@Named
@ViewScoped
public class ReporteInventarioBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private static Logger log = LogManager.getLogger(ReporteInventarioBean.class);

	private Date fecha;

	private Planta plantaSelect;
	private Camara camaraSelect;
	private Cliente clienteSelect;

	private List<Cliente> listaClientes;
	private List<Planta> listaPlanta;
	private List<Camara> listaCamara;

	private PlantaDAO plantaDAO;
	private CamaraDAO camaraDAO;

	private FacesContext context;
	private HttpServletRequest request;
	private Usuario usuario;
	private List<RepInventario> reporte;
	
	private StreamedContent file;

	public ReporteInventarioBean() {
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

		this.context = FacesContext.getCurrentInstance();
		this.request = (HttpServletRequest) context.getExternalContext().getRequest();
		this.usuario = (Usuario) request.getSession(false).getAttribute("usuario");

		this.plantaSelect = new Planta();
		this.camaraSelect = new Camara();
		this.clienteSelect = new Cliente();
		this.listaClientes = (List<Cliente>) this.request.getSession(false).getAttribute("clientesActivosList");

		if ((this.usuario.getPerfil() == 1) || (this.usuario.getPerfil() == 4)) {
			this.listaPlanta.add(this.plantaDAO.buscarPorId(this.usuario.getIdPlanta()));
			this.plantaSelect = this.listaPlanta.get(0);
		} else {
			listaPlanta = plantaDAO.buscarTodos();
		}

		this.filtradoCamara();

		byte bytes[] = {};
		this.file = DefaultStreamedContent.builder().contentType("application/pdf").contentLength(bytes.length)
				.name("inventario.pdf").stream(() -> new ByteArrayInputStream(bytes)).build();
	}

	public void filtradoCamara() {
		this.listaCamara = this.camaraDAO.buscarPorPlanta(this.plantaSelect);
		this.plantaSelect.setCamaraList(this.listaCamara);
	}

	public void exportarPdf() throws JRException, IOException, SQLException {
		log.info("Exportando a pdf.....");
		String jasperPath = "/jasper/InventarioAlmacen.jrxml";
		String filename = "InventarioAlmacen" + fecha + ".pdf";
		String images = "/images/logoF.png";
		String message = null;
		Severity severity = null;
		File reportFile = new File(jasperPath);
		File imgfile = null;
		JasperReportUtil jasperReportUtil = null;
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
			if (clienteSelect == null) {
				clienteCve = null;
			} else {
				clienteCve = clienteSelect.getCteCve();
			}

			Integer camaraCve = null;
			if (camaraSelect == null) {
				camaraCve = null;
			} else {
				camaraCve = camaraSelect.getCamaraCve();
			}

			Integer plantaCve = null;
			if (plantaSelect == null) {
				plantaCve = null;
			} else {
				plantaCve = plantaSelect.getPlantaCve();
			}
			
			if( (this.usuario.getPerfil() == 1 || this.usuario.getPerfil() == 4) && plantaCve == null )
				throw new InventarioException("Debe seleccionar una planta.");

			connection = EntityManagerUtil.getConnection();
			parameters.put("REPORT_CONNECTION", connection);
			parameters.put("idCliente", clienteCve);
			parameters.put("Camara", camaraCve);
			parameters.put("Planta", plantaCve);
			parameters.put("Fecha", fecha);
			parameters.put("imagen", imgfile.getPath());
			log.info("Parametros: " + parameters.toString());
			jasperReportUtil = new JasperReportUtil();
			byte[] bytes = jasperReportUtil.createPDF(parameters, reportFile.getPath());
			InputStream input = new ByteArrayInputStream(bytes);
			this.file = DefaultStreamedContent.builder().contentType("application/pdf").name(filename)
					.stream(() -> input).build();
			log.info("El usuario {} descargo el reporte de inventario {}", this.usuario.getUsuario(), filename);
		} catch (Exception ex) {
			log.error("Problema general...", ex);
			message = String.format("No se pudo imprimir el reporte");
			severity = FacesMessage.SEVERITY_INFO;
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(severity, "Error en impresion", message));
			PrimeFaces.current().ajax().update("form:messages", "form:dt-facturacionServicios");
		} finally {
			conexion.close((Connection) connection);
		}
	}

	public void sleep() throws InterruptedException {
		TimeUnit.SECONDS.sleep(5);
	}

	public void exportarExcel() throws JRException, IOException, SQLException {
		log.info("Exportando a excel.....");
		String jasperPath = "/jasper/InventarioAlmacen.jrxml";
		String filename = "InventarioAlmacen" + fecha + ".xlsx";
		String images = "/images/logoF.png";
		String message = null;
		Severity severity = null;
		File reportFile = new File(jasperPath);
		File imgfile = null;
		JasperReportUtil jasperReportUtil = null;
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
			if (clienteSelect == null) {
				clienteCve = null;
			} else {
				clienteCve = clienteSelect.getCteCve();
			}

			Integer camaraCve = null;
			if (camaraSelect == null) {
				camaraCve = null;
			} else {
				camaraCve = camaraSelect.getCamaraCve();
			}

			Integer plantaCve = null;
			if (plantaSelect == null) {
				plantaCve = null;
			} else {
				plantaCve = plantaSelect.getPlantaCve();
			}

			connection = EntityManagerUtil.getConnection();
			parameters.put("REPORT_CONNECTION", connection);
			parameters.put("idCliente", clienteCve);
			parameters.put("Camara", camaraCve);
			parameters.put("Planta", plantaCve);
			parameters.put("Fecha", fecha);
			parameters.put("imagen", imgfile.getPath());
			log.info("Parametros: " + parameters.toString());
			jasperReportUtil = new JasperReportUtil();
			byte[] bytes = jasperReportUtil.createXLSX(parameters, reportFile.getPath());
			InputStream input = new ByteArrayInputStream(bytes);
			this.file = DefaultStreamedContent.builder().contentType("application/vnd.ms-excel").name(filename)
					.stream(() -> input).build();
			log.info("El usuario {} descargo el reporte de inventario {}", this.usuario.getUsuario(), filename);
		} catch (Exception ex) {
			log.error("Problema general...", ex);
			message = String.format("No se pudo imprimir el reporte");
			severity = FacesMessage.SEVERITY_INFO;
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(severity, "Error en impresion", message));
			PrimeFaces.current().ajax().update("form:messages", "form:dt-facturacionServicios");
		} finally {
			conexion.close((Connection) connection);
		}

	}

	public void generaReporte() {
		RepInventarioDAO reporteDAO = null;
		Integer clienteCve = null;

		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		String titulo = "Reporte Servicios";

		try {

			if (clienteSelect == null) {
				throw new InventarioException("Debe seleccionar un cliente.");
			} else {
				clienteCve = clienteSelect.getCteCve();
			}

			if (clienteCve == null)
				throw new InventarioException("Debe seleccionar un cliente.");

			Integer camaraCve = null;
			if (camaraSelect == null) {
				camaraCve = null;
			} else {
				camaraCve = camaraSelect.getCamaraCve();
			}

			Integer plantaCve = null;

			reporteDAO = new RepInventarioDAO();
			reporte = reporteDAO.buscar(fecha, clienteCve, plantaCve, camaraCve);
			log.debug("Registros del reporte: {}", reporte.size());
			log.info("El usuario {} consulta e reporte de inventario.", this.usuario.getUsuario());
		} catch (InventarioException ex) {
			log.error("Problema para consultar el reporte de salidas...", ex);
			mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_WARN;

			message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
		} catch (Exception ex) {
			log.error("Problema para consultar el reporte de salidas...", ex);
			mensaje = "Ha ocurrido un error en el sistema. Intente nuevamente.\nSi el problema persiste, por favor comuniquese con su administrador del sistema.";
			severity = FacesMessage.SEVERITY_ERROR;

			message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
		} finally {
			PrimeFaces.current().ajax().update("form:dt-reporte", "form:dtReporte", "form:messages");
		}
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
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

	public List<RepInventario> getReporte() {
		return reporte;
	}

	public void setReporte(List<RepInventario> reporte) {
		this.reporte = reporte;
	}

	public StreamedContent getFile() {
		return file;
	}

	public void setFile(StreamedContent file) {
		this.file = file;
	}

}
