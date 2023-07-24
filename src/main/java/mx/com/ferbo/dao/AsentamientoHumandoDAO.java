package mx.com.ferbo.dao;

import java.util.List;

import javax.persistence.EntityManager;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.AsentamientoHumano;
import mx.com.ferbo.util.EntityManagerUtil;

public class AsentamientoHumandoDAO extends IBaseDAO<AsentamientoHumano, Integer> {

	@Override
	public AsentamientoHumano buscarPorId(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AsentamientoHumano> buscarTodos() {
		List<AsentamientoHumano> listado = null;
		EntityManager em = EntityManagerUtil.getEntityManager();
		listado = em.createNamedQuery("AsentamientoHumano.findAll", AsentamientoHumano.class).getResultList();
		return listado;
	}

	@Override
	public List<AsentamientoHumano> buscarPorCriterios(AsentamientoHumano e) {		
		List<AsentamientoHumano> listado = null;
		EntityManager em = EntityManagerUtil.getEntityManager();
		listado = em.createNamedQuery("AsentamientoHumano.findByDomicilio", AsentamientoHumano.class).setParameter("paisCve", e.getAsentamientoHumanoPK().getPaisCve()).setParameter("estadoCve", e.getAsentamientoHumanoPK().getEstadoCve()).setParameter("municipioCve", e.getAsentamientoHumanoPK().getMunicipioCve()).setParameter("ciudadCve", e.getAsentamientoHumanoPK().getCiudadCve()).getResultList();
		return listado;
	}

	@Override
	public String actualizar(AsentamientoHumano asentamientoHumano) {
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.merge(asentamientoHumano);
			em.getTransaction().commit();
			em.close();
		} catch (Exception e) {
			System.out.println("ERROR guardando Asentamiento Humano" + e.getMessage());
			return "ERROR";
		}
		return null;
	}

	@Override
	public String guardar(AsentamientoHumano asentamientoHumano) {
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.persist(asentamientoHumano);
			em.getTransaction().commit();
			em.close();
		} catch (Exception e) {
			System.out.println("ERROR guardando Asentamiento Humano" + e.getMessage());
			return "ERROR";
		}
		return null;
	}

	@Override
	public String eliminar(AsentamientoHumano asentamientoHumano) {
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.remove(em.merge(asentamientoHumano));
			em.getTransaction().commit();
			em.close();
		} catch (Exception e) {
			System.out.println("ERROR" + e.getMessage());
			return "ERROR";
		}
		return null;
	}

	@Override
	public String eliminarListado(List<AsentamientoHumano> listado) {
		// TODO Auto-generated method stub
		return null;
	}

}
