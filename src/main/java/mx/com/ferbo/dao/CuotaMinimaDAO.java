package mx.com.ferbo.dao;

import java.util.List;

import javax.persistence.EntityManager;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.CuotaMinima;
import mx.com.ferbo.util.EntityManagerUtil;

public class CuotaMinimaDAO extends IBaseDAO<CuotaMinima, Integer> {

	@Override
	public CuotaMinima buscarPorId(Integer id) {
		return null;
	}

	@Override
	public List<CuotaMinima> buscarTodos() {
		List<CuotaMinima> listado;
		EntityManager em = EntityManagerUtil.getEntityManager();
		listado = em.createNamedQuery("CuotaMinima.findAll", CuotaMinima.class).getResultList();
		return listado;
	}

	@Override
	public List<CuotaMinima> buscarPorCriterios(CuotaMinima e) {
		if (e.getCliente().getCteCve() != null) {
			return this.buscarPorCliente(e);
		}
		return null;
	}

	@Override
	public String actualizar(CuotaMinima e) {
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.createNativeQuery("UPDATE cuota_minima SET cuota_value = :cuotaValue WHERE (cte_cve = :cteCve)")
					.setParameter("cuotaValue", e.getCuotaValue()).setParameter("cteCve", e.getCliente().getCteCve())
					.executeUpdate();
			em.getTransaction().commit();
			em.close();
		} catch (Exception ex) {
			System.out.println("ERROR" + ex.getMessage());
			return "ERROR";
		}
		return null;
	}

	@Override
	public String guardar(CuotaMinima e) {
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			int cuotaId = this.buscarTodos().size() - 1;
			em.createNativeQuery(
					"INSERT INTO cuota_minima (cte_cve, cuota_id, cuota_enabled, cuota_value) VALUES (:cteCve,:cuotaId, b'1', :cuotaValue)")
					.setParameter("cteCve", e.getCliente().getCteCve()).setParameter("cuotaId", cuotaId + 1)
					.setParameter("cuotaValue", e.getCuotaValue()).executeUpdate();
			em.getTransaction().commit();
			em.close();
		} catch (Exception ex) {
			System.out.println("ERROR" + ex.getMessage());
			return "ERROR";
		}

		return null;
	}

	@Override
	public String eliminar(CuotaMinima e) {
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.createNativeQuery("DELETE FROM cuota_minima WHERE (cte_cve = :cteCve)")
					.setParameter("cteCve", e.getCliente().getCteCve()).executeUpdate();
			em.getTransaction().commit();
			em.close();
		} catch (Exception ex) {
			System.out.println("ERROR" + ex.getMessage());
			return "ERROR";
		}
		return null;
	}

	@Override
	public String eliminarListado(List<CuotaMinima> listado) {
		return null;
	}

	private List<CuotaMinima> buscarPorCliente(CuotaMinima c) {
		EntityManager em = EntityManagerUtil.getEntityManager();
		return em.createNamedQuery("CuotaMinima.findByCteCve", CuotaMinima.class)
				.setParameter("cteCve", c.getCliente().getCteCve()).getResultList();
	}

	public int cuentaRegistros(CuotaMinima e) {
		EntityManager em = EntityManagerUtil.getEntityManager();
		em.getTransaction().begin();
		return em.createNativeQuery("Select count(cuota_id) cuota_minima").executeUpdate();
	}

}
