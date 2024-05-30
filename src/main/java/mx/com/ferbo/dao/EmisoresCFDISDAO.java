package mx.com.ferbo.dao;

import static mx.com.ferbo.util.EntityManagerUtil.getEntityManager;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.Certificado;
import mx.com.ferbo.model.EmisoresCFDIS;
import mx.com.ferbo.util.EntityManagerUtil;

public class EmisoresCFDISDAO extends IBaseDAO<EmisoresCFDIS, Integer>{
	
	private static Logger log = LogManager.getLogger(EmisoresCFDISDAO.class);

	@SuppressWarnings("unchecked")
	public List<EmisoresCFDIS> findall() {
		EntityManager entity = null;
		List<EmisoresCFDIS> emisores= null;
		
		try {
		
			entity = getEntityManager();		
			Query sql = entity.createNamedQuery("EmisoresCFDIS.findAll", EmisoresCFDIS.class);
			emisores = sql.getResultList();
			
		} catch (Exception e) {
			log.error("Problema al retornar lista emisores",e);
		}finally {
			EntityManagerUtil.close(entity);
		}
		
		return emisores;
	}

	public List<EmisoresCFDIS> findall(Boolean isFullInfo){ 
		
		EntityManager em = null;
		List<EmisoresCFDIS> lista = null;
		
		try {
			
			em = EntityManagerUtil.getEntityManager();
			lista = em.createNamedQuery("EmisoresCFDIS.findAll",EmisoresCFDIS.class).getResultList();
			
			if(isFullInfo == false)
				return lista;
			
			for(EmisoresCFDIS e: lista) {
				log.debug(e.getCd_regimen().getNb_regimen());
				log.debug(e.getListaCertificado().size());
				
			}
						
		}catch (Exception e) {
			log.error("Problema para recuperar lista emisores",e);
		} finally {
			EntityManagerUtil.close(em);
		}
		
		return lista;
	}
	
	@Override
	public EmisoresCFDIS buscarPorId(Integer cd_emisor) {
		
		EntityManager em = null;
		EmisoresCFDIS emi = null;		
		
		try {
			
			em = EntityManagerUtil.getEntityManager();
			emi = em.createNamedQuery("EmisoresCFDIS.findBycdEmisor", EmisoresCFDIS.class).setParameter("cd_emisor", cd_emisor).getSingleResult();
			
		} catch (Exception e) {
			
			log.error("Problema para buscar el emisor",e);
			
		}finally {
			EntityManagerUtil.close(em);
		}
		
		return emi;
	}

	@Override
	public List<EmisoresCFDIS> buscarTodos() {
		
		
		List<EmisoresCFDIS> listaEmisores = null;
		EntityManager em = null;
		
		try {
			em = EntityManagerUtil.getEntityManager();
			listaEmisores = em.createNamedQuery("EmisoresCFDIS.findAll", EmisoresCFDIS.class).getResultList();
		} catch (Exception e) {
			log.error("Probelma al recuperar lista emisoresCFDIS", e);
		}finally {
			EntityManagerUtil.close(em);
		}
		
		return listaEmisores;
	}

	@Override
	public List<EmisoresCFDIS> buscarPorCriterios(EmisoresCFDIS e) {
		
		EntityManager em = null;
		List<EmisoresCFDIS> listaEmisores =null;
		
		try {
			
			em = EntityManagerUtil.getEntityManager();
			listaEmisores = em.createNamedQuery("EmisoresCFDIS.findByregimenFiscal", EmisoresCFDIS.class).setParameter("cd_regimen", e.getCd_regimen()).getResultList();
			
		} catch (Exception ex) {
			log.error("Problema para buscar emisor por criterio",ex);
		}finally {
			EntityManagerUtil.close(em);
		}
		
		return listaEmisores;
	}

	@Override
	public String actualizar(EmisoresCFDIS emi) {
		try{
			EntityManager ent = getEntityManager();
			ent.getTransaction().begin();
			ent.merge(emi);
			ent.getTransaction().commit();
			ent.close();
		}catch(Exception e){
			return "Failed!" + e.getMessage();
		}
		return null;
	}

	@Override
	public String guardar(EmisoresCFDIS emi) {
		
		EntityManager ent = null;
		
		try {
			ent = getEntityManager();
			ent.getTransaction().begin();
			ent.persist(emi);
			ent.getTransaction().commit();
			
		}catch(Exception e){
 			return "Failed" + e.getMessage();
		}finally {
			EntityManagerUtil.close(ent);
		}
		return null;
	}

	@Override
	public String eliminar(EmisoresCFDIS emi) {
		
		EntityManager ent = null;
		
		try {
			ent = getEntityManager();
			ent.getTransaction().begin();
			ent.remove(ent.merge(emi));
			ent.getTransaction().commit();
		}catch(Exception e) {
			return "Failed!"+e.getMessage();
		}finally {
			ent.close();
		}
		
		return null;
	}

	@Override
	public String eliminarListado(List<EmisoresCFDIS> listado) {
		
		EntityManager em = null;
		
		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			for(EmisoresCFDIS emisores : listado) {
				em.remove(em.merge(emisores));
			}
			em.getTransaction().commit();
		}catch(Exception e){
			System.out.println("Error" + e.getMessage());
		}finally {
			EntityManagerUtil.close(em);
		}
		
		return null;
	}

	public List<EmisoresCFDIS> buscarTodos(boolean isFullInfo) {
		List<EmisoresCFDIS> modelList = null;
		EntityManager em = null;
		
		try {
			em = EntityManagerUtil.getEntityManager();
			modelList = em.createNamedQuery("EmisoresCFDIS.findAll", EmisoresCFDIS.class)
					.getResultList()
					;
			if(isFullInfo == false)
				return modelList;
			
			for(EmisoresCFDIS e : modelList) {
				for(Certificado c : e.getListaCertificado()) {
					log.info("Certificado: {}", c.getCdCertificado());
				}
			}
			
		} catch(Exception ex) {
			
		} finally {
			
		}
		
		return modelList;
	}

}
