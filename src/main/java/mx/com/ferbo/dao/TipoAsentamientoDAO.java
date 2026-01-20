package mx.com.ferbo.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.TipoAsentamiento;
import mx.com.ferbo.util.EntityManagerUtil;

@Deprecated
public class TipoAsentamientoDAO extends IBaseDAO<TipoAsentamiento, Integer> {
	Logger log = LogManager.getLogger(TipoAsentamientoDAO.class);
	
	@SuppressWarnings("unchecked")
	public List<TipoAsentamiento> findall() {
		EntityManager entity = null;
		List<TipoAsentamiento> TipoAs= null;
		try {
			 entity = EntityManagerUtil.getEntityManager();
			 Query sql = entity.createNamedQuery("TipoAsentamiento.findAll", TipoAsentamiento.class);
			 TipoAs = sql.getResultList();
		}catch(Exception e) {
			log.error("Error al obtener informacion",e);
		}finally {
			EntityManagerUtil.close(entity);
		}
		return TipoAs;
	}
	@Override
	public TipoAsentamiento buscarPorId(Integer id) {
		EntityManager entity = null;
		TipoAsentamiento Tasn = null;
		try {
			entity =EntityManagerUtil.getEntityManager();
			Query sql = entity.createNamedQuery("TipoAsentamiento.findByTipoasntmntoCve",TipoAsentamiento.class)
					.setParameter("tipoasntmntoCve",id.shortValue());
			Tasn = (TipoAsentamiento) sql.getSingleResult();
		}catch(Exception e) {
			log.error("Error al obtener informacion",e);
		}finally {
			EntityManagerUtil.close(entity);
		}
		return Tasn;
	}
	

	@Override
	public List<TipoAsentamiento> buscarTodos() {
		List<TipoAsentamiento> listado = null;
		EntityManager em = null;
		try {
			 em = EntityManagerUtil.getEntityManager();
			listado = em.createNamedQuery("TipoAsentamiento.findAll", TipoAsentamiento.class).getResultList();
		}catch(Exception e) {
			log.error("Error al obtener informacion",e);
		}finally {
			EntityManagerUtil.close(em);
		}
		
		return listado;
	}

	@Override
	public List<TipoAsentamiento> buscarPorCriterios(TipoAsentamiento e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String actualizar(TipoAsentamiento tipoAsentamiento) {
		EntityManager em = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.merge(tipoAsentamiento);
			em.getTransaction().commit();
		} catch (Exception e) {
			System.out.println("ERROR actualizando Tipo de Asentamiento" + e.getMessage());
			return "ERROR";
		}finally {
			EntityManagerUtil.close(em);
		}
		return null;
	}

	@Override
	public String guardar(TipoAsentamiento tipoAsentamiento) {
		EntityManager em = null;
		try {
			 em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.persist(tipoAsentamiento);
			em.getTransaction().commit();
		} catch (Exception e) {
			System.out.println("ERROR guardando Tipo de Asentamiento" + e.getMessage());
			return "ERROR";
		}finally {
			EntityManagerUtil.close(em);
		}
		return null;
	}

	@Override
	public String eliminar(TipoAsentamiento tipoAsentamiento) {
		EntityManager em = null;
		try {
			 em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.remove(em.merge(tipoAsentamiento));
			em.getTransaction().commit();
		} catch (Exception e) {
			System.out.println("ERROR" + e.getMessage());
			return "ERROR";
		}finally {
			EntityManagerUtil.close(em);
		}
		return null;
	}

	@Override
	public String eliminarListado(List<TipoAsentamiento> listado) {
		// TODO Auto-generated method stub
		return null;
	}

}
