package mx.com.ferbo.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.ConstanciaDeServicio;
import mx.com.ferbo.model.ConstanciaServicioDetalle;
import mx.com.ferbo.model.PartidaServicio;
import mx.com.ferbo.util.EntityManagerUtil;

public class ConstanciaServicioDAO extends IBaseDAO<ConstanciaDeServicio, Integer> {
	private static Logger log = LogManager.getLogger(ConstanciaServicioDAO.class);
	
	public EntityManager em = null;
	
	public ConstanciaServicioDAO() {
	}
	
	public ConstanciaServicioDAO(EntityManager em) {
		this.em = em;
	}

	@Override
	public ConstanciaDeServicio buscarPorId(Integer id) {
		EntityManager em = null;
		ConstanciaDeServicio constancia = null;
		
		try {
			em = EntityManagerUtil.getEntityManager();
			constancia = em.find(ConstanciaDeServicio.class, id);
			
		} catch(Exception ex) {
			log.error("Problema para consultar la constancia...", ex);
		} finally {
			EntityManagerUtil.close(em);
		}
		
		return constancia;
	}
	
	public ConstanciaDeServicio buscarPorId(Integer id, boolean isFullInfo) {
		EntityManager em = null;
		ConstanciaDeServicio constancia = null;
		
		try {
			em = EntityManagerUtil.getEntityManager();
			constancia = em.find(ConstanciaDeServicio.class, id);
			
			if(isFullInfo == false)
				return constancia;
			
			log.debug("Estado constancia: {}", constancia.getStatus().getEdoCve());
			
			List<PartidaServicio> psList = constancia.getPartidaServicioList();
			log.debug("Partidas Servicio: {}",psList.size());
			for(PartidaServicio ps : psList) {
				log.debug("Partida Servicio Id: {}", ps.getPartidaCve());
			}
			
			List<ConstanciaServicioDetalle> csdList = constancia.getConstanciaServicioDetalleList();
			log.debug("Constancia Servicio Detalle: {}", csdList.size());
			for(ConstanciaServicioDetalle csd : csdList) {
				log.debug("Constancia Servicio Detalle: {}", csd.getConstanciaServicioDetalleCve());
			}
			
		} catch(Exception ex) {
			log.error("Problema para consultar la constancia...", ex);
		} finally {
			EntityManagerUtil.close(em);
		}
		
		return constancia;
	}
	
	public List<ConstanciaDeServicio> buscarPorFolioCliente(String folioCliente) {
		List<ConstanciaDeServicio> alConstancias = null;
		EntityManager em = null;
		
		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			alConstancias = em.createNamedQuery("ConstanciaDeServicio.findByFolioCliente", ConstanciaDeServicio.class)
					.setParameter("folioCliente", folioCliente)
					.getResultList();
			
			em.getTransaction().commit();
		} catch(Exception ex) {
			EntityManagerUtil.rollback(em);
		} finally {
			EntityManagerUtil.close(em);
		}
		
		return alConstancias;
	}

	@Override
	public List<ConstanciaDeServicio> buscarTodos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ConstanciaDeServicio> buscarPorCriterios(ConstanciaDeServicio constanciaDeServicio) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<ConstanciaDeServicio> buscar(Date fechaInicio, Date fechaFin, Integer idCliente, String folioCliente) {
		List<ConstanciaDeServicio> resultList = null;
		EntityManager em = null;
		
		try {
			if(folioCliente != null && folioCliente.contains("%") == false)
				folioCliente = "%".concat(folioCliente).concat("%");
			
			em = EntityManagerUtil.getEntityManager();
			
			resultList = em.createNativeQuery("SELECT * FROM (\n"
					+ "	SELECT * FROM (\n"
					+ "		SELECT * FROM constancia_de_servicio cds\n"
					+ "		WHERE (:idCliente IS NULL OR cds.CTE_CVE = :idCliente)\n"
					+ "	) cdd2 WHERE ((cdd2.FECHA BETWEEN :fechaInicio AND :fechaFin) OR (:fechaInicio IS NULL OR :fechaFin IS NULL))\n"
					+ ") cs3 WHERE (:folioCliente IS NULL OR cs3.FOLIO_CLIENTE LIKE :folioCliente)", ConstanciaDeServicio.class)
					.setParameter("fechaInicio", fechaInicio)
					.setParameter("fechaFin", fechaFin)
					.setParameter("idCliente", idCliente)
					.setParameter("folioCliente", folioCliente)
					.getResultList()
					;
			
		} catch(Exception ex) {
			log.error("Problema para consultar las constancias...", ex);
		} finally {
			EntityManagerUtil.close(em);
		}
		
		return resultList;
	}
	
	@Override
	public String actualizar(ConstanciaDeServicio e) {
		EntityManager em = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.merge(e);
			em.getTransaction().commit();
		} catch(Exception ex) {
			EntityManagerUtil.rollback(em);
			log.error("Problema al actualizar la constancia de servicio...", ex);
		}finally {
			EntityManagerUtil.close(em);
		}
		return null;
	}

	@Override
	public String guardar(ConstanciaDeServicio e) {
		EntityManager em = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.persist(e);
			em.getTransaction().commit();
		} catch(Exception ex) {
			EntityManagerUtil.rollback(em);
			ex.printStackTrace();
		} finally {
			EntityManagerUtil.close(em);
		}
		return null;
	}

	@Override
	public String eliminar(ConstanciaDeServicio e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminarListado(List<ConstanciaDeServicio> listado) {
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
