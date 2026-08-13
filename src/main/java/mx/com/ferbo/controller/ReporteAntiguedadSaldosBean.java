package mx.com.ferbo.controller;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
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
import org.primefaces.model.StreamedContent;

import com.ferbo.gestion.reports.jasper.AntiguedadSaldosJR;
import com.ferbo.gestion.reports.util.TipoAntiguedadSaldos;

import mx.com.ferbo.dao.EmisoresCFDISDAO;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.EmisoresCFDIS;
import mx.com.ferbo.model.Usuario;
import mx.com.ferbo.util.EntityManagerUtil;
import mx.com.ferbo.util.FacesUtils;
import mx.com.ferbo.util.InventarioException;
import mx.com.ferbo.util.conexion;
import net.sf.jasperreports.engine.JRException;

@Named
@ViewScoped
public class ReporteAntiguedadSaldosBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private static Logger log = LogManager.getLogger(ReporteAntiguedadSaldosBean.class);

	private Date fecha;
	private Date maxDate;
	private String tipoReporte;

	private List<Cliente> listClientesSelect;
	private List<Cliente> listaClientes;
	private List<EmisoresCFDIS> emisorList;
	private EmisoresCFDIS emisorSelect;
	private EmisoresCFDISDAO emisoresDAO;

	private FacesContext context;
	private HttpServletRequest request;
	private Usuario usuario;

	private AntiguedadSaldosJR antiguedadSaldosJR = null;
	private StreamedContent file;

	public ReporteAntiguedadSaldosBean() {
		this.fecha = new Date();
		this.listaClientes = new ArrayList<Cliente>();
		this.emisoresDAO = new EmisoresCFDISDAO();
	}

	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() {
		this.context = FacesContext.getCurrentInstance();
		this.request = (HttpServletRequest) this.context.getExternalContext().getRequest();
		this.usuario = (Usuario) this.request.getSession(false).getAttribute("usuario");

		this.listaClientes = (List<Cliente>) request.getSession(false).getAttribute("clientesActivosList");
		this.listClientesSelect = new ArrayList<Cliente>();
		this.emisorList = emisoresDAO.buscarTodos();
		this.emisorSelect = new EmisoresCFDIS();

		Date today = new Date();
		this.maxDate = new Date(today.getTime());
		log.info("El usuario {} esta ingresando a reporte de antiguedad de saldos", this.usuario.getUsuario());
	}

	public void exportarPdf() throws JRException, IOException, SQLException {
		log.info("Exportando a pdf...");
		String images = "/images/logoF.png";
		String message = null;
		Severity severity = null;
		Connection connection = null;
		String filename = null;

		try {
			if (this.tipoReporte == null || this.tipoReporte.isEmpty()) {
				throw new InventarioException("Debe seleccionar el tipo de filtro de descarga");
			}

			List<Integer> listClientes = null;
			if (this.listClientesSelect != null || !this.listClientesSelect.isEmpty()) {
				listClientes = new ArrayList<Integer>();
				for (Cliente cliente : this.listClientesSelect) {
					listClientes.add(cliente.getCteCve());
				}
			}

			String emisorRFC = null;
			if (this.emisorSelect != null) {
				emisorRFC = this.emisorSelect.getNb_rfc();
			}

			connection = EntityManagerUtil.getConnection();

			if (this.tipoReporte.matches("Condensado")) {
				filename = String.format("AntiguedadSaldosCondensado%s.pdf", this.fecha);

				this.antiguedadSaldosJR = new AntiguedadSaldosJR(connection, images, TipoAntiguedadSaldos.CONDENSADO);
				byte[] bytes = this.antiguedadSaldosJR.getPDF(listClientes, emisorRFC, this.fecha);
				this.file = FacesUtils.toPDF(bytes, filename);
				log.info("El usuario {} descargo el reporte de antiguedad de saldos {}", this.usuario.getUsuario(), filename);
			} else if (tipoReporte.matches("Desglosado")) {
				filename = String.format("AntiguedadSaldosDesglosado%s.pdf", this.fecha);

				this.antiguedadSaldosJR = new AntiguedadSaldosJR(connection, images, TipoAntiguedadSaldos.DESGLOSADO);
				byte[] bytes = this.antiguedadSaldosJR.getPDF(listClientes, emisorRFC, this.fecha);
				this.file = FacesUtils.toPDF(bytes, filename);
				log.info("El usuario {} descargo el reporte de antiguedad de saldos {}", this.usuario.getUsuario(), filename);
			}

		} catch (Exception ex) {
			log.error("Problema general...", ex);
			message = String.format("No se puede descargar el reporte");
			severity = FacesMessage.SEVERITY_ERROR;
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, "Error en impresion", message));
		} finally {
			EntityManagerUtil.close((Connection) connection);
			PrimeFaces.current().ajax().update("form:messages", "form:dt-ClienteAlmacen");
		}

	}

	public void exportarExcel() throws JRException, IOException, SQLException {
		log.info("Exportando a excel...");
		String images = "/images/logo.jpeg";
		String message = null;
		Severity severity = null;
		Connection connection = null;
		String filename = null;

		try {
			if (this.tipoReporte == null || this.tipoReporte.isEmpty()) {
				throw new InventarioException("Debe seleccionar el tipo de filtro de descarga");
			}

			List<Integer> listClientes = null;
			if (this.listClientesSelect != null || !this.listClientesSelect.isEmpty()) {
				listClientes = new ArrayList<Integer>();
				for (Cliente cliente : this.listClientesSelect) {
					listClientes.add(cliente.getCteCve());
				}
			}

			String emisorRFC = null;
			if (this.emisorSelect != null) {
				emisorRFC = this.emisorSelect.getNb_rfc();
			}

			connection = EntityManagerUtil.getConnection();

			if (this.tipoReporte.matches("Condensado")) {
				filename = String.format("AntiguedadSaldosCondensado%s.xlsx", this.fecha);

				this.antiguedadSaldosJR = new AntiguedadSaldosJR(connection, images, TipoAntiguedadSaldos.CONDENSADO);
				byte[] bytes = this.antiguedadSaldosJR.getXLSX(listClientes, emisorRFC, this.fecha);
				this.file = FacesUtils.toXLSX(bytes, filename);
				log.info("El usuario {} descargo el reporte de antiguedad de saldos {}", this.usuario.getUsuario(), filename);
			} else if (this.tipoReporte.matches("Desglosado")) {
				filename = String.format("AntiguedadSaldosDesglosado%s.xlsx", this.fecha);

				this.antiguedadSaldosJR = new AntiguedadSaldosJR(connection, images, TipoAntiguedadSaldos.DESGLOSADO);
				byte[] bytes = this.antiguedadSaldosJR.getXLSX(listClientes, emisorRFC, this.fecha);
				this.file = FacesUtils.toXLSX(bytes, filename);
				log.info("El usuario {} descargo el reporte de antiguedad de saldos {}", this.usuario.getUsuario(), filename);
			}
		} catch (Exception ex) {
			log.error("Problema general...", ex);
			message = String.format("No se puede descargar el reporte");
			severity = FacesMessage.SEVERITY_ERROR;
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(severity, "Error en impresion", message));
		} finally {
			conexion.close((Connection) connection);
			PrimeFaces.current().ajax().update("form:messages", "form:dt-ClienteAlmacen");
		}
	}

	public String getClientesSeleccionadosLabel() {
		if (listClientesSelect == null || listClientesSelect.isEmpty()) {
			return "Todos los clientes";
		}

		int size = listClientesSelect.size();
		return size == 1 ? "1 cliente seleccionado" : size + " clientes seleccionados";
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public List<Cliente> getListClientesSelect() {
		return listClientesSelect;
	}

	public void setListClientesSelect(List<Cliente> listClientesSelect) {
		this.listClientesSelect = listClientesSelect;
	}

	public List<Cliente> getListaClientes() {
		return listaClientes;
	}

	public void setListaClientes(List<Cliente> listaClientes) {
		this.listaClientes = listaClientes;
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

	public List<EmisoresCFDIS> getEmisorList() {
		return emisorList;
	}

	public void setEmisorList(List<EmisoresCFDIS> emisorList) {
		this.emisorList = emisorList;
	}

	public EmisoresCFDIS getEmisorSelect() {
		return emisorSelect;
	}

	public void setEmisorSelect(EmisoresCFDIS emisorSelect) {
		this.emisorSelect = emisorSelect;
	}

	public StreamedContent getFile() {
		return file;
	}

	public void setFile(StreamedContent file) {
		this.file = file;
	}

}
