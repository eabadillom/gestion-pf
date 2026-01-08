package mx.com.ferbo.business;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.dao.n.CategoriaEgresoDAO;
import mx.com.ferbo.model.n.CategoriaEgreso;
import mx.com.ferbo.model.n.TipoEgreso;
import mx.com.ferbo.util.DAOException;
import mx.com.ferbo.util.InventarioException;

@Named
@RequestScoped
public class CategoriaEgresoBL {

    private static final Logger log = LogManager.getLogger(CategoriaEgresoBL.class);

    @Inject
    private CategoriaEgresoDAO categoriaEgresoDAO;

    public CategoriaEgreso validarCategoriaEgreso(CategoriaEgreso categoriaEgreso) throws InventarioException {
        if (categoriaEgreso == null) {
            throw new InventarioException("La categoria de egreso no puede ser vacía.");
        }
        
        if (categoriaEgreso.getNombre().equalsIgnoreCase("")) {
            throw new InventarioException("La categoria de egreso no tiene asociado un nombre.");
        }

        if (categoriaEgreso.getActivo() == null) {
            throw new InventarioException("La categoria de egreso no se sabe si esta activa o inactiva.");
        }

        if (categoriaEgreso.getId() == null) {
            throw new InventarioException("La categoria de egreso " + categoriaEgreso.getNombre() + " no esta asociada a ningun tipo de egreso.");
        }

        return categoriaEgreso;
    }

    public List<CategoriaEgreso> obtenerCaterorias(boolean activo) throws InventarioException{
        try {
            return categoriaEgresoDAO.buscarTodosActivos(activo);
        } catch (DAOException ex) {
            log.warn(ex.getMessage());
            throw new InventarioException(ex.getMessage());
        }
    }

    public List<CategoriaEgreso> obtenerCateogiasPorTipoYEstado(TipoEgreso tipoEgreso, boolean activo) throws InventarioException {
        try {
            return categoriaEgresoDAO.buscarTodosPorTipoEgresoYActivo(tipoEgreso.getId(), activo);
        } catch (Exception ex) {
            log.warn("No se obtuvo elemento alguno de categorias asociadas con el tipo de egreso {} y el estado {}. {}", tipoEgreso.getNombre(), activo, ex);
            throw new InventarioException("Hubo un problema al obtene categorias asociadas con el tipo de egreso " + tipoEgreso.getNombre() + " y estado " + activo);
        }
    }

    public List<CategoriaEgreso> obtenerCategiriasP(TipoEgreso tipoEgreso) throws InventarioException{
        try {
            return categoriaEgresoDAO.buscarTodosPorTipoEgreso(tipoEgreso.getId());
        } catch (DAOException ex) {
            log.warn("No se obtuvo elemento alguno de las categorias asocialdas con el tipo de egreso {}. {}", tipoEgreso.getNombre(), ex);
            throw new InventarioException("Hubo un problema al obtene categorias asociadas con el tipo de egreso " + tipoEgreso.getNombre());
        }
    }

    public String agregarOActualizar(CategoriaEgreso categoriaEgreso) throws InventarioException {
        validarCategoriaEgreso(categoriaEgreso);
        String mensaje = "La categoria de egreso " + categoriaEgreso.getNombre();

        if (categoriaEgreso.getId() == null) {
            categoriaEgresoDAO.guardar(categoriaEgreso);
            mensaje += " se agrego correctamente";
        } else {
            categoriaEgresoDAO.actualizar(categoriaEgreso);
            mensaje += " se actualizo correctamente";
        }
        return mensaje;
    }

}
