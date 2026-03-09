package mx.com.ferbo.modulos.egresos.dao.egreso;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.modulos.egresos.dao.EgresoBaseDAO;
import mx.com.ferbo.modulos.egresos.model.catsecundarios.StatusActivoFijo;
import mx.com.ferbo.modulos.egresos.model.egreso.ActivoFijo;

@Named
@ApplicationScoped
public class ActivoFijoDAO extends EgresoBaseDAO <ActivoFijo, StatusActivoFijo>{

    private static final Logger log = LogManager.getLogger(ActivoFijoDAO.class);

    public ActivoFijoDAO(){
        super(ActivoFijo.class);
    }

    @Override
    protected Class<ActivoFijo> getEntityClass() {
        return ActivoFijo.class;
    }

}
