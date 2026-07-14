package mx.com.ferbo.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.Parametro;
import mx.com.ferbo.util.EntityManagerUtil;

public class ParametroDAO extends IBaseDAO<Parametro, Integer> {

	@Override
	public Parametro buscarPorId(Integer id) {
		return null;
	}

	public Parametro buscarPorNombre(String nombre) {

		EntityManager em = null;
		Parametro parametro = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			parametro = em.createNamedQuery("Parametro.findByNombre", Parametro.class).setParameter("nombre", nombre)
				.getSingleResult();
		} catch (Exception ex) {
			System.out.println("Error: " + ex.getMessage());
		} finally {
			EntityManagerUtil.close(em);
		}

		return parametro;
	}

	@Override
	public List<Parametro> buscarTodos() {
		EntityManager em = null;
		List<Parametro> lista = new ArrayList<>();
		try {
			em = EntityManagerUtil.getEntityManager();
			lista = em.createNamedQuery("Parametro.findAll", Parametro.class).getResultList();
		} catch (Exception ex) {
			System.out.println("Error:" + ex.getMessage());
		} finally {
			EntityManagerUtil.close(em);
		}
		return lista;
	}

	@Override
	public List<Parametro> buscarPorCriterios(Parametro e) {
		return null;
	}

	@Override
	public String actualizar(Parametro parametro) {
		EntityManager em = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.merge(parametro);
			em.getTransaction().commit();
		} catch (Exception e2) {
			System.out.println("ERROR..." + e2.getMessage());
			return "ERROR";
		} finally {
			EntityManagerUtil.close(em);
		}

		return null;
	}

	@Override
	public String guardar(Parametro parametro) {
		EntityManager em = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.persist(parametro);
			em.getTransaction().commit();
		} catch (Exception e) {
			System.out.print("ERROR..." + e.getMessage());
			return "ERROR";
		} finally {
			EntityManagerUtil.close(em);
		}
		return null;
	}

	@Override
	public String eliminar(Parametro parametro) {
		EntityManager em = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.remove(parametro);
			em.getTransaction().commit();
		} catch (Exception e) {
			System.out.print("ERROR..." + e.getMessage());
			return "ERROR";
		} finally {
			em.close();
		}
		return null;
	}

	@Override
	public String eliminarListado(List<Parametro> listado) {
		EntityManager em = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			for (Parametro para : listado) {
				em.remove(em.merge(para));
			}
			em.getTransaction().commit();
		} catch (Exception e) {
			System.out.print("ERROR..." + e.getMessage());
			return "ERROR";
		} finally {
			EntityManagerUtil.close(em);
		}

		return null;
	}

}
