package mx.com.ferbo.dao;

import java.util.List;

import javax.persistence.EntityManager;

import org.apache.log4j.Logger;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.UnidadDeManejo;
import mx.com.ferbo.util.EntityManagerUtil;

public class UnidadDeManejoDAO extends IBaseDAO<UnidadDeManejo, Integer> {
	private static Logger log = Logger.getLogger(UnidadDeManejoDAO.class);

	@Override
	public UnidadDeManejo buscarPorId(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UnidadDeManejo> buscarTodos() {
		List<UnidadDeManejo> listado = null;
		EntityManager em = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			listado = em.createNamedQuery("UnidadDeManejo.findAll", UnidadDeManejo.class).getResultList();
		} catch(Exception ex) {
			log.error("Problema para obtener el listado de unidades de manejo...", ex);
		} finally {
			EntityManagerUtil.close(em);
		}
		
		return listado;
	}

	@Override
	public List<UnidadDeManejo> buscarPorCriterios(UnidadDeManejo e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String actualizar(UnidadDeManejo unidadManejo) {
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.merge(unidadManejo);
			em.getTransaction().commit();
			em.close();
		} catch (Exception e) {
			System.out.println("ERROR actualizando Banco" + e.getMessage());
			return "ERROR";
		}
		return null;
	}

	@Override
	public String guardar(UnidadDeManejo unidadManejo) {
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.persist(unidadManejo);
			em.getTransaction().commit();
			em.close();
		} catch (Exception e) {
			System.out.println("ERROR guardando Banco" + e.getMessage());
			return "ERROR";
		}
		return null;
	}

	@Override
	public String eliminar(UnidadDeManejo unidadManejo) {
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.remove(em.merge(unidadManejo));
			em.getTransaction().commit();
			em.close();
		} catch (Exception e) {
			System.out.println("ERROR" + e.getMessage());
			return "ERROR";
		}
		return null;
	}

	@Override
	public String eliminarListado(List<UnidadDeManejo> listado) {
		// TODO Auto-generated method stub
		return null;
	}
}
