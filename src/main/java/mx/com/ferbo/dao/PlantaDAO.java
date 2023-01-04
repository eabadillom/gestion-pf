package mx.com.ferbo.dao;

import static mx.com.ferbo.util.EntityManagerUtil.getEntityManager;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import mx.com.ferbo.model.Planta;
import mx.com.ferbo.model.Usuario;

public class PlantaDAO {

	@SuppressWarnings("unchecked")
	public List<Planta> findall() {
		EntityManager entity = getEntityManager();
		List<Planta> plantas = null;
		Query sql = entity.createNamedQuery("Planta.findAll", Planta.class);
		plantas = sql.getResultList();
		return plantas;
	}

	@SuppressWarnings("unchecked")
	public List<Usuario> getUsuarios() {
		EntityManager entity = getEntityManager();
		List<Usuario> usuarios = null;
		Query sql = entity.createQuery("SELECT u FROM Usuario u WHERE u.perfil IN (1, 4)");
		usuarios = sql.getResultList();
		return usuarios;
	}
	
	public String save(Planta p) {
		try {
			EntityManager entity = getEntityManager();
			entity.getTransaction().begin();
			entity.persist(p);
			entity.getTransaction().commit();
			entity.close();
		} catch (Exception e) {
			return "Failed!! " + e.getMessage();
		}
		return null;
	}

	public String update(Planta p) {
		try {
			EntityManager entity = getEntityManager();
			entity.getTransaction().begin();
			entity.merge(p);
			entity.getTransaction().commit();
			entity.close();
		} catch (Exception e) {
			return "Failed!! " + e.getMessage();
		}
		return null;
	}

	public String delete(Planta p) {
		try {
			EntityManager entity = getEntityManager();
			entity.getTransaction().begin();
			entity.remove(entity.merge(p));
			entity.getTransaction().commit();
			entity.close();
		} catch (Exception e) {
			return "Failed!! " + e.getMessage();
		}
		return null;
	}
}
