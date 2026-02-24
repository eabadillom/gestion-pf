package mx.com.ferbo.dao.categresos;


import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.model.categresos.TipoDocumentoEgreso;

@Named
@ApplicationScoped
public class TipoDocumentoDAO extends CatEgresoBaseDAO <TipoDocumentoEgreso> {

    private static final Logger log = LogManager.getLogger(TipoDocumentoDAO.class);

    public TipoDocumentoDAO(){
        super(TipoDocumentoEgreso.class);
    }

    @Override
    protected Class<TipoDocumentoEgreso> getEntityClass() {
        return TipoDocumentoEgreso.class;
    }

}
