package mx.com.ferbo.dao;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.ConstanciaDeDeposito;
import mx.com.ferbo.model.ConstanciaDepositoDetalle;
import mx.com.ferbo.model.ConstanciaFactura;
import mx.com.ferbo.model.ConstanciaSalida;
import mx.com.ferbo.model.DetalleConstanciaSalida;
import mx.com.ferbo.model.Partida;
import mx.com.ferbo.util.DateUtil;
import mx.com.ferbo.util.EntityManagerUtil;
import mx.com.ferbo.util.InventarioException;

public class FacturacionVigenciasDAO extends IBaseDAO<ConstanciaFactura, Integer> {
	
	private static Logger log = LogManager.getLogger(FacturacionVigenciasDAO.class);
	
	EntityManager em = null;

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
					+ "FROM constancia_de_deposito cdd "
					+ "INNER JOIN partida p ON p.folio = cdd.folio "
					+ "INNER JOIN camara c ON p.CAMARA_CVE = c.CAMARA_CVE "
					+ "INNER JOIN planta plt ON c.PLANTA_CVE = plt.PLANTA_CVE "
					+ "INNER JOIN detalle_partida dp ON p.partida_cve = dp.partida_cve AND det_part_cve = 1 "
					+ "INNER JOIN unidad_de_producto udp ON p.unidad_de_producto_cve = udp.unidad_de_producto_cve "
					+ "INNER JOIN producto prd ON udp.producto_cve = prd.producto_cve "
					+ "INNER JOIN unidad_de_manejo udm ON udp.unidad_de_manejo_cve = udm.unidad_de_manejo_cve "
					+ "INNER JOIN aviso a ON cdd.aviso_cve = a.aviso_cve "
					+ "LEFT OUTER JOIN ( "
					+ "    SELECT "
					+ "        dcs.partida_cve, "
					+ "        MAX(cs.fecha) AS fecha_ult_sal, "
					+ "        SUM(dcs.peso) AS peso, "
					+ "        'Kilogramo' AS unidad_peso, "
					+ "            SUM(dcs.cantidad) AS cantidad, "
					+ "            dcs.unidad AS unidad_manejo "
					+ "        FROM constancia_salida cs "
					+ "        INNER JOIN detalle_constancia_salida dcs ON "
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
					+ "        AND cdd.cte_cve = :cteCve "
					+ "        AND cdd.fecha_ingreso <= :fechaCorte "
					+ "        AND plt.PLANTA_CVE = :plantaCve "
					+ "        ) T "
					+"         WHERE T.cantidad > 0 "
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
				log.debug("Constancia de deposito: {}",constancia.getFolioCliente());
				listaTmpConstancias = constancia.getConstanciaFacturaList();
				List<ConstanciaDepositoDetalle> allConstanciaDepositoDetalle = constancia.getConstanciaDepositoDetalleList();
				log.debug("Lista constanciaDepositoDetalle.size() = {}", allConstanciaDepositoDetalle.size());
				lConstanciaFactura = listaTmpConstancias.stream()
						.filter(c -> 
							(c.getFactura().getStatus().getId() == 1) //Status Por cobrar
							|| (c.getFactura().getStatus().getId() == 3) //Status Pagada
							|| (c.getFactura().getStatus().getId() == 4)) //Status Pago parcial
						.collect(Collectors.toList());
				
				if(lConstanciaFactura.size() <= 0)
					continue;
				
				cf = this.getConstanciaFactura(constancia, fechaCorte);
				final Date vigenciaIni = cf.getVigenciaInicio();
				final Date vigenciaFin = cf.getVigenciaFin();
				
				log.debug("folio: {}, vigencia ini: {}, vigencia fin: {}", cf.getFolioCliente(), cf.getVigenciaInicio(), cf.getVigenciaFin());
				
				List<ConstanciaFactura> lCFFacturadas = lConstanciaFactura.stream()
						.filter(c -> (c.getVigenciaInicio().equals(vigenciaIni) || c.getVigenciaFin().equals(vigenciaFin)))
						.collect(Collectors.toList());
				
				log.debug("Constancias facturadas: {}", lCFFacturadas.size());
				if(lCFFacturadas.size() > 0)
					continue;
				
				list.add(cf);
				
				for(Partida p : constancia.getPartidaList()) {
					BigDecimal cantidadT = new BigDecimal(p.getCantidadTotal());
					BigDecimal pesoTotal = p.getPesoTotal();
					BigDecimal cajasTarima = null;//cajas x Tarima
					BigDecimal noTarimas = new BigDecimal(0);//noTarimas
					BigDecimal tarimas = p.getNoTarimas();//cantidad_total
					BigDecimal salidaCantidad = new BigDecimal(0),salidaPeso = new BigDecimal(0);
					
					cajasTarima = cantidadT.divide(tarimas, 2, BigDecimal.ROUND_HALF_UP);
					
					log.debug("caja x tarima "+cajasTarima);
					
					for(DetalleConstanciaSalida dcs: p.getDetalleConstanciaSalidaList()) {
						
						ConstanciaSalida constanciaSalida = new ConstanciaSalida();
						constanciaSalida = dcs.getConstanciaCve();//OBTENGO OBJETO SIMPLE
						
						if(constanciaSalida.getStatus().getId()==2)
							continue;
						
						if(constanciaSalida.getFecha().after(cf.getVigenciaInicio()))
							continue;
						
						BigDecimal cantidad = new BigDecimal(dcs.getCantidad());
						BigDecimal peso = dcs.getPeso();
						
						salidaCantidad = salidaCantidad.add(cantidad);//suma total de cantidad salida
						salidaPeso = salidaPeso.add(peso);//suma total de peso salida 
						
						log.debug("Fecha: "+constanciaSalida.getFecha());
					}
					
					cantidadT = cantidadT.subtract(salidaCantidad);
					pesoTotal = pesoTotal.subtract(salidaPeso);
					noTarimas = cantidadT.divide(cajasTarima,0,RoundingMode.UP);
					
					p.setCantidadTotal(cantidadT.intValue());
					p.setPesoTotal(pesoTotal);
					p.setNoTarimas(noTarimas);
					
					log.debug("Folio: {}, Saldo: cantidad = {}, peso = {}, tarimas = {}", constancia.getFolioCliente(), cantidadT, pesoTotal, noTarimas);
				}
				
				constancia.setConstanciaFacturaList(new ArrayList<>());
				constancia.setConstanciaFacturaList(list);
				
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
		
		log.debug("Constancia: {}",constancia.getFolioCliente());
		
		vigencia = constancia.getAvisoCve().getAvisoVigencia();
		vigenciaInicio = constancia.getFechaIngreso();
		vigenciaFin = DateUtil.fechaVencimiento(vigenciaInicio, vigencia, false);
		
		log.debug(String.format("Calculando vigencia: inicio: %s, fin: %s", DateUtil.getString(vigenciaInicio, DateUtil.FORMATO_YYYY_MM_DD_HH_MM_SS), DateUtil.getString(vigenciaFin, DateUtil.FORMATO_YYYY_MM_DD_HH_MM_SS)));
		
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
		}
		
		cf = new ConstanciaFactura();
		cf.setFolio(constancia);
		cf.setFolioCliente(constancia.getFolioCliente());
		cf.setVigenciaInicio(vigenciaInicio);
		cf.setVigenciaFin(vigenciaFin);
		
		return cf;
	}
	
	public EntityManager getEm() {
		return em;
	}

	public void setEm(EntityManager em) {
		this.em = em;
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
