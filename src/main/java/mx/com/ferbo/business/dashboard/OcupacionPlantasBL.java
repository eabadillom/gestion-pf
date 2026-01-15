package mx.com.ferbo.business.dashboard;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import mx.com.ferbo.dao.n.CamaraDAO;
import mx.com.ferbo.dao.n.OcupacionPlantaDAO;
import mx.com.ferbo.dao.n.PlantaDAO;
import mx.com.ferbo.model.Camara;
import mx.com.ferbo.ui.OcupacionPlanta;
import mx.com.ferbo.model.Planta;
import mx.com.ferbo.ui.PosicionesPlanta;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author alberto
 */
@Named
@RequestScoped
public class OcupacionPlantasBL 
{
    private static Logger log = LogManager.getLogger(OcupacionPlantasBL.class);
    
    @Inject
    private OcupacionPlantaDAO ocupacionPlantaDAO;
    
    @Inject
    private PlantaDAO plantaDAO;
    
    @Inject
    private CamaraDAO camaraDAO;       
    
    public List<OcupacionPlanta> tarimasPorPlanta(Date fecha, Integer idPlanta)
    {
        return ocupacionPlantaDAO.ocupacionPlantaCamara(fecha, null, idPlanta, null);
    }
    
    public List<PosicionesPlanta> posicionesPorPlanta(Date fecha, Integer idPlanta)
    {
        List<PosicionesPlanta> posiciones = new ArrayList();
        
        Planta planta = plantaDAO.buscarPorId(idPlanta).get();
        List<Camara> listCamaras = camaraDAO.findById(idPlanta);
        List<OcupacionPlanta> ocupacion = this.tarimasPorPlanta(fecha, idPlanta);
        
        for(OcupacionPlanta aux : ocupacion)
        {
            PosicionesPlanta ocupacionPlanta = new PosicionesPlanta();
            BigDecimal totalPosiciones = BigDecimal.ZERO;
            Integer nbTarimaCount = 0;
            
            ocupacionPlanta.setPlanta(planta.getPlantaAbrev());
            
            for(Camara auxCamara : listCamaras){
                if(auxCamara.getCamaraAbrev().equals(aux.getCamara())){
                    ocupacionPlanta.setCamara(aux.getCamara());
                }
            }
            
            if(aux.getNbTarima() != null) {
                nbTarimaCount++;
            }
            
            if(aux.getTarima() != null){
                BigDecimal cantidad = aux.getTarima().setScale(0, RoundingMode.CEILING);
                totalPosiciones = totalPosiciones.add(cantidad);
            }
            
            BigDecimal total = totalPosiciones.add(BigDecimal.valueOf(nbTarimaCount));
            ocupacionPlanta.setPosiciones(total);
            
            posiciones.add(ocupacionPlanta);
        }
        
        return posiciones;
    }
    
    public Map<String, Map<String, BigDecimal>> posiciones(List<PosicionesPlanta> listPosicionesPorPlanta){
        Map<String, Map<String, BigDecimal>> reporte = listPosicionesPorPlanta.stream()
            .collect(Collectors.groupingBy(
                PosicionesPlanta::getPlanta, // Primer nivel: Agrupar por Planta
                Collectors.toMap(
                    PosicionesPlanta::getCamara,   // Segundo nivel: La llave es la Cámara
                    PosicionesPlanta::getPosiciones, // El valor es la posición
                    BigDecimal::add                 // Si la cámara se repite, suma los valores
                )
            ));
        
        return reporte;
    }
    
    public Map<String, Map<String, BigDecimal>> reportePorPlanta(Planta planta, Date fechaHoy)
    {
        List<PosicionesPlanta> listPosicionesPorPlanta = this.posicionesPorPlanta(fechaHoy, planta.getPlantaCve());
        Map<String, Map<String, BigDecimal>> auxReporte = this.posiciones(listPosicionesPorPlanta);
        
        return auxReporte;
    }
    
    public Map<String, Map<String, BigDecimal>> reporteTodasPlantas(List<Planta> listPlanta, Date fechaHoy)
    {
        Map<String, Map<String, BigDecimal>> auxReporte = null;
        List<PosicionesPlanta> listPosicionesPorPlanta = new ArrayList();
        
        for(Planta aux : listPlanta)
        {
            List<PosicionesPlanta> posiciones = this.posicionesPorPlanta(fechaHoy, aux.getPlantaCve()); 
            listPosicionesPorPlanta.addAll(posiciones);
        }
        
        auxReporte = this.posiciones(listPosicionesPorPlanta);
        return auxReporte;
    }
    
}
