
package mx.com.ferbo.business.categresos;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import mx.com.ferbo.dao.categresos.StatusCargoEgresoDAO;
import mx.com.ferbo.util.InventarioException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import mx.com.ferbo.model.categresos.CatEgreso;

@Named
@RequestScoped
public class StatusCargoEgresoBL extends CatEgresoBaseBL {
    
    private static final Logger log = LogManager.getLogger(StatusCargoEgresoBL.class);
    
    @Inject
    private StatusCargoEgresoDAO dao;
    
    @PostConstruct
    public void init(){
        setDao(dao);
    }

    @Override
    protected void validarEspecifico(CatEgreso model) throws InventarioException {
        // Metodo vacio
    }
    
}
