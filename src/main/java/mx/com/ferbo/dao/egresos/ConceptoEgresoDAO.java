package mx.com.ferbo.dao.egresos;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.model.egresos.ConceptoEgreso;

@Named
@ApplicationScoped
public class ConceptoEgresoDAO extends EgresoBaseDAO<ConceptoEgreso> {

    private static final Logger log = LogManager.getLogger(ConceptoEgresoDAO.class);

    public ConceptoEgresoDAO(){
        super(ConceptoEgreso.class);
    }

    @Override
    protected Class<ConceptoEgreso> getEntityClass() {
        return ConceptoEgreso.class;
    }
}
