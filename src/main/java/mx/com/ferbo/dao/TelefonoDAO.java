package mx.com.ferbo.dao;

import java.util.List;

import javax.persistence.EntityManager;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.Telefono;
import mx.com.ferbo.util.EntityManagerUtil;

public class TelefonoDAO extends IBaseDAO<Telefono, Integer> {

	@Override
	public Telefono buscarPorId(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Telefono> buscarTodos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Telefono> buscarPorCriterios(Telefono e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String actualizar(Telefono telefono) {
		EntityManager em = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.merge(telefono);
			em.getTransaction().commit();
		} catch (Exception e) {
			System.err.println("ERROR" + e.getMessage());
			return "ERROR";
		} finally {
			EntityManagerUtil.close(em);
		}
		return null;
	}

	@Override
	public String guardar(Telefono telefono) {
		EntityManager em = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.persist(telefono);
			em.getTransaction().commit();
		} catch (Exception e) {
			System.err.println("ERROR" + e.getMessage());
			return "ERROR";
		} finally {
			EntityManagerUtil.close(em);
		}
		return null;
	}

	@Override
	public String eliminar(Telefono e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminarListado(List<Telefono> listado) {
		// TODO Auto-generated method stub
		return null;
	}

}
