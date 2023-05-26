package mx.com.ferbo.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.Aviso;
import mx.com.ferbo.model.PrecioServicio;
import mx.com.ferbo.util.EntityManagerUtil;

public class AvisoDAO extends IBaseDAO<Aviso,Integer>{
	
	private Logger log = Logger.getLogger(AvisoDAO.class);

	@SuppressWarnings("unchecked")
	public List<Aviso> findall() {
		EntityManager entity = EntityManagerUtil.getEntityManager();
		List<Aviso> aviso= null;
		Query sql = entity.createNamedQuery("Aviso.findAll", Aviso.class);
		aviso = sql.getResultList();
		return aviso;
	}	
	
	@Override
	public Aviso buscarPorId(Integer id) {
		Aviso aviso = null;
		EntityManager em = null;
		Query query = null;
		
		try {
			em = EntityManagerUtil.getEntityManager();
			query = em.createNamedQuery("Aviso.findByAvisoCve", Aviso.class)
					.setParameter("avisoCve", id)
					;
			aviso = (Aviso) query.getSingleResult();
		} catch(Exception ex) {
			log.error("Problema para obtener el aviso " + id, ex);
		} finally {
			EntityManagerUtil.close(em);
		}
		return aviso;
	}
	
	public Aviso buscarPorId(Integer id, boolean isFullInfo) {
		Aviso aviso = null;
		EntityManager em = null;
		
		try {
			em = EntityManagerUtil.getEntityManager();
			aviso = em.find(Aviso.class, id);
			
			if(isFullInfo == false)
				return aviso;
			log.debug(aviso.getPlantaCve().getPlantaCve());
			log.debug(aviso.getCteCve().getCteCve());
			log.debug(aviso.getCategoriaCve().getCategoriaCve());
			log.debug(aviso.getPrecioServicioList().size());
			for(PrecioServicio ps : aviso.getPrecioServicioList()) {
				log.debug("ID Precio Servicio: " + ps.getId());
			}
			
		} catch(Exception ex) {
			log.error("Problema para obtener el aviso " + id, ex);
		} finally {
			EntityManagerUtil.close(em);
		}
		return aviso;
	}

	@Override
	public List<Aviso> buscarTodos() {
		EntityManager em = null;
		List<Aviso> lista = null;
		try {
			em = EntityManagerUtil.getEntityManager();			
			lista = em.createNamedQuery("Aviso.findAll", Aviso.class).getResultList();
		} catch(Exception ex) {
			log.error("Problema para obtener el listado de avisos...", ex);
		} finally {
			EntityManagerUtil.close(em);
		}

		return lista;
	}
	
	@SuppressWarnings("unchecked")
	public List<Aviso> buscarTodos(boolean isFullInfo) {
		List<Aviso> lista = null;
		EntityManager em = null;
		Query query = null;
		
		try {
			em = EntityManagerUtil.getEntityManager();
			query = em.createNamedQuery("Aviso.findAll", Aviso.class);
			
			lista = query.getResultList();
			
			if(isFullInfo == false)
				return lista;
			
			for(Aviso aviso : lista) {
				aviso.getCategoriaCve().getCategoriaCve();
			}
			
		} catch(Exception ex) {
			log.error("Problema para obtener el listado de avisos...", ex);
		} finally {
			EntityManagerUtil.close(em);
		}
		
		return lista;
	}

	@Override
	public List<Aviso> buscarPorCriterios(Aviso e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String actualizar(Aviso e) {
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
//			em.createNativeQuery(
//					"UPDATE aviso SET aviso_po = :avisoPo , aviso_pedimento = :avisoPedimento, aviso_sap = :avisoSap, aviso_lote = :avisoLote,aviso_caducidad = :avisoCaducidad,aviso_tarima = :avisoTarima,aviso_otro = :avisoOtro,aviso_vigencia = :avisoVigencia,aviso_plazo = :avisoPlazo, aviso_val_seg = :avisoValSeg  WHERE (cte_cve = :cteCve) and (aviso_cve = :avisoCve)") 
//			.setParameter("avisoCve",e.getAvisoCve())
//			.setParameter("avisoPo",e.getAvisoPo())
//			.setParameter("avisoPedimento",e.getAvisoPedimento())
//			.setParameter("avisoSap",e.getAvisoSap())
//			.setParameter("avisoLote",e.getAvisoLote())
//			.setParameter("avisoCaducidad",e.getAvisoCaducidad())
//			.setParameter("avisoTarima",e.getAvisoTarima())
//			.setParameter("avisoOtro",e.getAvisoOtro())
//			.setParameter("cteCve",e.getCteCve())
//			.setParameter("avisoVigencia", e.getAvisoVigencia())
//			.setParameter("avisoPlazo", e.getAvisoPlazo())
//			.setParameter("avisoValSeg", e.getAvisoValSeg())
//					.executeUpdate();
			
			em.merge(e);
			em.getTransaction().commit();
			em.close();
		} catch (Exception ex) {
			System.out.println("ERROR" + ex.getMessage());
			return "ERROR";
		}
		return null;
	}

	@Override
	public String guardar(Aviso e) {
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.createNativeQuery("INSERT INTO aviso (aviso_cve, aviso_po, aviso_pedimento, aviso_sap,aviso_lote,aviso_caducidad,aviso_tarima,aviso_otro,aviso_temp,aviso_fecha,planta_cve,aviso_observaciones,cte_cve,categoria_cve,aviso_vigencia,aviso_val_seg,aviso_plazo,aviso_tp_facturacion) VALUES (:avisoCve, :avisoPo, :avisoPedimento, :avisoSap,:avisoLote,:avisoCaducidad,:avisoTarima,:avisoOtro,:avisoTemp,:avisoFecha,:plantaCve,:avisoObservaciones,:cteCve,:categoriaCve,:avisoVigencia,:avisoValSeg,:avisoPlazo,:avisoTpFacturacion)")
			.setParameter("avisoCve",e.getAvisoCve())
			.setParameter("avisoPo",e.getAvisoPo())
			.setParameter("avisoPedimento",e.getAvisoPedimento())
			.setParameter("avisoSap",e.getAvisoSap())
			.setParameter("avisoLote",e.getAvisoLote())
			.setParameter("avisoCaducidad",e.getAvisoCaducidad())
			.setParameter("avisoTarima",e.getAvisoTarima())
			.setParameter("avisoOtro",e.getAvisoOtro())
			.setParameter("avisoTemp",e.getAvisoTemp())
			.setParameter("avisoFecha",e.getAvisoFecha())
			.setParameter("plantaCve",e.getPlantaCve())
			.setParameter("avisoObservaciones",e.getAvisoObservaciones())
			.setParameter("cteCve",e.getCteCve())
			.setParameter("categoriaCve",e.getCategoriaCve())
			.setParameter("avisoVigencia",e.getAvisoVigencia())
			.setParameter("avisoValSeg",e.getAvisoValSeg())
			.setParameter("avisoPlazo",e.getAvisoPlazo())
			.setParameter("avisoTpFacturacion",e.getAvisoTpFacturacion())
			.executeUpdate();
			em.getTransaction().commit();
			em.close();
		} catch (Exception ex) {
			System.out.println("ERROR" + ex.getMessage());
			return "ERROR";
		}
		return null;
	}

	@Override
	public String eliminar(Aviso e) {
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.createNativeQuery("DELETE FROM aviso WHERE (aviso_cve = :avisoCve)")
					.setParameter("avisoCve", e.getAvisoCve()).executeUpdate();
			em.getTransaction().commit();
			em.close();
		} catch (Exception ex) {
			System.out.println("ERROR" + ex.getMessage());
			return "ERROR";
		}
		return null;
	}

	@Override
	public String eliminarListado(List<Aviso> listado) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public List<Aviso> buscarPorCliente(Aviso e){
		EntityManager em = EntityManagerUtil.getEntityManager();
		return em.createNamedQuery("Aviso.findByAvisoCliente", Aviso.class)
				.setParameter("cteCve", e.getCteCve().getCteCve()).getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Aviso> buscarPorCliente(Integer cteCve) {
		List<Aviso> lista = null;
		EntityManager em = null;
		Query query = null;
		
		try {
			em = EntityManagerUtil.getEntityManager();
			query = em.createNamedQuery("Aviso.findByAvisoCliente", Aviso.class)
					.setParameter("cteCve", cteCve);
			lista = query.getResultList();
		} catch(Exception ex) {
			log.error("Problema en la consulta de los avisos del cliente " + cteCve, ex);
		} finally {
			EntityManagerUtil.close(em);
		}
		return lista;
	}

}
