package mx.com.ferbo.graficas;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import mx.com.ferbo.ui.OcupacionCamara;
import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.optionconfig.legend.Legend;
import org.primefaces.model.charts.optionconfig.legend.LegendLabel;
import org.primefaces.model.charts.optionconfig.title.Title;
import org.primefaces.model.charts.pie.PieChartDataSet;
import org.primefaces.model.charts.pie.PieChartModel;
import org.primefaces.model.charts.pie.PieChartOptions;

/**
 *
 * @author alberto
 */
public class OcupacionChart 
{
    private static final Random RND = new Random();
    
    public static PieChartModel build(List<OcupacionCamara> lista, String titulo) 
    {

        PieChartModel pieModel = new PieChartModel();
        ChartData data = new ChartData();
        PieChartDataSet dataSet = new PieChartDataSet();

        List<Number> values = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        List<String> bgColors = new ArrayList<>();

        BigDecimal total = lista.stream()
            .map(OcupacionCamara::getTarima)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        for (OcupacionCamara oc : lista) 
        {
            values.add(oc.getTarima());

            BigDecimal porcentaje = oc.getTarima()
                .multiply(BigDecimal.valueOf(100))
                .divide(total, 1, RoundingMode.HALF_UP);

            labels.add(
                oc.getPlanta_ds() +
                String.format(" ( %s %% ) %s posiciones", porcentaje, oc.getTarima())
            );

            bgColors.add(colorRandom());
        }

        dataSet.setData(values);
        dataSet.setBackgroundColor(bgColors);
        dataSet.setHidden(false);

        data.addChartDataSet(dataSet);
        data.setLabels(labels);

        pieModel.setData(data);
        pieModel.setOptions(buildOptions(titulo));

        return pieModel;
    }
    
    /* OPCIONES */
    private static PieChartOptions buildOptions(String titulo) 
    {
        PieChartOptions options = new PieChartOptions();

        Legend legend = new Legend();
        legend.setDisplay(true);

        LegendLabel legendLabels = new LegendLabel();
        legendLabels.setFontStyle("bold");
        legendLabels.setFontColor("#2980B9");
        legendLabels.setFontSize(12);

        legend.setLabels(legendLabels);
        options.setLegend(legend);

        Title title = new Title();
        title.setDisplay(true);
        title.setText(titulo);
        title.setFontSize(16);
        title.setFontStyle("bold");
        title.setFontColor("#2c3e50");

        options.setTitle(title);

        return options;
    }
    
    /* COLORES */
    private static String colorRandom() 
    {
        return String.format("rgb(%d,%d,%d)", RND.nextInt(256), RND.nextInt(256), RND.nextInt(256));
    }
    
}
