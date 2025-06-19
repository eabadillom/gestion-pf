package mx.com.ferbo.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.ui.RepTarimas;
import mx.com.ferbo.util.EntityManagerUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author alberto
 */
public class RepTarimasDAO extends IBaseDAO<RepTarimas, Integer>
{
    private static Logger log = LogManager.getLogger(RepTarimasDAO.class);

    @Override
    public List<RepTarimas> buscarTodos() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<RepTarimas> buscarPorCriterios(RepTarimas e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String eliminarListado(List<RepTarimas> listado) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public List<RepTarimas> buscarPorClientePlanta(Integer idCliente, Integer idPlanta)
    {
        List<RepTarimas> modelList = null;
        EntityManager entity = null;
        String sql = null;
        
        try{
            sql = "select " +
                "	cl.CTE_NOMBRE, " +
                "	pl.PLANTA_DS, " +
                "	sum(tarimas) as tarimas " +
                " from ( " + 
                "	select " +
                "		i1.cte_cve, " +
                "		i1.planta_cve, " +
                "		sum(tarima) as tarimas " +
                "	from ( " +
                "		select " +
                "			cdd.CTE_CVE, " +
                "			plt.planta_cve, " +
                "			CEILING((p.cantidad_total - COALESCE(s.cantidad, 0)) * p.no_tarimas / p.CANTIDAD_TOTAL ) as tarima " +
                "		from constancia_de_deposito cdd " +
                "		inner join partida p on cdd.folio = p.folio " +
                "		inner join camara cam on p.CAMARA_CVE = cam.CAMARA_CVE " +
                "		inner join planta plt on cam.PLANTA_CVE = plt.PLANTA_CVE " +
                "		left outer join ( " +
                "			select " +
                "				dcs.PARTIDA_CVE, " +
                "				sum(dcs.CANTIDAD) as cantidad, " +
                "				sum(dcs.PESO) as peso " +
                "			from detalle_constancia_salida dcs " +
                "			inner join constancia_salida cs on cs.ID = dcs.CONSTANCIA_CVE " +
                "			where cs.status = 1 " +
                "			group by dcs.PARTIDA_CVE " +
                "		) s on p.PARTIDA_CVE = s.partida_cve " +
                "		where cdd.status in (1) " +
                "		and (p.CANTIDAD_TOTAL - COALESCE(s.cantidad, 0)) " +
                "		and p.no_tarimas >= 1.000 and p.cd_tarima is null " +
                "		and (cdd.CTE_CVE = :idCliente or :idCliente is null) " +
                "		and (plt.PLANTA_CVE = :idPlanta or :idPlanta is null) " +
                "		union all " + 
                "		select " +
                "			cdd.CTE_CVE, " +
                "			plt.planta_cve, " +
                "			p.no_tarimas as tarima " +
                "		from constancia_de_deposito cdd " +
                "		inner join partida p on cdd.folio = p.folio " +
                "		inner join camara cam on p.CAMARA_CVE = cam.CAMARA_CVE " +
                "		inner join planta plt on cam.PLANTA_CVE = plt.PLANTA_CVE " +
                "		left outer join ( " +
                "			select " +
                "				dcs.PARTIDA_CVE, " +
                "				sum(dcs.CANTIDAD) as cantidad, " +
                "				sum(dcs.PESO) as peso " +
                "			from detalle_constancia_salida dcs " +
                "			inner join constancia_salida cs on cs.ID = dcs.CONSTANCIA_CVE " +
                "			where cs.status = 1 " +
                "			group by dcs.PARTIDA_CVE " +
                "		) s on p.PARTIDA_CVE = s.partida_cve " +
                "		where cdd.status in (1) " +
                "		and (p.CANTIDAD_TOTAL - COALESCE(s.cantidad, 0)) " +
                "		and p.no_tarimas < 1.000 and p.cd_tarima is null " +
                "		and (cdd.CTE_CVE = :idCliente or :idCliente is null) " +
                "		and (plt.PLANTA_CVE = :idPlanta or :idPlanta is null) " +
                "	) i1 " +
                "	group by i1.cte_cve, i1.planta_cve " +
                "	union all " +
                "	select " +
                "		cdd.CTE_CVE, " +
                "		plt.planta_cve, " +
                "		count(t.cd_tarima) as tarimas " +
                "	from constancia_de_deposito cdd " +
                "	inner join partida p on cdd.FOLIO = p.FOLIO " +
                "	inner join tarima t on p.cd_tarima = t.cd_tarima " +
                "	inner join camara cam on p.CAMARA_CVE = cam.CAMARA_CVE " +
                "	inner join planta plt on cam.PLANTA_CVE = plt.PLANTA_CVE " +
                "	left outer join ( " +
                "		select " +
                "			dcs.PARTIDA_CVE, " +
                "			sum(dcs.CANTIDAD) as cantidad, " +
                "			sum(dcs.PESO) as peso " +
                "		from detalle_constancia_salida dcs " +
                "		inner join constancia_salida cs on cs.ID = dcs.CONSTANCIA_CVE " +
                "		where cs.status = 1 " +
                "		group by dcs.PARTIDA_CVE " +
                "	) s on p.PARTIDA_CVE = s.partida_cve " +
                "	where cdd.status in (1) " +
                "	and (p.CANTIDAD_TOTAL - COALESCE(s.cantidad, 0)) > 0 " +
                "	and p.cd_tarima is not null " +
                "	and (cdd.CTE_CVE = :idCliente or :idCliente is null) " +
                "	and (plt.PLANTA_CVE = :idPlanta or :idPlanta is null) " +
                "	group by cdd.CTE_CVE, plt.PLANTA_CVE " +
                ") i " +
                " inner join cliente cl on cl.CTE_CVE = i.cte_cve " +
                " inner join planta pl on pl.PLANTA_CVE = i.planta_cve " +
                " group by i.cte_cve, i.planta_cve " +
                " order by cl.CTE_NOMBRE asc, pl.PLANTA_DS asc";
            
            entity = EntityManagerUtil.getEntityManager();
            
            
            List<Object[]> resultados = entity.createNativeQuery(sql)
                .setParameter("idCliente", idCliente)
                .setParameter("idPlanta", idPlanta)
                .getResultList();
            
            modelList = new ArrayList<>();
            
            for(Object[] fila : resultados)
            {
                int idx = 0;
                RepTarimas tarima = new RepTarimas();
                tarima.setNombreCliente(((String) fila[idx++]));
                tarima.setNombrePlanta(((String) fila[idx++]));
                tarima.setNumeroTarimas(((Number) fila[idx++]).intValue());
                modelList.add(tarima);
            }
        }catch(Exception ex) {
            log.error("Problema para obtener el reporte de Tarimas...", ex);
        } finally {
            EntityManagerUtil.close(entity);
        }
        
        return modelList;
    }
    
}
