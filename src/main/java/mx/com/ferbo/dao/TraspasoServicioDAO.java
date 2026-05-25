package mx.com.ferbo.dao;

import java.util.List;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.ConstanciaTraspaso;
import mx.com.ferbo.model.TraspasoServicio;
import mx.com.ferbo.util.EntityManagerUtil;

public class TraspasoServicioDAO extends IBaseDAO<TraspasoServicioDAO, Integer> {

	private static Logger log = LogManager.getLogger(TraspasoServicioDAO.class);

	@Override
	public TraspasoServicioDAO buscarPorId(Integer id) {
		return null;
	}

	public List<TraspasoServicio> buscarPorConstancia(ConstanciaTraspaso ct) {

		EntityManager em = null;
		List<TraspasoServicio> lista = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			lista = em.createNamedQuery("TraspasoServicio.findByTraspaso", TraspasoServicio.class)
					.setParameter("traspaso", ct.getId()).getResultList();
		} catch (Exception e) {
			log.error("Problema al buscar por contsancia traspaso servicio", e);
		} finally {
			EntityManagerUtil.close(em);
		}

		return lista;
	}

	@Override
	public List<TraspasoServicioDAO> buscarTodos() {
		return null;
	}

	@Override
	public List<TraspasoServicioDAO> buscarPorCriterios(TraspasoServicioDAO e) {
		return null;
	}

	@Override
	public String actualizar(TraspasoServicioDAO e) {
		return null;
	}

	@Override
	public String guardar(TraspasoServicioDAO e) {
		return null;
	}

	@Override
	public String eliminar(TraspasoServicioDAO e) {
		return null;
	}

	@Override
	public String eliminarListado(List<TraspasoServicioDAO> listado) {
		return null;
	}

}
