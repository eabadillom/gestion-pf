
package mx.com.ferbo.modulos.egresos.business.categreso;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import mx.com.ferbo.modulos.egresos.business.CatEgresoBaseBL;
import mx.com.ferbo.modulos.egresos.dao.catsecundarios.StatusDevolucionEgresoDAO;
import mx.com.ferbo.modulos.egresos.model.catsecundarios.StatusDevolucionEgreso;
import mx.com.ferbo.util.InventarioException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Named
@RequestScoped
public class StatusDevolucionEgresoBL extends CatEgresoBaseBL<StatusDevolucionEgreso>{
    
    private static final Logger log = LogManager.getLogger(StatusDevolucionEgresoBL.class);
    
    @Inject
    private StatusDevolucionEgresoDAO dao;
    
    @PostConstruct
    public void init(){
        setDao(dao);
    }

    @Override
    protected void validarEspecifico(StatusDevolucionEgreso entity) throws InventarioException {
        // Método sin  aplicar porque no se necesita
    }
    
}
