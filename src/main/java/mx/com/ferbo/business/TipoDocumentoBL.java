package mx.com.ferbo.business;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.dao.n.TipoDocumentoDAO;
import mx.com.ferbo.model.TipoDocumento;
import mx.com.ferbo.util.InventarioException;

@Named
@RequestScoped
public class TipoDocumentoBL {

    private static final Logger log = LogManager.getLogger();

    @Inject
    private TipoDocumentoDAO tipoDocumentoDAO;

    public TipoDocumento validarTipoDocumento(TipoDocumento tipoDocumento) throws InventarioException {
        if (tipoDocumento == null){
            throw new InventarioException("El tipo de docunento no puede ser vacío."); 
        }

        if ("".equalsIgnoreCase(tipoDocumento.getNombre())){}

        return tipoDocumento;
    }
}
