package mx.com.ferbo.dao;

import java.util.List;

import javax.persistence.EntityManager;

import mx.com.ferbo.model.CandadoSalida;
import mx.com.ferbo.util.EntityManagerUtil;

public class CandadoSalidaDAO {

	public List<CandadoSalida> findAll() {
		EntityManager entity = EntityManagerUtil.getEntityManager();
		List<CandadoSalida> list = null;
		list = entity.createNamedQuery("CandadoSalida.findAll", CandadoSalida.class).getResultList();
		return list;
	};

	public String update(CandadoSalida cS) {
		try {
			EntityManager entity = EntityManagerUtil.getEntityManager();
			entity.getTransaction().begin();
			entity.merge(cS);
			entity.getTransaction().commit();
			entity.close();
		} catch (Exception e) {
			return "Failed!! " + e.getMessage();
		}
		return null;
	};

}
