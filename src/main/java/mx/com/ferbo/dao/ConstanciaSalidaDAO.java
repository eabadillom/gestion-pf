package mx.com.ferbo.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.ConstanciaSalida;
import mx.com.ferbo.model.ConstanciaSalidaServicios;
import mx.com.ferbo.model.DetalleConstanciaSalida;
import mx.com.ferbo.util.EntityManagerUtil;

public class ConstanciaSalidaDAO extends IBaseDAO<ConstanciaSalida, Integer> {
	
	private static Logger log = LogManager.getLogger(ConstanciaSalidaDAO.class);

	public EntityManager em = null;
	
	@Override
	public ConstanciaSalida buscarPorId(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public ConstanciaSalida buscarPorId(Integer id, boolean isFullInfo) {
		ConstanciaSalida constancia = null;
		EntityManager em = null;
		
		try {
			em = EntityManagerUtil.getEntityManager();
			constancia = em.find(ConstanciaSalida.class, id);
			
			if(isFullInfo == false)
				return constancia;
			
			List<DetalleConstanciaSalida> dcsList = constancia.getDetalleConstanciaSalidaList();
			for(DetalleConstanciaSalida dcs : dcsList) {
				log.debug("DCS: {}", dcs.getId());
				log.debug("PartidaCve: {}", dcs.getPartidaCve().getPartidaCve());
				log.debug("Partida cajas: {}", dcs.getPartidaCve().getCantidadTotal());
			}
			
			List<ConstanciaSalidaServicios> cssrvList = constancia.getConstanciaSalidaServiciosList();
			for(ConstanciaSalidaServicios cssrv : cssrvList) {
				log.debug("CSSrv: {}", cssrv);
			}
			
		} catch(Exception ex) {
			log.error("Problema para obtener la constancia de salida...", ex);
		} finally {
			EntityManagerUtil.close(em);
		}
		return constancia;
	}

	@Override
	public List<ConstanciaSalida> buscarTodos() {
		List<ConstanciaSalida> lista = null;
		EntityManager em = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			lista = em.createNamedQuery("ConstanciaSalida.findAll",ConstanciaSalida.class).getResultList();
		} catch(Exception ex) {
			log.error("Problema para obtener el listado de constancias de salida...", ex);
		} finally {
			EntityManagerUtil.close(em);
		}
		
		return lista;
	}

	@Override
	public List<ConstanciaSalida> buscarPorCriterios(ConstanciaSalida e) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<ConstanciaSalida> buscar(Date fechaInicio, Date fechaFin, Integer idCliente, String folioCliente) {
		List<ConstanciaSalida> resultList = null;
		EntityManager em = null;
		Query sql = null;
		
		try {
			if(folioCliente != null && folioCliente.contains("%") == false)
				folioCliente = "%".concat(folioCliente).concat("%");
			
			em = EntityManagerUtil.getEntityManager();
			sql = em.createNativeQuery("SELECT * FROM (\n"
					+ "	SELECT * FROM (\n"
					+ "		SELECT *\n"
					+ "		FROM constancia_salida cs\n"
					+ "		where (:idCliente IS NULL OR cs.CLIENTE_CVE = :idCliente)\n"
					+ "	) cs2 WHERE ((cs2.FECHA BETWEEN :fechaInicio AND :fechaFin) OR (:fechaInicio IS NULL OR :fechaFin IS NULL)) \n"
					+ ") cs3 WHERE (:folioCliente IS NULL OR cs3.NUMERO like :folioCliente ) ORDER BY cs3.FECHA", ConstanciaSalida.class)
					.setParameter("fechaInicio", fechaInicio)
					.setParameter("fechaFin", fechaFin)
					.setParameter("idCliente", idCliente)
					.setParameter("folioCliente", folioCliente)
					;
			resultList = sql.getResultList();
			
		} catch(Exception ex) {
			log.error("Problema para consultar las constancias de salida...", ex);
		} finally {
			EntityManagerUtil.close(em);
		}
		
		return resultList;
	}

	@Override
	public String actualizar(ConstanciaSalida constanciaSalida) {
		EntityManager em = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.merge(constanciaSalida);
			em.getTransaction().commit();
		} catch (Exception e) {
			log.error("Problema para guardar la constancia de salida...", e); 
			return "ERROR";
		} finally {
			EntityManagerUtil.close(em);
		}
		
		return null;
	}
	
	public String actualizarStatus(ConstanciaSalida constanciaSalida) {
		EntityManager em = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			Query actualizar = em.createNativeQuery(" UPDATE constancia_salida SET STATUS = :status, OBSERVACIONES = :observaciones WHERE ID = :id ") ;
			actualizar.setParameter("status",(constanciaSalida.getStatus()==null) ? 1:2);//si constancia de salida status es null colocar 1 en otro caso colocar 2
			actualizar.setParameter("id", constanciaSalida.getId());
			actualizar.setParameter("observaciones", constanciaSalida.getObservaciones());
			actualizar.executeUpdate();
			em.getTransaction().commit();
		} catch (Exception e) {
			log.error("Problema para actualizar el status de la constancia de salida...", e);
			return "ERROR";
		} finally {
			EntityManagerUtil.close(em);
		}
		
		return null;
	}
	
	@Override
	public String guardar(ConstanciaSalida constanciaSalida) {
		EntityManager em = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.persist(constanciaSalida);//guarda el servicio dado (NO es gestionado)
			em.getTransaction().commit();
		} catch (Exception e) {
			log.error("Problema para guardar la constancia de salida...", e); 
			return "ERROR";
		} finally {
			EntityManagerUtil.close(em);
		}
		
		return null;
	}
	
	public String buscarPorNumero(String numFolio) {
		EntityManager em = null;
		ConstanciaSalida constanciaSalida;
		String folio = "";
		
		try {
			
			em = EntityManagerUtil.getEntityManager();
			constanciaSalida = em.createNamedQuery("ConstanciaSalida.findByNumero", ConstanciaSalida.class).setParameter("numero", numFolio).getSingleResult();
			if(constanciaSalida!=null) {
				folio = constanciaSalida.getNumero();
			}			
			
		} catch (Exception e) {
			log.error("Error al buscar por numero constancia salida", e.getMessage());
		}finally {
			EntityManagerUtil.close(em);
		}
		
		return folio;
	}
	
	public EntityManager getEm() {
		return em;
	}

	public void setEm(EntityManager em) {
		this.em = em;
	}

	@Override
	public String eliminar(ConstanciaSalida e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminarListado(List<ConstanciaSalida> listado) {
		// TODO Auto-generated method stub
		return null;
	}

}
