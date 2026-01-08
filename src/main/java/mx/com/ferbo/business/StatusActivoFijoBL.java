package mx.com.ferbo.business;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.dao.n.StatusActivoFijoDAO;
import mx.com.ferbo.model.StatusActivoFijo;
import mx.com.ferbo.util.DAOException;
import mx.com.ferbo.util.InventarioException;

@Named
@RequestScoped
public class StatusActivoFijoBL {

    private static final Logger log = LogManager.getLogger(StatusActivoFijoBL.class );

    @Inject
    private StatusActivoFijoDAO statusActivoFijoDAO;

    public StatusActivoFijo validarStatusActivoFijo(StatusActivoFijo statusActivoFijo) throws InventarioException {
        if (statusActivoFijo == null) {
            throw new InventarioException("El status de activo fijo no puede ser vacío.");
        }

        if ("".equalsIgnoreCase(statusActivoFijo.getNombre())) {
            throw new InventarioException("El status de activo fijo no tiene asociado un nombre.");
        }

        if ("".equalsIgnoreCase(statusActivoFijo.getDescripcion())){
            throw new InventarioException("El status de activo fijo debe tener una descripción.");
        }

        if (statusActivoFijo.getActivo() == null) {
            throw new InventarioException("No se sabe si el status de activo fijo esta activo o no.");
        }

        return statusActivoFijo;
    }

    public List<StatusActivoFijo> obtenerStatusActivoFijo(boolean activo) throws InventarioException {
        try {
            return statusActivoFijoDAO.buscarActivos(activo);
        } catch (DAOException ex) {
            log.warn(ex.getMessage());
            throw new InventarioException(ex.getMessage());
        }
    }

    public String agregarOActualizar(StatusActivoFijo statusActivoFijo) throws InventarioException {
        validarStatusActivoFijo(statusActivoFijo);
        String mensaje = "El status " + statusActivoFijo.getNombre() + " de activo fijo";

        if (statusActivoFijo.getId() == null) {
            statusActivoFijoDAO.guardar(statusActivoFijo);
            mensaje += " se guardo exitosamente.";
        } else {
            statusActivoFijoDAO.actualizar(statusActivoFijo);
            mensaje += " se actualizo exitosamente";
        }
        return mensaje;
    }
}
