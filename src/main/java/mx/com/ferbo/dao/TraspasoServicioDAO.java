package mx.com.ferbo.dao;

import java.util.List;

import javax.persistence.EntityManager;

import org.apache.log4j.Logger;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.ConstanciaTraspaso;
import mx.com.ferbo.model.TraspasoPartida;
import mx.com.ferbo.model.TraspasoServicio;
import mx.com.ferbo.util.EntityManagerUtil;

public class TraspasoServicioDAO extends IBaseDAO<TraspasoServicioDAO, Integer>{
	
	private static Logger log = Logger.getLogger(TraspasoServicioDAO.class);

	@Override
	public TraspasoServicioDAO buscarPorId(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}
	public List<TraspasoServicio> buscarPorConstancia(ConstanciaTraspaso ct) {
		// TODO Auto-generated method stub
		
		EntityManager em = null;
		List<TraspasoServicio> lista = null; 
		try {
			em = EntityManagerUtil.getEntityManager();
			lista = em.createNamedQuery("TraspasoServicio.findByTraspaso", TraspasoServicio.class).
			setParameter("traspaso", ct.getId()).
			getResultList();
		} catch (Exception e) {
			log.error("Problema al buscar por contsancia traspaso servicio", e);
		}finally {
			EntityManagerUtil.close(em);
		}
		
		
		return lista;
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
