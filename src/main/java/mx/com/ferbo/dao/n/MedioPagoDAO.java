package mx.com.ferbo.dao.n;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.MedioPago;
import mx.com.ferbo.util.InventarioException;

@Named
@ApplicationScoped
public class MedioPagoDAO extends BaseDAO<MedioPago, Integer> {

    private static Logger log = LogManager.getLogger(MedioPagoDAO.class);

    public MedioPagoDAO(){
        super(MedioPago.class);
    }

    public List<MedioPago> buscarVigentes(Date fecha) throws InventarioException {
		List<MedioPago> list = new ArrayList<>();
		EntityManager em = null;
		
		try {
			em = super.getEntityManager();
			list = em.createNamedQuery("MedioPago.findVigentes", MedioPago.class)
					.setParameter("fecha", fecha).getResultList();
		} catch(Exception ex) {
			log.error("Problema para leer el catálogo de medios de pago...", ex);
            throw new InventarioException("Problema para obtener los medios de pago.");
		} finally {
			super.close(em);
		}
		
		return list;
	}
}
