package mx.com.ferbo.dao.n;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.StatusPago;
import mx.com.ferbo.util.DAOException;

@Named
@ApplicationScoped
public class StatusPagoDAO extends BaseDAO<StatusPago, Integer> {

    private static final Logger log = LogManager.getLogger(StatusPagoDAO.class);

    public StatusPagoDAO() {
        super(StatusPago.class);
    }

    public StatusPago buscarPorNombre(String nombre) throws DAOException {
        StatusPago status = null;
        EntityManager em = null;
        try {
            em = super.getEntityManager();
            status = em.createQuery("StatusPago.findByNombre", StatusPago.class).setParameter("nombre", nombre)
                    .getSingleResult();
            return status;
        } catch (Exception ex) {
            log.error("Error al obtener el status del pago con el nombre {}. {}", nombre, ex);
            throw new DAOException("Hubo un problema al obtener el status de pago por nombre.");
        } finally {
            super.close(em);
        }
    }

    public List<StatusPago> buscarStatusPago(boolean activo) throws DAOException {
        List<StatusPago> lista = null;
        EntityManager em = null;
        try {
            em = super.getEntityManager();
            if (activo) {
                lista = em.createQuery("StatusPago.findAllActivos", StatusPago.class).getResultList();
            } else {
                lista = em.createQuery("StatusPago.findAllNoActivos", StatusPago.class).getResultList();
            }
            return lista;
        } catch (Exception ex) {
            String estado = (activo) ? "activos" : "no activos";
            log.error("Error al buscar los status de pago {}. {}", estado, ex);
            throw new DAOException("Hubo un problema al buscar los status de pago " + estado);
        } finally {
            super.close(em);
        }
    }
}
