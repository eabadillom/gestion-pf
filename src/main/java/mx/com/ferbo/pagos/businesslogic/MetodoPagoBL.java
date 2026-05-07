package mx.com.ferbo.pagos.businesslogic;

import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ferbo.tools.exception.SystemException;

import mx.com.ferbo.model.MetodoPago;
import mx.com.ferbo.pagos.dao.MetodoPagoDAO;

@Named
@RequestScoped
public class MetodoPagoBL {

    private static final Logger log = LogManager.getLogger(MetodoPagoBL.class);

    @Inject
    private MetodoPagoDAO metodoPagoDAO;

    public List<MetodoPago> obtenerMetodosPago() throws SystemException {
        log.info("Inicia proceso para obtener todos los metodos de pago");
        Date fecha = new Date();

        try {
            return metodoPagoDAO.buscarVigentes(fecha);
        } catch (SystemException ex) {
            log.error("Error al obtener los métodos de pago", ex);
            throw ex;
        }
    }

}
