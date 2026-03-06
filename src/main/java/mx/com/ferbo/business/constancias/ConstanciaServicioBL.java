package mx.com.ferbo.business.constancias;

import javax.inject.Inject;
import mx.com.ferbo.dao.n.ConstanciaServicioDAO;
import mx.com.ferbo.model.ConstanciaDeServicio;
import mx.com.ferbo.util.InventarioException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author alberto
 */
public class ConstanciaServicioBL 
{
    private static final Logger log = LogManager.getLogger(ConstanciaServicioBL.class);
    
    @Inject
    private ConstanciaServicioDAO constanciaServicioDAO;
    
    public void guardarConstancia(ConstanciaDeServicio constanciaDeServicio) throws InventarioException {
        if(constanciaDeServicio == null)
            throw new InventarioException("La constancia de servicio no puede ser vacía");
        
        constanciaServicioDAO.guardar(constanciaDeServicio);
    }
    
}
