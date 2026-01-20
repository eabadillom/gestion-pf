package mx.com.ferbo.dao.n.catalogos;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.n.catalogos.StatusEgreso;

@Named
@ApplicationScoped
public class StatusEgresoDAO extends BaseDAO <StatusEgreso, Integer> {

    private static final Logger log = LogManager.getLogger(StatusEgresoDAO.class);

    public StatusEgresoDAO(){
        super(StatusEgreso.class);
    }
}
