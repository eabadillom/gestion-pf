package mx.com.ferbo.dao;

import java.util.List;

import javax.persistence.EntityManager;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.ConstanciaDepositoDetalle;
import mx.com.ferbo.util.EntityManagerUtil;

public class ConstanciaDepositoDetalleDAO extends IBaseDAO<ConstanciaDepositoDetalle,Integer>{

	@Override
	public ConstanciaDepositoDetalle buscarPorId(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ConstanciaDepositoDetalle> buscarTodos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ConstanciaDepositoDetalle> buscarPorCriterios(ConstanciaDepositoDetalle e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String actualizar(ConstanciaDepositoDetalle e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String guardar(ConstanciaDepositoDetalle constanciaDD) {
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.persist(constanciaDD);
			em.getTransaction().commit();
			em.close();
		} catch (Exception e) {
			System.out.println("ERROR" + e.getMessage());
			return "ERROR";
		}
		return null;
	}

	@Override
	public String eliminar(ConstanciaDepositoDetalle e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminarListado(List<ConstanciaDepositoDetalle> listado) {
		// TODO Auto-generated method stub
		return null;
	}

}
