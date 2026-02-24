
package mx.com.ferbo.dao.categresos;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import mx.com.ferbo.model.categresos.TipoMovimientoEgreso;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Named
@ApplicationScoped
public class TipoMovimientoEgresoDAO extends CatEgresoBaseDAO<TipoMovimientoEgreso>{
    
    private static final Logger log = LogManager.getLogger(TipoMovimientoEgresoDAO.class);
    
    public TipoMovimientoEgresoDAO(){
        super(TipoMovimientoEgreso.class);
    }

    @Override
    protected Class<TipoMovimientoEgreso> getEntityClass() {
        return TipoMovimientoEgreso.class;
    }
}
