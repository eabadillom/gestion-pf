package mx.com.ferbo.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.Categoria;
import mx.com.ferbo.util.EntityManagerUtil;

public class CategoriaDAO extends IBaseDAO<Categoria, Integer>{
	private static Logger log = Logger.getLogger(CategoriaDAO.class); 

	@Override
	public Categoria buscarPorId(Integer id) {
		Categoria categoria = null;
		EntityManager em = null;
		Query query = null;
		
		try {
			em = EntityManagerUtil.getEntityManager();
			query = em.createNamedQuery("Categoria.findByCategoriaCve", Categoria.class)
					.setParameter("categoriaCve", id)
					;
			categoria = (Categoria) query.getSingleResult();
		} catch(Exception ex) {
			log.error("Problema al consultar la categoria: " + id, ex);
		} finally {
			EntityManagerUtil.close(em);
		}
		return categoria;
	}

	@Override
	public List<Categoria> buscarTodos() {
		EntityManager em = null;
		List<Categoria> list = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			list = em.createNamedQuery("Categoria.findAll", Categoria.class).getResultList();
		} catch(Exception ex) {
			log.error("Problema para obtener el listado de categorías...", ex);
		} finally {
			EntityManagerUtil.close(em);
		}
		return list;
	}

	@Override
	public List<Categoria> buscarPorCriterios(Categoria e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String actualizar(Categoria e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String guardar(Categoria e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminar(Categoria e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminarListado(List<Categoria> listado) {
		// TODO Auto-generated method stub
		return null;
	}

}
