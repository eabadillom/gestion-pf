package mx.com.ferbo.controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.axes.cartesian.CartesianScales;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearAxes;
import org.primefaces.model.charts.bar.BarChartDataSet;
import org.primefaces.model.charts.bar.BarChartModel;
import org.primefaces.model.charts.bar.BarChartOptions;
import org.primefaces.model.charts.optionconfig.title.Title;
import org.primefaces.model.charts.optionconfig.tooltip.Tooltip;

import mx.com.ferbo.dao.ImporteEgresosDAO;
import mx.com.ferbo.dao.RepOcupacionCamaraDAO;
import mx.com.ferbo.ui.OcupacionCamara;
import mx.com.ferbo.ui.importeUtilidad;


@Named
@ViewScoped
public class dashBoardBean implements Serializable{

	private static final long serialVersionUID = -6551673633472266325L;
	private static Logger log = LogManager.getLogger(dashBoardBean.class);
    private BarChartModel stackedGroupBarModel;
	private importeUtilidad fechaUtilidad;
	private BigDecimal sumaIngresosSelect;
	private BigDecimal sumaEgresosSelect;
	private ImporteEgresosDAO importeEgresosDAO;
	private Date mesActual;
    private LineChartModel cartesianLinerModel;
	private List<importeUtilidad> listaImporteUtilidad;
	private BarChartModel modelCamara;
	private List<OcupacionCamara> listOcupacionCamara;
	private RepOcupacionCamaraDAO ocupacionCamaraDAO;
	

	public dashBoardBean() {
		listaImporteUtilidad = new ArrayList<>();
		importeEgresosDAO = new ImporteEgresosDAO();
		sumaIngresosSelect = null;
		sumaEgresosSelect = null;
		
		listOcupacionCamara = new ArrayList<>();
		ocupacionCamaraDAO = new RepOcupacionCamaraDAO();
		
	}

	@PostConstruct
	public void init()  {
		try {
			grafica();
			graficaOcupacionCamaras();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
		
		 
	public void grafica() throws ParseException {
		stackedGroupBarModel = new BarChartModel();
		ChartData data = new ChartData();
		fechaUtilidad = null;
		Date fechaAnt = null;
		String fechaActual = DateFormat.getDateInstance().format(new Date());
		Date fecha = new SimpleDateFormat("dd/MM/yyyy").parse(fechaActual);
		Calendar calendario = Calendar.getInstance();
		calendario.setTime(fecha);
		calendario.add(Calendar.MONTH, -1);
		String nuevaFecha = new SimpleDateFormat("dd/MM/yyyy").format(calendario.getTime());
		log.debug(nuevaFecha);
		fechaAnt=new SimpleDateFormat("dd/MM/yyyy").parse(nuevaFecha);  
	    log.debug(nuevaFecha+"\t"+fechaAnt);  
	    listaImporteUtilidad = importeEgresosDAO.obtenerUtilidadPorEmisor(null,fechaAnt);

		//Primer SET
				BarChartDataSet barDS = new BarChartDataSet();
				barDS.setLabel("Ingresos");
				barDS.setBackgroundColor("rgb(255,99,132)");
				barDS.setStack("Stack 0");
				
		//Segundo SET 
				BarChartDataSet barDS2 = new BarChartDataSet();
				barDS2.setLabel("Egresos");
				barDS2.setBackgroundColor("rgb(128,197,237)");
				barDS2.setStack("Stack 1");
				
				 
		//Tercer SET
				BarChartDataSet barDS3 = new BarChartDataSet();
				barDS3.setLabel("Utilidad/Perdida ");
				barDS3.setBackgroundColor("rgb(75,192,192)");
				barDS3.setStack("Stack 2");
				  
		List<String> listaEmisor = new ArrayList<>();
		List<Number> listaPagos = new ArrayList<>();
		List<Number> listaEgresos = new ArrayList<>();
		List<Number> listaUtilidad = new ArrayList<>();
		
		for(importeUtilidad u : listaImporteUtilidad) {
			u.setFecha(fechaAnt);
			
			listaPagos.add(u.getPagos());
			barDS.setData(listaPagos);
			

			listaEgresos.add(u.getEgresos());		
			barDS2.setData(listaEgresos);

			listaUtilidad.add(u.getUtilidadPerdida());
			barDS3.setData(listaUtilidad);			
			
			listaEmisor.add(u.getEmiNombre());
		
		}
		data.addChartDataSet(barDS);
		data.addChartDataSet(barDS2);
		data.addChartDataSet(barDS3);
		data.setLabels(listaEmisor);
		stackedGroupBarModel.setData(data);
	stackedGroupBarModel.setExtender("charExtender");
		//configuracion de labels

		
		//Options
		BarChartOptions options = new BarChartOptions();
		CartesianScales cScales = new CartesianScales();
		CartesianLinearAxes linearAxes = new CartesianLinearAxes();
		
		linearAxes.setStacked(false);
		linearAxes.setOffset(false);
		
		/*cScales.addXAxesData(linearAxes);
		cScales.addYAxesData(linearAxes);
		options.setScales(cScales);*/
		
		    stackedGroupBarModel.setOptions(options);
		    stackedGroupBarModel.setExtender("charExtender");
		Title title = new Title();
		title.setDisplay(false);
		title.setText("Balance General");
		options.setTitle(title);
		
		Title subtitle = new Title();
		subtitle.setDisplay(true);
		subtitle.setText("Ingresos - Egresos"); // Mostrar el primer valor de pagos como ejemplo
		options.setTitle(subtitle);
		Tooltip tooltip = new Tooltip();
		tooltip.setMode("index");
		tooltip.setIntersect(false);
		options.setTooltip(tooltip);
		stackedGroupBarModel.setOptions(options);
	}
	
	public void graficaOcupacionCamaras() {
		
		Date fecha = new Date();
		
		modelCamara = new BarChartModel();		
		ChartData dataSet = new ChartData();
		
		BarChartDataSet barDataSet = new BarChartDataSet();
		BarChartDataSet barDataSet2 = new BarChartDataSet();
		
		barDataSet.setLabel("Disponibles");
		barDataSet.setBackgroundColor("rgb(255, 99, 132)");
		barDataSet.setStack("Stack 0");
		
		barDataSet2.setLabel("Ocupadas");
		barDataSet2.setBackgroundColor("rgb(54, 162, 235)");
		barDataSet2.setStack("Stack 1");
		
		List<Number> values = new ArrayList<>();
		List<Number> values2 = new ArrayList<>();
		List<String> labels = new ArrayList<>();
		
		listOcupacionCamara = ocupacionCamaraDAO.ocupacionCamara(fecha, null, null, null);
		
		for(OcupacionCamara oc: listOcupacionCamara) {
			
			if(oc.getPlanta_ds().equals("P1 CENTRAL DE ABASTOS")) {
				values.add(oc.getPosiciones_Disponibles());
				values2.add(oc.getTarima());
				labels.add(oc.getPlanta_ds()+":"+oc.getCamara_ds());
			}
			
			if(oc.getPlanta_ds().equals("P2 TEPALCATES")) {
				values.add(oc.getPosiciones_Disponibles());
				values2.add(oc.getTarima());
				labels.add(oc.getPlanta_ds()+":"+oc.getCamara_ds());
			}
			
			if(oc.getPlanta_ds().equals("P3 CENTRAL DE ABASTOS")) {
				values.add(oc.getPosiciones_Disponibles());
				values2.add(oc.getTarima());
				labels.add(oc.getPlanta_ds()+":"+oc.getCamara_ds());
			}
			
			if(oc.getPlanta_ds().equals("P4 URBANA IXHUATEPEC")) {
				values.add(oc.getPosiciones_Disponibles());
				values2.add(oc.getTarima());
				labels.add(oc.getPlanta_ds()+":"+oc.getCamara_ds());
			}
			
			if(oc.getPlanta_ds().equals("P5 ORO")) {
				values.add(oc.getPosiciones_Disponibles());
				values2.add(oc.getTarima());
				labels.add(oc.getPlanta_ds()+":"+oc.getCamara_ds());
			}
			
		}
		
		barDataSet.setData(values);
		barDataSet2.setData(values2);
		dataSet.setLabels(labels);
		dataSet.addChartDataSet(barDataSet);
		dataSet.addChartDataSet(barDataSet2);
		
		modelCamara.setData(dataSet);
		modelCamara.setData(dataSet);
		modelCamara.setExtender("charExtender");
		
	}

	

	public BarChartModel getStackedGroupBarModel() {
		return stackedGroupBarModel;
	}

	public void setStackedGroupBarModel(BarChartModel stackedGroupBarModel) {
		this.stackedGroupBarModel = stackedGroupBarModel;
	}

	public importeUtilidad getFechaUtilidad() {
		return fechaUtilidad;
	}

	public void setFechaUtilidad(importeUtilidad fechaUtilidad) {
		this.fechaUtilidad = fechaUtilidad;
	}

	public Date getMesActual() {
		return mesActual;
	}

	public void setMesActual(Date mesActual) {
		this.mesActual = mesActual;
	}

	public List<importeUtilidad> getListaImporteUtilidad() {
		return listaImporteUtilidad;
	}

	public void setListaImporteUtilidad(List<importeUtilidad> listaImporteUtilidad) {
		this.listaImporteUtilidad = listaImporteUtilidad;
	}

	public BigDecimal getSumaEgresosSelect() {
		return sumaEgresosSelect;
	}

	public void setSumaEgresosSelect(BigDecimal sumaEgresosSelect) {
		this.sumaEgresosSelect = sumaEgresosSelect;
	}

	public BigDecimal getSumaIngresosSelect() {
		return sumaIngresosSelect;
	}

	public void setSumaIngresosSelect(BigDecimal sumaIngresosSelect) {
		this.sumaIngresosSelect = sumaIngresosSelect;
	}

	public LineChartModel getCartesianLinerModel() {
		return cartesianLinerModel;
	}

	public void setCartesianLinerModel(LineChartModel cartesianLinerModel) {
		this.cartesianLinerModel = cartesianLinerModel;
	}

	public static Logger getLog() {
		return log;
	}

	public static void setLog(Logger log) {
		dashBoardBean.log = log;
	}

	public BarChartModel getModelCamara() {
		return modelCamara;
	}

	public void setModelCamara(BarChartModel modelCamara) {
		this.modelCamara = modelCamara;
	}


	

}
