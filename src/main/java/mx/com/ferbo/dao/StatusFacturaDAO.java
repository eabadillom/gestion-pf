package mx.com.ferbo.dao;

import java.util.List;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.StatusFactura;
import mx.com.ferbo.util.EntityManagerUtil;

public class StatusFacturaDAO extends IBaseDAO<StatusFactura, Integer> {
	private static Logger log = LogManager.getLogger(StatusFacturaDAO.class);

	@Override
	public StatusFactura buscarPorId(Integer id) {
		EntityManager entity = null;
		StatusFactura sf = null;
		try {
			entity = EntityManagerUtil.getEntityManager();
			sf = entity.createNamedQuery("StatusFactura.findById", StatusFactura.class).setParameter("id", id)
					.getSingleResult();
		} catch (Exception ex) {
			log.error("Problema al buscar el status factura...", ex);
		} finally {
			EntityManagerUtil.close(entity);
		}

		return sf;
	}

	@Override
	public List<StatusFactura> buscarTodos() {
		EntityManager em = EntityManagerUtil.getEntityManager();
		return em.createNamedQuery("StatusFactura.findAll", StatusFactura.class).getResultList();
	}

	@Override
	public List<StatusFactura> buscarPorCriterios(StatusFactura e) {
		return null;
	}

	@Override
	public String actualizar(StatusFactura e) {
		return null;
	}

	@Override
	public String guardar(StatusFactura e) {
		return null;
	}

	@Override
	public String eliminar(StatusFactura e) {
		return null;
	}

	@Override
	public String eliminarListado(List<StatusFactura> listado) {
		return null;
	}

}
