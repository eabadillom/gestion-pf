package mx.com.ferbo.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.EstadoConstancia;
import mx.com.ferbo.util.EntityManagerUtil;

public class EstadoConstanciaDAO extends IBaseDAO<EstadoConstancia, Integer> {

	@Override
	public EstadoConstancia buscarPorId(Integer id) {
		EstadoConstancia bean = null;
		EntityManager em = null;
		Query query = null;

		try {
			em = EntityManagerUtil.getEntityManager();
			query = em.createNamedQuery("EstadoConstancia.findByEdoCve", EstadoConstancia.class).setParameter("edoCve",
					id);
			bean = (EstadoConstancia) query.getSingleResult();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (em != null)
				em.close();
		}
		return bean;
	}

	@Override
	public List<EstadoConstancia> buscarTodos() {
		EntityManager entity = null;
		List<EstadoConstancia> alEstados = null;
		try {
			entity = EntityManagerUtil.getEntityManager();
			alEstados = entity.createNamedQuery("EstadoConstancia.findAll", EstadoConstancia.class).getResultList();
		} finally {
			EntityManagerUtil.close(entity);
		}
		return alEstados;
	}

	@Override
	public List<EstadoConstancia> buscarPorCriterios(EstadoConstancia e) {
		return null;
	}

	@Override
	public String actualizar(EstadoConstancia e) {
		return null;
	}

	@Override
	public String guardar(EstadoConstancia e) {
		return null;
	}

	@Override
	public String eliminar(EstadoConstancia e) {
		return null;
	}

	@Override
	public String eliminarListado(List<EstadoConstancia> listado) {
		return null;
	}

}
