package mx.com.ferbo.dao.categresos;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.model.categresos.StatusEgreso;

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
