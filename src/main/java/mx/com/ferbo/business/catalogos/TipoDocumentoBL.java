package mx.com.ferbo.business.catalogos;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.dao.n.catalogos.TipoDocumentoDAO;
import mx.com.ferbo.model.n.catalogos.TipoDocumento;
import mx.com.ferbo.util.InventarioException;

@Named
@RequestScoped
public class TipoDocumentoBL extends BaseCatalogosBL<TipoDocumento> {

    private static final Logger log = LogManager.getLogger();

    @Inject
    public TipoDocumentoBL(TipoDocumentoDAO tipoDocumentoDAO){
        super(tipoDocumentoDAO);
    }

    @Override
    protected void validarEspecifico(TipoDocumento model) throws InventarioException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'validarEspecifico'");
    }
}
