package mx.com.ferbo.dao.n;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.ImporteEgreso;
import mx.com.ferbo.ui.ImporteUtilidad;
import mx.com.ferbo.util.DAOException;
import mx.com.ferbo.util.DateUtil;

@Named
@ApplicationScoped
public class ImporteEgresosDAO extends BaseDAO<ImporteEgreso, Integer> {

    private static final Logger log = LogManager.getLogger(ImporteEgresosDAO.class);

    public ImporteEgresosDAO() {
        super(ImporteEgreso.class);
    }

    public List<ImporteUtilidad> obtenerUtilidadPorEmisor(String emisor, Date fecha) throws DAOException {
        EntityManager em = null;
        List<ImporteUtilidad> list = null;
        try {
            em = super.getEntityManager();
            String sql = "SELECT  " + "    combined.emi_nombre AS emi_nombre, "
                    + "    DATE_FORMAT(combined.fecha, '%Y-%m') AS fecha, " + "    SUM(combined.total_pagos) AS pagos, "
                    + "    SUM(combined.total_egresos) AS egresos, "
                    + "    SUM(combined.total_pagos - combined.total_egresos) AS utilidad_perdida " + "FROM ( "
                    + "    SELECT  " + "        ie.fecha, " + "        e.nb_emisor AS emi_nombre, "
                    + "        COALESCE(SUM(ie.importe), 0) AS total_egresos, " + "        0 AS total_pagos "
                    + "    FROM  " + "        emisor e "
                    + "        LEFT JOIN importe_egreso ie ON e.cd_emisor = ie.cd_emisor "
                    + "            AND DATE_FORMAT(ie.fecha, '%Y-%m') = DATE_FORMAT(:fecha, '%Y-%m')   " + "    WHERE  "
                    + "        (e.nb_emisor = :EMISOR OR :EMISOR IS NULL) " + "    GROUP BY  "
                    + "        ie.fecha, e.nb_emisor " + " " + "    UNION " + " " + "    SELECT  "
                    + "        MAX(p.fecha) AS fecha, " + "        f.emi_nombre, " + "        0 AS total_egresos, "
                    + "        COALESCE(SUM(p.monto), 0) AS total_pagos " + "    FROM  " + "        factura f "
                    + "        LEFT JOIN pago p ON f.id = p.factura "
                    + "            AND DATE_FORMAT(f.fecha, '%Y-%m') = DATE_FORMAT(:fecha, '%Y-%m')   " + "    WHERE  "
                    + "        (f.emi_nombre = :EMISOR OR :EMISOR IS NULL) " + "    GROUP BY  "
                    + "        f.emi_nombre " + ") AS combined " + "WHERE fecha IS NOT NULL " + "GROUP BY  "
                    + "    emi_nombre, DATE_FORMAT(combined.fecha, '%Y-%m') " + "ORDER BY  "
                    + "    fecha, emi_nombre; ";

            Query query = em.createNativeQuery(sql).setParameter("EMISOR", emisor).setParameter("fecha", fecha);

            List<Object[]> listaObjetos = query.getResultList();

            for (Object[] o : listaObjetos) {
                ImporteUtilidad u = new ImporteUtilidad();
                int id = 0;
                u.setEmiNombre((String) o[id++]);
                String fh = ((String) o[id++]);
                Date f = DateUtil.getDate(fh, DateUtil.FORMATO_YYYY_MM);
                u.setFecha(f);
                u.setPagos((BigDecimal) o[id++]);
                u.setEgresos((BigDecimal) o[id++]);
                u.setUtilidadPerdida((BigDecimal) o[id++]);
                list.add(u);
            }

            return list;
        } catch (Exception e) {
            log.warn("Error al obtener la utilidad del emisor {} en la fecha {}", emisor, fecha);
            throw new DAOException("Hubo un problema al obtener la utilidad del emisor " + emisor);
        } finally {
            super.close(em);
        }
    }

    public List<ImporteEgreso> buscarPorEmisorYFechas(String emisor, Date fechaInicio, Date fechaFin)
            throws DAOException {
        EntityManager em = null;
        List<ImporteEgreso> list = null;
        try {
            em = super.getEntityManager();
            list = em.createNamedQuery("ImporteEgreso.findByParametros", ImporteEgreso.class)
                    .setParameter("emisor", emisor)
                    .setParameter("fechaInicio", fechaInicio)
                    .setParameter("fechaFin", fechaFin)
                    .getResultList();
            return list;
        } catch (Exception ex) {
            log.warn("Error al obtener los importes de ingreso del emisor {} desde {} hasta la fecha {}. {}", emisor,
                    fechaInicio, fechaFin, ex);
            throw new DAOException("Hubo un problema al obtener todos los importes de ingreso del emisor");
        } finally {
            super.close(em);
        }
    }

    public List<ImporteEgreso> findAll() throws DAOException {
        EntityManager em = null;
        List<ImporteEgreso> list = null;
        try {
            em = super.getEntityManager();
            list = em.createNamedQuery("ImporteEgreso.findByAll", ImporteEgreso.class).getResultList();
            return list;
        } catch (Exception ex) {
            log.warn("Error al obtener todos los importes de egreso. {}", ex);
            throw new DAOException("Hubo un problema al obtener todos los importes de egreso.");
        } finally {
            super.close(em);
        }
    }
}
