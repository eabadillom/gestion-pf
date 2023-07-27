package mx.com.ferbo.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.jfree.util.Log;

import mx.com.ferbo.model.SerieNota;
import mx.com.ferbo.model.StatusSerie;
import mx.com.ferbo.util.EntityManagerUtil;

public class SerieNotaDAO {
	private static Logger log = Logger.getLogger(PosicionCamaraDAO.class);

	public List<SerieNota> findAll() {		
		EntityManager entity = null;
		List<SerieNota> list = null;
		try {
			entity = EntityManagerUtil.getEntityManager();
			list = entity.createNamedQuery("SerieNota.findAll", SerieNota.class).getResultList();
		}catch(Exception e) {
			log.error("Error al intentar obtener informacion",e);
		}finally {
			EntityManagerUtil.close(entity);
		}
		return list;
	};

	public List<StatusSerie> findStatus() {
		EntityManager entity = null;
		List<StatusSerie> list = null;
		try {
			entity = EntityManagerUtil.getEntityManager();
			list = entity.createNamedQuery("StatusSerie.findAll", StatusSerie.class).getResultList();			
		}catch(Exception e) {
			log.error("Error al intentar obtener informacion",e);
		}finally {
			EntityManagerUtil.close(entity);
		}
		return list;
	};

	public String save(SerieNota sN) {
		EntityManager entity= null;
		try {
			 entity = EntityManagerUtil.getEntityManager();
			entity.getTransaction().begin();
			entity.persist(sN);
			entity.getTransaction().commit();
			entity.close();
		} catch (Exception e) {
			return e.getMessage();
		}finally {
			EntityManagerUtil.close(entity);
		}
		return null;
	};

	public String update(SerieNota sN) {
		EntityManager entity = null;
		try {
			 entity = EntityManagerUtil.getEntityManager();
			entity.getTransaction().begin();
			entity.merge(sN);
			entity.getTransaction().commit();
			entity.close();
		} catch (Exception e) {
			return e.getMessage();
		}finally {
			EntityManagerUtil.close(entity);
		}
		return null;
	};

	public String cancelar(int id) {
		EntityManager entity = null;
		try {
		 entity = EntityManagerUtil.getEntityManager();
		entity.getTransaction().begin();
		Query sql = entity.createNativeQuery("update SERIE_NOTA set STATUS_SERIE = 3 where ID = ?;");
		sql.setParameter(1, id);
		sql.executeUpdate();
		entity.getTransaction().commit();
		}catch(Exception e) {
			log.error("Error al obtener informacion",e);
		}finally {
			EntityManagerUtil.close(entity);
		}
		return null;
	};

	public String delete(SerieNota sN) {
		EntityManager entity = null;
		try {
			 entity = EntityManagerUtil.getEntityManager();
			entity.getTransaction().begin();
			entity.remove(entity.merge(sN));
			entity.getTransaction().commit();
		} catch (Exception e) {
			return e.getMessage();
		}finally {
			EntityManagerUtil.close(entity);
		}
		return null;
	};

}
