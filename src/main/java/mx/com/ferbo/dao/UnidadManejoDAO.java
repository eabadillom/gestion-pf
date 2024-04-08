package mx.com.ferbo.dao;

import java.util.List;

import javax.persistence.EntityManager;

import org.jfree.util.Log;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.UnidadDeManejo;
import mx.com.ferbo.util.EntityManagerUtil;

public class UnidadManejoDAO extends IBaseDAO<UnidadDeManejo, Integer> {

	@Override
	public UnidadDeManejo buscarPorId(Integer id) {
		return null;
	}

	@Override
	public List<UnidadDeManejo> buscarTodos() {

		EntityManager em = null;
		List<UnidadDeManejo> listado = null;

		try {
			em = EntityManagerUtil.getEntityManager();
			listado = em.createNamedQuery("UnidadDeManejo.findAll", UnidadDeManejo.class).getResultList();
		} catch (Exception e) {
			Log.error("Problema al traer datos unidad de manejo", e);
		} finally {
			EntityManagerUtil.close(em);
		}

		return listado;
	}

	@Override
	public List<UnidadDeManejo> buscarPorCriterios(UnidadDeManejo e) {
		return null;
	}

	@Override
	public String actualizar(UnidadDeManejo e) {
		return null;
	}

	@Override
	public String guardar(UnidadDeManejo e) {
		return null;
	}

	@Override
	public String eliminar(UnidadDeManejo e) {
		return null;
	}

	@Override
	public String eliminarListado(List<UnidadDeManejo> listado) {
		return null;
	}

}
