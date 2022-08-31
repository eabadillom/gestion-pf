package mx.com.ferbo.dao;

import java.util.List;

import javax.persistence.EntityManager;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.Categoria;
import mx.com.ferbo.util.EntityManagerUtil;

public class CategoriaDAO extends IBaseDAO<Categoria, Integer>{

	@Override
	public Categoria buscarPorId(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Categoria> buscarTodos() {
		EntityManager em = EntityManagerUtil.getEntityManager();
		return em.createNamedQuery("Categoria.findAll", Categoria.class).getResultList();
	}

	@Override
	public List<Categoria> buscarPorCriterios(Categoria e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String actualizar(Categoria e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String guardar(Categoria e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminar(Categoria e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminarListado(List<Categoria> listado) {
		// TODO Auto-generated method stub
		return null;
	}

}
