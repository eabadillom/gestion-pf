
package mx.com.ferbo.dao.egresos;

import java.util.List;
import javax.persistence.EntityManager;
import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.util.DAOException;

public abstract class EgresoBaseDAO<T> extends BaseDAO<T, Integer> {

    private final String entityName;

    protected EgresoBaseDAO(Class<T> entityClass) {
        super(entityClass);
        this.entityName = entityClass.getSimpleName();
    }

    public List<T> buscarPorImporteEgreso(Integer idImporteEgreso) throws DAOException {
        EntityManager em = null;
        try {
            em = super.getEntityManager();

            return em.createNamedQuery(
                    entityName + ".findAllByImporteEgreso",
                    getEntityClass()
            )
            .setParameter("idImporteEgreso", idImporteEgreso)
            .getResultList();

        } catch (Exception ex) {
            throw new DAOException("Error al buscar por ImporteEgreso.", ex);
        } finally {
            super.close(em);
        }
    }

    protected abstract Class<T> getEntityClass();
}
