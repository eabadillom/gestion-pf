
package mx.com.ferbo.dao.categresos;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import mx.com.ferbo.model.categresos.TipoDevolucionEgreso;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Named
@ApplicationScoped
public class TipoDevolucionEgresoDAO extends CatEgresoBaseDAO<TipoDevolucionEgreso>{
 
    private static final Logger log = LogManager.getLogger(TipoDevolucionEgresoDAO.class);
    
    public TipoDevolucionEgresoDAO(){
        super(TipoDevolucionEgreso.class);
    }

    @Override
    protected Class<TipoDevolucionEgreso> getEntityClass() {
        return TipoDevolucionEgreso.class;
    }
}
