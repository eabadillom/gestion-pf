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
import mx.com.ferbo.util.DAOException;

@Named
@ApplicationScoped
public class MedioPagoDAO extends BaseDAO<MedioPago, Integer> {

	private static Logger log = LogManager.getLogger(MedioPagoDAO.class);

	public MedioPagoDAO() {
		super(MedioPago.class);
	}

	public List<MedioPago> buscarVigentes(Date fecha) throws DAOException {
		List<MedioPago> list = new ArrayList<>();
		EntityManager em = null;

		try {
			em = super.getEntityManager();
			list = em.createNamedQuery("MedioPago.findVigentes", MedioPago.class)
					.setParameter("fecha", fecha).getResultList();
		} catch (Exception ex) {
			log.error("Error al obtener los medios de pago vigentes hasta la fecha: " + fecha, ex);
			throw new DAOException("Ocurrio un problema al obtener los medios de pago vigentes hasta la fecha: " + fecha,
					ex);
		} finally {
			super.close(em);
		}

		return list;
	}
}
