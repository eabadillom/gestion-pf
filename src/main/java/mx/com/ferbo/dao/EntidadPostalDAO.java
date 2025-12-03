package mx.com.ferbo.dao;

import java.util.List;

import javax.persistence.EntityManager;

import org.jfree.util.Log;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.AsentamientoHumano;
import mx.com.ferbo.model.EntidadPostal;
import mx.com.ferbo.util.EntityManagerUtil;

@Deprecated
public class EntidadPostalDAO extends IBaseDAO<EntidadPostal, Integer> {

	@Override
	public EntidadPostal buscarPorId(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<EntidadPostal> buscarTodos() {
		List<EntidadPostal> listado = null;
		EntityManager em = EntityManagerUtil.getEntityManager();
		listado = em.createNamedQuery("EntidadPostal.findAll", EntidadPostal.class).getResultList();
		return listado;
	}

	@Override
	public List<EntidadPostal> buscarPorCriterios(EntidadPostal e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String actualizar(EntidadPostal entidadPostal) {
		
		EntityManager em = null;
		
		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.merge(entidadPostal);
			em.getTransaction().commit();
		} catch (Exception e) {
			Log.error("Problema al actualizar entidad postal",e);
		}finally {
			EntityManagerUtil.close(em);
		}
		return null;
	}

	@Override
	public String guardar(EntidadPostal entidadPostal) {
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.persist(entidadPostal);
			em.getTransaction().commit();
			em.close();
		} catch (Exception e) {
			System.out.println("ERROR guardando Entidad Postal" + e.getMessage());
			return "ERROR";
		}
		return null;
	}

	@Override
	public String eliminar(EntidadPostal entidadPostal) {
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.remove(em.merge(entidadPostal));
			em.getTransaction().commit();
			em.close();
		} catch (Exception e) {
			System.out.println("ERROR" + e.getMessage());
			return "ERROR";
		}
		return null;
	}

	@Override
	public String eliminarListado(List<EntidadPostal> listado) {
		// TODO Auto-generated method stub
		return null;
	}

}
