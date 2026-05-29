package mx.com.ferbo.dao;

import java.util.List;

import javax.persistence.EntityManager;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.AsentamientoHumano;
import mx.com.ferbo.util.EntityManagerUtil;

public class ColoniasDAO extends IBaseDAO<AsentamientoHumano, Integer> {

	@Override
	public AsentamientoHumano buscarPorId(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AsentamientoHumano> buscarTodos() {
		List<AsentamientoHumano> listado = null;
		EntityManager em = EntityManagerUtil.getEntityManager();
		listado = em.createNamedQuery("AsentamientoHumano.findAll", AsentamientoHumano.class).getResultList();
		return listado;
	}

	@Override
	public List<AsentamientoHumano> buscarPorCriterios(AsentamientoHumano e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String actualizar(AsentamientoHumano e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String guardar(AsentamientoHumano e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminar(AsentamientoHumano e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminarListado(List<AsentamientoHumano> listado) {
		// TODO Auto-generated method stub
		return null;
	}

}
