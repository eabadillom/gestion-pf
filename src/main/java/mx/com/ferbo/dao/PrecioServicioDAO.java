package mx.com.ferbo.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.Aviso;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.PrecioServicio;
import mx.com.ferbo.model.Servicio;
import mx.com.ferbo.util.EntityManagerUtil;

public class PrecioServicioDAO extends IBaseDAO<PrecioServicio, Integer> {
	private static Logger log = LogManager.getLogger(PrecioServicioDAO.class);

	@Override
	public PrecioServicio buscarPorId(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PrecioServicio> buscarTodos() {
		List<PrecioServicio> listado = new ArrayList<>();
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			listado = em.createNamedQuery("PrecioServicio.findAll", PrecioServicio.class).getResultList();
		} catch (Exception e) {
			log.error("Problema para buscar a todos los clientes...", e);
		}
		return listado;
	}

	@Override
	public List<PrecioServicio> buscarPorCriterios(PrecioServicio e) {
		if(e.getCliente().getCteCve() == null)
			return null;
		if(e.getServicio()!=null) {
			return this.buscarPorClienteServicio(e);
		}
		if(e.getAvisoCve()!=null) {
			return this.buscarPorClienteAviso(e);
		}
		return this.buscarPorCliente(e);
	}
	
	public PrecioServicio buscar(Integer cteCve, Integer avisoCve, Integer servicioCve, boolean isFullInfo) {
		PrecioServicio precio = null;
		EntityManager em = null;
		Query query = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			query = em.createNamedQuery("PrecioServicio.findByClienteAvisoServicio", PrecioServicio.class);
			precio = (PrecioServicio) query
					.setParameter("cteCve", cteCve)
					.setParameter("avisoCve", avisoCve)
					.getSingleResult();
			if(isFullInfo == false)
				return precio;
			precio.getServicio().getServicioCve();
		} catch(Exception ex) {
			log.error("Problema para obtener el precio-servicio...", ex);
		} finally {
			EntityManagerUtil.close(em);
		}
		return precio;
	}
	
	public List<PrecioServicio> buscarPorAviso(Aviso aviso, Cliente cliente){
		List<PrecioServicio> listaPrecioServicio = new ArrayList<>();
		EntityManager entity = null;
		try {
			entity = EntityManagerUtil.getEntityManager();
			listaPrecioServicio = entity.createNamedQuery("PrecioServicio.findByAvisoAndCliente", PrecioServicio.class)
					.setParameter("cteCve", cliente.getCteCve())
					.setParameter("avisoCve", aviso.getAvisoCve()).getResultList();
		} catch (Exception e) {
			log.error("Problema para obtener el PrecioServicio...", e);
		} finally {
			EntityManagerUtil.close(entity);
		}
		return listaPrecioServicio;
	}
	

	public List<PrecioServicio> busquedaServicio(Integer avisoCve, Integer clienteCve, Integer servicioCve) {
		List<PrecioServicio> precioServicio = null;
		EntityManager entity = null;
		try {
			entity = EntityManagerUtil.getEntityManager();
			precioServicio = entity.createNamedQuery("PrecioServicio.findByServicioAndAvisoAndCliente", PrecioServicio.class)
					.setParameter("cteCve", clienteCve)
					.setParameter("avisoCve", avisoCve)
					.setParameter("servicioCve", servicioCve).getResultList();
		} catch (Exception e) {
			log.error("Problema para obtener el precio servicio...", e);
		} finally {
			EntityManagerUtil.close(entity);
		}
		return precioServicio;
	}
	
	@SuppressWarnings("unchecked")
	public List<PrecioServicio> buscarDisponibles(Integer cteCve, Integer avisoCve) {
		List<PrecioServicio> lista = null;
		EntityManager em = null;
		Query query = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			query = em.createNativeQuery("SELECT ps.id, ps.cliente, ps.servicio, ps.unidad, ps.precio, ps.aviso_cve FROM precio_servicio ps "
					+ "					LEFT OUTER JOIN (SELECT t.cliente, t.servicio, t.unidad, "
					+ "					t.precio, t.aviso_cve FROM precio_servicio t "
					+ "					WHERE t.cliente = :cteCve AND t.aviso_cve = :avisoCve ) tmp ON "
					+ "					ps.cliente = tmp.cliente AND ps.servicio = tmp.servicio WHERE ps.aviso_cve = 1 "
					+ "					AND ps.cliente = :cteCve AND (tmp.cliente IS NULL AND tmp.servicio IS NULL)", PrecioServicio.class)
					.setParameter("cteCve", cteCve)
					.setParameter("avisoCve", avisoCve)
					;
			lista = query.getResultList();
		} catch(Exception ex) {
			log.error("Problema para obtener el precio servicio...", ex);
		} finally {
			EntityManagerUtil.close(em);
		}
		return lista;
	}

	@Override
	public String actualizar(PrecioServicio precioServicio) {
		EntityManager em = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.merge(precioServicio);
			em.getTransaction().commit();
		} catch (Exception e) {
			log.error("Problema para actualizar el precio servicio...", e);
			return "ERROR";
		} finally {
			EntityManagerUtil.close(em);
		}
		return null;
	}

	@Override
	public String guardar(PrecioServicio precioServicio) {
		EntityManager em = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.persist(precioServicio);
			em.getTransaction().commit();
		} catch (Exception e) {
 			log.error("Problema para guadar el precio servicio...", e);
			return "ERROR";
		} finally {
			EntityManagerUtil.close(em);
		}
		return null;
	}

	@Override
	public String eliminar(PrecioServicio precioServicio) {
		EntityManager em = null;
		Query query = null;
		PrecioServicio ps = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			query = em.createNamedQuery("PrecioServicio.findById", PrecioServicio.class)
					.setParameter("id", precioServicio.getId())
					;
			ps = (PrecioServicio) query.getSingleResult();
			em.remove(ps);
			em.getTransaction().commit();
			
		} catch (Exception e) {
			log.error("Problema para eliminar el precio servicio...", e);
			return "ERROR";
		} finally {
			EntityManagerUtil.close(em);
		}
		return null;
	}

	@Override
	public String eliminarListado(List<PrecioServicio> listado) {
		String val = "";
		for(PrecioServicio ps:listado) {
			val=this.eliminar(ps);
		}
		return val != null ? val:null;
	}
	
	public PrecioServicio getPrecioMinimoPorServicio(Integer idServicio) {
		ServicioDAO servicioDAO = new ServicioDAO();
		Servicio servicio = null;
		Integer servicioCve = null;
		PrecioServicio bean = null;
		EntityManager em = null;
		Query nativeQuery = null;
		Object[] obj = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			nativeQuery = em.createNativeQuery("SELECT servicio, min(precio) as precio FROM precio_servicio WHERE servicio = :idServicio GROUP BY servicio ORDER BY servicio, precio")
					.setParameter("idServicio", idServicio);
			obj = (Object[]) nativeQuery.getSingleResult();
			//bean = (PrecioServicio) nativeQuery.getSingleResult();
			servicioCve = (Integer) obj[0];
			bean = new PrecioServicio();
			bean.setPrecio((BigDecimal)obj[1]);
			servicio = servicioDAO.buscarPorId(servicioCve);
			bean.setServicio(servicio);
			em.getTransaction().commit();
		} catch(PersistenceException ex) {
			log.error("Problema para obtener el precio servicio...", ex);
		} catch(Exception ex) {
			log.error("Problema para obtener el precio servicio...", ex);
		} finally {
			EntityManagerUtil.close(em);
		}
		return bean;
	}
	
	public List<PrecioServicio> buscarPorCliente(Integer cteCve, boolean isFullInfo) {
		List<PrecioServicio> list = null;
		EntityManager em = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			list = em.createNamedQuery("PrecioServicio.findByCliente", PrecioServicio.class)
			.setParameter("cteCve", cteCve)
			.getResultList()
			;
			if(isFullInfo == false)
				return list;
			for(PrecioServicio ps : list) {
				log.debug(ps.getCliente().getCteCve());
				log.debug(ps.getServicio().getServicioCve());
				log.debug(ps.getUnidad().getUnidadDeManejoCve());
				log.debug(ps.getAvisoCve().getAvisoCve());
			}
		} catch(Exception ex) {
			log.error("Problema para obtener el listado de precios...", ex);
		} finally {
			EntityManagerUtil.close(em);
		}
		return list;
	}

	private List<PrecioServicio> buscarPorCliente(PrecioServicio e) {
		List<PrecioServicio> list = null;
		EntityManager em = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			list = em.createNamedQuery("PrecioServicio.findByCliente", PrecioServicio.class)
					.setParameter("cteCve", e.getCliente().getCteCve()).getResultList();
		} catch(Exception ex) {
			log.error("Problema para obtener la lista de precio servicio...", ex);
		} finally {
			EntityManagerUtil.close(em);
		}
		
		return list;
	}
	
	private List<PrecioServicio> buscarPorClienteServicio(PrecioServicio e){
		List<PrecioServicio> lista = null;
		EntityManager em = null;
		
		try {
			em = EntityManagerUtil.getEntityManager();
			lista = em.createNamedQuery("PrecioServicio.findByClienteServicio", PrecioServicio.class)
					.setParameter("cteCve", e.getCliente().getCteCve())
					.setParameter("servicioCve", e.getServicio())
					.getResultList();
		} catch(Exception ex) {
			log.error("Problema para obtener la lista de precio servicio...", ex);
		} finally {
			EntityManagerUtil.close(em);
		}
		return lista;
	}
	
	private List<PrecioServicio> buscarPorClienteAviso(PrecioServicio e){
		List<PrecioServicio> lista = null;
		EntityManager em = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			lista = em.createNamedQuery("PrecioServicio.findByClienteAviso", PrecioServicio.class)
					.setParameter("cteCve", e.getCliente().getCteCve())
					.setParameter("avisoCve", e.getAvisoCve().getAvisoCve())
					.getResultList();
		} catch(Exception ex) {
			log.error("Problema para obtener el listado de precio servicio...", ex);
		} finally {
			EntityManagerUtil.close(em);
		}
		return lista;
	}	
	
	public int obtenFinal() {
		int valorFinal=0;
		EntityManager em = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			valorFinal=(int) em.createNativeQuery("Select max(id) from precio_servicio").getSingleResult();
			em.getTransaction().commit();
		} catch (Exception ex) {
			System.out.println("ERROR" + ex.getMessage());
			return 0;
		} finally {
			EntityManagerUtil.close(em);
		}
		return valorFinal;
	}
}