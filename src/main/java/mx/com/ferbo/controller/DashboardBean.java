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
import org.primefaces.model.charts.bar.BarChartModel;
import org.primefaces.model.charts.donut.DonutChartDataSet;
import org.primefaces.model.charts.donut.DonutChartModel;
import org.primefaces.model.charts.donut.DonutChartOptions;
import org.primefaces.model.charts.optionconfig.title.Title;

import mx.com.ferbo.dao.ReportesVentasDAO;
import mx.com.ferbo.graficas.OcupacionCamaraChart;
import mx.com.ferbo.graficas.UtilidadPorMesMensualStackedBar;
import mx.com.ferbo.graficas.VentasFormaPagoStackedBar;
import mx.com.ferbo.graficas.VentasRazonSocialDonutChart;
import mx.com.ferbo.model.FacturacionGeneral;
import mx.com.ferbo.model.VentasGlobales;
import mx.com.ferbo.ui.ImporteUtilidad;
import mx.com.ferbo.util.DateUtil;

@Named
@ViewScoped
public class DashboardBean implements Serializable {

    private static final long serialVersionUID = -6551673633472266325L;
    private static Logger log = LogManager.getLogger(DashboardBean.class);

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

    public DashboardBean() 
    {
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
    }

    @PostConstruct
    public void init() 
    {
        try {
            fechaPrueba = new Date();
            mesActual = new Date();

            stackedGroupBarModel = UtilidadPorMesMensualStackedBar.construirGrafica();
            ventaGlobales();
            ventasPorMes();
            ventaRazonSocial();
            gananciaPorRazonSocial();
            donutModel = VentasRazonSocialDonutChart.construirModeloVentasRazonSocial();
            modelCamara = OcupacionCamaraChart.construirModeloOcupacionCamara();
            VentaDia();
            smVentasFormaPago = VentasFormaPagoStackedBar.build();

            Date today = new Date();
            maxDate = new Date(today.getTime());
        } catch (ParseException e) {

            e.printStackTrace();
        }
    }

    public void ventaRazonSocial() 
    {
        Date fechaFin = new Date();
        Date fechaInicio = DateUtil.getFirstDayOfMonth(fechaFin);

        listaVentaRazonSocial = reportesVentasDAO.ventaPorRazonSocial(fechaInicio, fechaFin);

        PrimeFaces.current().ajax().update("form:dt-ventaRS");
    }

    public void gananciaPorRazonSocial() 
    {
        Date fechaFin = new Date();
        Date fechainicio = DateUtil.getFirstDayOfMonth(fechaFin);

        listaGananciaRazonSocial = reportesVentasDAO.gananciaPorRazonSocial(fechainicio, fechaFin);

        PrimeFaces.current().ajax().update("form:dt-gananciaRS");
    }

    // @SuppressWarnings("static-access")
    public void ventaGlobales() 
    {
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

    public void VentaDia() 
    {
        // System.out.println(mesActual);
        Date fechaActual = new Date();
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

    public void facturacion() 
    {
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

    public void readerList() 
    {
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

            log.info("Lista mes actual: {}", listaFacturacionGeneral);
            log.info("Lista mes anterior: {}", listFacturacionMesAnt);
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

    public List<FacturacionGeneral> mesAnterior(List<FacturacionGeneral> listMesAnt, int dia) 
    {
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

    public List<FacturacionGeneral> mesActual(List<FacturacionGeneral> listMesAct, int dia) 
    {
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
        DashboardBean.log = log;
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
