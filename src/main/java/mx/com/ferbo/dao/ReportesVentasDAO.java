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
import mx.com.ferbo.util.DateUtil;
import mx.com.ferbo.util.EntityManagerUtil;

public class ReportesVentasDAO extends IBaseDAO<Factura, Integer> {

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

	public List<FacturacionGeneral> desgloseFacturacion(Date fechaIni, Date fechaFin){
		List<FacturacionGeneral> listaFacturacion =null;
		EntityManager em = null;
		String sql = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			listaFacturacion = new ArrayList<>();
			
			sql = "select combined.fecha,sum(combined.total_facturas), sum(combined.total_facturas_por_status) from\n" + 
					"(\n" + 
					"select fecha , SUM(f.total) as total_facturas, null as total_facturas_por_status\n" + 
					"from factura f\n" + 
					"where f.fecha BETWEEN :fechaIni AND :fechaFin\n" + 
					"group by fecha\n" + 
					"\n" + 
					"union\n" + 
					"select fecha , null as total_facturas, SUM(f.total) as total_facturas_por_status\n" + 
					"from factura f\n" + 
					"where f.fecha BETWEEN :fechaIni AND :fechaFin and status in (1,3,4)\n" + 
					"group by fecha\n" + 
					"\n" + 
					")combined\n" + 
					"\n" + 
					"group by combined.fecha\n" +
					"order by combined.fecha ";
			
				Query query = em.createNativeQuery(sql)
					.setParameter("fechaIni", DateUtil.getString(fechaIni, DateUtil.FORMATO_YYYY_MM_DD))
					.setParameter("fechaFin", DateUtil.getString(fechaFin, DateUtil.FORMATO_YYYY_MM_DD));
		
			
			List<Object[]> listaObjetos = query.getResultList();
			 
			
			for(Object[] o: listaObjetos) {
				FacturacionGeneral fg = new FacturacionGeneral();
				int id = 0;
				fg.setFecha((Date) o[id++]);
				fg.setTotal_facturacion((BigDecimal) o[id++]);
				fg.setTotal_por_cobrar_timbradas((BigDecimal) o[id++]);
				listaFacturacion.add(fg);
			}
			return listaFacturacion;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	
		
	}
	/*@SuppressWarnings("unchecked")
	public List<FacturacionGeneral> comparativa(Date fechaIni, Date fechaFin){
		
		List<FacturacionGeneral> lista = null;
		EntityManager entity = null;
		String sql = null;
		
		try {
			entity = EntityManagerUtil.getEntityManager();
			entity.getTransaction().begin();
			
			lista = new ArrayList<>();
			
			sql = "SELECT\n" + 
					"SUM(t.facturacion_total) AS total_facturacion,\n" + 
					"SUM(t.factura_timbrada) AS total_por_cobrar_timbradas,\n" + 
					"SUM(t.factura_efectivo) AS total_por_cobrar_efectivo\n" + 
					"FROM(\n" + 
					"SELECT\n" + 
					"S.facturacion_total,\n" + 
					"S.factura_timbrada,\n" + 
					"0 AS factura_efectivo\n" + 
					"FROM(\n" + 
					"SELECT\n" + 
					"SUM(f.total) as facturacion_total,\n" + 
					"0 as factura_timbrada\n" + 
					"FROM\n" + 
					"factura f\n" + 
					"WHERE f.fecha BETWEEN  :fechaIni AND :fechaFin\n" + 
					"UNION ALL\n" + 
					"SELECT\n" + 
					"0 as facturacion_total,\n" + 
					"SUM(f.total) as factura_timbrada\n" + 
					"FROM\n" + 
					"factura f\n" + 
					"WHERE f.uuid IS NOT NULL AND f.fecha BETWEEN  :fechaIni AND :fechaFin\n" + 
					")S\n" + 
					"\n" + 
					"union all\n" + 
					"\n" + 
					"SELECT\n" + 
					"0 AS facturacion_total,\n" + 
					"0 AS factura_timbrada,\n" + 
					"SUM(f.total) AS factura_efectivo\n" + 
					"FROM\n" + 
					"factura f\n" + 
					"WHERE f.uuid IS NULL AND f.fecha BETWEEN  :fechaIni AND :fechaFin)t\n";

			
					
					Query query = entity.createNativeQuery(sql)
							.setParameter("fechaIni", DateUtil.getString(fechaIni, DateUtil.FORMATO_YYYY_MM_DD))
							.setParameter("fechaFin", DateUtil.getString(fechaFin, DateUtil.FORMATO_YYYY_MM_DD));
				
					
					List<Object[]> listaObjetos = query.getResultList();
					 
					
					for(Object[] o: listaObjetos) {
						FacturacionGeneral fg = new FacturacionGeneral();
						int id = 0;
						fg.setTotal_facturacion((BigDecimal) o[id++]);
						fg.setTotal_por_cobrar_timbradas((BigDecimal) o[id++]);
						fg.setTotal_por_cobrar_efectivo((BigDecimal) o[id++]);
						lista.add(fg);
					}
					
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			entity.close();
		}
		return lista;
	}*/

	
	
}
