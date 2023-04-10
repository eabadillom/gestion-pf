package mx.com.ferbo.dao;

import static mx.com.ferbo.util.EntityManagerUtil.getEntityManager;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.Camara;
import mx.com.ferbo.model.ConstanciaTraspaso;
import mx.com.ferbo.model.Posicion;
import mx.com.ferbo.model.TraspasoPartida;
import mx.com.ferbo.util.EntityManagerUtil;

public class TraspasoPartidaDAO extends IBaseDAO<TraspasoPartida, Integer>{
	
	@SuppressWarnings("unchecked")
	public List<TraspasoPartida> findall() {
		EntityManager entity = getEntityManager();
		List<TraspasoPartida> tp = null;
		Query sql = entity.createNamedQuery("TraspasoPartida.findAll", TraspasoPartida.class);
		tp = sql.getResultList();
		return tp;
	}
	
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

	public List<TraspasoPartida> buscarPorConstancia(ConstanciaTraspaso ct) {
		// TODO Auto-generated method stub
		EntityManager em = EntityManagerUtil.getEntityManager();
		return em.createNamedQuery("TraspasoPartida.findByTraspaso", TraspasoPartida.class).
				setParameter("traspaso", ct.getId()).
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
