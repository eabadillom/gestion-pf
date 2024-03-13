package mx.com.ferbo.controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
import org.primefaces.PrimeFaces;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.axes.cartesian.CartesianScales;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearAxes;
import org.primefaces.model.charts.bar.BarChartDataSet;
import org.primefaces.model.charts.bar.BarChartModel;
import org.primefaces.model.charts.bar.BarChartOptions;
import org.primefaces.model.charts.optionconfig.title.Title;
import org.primefaces.model.charts.optionconfig.tooltip.Tooltip;

import mx.com.ferbo.dao.FacturaDAO;
import mx.com.ferbo.dao.ImporteEgresosDAO;
import mx.com.ferbo.dao.ReportesVentasDAO;
import mx.com.ferbo.model.FacturacionGeneral;
import mx.com.ferbo.ui.importeUtilidad;
import mx.com.ferbo.util.DateUtil;


@Named
@ViewScoped
public class dashBoardBean implements Serializable{

	private static final long serialVersionUID = -6551673633472266325L;
	private static Logger log = LogManager.getLogger(dashBoardBean.class);
    private BarChartModel stackedGroupBarModel;
	private importeUtilidad fechaUtilidad;
    private LineChartModel cartesianLinerModel;
	private FacturacionGeneral facturacionSelected;
	
	private BigDecimal totalFacturacion;
	private BigDecimal sumaEgresosSelect;
	private Date mesActual;
	private Date maxDate;
	
	private ImporteEgresosDAO importeEgresosDAO;
	private ReportesVentasDAO reportesVentasDAO;
	
	private List<importeUtilidad> listaImporteUtilidad;
	private List<FacturacionGeneral> listaFacturacionGeneral;
	

	public dashBoardBean() {
		listaImporteUtilidad = new ArrayList<>();
		importeEgresosDAO = new ImporteEgresosDAO();
		totalFacturacion = null;
		sumaEgresosSelect = null;
		reportesVentasDAO = new ReportesVentasDAO();
		listaFacturacionGeneral = new ArrayList<>();
		//mesActual = new Date();
	}

	@PostConstruct
	public void init()  {
		try {
			Date today = new Date();
			long oneDay = 24 * 60 * 60 * 1000;
			mesActual = new Date();
			maxDate = new Date(today.getTime());
			grafica();
		//	facturacion();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
		public void facturacion() throws ParseException {
			//Date finMes;
			BigDecimal porcentaje = new BigDecimal(0).setScale(2);
			BigDecimal acumulado = null;
			Date iniMes;
			//finMes = DateUtil.getLastDayOfMonth(mesActual);
			iniMes = DateUtil.getFirstDayOfMonth(mesActual);
			listaFacturacionGeneral = reportesVentasDAO.desgloseFacturacion(iniMes, mesActual);
			
			//modificar fechas
			Calendar calendario = Calendar.getInstance();
			calendario.setTime(mesActual);
			calendario.add(Calendar.MONTH, -1);
			mesActual = calendario.getTime();
			iniMes = DateUtil.getFirstDayOfMonth(mesActual);
			
			List<FacturacionGeneral> listFacturacionMesAnt = reportesVentasDAO.desgloseFacturacion(iniMes, mesActual);
			
			for (FacturacionGeneral fg : listaFacturacionGeneral) {
				for (FacturacionGeneral f : listFacturacionMesAnt) {
				
					if(DateUtil.getDia(fg.getFecha()) == DateUtil.getDia(f.getFecha())) {
						if(fg.getTotal_facturacion() .compareTo( f.getTotal_facturacion()) == 1 ) {
							porcentaje = ((fg.getTotal_facturacion().subtract(f.getTotal_facturacion())).divide(fg.getTotal_facturacion(),2,RoundingMode.HALF_UP));
						}else {
							porcentaje = ((f.getTotal_facturacion().subtract(fg.getTotal_facturacion())).divide(f.getTotal_facturacion(),2,RoundingMode.HALF_UP));
						}
						fg.setPorcentaje(porcentaje);
						break;
					}else {
						fg.setPorcentaje(new BigDecimal(0));
					}
				
				}
				//acumulado = acumulado.add(fg.getTotal_facturacion());
				fg.setAcumulado(acumulado);				
			}			
			//listaFacturacionGeneral.add(facturacionSelected);
			
			mesActual = new Date();
			PrimeFaces.current().ajax().update("dt-facturacion");
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
		System.out.println(nuevaFecha);
		fechaAnt=new SimpleDateFormat("dd/MM/yyyy").parse(nuevaFecha);  
	    System.out.println(nuevaFecha+"\t"+fechaAnt);  
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
		subtitle.setText("Ingesos - Egresos"); // Mostrar el primer valor de pagos como ejemplo
		options.setTitle(subtitle);
		Tooltip tooltip = new Tooltip();
		tooltip.setMode("index");
		tooltip.setIntersect(false);
		options.setTooltip(tooltip);
		stackedGroupBarModel.setOptions(options);
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
		return totalFacturacion;
	}

	public void setSumaIngresosSelect(BigDecimal sumaIngresosSelect) {
		this.totalFacturacion = sumaIngresosSelect;
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

	public FacturacionGeneral getFacturacionSelected() {
		return facturacionSelected;
	}

	public void setFacturacionSelected(FacturacionGeneral facturacionSelected) {
		this.facturacionSelected = facturacionSelected;
	}

	public List<FacturacionGeneral> getListaFacturacionGeneral() {
		return listaFacturacionGeneral;
	}

	public void setListaFacturacionGeneral(List<FacturacionGeneral> listaFacturacionGeneral) {
		this.listaFacturacionGeneral = listaFacturacionGeneral;
	}

	public BigDecimal getTotalFacturacion() {
		return totalFacturacion;
	}

	public void setTotalFacturacion(BigDecimal totalFacturacion) {
		this.totalFacturacion = totalFacturacion;
	}

	public Date getMaxDate() {
		return maxDate;
	}

	public void setMaxDate(Date maxDate) {
		this.maxDate = maxDate;
	}


	

}
