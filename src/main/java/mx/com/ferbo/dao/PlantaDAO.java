package mx.com.ferbo.dao;

import static mx.com.ferbo.util.EntityManagerUtil.getEntityManager;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.Camara;
import mx.com.ferbo.model.EmisoresCFDIS;
import mx.com.ferbo.model.Planta;
import mx.com.ferbo.model.Usuario;
import mx.com.ferbo.util.EntityManagerUtil;

public class PlantaDAO extends IBaseDAO<Planta, Integer>{
	private static Logger log = Logger.getLogger(PlantaDAO.class);

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
		EntityManager entity = getEntityManager();
		List<Planta> plantas = null;
		try {
			Query sql = entity.createNamedQuery("Planta.findAll", Planta.class);
			plantas = sql.getResultList();
			
			if(isFullInfo == false)
				return plantas;
			
			for(Planta p : plantas) {
				log.debug(p.getIdUsuario().getUsuario());//ERROR lazy 
				
				log.debug(p.getIdEmisoresCFDIS().getCd_emisor()); //no tienen notacion lazy
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
		try {
			EntityManager entity = getEntityManager();
			entity.getTransaction().begin();
			entity.persist(p);
			entity.getTransaction().commit();
			entity.close();
		} catch (Exception e) {
			return "Failed!! " + e.getMessage();
		}
		return null;
	}
	

	public String update(Planta p) {
		//String consulta = "SELECT * FROM PLANTA WHERE planta_ds = ?";
		//Planta planta;
		try {
			EntityManager entity = getEntityManager();
			entity.getTransaction().begin();
			entity.merge(p);
			entity.getTransaction().commit();
			entity.close();
		   } catch (Exception e) {
			return "Failed!!" + e.getMessage();
		}
		return null;
	}

	public String delete(Planta p) {
		try {
			EntityManager entity = getEntityManager();
			entity.getTransaction().begin();
			entity.remove(entity.merge(p));
			entity.getTransaction().commit();
			entity.close();
		} catch (Exception e) {
			return "Failed!! " + e.getMessage();
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
