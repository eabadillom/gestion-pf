package mx.com.ferbo.dao;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.Saldo;
import mx.com.ferbo.util.EntityManagerUtil;

public class SaldoDAO {
	private static Logger log = LogManager.getLogger(SaldoDAO.class);

	public Saldo getSaldo(Cliente cliente, Date fecha, String emisorRFC) {
		Saldo saldo = null;
		Object[] obj = null;
		EntityManager em = null;
		Query query = null;

		try {
			em = EntityManagerUtil.getEntityManager();
			query = em.createNativeQuery(
					"WITH cartera (id,numero_cliente, cliente, nom_serie, numero, nombre_cliente, fecha, subtotal, iva, total, saldo, status, nombre_status, emi_rfc, emi_nombre,plazo, dias) AS\n"
							+ "(\n" + "	SELECT\n" + "        factura.id,\n" + "        factura.numero_cliente,\n"
							+ "        factura.cliente,\n" + "        factura.nom_serie,\n"
							+ "        factura.numero,\n" + "        trim(factura.nombre_cliente) AS nombre_cliente,\n"
							+ "        factura.fecha as fecha,\n" + "        factura.subtotal as subtotal,\n"
							+ "        factura.iva as iva,\n" + "        factura.total as total,\n"
							+ "        factura.total AS saldo,\n" + "        factura.status as status,\n"
							+ "        status_factura.nombre as nombre_status,\n" + "        factura.emi_rfc,\n"
							+ "        factura.emi_nombre,\n" + "        factura.plazo as plazo,\n"
							+ "        DATEDIFF(:fecha, date_add(factura.fecha, INTERVAL factura.plazo DAY)) as dias\n"
							+ "    FROM factura\n"
							+ "    INNER JOIN status_factura on factura.status = status_factura.id\n" + "    WHERE\n"
							+ "        factura.ID NOT IN(SELECT pago.factura FROM pago WHERE pago.fecha <= :fecha ) AND\n"
							+ "        factura.status NOT IN (0,2) AND\n" + "        factura.fecha <= :fecha AND\n"
							+ "        (factura.cliente = :idCliente OR :idCliente IS NULL) AND\n"
							+ "        (factura.emi_rfc = :emisorRFC OR :emisorRFC IS NULL)\n" + "    UNION all\n"
							+ "    SELECT * FROM (\n" + "	    SELECT\n" + "	       factura.id as id,\n"
							+ "	       factura.numero_cliente AS numero_cte,\n"
							+ "	       factura.cliente as cliente,\n" + "	       factura.nom_serie as nom_serie,\n"
							+ "	       factura.numero as numero,\n"
							+ "	       trim(factura.nombre_cliente) AS nombre_cliente,\n" + "	       factura.fecha,\n"
							+ "	       (factura.total - ingreso.monto)/(1.16) as subtotal,\n"
							+ "	       (factura.total - ingreso.monto) - subtotal as iva,\n"
							+ "	       (factura.total - ingreso.monto) AS total,\n"
							+ "	       (factura.total - ingreso.monto) AS saldo,\n"
							+ "	       factura.status as status,\n" + "	       status_factura.nombre as nombre,\n"
							+ "	       factura.emi_rfc,\n" + "	       factura.emi_nombre,\n"
							+ "	       factura.plazo as plazo,\n"
							+ "	       DATEDIFF(:fecha, date_add(factura.fecha, INTERVAL factura.plazo DAY)) as dias\n"
							+ "	    FROM (\n" + "	    	SELECT\n" + "	    		factura,\n"
							+ "	    		sum(monto) as monto\n" + "	    	FROM (\n" + "	    		SELECT\n"
							+ "		    		p.factura,\n" + "		    		sum(p.monto) as monto\n"
							+ "		    	FROM pago p\n" + "		    	WHERE p.tipo NOT IN (5)\n"
							+ "		    	AND p.fecha <= :fecha\n" + "		    	GROUP BY p.factura\n"
							+ "		    	UNION ALL\n" + "		    	SELECT\n"
							+ "		    		nf.FACTURA,\n" + "		    		sum(nf.CANTIDAD) as monto\n"
							+ "		    	FROM nota_credito nc\n"
							+ "		    	INNER JOIN nota_x_facturas nf ON nc.ID = nf.NOTA AND nc.STATUS = 1\n"
							+ "		    	WHERE nc.FECHA <= :fecha\n" + "		    	GROUP BY nf.FACTURA\n"
							+ "	    	) a\n" + "	    	GROUP BY a.factura\n" + "		) ingreso\n"
							+ "	    INNER JOIN factura ON ingreso.factura = factura.id\n"
							+ "	    INNER JOIN status_factura ON status_factura.id = factura.status\n" + "	    WHERE\n"
							+ "	       (factura.cliente = :idCliente OR :idCliente IS NULL) AND\n"
							+ "	       (factura.emi_rfc = :emisorRFC OR :emisorRFC IS NULL)\n"
							+ "	    ORDER BY fecha\n" + "    ) b WHERE b.saldo > 0\n" + ")\n" + "SELECT\n"
							+ "	numero_cliente, nombre_cliente, emi_rfc, emi_nombre,\n" + "	sum(saldo) as saldo,\n"
							+ "	sum(COALESCE(en_plazo, 0)) as en_plazo,\n" + "	sum(COALESCE(d_8, 0)) as d_8,\n"
							+ "	sum(COALESCE(d_15, 0)) as d_15,\n" + "	sum(COALESCE(d_30, 0)) as d_30,\n"
							+ "	sum(COALESCE(d_60, 0)) as d_60,\n" + "	sum(COALESCE(md_60,0)) as md_60\n" + "FROM (\n"
							+ "	SELECT\n"
							+ "		numero_cliente, nombre_cliente, emi_rfc, emi_nombre, sum(saldo) AS saldo,\n"
							+ "		sum(saldo) as en_plazo,\n" + "		null as d_8,\n" + "		null as d_15,\n"
							+ "		null as d_30,\n" + "		null as d_60,\n" + "		null as md_60\n"
							+ "	FROM cartera\n" + "	WHERE cartera.dias < 1\n"
							+ "	GROUP BY emi_rfc, emi_nombre, numero_cliente, nombre_cliente\n" + "	UNION \n"
							+ "	SELECT\n"
							+ "		numero_cliente, nombre_cliente, emi_rfc, emi_nombre, sum(saldo) AS saldo,\n"
							+ "		null as en_plazo,\n" + "	    sum(saldo) as d_8,\n" + "	    null as d_15,\n"
							+ "	    null as d_30,\n" + "	    null as d_60,\n" + "	    null as md_60\n"
							+ "	FROM cartera\n" + "	WHERE cartera.dias BETWEEN 1 AND 8\n"
							+ "	GROUP BY emi_rfc, emi_nombre, numero_cliente, nombre_cliente\n" + "	UNION \n"
							+ "	SELECT\n"
							+ "		numero_cliente, nombre_cliente, emi_rfc, emi_nombre, sum(saldo) AS saldo,\n"
							+ "		null as en_plazo,\n" + "	    sum(saldo) as d_8,\n" + "	    null as d_15,\n"
							+ "	    null as d_30,\n" + "	    null as d_60,\n" + "	    null as md_60\n"
							+ "	FROM cartera\n" + "	WHERE cartera.dias BETWEEN 1 AND 8\n"
							+ "	GROUP BY emi_rfc, emi_nombre, numero_cliente, nombre_cliente\n" + "	UNION\n"
							+ "	SELECT\n"
							+ "		numero_cliente, nombre_cliente, emi_rfc, emi_nombre, sum(saldo) AS saldo,\n"
							+ "		null as en_plazo,\n" + "	    sum(saldo) as d_8,\n" + "	    null as d_15,\n"
							+ "	    null as d_30,\n" + "	    null as d_60,\n" + "	    null as md_60\n"
							+ "	FROM cartera\n" + "	WHERE cartera.dias BETWEEN 1 AND 8\n"
							+ "	GROUP BY emi_rfc, emi_nombre, numero_cliente, nombre_cliente\n" + "	UNION\n"
							+ "	SELECT\n"
							+ "		numero_cliente, nombre_cliente, emi_rfc, emi_nombre, sum(saldo) AS saldo,\n"
							+ "		null as en_plazo,\n" + "	    null as d_8,\n" + "	    sum(saldo) as d_15,\n"
							+ "	    null as d_30,\n" + "	    null as d_60,\n" + "	    null as md_60\n"
							+ "	FROM cartera\n" + "	WHERE cartera.dias BETWEEN 9 AND 15\n"
							+ "	GROUP BY emi_rfc, emi_nombre, numero_cliente, nombre_cliente\n" + "	UNION\n"
							+ "	SELECT\n"
							+ "		numero_cliente, nombre_cliente, emi_rfc, emi_nombre, sum(saldo) AS saldo,\n"
							+ "		null as en_plazo,\n" + "	    null as d_8,\n" + "	    null as d_15,\n"
							+ "	    sum(saldo) as d_30,\n" + "	    null as d_60,\n" + "	    null as md_60\n"
							+ "	FROM cartera\n" + "	WHERE cartera.dias BETWEEN 16 AND 30\n"
							+ "	GROUP BY emi_rfc, emi_nombre, numero_cliente, nombre_cliente\n" + "	UNION\n"
							+ "	SELECT\n"
							+ "		numero_cliente, nombre_cliente, emi_rfc, emi_nombre, sum(saldo) AS saldo,\n"
							+ "		null as en_plazo,\n" + "	    null as d_8,\n" + "	    null as d_15,\n"
							+ "	    null as d_30,\n" + "	    sum(saldo) as d_60,\n" + "	    null as md_60\n"
							+ "	FROM cartera\n" + "	WHERE cartera.dias BETWEEN 31 AND 60\n"
							+ "	GROUP BY emi_rfc, emi_nombre, numero_cliente, nombre_cliente\n" + "	UNION\n"
							+ "	SELECT\n"
							+ "		numero_cliente, nombre_cliente, emi_rfc, emi_nombre, sum(saldo) AS saldo,\n"
							+ "		null as en_plazo,\n" + "	    null as d_8,\n" + "	    null as d_15,\n"
							+ "	    null as d_30,\n" + "	    null as d_60,\n" + "	    sum(saldo) as md_60\n"
							+ "	FROM cartera\n" + "	WHERE cartera.dias > 60\n"
							+ "	GROUP BY emi_rfc, emi_nombre, numero_cliente, nombre_cliente\n" + ") x\n" + "GROUP BY\n"
							+ "	emi_rfc, emi_nombre, numero_cliente, nombre_cliente\n"
							+ "ORDER BY x.emi_nombre, x.nombre_cliente")
					.setParameter("idCliente", cliente.getCteCve()).setParameter("fecha", fecha)
					.setParameter("emisorRFC", emisorRFC);

			obj = (Object[]) query.getSingleResult();

			int idx = 0;
			saldo = new Saldo();

			saldo.setNumeroCliente((String) obj[idx++]);
			saldo.setNombreCliente((String) obj[idx++]);
			saldo.setEmisorRFC((String) obj[idx++]);
			saldo.setEmisorNombre((String) obj[idx++]);
			saldo.setSaldo((BigDecimal) obj[idx++]);
			saldo.setEnPlazo((BigDecimal) obj[idx++]);
			saldo.setAtraso8dias((BigDecimal) obj[idx++]);
			saldo.setAtraso15dias((BigDecimal) obj[idx++]);
			saldo.setAtraso30dias((BigDecimal) obj[idx++]);
			saldo.setAtraso60dias((BigDecimal) obj[idx++]);
			saldo.setAtrasoMayor60dias((BigDecimal) obj[idx++]);

		} catch (NoResultException ex) {
			log.warn("Saldo no encontrado para el cliente id: {}", cliente.getCteCve());
		} catch (Exception ex) {
			log.error("Problema para obtener el saldo...", ex);
		} finally {
			EntityManagerUtil.close(em);
		}

		return saldo;
	}

}
