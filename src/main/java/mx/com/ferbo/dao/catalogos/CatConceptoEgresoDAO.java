package mx.com.ferbo.dao.catalogos;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.catalogos.CatConceptoEgreso;
import mx.com.ferbo.util.DAOException;

@Named
@ApplicationScoped
public class CatConceptoEgresoDAO extends BaseDAO<CatConceptoEgreso, Integer> {

    private static final Logger log = LogManager.getLogger(CatConceptoEgresoDAO.class);

    CatConceptoEgresoDAO(){
        super(CatConceptoEgreso.class);
    }

    public List<CatConceptoEgreso> buscarPorCategoriaEgreso(Integer id) throws DAOException{
        List<CatConceptoEgreso> lista = null;
        EntityManager em = null;
        try {
            em = super.getEntityManager();
            lista = em.createNamedQuery("CatConceptoEgreso.findAllByCategoriaEgreso", CatConceptoEgreso.class).setParameter("id", id).getResultList();
            return lista;
        } catch (Exception ex) {
            log.error("Error al obtner la lista de conceptos de egreso relacionadas con la categoria de id {}. {}", id, ex);
            throw new DAOException("Hubo un problema al obtener la lista de conceptos.");
        } finally {
            super.close(em);
        }
    }

    public List<CatConceptoEgreso> buscarPorCategoriaEgresoYEstado(Integer id, Boolean vigente) throws DAOException {
        List<CatConceptoEgreso> lista = null;
        EntityManager em = null;
        try {
            em = super.getEntityManager();
            lista = em.createNamedQuery("CatConceptoEgreso.findAllByCategoriaEgresoYVigencia", CatConceptoEgreso.class).setParameter("id", id).setParameter("vigente", vigente).getResultList();
            return lista;
        } catch (Exception ex) {
            log.error("Error al obtner la lista de conceptos de egreso relacionadas con la categoria de id {} y si es vigente {}. {}", id, vigente, ex);
            throw new DAOException("Hubo un problema al obtener la lista de conceptos.");
        } finally {
            super.close(em);
        }
    }
}
