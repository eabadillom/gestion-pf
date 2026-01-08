package mx.com.ferbo.dao.n.catalogos;


import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.n.catalogos.TipoDocumento;

@Named
@ApplicationScoped
public class TipoDocumentoDAO extends BaseDAO <TipoDocumento, Integer> {

    private static final Logger log = LogManager.getLogger(TipoDocumentoDAO.class);

    public TipoDocumentoDAO(){
        super(TipoDocumento.class);
    }

}
