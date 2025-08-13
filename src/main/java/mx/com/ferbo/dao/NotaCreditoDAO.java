package mx.com.ferbo.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.model.CancelaNotaCredito;
import mx.com.ferbo.model.NotaCredito;
import mx.com.ferbo.model.NotaPorFactura;
import mx.com.ferbo.util.EntityManagerUtil;

public class NotaCreditoDAO {
	private static Logger log = LogManager.getLogger(NotaCreditoDAO.class);
	
	EntityManager entity = EntityManagerUtil.getEntityManager();
	EntityManager em = null;
	
	@SuppressWarnings("unchecked")
	public List<NotaCredito> findAll(){
		List<NotaCredito> notaCredito;
		Query sql = entity.createNamedQuery("NotaCredito.findAll", NotaCredito.class);
		notaCredito = sql.getResultList();
		System.out.println(notaCredito + "*****************************************************");
		return notaCredito;
	}
	
	public void guardar(NotaCredito notaCredito) {
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.persist(notaCredito);
			em.getTransaction().commit();
			em.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
	}
	
	public EntityManager getEm() {
		return em;
	}

	public void setEm(EntityManager em) {
		this.em = em;
	}
	
	
	public List<NotaCredito> buscarPor(Date fechaInicio, Date fechaFin, Integer idCliente) {
		List<NotaCredito> resultList = null;
		EntityManager em = null;
		
		try {
			em = EntityManagerUtil.getEntityManager();
			resultList = em.createNamedQuery("NotaCredito.findByPeriodoCliente", NotaCredito.class)
					.setParameter("fechaInicio", fechaInicio)
					.setParameter("fechaFin", fechaFin)
					.setParameter("idCliente", idCliente)
					.getResultList()
					;
			for(NotaCredito nota : resultList) {
				log.debug("Status nota: {}", nota.getStatus().getId());
			}
			
		} catch(Exception ex) {
			log.error("Problema para consultar las notas de crédito...", ex);
		} finally {
			EntityManagerUtil.close(em);
		}
		
		return resultList;
	}
	
	
	public NotaCredito buscarPor(Integer idNotaCredito, boolean isFullInfo) {
		NotaCredito nota = null;
		EntityManager entity = null;
		
		try {
			entity = EntityManagerUtil.getEntityManager();
			nota = entity.find(NotaCredito.class, idNotaCredito);
			
			if(isFullInfo == false)
				return nota;
			
			log.debug("Status Nota Credito: {}", nota.getStatus().getDescripcion());
			
			List<NotaPorFactura> notaFacturaList = nota.getNotaFacturaList();
			for(NotaPorFactura nf : notaFacturaList) {
				log.debug("Nota: {}", nf.getNotaPorFacturaPK().getNota().getId());
				log.debug("Factura: {}", nf.getNotaPorFacturaPK().getFactura().getId());
			}
			
			List<CancelaNotaCredito> cancelaList = nota.getCancelaNotaCreditoList();
			
			for(CancelaNotaCredito cancela : cancelaList) {
				log.debug("Cancela nota factura: {}", cancela.getId());
			}
			
		} catch(Exception ex) {
			log.error("Problema para obtener el detalle de la nota de credito...", ex);
		} finally {
			EntityManagerUtil.close(entity);
		}
		return nota;
	}
	
	
	public String actualizar(NotaCredito notaCredito) {
		String resultado = null;
		EntityManager em = null;
		
		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			notaCredito = em.merge(notaCredito);
			em.getTransaction().commit();
		} catch(Exception ex) {
			EntityManagerUtil.rollback(em);
			resultado = ex.getMessage();
		} finally {
			EntityManagerUtil.close(em);
		}
		
		return resultado;
	}
}
