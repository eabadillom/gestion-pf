
package mx.com.ferbo.dao.catalogos;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.catalogos.StatusCargoEgreso;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Named
@ApplicationScoped
public class StatusCargoEgresoDAO extends BaseDAO<StatusCargoEgreso, Integer>{
    
    private static final Logger log = LogManager.getLogger(StatusCargoEgresoDAO.class);
    
    public StatusCargoEgresoDAO() {
        super(StatusCargoEgreso.class);
    }

}
