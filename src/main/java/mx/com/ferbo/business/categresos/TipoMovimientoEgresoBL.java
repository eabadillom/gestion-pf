
package mx.com.ferbo.business.categresos;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import mx.com.ferbo.dao.categresos.TipoMovimientoEgresoDAO;
import mx.com.ferbo.model.categresos.TipoMovimientoEgreso;
import mx.com.ferbo.util.InventarioException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Named
@RequestScoped
public class TipoMovimientoEgresoBL extends CatEgresoBaseBL <TipoMovimientoEgreso>{
    
    private static final Logger log = LogManager.getLogger(TipoMovimientoEgresoBL.class);
    
    @Inject
    private TipoMovimientoEgresoDAO dao;
    
    @PostConstruct
    public void init(){
        setDao(dao);
    }

    @Override
    protected void validarEspecifico(TipoMovimientoEgreso entity) throws InventarioException {
        // Método sin implementar porque no es necesario
    }
}
