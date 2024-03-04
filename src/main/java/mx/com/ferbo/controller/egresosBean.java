package mx.com.ferbo.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.ejb.Init;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.axes.cartesian.CartesianScales;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearAxes;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearTicks;
import org.primefaces.model.charts.bar.BarChartDataSet;
import org.primefaces.model.charts.bar.BarChartModel;
import org.primefaces.model.charts.bar.BarChartOptions;
import org.primefaces.model.charts.optionconfig.animation.Animation;
import org.primefaces.model.charts.optionconfig.legend.Legend;
import org.primefaces.model.charts.optionconfig.legend.LegendLabel;
import org.primefaces.model.charts.optionconfig.title.Title;
import org.primefaces.model.charts.optionconfig.tooltip.Tooltip;
import org.primefaces.model.charts.pie.PieChartDataSet;
import org.primefaces.model.charts.pie.PieChartModel;

import mx.com.ferbo.dao.CategoriaEgresosDAO;
import mx.com.ferbo.dao.EmisoresCFDISDAO;
import mx.com.ferbo.dao.ImporteEgresosDAO;
import mx.com.ferbo.dao.egresosDAO;
import mx.com.ferbo.dao.tipoEgresoDAO;
import mx.com.ferbo.model.CategoriaEgreso;
import mx.com.ferbo.model.EmisoresCFDIS;
import mx.com.ferbo.model.ImporteEgreso;
import mx.com.ferbo.model.TipoEgreso;
import mx.com.ferbo.model.egresos;
import mx.com.ferbo.ui.RepEstadoCuenta;
import mx.com.ferbo.ui.importeUtilidad;
import mx.com.ferbo.util.DateUtil;
import mx.com.ferbo.util.EntityManagerUtil;
import mx.com.ferbo.util.JasperReportUtil;
import mx.com.ferbo.util.conexion;
import net.sf.jasperreports.engine.JRException;

@Named
@ViewScoped
public class egresosBean implements Serializable{
	private static Logger log = LogManager.getLogger(egresosBean.class);
	private static final long serialVersionUID = 1L;
	
	private egresos nuevoEgreso;
	private String conceptoEgreso;
	private Date fechaActual;
	private CategoriaEgreso categoriaSelect;
	private TipoEgreso tipoSelect;
	private EmisoresCFDIS emisor;
	private BarChartModel barModel;
    private BarChartModel stackedGroupBarModel;
	
	private ImporteEgreso nuevoImporte;
	private ImporteEgreso importeSelected;
	private String importe;
	private Date mesActual;
	
	private List<CategoriaEgreso> listaCatEgresos;
	private List<TipoEgreso> listaTipoEgresos;
	private List<EmisoresCFDIS> listaEmisores;
	private List<ImporteEgreso> listaImporteEgreso;
	private List<importeUtilidad> listaImporteUtilidad;
	private List<egresos> listaEgresos;
	private CategoriaEgresosDAO categoriaDAO;
	private tipoEgresoDAO tipoDAO;
	private EmisoresCFDISDAO emisoresDAO;
	private egresosDAO egresosDAO;
	private ImporteEgresosDAO importeEgresosDAO;
	private ImporteEgreso i;
	
	public egresosBean() {
		listaEmisores = new ArrayList<>();
		listaCatEgresos = new ArrayList<>();
		listaTipoEgresos = new ArrayList<>();
		listaEgresos = new ArrayList<>();
		categoriaDAO = new CategoriaEgresosDAO();
		tipoDAO = new tipoEgresoDAO();
		emisoresDAO = new EmisoresCFDISDAO();
		egresosDAO = new egresosDAO();
		fechaActual = new Date();
		importeEgresosDAO = new ImporteEgresosDAO();
		importeSelected = new ImporteEgreso();
		listaImporteUtilidad = new ArrayList<>();
	}
	
	@SuppressWarnings("unchecked")
	@PostConstruct
	public void Init() {
		listaImporteUtilidad = importeEgresosDAO.obtenerUtilidadPorEmisor(null, mesActual);
		nuevoImporte = new ImporteEgreso();
		listaEgresos = egresosDAO.buscarTodos();
		listaEmisores = emisoresDAO.buscarTodos();
		listaCatEgresos = categoriaDAO.findByAll();
		listaTipoEgresos = tipoDAO.findByAll();
		listaImporteEgreso = importeEgresosDAO.buscarTodos();
		mesActual = new Date();
		nuevoEgreso = new egresos();
		barModel = new BarChartModel();
		stackedGroupBarModel = new BarChartModel();
	}
	
	public void actualizar() {
		PrimeFaces.current().executeInitScript("PF('dg-importe').hide()");
		try {
			String msj = importeEgresosDAO.guardar(importeSelected);
			if(msj == null) {
				listaImporteEgreso = importeEgresosDAO.buscarTodos();
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Importe agregado"+ importeSelected.getImporte(), null));
				PrimeFaces.current().ajax().update("form:messages", "form:dt-egresos");
		} 
		}catch (Exception e) {
		e.printStackTrace();
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
				"Error al modificar " + importeSelected.getImporte(), null));
		PrimeFaces.current().ajax().update("form:messages");
		}
		//}else {
	}
	
	public void nuevoRegistroImporte() {
		nuevoImporte = new ImporteEgreso();
	}
	public void nuevoRegistro() {
		nuevoEgreso = new egresos();
	}
	
	public void handleToggle(ToggleEvent event) {
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Toggled", "Visibility:" + event.getVisibility());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

	public void guardar() {
		PrimeFaces.current().executeInitScript("PF('dg-agregaConcepto').hide()");
		String msj = null;
		
		if(msj == null) {
			listaEgresos.clear();
			nuevoEgreso.setNombreEgreso(conceptoEgreso);
			listaEgresos= egresosDAO.buscarTodos();
			msj = egresosDAO.guardar(nuevoEgreso);
			FacesContext.getCurrentInstance().addMessage( null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Egreso registrado", null));
			PrimeFaces.current().ajax().update("form:messages","form:messages");
		}else {
			FacesContext.getCurrentInstance().addMessage( null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Egreso no registrado", msj));
			PrimeFaces.current().ajax().update("form:messages");
		}
		//nuevoEgreso = new egresos();
	};

	public void exportarPDF() throws JRException, IOException, SQLException{
		log.info("exportando a PDF");
		String jasperPath = null;
		String filename = null;
		String images = null;
		String message = null;
		Severity severity = null;
		File reportFile = null;
		File imgfile = null;
		Date finMes;
		JasperReportUtil jasperReportUtil = new JasperReportUtil();
		Map<String, Object> parameters = null;
		Connection connection = null;
		parameters = new HashMap<String, Object>();

		try {
			jasperPath =  "/jasper/estado_de_resultados.jrxml";
			filename ="estado_de_resultados" + mesActual + ".pdf";
			images = "/images/logo.jpeg";
			reportFile = new File(jasperPath);
			URL resource = getClass().getResource(jasperPath);
			URL resourceimg = getClass().getResource(images);
			String file = resource.getFile();
			String img = resourceimg.getFile();
			reportFile = new File(file);
			imgfile = new File(img);
			log.info(reportFile.getPath());
			
			String emi = null;
			Integer cd_emi = null;
			

				if (cd_emi == null )
					emi = null;
				else
					emi = importeSelected.getCdEmisor().getNb_emisor();
			
			finMes = DateUtil.getLastDayOfMonth(mesActual);
			connection = EntityManagerUtil.getConnection();
			parameters.put("REPORT_CONNECTION", connection);
			parameters.put("EMISOR",emi );
			parameters.put("FECHAINI", mesActual);
			parameters.put("FECHAFIN", finMes);
			parameters.put("image", imgfile.getPath());
			log.info("Parametros: " + parameters.toString());
			jasperReportUtil.createPdf(filename, parameters, reportFile.getPath());
			//egresoPDF = jasperReportUtil.getPdf(filename, parameters, reportFile.getPath());
			/*jasperReportUtil = new JasperReportUtil();
			byte[] bytes = jasperReportUtil.createPDF(parameters, reportFile.getPath());
			InputStream input = new ByteArrayInputStream(bytes);
			this.egresoPDF = DefaultStreamedContent.builder()
					.contentType("application/pdf")
					.name(filename)
					.stream(() -> input )
					.build();*/
			log.info("Reporte generado {}...", filename);
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
		log.info("exportando a PDF");
		String jasperPath = null;
		String filename = null;
		String images = null;
		String message = null;
		Severity severity = null;
		File reportFile = null;
		File imgfile = null;
		Date finMes;
		JasperReportUtil jasperReportUtil = new JasperReportUtil();
		Map<String, Object> parameters = null;
		Connection connection = null;
		parameters = new HashMap<String, Object>();

		try {
			jasperPath =  "/jasper/estado_de_resultados.jrxml";
			filename ="estado_de_resultados" + mesActual + ".xlsx";
			images = "/images/logo.jpeg";
			reportFile = new File(jasperPath);
			URL resource = getClass().getResource(jasperPath);
			URL resourceimg = getClass().getResource(images);
			String file = resource.getFile();
			String img = resourceimg.getFile();
			reportFile = new File(file);
			imgfile = new File(img);
			log.info(reportFile.getPath());
			
			String emi = null;
			Integer cd_emi = null;
			

				if (cd_emi == null )
					emi = null;
				else
					emi = importeSelected.getCdEmisor().getNb_emisor();
			
			finMes = DateUtil.getLastDayOfMonth(mesActual);
			connection = EntityManagerUtil.getConnection();
			parameters.put("REPORT_CONNECTION", connection);
			parameters.put("EMISOR",emi );
			parameters.put("FECHAINI", mesActual);
			parameters.put("FECHAFIN", finMes);
			parameters.put("image", imgfile.getPath());
			log.info("Parametros: " + parameters.toString());
			jasperReportUtil.createXlsx(filename, parameters, reportFile.getPath());
			//egresoPDF = jasperReportUtil.getPdf(filename, parameters, reportFile.getPath());
			/*jasperReportUtil = new JasperReportUtil();
			byte[] bytes = jasperReportUtil.createPDF(parameters, reportFile.getPath());
			InputStream input = new ByteArrayInputStream(bytes);
			this.egresoPDF = DefaultStreamedContent.builder()
					.contentType("application/pdf")
					.name(filename)
					.stream(() -> input )
					.build();*/
			log.info("Reporte generado {}...", filename);
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
	
	public void grafica() throws ParseException {
	
		 	stackedGroupBarModel = new BarChartModel();
	        ChartData data = new ChartData();
	        Date fechaAnt = null;
			String fechaActual = DateFormat.getDateInstance().format(new Date());
			Date fecha = new SimpleDateFormat("dd/MM/yyyy").parse(fechaActual);
			Calendar calendario = Calendar.getInstance();
			calendario.setTime(fecha);
			calendario.add(Calendar.MONTH, -1);
			String nuevaFecha = new SimpleDateFormat("dd/MM/yyyy").format(calendario.getTime());
			System.out.println(nuevaFecha);
			fechaAnt=new SimpleDateFormat("dd/MM/yyyy").parse(nuevaFecha);  
		    System.out.println(nuevaFecha+"\t"+fechaAnt);  
		    listaImporteUtilidad = importeEgresosDAO.obtenerUtilidadPorEmisor(null,fechaAnt);
	        //Primer dataSet
	        BarChartDataSet barDataSet = new BarChartDataSet();
	        barDataSet.setLabel("Ingresos");
	        barDataSet.setBackgroundColor("rgb(255, 99, 132)");
	        barDataSet.setStack("Stack 0");
			
			List<Number> listaUtilidadPagos = new ArrayList<>();
			for(importeUtilidad u : listaImporteUtilidad)
				listaUtilidadPagos.add(u.getPagos());
			barDataSet.setData(listaUtilidadPagos);

			//Segundo DataSet
			BarChartDataSet barDataSet2 = new BarChartDataSet();
			barDataSet2.setLabel("Egresos");
			barDataSet2.setBackgroundColor("rgb(54, 162, 235)");
			barDataSet2.setStack("Stack 1");

			List<Number> listaUtilidadEgresos = new ArrayList<>();
			for(importeUtilidad u : listaImporteUtilidad)
				listaUtilidadEgresos.add(u.getEgresos());
			barDataSet2.setData(listaUtilidadEgresos);
	        
			//terccer dataset
			BarChartDataSet barDataSet3 = new BarChartDataSet();
			barDataSet3.setLabel("Utilidad/Perdida");
			barDataSet3.setBackgroundColor("rgb(75, 192, 192)");
			barDataSet3.setStack("Stack 2");

			List<Number> listaUtilidadPerdida = new ArrayList<>();
			for(importeUtilidad u : listaImporteUtilidad)
				listaUtilidadPerdida.add(u.getUtilidadPerdida());
			barDataSet3.setData(listaUtilidadPerdida);
	        			

	        data.addChartDataSet(barDataSet);
	       data.addChartDataSet(barDataSet2);
	        data.addChartDataSet(barDataSet3);

	        List<String> listaEmisores = new ArrayList<>();
	        for(importeUtilidad i : listaImporteUtilidad)
	        	listaEmisores.add(i.getEmiNombre());
	        data.setLabels(listaEmisores);
	        stackedGroupBarModel.setData(data);

	        //Options
	        BarChartOptions options = new BarChartOptions();
	        CartesianScales cScales = new CartesianScales();
	        CartesianLinearAxes linearAxes = new CartesianLinearAxes();
	        linearAxes.setStacked(true);
	        linearAxes.setOffset(true);
	        cScales.addXAxesData(linearAxes);
	        cScales.addYAxesData(linearAxes);
	        options.setScales(cScales);

	        Title title = new Title();
	        title.setDisplay(true);
	        title.setText("Balance General");
	        options.setTitle(title);

	        Tooltip tooltip = new Tooltip();
	        tooltip.setMode("index");
	        tooltip.setIntersect(false);
	        options.setTooltip(tooltip);

	        stackedGroupBarModel.setOptions(options);
	    	stackedGroupBarModel.setExtender("charExtender");

	}
	
	
	
	
	public egresos getNuevoEgreso() {
		return nuevoEgreso;
	}


	public void setNuevoEgreso(egresos nuevoEgreso) {
		this.nuevoEgreso = nuevoEgreso;
	}


	public CategoriaEgreso getCategoriaSelect() {
		return categoriaSelect;
	}

	public void setCategoriaSelect(CategoriaEgreso categoriaSelect) {
		this.categoriaSelect = categoriaSelect;
	}

	public TipoEgreso getTipoSelect() {
		return tipoSelect;
	}

	public void setTipoSelect(TipoEgreso tipoSelect) {
		this.tipoSelect = tipoSelect;
	}

	public List<CategoriaEgreso> getListaCatEgresos() {
		return listaCatEgresos;
	}

	public void setListaCatEgresos(List<CategoriaEgreso> listaCatEgresos) {
		this.listaCatEgresos = listaCatEgresos;
	}

	public List<TipoEgreso> getListaTipoEgresos() {
		return listaTipoEgresos;
	}

	public void setListaTipoEgresos(List<TipoEgreso> listaTipoEgresos) {
		this.listaTipoEgresos = listaTipoEgresos;
	}

	public List<EmisoresCFDIS> getListaEmisores() {
		return listaEmisores;
	}

	public void setListaEmisores(List<EmisoresCFDIS> listaEmisores) {
		this.listaEmisores = listaEmisores;
	}
	
	public ImporteEgreso getNuevoImporte() {
		return nuevoImporte;
	}

	public void setNuevoImporte(ImporteEgreso nuevoImporte) {
		this.nuevoImporte = nuevoImporte;
	}

	public List<egresos> getListaEgresos() {
		return listaEgresos;
	}

	public void setListaEgresos(List<egresos> listaEgresos) {
		this.listaEgresos = listaEgresos;
	}

	public String getImporte() {
		return importe;
	}

	public void setImporte(String importe) {
		this.importe = importe;
	}

	public Date getFechaActual() {
		return fechaActual;
	}

	public void setFechaActual(Date fechaActual) {
		this.fechaActual = fechaActual;
	}

	public List<ImporteEgreso> getListaImporteEgreso() {
		return listaImporteEgreso;
	}

	public void setListaImporteEgreso(List<ImporteEgreso> listaImporteEgreso) {
		this.listaImporteEgreso = listaImporteEgreso;
	}

	public ImporteEgreso getImporteSelected() {
		return importeSelected;
	}

	public void setImporteSelected(ImporteEgreso importeSelected) {
		this.importeSelected = importeSelected;
	}

	public Date getMesActual() {
		return mesActual;
	}

	public void setMesActual(Date mesActual) {
		this.mesActual = mesActual;
	}

	public EmisoresCFDIS getEmisor() {
		return emisor;
	}

	public void setEmisor(EmisoresCFDIS emisor) {
		this.emisor = emisor;
	}


	public String getConceptoEgreso() {
		return conceptoEgreso;
	}

	public void setConceptoEgreso(String conceptoEgreso) {
		this.conceptoEgreso = conceptoEgreso;
	}

	public BarChartModel getBarModel() {
		return barModel;
	}

	public void setBarModel(BarChartModel barModel) {
		this.barModel = barModel;
	}

	public ImporteEgreso getI() {
		return i;
	}

	public void setI(ImporteEgreso i) {
		this.i = i;
	}

	public List<importeUtilidad> getListaImporteUtilidad() {
		return listaImporteUtilidad;
	}

	public void setListaImporteUtilidad(List<importeUtilidad> listaImporteUtilidad) {
		this.listaImporteUtilidad = listaImporteUtilidad;
	}

	public BarChartModel getStackedGroupBarModel() {
		return stackedGroupBarModel;
	}

	public void setStackedGroupBarModel(BarChartModel stackedGroupBarModel) {
		this.stackedGroupBarModel = stackedGroupBarModel;
	}


	
	
}
