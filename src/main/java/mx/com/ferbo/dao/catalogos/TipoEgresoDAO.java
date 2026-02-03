package mx.com.ferbo.dao.catalogos;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.catalogos.TipoEgreso;

@Named
@ApplicationScoped
public class TipoEgresoDAO extends BaseDAO <TipoEgreso, Integer> {

    private static final Logger log = LogManager.getLogger(TipoEgresoDAO.class);

    public TipoEgresoDAO(){
        super(TipoEgreso.class);
    }

}
