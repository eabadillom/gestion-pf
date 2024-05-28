package mx.com.ferbo.dao;

import static mx.com.ferbo.util.EntityManagerUtil.getEntityManager;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.AsentamientoHumano;
import mx.com.ferbo.model.Estados;
import mx.com.ferbo.model.EstadosPK;
import mx.com.ferbo.model.Paises;
import mx.com.ferbo.util.EntityManagerUtil;

public class EstadosDAO extends IBaseDAO<Estados, Integer>{
	
	private static Logger log = LogManager.getLogger(EstadosDAO.class);
	
	@SuppressWarnings("unchecked")
	public List<Estados> findall() {
		EntityManager entity = getEntityManager();
		List<Estados> estados = null;
		Query sql = entity.createNamedQuery("Estados.findAll", Estados.class);
		estados = sql.getResultList();
		entity.close();
		return estados;
	}
	@Override
	public Estados buscarPorId(Integer idEstado) {
		
		EntityManager entity = null;		
		Estados estado = null;
		Query sql = null;
		
		try {
			
			entity = getEntityManager();			
			sql = entity.createNamedQuery("Estados.findByEstadoCve",Estados.class)
					.setParameter("estadoCve", idEstado);
			estado = (Estados) sql.getSingleResult();
			
		} catch (Exception ex) {
			log.error("Problema al buscar el Estado", ex);
		}finally {
			EntityManagerUtil.close(entity);
		}
		
		
		return estado;
	}
	
	public Estados buscarPorId(Integer idPais, Integer idEstado) {
		Estados model = null;
		EntityManager em = null;
		
		try {
			em = getEntityManager();
			model = em.find(Estados.class, new EstadosPK(idPais, idEstado));
			
		} catch(Exception ex) {
			log.warn("Problema para obtener la información del Estado...", ex);
		} finally {
			EntityManagerUtil.close(em);
		}
		
		return model;
	}
	

	@Override
	public List<Estados> buscarTodos() {
		List<Estados> listado = null;
		EntityManager em = EntityManagerUtil.getEntityManager();
		listado = em.createNamedQuery("Estados.findAll", Estados.class).getResultList();
		return listado;
	}

	public List<Estados> buscarPorCriteriosEstados(Estados e) {
		List<Estados> listado = null;
		EntityManager em = EntityManagerUtil.getEntityManager();
		listado = em.createNamedQuery("Estados.findByPaisCve", Estados.class).setParameter("paisCve", e.getEstadosPK().getPaisCve()).getResultList();
		return listado;
	}
	
	@Override
	public List<Estados> buscarPorCriterios(Estados e) {
		// TODO Auto-generated method stub
		EntityManager em = EntityManagerUtil.getEntityManager();
		if (e.getPaises().getPaisCve() != null) {
			TypedQuery<Estados> consEstados = em.createNamedQuery("Estados.findByPaisCve", Estados.class);
			consEstados.setParameter("paisCve", e.getPaises().getPaisCve());
			List<Estados> listado = consEstados.getResultList();
			return listado;
		} else {
			return null;
		} 
	}

	@Override
	public String actualizar(Estados estados) {
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.merge(estados);
			em.getTransaction().commit();
			em.close();
		} catch (Exception e) {
			System.out.println("ERROR actualizando Estado" + e.getMessage());
			return "ERROR";
		}
		return null;
	}

	@Override
	public String guardar(Estados estados) {
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.persist(estados);
			em.getTransaction().commit();
			em.close();
		} catch (Exception e) {
			System.out.println("ERROR guardando Estado" + e.getMessage());
			return "ERROR";
		}
		return null;
	}

	@Override
	public String eliminar(Estados estados) {
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.createQuery("DELETE FROM Estados e WHERE e.estadosPK.paisCve =:paisCve and e.estadosPK.estadoCve =:estadoCve")
			.setParameter("paisCve", estados.getEstadosPK().getPaisCve())
			.setParameter("estadoCve", estados.getEstadosPK().getEstadoCve()).executeUpdate();
			em.getTransaction().commit();
			em.close();
		} catch (Exception e) {
			System.out.println("ERROR" + e.getMessage());
			return "ERROR";
		}
		return null;
	}

	@Override
	public String eliminarListado(List<Estados> listado) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Estados> buscaPorId(Integer id) {
		EntityManager em = EntityManagerUtil.getEntityManager();
		return em.createNamedQuery("Estados.findByEstadoCve", Estados.class)
				.setParameter("estadoCve", id).getResultList();
	}
	
	public List<Estados> buscaPorAsentamiento(AsentamientoHumano as) {
		EntityManager em = EntityManagerUtil.getEntityManager();
		return em.createNamedQuery("Estados.findByCriterios", Estados.class)
				.setParameter("paisCve", as.getAsentamientoHumanoPK().getPaisCve())
				.setParameter("estadoCve", as.getAsentamientoHumanoPK().getEstadoCve()).getResultList();
	}
	
	public List<Estados> buscarPorPais(Paises pais) {
		EntityManager em = null;
		List<Estados> list = null;
		
		try {
			em = EntityManagerUtil.getEntityManager();
			list = em.createNamedQuery("Estados.findByPaisCve", Estados.class)
			.setParameter("paisCve", pais.getPaisCve())
			.getResultList()
			;
		} catch(Exception ex) {
			log.error("Problema al obtener la información de estados del pais " + pais.getPaisCve(), ex);
		} finally {
			EntityManagerUtil.close(em);
		}
		
		return list;
	}
	

}
