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
import mx.com.ferbo.model.Ciudades;
import mx.com.ferbo.util.EntityManagerUtil;

public class CiudadesDAO extends IBaseDAO<Ciudades, Integer> {

	private static Logger log = LogManager.getLogger(CiudadesDAO.class);

	@SuppressWarnings("unchecked")
	public List<Ciudades> findall() {

		EntityManager entity = null;
		List<Ciudades> ciudades = null;

		try {
			entity = getEntityManager();
			Query sql = entity.createNamedQuery("Ciudades.findAll", Ciudades.class);
			ciudades = sql.getResultList();
		} catch (Exception e) {
			log.error("Problema al recuperar ciudades", e);
		} finally {
			EntityManagerUtil.close(entity);
		}

		return ciudades;
	}

	@Override
	public Ciudades buscarPorId(Integer id) {
		
		return null;
	}

	@Override
	public List<Ciudades> buscarTodos() {

		EntityManager em = null;
		List<Ciudades> listado = null;

		try {
			em = EntityManagerUtil.getEntityManager();
			listado = em.createNamedQuery("Ciudades.findAll", Ciudades.class).getResultList();
		} catch (Exception e) {
			log.error("Problema al recuperar ciudades", e);
		} finally {
			EntityManagerUtil.close(em);
		}

		return listado;
	}

	public List<Ciudades> buscarPorCriteriosCiudades(Ciudades e) {

		EntityManager em = null;
		List<Ciudades> listado = null;

		try {
			em = EntityManagerUtil.getEntityManager();
			listado = em.createNamedQuery("Ciudades.findByPaisCveEstadoCveMunicipioCve", Ciudades.class)
					.setParameter("paisCve", e.getCiudadesPK().getPaisCve())
					.setParameter("estadoCve", e.getCiudadesPK().getEstadoCve())
					.setParameter("municipioCve", e.getCiudadesPK().getMunicipioCve()).getResultList();
		} catch (Exception e2) {
			
		} finally {
			EntityManagerUtil.close(em);
		}

		return listado;
	}

	@Override
	public List<Ciudades> buscarPorCriterios(Ciudades e) {// verificar que funcione correctamente
		

		EntityManager em = null;
		List<Ciudades> listado = null;

		try {
			em = EntityManagerUtil.getEntityManager();
			if (e.getMunicipios().getMunicipiosPK().getMunicipioCve() > 0) {
				TypedQuery<Ciudades> consEstados = em.createNamedQuery("Ciudades.findByEstadoMunicipioCve",
						Ciudades.class);
				consEstados.setParameter("municipioCve", e.getMunicipios().getMunicipiosPK().getMunicipioCve())
						.setParameter("estadoCve", e.getMunicipios().getEstados().getEstadosPK().getEstadoCve());
				listado = consEstados.getResultList();
			}
			if (e.getCiudadesPK().getPaisCve() != -1 && e.getCiudadesPK().getEstadoCve() != -1
					&& e.getCiudadesPK().getMunicipioCve() != -1) {
				listado = em.createNamedQuery("Ciudades.findByPaisCveEstadoCveMunicipioCve", Ciudades.class)
						.setParameter("paisCve", e.getCiudadesPK().getPaisCve())
						.setParameter("estadoCve", e.getCiudadesPK().getEstadoCve())
						.setParameter("municipioCve", e.getCiudadesPK().getMunicipioCve()).getResultList();
			}

		} catch (Exception e2) {
			
		} finally {
			EntityManagerUtil.close(em);
		}

		return listado;
	}

	@Override
	public String actualizar(Ciudades ciudades) {

		EntityManager em = null;

		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.merge(ciudades);
			em.getTransaction().commit();
		} catch (Exception e) {
			System.out.println("ERROR guardando Municipio" + e.getMessage());
			return "ERROR";
		} finally {
			EntityManagerUtil.close(em);
		}
		return null;
	}

	@Override
	public String guardar(Ciudades ciudades) {

		EntityManager em = null;

		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.persist(ciudades);
			em.getTransaction().commit();
		} catch (Exception e) {
			System.out.println("ERROR guardando Municipio" + e.getMessage());
			return "ERROR";
		} finally {
			EntityManagerUtil.close(em);
		}
		return null;
	}

	@Override
	public String eliminar(Ciudades ciudades) {

		EntityManager em = null;

		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
//			em.remove(em.merge(ciudades));
			em.createQuery(
					"DELETE FROM Ciudades c WHERE c.ciudadesPK.paisCve =:paisCve and c.ciudadesPK.estadoCve =:estadoCve and c.ciudadesPK.municipioCve =:municipioCve and c.ciudadesPK.ciudadCve =:ciudadCve")
					.setParameter("paisCve", ciudades.getCiudadesPK().getPaisCve())
					.setParameter("estadoCve", ciudades.getCiudadesPK().getEstadoCve())
					.setParameter("municipioCve", ciudades.getCiudadesPK().getMunicipioCve())
					.setParameter("ciudadCve", ciudades.getCiudadesPK().getCiudadCve()).executeUpdate();
			em.getTransaction().commit();
		} catch (Exception e) {
			System.out.println("ERROR" + e.getMessage());
			return "ERROR";
		} finally {
			EntityManagerUtil.close(em);
		}
		return null;
	}

	@Override
	public String eliminarListado(List<Ciudades> listado) {
		
		return null;
	}

	public List<Ciudades> buscaPorId(Integer id) {
		EntityManager em = EntityManagerUtil.getEntityManager();
		return em.createNamedQuery("Ciudades.findByCiudadCve", Ciudades.class).setParameter("ciudadCve", id)
				.getResultList();
	}

	public List<Ciudades> buscaPorAsentamiento(AsentamientoHumano as) {
		EntityManager em = EntityManagerUtil.getEntityManager();
		return em.createNamedQuery("Ciudades.findByTodo", Ciudades.class)
				.setParameter("municipioCve", as.getAsentamientoHumanoPK().getMunicipioCve())
				.setParameter("estadoCve", as.getAsentamientoHumanoPK().getEstadoCve())
				.setParameter("ciudadCve", as.getAsentamientoHumanoPK().getCiudadCve()).getResultList();
	}
}
