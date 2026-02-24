package mx.com.ferbo.dao.egresos;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import mx.com.ferbo.model.egresos.PagoEgreso;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Named
@ApplicationScoped
public class PagoEgresoDAO extends EgresoBaseDAO <PagoEgreso> {

    private static final Logger log = LogManager.getLogger(PagoEgresoDAO.class);

    public PagoEgresoDAO() {
        super(PagoEgreso.class);
    }

    @Override
    protected Class getEntityClass() {
        return PagoEgreso.class;
    }

}
