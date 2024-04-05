package mx.com.ferbo.dao;

import java.util.List;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.UnidadDeManejo;
import mx.com.ferbo.util.EntityManagerUtil;

public class UnidadDeManejoDAO extends IBaseDAO<UnidadDeManejo, Integer> {
	private static Logger log = LogManager.getLogger(UnidadDeManejoDAO.class);

	@Override
	public UnidadDeManejo buscarPorId(Integer id) {
		UnidadDeManejo unidad = null;
		;
		EntityManager em = null;

		try {
			em = EntityManagerUtil.getEntityManager();
			unidad = em.find(UnidadDeManejo.class, id);

		} catch (Exception ex) {
			log.error("Problema para obtener la unidad de manejo...", ex);
		} finally {
			EntityManagerUtil.close(em);
		}
		return unidad;
	}

	@Override
	public List<UnidadDeManejo> buscarTodos() {
		List<UnidadDeManejo> listado = null;
		EntityManager em = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			listado = em.createNamedQuery("UnidadDeManejo.findAll", UnidadDeManejo.class).getResultList();
		} catch (Exception ex) {
			log.error("Problema para obtener el listado de unidades de manejo...", ex);
		} finally {
			EntityManagerUtil.close(em);
		}

		return listado;
	}

	@Override
	public List<UnidadDeManejo> buscarPorCriterios(UnidadDeManejo e) {
		return null;
	}

	@Override
	public String actualizar(UnidadDeManejo unidadManejo) {

		EntityManager em = null;

		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.merge(unidadManejo);
			em.getTransaction().commit();
		} catch (Exception e) {
			log.error("Problema al actualizar la unidad de manejo", e);
		} finally {
			EntityManagerUtil.close(em);
		}
		return null;
	}

	@Override
	public String guardar(UnidadDeManejo unidadManejo) {

		EntityManager em = null;

		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.persist(unidadManejo);
			em.getTransaction().commit();
		} catch (Exception e) {
			log.error("Problema para guardar unidad de manejo", e);
		} finally {
			EntityManagerUtil.close(em);
		}
		return null;
	}

	@Override
	public String eliminar(UnidadDeManejo unidadManejo) {

		EntityManager em = null;

		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.remove(em.merge(unidadManejo));
			em.getTransaction().commit();
		} catch (Exception e) {
			log.error("Problema para eliminar unidad de manejo", e);
		} finally {
			EntityManagerUtil.close(em);
		}
		return null;
	}

	@Override
	public String eliminarListado(List<UnidadDeManejo> listado) {
		return null;
	}
}
