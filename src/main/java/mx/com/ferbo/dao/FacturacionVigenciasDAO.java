package mx.com.ferbo.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.ConstanciaDeDeposito;
import mx.com.ferbo.model.ConstanciaFactura;
import mx.com.ferbo.util.DateUtil;
import mx.com.ferbo.util.EntityManagerUtil;
import mx.com.ferbo.util.InventarioException;

public class FacturacionVigenciasDAO extends IBaseDAO<ConstanciaFactura, Integer> {
	
	private static Logger log = Logger.getLogger(FacturacionVigenciasDAO.class);

	@Override
	public ConstanciaFactura buscarPorId(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ConstanciaFactura> buscarTodos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ConstanciaFactura> buscarPorCriterios(ConstanciaFactura e) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<ConstanciaFactura> buscarNoFacturados(Integer idCliente, Date fechaCorte, Integer idPlanta) {
		List<ConstanciaFactura> list = null;
		List<ConstanciaFactura> listaTmpConstancias = null;
		List<ConstanciaDeDeposito> listaConstancias = null;
		List<ConstanciaFactura> lConstanciaFactura = null;
		ConstanciaFactura cf = null;
		EntityManager em = null;
		String sql = null;
		
		try {
			em = EntityManagerUtil.getEntityManager();
			list = new ArrayList<>();
			sql = "SELECT "
					+ "     folio, "
					+ "     folio_cliente, "
					+ "     cte_cve, "
					+ "     fecha_ingreso, "
					+ "     nombre_transportista, "
					+ "     placas_transporte, "
					+ "     observaciones, "
					+ "     valor_declarado, "
					+ "     status, "
					+ "     aviso_cve, "
					+ "     temperatura "
					+ "FROM ( "
					+ "    SELECT "
					+ "        cdd.folio, "
					+ "        cdd.folio_cliente, "
					+ "        cdd.cte_cve, "
					+ "        cdd.fecha_ingreso, "
					+ "        cdd.nombre_transportista, "
					+ "        cdd.placas_transporte, "
					+ "        cdd.observaciones, "
					+ "        cdd.valor_declarado, "
					+ "        cdd.status, "
					+ "        cdd.aviso_cve, "
					+ "        cdd.temperatura, "
					+ "        (p.peso_total - COALESCE(s.peso, 0) )AS peso, "
					+ "        'Kilogramo' AS unidad_peso, "
					+ "        (p.cantidad_total - COALESCE(s.cantidad, 0) ) AS cantidad, "
					+ "        udm.unidad_de_manejo_ds AS unidad_manejo, "
					+ "        prd.producto_ds "
					+ "FROM CONSTANCIA_DE_DEPOSITO cdd "
					+ "INNER JOIN PARTIDA p ON p.folio = cdd.folio "
					+ "INNER JOIN CAMARA c ON p.CAMARA_CVE = c.CAMARA_CVE "
					+ "INNER JOIN PLANTA plt ON c.PLANTA_CVE = plt.PLANTA_CVE "
					+ "INNER JOIN DETALLE_PARTIDA dp ON p.partida_cve = dp.partida_cve AND det_part_cve = 1 "
					+ "INNER JOIN UNIDAD_DE_PRODUCTO udp ON p.unidad_de_producto_cve = udp.unidad_de_producto_cve "
					+ "INNER JOIN PRODUCTO prd ON udp.producto_cve = prd.producto_cve "
					+ "INNER JOIN UNIDAD_DE_MANEJO udm ON udp.unidad_de_manejo_cve = udm.unidad_de_manejo_cve "
					+ "INNER JOIN aviso a ON cdd.aviso_cve = a.aviso_cve "
					+ "LEFT OUTER JOIN ( "
					+ "    SELECT "
					+ "        dcs.partida_cve, "
					+ "        MAX(cs.fecha) AS fecha_ult_sal, "
					+ "        SUM(dcs.peso) AS peso, "
					+ "        'Kilogramo' AS unidad_peso, "
					+ "            SUM(dcs.cantidad) AS cantidad, "
					+ "            dcs.unidad AS unidad_manejo "
					+ "        FROM CONSTANCIA_SALIDA cs "
					+ "        INNER JOIN DETALLE_CONSTANCIA_SALIDA dcs ON "
					+ "            cs.id = dcs.constancia_cve "
					+ "        WHERE "
					+ "            cs.status = 1 "
					+ "            AND cs.cliente_cve = :cteCve "
					+ "            AND cs.fecha <= :fechaCorte "
					+ "        GROUP BY "
					+ "            dcs.partida_cve, "
					+ "            dcs.unidad "
					+ ") s ON p.partida_cve = s.partida_cve "
					+ "    WHERE "
					+ "        cdd.aviso_cve IS NOT NULL "
					+ "        AND peso > 0.0 "
					+ "        AND cantidad > 0 "
					+ "        AND cdd.cte_cve = :cteCve "
					+ "        AND cdd.fecha_ingreso <= :fechaCorte "
					+ "        AND plt.PLANTA_CVE = :plantaCve "
					+ "        ) T "
					+ "GROUP BY "
					+ "    folio, "
					+ "    folio_cliente, "
					+ "    cte_cve, "
					+ "    fecha_ingreso, "
					+ "    nombre_transportista, "
					+ "    placas_transporte, "
					+ "    observaciones, "
					+ "    valor_declarado, "
					+ "    status, "
					+ "	aviso_cve, "
					+ "	temperatura "
					+ "ORDER BY "
					+ "    fecha_ingreso ASC ";
			
			Query query = em.createNativeQuery(sql, ConstanciaDeDeposito.class)
					.setParameter("cteCve", idCliente)
					.setParameter("fechaCorte", fechaCorte)
					.setParameter("plantaCve", idPlanta)
					;
			listaConstancias = query.getResultList();
			
			for(ConstanciaDeDeposito constancia : listaConstancias) {
				listaTmpConstancias = constancia.getConstanciaFacturaList();
				lConstanciaFactura = listaTmpConstancias.stream()
						.filter(c -> 
							(c.getFactura().getStatus().getId() == 1) //Status Por cobrar
							|| (c.getFactura().getStatus().getId() == 3) //Status Pagada
							|| (c.getFactura().getStatus().getId() == 4)) //Status Pago parcial
						.collect(Collectors.toList());
				
				if(lConstanciaFactura.size() > 0)
					continue;
				
				cf = this.getConstanciaFactura(constancia, fechaCorte);
				list.add(cf);
			}
			
		} catch(Exception ex) {
			ex.printStackTrace();
		} finally {
			if(em != null)
				em.close();
		}
		return list;
	}
	
	private ConstanciaFactura getConstanciaFactura(ConstanciaDeDeposito constancia, Date fechaCorte) throws InventarioException {
		ConstanciaFactura cf = null;
		int vigencia = -1;
		Date vigenciaInicio = null;
		Date vigenciaFin = null;
		int tmpIni = 0;
		int tmpFin = 0;
		
		vigencia = constancia.getAvisoCve().getAvisoVigencia();
		vigenciaInicio = constancia.getFechaIngreso();
		vigenciaFin = DateUtil.fechaVencimiento(vigenciaInicio, vigencia, false);
		
		if(fechaCorte.compareTo(vigenciaInicio) < 0)
			return null;
		
		while(vigenciaInicio.compareTo(fechaCorte) <= 0) {
			
			
			tmpIni = vigenciaInicio.compareTo(fechaCorte);
			tmpFin = vigenciaFin.compareTo(fechaCorte);
			
			if(tmpIni <= 0 && tmpFin >= 0) {
				break;
			}
			vigenciaInicio = DateUtil.addDay(vigenciaFin, 1);
			vigenciaFin = DateUtil.fechaVencimiento(vigenciaInicio, vigencia, false);
			log.debug(String.format("Calculando vigencia: %s, inicio: %s, fin: %s", constancia.getFolioCliente(), DateUtil.getString(vigenciaInicio, DateUtil.FORMATO_YYYY_MM_DD_HH_MM_SS), DateUtil.getString(vigenciaFin, DateUtil.FORMATO_YYYY_MM_DD_HH_MM_SS)));
		}
		
		cf = new ConstanciaFactura();
		cf.setFolio(constancia);
		cf.setFolioCliente(constancia.getFolioCliente());
		cf.setVigenciaInicio(vigenciaInicio);
		cf.setVigenciaFin(vigenciaFin);
		
		return cf;
	}

	@Override
	public String actualizar(ConstanciaFactura e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String guardar(ConstanciaFactura e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminar(ConstanciaFactura e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminarListado(List<ConstanciaFactura> listado) {
		// TODO Auto-generated method stub
		return null;
	}

}