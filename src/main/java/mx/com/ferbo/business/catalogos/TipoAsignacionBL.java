package mx.com.ferbo.business.catalogos;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.dao.TipoAsignacionDAO;
import mx.com.ferbo.model.n.catalogos.TipoAsignacion;
import mx.com.ferbo.util.InventarioException;

@Named
@RequestScoped
public class TipoAsignacionBL extends BaseCatalogosBL<TipoAsignacion> {

    private static final Logger log = LogManager.getLogger(TipoAsignacionBL.class);

    @Inject
    public TipoAsignacionBL(TipoAsignacionDAO tipoAsignacionDAO){
        super(tipoAsignacionDAO);
    }

    @Override
    protected void validarEspecifico(TipoAsignacion model) throws InventarioException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'validarEspecifico'");
    }
}
