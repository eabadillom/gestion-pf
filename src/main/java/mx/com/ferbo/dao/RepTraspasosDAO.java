package mx.com.ferbo.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.ui.RepTraspasos;
import mx.com.ferbo.util.EntityManagerUtil;

public class RepTraspasosDAO extends IBaseDAO<RepTraspasos, Integer> {

	private static Logger log = LogManager.getLogger(RepTraspasosDAO.class);

	@Override
	public RepTraspasos buscarPorId(Integer id) {
		return null;
	}

	@Override
	public List<RepTraspasos> buscarTodos() {
		return null;
	}

	@Override
	public List<RepTraspasos> buscarPorCriterios(RepTraspasos e) {
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<RepTraspasos> buscar(Date fechaIni, Date fechaFin, Integer idCliente) {
		List<RepTraspasos> resultList = null;
		EntityManager entity = null;
		String sql = null;

		try {
			sql = "SELECT " + "	c.numero_cte, " + "	c.cte_nombre, " + "	ct.numero, " + "	ct.fecha, "
					+ "	ct.observacion, " + "	tp.constancia, " + "	tp.origen, " + "	tp.destino, "
					+ "	tp.cantidad, " + "	tp.descripcion, " + "	p.folio, " + "	prd.producto_ds, "
					+ "	um.unidad_de_manejo_ds " + "FROM constancia_traspaso ct "
					+ "INNER JOIN traspaso_partida tp ON ct.id = tp.traspaso "
					+ "INNER JOIN partida p ON tp.partida = p.PARTIDA_CVE "
					+ "INNER JOIN unidad_de_producto up ON p.UNIDAD_DE_PRODUCTO_CVE = up.UNIDAD_DE_PRODUCTO_CVE "
					+ "INNER JOIN unidad_de_manejo um ON up.UNIDAD_DE_MANEJO_CVE = um.UNIDAD_DE_MANEJO_CVE "
					+ "INNER JOIN producto prd ON up.PRODUCTO_CVE = prd.PRODUCTO_CVE "
					+ "INNER JOIN cliente c ON ct.cliente = c.cte_cve "
					+ "WHERE (ct.fecha BETWEEN :fechaIni AND :fechaFin) "
					+ "AND (ct.cliente = :idCliente OR :idCliente IS NULL) " + "ORDER BY " + "	c.cte_nombre ASC, "
					+ "	ct.fecha ASC, " + "	ct.numero ASC, " + "	p.folio ASC ";
			entity = EntityManagerUtil.getEntityManager();

			List<Object[]> results = entity.createNativeQuery(sql).setParameter("fechaIni", fechaIni)
					.setParameter("fechaFin", fechaFin).setParameter("idCliente", idCliente).getResultList();

			resultList = new ArrayList<RepTraspasos>();
			for (Object[] o : results) {
				RepTraspasos r = new RepTraspasos();
				int idx = 0;

				r.setNumeroCliente((String) o[idx++]);
				r.setNombreCliente((String) o[idx++]);
				r.setNumero((String) o[idx++]);
				r.setFecha((Date) o[idx++]);
				r.setObservacion((String) o[idx++]);
				r.setConstancia((String) o[idx++]);
				r.setOrigen((String) o[idx++]);
				r.setDestino((String) o[idx++]);
				r.setCantidad((BigDecimal) o[idx++]);
				r.setDescripcion((String) o[idx++]);
				r.setFolio((Integer) o[idx++]);
				r.setProductoDescripcion((String) o[idx++]);
				r.setUnidadDeManejo((String) o[idx++]);

				resultList.add(r);
			}

		} catch (Exception ex) {
			log.error("Problema para obtener el reporte de Entradas...", ex);
		} finally {
			EntityManagerUtil.close(entity);
		}

		return resultList;
	}

	@Override
	public String actualizar(RepTraspasos e) {
		return null;
	}

	@Override
	public String guardar(RepTraspasos e) {
		return null;
	}

	@Override
	public String eliminar(RepTraspasos e) {
		return null;
	}

	@Override
	public String eliminarListado(List<RepTraspasos> listado) {
		// TODO Auto-generated method stub
		return null;
	}

}
