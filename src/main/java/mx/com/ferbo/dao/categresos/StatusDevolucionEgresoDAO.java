
package mx.com.ferbo.dao.categresos;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import mx.com.ferbo.model.categresos.StatusDevolucionEgreso;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Named
@ApplicationScoped
public class StatusDevolucionEgresoDAO extends CatEgresoBaseDAO<StatusDevolucionEgreso> {
    
    private static final Logger log = LogManager.getLogger(StatusDevolucionEgresoDAO.class);
    
    public StatusDevolucionEgresoDAO(){
        super(StatusDevolucionEgreso.class);
    }

    @Override
    protected Class<StatusDevolucionEgreso> getEntityClass() {
        return StatusDevolucionEgreso.class;
    }
}
