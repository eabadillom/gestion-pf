package mx.com.ferbo.dao.catalogos;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.catalogos.ConceptoEgreso;

@Named
@ApplicationScoped
public class ConceptoEgresoDAO extends BaseDAO<ConceptoEgreso, Integer> {

    private static final Logger log = LogManager.getLogger(ConceptoEgresoDAO.class);

    public ConceptoEgresoDAO(){
        super(ConceptoEgreso.class);
    }
}
