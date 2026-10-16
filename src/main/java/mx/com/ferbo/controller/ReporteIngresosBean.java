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
import org.primefaces.model.StreamedContent;

import com.ferbo.gestion.reports.jasper.ingresos.IngresosJR;

import mx.com.ferbo.dao.BancoDAO;
import mx.com.ferbo.model.Bancos;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.util.EntityManagerUtil;
import mx.com.ferbo.util.FacesUtils;
import mx.com.ferbo.util.JasperReportUtil;
import mx.com.ferbo.util.conexion;

@Named
@ViewScoped
public class ReporteIngresosBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private static Logger log = LogManager.getLogger(ReporteAlmacenFechaBean.class);
	
	private List<Cliente> listCliente;
	private List<Bancos> listBanco;
	
	private Cliente clienteSelect;
	private Bancos bancoSelect;
	
	private BancoDAO bancoDAO;
	
	private Date fechaInicio;
	private Date fechaFin;
	private Date fechaActual;
	private Date maxDate;
	
	private FacesContext faceContext;
    private HttpServletRequest request;
    private StreamedContent file;
	
	public ReporteIngresosBean() {
		
		listCliente = new ArrayList<Cliente>();
		listBanco = new ArrayList<Bancos>();
		bancoDAO = new BancoDAO();
		clienteSelect = new Cliente();
		bancoSelect = new Bancos();
		
	}
	
	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() {
		
		faceContext = FacesContext.getCurrentInstance();
        request = (HttpServletRequest) faceContext.getExternalContext().getRequest();
        
        listCliente = (List<Cliente>) request.getSession(false).getAttribute("clientesActivosList");
        listBanco = bancoDAO.findall();
		
		fechaInicio = new Date();
		fechaFin = new Date();
		fechaActual = new Date();
		
		Date today = new Date();
		maxDate = new Date(today.getTime() );
	}
	
	public void exportarPdf() {
		String message;
		Severity severity;
		log.info("exportando a PDF");
		Connection connection = null;
		byte[] bytes;

		final String images = "/images/logoF.png";
		String filename = "reporteIngresos" + fechaActual + ".pdf";
		
		try {
			Integer clienteCve = this.clienteSelect == null ? null : this.clienteSelect.getCteCve();
			Integer idBanco = this.bancoSelect == null ? null : this.bancoSelect.getId();
			
			connection = EntityManagerUtil.getConnection();
			IngresosJR jasper = new IngresosJR(connection, images);
			bytes = jasper.getPDF(fechaInicio, fechaFin, clienteCve, idBanco);
			
			this.file = FacesUtils.toPDF(bytes, filename);
		} catch (Exception ex) {
			log.error("Problema general...", ex);
			message = String.format("No se pudo imprimir el reporte");
			severity = FacesMessage.SEVERITY_INFO;
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, "Error en impresion", message));
			PrimeFaces.current().ajax().update("form:messages");
		} finally {
			conexion.close((Connection) connection);
		}
	}
	
	public void exportarExcel() {
		String message;
		Severity severity;
		log.info("exportando a XLSX");
		Connection connection = null;
		byte[] bytes;

		final String images = "/images/logoF.png";
		String filename = "reporteIngresos" + fechaActual + ".xlsx";
		
		try {
			Integer clienteCve = this.clienteSelect == null ? null : this.clienteSelect.getCteCve();
			Integer idBanco = this.bancoSelect == null ? null : this.bancoSelect.getId();
			
			connection = EntityManagerUtil.getConnection();
			IngresosJR jasper = new IngresosJR(connection, images);
			bytes = jasper.getXLSX(fechaInicio, fechaFin, clienteCve, idBanco);
			
			this.file = FacesUtils.toXLSX(bytes, filename);
		} catch (Exception ex) {
			log.error("Problema general...", ex);
			message = String.format("No se pudo imprimir el reporte");
			severity = FacesMessage.SEVERITY_INFO;
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, "Error en impresion", message));
			PrimeFaces.current().ajax().update("form:messages");
		} finally {
			conexion.close((Connection) connection);
		}
	}

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Date getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	public List<Cliente> getListCliente() {
		return listCliente;
	}

	public void setListCliente(List<Cliente> listCliente) {
		this.listCliente = listCliente;
	}

	public Cliente getClienteSelect() {
		return clienteSelect;
	}

	public void setClienteSelect(Cliente clienteSelect) {
		this.clienteSelect = clienteSelect;
	}
	
	public List<Bancos> getListBanco() {
		return listBanco;
	}

	public void setListBanco(List<Bancos> listBanco) {
		this.listBanco = listBanco;
	}

	public Bancos getBancoSelect() {
		return bancoSelect;
	}

	public void setBancoSelect(Bancos bancoSelect) {
		this.bancoSelect = bancoSelect;
	}

	public Date getFechaActual() {
		return fechaActual;
	}

	public void setFechaActual(Date fechaActual) {
		this.fechaActual = fechaActual;
	}

	public Date getMaxDate() {
		return maxDate;
	}

	public void setMaxDate(Date maxDate) {
		this.maxDate = maxDate;
	}

	public StreamedContent getFile() {
		return file;
	}

	public void setFile(StreamedContent file) {
		this.file = file;
	}

	
}
