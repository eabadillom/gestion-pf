package mx.com.ferbo.controller;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.ConstanciaServicioDetalle;
import mx.com.ferbo.util.EntityManagerUtil;

public class ConstanciaServicioDetalleDAO extends IBaseDAO<ConstanciaServicioDetalle, Integer> {
	
	EntityManager em = null;
	
	public ConstanciaServicioDetalleDAO() {
	}
	
	public ConstanciaServicioDetalleDAO(EntityManager em) {
		this.em = em;
	}

	@Override
	public ConstanciaServicioDetalle buscarPorId(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ConstanciaServicioDetalle> buscarTodos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ConstanciaServicioDetalle> buscarPorCriterios(ConstanciaServicioDetalle e) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public List<ConstanciaServicioDetalle> buscarPorFolio(Integer folio) {
		List<ConstanciaServicioDetalle> alServicios = null;
		try {
			alServicios = em.createNamedQuery("ConstanciaServicioDetalle.findByFolio", ConstanciaServicioDetalle.class)
					.setParameter("folio", folio)
					.getResultList()
					;
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
		return alServicios;
	}

	@Override
	public String actualizar(ConstanciaServicioDetalle e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String guardar(ConstanciaServicioDetalle e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminar(ConstanciaServicioDetalle e) {
		EntityManager entity = null;
		EntityTransaction transaction = null;
		try {
			entity = EntityManagerUtil.getEntityManager();
			transaction = entity.getTransaction();
			transaction.begin();
			entity.remove(e);
			transaction.commit();
		} finally {
			transaction.rollback();
			entity.close();
		}
	
		return null;
	}

	@Override
	public String eliminarListado(List<ConstanciaServicioDetalle> listado) {
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
