package mx.com.ferbo.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.RegimenFiscal;
import mx.com.ferbo.util.EntityManagerUtil;

public class RegimenFiscalDAO extends IBaseDAO<RegimenFiscal, String> {

	private static Logger log = LogManager.getLogger(RegimenFiscal.class);

	@SuppressWarnings("unchecked")
	public List<RegimenFiscal> findAll() {

		EntityManager entity = null;
		List<RegimenFiscal> listaRegimen = null;

		try {

			entity = EntityManagerUtil.getEntityManager();
			Query sql = (Query) entity.createNamedQuery("RegimenFiscal.findAll", RegimenFiscal.class);
			listaRegimen = (List<RegimenFiscal>) sql.getResultList();

		} catch (Exception ex) {
			log.error("Problema para traer lista sregimen", ex);
		} finally {
			EntityManagerUtil.close(entity);
		}

		return listaRegimen;
	}

	@Override
	public RegimenFiscal buscarPorId(String cd_regimen) {

		EntityManager entity = null;
		RegimenFiscal rf = null;

		try {
			entity = EntityManagerUtil.getEntityManager();
			rf = entity.createNamedQuery("RegimenFiscal.findBycdRegimen", RegimenFiscal.class)
					.setParameter("cd_regimen", cd_regimen).getSingleResult();
		} catch (Exception e) {
			log.error("Problema para encontrar regimen por id", e);
		} finally {
			EntityManagerUtil.close(entity);
		}

		return rf;
	}

	public List<RegimenFiscal> buscarPorPersonaFisica() {

		EntityManager em = null;
		List<RegimenFiscal> listaRegimen = null;

		try {
			em = EntityManagerUtil.getEntityManager();
			listaRegimen = em.createNamedQuery("RegimenFiscal.findByst_per_fisica", RegimenFiscal.class)
					.getResultList();
		} catch (Exception ex) {
			log.error("problema para buscar por persona fisica", ex);
		} finally {
			EntityManagerUtil.close(em);
		}

		return listaRegimen;
	}

	public List<RegimenFiscal> buscarPorPersonaMoral() {

		EntityManager em = null;
		List<RegimenFiscal> listaRegimen = null;

		try {
			em = EntityManagerUtil.getEntityManager();
			listaRegimen = em.createNamedQuery("RegimenFiscal.findByst_per_moral", RegimenFiscal.class).getResultList();
		} catch (Exception e) {
			log.error("Problema para buscar por persona moral", e);
		} finally {
			EntityManagerUtil.close(em);
		}

		return listaRegimen;
	}

	@Override
	public List<RegimenFiscal> buscarTodos() {

		EntityManager em = null;
		List<RegimenFiscal> listaRegimen = null;

		try {
			em = EntityManagerUtil.getEntityManager();
			listaRegimen = em.createNamedQuery("RegimenFiscal.findAll", RegimenFiscal.class).getResultList();
		} catch (Exception e) {
			log.error("Problema para buscar regimenes", e);
		} finally {
			EntityManagerUtil.close(em);
		}

		return listaRegimen;
	}

	@Override
	public List<RegimenFiscal> buscarPorCriterios(RegimenFiscal r) {

		EntityManager em = null;
		List<RegimenFiscal> listaRegimen = null;

		try {
			em = EntityManagerUtil.getEntityManager();
			listaRegimen = em.createNamedQuery("RegimenFiscal.findBycdRegimen", RegimenFiscal.class)
					.setParameter("cd_regimen", r.getCd_regimen()).getResultList();
		} catch (Exception e) {
			log.error("Problema para buscar por criterios", e);
		} finally {
			EntityManagerUtil.close(em);
		}

		return listaRegimen;
	}

	@Override
	public String actualizar(RegimenFiscal r) {

		EntityManager em = null;

		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.merge(r);
			em.getTransaction().commit();
		} catch (Exception e) {
			log.error("Problema al actualizar", e);
		} finally {
			EntityManagerUtil.close(em);
		}
		return null;
	}

	@Override
	public String guardar(RegimenFiscal r) {

		EntityManager em = null;

		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.persist(r);
			em.getTransaction().commit();
		} catch (Exception e) {
			log.error("Problema al guardar", e);
		} finally {
			EntityManagerUtil.close(em);
		}
		return null;
	}

	@Override
	public String eliminar(RegimenFiscal r) {

		EntityManager em = null;

		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.remove(em.merge(r));
			em.getTransaction().commit();
		} catch (Exception e) {
			log.error("Problema para eliminar regimen", e);
		} finally {
			EntityManagerUtil.close(em);
		}
		return null;
	}

	@Override
	public String eliminarListado(List<RegimenFiscal> listado) {

		EntityManager em = null;

		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			for (RegimenFiscal rf : listado) {
				em.remove(em.merge(rf));
			}
			em.getTransaction().commit();
		} catch (Exception e) {
			log.error("Problema al eliminar listado", e);
		} finally {
			EntityManagerUtil.close(em);
		}
		return null;
	}

}
