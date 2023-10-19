package mx.com.ferbo.controller;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.ConstanciaServicioDetalle;
import mx.com.ferbo.util.EntityManagerUtil;

public class ConstanciaServicioDetalleDAO extends IBaseDAO<ConstanciaServicioDetalle, Integer> {
	private static Logger log = LogManager.getLogger(ConstanciaServicioDetalleDAO.class);
	
	EntityManager em = null;
	
	public ConstanciaServicioDetalleDAO() {
	}
	
	public ConstanciaServicioDetalleDAO(EntityManager em) {
		this.em = em;
	}

	@Override
	public ConstanciaServicioDetalle buscarPorId(Integer id) {
		ConstanciaServicioDetalle servicio = null;
		EntityManager manager = null;
		
		try {
			manager = EntityManagerUtil.getEntityManager();
			servicio = manager.find(ConstanciaServicioDetalle.class, id);
		} catch(Exception ex) {
			log.error("Problema para obtener Constancia Servicio Detalle...", ex);
		} finally {
			EntityManagerUtil.close(manager);
		}
		
		return servicio;
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
		EntityManager entity = null;
		EntityTransaction transaction = null;
		ConstanciaServicioDetalle servicio = null;
		try {
			entity = EntityManagerUtil.getEntityManager();
			transaction = entity.getTransaction();
			transaction.begin();
			servicio = entity.merge(e);
			transaction.commit();
		} catch(Exception ex) {
			log.error("Problema para actualizar Constancia Servicio Detalle...", ex);
			EntityManagerUtil.rollback(entity);
		} finally {
			EntityManagerUtil.close(entity);
		}
		
		return null;
	}

	@Override
	public String guardar(ConstanciaServicioDetalle e) {
		EntityManager entity = null;
		EntityTransaction transaction = null;
		
		try {
			entity = EntityManagerUtil.getEntityManager();
			transaction = entity.getTransaction();
			transaction.begin();
			entity.persist(e);
			transaction.commit();
		} catch(Exception ex) {
			log.error("Problema para agregar Constancia Servicio Detalle...", ex);
			EntityManagerUtil.rollback(entity);
		} finally {
			EntityManagerUtil.close(entity);
		}
		
		
		return null;
	}

	@Override
	public String eliminar(ConstanciaServicioDetalle e) {
		EntityManager entity = null;
		EntityTransaction transaction = null;
		ConstanciaServicioDetalle csd = null;
		try {
			entity = EntityManagerUtil.getEntityManager();
			transaction = entity.getTransaction();
			transaction.begin();
			csd = entity.find(ConstanciaServicioDetalle.class, e.getConstanciaServicioDetalleCve());
			entity.remove(csd);
			transaction.commit();
		} catch(Exception ex) {
			log.error("Problema para eliminar Constancia Servicio Detalle...", ex);
			EntityManagerUtil.rollback(entity);
		} finally {
			EntityManagerUtil.close(entity);
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
