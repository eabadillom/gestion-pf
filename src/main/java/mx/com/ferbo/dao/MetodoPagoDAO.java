package mx.com.ferbo.dao;

import java.util.List;

import javax.persistence.EntityManager;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.MetodoPago;
import mx.com.ferbo.util.EntityManagerUtil;

public class MetodoPagoDAO extends IBaseDAO<MetodoPago,String>{

	@Override
	public MetodoPago buscarPorId(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MetodoPago> buscarTodos() {
		// TODO Auto-generated method stub
		EntityManager em = EntityManagerUtil.getEntityManager();
		List<MetodoPago> lista = null;
		lista = em.createNamedQuery("MetodoPago.findAll", MetodoPago.class).getResultList();
		
		return lista;
	}

	@Override
	public List<MetodoPago> buscarPorCriterios(MetodoPago e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String actualizar(MetodoPago e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String guardar(MetodoPago e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminar(MetodoPago e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminarListado(List<MetodoPago> listado) {
		// TODO Auto-generated method stub
		return null;
	}

}
