package mx.com.ferbo.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.ConstanciaSalida;
import mx.com.ferbo.model.ConstanciaSalidaServicios;
import mx.com.ferbo.model.DetalleConstanciaSalida;
import mx.com.ferbo.util.EntityManagerUtil;

public class ConstanciaSalidaDAO extends IBaseDAO<ConstanciaSalida, Integer> {

	private static Logger log = LogManager.getLogger(ConstanciaSalidaDAO.class);

	public EntityManager em = null;

	@Override
	public ConstanciaSalida buscarPorId(Integer id) {
		return null;
	}

	public ConstanciaSalida buscarPorId(Integer id, boolean isFullInfo) {
		ConstanciaSalida constancia = null;
		EntityManager em = null;

		try {
			em = EntityManagerUtil.getEntityManager();
			constancia = em.find(ConstanciaSalida.class, id);

			if (isFullInfo == false)
				return constancia;

			List<DetalleConstanciaSalida> dcsList = constancia.getDetalleConstanciaSalidaList();
			for (DetalleConstanciaSalida dcs : dcsList) {
				log.debug("DCS: {}", dcs.getId());
				log.debug("PartidaCve: {}", dcs.getPartidaCve().getPartidaCve());
				log.debug("Partida cajas: {}", dcs.getPartidaCve().getCantidadTotal());
			}

			List<ConstanciaSalidaServicios> cssrvList = constancia.getConstanciaSalidaServiciosList();
			for (ConstanciaSalidaServicios cssrv : cssrvList) {
				log.debug("CSSrv: {}", cssrv);
			}

		} catch (Exception ex) {
			log.error("Problema para obtener la constancia de salida...", ex);
		} finally {
			EntityManagerUtil.close(em);
		}
		return constancia;
	}

	@Override
	public List<ConstanciaSalida> buscarTodos() {
		List<ConstanciaSalida> lista = null;
		EntityManager em = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			lista = em.createNamedQuery("ConstanciaSalida.findAll", ConstanciaSalida.class).getResultList();
		} catch (Exception ex) {
			log.error("Problema para obtener el listado de constancias de salida...", ex);
		} finally {
			EntityManagerUtil.close(em);
		}

		return lista;
	}

	@Override
	public List<ConstanciaSalida> buscarPorCriterios(ConstanciaSalida e) {
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<ConstanciaSalida> buscarPorCriterios(String folioCliente, Date fechaInico, Date fechaFin,
			int idCliente) {
		Cliente cliente = new Cliente();
		Map<String, Object> paramaterMap = new HashMap<String, Object>();
		List<String> whereCause = new ArrayList<String>();
		StringBuilder queryBuilder = new StringBuilder();

		try {

			Query q = null;
			queryBuilder.append("SELECT c FROM ConstanciaSalida c");

			if (fechaInico != null && fechaFin != null) {
				whereCause.add("(c.fecha BETWEEN :fechaInicio AND :fechaFinal)");
				paramaterMap.put("fechaInicio", fechaInico);
				paramaterMap.put("fechaFinal", fechaFin);
			}
			if (folioCliente != null && !"".equalsIgnoreCase(folioCliente.trim())) {
				whereCause.add("c.numero = :folioCliente");
				paramaterMap.put("folioCliente", folioCliente);
			}
			if (idCliente != 0) {
				cliente.setCteCve(idCliente);
				whereCause.add("c.clienteCve = :idCliente");
				paramaterMap.put("idCliente", cliente);
			}

			queryBuilder.append(" WHERE " + StringUtils.join(whereCause, " AND "));

			q = em.createQuery(queryBuilder.toString());

			for (String key : paramaterMap.keySet()) {
				q.setParameter(key, paramaterMap.get(key));
			}

			List<ConstanciaSalida> listado = (List<ConstanciaSalida>) q.getResultList();

			for (ConstanciaSalida cs : listado) {
				List<DetalleConstanciaSalida> listaDetalle = cs.getDetalleConstanciaSalidaList();
				listaDetalle.size();
				for (DetalleConstanciaSalida dcs : listaDetalle) {
					log.debug(dcs.getPartidaCve());
				}
			}

			return listado;
		} catch (Exception e) {
			System.out.println("ERROR" + e.getMessage());
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<ConstanciaSalida> buscar(Date fechaInicio, Date fechaFin, Integer idCliente, String folioCliente) {
		List<ConstanciaSalida> resultList = null;
		EntityManager em = null;
		Query sql = null;

		try {
			if (folioCliente != null && folioCliente.contains("%") == false)
				folioCliente = "%".concat(folioCliente).concat("%");

			em = EntityManagerUtil.getEntityManager();
			sql = em.createNativeQuery("SELECT * FROM (\n" + "	SELECT * FROM (\n" + "		SELECT *\n"
					+ "		FROM constancia_salida cs\n"
					+ "		where (:idCliente IS NULL OR cs.CLIENTE_CVE = :idCliente)\n"
					+ "	) cs2 WHERE ((cs2.FECHA BETWEEN :fechaInicio AND :fechaFin) OR (:fechaInicio IS NULL OR :fechaFin IS NULL)) \n"
					+ ") cs3 WHERE (:folioCliente IS NULL OR cs3.NUMERO like :folioCliente ) ORDER BY cs3.FECHA",
					ConstanciaSalida.class).setParameter("fechaInicio", fechaInicio).setParameter("fechaFin", fechaFin)
					.setParameter("idCliente", idCliente).setParameter("folioCliente", folioCliente);
			resultList = sql.getResultList();

		} catch (Exception ex) {
			log.error("Problema para consultar las constancias de salida...", ex);
		} finally {
			EntityManagerUtil.close(em);
		}

		return resultList;
	}

	@Override
	public String actualizar(ConstanciaSalida constanciaSalida) {
		EntityManager em = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.merge(constanciaSalida);
			em.getTransaction().commit();
		} catch (Exception e) {
			log.error("Problema para guardar la constancia de salida...", e);
			return "ERROR";
		} finally {
			EntityManagerUtil.close(em);
		}

		return null;
	}

	public String actualizarStatus(ConstanciaSalida constanciaSalida) {
		EntityManager em = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			Query actualizar = em.createNativeQuery(
					" UPDATE constancia_salida SET STATUS = :status, OBSERVACIONES = :observaciones WHERE ID = :id ");
			actualizar.setParameter("status", (constanciaSalida.getStatus() == null) ? 1 : 2);// si constancia de salida
																								// status es null
																								// colocar 1 en otro
																								// caso colocar 2
			actualizar.setParameter("id", constanciaSalida.getId());
			actualizar.setParameter("observaciones", constanciaSalida.getObservaciones());
			actualizar.executeUpdate();
			em.getTransaction().commit();
		} catch (Exception e) {
			log.error("Problema para actualizar el status de la constancia de salida...", e);
			return "ERROR";
		} finally {
			EntityManagerUtil.close(em);
		}

		return null;
	}

	@Override
	public String guardar(ConstanciaSalida constanciaSalida) {
		EntityManager em = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.persist(constanciaSalida);// guarda el servicio dado (NO es gestionado)
			em.getTransaction().commit();
		} catch (Exception e) {
			log.error("Problema para guardar la constancia de salida...", e);
			return "ERROR";
		} finally {
			EntityManagerUtil.close(em);
		}

		return null;
	}

	public String buscarPorNumero(String numFolio) {
		EntityManager em = null;
		ConstanciaSalida constanciaSalida;
		String folio = "";

		try {

			em = EntityManagerUtil.getEntityManager();
			constanciaSalida = em.createNamedQuery("ConstanciaSalida.findByNumero", ConstanciaSalida.class)
					.setParameter("numero", numFolio).getSingleResult();
			if (constanciaSalida != null) {
				folio = constanciaSalida.getNumero();
			}

		} catch (Exception e) {
			log.error("Error al buscar por numero constancia salida", e.getMessage());
		} finally {
			EntityManagerUtil.close(em);
		}

		return folio;
	}

	public EntityManager getEm() {
		return em;
	}

	public void setEm(EntityManager em) {
		this.em = em;
	}

	@Override
	public String eliminar(ConstanciaSalida e) {
		return null;
	}

	@Override
	public String eliminarListado(List<ConstanciaSalida> listado) {
		return null;
	}

}
