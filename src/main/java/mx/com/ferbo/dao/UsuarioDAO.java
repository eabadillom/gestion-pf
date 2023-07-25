package mx.com.ferbo.dao;

import static mx.com.ferbo.util.EntityManagerUtil.getEntityManager;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.Perfil;
import mx.com.ferbo.model.Planta;
import mx.com.ferbo.model.Usuario;
import mx.com.ferbo.util.EntityManagerUtil;

public class UsuarioDAO extends IBaseDAO<Usuario, Integer>{
	
	private static Logger log = LogManager.getLogger(UsuarioDAO.class);

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
		EntityManager em = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			usuario = em.createNamedQuery("Usuario.findByUsuario", Usuario.class)
					.setParameter("usuario", username)
					.getSingleResult();
		} catch(Exception ex) {
			log.error("Problema para obtener el usuario: " + username, ex);
		} finally {
			EntityManagerUtil.close(em);
		}
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
	public String eliminar(Usuario u) {
		try {
			EntityManager entity = getEntityManager();
			entity.getTransaction().begin();
			entity.remove(entity.merge(u));
			entity.getTransaction().commit();
			entity.close();
		} catch (Exception e) {
			return "Failed!! " + e.getMessage();
		}
		return null;
	}

	@Override
	public String eliminarListado(List<Usuario> listado) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public List<Usuario> buscarPorPerfil(Integer idPerfil) {
		List<Usuario> usuarios = null;
		EntityManager em = null;
		
		try {
			em = EntityManagerUtil.getEntityManager();
			usuarios = em.createNamedQuery("Usuario.findByPerfil", Usuario.class)
			.setParameter("perfil", idPerfil)
			.getResultList()
			;
		} catch(Exception ex) {
			log.error("Problema para obtener el listado de usuarios del perfil " + idPerfil, ex);
		} finally {
			EntityManagerUtil.close(em);
		}
		
		return usuarios;
	}

}
