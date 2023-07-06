package mx.com.ferbo.dao;

import java.util.List;

import javax.persistence.EntityManager;

import org.apache.log4j.Logger;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.ProductoPorCliente;
import mx.com.ferbo.util.EntityManagerUtil;

public class ProductoClienteDAO extends IBaseDAO<ProductoPorCliente, Integer> {
	private EntityManager manager = null;
	private static Logger log = Logger.getLogger(ProductoClienteDAO.class);

	@Override
	public ProductoPorCliente buscarPorId(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ProductoPorCliente> buscarTodos() {
		List<ProductoPorCliente> listado;
		EntityManager em = EntityManagerUtil.getEntityManager();
		listado = em.createNamedQuery("ProductoPorCliente.findAll", ProductoPorCliente.class).getResultList();
		return listado;
	}

	@Override
	public List<ProductoPorCliente> buscarPorCriterios(ProductoPorCliente e) {
		List<ProductoPorCliente> listado = null;
		EntityManager em = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			listado = em.createNamedQuery("ProductoPorCliente.findByCteCve",ProductoPorCliente.class).setParameter("cteCve", e.getCteCve().getCteCve()).getResultList();
		} catch(Exception ex) {
			log.error("Problema para obtener el listado de productos por cliente...", ex);
		} finally {
			EntityManagerUtil.close(em);
		}
		
		return listado;
	}
	
	
	public List<ProductoPorCliente> buscarPorCliente(Integer cteCve, boolean isFullInfo) {
		List<ProductoPorCliente> listado = null;
		EntityManager em = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			listado = em.createNamedQuery("ProductoPorCliente.findByCteCve",ProductoPorCliente.class)
					.setParameter("cteCve", cteCve)
					.getResultList();
			
			if(isFullInfo == false)
				return listado;
			
			for(ProductoPorCliente p : listado) {
				p.getProductoCve().getProductoCve();
			}
			
		} catch(Exception ex) {
			log.error("Problema para obtener el listado de productos por cliente...", ex);
		} finally {
			EntityManagerUtil.close(em);
		}
		
		return listado;
	}
	
	public List<ProductoPorCliente> buscarPorCteCve(Cliente cliente) {
		List<ProductoPorCliente> alProductos = null;
		boolean entityManagerInternal = false;
		EntityManager em = null;
		try {
			if(em == null) {
				entityManagerInternal = true;
				em = EntityManagerUtil.getEntityManager();
			}
			alProductos = em.createNamedQuery("ProductoPorCliente.findByCteCve", ProductoPorCliente.class)
					.setParameter("cteCve", cliente)
					.getResultList();
		} finally {
			if(entityManagerInternal) {
				EntityManagerUtil.close(em);
			}
		}
		
		return alProductos;
	}

	@Override
	public String actualizar(ProductoPorCliente productoCliente) {
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.createQuery("UPDATE ProductoPorCliente as pc set pc.productoCve.productoCve = :prdCve where pc.prodXCteCve = :pxc AND pc.cteCve.cteCve = :cteCve")
			.setParameter("prdCve", productoCliente.getProductoCve().getProductoCve())
			.setParameter("pxc", productoCliente.getProdXCteCve())
			.setParameter("cteCve", productoCliente.getCteCve().getCteCve())
			.executeUpdate();
			em.getTransaction().commit();
			em.close();
		} catch (Exception e) {
			System.out.println("ERROR" + e.getMessage());
			return "ERROR";
		}
		return null;
	}

	@Override
	public String guardar(ProductoPorCliente prodCliente) {
		// TODO Auto-generated method stub
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.persist(prodCliente);
			em.getTransaction().commit();
		//	em.close();
		} catch (Exception e) {
			System.out.println("ERROR" + e.getMessage());
			return "ERROR";
		}
		return null;
	}

	@Override
	public String eliminar(ProductoPorCliente prodCliente) {
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.createQuery("DELETE from ProductoPorCliente as pc where pc.prodXCteCve = :pxc AND pc.cteCve.cteCve = :cteCve and pc.productoCve.productoCve = :prdCve")
			.setParameter("prdCve", prodCliente.getProductoCve().getProductoCve())
			.setParameter("pxc", prodCliente.getProdXCteCve())
			.setParameter("cteCve", prodCliente.getCteCve().getCteCve())
			.executeUpdate();
			em.getTransaction().commit();
			em.close();
		} catch (Exception e) {
			System.out.println("ERROR" + e.getMessage());
			return "ERROR";
		}
		return null;
	}

	@Override
	public String eliminarListado(List<ProductoPorCliente> listado) {
		// TODO Auto-generated method stub
		return null;
	}

	public EntityManager getEntityManager() {
		return manager;
	}

	public void setEntityManager(EntityManager manager) {
		this.manager = manager;
	}

}
