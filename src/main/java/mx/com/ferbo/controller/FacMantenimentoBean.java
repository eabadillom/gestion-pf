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
import mx.com.ferbo.dao.FacturaDAO;
import mx.com.ferbo.dao.FacturaMedioPagoDAO;
import mx.com.ferbo.dao.MedioPagoDAO;
import mx.com.ferbo.dao.MetodoPagoDAO;
import mx.com.ferbo.dao.StatusFacturaDAO;
import mx.com.ferbo.model.CancelaFactura;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.Factura;
import mx.com.ferbo.model.FacturaMedioPago;
import mx.com.ferbo.model.MedioPago;
import mx.com.ferbo.model.MetodoPago;
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
	private Cliente clienteSelect;
	private List<MetodoPago> listaMetodoPago;
	private List<MedioPago> listaMedioPago;

	private List<Factura> listFac;
	private FacturaDAO daoFac;
	private Factura seleccion;
	private FacturaMedioPagoDAO factMedioPagoDAO;
	
	private CancelaFactura cancelaFactura = null;
	private CancelaFacturaDAO cancelaDAO = null;
	private MetodoPagoDAO metodoPagoDAO;
	private MedioPagoDAO medioPagoDAO;

	private String cdMetodoPagoSelected;
	private Integer idMedioPagoSelected;

	private Date actual = GregorianCalendar.getInstance().getTime();
	private Date de;
	private Date hasta;
	private String folio;
	private Date fechaModificada;

	private StreamedContent file;
	private Usuario usuario;

	public FacMantenimentoBean() {
		seleccion = new Factura();
		daoFac = new FacturaDAO();
		medioPagoDAO = new MedioPagoDAO();
		metodoPagoDAO = new MetodoPagoDAO();
		cancelaDAO = new CancelaFacturaDAO();
		factMedioPagoDAO = new FacturaMedioPagoDAO();
		listFac = new ArrayList<Factura>();
		listClientes = new ArrayList<>();
		listaMedioPago = new ArrayList<MedioPago>();
		listaMetodoPago = new ArrayList<MetodoPago>();
		de = new Date();
		fechaModificada = new Date();
		hasta = new Date();
	};

	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() {
		context = FacesContext.getCurrentInstance();
		request = (HttpServletRequest) context.getExternalContext().getRequest();
		session = request.getSession(false);
		listClientes = (List<Cliente>) request.getSession(false).getAttribute("clientesActivosList");
		this.usuario = (Usuario) session.getAttribute("usuario");
		byte bytes[] = {};
		this.file = DefaultStreamedContent.builder().contentType("application/pdf").contentLength(bytes.length)
				.name("factura.pdf").stream(() -> new ByteArrayInputStream(bytes)).build();
		
	}

	public void consultarPagos() {
		this.listaMedioPago = medioPagoDAO.buscarVigentes(new Date());
		this.listaMetodoPago = metodoPagoDAO.buscarVigentes(new Date());
	}

	public void findFacture() {

		if (clienteSelect == null) {
			listFac = daoFac.buscaFacturas(de, this.hasta, true);
		} else {
			listFac = daoFac.buscaFacturas(clienteSelect, de, this.hasta, true);
		}
	}

	public void preparaCancelacion() {
		this.cancelaFactura = new CancelaFactura();
		this.cancelaFactura.setFactura(seleccion);
	}

	public void updateFactura() {
		
		FacesMessage message = null;
		String mensaje = null;
		Severity severity = null;
		
		MedioPago mp = medioPagoDAO.buscarPorId(idMedioPagoSelected);			
		
		FacturaMedioPago factMedioPago;
		try {
			factMedioPago = factMedioPagoDAO.buscarPorFactura(seleccion.getId());
			factMedioPago.setMpDescripcion(mp.getMpDescripcion());
			factMedioPago.setMpId(mp);
			//seleccion.setFecha(fechaModificada);
			
			seleccion.setMetodoPago(cdMetodoPagoSelected);
			if(factMedioPagoDAO.actualizar(factMedioPago ) == null && daoFac.actualizarFechaFactura(seleccion) == null) {
				
				mensaje = "Factura medio pago de Factura: " + seleccion.getId() + " actualizada";
				severity = FacesMessage.SEVERITY_INFO;			
			}else {
				mensaje = "Factura medio pago de Factura: " + seleccion.getId() + " no actualizada";
				severity = FacesMessage.SEVERITY_ERROR;
			}
		} catch (Exception e) {
			log.error("Ocurrió un problema en la actualización de la factura...", e);
		}
		
		
		message = new FacesMessage(severity, "Actualizacion", mensaje );
		FacesContext.getCurrentInstance().addMessage(null, message);
		PrimeFaces.current().ajax().update("form:messages");
		
	}
	
	public void datosCliente() {
		
		consultarPagos();
		
		Factura fact = daoFac.buscarPorId(seleccion.getId(), true);		
		idMedioPagoSelected = fact.getFacturaMedioPagoList().get(0).getMpId().getMpId();
		
		cdMetodoPagoSelected = seleccion.getMetodoPago();
		
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
			if (respuesta != null)
				throw new InventarioException("Error al cancelar la factura.");

			respuesta = cancelaDAO.guardar(cancelaFactura);
			if (respuesta != null)
				throw new InventarioException("Error al cancelar la factura.");

			if (clienteSelect == null)
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

			if (severity == null)
				severity = FacesMessage.SEVERITY_FATAL;
			if (message == null)
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
			// jasperReportUtil.createPdf(filename, parameters, reportFile.getPath());
			byte[] bytes = jasperReportUtil.createPDF(parameters, reportFile.getPath());
			InputStream input = new ByteArrayInputStream(bytes);
			this.file = DefaultStreamedContent.builder().contentType("application/pdf").name(filename)
					.stream(() -> input).build();
			log.info("Factura generada {}...", filename);
		} catch (Exception ex) {
			log.error("Problema general...", ex);
			message = String.format("No se pudo imprimir el folio %s-%s", this.seleccion.getNomSerie(),
					factura.getNumero());
			severity = FacesMessage.SEVERITY_INFO;
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(severity, "Error en impresion", message));
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
		} catch (Exception ex) {
			log.error("Problema para obtener los servicios del cliente.", ex);
			message = "Problema con la información de servicios.";
			severity = FacesMessage.SEVERITY_ERROR;
		} finally {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, "Timbrado CFDI", message));
			PrimeFaces.current().ajax().update("form:messages");
		}
	}

	public void exportar() throws JRException, IOException, SQLException {
		String jasperPath = null;
		String filename = null;
		String message = null;
		Severity severity = null;
		File reportFile = null;
		JasperReportUtil jasperReportUtil = null;
		Map<String, Object> parameters = null;
		Connection conn = null;

		Integer idCliente = null;
		Date fechaInicio = null;
		Date fechaFin = null;

		try {
			idCliente = this.clienteSelect == null ? null : this.clienteSelect.getCteCve();
			fechaInicio = this.de;
			fechaFin = this.hasta;

			jasperPath = "/jasper/consulta_facturacion.jrxml";
			filename = String.format("consulta_facturacion.xls");
			reportFile = new File(jasperPath);
			URL resource = getClass().getResource(jasperPath);
			String file = resource.getFile();
			reportFile = new File(file);
			log.debug("Ruta del reporte: {}", reportFile.getPath());

			conn = EntityManagerUtil.getConnection();
			parameters = new HashMap<String, Object>();
			parameters.put("REPORT_CONNECTION", conn);
			parameters.put("idCliente", idCliente);
			parameters.put("fechaIni", fechaInicio);
			parameters.put("fechaFin", fechaFin);
			log.debug("Parametros: {}", parameters.toString());

			jasperReportUtil = new JasperReportUtil();
			this.file = jasperReportUtil.getXls(filename, parameters, reportFile.getPath());
			log.info("Factura generada {}...", filename);
		} catch (Exception ex) {
			log.error("Problema general...", ex);
			message = String.format("No se puede realizar la exportación de la consulta.");
			severity = FacesMessage.SEVERITY_INFO;
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(severity, "Error en impresion", message));
			PrimeFaces.current().ajax().update("form:messages", "form:dt-facturacionServicios");
		} finally {
			conexion.close((Connection) conn);
			PrimeFaces.current().ajax().update("frmFactura:file-factura");
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

	public void setDe(Date de) {
		this.de = de;
	}

	public Date getHasta() {
		return hasta;
	}

	public void setHasta(Date hasta) {
		this.hasta = hasta;
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

	public String getCdMetodoPagoSelected() {
		return cdMetodoPagoSelected;
	}

	public void setCdMetodoPagoSelected(String cdMetodoPagoSelected) {
		this.cdMetodoPagoSelected = cdMetodoPagoSelected;
	}

	public Integer getIdMedioPagoSelected() {
		return idMedioPagoSelected;
	}

	public void setIdMedioPagoSelected(Integer idMedioPagoSelected) {
		this.idMedioPagoSelected = idMedioPagoSelected;
	}

	public List<MetodoPago> getLstMetodoPago() {
		return listaMetodoPago;
	}

	public void setLstMetodoPago(List<MetodoPago> lstMetodoPago) {
		this.listaMetodoPago = lstMetodoPago;
	}

	public List<MetodoPago> getListaMetodoPago() {
		return listaMetodoPago;
	}

	public void setListaMetodoPago(List<MetodoPago> listaMetodoPago) {
		this.listaMetodoPago = listaMetodoPago;
	}

	public List<MedioPago> getListaMedioPago() {
		return listaMedioPago;
	}

	public void setListaMedioPago(List<MedioPago> listaMedioPago) {
		this.listaMedioPago = listaMedioPago;
	}

	public Date getFechaModificada() {
		return fechaModificada;
	}

	public void setFechaModificada(Date fechaModificada) {
		this.fechaModificada = fechaModificada;
	}

}
