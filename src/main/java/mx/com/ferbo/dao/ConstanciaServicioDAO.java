package mx.com.ferbo.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.Cliente;
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

	@SuppressWarnings("unchecked")
	public List<ConstanciaDeServicio> buscarPorCriterio(String folioCliente, Date fechaInico, Date fechaFin, int idCliente) {
		Cliente cliente = new Cliente();
		Map<String, Object> paramaterMap = new HashMap<String, Object>();
		List<String> whereCause = new ArrayList<String>();
		StringBuilder queryBuilder = new StringBuilder();
		
		try {
			Query q = null;
			queryBuilder.append("SELECT cds FROM ConstanciaDeServicio cds");

			if (fechaInico != null && fechaFin != null) {
				whereCause.add("(cds.fecha BETWEEN :fechaInicio AND :fechaFinal)");
				paramaterMap.put("fechaInicio", fechaInico);
				paramaterMap.put("fechaFinal", fechaFin);
			}
			if (folioCliente != null && !"".equalsIgnoreCase(folioCliente.trim())) {
				whereCause.add("cds.folioCliente = :folioCliente");
				paramaterMap.put("folioCliente", folioCliente);
			}
			if (idCliente != 0) {
				cliente.setCteCve(idCliente);
				whereCause.add("cds.cteCve = :idCliente");
				paramaterMap.put("idCliente", cliente);
			}

			queryBuilder.append(" WHERE " + StringUtils.join(whereCause, " AND "));

			q = em.createQuery(queryBuilder.toString());

			for (String key : paramaterMap.keySet()) {
				q.setParameter(key, paramaterMap.get(key));
			}

			List<ConstanciaDeServicio> listado = (List<ConstanciaDeServicio>) q.getResultList();

			return listado;
		} catch (Exception e) {
			System.out.println("ERROR" + e.getMessage());
			return null;
		}
	}

	@Override
	public List<ConstanciaDeServicio> buscarPorCriterios(ConstanciaDeServicio constanciaDeServicio) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public List<ConstanciaDeServicio> buscar(Date fechaInicio, Date fechaFin, Integer idCliente, String folioCliente) {
		List<ConstanciaDeServicio> resultList = null;
		EntityManager em = null;
		
		try {
			em = EntityManagerUtil.getEntityManager();
			
			resultList = em.createNamedQuery("ConstanciaDeServicio.findByPeriodoClienteFolioCliente", ConstanciaDeServicio.class)
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
