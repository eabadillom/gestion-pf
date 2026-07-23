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

    private final static Logger log = LogManager.getLogger(StatusEgresoDAO.class);

    public StatusEgresoDAO() {
        super(StatusEgreso.class);
    }

    public StatusEgreso buscarPorClave(String clave) throws SystemException {
        EntityManager em = null ;
        try {
            em = getEntityManager();
            return em.createNamedQuery("StatusEgreso.findByClave", StatusEgreso.class).setParameter("clave", clave)
                    .getSingleResult();
        } catch (Exception ex) {
            log.error("Error al momento de buscar los status de egreso por clave. {}", ex);
            throw new SystemException("Hubo un problema al momento de buscar los status de egresos por clave");
        } finally {
            close(em);
        }

    }

    public List<StatusEgreso> buscarActivosOInactivos(Boolean activo) throws SystemException {
        EntityManager em = null ;
        try {
            em = getEntityManager();
            return em.createNamedQuery("StatusEgreso.findActivosOInactivos", StatusEgreso.class)
                    .setParameter("activo", activo).getResultList();

        } catch (Exception ex) {
            String estado = (activo) ? "activos" : "inactivos";
            log.error("Error al momento de buscar los status de egreso {}. {}", estado, ex);
            throw new SystemException("Hubo un problema al momento de buscar los status de egreso " + estado + ".");
        } finally {
            close(em);
        }
    }
}
