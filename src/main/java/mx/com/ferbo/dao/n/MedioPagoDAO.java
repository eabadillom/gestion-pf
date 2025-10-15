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

@Named
@ApplicationScoped
public class MedioPagoDAO extends BaseDAO<MedioPago, Integer> {

	private static Logger log = LogManager.getLogger(MedioPagoDAO.class);

	public MedioPagoDAO() {
		super(MedioPago.class);
	}

	public List<MedioPago> buscarVigentes(Date fecha) {
		List<MedioPago> list = new ArrayList<>();
		EntityManager em = null;

		try {
			em = super.getEntityManager();
			list = em.createNamedQuery("MedioPago.findVigentes", MedioPago.class)
					.setParameter("fecha", fecha).getResultList();
		} finally {
			super.close(em);
		}

		return list;
	}
}
