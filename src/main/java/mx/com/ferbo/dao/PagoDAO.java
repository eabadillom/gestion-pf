package mx.com.ferbo.dao;

import java.util.List;

import javax.persistence.EntityManager;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.Pago;
import mx.com.ferbo.util.EntityManagerUtil;

public class PagoDAO extends IBaseDAO<Pago, Integer> {

	@Override
	public Pago buscarPorId(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Pago> buscarTodos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Pago> buscarPorCriterios(Pago e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String actualizar(Pago e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String guardar(Pago e) {
		// TODO Auto-generated method stub
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.persist(e);
			em.getTransaction().commit();
		} catch (Exception ex) {
			System.out.println("ERROR" + ex.getMessage());
			return "ERROR";
		}
		return null;

	}

	@Override
	public String eliminar(Pago e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminarListado(List<Pago> listado) {
		// TODO Auto-generated method stub
		return null;
	}

}
