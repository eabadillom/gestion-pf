package mx.com.ferbo.dao;


import java.util.List;

import javax.persistence.EntityManager;

import org.jfree.util.Log;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.TipoCobro;
import mx.com.ferbo.util.EntityManagerUtil;

public class TipoCobroDAO extends IBaseDAO<TipoCobro, Integer> {

	@Override
	public TipoCobro buscarPorId(Integer id) {
		TipoCobro model = null;
		EntityManager em = null;
		
		try {
			em = EntityManagerUtil.getEntityManager();
			model = em.find(TipoCobro.class, id);
		} catch(Exception ex) {
			Log.warn("Problema para obtener el tipo de cobro " + id, ex);
		} finally {
			EntityManagerUtil.close(em);
		}
		
		return model;
	}

	@Override
	public List<TipoCobro> buscarTodos() {
		List<TipoCobro> listado = null;
		EntityManager em = EntityManagerUtil.getEntityManager();
		listado = em.createNamedQuery("TipoCobro.findAll", TipoCobro.class).getResultList();
		return listado;
	}

	@Override
	public List<TipoCobro> buscarPorCriterios(TipoCobro e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String actualizar(TipoCobro e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String guardar(TipoCobro e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminar(TipoCobro e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminarListado(List<TipoCobro> listado) {
		// TODO Auto-generated method stub
		return null;
	}

}
