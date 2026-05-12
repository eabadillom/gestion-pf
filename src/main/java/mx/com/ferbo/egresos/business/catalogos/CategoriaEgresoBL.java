package mx.com.ferbo.egresos.business.catalogos;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ferbo.tools.exception.BusinessException;
import com.ferbo.tools.exception.SystemException;
import com.ferbo.tools.validation.ObjectValidator;
import com.ferbo.tools.validation.ObjectValidatorBuilder;

import mx.com.ferbo.egresos.dao.catalogos.CategoriaEgresoDAO;
import mx.com.ferbo.egresos.model.Egreso;
import mx.com.ferbo.egresos.model.calogos.CategoriaEgreso;

@Named
@RequestScoped
public class CategoriaEgresoBL implements CatalogoBL<CategoriaEgreso> {

    private static final Logger log = LogManager.getLogger(CategoriaEgresoBL.class);

    @Inject
    private CategoriaEgresoDAO dao;

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
            if (categoria.getId() == null) {
                dao.guardar(categoria);
            } else {
                dao.actualizar(categoria);
            }
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

    public void desactivarCategoria(List<Egreso> egresos, CategoriaEgreso categoria) {
        
        ObjectValidator.notNull(categoria, "categoria egreso");

        if (!categoria.getActivo()) {
            throw new BusinessException("La categoría de egreso ya se encuenta desactivada");
        }

        if (!egresos.isEmpty()) {
            throw new BusinessException("No se puede desactivar la categoría del egreso por tenener egresos dependiente de ella.");
        }

        categoria.setActivo(Boolean.FALSE);
    }
}
