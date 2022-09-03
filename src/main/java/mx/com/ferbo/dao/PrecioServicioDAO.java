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
		if (e.getCliente().getCteCve() != null) {
			if(e.getServicio()!=null) {
				return this.buscarPorClienteServicio(e);
			}
			return this.buscarPorCliente(e);
		}
		

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
			em.close();
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
			// em.close();
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

	private List<PrecioServicio> buscarPorCliente(PrecioServicio e) {
		EntityManager em = EntityManagerUtil.getEntityManager();
		return em.createNamedQuery("PrecioServicio.findByCliente", PrecioServicio.class)
				.setParameter("cteCve", e.getCliente().getCteCve()).getResultList();
	}
	
	private List<PrecioServicio> buscarPorClienteServicio(PrecioServicio e){
		EntityManager em = EntityManagerUtil.getEntityManager();
		return em.createNamedQuery("PrecioServicio.findByClienteServicio", PrecioServicio.class)
				.setParameter("cteCve", e.getCliente().getCteCve())
				.setParameter("servicioCve", e.getServicio())
				.getResultList();
	}
	
}
