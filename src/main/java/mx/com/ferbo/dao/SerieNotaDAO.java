package mx.com.ferbo.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CacheRetrieveMode;
import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.SerieNota;
import mx.com.ferbo.model.StatusSerie;
import mx.com.ferbo.util.EntityManagerUtil;

public class SerieNotaDAO extends BaseDAO<SerieNota, Integer> {
	
	public SerieNotaDAO() {
		super(SerieNota.class);
	}

	private static Logger log = LogManager.getLogger(PosicionCamaraDAO.class);

	public List<SerieNota> findAll() {		
		EntityManager entity = null;
		List<SerieNota> list = null;
		try {
			entity = EntityManagerUtil.getEntityManager();
			list = entity.createNamedQuery("SerieNota.findAll", SerieNota.class)
					.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS)
					.getResultList();
		}catch(Exception e) {
			log.error("Error al intentar obtener informacion",e);
		}finally {
			EntityManagerUtil.close(entity);
		}
		return list;
	}

	public List<StatusSerie> findStatus() {
		EntityManager entity = null;
		List<StatusSerie> list = null;
		try {
			entity = EntityManagerUtil.getEntityManager();
			list = entity.createNamedQuery("StatusSerie.findAll", StatusSerie.class)
					.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS)
					.getResultList();
		}catch(Exception e) {
			log.error("Error al intentar obtener informacion",e);
		}finally {
			EntityManagerUtil.close(entity);
		}
		return list;
	}
	
	/**Obtiene el listado de series activas.
	 * @param idPlanta Por el momento, el parámetro idPlanta no se ocupa, pero se prepara para que a futuro se utilice con el timbrado CFDI.
	 * @return Lista de Series activas por razón social (planta / sucursal).
	 */
	public List<SerieNota> buscarActivas(Integer idPlanta) {
		EntityManager entity = null;
		List<SerieNota> lista = null;
		try {
			entity = EntityManagerUtil.getEntityManager();
			lista = entity.createNamedQuery("SerieNota.findActivas", this.modelClass)
					.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS)
					.getResultList();
			
		} catch(Exception ex) {
			log.error("Problema para obtener la lista de series de notas de crédito...", ex);
			lista = new ArrayList<SerieNota>();
		} finally {
			EntityManagerUtil.close(entity);
		}
		
		return lista;
	}
}
