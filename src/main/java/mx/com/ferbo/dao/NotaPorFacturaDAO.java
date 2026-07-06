package mx.com.ferbo.dao;

import java.util.List;

import javax.persistence.EntityManager;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.NotaPorFactura;
import mx.com.ferbo.util.EntityManagerUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NotaPorFacturaDAO extends IBaseDAO<NotaPorFactura, Integer> {
        private static Logger log = LogManager.getLogger(NotaPorFacturaDAO.class);

	@Override
	public NotaPorFactura buscarPorId(Integer id) {
		return null;
	}

	@Override
	public List<NotaPorFactura> buscarTodos() {
		return null;
	}

	@Override
	public List<NotaPorFactura> buscarPorCriterios(NotaPorFactura e) {
		return null;
	}

	@Override
	public String actualizar(NotaPorFactura nota) {
		EntityManager em = null;
                try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.merge(nota);
			em.getTransaction().commit();
		} catch (Exception e) {
			log.error("Error al actualizar la nota por factura... " + e.getMessage());
		} finally {
                    EntityManagerUtil.close(em);
                }

		return null;
	}

	@Override
	public String guardar(NotaPorFactura nota) {
                EntityManager em = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.persist(nota);
			em.getTransaction().commit();
		} catch (Exception e) {
			log.error("Error al guardar una nota por factura... " + e.getMessage());
			return "ERROR";
		} finally {
                    EntityManagerUtil.close(em);
                }

		return null;
	}

	@Override
	public String eliminar(NotaPorFactura e) {
		return null;
	}

	@Override
	public String eliminarListado(List<NotaPorFactura> listado) {
		return null;
	}

}
