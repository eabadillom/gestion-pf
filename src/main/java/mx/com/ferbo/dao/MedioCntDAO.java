package mx.com.ferbo.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.MedioContacto;
import mx.com.ferbo.util.EntityManagerUtil;

public class MedioCntDAO extends IBaseDAO<MedioContacto, Integer> {
	private static Logger log = LogManager.getLogger(MedioCntDAO.class);

	@Override
	public MedioContacto buscarPorId(Integer id) {
		MedioContacto medioContacto = null;
		EntityManager em = null;
		Query query = null;

		try {
			em = EntityManagerUtil.getEntityManager();
			query = em.createNamedQuery("MedioCnt.findByIdMedio", MedioContacto.class).setParameter("idMedio", id);

			medioContacto = (MedioContacto) query.getSingleResult();
		} finally {
			EntityManagerUtil.close(em);
		}

		return medioContacto;
	}

	@Override
	public List<MedioContacto> buscarTodos() {
		return null;

	}

	@Override
	public List<MedioContacto> buscarPorCriterios(MedioContacto e) {

		EntityManager em = null;
		List<MedioContacto> lista = null;

		try {
			em = EntityManagerUtil.getEntityManager();
			lista = em.createNamedQuery("MedioCnt.findByIdContacto", MedioContacto.class)
					.setParameter("idContacto", e.getContacto().getId()).getResultList();
		} catch (Exception e2) {
			log.error("Problema al encontrar registros", e2);
		} finally {
			EntityManagerUtil.close(em);
		}

		return lista;
	}

	@Override
	public String actualizar(MedioContacto medio) {
		EntityManager em = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.merge(medio);
			em.getTransaction().commit();
		} catch (Exception ex) {
			log.error("Problema para actualizar el medio de contacto...", ex);
			return ex.getMessage();
		} finally {
			EntityManagerUtil.close(em);
		}
		return null;
	}

	@Override
	public String guardar(MedioContacto medio) {
		EntityManager em = null;

		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.persist(medio);
			em.getTransaction().commit();
		} catch (Exception ex) {
			log.error("Problema para guardar el medio de contacto...");
			return ex.getMessage();
		} finally {
			EntityManagerUtil.close(em);
		}

		return null;
	}

	@Override
	public String eliminar(MedioContacto medio) {
		EntityManager em = EntityManagerUtil.getEntityManager();
		try {
			em.getTransaction().begin();
			medio = em.merge(medio);
			em.remove(medio);
			em.getTransaction().commit();
		} catch (Exception e) {
			log.error("Error al eliminar el medio de contacto...", e);
			return "ERROR";
		} finally {
			EntityManagerUtil.close(em);
		}
		return null;
	}

	@Override
	public String eliminarListado(List<MedioContacto> listado) {
		return null;
	}

	public String guardaMedioCnt(MedioContacto medio) {
		EntityManager em = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			if (medio.getTipoMedio().equalsIgnoreCase("m")) {
				em.persist(medio.getMail());
				medio.setTelefono(null);
			} else {
				em.persist(medio.getTelefono());
				medio.setMail(null);
			}
			em.persist(medio);
			em.getTransaction().commit();
		} catch (Exception e) {
			log.error("Error al guardar el medio de contacto", e);
			return "ERROR";
		} finally {
			EntityManagerUtil.close(em);
		}
		return null;
	}

}
