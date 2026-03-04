package mx.com.ferbo.modulos.pagos.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.util.DAOException;

public abstract class PagoBaseDAO<T> extends BaseDAO<T, Integer> {

    private final String entityName;
    private static final Logger log = LogManager.getLogger(PagoBaseDAO.class);

    protected PagoBaseDAO(Class<T> entityClass) {
        super(entityClass);
        this.entityName = entityClass.getSimpleName();
    }

    protected abstract Class<T> getEntityClass();

    public List<T> buscarVigentes(Date fecha) throws DAOException {
        EntityManager em = null;
        try {
            em = super.getEntityManager();
            return em.createNamedQuery(entityName + ".findVigentes", getEntityClass())
                     .setParameter("fecha", fecha)
                     .getResultList();
        } catch (Exception ex) {
            log.error("Error al obtener los registros vigentes de {} hasta la fecha {}", entityName, fecha, ex);
            throw new DAOException("Ocurrió un problema al obtener los registros vigentes de " + entityName, ex);
        } finally {
            super.close(em);
        }
    }
}