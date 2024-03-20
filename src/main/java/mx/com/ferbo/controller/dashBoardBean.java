package mx.com.ferbo.controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
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

import mx.com.ferbo.dao.ImporteEgresosDAO;

import mx.com.ferbo.dao.ParametroDAO;
import mx.com.ferbo.dao.ReportesVentasDAO;
import mx.com.ferbo.model.FacturacionGeneral;
import mx.com.ferbo.model.Parametro;
import mx.com.ferbo.model.VentasGlobales;

import mx.com.ferbo.dao.RepOcupacionCamaraDAO;
import mx.com.ferbo.ui.OcupacionCamara;

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
	private Date fechaPrueba = new Date();
	private VentasGlobales ventasGlobales;
	private ImporteEgresosDAO importeEgresosDAO;
	private ReportesVentasDAO reportesVentasDAO;
	
	private List<importeUtilidad> listaImporteUtilidad;

	private List<FacturacionGeneral> listaFacturacionGeneral;
	private List<FacturacionGeneral> listFacturacionMesAnt;
	private List<VentasGlobales> listaVentasGlobales;
	private List<FacturacionGeneral> listaVentaDia;
	

	private BarChartModel modelCamara;
	private List<OcupacionCamara> listOcupacionCamara;
	private RepOcupacionCamaraDAO ocupacionCamaraDAO;


	public dashBoardBean() {
		listaImporteUtilidad = new ArrayList<>();
		listaVentasGlobales = new ArrayList<>();
		importeEgresosDAO = new ImporteEgresosDAO();
		totalFacturacion = null;
		sumaEgresosSelect = null;
		ventasGlobales = new VentasGlobales();
		reportesVentasDAO = new ReportesVentasDAO();
		listaFacturacionGeneral = new ArrayList<>();
		listaVentaDia = new ArrayList<>();
		
		listOcupacionCamara = new ArrayList<>();
		ocupacionCamaraDAO = new RepOcupacionCamaraDAO();
		
	}

	@PostConstruct
	public void init()  {
		try {
			
			Date today = new Date();
			long oneDay = 24 * 60 * 60 * 1000;
			mesActual = new Date();
			//maxDate = new Date(today.getTime());
			grafica();
			ventaGlobales();
		
			graficaOcupacionCamaras();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void ventaGlobales() {
		Date iniMes;
		iniMes = DateUtil.getFirstDayOfMonth(fechaPrueba);
		listaVentasGlobales = reportesVentasDAO.ventasGanancias(iniMes, fechaPrueba);
		for (VentasGlobales vg : listaVentasGlobales) {
			ventasGlobales.setVentasTotales(vg.getVentasTotales());
			ventasGlobales.setGanancias(vg.getGanancias());
			ventasGlobales.setPorcentajeGanancias(vg.getPorcentajeGanancias());
		}
		//
	}
	
	public void VentaDia() {
		
		int dia = DateUtil.getDia(mesActual);		
		DateUtil.setDia(fechaPrueba,dia);	
		
		listaVentaDia = reportesVentasDAO.obtenerVentaDia(fechaPrueba);//venta del dia 
		
		//venta de mes anterior 
		
		PrimeFaces.current().ajax().update("form:dt-ventas");
		
	}
	
		public void facturacion() {
			//Date finMes;
			
			BigDecimal porcentaje = new BigDecimal(0).setScale(2);
			BigDecimal resta = new BigDecimal(0).setScale(2);
			BigDecimal acumulado = null;
			int count = 0;
			
			
			FacturacionGeneral factAct = new FacturacionGeneral();
			FacturacionGeneral factAnt = new FacturacionGeneral();
			
			Collections.sort(listaFacturacionGeneral);
			Collections.sort(listFacturacionMesAnt);
			
			for (FacturacionGeneral fg : listaFacturacionGeneral) { // 12 de marzo, 11 de marzo, 8, 9							
				
				
				if(fg.getTotal_facturacion().compareTo(new BigDecimal(0))==0) {
					fg.setTotal_facturacion(factAct.getTotal_facturacion());
				}
				factAct = fg;
				if(factAct.getTotal_facturacion() == null) {
					factAct.setTotal_facturacion(new BigDecimal(0));
				}
				
				for (FacturacionGeneral f : listFacturacionMesAnt) { // 12 de febrero , 10, 8, 9
					
					if(f.getTotal_facturacion().compareTo(new BigDecimal(0))==0) {
						f.setTotal_facturacion(factAnt.getTotal_facturacion());
					}
					factAnt = f;
					if(factAnt.getTotal_facturacion() ==null) {
						factAnt.setTotal_facturacion(new BigDecimal(0));
					}
					
					if(DateUtil.getDia(fg.getFecha()) == DateUtil.getDia(f.getFecha())) {
					
						resta = fg.getTotal_facturacion().subtract(f.getTotal_facturacion());
						System.out.println(resta);
						porcentaje = resta.divide(fg.getTotal_facturacion(),2,RoundingMode.HALF_UP);
						
						fg.setPorcentaje(porcentaje);
						count ++;
						break;						
					}
					porcentaje = new BigDecimal(0).setScale(2);
				}
				
				//acumulado = acumulado.add(fg.getTotal_facturacion());
				fg.setAcumulado(acumulado);				
			}			
			
			
			//listaFacturacionGeneral.add(facturacionSelected);
			mesActual = new Date();
			PrimeFaces.current().ajax().update("dt-facturacion");
		}
	
	public void readerList() {
		FacesMessage msj = null;
		String mensaje = null;
		Severity severity = null;
		
		listaFacturacionGeneral = new ArrayList<>();
		listFacturacionMesAnt = new ArrayList<>();
		try {
			
		Date iniMes;
		int dia = DateUtil.getDia(fechaPrueba);
		//finMes = DateUtil.getLastDayOfMonth(mesActual);
		iniMes = DateUtil.getFirstDayOfMonth(fechaPrueba);
		
		listaFacturacionGeneral = reportesVentasDAO.desgloseFacturacion(iniMes, fechaPrueba);
		
		//modificar fechas
		Calendar calendario = Calendar.getInstance();
		calendario.setTime(fechaPrueba);
		calendario.add(Calendar.MONTH, -1);
		fechaPrueba = calendario.getTime();
		iniMes = DateUtil.getFirstDayOfMonth(fechaPrueba);
		
		listFacturacionMesAnt = reportesVentasDAO.desgloseFacturacion(iniMes, fechaPrueba);
		listFacturacionMesAnt = mesAnterior(listFacturacionMesAnt,dia); //llenamos lista de mes anterior con fechas no existentes 
		listaFacturacionGeneral = mesActual(listaFacturacionGeneral, dia);//llenar lista de mes actual con fechas no existentes 
		facturacion();
		
		System.out.println("Lista mes actual: " + listaFacturacionGeneral);
		System.out.println("Lista mes anterior: " + listFacturacionMesAnt);
			
		} catch (Exception e) {
			e.printStackTrace();
			mensaje = e.getMessage();
			severity = FacesMessage.SEVERITY_ERROR;
			msj = new FacesMessage(severity, "Error Facturacion", mensaje);
			FacesContext.getCurrentInstance().addMessage(null, msj);
			
		} finally {
			PrimeFaces.current().ajax().update(":form:messages" );	
		}
	
		
		
	}
	
	public List<FacturacionGeneral> mesAnterior(List<FacturacionGeneral> listMesAnt , int dia) {
		
		BigDecimal porcentaje = new BigDecimal(0).setScale(2);		
		int count = 0;
		
		while(dia>0) {
			for (FacturacionGeneral f : listMesAnt) { // 12 de febrero , 10, 8, 9
			
				if(dia == DateUtil.getDia(f.getFecha())) {					
					count ++;
					break;						
				}
			
			}
			if(count==0) {
				Date fech = new Date();
				Calendar cal = Calendar.getInstance();
				cal.setTime(fech);
				cal.add(Calendar.MONTH, -1);
				
				fech = cal.getTime();
				
				DateUtil.setDia(fech, dia);				
				
				FacturacionGeneral factTemp = new FacturacionGeneral();					
				factTemp.setFecha(fech);
				factTemp.setAcumulado(new BigDecimal(0));
				factTemp.setPorcentaje(new BigDecimal(0));
				factTemp.setTotal_facturacion(new BigDecimal(0));
				listMesAnt.add(factTemp);				
			}
			count = 0;
			dia--;
		}			
		
		return listMesAnt;
		
	}
	
	public List<FacturacionGeneral> mesActual(List<FacturacionGeneral> listMesAct, int dia ) {
		
		int count  = 0;
		
		while(dia>0) {
			for(FacturacionGeneral ant: listMesAct) {//11 de marzo, 9 de marz				
				if(dia == DateUtil.getDia(ant.getFecha())) {					
					count ++;			
					break;
				}
			}
			
			if(count == 0) {
				Date fech = new Date(); //problema tomo fecha actual en marzo y genera fechas de mes de marzo
				
				Calendar cal = Calendar.getInstance();
				cal.setTime(fechaPrueba);
				cal.add(Calendar.MONTH, 1);
				
				fech = cal.getTime();
				
				DateUtil.setDia(fech, dia);	
				
				FacturacionGeneral factTemp = new FacturacionGeneral();					
				factTemp.setFecha(fech);
				factTemp.setAcumulado(new BigDecimal(0));
				factTemp.setPorcentaje(new BigDecimal(0));
				factTemp.setTotal_facturacion(new BigDecimal(0));
				listMesAct.add(factTemp);				
			}
			count = 0;
			dia--;
		}
		return listMesAct;
	}
	
	
		 
	public void grafica() throws ParseException {
		stackedGroupBarModel = new BarChartModel();
		ChartData data = new ChartData();
		fechaUtilidad = null;
		Date fechaInicial = new Date();
		Date fechaFinal = new Date();
	
	
		DateUtil.setDia(fechaInicial, 1);
		DateUtil.setMes(fechaInicial, 0);
		DateUtil.setAnio(fechaInicial,DateUtil.getAnio(fechaFinal) );		
		
		System.out.println(fechaFinal);
		System.out.println(fechaInicial);
		String nuevaFechaInicio = new SimpleDateFormat("yyyy-MM-dd").format(fechaInicial);
		String nuevaFechaFin = new SimpleDateFormat("yyyy-MM-dd").format(fechaFinal);
		listaImporteUtilidad = reportesVentasDAO.UtilidadPorMesAnual(nuevaFechaInicio, nuevaFechaFin);

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
				  
		List<String> listaFecha = new ArrayList<>();
		List<Number> listaPagos = new ArrayList<>();
		List<Number> listaEgresos = new ArrayList<>();
		List<Number> listaUtilidad = new ArrayList<>();
		
		for(importeUtilidad u : listaImporteUtilidad) {
			//u.setFecha(fechaInicial);
			
			listaPagos.add(u.getPagos());
			barDS.setData(listaPagos);
			

			listaEgresos.add(u.getEgresos());		
			barDS2.setData(listaEgresos);

			listaUtilidad.add(u.getUtilidadPerdida());
			barDS3.setData(listaUtilidad);			
			String parse = new SimpleDateFormat("MMM").format(u.getFecha());
			listaFecha.add(parse);
			
		
		}
		data.addChartDataSet(barDS);
		data.addChartDataSet(barDS2);
		data.addChartDataSet(barDS3);
		data.setLabels(listaFecha);
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

	public List<FacturacionGeneral> getListaVentaDia() {
		return listaVentaDia;
	}

	public void setListaVentaDia(List<FacturacionGeneral> listaVentaDia) {
		this.listaVentaDia = listaVentaDia;
	}

	public Date getFechaPrueba() {
		return fechaPrueba;
	}

	public void setFechaPrueba(Date fechaPrueba) {
		this.fechaPrueba = fechaPrueba;
	}

	public List<FacturacionGeneral> getListFacturacionMesAnt() {
		return listFacturacionMesAnt;
	}

	public void setListFacturacionMesAnt(List<FacturacionGeneral> listFacturacionMesAnt) {
		this.listFacturacionMesAnt = listFacturacionMesAnt;
	}

	public VentasGlobales getVentasGlobales() {
		return ventasGlobales;
	}

	public void setVentasGlobales(VentasGlobales ventasGlobales) {
		this.ventasGlobales = ventasGlobales;

	public BarChartModel getModelCamara() {
		return modelCamara;
	}

	public void setModelCamara(BarChartModel modelCamara) {
		this.modelCamara = modelCamara;

	}


	

}
