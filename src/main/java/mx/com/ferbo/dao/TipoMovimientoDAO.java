package mx.com.ferbo.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.TipoMovimiento;
import mx.com.ferbo.util.EntityManagerUtil;

public class TipoMovimientoDAO extends IBaseDAO<TipoMovimiento, Integer> {
	
	private static Logger log = LogManager.getLogger(TipoMovimientoDAO.class);

	@Override
	public TipoMovimiento buscarPorId(Integer id) {
		TipoMovimiento tipo = null;
		EntityManager em = null;
		
		try {
			em = EntityManagerUtil.getEntityManager();
			tipo = em.find(TipoMovimiento.class, id);
		} catch(Exception ex) {
			log.error("Problema para obtener el tipo de movimiento...", ex);
		} finally {
			EntityManagerUtil.close(em);
		}
		
		return tipo;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TipoMovimiento> buscarTodos() {
		List<TipoMovimiento> lista = null;
		EntityManager em = null;
		Query query = null;
		
		try {
			em = EntityManagerUtil.getEntityManager();
			query = em.createNamedQuery("TipoMovimiento.findAll", TipoMovimiento.class);
			lista = query.getResultList();
		} catch(Exception ex) {
			log.error("Problema para obtener los tipos de movimiento...", ex);
		} finally {
			EntityManagerUtil.close(em);
		}
		return lista;
	}

	@Override
	public List<TipoMovimiento> buscarPorCriterios(TipoMovimiento e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String actualizar(TipoMovimiento e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String guardar(TipoMovimiento e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminar(TipoMovimiento e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminarListado(List<TipoMovimiento> listado) {
		// TODO Auto-generated method stub
		return null;
	}

}
