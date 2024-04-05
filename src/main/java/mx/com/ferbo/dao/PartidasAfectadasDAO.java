package mx.com.ferbo.dao;

import static mx.com.ferbo.util.EntityManagerUtil.getEntityManager;

import java.util.List;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.PartidasAfectadas;
import mx.com.ferbo.util.EntityManagerUtil;

public class PartidasAfectadasDAO extends IBaseDAO<PartidasAfectadas, Integer> {

	private static Logger log = LogManager.getLogger(PartidasAfectadasDAO.class);

	@Override
	public PartidasAfectadas buscarPorId(Integer id) {

		EntityManager em = null;
		PartidasAfectadas pa = null;

		try {
			em = EntityManagerUtil.getEntityManager();
			pa = em.createNamedQuery("findByID", PartidasAfectadas.class).getSingleResult();
		} catch (Exception e) {
			log.error("Problema al buscar por id", e);
		} finally {
			EntityManagerUtil.close(em);
		}

		return pa;
	}

	@Override
	public List<PartidasAfectadas> buscarTodos() {

		EntityManager em = null;
		List<PartidasAfectadas> listapa = null;

		try {
			em = EntityManagerUtil.getEntityManager();
			listapa = em.createNamedQuery("", PartidasAfectadas.class).getResultList();
		} catch (Exception e) {
			log.error("Problema al obtener list de partidas afectadas", e);
		} finally {
			EntityManagerUtil.close(em);
		}

		return listapa;
	}

	@Override
	public List<PartidasAfectadas> buscarPorCriterios(PartidasAfectadas e) {
		return null;
	}

	@Override
	public String actualizar(PartidasAfectadas p) {
		try {
			EntityManager entity = getEntityManager();
			entity.getTransaction().begin();
			entity.merge(p);
			entity.getTransaction().commit();
			entity.close();
			System.out.println(p);
		} catch (Exception e) {
			return "Failed!" + e.getMessage();
		}
		return null;
	}

	@Override
	public String guardar(PartidasAfectadas p) {
		try {
			EntityManager entity = getEntityManager();
			entity.getTransaction().begin();
			entity.persist(p);
			entity.getTransaction().commit();
			entity.close();
		} catch (Exception e) {
			return "Failed!! " + e.getMessage();
		}
		return null;
	}

	@Override
	public String eliminar(PartidasAfectadas p) {
		try {
			EntityManager entity = getEntityManager();
			entity.getTransaction().begin();
			entity.remove(entity.merge(p));
			entity.getTransaction().commit();
			entity.close();
		} catch (Exception e) {
			return "Failed" + e.getMessage();
		}
		return null;
	}

	@Override
	public String eliminarListado(List<PartidasAfectadas> listado) {

		return null;
	}

}
