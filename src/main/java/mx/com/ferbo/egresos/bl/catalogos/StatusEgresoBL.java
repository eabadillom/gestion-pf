package mx.com.ferbo.egresos.bl.catalogos;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ferbo.tools.exception.SystemException;
import com.ferbo.tools.validation.ObjectValidatorBuilder;

import mx.com.ferbo.egresos.dao.catalogos.StatusEgresoDAO;
import mx.com.ferbo.egresos.model.calogos.StatusEgreso;

public class StatusEgresoBL {

    private static final Logger log = LogManager.getLogger(StatusEgresoBL.class);

    private final StatusEgresoDAO dao;

    public StatusEgresoBL(StatusEgresoDAO dao) {
        this.dao = dao;
    }

    public StatusEgreso buscarPorClave(String clave) {
        try {
            return dao.buscarPorClave(clave);

        } catch (Exception ex) {
            log.error("Error buscando status con clave {}", clave, ex);
            throw new SystemException("No se pudo obtener el status");
        }
    }

    public List<StatusEgreso> buscarActivos(Boolean activo) {
        try {
            return dao.buscarActivosOInactivos(activo);

        } catch (Exception ex) {
            String estado = Boolean.TRUE.equals(activo) ? "activos" : "inactivos";

            log.error("Error buscando status {}", estado, ex);
            throw new SystemException("Error consultando status " + estado);
        }
    }

    public void guardar(StatusEgreso status) {

        validar(status);

        try {
            dao.save(status);

        } catch (Exception ex) {
            log.error("Error al guardar status {}", status.getNombre(), ex);
            throw new SystemException(
                    "Hubo un problema al guardar el status: " + status.getNombre());
        }
    }

    private void validar(StatusEgreso status) {

        new ObjectValidatorBuilder<>("status egreso", status)
                .validateObject()
                .texto("nombre", StatusEgreso::getNombre)
                .texto("clave", StatusEgreso::getClave)
                .texto("descripcion", StatusEgreso::getDescripcion)
                .integer("orden", StatusEgreso::getOrden, 1, 100)
                .validateOrThrow();
    }
    
}
