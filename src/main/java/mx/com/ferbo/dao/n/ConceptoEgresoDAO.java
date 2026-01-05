package mx.com.ferbo.dao.n;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.n.ConceptoEgreso;
import mx.com.ferbo.util.DAOException;

@Named
@ApplicationScoped
public class ConceptoEgresoDAO extends BaseDAO <ConceptoEgreso, Integer> {

    private static final Logger log = LogManager.getLogger(ConceptoEgresoDAO.class);

    public ConceptoEgresoDAO(){
        super(ConceptoEgreso.class);
    }

    public List<ConceptoEgreso> buscarTodosPorCategoriaEgreso(Integer id) throws DAOException{
        List<ConceptoEgreso> lista = null;
        EntityManager em = null;
        try {
            em = super.getEntityManager();
            lista = em.createQuery("ConceptoEgreso.findAllByCategoriaEgreso", ConceptoEgreso.class).setParameter("id", id).getResultList();
            return lista;
        } catch (Exception ex) {
            log.error("Error al buscar los conceptos de egreso de la categoria con id {}. {}", id, ex);
            throw new DAOException("Hubo un problema al buscar los conceptos de egreso por id de categoria");
        } finally {
            super.close(em);
        }
    }
}
