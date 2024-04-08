package mx.com.ferbo.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.Factura;
import mx.com.ferbo.model.Pago;
import mx.com.ferbo.util.EntityManagerUtil;

public class PagoDAO extends IBaseDAO<Pago, Integer> {
	private static Logger log = LogManager.getLogger(PagoDAO.class);

	@SuppressWarnings("unchecked")
	public List<Pago> findall() {
		EntityManager entity = EntityManagerUtil.getEntityManager();
		List<Pago> p = null;
		Query sql = entity.createNamedQuery("Pago.findAll", Pago.class);
		p = sql.getResultList();
		return p;
	}

	@Override
	public Pago buscarPorId(Integer id) {
		EntityManager em = null;
		Pago pago = null;

		try {
			em = EntityManagerUtil.getEntityManager();
			pago = em.find(Pago.class, id);
		} catch (Exception ex) {
			log.error("Problema para consultar el pago...", ex);
		} finally {
			EntityManagerUtil.close(em);
		}

		return pago;
	}

	public Pago buscarPorId(Integer id, boolean isFullInfo) {
		EntityManager em = null;
		Pago pago = null;

		try {
			em = EntityManagerUtil.getEntityManager();
			pago = em.find(Pago.class, id);

			if (isFullInfo == false)
				return pago;

			log.debug("Factura: {}", pago.getFactura().getId());
			for (Pago p : pago.getFactura().getPagoList()) {
				log.debug("Pago: {}", p.getId());
			}

		} catch (Exception ex) {
			log.error("Problema para consultar el pago...", ex);
		} finally {
			EntityManagerUtil.close(em);
		}

		return pago;
	}

	public List<Pago> buscarPorFactura(Integer id) {
		List<Pago> lista = null;
		EntityManager em = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			lista = em.createNamedQuery("Pago.findByFacturaId", Pago.class).setParameter("facturaId", id)
					.getResultList();
			log.info("El listado de pagos de la factura se obtuvo correctamente.");

		} catch (Exception e) {
			log.error("Ocurrió un error al consultar el listado de pagos de la factura " + id, e);
		} finally {
			EntityManagerUtil.close(em);
		}
		return lista;

	}

	@Override
	public List<Pago> buscarTodos() {
		return null;
	}

	@Override
	public List<Pago> buscarPorCriterios(Pago e) {
		return null;
	}

	@Override
	public String actualizar(Pago pago) {
		EntityManager em = null;

		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
//			em.merge(pago);
			em.createNativeQuery(
					"UPDATE pago SET factura = :idFactura, tipo = :idTipo, monto= :monto, fecha = :fecha, banco = :idBanco, referencia = :referencia WHERE id = :idPago")
					.setParameter("idFactura", pago.getFactura().getId()).setParameter("idTipo", pago.getTipo().getId())
					.setParameter("monto", pago.getMonto()).setParameter("fecha", pago.getFecha())
					.setParameter("idBanco", pago.getBanco().getId()).setParameter("referencia", pago.getReferencia())
					.setParameter("idPago", pago.getId()).executeUpdate();
			em.getTransaction().commit();
			em.close();
		} catch (Exception ex) {
			log.error("Problema para actualizar el pago...", ex);
			return ex.getMessage();
		} finally {
			EntityManagerUtil.close(em);
		}
		return null;
	}

	@Override
	public String guardar(Pago e) {
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.persist(e);
			em.getTransaction().commit();
		} catch (Exception ex) {
			System.out.println("ERROR" + ex.getMessage());
			return "ERROR";
		}
		return null;

	}

	@Override
	public String eliminar(Pago e) {
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.createQuery("DELETE FROM Pago as p where p.id = :id").setParameter("id", e.getId()).executeUpdate();
			em.getTransaction().commit();
			em.close();
		} catch (Exception ex) {
			System.out.println("ERROR" + ex.getMessage());
			return "ERROR";
		}
		return null;

	}

	@Override
	public String eliminarListado(List<Pago> listado) {
		return null;
	}

	public List<Pago> buscaPorFactura(Factura f) {
		EntityManager em = EntityManagerUtil.getEntityManager();
		return em.createNamedQuery("Pago.findByFacturaId", Pago.class).setParameter("facturaId", f.getId())
				.getResultList();
	}

	public List<Pago> buscaPorClienteFechas(Cliente c, Date startDate, Date endDate) {
		EntityManager em = EntityManagerUtil.getEntityManager();
		return em.createNamedQuery("Pago.findByClienteFechas", Pago.class)
				.setParameter("cteCve", (c == null ? null : c.getCteCve())).setParameter("startDate", startDate)
				.setParameter("endDate", endDate).getResultList();
	}

}
