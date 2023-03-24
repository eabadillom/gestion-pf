package mx.com.ferbo.dao;

import java.util.List;

import javax.persistence.EntityManager;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.UnidadDeProducto;
import mx.com.ferbo.util.EntityManagerUtil;

public class UnidadDeProductoDAO extends IBaseDAO<UnidadDeProducto, Integer>{

	@Override
	public UnidadDeProducto buscarPorId(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UnidadDeProducto> buscarTodos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UnidadDeProducto> buscarPorCriterios(UnidadDeProducto e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String actualizar(UnidadDeProducto unidadDeProducto) {
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.merge(unidadDeProducto);
			em.getTransaction().commit();
			em.close();
		} catch (Exception e) {
			System.out.println("ERROR" + e.getMessage());
			return "ERROR";
		}
		
		
		return null;
	}

	@Override
	public String guardar(UnidadDeProducto unidadDeProducto) {
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.persist(unidadDeProducto);
			em.getTransaction().commit();
			em.close();
		} catch (Exception e) {
			System.out.println("ERROR" + e.getMessage());
			return "ERROR";
		}
		return null;
	}

	@Override
	public String eliminar(UnidadDeProducto e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminarListado(List<UnidadDeProducto> listado) {
		// TODO Auto-generated method stub
		return null;
	}

}
