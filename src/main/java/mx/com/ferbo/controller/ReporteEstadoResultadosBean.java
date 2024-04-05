package mx.com.ferbo.controller;

import java.io.File;
import java.io.Serializable;
import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;
import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.pie.PieChartDataSet;
import org.primefaces.model.charts.pie.PieChartModel;

import mx.com.ferbo.dao.EmisoresCFDISDAO;
import mx.com.ferbo.dao.RepEstadoCuentaDAO;
import mx.com.ferbo.model.EmisoresCFDIS;
import mx.com.ferbo.ui.RepEstadoCuenta;
import mx.com.ferbo.util.DateUtil;
import mx.com.ferbo.util.EntityManagerUtil;
import mx.com.ferbo.util.InventarioException;
import mx.com.ferbo.util.JasperReportUtil;
import mx.com.ferbo.util.conexion;

@Named
@ViewScoped
public class ReporteEstadoResultadosBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private static Logger log = LogManager.getLogger(ReporteEstadoResultadosBean.class);

	private List<EmisoresCFDIS> listaEmisores;
	private List<RepEstadoCuenta> listaestadoCuenta;

	private PieChartModel pieModel;

	private EmisoresCFDISDAO emisorDAO;
	private RepEstadoCuentaDAO estadoCuentaDAO;

	private Date fechaInicio;
	private Date fechaFin;
	private Date maxDate;
	private Date mesActual;

	private RepEstadoCuenta estadoCuenta;
	private EmisoresCFDIS emisor;

	public ReporteEstadoResultadosBean() {

		listaEmisores = new ArrayList<EmisoresCFDIS>();

		emisorDAO = new EmisoresCFDISDAO();
		emisor = new EmisoresCFDIS();
		estadoCuentaDAO = new RepEstadoCuentaDAO();
		setListaestadoCuenta(new ArrayList<RepEstadoCuenta>());

	}

	@PostConstruct
	public void init() {

		fechaInicio = new Date(Calendar.MONTH);
		fechaFin = new Date(Calendar.MONTH);
		mesActual = new Date();

		listaEmisores = emisorDAO.buscarTodos();

		Date today = new Date();

		maxDate = new Date(today.getTime());
		consultaGrafica();
	}

	public void consultar() {
		FacesMessage message = null;
		@SuppressWarnings("unused")
		Severity severity = null;
		String mensaje = null;
		Date finMes;
		finMes = DateUtil.getLastDayOfMonth(mesActual);
		try {
			if (emisor != null) {
				listaestadoCuenta = estadoCuentaDAO.listaEstadoCuenta(mesActual, emisor.getNb_emisor(), finMes);

			} else {
				throw new InventarioException("Selecciona almenos un emisor");
			}

		} catch (InventarioException ex) {
			mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_WARN;

			message = new FacesMessage(mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
		} catch (Exception e) {
			mensaje = e.getMessage();

			severity = FacesMessage.SEVERITY_WARN;

			message = new FacesMessage(mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
		} finally {
			PrimeFaces.current().ajax().update("form:dtestadoResultado", "form:messages");

		}

	}

	public void consultaGrafica() {
		pieModel = new PieChartModel();
		ChartData data = new ChartData();
		Random rnd = new Random();

		PieChartDataSet dataSet = new PieChartDataSet();
		List<Number> values = new ArrayList<>();

		Integer size = listaestadoCuenta.size();
		Integer i = 0;

		List<String> labels = new ArrayList<>();

		for (RepEstadoCuenta edc : listaestadoCuenta) {

			if (i < size) {
				values.add(edc.getVentas());
				labels.add(edc.getFecha().toString());
				i++;
			}

		}

		dataSet.setData(values);

		/*
		 * values.add(300); values.add(50); values.add(100);
		 */

		List<Integer> rgb = null;
		List<String> bgColors = new ArrayList<>();
		Integer numero;

		for (int bgcolor = 0; bgcolor < size; bgcolor++) { // repito las acciones la veces del tamaño de mi lista de
															// ocupacionCamaras son los objetos que se van a crear de
															// bgColors

			rgb = new ArrayList<Integer>();

			for (int color = 0; color < 3; color++) {// creo 3 numeros random tomando en cuenta el rango de valores RGB
				numero = (int) (rnd.nextDouble() * 256);
				rgb.add(numero);
			}

			bgColors.add(
					"rgb(" + rgb.get(0).toString() + "," + rgb.get(1).toString() + "," + rgb.get(2).toString() + ")");// le
																														// agrego
																														// los
																														// datos
																														// a
																														// la
																														// lista
																														// bgColors
			log.info(bgColors.get(bgcolor));
		}

		dataSet.setBackgroundColor(bgColors);
		data.addChartDataSet(dataSet);
		data.setLabels(labels);

		pieModel.setData(data);

		/*
		 * bgColors.add("rgb(255, 99, 132)"); bgColors.add("rgb(54, 162, 235)");
		 * bgColors.add("rgb(255, 205, 86)");
		 */

		/*
		 * labels.add("Red"); labels.add("Blue"); labels.add("Yellow");
		 */
	}

	public void exportarPdf() {
		log.info("exportando a PDF");
		String jasperPath = "/jasper/RepEstadoCuenta.jrxml";
		String filename = "reporteEstadoCuenta" + mesActual + ".pdf";
		String images = "/images/logo.jpeg";
		String message = null;
		Severity severity = null;
		File reportFile = new File(jasperPath);
		File imgfile = null;
		Date finMes;

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

			String emi = null;
			if (emisor == null) {
				emi = null;
			} else {
				emi = emisor.getNb_emisor();
			}
			finMes = DateUtil.getLastDayOfMonth(mesActual);
			connection = EntityManagerUtil.getConnection();
			parameters.put("REPORT_CONNECTION", connection);
			parameters.put("emisorN", emi);
			parameters.put("fechaIni", mesActual);
			parameters.put("fechaFin", finMes);
			parameters.put("imagen", imgfile.getPath());
			log.info("Parametros: " + parameters.toString());
			jasperReportUtil.createPdf(filename, parameters, reportFile.getPath());
		} catch (Exception ex) {
			log.error("Problema general...", ex);
			message = String.format("No se pudo imprimir el reporte");
			severity = FacesMessage.SEVERITY_INFO;
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(severity, "Error en impresion", message));
			PrimeFaces.current().ajax().update("form:messages");
		} finally {
			conexion.close((Connection) connection);
		}

	}

	public void exportarExcel() {

		log.info("exṕrtando a excel");
		String jasperPath = "/jasper/RepEstadoCuenta.jrxml";
		String filename = "reporteIngresos" + mesActual + ".xlsx";
		String images = "/images/logo.jpeg";
		String message = null;
		Severity severity = null;
		File reportFile = new File(jasperPath);
		File imgfile = null;
		Date finMes;
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

			String emi = null;
			if (emisor == null) {
				emi = null;
			} else {
				emi = emisor.getNb_emisor();
			}
			finMes = DateUtil.getLastDayOfMonth(mesActual);
			connection = EntityManagerUtil.getConnection();
			parameters.put("REPORT_CONNECTION", connection);
			parameters.put("emisorN", emi);
			parameters.put("fechaIni", mesActual);
			parameters.put("fechaFin", finMes);
			parameters.put("imagen", imgfile.getPath());
			log.info("Parametros: " + parameters.toString());
			jasperReportUtil.createXlsx(filename, parameters, reportFile.getPath());
		} catch (Exception ex) {
			log.error("Problema general...", ex);
			message = String.format("No se pudo imprimir el reporte");
			severity = FacesMessage.SEVERITY_INFO;
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(severity, "Error en impresion", message));
			PrimeFaces.current().ajax().update("form:messages");
		} finally {
			conexion.close((Connection) connection);
		}

	}

	public Date getMaxDate() {
		return maxDate;
	}

	public void setMaxDate(Date maxDate) {
		this.maxDate = maxDate;
	}

	public Date getMesActual() {
		return mesActual;
	}

	public void setMesActual(Date mesActual) {
		this.mesActual = mesActual;
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

	public EmisoresCFDIS getEmisor() {
		return emisor;
	}

	public void setEmisor(EmisoresCFDIS emisor) {
		this.emisor = emisor;
	}

	public EmisoresCFDISDAO getEmisorDAO() {
		return emisorDAO;
	}

	public void setEmisorDAO(EmisoresCFDISDAO emisorDAO) {
		this.emisorDAO = emisorDAO;
	}

	public List<EmisoresCFDIS> getListaEmisores() {
		return listaEmisores;
	}

	public void setListaEmisores(List<EmisoresCFDIS> listaEmisores) {
		this.listaEmisores = listaEmisores;
	}

	public RepEstadoCuenta getEstadoCuenta() {
		return estadoCuenta;
	}

	public void setEstadoCuenta(RepEstadoCuenta estadoCuenta) {
		this.estadoCuenta = estadoCuenta;
	}

	public List<RepEstadoCuenta> getListaestadoCuenta() {
		return listaestadoCuenta;
	}

	public void setListaestadoCuenta(List<RepEstadoCuenta> listaestadoCuenta) {
		this.listaestadoCuenta = listaestadoCuenta;
	}

	public PieChartModel getPieModel() {
		return pieModel;
	}

	public void setPieModel(PieChartModel pieModel) {
		this.pieModel = pieModel;
	}

}
