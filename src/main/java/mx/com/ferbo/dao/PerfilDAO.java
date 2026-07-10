package mx.com.ferbo.dao;

import static mx.com.ferbo.util.EntityManagerUtil.getEntityManager;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.Perfil;
import mx.com.ferbo.util.EntityManagerUtil;

public class PerfilDAO extends IBaseDAO<Perfil, Integer> {

	@SuppressWarnings("unchecked")
	public List<Perfil> findall() {
		EntityManager entity = null;
		List<Perfil> perfil = null;
		try {
			entity = EntityManagerUtil.getEntityManager();
			Query sql = entity.createNamedQuery("Perfil.findAll", Perfil.class);
			perfil = sql.getResultList();
		} catch (Exception ex) {
			System.err.println("Error: " + ex.getMessage());
		} finally {
			EntityManagerUtil.close(entity);
		}
		return perfil;
	}

	@Override
	public Perfil buscarPorId(Integer id) {
		EntityManager entity = null;
		Perfil perfil = null;
		try {
			entity = EntityManagerUtil.getEntityManager();
			Query sql = entity.createNamedQuery("Perfil.findById", Perfil.class).setParameter("id", id);
			perfil = (Perfil) sql.getSingleResult();
		} catch (Exception ex) {
			System.err.println("Error: " + ex.getMessage());
		} finally {
			EntityManagerUtil.close(entity);
		}

		return perfil;
	}

	@Override
	public List<Perfil> buscarTodos() {
		List<Perfil> perfil = null;
		EntityManager em = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			perfil = em.createNamedQuery("Perfil.findAll", Perfil.class).getResultList();
		} catch (Exception ex) {
			System.err.println("Error: " + ex.getMessage());
		} finally {
			EntityManagerUtil.close(em);
		}
		return perfil;
	}

	@Override
	public List<Perfil> buscarPorCriterios(Perfil e) {
		return null;
	}

	@Override
	public String actualizar(Perfil perfil) {
		EntityManager em = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.merge(perfil);
			em.getTransaction().commit();
		} catch (Exception e) {
			System.out.println("ERROR actualizando Países" + e.getMessage());
			return "ERROR";
		} finally {
			EntityManagerUtil.close(em);
		}
		return null;
	}

	@Override
	public String guardar(Perfil perfil) {
		EntityManager em = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.persist(perfil);
			em.getTransaction().commit();
		} catch (Exception e) {
			System.out.println("ERROR guardando Perfil" + e.getMessage());
			return "ERROR";
		} finally {
			EntityManagerUtil.close(em);
		}
		return null;
	}

	@Override
	public String eliminar(Perfil perfil) {
		EntityManager em = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.remove(em.merge(perfil));
			em.getTransaction().commit();
		} catch (Exception e) {
			System.out.println("ERROR" + e.getMessage());
			return "ERROR";
		} finally {
			EntityManagerUtil.close(em);
		}
		return null;
	}

	@Override
	public String eliminarListado(List<Perfil> listado) {
		return null;
	}

	public List<Perfil> buscaPorId(Integer id) {
		EntityManager em = null;
		List<Perfil> lst = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			lst = em.createNamedQuery("Paises.findById", Perfil.class).setParameter("paisCve", id).getResultList();
		} catch (Exception ex) {
			System.err.println("Error: " + ex.getMessage());
		} finally {
			EntityManagerUtil.close(em);
		}
		return lst;
	}

	@SuppressWarnings("unchecked")
	public List<Perfil> getPerfil() {
		EntityManager entity = null;
		List<Perfil> perfil = null;
		try {
			entity = getEntityManager();
			Query sql = entity.createQuery("SELECT u FROM Perfil u WHERE u.nombre IN (1, 2)");
			perfil = sql.getResultList();
		} catch (Exception ex) {
			System.err.println("Errer: " + ex.getMessage());
		} finally {
			EntityManagerUtil.close(entity);
		}
		return perfil;
	}

}
