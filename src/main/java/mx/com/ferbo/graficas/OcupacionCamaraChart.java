package mx.com.ferbo.graficas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mx.com.ferbo.dao.PlantaDAO;
import mx.com.ferbo.dao.RepOcupacionCamaraDAO;
import mx.com.ferbo.model.Planta;
import mx.com.ferbo.ui.OcupacionCamara;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.bar.BarChartDataSet;
import org.primefaces.model.charts.bar.BarChartModel;
import org.primefaces.model.charts.bar.BarChartOptions;
import org.primefaces.model.charts.optionconfig.title.Title;

/**
 *
 * @author alberto
 */
public class OcupacionCamaraChart implements Serializable
{
    private static final Logger log = LogManager.getLogger(OcupacionCamaraChart.class);
    private static final long serialVersionUID = 1L;
    
    private static final String LABEL_DISPONIBLES = "Disponibles";
    private static final String LABEL_OCUPADAS = "Ocupadas";

    private static final String COLOR_DISPONIBLES = "rgb(75, 192, 75)";
    private static final String COLOR_OCUPADAS = "rgb(255, 99, 132)";

    private static final String STACK_DISPONIBLES = "Stack 1";
    private static final String STACK_OCUPADAS = "Stack 1";

    private static final String TITULO_GRAFICA = "Ocupación Cámaras";

    private static final String CHART_EXTENDER = "charOcupacionCamaraExtender";
    
    public static BarChartModel construirModeloOcupacionCamara() 
    {
        Date fecha = new Date();
        RepOcupacionCamaraDAO ocupacionCamaraDAO = new RepOcupacionCamaraDAO();
        PlantaDAO plantaDAO = new PlantaDAO();
        List<OcupacionCamara> listOcupacionCamara = ocupacionCamaraDAO.ocupacionCamara(fecha, null, null, null);
        List<Planta> listPlantas = plantaDAO.findall(false);

        List<Number> disponibles = new ArrayList<>();
        List<Number> ocupadas = new ArrayList<>();
        List<String> etiquetas = new ArrayList<>();

        for (OcupacionCamara oc : listOcupacionCamara) 
        {
            if (plantaExiste(listPlantas, oc.getPlanta_ds())) 
            {
                disponibles.add(oc.getPosiciones_Disponibles());
                ocupadas.add(oc.getTarima());
                etiquetas.add(generarEtiqueta(oc));
            }
        }

        return construirGrafica(disponibles, ocupadas, etiquetas);
    }
    
    private static boolean plantaExiste(List<Planta> plantas, String plantaDs) 
    {
        return plantas.stream().anyMatch(p -> p.getPlantaDs().equals(plantaDs));
    }

    private static String generarEtiqueta(OcupacionCamara oc) 
    {
        return "P" + oc.getPlanta_abrev() + ": " + oc.getCamara_abrev();
    }

    private static BarChartModel construirGrafica(List<Number> disponibles, List<Number> ocupadas, List<String> etiquetas) 
    {
        BarChartDataSet disponiblesDataSet = new BarChartDataSet();
        disponiblesDataSet.setLabel(LABEL_DISPONIBLES);
        disponiblesDataSet.setBackgroundColor(COLOR_DISPONIBLES);
        disponiblesDataSet.setStack(STACK_DISPONIBLES);
        disponiblesDataSet.setData(disponibles);

        BarChartDataSet ocupadasDataSet = new BarChartDataSet();
        ocupadasDataSet.setLabel(LABEL_OCUPADAS);
        ocupadasDataSet.setBackgroundColor(COLOR_OCUPADAS);
        ocupadasDataSet.setStack(STACK_OCUPADAS);
        ocupadasDataSet.setData(ocupadas);

        ChartData data = new ChartData();
        data.setLabels(etiquetas);
        data.addChartDataSet(disponiblesDataSet);
        data.addChartDataSet(ocupadasDataSet);

        Title title = new Title();
        title.setDisplay(true);
        title.setText(TITULO_GRAFICA);

        BarChartOptions options = new BarChartOptions();
        options.setTitle(title);

        BarChartModel model = new BarChartModel();
        model.setData(data);
        model.setOptions(options);
        model.setExtender(CHART_EXTENDER);

        return model;
    }
    
}
