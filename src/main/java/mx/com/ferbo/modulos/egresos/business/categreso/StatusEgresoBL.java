
package mx.com.ferbo.modulos.egresos.business.categreso;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import mx.com.ferbo.modulos.egresos.business.CatEgresoBaseBL;
import mx.com.ferbo.modulos.egresos.dao.catsecundarios.StatusEgresoDAO;
import mx.com.ferbo.modulos.egresos.model.catsecundarios.StatusEgreso;
import mx.com.ferbo.util.InventarioException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Named
@RequestScoped
public class StatusEgresoBL extends CatEgresoBaseBL<StatusEgreso> {
    
    private static final Logger log = LogManager.getLogger(StatusEgresoBL.class);
    
    @Inject
    private StatusEgresoDAO dao;
    
    @PostConstruct
    public void init(){
        setDao(dao);
    }

    @Override
    protected void validarEspecifico(StatusEgreso model) throws InventarioException {
        // Metodo vacío
    }
}
