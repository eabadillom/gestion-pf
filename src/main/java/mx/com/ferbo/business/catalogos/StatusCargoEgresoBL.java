
package mx.com.ferbo.business.catalogos;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import mx.com.ferbo.dao.catalogos.StatusCargoEgresoDAO;
import mx.com.ferbo.model.catalogos.Catalogo;
import mx.com.ferbo.util.InventarioException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Named
@RequestScoped
public class StatusCargoEgresoBL extends BaseCatalogosBL {
    
    private static final Logger log = LogManager.getLogger(StatusCargoEgresoBL.class);
    
    @Inject
    private StatusCargoEgresoDAO dao;
    
    @PostConstruct
    public void init(){
        setDao(dao);
    }

    @Override
    protected void validarEspecifico(Catalogo model) throws InventarioException {
        // Metodo vacio
    }
    
}
