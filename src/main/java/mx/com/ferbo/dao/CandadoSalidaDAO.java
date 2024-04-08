package mx.com.ferbo.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.CandadoSalida;
import mx.com.ferbo.util.EntityManagerUtil;

public class CandadoSalidaDAO extends IBaseDAO<CandadoSalida, Integer> {
	private static Logger log = LogManager.getLogger(CandadoSalidaDAO.class);

	@Override
	public CandadoSalida buscarPorId(Integer id) {
		CandadoSalida candado = null;
		EntityManager em = null;

		try {
			em = EntityManagerUtil.getEntityManager();
			candado = em.find(CandadoSalida.class, id);
		} catch (Exception ex) {
			log.error("Problema para obtener el candado de salida...", ex);
		} finally {
			EntityManagerUtil.close(em);
		}

		return candado;
	}

	public CandadoSalida buscarPorCliente(Integer idCliente) {
		CandadoSalida candado = null;
		EntityManager em = null;

		try {
			em = EntityManagerUtil.getEntityManager();
			candado = em.createNamedQuery("CandadoSalida.findByCliente", CandadoSalida.class)
					.setParameter("idCliente", idCliente).getSingleResult();

		} catch (Exception ex) {
			log.error("Problema para obtener el candado de salida...", ex);
		} finally {
			EntityManagerUtil.close(em);
		}

		return candado;
	}

	@Override
	public List<CandadoSalida> buscarTodos() {
		//  Auto-generated method stub
		return null;
	}

	@Override
	public List<CandadoSalida> buscarPorCriterios(CandadoSalida e) {
		//  Auto-generated method stub
		return null;
	}

	@Override
	public String actualizar(CandadoSalida e) {
		EntityManager em = null;

		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			e = em.merge(e);
			em.getTransaction().commit();
		} catch (Exception ex) {
			log.error("Problema para actualizar el candado de salida...", ex);
			EntityManagerUtil.rollback(em);
			return ex.getMessage();
		} finally {
			EntityManagerUtil.close(em);
		}

		return null;
	}

	@Override
	public String guardar(CandadoSalida e) {
		EntityManager em = null;

		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.persist(e);
			em.getTransaction().commit();
		} catch (Exception ex) {
			log.error("Problema para guardar el candado de salida...", ex);
			EntityManagerUtil.rollback(em);
			return ex.getMessage();
		} finally {
			EntityManagerUtil.close(em);
		}

		return null;
	}

	@Override
	public String eliminar(CandadoSalida e) {
		//  Auto-generated method stub
		return null;
	}

	@Override
	public String eliminarListado(List<CandadoSalida> listado) {
		//  Auto-generated method stub
		return null;
	};

	@SuppressWarnings("unchecked")
	public List<CandadoSalida> findAll() {
		EntityManager entity = EntityManagerUtil.getEntityManager();
		List<CandadoSalida> list = null;
		Query sql = entity.createNamedQuery("CandadoSalida.findAll", CandadoSalida.class);
		list = sql.getResultList();

		for (CandadoSalida c : list) {
			log.debug(c.toString());
		}

		return list;
	};

	public String save(CandadoSalida candado) {

		EntityManager em = null;

		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.persist(candado);
			em.getTransaction().commit();

		} catch (Exception e) {
			return "Error" + e.getMessage();
		} finally {
			EntityManagerUtil.close(em);
		}

		return null;
	}

	public String update(CandadoSalida cS) {
		try {
			EntityManager entity = EntityManagerUtil.getEntityManager();
			entity.getTransaction().begin();
			entity.merge(cS);
			entity.getTransaction().commit();
			entity.close();
		} catch (Exception e) {
			return "Failed!! " + e.getMessage();
		}
		return null;
	}

}
