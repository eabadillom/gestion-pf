package mx.com.ferbo.dao;

import java.util.List;

import javax.persistence.EntityManager;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.StatusFactura;
import mx.com.ferbo.util.EntityManagerUtil;

public class StatusFacturaDAO extends IBaseDAO<StatusFactura, Integer>{

	@Override
	public StatusFactura buscarPorId(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<StatusFactura> buscarTodos() {
		// TODO Auto-generated method stub
		EntityManager em = EntityManagerUtil.getEntityManager();
		return em.createNamedQuery("StatusFactura.findAll", StatusFactura.class).getResultList();
		}

	@Override
	public List<StatusFactura> buscarPorCriterios(StatusFactura e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String actualizar(StatusFactura e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String guardar(StatusFactura e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminar(StatusFactura e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminarListado(List<StatusFactura> listado) {
		// TODO Auto-generated method stub
		return null;
	}

}
