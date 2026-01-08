package mx.com.ferbo.business;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.dao.n.StatusPagoDAO;
import mx.com.ferbo.model.StatusPago;
import mx.com.ferbo.util.DAOException;
import mx.com.ferbo.util.InventarioException;

@Named
@RequestScoped
public class StatusPagoBL {

    private static final Logger log = LogManager.getLogger(StatusPagoBL.class);

    @Inject
    private StatusPagoDAO statusPagoDAO;

    public StatusPago validarStatusPago(StatusPago statusPago) throws InventarioException {
        if (statusPago == null) {
            throw new InventarioException("El status del pago no puede ser vacío.");
        }

        if ("".equalsIgnoreCase(statusPago.getNombre())) {
            throw new InventarioException("El status del pago no tiene ningun nombre asociado.");
        }

        if ("".equalsIgnoreCase(statusPago.getDescripcion())) {
            throw new InventarioException("El status del pago debe tener un descripción");
        }

        if (statusPago.getActivo() == null) {
            throw new InventarioException("No se sabe si el status del pago esta o no activo.");
        }
        
        return statusPago;
    }

    public List<StatusPago> obtenerStatusPago(boolean activo) throws InventarioException {
        try {
            return statusPagoDAO.buscarStatusPago(activo);
        } catch (DAOException ex) {
            log.warn(ex.getMessage());
            throw new InventarioException(ex.getMessage());
        }
    }

    public String agregarOActualizar(StatusPago statusPago) throws InventarioException {
        validarStatusPago(statusPago);
        String mensaje = "El status de pago " +  statusPago.getNombre();

        if (statusPago.getId() == null) {
            statusPagoDAO.guardar(statusPago);
            mensaje += " se guardo exitosamente";
        } else {
            statusPagoDAO.actualizar(statusPago);
            mensaje += "se actualizo exitosamente";
        }
        return mensaje;
    }
}
