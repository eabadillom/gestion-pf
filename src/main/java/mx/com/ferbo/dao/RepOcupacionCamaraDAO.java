package mx.com.ferbo.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.ui.OcupacionCamara;
import mx.com.ferbo.util.EntityManagerUtil;

public class RepOcupacionCamaraDAO {
	
	private static Logger log = LogManager.getLogger(RepOcupacionCamaraDAO.class);
	
	@SuppressWarnings("unchecked")
	public List<OcupacionCamara> ocupacionCamara(Date fecha, Integer idCliente, Integer idPlanta){
		
		EntityManager em = null;		
		List<OcupacionCamara> listaOcupacionCamara = null;
		String sql = null;
		
		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			
			
			listaOcupacionCamara = new ArrayList<OcupacionCamara>();
			
			sql = "WITH i AS (\n" +
                                "    SELECT\n" +
                                "        c.CTE_NOMBRE AS cliente,\n" +
                                "        plt.PLANTA_DS AS planta,\n" +
                                "        cdd.FOLIO,\n" +
                                "        p.PARTIDA_CVE,\n" +
                                "        p.cantidad_total,\n" +
                                "        (p.CANTIDAD_TOTAL - COALESCE(s.cantidad, 0)) AS cantidad,\n" +
                                "        p.peso_total,\n" +
                                "        (p.PESO_TOTAL - COALESCE(s.peso, 0)) AS peso,\n" +
                                "        p.no_tarimas,\n" +
                                "        p.cd_tarima\n" +
                                "    FROM constancia_de_deposito cdd\n" +
                                "    INNER JOIN cliente c ON cdd.CTE_CVE = c.CTE_CVE\n" +
                                "    INNER JOIN partida p ON cdd.folio = p.folio\n" +
                                "    INNER JOIN camara cam ON p.CAMARA_CVE = cam.CAMARA_CVE\n" +
                                "    INNER JOIN planta plt ON cam.PLANTA_CVE = plt.PLANTA_CVE\n" +
                                "    LEFT JOIN (\n" +
                                "        SELECT\n" +
                                "            dcs.PARTIDA_CVE,\n" +
                                "            SUM(dcs.CANTIDAD) AS cantidad,\n" +
                                "            SUM(dcs.PESO) AS peso\n" +
                                "        FROM detalle_constancia_salida dcs\n" +
                                "        INNER JOIN constancia_salida cs ON cs.ID = dcs.CONSTANCIA_CVE\n" +
                                "        WHERE cs.STATUS = 1 AND cs.FECHA <= :fecha\n" +
                                "        GROUP BY dcs.PARTIDA_CVE\n" +
                                "    ) s ON p.PARTIDA_CVE = s.PARTIDA_CVE\n" +
                                "    WHERE cdd.status = 1\n" +
                                "      AND cdd.FECHA_INGRESO <= :fecha\n" +
                                "      AND (:idPlanta IS NULL OR plt.PLANTA_CVE = :idPlanta)\n" +
                                "      AND (:idCliente IS NULL OR cdd.cte_cve = :idCliente)\n" +
                                "      AND (p.CANTIDAD_TOTAL - COALESCE(s.cantidad, 0)) > 0\n" +
                                "),\n" +
                                "t1 AS (\n" +
                                "    SELECT i.cliente, i.planta, CEILING((i.peso / i.peso_total) * i.no_tarimas) AS tarimas\n" +
                                "    FROM i\n" +
                                "    WHERE i.no_tarimas IS NOT NULL AND i.cd_tarima IS NULL\n" +
                                "),\n" +
                                "t2 AS (\n" +
                                "    SELECT i.cliente, i.planta, COUNT(DISTINCT i.cd_tarima) AS tarimas\n" +
                                "    FROM i\n" +
                                "    WHERE i.no_tarimas IS NULL AND i.cd_tarima IS NOT NULL\n" +
                                "    GROUP BY i.cliente, i.planta\n" +
                                ")\n" +
                                "SELECT o.cliente, o.planta, SUM(o.tarimas) AS tarimas\n" +
                                "FROM (\n" +
                                "    SELECT * FROM t1\n" +
                                "    UNION ALL\n" +
                                "    SELECT * FROM t2\n" +
                                ") o\n" +
                                "GROUP BY o.cliente, o.planta\n" +
                                "ORDER BY o.cliente, o.planta";
			
			Query query = em.createNativeQuery(sql)
					.setParameter("fecha", fecha)
					.setParameter("idCliente", idCliente)
					.setParameter("idPlanta", idPlanta);
			
			List<Object[]> listaObjetos = query.getResultList();
						
			
			for(Object[] o: listaObjetos) {
				
				OcupacionCamara oc = new OcupacionCamara();
				int id = 0;
				
				oc.setCte_nombre((String) o[id++]);
				oc.setPlanta_ds((String) o[id++]);
				oc.setTarima((BigDecimal)o[id++]);//setear al objeto total posiciones y posiciones disponibles
				
				listaOcupacionCamara.add(oc);
				
			}
			
			
		} catch (Exception e) {
			log.info("Error al obtener Ocupacion de Camaras " + e.getMessage());
		}finally {
			em.close();
		}
		
		
		return listaOcupacionCamara;			
	}
	

}
