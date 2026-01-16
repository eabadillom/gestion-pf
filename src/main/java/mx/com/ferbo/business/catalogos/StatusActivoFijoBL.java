package mx.com.ferbo.business.catalogos;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.dao.n.catalogos.StatusActivoFijoDAO;
import mx.com.ferbo.model.n.catalogos.StatusActivoFijo;
import mx.com.ferbo.util.InventarioException;

@Named
@RequestScoped
public class StatusActivoFijoBL extends BaseCatalogosBL<StatusActivoFijo> {

    private static final Logger log = LogManager.getLogger(StatusActivoFijoBL.class );

    @Inject
    private StatusActivoFijoDAO statusActivoFijoDAO;

    @PostConstruct
    public void init(){
        setDao(statusActivoFijoDAO);
    }

    @Override
    protected void validarEspecifico(StatusActivoFijo model) throws InventarioException {
        // Metodo vacío porque no hay más validaciones
    }

    
}
