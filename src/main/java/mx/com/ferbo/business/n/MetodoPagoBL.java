package mx.com.ferbo.business.n;

import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.dao.n.MetodoPagoDAO;
import mx.com.ferbo.model.MetodoPago;
import mx.com.ferbo.util.DAOException;
import mx.com.ferbo.util.InventarioException;

@Named
@RequestScoped
public class MetodoPagoBL {

    private static final Logger log = LogManager.getLogger(MetodoPagoBL.class);

    @Inject
    private MetodoPagoDAO metodoPagoDAO;

    public List<MetodoPago> obtenerMetodosPago() throws InventarioException {
        log.info("Inicia proceso para obtener todos los metodos de pago");
        Date fecha = new Date();

        try {
            return metodoPagoDAO.buscarVigentes(fecha);
        } catch (DAOException ex) {
            log.error("Error al obtener los métodos de pago", ex);
            throw new InventarioException("Ocurrió un problema al obtener los métodos de pago", ex);
        }
    }

}
