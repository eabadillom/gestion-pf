package mx.com.ferbo.commons.dao;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import mx.com.ferbo.util.DAOException;
import mx.com.ferbo.util.EntityManagerUtil;
import mx.com.ferbo.util.InventarioException;

public abstract class BaseDAO<MODEL, PK> {
	private static Logger log = LogManager.getLogger(BaseDAO.class);
	protected Class<MODEL> modelClass;
	protected static EntityManagerFactory emf = null;

	public BaseDAO(Class<MODEL> modelClass) {
		this.modelClass = modelClass;
	}

	public EntityManager getEntityManager() {
		EntityManager em = null;
		try {
			em = EntityManagerUtil.getEntityManager();
		} catch (Exception ex) {
			log.error("Problema para obtener el entity manager...", ex);
		}

		return em;
	}

	public Optional<MODEL> buscarPorId(PK id) {
		Optional<MODEL> optional = null;
		MODEL model = null;

		EntityManager em = null;

		try {
			em = EntityManagerUtil.getEntityManager();
			model = em.find(modelClass, id);
			optional = Optional.of(model);
		} catch (Exception ex) {
			log.warn("Problema para obtener el elemento por ID: {}", id);
			optional = Optional.empty();
		} finally {
			EntityManagerUtil.close(em);
		}

		return optional;
	}

	public synchronized void guardar(MODEL model) throws InventarioException {
		EntityManager em = null;

		try {
			log.info("Guardando objeto: {}", model);
			em = getEntityManager();
			em.getTransaction().begin();
			em.persist(model);
			em.getTransaction().commit();
			log.info("Objeto guardado correctamente: {}", model);
		} catch (Exception ex) {
			rollback(em);
			log.error("Problema para guardar el objeto: " + model, ex);
			throw new InventarioException("Error al guardar en la base de datos.");
		} finally {
			close(em);
		}
	}

	public synchronized void actualizar(MODEL model) throws InventarioException {
		EntityManager em = null;

		try {
			log.info("Actualizando objeto: {}", model);
			em = getEntityManager();
			em.getTransaction().begin();
			model = em.merge(model);
			em.getTransaction().commit();
			log.info("Objeto actualizado correctamente: {}", model);
		} catch (Exception ex) {
			rollback(em);
			log.error("Problema para actualizar el objeto: " + model, ex);
			throw new InventarioException("Error al actualizar en la base de datos.");
		} finally {
			close(em);
		}
	}

	// Metodo pensado inicialmente en los catalogos
	public MODEL buscarPorNombre(String nombre) throws DAOException {
		EntityManager em = null;
		try {
			em = getEntityManager();
			String jpql = "SELECT e FROM " + modelClass.getSimpleName() + " e WHERE e.nombre = :nombre";
			return em.createQuery(jpql, modelClass)
					.setParameter("nombre", nombre)
					.getSingleResult();
		} catch (Exception ex) {
			log.error("Error al buscar {} por nombre {}: {}", modelClass.getSimpleName(), nombre, ex);
			throw new DAOException(
					"Hubo un problema al buscar " + modelClass.getSimpleName() + " por nombre: " + nombre, ex);
		} finally {
			close(em);
		}
	}

	// Metodo pensado inicialmente en los catalogos
	public List<MODEL> findByVigente(boolean vigente) throws DAOException {
		EntityManager em = null;
		List<MODEL> resultados = null;
		try {
			em = getEntityManager();
			String jpql = "SELECT e FROM " + modelClass.getSimpleName() + " e WHERE e.vigente = :vigente";
			resultados = em.createQuery(jpql, modelClass)
					.setParameter("vigente", vigente)
					.getResultList();
		} catch (Exception ex) {
			log.error("Error al obtener elementos vigentes: ", ex);
			throw new DAOException("Error al obtener elementos vigentes de " + modelClass.getSimpleName(), ex);
		} finally {
			close(em);
		}
		return resultados;
	}

	public synchronized void eliminar(MODEL model) throws InventarioException {
		EntityManager em = null;

		try {
			log.info("Eliminando objeto: {}", model);
			em = getEntityManager();
			em.getTransaction().begin();
			em.remove(em.contains(model) ? model : em.merge(model));
			em.getTransaction().commit();
			log.info("Objeto eliminado correctamente: {}", model);
		} catch (Exception ex) {
			this.rollback(em);
			log.error("Probleam para eliminar el objeto: " + model, ex);
			throw new InventarioException("Error al eliminar en la base de datos.");
		} finally {
			this.close(em);
		}
	}

	public synchronized void close(EntityManager em) {
		if (em == null)
			return;

		if (em.isOpen())
			em.close();
		em = null;
		return;
	}

	public synchronized void rollback(EntityManager em) {
		if (em == null)
			return;

		if (em.isOpen() == false)
			return;

		em.getTransaction().rollback();
	}

}
