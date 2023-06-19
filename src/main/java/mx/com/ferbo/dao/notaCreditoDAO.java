package mx.com.ferbo.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import mx.com.ferbo.util.EntityManagerUtil;
import mx.com.ferbo.util.JPAEntity;
import mx.com.ferbo.model.NotaCredito;

public class notaCreditoDAO {
	
	EntityManager entity = JPAEntity.getEntity().createEntityManager();
	
	@SuppressWarnings("unchecked")
	public List<NotaCredito> findAll(){
		List<NotaCredito> notaCredito;
		Query sql = entity.createNamedQuery("NotaCredito.findAll", NotaCredito.class);
		notaCredito = sql.getResultList();
		System.out.println(notaCredito + "*****************************************************");
		return notaCredito;
	}
	
	public void guardar(NotaCredito notaCredito) {
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.persist(notaCredito);
			em.getTransaction().commit();
			em.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
	}
	
	 
}