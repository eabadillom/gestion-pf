package mx.com.ferbo.dao.n;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.StatusActivoFijo;

@Named
@ApplicationScoped
public class StatusActivoFijoDAO extends BaseDAO <StatusActivoFijo, Integer> {

    private static final Logger log = LogManager.getLogger(StatusActivoFijoDAO.class);

    public StatusActivoFijoDAO(){
        super(StatusActivoFijo.class);
    }
}
