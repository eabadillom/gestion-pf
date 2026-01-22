package mx.com.ferbo.graficas;

import java.io.Serializable;

import java.text.ParseException;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import java.util.function.Function;
import java.util.stream.Collectors;

import mx.com.ferbo.dao.ReportesVentasDAO;
import mx.com.ferbo.ui.ImporteUtilidad;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.axes.cartesian.CartesianScales;
import org.primefaces.model.charts.axes.cartesian.category.CartesianCategoryAxes;
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
public class UtilidadPorMesMensualStackedBar implements Serializable
{
    private static final Logger log = LogManager.getLogger(UtilidadPorMesMensualStackedBar.class);
    private static final long serialVersionUID = 1L;
    
    private static final String LBL_INGRESOS = "Ingresos";
    private static final String RGB_INGRESOS = "rgb(252, 174, 174)";
    
    private static final String LBL_EGRESOS = "Egresos";
    private static final String RGB_EGRESOS = "rgb(174, 252, 244)";
    
    private static final String LBL_UTILIDAD = "Utilidad/Perdida";
    private static final String RGB_UTILIDAD = "rgb(233, 193, 250)";
    
    private static final String LBL_EFECTIVO = "Efectivo";
    private static final String RGB_EFECTIVO = "rgb(194, 193, 250)";
    
    private static final String TITULO_GRAFICA = "Ingresos - Egresos";
    
    private static final String CHART_EXTENDER = "charExtender";

    private static List<ImporteUtilidad> obtenerUtilidadPorMesAnual() 
    {
        ReportesVentasDAO reportesVentasDAO = new ReportesVentasDAO();

        LocalDate fechaFinal = LocalDate.now();
        LocalDate fechaInicial = LocalDate.of(fechaFinal.getYear(), 1, 1);

        String nuevaFechaInicio = fechaInicial.toString();
        String nuevaFechaFin = fechaFinal.toString();

        return reportesVentasDAO.UtilidadPorMesAnual(nuevaFechaInicio, nuevaFechaFin);
    }

    public static BarChartModel construirGrafica() throws ParseException
    {
        BarChartModel model = new BarChartModel();
        ChartData data = new ChartData();
        List<ImporteUtilidad> utilidadMesAnual = obtenerUtilidadPorMesAnual();

        // Configuración dinámica de datasets
        List<DataSetConfig> configuraciones = new ArrayList<>();
        configuraciones.add(new DataSetConfig(LBL_INGRESOS, RGB_INGRESOS, "Stack 0", u -> u.getPagos()));
        configuraciones.add(new DataSetConfig(LBL_EGRESOS, RGB_EGRESOS, "Stack 1", u -> u.getEgresos()));
        configuraciones.add(new DataSetConfig(LBL_UTILIDAD, RGB_UTILIDAD, "Stack 2", u -> u.getUtilidadPerdida()));
        configuraciones.add(new DataSetConfig(LBL_EFECTIVO, RGB_EFECTIVO, "Stack 3", u -> u.getIzq()));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM", Locale.getDefault());
        List<String> etiquetas = new ArrayList<>();

        // Construir datasets en base a la config
        for(DataSetConfig config : configuraciones) 
        {
            BarChartDataSet ds = crearDataSet(config.label, config.color, config.stack);
            List<Number> valores = utilidadMesAnual.stream()
                    .map(config.mapper)
                    .collect(Collectors.toList());
            ds.setData(valores);
            data.addChartDataSet(ds);
        }

        // Etiquetas de meses
        for(ImporteUtilidad u : utilidadMesAnual) 
        {
            String mes = formatter.format(u.getFecha().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            etiquetas.add(mes);
        }

        data.setLabels(etiquetas);
        model.setData(data);
        model.setOptions(crearOpciones());
        model.setExtender(CHART_EXTENDER);

        return model;
    }

    private static BarChartDataSet crearDataSet(String label, String color, String stack) 
    {
        BarChartDataSet dataSet = new BarChartDataSet();
        dataSet.setLabel(label);
        dataSet.setBackgroundColor(color);
        dataSet.setStack(stack);
        return dataSet;
    }

    private static BarChartOptions crearOpciones() 
    {
        BarChartOptions options = new BarChartOptions();

        // === Eje Y ===
        CartesianLinearAxes yAxes = new CartesianLinearAxes();
        yAxes.setStacked(false);
        yAxes.setOffset(false);

        // === Eje X ===
        CartesianCategoryAxes xAxes = new CartesianCategoryAxes();
        xAxes.setStacked(false);
        xAxes.setOffset(true);

        // === Escalas ===
        CartesianScales scales = new CartesianScales();
        scales.addYAxesData(yAxes);
        scales.addXAxesData(xAxes);

        options.setScales(scales);

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

    /**
     * Clase interna para representar la configuración de cada dataset.
     */
    private static class DataSetConfig 
    {
        final String label;
        final String color;
        final String stack;
        final Function<ImporteUtilidad, Number> mapper;

        DataSetConfig(String label, String color, String stack, Function<ImporteUtilidad, Number> mapper) 
        {
            this.label = label;
            this.color = color;
            this.stack = stack;
            this.mapper = mapper;
        }
    }
}
