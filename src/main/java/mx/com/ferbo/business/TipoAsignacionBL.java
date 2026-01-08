package mx.com.ferbo.business;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.dao.TipoAsignacionDAO;
import mx.com.ferbo.model.TipoAsignacion;
import mx.com.ferbo.util.InventarioException;

@Named
@RequestScoped
public class TipoAsignacionBL {

    private static final Logger log = LogManager.getLogger(TipoAsignacionBL.class);

    @Inject
    private TipoAsignacionDAO tipoAsignacionDAO;

    public TipoAsignacion validarTipoAsignacion(TipoAsignacion tipoAsignacion) throws InventarioException {
        if (tipoAsignacion == null) {
            throw new InventarioException("El tipo de asignación no puede ser vacío.");
        }

        if ("".equalsIgnoreCase(tipoAsignacion.getNombre())){
            throw new InventarioException("El tipo de asignación no tiene ningun nombre asociado");
        }

        if (tipoAsignacion.getActivo() == null) {
            throw new InventarioException("No se sabe si el tipo de asignación está o no activo");
        }

        return tipoAsignacion;
    }

    public List<TipoAsignacion> obtenesTiposAsignacion(boolean activo) throws InventarioException{
        try {
            return tipoAsignacionDAO.buscarTiposAsignacion(activo);
        } catch (Exception ex) {
            log.warn(ex.getMessage());
            throw new InventarioException(ex.getMessage());
        }
    }

    public String agregarOActualizar(TipoAsignacion tipoAsignacion) throws InventarioException {
        validarTipoAsignacion(tipoAsignacion);
        String mensaje = "El tipo de asignación " + tipoAsignacion.getNombre(); 
        
        if (tipoAsignacion.getId() == null) {
            tipoAsignacionDAO.guardar(tipoAsignacion);
            mensaje += " se guardo exitorsamente";
        } else {
            tipoAsignacionDAO.actualizar(tipoAsignacion);
            mensaje += " se actualizo correctamente";
        }
        return mensaje;
    }
}
