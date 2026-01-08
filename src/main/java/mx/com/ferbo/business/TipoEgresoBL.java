package mx.com.ferbo.business;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.dao.n.TipoEgresoDAO;
import mx.com.ferbo.model.n.TipoEgreso;
import mx.com.ferbo.util.DAOException;
import mx.com.ferbo.util.InventarioException;

@Named
@RequestScoped
public class TipoEgresoBL {

    private static final Logger log = LogManager.getLogger(TipoEgresoBL.class);

    @Inject
    private TipoEgresoDAO tipoEgresoDAO;

    public TipoEgreso validarTipoEgreso(TipoEgreso tipoEgreso) throws InventarioException {
        if (tipoEgreso == null) {
            throw new InventarioException("El tipo de egreso no puede ser vacío");
        }

        if (tipoEgreso.getNombre().equalsIgnoreCase("")) {
            throw new InventarioException("El tipo de egreso no tiene ningun nombre asociado");
        }

        if (tipoEgreso.getActivo() == null) {
            throw new InventarioException("El tipo de egreso no se sabe si esta activo o inactivo");
        }

        return tipoEgreso;
    } 

    public List<TipoEgreso> obtenerTiposEgreso(boolean activo) throws InventarioException{
        try{
            return tipoEgresoDAO.buscarActivos(activo);
        } catch(DAOException ex){
            log.warn(ex.getMessage());
            throw new InventarioException(ex.getMessage());
        }
    }

    public String agregarOActualizar(TipoEgreso tipoEgreso) throws InventarioException {
        validarTipoEgreso(tipoEgreso);
        String mensaje = "El tipo de egreso " + tipoEgreso.getNombre();

        if (tipoEgreso.getId() == null) {
            tipoEgresoDAO.guardar(tipoEgreso);
            mensaje += " se guardo correctamente.";
        } else {
            tipoEgresoDAO.actualizar(tipoEgreso);
            mensaje += " se actualizo correctamente.";
        }
        return mensaje;
    }
}
