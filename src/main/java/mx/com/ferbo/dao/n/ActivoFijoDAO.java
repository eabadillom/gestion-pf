package mx.com.ferbo.dao.n;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.ActivoFijo;

@Named
@ApplicationScoped
public class ActivoFijoDAO extends BaseDAO <ActivoFijo, Integer> {

    private static final Logger log = LogManager.getLogger(ActivoFijoDAO.class);

    public ActivoFijoDAO(){
        super(ActivoFijo.class);
    }
}
