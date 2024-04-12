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
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

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
import org.primefaces.model.charts.donut.DonutChartDataSet;
import org.primefaces.model.charts.donut.DonutChartModel;
import org.primefaces.model.charts.donut.DonutChartOptions;
import org.primefaces.model.charts.optionconfig.title.Title;
import org.primefaces.model.charts.optionconfig.tooltip.Tooltip;

import mx.com.ferbo.dao.PlantaDAO;
import mx.com.ferbo.dao.RepOcupacionCamaraDAO;
import mx.com.ferbo.dao.ReportesVentasDAO;
import mx.com.ferbo.model.FacturacionGeneral;
import mx.com.ferbo.model.Planta;
import mx.com.ferbo.model.VentasGlobales;
import mx.com.ferbo.ui.OcupacionCamara;
import mx.com.ferbo.ui.ImporteUtilidad;
import mx.com.ferbo.util.DateUtil;
import mx.com.ferbo.util.ReporteVentaUtil;

@Named
@ViewScoped
public class DashBoardBean implements Serializable {

	private static final long serialVersionUID = -6551673633472266325L;
	private static Logger log = LogManager.getLogger(DashBoardBean.class);

	private DonutChartModel donutModel;
	private BarChartModel stackedGroupBarModel;
	private BarChartModel smVentasFormaPago;

	private ImporteUtilidad fechaUtilidad;
	private LineChartModel cartesianLinerModel;
	private FacturacionGeneral facturacionSelected;

	private BigDecimal totalFacturacion;
	private BigDecimal sumaEgresosSelect;
	private Date mesActual;
	private Date maxDate;
	private Date fechaPrueba;
	private VentasGlobales ventasGlobales;
	private ReportesVentasDAO reportesVentasDAO;

	private List<ImporteUtilidad> listaImporteUtilidad;
	private List<FacturacionGeneral> listaFacturacionGeneral;
	private List<FacturacionGeneral> listFacturacionMesAnt;
	private List<VentasGlobales> listaVentasGlobales;
	private List<FacturacionGeneral> listaVentaDia;
	private List<FacturacionGeneral> listaImporteGlobal;
	private List<FacturacionGeneral> listaVentaRazonSocial;
	private List<FacturacionGeneral> listaGananciaRazonSocial;
	private List<FacturacionGeneral> listaVentaPagos;

	private BarChartModel modelCamara;
	private List<OcupacionCamara> listOcupacionCamara;
	private RepOcupacionCamaraDAO ocupacionCamaraDAO;

	private List<Planta> listPlantas;
	private PlantaDAO plantaDAO;

	public DashBoardBean() {
		listaImporteUtilidad = new ArrayList<>();
		listaVentasGlobales = new ArrayList<>();
		totalFacturacion = null;
		sumaEgresosSelect = null;
		ventasGlobales = new VentasGlobales();
		reportesVentasDAO = new ReportesVentasDAO();
		listaFacturacionGeneral = new ArrayList<>();
		listaVentaDia = new ArrayList<>();
		listaVentaRazonSocial = new ArrayList<>();
		listaGananciaRazonSocial = new ArrayList<>();
		listaVentaPagos = new ArrayList<>();

		listOcupacionCamara = new ArrayList<>();
		ocupacionCamaraDAO = new RepOcupacionCamaraDAO();

		listPlantas = new ArrayList<Planta>();
		plantaDAO = new PlantaDAO();

	}

	@PostConstruct
	public void init() {
		try {
			fechaPrueba = new Date();
			mesActual = new Date();

			grafica();
			ventaGlobales();
			ventasPorMes();
			ventaRazonSocial();
			gananciaPorRazonSocial();
			createDonutModel();
			graficaOcupacionCamaras();
			VentaDia();
			ventaPorPago();
			createStackedBarModel();

			Date today = new Date();
			maxDate = new Date(today.getTime());

		} catch (ParseException e) {

			e.printStackTrace();
		}
	}

	public void ventaPorPago() {

		Date fechaInicio = new Date();
		DateUtil.setDia(fechaInicio, 01);
		DateUtil.setMes(fechaInicio, 00);

		Date fechaFin = new Date();

		listaVentaPagos = reportesVentasDAO.ventaPorFormaPago(fechaInicio, fechaFin);

	}

	public void ventaRazonSocial() {

		Date fechaInicio = DateUtil.getFirstDayOfMonth(fechaPrueba);
		Date fechaFin = new Date();

		listaVentaRazonSocial = reportesVentasDAO.ventaPorRazonSocial(fechaInicio, fechaFin);

		PrimeFaces.current().ajax().update("form:dt-ventaRS");

	}

	public void gananciaPorRazonSocial() {

		Date fechaFin = new Date();
		Date fechainicio = DateUtil.getFirstDayOfMonth(fechaFin);

		listaGananciaRazonSocial = reportesVentasDAO.gananciaPorRazonSocial(fechainicio, fechaFin);

		PrimeFaces.current().ajax().update("form:dt-gananciaRS");

	}

	// @SuppressWarnings("static-access")
	public void ventaGlobales() {
		Date iniMes;
		iniMes = DateUtil.getFirstDayOfMonth(fechaPrueba);
		listaVentasGlobales = reportesVentasDAO.ventasGanancias(iniMes, fechaPrueba);
		try {
			
			
			for (VentasGlobales vg : listaVentasGlobales) {					
					ventasGlobales.setVentasTotales(vg.getVentasTotales());
					ventasGlobales.setGanancias(vg.getGanancias());
					ventasGlobales.setPorcentajeGanancias(vg.getPorcentajeGanancias().setScale(2, RoundingMode.DOWN));
			}
			
		} catch (Exception e) {// ??????????????????????
			/*
			 * if(ventasGlobales.getVentasTotales() == null) {
			 * ventasGlobales.setVentasTotales(new BigDecimal(0)); }
			 * if(ventasGlobales.getGanancias() == null) { ventasGlobales.setGanancias(new
			 * BigDecimal(0)); } if(ventasGlobales.getPorcentajeGanancias() == null) {
			 * ventasGlobales.setPorcentajeGanancias(new BigDecimal(0)); }
			 */
			e.printStackTrace();
			log.info("Error ventas globales" + e.getMessage());

		}

	}

	public void VentaDia() {
		// System.out.println(mesActual);
		Date fechaActual = new  Date();
		DateUtil.setMes(fechaActual, DateUtil.getMes(mesActual));
		DateUtil.setAnio(fechaActual, DateUtil.getAnio(mesActual));
		mesActual = fechaActual;
		DateUtil.getLastDayOfMonth(mesActual);
		listaVentaDia = reportesVentasDAO.obtenerVentaDia(mesActual);// venta del dia

		if (!listaVentaDia.isEmpty()) {
			listaVentaDia.get(0).setPorcentaje(new BigDecimal(0));
		}
		
	
		PrimeFaces.current().ajax().update("form:dt-ventas");

	}

	public void facturacion() {
		// Date finMes;

		BigDecimal porcentaje = new BigDecimal(0).setScale(2);
		BigDecimal resta = new BigDecimal(0).setScale(2);
		BigDecimal acumulado = null;

		FacturacionGeneral factAct = new FacturacionGeneral();
		FacturacionGeneral factAnt = new FacturacionGeneral();

		Collections.sort(listaFacturacionGeneral);
		Collections.sort(listFacturacionMesAnt);

		for (FacturacionGeneral fg : listaFacturacionGeneral) { // 12 de marzo, 11 de marzo, 8, 9

			if (fg.getTotal_facturacion().compareTo(new BigDecimal(0)) == 0) {
				fg.setTotal_facturacion(factAct.getTotal_facturacion());
			}
			factAct = fg;
			if (factAct.getTotal_facturacion() == null) {
				factAct.setTotal_facturacion(new BigDecimal(0));
			}

			for (FacturacionGeneral f : listFacturacionMesAnt) { // 12 de febrero , 10, 8, 9

				if (f.getTotal_facturacion().compareTo(new BigDecimal(0)) == 0) {
					f.setTotal_facturacion(factAnt.getTotal_facturacion());
				}
				factAnt = f;
				if (factAnt.getTotal_facturacion() == null) {
					factAnt.setTotal_facturacion(new BigDecimal(0));
				}

				if (DateUtil.getDia(fg.getFecha()) == DateUtil.getDia(f.getFecha())) {

					resta = fg.getTotal_facturacion().subtract(f.getTotal_facturacion());
					System.out.println(resta);
					porcentaje = resta.divide(fg.getTotal_facturacion(), 2, RoundingMode.HALF_UP);

					fg.setPorcentaje(porcentaje);
					break;
				}
				porcentaje = new BigDecimal(0).setScale(2);
			}

			// acumulado = acumulado.add(fg.getTotal_facturacion());
			fg.setAcumulado(acumulado);
		}

		// listaFacturacionGeneral.add(facturacionSelected);
		// mesActual = new Date();
		PrimeFaces.current().ajax().update("dt-facturacion");
	}

	public void ventasPorMes() {
		Date iniMes = new Date();
		DateUtil.setDia(iniMes, 01);
		DateUtil.setMes(iniMes, 0);
		// System.out.println("Ventas realizadas por mes");
		listaImporteGlobal = reportesVentasDAO.ventasPorMesAnual(iniMes, fechaPrueba);
	}

	public void readerList() {

		FacesMessage msj = null;
		String mensaje = null;
		Severity severity = null;

		listaFacturacionGeneral = new ArrayList<>();
		listFacturacionMesAnt = new ArrayList<>();
		try {
			DateUtil.setMes(fechaPrueba, DateUtil.getMes(mesActual));
			DateUtil.setAnio(fechaPrueba, DateUtil.getAnio(mesActual));
			Date iniMes;
			int dia = DateUtil.getDia(fechaPrueba);
			// finMes = DateUtil.getLastDayOfMonth(mesActual);
			iniMes = DateUtil.getFirstDayOfMonth(fechaPrueba);

			listaFacturacionGeneral = reportesVentasDAO.desgloseFacturacion(iniMes, fechaPrueba);

			// modificar fechas
			Calendar calendario = Calendar.getInstance();
			calendario.setTime(fechaPrueba);
			calendario.add(Calendar.MONTH, -1);
			fechaPrueba = calendario.getTime();
			iniMes = DateUtil.getFirstDayOfMonth(fechaPrueba);

			listFacturacionMesAnt = reportesVentasDAO.desgloseFacturacion(iniMes, fechaPrueba);
			listFacturacionMesAnt = mesAnterior(listFacturacionMesAnt, dia); // llenamos lista de mes anterior con
																				// fechas no existentes
			listaFacturacionGeneral = mesActual(listaFacturacionGeneral, dia);// llenar lista de mes actual con fechas
																				// no existentes
			facturacion();

			System.out.println("Lista mes actual: " + listaFacturacionGeneral);
			System.out.println("Lista mes anterior: " + listFacturacionMesAnt);

		} catch (Exception e) {
			// e.printStackTrace();
			if (listFacturacionMesAnt == null) {
				listFacturacionMesAnt = new ArrayList<>();
			}

			if (listaFacturacionGeneral == null) {
				listaFacturacionGeneral = new ArrayList<>();
			}
			mensaje = e.getMessage();
			severity = FacesMessage.SEVERITY_ERROR;
			msj = new FacesMessage(severity, "Error Facturacion", mensaje);
			FacesContext.getCurrentInstance().addMessage(null, msj);

		} finally {
			PrimeFaces.current().ajax().update(":form:messages");
			Calendar calendario = Calendar.getInstance();
			calendario.setTime(fechaPrueba);
			calendario.add(Calendar.MONTH, 1);
			fechaPrueba = calendario.getTime();
		}

	}

	public List<FacturacionGeneral> mesAnterior(List<FacturacionGeneral> listMesAnt, int dia) {

		int count = 0;

		while (dia > 0) {
			for (FacturacionGeneral f : listMesAnt) { // 12 de febrero , 10, 8, 9

				if (dia == DateUtil.getDia(f.getFecha())) {
					count++;
					break;
				}

			}
			if (count == 0) {
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

	public List<FacturacionGeneral> mesActual(List<FacturacionGeneral> listMesAct, int dia) {

		int count = 0;

		while (dia > 0) {
			for (FacturacionGeneral ant : listMesAct) {// 11 de marzo, 9 de marz
				if (dia == DateUtil.getDia(ant.getFecha())) {
					count++;
					break;
				}
			}

			if (count == 0) {
				Date fech = new Date(); // problema tomo fecha actual en marzo y genera fechas de mes de marzo

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
		DateUtil.setAnio(fechaInicial, DateUtil.getAnio(fechaFinal));

		System.out.println(fechaFinal);
		System.out.println(fechaInicial);
		String nuevaFechaInicio = new SimpleDateFormat("yyyy-MM-dd").format(fechaInicial);
		String nuevaFechaFin = new SimpleDateFormat("yyyy-MM-dd").format(fechaFinal);
		listaImporteUtilidad = reportesVentasDAO.UtilidadPorMesAnual(nuevaFechaInicio, nuevaFechaFin);

		// listaImporteUtilidad =
		// importeEgresosDAO.obtenerUtilidadPorEmisor(null,fechaAnt);

		// Primer SET
		BarChartDataSet barDS = new BarChartDataSet();
		barDS.setLabel("Ingresos");
		barDS.setBackgroundColor("rgb( 252, 174, 174)");
		barDS.setStack("Stack 0");

		// Segundo SET
		BarChartDataSet barDS2 = new BarChartDataSet();
		barDS2.setLabel("Egresos");
		barDS2.setBackgroundColor("rgb( 174, 252, 244)");
		barDS2.setStack("Stack 1");

		// Tercer SET
		BarChartDataSet barDS3 = new BarChartDataSet();
		barDS3.setLabel("Utilidad/Perdida ");
		barDS3.setBackgroundColor("rgb(  233, 193, 250 )");
		barDS3.setStack("Stack 2");

		// Cuarto SET
		BarChartDataSet barDS4 = new BarChartDataSet();
		barDS4.setLabel("Efectivo ");
		barDS4.setBackgroundColor("rgb( 194, 193, 250 )");
		barDS4.setStack("Stack 3");

		List<String> listaFecha = new ArrayList<>();
		List<Number> listaPagos = new ArrayList<>();
		List<Number> listaEgresos = new ArrayList<>();
		List<Number> listaUtilidad = new ArrayList<>();
		List<Number> listaEfectivo = new ArrayList<>();

		for (ImporteUtilidad u : listaImporteUtilidad) {
			// u.setFecha(fechaInicial);

			listaPagos.add(u.getPagos());
			barDS.setData(listaPagos);

			listaEgresos.add(u.getEgresos());
			barDS2.setData(listaEgresos);

			listaUtilidad.add(u.getUtilidadPerdida());
			barDS3.setData(listaUtilidad);

			listaEfectivo.add(u.getIzq());
			barDS4.setData(listaEfectivo);

			String parse = new SimpleDateFormat("MMM").format(u.getFecha());
			listaFecha.add(parse);

		}
		data.addChartDataSet(barDS);
		data.addChartDataSet(barDS2);
		data.addChartDataSet(barDS3);
		data.addChartDataSet(barDS4);
		data.setLabels(listaFecha);
		stackedGroupBarModel.setData(data);

		// configuracion de labels

		// Options
		BarChartOptions options = new BarChartOptions();
		CartesianLinearAxes linearAxes = new CartesianLinearAxes();

		linearAxes.setStacked(false);
		linearAxes.setOffset(false);

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

		PrimeFaces.current().ajax().update("form:grafica");
	}

	public void graficaOcupacionCamaras() {

		Date fecha = new Date();

		modelCamara = new BarChartModel();
		ChartData dataSet = new ChartData();

		BarChartDataSet barDataSet = new BarChartDataSet();
		BarChartDataSet barDataSet2 = new BarChartDataSet();

		barDataSet.setLabel("Disponibles");
		barDataSet.setBackgroundColor("rgb( 252, 174, 174)");
		barDataSet.setStack("Stack 0");

		barDataSet2.setLabel("Ocupadas");
		barDataSet2.setBackgroundColor("rgb(  233, 193, 250 )");
		barDataSet2.setStack("Stack 1");

		List<Number> values = new ArrayList<>();
		List<Number> values2 = new ArrayList<>();
		List<String> labels = new ArrayList<>();

		listOcupacionCamara = ocupacionCamaraDAO.ocupacionCamara(fecha, null, null, null);

		listPlantas = plantaDAO.findall(false);

		for (OcupacionCamara oc : listOcupacionCamara) {
			for (Planta p : listPlantas) {

				if (oc.getPlanta_ds().equals(p.getPlantaDs())) {

					values.add(oc.getPosiciones_Disponibles());
					values2.add(oc.getTarima());
					labels.add("P" + oc.getPlanta_abrev() + ": " + oc.getCamara_abrev());
				}
			}

		}

		barDataSet.setData(values);
		barDataSet2.setData(values2);
		dataSet.setLabels(labels);
		dataSet.addChartDataSet(barDataSet);
		dataSet.addChartDataSet(barDataSet2);

		Title title = new Title();
		title.setDisplay(true);
		title.setText("Ocupación Cámaras");
		BarChartOptions options = new BarChartOptions();
		options.setTitle(title);

		modelCamara.setData(dataSet);
		modelCamara.setData(dataSet);
		modelCamara.setOptions(options);
		modelCamara.setExtender("charExtender");

	}

	public void createDonutModel() {
		donutModel = new DonutChartModel();
		ChartData data = new ChartData();
		DonutChartOptions options = new DonutChartOptions();
		donutModel.setOptions(options);

		DonutChartDataSet dataSet = new DonutChartDataSet();
		List<Number> listaventas = new ArrayList<>();
		for (FacturacionGeneral f : listaVentaRazonSocial) {
			listaventas.add(f.getTotal_facturacion());
			dataSet.setData(listaventas);
		}

		List<String> bgColors = new ArrayList<>();
		bgColors.add("rgb( 194, 193, 250 )");
		bgColors.add("rgb(  233, 193, 250 )");
		bgColors.add("rgb( 174, 252, 244)");
		bgColors.add("rgb(  165, 204, 249 )");
		bgColors.add("rgb( 250, 223, 193 )");
		
		dataSet.setBackgroundColor(bgColors);

		data.addChartDataSet(dataSet);
		List<String> listaEmisores = new ArrayList<>();
		for (FacturacionGeneral fc : listaVentaRazonSocial) {
			listaEmisores.add(fc.getRazonSocial());
			data.setLabels(listaEmisores);
		}

		Title title = new Title();
		title.setDisplay(true);
		title.setText("Ventas Por Razón Social");
		options.setTitle(title);

		donutModel.setData(data);
		donutModel.setExtender("charExtenderDonut");
	}

	public void createStackedBarModel() {
		smVentasFormaPago = new BarChartModel();
		ChartData data = new ChartData();

		BarChartDataSet barDataSet = new BarChartDataSet();
		barDataSet.setLabel("Efectivo");
		barDataSet.setBackgroundColor("rgb( 252, 174, 174)");
		List<Number> value = new ArrayList<>();
		List<String> labels = new ArrayList<>();

		BarChartDataSet barDataSet2 = new BarChartDataSet();
		barDataSet2.setLabel("Documento de transferencia");
		barDataSet2.setBackgroundColor("rgb( 174, 252, 244)");
		List<Number> value2 = new ArrayList<>();

		BarChartDataSet barDataSet3 = new BarChartDataSet();
		barDataSet3.setLabel("Nota de credito");
		barDataSet3.setBackgroundColor("rgb(  233, 193, 250 )");
		List<Number> value3 = new ArrayList<>();

		BarChartDataSet barDataSet4 = new BarChartDataSet();
		barDataSet4.setLabel("Cheque de caja");
		barDataSet4.setBackgroundColor("rgb( 194, 193, 250 )");
		List<Number> value4 = new ArrayList<>();

		List<FacturacionGeneral> listEnero = listaVentaPagos.stream().filter(e -> DateUtil.getMes(e.getFecha()) == 0)
				.collect(Collectors.toList());
		List<FacturacionGeneral> listFebrero = listaVentaPagos.stream().filter(e -> DateUtil.getMes(e.getFecha()) == 1)
				.collect(Collectors.toList());
		List<FacturacionGeneral> listMarzo = listaVentaPagos.stream().filter(e -> DateUtil.getMes(e.getFecha()) == 2)
				.collect(Collectors.toList());
		List<FacturacionGeneral> listAbril = listaVentaPagos.stream().filter(e -> DateUtil.getMes(e.getFecha()) == 3)
				.collect(Collectors.toList());
		List<FacturacionGeneral> listMayo = listaVentaPagos.stream().filter(e -> DateUtil.getMes(e.getFecha()) == 4)
				.collect(Collectors.toList());
		List<FacturacionGeneral> listJunio = listaVentaPagos.stream().filter(e -> DateUtil.getMes(e.getFecha()) == 5)
				.collect(Collectors.toList());
		List<FacturacionGeneral> listJulio = listaVentaPagos.stream().filter(e -> DateUtil.getMes(e.getFecha()) == 6)
				.collect(Collectors.toList());
		List<FacturacionGeneral> listAgosto = listaVentaPagos.stream().filter(e -> DateUtil.getMes(e.getFecha()) == 7)
				.collect(Collectors.toList());
		List<FacturacionGeneral> listSeptiembre = listaVentaPagos.stream()
				.filter(e -> DateUtil.getMes(e.getFecha()) == 8).collect(Collectors.toList());
		List<FacturacionGeneral> listOctubre = listaVentaPagos.stream().filter(e -> DateUtil.getMes(e.getFecha()) == 9)
				.collect(Collectors.toList());
		List<FacturacionGeneral> listNoviembre = listaVentaPagos.stream()
				.filter(e -> DateUtil.getMes(e.getFecha()) == 10).collect(Collectors.toList());
		List<FacturacionGeneral> listDiciembre = listaVentaPagos.stream()
				.filter(e -> DateUtil.getMes(e.getFecha()) == 11).collect(Collectors.toList());

		HashMap<String, FacturacionGeneral> Enero = new HashMap<String, FacturacionGeneral>();
		HashMap<String, FacturacionGeneral> Febrero = new HashMap<String, FacturacionGeneral>();
		HashMap<String, FacturacionGeneral> Marzo = new HashMap<String, FacturacionGeneral>();
		HashMap<String, FacturacionGeneral> Abril = new HashMap<String, FacturacionGeneral>();
		HashMap<String, FacturacionGeneral> Mayo = new HashMap<String, FacturacionGeneral>();
		HashMap<String, FacturacionGeneral> Junio = new HashMap<String, FacturacionGeneral>();
		HashMap<String, FacturacionGeneral> Julio = new HashMap<String, FacturacionGeneral>();
		HashMap<String, FacturacionGeneral> Agosto = new HashMap<String, FacturacionGeneral>();
		HashMap<String, FacturacionGeneral> Septiembre = new HashMap<String, FacturacionGeneral>();
		HashMap<String, FacturacionGeneral> Octubre = new HashMap<String, FacturacionGeneral>();
		HashMap<String, FacturacionGeneral> Noviembre = new HashMap<String, FacturacionGeneral>();
		HashMap<String, FacturacionGeneral> Diciembre = new HashMap<String, FacturacionGeneral>();

		Enero = verificarMes(listEnero);
		Febrero = verificarMes(listFebrero);
		Marzo = verificarMes(listMarzo);
		Abril = verificarMes(listAbril);
		Mayo = verificarMes(listMayo);
		Junio = verificarMes(listJunio);
		Julio = verificarMes(listJulio);
		Agosto = verificarMes(listAgosto);
		Septiembre = verificarMes(listSeptiembre);
		Octubre = verificarMes(listOctubre);
		Noviembre = verificarMes(listNoviembre);
		Diciembre = verificarMes(listDiciembre);

		ReporteVentaUtil reporteVentaUtil = new ReporteVentaUtil();

		value = reporteVentaUtil.ventaMesPago(Enero, "Efectivo");
		value2 = reporteVentaUtil.ventaMesPago(Enero, "Documento de transferencia");
		value3 = reporteVentaUtil.ventaMesPago(Enero, "Nota de credito");
		value4 = reporteVentaUtil.ventaMesPago(Enero, "Cheque de caja");

		value.addAll(reporteVentaUtil.ventaMesPago(Febrero, "Efectivo"));
		value2.addAll(reporteVentaUtil.ventaMesPago(Febrero, "Documento de transferencia"));
		value3.addAll(reporteVentaUtil.ventaMesPago(Febrero, "Nota de credito"));
		value4.addAll(reporteVentaUtil.ventaMesPago(Febrero, "Cheque de caja"));

		value.addAll(reporteVentaUtil.ventaMesPago(Marzo, "Efectivo"));
		value2.addAll(reporteVentaUtil.ventaMesPago(Marzo, "Documento de transferencia"));
		value3.addAll(reporteVentaUtil.ventaMesPago(Marzo, "Nota de credito"));
		value4.addAll(reporteVentaUtil.ventaMesPago(Marzo, "Cheque de caja"));

		value.addAll(reporteVentaUtil.ventaMesPago(Abril, "Efectivo"));
		value2.addAll(reporteVentaUtil.ventaMesPago(Abril, "Documento de transferencia"));
		value3.addAll(reporteVentaUtil.ventaMesPago(Abril, "Nota de credito"));
		value4.addAll(reporteVentaUtil.ventaMesPago(Abril, "Cheque de caja"));

		value.addAll(reporteVentaUtil.ventaMesPago(Mayo, "Efectivo"));
		value2.addAll(reporteVentaUtil.ventaMesPago(Mayo, "Documento de transferencia"));
		value3.addAll(reporteVentaUtil.ventaMesPago(Mayo, "Nota de credito"));
		value4.addAll(reporteVentaUtil.ventaMesPago(Mayo, "Cheque de caja"));

		value.addAll(reporteVentaUtil.ventaMesPago(Junio, "Efectivo"));
		value2.addAll(reporteVentaUtil.ventaMesPago(Junio, "Documento de transferencia"));
		value3.addAll(reporteVentaUtil.ventaMesPago(Junio, "Nota de credito"));
		value4.addAll(reporteVentaUtil.ventaMesPago(Junio, "Cheque de caja"));

		value.addAll(reporteVentaUtil.ventaMesPago(Julio, "Efectivo"));
		value2.addAll(reporteVentaUtil.ventaMesPago(Julio, "Documento de transferencia"));
		value3.addAll(reporteVentaUtil.ventaMesPago(Julio, "Nota de credito"));
		value4.addAll(reporteVentaUtil.ventaMesPago(Julio, "Cheque de caja"));

		value.addAll(reporteVentaUtil.ventaMesPago(Agosto, "Efectivo"));
		value2.addAll(reporteVentaUtil.ventaMesPago(Agosto, "Documento de transferencia"));
		value3.addAll(reporteVentaUtil.ventaMesPago(Agosto, "Nota de credito"));
		value4.addAll(reporteVentaUtil.ventaMesPago(Agosto, "Cheque de caja"));

		value.addAll(reporteVentaUtil.ventaMesPago(Septiembre, "Efectivo"));
		value2.addAll(reporteVentaUtil.ventaMesPago(Septiembre, "Documento de transferencia"));
		value3.addAll(reporteVentaUtil.ventaMesPago(Septiembre, "Nota de credito"));
		value4.addAll(reporteVentaUtil.ventaMesPago(Septiembre, "Cheque de caja"));

		value.addAll(reporteVentaUtil.ventaMesPago(Octubre, "Efectivo"));
		value2.addAll(reporteVentaUtil.ventaMesPago(Octubre, "Documento de transferencia"));
		value3.addAll(reporteVentaUtil.ventaMesPago(Octubre, "Nota de credito"));
		value4.addAll(reporteVentaUtil.ventaMesPago(Octubre, "Cheque de caja"));

		value.addAll(reporteVentaUtil.ventaMesPago(Noviembre, "Efectivo"));
		value2.addAll(reporteVentaUtil.ventaMesPago(Noviembre, "Documento de transferencia"));
		value3.addAll(reporteVentaUtil.ventaMesPago(Noviembre, "Nota de credito"));
		value4.addAll(reporteVentaUtil.ventaMesPago(Noviembre, "Cheque de caja"));

		value.addAll(reporteVentaUtil.ventaMesPago(Diciembre, "Efectivo"));
		value2.addAll(reporteVentaUtil.ventaMesPago(Diciembre, "Documento de transferencia"));
		value3.addAll(reporteVentaUtil.ventaMesPago(Diciembre, "Nota de credito"));
		value4.addAll(reporteVentaUtil.ventaMesPago(Diciembre, "Cheque de caja"));

		barDataSet.setData(value);
		barDataSet2.setData(value2);
		barDataSet3.setData(value3);
		barDataSet4.setData(value4);

		data.addChartDataSet(barDataSet);
		data.addChartDataSet(barDataSet2);
		data.addChartDataSet(barDataSet3);
		data.addChartDataSet(barDataSet4);

		labels.add("Enero");
		labels.add("Febrero");
		labels.add("Marzo");
		labels.add("Abril");
		labels.add("Mayo");
		labels.add("Junio");
		labels.add("Julio");
		labels.add("Agosto");
		labels.add("Septiembre");
		labels.add("Octubre");
		labels.add("Noviembre");
		labels.add("Diciembre");
		data.setLabels(labels);
		smVentasFormaPago.setData(data);

		// Options
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
		title.setText("Ventas por tipo de Pago");
		options.setTitle(title);

		Tooltip tooltip = new Tooltip();
		tooltip.setMode("index");
		tooltip.setIntersect(false);
		options.setTooltip(tooltip);

		smVentasFormaPago.setOptions(options);
		smVentasFormaPago.setExtender("charExtender");

	}

	public HashMap<String, FacturacionGeneral> verificarMes(List<FacturacionGeneral> list) {

		HashMap<String, FacturacionGeneral> map = new HashMap<String, FacturacionGeneral>();
		FacturacionGeneral fg = new FacturacionGeneral();
		boolean efectivo = false;
		boolean doc = false;
		boolean nota = false;
		boolean cheque = false;

		fg.setPagosPorMes(new BigDecimal(0));

		for (FacturacionGeneral f : list) {
			if (f.getTipoPago().equals("Efectivo")) {
				map.put("Efectivo", f);
				efectivo = true;
			} else {
				if (efectivo == false) {
					map.put("Efectivo", fg);
				}
			}

			if (f.getTipoPago().equals("Documento de transferencia")) {
				map.put("Documento de transferencia", f);
				doc = true;
			} else {
				if (doc == false) {
					map.put("Documento de transferencia", fg);
				}
			}

			if (f.getTipoPago().equals("Nota de credito")) {
				map.put("Nota de credito", f);
				nota = true;
			} else {
				if (nota == false) {
					map.put("Nota de credito", fg);
				}
			}

			if (f.getTipoPago().equals("Cheque de caja")) {
				map.put("Cheque de caja", f);
				cheque = true;
			} else {
				if (cheque == false) {
					map.put("Cheque de caja", fg);
				}
			}

		}

		return map;
	}

	public BarChartModel getStackedGroupBarModel() {
		return stackedGroupBarModel;
	}

	public void setStackedGroupBarModel(BarChartModel stackedGroupBarModel) {
		this.stackedGroupBarModel = stackedGroupBarModel;
	}

	public ImporteUtilidad getFechaUtilidad() {
		return fechaUtilidad;
	}

	public void setFechaUtilidad(ImporteUtilidad fechaUtilidad) {
		this.fechaUtilidad = fechaUtilidad;
	}

	public Date getMesActual() {
		return mesActual;
	}

	public void setMesActual(Date mesActual) {
		this.mesActual = mesActual;
	}

	public List<ImporteUtilidad> getListaImporteUtilidad() {
		return listaImporteUtilidad;
	}

	public void setListaImporteUtilidad(List<ImporteUtilidad> listaImporteUtilidad) {
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
		DashBoardBean.log = log;
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
	}

	public BarChartModel getModelCamara() {
		return modelCamara;
	}

	public void setModelCamara(BarChartModel modelCamara) {
		this.modelCamara = modelCamara;

	}

	public List<VentasGlobales> getListaVentasGlobales() {
		return listaVentasGlobales;
	}

	public void setListaVentasGlobales(List<VentasGlobales> listaVentasGlobales) {
		this.listaVentasGlobales = listaVentasGlobales;
	}

	public List<FacturacionGeneral> getListaImporteGlobal() {
		return listaImporteGlobal;
	}

	public void setListaImporteGlobal(List<FacturacionGeneral> listaImporteGlobal) {
		this.listaImporteGlobal = listaImporteGlobal;
	}

	public List<FacturacionGeneral> getListaVentaRazonSocial() {
		return listaVentaRazonSocial;
	}

	public void setListaVentaRazonSocial(List<FacturacionGeneral> listaVentaRazonSocial) {
		this.listaVentaRazonSocial = listaVentaRazonSocial;
	}

	public DonutChartModel getDonutModel() {
		return donutModel;
	}

	public void setDonutModel(DonutChartModel donutModel) {
		this.donutModel = donutModel;
	}

	public BarChartModel getSmVentasFormaPago() {
		return smVentasFormaPago;
	}

	public void setSmVentasFormaPago(BarChartModel smVentasFormaPago) {
		this.smVentasFormaPago = smVentasFormaPago;
	}

	public List<FacturacionGeneral> getListaVentaPagos() {
		return listaVentaPagos;
	}

	public void setListaVentaPagos(List<FacturacionGeneral> listaVentaPagos) {
		this.listaVentaPagos = listaVentaPagos;
	}

	public List<FacturacionGeneral> getListaGananciaRazonSocial() {
		return listaGananciaRazonSocial;
	}

	public void setListaGananciaRazonSocial(List<FacturacionGeneral> listaGananciaRazonSocial) {
		this.listaGananciaRazonSocial = listaGananciaRazonSocial;
	}

}
