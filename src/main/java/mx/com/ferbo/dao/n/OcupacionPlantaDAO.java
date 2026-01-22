package mx.com.ferbo.dao.n;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.ui.OcupacionPlanta;
import mx.com.ferbo.util.EntityManagerUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author alberto
 */
@Named
@ApplicationScoped
public class OcupacionPlantaDAO extends BaseDAO<OcupacionPlanta, Integer> 
{
    private static Logger log = LogManager.getLogger(OcupacionPlanta.class);
    
    public OcupacionPlantaDAO(Class<OcupacionPlanta> modelClass) {
        super(modelClass);
    }
    
    public OcupacionPlantaDAO(){
        super(OcupacionPlanta.class);
    }
    
    public List<OcupacionPlanta> ocupacionPlantaCamara(Date fecha, Integer idCliente, Integer idPlanta, Integer idCamara)
    {
        EntityManager em = null;		
        List<OcupacionPlanta> listOcupacionPlantaCamara = null;
        String sql = null;

        try{
            em = EntityManagerUtil.getEntityManager();

            listOcupacionPlantaCamara = new ArrayList<>();

            sql = "WITH inventario AS (\n" +
                    "    SELECT\n" +
                    "        cddEnt.folio            AS folio,\n" +
                    "        cli.cte_nombre          AS cliente,\n" +
                    "        cli.numero_cte          AS numero_cte,\n" +
                    "        cddEnt.folio_cliente    AS folio_cliente,\n" +
                    "        cddEnt.fecha_ingreso    AS ingreso,\n" +
                    "        t.nb_tarima             AS nb_tarima,\n" +
                    "        parEnt.partida_cve      AS partida_cve,\n" +
                    "        (parEnt.cantidad_total - COALESCE(salidas.cantidad, 0)) AS cantidad,\n" +
                    "        parEnt.cantidad_total   AS cantidad_total,\n" +
                    "        udm.unidad_de_manejo_ds AS unidad_cobro,\n" +
                    "        parEnt.no_tarimas       AS no_tarimas,\n" +
                    "        (parEnt.peso_total - COALESCE(salidas.peso, 0)) AS peso,\n" +
                    "        prd.producto_cve        as producto_cve,\n" +
                    "        prd.producto_ds         AS producto,\n" +
                    "        cam.camara_cve          AS cam_cve,\n" +
                    "        cam.camara_abrev        AS camara,\n" +
                    "        plt.planta_cve          AS plt_cve,\n" +
                    "        plt.planta_abrev        AS planta,\n" +
                    "        parEnt.cd_tarima        AS cd_tarima\n" +
                    "    FROM partida parEnt\n" +
                    "    INNER JOIN (\n" +
                    "	    SELECT *\n" +
                    "	    FROM constancia_de_deposito cdd\n" +
                    "	    WHERE cdd.status = 1\n" +
                    "	      AND (cdd.cte_cve = :idCliente OR :idCliente IS NULL)\n" +
                    "	      AND cdd.fecha_ingreso <= :Fecha\n" +
                    "	) cddEnt ON parEnt.folio = cddEnt.folio\n" +
                    "    LEFT JOIN tarima t ON t.cd_tarima = parEnt.cd_tarima\n" +
                    "    INNER JOIN unidad_de_producto udp ON udp.unidad_de_producto_cve = parEnt.unidad_de_producto_cve\n" +
                    "    INNER JOIN producto prd ON prd.producto_cve = udp.producto_cve\n" +
                    "    INNER JOIN unidad_de_manejo udm ON udm.unidad_de_manejo_cve = udp.unidad_de_manejo_cve\n" +
                    "    INNER JOIN camara cam ON cam.camara_cve = parEnt.camara_cve\n" +
                    "    INNER JOIN planta plt ON plt.planta_cve = cam.planta_cve\n" +
                    "    INNER JOIN cliente cli ON cli.cte_cve = cddEnt.cte_cve\n" +
                    "    INNER JOIN (\n" +
                    "    SELECT tdp.*\n" +
                    "    FROM detalle_partida tdp\n" +
                    "    INNER JOIN (\n" +
                    "	        SELECT dp.partida_cve,\n" +
                    "	               MAX(dp.det_part_cve) AS det_part_cve\n" +
                    "	        FROM detalle_partida dp\n" +
                    "	        GROUP BY dp.partida_cve\n" +
                    "	    ) tmdp\n" +
                    "	      ON tdp.partida_cve = tmdp.partida_cve\n" +
                    "	     AND tdp.det_part_cve = tmdp.det_part_cve\n" +
                    "	) detPart\n" +
                    "	ON detPart.partida_cve = parEnt.partida_cve\n" +
                    "    LEFT JOIN posicion_partida pp ON parEnt.partida_cve = pp.id_partida\n" +
                    "    LEFT JOIN posicion pos ON pp.id_posicion = pos.id_posicion\n" +
                    "    LEFT OUTER JOIN (\n" +
                    "	    SELECT \n" +
                    "	        dcs.partida_cve,\n" +
                    "	        SUM(COALESCE(dcs.peso,0)) AS peso,\n" +
                    "	        SUM(COALESCE(dcs.cantidad,0)) AS cantidad\n" +
                    "	    FROM constancia_salida cSal\n" +
                    "	    INNER JOIN detalle_constancia_salida dcs \n" +
                    "	        ON dcs.constancia_cve = cSal.id\n" +
                    "	    WHERE cSal.status = 1 \n" +
                    "	      AND cSal.fecha <= :Fecha\n" +
                    "	    GROUP BY dcs.partida_cve\n" +
                    "	) salidas ON parEnt.partida_cve = salidas.partida_cve\n" +
                    "    WHERE\n" +
                    "        cddEnt.status <> 4\n" +
                    "        AND (cam.camara_cve = :Camara OR :Camara IS NULL)\n" +
                    "        AND (plt.planta_cve = :Planta OR :Planta IS NULL)\n" +
                    "), \n" +
                    "t1 AS (\n" +
                    "    SELECT folio, cliente, numero_cte, folio_cliente, ingreso, nb_tarima, sum(cantidad) as cantidad, unidad_cobro, SUM(CEILING(cantidad * no_tarimas / cantidad_total)) AS tarima, sum(peso) AS peso, producto_cve, producto, cam_cve, camara, plt_cve, planta\n" +
                    "    FROM inventario\n" +
                    "    WHERE no_tarimas >= 1 AND cd_tarima IS NULL\n" +
                    "    GROUP BY folio, cliente, numero_cte, folio_cliente, ingreso, nb_tarima, unidad_cobro, producto_cve, producto, cam_cve, camara, plt_cve, planta\n" +
                    "), \n" +
                    "t2 AS (\n" +
                    "    SELECT folio, cliente, numero_cte, folio_cliente, ingreso, nb_tarima, sum(cantidad) as cantidad, unidad_cobro, SUM(no_tarimas) AS tarima, sum(peso) AS peso, producto_cve, producto, cam_cve, camara, plt_cve, planta\n" +
                    "    FROM inventario\n" +
                    "    WHERE no_tarimas < 1 AND cd_tarima IS NULL\n" +
                    "    GROUP BY folio, cliente, numero_cte, folio_cliente, ingreso, nb_tarima, unidad_cobro, producto_cve, producto, cam_cve, camara, plt_cve, planta\n" +
                    "), \n" +
                    "t3 AS (\n" +
                    "    SELECT folio, cliente, numero_cte, folio_cliente, ingreso, nb_tarima, sum(cantidad) as cantidad, unidad_cobro, SUM(no_tarimas) AS tarima, sum(peso) AS peso, producto_cve, producto, cam_cve, camara, plt_cve, planta\n" +
                    "    FROM inventario \n" +
                    "    WHERE cd_tarima IS NOT NULL\n" +
                    "    GROUP BY folio, cliente, numero_cte, folio_cliente, ingreso, nb_tarima, unidad_cobro, producto_cve, producto, cam_cve, camara, plt_cve, planta\n" +
                    ")\n" +
                    "SELECT I.nb_tarima,\n" +
                    "       SUM(I.tarima),\n" +
                    "       I.camara,\n" +
                    "       I.planta\n" +
                    "FROM (\n" +
                    "    SELECT * FROM t1\n" +
                    "    UNION ALL\n" +
                    "    SELECT * FROM t2\n" +
                    "    UNION ALL\n" +
                    "    SELECT * FROM t3\n" +
                    ") I\n" +
                    "WHERE I.cantidad > 0\n" +
                    "GROUP BY I.nb_tarima, I.camara, I.planta\n" +
                    "ORDER BY I.nb_tarima";

            Query query = em.createNativeQuery(sql)
                .setParameter("Fecha", fecha)
                .setParameter("idCliente", idCliente)
                .setParameter("Camara", idCamara)
                .setParameter("Planta", idPlanta);

            List<Object[]> listaObjetos = query.getResultList();
            
            for(Object[] o: listaObjetos) {
                OcupacionPlanta op = new OcupacionPlanta();
                int id = 0;
                
                op.setNbTarima((String) o[id++]);
                op.setTarima((BigDecimal) o[id++]);
                op.setCamara((String) o[id++]);
                op.setPlanta((String) o[id++]);
                
                listOcupacionPlantaCamara.add(op);
            }
        } catch (Exception e) {
            log.info("Error al obtener Ocupacion de Plantas" + e.getMessage());
        }finally {
            EntityManagerUtil.close(em);
        }

        return listOcupacionPlantaCamara;
    }
    
}
