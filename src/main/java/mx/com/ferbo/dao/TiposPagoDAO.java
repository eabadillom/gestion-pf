package mx.com.ferbo.dao;

import static mx.com.ferbo.util.EntityManagerUtil.getEntityManager;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import mx.com.ferbo.model.MedioPago;

public class TiposPagoDAO {

	@SuppressWarnings("unchecked")
	public List<MedioPago> findAll() {
		EntityManager entity = getEntityManager();
		List<MedioPago> tipos = null;
		Query sql = entity.createNamedQuery("MedioPago.findAll", MedioPago.class);
		tipos = sql.getResultList();
		entity.close();
		return tipos;
	}

	public String save(MedioPago mp) {
		try {
			EntityManager entity = getEntityManager();
			entity.getTransaction().begin();
			entity.persist(mp);
			entity.getTransaction().commit();
			entity.close();
		} catch (Exception e) {
			return "Failed!! " + e.getMessage();
		}
		return null;
	}

	public String update(MedioPago mp) {
		try {
			EntityManager entity = getEntityManager();
			entity.getTransaction().begin();
			entity.merge(mp);
			entity.getTransaction().commit();
			entity.close();
		} catch (Exception e) {
			return "Failed!! " + e.getMessage();
		}
		return null;
	}
}
