package mx.com.ferbo.dao;

import java.util.List;

import javax.persistence.EntityManager;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.MedioPago;
import mx.com.ferbo.util.EntityManagerUtil;

public class MedioPagoDAO extends IBaseDAO<MedioPago,Integer>{

	@Override
	public MedioPago buscarPorId(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MedioPago> buscarTodos() {
		// TODO Auto-generated method stub
		EntityManager em = EntityManagerUtil.getEntityManager();
		List<MedioPago> lista = null;
		lista = em.createNamedQuery("MedioPago.findAll",MedioPago.class).getResultList();
		
		return lista;
	}

	@Override
	public List<MedioPago> buscarPorCriterios(MedioPago e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String actualizar(MedioPago e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String guardar(MedioPago e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminar(MedioPago e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminarListado(List<MedioPago> listado) {
		// TODO Auto-generated method stub
		return null;
	}

}
