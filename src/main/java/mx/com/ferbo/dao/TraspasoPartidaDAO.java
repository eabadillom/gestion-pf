package mx.com.ferbo.dao;

import static mx.com.ferbo.util.EntityManagerUtil.getEntityManager;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.jfree.util.Log;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.ConstanciaTraspaso;
import mx.com.ferbo.model.TraspasoPartida;
import mx.com.ferbo.util.EntityManagerUtil;

public class TraspasoPartidaDAO extends IBaseDAO<TraspasoPartida, Integer> {

	@SuppressWarnings("unchecked")
	public List<TraspasoPartida> findall() {

		EntityManager entity = null;
		List<TraspasoPartida> tp = null;

		try {
			entity = getEntityManager();

			Query sql = entity.createNamedQuery("TraspasoPartida.findAll", TraspasoPartida.class);
			tp = sql.getResultList();
		} catch (Exception e) {
			Log.error("problema al obtener listado de traspasos partidas", e);
		} finally {
			EntityManagerUtil.close(entity);
		}

		return tp;
	}

	@Override
	public TraspasoPartida buscarPorId(Integer id) {
		return null;
	}

	@Override
	public List<TraspasoPartida> buscarTodos() {
		return null;
	}

	@Override
	public List<TraspasoPartida> buscarPorCriterios(TraspasoPartida e) {

		EntityManager em = null;
		List<TraspasoPartida> lista = null;

		try {
			em = EntityManagerUtil.getEntityManager();
			lista = em.createNamedQuery("TraspasoPartida.findByPartida", TraspasoPartida.class)
					.setParameter("partidaCve", e.getPartida().getPartidaCve()).getResultList();
		} catch (Exception e2) {
			Log.error("problema al buscar por criterios", e2);
		} finally {
			EntityManagerUtil.close(em);
		}

		return lista;
	}

	public List<TraspasoPartida> buscarPorConstancia(ConstanciaTraspaso ct) {

		EntityManager em = null;
		List<TraspasoPartida> lista = null;

		try {
			em = EntityManagerUtil.getEntityManager();
			lista = em.createNamedQuery("TraspasoPartida.findByTraspaso", TraspasoPartida.class)
					.setParameter("traspaso", ct.getId()).getResultList();
		} catch (Exception e) {
			Log.error("Problema al buscar por constancia traspaso", e);
		} finally {
			EntityManagerUtil.close(em);
		}

		return lista;
	}

	@Override
	public String actualizar(TraspasoPartida e) {
		return null;
	}

	@Override
	public String guardar(TraspasoPartida e) {
		return null;
	}

	@Override
	public String eliminar(TraspasoPartida e) {
		return null;
	}

	@Override
	public String eliminarListado(List<TraspasoPartida> listado) {
		return null;
	}

}
