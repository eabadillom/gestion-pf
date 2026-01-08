package mx.com.ferbo.business.catalogos;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.dao.n.catalogos.TipoEgresoDAO;
import mx.com.ferbo.model.n.catalogos.TipoEgreso;
import mx.com.ferbo.util.InventarioException;

@Named
@RequestScoped
public class TipoEgresoBL extends BaseCatalogosBL<TipoEgreso> {

    private static final Logger log = LogManager.getLogger(TipoEgresoBL.class);

    @Inject
    public TipoEgresoBL(TipoEgresoDAO tipoEgresoDAO){
        super(tipoEgresoDAO);
    }

    @Override
    protected void validarEspecifico(TipoEgreso model) throws InventarioException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'validarEspecifico'");
    }
}
