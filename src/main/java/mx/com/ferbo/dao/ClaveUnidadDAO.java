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
            ClaveUnidad claveUnidad = null;
            EntityManager em = null;
            try {
                em = EntityManagerUtil.getEntityManager();
		claveUnidad = em.createNamedQuery("ClaveUnidad.findByCdUnidad", ClaveUnidad.class).
				setParameter("cdUnidad", cdUnidad).getSingleResult();
            } catch(Exception ex) {
                    log.error("Problema para obtener los conceptos...", ex);
            } finally {
                    EntityManagerUtil.close(em);
            }
            return claveUnidad;
	}

	@Override
	public List<ClaveUnidad> buscarTodos() {
            List<ClaveUnidad> lista = null;
            EntityManager em = null;
            try {
		em = EntityManagerUtil.getEntityManager();
		 
		lista = em.createNamedQuery("ClaveUnidad.findAll", ClaveUnidad.class).getResultList();
	    } catch(Exception ex) {
                log.error("Problema para obtener los conceptos...", ex);
            } finally {
                EntityManagerUtil.close(em);
            }
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
		EntityManager em = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.merge(claveUnidad);
			em.getTransaction().commit();
		} catch(Exception e){
			log.error("Problema al actualizar los conceptos...", e);
			return "ERROR";
		} finally {
                        EntityManagerUtil.close(em);
                }
		return null;
	}

	@Override
	public String guardar(ClaveUnidad claveUnidad) {
		EntityManager em = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.persist(claveUnidad);
			em.getTransaction().commit();
		} catch (Exception e) {
			log.error("Problema al guardar los conceptos...", e);
			return "ERROR";
		} finally {
                        EntityManagerUtil.close(em);
                }
		
		return null;
	}

	@Override
	public String eliminar(ClaveUnidad claveUnidad) {
		EntityManager em = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.remove(em.merge(claveUnidad));
			em.getTransaction().commit();
		} catch (Exception e) {
			log.error("Problema al eliminar los conceptos...", e);
			return "ERROR";
		} finally {
                        EntityManagerUtil.close(em);
                }
		
		return null;
	}

	@Override
	public String eliminarListado(List<ClaveUnidad> listado) {
		EntityManager em = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			for(ClaveUnidad claveUnidad:listado){
				em.remove(em.merge(claveUnidad));
			}
			em.getTransaction().commit();
		} catch (Exception e) {
			log.error("Problema al eliminar el listado de conceptos...", e);
		} finally {
                        EntityManagerUtil.close(em);
                }
		
		return null;
	}
}
