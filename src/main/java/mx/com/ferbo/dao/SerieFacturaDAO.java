package mx.com.ferbo.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.model.SerieFactura;
import mx.com.ferbo.model.StatusSerie;
import mx.com.ferbo.util.EntityManagerUtil;

public class SerieFacturaDAO {
	private static Logger log = LogManager.getLogger(SerieFacturaDAO.class);

	public List<SerieFactura> findAll() {
		EntityManager entity = null;
		List<SerieFactura> list = null;
		
		try {
			entity = EntityManagerUtil.getEntityManager();
			list = entity.createNamedQuery("SerieFactura.findAll", SerieFactura.class).getResultList();
		}catch(Exception e) {
			log.error("Error al obtener informacion",e);
		}finally {
			EntityManagerUtil.close(entity);
		}
		return list;
	};

	public List<StatusSerie> findStatus() {
		EntityManager entity = EntityManagerUtil.getEntityManager();
		List<StatusSerie> list = null;
	try {
		entity = EntityManagerUtil.getEntityManager();
		list = entity.createNamedQuery("StatusSerie.findAll", StatusSerie.class).getResultList();
		}catch(Exception e) {
			log.error("Error al obtener informacion",e);
		}finally {
			EntityManagerUtil.close(entity);
		}
	
		return list;
	};

	public String save(SerieFactura sF) {
		EntityManager entity = null;
		try {
			 entity = EntityManagerUtil.getEntityManager();
			entity.getTransaction().begin();
			entity.persist(sF);
			entity.getTransaction().commit();
			entity.close();
		} catch (Exception e) {
			return e.getMessage();
		}finally {
			EntityManagerUtil.close(entity);
		}
		return null;
	};

	public String update(SerieFactura sF) {
		EntityManager entity = null;
		try {
			 entity = EntityManagerUtil.getEntityManager();
			entity.getTransaction().begin();
			entity.merge(sF);
			entity.getTransaction().commit();
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
		Query sql = entity.createNativeQuery("update serie_factura set status_serie = 3 where id = ?;");
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

	public String delete(SerieFactura sF) {
		EntityManager entity = null;
		try {
			 entity = EntityManagerUtil.getEntityManager();
			entity.getTransaction().begin();
			entity.remove(entity.merge(sF));
			entity.getTransaction().commit();
		} catch (Exception e) {
			return e.getMessage();
		}finally {
			EntityManagerUtil.close(entity);
		}
		return null;
	};

}
