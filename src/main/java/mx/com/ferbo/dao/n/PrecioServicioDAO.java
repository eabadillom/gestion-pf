package mx.com.ferbo.dao.n;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.Aviso;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.PrecioServicio;
import mx.com.ferbo.util.DAOException;

@Named
@ApplicationScoped
public class PrecioServicioDAO extends BaseDAO<PrecioServicio, Integer> {

    private static Logger log = LogManager.getLogger(PrecioServicioDAO.class);

    public PrecioServicioDAO() {
        super(PrecioServicio.class);
    }

    public List<PrecioServicio> buscarPorClienteSinAviso(Integer cteCve) throws DAOException {
        EntityManager em = null;
        List<PrecioServicio> lista = null;
        try {
            em = super.getEntityManager();
            lista = em.createNamedQuery("PrecioServicio.findByClienteSinAviso", PrecioServicio.class)
                    .setParameter("cteCve", cteCve)
                    .getResultList();
            return lista;
        } catch (Exception ex) {
            log.error("Error al obtener la lista de precios de servicio sin aviso del cliente. {}", ex);
            throw new DAOException("Hubo un problema al obtener los precios servicio sin aviso del cliente con id: " + cteCve);
        } finally {
            super.close(em);
        }
    }

    public List<PrecioServicio> buscarPorClienteConAviso(Integer cteCve, Integer avisoCve) throws DAOException {
        EntityManager em = null;
        List<PrecioServicio> lista = null;
        try {
            em = super.getEntityManager();
            lista = em.createNamedQuery("PrecioServicio.findByClienteAviso", PrecioServicio.class)
                    .setParameter("cteCve", cteCve)
                    .setParameter("avisoCve", avisoCve)
                    .getResultList();
            return lista;
        } catch (Exception ex) {
            log.error("Error al obtener la lista de precios de servicio con aviso del cliente. {}", ex);
            throw new DAOException("Hubo un problema al obtener los precios servicio con aviso del cliente con id: " + cteCve);
        } finally {
            super.close(em);
        }
    }
}
