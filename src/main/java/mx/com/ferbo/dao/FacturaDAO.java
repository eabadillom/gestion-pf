package mx.com.ferbo.dao;

import java.util.List;

import javax.persistence.EntityManager;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.Factura;
import mx.com.ferbo.model.StatusFactura;
import mx.com.ferbo.util.EntityManagerUtil;

public class FacturaDAO extends IBaseDAO<Factura, Integer>{

	@Override
	public Factura buscarPorId(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Factura> buscarTodos() {
		// TODO Auto-generated method stub
		EntityManager em = EntityManagerUtil.getEntityManager();
		return em.createNamedQuery("Factura.findAll", Factura.class).getResultList();
	}

	@Override
	public List<Factura> buscarPorCriterios(Factura e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String actualizar(Factura e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String guardar(Factura e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminar(Factura e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminarListado(List<Factura> listado) {
		// TODO Auto-generated method stub
		return null;
	}

}
