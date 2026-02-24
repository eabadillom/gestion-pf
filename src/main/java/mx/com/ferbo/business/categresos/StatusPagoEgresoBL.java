package mx.com.ferbo.business.categresos;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.dao.categresos.StatusPagoEgresoDAO;
import mx.com.ferbo.model.categresos.StatusPagoEgreso;
import mx.com.ferbo.util.InventarioException;

@Named
@RequestScoped
public class StatusPagoEgresoBL extends CatEgresoBaseBL<StatusPagoEgreso>{

    private static final Logger log = LogManager.getLogger(StatusPagoEgresoBL.class);

    @Inject
    private StatusPagoEgresoDAO dao;

    @PostConstruct
    public void init(){
        setDao(dao);
    }

    @Override
    protected void validarEspecifico(StatusPagoEgreso model) throws InventarioException {
        // Metodo vacío porque no hay más validaciones
    }
    
}
