package mx.com.ferbo.dao;

import java.math.BigDecimal;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.Saldo;
import mx.com.ferbo.util.EntityManagerUtil;

public class SaldoDAO {
	private static Logger log = LogManager.getLogger(SaldoDAO.class);
	
	public Saldo getSaldo(Cliente cliente) {
		Saldo saldo = null;
		BigDecimal bSaldo = null;
		EntityManager em = null;
		Query query = null;
		
		try {
			em = EntityManagerUtil.getEntityManager();
			query = em.createNativeQuery("select coalesce(sum(saldo), 0) as saldo from (\n"
					+ "	select\n"
					+ "		f.cliente,\n"
					+ "		f.id,\n"
					+ "		(f.total - coalesce(pg.pago, 0) ) as saldo\n"
					+ "	from factura f \n"
					+ "	left outer join (\n"
					+ "		select\n"
					+ "			p.factura,\n"
					+ "			sum(monto) as pago\n"
					+ "		from pago p\n"
					+ "		group by p.factura\n"
					+ "	) pg on f.id = pg.factura\n"
					+ "	where f.status not in (0,2)\n"
					+ "	and cliente = :idCliente\n"
					+ ") sld where saldo > 0")
					.setParameter("idCliente", cliente.getCteCve())
					;
			bSaldo = (BigDecimal) query.getSingleResult();
			
			int idx = 0 ;
			saldo = new Saldo();
			saldo.setSaldo(bSaldo);
			saldo.setCliente(cliente);
			
		} catch(Exception ex) {
			log.error("Problema para botener el saldo...", ex);
		} finally {
			EntityManagerUtil.close(em);
		}
		
		return saldo;
	}

}
