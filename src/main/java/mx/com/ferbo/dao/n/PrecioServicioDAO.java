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
    
    public List<PrecioServicio> buscarServiciosPorCliente(Cliente cliente) throws DAOException {
        List<PrecioServicio> lista = null;
        EntityManager em = null;
        
        try {
            em = super.getEntityManager();
            lista = em.createNamedQuery("PrecioServicio.findByCliente", PrecioServicio.class)
                    .setParameter("cteCve", cliente.getCteCve())
                    .getResultList()
                    ;
        } catch (Exception ex) {
            log.error("Error al obtener la lista de PrecioServicio del cliente: {}",
                    cliente.toString(), ex);
            throw new DAOException("Error al consultar los precios de los servicios para el cliente", ex);
        } finally {
            super.close(em);
        }
        
        return lista;
    }

    public List<PrecioServicio> buscarPorClienteServicio(PrecioServicio ps) throws DAOException {
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

    public List<PrecioServicio> buscarPorClienteAviso(PrecioServicio ps) throws DAOException {
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

    public List<PrecioServicio> buscarPorCliente(PrecioServicio ps) throws DAOException {
        List<PrecioServicio> list = null;
        EntityManager em = null;
        try {
            em = super.getEntityManager();
            list = em.createNamedQuery("PrecioServicio.findByCliente", PrecioServicio.class)
                    .setParameter("cteCve", ps.getCliente().getCteCve()).getResultList();
        } catch (Exception ex) {
            log.error("Problema para obtener la lista de precio servicio...", ex);
            throw new DAOException("Hubo un problema al obtener los servicios del cliente");
        } finally {
            super.close(em);
        }

        return list;
    }

    public List<PrecioServicio> buscarPorCliente(Integer cteCve, Boolean isFullInfo) throws DAOException {
		List<PrecioServicio> list = null;
		EntityManager em = null;
		try {
			em = super.getEntityManager();
			list = em.createNamedQuery("PrecioServicio.findByCliente", PrecioServicio.class)
			.setParameter("cteCve", cteCve)
			.getResultList()
			;
			if(isFullInfo == false)
				return list;
			for(PrecioServicio ps : list) {
				log.debug(ps.getCliente().getCteCve());
				log.debug(ps.getServicio().getServicioCve());
				log.debug(ps.getUnidad().getUnidadDeManejoCve());
				log.debug(ps.getAvisoCve().getAvisoCve());
			}
		} catch(Exception ex) {
			log.error("Problema para obtener el listado de precios...", ex);
            throw new DAOException("Problema al obtener la lista de precios de los servicios del cliente");
		} finally {
			super.close(em);
		}
		return list;
	}

    public List<PrecioServicio> buscarDisponibles(Cliente cliente, Aviso aviso) throws DAOException {
		List<PrecioServicio> lista = null;
		EntityManager em = null;
		try {
			em = super.getEntityManager();
            lista = em.createNamedQuery("PrecioServicio.findAllByClienteAviso", PrecioServicio.class)
					.setParameter("idCliente", cliente.getCteCve())
					.setParameter("avisoCve", aviso.getAvisoCve())
                    .getResultList()
					;
		} catch(Exception ex) {
			log.error("Problema para obtener los precios de servcio relacionados con el aviso " + aviso + " del cliente: " + cliente, ex);
            throw new DAOException("No se encontraron precios de servicios relacionados con el aviso y cliente seleccionados");
		} finally {
			super.close(em);
		}
		return lista;
	}
}
