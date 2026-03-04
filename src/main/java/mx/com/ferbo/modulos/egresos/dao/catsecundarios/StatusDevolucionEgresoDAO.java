
package mx.com.ferbo.modulos.egresos.dao.catsecundarios;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.modulos.egresos.dao.CatEgresoBaseDAO;
import mx.com.ferbo.modulos.egresos.model.catsecundarios.StatusDevolucionEgreso;

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
