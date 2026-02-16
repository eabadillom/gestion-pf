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
import java.util.Random;

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
    private static final Logger log = LogManager.getLogger(VentasRazonSocialDonutChart.class);
    private static final long serialVersionUID = 1L;
    
    private static final String TITULO_GRAFICA = "Ventas Por Razón Social";
    private static final String CHART_EXTENDER = "charExtenderDonut";
    
    private static final Random RND = new Random();
    
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
        
        donutModel.setOptions(chartOptions());
        
        Integer totalVentas = listaVentaRazonSocial.size();
        
        data.addChartDataSet(crearDataSet(listaVentaRazonSocial, totalVentas));

        data.setLabels(labels(listaVentaRazonSocial));

        donutModel.setData(data);
        donutModel.setExtender(CHART_EXTENDER);

        return donutModel;
    }
    
    private static DonutChartDataSet crearDataSet(List<FacturacionGeneral> listaVentaRazonSocial, Integer totalVentas) // Crear dataset
    {
        DonutChartDataSet dataSet = new DonutChartDataSet();
        List<Number> listaVentas = new ArrayList<>();
        
        for (FacturacionGeneral f : listaVentaRazonSocial) {
            listaVentas.add(f.getTotal_facturacion());
        }
        dataSet.setData(listaVentas);
        
        // Colores de fondo
        List<String> colorsBackground = new ArrayList();
        
        for(int i=0; i < totalVentas; i++){
            colorsBackground.add(getDefaultRandomColor());
        }
        
        dataSet.setBackgroundColor(colorsBackground);
        
        return dataSet;
    }
    
    private static DonutChartOptions chartOptions() //
    {
        DonutChartOptions options = new DonutChartOptions();
        options.setTitle(titulo());
        
        return options;
    }
    
    private static List<String> labels(List<FacturacionGeneral> listaVentaRazonSocial) // Etiquetas
    {
        List<String> listaEmisores = new ArrayList<>();
        
        for (FacturacionGeneral fc : listaVentaRazonSocial) {
            listaEmisores.add(fc.getRazonSocial());
        }
        return listaEmisores;
    }
    
    private static Title titulo() // Título
    {
        Title title = new Title();
        title.setDisplay(true);
        title.setText(TITULO_GRAFICA);
        return title;
    }
    
    private static String getDefaultRandomColor() //Set Random Color
    {
        return String.format("rgb(%d,%d,%d)", RND.nextInt(256), RND.nextInt(256), RND.nextInt(256));
    }
    
}
