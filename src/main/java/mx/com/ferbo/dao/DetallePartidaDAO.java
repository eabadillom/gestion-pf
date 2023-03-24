package mx.com.ferbo.dao;

import java.util.List;

import javax.persistence.EntityManager;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.DetallePartida;
import mx.com.ferbo.util.EntityManagerUtil;

public class DetallePartidaDAO extends IBaseDAO<DetallePartida, Integer> {

	@Override
	public DetallePartida buscarPorId(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DetallePartida> buscarTodos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DetallePartida> buscarPorCriterios(DetallePartida e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String actualizar(DetallePartida e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String guardar(DetallePartida detallePartida) {
		
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.persist(detallePartida);
			em.getTransaction().commit();
			em.close();
		} catch (Exception e) {
			System.out.println("ERROR" + e.getCause());
			return "ERROR";
		}
		return null;
		
	}

	@Override
	public String eliminar(DetallePartida e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminarListado(List<DetallePartida> listado) {
		// TODO Auto-generated method stub
		return null;
	}

}
