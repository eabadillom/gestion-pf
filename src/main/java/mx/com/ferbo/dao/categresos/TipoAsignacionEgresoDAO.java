package mx.com.ferbo.dao.categresos;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.model.categresos.TipoAsignacionEgreso;

@Named
@ApplicationScoped
public class TipoAsignacionEgresoDAO extends CatEgresoBaseDAO<TipoAsignacionEgreso> {

    private static final Logger log = LogManager.getLogger(TipoAsignacionEgresoDAO.class);

    public TipoAsignacionEgresoDAO(){
        super(TipoAsignacionEgreso.class);
    }

    @Override
    protected Class<TipoAsignacionEgreso> getEntityClass() {
        return TipoAsignacionEgreso.class;
    }
}
