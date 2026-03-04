package mx.com.ferbo.modulos.egresos.dao.catsecundarios;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.modulos.egresos.dao.CatEgresoBaseDAO;
import mx.com.ferbo.modulos.egresos.model.catsecundarios.StatusEgreso;

@Named
@ApplicationScoped
public class StatusEgresoDAO extends CatEgresoBaseDAO <StatusEgreso> {

    private static final Logger log = LogManager.getLogger(StatusEgresoDAO.class);

    public StatusEgresoDAO(){
        super(StatusEgreso.class);
    }

    @Override
    protected Class<StatusEgreso> getEntityClass() {
        return StatusEgreso.class;
    }
}
