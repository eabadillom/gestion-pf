package mx.com.ferbo.graficas;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.List;
import java.util.function.Function;
import mx.com.ferbo.dao.ImporteEgresosDAO;

import mx.com.ferbo.ui.ImporteUtilidad;

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
public class ImporteUtilidadStackedBar implements Serializable
{
    private static final Logger log = LogManager.getLogger(UtilidadPorMesMensualStackedBar.class);
    private static final long serialVersionUID = 1L;
    
    private static final String INGRESOS = "Ingresos";
    private static final String RGB_INGRESOS = "rgb(255, 99, 132)";
    private static final String STACK_INGRESOS = "Stack 0";
    
    private static final String EGRESOS = "Egresos";
    private static final String RGB_EGRESOS = "rgb(54, 162, 235)";
    private static final String STACK_EGRESOS = "Stack 1";
    
    private static final String UTILIDAD_PERDIDA = "Utilidad/Perdida";
    private static final String RGB_UTILIDAD_PERDIDA = "rgb(75, 192, 192)";
    private static final String STACK_UTILIDAD_PERDIDA = "Stack 2";
    
    private static final String TITULO_GRAFICA = "Balance General";
    private static final String CHART_EXTENDER = "charExtender";
    
    public static BarChartModel construirModeloImporteUtilidad(Date periodo) throws ParseException 
    {
        //Calendar calendario = Calendar.getInstance();
        //calendario.add(Calendar.MONTH, -1);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String nuevaFecha = sdf.format(periodo);
        Date fechaAnt = sdf.parse(nuevaFecha);
        
        ImporteEgresosDAO importeEgresosDAO = new ImporteEgresosDAO();
        List<ImporteUtilidad> listaImporteUtilidad = importeEgresosDAO.obtenerUtilidadPorEmisor(null, fechaAnt);
        
        return construirGrafica(listaImporteUtilidad);
    }
    
    private static BarChartModel construirGrafica(List<ImporteUtilidad> listaImporteUtilidad) throws ParseException
    {
        BarChartModel stackedGroupBarModel = new BarChartModel();
        ChartData data = new ChartData();
        
        // Primer dataSet
        data.addChartDataSet(crearDataSet(INGRESOS, RGB_INGRESOS, STACK_INGRESOS, listaImporteUtilidad, ImporteUtilidad::getPagos));
        
        // Segundo DataSet
        data.addChartDataSet(crearDataSet(EGRESOS, RGB_EGRESOS, STACK_EGRESOS, listaImporteUtilidad, ImporteUtilidad::getEgresos));

        // Terccer dataset
        data.addChartDataSet(crearDataSet(UTILIDAD_PERDIDA, RGB_UTILIDAD_PERDIDA, STACK_UTILIDAD_PERDIDA, listaImporteUtilidad, ImporteUtilidad::getUtilidadPerdida));

        // Labels (emisores)
        List<String> listaEmisores = listaImporteUtilidad.stream()
                .map(ImporteUtilidad::getEmiNombre)
                .toList();

        data.setLabels(listaEmisores);
        stackedGroupBarModel.setData(data);

        stackedGroupBarModel.setOptions(generarOpciones());
        stackedGroupBarModel.setExtender(CHART_EXTENDER);
        
        return stackedGroupBarModel;
    }
    
    private static BarChartDataSet crearDataSet(String label, String color, String stack, List<ImporteUtilidad> lista, Function<ImporteUtilidad, Number> getter) 
    {
        BarChartDataSet barDataSet = new BarChartDataSet();
        barDataSet.setLabel(label);
        barDataSet.setBackgroundColor(color);
        barDataSet.setStack(stack);
        barDataSet.setData(lista.stream().map(getter).toList());
        
        return barDataSet;
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
    
}
