package mx.com.ferbo.dao;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.TipoAsignacion;

@Named
@ApplicationScoped
public class TipoAsignacionDAO extends BaseDAO<TipoAsignacion, Integer> {

    private static final Logger log = LogManager.getLogger(TipoAsignacionDAO.class);

    public TipoAsignacionDAO(){
        super(TipoAsignacion.class);
    }
}
