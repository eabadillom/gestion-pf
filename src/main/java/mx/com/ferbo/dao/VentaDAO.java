package mx.com.ferbo.dao;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.Ventas;
import mx.com.ferbo.util.EntityManagerUtil;

public class VentaDAO extends IBaseDAO<Ventas, Integer> {

	@Override
	public Ventas buscarPorId(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Ventas> buscarTodos() {
		
		EntityManager em = null;
		Query query = null;
		List<Ventas> list = null;
		
		try {
			
			em = EntityManagerUtil.getEntityManager();
			query = em.createNamedQuery("Ventas.findByAll", Ventas.class);
			
			list = query.getResultList();
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}finally {
			EntityManagerUtil.close(em);
		}
		
		return list;
	}

	@Override
	public List<Ventas> buscarPorCriterios(Ventas e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String actualizar(Ventas e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String guardar(Ventas e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminar(Ventas e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminarListado(List<Ventas> listado) {
		// TODO Auto-generated method stub
		return null;
	}

}
