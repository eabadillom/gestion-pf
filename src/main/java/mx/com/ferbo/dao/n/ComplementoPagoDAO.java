package mx.com.ferbo.dao.n;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.ComplementoPago;
import mx.com.ferbo.util.DAOException;

@Named
@ApplicationScoped
public class ComplementoPagoDAO extends BaseDAO<ComplementoPago, Integer> {
	private static Logger log = LogManager.getLogger(ComplementoPagoDAO.class);

	public ComplementoPagoDAO() {
		super(ComplementoPago.class);
	}
	
	public Optional<ComplementoPago> cargar(Integer id) {
		Optional<ComplementoPago> optional;
		ComplementoPago model;
		EntityManager em = null;
		String query;
		
		try {
			query = "SELECT cp FROM ComplementoPago cp "
					+ "INNER JOIN FETCH cp.listPagos p "
					+ "INNER JOIN FETCH p.factura f "
					+ "INNER JOIN FETCH cp.emisor e "
					+ "INNER JOIN FETCH cp.receptor r "
					+ "WHERE cp.id = :id";
			
			em = this.getEntityManager();
			model = em.createQuery(query, this.modelClass)
					.setParameter("id", id)
					.getSingleResult();
			
			optional = Optional.of(model);
		} catch(Exception ex) {
			log.error("Problema para cargar la información del complemento de pago...", ex);
			optional = Optional.empty();
		} finally {
			this.close(em);
		}
		
		return optional;
	}

	public ComplementoPago buscarPorFolioSerie(String numero, String serie) throws DAOException {
		ComplementoPago complementoPago = null;
		EntityManager em = null;
		try {
			em = super.getEntityManager();
			complementoPago = em.createNamedQuery("ComplementoPago.findByFolioSerie", ComplementoPago.class)
					.setParameter("numero", numero).setParameter("serie", serie).getSingleResult();
		} catch (Exception ex) {
			log.error("Problema al obtener la lista de complementos de pago...", ex);
			throw new DAOException("Problema al obtener los complementos de pagos");
		} finally {
			super.close(em);
		}

		return complementoPago;
	}

	public List<ComplementoPago> buscarPorPeriodoRegistro(Date inicio, Date fin) throws DAOException {
		List<ComplementoPago> listComplementoPago = null;
		EntityManager em = null;
		try {
			em = super.getEntityManager();
			listComplementoPago = em.createNamedQuery("ComplementoPago.findByRegistro", ComplementoPago.class)
					.setParameter("inicio", inicio).setParameter("fin", fin).getResultList();
		} catch (Exception ex) {
			log.error("Problema al obtener la lista de complementos de pago...", ex);
			throw new DAOException("Problema al obtener los complementos de pagos");
		} finally {
			super.close(em);
		}
		return listComplementoPago;
	}

	public List<ComplementoPago> buscarPorPeriodoTimbrado(Date inicio, Date fin) throws DAOException {
		List<ComplementoPago> listComplementoPago = null;
		EntityManager em = null;
		try {
			em = super.getEntityManager();
			listComplementoPago = em.createNamedQuery("ComplementoPago.findByTimbrado", ComplementoPago.class)
					.setParameter("inicio", inicio).setParameter("fin", fin).getResultList();
		} catch (Exception ex) {
			log.error("Problema al obtener la lista de complementos de pago...", ex);
			throw new DAOException("Problema al obtener los complementos de pagos");
		} finally {
			super.close(em);
		}
		return listComplementoPago;
	}

}
