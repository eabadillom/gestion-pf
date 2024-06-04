package mx.com.ferbo.dao;

import java.util.List;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.ClaveUnidad;
import mx.com.ferbo.util.EntityManagerUtil;

public class ClaveUnidadDAO extends IBaseDAO<ClaveUnidad,String>{
	private static Logger log = LogManager.getLogger(ClaveUnidadDAO.class);

	@Override
	public ClaveUnidad buscarPorId(String cdUnidad) {
		EntityManager em = EntityManagerUtil.getEntityManager();
		return em.createNamedQuery("ClaveUnidad.findByCdUnidad", ClaveUnidad.class).
				setParameter("cdUnidad", cdUnidad).getSingleResult();		
	}

	@Override
	public List<ClaveUnidad> buscarTodos() {
		
		EntityManager em = EntityManagerUtil.getEntityManager();
		List<ClaveUnidad> lista = null; 
		lista = em.createNamedQuery("ClaveUnidad.findAll", ClaveUnidad.class).getResultList();
		
		return lista;
	}
	
	public List<ClaveUnidad> buscarPorClaveNombre(String clave, String nombre) {
		List<ClaveUnidad> modelList = null;
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
			modelList = em.createNamedQuery("ClaveUnidad.likeClaveNombre", ClaveUnidad.class)
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
	public List<ClaveUnidad> buscarPorCriterios(ClaveUnidad e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String actualizar(ClaveUnidad claveUnidad) {
		
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.merge(claveUnidad);
			em.getTransaction().commit();
			em.close();
		}catch(Exception e){
			System.out.print("ERROR..." + e.getMessage());
			return "ERROR";
		}
		return null;
	}

	@Override
	public String guardar(ClaveUnidad claveUnidad) {
		
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.persist(claveUnidad);
			em.getTransaction().commit();
			em.close();
		} catch (Exception e) {
			System.out.print("ERROR..." + e.getMessage());
			return "ERROR";
		}
		
		return null;
	}

	@Override
	public String eliminar(ClaveUnidad claveUnidad) {
		
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.remove(em.merge(claveUnidad));
			em.getTransaction().commit();
			em.close();
		} catch (Exception e) {
			System.out.print("ERROR..." + e.getMessage());
			return "ERROR";
		}
		
		return null;
	}

	@Override
	public String eliminarListado(List<ClaveUnidad> listado) {
		
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			for(ClaveUnidad claveUnidad:listado){
				em.remove(em.merge(claveUnidad));
			}
			em.getTransaction().commit();
			em.close();
		} catch (Exception e) {
			System.out.print("ERROR..." + e.getMessage());
		}
		
		return null;
	}
}
