package mx.com.ferbo.commons.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public abstract class IBaseDAO<MODEL, PK> {
	
	private static Logger log = LogManager.getLogger(IBaseDAO.class);
	
	protected Class<MODEL> modelClass;
	
	public IBaseDAO() {
	}
	
	public IBaseDAO(Class<MODEL> modelClass) {
		this.modelClass = modelClass;
	}
	
	public EntityManager getEntityManager() {
		EntityManager em = null;
		EntityManagerFactory emf = null;
		String PERSIST_UNIT = "gestionPU";
		try {
			emf = Persistence.createEntityManagerFactory(PERSIST_UNIT);
			em = emf.createEntityManager();
		} catch(Exception ex) {
			log.error("Problema para obtener el entity manager...", ex);
		}
		
		return em;
	}
	
	public synchronized void close(EntityManager em) {
		if(em == null)
			return;
		
		if(em.isOpen())
			em.close();
		em = null;
		return;
	}
	
	public synchronized void rollback(EntityManager em) {
		if(em == null)
			return;
		
		if(em.isOpen() == false)
			return;
		
		em.getTransaction().rollback();
	}
    
    public MODEL buscarPorId(PK id) {
    	MODEL model = null;
		
		EntityManager em = null;
		
		try {
			em = this.getEntityManager();
			model = em.find(modelClass, id);
		} catch(Exception ex) {
			log.warn("Problema para obtener el elemento por ID: {}", id);
		} finally {
			this.close(em);
		}
		
		return model;
    }

    public abstract List<MODEL> buscarTodos();

    public abstract List<MODEL> buscarPorCriterios(MODEL e);

    public String actualizar(MODEL model) {
    	String respuesta = null;
    	EntityManager em = null;
		try {
			log.info("Actualizando objeto: {}", model);
			em = getEntityManager();
			em.getTransaction().begin();
			model = em.merge(model);
			em.getTransaction().commit();
			log.info("Objeto actualizado correctamente: {}", model);
		} catch(Exception ex) {
			respuesta = ex.getMessage();
			rollback(em);
			log.error("Problema para actualizar el objeto: " + model, ex);
		} finally {
			close(em);
		}
		
		return respuesta;
    }

    public String guardar(MODEL model) {
    	String respuesta = null;
    	EntityManager em = null;
		try {
			log.info("Guardando objeto: {}", model);
			em = getEntityManager();
			em.getTransaction().begin();
			if(em.contains(model)) {
				model = em.merge(model);
			} else
				em.persist(model);
			
			em.getTransaction().commit();
			log.info("Objeto guardado correctamente: {}", model);
		} catch(Exception ex) {
			respuesta = ex.getMessage();
			rollback(em);
			log.error("Problema para guardar el objeto: " + model, ex);
		} finally {
			close(em);
		}
		
		return respuesta;
    }
    
    public String eliminar(MODEL model) {
    	String respuesta = null;
    	EntityManager em = null;
		try {
			log.info("Eliminando objeto: {}", model);
			em = getEntityManager();
			em.getTransaction().begin();
			em.remove(model);
			em.getTransaction().commit();
			log.info("Objeto eliminado correctamente: {}", model);
		} catch(Exception ex) {
			respuesta = ex.getMessage();
			this.rollback(em);
			log.error("Probleam para eliminar el objeto: " + model, ex);
		} finally {
			this.close(em);
		}
		return respuesta;
    }
    
    public abstract String eliminarListado(List<MODEL> listado);

}
