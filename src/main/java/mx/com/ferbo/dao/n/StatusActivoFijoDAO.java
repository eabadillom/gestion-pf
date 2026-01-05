package mx.com.ferbo.dao.n;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.StatusActivoFijo;
import mx.com.ferbo.util.DAOException;

@Named
@ApplicationScoped
public class StatusActivoFijoDAO extends BaseDAO <StatusActivoFijo, Integer> {

    private static final Logger log = LogManager.getLogger(StatusActivoFijoDAO.class);

    public StatusActivoFijoDAO(){
        super(StatusActivoFijo.class);
    }

    public StatusActivoFijo buscarPorNombre(String nombre) throws DAOException{
        StatusActivoFijo status = null;
        EntityManager em = null;
        try {
            em = super.getEntityManager();
            status = em.createQuery("StatusActivoFijo.findByNombre", StatusActivoFijo.class).setParameter("nombre", nombre).getSingleResult();
            return status;
        } catch (Exception ex) {
            log.error("Error al buscar el status del acitvo fijo con el nombre {}. {}");
            throw new DAOException("Hubo un problema al buscar el status del activo fijo por nombre");
        } finally {
            super.close(em);
        }
    }

    public List<StatusActivoFijo> buscarActivos(boolean acitvo) throws DAOException{
        List<StatusActivoFijo> lista = null;
        EntityManager em = null;
        try {
            em = super.getEntityManager();
            if (acitvo) {
                lista = em.createQuery("StatusActivoFijo.findAllActivos", StatusActivoFijo.class).getResultList();
            } else {
                lista = em.createQuery("StatusActivoFijo.findAllNoActivos", StatusActivoFijo.class).getResultList();
            }
            return lista;
        } catch (Exception ex) {
            log.error("Error al buscar la lista de status de activo fijo. {}");
            throw new DAOException("Hubo un problema al buscar la lista de status de activo fijo.");
        } finally {
            super.close(em);
        }
    }
}
