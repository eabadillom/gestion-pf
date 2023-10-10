package mx.com.ferbo.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.log4j.Logger;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.ui.RepEstadoCuenta;
import mx.com.ferbo.util.EntityManagerUtil;

public class RepEstadoCuentaDAO extends IBaseDAO<RepEstadoCuenta, Integer> {
private static Logger log = Logger.getLogger(RepEstadoCuentaDAO.class);

	@Override
	public RepEstadoCuenta buscarPorId(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<RepEstadoCuenta> buscarTodos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<RepEstadoCuenta> buscarPorCriterios(RepEstadoCuenta e) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<RepEstadoCuenta> listaEstadoCuenta( Date fecha, BigDecimal ventas, BigDecimal pagos, BigDecimal saldoInicial, String emisor){
		List<RepEstadoCuenta> listaEstadoCuenta = null;
		EntityManager em = null;
		String sql = null;
		try {
			sql = "SELECT "
									+" fecha, "
									+"sum(ventas) as ventas, "
									+" sum(pagos) as pagos, "
									+" saldo_inicial,"
									+" estadoCuenta."
									+" emisor"
									+" FROM "
									+" (SELECT "
									+" f.fecha, "
									+" 'factura' as Tipo,"
									+" f.total as ventas,"
									+" 0 as pagos,"
									+" FROM factura f"
									+" WHERE f.fecha BETWEEN $P{fechaIni} AND $P{fechaFin} AND emi_nombre =  $P{emisorN} OR $P{emisorN} IS NULL"
									+" UNION ALL"
									+"SELECT "
									+"p.fecha, "
									+"'pago' as Tipo,"
									+"0 as ventas,"
									+"p.monto as pagos, "
									+"FROM pago p"
									+"RIGHT JOIN factura f ON p.factura = f.id"
									+"WHERE p.fecha BETWEEN $P{fechaIni} "
									+ "AND $P{fechaFin} "
									+ "AND (f.emi_nombre = $P{emisorN}   OR $P{emisorN} IS NULL )"
									+") b "
									+"JOIN ( SELECT SUM(saldo) as saldo_inicial, emisor"
									+"FROM ( "
									+ "SELECT "
									+"(factura.total - COALESCE(pago.monto, 0)) as saldo, "
									+"factura.emi_nombre as emisor, "
									+"FROM factura "
									+" LEFT JOIN pago ON factura.id  = pago.factura "
									+"WHERE status IN ('1', '3', '4') "
									+"AND emi_rfc IS NOT NULL "
									+"AND (factura.emi_nombre = $P{emisorN} OR $P{emisorN} IS NULL )"
									+"AND factura.fecha < $P{fechaIni} "
									+"GROUP BY  "
									+"emisor,"
									+"saldo, "
									+"ORDER BY "
									+ "emisor"
									+ ")saldoInicial "
									+ "GROUP by "
									+ "emisor"
									+ ")estadoCuenta"
									+ "GROUP BY "
									+ "fecha,saldo_inicial,"
									+ "emisor "
									+ "ORDER BY "
									+ "estadoCuenta."
									+ "emisor, "
									+ "b.fecha ";
					em = EntityManagerUtil.getEntityManager();
				    List<Object[]> results = em.createNativeQuery(sql)
				    		.setParameter("fecha", fecha)
							.setParameter("ventas", ventas)
							.setParameter("pagos", pagos)
							.setParameter("saldoInicial", saldoInicial)
							.setParameter("emisor", emisor)
							.getResultList()
							;
				    listaEstadoCuenta = new ArrayList<RepEstadoCuenta>();
					for(Object[] o : results) {
						RepEstadoCuenta rec = new RepEstadoCuenta();
						int idx = 0 ;
						rec.setFecha((Date) o[idx++]);
						rec.setVentas((BigDecimal) o[idx++]);
						rec.setPagos((BigDecimal) o[idx++]);
						rec.setSaldoInicial((BigDecimal) o[idx++]);
						rec.setEmisor((String) o[idx++]);
						
					listaEstadoCuenta.add(rec);
					
					}
		} catch (Exception e) {
			
		}
		
		
		return listaEstadoCuenta;
		/*em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			listaEstadoCuenta = new ArrayList<RepEstadoCuenta>();*/
	}

	@Override
	public String actualizar(RepEstadoCuenta e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String guardar(RepEstadoCuenta e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminar(RepEstadoCuenta e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminarListado(List<RepEstadoCuenta> listado) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
