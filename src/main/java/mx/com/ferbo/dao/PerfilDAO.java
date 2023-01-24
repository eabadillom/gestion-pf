package mx.com.ferbo.dao;

import static mx.com.ferbo.util.EntityManagerUtil.getEntityManager;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.Perfil;
import mx.com.ferbo.model.Usuario;
import mx.com.ferbo.util.EntityManagerUtil;

public class PerfilDAO extends IBaseDAO<Perfil, Integer> {
	
	@SuppressWarnings("unchecked")
	public List<Perfil> findall() {
		EntityManager entity = getEntityManager();
		List<Perfil> perfil = null;
		Query sql = entity.createNamedQuery("Perfil.findAll", Perfil.class);
		perfil = sql.getResultList();
		return perfil;
	}
	@Override
	public Perfil buscarPorId(Integer id) {
		EntityManager entity = getEntityManager();
		Perfil perfil = null;
		Query sql = entity.createNamedQuery("Perfil.findById",Perfil.class)
				.setParameter("id", id);
		perfil = (Perfil) sql.getSingleResult();
		
		return perfil;
	}

	@Override
	public List<Perfil> buscarTodos() {
		List<Perfil> perfil= null;
		EntityManager em = EntityManagerUtil.getEntityManager();
		perfil = em.createNamedQuery("Perfil.findAll", Perfil.class).getResultList();
		return perfil;
	}

	@Override
	public List<Perfil> buscarPorCriterios(Perfil e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String actualizar(Perfil perfil) {
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.merge(perfil);
			em.getTransaction().commit();
			em.close();
		} catch (Exception e) {
			System.out.println("ERROR actualizando Países" + e.getMessage());
			return "ERROR";
		}
		return null;
	}

	@Override
	public String guardar(Perfil perfil) {
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.persist(perfil);
			em.getTransaction().commit();
			em.close();
		} catch (Exception e) {
			System.out.println("ERROR guardando Perfil" + e.getMessage());
			return "ERROR";
		}return null;
	}

	@Override
	public String eliminar(Perfil perfil) {
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.remove(em.merge(perfil));
			em.getTransaction().commit();
			em.close();
		} catch (Exception e) {
			System.out.println("ERROR" + e.getMessage());
			return "ERROR";
		}return null;
	}

	@Override
	public String eliminarListado(List<Perfil> listado) {
				return null;
	}
	public List<Perfil> buscaPorId(Integer id) {
		EntityManager em = EntityManagerUtil.getEntityManager();
		return em.createNamedQuery("Paises.findById", Perfil.class)
				.setParameter("paisCve", id).getResultList();
	}
	@SuppressWarnings("unchecked")
	public List<Perfil> getPerfil() {
		EntityManager entity = getEntityManager();
		List<Perfil> perfil = null;
		Query sql = entity.createQuery("SELECT u FROM Perfil u WHERE u.nombre IN (1, 2)");
		perfil = sql.getResultList();
		return perfil;
	}

}
