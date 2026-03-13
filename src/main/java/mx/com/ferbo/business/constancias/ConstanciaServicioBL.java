package mx.com.ferbo.business.constancias;

import java.util.Optional;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import mx.com.ferbo.dao.n.EstadoConstanciaDAO;
import mx.com.ferbo.dao.n.ConstanciaServicioDAO;
import mx.com.ferbo.model.ConstanciaDeServicio;
import mx.com.ferbo.model.EstadoConstancia;
import mx.com.ferbo.util.InventarioException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author alberto
 */
@Named
@RequestScoped
public class ConstanciaServicioBL 
{
    private static final Logger log = LogManager.getLogger(ConstanciaServicioBL.class);
    
    @Inject
    private ConstanciaServicioDAO constanciaServicioDAO;
    
    @Inject
    private EstadoConstanciaDAO estadoConstanciaDAO;
    
    public void guardarConstancia(ConstanciaDeServicio constanciaDeServicio) throws InventarioException {
        if(constanciaDeServicio == null)
            throw new InventarioException("La constancia de servicio no puede ser vacía");
        
        constanciaServicioDAO.guardar(constanciaDeServicio);
    }
    
    public EstadoConstancia buscarEstadoConstancia(Integer idEstadoConstancia) throws InventarioException {
        if(idEstadoConstancia == null)
            throw new InventarioException("El estado constancia no puede ser vacía");
        
        Optional<EstadoConstancia> auxEstadoConstancia = estadoConstanciaDAO.buscarPorId(idEstadoConstancia);
        EstadoConstancia estadoConstancia = null;
        
        if(auxEstadoConstancia.isPresent())
            estadoConstancia = auxEstadoConstancia.get();
        else
            throw new InventarioException("No se encontro registro con ese identificador");
        
        return estadoConstancia;
    }
    
}
