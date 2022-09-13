package mx.com.ferbo.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.PartidaServicio;
import mx.com.ferbo.util.EntityManagerUtil;

public class PartidaServicioDAO extends IBaseDAO<PartidaServicio, Integer>{
	
	EntityManager em = null;
	
	public PartidaServicioDAO() {
	}
	
	public PartidaServicioDAO(EntityManager em) {
		this.em = em;
	}

	@Override
	public PartidaServicio buscarPorId(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PartidaServicio> buscarTodos() {
		// TODO Auto-generated method stub
		return null;
	}
	
	//@SuppressWarnings("unchecked")
	public List<PartidaServicio> buscarPorFolio(Integer folio) {
		List<PartidaServicio> alPartidas = null;
		try {
			alPartidas = em.createNamedQuery("PartidaServicio.findByFolio", PartidaServicio.class)
				.setParameter("folio", folio)
				.getResultList()
				;
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
		return alPartidas;
	}

	@Override
	public List<PartidaServicio> buscarPorCriterios(PartidaServicio e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String actualizar(PartidaServicio e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String guardar(PartidaServicio e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminar(PartidaServicio e) {
			em = EntityManagerUtil.getEntityManager();
			em.remove(e);
		return null;
	}

	@Override
	public String eliminarListado(List<PartidaServicio> listado) {
		// TODO Auto-generated method stub
		return null;
	}

	public EntityManager getEntityManager() {
		return em;
	}

	public void setEntityManager(EntityManager em) {
		this.em = em;
	}
}
