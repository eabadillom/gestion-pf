package mx.com.ferbo.dao.categresos;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.categresos.StatusPago;

@Named
@ApplicationScoped
public class StatusPagoDAO extends BaseDAO<StatusPago, Integer> {

    private static final Logger log = LogManager.getLogger(StatusPagoDAO.class);

    public StatusPagoDAO() {
        super(StatusPago.class);
    }

}
