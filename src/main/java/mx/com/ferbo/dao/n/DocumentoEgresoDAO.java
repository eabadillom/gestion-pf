package mx.com.ferbo.dao.n;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.DocumentoEgreso;

@Named
@ApplicationScoped
public class DocumentoEgresoDAO extends BaseDAO <DocumentoEgreso, Integer> {

    private static final Logger log = LogManager.getLogger(DocumentoEgresoDAO.class);

    public DocumentoEgresoDAO(){
        super(DocumentoEgreso.class);
    }
}
