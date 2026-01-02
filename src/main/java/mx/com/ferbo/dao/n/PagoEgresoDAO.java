package mx.com.ferbo.dao.n;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.PagoEgreso;

@Named
@ApplicationScoped
public class PagoEgresoDAO extends BaseDAO <PagoEgreso, Integer> {

    private static final Logger log  = LogManager.getLogger(PagoEgresoDAO.class);

    public PagoEgresoDAO(){
        super(PagoEgreso.class);
    }
}
