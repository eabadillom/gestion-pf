package mx.com.ferbo.business.almacen;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.dao.n.CamaraDAO;
import mx.com.ferbo.dao.n.PlantaDAO;
import mx.com.ferbo.model.Camara;
import mx.com.ferbo.model.Planta;
import mx.com.ferbo.util.InventarioException;

@Named
@RequestScoped
public class PlantaBL 
{
    private static final Logger log = LogManager.getLogger(PlantaBL.class);

    @Inject private static PlantaDAO plantaDAO;
    @Inject private static CamaraDAO camaraDAO;
    
    public PlantaBL() {
    }
    
    public List<Planta> buscarTodos(Boolean isFullInfo) {
        String tipo = (isFullInfo) ? "completa": "incompleta";
        
        log.info("Inicia proceso para obtener todas las plantas con información {}", tipo);
        List<Planta> list = plantaDAO.buscarTodos(isFullInfo);

        if (list == null) {
            return new ArrayList<>();
        }

        return list;
    }
    
    public Planta buscar(Integer idPlanta) throws InventarioException {
        if(idPlanta == null)
            throw new InventarioException("Debe indicar una planta.");
        
        return plantaDAO.buscarPorId(idPlanta)
        		.orElseThrow(() -> new InventarioException("No se encontro registro con ese identificador"));
    }
    
    //TODO Debería moverse a una clase CamaraBL
    public Camara buscarCamaraPorId(Integer idCamara) throws InventarioException
    {
        if(idCamara == null)
            throw new InventarioException("Debe indicar una cámara.");
        
        return camaraDAO.buscarPorId(idCamara)
        		.orElseThrow(() -> new InventarioException("No se encontro registro con ese identificador"));
    }

}
