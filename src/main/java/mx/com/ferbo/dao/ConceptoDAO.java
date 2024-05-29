package mx.com.ferbo.dao;

import java.util.List;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.Concepto;
import mx.com.ferbo.util.EntityManagerUtil;

public class ConceptoDAO extends IBaseDAO<Concepto, String> {
	private static Logger log = LogManager.getLogger(ConceptoDAO.class);

	@Override
	public Concepto buscarPorId(String clave) {
		Concepto model = null;
		EntityManager em = null;
		
		try {
			em = EntityManagerUtil.getEntityManager();
			model = em.find(Concepto.class, clave);
		} catch(Exception ex) {
			log.warn("Problema para obtener la clave: " + clave, ex);
		} finally {
			EntityManagerUtil.close(em);
		}
		
		return model;
	}
	
	public List<Concepto> buscarPorNombre(String nombre){
		List<Concepto> modelList = null;
		EntityManager em = null;
		String prmNombre = null;
		
		try {
			prmNombre = new String(nombre);
			
			if(prmNombre.startsWith("%") == false)
				prmNombre = "%".concat(prmNombre);
			
			if(prmNombre.endsWith("%") == false)
				prmNombre = prmNombre.concat("%");
			
			em = EntityManagerUtil.getEntityManager();
			modelList = em.createNamedQuery("Concepto.likeNombre", Concepto.class)
					.setParameter("nombre", prmNombre)
					.getResultList()
					;
		} catch(Exception ex) {
			log.error("Problema para obtener la lista de Conceptos...", ex);
		} finally {
			EntityManagerUtil.close(em);
		}
		
		return modelList;
	}
	
	public List<Concepto> buscarPorClaveNombre(String clave, String nombre) {
		List<Concepto> modelList = null;
		EntityManager em = null;
		String prmClave = null;
		String prmNombre = null;
		
		try {
			prmClave = new String(clave);
			if(prmClave.startsWith("%") == false)
				prmClave = "%".concat(prmClave);
			if(prmClave.endsWith("%") == false)
				prmClave = prmClave.concat("%");
			
			prmNombre = new String(nombre);
			if(prmNombre.startsWith("%") == false)
				prmNombre = "%".concat(prmNombre);
			if(prmNombre.endsWith("%") == false)
				prmNombre = prmNombre.concat("%");
			
			em = EntityManagerUtil.getEntityManager();
			modelList = em.createNamedQuery("Concepto.likeClaveNombre", Concepto.class)
					.setParameter("clave", prmClave)
					.setParameter("nombre", prmNombre)
					.getResultList()
					;
		} catch(Exception ex) {
			log.error("Problema para obtener la lista de Conceptos...", ex);
		} finally {
			EntityManagerUtil.close(em);
		}
		
		return modelList;
	}

	@Override
	public List<Concepto> buscarTodos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Concepto> buscarPorCriterios(Concepto e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String actualizar(Concepto e) {
		String result = null;
		EntityManager em = null;
		
		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			e = em.merge(e);
			em.getTransaction().commit();
			result = null;
		} catch(Exception ex) {
			em.getTransaction().rollback();
			log.error("Problema para actualizar el concepto: " + e, ex);
			result = ex.getMessage();
		} finally {
			EntityManagerUtil.close(em);
		}
		return result;
	}

	@Override
	public String guardar(Concepto e) {
		String result = null;
		EntityManager em = null;
		
		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.persist(e);
			em.getTransaction().commit();
			result = null;
		} catch(Exception ex) {
			em.getTransaction().rollback();
			log.error("Problema para guardar el concepto: " + e, ex);
			result = ex.getMessage();
		} finally {
			EntityManagerUtil.close(em);
		}
		return result;
	}

	@Override
	public String eliminar(Concepto e) {
		String result = null;
		EntityManager em = null;
		
		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.remove(e);
			em.getTransaction().commit();
			result = null;
		} catch(Exception ex) {
			em.getTransaction().rollback();
			log.error("Problema para eliminar el concepto: " + e, ex);
			result = ex.getMessage();
		} finally {
			EntityManagerUtil.close(em);
		}
		return result;
	}

	@Override
	public String eliminarListado(List<Concepto> listado) {
		// TODO Auto-generated method stub
		return null;
	}

}
