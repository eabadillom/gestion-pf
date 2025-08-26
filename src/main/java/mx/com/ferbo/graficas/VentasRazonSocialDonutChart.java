package mx.com.ferbo.graficas;

import org.primefaces.model.charts.donut.DonutChartModel;
import org.primefaces.model.charts.donut.DonutChartDataSet;
import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.optionconfig.title.Title;
import org.primefaces.model.charts.donut.DonutChartOptions;

import java.io.Serializable;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import mx.com.ferbo.dao.ReportesVentasDAO;
import mx.com.ferbo.model.FacturacionGeneral;
import mx.com.ferbo.util.DateUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author alberto
 */
public class VentasRazonSocialDonutChart implements Serializable
{
    private static final Logger log = LogManager.getLogger(UtilidadPorMesMensualStackedBar.class);
    private static final long serialVersionUID = 1L;
    
    private static final String TITULO_GRAFICA = "Ventas Por Razón Social";
    private static final String CHART_EXTENDER = "charExtenderDonut";
    
    public static DonutChartModel construirModeloVentasRazonSocial()
    {
        Date fechaFin = new Date();
        Date fechaInicio = DateUtil.getFirstDayOfMonth(fechaFin);
        ReportesVentasDAO reportesVentasDAO = new ReportesVentasDAO();
        List<FacturacionGeneral> listaVentaRazonSocial = reportesVentasDAO.ventaPorRazonSocial(fechaInicio, fechaFin);

        return construirGrafica(listaVentaRazonSocial);
    }

    public static DonutChartModel construirGrafica(List<FacturacionGeneral> listaVentaRazonSocial) 
    {
        DonutChartModel donutModel = new DonutChartModel();
        ChartData data = new ChartData();
        DonutChartOptions options = new DonutChartOptions();
        donutModel.setOptions(options);

        // Crear dataset
        DonutChartDataSet dataSet = new DonutChartDataSet();
        List<Number> listaVentas = new ArrayList<>();
        
        for (FacturacionGeneral f : listaVentaRazonSocial) {
            listaVentas.add(f.getTotal_facturacion());
        }
        dataSet.setData(listaVentas);

        // Colores de fondo
        dataSet.setBackgroundColor(getDefaultColors());
        data.addChartDataSet(dataSet);

        // Etiquetas
        List<String> listaEmisores = new ArrayList<>();
        for (FacturacionGeneral fc : listaVentaRazonSocial) {
            listaEmisores.add(fc.getRazonSocial());
        }
        data.setLabels(listaEmisores);

        // Título
        Title title = new Title();
        title.setDisplay(true);
        title.setText(TITULO_GRAFICA);
        options.setTitle(title);

        donutModel.setData(data);
        donutModel.setExtender(CHART_EXTENDER);

        return donutModel;
    }
    
    private static List<String> getDefaultColors() 
    {
        List<String> bgColors = new ArrayList<>();
        bgColors.add("rgb(194, 193, 250)");
        bgColors.add("rgb(233, 193, 250)");
        bgColors.add("rgb(174, 252, 244)");
        bgColors.add("rgb(165, 204, 249)");
        bgColors.add("rgb(250, 223, 193)");
        return bgColors;
    }
    
}
