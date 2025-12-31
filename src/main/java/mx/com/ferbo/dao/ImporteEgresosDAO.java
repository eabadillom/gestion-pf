package mx.com.ferbo.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.ImporteEgreso;
import mx.com.ferbo.ui.ImporteUtilidad;
import mx.com.ferbo.util.DateUtil;
import mx.com.ferbo.util.EntityManagerUtil;

/**
 *
 * @author alberto
 */
@Deprecated
public class ImporteEgresosDAO extends IBaseDAO<ImporteEgreso, Integer>
{
    private static Logger log = LogManager.getLogger(ImporteEgresosDAO.class);

    @Override
    public ImporteEgreso buscarPorId(Integer id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<ImporteEgreso> buscarTodos() 
    {
        List<ImporteEgreso> listaEgresos = null;
        EntityManager entity = null;

        try {
            entity = EntityManagerUtil.getEntityManager();
            listaEgresos = entity.createNamedQuery("ImporteEgreso.findByAll", ImporteEgreso.class).getResultList();
        } catch (Exception e) {
            log.error("Error al obtener informacion", e);
        } finally {
            EntityManagerUtil.close(entity);
        }
        return listaEgresos;
    }
    
    public List<ImporteEgreso> buscarPorEmisorYFechas(String emisor, Date fechaInicio, Date fechaFin)
    {
        List<ImporteEgreso> listaEgresos = null;
        EntityManager entity = null;
        
        try{
            entity = EntityManagerUtil.getEntityManager();
            listaEgresos = entity.createNamedQuery("ImporteEgreso.findByParametros", ImporteEgreso.class)
                .setParameter("emisor", emisor)
                .setParameter("fechaInicio", fechaInicio)
                .setParameter("fechaFin", fechaFin)
                .getResultList();
        } catch (Exception e) {
            log.error("Error al obtener informacion", e);
        } finally {
            EntityManagerUtil.close(entity);
        }
        
        return listaEgresos;
    }

    @SuppressWarnings("unchecked")
    public List<ImporteUtilidad> obtenerUtilidadPorEmisor(String emisor, Date fecha) 
    {
        List<ImporteUtilidad> lista = null;
        EntityManager entity = null;
        String sql = null;

        try {
            entity = EntityManagerUtil.getEntityManager();

            lista = new ArrayList<>();

            sql = "SELECT  " + "    combined.emi_nombre AS emi_nombre, "
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

            Query query = entity.createNativeQuery(sql).setParameter("EMISOR", emisor).setParameter("fecha", fecha);

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
                lista.add(u);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            EntityManagerUtil.close(entity);
        }
        return lista;
    }

    @Override
    public List<ImporteEgreso> buscarPorCriterios(ImporteEgreso e) {

        return null;
    }

    @Override
    public String actualizar(ImporteEgreso e) {

        return null;
    }

    @Override
    public String guardar(ImporteEgreso ie) {
        EntityManager entity = null;
        try {
            entity = EntityManagerUtil.getEntityManager();
            entity.getTransaction().begin();
            entity.persist(ie);
            entity.getTransaction().commit();
            entity.close();
        } catch (Exception e) {
            return e.getMessage();
        } finally {
            EntityManagerUtil.close(entity);
        }
        return null;
    }

    @Override
    public String eliminar(ImporteEgreso e) {
        return null;
    }

    @Override
    public String eliminarListado(List<ImporteEgreso> listado) {
        return null;
    }
    
}
