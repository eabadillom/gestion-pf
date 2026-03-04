
package mx.com.ferbo.modulos.egresos.dao.catsecundarios;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.modulos.egresos.dao.CatEgresoBaseDAO;
import mx.com.ferbo.modulos.egresos.model.catsecundarios.StatusCargoEgreso;

@Named
@ApplicationScoped
public class StatusCargoEgresoDAO extends CatEgresoBaseDAO<StatusCargoEgreso>{
    
    private static final Logger log = LogManager.getLogger(StatusCargoEgresoDAO.class);
    
    public StatusCargoEgresoDAO() {
        super(StatusCargoEgreso.class);
    }

    @Override
    protected Class<StatusCargoEgreso> getEntityClass() {
        return StatusCargoEgreso.class;
    }

}
