package mx.com.ferbo.dao.n;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.PrecioServicio;
import mx.com.ferbo.util.DAOException;

@Named
@ApplicationScoped
public class PrecioServicioDAO extends BaseDAO<PrecioServicio, Integer> {

    private static Logger log = LogManager.getLogger(PrecioServicioDAO.class);

    public PrecioServicioDAO() {
        super(PrecioServicio.class);
    }

    private List<PrecioServicio> buscarPorClienteServicio(PrecioServicio ps) throws DAOException {
        List<PrecioServicio> lista = null;
        EntityManager em = null;

        try {
            em = super.getEntityManager();
            lista = em.createNamedQuery("PrecioServicio.findByClienteServicio", PrecioServicio.class)
                    .setParameter("cteCve", ps.getCliente().getCteCve())
                    .setParameter("servicioCve", ps.getServicio())
                    .getResultList();
        } catch (Exception ex) {
            log.error("Error al obtener la lista de PrecioServicio (cteCve={}, servicio={})",
                    ps.getCliente().getCteCve(), ps.getServicio(), ex);
            throw new DAOException("Error al consultar los precios de los servicios para el cliente", ex);
        } finally {
            super.close(em);
        }
        return lista;
    }

    private List<PrecioServicio> buscarPorClienteAviso(PrecioServicio ps) throws DAOException {
        List<PrecioServicio> lista = null;
        EntityManager em = null;
        try {
            em = super.getEntityManager();
            lista = em.createNamedQuery("PrecioServicio.findByClienteAviso", PrecioServicio.class)
                    .setParameter("cteCve", ps.getCliente().getCteCve())
                    .setParameter("avisoCve", ps.getAvisoCve().getAvisoCve())
                    .getResultList();
        } catch (Exception ex) {
            log.error("Problema para obtener el listado de precio servicio...", ex);
            throw new DAOException("Error al consultar los precios de los servicios para el cliente", ex);
        } finally {
            super.close(em);
        }
        return lista;
    }

    private List<PrecioServicio> buscarPorCliente(PrecioServicio ps) {
        List<PrecioServicio> list = null;
        EntityManager em = null;
        try {
            em = super.getEntityManager();
            list = em.createNamedQuery("PrecioServicio.findByCliente", PrecioServicio.class)
                    .setParameter("cteCve", ps.getCliente().getCteCve()).getResultList();
        } catch (Exception ex) {
            log.error("Problema para obtener la lista de precio servicio...", ex);
        } finally {
            super.close(em);
        }

        return list;
    }
}