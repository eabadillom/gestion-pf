package mx.com.ferbo.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.Factura;
import mx.com.ferbo.model.FacturacionGeneral;
import mx.com.ferbo.model.VentasGlobales;
import mx.com.ferbo.ui.ImporteUtilidad;
import mx.com.ferbo.util.DateUtil;
import mx.com.ferbo.util.EntityManagerUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ReportesVentasDAO extends IBaseDAO<Factura, Integer> 
{
    private static Logger log = LogManager.getLogger(ReportesVentasDAO.class);

    @Override
    public Factura buscarPorId(Integer id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Factura> buscarTodos() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Factura> buscarPorCriterios(Factura e) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String actualizar(Factura e) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String guardar(Factura e) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String eliminar(Factura e) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String eliminarListado(List<Factura> listado) {
        // TODO Auto-generated method stub
        return null;
    }

    @SuppressWarnings("unchecked")
    public List<VentasGlobales> ventasGanancias(Date fechaIni, Date fechaFin) 
    {
        List<VentasGlobales> listaVentas = null;
        EntityManager em = null;
        String sql = null;
        try {
            em = EntityManagerUtil.getEntityManager();

            listaVentas = new ArrayList<>();

            sql = "SELECT\n" +
                    "	COALESCE(a.ventas_totales, 0) ,\n" +
                    "	COALESCE((a.ventas_totales - a.egresos), 0) AS ganancias,\n" +
                    "	COALESCE(((a.ventas_totales - a.egresos) / a.ventas_totales), 0) AS porcentaje_ganancia\n" +
                    "FROM\n" +
                    "(\n" +
                    "	SELECT SUM(combined.total_facturas), SUM(combined.total_ventas),\n" +
                    "		SUM(combined.total_facturas + combined.total_ventas ) AS ventas_totales, SUM(combined.Total_egresos ) AS egresos\n" +
                    "	FROM\n" +
                    "	(\n" +
                    "		SELECT SUM(f.total) AS total_facturas, 0 AS total_ventas, 0 AS Total_egresos, 0 AS ventas_totales\n" +
                    "		FROM factura f\n" +
                    "		WHERE f.fecha BETWEEN :fechaini AND :fechaFin AND status IN (1, 3, 4)\n" +
                    "		GROUP BY fecha\n" +
                    "		UNION\n" +
                    "		SELECT 0 AS total_facturas, SUM(v.total) AS total_ventas, 0 AS Total_egresos, 0 AS ventas_totales\n" +
                    "		FROM venta v\n" +
                    "		INNER JOIN parametro p ON p.valor = 'true' AND p.nombre = 'SWIZQ'\n" +
                    "		WHERE v.fecha BETWEEN :fechaini AND :fechaFin \n" +
                    "		GROUP BY fecha\n" +
                    "		UNION\n" +
                    "		SELECT 0 AS total_facturas, 0 AS total_ventas, SUM(ie.importe) AS Total_egresos, 0 AS ventas_totales\n" +
                    "		FROM importe_egreso ie\n" +
                    "		WHERE ie.fecha BETWEEN :fechaini AND :fechaFin\n" +
                    "		GROUP BY fecha \n" +
                    "	) combined \n" +
                    ")a";

            Query query = em.createNativeQuery(sql)
                    .setParameter("fechaini", DateUtil.getString(fechaIni, DateUtil.FORMATO_YYYY_MM_DD))
                    .setParameter("fechaFin", DateUtil.getString(fechaFin, DateUtil.FORMATO_YYYY_MM_DD));

            List<Object[]> listaObjetos = query.getResultList();

            int id = 0;
            for (Object[] o : listaObjetos) {
                VentasGlobales vg = new VentasGlobales();
                vg.setVentasTotales((BigDecimal) o[id++]);
                vg.setGanancias((BigDecimal) o[id++]);
                vg.setPorcentajeGanancias((BigDecimal) o[id++]);
                listaVentas.add(vg);
            }
            return listaVentas;
        } catch (Exception e) {
            log.error("Error al consultar ventas por ganancias: {}", e);
            return null;
        } finally {
            EntityManagerUtil.close(em);
        }

    }

    @SuppressWarnings("unchecked")
    public List<FacturacionGeneral> desgloseFacturacion(Date fechaIni, Date fechaFin) 
    {
        List<FacturacionGeneral> listaFacturacion = null;
        EntityManager em = null;
        String sql = null;
        try {
            em = EntityManagerUtil.getEntityManager();

            listaFacturacion = new ArrayList<>();

            sql = "SELECT combined.fecha, SUM(combined.total_facturas), SUM(combined.total_efectivo_por_dia) \n" +
                    "FROM (\n" +
                    "    SELECT fecha, SUM(f.total) AS total_facturas, NULL AS total_efectivo_por_dia\n" +
                    "    FROM factura f WHERE f.fecha BETWEEN :fechaIni AND :fechaFin AND status IN (1,3,4)\n" +
                    "    GROUP BY fecha \n" +
                    "	UNION\n" +
                    "    SELECT fecha, NULL AS total_facturas, SUM(v.total) AS total_efectivo_por_dia\n" +
                    "    FROM venta v\n" +
                    "    INNER JOIN parametro p ON p.valor = 'true' AND p.nombre = 'SWIZQ'\n" +
                    "    WHERE v.fecha BETWEEN :fechaIni AND :fechaFin\n" +
                    "    GROUP BY fecha\n" +
                    "    )combined \n" +
                    "GROUP BY combined.fecha \n" +
                    "ORDER BY combined.fecha DESC";

            Query query = em.createNativeQuery(sql)
                    .setParameter("fechaIni", DateUtil.getString(fechaIni, DateUtil.FORMATO_YYYY_MM_DD))
                    .setParameter("fechaFin", DateUtil.getString(fechaFin, DateUtil.FORMATO_YYYY_MM_DD));

            List<Object[]> listaObjetos = query.getResultList();

            for (Object[] o : listaObjetos) {
                FacturacionGeneral fg = new FacturacionGeneral();
                int id = 0;
                fg.setFecha((Date) o[id++]);
                fg.setTotal_facturacion((BigDecimal) o[id++]);
                fg.setTotal_por_efectivo((BigDecimal) o[id++]);
                listaFacturacion.add(fg);
            }
            return listaFacturacion;
        } catch (Exception e) {
            log.error("Error al consultar el desgloce de facturacion: {}", e);
            return null;
        } finally {
            EntityManagerUtil.close(em);
        }
    }

    @SuppressWarnings("unchecked")
    public List<ImporteUtilidad> UtilidadPorMesAnual(String fechaIni, String fechaFin) 
    {
        List<ImporteUtilidad> lista = null;
        EntityManager entity = null;
        String sql = null;

        try {
            entity = EntityManagerUtil.getEntityManager();

            lista = new ArrayList<>();

            sql = "SELECT resultados.fecha, SUM(resultados.pagos), SUM(resultados.egresos), SUM(resultados.utilidad_perdida), SUM(resultados.izq)\n" +
                    "FROM(\n" +
                    "	SELECT DATE_FORMAT(combined.fecha, '%Y-%m') AS fecha, SUM(combined.total_pagos) AS pagos, SUM(combined.total_egresos) AS egresos, \n" +
                    "		SUM(combined.total_pagos - combined.total_egresos) AS utilidad_perdida, SUM(combined.efectivo) AS izq \n" +
                    "	FROM (\n" +
                    "		SELECT ie.fecha, COALESCE(SUM(ie.importe), 0) AS total_egresos, 0 AS total_pagos, 0 AS efectivo\n" +
                    "	    FROM emisor e \n" +
                    "	    LEFT JOIN importe_egreso ie ON e.cd_emisor = ie.cd_emisor \n" +
                    "		WHERE ie.fecha BETWEEN :fechaIni AND :fechaFin\n" +
                    "	    GROUP BY ie.fecha \n" +
                    "	    UNION\n" +
                    "	    SELECT MAX(f.fecha) AS fecha, 0 AS total_egresos, COALESCE(SUM(p.monto), 0) AS total_pagos, 0 as efectivo\n" +
                    "	    FROM factura f \n" +
                    "	    LEFT JOIN pago p ON f.id = p.factura  \n" +
                    "		WHERE f.fecha BETWEEN :fechaIni AND :fechaFin AND status in (1,3,4)\n" +
                    "	    GROUP BY f.fecha \n" +
                    "	    UNION\n" +
                    "	    SELECT v.fecha, 0 AS total_egresos, 0 AS total_pagos, sum(v.total) AS efectivo \n" +
                    "	    FROM venta v\n" +
                    "	    INNER JOIN parametro p ON p.valor = 'true' AND p.nombre = 'SWIZQ' \n" +
                    "	    WHERE v.fecha BETWEEN :fechaIni AND :fechaFin\n" +
                    "	    GROUP BY v.fecha\n" +
                    "	    ) AS combined \n" +
                    "	WHERE fecha IS NOT NULL\n" +
                    "	GROUP BY combined.fecha \n" +
                    "	ORDER BY fecha\n" +
                    ") AS resultados\n" +
                    "GROUP BY resultados.fecha \n" +
                    "ORDER BY resultados.fecha";

            Query query = entity.createNativeQuery(sql).setParameter("fechaIni", fechaIni).setParameter("fechaFin",
                    fechaFin);

            List<Object[]> listaObjetos = query.getResultList();

            for (Object[] o : listaObjetos) {
                ImporteUtilidad u = new ImporteUtilidad();
                int id = 0;
                String fh = ((String) o[id++]);
                Date f = DateUtil.getDate(fh, DateUtil.FORMATO_YYYY_MM);
                u.setFecha(f);
                u.setPagos((BigDecimal) o[id++]);
                u.setEgresos((BigDecimal) o[id++]);
                u.setUtilidadPerdida((BigDecimal) o[id++]);
                u.setIzq((BigDecimal) o[id++]);
                lista.add(u);
            }

        } catch (Exception e) {
            log.error("Error al consultar la utilidad por mes: {}", e);
        } finally {
            EntityManagerUtil.close(entity);
        }
        return lista;
    }

    @SuppressWarnings("unchecked")
    public List<FacturacionGeneral> ventasPorMesAnual(Date fechaini, Date fechafin) 
    {
        List<FacturacionGeneral> lista = null;
        EntityManager entity = null;
        String sql = null;

        try {
            entity = EntityManagerUtil.getEntityManager();

            lista = new ArrayList<>();

            sql = "SELECT fecha, SUM(pagos) AS pagos\n" +
                    "FROM (\n" +
                    "	SELECT DATE_FORMAT(combined.fecha, '%Y-%m') AS fecha, SUM(combined.total_pagos + combined.efectivo) AS pagos\n" +
                    "	FROM (\n" +
                    "	    SELECT ie.fecha, 0 AS total_pagos, 0 AS efectivo\n" +
                    "	    FROM importe_egreso ie\n" +
                    "	    WHERE ie.fecha BETWEEN :fechaIni AND :fechaFin\n" +
                    "	    GROUP BY ie.fecha\n" +
                    "	    UNION ALL\n" +
                    "	    SELECT MAX(f.fecha) AS fecha, COALESCE(SUM(p.monto), 0) AS total_pagos, 0 AS efectivo\n" +
                    "	    FROM factura f\n" +
                    "		LEFT JOIN pago p ON f.id = p.factura\n" +
                    "	    WHERE f.fecha BETWEEN :fechaIni AND :fechaFin AND status IN (1,3,4)\n" +
                    "	    GROUP BY  f.fecha\n" +
                    "	    UNION ALL\n" +
                    "	    SELECT v.fecha ,0 AS total_pagos, sum(total) AS efectivo\n" +
                    "	    FROM venta v\n" +
                    "	    INNER JOIN parametro p ON p.valor = 'true' AND p.nombre = 'SWIZQ'\n" +
                    "	    WHERE v.fecha BETWEEN :fechaIni AND :fechaFin\n" +
                    "	    GROUP BY v.fecha\n" +
                    "	) AS combined\n" +
                    "	WHERE fecha IS NOT NULL\n" +
                    "	GROUP BY combined.fecha\n" +
                    "	ORDER BY fecha\n" +
                    ") AS resultados\n" +
                    "GROUP BY resultados.fecha\n" +
                    "ORDER BY resultados.fecha";

            Query query = entity.createNativeQuery(sql)
                    .setParameter("fechaIni", DateUtil.getString(fechaini, DateUtil.FORMATO_YYYY_MM_DD))
                    .setParameter("fechaFin", DateUtil.getString(fechafin, DateUtil.FORMATO_YYYY_MM_DD));

            List<Object[]> listaObjetos = query.getResultList();

            for (Object[] o : listaObjetos) {
                FacturacionGeneral fg = new FacturacionGeneral();
                int id = 0;
                String fh = ((String) o[id++]);
                Date f = DateUtil.getDate(fh, DateUtil.FORMATO_YYYY_MM);
                fg.setFecha(f);
                fg.setPagosPorMes((BigDecimal) o[id++]);
                lista.add(fg);
            }

        } catch (Exception e) {
            log.error("Error al consultar ventas por mes anual", e);
        } finally {
            EntityManagerUtil.close(entity);
        }
        return lista;
    }

    @SuppressWarnings("unchecked")
    public List<FacturacionGeneral> obtenerVentaDia(Date fechaIni) 
    {
        List<FacturacionGeneral> lista = null;
        EntityManager entity = null;
        String sql = null;

        try {
            entity = EntityManagerUtil.getEntityManager();

            lista = new ArrayList<>();

            sql = "SELECT combined.fecha, SUM(combined.total_facturas) AS total_facturas, SUM(combined.total_efectivo_por_dia) AS total_efectivo_por_dia\n" +
                    "FROM (\n" +
                    "    SELECT fecha , SUM(f.total) AS total_facturas, NULL AS total_efectivo_por_dia\n" +
                    "    FROM factura f \n" +
                    "    WHERE f.fecha = :fechaIni AND status IN (1,3,4)  \n" +
                    "    GROUP BY fecha\n" +
                    "    UNION\n" +
                    "    SELECT fecha, NULL AS total_facturas, SUM(v.total) AS total_efectivo_por_dia\n" +
                    "    FROM venta v\n" +
                    "    INNER JOIN parametro p ON p.valor = 'true' AND p.nombre = 'SWIZQ'\n" +
                    "	WHERE v.fecha = :fechaIni\n" +
                    "	GROUP BY fecha    \n" +
                    ")combined\n" +
                    "GROUP BY combined.fecha  \n" +
                    "ORDER BY combined.fecha";

            Query query = entity.createNativeQuery(sql).setParameter("fechaIni",
                    DateUtil.getString(fechaIni, DateUtil.FORMATO_YYYY_MM_DD));

            List<Object[]> listaObjetos = query.getResultList();

            for (Object[] o : listaObjetos) {
                FacturacionGeneral fg = new FacturacionGeneral();
                int id = 0;

                fg.setFecha((Date) o[id++]);
                fg.setTotal_facturacion((BigDecimal) o[id++]);
                fg.setTotal_por_efectivo((BigDecimal) o[id++]);
                lista.add(fg);
            }

        } catch (Exception e) {
            log.error("Error al consultar venta por dia", e);
        } finally {
            EntityManagerUtil.close(entity);
        }
        return lista;
    }

    @SuppressWarnings("unchecked")
    public List<FacturacionGeneral> ventaPorRazonSocial(Date fechaInicio, Date fechaFin) 
    {
        List<FacturacionGeneral> list = new ArrayList<>();
        EntityManager em = null;
        String sql = null;
        Query query = null;

        try {

            em = EntityManagerUtil.getEntityManager();

            sql = "SELECT SUM(c.total) AS total, c.emisor AS emisor \n" +
                    "FROM (\n" +
                    "    SELECT sum(f.total) AS total, f.emi_nombre AS emisor\n" +
                    "    FROM factura f\n" +
                    "    WHERE f.fecha BETWEEN :fechaIni AND :fechaFin AND f.status in (1,3,4)\n" +
                    "    GROUP BY emisor\n" +
                    "    UNION\n" +
                    "    SELECT sum(v.total) AS total, e.nb_emisor AS emisor\n" +
                    "    FROM venta v\n" +
                    "    INNER JOIN parametro p ON p.valor = 'true' AND p.nombre = 'SWIZQ'\n" +
                    "    INNER JOIN emisor e ON e.cd_emisor = v.id_emisor\n" +
                    "    WHERE v.fecha BETWEEN :fechaIni AND :fechaFin\n" +
                    "    GROUP by emisor\n" +
                    ")c\n" +
                    "GROUP BY emisor";

            query = em.createNativeQuery(sql)
                    .setParameter("fechaIni", DateUtil.getString(fechaInicio, DateUtil.FORMATO_YYYY_MM_DD))
                    .setParameter("fechaFin", DateUtil.getString(fechaFin, DateUtil.FORMATO_YYYY_MM_DD));

            List<Object[]> listaObjetos = query.getResultList();

            for (Object[] o : listaObjetos) {
                FacturacionGeneral fg = new FacturacionGeneral();
                int id = 0;

                fg.setTotal_facturacion((BigDecimal) o[id++]);
                fg.setRazonSocial((String) o[id++]);
                list.add(fg);
            }

        } catch (Exception e) {
            log.error("Error al consultar venta por dia: {}", e);
            return null;
        } finally {
            EntityManagerUtil.close(em);
        }

        return list;
    }

    @SuppressWarnings("unchecked")
    public List<FacturacionGeneral> gananciaPorRazonSocial(Date fechaInicio, Date fechaFin) 
    {
        List<FacturacionGeneral> list = new ArrayList<>();
        EntityManager em = null;
        String sql = null;
        Query query = null;

        try {

            em = EntityManagerUtil.getEntityManager();

            sql = "SELECT SUM(d.total - d.egreso) AS ganancia, d.emisor AS emisor \n" +
                    "FROM (\n" +
                    "    SELECT SUM(c.total) AS total, c.emisor AS emisor, 0 AS egreso \n" +
                    "    FROM(\n" +
                    "        SELECT SUM(f.total) AS total, f.emi_nombre  AS emisor\n" +
                    "        FROM factura f\n" +
                    "        WHERE f.fecha BETWEEN :fechaIni AND :fechaFin AND f.status IN (1,3,4)\n" +
                    "        GROUP BY emisor\n" +
                    "        UNION\n" +
                    "        SELECT SUM(v.total), e.nb_emisor AS emisor\n" +
                    "        FROM venta v\n" +
                    "        INNER JOIN parametro p ON p.valor = 'true' AND p.nombre = 'SWIZQ'\n" +
                    "        INNER JOIN emisor e ON e.cd_emisor = v.id_emisor\n" +
                    "        WHERE v.fecha BETWEEN :fechaIni AND :fechaFin\n" +
                    "        GROUP BY emisor\n" +
                    "    )c\n" +
                    "    GROUP BY emisor\n" +
                    "    UNION\n" +
                    "    SELECT 0 AS total, e.nb_emisor AS emisor, SUM(ie.importe) AS egreso\n" +
                    "    FROM importe_egreso ie\n" +
                    "    INNER JOIN emisor e ON ie.cd_emisor = e.cd_emisor\n" +
                    "    WHERE ie.fecha BETWEEN :fechaIni AND :fechaFin\n" +
                    "    GROUP BY emisor\n" +
                    ")d\n" +
                    "GROUP BY emisor";

            query = em.createNativeQuery(sql)
                    .setParameter("fechaIni", DateUtil.getString(fechaInicio, DateUtil.FORMATO_YYYY_MM_DD))
                    .setParameter("fechaFin", DateUtil.getString(fechaFin, DateUtil.FORMATO_YYYY_MM_DD));

            List<Object[]> listaObjetos = query.getResultList();

            for (Object[] o : listaObjetos) {
                FacturacionGeneral fg = new FacturacionGeneral();
                int id = 0;

                fg.setTotal_facturacion((BigDecimal) o[id++]);
                fg.setRazonSocial((String) o[id++]);
                list.add(fg);
            }

        } catch (Exception e) {
            log.error("Error al consultar ganancia por razon social: {}", e);
            return null;
        } finally {
            EntityManagerUtil.close(em);
        }

        return list;
    }

    @SuppressWarnings("unchecked")
    public List<FacturacionGeneral> ventaPorFormaPago(Date fechaInicio, Date fechaFin) 
    {
        List<FacturacionGeneral> list = new ArrayList<>();
        EntityManager em = null;
        String sql = null;
        Query query = null;

        try {

            em = EntityManagerUtil.getEntityManager();

            sql = "SELECT DATE_FORMAT(c.fecha,'%Y-%m') AS mes, SUM(c.monto), c.tipo AS tipo \n" +
                    "FROM(\n" +
                    "    SELECT p.fecha AS fecha, SUM(p.monto) AS monto, tp.nombre AS tipo \n" +
                    "    FROM factura f\n" +
                    "    INNER JOIN pago p ON p.factura = f.id\n" +
                    "    INNER JOIN tipo_pago tp ON tp.id = p.tipo\n" +
                    "    WHERE p.fecha BETWEEN :fechaIni AND :fechaFin\n" +
                    "	GROUP BY p.tipo, p.fecha\n" +
                    "    UNION\n" +
                    "    SELECT v.fecha AS fecha, v.total AS monto, 'Efectivo' AS tipo\n" +
                    "    FROM venta v\n" +
                    "    INNER JOIN parametro p ON p.valor = 'true' AND p.nombre = 'SWIZQ' \n" +
                    "    WHERE v.fecha BETWEEN :fechaIni AND :fechaFin \n" +
                    ")c \n" +
                    "GROUP BY mes, tipo \n" +
                    "ORDER BY mes";

            query = em.createNativeQuery(sql)
                    .setParameter("fechaIni", DateUtil.getString(fechaInicio, DateUtil.FORMATO_YYYY_MM_DD))
                    .setParameter("fechaFin", DateUtil.getString(fechaFin, DateUtil.FORMATO_YYYY_MM_DD));

            List<Object[]> listaObjetos = query.getResultList();

            for (Object[] o : listaObjetos) {
                FacturacionGeneral fg = new FacturacionGeneral();
                int id = 0;

                String fh = ((String) o[id++]);
                Date f = DateUtil.getDate(fh, DateUtil.FORMATO_YYYY_MM);
                fg.setFecha(f);
                fg.setPagosPorMes((BigDecimal) o[id++]);
                fg.setTipoPago((String) o[id++]);
                list.add(fg);
            }

        } catch (Exception e) {
            log.error("Error al consultar venta por forma de pago: {}", e);
            return null;
        } finally {
            EntityManagerUtil.close(em);
        }

        return list;
    }

}
