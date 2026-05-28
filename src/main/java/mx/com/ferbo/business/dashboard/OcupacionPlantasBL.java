package mx.com.ferbo.business.dashboard;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.dao.n.OcupacionPlantaDAO;
import mx.com.ferbo.model.Planta;
import mx.com.ferbo.ui.OcupacionPlanta;

/**
 *
 * @author alberto
 */
@Named
@RequestScoped
public class OcupacionPlantasBL
{
    private static Logger log = LogManager.getLogger(OcupacionPlantasBL.class);
    
    private OcupacionPlantaDAO ocupacionPlantaDAO;
    
    public OcupacionPlantasBL() {
    	this.ocupacionPlantaDAO = new OcupacionPlantaDAO();
	}
    
    public List<OcupacionPlanta> posicionesPorPlanta(Date fecha, Integer idPlanta)
    {
        return ocupacionPlantaDAO.ocupacionPlantaCamara(fecha, null, idPlanta, null);
    }
    
    public Map<String, Map<String, BigDecimal>> posiciones(List<OcupacionPlanta> listPosicionesPorPlanta) {
        Map<String, Map<String, BigDecimal>> reporte = listPosicionesPorPlanta.stream()
            .collect(Collectors.groupingBy(
                OcupacionPlanta::getPlanta, // Primer nivel: Agrupar por Planta
                TreeMap::new,
                Collectors.toMap(
                    OcupacionPlanta::getCamara,   // Segundo nivel: La llave es la Cámara
                    OcupacionPlanta::getTarima, // El valor es la posición
                    BigDecimal::add,                 // Si la cámara se repite, suma los valores
                    TreeMap::new
                )
            ));
        
        return reporte;
    }
    
    public Map<String, Map<String, BigDecimal>> reportePorPlanta(Planta planta, Date fechaHoy)
    {
    	log.info("Iniciando reporte de ocupación de camara por planta...");
        List<OcupacionPlanta> listPosicionesPorPlanta = this.posicionesPorPlanta(fechaHoy, planta.getPlantaCve());
        Map<String, Map<String, BigDecimal>> auxReporte = this.posiciones(listPosicionesPorPlanta);
        log.info("Terminado reporte de ocupación de camara por planta.");
        return auxReporte;
    }
    
    public Map<String, Map<String, BigDecimal>> reporteTodasPlantas(List<Planta> listPlanta, Date fechaHoy)
    {
        Map<String, Map<String, BigDecimal>> auxReporte = null;
        List<OcupacionPlanta> listPosicionesPorPlanta = new ArrayList<OcupacionPlanta>();
        log.info("Iniciando reporte de posiciones por camara...");
        
        listPosicionesPorPlanta = this.posicionesPorPlanta(fechaHoy, null);
        
        auxReporte = this.posiciones(listPosicionesPorPlanta);
        log.info("Terminado reporte de posiciones por camara.");
        return auxReporte;
    }
}
