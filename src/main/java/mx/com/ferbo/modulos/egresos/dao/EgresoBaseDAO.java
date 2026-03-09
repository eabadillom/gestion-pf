package mx.com.ferbo.modulos.egresos.dao;

import java.util.List;
import javax.persistence.EntityManager;
import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.modulos.egresos.model.CatEgreso;
import mx.com.ferbo.modulos.egresos.model.Egreso;
import mx.com.ferbo.util.DAOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class EgresoBaseDAO<T extends Egreso, C extends CatEgreso> extends BaseDAO<T, Integer> {

    private final String entityName;

    private static final Logger log = LogManager.getLogger(EgresoBaseDAO.class);

    protected EgresoBaseDAO(Class<T> entityClass) {
        super(entityClass);
        this.entityName = entityClass.getSimpleName();
    }

    protected abstract Class<T> getEntityClass();

    public List<T> buscarPorImporteEgreso(Integer idImporteEgreso) throws DAOException {
        EntityManager em = null;
        try {
            em = super.getEntityManager();

            return em.createNamedQuery(
                    entityName + ".findAllByImporteEgreso",
                    getEntityClass())
                    .setParameter("idImporteEgreso", idImporteEgreso)
                    .getResultList();

        } catch (Exception ex) {
            log.error("Error al buscar los registros de {} por importe egreso. {}", entityName, ex);
            throw new DAOException("Hubo un problema al buscar por Importe Egreso.", ex);
        } finally {
            super.close(em);
        }
    }

    public List<T> buscarPorImporteEgresoYStatus(Integer idImporteEgreso, List<C> lstStatus)
            throws DAOException {
        EntityManager em = null;
        try {
            em = super.getEntityManager();

            String ql = "SELECT e FROM " + entityName + " e " +
                    "WHERE e.importeEgreso.id = :idImporteEgreso " +
                    "AND e.status IN :statusList";

            return em.createQuery(ql, getEntityClass())
                    .setParameter("idImporteEgreso", idImporteEgreso)
                    .setParameter("lstStatus", lstStatus)
                    .getResultList();

        } catch (Exception ex) {
            log.error("Error al buscar los registros de {} por importe egreso y status. {}", entityName, ex);
            throw new DAOException("Hubo un problema al buscar por importe egreso y status");
        } finally {
            super.close(em);
        }
    }

}
