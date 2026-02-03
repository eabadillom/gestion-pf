package mx.com.ferbo.business.catalogos;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.dao.catalogos.TipoEgresoDAO;
import mx.com.ferbo.model.catalogos.TipoEgreso;
import mx.com.ferbo.util.InventarioException;

@Named
@RequestScoped
public class TipoEgresoBL extends BaseCatalogosBL<TipoEgreso> {

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
