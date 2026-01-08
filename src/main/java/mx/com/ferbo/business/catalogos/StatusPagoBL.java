package mx.com.ferbo.business.catalogos;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.dao.n.catalogos.StatusPagoDAO;
import mx.com.ferbo.model.n.catalogos.StatusPago;
import mx.com.ferbo.util.InventarioException;

@Named
@RequestScoped
public class StatusPagoBL extends BaseCatalogosBL<StatusPago>{

    private static final Logger log = LogManager.getLogger(StatusPagoBL.class);

    @Inject
    public StatusPagoBL(StatusPagoDAO statusPagoDAO){
        super(statusPagoDAO);
    }

    @Override
    protected void validarEspecifico(StatusPago model) throws InventarioException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'validarEspecifico'");
    }
    
}
