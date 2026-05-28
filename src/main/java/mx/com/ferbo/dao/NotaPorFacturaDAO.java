package mx.com.ferbo.dao;

import java.util.List;

import javax.persistence.EntityManager;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.NotaPorFactura;
import mx.com.ferbo.util.EntityManagerUtil;

public class NotaPorFacturaDAO extends IBaseDAO<NotaPorFactura, Integer> {

	@Override
	public NotaPorFactura buscarPorId(Integer id) {
		return null;
	}

	@Override
	public List<NotaPorFactura> buscarTodos() {
		return null;
	}

	@Override
	public List<NotaPorFactura> buscarPorCriterios(NotaPorFactura e) {
		return null;
	}

	@Override
	public String actualizar(NotaPorFactura nota) {
		try {

			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.merge(nota);
			em.getTransaction().commit();
			em.close();
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}

		return null;
	}

	@Override
	public String guardar(NotaPorFactura nota) {

		try {

			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.persist(nota);
			em.getTransaction().commit();
			em.close();
		} catch (Exception e) {
			System.out.println("ERROR: " + e.getMessage());
			return "ERROR";
		}

		return null;
	}

	@Override
	public String eliminar(NotaPorFactura e) {
		return null;
	}

	@Override
	public String eliminarListado(List<NotaPorFactura> listado) {
		return null;
	}

}
