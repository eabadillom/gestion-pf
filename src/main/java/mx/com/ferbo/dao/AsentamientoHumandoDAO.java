package mx.com.ferbo.dao;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.AsentamientoHumano;
import mx.com.ferbo.model.EntidadPostal;
import mx.com.ferbo.util.EntityManagerUtil;

public class AsentamientoHumandoDAO extends IBaseDAO<AsentamientoHumano, Integer> {
	Logger log = LogManager.getLogger(AsentamientoHumandoDAO.class);
	
	@SuppressWarnings("unchecked")
	public List<AsentamientoHumano> findall() {
		EntityManager entity = null;
		List<AsentamientoHumano> asnHumano= null;
		try {
			entity =EntityManagerUtil.getEntityManager();
			Query sql = entity.createNamedQuery("AsentamientoHumano.findAll", AsentamientoHumano.class);
			asnHumano = sql.getResultList();
		}catch(Exception e) {
			log.error("Problemas para obtener informacion",e);
		}finally {
			EntityManagerUtil.close(entity);	
		}
		return asnHumano;
	}
	
	public AsentamientoHumano buscarPorAsentamiento(Integer paisCve, Integer estadoCve, Integer municipioCve, Integer ciudadCve, Integer asentamientoCve) {
		EntityManager entity = null;
		AsentamientoHumano asn = null;
		try {
			entity = EntityManagerUtil.getEntityManager();
			asn = entity.createNamedQuery("AsentamientoHumano.findAsentamiento", AsentamientoHumano.class)
					.setParameter("paisCve",paisCve)
					.setParameter("estadoCve",estadoCve)
					.setParameter("municipioCve",municipioCve)
					.setParameter("ciudadCve",ciudadCve)
					.setParameter("asentamientoCve",asentamientoCve)
					.getSingleResult();
		}catch(Exception e) {
			log.error("Problemas para obtener informacion",e);
		}finally {
			EntityManagerUtil.close(entity);
		}
		return asn;
	}
	
	public List<AsentamientoHumano> buscarPorPaisEstadoMunicipioCiudad(Integer idPais, Integer idEstado, Integer idMunicipio, Integer idCiudad) {
		EntityManager entity = null;
		List<AsentamientoHumano> modelList = null;
		
		try {
			entity = EntityManagerUtil.getEntityManager();
			modelList = entity.createNamedQuery("AsentamientoHumano.findByPaisEstadoMunicipioCiudad", AsentamientoHumano.class)
					.setParameter("paisCve",idPais)
					.setParameter("estadoCve", idEstado)
					.setParameter("municipioCve",idMunicipio)
					.setParameter("ciudadCve", idCiudad)
					.getResultList()
					;
		} catch(Exception e) {
			log.error("Problemas para obtener informacion", e);
		} finally {
			EntityManagerUtil.close(entity);
		}
		
		return modelList;
	}

	@Override
	public List<AsentamientoHumano> buscarTodos() {
		List<AsentamientoHumano> listado = null;
		EntityManager em =null;
		try {
		em = EntityManagerUtil.getEntityManager();
		listado = em.createNamedQuery("AsentamientoHumano.findAll", AsentamientoHumano.class).getResultList();
		}catch(Exception e) {
			log.error("Problemas para obtener informacion", e);
		}finally {
			EntityManagerUtil.close(em);
		}
		return listado;
	}

	@Override
	public List<AsentamientoHumano> buscarPorCriterios(AsentamientoHumano a) {		
		List<AsentamientoHumano> listado = null;
		EntityManager em = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			listado = em.createNamedQuery("AsentamientoHumano.findByDomicilio", AsentamientoHumano.class)
					.setParameter("paisCve", a.getAsentamientoHumanoPK().getPaisCve())
					.setParameter("estadoCve", a.getAsentamientoHumanoPK().getEstadoCve())
					.setParameter("municipioCve", a.getAsentamientoHumanoPK().getMunicipioCve())
					.setParameter("ciudadCve", a.getAsentamientoHumanoPK().getCiudadCve())
					.getResultList();
		}catch(Exception e) {
			log.error("Problemas para obtener informacion",e);
		}finally {
			EntityManagerUtil.close(em);
		}
		return listado;
	}
	public List<AsentamientoHumano> buscarPorCriterioEspecial(AsentamientoHumano a) {		
		List<AsentamientoHumano> listado = null;
		EntityManager em = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			listado = em.createNamedQuery( "AsentamientoHumano.findByDomicilioCompleto", AsentamientoHumano.class)
					.setParameter("paisCve", a.getAsentamientoHumanoPK().getPaisCve())
					.setParameter("estadoCve", a.getAsentamientoHumanoPK().getEstadoCve())
					.setParameter("municipioCve", a.getAsentamientoHumanoPK().getMunicipioCve())
					.setParameter("ciudadCve", a.getAsentamientoHumanoPK().getCiudadCve())
					.setParameter("tipoasntmntoCve", a.getAsentamientoHumanoPK().getTipoasntmntoCve())
					.setParameter("entidadpostalCve", a.getAsentamientoHumanoPK().getEntidadpostalCve())
					.getResultList();
		}catch(Exception e) {
			log.error("Problemas para obtener informacion",e);
		}finally {
			EntityManagerUtil.close(em);
		}
		return listado;
	}
	
	public List<AsentamientoHumano> buscarPorEntidadPostal(EntidadPostal e){
		
		EntityManager em = null;
		List<AsentamientoHumano> lista = null;
		
		try {
			
			em = EntityManagerUtil.getEntityManager();
			lista = em.createNamedQuery("AsentamientoHumano.findByEntidadpostalCve",AsentamientoHumano.class)
					.setParameter("entidadpostalCve", e.getEntidadpostalCve() ).getResultList();
			
		} catch (Exception ex) {
			log.error("Problema al encontrar registros asentamiento",ex);
		}finally {
			EntityManagerUtil.close(em);
		}
		
		return lista;
	}
	
	public AsentamientoHumano buscar(AsentamientoHumano a) {		
		AsentamientoHumano listado = null;
		EntityManager em = null;
		
		try {
			 em = EntityManagerUtil.getEntityManager();
			 listado = em.createNamedQuery("AsentamientoHumano.findAsentamiento", AsentamientoHumano.class)
					 .setParameter("paisCve", a.getAsentamientoHumanoPK().getPaisCve())
					 .setParameter("estadoCve", a.getAsentamientoHumanoPK().getEstadoCve())
					 .setParameter("municipioCve", a.getAsentamientoHumanoPK().getMunicipioCve())
					 .setParameter("ciudadCve", a.getAsentamientoHumanoPK().getCiudadCve())
					 .setParameter("asentamientoCve", a.getAsentamientoHumanoPK().getAsentamientoCve())
					 .getSingleResult();
		}catch(Exception e) {
			log.error("Problemas para obtener informacion", e);
		}finally {
			EntityManagerUtil.close(em);
		}
		return listado;
	}
	@Override
	public String actualizar(AsentamientoHumano asentamientoHumano) {
		EntityManager em =null;
		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.merge(asentamientoHumano);
			em.getTransaction().commit();
		} catch (Exception e) {
			log.error("problema al actualizar",e);
		}finally {
			EntityManagerUtil.close(em);	
		}
		return null;
	}

	@Override
	public String guardar(AsentamientoHumano asentamientoHumano) {
		EntityManager em = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.persist(asentamientoHumano);
			em.getTransaction().commit();
		} catch (Exception e) {
			System.out.println("ERROR guardando Asentamiento Humano" + e.getMessage());
			return "ERROR";
		}finally {
			EntityManagerUtil.close(em);
		}
		return null;
	}

	@Override
	public String eliminar(AsentamientoHumano asentamientoHumano) {
		EntityManager em = null;
		try {
			 em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
//			em.remove(em.merge(asentamientoHumano));
			em.createQuery("DELETE FROM AsentamientoHumano ah WHERE ah.asentamientoHumanoPK.paisCve =:paisCve and ah.asentamientoHumanoPK.estadoCve =:estadoCve and ah.asentamientoHumanoPK.municipioCve =:municipioCve and ah.asentamientoHumanoPK.ciudadCve =:ciudadCve and ah.asentamientoHumanoPK.tipoasntmntoCve =:tipoasntmntoCve and ah.asentamientoHumanoPK.entidadpostalCve =:entidadpostalCve and ah.asentamientoHumanoPK.asentamientoCve =:asentamientoCve")
			.setParameter("paisCve", asentamientoHumano.getAsentamientoHumanoPK().getPaisCve())
			.setParameter("estadoCve", asentamientoHumano.getAsentamientoHumanoPK().getEstadoCve())
			.setParameter("municipioCve", asentamientoHumano.getAsentamientoHumanoPK().getMunicipioCve())
			.setParameter("ciudadCve", asentamientoHumano.getAsentamientoHumanoPK().getCiudadCve())
			.setParameter("tipoasntmntoCve", asentamientoHumano.getAsentamientoHumanoPK().getTipoasntmntoCve())
			.setParameter("entidadpostalCve", asentamientoHumano.getAsentamientoHumanoPK().getEntidadpostalCve())
			.setParameter("asentamientoCve", asentamientoHumano.getAsentamientoHumanoPK().getAsentamientoCve()).executeUpdate();
			em.getTransaction().commit();
			em.close();
		} catch (Exception e) {
			System.out.println("ERROR" + e.getMessage());
			return "ERROR";
		}finally{
			EntityManagerUtil.close(em);
		}
		return null;
	}

	@Override
	public String eliminarListado(List<AsentamientoHumano> listado) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public AsentamientoHumano buscarPorId(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}
}
