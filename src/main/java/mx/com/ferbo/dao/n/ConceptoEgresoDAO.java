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

    public List<ConceptoEgreso> buscarTodosActivos(boolean activo) throws DAOException {
        List<ConceptoEgreso> lista;
        EntityManager em = null;
        try {
            int estado = (activo) ? 1 : 0;
            em = super.getEntityManager();
            lista = em.createQuery("ConceptoEgreso.findAllActivos", ConceptoEgreso.class).setParameter("activo", estado).getResultList();
            return lista;
        } catch (Exception ex) {
            String estado = (activo) ? "activos" : "no activos"; 
            log.error("Error al buscar la lista de conceptos de egreso {}. {}", estado, ex);
            throw new DAOException("Hubo un problema al buscar la lista de conceptos de egreso " + estado);
        } finally {
            super.close(em);
        }

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

    public List<ConceptoEgreso> buscarTodoPorCategoriaEgresoYActivo(Integer id, boolean activo) throws DAOException {
        List<ConceptoEgreso> lista = null;
        EntityManager em = null;
        try {
            em = super.getEntityManager();
            int estado = (activo) ? 1 : 0; 
            lista = em.createQuery("ConceptoEgreso.findAllByCategoriaEgresoYActivo", ConceptoEgreso.class).setParameter("id", id).setParameter("activo", estado).getResultList();
            return lista;
        } catch (Exception ex) {
            String estado = (activo) ? "activo" : "no activo";
            log.error("Error al obtener la lista de conceptos de egreso asociadad al id {} de categorias y estado {}. {}", id, estado,ex);
            throw new DAOException("Hubo un problema al buscar el listado de conceptos de egreso con el id y estado selccionados");
        } finally {
            super.close(em);
        }
    }
}
