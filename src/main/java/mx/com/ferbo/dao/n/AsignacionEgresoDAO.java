package mx.com.ferbo.dao.n;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.AsignacionEgreso;

@Named
@ApplicationScoped
public class AsignacionEgresoDAO extends BaseDAO <AsignacionEgreso, Integer> {

    private static final Logger log = LogManager.getLogger(AsignacionEgresoDAO.class);

    public AsignacionEgresoDAO(){
        super(AsignacionEgreso.class);
    }
}
