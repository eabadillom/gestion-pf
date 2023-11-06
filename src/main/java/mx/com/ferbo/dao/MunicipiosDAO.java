package mx.com.ferbo.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.AsentamientoHumano;
import mx.com.ferbo.model.Municipios;
import mx.com.ferbo.util.EntityManagerUtil;

public class MunicipiosDAO extends IBaseDAO<Municipios, Integer> {
	Logger log = LogManager.getLogger(MunicipiosDAO.class);
	
	@SuppressWarnings("unchecked")
	public List<Municipios> findall() {
		EntityManager entity = null;
		List<Municipios> municipios = null;
		try {
			 entity = EntityManagerUtil.getEntityManager();
			 Query sql = entity.createNamedQuery("Municipios.findAll", Municipios.class);
			 municipios = sql.getResultList();
		}catch(Exception e) {
			log.error("Problemas para obtener informacion",e);
		}finally {
			EntityManagerUtil.close(entity);
		}
		return municipios;
	}
	
	@Override
	public Municipios buscarPorId(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Municipios> buscarTodos() {
		List<Municipios> listado = null;
		EntityManager em = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			listado = em.createNamedQuery("Municipios.findAll", Municipios.class).getResultList();
		}catch(Exception e) {
			log.error("Problemas para obtener informacion",e);
		}finally {
			EntityManagerUtil.close(em);
		}
		return listado;
	}

	public List<Municipios> buscarPorCriteriosMunicipios(Municipios m) {
		List<Municipios> listado = null;
		EntityManager em = null;
		try {
		em = EntityManagerUtil.getEntityManager();
		listado = em.createNamedQuery("Municipios.findByPaisCveEstadoCve", Municipios.class).setParameter("estadoCve", m.getMunicipiosPK().getEstadoCve()).setParameter("paisCve", m.getMunicipiosPK().getPaisCve()).getResultList();
		
		}catch(Exception e) {
			log.error("Problemas para obtener informacion",e);
		}finally {
			EntityManagerUtil.close(em);
		}
		return listado;
	}

	@Override
	public List<Municipios> buscarPorCriterios(Municipios m) {
		EntityManager em = null;
		List<Municipios> listado = null;
		try {
			 em = EntityManagerUtil.getEntityManager();
			 if (m.getEstados().getEstadosPK().getEstadoCve() > 0) {
				 TypedQuery<Municipios> consEstados = em.createNamedQuery("Municipios.findByEstadoCve", Municipios.class);
				 consEstados.setParameter("estadoCve", m.getEstados().getEstadosPK().getEstadoCve());
				 listado = consEstados.getResultList();
				 return listado;
			 } else if(m.getMunicipiosPK().getEstadoCve() != -1 && m.getMunicipiosPK().getPaisCve() != -1){
				 listado = em.createNamedQuery("Municipios.findByPaisCveEstadoCve", Municipios.class).setParameter("estadoCve", m.getMunicipiosPK().getEstadoCve()).setParameter("paisCve", m.getMunicipiosPK().getPaisCve()).getResultList();
				 return listado;
			 } 
		}catch(Exception e) {
			log.error("Problemas para obtener informacion",e);
		}finally {
			EntityManagerUtil.close(em);	
		}
		 return listado;
	}

	@Override
	public String actualizar(Municipios municipios) {
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.merge(municipios);
			em.getTransaction().commit();
			em.close();
		} catch (Exception e) {
			System.out.println("ERROR guardando Municipio" + e.getMessage());
			return "ERROR";
		}
		return null;
	}

	@Override
	public String guardar(Municipios municipios) {
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.persist(municipios);
			em.getTransaction().commit();
			em.close();
		} catch (Exception e) {
			System.out.println("ERROR guardando Municipio" + e.getMessage());
			return "ERROR";
		}
		return null;
	}

	@Override
	public String eliminar(Municipios municipios) {
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.createQuery("DELETE FROM Municipios m WHERE m.municipiosPK.paisCve =:paisCve and m.municipiosPK.estadoCve =:estadoCve and m.municipiosPK.municipioCve =:municipioCve")
			.setParameter("paisCve", municipios.getMunicipiosPK().getPaisCve())
			.setParameter("estadoCve", municipios.getMunicipiosPK().getEstadoCve())
			.setParameter("municipioCve", municipios.getMunicipiosPK().getMunicipioCve()).executeUpdate();
			em.getTransaction().commit();
			em.close();
		} catch (Exception e) {
			System.out.println("ERROR" + e.getMessage());
			return "ERROR";
		}
		return null;
	}

	@Override
	public String eliminarListado(List<Municipios> listado) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Municipios> buscaPorId(Integer id) {
		EntityManager em = null;
		try {
		 em = EntityManagerUtil.getEntityManager();
		return em.createNamedQuery("Municipios.findByMunicipioCve", Municipios.class)
				.setParameter("municipioCve", id).getResultList();
		}catch(Exception e) {
			log.error("Problemas para obtener informacion",e);
		}finally {
			EntityManagerUtil.close(em);
		}
		return null;
	}
	
	public List<Municipios> buscaPorAsentamiento(AsentamientoHumano as) {
		EntityManager em = null;
		try {
		em = EntityManagerUtil.getEntityManager();
		return em.createNamedQuery("Municipios.findByTodo", Municipios.class)
				.setParameter("municipioCve", as.getAsentamientoHumanoPK().getMunicipioCve())
				.setParameter("estadoCve", as.getAsentamientoHumanoPK().getEstadoCve())
				.setParameter("paisCve", as.getAsentamientoHumanoPK().getPaisCve())
				.getResultList();
		}catch(Exception e ) {
			log.error("Problemas para obtener informacion",e);
		}finally {
			EntityManagerUtil.close(em);
		}
		return null;
	}

}
