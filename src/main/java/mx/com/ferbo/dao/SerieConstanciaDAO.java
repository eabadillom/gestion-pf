package mx.com.ferbo.dao;

import java.util.List;

import javax.persistence.EntityManager;

import org.apache.log4j.Logger;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.SerieConstancia;
import mx.com.ferbo.model.SerieConstanciaPK;
import mx.com.ferbo.util.EntityManagerUtil;

public class SerieConstanciaDAO extends IBaseDAO<SerieConstancia, SerieConstanciaPK> {
	
	private static Logger log = Logger.getLogger(SerieConstanciaDAO.class);

	@Override
	public SerieConstancia buscarPorId(SerieConstanciaPK id) {
		SerieConstancia serie = null;
		EntityManager em = null;
		
		try {
			em = EntityManagerUtil.getEntityManager();
			serie = em.find(SerieConstancia.class, id);
		} catch(Exception ex) {
			log.error("Problema para obtener la serie-constancia...", ex);
		} finally {
			EntityManagerUtil.close(em);
		}
		
		return serie;
	}

	@Override
	public List<SerieConstancia> buscarTodos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SerieConstancia> buscarPorCriterios(SerieConstancia e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String actualizar(SerieConstancia e) {
		EntityManager em = null;
		
		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.merge(e);
			em.getTransaction().commit();
		} catch(Exception ex) {
			log.error("Problema para actualizar la serie-constancia", ex);
			EntityManagerUtil.rollback(em);
		} finally {
			EntityManagerUtil.close(em);
		}
		
		return null;
	}

	@Override
	public String guardar(SerieConstancia e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminar(SerieConstancia e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminarListado(List<SerieConstancia> listado) {
		// TODO Auto-generated method stub
		return null;
	}

	

}
