package mx.com.ferbo.pagos.dao;

import java.util.Date;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ferbo.tools.exception.SystemException;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.MedioPago;

@Named
@ApplicationScoped
public class FormaPagoDAO extends BaseDAO<MedioPago, Integer> {

	private static Logger log = LogManager.getLogger(FormaPagoDAO.class);

	private EntityManager em;

	public FormaPagoDAO() {
		super(MedioPago.class);
	}

	public List<MedioPago> buscarVigentes(Date fecha) throws SystemException {
		try {
			em = super.getEntityManager();
			return em.createNamedQuery("MedioPago.findVigentes", MedioPago.class)
					.setParameter("fecha", fecha).getResultList();
		} catch (Exception ex) {
			log.error("Error al obtener los medios de pago vigentes hasta la fecha: " + fecha, ex);
			throw new SystemException(
					"Ocurrio un problema al obtener los medios de pago vigentes hasta la fecha: " + fecha);
		} finally {
			close(em);
		}
	}
}
