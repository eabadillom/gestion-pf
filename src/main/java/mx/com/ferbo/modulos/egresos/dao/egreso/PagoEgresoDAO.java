package mx.com.ferbo.modulos.egresos.dao.egreso;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.modulos.egresos.dao.EgresoBaseDAO;
import mx.com.ferbo.modulos.egresos.model.catsecundarios.StatusPagoEgreso;
import mx.com.ferbo.modulos.egresos.model.egreso.PagoEgreso;

@Named
@ApplicationScoped
public class PagoEgresoDAO extends EgresoBaseDAO <PagoEgreso, StatusPagoEgreso> {

    private static final Logger log = LogManager.getLogger(PagoEgresoDAO.class);

    public PagoEgresoDAO() {
        super(PagoEgreso.class);
    }

    @Override
    protected Class<PagoEgreso> getEntityClass() {
        return PagoEgreso.class;
    }

}
