package mx.com.ferbo.modulos.egresos.dao.catsecundarios;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.modulos.egresos.dao.CatEgresoBaseDAO;
import mx.com.ferbo.modulos.egresos.model.catsecundarios.TipoCargoEgreso;

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
