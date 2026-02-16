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
    private static Logger log = LogManager.getLogger(OcupacionPlantaDAO.class);
    
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

            sql = "WITH i AS (\n" +
                "    SELECT\n" +
                "        c.CTE_NOMBRE AS cliente,\n" +
                "        plt.PLANTA_DS AS planta,\n" +
                "        cam.CAMARA_DS AS camara,\n" +
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
                "    WHERE cdd.status = 1 AND cdd.FECHA_INGRESO <= :fecha\n" +
                "      AND (:idPlanta IS NULL OR plt.PLANTA_CVE = :idPlanta)\n" +
                "      AND (:idCamara IS NULL OR cam.CAMARA_CVE = :idCamara)\n" +
                "      AND (:idCliente IS NULL OR cdd.cte_cve = :idCliente)\n" +
                "),\n" +
                "t1 AS (\n" +
                "    SELECT i.planta, i.camara , CEILING((i.peso / i.peso_total) * i.no_tarimas) AS tarimas\n" +
                "    FROM i\n" +
                "    WHERE i.no_tarimas IS NOT NULL AND i.cd_tarima IS NULL AND i.cantidad > 0\n" +
                "),\n" +
                "t2 AS (\n" +
                "    SELECT i.planta, i.camara , COUNT(DISTINCT i.cd_tarima) AS tarimas\n" +
                "    FROM i\n" +
                "    WHERE i.no_tarimas IS NULL AND i.cd_tarima IS NOT NULL AND i.cantidad > 0\n" +
                "    GROUP BY i.planta, i.camara \n" +
                ")\n" +
                "SELECT o.planta, o.camara, SUM(o.tarimas) AS tarimas\n" +
                "FROM (\n" +
                "    SELECT * FROM t1\n" +
                "    UNION ALL\n" +
                "    SELECT * FROM t2\n" +
                ") o\n" +
                "GROUP BY o.planta, o.camara\n" +
                "ORDER BY o.planta, o.camara";

            Query query = em.createNativeQuery(sql)
                .setParameter("fecha", fecha)
                .setParameter("idCliente", idCliente)
                .setParameter("idCamara", idCamara)
                .setParameter("idPlanta", idPlanta);

            List<Object[]> listaObjetos = query.getResultList();
            
            for(Object[] o: listaObjetos) {
                OcupacionPlanta op = new OcupacionPlanta();
                int id = 0;
                
                op.setPlanta((String) o[id++]);
                op.setCamara((String) o[id++]);
                op.setTarima((BigDecimal) o[id++]);
                
                listOcupacionPlantaCamara.add(op);
            }
            
        } catch (Exception e) {
            log.info("Error al obtener ocupacion de posiciones por plantas...", e);
        }finally {
            super.close(em);
        }

        return listOcupacionPlantaCamara;
    }
    
}
