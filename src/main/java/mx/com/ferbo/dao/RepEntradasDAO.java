package mx.com.ferbo.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.ui.RepEntradas;
import mx.com.ferbo.util.EntityManagerUtil;

public class RepEntradasDAO extends IBaseDAO<RepEntradas, Integer>{
	private static Logger log = LogManager.getLogger(RepEntradasDAO.class);

	@Override
	public RepEntradas buscarPorId(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<RepEntradas> buscarTodos() {
		
		return null;
	}

	@Override
	public List<RepEntradas> buscarPorCriterios(RepEntradas e) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<RepEntradas> buscar(Date fechaIni, Date fechaFin, Integer idCliente, Integer idPlanta, Integer idCamara) {
		List<RepEntradas> resultList = null;
		EntityManager entity = null;
		String sql = null;
		
		try {
			sql = "SELECT "
					+ "	cdd.folio_cliente, "
					+ "	cdd.fecha_ingreso, "
					+ "	c.numero_cte, "
					+ "	c.cte_nombre, "
					+ "	p.PESO_TOTAL, "
					+ "	p.CANTIDAD_TOTAL, "
					+ "	p.valorMercancia, "
					+ "	dp.dtp_codigo, "
					+ "	dp.dtp_SAP, "
					+ "	prd.producto_ds, "
					+ "	dp.dtp_lote, "
					+ "	dp.dtp_po, "
					+ "	dp.dtp_caducidad, "
					+ "	dp.dtp_tarimas, "
					+ "	dp.cantidad_u_manejo, "
					+ "	udm.unidad_de_manejo_ds, "
					+ "	plt.planta_cve, "
					+ "	plt.planta_abrev AS planta, "
					+ "	cam.camara_cve, "
					+ "	cam.camara_abrev AS camara "
					+ "FROM partida p INNER JOIN constancia_de_deposito cdd ON cdd.folio = p.folio "
					+ "INNER JOIN cliente c ON c.cte_cve = cdd.cte_cve "
					+ "INNER JOIN detalle_partida dp ON dp.partida_cve = p.partida_cve AND dp.DET_PART_CVE = 1 "
					+ "INNER JOIN unidad_de_producto udp ON udp.unidad_de_producto_cve = p.unidad_de_producto_cve "
					+ "INNER JOIN unidad_de_manejo udm ON udm.unidad_de_manejo_cve = udp.UNIDAD_DE_MANEJO_CVE "
					+ "INNER JOIN producto prd ON prd.producto_cve = udp.producto_cve "
					+ "INNER JOIN camara cam ON cam.camara_cve = p.camara_cve "
					+ "INNER JOIN planta plt ON cam.planta_cve = plt.planta_cve "
					+ "WHERE cdd.fecha_ingreso BETWEEN :fechaIni AND :fechaFin "
					+ "	AND (cdd.cte_cve = :idCliente OR :idCliente IS NULL) "
					+ "	AND (cam.camara_cve = :idCamara OR :idCamara IS NULL) "
					+ "	AND (plt.planta_cve = :idPlanta OR :idPlanta IS NULL) "
					+ "ORDER BY "
					+ "	c.cte_nombre DESC, "
					+ "	c.numero_cte DESC, "
					+ "	prd.producto_ds DESC, "
					+ "	cdd.fecha_ingreso DESC ";
			entity = EntityManagerUtil.getEntityManager();
			
			List<Object[]> results = entity.createNativeQuery(sql)
					.setParameter("fechaIni", fechaIni)
					.setParameter("fechaFin", fechaFin)
					.setParameter("idCliente", idCliente)
					.setParameter("idPlanta", idPlanta)
					.setParameter("idCamara", idCamara)
					.getResultList()
					;
			
			resultList = new ArrayList<RepEntradas>();
			for(Object[] o : results) {
				RepEntradas r = new RepEntradas();
				int idx = 0;
				r.setFolioCliente((String) o[idx++]);
				r.setFechaIngreso((Date) o[idx++]);
				r.setNumeroCliente((String) o[idx++]);
				r.setNombreCliente((String) o[idx++]);
				r.setPesoTotal((BigDecimal) o[idx++]);
				r.setCantidadTotal((Integer) o[idx++]);
				r.setValorMercancia((BigDecimal) o[idx++]);
				r.setDtpCodigo((String) o[idx++]);
				r.setDtpSap((String) o[idx++]);
				r.setProductoDescripcion((String) o[idx++]);
				r.setDtpLote((String) o[idx++]);
				r.setDtpPo((String) o[idx++]);
				r.setDtpCaducidad((Date) o[idx++]);
				r.setDtpTarimas((String) o[idx++]);
				r.setCantidadUnidadManejo((Integer) o[idx++]);
				r.setUnidadManejoDescripcion((String) o[idx++]);
				r.setIdPlanta((Integer) o[idx++]);
				r.setNombrePlanta((String) o[idx++]);
				r.setIdCamara((Integer) o[idx++]);
				r.setNombreCamara((String) o[idx++]);
				
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
	public String actualizar(RepEntradas e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String guardar(RepEntradas e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminar(RepEntradas e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminarListado(List<RepEntradas> listado) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
