package mx.com.ferbo.dao.n;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.ui.RepSalidas;
import mx.com.ferbo.util.EntityManagerUtil;

@Named
@ApplicationScoped
public class RepSalidasDAO extends BaseDAO<RepSalidas, Integer> 
{
    private static Logger log = LogManager.getLogger(RepSalidasDAO.class);

    public RepSalidasDAO() {
        super(RepSalidas.class);
    }
    
    public List<RepSalidas> buscar(Date fechaIni, Date fechaFin, List<Integer> listClientes, Integer idPlanta, Integer idCamara) 
    {
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
                    + "FROM constancia_salida cs "
                    + "INNER JOIN detalle_constancia_salida dcs ON dcs.constancia_cve = cs.id  "
                    + "INNER JOIN partida p ON p.PARTIDA_CVE = dcs.partida_cve "
                    + "INNER JOIN constancia_de_deposito cdd ON p.folio = cdd.folio "
                    + "INNER JOIN cliente c ON c.CTE_CVE = cdd.cte_cve  "
                    + "INNER JOIN camara cam ON cam.camara_cve = dcs.camara_cve "
                    + "INNER JOIN planta plt ON plt.planta_cve = cam.planta_cve "
                    + "WHERE cs.status NOT IN (2) "
                    + "	AND cs.FECHA BETWEEN :fechaIni AND :fechaFin "
                    + "	AND (:idCliente IS NULL OR cdd.cte_cve IN :idCliente) "
                    + "	AND (plt.planta_cve = :idPlanta OR :idPlanta IS NULL) "
                    + "	AND (cam.camara_cve = :idCamara OR :idCamara IS NULL) "
                    + "ORDER BY "
                    + "	c.cte_nombre ASC,"
                    + "	cdd.FECHA_INGRESO ASC,"
                    + "	cdd.folio_cliente asc,"
                    + "	p.PARTIDA_CVE ASC,"
                    + "	cs.fecha ASC";
            entity = EntityManagerUtil.getEntityManager();

            List<Object[]> results = entity.createNativeQuery(sql)
                    .setParameter("fechaIni", fechaIni)
                    .setParameter("fechaFin", fechaFin)
                    .setParameter("idCliente", listClientes)
                    .setParameter("idPlanta", idPlanta)
                    .setParameter("idCamara", idCamara)
                    .getResultList();

            resultList = new ArrayList<RepSalidas>();
            for (Object[] o : results) {
                RepSalidas r = new RepSalidas();
                int idx = 0;

                r.setFecha((Date) o[idx++]);
                r.setFolioCliente((String) o[idx++]);
                r.setNumero((String) o[idx++]);
                r.setProducto((String) o[idx++]);
                r.setUnidad((String) o[idx++]);
                r.setCantidad((Integer) o[idx++]);
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

        } catch (Exception ex) {
            log.error("Problema para obtener el reporte de Entradas...", ex);
        } finally {
            EntityManagerUtil.close(entity);
        }

        return resultList;
    }
    
}
