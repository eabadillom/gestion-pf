
package mx.com.ferbo.dao.egresos;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import mx.com.ferbo.model.egresos.DevolucionEgreso;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Named
@ApplicationScoped
public class DevolucionEgresoDAO extends EgresoBaseDAO<DevolucionEgreso>{

    private static final Logger log = LogManager.getLogger(DevolucionEgresoDAO.class);
    
    public DevolucionEgresoDAO() {
        super(DevolucionEgreso.class);
    }
    
    @Override
    protected Class<DevolucionEgreso> getEntityClass() {
        return DevolucionEgreso.class;
    }
    
}
