package mx.com.ferbo.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.ui.RepSalidas;
import mx.com.ferbo.util.EntityManagerUtil;

public class RepSalidasDAO extends IBaseDAO<RepSalidas, Integer> {
	private static Logger log = LogManager.getLogger(RepSalidasDAO.class);

	@Override
	public RepSalidas buscarPorId(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<RepSalidas> buscarTodos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<RepSalidas> buscarPorCriterios(RepSalidas e) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<RepSalidas> buscar(Date fechaIni, Date fechaFin, Integer idCliente, Integer idPlanta, Integer idCamara) {
		List<RepSalidas> resultList = null;
		EntityManager entity = null;
		String sql = null;
		
		try {
			sql = "SELECT "
					+ "	cs.fecha, "
					+ "	cdd.folio_cliente, "
					+ "	cs.numero, "
					+ "	dcs.producto, "
					+ "	dcs.unidad, "
					+ "	dcs.cantidad, "
					+ "	dcs.peso, "
					+ "	c.numero_cte, "
					+ "	c.cte_nombre, "
					+ "	cdd.fecha_ingreso, "
					+ "	p.partida_cve, "
					+ "	p.cantidad_total, "
					+ "	p.peso_total, "
					+ "	cam.camara_cve, "
					+ "	cam.camara_abrev AS camara, "
					+ "	plt.planta_cve, "
					+ "	plt.planta_abrev AS planta "
					+ "FROM CONSTANCIA_SALIDA cs "
					+ "INNER JOIN DETALLE_CONSTANCIA_SALIDA dcs ON dcs.constancia_cve = cs.id  "
					+ "INNER JOIN PARTIDA p ON p.PARTIDA_CVE = dcs.partida_cve "
					+ "INNER JOIN CONSTANCIA_DE_DEPOSITO cdd ON p.folio = cdd.folio "
					+ "INNER JOIN CLIENTE c ON c.CTE_CVE = cdd.cte_cve  "
					+ "INNER JOIN CAMARA cam ON cam.camara_cve = dcs.camara_cve "
					+ "INNER JOIN PLANTA plt ON plt.planta_cve = cam.planta_cve "
					+ "WHERE cs.status NOT IN (2) "
					+ "	AND cs.FECHA BETWEEN :fechaIni AND :fechaFin "
					+ "	AND (cdd.cte_cve = :idCliente OR :idCliente IS NULL) "
					+ "	AND (plt.planta_cve = :idPlanta OR :idPlanta IS NULL) "
					+ "	AND (cam.camara_cve = :idCamara OR :idCamara IS NULL) "
					+ "ORDER BY "
					+ "	c.cte_nombre ASC, "
					+ "	p.PARTIDA_CVE ASC, "
					+ "	cdd.folio_cliente ASC, "
					+ "	cs.fecha ASC ";
			entity = EntityManagerUtil.getEntityManager();
			
			List<Object[]> results = entity.createNativeQuery(sql)
					.setParameter("fechaIni", fechaIni)
					.setParameter("fechaFin", fechaFin)
					.setParameter("idCliente", idCliente)
					.setParameter("idPlanta", idPlanta)
					.setParameter("idCamara", idCamara)
					.getResultList()
					;
			
			
			resultList = new ArrayList<RepSalidas>();
			for(Object[] o : results) {
				RepSalidas r = new RepSalidas();
				int idx = 0;
				
				r.setFecha( (Date) o[idx++] );
				r.setFolioCliente((String) o[idx++]);
				r.setNumero((String) o[idx++]);
				r.setProducto((String) o[idx++]);
				r.setUnidad((String) o[idx++]);
				r.setCantidad((Integer)o[idx++]);
				r.setPeso((BigDecimal) o[idx++]);
				r.setNumeroCliente((String) o[idx++]);
				r.setNombreCliente((String) o[idx++]);
				r.setFechaIngreso((Date) o[idx++]);
				r.setIdPartida((Integer) o[idx++]);
				r.setCantidadTotal((Integer) o[idx++]);
				r.setPesoTotal((BigDecimal) o[idx++]);
				r.setIdCamara((Integer) o[idx++]);
				r.setCamara((String) o[idx++]);
				r.setIdPlanta((Integer) o[idx++]);
				r.setPlanta((String) o[idx++]);
				
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
	public String actualizar(RepSalidas e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String guardar(RepSalidas e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminar(RepSalidas e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminarListado(List<RepSalidas> listado) {
		// TODO Auto-generated method stub
		return null;
	}

}
