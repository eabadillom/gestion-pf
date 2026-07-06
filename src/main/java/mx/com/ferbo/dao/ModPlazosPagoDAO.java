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
        
        @SuppressWarnings("unchecked")
	public List<Factura> findDacturas(Cliente c, Date de, Date hasta) {
            List<Factura> list = null;
            EntityManager entity = null; 
            try {
                entity = EntityManagerUtil.getEntityManager();
		
		list = entity.createQuery("SELECT f FROM Factura f WHERE f.cliente = :cliente AND f.status IN (1, 4) AND f.fecha BETWEEN :de AND :hasta")
                    .setParameter("cliente", c)
                    .setParameter("de", de)
                    .setParameter("hasta", hasta)
                    .getResultList();
            } catch (Exception e) {
                    log.error("Problemas para obtener la lista de facturas... ", e);
            } finally {
                    EntityManagerUtil.close(entity);
            }
            return list;
	}

	public String update(List<Factura> list, int num) {
		EntityManager entity = null;
                try {
                    entity = EntityManagerUtil.getEntityManager();
                    entity.getTransaction().begin();
                    for (Factura f : list) {
                        f.setPlazo(num);
                        entity.merge(f);
                    }
                    entity.getTransaction().commit();
		} catch (Exception e) {
                    log.error("Problemas para actualizar modPlazosPago... ", e);
		} finally {
                    EntityManagerUtil.close(entity);
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
					.setParameter("plazo", plazo).setParameter("idFactura", factura.getId()).executeUpdate();
			log.info("Registros afectados: ({})", rows);
			entity.getTransaction().commit();
		} catch (Exception e) {
			log.error("Problemas para actualizar modPlazosPago... ", e);
		} finally {
			EntityManagerUtil.close(entity);
		}
		return result;
	}
}
