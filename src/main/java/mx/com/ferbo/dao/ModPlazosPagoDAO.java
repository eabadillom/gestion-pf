package mx.com.ferbo.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.Factura;
import mx.com.ferbo.util.EntityManagerUtil;

public class ModPlazosPagoDAO {
	private static Logger log = LogManager.getLogger(ModPlazosPagoDAO.class);

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
	
	public String updatePlazo(Factura factura, int plazo) {
		String result = null;
		EntityManager entity = null;
		int rows = -1;
		try {
			entity = EntityManagerUtil.getEntityManager();
			entity.getTransaction().begin();
			rows = entity.createNativeQuery("UPDATE factura SET plazo = :plazo WHERE factura.id = :idFactura")
				.setParameter("plazo", plazo)
				.setParameter("idFactura", factura.getId())
				.executeUpdate()
				;
			log.info("Registros afectados: ({})", rows);
			entity.getTransaction().commit();
		} catch (Exception e) {
			return e.getMessage();
		} finally {
			EntityManagerUtil.close(entity);
		}
		return result;
	}
}
