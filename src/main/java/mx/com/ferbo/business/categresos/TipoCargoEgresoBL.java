package mx.com.ferbo.business.categresos;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.dao.categresos.TipoCargoEgresoDAO;
import mx.com.ferbo.model.categresos.TipoCargoEgreso;
import mx.com.ferbo.util.InventarioException;

@Named
@RequestScoped
public class TipoCargoEgresoBL extends CatEgresoBaseBL<TipoCargoEgreso> {

    private static final Logger log = LogManager.getLogger(TipoCargoEgresoBL.class);

    @Inject
    private TipoCargoEgresoDAO dao;

    @PostConstruct
    public void init(){
        setDao(dao);
    }

    @Override
    protected void validarEspecifico(TipoCargoEgreso model) throws InventarioException {
        if (model.getTieneIVA() == null) {
            throw new InventarioException("No se sabe si el tipo de cargo tiene o no IVA");
        }

        if (model.getTieneIEPS() == null) {
            throw new InventarioException("No se sabe si el tipo de cargo tiene o no IEPS");
        }

    }
}
