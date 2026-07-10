package mx.com.ferbo.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.MetodoPago;
import mx.com.ferbo.util.EntityManagerUtil;

public class MetodoPagoDAO extends IBaseDAO<MetodoPago, String> {
	private static Logger log = LogManager.getLogger(MetodoPagoDAO.class);

	@Override
	public MetodoPago buscarPorId(String id) {
		return null;
	}

	public MetodoPago buscarPorMetodoPago(String metodoPago) {
            EntityManager entity = null;
            MetodoPago metodoP = null;
            try {
                entity = EntityManagerUtil.getEntityManager();
		metodoP = entity.createNamedQuery("MetodoPago.findByCdMetodoPago", MetodoPago.class)
				.setParameter("cdMetodoPago", metodoPago).getSingleResult();
            } catch (Exception ex) {
                log.error("Problema para obtener información del catálogo metodo_pago", ex);
            } finally {
                EntityManagerUtil.close(entity);
            }
            return metodoP;
	}

	@Override
	public List<MetodoPago> buscarTodos() {
            EntityManager em = null;
            List<MetodoPago> lista = null;
            try {
                em = EntityManagerUtil.getEntityManager();
		lista = em.createNamedQuery("MetodoPago.findAll", MetodoPago.class).getResultList();
            } catch (Exception ex) {
                log.error("Problema para obtener información del catálogo metodo_pago", ex);
            } finally {
                EntityManagerUtil.close(em);
            }
            return lista;
	}

	@SuppressWarnings("unchecked")
	public List<MetodoPago> buscarVigentes(Date fecha) {
		List<MetodoPago> list = null;
		EntityManager em = null;
		Query query = null;

		try {
			em = EntityManagerUtil.getEntityManager();
			query = em.createNamedQuery("MetodoPago.buscarVigentes", MetodoPago.class).setParameter("fecha", fecha);
			list = query.getResultList();
		} catch (Exception ex) {
			log.error("Problema para obtener información del catálogo metodo_pago", ex);
		} finally {
			EntityManagerUtil.close(em);
		}
		return list;
	}

	@Override
	public List<MetodoPago> buscarPorCriterios(MetodoPago e) {
		return null;
	}

	@Override
	public String actualizar(MetodoPago e) {
		return null;
	}

	@Override
	public String guardar(MetodoPago e) {
		return null;
	}

	@Override
	public String eliminar(MetodoPago e) {
		return null;
	}

	@Override
	public String eliminarListado(List<MetodoPago> listado) {
		return null;
	}

}
