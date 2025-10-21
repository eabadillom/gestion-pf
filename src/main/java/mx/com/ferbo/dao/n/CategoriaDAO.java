package mx.com.ferbo.dao.n;

import java.util.List;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.Categoria;

public class CategoriaDAO extends BaseDAO<Categoria, Integer> {

    private static Logger log = LogManager.getLogger(CategoriaDAO.class);

    public CategoriaDAO() {
        super(Categoria.class);
    }

    public List<Categoria> buscarTodos() {
        EntityManager em = null;
        List<Categoria> list = null;
        try {
            em = super.getEntityManager();
            list = em.createNamedQuery("Categoria.findAll", Categoria.class).getResultList();
        } finally {
            super.close(em);
        }
        return list;
    }

}
