package mx.com.ferbo.dao;

import java.util.List;

import javax.persistence.EntityManager;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.ClaveUnidad;
import mx.com.ferbo.util.EntityManagerUtil;

public class ClaveUnidadDAO extends IBaseDAO<ClaveUnidad,String>{

	@Override
	public ClaveUnidad buscarPorId(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ClaveUnidad> buscarTodos() {
		
		EntityManager em = EntityManagerUtil.getEntityManager();
		List<ClaveUnidad> lista = null; 
		lista = em.createNamedQuery("ClaveUnidad.findAll", ClaveUnidad.class).getResultList();
		
		return lista;
	}

	@Override
	public List<ClaveUnidad> buscarPorCriterios(ClaveUnidad e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String actualizar(ClaveUnidad claveUnidad) {
		
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.merge(claveUnidad);
			em.getTransaction().commit();
			em.close();
		}catch(Exception e){
			System.out.print("ERROR..." + e.getMessage());
			return "ERROR";
		}
		return null;
	}

	@Override
	public String guardar(ClaveUnidad claveUnidad) {
		
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.persist(claveUnidad);
			em.getTransaction().commit();
			em.close();
		} catch (Exception e) {
			System.out.print("ERROR..." + e.getMessage());
			return "ERROR";
		}
		
		return null;
	}

	@Override
	public String eliminar(ClaveUnidad claveUnidad) {
		
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.remove(em.merge(claveUnidad));
			em.getTransaction().commit();
			em.close();
		} catch (Exception e) {
			System.out.print("ERROR..." + e.getMessage());
			return "ERROR";
		}
		
		return null;
	}

	@Override
	public String eliminarListado(List<ClaveUnidad> listado) {
		
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			for(ClaveUnidad claveUnidad:listado){
				em.remove(em.merge(claveUnidad));
			}
			em.getTransaction().commit();
			em.close();
		} catch (Exception e) {
			System.out.print("ERROR..." + e.getMessage());
		}
		
		return null;
	}
}
