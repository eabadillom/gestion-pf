package mx.com.ferbo.business.catalogos;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.dao.n.catalogos.CategoriaEgresoDAO;
import mx.com.ferbo.model.n.catalogos.CategoriaEgreso;
import mx.com.ferbo.model.n.catalogos.TipoEgreso;
import mx.com.ferbo.util.DAOException;
import mx.com.ferbo.util.InventarioException;

@Named
@RequestScoped
public class CategoriaEgresoBL extends BaseCatalogosBL<CategoriaEgreso> {

    private static final Logger log = LogManager.getLogger(CategoriaEgresoBL.class);

    @Inject
    private CategoriaEgresoDAO categoriaEgresoDAO;

    @PostConstruct
    public void init() {
        setDao(categoriaEgresoDAO);
    }

    @Override
    protected void validarEspecifico(CategoriaEgreso model) throws InventarioException {
        if (model.getTipoEgreso() == null) {
            throw new InventarioException("La categoria " + model.getNombre() + " no tiene ninguna asociación con tipo de egreso.");
        }
    }

    public List<CategoriaEgreso> obtenerCateogiasPorTipoYEstado(TipoEgreso tipoEgreso, boolean activo) throws InventarioException {
        try {
            return categoriaEgresoDAO.buscarTodosPorTipoEgresoYActivo(tipoEgreso.getId(), activo);
        } catch (DAOException ex) {
            log.warn("No se obtuvo elemento alguno de categorias asociadas con el tipo de egreso {} y el estado {}. {}", tipoEgreso.getNombre(), activo, ex);
            throw new InventarioException("Hubo un problema al obtene categorias asociadas con el tipo de egreso " + tipoEgreso.getNombre() + " y estado " + activo);
        }
    }

    public List<CategoriaEgreso> obtenerCategiriasPorTipoEgreso(TipoEgreso tipoEgreso) throws InventarioException {
        try {
            return categoriaEgresoDAO.buscarTodosPorTipoEgreso(tipoEgreso.getId());
        } catch (DAOException ex) {
            log.warn("No se obtuvo elemento alguno de las categorias asocialdas con el tipo de egreso {}. {}", tipoEgreso.getNombre(), ex);
            throw new InventarioException("Hubo un problema al obtene categorias asociadas con el tipo de egreso " + tipoEgreso.getNombre());
        }
    }

    public void verificarExistenciaHijos(TipoEgreso tipo) throws InventarioException {
        try {
            List<CategoriaEgreso> categorias = categoriaEgresoDAO.buscarTodosPorTipoEgresoYActivo(tipo.getId(), true);
            if (!categorias.isEmpty()) {
                throw new DAOException("No se puede cancelar el tipo de egreso por tener categorias vigentes.");
            }
        } catch (DAOException ex) {
            throw new InventarioException(ex.getMessage());
        }
    }

}
