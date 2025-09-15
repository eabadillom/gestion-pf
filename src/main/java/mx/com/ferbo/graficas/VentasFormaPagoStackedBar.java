package mx.com.ferbo.graficas;

import java.io.Serializable;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import mx.com.ferbo.dao.ReportesVentasDAO;
import mx.com.ferbo.model.FacturacionGeneral;
import mx.com.ferbo.util.DateUtil;
import mx.com.ferbo.util.ReporteVentaUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.axes.cartesian.CartesianScales;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearAxes;
import org.primefaces.model.charts.bar.BarChartDataSet;
import org.primefaces.model.charts.bar.BarChartModel;
import org.primefaces.model.charts.bar.BarChartOptions;
import org.primefaces.model.charts.optionconfig.title.Title;
import org.primefaces.model.charts.optionconfig.tooltip.Tooltip;

/**
 *
 * @author alberto
 */
public class VentasFormaPagoStackedBar implements Serializable
{
    private static final Logger log = LogManager.getLogger(VentasFormaPagoStackedBar.class);
    private static final long serialVersionUID = 1L;
    
    private static final String EFECTIVO = "Efectivo";
    private static final String RGB_EFECTIVO = "rgb(252, 174, 174)";
    
    private static final String DOC_TRANSFERENCIA = "Documento de transferencia";
    private static final String RGB_DOC_TRANSFERENCIA = "rgb(174, 252, 244)";
    
    private static final String NOTA_CREDITO = "Nota de credito";
    private static final String RGB_NOTA_CREDITO = "rgb(233, 193, 250)";
    
    private static final String CHEQUE_CAJA = "Cheque de caja";
    private static final String RGB_CHEQUE_CAJA = "rgb(194, 193, 250)";
    
    private static final String TITULO_GRAFICA = "Ventas Por Tipo de Pago";
    
    private static final String CHART_EXTENDER = "charExtender";
    
    private static List<FacturacionGeneral> ventaPorPago() 
    {
        ReportesVentasDAO reportesVentasDAO = new ReportesVentasDAO();
        Date fechaInicio = new Date();
        Date fechaFin = new Date();
        int inicioAnio = DateUtil.getAnio(fechaFin);

        DateUtil.setDia(fechaInicio, 01);
        DateUtil.setMes(fechaInicio, 01);
        DateUtil.setAnio(fechaInicio, inicioAnio);

        return reportesVentasDAO.ventaPorFormaPago(fechaInicio, fechaFin);
    }

    public static BarChartModel build() 
    {
        ReporteVentaUtil reporteVentaUtil = new ReporteVentaUtil();
        BarChartModel chartModel = new BarChartModel();
        ChartData data = new ChartData();

        Map<String, BarChartDataSet> dataSets = crearDataSets();

        Map<Integer, List<FacturacionGeneral>> datosPorMes = agruparPorMes();
        for (int mes = 0; mes < 12; mes++) 
        {
            Map<String, FacturacionGeneral> pagosMes = verificarMes(datosPorMes.getOrDefault(mes, Collections.emptyList()));

            for (Map.Entry<String, BarChartDataSet> entry : dataSets.entrySet()) 
            {
                List<Number> valores = entry.getValue().getData() != null
                        ? entry.getValue().getData()
                        : new ArrayList<>();
                valores.addAll(reporteVentaUtil.ventaMesPago((HashMap<String, FacturacionGeneral>) pagosMes, entry.getKey()));
                entry.getValue().setData(valores);
            }
        }

        dataSets.values().forEach(data::addChartDataSet);
        data.setLabels(obtenerMeses());
        chartModel.setData(data);
        chartModel.setOptions(generarOpciones());
        chartModel.setExtender(CHART_EXTENDER);

        return chartModel;
    }

    private static Map<String, BarChartDataSet> crearDataSets() 
    {
        Map<String, BarChartDataSet> map = new LinkedHashMap<>();

        map.put(EFECTIVO, crearEtiquetas(EFECTIVO, RGB_EFECTIVO));
        map.put(DOC_TRANSFERENCIA, crearEtiquetas(DOC_TRANSFERENCIA, RGB_DOC_TRANSFERENCIA));
        map.put(NOTA_CREDITO, crearEtiquetas(NOTA_CREDITO, RGB_NOTA_CREDITO));
        map.put(CHEQUE_CAJA, crearEtiquetas(CHEQUE_CAJA, RGB_CHEQUE_CAJA));

        return map;
    }

    private static BarChartDataSet crearEtiquetas(String label, String color) 
    {
        BarChartDataSet dataSet = new BarChartDataSet();
        dataSet.setLabel(label);
        dataSet.setBackgroundColor(color);
        return dataSet;
    }

    private static Map<Integer, List<FacturacionGeneral>> agruparPorMes() 
    {
        List<FacturacionGeneral> listaVentaPagos = ventaPorPago();
        return listaVentaPagos.stream().collect(Collectors.groupingBy(f -> DateUtil.getMes(f.getFecha())));
    }

    private static List<String> obtenerMeses() 
    {
        return Arrays.asList("Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre");
    }

    private static BarChartOptions generarOpciones() 
    {
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
        title.setText(TITULO_GRAFICA);
        options.setTitle(title);

        Tooltip tooltip = new Tooltip();
        tooltip.setMode("index");
        tooltip.setIntersect(false);
        options.setTooltip(tooltip);

        return options;
    }
    
    private static HashMap<String, FacturacionGeneral> verificarMes(List<FacturacionGeneral> list) 
    {
        HashMap<String, FacturacionGeneral> map = new HashMap<String, FacturacionGeneral>();
        FacturacionGeneral fg = new FacturacionGeneral();
        boolean efectivo = false;
        boolean doc = false;
        boolean nota = false;
        boolean cheque = false;

        fg.setPagosPorMes(new BigDecimal(0));

        for (FacturacionGeneral f : list) 
        {
            if (f.getTipoPago().equals(EFECTIVO)) {
                map.put(EFECTIVO, f);
                efectivo = true;
            } else {
                if (efectivo == false) {
                        map.put(EFECTIVO, fg);
                }
            }

            if (f.getTipoPago().equals(DOC_TRANSFERENCIA)) {
                map.put(DOC_TRANSFERENCIA, f);
                doc = true;
            } else {
                if (doc == false) {
                    map.put(DOC_TRANSFERENCIA, fg);
                }
            }

            if (f.getTipoPago().equals(NOTA_CREDITO)) {
                map.put(NOTA_CREDITO, f);
                nota = true;
            } else {
                if (nota == false) {
                    map.put(NOTA_CREDITO, fg);
                }
            }

            if (f.getTipoPago().equals(CHEQUE_CAJA)) {
                map.put(CHEQUE_CAJA, f);
                cheque = true;
            } else {
                if (cheque == false) {
                    map.put(CHEQUE_CAJA, fg);
                }
            }

        }

        return map;
    }
    
}
