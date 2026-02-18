package mx.com.ferbo.dao.egresos;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.model.egresos.ActivoFijo;

@Named
@ApplicationScoped
public class ActivoFijoDAO extends EgresoBaseDAO <ActivoFijo>{

    private static final Logger log = LogManager.getLogger(ActivoFijoDAO.class);

    public ActivoFijoDAO(){
        super(ActivoFijo.class);
    }

    @Override
    protected Class<ActivoFijo> getEntityClass() {
        return ActivoFijo.class;
    }

}
