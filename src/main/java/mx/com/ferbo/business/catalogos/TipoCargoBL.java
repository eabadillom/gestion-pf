package mx.com.ferbo.business.catalogos;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.dao.catalogos.TipoCargoDAO;
import mx.com.ferbo.model.catalogos.TipoCargo;
import mx.com.ferbo.util.InventarioException;

@Named
@RequestScoped
public class TipoCargoBL extends BaseCatalogosBL<TipoCargo> {

    private static final Logger log = LogManager.getLogger(TipoCargoBL.class);

    @Inject
    private TipoCargoDAO dao;

    @PostConstruct
    public void init(){
        setDao(dao);
    }

    @Override
    protected void validarEspecifico(TipoCargo model) throws InventarioException {
        if (model.getTieneIVA() == null) {
            throw new InventarioException("No se sabe si el tipo de cargo tiene o no IVA");
        }

        if (model.getTieneIEPS() == null) {
            throw new InventarioException("No se sabe si el tipo de cargo tiene o no IEPS");
        }

    }
}
