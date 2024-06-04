package mx.com.ferbo.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import mx.com.ferbo.util.EntityManagerUtil;
import mx.com.ferbo.util.JPAEntity;
import mx.com.ferbo.model.ChequeDevuelto;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.Factura;

public class ChequesDevueltosDAO {
	
	EntityManager entity = JPAEntity.getEntity().createEntityManager();
	
	@SuppressWarnings("unchecked")
	public List<ChequeDevuelto> findAll(){
		List<ChequeDevuelto> cDevuelto;
		Query sql = entity.createNamedQuery("ChequeDevuelto.findAll", ChequeDevuelto.class);
		cDevuelto = sql.getResultList();
		System.out.println(cDevuelto + "*****************************************************");
		return cDevuelto;
	}
	
	public List<Factura> findDacturas(Cliente c) {
		EntityManager entity = EntityManagerUtil.getEntityManager();
		@SuppressWarnings("unchecked")
		List<Factura> list = entity.createQuery("SELECT f FROM Factura f WHERE f.cliente = :cliente")
				.setParameter("cliente", c).getResultList();
		return list;
	}
	
	 
}