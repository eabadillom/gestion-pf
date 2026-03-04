package mx.com.ferbo.modulos.egresos.dao.catsecundarios;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.modulos.egresos.dao.CatEgresoBaseDAO;
import mx.com.ferbo.modulos.egresos.model.catsecundarios.StatusActivoFijo;

@Named
@ApplicationScoped
public class StatusActivoFijoDAO extends CatEgresoBaseDAO <StatusActivoFijo> {

    private static final Logger log = LogManager.getLogger(StatusActivoFijoDAO.class);

    public StatusActivoFijoDAO(){
        super(StatusActivoFijo.class);
    }

    @Override
    protected Class<StatusActivoFijo> getEntityClass() {
        return StatusActivoFijo.class;
    }

}
