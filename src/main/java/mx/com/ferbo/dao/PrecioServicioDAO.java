package mx.com.ferbo.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.Aviso;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.PrecioServicio;
import mx.com.ferbo.model.Servicio;
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
	
	public List<PrecioServicio> buscarPorAviso(Aviso aviso, Cliente cliente){
		
		List<PrecioServicio> listaPrecioServicio = new ArrayList<>();
		
		try {
			
			EntityManager entity = EntityManagerUtil.getEntityManager();
			listaPrecioServicio = entity.createNamedQuery("PrecioServicio.findByAvisoAndCliente", PrecioServicio.class)
					.setParameter("cteCve", cliente.getCteCve())
					.setParameter("avisoCve", aviso.getAvisoCve()).getResultList();
			
		} catch (Exception e) {
			System.out.println("ERROR" + e.getMessage());
			
		} 
		
		
		return listaPrecioServicio;
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
	
	public PrecioServicio getPrecioMinimoPorServicio(Integer idServicio) {
		PrecioServicio bean = null;
		EntityManager em = null;
		Query nativeQuery = null;
		Object[] obj = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			nativeQuery = em.createNativeQuery(
					"SELECT servicio, min(precio) as precio FROM precio_servicio WHERE servicio = :idServicio GROUP BY servicio ORDER BY servicio, precio")
					.setParameter("idServicio", idServicio);
			obj = (Object [])nativeQuery.getSingleResult();
			bean = new PrecioServicio();
			bean.setServicio(new Servicio((int)obj[0]));
			bean.setPrecio((BigDecimal)obj[1]);
			
			em.getTransaction().commit();
			
		} catch(PersistenceException ex) {
			ex.printStackTrace();
		} catch(Exception ex) {
			ex.printStackTrace();
		} finally {
			em.close();
		}
		
		return bean;
	}

}
