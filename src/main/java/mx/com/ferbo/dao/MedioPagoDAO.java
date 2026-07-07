package mx.com.ferbo.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.MedioPago;
import mx.com.ferbo.util.EntityManagerUtil;

public class MedioPagoDAO extends IBaseDAO<MedioPago, Integer> {
	private static Logger log = LogManager.getLogger(MedioPagoDAO.class);

	@Override
	public MedioPago buscarPorId(Integer id) {
		EntityManager em = null;
		Query query = null;
		MedioPago mp = null;

		try {
			mp = new MedioPago();
			em = EntityManagerUtil.getEntityManager();
			query = em.createNamedQuery("MedioPago.findByMpId", MedioPago.class)
                            .setParameter("mpId", id);

			mp = (MedioPago) query.getSingleResult();

		} catch (Exception e) {
			log.info("Error al buscar medio Pago por id", e.getMessage());
		} finally {
			EntityManagerUtil.close(em);
		}

		return mp;
	}

	public MedioPago buscarPorFormaPago(String formaPago) {
            EntityManager entity = null;
            MedioPago mp = null;
            try {
                entity = EntityManagerUtil.getEntityManager();
                mp = entity.createNamedQuery("MedioPago.findBympformaPago", MedioPago.class)
                    .setParameter("mpformaPago", formaPago)
                    .getSingleResult();
            } catch (Exception ex) {
                log.error("Problema para leer el catálogo de medios de pago...", ex);
            } finally {
                    EntityManagerUtil.close(entity);
            }
            return mp;
	}

	@Override
	public List<MedioPago> buscarTodos() {
            EntityManager em = null;
            List<MedioPago> lista = null;
            try {
                em = EntityManagerUtil.getEntityManager();
                lista = em.createNamedQuery("MedioPago.findAll", MedioPago.class).getResultList();
            } catch (Exception ex) {
                log.error("Problema para leer el catálogo de medios de pago...", ex);
            } finally {
                EntityManagerUtil.close(em);
            }    
            return lista;
	}

	@Override
	public List<MedioPago> buscarPorCriterios(MedioPago e) {
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<MedioPago> buscarVigentes(Date fecha) {
		List<MedioPago> list = null;
		EntityManager em = null;
		Query query = null;

		try {
			em = EntityManagerUtil.getEntityManager();
			query = em.createNamedQuery("MedioPago.findVigentes", MedioPago.class).setParameter("fecha", fecha);
			list = query.getResultList();
		} catch (Exception ex) {
			log.error("Problema para leer el catálogo de medios de pago...", ex);
		} finally {
			EntityManagerUtil.close(em);
		}

		return list;
	}

	@Override
	public String actualizar(MedioPago e) {
		return null;
	}

	@Override
	public String guardar(MedioPago e) {
		return null;
	}

	@Override
	public String eliminar(MedioPago e) {
		return null;
	}

	@Override
	public String eliminarListado(List<MedioPago> listado) {
		return null;
	}

}
