
package mx.com.ferbo.business.categresos;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import mx.com.ferbo.dao.categresos.TipoDevolucionEgresoDAO;
import mx.com.ferbo.model.categresos.TipoDevolucionEgreso;
import mx.com.ferbo.util.InventarioException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Named
@RequestScoped
public class TipoDevolucionEgresoBL extends CatEgresoBaseBL <TipoDevolucionEgreso>{
    
    private static final Logger log = LogManager.getLogger(TipoDevolucionEgresoBL.class);

    @Inject
    private TipoDevolucionEgresoDAO dao;
    
    @PostConstruct
    public void init(){
        setDao(dao);
    }
    
    @Override
    protected void validarEspecifico(TipoDevolucionEgreso entity) throws InventarioException {
        // Método sin aplicar porque no es necesario
    }
    
}
