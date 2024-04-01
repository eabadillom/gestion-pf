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

	@SuppressWarnings("unchecked")
	public List<VentasGlobales> ventasGanancias(Date fechaIni, Date fechaFin) {
		List<VentasGlobales> listaVentas = null;
		EntityManager em = null;
		String sql = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			listaVentas = new ArrayList<>();

			sql = "select a.ventas_totales ,(a.ventas_totales - a.egresos) as ganancias,((a.ventas_totales - a.egresos) / a.ventas_totales) as porcentaje_ganancia from(\n"
					+ "select SUM(combined.total_facturas), SUM(combined.total_ventas), sum(combined.total_facturas + combined.total_ventas ) as ventas_totales, sum(combined.Total_egresos) as egresos\n"
					+ "from (\n"
					+ "select sum(f.total) as total_facturas,0 as total_ventas , 0 as Total_egresos, 0 as ventas_totales\n"
					+ "from factura f\n" + "where f.fecha BETWEEN :fechaini and :fechaFin and status in (1,3,4)\n"
					+ "group by fecha\n" + "\n" + "UNION\n" + "\n"
					+ "select 0 as total_facturas,sum(v.total) as total_ventas , 0 as Total_egresos, 0 as ventas_totales\n"
					+ "from venta v\n" 
					+"inner join parametro p on p.valor = 'true' and p.nombre = 'SWIZQ' \n " 
					+ "where v.fecha BETWEEN :fechaini AND :fechaFin \n " 
					+ "group by fecha \n"
					+ "UNION \n"
					+ "select 0 as total_facturas, 0 as total_ventas , sum(ie.importe) as Total_egresos, 0 as ventas_totales \n"
					+ "from importe_egreso ie\n" + "where ie.fecha BETWEEN :fechaini AND :fechaFin\n"
					+ "group by fecha \n" + "\n" + ")combined \n" + ")a \n";

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
			e.printStackTrace();
			return null;
		}

	}

	@SuppressWarnings("unchecked")
	public List<FacturacionGeneral> desgloseFacturacion(Date fechaIni, Date fechaFin) {
		List<FacturacionGeneral> listaFacturacion = null;
		EntityManager em = null;
		String sql = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			listaFacturacion = new ArrayList<>();

			sql = "select combined.fecha, sum(combined.total_facturas),SUM(combined.total_efectivo_por_dia) from\n"
					+ "(\n" + "\n" + "\n"
					+ "select fecha , SUM(f.total) as total_facturas,null as  total_efectivo_por_dia\n"
					+ "from factura f\n" + "where f.fecha BETWEEN :fechaIni AND :fechaFin and status in (1,3,4)\n"
					+ "\n" + "group by fecha\n" + "\n" + "union\n" + "\n"
					+ "select fecha,null as total_facturas, sum(v.total) as total_efectivo_por_dia\n"
					+ "from venta v\n" 
					+"inner join parametro p on p.valor = 'true' and p.nombre = 'SWIZQ' \n " 
					+ "where v.fecha BETWEEN :fechaIni AND :fechaFin \n" 
					+ "group by fecha \n" 
					+ ")combined \n" + "group by combined.fecha \n" + "order by combined.fecha DESC \n" + "\n" + "\n";

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
			e.printStackTrace();
			return null;
		}

	}

	@SuppressWarnings("unchecked")
	public List<importeUtilidad> UtilidadPorMesAnual(String fechaIni, String fechaFin) {
		List<importeUtilidad> lista = null;
		EntityManager entity = null;
		String sql = null;

		try {
			entity = EntityManagerUtil.getEntityManager();
			entity.getTransaction().begin();

			lista = new ArrayList<>();

			sql = "SELECT DATE_FORMAT(combined.fecha, '%Y-%m') AS fecha,\n" + "SUM(combined.total_pagos) AS pagos,\n"
					+ "SUM(combined.total_egresos) AS egresos,\n"
					+ "SUM(combined.total_pagos - combined.total_egresos) AS utilidad_perdida,\n"
					+ "Sum(combined.efectivo) as izq\n" + "FROM (\n"
					+ "SELECT ie.fecha,  COALESCE(SUM(ie.importe), 0) AS total_egresos,  0 AS total_pagos , 0 as efectivo\n"
					+ "FROM  emisor e\n" + "LEFT JOIN importe_egreso ie ON e.cd_emisor = ie.cd_emisor\n"
					+ "AND DATE_FORMAT(ie.fecha, '%Y-%m') BETWEEN  DATE_FORMAT(:fechaIni, '%Y-%m') and DATE_FORMAT(:fechaFin, '%Y-%m')\n"
					+ "GROUP BY  ie.fecha\n" + "\n" + "UNION\n" + "\n" + "SELECT\n"
					+ "MAX(p.fecha) AS fecha, 0 AS total_egresos, COALESCE(SUM(p.monto), 0) AS total_pagos , 0 as efectivo\n"
					+ "FROM  factura f\n" + "LEFT JOIN pago p ON f.id = p.factura\n" + "and status in (1,3,4)\n"
					+ "AND DATE_FORMAT(f.fecha, '%Y-%m') BETWEEN  DATE_FORMAT(:fechaIni, '%Y-%m') and DATE_FORMAT(:fechaFin, '%Y-%m')\n"
					+ "GROUP BY  f.fecha\n" + "\n" + "UNION\n" + "\n"
					+ "SELECT v.fecha,0 as total_egresos, 0 as total_pagos,sum(total) as efectivo\n" + "FROM venta v\n "
					+"inner join parametro p on p.valor = 'true' and p.nombre = 'SWIZQ' \n " 
					+ "GROUP BY  v.fecha\n" + ") AS combined\n" + "where fecha  is not null\n" + "GROUP BY\n"
					+ "DATE_FORMAT(combined.fecha, '%Y-%m')\n" + "ORDER BY  fecha;\n";

			Query query = entity.createNativeQuery(sql).setParameter("fechaIni", fechaIni).setParameter("fechaFin",
					fechaFin);

			List<Object[]> listaObjetos = query.getResultList();

			for (Object[] o : listaObjetos) {
				importeUtilidad u = new importeUtilidad();
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
			e.printStackTrace();
		}
		return lista;
	}   
  
	@SuppressWarnings("unchecked")
	public List<FacturacionGeneral> ventasPorMesAnual(Date fechaini, Date fechafin) {
		List<FacturacionGeneral> lista = null;
		EntityManager entity = null;
		String sql = null;
    
		try {
			entity = EntityManagerUtil.getEntityManager();
			entity.getTransaction().begin();

			lista = new ArrayList<>();

			sql = "SELECT DATE_FORMAT(combined.fecha, '%Y-%m') AS fecha,\n" + 
					"SUM(combined.total_pagos + combined.efectivo) AS pagos \n" + 
					"FROM (\n" + 
					"SELECT ie.fecha,  0 AS total_pagos , 0 as efectivo \n" + 
					"FROM  importe_egreso ie \n" + 
					"WHERE DATE_FORMAT(ie.fecha, '%Y-%m') BETWEEN  DATE_FORMAT(:fechaIni, '%Y-%m') and DATE_FORMAT(:fechaFin, '%Y-%m')\n" + 
					"GROUP BY  ie.fecha\n" + 
					"\n" + 
					"UNION\n" + 
					"\n" + 
					"SELECT\n" + 
					"MAX(p.fecha) AS fecha, COALESCE(SUM(p.monto), 0) AS total_pagos , 0 as efectivo\n" + 
					"FROM  factura f\n" + 
					"LEFT JOIN pago p ON f.id = p.factura\n" + 
					"and status in (1,3,4)\n" + 
					"AND DATE_FORMAT(f.fecha, '%Y-%m') BETWEEN  DATE_FORMAT(:fechaIni, '%Y-%m') and DATE_FORMAT(:fechaFin, '%Y-%m')\n" + 
					"GROUP BY  f.fecha\n" + 
					"\n" + 
					"UNION\n" + 
					"\n" + 
					"SELECT v.fecha ,0 AS total_pagos, sum(total) as efectivo\n" + 
					"FROM venta v\n" + 
					"inner join parametro p on p.valor = 'true' and p.nombre = 'SWIZQ' \n " +
					"GROUP BY  v.fecha\n" + 
					"\n" + 
					") AS combined\n" + 
					"where fecha  is not null\n" + 
					"GROUP BY\n" + 
					"DATE_FORMAT(combined.fecha, '%Y-%m')\n" + 
					"ORDER BY  fecha;\n";

			Query query = entity.createNativeQuery(sql)
					.setParameter("fechaIni",DateUtil.getString(fechaini, DateUtil.FORMATO_YYYY_MM_DD))
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
			e.printStackTrace();
		} finally {
			entity.close();
		}
		return lista;
	}

	@SuppressWarnings("unchecked")
	public List<FacturacionGeneral> obtenerVentaDia(Date fechaIni) {

		List<FacturacionGeneral> lista = null;
		EntityManager entity = null;
		String sql = null;

		try {
			entity = EntityManagerUtil.getEntityManager();
			entity.getTransaction().begin();

			lista = new ArrayList<>();

			sql = "select combined.fecha,sum(combined.total_facturas),sum(combined.total_efectivo_por_dia)\n" + 
					"from  (\n" + 
					"select fecha , SUM(f.total) as total_facturas, null as total_efectivo_por_dia\n" + 
					"from factura f  where f.fecha = :fechaIni and status in (1,3,4)  group by fecha\n" + 
					"union\n" + 
					"select fecha ,null as total_facturas, SUM(v.total) as total_efectivo_por_dia\n" + 
					"from venta v\n" + 
					"inner join parametro p on p.valor = 'true' and p.nombre = 'SWIZQ' \n " +
					"where v.fecha = :fechaIni  group by fecha    )combined\n" + 
					"group by combined.fecha  order by combined.fecha;\n";

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
			e.printStackTrace();
		} finally {
			entity.close();
		}
		return lista;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<FacturacionGeneral> ventaPorRazonSocial(Date fechaInicio, Date fechaFin ){
		
		List<FacturacionGeneral> list = new ArrayList<>();
		EntityManager em = null;
		String sql = null;
		Query query = null;
		
		try {
			
			em = EntityManagerUtil.getEntityManager();
			
			sql = "select SUM(c.total) as total, c.emisor as emisor from(\n" + 
					"select sum(f.total) as total, f.emi_nombre as emisor\n" + 
					"from factura f\n" + 
					"where f.fecha between :fechaini and :fechafin and f.status in (1,3,4)\n" + 
					"group by emisor\n" + 
					"\n" + 
					"union\n" + 
					"\n" + 
					"select sum(v.total) as total, e.nb_emisor as emisor\n" + 
					"from venta v\n" + 
					"inner join parametro p on p.valor = 'true' and p.nombre = 'SWIZQ' \n " + 
					"inner join emisor e on e.cd_emisor = v.id_emisor \n" + 
					"where v.fecha between :fechaini and :fechafin \n" + 
					"GROUP by emisor \n " + 
					")c \n" + 
					"GROUP BY emisor \n ";
			
			query = em.createNativeQuery(sql)
						.setParameter("fechaini",DateUtil.getString(fechaInicio, DateUtil.FORMATO_YYYY_MM_DD))
						.setParameter("fechafin",DateUtil.getString(fechaFin, DateUtil.FORMATO_YYYY_MM_DD));
			
			List<Object[]> listaObjetos = query.getResultList();
			
			for (Object[] o : listaObjetos) {
				FacturacionGeneral fg = new FacturacionGeneral();
				int id = 0;

				fg.setTotal_facturacion((BigDecimal) o[id++]);				
				fg.setRazonSocial((String) o[id++]);
				list.add(fg);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}finally {
			EntityManagerUtil.close(em);
		}
				
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<FacturacionGeneral> gananciaPorRazonSocial(Date fechaInicio, Date fechaFin ){
		
		List<FacturacionGeneral> list = new ArrayList<>();
		EntityManager em = null;
		String sql = null;
		Query query = null;
		
		try {
			
			em = EntityManagerUtil.getEntityManager();
			
			sql = "select sum(d.total - d.egreso) as ganancia, d.emisor as emisor from(\n" + 
					"select SUM(c.total) as total, c.emisor as emisor, 0 as egreso from(\n" + 
					"select sum(f.total) as total, f.emi_nombre as emisor\n" + 
					"from factura f\n" + 
					"where f.fecha between :fechaini and :fechafin and f.status in (1,3,4)\n" + 
					"group by emisor\n" + 
					"\n" + 
					"union\n" + 
					"\n" + 
					"select sum(v.total), e.nb_emisor as emisor\n" + 
					"from venta v\n" + 
					"inner join parametro p on p.valor = 'true' and p.nombre = 'SWIZQ' \n "  + 
					"inner join emisor e on e.cd_emisor = v.id_emisor\n" + 
					"where v.fecha between :fechaini and :fechafin\n" + 
					"GROUP by emisor\n" + 
					"\n" + 
					")c\n" + 
					"GROUP BY emisor\n" + 
					"\n" + 
					"\n" + 
					"union\n" + 
					"\n" + 
					"select 0 as total , e.nb_emisor as emisor,sum(ie.importe) as egreso\n" + 
					"from importe_egreso ie\n" + 
					"inner join emisor e on ie.cd_emisor = e.cd_emisor\n" + 
					"where ie.fecha between :fechaini and :fechafin\n" + 
					"group by emisor\n" + 
					"\n" + 
					")d\n" + 
					"group by emisor\n";
			
			query = em.createNativeQuery(sql)
						.setParameter("fechaini",DateUtil.getString(fechaInicio, DateUtil.FORMATO_YYYY_MM_DD))
						.setParameter("fechafin",DateUtil.getString(fechaFin, DateUtil.FORMATO_YYYY_MM_DD));
			
			List<Object[]> listaObjetos = query.getResultList();
			
			for (Object[] o : listaObjetos) {
				FacturacionGeneral fg = new FacturacionGeneral();
				int id = 0;

				fg.setTotal_facturacion((BigDecimal) o[id++]);				
				fg.setRazonSocial((String) o[id++]);
				list.add(fg);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}finally {
			EntityManagerUtil.close(em);
		}
				
		return list;
	}
	
	
@SuppressWarnings("unchecked")
public List<FacturacionGeneral> ventaPorFormaPago(Date fechaInicio, Date fechaFin ){
		
		List<FacturacionGeneral> list = new ArrayList<>();
		EntityManager em = null;
		String sql = null;
		Query query = null;
		
		try {
			
			em = EntityManagerUtil.getEntityManager();
			
			sql = "select date_format(c.fecha,'%Y-%m') as mes, sum(c.monto), c.tipo as tipo  from(\n" + 
					"SELECT p.fecha as fecha,sum(p.monto) as monto, tp.nombre as tipo from factura f\n" + 
					"inner join pago p on p.factura = f.id\n" + 
					"inner join tipo_pago tp on tp.id = p.tipo\n" + 
					"where p.fecha BETWEEN :fechaini and :fechafin\n" + 
					"group by p.tipo, p.fecha\n" + 
					"union\n" + 
					"select v.fecha as fecha, v.total as monto, 'Efectivo' as tipo\n" + 
					"from venta v\n" + 
					"inner join parametro p on p.valor = 'true' and p.nombre = 'SWIZQ' \n " + 
					"where v.fecha between :fechaini and :fechafin \n " + 
					")c \n" + 
					"Group by mes, tipo \n" + 
					"order by mes\n";
			
			query = em.createNativeQuery(sql)
						.setParameter("fechaini",DateUtil.getString(fechaInicio, DateUtil.FORMATO_YYYY_MM_DD))
						.setParameter("fechafin",DateUtil.getString(fechaFin, DateUtil.FORMATO_YYYY_MM_DD));
			
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
			e.printStackTrace();
			return null;
		}finally {
			EntityManagerUtil.close(em);
		}
				
		return list;
	}
	
	
	
	
	
	
	

}
