package mx.com.ferbo.dao;

import static mx.com.ferbo.util.EntityManagerUtil.getEntityManager;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.Camara;
import mx.com.ferbo.model.Planta;
import mx.com.ferbo.util.EntityManagerUtil;

public class CamaraDAO extends IBaseDAO<Camara, Integer> {

	
	@SuppressWarnings("unchecked")
	public List<Camara> findall() {
		EntityManager entity = getEntityManager();
		List<Camara> camara = null;
		Query sql = entity.createNamedQuery("Camara.findAll", Camara.class);
		camara = sql.getResultList();
		return camara;
	}
	@Override
	public Camara buscarPorId(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Camara> buscarTodos() {
		List<Camara> listado = null;
		EntityManager em = EntityManagerUtil.getEntityManager();
		listado = em.createNamedQuery("Camara.findAll", Camara.class).getResultList();
		return listado;
	}

	@Override
	public List<Camara> buscarPorCriterios(Camara e) {
		
		return null;
	}

	@Override
	public String actualizar(Camara camara) {
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.merge(camara);
			em.getTransaction().commit();
		} catch (Exception e) {
			System.out.println("ERROR" + e.getMessage());
			return "ERROR";
		}
		return null;
	}

	@Override
	public String guardar(Camara camara) {
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.persist(camara);
			em.getTransaction().commit();
		} catch (Exception e) {
			System.out.println("ERROR" + e.getMessage());
			return "ERROR";
		}
		return null;
	}

	@Override
	public String eliminar(Camara camara) {
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.remove(em.merge(camara));
			em.getTransaction().commit();
		} catch (Exception e) {
			System.out.println("ERROR" + e.getMessage());
			return "ERROR";
		}
		return null;
	}

	@Override
	public String eliminarListado(List<Camara> listado) {
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			for (Camara camara : listado) {
				em.remove(em.merge(camara));
			}
			em.getTransaction().commit();
		} catch (Exception e) {
			System.out.println("ERROR" + e.getMessage());
			return "ERROR";
		}
		return null;
	}

}
