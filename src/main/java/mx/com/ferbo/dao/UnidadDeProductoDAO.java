package mx.com.ferbo.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.UnidadDeProducto;
import mx.com.ferbo.util.EntityManagerUtil;

public class UnidadDeProductoDAO extends IBaseDAO<UnidadDeProducto, Integer>{
	private static Logger log = LogManager.getLogger(UnidadDeProducto.class);

	@Override
	public UnidadDeProducto buscarPorId(Integer id) {
		UnidadDeProducto bean = null;
		EntityManager em = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			Query query = em.createNamedQuery("UnidadDeProducto.findByUnidadDeProductoCve", UnidadDeProducto.class)
					.setParameter("productoCve", id)
					;
			bean = (UnidadDeProducto) query.getSingleResult();
			
		} catch(Exception ex) {
			ex.printStackTrace();
		} finally {
			if(em != null)
				em.close();
		}
		return bean;
	}
	
	public List<UnidadDeProducto> buscarPorCliente(Integer idCliente) {
		List<UnidadDeProducto> modelList = null;
		EntityManager em = null;
		
		try {
			em = this.getEntityManager();
		} catch(Exception ex) {
			log.error("Problema para obtener la lista de Unidades de producto por cliente...", ex);
		} finally {
			this.close(em);
		}
		
		return modelList;
	}

	@Override
	public List<UnidadDeProducto> buscarTodos() {
		throw new UnsupportedOperationException("Esta función no está disponible.");
	}

	@Override
	public List<UnidadDeProducto> buscarPorCriterios(UnidadDeProducto e) {
		throw new UnsupportedOperationException("Esta función no está disponible.");
	}
	
	@SuppressWarnings("unchecked")
	public UnidadDeProducto buscarPorProductoUnidad(Integer idProducto, Integer idUnidad) {
		List<UnidadDeProducto> list = null;
		UnidadDeProducto bean = null;
		EntityManager em = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			Query query = em.createNamedQuery("UnidadDeProducto.findByProductoCveUnidadDeProductoCve", UnidadDeProducto.class)
					.setParameter("productoCve", idProducto)
					.setParameter("unidadDeManejoCve", idUnidad);
			
			list = query.getResultList();
			
			if(list == null || list.size() <= 0)
				return null;

			bean = list.get(0);
			bean.getProductoCve();
			bean.getUnidadDeManejoCve();
			
		} catch(Exception ex) {
			ex.printStackTrace();
		} finally {
			if(em != null)
				em.close();
		}
		return bean;
	}

	@Override
	public String actualizar(UnidadDeProducto unidadDeProducto) {
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.merge(unidadDeProducto);
			em.getTransaction().commit();
			em.close();
		} catch (Exception e) {
			System.out.println("ERROR" + e.getMessage());
			return "ERROR";
		}
		
		
		return null;
	}

	@Override
	public String guardar(UnidadDeProducto unidadDeProducto) {
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.persist(unidadDeProducto);
			em.getTransaction().commit();
			em.close();
		} catch (Exception e) {
			System.out.println("ERROR" + e.getMessage());
			return "ERROR";
		}
		return null;
	}

	@Override
	public String eliminar(UnidadDeProducto e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminarListado(List<UnidadDeProducto> listado) {
		// TODO Auto-generated method stub
		return null;
	}

}
