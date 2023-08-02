package mx.com.ferbo.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import mx.com.ferbo.model.CandadoSalida;
import mx.com.ferbo.util.EntityManagerUtil;

public class CandadoSalidaDAO {

	@SuppressWarnings("unchecked")
	public List<CandadoSalida> findAll() {
		EntityManager entity = EntityManagerUtil.getEntityManager();
		List<CandadoSalida> list = null;
		Query sql = entity.createNamedQuery("CandadoSalida.findAll", CandadoSalida.class);
		list = sql.getResultList();
		
		for(CandadoSalida c : list) {
			System.out.println(c.toString());
		}
		
		return list;
	};
	
	public String save(CandadoSalida candado) {
		
		EntityManager em = null;
		
		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.persist(candado);
			em.getTransaction().commit();
			
		} catch (Exception e) {
			return "Error" + e.getMessage();
		}finally {
			EntityManagerUtil.close(em);
		}
		
		return null;
	}

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
