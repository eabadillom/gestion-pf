package mx.com.ferbo.egresos.business.catalogos;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ferbo.tools.exception.BusinessException;
import com.ferbo.tools.exception.SystemException;
import com.ferbo.tools.validation.ObjectValidator;
import com.ferbo.tools.validation.ObjectValidatorBuilder;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import mx.com.ferbo.egresos.dao.catalogos.StatusEgresoDAO;
import mx.com.ferbo.egresos.model.Egreso;
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
    public void guardar(StatusEgreso status) throws SystemException {
        String estado = "";
        try {
            if (status.getId() == null) {
                dao.guardar(status);
                estado = "guardar";
            } else {
                dao.actualizar(status);
                estado = "actualizar";
            }
        } catch (InventarioException ex) {
            log.error("Error al {} status {}", estado, status.getNombre(), ex);
            throw new SystemException("Hubo un problema al momento de " + estado +" el status: " + status.getNombre());
        }
    }

    public void validar(StatusEgreso status) {

        new ObjectValidatorBuilder<>("status egreso", status)
                .validateObject()
                .texto("nombre", StatusEgreso::getNombre)
                .texto("clave", StatusEgreso::getClave)
                .texto("descripcion", StatusEgreso::getDescripcion)
                .integer("orden", StatusEgreso::getOrden, 1, 100)
                .validateOrThrow();
    }

    public void desactivarStatus(List<Egreso> egresos, StatusEgreso status) {
        ObjectValidator.notNull(status, "status de egreso");

        if (!egresos.isEmpty()) {
            throw new BusinessException("No se puede desactivar el status del egreso por tener egresos dependientes de el.");
        }
        
        Boolean nuevo = status.getActivo();

        status.setActivo(!nuevo);
    }
    
}
