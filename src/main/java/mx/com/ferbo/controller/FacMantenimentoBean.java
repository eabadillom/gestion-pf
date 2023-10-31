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
import java.util.GregorianCalendar;
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
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import com.ferbo.facturama.tools.FacturamaException;

import mx.com.ferbo.business.FacturamaBL;
import mx.com.ferbo.dao.CancelaFacturaDAO;
import mx.com.ferbo.dao.ClienteDAO;
import mx.com.ferbo.dao.FacturaDAO;
import mx.com.ferbo.dao.StatusFacturaDAO;
import mx.com.ferbo.model.CancelaFactura;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.Factura;
import mx.com.ferbo.model.StatusFactura;
import mx.com.ferbo.model.Usuario;
import mx.com.ferbo.util.EntityManagerUtil;
import mx.com.ferbo.util.InventarioException;
import mx.com.ferbo.util.JasperReportUtil;
import mx.com.ferbo.util.conexion;
import net.sf.jasperreports.engine.JRException;

@Named
@ViewScoped
public class FacMantenimentoBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = LogManager.getLogger(FacMantenimentoBean.class);
	
	private FacesContext context;
	private HttpServletRequest request;
	private HttpSession session;

	private List<Cliente> listClientes;
	private ClienteDAO daoCliente;
	private Cliente clienteSelect;

	private List<Factura> listFac;
	private FacturaDAO daoFac;
	private Factura seleccion;
	private CancelaFactura cancelaFactura = null;
	private CancelaFacturaDAO cancelaDAO = null;

	private Date actual = GregorianCalendar.getInstance().getTime();
	private Date de;
	private Date hasta;
	private String folio;
	
	private StreamedContent file;
	private Usuario usuario;

	public FacMantenimentoBean() {
		seleccion = new Factura();
		daoCliente = new ClienteDAO();
		daoFac = new FacturaDAO();
		cancelaDAO = new CancelaFacturaDAO();
		listClientes = daoCliente.buscarTodos();
		listFac = new ArrayList<Factura>();
		de = new Date();
		hasta = new Date();
	};
	
	@PostConstruct
	public void init() {
		context = FacesContext.getCurrentInstance();
		request = (HttpServletRequest) context.getExternalContext().getRequest();
		session = request.getSession(false);
		
		this.usuario = (Usuario) session.getAttribute("usuario");
		
		byte bytes[] = {};
		this.file = DefaultStreamedContent.builder()
				.contentType("application/pdf")
				.contentLength(bytes.length)
				.name("factura.pdf")
				.stream(() -> new ByteArrayInputStream(bytes) )
				.build();
		
	}

	public void findFacture() {
		
		if(clienteSelect == null) {
			listFac = daoFac.buscaFacturas(de, actual, true);
		} else {
			listFac = daoFac.buscaFacturas(clienteSelect, de, actual, true);
		}
	};
	
	public void preparaCancelacion() {
		this.cancelaFactura = new CancelaFactura();
		this.cancelaFactura.setFactura(seleccion);
	}

	public void cancelaFactura() {
		String message = null;
		Severity severity = null;
		
		String respuesta = null;
		
		try {
			StatusFacturaDAO statusDAO = new StatusFacturaDAO();
			StatusFactura statusCancelada = statusDAO.buscarPorId(StatusFactura.STATUS_CANCELADA);
			this.seleccion.setStatus(statusCancelada);
			this.cancelaFactura.setFactura(seleccion);
			
			respuesta = daoFac.actualizaStatus(seleccion);
			if(respuesta != null)
				throw new InventarioException("Error al cancelar la factura.");
			
			respuesta = cancelaDAO.guardar(cancelaFactura);
			if(respuesta != null)
				throw new InventarioException("Error al cancelar la factura.");
			
			if(clienteSelect == null)
				listFac = daoFac.buscaFacturas(de, actual, true);
			else
				listFac = daoFac.buscaFacturas(clienteSelect, de, actual, true);
			
			seleccion = new Factura();
			cancelaFactura = null;
			
			severity = FacesMessage.SEVERITY_INFO;
			message = "La factura se canceló correctamente.";
			
			PrimeFaces.current().executeScript("PF('dg-delete').hide()");
		} catch (InventarioException e) {
			message = e.getMessage();
			severity = FacesMessage.SEVERITY_ERROR;
		} catch (Exception ex) {
			log.error("Problema general...", ex);
			message = "Problema al cancelar la factura.";
			severity = FacesMessage.SEVERITY_ERROR;
		} finally {
			
			if(severity == null)
				severity = FacesMessage.SEVERITY_FATAL;
			if(message == null)
				message = "Ocurrió un error con la actualización de la factura.";
			
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, "Timbrado CFDI", message));
			PrimeFaces.current().ajax().update("form:messages", "form:dtSerieFac");
		}
	}
	
	
	public void jasper() throws JRException, IOException, SQLException {
		Factura factura = null;
		String jasperPath = null;
		String filename = null;
		String images = null;
		String message = null;
		Severity severity = null;
		File reportFile = null;
		File imgfile = null;
		JasperReportUtil jasperReportUtil = null;
		Map<String, Object> parameters = null;
		Connection conn = null;
		Integer idFactura = null;
		
		try {
			factura = this.seleccion;
			
			jasperPath = "/jasper/Factura.jrxml";
			filename = String.format("Factura-Folio_%s-%s.pdf", factura.getNomSerie(), factura.getNumero());
			images = "/images/logo.jpeg";
			reportFile = new File(jasperPath);
			URL resource = getClass().getResource(jasperPath);
			URL resourceimg = getClass().getResource(images);
			String file = resource.getFile();
			String img = resourceimg.getFile();
			reportFile = new File(file);
			imgfile = new File(img);
			log.debug("Ruta del reporte: {}", reportFile.getPath());
			
			
			idFactura = factura.getId();
			conn = EntityManagerUtil.getConnection();
			parameters = new HashMap<String, Object>();
			parameters.put("REPORT_CONNECTION", conn);
			parameters.put("idFactura", idFactura);
			parameters.put("dsIdFactura", idFactura);
			parameters.put("imagen", imgfile.getPath());
			log.debug("Parametros: {}", parameters.toString());
			
			jasperReportUtil = new JasperReportUtil();
			//jasperReportUtil.createPdf(filename, parameters, reportFile.getPath());
			byte[] bytes = jasperReportUtil.createPDF(parameters, reportFile.getPath());
			InputStream input = new ByteArrayInputStream(bytes);
			this.file = DefaultStreamedContent.builder()
					.contentType("application/pdf")
					.name(filename)
					.stream(() -> input )
					.build();
			log.info("Factura generada {}...", filename);
		} catch (Exception ex) {
			log.error("Problema general...", ex);
			message = String.format("No se pudo imprimir el folio %s-%s", this.seleccion.getNomSerie(), factura.getNumero());
			severity = FacesMessage.SEVERITY_INFO;
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, "Error en impresion", message));
			PrimeFaces.current().ajax().update("form:messages", "form:dt-facturacionServicios");
		} finally {
			conexion.close((Connection) conn);
			PrimeFaces.current().ajax().update("frmFactura:file-factura");
		}

	}
	
	public void timbrar(Factura factura) {
		String message = null;
		Severity severity = null;
		
		FacturamaBL facturamaBO = new FacturamaBL(factura.getId(), this.usuario);
		try {
			log.info("Timbrando factura: {}...", factura);
			facturamaBO.timbrar();
			facturamaBO.sendMail();
			log.info("Timbrado completado correctamente.");
			
			severity = FacesMessage.SEVERITY_INFO;
			message = "El timbrado se generó correctamente";
		} catch (FacturamaException e) {
			message = String.format("Mensaje de Facturama: %s", e.getMessage());
			severity = FacesMessage.SEVERITY_ERROR;
		} catch (InventarioException e) {
			message = e.getMessage();
			severity = FacesMessage.SEVERITY_ERROR;
		} catch(Exception ex) {
			log.error("Problema para obtener los servicios del cliente.", ex);
			message = "Problema con la información de servicios.";
			severity = FacesMessage.SEVERITY_ERROR;
		} finally {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, "Timbrado CFDI", message));
			PrimeFaces.current().ajax().update("form:messages");
		}
	}
	
	public void setFolioFactura(Factura factura) {
		this.folio = String.format("%s-%s", factura.getNomSerie(), factura.getNumero());
		log.info("Preparando vista previa de la factura (Folio: {})", this.folio);
	}
	
	public List<Cliente> getListClientes() {
		return listClientes;
	}

	public void setListClientes(List<Cliente> listClientes) {
		this.listClientes = listClientes;
	}

	public Cliente getClienteSelect() {
		return clienteSelect;
	}

	public void setClienteSelect(Cliente clienteSelect) {
		this.clienteSelect = clienteSelect;
	}

	public List<Factura> getListFac() {
		return listFac;
	}

	public void setListFac(List<Factura> listFac) {
		this.listFac = listFac;
	}

	public Factura getSeleccion() {
		return seleccion;
	}

	public void setSeleccion(Factura seleccion) {
		this.seleccion = seleccion;
	}

	public Date getActual() {
		return actual;
	}

	public void setActual(Date actual) {
		this.actual = actual;
	}

	public Date getDe() {
		return de;
	}

	@SuppressWarnings("deprecation")
	public void setDe(Date de) {
		if (de.getDate() > hasta.getDate()) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Fecha Invalida", "La fecha seleccionada debe ser menor a la fecha final"));
			PrimeFaces.current().ajax().update("form:messages");
			this.de = this.actual;
			this.hasta = this.actual;
		} else {
			this.de = de;
		}
	}

	public Date getHasta() {
		return hasta;
	}

	public void setHasta(Date hasta) {
		if (de.compareTo(hasta) > 0) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Fecha Invalida", "La fecha seleccionada debe ser mayor a la fecha inicial"));
			PrimeFaces.current().ajax().update("form:messages");
			this.de = this.actual;
			this.hasta = this.actual;
		} else {
			this.hasta = hasta;
		}
	}

	public StreamedContent getFile() {
		return file;
	}

	public void setFile(StreamedContent file) {
		this.file = file;
	}

	public String getFolio() {
		return folio;
	}

	public void setFolio(String folio) {
		this.folio = folio;
	}

	public CancelaFactura getCancelaFactura() {
		return cancelaFactura;
	}

	public void setCancelaFactura(CancelaFactura cancelaFactura) {
		this.cancelaFactura = cancelaFactura;
	}

	
}
