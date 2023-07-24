package mx.com.ferbo.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.Factura;
import mx.com.ferbo.util.EntityManagerUtil;

public class ModPlazosPagoDAO {

	public List<Factura> findDacturas(Cliente c, Date de, Date hasta) {
		EntityManager entity = EntityManagerUtil.getEntityManager();
		@SuppressWarnings("unchecked")
		List<Factura> list = entity.createQuery(
				"SELECT f FROM Factura f WHERE f.cliente = :cliente AND f.status IN (1, 4) AND f.fecha BETWEEN :de AND :hasta")
				.setParameter("cliente", c).setParameter("de", de).setParameter("hasta", hasta).getResultList();
		return list;
	};

	public String update(List<Factura> list, int num) {
		try {
			EntityManager entity = EntityManagerUtil.getEntityManager();
			entity.getTransaction().begin();
			for (Factura f : list) {
				f.setPlazo(num);
				entity.merge(f);
			}
			entity.getTransaction().commit();
			entity.close();
		} catch (Exception e) {
			return e.getMessage();
		}
		return null;
	};
}
