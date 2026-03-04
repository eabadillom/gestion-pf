
package mx.com.ferbo.modulos.egresos.dao.catsecundarios;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.modulos.egresos.dao.CatEgresoBaseDAO;
import mx.com.ferbo.modulos.egresos.model.catsecundarios.TipoDevolucionEgreso;

@Named
@ApplicationScoped
public class TipoDevolucionEgresoDAO extends CatEgresoBaseDAO<TipoDevolucionEgreso>{
 
    private static final Logger log = LogManager.getLogger(TipoDevolucionEgresoDAO.class);
    
    public TipoDevolucionEgresoDAO(){
        super(TipoDevolucionEgreso.class);
    }

    @Override
    protected Class<TipoDevolucionEgreso> getEntityClass() {
        return TipoDevolucionEgreso.class;
    }
}
