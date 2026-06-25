package mx.com.ferbo.dao;

import java.util.List;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.DetallePartida;
import mx.com.ferbo.util.EntityManagerUtil;

public class DetallePartidaDAO extends IBaseDAO<DetallePartida, Integer> {
	private static Logger log = LogManager.getLogger(DetallePartidaDAO.class);

	@Override
	public DetallePartida buscarPorId(Integer id) {
		return null;
	}

	@Override
	public List<DetallePartida> buscarTodos() {
		return null;
	}

	@Override
	public List<DetallePartida> buscarPorCriterios(DetallePartida e) {
		return null;
	}

	@Override
	public String actualizar(DetallePartida detallePartida) {

		EntityManager em = null;

		try {

			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.merge(detallePartida);
			em.getTransaction().commit();

		} catch (Exception e2) {
			log.error("Problema al actualizar detallePartida", e2);
			return "ERROR";
		} finally {
			EntityManagerUtil.close(em);
		}

		return null;
	}

	@Override
	public String guardar(DetallePartida detallePartida) {
		EntityManager em = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.persist(detallePartida);
			em.getTransaction().commit();
		} catch (Exception e) {
			log.error("Problema para guardar en Detalle Partida...", e);
			System.out.println("ERROR" + e.getCause());
			return "ERROR";
		} finally {
			EntityManagerUtil.close(em);
		}
		return null;

	}

	@Override
	public String eliminar(DetallePartida e) {
		return null;
	}

	@Override
	public String eliminarListado(List<DetallePartida> listado) {
		return null;
	}

	public List<DetallePartida> buscarPorPartida(Integer partidaCve) {
		List<DetallePartida> list = null;
		EntityManager em = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			list = em.createNamedQuery("DetallePartida.findByPartidaCve", DetallePartida.class)
					.setParameter("partidaCve", partidaCve).getResultList();
		} catch (Exception ex) {
			log.error("Problema para obtener el listado de detalle partida...", ex);
		} finally {
			EntityManagerUtil.close(em);
		}
		return list;
	}
}
