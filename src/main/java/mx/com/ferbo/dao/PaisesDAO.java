package mx.com.ferbo.dao;

import java.util.List;

import javax.persistence.EntityManager;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.Paises;
import mx.com.ferbo.util.EntityManagerUtil;

public class PaisesDAO extends IBaseDAO<Paises, Integer> {

	@Override
	public Paises buscarPorId(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Paises> buscarTodos() {
		List<Paises> listado = null;
		EntityManager em = EntityManagerUtil.getEntityManager();
		listado = em.createNamedQuery("Paises.findAll", Paises.class).getResultList();
		return listado;
	}

	@Override
	public List<Paises> buscarPorCriterios(Paises e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String actualizar(Paises paises) {
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.merge(paises);
			em.getTransaction().commit();
			em.close();
		} catch (Exception e) {
			System.out.println("ERROR actualizando Países" + e.getMessage());
			return "ERROR";
		}
		return null;
	}

	@Override
	public String guardar(Paises paises) {
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.persist(paises);
			em.getTransaction().commit();
			em.close();
		} catch (Exception e) {
			System.out.println("ERROR guardando Países" + e.getMessage());
			return "ERROR";
		}
		return null;
	}

	@Override
	public String eliminar(Paises paises) {
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.remove(em.merge(paises));
			em.getTransaction().commit();
			em.close();
		} catch (Exception e) {
			System.out.println("ERROR" + e.getMessage());
			return "ERROR";
		}
		return null;
	}

	@Override
	public String eliminarListado(List<Paises> listado) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Paises> buscaPorId(Integer id) {
		EntityManager em = EntityManagerUtil.getEntityManager();
		return em.createNamedQuery("Paises.findByPaisCve", Paises.class)
				.setParameter("paisCve", id).getResultList();
	}

}
