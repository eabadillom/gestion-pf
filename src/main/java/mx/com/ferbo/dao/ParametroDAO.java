package mx.com.ferbo.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.Parametro;
import mx.com.ferbo.util.EntityManagerUtil;

public class ParametroDAO extends IBaseDAO<Parametro,Integer>{

	@Override
	public Parametro buscarPorId(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Parametro> buscarTodos() {
		EntityManager em = EntityManagerUtil.getEntityManager();
		List<Parametro> lista = new ArrayList<>();
		lista = em.createNamedQuery("Parametro.findAll", Parametro.class).getResultList();
		
		return lista;
	}

	@Override
	public List<Parametro> buscarPorCriterios(Parametro e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String actualizar(Parametro parametro) {
		
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.merge(parametro);
			em.getTransaction().commit();
			em.close();
		} catch (Exception e2) {
			System.out.println("ERROR..." + e2.getMessage());
			return "ERROR";
		}
		
		return null;
	}

	@Override
	public String guardar(Parametro parametro) {
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.persist(parametro);
			em.getTransaction().commit();
			em.close();
		} catch (Exception e) {
			System.out.print("ERROR..." + e.getMessage());
			return "ERROR";
		}
		return null;
	}

	@Override
	public String eliminar(Parametro parametro) {
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.remove(parametro);
			em.getTransaction().commit();
			em.close();
		} catch (Exception e) {
			System.out.print("ERROR..." + e.getMessage());
			return "ERROR";
		}
		return null;
	}

	@Override
	public String eliminarListado(List<Parametro> listado) {
		
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			for(Parametro para: listado) {
				em.remove(em.merge(para));
			}
			em.getTransaction().commit();
			em.close();
		} catch (Exception e) {
			System.out.print("ERROR..." + e.getMessage());
			return "ERROR";
		}
		
		return null;
	}
	
	

}
