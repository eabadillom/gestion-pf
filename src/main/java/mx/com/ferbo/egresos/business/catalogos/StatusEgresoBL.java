package mx.com.ferbo.egresos.business.catalogos;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ferbo.tools.exception.SystemException;
import com.ferbo.tools.validation.ObjectValidatorBuilder;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import mx.com.ferbo.egresos.dao.catalogos.StatusEgresoDAO;
import mx.com.ferbo.egresos.model.calogos.StatusEgreso;
import mx.com.ferbo.util.InventarioException;

@Named
@RequestScoped
public class StatusEgresoBL implements CatalogoBL<StatusEgreso> {

    private static final Logger log = LogManager.getLogger(StatusEgresoBL.class);

    @Inject
    private StatusEgresoDAO dao;

    @Override
    public StatusEgreso buscarPorClave(String clave) {
        try {
            return dao.buscarPorClave(clave);

        } catch (Exception ex) {
            log.error("Error buscando status con clave {}", clave, ex);
            throw new SystemException("No se pudo obtener el status");
        }
    }

    @Override
    public List<StatusEgreso> buscarActivos(Boolean activo) {
        try {
            return dao.buscarActivosOInactivos(activo);

        } catch (Exception ex) {
            String estado = Boolean.TRUE.equals(activo) ? "activos" : "inactivos";

            log.error("Error buscando status {}", estado, ex);
            throw new SystemException("Error consultando status " + estado);
        }
    }

    @Override
    public void guardar(StatusEgreso status) throws InventarioException {
        validar(status);
        try {
            if (status.getId() == null) {
                dao.guardar(status);
            } else {
                dao.actualizar(status);
            }
        } catch (InventarioException ex) {
            log.error("Error al guardar status {}", status.getNombre(), ex);
            throw ex;
            //throw new SystemException("Hubo un problema al guardar el status: " + status.getNombre());
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
