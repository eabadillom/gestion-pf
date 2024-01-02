package mx.com.ferbo.dao;

import java.util.List;

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

	
	public SerieConstancia buscarPorClienteAndPlanta(SerieConstancia serieConstancia) {
		EntityManager em = null;
		Query sql = null;
		SerieConstancia sc = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			
			sql = em.createNativeQuery("SELECT sc.id_cliente,sc.tp_serie,sc.nu_serie,sc.id_planta FROM serie_constancia  sc WHERE sc.id_cliente = :idcliente AND sc.id_planta = :idplanta AND sc.tp_serie = :tpSerie",SerieConstancia.class)
					.setParameter("idcliente", serieConstancia.getSerieConstanciaPK().getCliente().getCteCve())
					.setParameter("idplanta", serieConstancia.getIdPlanta().getPlantaCve())
					.setParameter("tpSerie", serieConstancia.getSerieConstanciaPK().getTpSerie() );
			sc = (SerieConstancia) sql.getSingleResult();
			
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Error al buscar serie...........");
		}finally{
			em.close();
		}
		return sc;
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
