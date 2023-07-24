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
		Query sql = entity.createNamedQuery("Usuario.findAll", Usuario.class);
		usuarios = sql.getResultList();
		return usuarios;
	}

	public String save(Planta p) {
		System.out.println(p.getIdUsuario());
		try {
			EntityManager entity = getEntityManager();
			entity.getTransaction().begin();
			Query sql = entity.createNativeQuery(
					"insert into PLANTA (PLANTA_DS, planta_abrev, planta_sufijo, id_usuario) values(?,?,?,?)");
			sql.setParameter(1, p.getPlantaDs());
			sql.setParameter(2, p.getPlantaAbrev());
			sql.setParameter(3, p.getPlantaSufijo());
			sql.setParameter(4, p.getIdUsuario().getId());
			sql.executeUpdate();
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
			Query sql = entity.createNativeQuery(
					"update PLANTA set PLANTA_DS=?, planta_abrev=?, planta_sufijo=?, id_usuario=? where PLANTA_CVE=?;");
			sql.setParameter(1, p.getPlantaDs());
			sql.setParameter(2, p.getPlantaAbrev());
			sql.setParameter(3, p.getPlantaSufijo());
			sql.setParameter(4, p.getIdUsuario().getId());
			sql.setParameter(5, p.getPlantaCve());
			sql.executeUpdate();
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
