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
	public List<OcupacionCamara> ocupacionCamara(Date fecha, Integer idCliente, Integer idPlanta, Integer idCamara){
		
		EntityManager em = null;		
		List<OcupacionCamara> listaOcupacionCamara = null;
		String sql = null;
		
		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			
			
			listaOcupacionCamara = new ArrayList<OcupacionCamara>();
			
			sql = "SELECT " + 
					"gen.camaraCve, " + 
					"gen.camaraAbrev, " + 
					"gen.camaraDs, " + 
					"gen.plantaDs, " + 
					"cli.totalPos as posicionesPermitidasCamara, " + 
					"cli.tarima as posicionesOcupadasCliente, " + 
					"gen.tarima as posicionesGeneralesOcupadas, " + 
					"(cli.totalPos - gen.tarima) as posicionesDisponibles " + 
					"FROM " + 
					"( " + 
					"select " + 
					"I.camara_cve AS camaraCve, " + 
					"I.camara_abrev AS camaraAbrev, " + 
					"I.camara_ds AS camaraDs, " + 
					"I.planta_ds AS plantaDs, " + 
					"SUM(I.tarima) AS tarima, " + 
					"I.total_pos AS totalPos " + 
					"from " + 
					"( " + 
					"select " + 
					"cam.CAMARA_CVE AS camara_cve, " + 
					"cam.CAMARA_ABREV AS camara_abrev, " + 
					"cam.CAMARA_DS AS camara_ds, " + 
					"plt.PLANTA_DS AS planta_ds, " + 
					"(parEnt.cantidad_total - COALESCE(salidas.cantidad, 0)) as cantidad, " + 
					"CEILING((parEnt.cantidad_total - COALESCE(salidas.cantidad, 0)) * parEnt.no_tarimas / parEnt.CANTIDAD_TOTAL ) as tarima, " + 
					"cam.total_posiciones AS total_pos " + 
					"from " + 
					"partida parEnt " + 
					"inner join ( " + 
					"select " + 
					"* " + 
					"from " + 
					"constancia_de_deposito cdd " + 
					"WHERE " + 
					"(cdd.cte_cve = :idCliente " + 
					"OR :idCliente IS NULL) " + 
					"AND cdd.fecha_ingreso <= :Fecha " + 
					") cddEnt on " + 
					"parEnt.folio = cddEnt.folio " + 
					"inner join unidad_de_producto udp on " + 
					"udp.unidad_de_producto_cve = parEnt.unidad_de_producto_cve " + 
					"inner join producto prd on " + 
					"prd.producto_cve = udp.producto_cve " + 
					"inner join unidad_de_manejo udm on " + 
					"udm.unidad_de_manejo_cve = udp.unidad_de_manejo_cve " + 
					"inner join camara cam on " + 
					"cam.camara_cve = parEnt.camara_cve " + 
					"inner join planta plt on " + 
					"plt.planta_cve = cam.planta_cve " + 
					"inner join cliente cli on " + 
					"cli.cte_cve = cddEnt.cte_cve " + 
					"inner join " + 
					"( " + 
					"select " + 
					"tdp.* " + 
					"from " + 
					"detalle_partida tdp " + 
					"inner join ( " + 
					"select " + 
					"dp.partida_cve, " + 
					"max(dp.det_part_cve) as det_part_cve " + 
					"from " + 
					"detalle_partida dp " + 
					"group by " + 
					"dp.partida_cve " + 
					") tmdp on " + 
					"tdp.partida_cve = tmdp.partida_cve " + 
					"and tdp.det_part_cve = tmdp.det_part_cve " + 
					") detPart on " + 
					"detPart.partida_cve = parEnt.partida_cve " + 
					"left outer join posicion_partida pp on " + 
					"parEnt.partida_cve = pp.ID_PARTIDA " + 
					"left outer join posicion pos on " + 
					"pp.ID_POSICION = pos.id_posicion " + 
					"left outer join ( " + 
					"select " + 
					"dcs.partida_cve, " + 
					"sum(COALESCE(dcs.peso, 0)) as peso, " + 
					"sum(COALESCE(dcs.cantidad, 0)) as cantidad " + 
					"from " + 
					"constancia_salida cSal " + 
					"inner join detalle_constancia_salida dcs on " + 
					"dcs.constancia_cve = cSal.id " + 
					"WHERE " + 
					"cSal.status = 1 " + 
					"AND cSal.fecha <= :Fecha " + 
					"group by " + 
					"dcs.PARTIDA_CVE " + 
					") salidas ON " + 
					"parEnt.partida_cve = salidas.partida_cve " + 
					"where " + 
					"cddEnt.status <> 4 " + 
					"AND parEnt.no_tarimas >= 1.00 " + 
					"AND (cam.camara_cve = :idCamara " + 
					"OR :idCamara IS NULL ) " + 
					"AND (plt.planta_cve = :idPlanta " + 
					"OR :idPlanta IS NULL ) " + 
					"union all " + 					
					"select " + 
					"cam.CAMARA_CVE AS camara_cve, " + 
					"cam.CAMARA_ABREV AS camara_abrev, " + 
					"cam.CAMARA_DS AS camara_ds, " + 
					"plt.PLANTA_DS AS planta_ds, " + 
					"(parEnt.cantidad_total - COALESCE(salidas.cantidad, 0)) as cantidad, " + 
					"parEnt.no_tarimas, " + 					
					"cam.total_posiciones AS total_pos " + 
					"from " + 
					"partida parEnt " + 
					"inner join ( " + 
					"select " + 
					"* " + 
					"from " + 
					"constancia_de_deposito cdd " + 
					"WHERE " + 
					"(cdd.cte_cve = :idCliente " + 
					"OR :idCliente IS NULL) " + 
					"AND cdd.fecha_ingreso <= :Fecha " + 
					") cddEnt on " + 
					"parEnt.folio = cddEnt.folio " + 
					"inner join unidad_de_producto udp on " + 
					"udp.unidad_de_producto_cve = parEnt.unidad_de_producto_cve " + 
					"inner join producto prd on " + 
					"prd.producto_cve = udp.producto_cve " + 
					"inner join unidad_de_manejo udm on " + 
					"udm.unidad_de_manejo_cve = udp.unidad_de_manejo_cve " + 
					"inner join camara cam on " + 
					"cam.camara_cve = parEnt.camara_cve " + 
					"inner join planta plt on " + 
					"plt.planta_cve = cam.planta_cve " + 
					"inner join cliente cli on " + 
					"cli.cte_cve = cddEnt.cte_cve " + 
					"inner join " + 
					"( " + 
					"select " + 
					"tdp.* " + 
					"from " + 
					"detalle_partida tdp " + 
					"inner join ( " + 
					"select " + 
					"dp.partida_cve, " + 
					"max(dp.det_part_cve) as det_part_cve " + 
					"from " + 
					"detalle_partida dp " + 
					"group by " + 
					"dp.partida_cve " + 
					") tmdp on " + 
					"tdp.partida_cve = tmdp.partida_cve " + 
					"and tdp.det_part_cve = tmdp.det_part_cve " + 
					") detPart on " + 
					"detPart.partida_cve = parEnt.partida_cve " + 
					"left outer join posicion_partida pp on " + 
					"parEnt.partida_cve = pp.ID_PARTIDA " + 
					"left outer join posicion pos on " + 
					"pp.ID_POSICION = pos.id_posicion " + 
					"left outer join ( " + 
					"select " + 
					"dcs.partida_cve, " + 
					"sum(COALESCE(dcs.peso, 0)) as peso, " + 
					"sum(COALESCE(dcs.cantidad, 0)) as cantidad " + 
					"from " + 
					"constancia_salida cSal " + 
					"inner join detalle_constancia_salida dcs on " + 
					"dcs.constancia_cve = cSal.id " + 
					"WHERE " + 
					"cSal.status = 1 " + 
					"AND cSal.fecha <= :Fecha " + 
					"group by " + 
					"dcs.PARTIDA_CVE " + 
					") salidas ON " + 
					"parEnt.partida_cve = salidas.partida_cve " + 
					"where " + 
					"cddEnt.status <> 4 " + 
					"AND parEnt.no_tarimas < 1.00 " + 
					"AND (cam.camara_cve = :idCamara " + 
					"OR :idCamara IS NULL ) " + 
					"AND (plt.planta_cve = :idPlanta " + 
					"OR :idPlanta IS NULL ) " + 
					" " + 
					" " + 
					") I " + 
					"WHERE " + 
					"I.cantidad > 0 " + 
					"group by " + 
					"camaraCve, " + 
					"camaraAbrev, " + 
					"camaraDs, " + 
					"plantaDs, " + 
					"totalPos " + 
					")cli " + 
					"inner join( " + 
					"select " + 
					"I.camara_cve AS camaraCve, " + 
					"I.camara_abrev AS camaraAbrev, " + 
					"I.camara_ds AS camaraDs, " + 
					"I.planta_ds AS plantaDs, " + 
					"SUM(I.tarima) AS tarima " + 
					"from " + 
					"( " + 
					"select " + 
					"cam.CAMARA_CVE AS camara_cve, " + 
					"cam.CAMARA_ABREV AS camara_abrev, " + 
					"cam.CAMARA_DS AS camara_ds, " + 
					"plt.PLANTA_DS AS planta_ds, " + 
					"(parEnt.cantidad_total - COALESCE(salidas.cantidad, 0)) as cantidad, " + 
					"CEILING((parEnt.cantidad_total - COALESCE(salidas.cantidad, 0)) * parEnt.no_tarimas / parEnt.CANTIDAD_TOTAL ) as tarima, " + 
					"cam.total_posiciones AS total_pos " + 
					"from " + 
					"partida parEnt " + 
					"inner join ( " + 
					"select " + 
					"* " + 
					"from " + 
					"constancia_de_deposito cdd " + 
					"WHERE " + 
					"cdd.fecha_ingreso <= :Fecha " + 
					") cddEnt on " + 
					"parEnt.folio = cddEnt.folio " + 
					"inner join unidad_de_producto udp on " + 
					"udp.unidad_de_producto_cve = parEnt.unidad_de_producto_cve " + 
					"inner join producto prd on " + 
					"prd.producto_cve = udp.producto_cve " + 
					"inner join unidad_de_manejo udm on " + 
					"udm.unidad_de_manejo_cve = udp.unidad_de_manejo_cve " + 
					"inner join camara cam on " + 
					"cam.camara_cve = parEnt.camara_cve " + 
					"inner join planta plt on " + 
					"plt.planta_cve = cam.planta_cve " + 
					"inner join cliente cli on " + 
					"cli.cte_cve = cddEnt.cte_cve " + 
					"inner join " + 
					"( " + 
					"select " + 
					"tdp.* " + 
					"from " + 
					"detalle_partida tdp " + 
					"inner join ( " + 
					"select " + 
					"dp.partida_cve, " + 
					"max(dp.det_part_cve) as det_part_cve " + 
					"from " + 
					"detalle_partida dp " + 
					"group by " + 
					"dp.partida_cve " + 
					") tmdp on " + 
					"tdp.partida_cve = tmdp.partida_cve " + 
					"and tdp.det_part_cve = tmdp.det_part_cve " + 
					") detPart on " + 
					"detPart.partida_cve = parEnt.partida_cve " + 
					"left outer join posicion_partida pp on " + 
					"parEnt.partida_cve = pp.ID_PARTIDA " + 
					"left outer join posicion pos on " + 
					"pp.ID_POSICION = pos.id_posicion " + 
					"left outer join ( " + 
					"select " + 
					"dcs.partida_cve, " + 
					"sum(COALESCE(dcs.peso, 0)) as peso, " + 
					"sum(COALESCE(dcs.cantidad, 0)) as cantidad " + 
					"from " + 
					"constancia_salida cSal " + 
					"inner join detalle_constancia_salida dcs on " + 
					"dcs.constancia_cve = cSal.id " + 
					"WHERE " + 
					"cSal.status = 1 " + 
					"AND cSal.fecha <= :Fecha " + 
					"group by " + 
					"dcs.PARTIDA_CVE " + 
					") salidas ON " + 
					"parEnt.partida_cve = salidas.partida_cve " + 
					"where " + 
					"cddEnt.status <> 4 " + 
					"AND parEnt.no_tarimas >= 1.00 " + 
					"AND (cam.camara_cve = :idCamara " + 
					"OR :idCamara IS NULL ) " + 
					"AND (plt.planta_cve = :idPlanta " + 
					"OR :idPlanta IS NULL ) " + 
					"union all " + 
					"select " + 
					"cam.CAMARA_CVE AS camara_cve, " + 
					"cam.CAMARA_ABREV AS camara_abrev, " + 
					"cam.CAMARA_DS AS camara_ds, " + 
					"plt.PLANTA_DS AS planta_ds, " + 
					"(parEnt.cantidad_total - COALESCE(salidas.cantidad, 0)) as cantidad, " + 
					"parEnt.no_tarimas, " + 					
					"cam.total_posiciones AS total_pos " + 
					"from " + 
					"partida parEnt " + 
					"inner join ( " + 
					"select " + 
					"* " + 
					"from " + 
					"constancia_de_deposito cdd " + 
					"WHERE " + 
					"cdd.fecha_ingreso <= :Fecha " + 
					") cddEnt on " + 
					"parEnt.folio = cddEnt.folio " + 
					"inner join unidad_de_producto udp on " + 
					"udp.unidad_de_producto_cve = parEnt.unidad_de_producto_cve " + 
					"inner join producto prd on " + 
					"prd.producto_cve = udp.producto_cve " + 
					"inner join unidad_de_manejo udm on " + 
					"udm.unidad_de_manejo_cve = udp.unidad_de_manejo_cve " + 
					"inner join camara cam on " + 
					"cam.camara_cve = parEnt.camara_cve " + 
					"inner join planta plt on " + 
					"plt.planta_cve = cam.planta_cve " + 
					"inner join cliente cli on " + 
					"cli.cte_cve = cddEnt.cte_cve " + 
					"inner join " + 
					"( " + 
					"select " + 
					"tdp.* " + 
					"from " + 
					"detalle_partida tdp " + 
					"inner join ( " + 
					"select " + 
					"dp.partida_cve, " + 
					"max(dp.det_part_cve) as det_part_cve " + 
					"from " + 
					"detalle_partida dp " + 
					"group by " + 
					"dp.partida_cve " + 
					") tmdp on " + 
					"tdp.partida_cve = tmdp.partida_cve " + 
					"and tdp.det_part_cve = tmdp.det_part_cve " + 
					") detPart on " + 
					"detPart.partida_cve = parEnt.partida_cve " + 
					"left outer join posicion_partida pp on " + 
					"parEnt.partida_cve = pp.ID_PARTIDA " + 
					"left outer join posicion pos on " + 
					"pp.ID_POSICION = pos.id_posicion " + 
					"left outer join ( " + 
					"select " + 
					"dcs.partida_cve, " + 
					"sum(COALESCE(dcs.peso, 0)) as peso, " + 
					"sum(COALESCE(dcs.cantidad, 0)) as cantidad " + 
					"from " + 
					"constancia_salida cSal " + 
					"inner join detalle_constancia_salida dcs on " + 
					"dcs.constancia_cve = cSal.id " + 
					"WHERE " + 
					"cSal.status = 1 " + 
					"AND cSal.fecha <= :Fecha " + 
					"group by " + 
					"dcs.PARTIDA_CVE " + 
					") salidas ON " + 
					"parEnt.partida_cve = salidas.partida_cve " + 
					"where " + 
					"cddEnt.status <> 4 " + 
					"AND parEnt.no_tarimas < 1.00 " + 
					"AND (cam.camara_cve = :idCamara " + 
					"OR :idCamara IS NULL ) " + 
					"AND (plt.planta_cve = :idPlanta " + 
					"OR :idPlanta IS NULL ) " + 
					" " + 
					" " + 
					") I " + 
					"WHERE " + 
					"I.cantidad > 0 " + 
					"group by " + 
					"camaraCve, " + 
					"camaraAbrev, " + 
					"camaraDs, " + 
					"plantaDs " + 
					")gen " + 
					"on " + 
					"cli.camaraCve = gen.camaraCve " + 
					"ORDER BY " + 
					"camaraCve ";
			
			Query query = em.createNativeQuery(sql)
					.setParameter("Fecha", fecha)
					.setParameter("idCliente", idCliente)
					.setParameter("idCamara", idCamara)
					.setParameter("idPlanta", idPlanta);
			
			List<Object[]> listaObjetos = query.getResultList();
						
			
			for(Object[] o: listaObjetos) {
				
				OcupacionCamara oc = new OcupacionCamara();
				int id = 0;
				
				oc.setCamara_cve((Integer) o[id++]);
				oc.setCamara_abrev((String) o[id++]);
				oc.setCamara_ds((String) o[id++]);
				oc.setPlanta_ds((String) o[id++]);
				oc.setTotal_pos((Integer) o[id++]);	
				oc.setTarima((BigDecimal)o[id++]);
				oc.setPosiciones_Generales_Ocupadas((BigDecimal)o[id++]);
				oc.setPosiciones_Disponibles((BigDecimal)o[id++]);
				
				
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
