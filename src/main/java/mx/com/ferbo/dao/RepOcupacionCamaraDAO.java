package mx.com.ferbo.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import mx.com.ferbo.ui.OcupacionCamara;
import mx.com.ferbo.util.EntityManagerUtil;

public class RepOcupacionCamaraDAO {
	
	private static Logger log = Logger.getLogger(RepOcupacionCamaraDAO.class);
	
	@SuppressWarnings("unchecked")
	public List<OcupacionCamara> ocupacionCamara(Date fecha, Integer idCliente, Integer idPlanta, Integer idCamara){
		
		EntityManager em = null;		
		List<OcupacionCamara> listaOcupacionCamara = null;
		String sql = null;
		
		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			
			
			listaOcupacionCamara = new ArrayList<OcupacionCamara>();
			
			sql = "select * from ("
					+ "	select"
					+ "	cam.CAMARA_CVE AS camara_cve,"
					+ "	cam.CAMARA_ABREV AS camara_abrev,"
					+ "	cam.CAMARA_DS AS camara_ds,"
					+ " plt.planta_cve AS planta_cve,"
					+ "	plt.PLANTA_DS AS planta_ds,"
					+ "	SUM(CEILING((parEnt.cantidad_total - COALESCE(salidas.cantidad, 0)) * parEnt.no_tarimas / parEnt.CANTIDAD_TOTAL )) as tarima"
					+ "	from PARTIDA parEnt"
					+ "	inner join ("
					+ "	select *"
					+ "	from CONSTANCIA_DE_DEPOSITO cdd"
					+ "	WHERE (cdd.cte_cve = :idCliente OR :idCliente IS NULL)"
					+ "	AND cdd.fecha_ingreso <= :Fecha"
					+ ") cddEnt on parEnt.folio = cddEnt.folio"
					+ "	inner join UNIDAD_DE_PRODUCTO udp on udp.unidad_de_producto_cve = parEnt.unidad_de_producto_cve"
					+ "	inner join PRODUCTO prd on prd.producto_cve = udp.producto_cve"
					+ "	inner join UNIDAD_DE_MANEJO udm on udm.unidad_de_manejo_cve = udp.unidad_de_manejo_cve"
					+ "	inner join CAMARA cam on cam.camara_cve = parEnt.camara_cve"
					+ "	inner join PLANTA plt on plt.planta_cve = cam.planta_cve"
					+ "	inner join CLIENTE cli on cli.cte_cve = cddEnt.cte_cve"
					+ "	left outer join POSICION_PARTIDA pp on parEnt.partida_cve = pp.ID_PARTIDA"
					+ "	left outer join posicion pos on pp.ID_POSICION = pos.id_posicion"
					+ "	left outer join ("
					+ "select"
					+ "	dcs.partida_cve,"
					+ "	sum(COALESCE(dcs.peso,0)) as peso,"
					+ "	sum(COALESCE(dcs.cantidad,0)) as cantidad"
					+ "	from CONSTANCIA_SALIDA cSal"
					+ "	inner join DETALLE_CONSTANCIA_SALIDA dcs on dcs.constancia_cve = cSal.id"
					+ "	WHERE cSal.status = 1"
					+ "	AND cSal.fecha <= :Fecha"
					+ "	group by"
					+ "	dcs.PARTIDA_CVE"
					+ ") salidas ON parEnt.partida_cve = salidas.partida_cve"
					+ "	where"
					+ "	cddEnt.status <> 4"
					+ "	AND (cam.camara_cve = :idCamara OR :idCamara IS NULL )"
					+ "	AND (plt.planta_cve  = :idPlanta OR :idPlanta IS NULL )"
					+ "	GROUP BY camara_cve,camara_ds, camara_abrev,planta_ds,planta_cve"
					+ "	ORDER BY planta_ds"
					+ ") I"
					+ " WHERE tarima > 0"
					+ " GROUP BY I.camara_cve, I.camara_ds, I.camara_abrev ,I.planta_ds,I.planta_cve";
			
			Query query = em.createNativeQuery(sql, OcupacionCamara.class)
					.setParameter("Fecha", fecha)
					.setParameter("idCliente", idCliente)
					.setParameter("idCamara", idCamara)
					.setParameter("idPlanta", idPlanta);
			
			listaOcupacionCamara = query.getResultList();
			
			
		} catch (Exception e) {
			log.info("Error al obtener Ocupacion de Camaras " + e.getMessage());
		}finally {
			em.close();
		}
		
		
		return listaOcupacionCamara;			
	}
	

}
