package mx.com.ferbo.business.categresos;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.dao.categresos.TipoEgresoDAO;
import mx.com.ferbo.model.categresos.TipoEgreso;
import mx.com.ferbo.util.InventarioException;

@Named
@RequestScoped
public class TipoEgresoBL extends CatEgresoBaseBL<TipoEgreso> {

    private static final Logger log = LogManager.getLogger(TipoEgresoBL.class);

    @Inject
    private TipoEgresoDAO dao;

    @PostConstruct
    public void init(){
        setDao(dao);
    }

    @Override
    protected void validarEspecifico(TipoEgreso model) throws InventarioException {
        // Metodo vacío porque no hay más validaciones
    }
}
