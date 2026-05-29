package mx.com.ferbo.egresos.dao;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.egresos.model.CancelaEgreso;

@Named
@ApplicationScoped
public class CancelaEgresoDAO extends BaseDAO<CancelaEgreso, Long>{

    private static final Logger log = LogManager.getLogger(CancelaEgresoDAO.class);

    public CancelaEgresoDAO() {
        super(CancelaEgreso.class);
    }

}
