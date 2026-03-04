package mx.com.ferbo.modulos.pagos.dao;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.model.MetodoPago;

@Named
@ApplicationScoped
public class MetodoPagoDAO extends PagoBaseDAO<MetodoPago> {

    private static Logger log = LogManager.getLogger(MetodoPago.class);

    public MetodoPagoDAO() {
        super(MetodoPago.class);
    }

    @Override
    protected Class<MetodoPago> getEntityClass() {
        return MetodoPago.class;
    }


}
