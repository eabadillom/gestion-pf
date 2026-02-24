package mx.com.ferbo.dao.categresos;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.model.categresos.TipoCargoEgreso;

@Named
@ApplicationScoped
public class TipoCargoEgresoDAO extends CatEgresoBaseDAO<TipoCargoEgreso>{

    private static final Logger log = LogManager.getLogger(TipoCargoEgresoDAO.class);

    public TipoCargoEgresoDAO(){
        super(TipoCargoEgreso.class);
    }

    @Override
    protected Class<TipoCargoEgreso> getEntityClass() {
        return TipoCargoEgreso.class;
    }
}
