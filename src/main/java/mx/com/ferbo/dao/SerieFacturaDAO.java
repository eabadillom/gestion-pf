package mx.com.ferbo.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.model.EmisoresCFDIS;
import mx.com.ferbo.model.SerieFactura;
import mx.com.ferbo.model.StatusSerie;
import mx.com.ferbo.util.EntityManagerUtil;

public class SerieFacturaDAO {
	private static Logger log = LogManager.getLogger(SerieFacturaDAO.class);

	public List<SerieFactura> findAll() {
		EntityManager entity = null;
		List<SerieFactura> list = null;
		
		try {
			entity = EntityManagerUtil.getEntityManager();
			list = entity.createNamedQuery("SerieFactura.findAll", SerieFactura.class).getResultList();
		}catch(Exception e) {
			log.error("Error al obtener informacion",e);
		}finally {
			EntityManagerUtil.close(entity);
		}
		return list;
	}
	
	public List<SerieFactura> findAll(boolean isFullInfo) {
		EntityManager entity = null;
		List<SerieFactura> modelList = null;
		
		try {
			entity = EntityManagerUtil.getEntityManager();
			modelList = entity.createNamedQuery("SerieFactura.findAll", SerieFactura.class)
					.getResultList()
					;
			
			if(isFullInfo == false)
				return modelList;
			
			for(SerieFactura model : modelList) {
				log.debug("Emisor: {}", model.getEmisor().getCd_emisor());
			}
			
		}catch(Exception e) {
			log.error("Error al obtener informacion",e);
		}finally {
			EntityManagerUtil.close(entity);
		}
		return modelList;
	}
	
	public SerieFactura findById(Integer idSerie) {
		
		EntityManager em = null;
		SerieFactura serie = null;
		Query query = null;
		
		try {
			em = EntityManagerUtil.getEntityManager();
			query = em.createNamedQuery("SerieFactura.findById", SerieFactura.class).setParameter("id",idSerie);
			
			serie = (SerieFactura) query.getSingleResult();
			
		} catch (Exception e) {
			log.info("Error al buscar Serie Factura por ID", e.getMessage());
			return null;
		}finally {
			EntityManagerUtil.close(em);
		}
		
		return serie;
	}

	public List<StatusSerie> findStatus() {
		EntityManager entity = EntityManagerUtil.getEntityManager();
		List<StatusSerie> list = null;
	try {
		entity = EntityManagerUtil.getEntityManager();
		list = entity.createNamedQuery("StatusSerie.findAll", StatusSerie.class).getResultList();
		}catch(Exception e) {
			log.error("Error al obtener informacion",e);
		}finally {
			EntityManagerUtil.close(entity);
		}
	
		return list;
	};

	public String save(SerieFactura sF) {
		EntityManager entity = null;
		try {
			 entity = EntityManagerUtil.getEntityManager();
			entity.getTransaction().begin();
			entity.persist(sF);
			entity.getTransaction().commit();
			entity.close();
		} catch (Exception e) {
			return e.getMessage();
		}finally {
			EntityManagerUtil.close(entity);
		}
		return null;
	};

	public String update(SerieFactura sF) {
		EntityManager entity = null;
		try {
			 entity = EntityManagerUtil.getEntityManager();
			entity.getTransaction().begin();
			entity.merge(sF);
			entity.getTransaction().commit();
		} catch (Exception e) {
			return e.getMessage();
		}finally {
			EntityManagerUtil.close(entity);
		}
		return null;
	};

	public String cancelar(int id) {
		EntityManager entity = null;
		try {
		 entity = EntityManagerUtil.getEntityManager();
		entity.getTransaction().begin();
		Query sql = entity.createNativeQuery("update serie_factura set status_serie = 3 where id = ?;");
		sql.setParameter(1, id);
		sql.executeUpdate();
		entity.getTransaction().commit();
		}catch(Exception e) {
			log.error("Error al obtener informacion",e);
		}finally {
			EntityManagerUtil.close(entity);
		}
		return null;
	};

	public String delete(SerieFactura sF) {
		EntityManager entity = null;
		try {
			 entity = EntityManagerUtil.getEntityManager();
			entity.getTransaction().begin();
			entity.remove(entity.merge(sF));
			entity.getTransaction().commit();
		} catch (Exception e) {
			return e.getMessage();
		}finally {
			EntityManagerUtil.close(entity);
		}
		return null;
	}

	public List<SerieFactura> buscarPorEmisor(EmisoresCFDIS idEmisoresCFDIS) {
		List<SerieFactura> modelList = null;
		EntityManager em = null;
		
		try {
			em = EntityManagerUtil.getEntityManager();
			modelList = em.createNamedQuery("SerieFactura.findByEmisor", SerieFactura.class)
					.setParameter("idEmisor", idEmisoresCFDIS.getCd_emisor())
					.getResultList()
					;
			
		} catch(Exception ex) {
			log.warn("Problema para obtener el listado de series de facturas...", ex);
		} finally {
			EntityManagerUtil.close(em);
		}
		
		return modelList;
	}
	
	public List<SerieFactura> buscarPorEmisor(EmisoresCFDIS idEmisoresCFDIS, boolean isFullInfo) {
		List<SerieFactura> modelList = null;
		EntityManager em = null;
		
		try {
			em = EntityManagerUtil.getEntityManager();
			modelList = em.createNamedQuery("SerieFactura.findByEmisor", SerieFactura.class)
					.setParameter("idEmisor", idEmisoresCFDIS.getCd_emisor())
					.getResultList()
					;
			
			if(isFullInfo == false)
				return modelList;
			
			//TODO pendiente la logica para obtener datos dependientes...
			
		} catch(Exception ex) {
			log.warn("Problema para obtener el listado de series de facturas...", ex);
		} finally {
			EntityManagerUtil.close(em);
		}
		
		return modelList;
	}

}
