package mx.com.ferbo.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.PrecioServicio;
import mx.com.ferbo.util.EntityManagerUtil;

public class PrecioServicioDAO extends IBaseDAO<PrecioServicio, Integer> {

	@Override
	public PrecioServicio buscarPorId(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PrecioServicio> buscarTodos() {
		List<PrecioServicio> listado = new ArrayList<>();
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			listado = em.createNamedQuery("PrecioServicio.findAll", PrecioServicio.class).getResultList();
		} catch (Exception e) {

		}
		return listado;
	}

	@Override
	public List<PrecioServicio> buscarPorCriterios(PrecioServicio e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String actualizar(PrecioServicio precioServicio) {
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.merge(precioServicio);
			em.getTransaction().commit();
		} catch (Exception e) {
			System.out.println("ERROR" + e.getMessage());
			return "ERROR";
		}
		return null;
	}

	@Override
	public String guardar(PrecioServicio precioServicio) {
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.persist(precioServicio);
			em.getTransaction().commit();
//			em.close();
		} catch (Exception e) {
			System.out.println("ERROR" + e.getMessage());
			return "ERROR";
		}
		return null;
	}

	@Override
	public String eliminar(PrecioServicio precioServicio) {
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.remove(em.merge(precioServicio));
			em.getTransaction().commit();
//			em.close();
		} catch (Exception e) {
			System.out.println("ERROR" + e.getMessage());
			return "ERROR";
		}
		return null;
	}

	@Override
	public String eliminarListado(List<PrecioServicio> listado) {
		// TODO Auto-generated method stub
		return null;
	}

}
