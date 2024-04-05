package mx.com.ferbo.dao;

import java.util.List;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.PreSalidaServicio;
import mx.com.ferbo.util.EntityManagerUtil;

public class PreSalidaServicioDAO extends IBaseDAO<PreSalidaServicio, Integer> {
	private static Logger log = LogManager.getLogger(PreSalidaServicioDAO.class);

	@Override
	public PreSalidaServicio buscarPorId(Integer id) {
		PreSalidaServicio pss = null;
		EntityManager em = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			pss = em.createNamedQuery("PreSalidaServicio.findByfolioSalida", PreSalidaServicio.class)
					.setParameter("folioSalida", id).getSingleResult();
		} catch (Exception ex) {
			log.error("Problema para obtener la orden de salida: " + id, ex);
		} finally {
			EntityManagerUtil.close(em);
		}
		return pss;
	}

	@Override
	public List<PreSalidaServicio> buscarTodos() {
		EntityManager em = null;
		List<PreSalidaServicio> listaSalidasServicios = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			listaSalidasServicios = em.createNamedQuery("PreSalidaServicio.findAll", PreSalidaServicio.class)
					.getResultList();
		} catch (Exception ex) {
			log.error("Problema para obtener el listado de clientes...", ex);
		} finally {
			EntityManagerUtil.close(em);
		}

		return listaSalidasServicios;
	}

	@Override
	public List<PreSalidaServicio> buscarPorCriterios(PreSalidaServicio e) {
		return null;
	}

	public List<PreSalidaServicio> buscarPorFolios(String folio) {
		List<PreSalidaServicio> listaSalidaServicios = null;
		EntityManager em = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			listaSalidaServicios = em.createNamedQuery("PreSalidaServicio.findByfolioSalida", PreSalidaServicio.class)
					.setParameter("folioSalida", folio).getResultList();
		} catch (Exception e) {
			log.error("Problemas para obtener el estado de resultados", e);
		} finally {
			EntityManagerUtil.close(em);
		}
		return listaSalidaServicios;
	}

	@Override
	public String actualizar(PreSalidaServicio e) {
		return null;
	}

	@Override
	public String guardar(PreSalidaServicio e) {
		return null;
	}

	@Override
	public String eliminar(PreSalidaServicio e) {
		return null;
	}

	@Override
	public String eliminarListado(List<PreSalidaServicio> listado) {
		return null;
	}

}
