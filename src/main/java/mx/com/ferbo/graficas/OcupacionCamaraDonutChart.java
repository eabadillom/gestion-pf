package mx.com.ferbo.graficas;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.donut.DonutChartDataSet;
import org.primefaces.model.charts.donut.DonutChartModel;
import org.primefaces.model.charts.donut.DonutChartOptions;
import org.primefaces.model.charts.optionconfig.title.Title;

/**
 *
 * @author alberto
 */
public class OcupacionCamaraDonutChart implements Serializable
{
    private static final Logger log = LogManager.getLogger(OcupacionCamaraDonutChart.class);
    private static final long serialVersionUID = 1L;

    private static final String TITULO_GRAFICA = "Ocupación Cámaras";
    private static final String CHART_EXTENDER = "charExtenderPosiciones";
    
    private static final Random RND = new Random();
    
    public static DonutChartModel construirModeloPosicionesPlanta(Set<Map.Entry<String, BigDecimal>> camarasPlanta){
        return construirGrafica(camarasPlanta);
    }

    private static DonutChartModel construirGrafica(Set<Map.Entry<String, BigDecimal>> camarasPlanta) 
    {
        DonutChartModel donutModel = new DonutChartModel();
        donutModel.setOptions(chartOptions());
        
        ChartData data = new ChartData();
        
        DonutChartDataSet dataSet = new DonutChartDataSet();

        List<Number> values = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        List<String> bgColors = new ArrayList<>();

        for (Map.Entry<String, BigDecimal> entry : camarasPlanta) {
            labels.add(entry.getKey());
            values.add(entry.getValue());
            bgColors.add(getDefaultRandomColor());
        }

        dataSet.setData(values);
        dataSet.setBackgroundColor(bgColors);

        data.addChartDataSet(dataSet);
        data.setLabels(labels);

        donutModel.setData(data);
        donutModel.setExtender(CHART_EXTENDER);
        
        return donutModel;
    }
    
    private static DonutChartOptions chartOptions() 
    {
        DonutChartOptions options = new DonutChartOptions();
        options.setTitle(titulo());
        
        return options;
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
        return String.format("rgb(%d,%d,%d,0.7)", RND.nextInt(256), RND.nextInt(256), RND.nextInt(256));
    }
    
}
