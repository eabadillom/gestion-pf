package mx.com.ferbo.egresos.dao.catalogos;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.transaction.SystemException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.egresos.model.calogos.StatusEgreso;

@Named
@ApplicationScoped
public class StatusEgresoDAO extends BaseDAO<StatusEgreso, Long> {

    private static final Logger log = LogManager.getLogger(StatusEgreso.class);

    public StatusEgresoDAO() {
        super(StatusEgreso.class);
    }

    public StatusEgresoDAO(Class<StatusEgreso> modelClass) {
        super(modelClass);
    }

    public StatusEgreso buscarPorClave(String clave) throws SystemException {
        EntityManager em = null;
        StatusEgreso status = null;
        try {
            em = super.getEntityManager();
            status = em.createNamedQuery("StatusEgreso.findByClave", StatusEgreso.class).setParameter("status", status)
                    .getSingleResult();
            return status;
        } catch (Exception ex) {
            log.info("Error al momento de buscar el status con clave: {}. {}", clave, ex);
            throw new SystemException("Hubo un problema al buscar el status por clave");
        } finally {
            super.close(em);
        }
    }

    public List<StatusEgreso> buscarActivosOInactivos(Boolean activo) throws SystemException {
        EntityManager em = null;
        List<StatusEgreso> status = null;
        try {
            em = super.getEntityManager();
            status = em.createNamedQuery("StatusEgreso.findActivosOInactivos", StatusEgreso.class)
                    .setParameter("activo", activo).getResultList();
            return status;
        } catch (Exception ex) {
            String estado = (activo) ? "activos" : "inactivos";
            log.info("Error al momento de buscar los status {}. {}", estado, ex);
            throw new SystemException("Hubo un problema al momento de bsucar los status " + estado + ".");
        } finally {
            super.close(em);
        }
    }
}
