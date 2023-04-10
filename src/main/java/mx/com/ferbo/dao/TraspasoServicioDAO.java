package mx.com.ferbo.dao;

import java.util.List;

import javax.persistence.EntityManager;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.ConstanciaTraspaso;
import mx.com.ferbo.model.TraspasoPartida;
import mx.com.ferbo.model.TraspasoServicio;
import mx.com.ferbo.util.EntityManagerUtil;

public class TraspasoServicioDAO extends IBaseDAO<TraspasoServicioDAO, Integer>{

	@Override
	public TraspasoServicioDAO buscarPorId(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}
	public List<TraspasoServicio> buscarPorConstancia(ConstanciaTraspaso ct) {
		// TODO Auto-generated method stub
		EntityManager em = EntityManagerUtil.getEntityManager();
		return em.createNamedQuery("TraspasoServicio.findByTraspaso", TraspasoServicio.class).
				setParameter("traspaso", ct.getId()).
				getResultList();
	}
	@Override
	public List<TraspasoServicioDAO> buscarTodos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TraspasoServicioDAO> buscarPorCriterios(TraspasoServicioDAO e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String actualizar(TraspasoServicioDAO e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String guardar(TraspasoServicioDAO e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminar(TraspasoServicioDAO e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminarListado(List<TraspasoServicioDAO> listado) {
		// TODO Auto-generated method stub
		return null;
	}

}
