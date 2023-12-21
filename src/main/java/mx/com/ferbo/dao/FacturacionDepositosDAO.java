package mx.com.ferbo.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.ConstanciaDeDeposito;
import mx.com.ferbo.model.ConstanciaDepositoDetalle;
import mx.com.ferbo.model.ConstanciaFactura;
import mx.com.ferbo.model.Partida;
import mx.com.ferbo.util.DateUtil;
import mx.com.ferbo.util.EntityManagerUtil;

public class FacturacionDepositosDAO extends IBaseDAO<ConstanciaDeDeposito, Integer> {
	private static Logger log = LogManager.getLogger(FacturacionDepositosDAO.class);
	
	EntityManager em = null;
	
	@Override
	public ConstanciaDeDeposito buscarPorId(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ConstanciaDeDeposito> buscarTodos() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<ConstanciaFactura> buscarNoFacturados(Integer idCliente, Integer idPlanta) {
		List<ConstanciaDeDeposito> listaConstancias = null;
		List<ConstanciaFactura> listaConstanciaFactura = null;
		List<Partida> allPartida = null;
		
		ConstanciaFactura cf = null;
		Date vigenciaInicio = null;
		Date vigenciaFin = null;
		int vigencia = 0;
		
		EntityManager em = null;
		String sql = null;
		
		try {
			em = EntityManagerUtil.getEntityManager();
			listaConstanciaFactura = new ArrayList<>();
			
			//La siguiente consulta recibe dos parámetros: cteCve y plantaCve
			sql = "select "
					+ "	cdd.FOLIO, "
					+ "	cdd.CTE_CVE, "
					+ "	cdd.FECHA_INGRESO, "
					+ "	cdd.NOMBRE_TRANSPORTISTA, "
					+ "	cdd.PLACAS_TRANSPORTE, "
					+ "	cdd.OBSERVACIONES, "
					+ "	cdd.folio_cliente, "
					+ "	cdd.valor_declarado, "
					+ "	cdd.status, "
					+ "	cdd.aviso_cve, "
					+ "	cdd.temperatura "
					+ "from constancia_de_deposito cdd "
					+ "INNER JOIN cliente cte ON cdd.CTE_CVE = cte.CTE_CVE "
					+ "LEFT OUTER JOIN ( "
					+ "	SELECT cf.* FROM constancia_factura cf "
					+ "	INNER JOIN factura f ON cf.factura = f.id "
					+ "	WHERE f.status NOT IN (0,2) "
					+ ") tCF ON cdd.FOLIO = tCF.folio "
					+ "INNER JOIN ( "
					+ "	select DISTINCT p.FOLIO, plt.PLANTA_CVE, plt.PLANTA_DS from partida p "
					+ "	INNER JOIN camara cam ON p.CAMARA_CVE = cam.CAMARA_CVE  "
					+ "	INNER JOIN planta plt ON cam.PLANTA_CVE = plt.PLANTA_CVE "
					+ ") tPlt ON cdd.FOLIO = tPlt.FOLIO "
					+ "INNER JOIN ( "
					+ "	SELECT FOLIO, COUNT(FOLIO) AS CTA_SERVICIOS from constancia_deposito_detalle cdet "
					+ "	GROUP BY FOLIO "
					+ ") det ON cdd.FOLIO = det.FOLIO "
					+ "WHERE cdd.status not in (4) "
					+ "AND cdd.CTE_CVE = :cteCve "
					+ "AND tCF.id IS NULL "
					+ "AND tPlt.planta_cve = :plantaCve "
					+ "order by cdd.folio_cliente ";
			
			Query query = em.createNativeQuery(sql, ConstanciaDeDeposito.class)
					.setParameter("cteCve", idCliente)
					.setParameter("plantaCve", idPlanta);
			;
			
			listaConstancias = query.getResultList();
			
			for(ConstanciaDeDeposito cdd: listaConstancias){
				List<ConstanciaDepositoDetalle> allConstanciaDepositoDetalle = cdd.getConstanciaDepositoDetalleList();
				log.debug("Lista ConstanciaDepositoDetalle.size() = {}", allConstanciaDepositoDetalle.size());
				vigencia = cdd.getAvisoCve().getAvisoVigencia();
				vigenciaInicio = cdd.getFechaIngreso();
				vigenciaFin = DateUtil.fechaVencimiento(vigenciaInicio, vigencia, false);
				
				
				cf = new ConstanciaFactura();
				
				cf.setFolio(cdd);
				cf.setFolioCliente(cdd.getFolioCliente());
				cf.setVigenciaInicio(cdd.getFechaIngreso());
				cf.setVigenciaFin(vigenciaFin);
				
				//FALTA RELACION DE POR CAD CONSTANCIA DE DEPOSITO AGREGAR LAS CONSTANCIAS FACTURAS CDD.SETLISTAFACTURAS
				listaConstanciaFactura.add(cf);
				cdd.setConstanciaFacturaList(new ArrayList<>());
				cdd.setConstanciaFacturaList(listaConstanciaFactura);
				
				log.debug("Constancia factura: {}, {}, {}", cf.getFolioCliente(), cf.getVigenciaInicio(), cf.getVigenciaFin());
				
				allPartida = cdd.getPartidaList();
				log.debug("AllPartida.size: {}", allPartida.size());
			}
			
			
			
		} catch(Exception ex) {
			log.error("Problema para obtener la lista de entradas no facturadas...", ex);
		} finally {
			if(em != null)
				em.close();
		}
		
		return listaConstanciaFactura;
	}

	@Override
	public List<ConstanciaDeDeposito> buscarPorCriterios(ConstanciaDeDeposito e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String actualizar(ConstanciaDeDeposito e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String guardar(ConstanciaDeDeposito e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminar(ConstanciaDeDeposito e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminarListado(List<ConstanciaDeDeposito> listado) {
		// TODO Auto-generated method stub
		return null;
	}

	public EntityManager getEm() {
		return em;
	}

	public void setEm(EntityManager em) {
		this.em = em;
	}
	
	

}
