package mx.com.ferbo.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.model.SerieNota;
import mx.com.ferbo.model.StatusSerie;
import mx.com.ferbo.util.EntityManagerUtil;

/**
 * @author esteban
 *
 */
/**
 * @author esteban
 *
 */
public class SerieNotaDAO {
	private static Logger log = LogManager.getLogger(PosicionCamaraDAO.class);

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
	
	
	/**Obtiene el listado de series activas.
	 * @param idPlanta Por el momento, el parámetro idPlanta no se ocupa, pero se prepara para que a futuro se utilice con el timbrado CFDI.
	 * @return Lista de Series activas por razón social (planta / sucursal).
	 */
	public List<SerieNota> buscarActivas(Integer idPlanta) {
		EntityManager entity = null;
		List<SerieNota> lista = null;
		try {
			entity = EntityManagerUtil.getEntityManager();
			lista = entity.createNamedQuery("SerieNota.findActivas", SerieNota.class)
					.getResultList()
					;
			
		} catch(Exception ex) {
			
		} finally {
			EntityManagerUtil.close(entity);
		}
		
		return lista;
		
	}

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
		Query sql = entity.createNativeQuery("update serie_nota set STATUS_SERIE = 3 where ID = ?;");
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
