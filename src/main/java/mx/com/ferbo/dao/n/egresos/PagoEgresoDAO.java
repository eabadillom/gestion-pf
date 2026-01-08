package mx.com.ferbo.dao.n.egresos;

import java.util.Date;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.n.egresos.PagoEgreso;
import mx.com.ferbo.util.DAOException;

@Named
@ApplicationScoped
public class PagoEgresoDAO extends BaseDAO<PagoEgreso, Integer> {

    private static final Logger log = LogManager.getLogger(PagoEgresoDAO.class);

    public PagoEgresoDAO() {
        super(PagoEgreso.class);
    }

    public List<PagoEgreso> buscarTodosPorImporteEgreso(Integer id) throws DAOException {
        List<PagoEgreso> lista = null;
        EntityManager em = null;
        try {
            em = super.getEntityManager();
            lista = em.createQuery("PagoEgreso.findAllByImporteEgreso", PagoEgreso.class).setParameter("id", id)
                    .getResultList();
            return lista;
        } catch (Exception ex) {
            log.error("Error al buscar los pagos asociados al importe de egreso con id {}. {}", id, ex);
            throw new DAOException("Hubo un problema al buscar los pagos asociados al importe de egreso por id.");
        } finally {
            super.close(em);
        }
    }

    public List<PagoEgreso> buscarTodosPorStatusEImporteEgreso(String status, Integer id) throws DAOException {
        List<PagoEgreso> lista = null;
        EntityManager em = null;
        try {
            em = super.getEntityManager();
            lista = em.createQuery("PagoEgreso.findAllByStatusEImporteEgreso", PagoEgreso.class)
                    .setParameter("status", status).setParameter("id", id).getResultList();
            return lista;
        } catch (Exception ex) {
            log.error("Error al buscar todos los pagos con el status {} y el id de importe de egreso {}. {}", status,
                    id, ex);
            throw new DAOException(
                    "Hubo un problema al obtener los pagos en base al status y el id de importe de egreso dados.");
        } finally {
            super.close(em);
        }
    }

    public List<PagoEgreso> buscarTodosPorMesYStatus(Date inicio, Date fin, String status) throws DAOException {
        List<PagoEgreso> lista = null;
        EntityManager em = null;
        try {
            em = super.getEntityManager();
            lista = em.createQuery("PagoEgreso.findAllbyMesYStatus", PagoEgreso.class).setParameter("status", status)
                    .setParameter("inicio", inicio).setParameter("fin", fin).getResultList();
            return lista;
        } catch (Exception ex) {
            log.error("Error al buscar todos los pagos en base al status {} y el perido desde {} hasta {}. {}", status, inicio, fin, ex);
            throw new DAOException("Hubo un problema al obtener todos los pagos en base al status y el periodo dado.");
        } finally {
            super.close(em);
        }
    }

    public List<PagoEgreso> buscarTodosPorFecheLimiteEImporteEgreso(Date hoy, Integer id) throws DAOException {
        List<PagoEgreso> lista = null;
        EntityManager em = null;
        try {
            em = super.getEntityManager();
            lista = em.createQuery("PagoEgreso.findAllByFechaLimiteEImporte", PagoEgreso.class).setParameter("hoy", hoy).setParameter("id", id).getResultList();
            return lista;
        } catch (Exception ex) {
            log.error("Error al buscar los pagos anteriores o iguales hoy {} del importe de egreso con id {}. {}", hoy, id, ex);
            throw new DAOException("Hubo un problema al buscar los pagos anteriores o iguales al dia de hoy de un determinado importe de egreso.");
        } finally {
            super.close(em);
        }
    }
}
