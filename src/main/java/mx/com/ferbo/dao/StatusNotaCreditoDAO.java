package mx.com.ferbo.dao;

import java.util.List;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.StatusNotaCredito;
import mx.com.ferbo.util.EntityManagerUtil;

public class StatusNotaCreditoDAO extends IBaseDAO<StatusNotaCredito, Integer> {
	private static Logger log = LogManager.getLogger(StatusNotaCreditoDAO.class);

	@Override
	public StatusNotaCredito buscarPorId(Integer id) {
		StatusNotaCredito status = null;
		EntityManager em = null;
		
		try {
			em = EntityManagerUtil.getEntityManager();
			status = em.find(StatusNotaCredito.class, id);
		} catch(Exception ex) {
			log.error("Problema para consultar el status de nota de crédito: {}", ex, id);
		} finally {
			EntityManagerUtil.close(em);
		}
		
		return status;
	}

	@Override
	public List<StatusNotaCredito> buscarTodos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<StatusNotaCredito> buscarPorCriterios(StatusNotaCredito e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String actualizar(StatusNotaCredito e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String guardar(StatusNotaCredito e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminar(StatusNotaCredito e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminarListado(List<StatusNotaCredito> listado) {
		// TODO Auto-generated method stub
		return null;
	}

}
