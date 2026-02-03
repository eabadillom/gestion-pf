package mx.com.ferbo.business.catalogos;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.dao.catalogos.TipoDocumentoDAO;
import mx.com.ferbo.model.catalogos.TipoDocumento;
import mx.com.ferbo.util.InventarioException;

@Named
@RequestScoped
public class TipoDocumentoBL extends BaseCatalogosBL<TipoDocumento> {

    private static final Logger log = LogManager.getLogger();

    @Inject
    private TipoDocumentoDAO dao;

    @PostConstruct
    public void init(){
        setDao(dao);
    }

    @Override
    protected void validarEspecifico(TipoDocumento model) throws InventarioException {
        // Metodo vacío porque no hay más validaciones
    }
}
