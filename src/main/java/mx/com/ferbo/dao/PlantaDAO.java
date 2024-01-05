package mx.com.ferbo.dao;

import static mx.com.ferbo.util.EntityManagerUtil.getEntityManager;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.EmisoresCFDIS;
import mx.com.ferbo.model.Planta;
import mx.com.ferbo.model.SerieConstancia;
import mx.com.ferbo.model.Usuario;
import mx.com.ferbo.util.EntityManagerUtil;

public class PlantaDAO extends IBaseDAO<Planta, Integer>{
	private static Logger log = LogManager.getLogger(PlantaDAO.class);

	@SuppressWarnings("unchecked")
	public List<Planta> findall() {
		EntityManager entity = getEntityManager();
		List<Planta> plantas = null;
		try {
			Query sql = entity.createNamedQuery("Planta.findAll", Planta.class);
			plantas = sql.getResultList();
		} catch(Exception ex) {
			log.error("Problema para obtener el listado de Plantas...", ex);
		} finally {
			EntityManagerUtil.close(entity);
		}
		
		return plantas;
	}
	
	@SuppressWarnings("unchecked")
	public List<Planta> findall(boolean isFullInfo) {
		EntityManager entity = null;
		List<Planta> plantas = null;
		try {
			entity = getEntityManager();
			Query sql = entity.createNamedQuery("Planta.findAll", Planta.class);
			plantas = sql.getResultList();
			
			if(isFullInfo == false)
				return plantas;
			
			for(Planta p : plantas) {
				log.debug(p.getIdUsuario().getUsuario());//ERROR lazy 
				
				log.debug(p.getIdEmisoresCFDIS().getNb_emisor()); //no tienen notacion lazy
				log.debug(p.getIdEmisoresCFDIS().getNb_rfc());
				log.debug(p.getIdEmisoresCFDIS().getNb_emisor());
				for(SerieConstancia serieConstancia : p.getSerieConstanciaList()) {
					log.debug(serieConstancia.getSerieConstanciaPK().getPlanta().getPlantaCve());
				}
			}
		} catch(Exception ex) {
			log.error("Problema para obtener el listado de Plantas...", ex);
		} finally {
			EntityManagerUtil.close(entity);
		}
		
		return plantas;
	}

	@SuppressWarnings("unchecked")
	public List<Usuario> getUsuarios() { //MODIFICADO	
		EntityManager entity = null;
		List<Usuario> usuarios = null;
		
		try {
		
			entity = getEntityManager();
			Query sql = entity.createQuery("SELECT u FROM Usuario u WHERE u.perfil IN (1, 4)");
			usuarios = sql.getResultList();			
			
		}catch (Exception e) {
			log.error("Problema al obtener a los usuarios", e);
		}finally {
			EntityManagerUtil.close(entity);
		}
		return usuarios;		
		
	}

	@SuppressWarnings("unchecked")
	public List<EmisoresCFDIS> getEmisor() { //MODIFICADO	
		
		EntityManager entity = null;
		List<EmisoresCFDIS> emisor = null;
		
		try {
			
			entity = getEntityManager();
			Query sql = entity.createQuery("SELECT e FROM EmisoresCFDIS e ");
			emisor = sql.getResultList();
			
		} catch (Exception e) {
			log.error("Problema al obtener a los emisores",e);
		}finally {
			EntityManagerUtil.close(entity);
		}
		
		return emisor;
	}
	
	public String save(Planta p) {
		EntityManager entity = null;
		try {
			entity = getEntityManager();
			entity.getTransaction().begin();
			entity.persist(p);
			entity.getTransaction().commit();
		} catch (Exception e) {
			log.error("Problema para guardar la planta...", e);
			return "Failed!! " + e.getMessage();
		} finally {
			EntityManagerUtil.close(entity);
		}
		return null;
	}
	

	public String update(Planta p) {
		EntityManager entity = null;
		try {
			entity = getEntityManager();
			entity.getTransaction().begin();
			entity.merge(p);
			entity.getTransaction().commit();
		} catch (Exception e) {
			log.error("Problema para actualizar la planta...", e);
			return "Failed!!" + e.getMessage();
		} finally {
			EntityManagerUtil.close(entity);
		}
		return null;
	}

	public String delete(Planta p) {
		EntityManager entity = null;
		try {
			entity = getEntityManager();
			entity.getTransaction().begin();
			entity.remove(entity.merge(p));
			entity.getTransaction().commit();
		} catch (Exception e) {
			log.error("Problema para eliminar la planta...", e);
			return "Failed!! " + e.getMessage();
		} finally {
			EntityManagerUtil.close(entity);
		}
		return null;
	}

	@Override
	public Planta buscarPorId(Integer id) {
		Planta planta = null;
		EntityManager em = null;
		
		try {
			em = EntityManagerUtil.getEntityManager();
			planta = em.find(Planta.class, id);
		} catch(Exception ex) {
			log.error("Problema para obtener la planta: " + id, ex);
		} finally {
			EntityManagerUtil.close(em);
		}
		return planta;
	}

	@Override
	public List<Planta> buscarTodos() {
		List<Planta> listado = null;
		EntityManager em = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			listado = em.createNamedQuery("Planta.findAll", Planta.class).getResultList();
		} catch(Exception ex) {
			log.error("Problema para obtener el listado de cámaras...", ex);
		} finally {
			EntityManagerUtil.close(em);
		}
		return listado;
	}

	@Override
	public List<Planta> buscarPorCriterios(Planta e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String actualizar(Planta e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String guardar(Planta e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminar(Planta e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminarListado(List<Planta> listado) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
