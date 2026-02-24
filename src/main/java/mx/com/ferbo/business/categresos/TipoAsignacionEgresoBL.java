package mx.com.ferbo.business.categresos;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.dao.categresos.TipoAsignacionEgresoDAO;
import mx.com.ferbo.model.categresos.TipoAsignacionEgreso;
import mx.com.ferbo.util.InventarioException;

@Named
@RequestScoped
public class TipoAsignacionEgresoBL extends CatEgresoBaseBL<TipoAsignacionEgreso> {

    private static final Logger log = LogManager.getLogger(TipoAsignacionEgresoBL.class);

    @Inject
    private TipoAsignacionEgresoDAO dao;

    @PostConstruct
    public void init(){
        setDao(dao);
    }

    @Override
    protected void validarEspecifico(TipoAsignacionEgreso model) throws InventarioException {
        // Metodo vacío porque no hay más validaciones
    }
}
