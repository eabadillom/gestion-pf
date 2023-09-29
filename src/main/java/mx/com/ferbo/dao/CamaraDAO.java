package mx.com.ferbo.dao;

import static mx.com.ferbo.util.EntityManagerUtil.getEntityManager;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.Camara;
import mx.com.ferbo.model.Planta;
import mx.com.ferbo.util.EntityManagerUtil;

public class CamaraDAO extends IBaseDAO<Camara, Integer> {
	private static Logger log = LogManager.getLogger(CamaraDAO.class);
	
	@SuppressWarnings("unchecked")
	public List<Camara> findall() {
		List<Camara> camara = null;
		Query sql = null;
		EntityManager entity = null;
		try {
			entity = getEntityManager();
			sql = entity.createNamedQuery("Camara.findAll", Camara.class);
			camara = sql.getResultList();
		} catch(Exception ex) {
			log.error("Problema para obtener el listado de cámaras...", ex);
		} finally {
			EntityManagerUtil.close(entity);
		}
		return camara;
	}
	
	public List<Camara> findall(Boolean isFullInfo) {
		List<Camara> camara = null;
		EntityManager entity = null;
		try {
			entity = getEntityManager();
			camara = entity.createNamedQuery("Camara.findAll", Camara.class).getResultList();
			
			if(isFullInfo==false)
				return camara;
			
			for(Camara c: camara) {
				log.debug(c.getPlantaCve().getPlantaDs());
			}
			
		} catch(Exception ex) {
			log.error("Problema para obtener el listado de cámaras...", ex);
		} finally {
			EntityManagerUtil.close(entity);
		}
		return camara;
	}
	
	@Override
	public Camara buscarPorId(Integer id) {
		Camara camara = null;
		EntityManager em = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			camara = em.find(Camara.class, id);
		} catch(Exception ex) {
			log.error("Problema para obtener la camara...", ex);
		} finally {
			EntityManagerUtil.close(em);
		}
		return camara;
	}

	@Override
	public List<Camara> buscarTodos() {
		List<Camara> listado = null;
		EntityManager em = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			listado = em.createNamedQuery("Camara.findAll", Camara.class).getResultList();
		} catch(Exception ex) {
			log.error("Problema para obtener el listado de cámaras...", ex);
		} finally {
			EntityManagerUtil.close(em);
		}
		return listado;
	}

	@Override
	public List<Camara> buscarPorCriterios(Camara e) {
		EntityManager em = null;
		List<Camara> listaC = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			listaC = em.createNamedQuery("Camara.findByplantaCve", Camara.class).setParameter("plantaCve",e.getPlantaCve()).getResultList();
		} catch(Exception ex) {
			log.error("Problema para obtener el listado de cámaras...", ex);
		} finally {
			EntityManagerUtil.close(em);
		}
		return listaC;
	}
	
	public List<Camara> buscarPorPlanta(Planta p) {
		List<Camara> listaC = null;
		EntityManager em = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			listaC = em.createNamedQuery("Camara.findByPlantaCve", Camara.class)
					.setParameter("plantaCve", p.getPlantaCve())
					.getResultList();
		} catch(Exception ex) {
			log.error("Problema para obtener el listado de cámaras...", ex);
		} finally {
			EntityManagerUtil.close(em);
		}
		return listaC;
	}

	@Override
	public String actualizar(Camara camara) {
		EntityManager em = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.merge(camara);
			em.getTransaction().commit();
		} catch (Exception e) {
			log.error("Problema para actualizar la cámara...", e);
			return "ERROR";
		} finally {
			EntityManagerUtil.close(em);
		}
		return null;
	}

	@Override
	public String guardar(Camara camara) {
		EntityManager em = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.persist(camara);
			em.getTransaction().commit();
		} catch (Exception e) {
			log.error("Problema para guardar la cámara...", e);
			return "ERROR";
		} finally {
			EntityManagerUtil.close(em);
		}
		return null;
	}

	@Override
	public String eliminar(Camara camara) {
		EntityManager em = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.remove(em.merge(camara));
			em.getTransaction().commit();
		} catch (Exception e) {
			log.error("Problema para eliminar la cámara...", e);
			return "ERROR";
		} finally {
			EntityManagerUtil.close(em);
		}
		return null;
	}

	@Override
	public String eliminarListado(List<Camara> listado) {
		EntityManager em = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			for (Camara camara : listado) {
				em.remove(em.merge(camara));
			}
			em.getTransaction().commit();
		} catch (Exception e) {
			EntityManagerUtil.rollback(em);
			return "ERROR";
		} finally {
			EntityManagerUtil.close(em);
		}
		return null;
	}

}
