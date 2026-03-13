package mx.com.ferbo.business.n;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import mx.com.ferbo.dao.n.CamaraDAO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.dao.n.PlantaDAO;
import mx.com.ferbo.model.Camara;
import mx.com.ferbo.model.Planta;
import mx.com.ferbo.util.InventarioException;

@Named
@RequestScoped
public class PlantaBL 
{
    private static final Logger log = LogManager.getLogger(PlantaBL.class);

    @Inject
    private PlantaDAO plantaDAO;
    
    @Inject
    private CamaraDAO camaraDAO;
    
    public List<Planta> obtenerPlantas(Boolean isFullInfo) {
        
        String tipo = (isFullInfo) ? "completa": "incompleta";
        
        log.info("Inicia proceso para obtener todas las plantas con información {}", tipo);
        List<Planta> list = plantaDAO.buscarTodos(isFullInfo);

        if (list == null) {
            return new ArrayList<>();
        }

        return list;
    }
    
    public Planta buscarPlanta(Integer idPlanta) throws InventarioException {
        if(idPlanta == null)
            throw new InventarioException("La planta no puede ser vacía");
        
        Optional<Planta> planta = plantaDAO.buscarPorId(idPlanta);
        Planta aux = null;
        
        if(planta.isPresent())
            aux = planta.get();
        else
            throw new InventarioException("No se encontro registro con ese identificador");
        
        return aux;
    }
    
    public Camara buscarCamaraPorId(Integer idCamara) throws InventarioException
    {
        if(idCamara == null)
            throw new InventarioException("La camara no puede ser vacía");
        
        Optional<Camara> camara = camaraDAO.buscarPorId(idCamara);
        Camara auxCamara = null;
        
        if(camara.isPresent())
            auxCamara = camara.get();
        else
            throw new InventarioException("No se encontro registro con ese identificador");
        
        return auxCamara;
    }

}
