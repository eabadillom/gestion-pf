package mx.com.ferbo.dao.n;

import java.util.List;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.Categoria;
import mx.com.ferbo.util.DAOException;

public class CategoriaDAO extends BaseDAO<Categoria, Integer> {

    private static Logger log = LogManager.getLogger(CategoriaDAO.class);

    public CategoriaDAO() {
        super(Categoria.class);
    }

    public List<Categoria> buscarTodos() throws DAOException {
        EntityManager em = null;
        List<Categoria> list = null;
        try {
            em = super.getEntityManager();
            list = em.createNamedQuery("Categoria.findAll", Categoria.class).getResultList();
        } catch (Exception ex){
            log.error("Error al obtener las categorias", ex);
            throw new DAOException("Ocurrió un error al obtener las categorias", ex);
        } finally {
            super.close(em);
        }
        return list;
    }

}
