package mx.com.ferbo.controller;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.Usuario;
import mx.com.ferbo.reports.AuxiliarClientesReport;
import mx.com.ferbo.reports.ConciliacionReport;
import mx.com.ferbo.util.DateUtil;
import mx.com.ferbo.util.InventarioException;

@Named
@ViewScoped
public class ReporteContabilidadBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private static Logger log = LogManager.getLogger(ReporteContabilidadBean.class);

	private Usuario usuario;
	private Date fecha;
	private Date maxDate;
	private Date fecha_ini;
	private Date fecha_fin;
	private String tipoReporte;

	private Cliente clienteSelect;
	private List<Cliente> listaClientes;
	private StreamedContent file;

	private FacesContext faceContext;
    private HttpServletRequest request;

	public ReporteContabilidadBean() {
		fecha = new Date();
		listaClientes = new ArrayList<Cliente>();
	}
	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() {
		faceContext = FacesContext.getCurrentInstance();
        request = (HttpServletRequest) faceContext.getExternalContext().getRequest();
        this.usuario = (Usuario) request.getSession(true).getAttribute("usuario");
		this.listaClientes = (List<Cliente>) request.getSession(false).getAttribute("clientesActivosList");
		this.clienteSelect = new Cliente();
		Date today = new Date();
		this.maxDate = new Date(today.getTime() );
		this.fecha_ini = new Date();
		this.fecha_fin = new Date();
	}
	

	
	public void exportarPdf() {
		String   message = null;
		Severity severity = null;
		
		String      fileName = null;
		InputStream input;
		byte[]      bytes = null;
		Integer     clienteCve = null;
		
		try {
			clienteCve = (clienteSelect == null) ? null : clienteSelect.getCteCve();
			
			if("A".equalsIgnoreCase(this.tipoReporte)) {
				
				fileName = String.format("AuxiliarClientes_%s.pdf", DateUtil.getString(fecha, DateUtil.FORMATO_YYYY_MM_DD));
				bytes = AuxiliarClientesReport.getPDF(fecha_ini, fecha_fin, clienteCve);
				log.info("El usuario {} ha descargado el reporte de contabilidad (auxiliar de clientes): {}", this.usuario.getUsuario(), fileName);
				
			} else if ("C".equalsIgnoreCase(this.tipoReporte)) {
				
				fileName = String.format("Conciliacion_%s.pdf", DateUtil.getString(fecha, DateUtil.FORMATO_YYYY_MM_DD));
				bytes = ConciliacionReport.getPDF(fecha_ini, fecha_fin, clienteCve);
				log.info("El usuario {} ha descargado el reporte de contabilidad (auxiliar de clientes): {}", this.usuario.getUsuario(), fileName);
				
			} else
				
				throw new InventarioException("Debe indicar el tipo de reporte.");
			
			input = new ByteArrayInputStream(bytes);
			this.file = DefaultStreamedContent.builder()
					.contentType("application/pdf")
					.name(fileName)
					.stream(() -> input)
					.contentLength(bytes.length)
					.build();
			
		} catch(InventarioException ex) {
			log.warn("Problema para descargar el reporte...", ex.getMessage());
			message = ex.getMessage();
			severity = FacesMessage.SEVERITY_WARN;
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, "Error en la descarga", message));
			PrimeFaces.current().ajax().update("form:messages");
		} catch(Exception ex) {
			log.error("Problema para descargar el reporte...", ex);
			message = String.format("No se pudo imprimir el reporte");
			severity = FacesMessage.SEVERITY_ERROR;
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, "Error en la descarga", message));
			PrimeFaces.current().ajax().update("form:messages");
		}
	}
	
	public void exportarExcel() {
		String   message = null;
		Severity severity = null;
		
		String      fileName = null;
		InputStream input;
		byte[]      bytes = null;
		Integer     clienteCve = null;
		
		try {
			clienteCve = (clienteSelect == null) ? null : clienteSelect.getCteCve();
			
			if("A".equalsIgnoreCase(this.tipoReporte)) {
				
				fileName = String.format("AuxiliarClientes_%s.xlsx", DateUtil.getString(fecha, DateUtil.FORMATO_YYYY_MM_DD));
				bytes = AuxiliarClientesReport.getXLSX(fecha_ini, fecha_fin, clienteCve);
				log.info("El usuario {} ha descargado el reporte de contabilidad (auxiliar de clientes): {}", this.usuario.getUsuario(), fileName);
				
			} else if ("C".equalsIgnoreCase(this.tipoReporte)) {
				
				fileName = String.format("Conciliacion_%s.xlsx", DateUtil.getString(fecha, DateUtil.FORMATO_YYYY_MM_DD));
				bytes = ConciliacionReport.getXLSX(fecha_ini, fecha_fin, clienteCve);
				log.info("El usuario {} ha descargado el reporte de contabilidad (auxiliar de clientes): {}", this.usuario.getUsuario(), fileName);
				
			} else
				
				throw new InventarioException("El tipo de reporte seleccionado es incorrecto");
			
			input = new ByteArrayInputStream(bytes);
			this.file = DefaultStreamedContent.builder()
					.contentType("application/vnd.ms-excel")
					.name(fileName)
					.stream(() -> input)
					.contentLength(bytes.length)
					.build();
			
		} catch(Exception ex) {
			log.error("Problema para descargar el reporte...", ex);
			message = String.format("No se pudo imprimir el reporte");
			severity = FacesMessage.SEVERITY_INFO;
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, "Error en la descarga", message));
			PrimeFaces.current().ajax().update("form:messages");
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
	public String getTipoReporte() {
		return tipoReporte;
	}
	public void setTipoReporte(String tipoReporte) {
		this.tipoReporte = tipoReporte;
	}
	public StreamedContent getFile() {
		return file;
	}
	public void setFile(StreamedContent file) {
		this.file = file;
	}
}
