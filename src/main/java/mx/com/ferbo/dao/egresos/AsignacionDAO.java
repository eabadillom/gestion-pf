
package mx.com.ferbo.dao.egresos;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.egresos.AsignacionEgreso;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Named
@ApplicationScoped
public class AsignacionDAO extends BaseDAO <AsignacionEgreso, Integer> {
    
    private static final Logger log = LogManager.getLogger(AsignacionDAO.class);
    
    public AsignacionDAO(){
        super(AsignacionEgreso.class);
    }
}
