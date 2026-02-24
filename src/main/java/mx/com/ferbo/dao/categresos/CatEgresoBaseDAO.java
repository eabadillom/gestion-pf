
package mx.com.ferbo.dao.categresos;

import java.util.List;
import javax.persistence.EntityManager;
import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.util.DAOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class CatEgresoBaseDAO<T> extends BaseDAO<T, Integer> {
    
    private final String entityName;
    
    private static final Logger log = LogManager.getLogger(CatEgresoBaseDAO.class);
    
    protected CatEgresoBaseDAO(Class<T> entityClass) {
        super(entityClass);
        this.entityName = entityClass.getSimpleName();
    }
    
    protected abstract Class<T> getEntityClass();
    
    // Metodo pensado inicialmente en los catalogos
	public T buscarPorNombre(String nombre) throws DAOException {
		EntityManager em = null;
		try {
			em = getEntityManager();
			return em.createQuery(entityName + ".findByNombre", getEntityClass())
					.setParameter("nombre", nombre)
					.getSingleResult();
		} catch (Exception ex) {
			log.error("Error al buscar {} por nombre {}: {}", modelClass.getSimpleName(), nombre, ex);
			throw new DAOException(
					"Hubo un problema al buscar " + modelClass.getSimpleName() + " por nombre: " + nombre, ex);
		} finally {
			close(em);
		}
	}

	// Metodo pensado inicialmente en los catalogos
	public List<T> findByVigente(boolean vigente) throws DAOException {
		EntityManager em = null;
		List<T> resultados = null;
		try {
			em = getEntityManager();
			resultados = em.createQuery(entityName + ".findAllVigentesONoVigentes", getEntityClass())
					.setParameter("vigente", vigente)
					.getResultList();
		} catch (Exception ex) {
			log.error("Error al obtener elementos vigentes: ", ex);
			throw new DAOException("Error al obtener elementos vigentes de " + modelClass.getSimpleName(), ex);
		} finally {
			close(em);
		}
		return resultados;
	}
}
