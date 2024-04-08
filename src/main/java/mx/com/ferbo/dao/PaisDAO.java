package mx.com.ferbo.dao;

import java.util.List;

import javax.persistence.EntityManager;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.Pais;
import mx.com.ferbo.util.EntityManagerUtil;

public class PaisDAO extends IBaseDAO<Pais, Integer> {

	@Override
	public Pais buscarPorId(Integer id) {
		return null;
	}

	@Override
	public List<Pais> buscarTodos() {
		List<Pais> listado;
		EntityManager em = EntityManagerUtil.getEntityManager();
		listado = em.createNamedQuery("Pais.findAll", Pais.class).getResultList();
		return listado;
	}

	@Override
	public List<Pais> buscarPorCriterios(Pais e) {
		return null;
	}

	@Override
	public String actualizar(Pais e) {
		return null;
	}

	@Override
	public String guardar(Pais e) {
		return null;
	}

	@Override
	public String eliminar(Pais e) {
		return null;
	}

	@Override
	public String eliminarListado(List<Pais> listado) {
		return null;
	}

}
