package mx.com.ferbo.dao;

import static mx.com.ferbo.util.EntityManagerUtil.getEntityManager;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.jfree.util.Log;

import mx.com.ferbo.model.MedioPago;
import mx.com.ferbo.util.EntityManagerUtil;

public class TiposPagoDAO {

	@SuppressWarnings("unchecked")
	public List<MedioPago> findAll() {
		EntityManager entity = null;
		List<MedioPago> tipos = null;
		try {
			entity = EntityManagerUtil.getEntityManager();
			Query sql = entity.createNamedQuery("MedioPago.findAll", MedioPago.class);
			tipos = sql.getResultList();
		} catch (Exception e) {
			Log.error("Problemas para obtener informacion", e);
		} finally {
			EntityManagerUtil.close(entity);
		}
		return tipos;
	}

	public String save(MedioPago mp) {

		EntityManager entity = null;

		try {
			entity = getEntityManager();
			entity.getTransaction().begin();
			entity.persist(mp);
			entity.getTransaction().commit();
		} catch (Exception e) {
			Log.error("Problema al guardar medio pago", e);
		} finally {
			EntityManagerUtil.close(entity);
		}
		return null;
	}

	public String update(MedioPago mp) {

		EntityManager entity = null;

		try {
			entity = getEntityManager();
			entity.getTransaction().begin();
			entity.merge(mp);
			entity.getTransaction().commit();
		} catch (Exception e) {
			Log.error("Problema para actualizar", e);
		} finally {
			EntityManagerUtil.close(entity);
		}
		return null;
	}
}
