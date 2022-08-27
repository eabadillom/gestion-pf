package mx.com.ferbo.dao;

import java.util.List;

import javax.persistence.EntityManager;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.TraspasoPartida;
import mx.com.ferbo.util.EntityManagerUtil;

public class TraspasoPartidaDAO extends IBaseDAO<TraspasoPartida, Integer>{

	@Override
	public TraspasoPartida buscarPorId(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TraspasoPartida> buscarTodos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TraspasoPartida> buscarPorCriterios(TraspasoPartida e) {
		// TODO Auto-generated method stub
		EntityManager em = EntityManagerUtil.getEntityManager();
		return em.createNamedQuery("TraspasoPartida.findByPartida", TraspasoPartida.class).
				setParameter("partidaCve", e.getPartida().getPartidaCve()).
				getResultList();
	}

	@Override
	public String actualizar(TraspasoPartida e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String guardar(TraspasoPartida e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminar(TraspasoPartida e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminarListado(List<TraspasoPartida> listado) {
		// TODO Auto-generated method stub
		return null;
	}
	

}
