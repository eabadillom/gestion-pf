
package mx.com.ferbo.business.catalogos;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import mx.com.ferbo.dao.n.catalogos.StatusEgresoDAO;
import mx.com.ferbo.model.n.catalogos.StatusEgreso;
import mx.com.ferbo.util.InventarioException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Named
@RequestScoped
public class StatusEgresoBL extends BaseCatalogosBL<StatusEgreso> {
    
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
