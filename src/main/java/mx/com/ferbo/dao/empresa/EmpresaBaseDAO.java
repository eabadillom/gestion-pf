
package mx.com.ferbo.dao.empresa;

import java.util.List;
import javax.persistence.EntityManager;
import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.util.DAOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public abstract class EmpresaBaseDAO<T> extends BaseDAO<T, Integer> {
    
    private final String entityName;
    
    private static final Logger log = LogManager.getLogger(EmpresaBaseDAO.class);
    
    protected EmpresaBaseDAO(Class<T> entityClass) {
        super(entityClass);
        this.entityName = entityClass.getSimpleName();
    }
    
    protected abstract Class<T> getEntityClass();
    
    /* Función recomendada para modelos con muy pocos registros */
	public List<T> buscarTodos() throws DAOException {
		EntityManager em = null;
		List<T> lista = null;
		try {
			em = getEntityManager(); // Método que abre un EntityManager
			lista = em.createNamedQuery(entityName + ".findAll", getEntityClass()).getResultList();
			return lista;
		} catch (Exception ex) {
			log.error("Error al buscar todos los registros de {}. {}", modelClass.getSimpleName(), ex);
			throw new DAOException("Hubo un problema al buscar todos los registros de " + modelClass.getSimpleName());
		} finally {
			close(em); // Método que cierra el EntityManager
		}
	}
}
