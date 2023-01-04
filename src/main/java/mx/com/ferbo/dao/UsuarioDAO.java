package mx.com.ferbo.dao;

import java.util.List;

import javax.persistence.EntityManager;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.Usuario;
import mx.com.ferbo.util.EntityManagerUtil;

public class UsuarioDAO extends IBaseDAO<Usuario, Integer>{

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
	public String actualizar(Usuario e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String guardar(Usuario e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminar(Usuario e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminarListado(List<Usuario> listado) {
		// TODO Auto-generated method stub
		return null;
	}

}
