package mx.com.ferbo.dao;

import java.util.List;

import javax.persistence.EntityManager;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.UdCobro;
import mx.com.ferbo.util.EntityManagerUtil;

public class UdCobroDAO extends IBaseDAO<UdCobro, Integer>{

	@Override
	public UdCobro buscarPorId(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UdCobro> buscarTodos() {
		// TODO Auto-generated method stub
		EntityManager em = EntityManagerUtil.getEntityManager();
		return em.createNamedQuery("UdCobro.findAll", UdCobro.class).getResultList();
	}

	@Override
	public List<UdCobro> buscarPorCriterios(UdCobro e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String actualizar(UdCobro e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String guardar(UdCobro e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminar(UdCobro e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminarListado(List<UdCobro> listado) {
		// TODO Auto-generated method stub
		return null;
	}

}
