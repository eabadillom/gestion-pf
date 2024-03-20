package mx.com.ferbo.dao;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.Factura;
import mx.com.ferbo.model.FacturacionGeneral;
import mx.com.ferbo.model.VentasGlobales;
import mx.com.ferbo.ui.importeUtilidad;
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

	/*public VentasGlobales ventasGanancias(Date fechaIni, Date fechaFin) {
		
		VentasGlobales ventas =null;
		EntityManager em = null;
		String sql = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			//em.getTransaction().begin();
			sql = "select a.ventas_totales ,(a.ventas_totales - a.egresos) as ganancias,((a.ventas_totales - a.egresos) / a.ventas_totales) as porcentaje from(\n " + 
					"select SUM(combined.total_facturas), SUM(combined.total_ventas), sum(combined.total_facturas + combined.total_ventas ) as ventas_totales, sum(combined.Total_egresos) as egresos\n " + 
					"from (\n " + 
					"select sum(f.total) as total_facturas,0 as total_ventas , 0 as Total_egresos, 0 as ventas_totales\n " + 
					"from factura f\n " + 
					"where f.fecha BETWEEN :fechaini and :fechaFin and status in (1,3,4)\n " + 
					"group by fecha\n " + 
					"\n" + 
					"UNION\n " + 
					"\n" + 
					"select 0 as total_facturas,sum(v.total) as total_ventas , 0 as Total_egresos, 0 as ventas_totales\n " + 
					"from ventas v\n " + 
					"where v.fecha BETWEEN :fechaini AND :fechaFin\n " + 
					"group by fecha\n " + 
					"UNION\n" + 
					"select 0 as total_facturas,0 as total_ventas , sum(ie.importe) as Total_egresos, 0 as ventas_totales\n " + 
					"from importe_egreso ie\n " + 
					"where ie.fecha BETWEEN :fechaini AND :fechaFin\n " + 
					"group by fecha\n " + 
					"\n" + 
					")combined\n " + 
					") a\n ";

			Query query = em.createNativeQuery(sql)
					.setParameter("fechaini",DateUtil.getString(fechaIni, DateUtil.FORMATO_YYYY_MM_DD))
					.setParameter("fechaFin", DateUtil.getString(fechaFin, DateUtil.FORMATO_YYYY_MM_DD));
			
			ventas = (VentasGlobales) query.getSingleResult();
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return ventas;	
		
	}*/
	
	@SuppressWarnings("unchecked")
	public List<VentasGlobales> ventasGanancias(Date fechaIni, Date fechaFin){
		List<VentasGlobales> listaVentas =null;
		EntityManager em = null;
		String sql = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			listaVentas = new ArrayList<>();
			
			sql = "select a.ventas_totales ,(a.ventas_totales - a.egresos) as ganancias,((a.ventas_totales - a.egresos) / a.ventas_totales) as porcentaje_ganancia from(\n" + 
					"select SUM(combined.total_facturas), SUM(combined.total_ventas), sum(combined.total_facturas + combined.total_ventas ) as ventas_totales, sum(combined.Total_egresos) as egresos\n" + 
					"from (\n" + 
					"select sum(f.total) as total_facturas,0 as total_ventas , 0 as Total_egresos, 0 as ventas_totales\n" + 
					"from factura f\n" + 
					"where f.fecha BETWEEN :fechaini and :fechaFin and status in (1,3,4)\n" + 
					"group by fecha\n" + 
					"\n" + 
					"UNION\n" + 
					"\n" + 
					"select 0 as total_facturas,sum(v.total) as total_ventas , 0 as Total_egresos, 0 as ventas_totales\n" + 
					"from ventas v\n" + 
					"where v.fecha BETWEEN :fechaini AND :fechaFin\n" + 
					"group by fecha\n" + 
					"UNION\n" + 
					"select 0 as total_facturas,0 as total_ventas , sum(ie.importe) as Total_egresos, 0 as ventas_totales\n" + 
					"from importe_egreso ie\n" + 
					"where ie.fecha BETWEEN :fechaini AND :fechaFin\n" + 
					"group by fecha\n" + 
					"\n" + 
					")combined\n" + 
					") a\n";
			
				Query query = em.createNativeQuery(sql)
					.setParameter("fechaini", DateUtil.getString(fechaIni, DateUtil.FORMATO_YYYY_MM_DD))
					.setParameter("fechaFin", DateUtil.getString(fechaFin, DateUtil.FORMATO_YYYY_MM_DD));
		
			
			List<Object[]> listaObjetos = query.getResultList();
			 
			
			int id = 0;
			for(Object[] o: listaObjetos) {
				VentasGlobales vg = new VentasGlobales();
				vg.setVentasTotales((BigDecimal) o[id++]);
				vg.setGanancias((BigDecimal) o[id++]);
				vg.setPorcentajeGanancias((BigDecimal) o[id++]);
				listaVentas.add(vg);
			}
			return listaVentas;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	
		
	}
	
	@SuppressWarnings("unchecked")
	public List<FacturacionGeneral> desgloseFacturacion(Date fechaIni, Date fechaFin){
		List<FacturacionGeneral> listaFacturacion =null;
		EntityManager em = null;
		String sql = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			listaFacturacion = new ArrayList<>();
			
			sql = "select combined.fecha, sum(combined.total_facturas),SUM(combined.total_efectivo_por_dia) from\n" + 
					"(\n" + 
					"\n" + 
					"\n" + 
					"select fecha , SUM(f.total) as total_facturas,null as  total_efectivo_por_dia\n" + 
					"from factura f\n" + 
					"where f.fecha BETWEEN :fechaIni AND :fechaFin and status in (1,3,4)\n" + 
					"\n" + 
					"group by fecha\n" + 
					"\n" + 
					"union\n" + 
					"\n" + 
					"select fecha,null as total_facturas, sum(v.total) as total_efectivo_por_dia\n" + 
					"from ventas v\n" + 
					"inner join parametro p on p.valor = 'true'\n" + 
					"where v.fecha BETWEEN :fechaIni AND :fechaFin\n" + 
					"\n" + 
					"group by fecha\n" + 
					"\n" + 
					")combined\n" + 
					"group by combined.fecha\n" + 
					"order by combined.fecha DESC\n" + 
					"\n" + 
					"\n";
			
				Query query = em.createNativeQuery(sql)
					.setParameter("fechaIni", DateUtil.getString(fechaIni, DateUtil.FORMATO_YYYY_MM_DD))
					.setParameter("fechaFin", DateUtil.getString(fechaFin, DateUtil.FORMATO_YYYY_MM_DD));
		
			
			List<Object[]> listaObjetos = query.getResultList();
			 
			
			for(Object[] o: listaObjetos) {
				FacturacionGeneral fg = new FacturacionGeneral();
				int id = 0;
				fg.setFecha((Date) o[id++]);
				fg.setTotal_facturacion((BigDecimal) o[id++]);
				fg.setTotal_por_efectivo((BigDecimal) o[id++]);
				listaFacturacion.add(fg);
			}
			return listaFacturacion;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	
		
	}
	
	@SuppressWarnings("unchecked")
	public List<importeUtilidad> UtilidadPorMesAnual(String fechaIni, String fechaFin){
		List<importeUtilidad> lista = null;
		EntityManager entity = null;
		String sql = null;
		
		try {
			entity = EntityManagerUtil.getEntityManager();
			entity.getTransaction().begin();
			
			lista = new ArrayList<>();
			
			sql="\n" + 
					"SELECT DATE_FORMAT(combined.fecha, '%Y-%m') AS fecha,\n" + 
					"SUM(combined.total_pagos) AS pagos,\n" + 
					"SUM(combined.total_egresos) AS egresos,\n" + 
					"SUM(combined.total_pagos - combined.total_egresos) AS utilidad_perdida\n" + 
					"FROM (\n" + 
					"SELECT\n" + 
					"ie.fecha,\n" + 
					"COALESCE(SUM(ie.importe), 0) AS total_egresos,\n" + 
					"0 AS total_pagos\n" + 
					"FROM\n" + 
					"emisor e\n" + 
					"LEFT JOIN importe_egreso ie ON e.cd_emisor = ie.cd_emisor\n" + 
					"AND DATE_FORMAT(ie.fecha, '%Y-%m') BETWEEN  DATE_FORMAT(:fechaIni, '%Y-%m') and DATE_FORMAT(:fechaFin, '%Y-%m')\n" + 
					"GROUP BY\n" + 
					"ie.fecha\n" + 
					"\n" + 
					"UNION\n" + 
					"\n" + 
					"SELECT\n" + 
					"MAX(p.fecha) AS fecha,\n" + 
					"0 AS total_egresos,\n" + 
					"COALESCE(SUM(p.monto), 0) AS total_pagos\n" + 
					"FROM\n" + 
					"factura f\n" + 
					"LEFT JOIN pago p ON f.id = p.factura\n" + 
					"AND DATE_FORMAT(f.fecha, '%Y-%m') BETWEEN  DATE_FORMAT(:fechaIni, '%Y-%m') and DATE_FORMAT(:fechaFin, '%Y-%m')\n" + 
					"GROUP BY\n" + 
					"f.fecha\n" + 
					") AS combined\n" + 
					"where fecha  is not null \n" +
					"GROUP BY\n" + 
					"DATE_FORMAT(combined.fecha, '%Y-%m')\n" + 
					"ORDER BY\n" + 
					"fecha\n";
			
			Query query = entity.createNativeQuery(sql)
					.setParameter("fechaIni", fechaIni)
					.setParameter("fechaFin", fechaFin);
					
					List<Object[]> listaObjetos = query.getResultList();
					
					
					for(Object[] o: listaObjetos) {
						importeUtilidad u = new importeUtilidad();
						int id = 0;
						String fh = ((String) o[id++]);
						Date f = DateUtil.getDate(fh, DateUtil.FORMATO_YYYY_MM);
						u.setFecha(f);
						u.setPagos((BigDecimal) o[id++]);
						u.setEgresos((BigDecimal) o[id++] );
						u.setUtilidadPerdida((BigDecimal) o[id++]);
						lista.add(u);
					}
					
		}catch (Exception e) {
			e.printStackTrace();
		}
		return lista;
	}
	
	
	
	
	
	@SuppressWarnings("unchecked")
	public List<FacturacionGeneral> obtenerVentaDia(Date fechaIni){
		
		List<FacturacionGeneral> lista = null;
		EntityManager entity = null;
		String sql = null;
		
		try {
			entity = EntityManagerUtil.getEntityManager();
			entity.getTransaction().begin();
			
			lista = new ArrayList<>();
			
			sql ="select combined.fecha,sum(combined.total_facturas),sum(combined.total_efectivo_por_dia)\n" + 
					"\n" + 
					"from\n" + 
					"(\n" + 
					"select fecha , SUM(f.total) as total_facturas, null as total_efectivo_por_dia\n" + 
					"from factura f\n" + 
					"where f.fecha = :fechaIni and status in (1,3,4)\n" + 
					"group by fecha\n" + 
					"\n" + 
					"union\n" + 
					"select fecha ,null as total_facturas, SUM(v.total) as total_efectivo_por_dia\n" + 
					"from ventas v\n" + 
					"where v.fecha = :fechaIni\n" + 
					"group by fecha\n" + 
					"\n" + 
					")combined\n" + 
					"\n" + 
					"group by combined.fecha\n" + 
					"order by combined.fecha\n";
			
					
					Query query = entity.createNativeQuery(sql)
							.setParameter("fechaIni", DateUtil.getString(fechaIni, DateUtil.FORMATO_YYYY_MM_DD));
				
					
					List<Object[]> listaObjetos = query.getResultList();
					 
					
					for(Object[] o: listaObjetos) {
						FacturacionGeneral fg = new FacturacionGeneral();
						int id = 0;
						
						fg.setFecha((Date) o[id++]);
						fg.setTotal_facturacion((BigDecimal) o[id++]);
						fg.setTotal_por_efectivo((BigDecimal) o[id++]);
						lista.add(fg);
					}
					
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			entity.close();
		}
		return lista;
	}

	
	
}
