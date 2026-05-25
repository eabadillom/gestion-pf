package mx.com.ferbo.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.Certificado;
import mx.com.ferbo.model.Planta;
import mx.com.ferbo.model.SerieConstancia;
import mx.com.ferbo.model.Usuario;
import mx.com.ferbo.util.EntityManagerUtil;

public class PlantaDAO extends IBaseDAO<Planta, Integer>{
	private static Logger log = LogManager.getLogger(PlantaDAO.class);

	@SuppressWarnings("unchecked")
	public List<Planta> findall() {
		EntityManager entity = getEntityManager();
		List<Planta> plantas = null;
		try {
			Query sql = entity.createNamedQuery("Planta.findAll", Planta.class);
			plantas = sql.getResultList();
		} catch(Exception ex) {
			log.error("Problema para obtener el listado de Plantas...", ex);
		} finally {
			EntityManagerUtil.close(entity);
		}
		
		return plantas;
	}
	
	@SuppressWarnings("unchecked")
	public List<Planta> findall(boolean isFullInfo) {
		EntityManager entity = null;
		List<Planta> plantas = null;
		try {
			entity = getEntityManager();
			Query sql = entity.createNamedQuery("Planta.findAll", Planta.class);
			plantas = sql.getResultList();
			
			if(isFullInfo == false)
				return plantas;
			
			for(Planta p : plantas) {
				log.debug(p.getIdUsuario().getUsuario());//ERROR lazy 
				
				log.debug(p.getIdEmisoresCFDIS().getNb_emisor()); //no tienen notacion lazy
				log.debug(p.getIdEmisoresCFDIS().getNb_rfc());
				log.debug(p.getIdEmisoresCFDIS().getNb_emisor());
				for(SerieConstancia serieConstancia : p.getSerieConstanciaList()) {
					log.debug(serieConstancia.getSerieConstanciaPK().getPlanta().getPlantaCve());
				}
			}
		} catch(Exception ex) {
			log.error("Problema para obtener el listado de Plantas...", ex);
		} finally {
			EntityManagerUtil.close(entity);
		}
		
		return plantas;
	}

	@SuppressWarnings("unchecked")
	public List<Usuario> getUsuarios() { //MODIFICADO	
		EntityManager entity = null;
		List<Usuario> usuarios = null;
		
		try {
		
			entity = getEntityManager();
			Query sql = entity.createQuery("SELECT u FROM Usuario u WHERE u.perfil IN (1, 4)");
			usuarios = sql.getResultList();			
			
		}catch (Exception e) {
			log.error("Problema al obtener a los usuarios", e);
		}finally {
			EntityManagerUtil.close(entity);
		}
		return usuarios;		
		
	}
	
	public List<Planta> buscarTodosSerieConstancia() {
		List<Planta> lista = null;
		EntityManager em = null;
		
		try {
			em = EntityManagerUtil.getEntityManager();
			lista = em.createNamedQuery("Planta.findAll", Planta.class)
					.getResultList();
			
			for(Planta planta : lista) {
				for(SerieConstancia sc : planta.getSerieConstanciaList()) {
					log.debug("Cliente: {} - TipoSerie: {} - Planta: {} - Serie: {}",
						sc.getSerieConstanciaPK().getCliente().getCteCve(),
						sc.getSerieConstanciaPK().getTpSerie(),
						sc.getSerieConstanciaPK().getPlanta().getPlantaCve(),
						sc.getNuSerie());
				}
			}
			
		} catch(Exception ex) {
			log.error("Problema para obtener el listado de plantas...", ex);
		} finally {
			EntityManagerUtil.close(em);
		}
		
		return lista;
	}
	
	public Planta buscarDetalleSerieConstancia(Integer idPlanta) {
		Planta planta = null;
		EntityManager em = null;
		
		try {
			em = EntityManagerUtil.getEntityManager();
			planta = em.find(Planta.class, idPlanta);
			
			for(SerieConstancia sc : planta.getSerieConstanciaList()) {
				log.debug("Cliente: {} - TipoSerie: {} - Planta: {} - Serie: {}",
					sc.getSerieConstanciaPK().getCliente().getCteCve(),
					sc.getSerieConstanciaPK().getTpSerie(),
					sc.getSerieConstanciaPK().getPlanta().getPlantaCve(),
					sc.getNuSerie());
			}
			
		} catch(Exception ex) {
			log.error("Problema para obtener la planta con el detealle de sus series de constancias...", ex);
		} finally {
			EntityManagerUtil.close(em);
		}
		
		return planta;
	}
	
	public String save(Planta p) {
		EntityManager entity = null;
		try {
			entity = getEntityManager();
			entity.getTransaction().begin();
			entity.persist(p);
			entity.getTransaction().commit();
		} catch (Exception e) {
			log.error("Problema para guardar la planta...", e);
			return "Failed!! " + e.getMessage();
		} finally {
			EntityManagerUtil.close(entity);
		}
		return null;
	}
	

	public String update(Planta p) {
		EntityManager entity = null;
		try {
			entity = getEntityManager();
			entity.getTransaction().begin();
			entity.merge(p);
			entity.getTransaction().commit();
		} catch (Exception e) {
			log.error("Problema para actualizar la planta...", e);
			return "Failed!!" + e.getMessage();
		} finally {
			EntityManagerUtil.close(entity);
		}
		return null;
	}

	public String delete(Planta p) {
		EntityManager entity = null;
		try {
			entity = getEntityManager();
			entity.getTransaction().begin();
			entity.remove(entity.merge(p));
			entity.getTransaction().commit();
		} catch (Exception e) {
			log.error("Problema para eliminar la planta...", e);
			return "Failed!! " + e.getMessage();
		} finally {
			EntityManagerUtil.close(entity);
		}
		return null;
	}

	@Override
	public Planta buscarPorId(Integer id) {
		Planta planta = null;
		EntityManager em = null;
		
		try {
			em = EntityManagerUtil.getEntityManager();
			planta = em.find(Planta.class, id);
		} catch(Exception ex) {
			log.error("Problema para obtener la planta: " + id, ex);
		} finally {
			EntityManagerUtil.close(em);
		}
		return planta;
	}
	
	public Planta buscarPorId(Integer id, boolean isFullInfo) {
		Planta planta = null;
		EntityManager em = null;
		
		try {
			em = EntityManagerUtil.getEntityManager();
			planta = em.find(Planta.class, id);
			
			if(isFullInfo == false)
				return planta;
			
			log.info("Usuario: {}", planta.getIdUsuario().getidUsuario());
			log.info("Emisor: {}", planta.getIdEmisoresCFDIS().getCd_emisor());
			for(Certificado c : planta.getIdEmisoresCFDIS().getListaCertificado()) {
				log.info("Certificado: {}", c.getCdCertificado());
			}
			log.info("Serie Factura: {}", (planta.getSerieFacturaDefault() == null ? null : planta.getSerieFacturaDefault().getId()));
			
		} catch(Exception ex) {
			log.warn("Problema para obtener la información de la planta...", ex);
		} finally {
			EntityManagerUtil.close(em);
		}
		
		return planta;
	}
	

	@Override
	public List<Planta> buscarTodos() {
		List<Planta> listado = null;
		EntityManager em = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			listado = em.createNamedQuery("Planta.findAll", Planta.class).getResultList();
		} catch(Exception ex) {
			log.error("Problema para obtener el listado de cámaras...", ex);
		} finally {
			EntityManagerUtil.close(em);
		}
		return listado;
	}
	
	public List<Planta> cargarCamaras() {
		List<Planta> modelList = null;
		EntityManager em = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			modelList = em.createNamedQuery("Planta.findAll", Planta.class).getResultList();
			for(Planta planta : modelList) {
				planta.getCamaraList().forEach(item -> log.debug("Camara: {}", item.getCamaraCve()));
			}
		} catch(Exception ex) {
			log.error("Problema para obtener el listado de cámaras...", ex);
		} finally {
			EntityManagerUtil.close(em);
		}
		return modelList;
	}

	@Override
	public List<Planta> buscarPorCriterios(Planta e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String actualizar(Planta e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String guardar(Planta e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminar(Planta e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminarListado(List<Planta> listado) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
