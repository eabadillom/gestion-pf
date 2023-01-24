package mx.com.ferbo.dao;

import static mx.com.ferbo.util.EntityManagerUtil.getEntityManager;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.Perfil;
import mx.com.ferbo.model.Planta;
import mx.com.ferbo.model.Usuario;
import mx.com.ferbo.util.EntityManagerUtil;

public class UsuarioDAO extends IBaseDAO<Usuario, Integer>{

	@SuppressWarnings("unchecked")
	public List<Usuario> findall() {
		EntityManager entity = getEntityManager();
		List<Usuario> usuarios = null;
		Query sql = entity.createNamedQuery("Usuario.findAll", Usuario.class);
		usuarios = sql.getResultList();
		return usuarios;
	}
	@SuppressWarnings("unchecked")
	public List<Planta> getPlanta() {
		EntityManager entity = getEntityManager();
		List<Planta> planta = null;
		Query sql = entity.createQuery("SELECT u FROM Usuario ");
		planta = sql.getResultList();
		return planta;
	}
	@SuppressWarnings("unchecked")
	public List<Usuario> getUsuarios() {
		EntityManager entity = getEntityManager();
		List<Usuario> usuarios = null;
		Query sql = entity.createQuery("SELECT u FROM Usuario u");
		usuarios = sql.getResultList();
		return usuarios;
	}
	@SuppressWarnings("unchecked")
	public List<Perfil> getPerfil() {
		EntityManager entity = getEntityManager();
		List<Perfil> perfil = null;
		Query sql = entity.createQuery("SELECT u FROM Perfil u ");
		perfil = sql.getResultList();
		return perfil;
	}
	@Override
	public Usuario buscarPorId(Integer id) {
		Usuario usuario = null;
		EntityManager em = EntityManagerUtil.getEntityManager();
		usuario = em.createNamedQuery("findById", Usuario.class).getSingleResult();
		return usuario;
	}

	@Override
	public List<Usuario> buscarTodos() {
		List<Usuario> usuarios = null;
		EntityManager em = EntityManagerUtil.getEntityManager();
		usuarios = em.createNamedQuery("", Usuario.class).getResultList();
		return usuarios;
	}
	
	public Usuario buscarPorUsuario(String username) {
		Usuario usuario = null;
		EntityManager em = EntityManagerUtil.getEntityManager();
		usuario = em.createNamedQuery("Usuario.findByUsuario", Usuario.class)
				.setParameter("usuario", username)
				.getSingleResult();
		return usuario;
	}

	@Override
	public List<Usuario> buscarPorCriterios(Usuario e) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String guardar(Usuario u) {
		try {
			EntityManager entity = getEntityManager();
			entity.getTransaction().begin();
			entity.persist(u);
			entity.getTransaction().commit();
			entity.close();
		} catch (Exception e) {
			return "Failed!! " + e.getMessage();
		}return null;
	}

	@Override
	public String actualizar(Usuario u) {
		try {
			EntityManager entity = getEntityManager();
			entity.getTransaction().begin();
			entity.merge(u);
			entity.getTransaction().commit();
			entity.close();
		   } catch (Exception e) {
			return "Failed!!" + e.getMessage();
		}
			return null;
	}
	@Override
	public String eliminar(Usuario e) {
		try {
			EntityManager entity = getEntityManager();
			entity.getTransaction().begin();
			entity.remove(entity.merge(e));
			entity.getTransaction().commit();
			entity.close();
		} catch (Exception e1) {
			return "Failed!! " + e1.getMessage();
		}return null;
	}

	@Override
	public String eliminarListado(List<Usuario> listado) {
		// TODO Auto-generated method stub
		return null;
	}

}
