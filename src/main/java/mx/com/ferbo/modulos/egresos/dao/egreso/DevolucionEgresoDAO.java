
package mx.com.ferbo.modulos.egresos.dao.egreso;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.modulos.egresos.dao.EgresoBaseDAO;
import mx.com.ferbo.modulos.egresos.model.catsecundarios.StatusDevolucionEgreso;
import mx.com.ferbo.modulos.egresos.model.egreso.DevolucionEgreso;

@Named
@ApplicationScoped
public class DevolucionEgresoDAO extends EgresoBaseDAO<DevolucionEgreso, StatusDevolucionEgreso>{

    private static final Logger log = LogManager.getLogger(DevolucionEgresoDAO.class);
    
    public DevolucionEgresoDAO() {
        super(DevolucionEgreso.class);
    }
    
    @Override
    protected Class<DevolucionEgreso> getEntityClass() {
        return DevolucionEgreso.class;
    }
    
}
