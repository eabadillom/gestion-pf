package mx.com.ferbo.dao;

import java.util.List;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.Producto;
import mx.com.ferbo.util.EntityManagerUtil;
/**
 *
 * @author Gabriel Moreno <gabrielmos0309@gmail.com>
 */
public class ProductoDAO extends IBaseDAO<Producto, Integer> {
	private static Logger log = LogManager.getLogger(ProductoDAO.class);

	@Override
	public Producto buscarPorId(Integer id) {
		Producto producto = null;
		EntityManager em = null;
		
		try {
			em = EntityManagerUtil.getEntityManager();
			producto = em.find(Producto.class, id);
		} catch(Exception ex) {
			log.error("Problema para obtener el producto...", ex);
		} finally {
			EntityManagerUtil.close(em);
		}
		
		return producto;
	}
	
	public List<Producto> buscarPorCliente(Integer idCliente) {
		List<Producto> modelList = null;
		EntityManager em = null;
		
		try {
			em = this.getEntityManager();
			modelList = em.createNamedQuery("Producto.findByCliente", Producto.class)
					.setParameter("idCliente", idCliente)
					.getResultList()
					;
		} catch(Exception ex) {
			log.error("Problema para obtener el listado de productos por cliente...", ex);
		} finally {
			this.close(em);
		}
		
		return modelList;
	}

	@Override
	public List<Producto> buscarTodos() {
		List<Producto> listado = null;
		EntityManager em = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			listado = em.createNamedQuery("Producto.findAll", Producto.class).getResultList();
		} catch(Exception ex) {
			log.error("Problema para obtener el listado de productos...", ex);
		} finally {
			EntityManagerUtil.close(em);
		}
		return listado;
	}

	@Override
	public List<Producto> buscarPorCriterios(Producto e) {
		return null;
	}

	@Override
	public String actualizar(Producto producto) {
		EntityManager em = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.merge(producto);
			em.getTransaction().commit();
		} catch (Exception e) {
			log.error("Problema para actualizar el producto: " + producto, e);
			return "ERROR";
		} finally {
			EntityManagerUtil.close(em);
		}
		return null;
	}

	@Override
	public String guardar(Producto producto) {
		EntityManager em = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.persist(producto);
			em.getTransaction().commit();
		} catch (Exception ex) {
			log.error("Problema para guardar el producto..." + producto, ex);
			return "ERROR";
		} finally {
			EntityManagerUtil.close(em);
		}
		return null;
	}

	@Override
	public String eliminar(Producto producto) {
		EntityManager em = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.remove(em.merge(producto));
			em.getTransaction().commit();
		} catch (Exception e) {
			log.error("Problema para eliminar el producto...", e);
			return "ERROR";
		} finally {
			EntityManagerUtil.close(em);
		}
		return null;
	}

	@Override
	public String eliminarListado(List<Producto> listado) {
		EntityManager em = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			for (Producto producto : listado) {
				em.remove(em.merge(producto));
			}
			em.getTransaction().commit();
		} catch (Exception e) {
			log.error("Problema para eliminar el listado de productos: " + listado, e);
			return "ERROR";
		} finally {
			EntityManagerUtil.close(em);
		}
		return null;
	}

}
