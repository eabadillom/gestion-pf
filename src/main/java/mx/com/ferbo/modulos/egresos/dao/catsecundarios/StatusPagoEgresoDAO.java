package mx.com.ferbo.modulos.egresos.dao.catsecundarios;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.modulos.egresos.dao.CatEgresoBaseDAO;
import mx.com.ferbo.modulos.egresos.model.catsecundarios.StatusPagoEgreso;

@Named
@ApplicationScoped
public class StatusPagoEgresoDAO extends CatEgresoBaseDAO<StatusPagoEgreso> {

    private static final Logger log = LogManager.getLogger(StatusPagoEgresoDAO.class);

    public StatusPagoEgresoDAO() {
        super(StatusPagoEgreso.class);
    }

    @Override
    protected Class<StatusPagoEgreso> getEntityClass() {
        return StatusPagoEgreso.class;
    }

}
