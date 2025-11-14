package mx.com.ferbo.business.n;

import java.util.List;
import javax.enterprise.context.RequestScoped;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.dao.n.CategoriaDAO;
import mx.com.ferbo.model.Categoria;
import mx.com.ferbo.util.DAOException;
import mx.com.ferbo.util.InventarioException;

@Named
@RequestScoped
public class CategoriaBL {

    private static final Logger log = LogManager.getLogger(CategoriaBL.class);

    @Inject
    private CategoriaDAO categoriaDAO;

    public List<Categoria> obtenerCategorias() throws InventarioException {
        try {
            return categoriaDAO.buscarTodos();
        } catch (DAOException ex) {
            log.error("Error al obtener las categorias", ex);
            throw new InventarioException("Ocurrió un error al obtener las categorias", ex);
        }
    }

}
