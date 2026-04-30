package mx.com.ferbo.egresos.bl.catalogos;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ferbo.tools.exception.SystemException;
import com.ferbo.tools.validation.ObjectValidatorBuilder;

import mx.com.ferbo.egresos.dao.catalogos.CategoriaEgresoDAO;
import mx.com.ferbo.egresos.model.calogos.CategoriaEgreso;

public class CategoriaEgresoBL {

    private static final Logger log = LogManager.getLogger(CategoriaEgresoBL.class);

    private final CategoriaEgresoDAO dao;

    public CategoriaEgresoBL(CategoriaEgresoDAO dao) {
        this.dao = dao;
    }

    public CategoriaEgreso buscarPorClave(String clave) {
        try {
            return dao.buscarPorClave(clave);

        } catch (Exception ex) {
            log.error("Error buscando categoria con clave {}", clave, ex);
            throw new SystemException("No se pudo obtener la categoría");
        }
    }

    public List<CategoriaEgreso> buscarActivos(Boolean activo) {
        try {
            return dao.buscarActivosOInactivos(activo);

        } catch (Exception ex) {
            String estado = Boolean.TRUE.equals(activo) ? "activas" : "inactivas";

            log.error("Error buscando categorías {}", estado, ex);
            throw new SystemException("Error consultando categorías " + estado);
        }
    }

    public void guardar(CategoriaEgreso categoria) {

        validar(categoria);

        try {
            dao.save(categoria);

        } catch (Exception ex) {
            log.error("Error al guardar categoria {}", categoria.getNombre(), ex);
            throw new SystemException(
                    "Hubo un problema al guardar la categoría: " + categoria.getNombre());
        }
    }

    private void validar(CategoriaEgreso categoria) {

        new ObjectValidatorBuilder<>("categoria egreso", categoria)
                .validateObject()
                .texto("nombre", CategoriaEgreso::getNombre)
                .texto("clave", CategoriaEgreso::getClave)
                .texto("descripcion", CategoriaEgreso::getDescripcion)
                .integer("orden", CategoriaEgreso::getOrden, 1, 100)
                .validateOrThrow();
    }
}
