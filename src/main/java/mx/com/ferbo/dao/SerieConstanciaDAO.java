package mx.com.ferbo.dao;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.SerieConstancia;
import mx.com.ferbo.model.SerieConstanciaPK;
import mx.com.ferbo.util.EntityManagerUtil;

public class SerieConstanciaDAO extends IBaseDAO<SerieConstancia, SerieConstanciaPK> {
	
	private static Logger log = LogManager.getLogger(SerieConstanciaDAO.class);

	@Override
	public SerieConstancia buscarPorId(SerieConstanciaPK id) {
		SerieConstancia serie = null;
		EntityManager em = null;
		
		try {
			em = EntityManagerUtil.getEntityManager();
			serie = em.find(SerieConstancia.class, id);
		} catch(Exception ex) {
			log.error("Problema para obtener la serie-constancia...", ex);
		} finally {
			EntityManagerUtil.close(em);
		}
		
		return serie;
	}

	@Override
	public List<SerieConstancia> buscarTodos() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<SerieConstancia> buscarPorClienteAndPlanta(Integer idCliente, Integer idPlanta) {
		EntityManager em = null;
		Query sql = null;
		List<SerieConstancia> sc = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			sql = em.createNamedQuery("SerieConstancia.findByClienteAndPlanta", SerieConstancia.class)
					.setParameter("idCliente", idCliente)
					.setParameter("idPlanta", idPlanta)
					;
			sc = sql.getResultList();
		} catch (Exception e) {
			log.error("Error al buscar serie...", e);
		}finally{
			EntityManagerUtil.close(em);
		}
		return sc;
	}
	
	public SerieConstancia buscarPorClienteAndPlanta(SerieConstancia serieConstancia) {
		EntityManager em = null;
		Query sql = null;
		SerieConstancia sc = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			
			sql = em.createNamedQuery("SerieConstancia.findByClienteTpSeriePlanta", SerieConstancia.class)
					.setParameter("idCliente", serieConstancia.getSerieConstanciaPK().getCliente().getCteCve())
					.setParameter("idPlanta", serieConstancia.getSerieConstanciaPK().getPlanta().getPlantaCve())
					.setParameter("tpSerie", serieConstancia.getSerieConstanciaPK().getTpSerie() );
			sc = (SerieConstancia) sql.getSingleResult();
			
		} catch (Exception e) {
			log.error("Error al buscar serie...", e);
		}finally{
			EntityManagerUtil.close(em);
		}
		return sc;
	}
	
	public Optional<SerieConstancia> buscarPorClienteTipoSerieAndPlanta(Integer idCliente, String tipoSerie, Integer idPlanta) {
		Optional<SerieConstancia> optional = null;
		SerieConstancia model = null;
		EntityManager em = null;
		
		try {
			em = this.getEntityManager();
			model = em.createNamedQuery("SerieConstancia.findByClienteTpSeriePlanta", modelClass)
					.setParameter("idCliente", idCliente)
					.setParameter("tpSerie", tipoSerie)
					.setParameter("idPlanta", idPlanta)
					.getSingleResult()
					;
			
			optional = Optional.of(model);
		} catch(Exception ex) {
			log.error("Problema para obtener la Serie-Constancia solicitada: cteCve = {}, tipoSerie = {}, plantaCve = {}, \n{}",
					idCliente, tipoSerie, idPlanta, ex);
			optional = Optional.empty();
		} finally {
			this.close(em);
		}
		
		return optional;
	}
	
	public List<SerieConstancia> buscarPorIdCliente(Integer idCliente) {
		
		EntityManager em = null;
		List<SerieConstancia> lista = null;
		
		try {
			
			em = EntityManagerUtil.getEntityManager();
			lista = em.createNamedQuery("SerieConstancia.findByIdCliente",SerieConstancia.class)
					.setParameter("idCliente", idCliente)
					.getResultList();
			
		} catch (Exception e2) {
			log.error("Problema no encuentra registros por criterios",e2);
		}finally {
			EntityManagerUtil.close(em);
		}
		
		
		return lista;
	}
	
	
	public List<SerieConstancia> buscarPorIdCliente(SerieConstanciaPK serie) {
		
		EntityManager em = null;
		List<SerieConstancia> lista = null;
		
		try {
			
			em = EntityManagerUtil.getEntityManager();
			lista = em.createNamedQuery("SerieConstancia.findByIdCliente",SerieConstancia.class)
					.setParameter("idCliente", serie.getCliente().getCteCve())
					.getResultList();
			
		} catch (Exception e2) {
			log.error("Problema no encuentra registros por criterios",e2);
		}finally {
			EntityManagerUtil.close(em);
		}
		
		
		return lista;
	}

	@Override
	public String actualizar(SerieConstancia e) {
		EntityManager em = null;
		
		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.merge(e);
			em.getTransaction().commit();
		} catch(Exception ex) {
			log.error("Problema para actualizar la serie-constancia", ex);
			EntityManagerUtil.rollback(em);
		} finally {
			EntityManagerUtil.close(em);
		}
		
		return null;
	}

	@Override
	public String guardar(SerieConstancia serieConstancia) {
		EntityManager em = null;
		
		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.persist(serieConstancia);
			em.getTransaction().commit();
		} catch (Exception e2) {
			EntityManagerUtil.rollback(em);
			log.error("Problema al guardar serie constancia", e2);
		}finally {
			EntityManagerUtil.close(em);
		}		
		
		return null;
	}

	@Override
	public String eliminar(SerieConstancia serieConstancia) {
		
		EntityManager em = null;
		
		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.remove(em.merge(serieConstancia));
			em.getTransaction().commit();
		} catch (Exception e2) {
			log.error("Problema al eliminar serie constancia");
		}finally {
			EntityManagerUtil.close(em);
		}
		
		return null;
	}

	@Override
	public String eliminarListado(List<SerieConstancia> listado) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SerieConstancia> buscarPorCriterios(SerieConstancia e) {
		// TODO Auto-generated method stub
		return null;
	}

	

}
