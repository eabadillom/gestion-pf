
package mx.com.ferbo.modulos.egresos.dao.catsecundarios;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.modulos.egresos.dao.CatEgresoBaseDAO;
import mx.com.ferbo.modulos.egresos.model.catsecundarios.TipoMovimientoEgreso;

@Named
@ApplicationScoped
public class TipoMovimientoEgresoDAO extends CatEgresoBaseDAO<TipoMovimientoEgreso>{
    
    private static final Logger log = LogManager.getLogger(TipoMovimientoEgresoDAO.class);
    
    public TipoMovimientoEgresoDAO(){
        super(TipoMovimientoEgreso.class);
    }

    @Override
    protected Class<TipoMovimientoEgreso> getEntityClass() {
        return TipoMovimientoEgreso.class;
    }
}
