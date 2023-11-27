package mx.com.ferbo.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.ui.RepServicios;
import mx.com.ferbo.util.EntityManagerUtil;

public class RepServiciosDAO extends IBaseDAO<RepServicios, Integer> {
	private static Logger log = LogManager.getLogger(RepServiciosDAO.class);

	@Override
	public RepServicios buscarPorId(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<RepServicios> buscarTodos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<RepServicios> buscarPorCriterios(RepServicios e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String actualizar(RepServicios e) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<RepServicios> buscar(Date fechaIni, Date fechaFin, Integer idCliente) {
		List<RepServicios> resultList = null;
		EntityManager entity = null;
		String sql = null;
		
		try {
			sql = "SELECT "
					+ "	cs.folio, "
					+ "	cs.folio_cliente, "
					+ "	cs.fecha, "
					+ "	cs.observaciones, "
					+ "	c.numero_cte, "
					+ "	c.cte_nombre, "
					+ "	prd.producto_ds, "
					+ "	ps.cantidad_de_cobro, "
					+ "	ps.cantidad_total, "
					+ "	COALESCE(udm.unidad_de_manejo_ds, 'Piezas') unidadManejo, "
					+ "	upper(s.servicio_ds) Servicio, "
					+ "	tc.nombre as tipo_cobro, "
					+ "	csd.servicio_cantidad "
					+ "FROM constancia_de_servicio cs "
					+ "INNER JOIN cliente c ON c.cte_cve = cs.cte_cve "
					+ "INNER JOIN partida_servicio ps ON ps.folio = cs.folio "
					+ "LEFT JOIN producto prd ON prd.producto_cve = ps.producto_cve "
					+ "LEFT JOIN unidad_de_manejo udm ON udm.unidad_de_manejo_cve = ps.unidad_de_manejo_cve "
					+ "INNER JOIN constancia_servicio_detalle csd ON csd.folio = cs.folio "
					+ "INNER JOIN servicio s ON s.servicio_cve = csd.servicio_cve "
					+ "INNER JOIN tipo_cobro tc ON tc.id = s.cobro "
					+ "WHERE "
					+ "	(cs.fecha BETWEEN :fechaIni AND :fechaFin) "
					+ "	AND (c.cte_cve = :idCliente OR :idCliente IS NULL) "
					+ "	AND (cs.status <> 4) "
					+ "ORDER BY "
					+ "	c.cte_nombre ASC, "
					+ "	cs.folio_cliente ASC "
				;
			entity = EntityManagerUtil.getEntityManager();

			List<Object[]> results = entity.createNativeQuery(sql)
					.setParameter("fechaIni", fechaIni)
					.setParameter("fechaFin", fechaFin)
					.setParameter("idCliente", idCliente)
					.getResultList()
					;
			
			resultList = new ArrayList<RepServicios>();
			for(Object[] o : results) {
				RepServicios r = new RepServicios();
				int idx = 0;
				
				r.setFolio((Integer) o[idx++]);
				r.setFolioCliente((String) o[idx++]);
				r.setFecha((Date) o[idx++]);
				r.setObservaciones((String) o[idx++]);
				r.setNumeroCliente((String) o[idx++]);
				r.setNombreCliente((String) o[idx++]);
				r.setProductoDescripcion((String) o[idx++]);
				r.setCantidadCobro((BigDecimal) o[idx++]);
				r.setCantidadTotal((Integer) o[idx++]);
				r.setUnidadManejo((String) o[idx++]);
				r.setServicio((String) o[idx++]);
				r.setTipoCobro((String) o[idx++]);
				r.setServicioCantidad((BigDecimal) o[idx++]);
				resultList.add(r);
			}
			
		} catch(Exception ex) {
			log.error("Problema para obtener el reporte de Entradas...", ex);
		} finally {
			EntityManagerUtil.close(entity);
		}
		
		return resultList;
	}

	@Override
	public String guardar(RepServicios e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminar(RepServicios e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminarListado(List<RepServicios> listado) {
		// TODO Auto-generated method stub
		return null;
	}

}
