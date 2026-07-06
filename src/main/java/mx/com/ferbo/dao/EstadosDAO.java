package mx.com.ferbo.dao;

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

@Deprecated
public class EstadosDAO extends IBaseDAO<Estados, Integer>{
	
	private static Logger log = LogManager.getLogger(EstadosDAO.class);
	
	@SuppressWarnings("unchecked")
	public List<Estados> findall() {
		EntityManager entity = null;
		List<Estados> estados = null;
		try {
                    entity = EntityManagerUtil.getEntityManager();
                
                    Query sql = entity.createNamedQuery("Estados.findAll", Estados.class);
                    estados = sql.getResultList();
		} catch (Exception ex) {
			log.error("Problema al buscar el Estado", ex);
		} finally {
			EntityManagerUtil.close(entity);
		}
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
		} finally {
			EntityManagerUtil.close(entity);
		}
		
		
		return estado;
	}
	
	public Estados buscarPorId(Paises idPais, Integer idEstado) {
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
            EntityManager em = null;
            try {
                em = EntityManagerUtil.getEntityManager();
		listado = em.createNamedQuery("Estados.findAll", Estados.class).getResultList();
            } catch(Exception ex) {
                log.warn("Problema para obtener la información del Estado...", ex);
            } finally {
                EntityManagerUtil.close(em);
            }
            return listado;
	}

	public List<Estados> buscarPorCriteriosEstados(Estados e) {
            List<Estados> listado = null;
            EntityManager em = null;
            try {
                em = EntityManagerUtil.getEntityManager();
		listado = em.createNamedQuery("Estados.findByPaisCve", Estados.class)
                    .setParameter("paisCve", e.getEstadosPK().getPais().getPaisCve())
                    .getResultList();
            } catch(Exception ex) {
                log.warn("Problema para obtener la información del Estado...", ex);
            } finally {
                EntityManagerUtil.close(em);
            }
            return listado;
	}
	
	@Override
	public List<Estados> buscarPorCriterios(Estados e) {
		// TODO Auto-generated method stub
            List<Estados> listEstados = null;
            EntityManager em = null;
            try {
                em = EntityManagerUtil.getEntityManager();
                TypedQuery<Estados> consEstados = em.createNamedQuery("Estados.findByPaisCve", Estados.class);
                consEstados.setParameter("paisCve", e.getEstadosPK().getPais().getPaisCve());
                listEstados = consEstados.getResultList();
                } catch(Exception ex) {
                log.warn("Problema para obtener la información del Estado...", ex);
            } finally {
                EntityManagerUtil.close(em);
            }
            return listEstados;
	}

	@Override
	public String actualizar(Estados estados) {
		EntityManager em = null;
                try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.merge(estados);
			em.getTransaction().commit();
		} catch (Exception e) {
                    log.error("ERROR actualizando Estado", e);
                    return "ERROR";
		} finally {
                    EntityManagerUtil.close(em);
		}
		return null;
	}

	@Override
	public String guardar(Estados estados) {
		EntityManager em = null;
                try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.persist(estados);
			em.getTransaction().commit();
		} catch (Exception e) {
			log.error("ERROR guardando Estado", e);
			return "ERROR";
		} finally {
                    EntityManagerUtil.close(em);
		}
		return null;
	}

	@Override
	public String eliminar(Estados estados) {
		EntityManager em = null;
                try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.createQuery("DELETE FROM Estados e WHERE e.estadosPK.pais.paisCve =:paisCve and e.estadosPK.estadoCve =:estadoCve")
			.setParameter("paisCve", estados.getEstadosPK().getPais().getPaisCve())
			.setParameter("estadoCve", estados.getEstadosPK().getEstadoCve()).executeUpdate();
			em.getTransaction().commit();
			em.close();
		} catch (Exception e) {
			log.error("Error al eliminar un estado", e);
			return "ERROR";
		} finally {
                    EntityManagerUtil.close(em);
		}
		return null;
	}

	@Override
	public String eliminarListado(List<Estados> listado) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Estados> buscaPorId(Integer id) {
            List<Estados> listEstados = null;
            EntityManager em = null;
            try {
                em = EntityManagerUtil.getEntityManager();
		listEstados = em.createNamedQuery("Estados.findByEstadoCve", Estados.class)
                    .setParameter("estadoCve", id)
                    .getResultList();
            } catch (Exception e) {
                log.error("Error al obneter la lista de estados", e);
            } finally {
                EntityManagerUtil.close(em);
            }
            return listEstados;
	}
	
	public List<Estados> buscaPorAsentamiento(AsentamientoHumano as) {
	    List<Estados> listEstados = null;
            EntityManager em = null;
            try {
                em = EntityManagerUtil.getEntityManager();
		listEstados = em.createNamedQuery("Estados.findByCriterios", Estados.class)
                    .setParameter("paisCve", as.getAsentamientoHumanoPK().getCiudades().getCiudadesPK().getMunicipios().getMunicipiosPK().getEstados().getEstadosPK().getPais().getPaisCve())
                    .setParameter("estadoCve", as.getAsentamientoHumanoPK().getCiudades().getCiudadesPK().getMunicipios().getMunicipiosPK().getEstados().getEstadosPK().getEstadoCve())
                    .getResultList();
            } catch (Exception e) {
                log.error("Error al obtener la lista de estados", e);
            } finally {
                EntityManagerUtil.close(em);
            }
            return listEstados;
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
