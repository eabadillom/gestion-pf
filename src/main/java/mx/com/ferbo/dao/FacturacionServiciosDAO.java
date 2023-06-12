package mx.com.ferbo.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.ConstanciaDeDeposito;
import mx.com.ferbo.model.ConstanciaDeServicio;
import mx.com.ferbo.model.ConstanciaFacturaDs;
import mx.com.ferbo.model.ConstanciaServicioDetalle;
import mx.com.ferbo.model.PrecioServicio;
import mx.com.ferbo.util.EntityManagerUtil;

public class FacturacionServiciosDAO extends IBaseDAO<ConstanciaFacturaDs, Integer> {
	
	private static Logger log = Logger.getLogger(FacturacionServiciosDAO.class);
	 
	public EntityManager em = null;
	
	@Override
	public ConstanciaFacturaDs buscarPorId(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ConstanciaFacturaDs> buscarTodos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ConstanciaFacturaDs> buscarPorCriterios(ConstanciaFacturaDs e) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<ConstanciaFacturaDs> buscarNoFacturados(Integer idCliente) {
		List<ConstanciaFacturaDs> list = null;
		List<ConstanciaFacturaDs> listaTmpConstancias = null;
		List<ConstanciaDeServicio> listaConstancias = null;
		List<ConstanciaFacturaDs> lConstanciaFactura = null;
		ConstanciaFacturaDs cf = null;
		//EntityManager em = null;
		String sql = null;
		
		try {
			//em = EntityManagerUtil.getEntityManager();
			list = new ArrayList<>();
			sql = "SELECT "
					+ " cs.FOLIO, "
					+ "	cs.CTE_CVE, "
					+ "	cs.FECHA, "
					+ "	cs.NOMBRE_TRANSPORTISTA, "
					+ "	cs.PLACAS_TRANSPORTE, "
					+ "	cs.OBSERVACIONES, "
					+ "	cs.FOLIO_CLIENTE, "
					+ "	cs.VALOR_DECLARADO, "
					+ "	cs.STATUS "
					+ "FROM CONSTANCIA_DE_SERVICIO cs "
					+ "INNER JOIN CLIENTE cte ON cs.CTE_CVE = cte.CTE_CVE "
					+ "LEFT OUTER JOIN ( "
					+ "	SELECT cf.* FROM CONSTANCIA_FACTURA_DS cf "
					+ "	INNER JOIN factura f ON cf.factura = f.id "
					+ "	WHERE f.status NOT IN (0,2) "
					+ ") tCF ON cs.FOLIO = tCF.folio "
					+ "INNER JOIN ( "
					+ "	SELECT FOLIO, COUNT(FOLIO) AS CTA_SERVICIOS FROM CONSTANCIA_SERVICIO_DETALLE cdet "
					+ "	GROUP BY FOLIO "
					+ ") det ON cs.FOLIO = det.FOLIO "
					+ "WHERE cs.status not in (4) "
					+ "AND cs.CTE_CVE = :cteCve "
					+ "AND tCF.id IS NULL "
					+ "ORDER BY cs.folio_cliente "
					;
			
			Query query = em.createNativeQuery(sql, ConstanciaDeServicio.class)
					.setParameter("cteCve", idCliente)
					;
			;
			
			listaConstancias = query.getResultList();
			
			for(ConstanciaDeServicio constancia : listaConstancias) {
				List<ConstanciaServicioDetalle> allConstanciaServicioDetalle = constancia.getConstanciaServicioDetalleList();//recuperando constancias de servicio detalle de servicio ds
				listaTmpConstancias = constancia.getConstanciaFacturaDsList();
				lConstanciaFactura = listaTmpConstancias.stream()
						.filter(c ->
							(c.getFactura().getStatus().getId() == 1 //Status por cobrar
							|| c.getFactura().getStatus().getId() == 3 //Status pagada
							|| c.getFactura().getStatus().getId() == 4) //status pago parcial
						).collect(Collectors.toList())
						;
				
				if(lConstanciaFactura.size() > 0)
					continue;
				
				cf = new ConstanciaFacturaDs();
				cf.setConstanciaDeServicio(constancia);
				cf.setFolioCliente(constancia.getFolioCliente());
				
				// FALTA RELACION DE CONSTANCIA FACTURA DS CON LA CONSTANCIA DE SERVICIO constancia.setConstanciaFacturaDsList(null);
				list.add(cf);
				constancia.setConstanciaFacturaDsList(new ArrayList<>());
				constancia.setConstanciaFacturaDsList(list);
				//modificacion
				for(ConstanciaServicioDetalle csd: allConstanciaServicioDetalle) {
					List<PrecioServicio> allPrecioServicio = csd.getServicioCve().getPrecioServicioList();
					System.out.println(allPrecioServicio.size());
				}
				
			}
			
			
		} catch(Exception ex) {
			log.error("Problema para obtener la lista de servicios no facturados...", ex);
		} finally {
			EntityManagerUtil.close(em);
		}
		
		return list;
	}

	@Override
	public String actualizar(ConstanciaFacturaDs e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String guardar(ConstanciaFacturaDs e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminar(ConstanciaFacturaDs e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminarListado(List<ConstanciaFacturaDs> listado) {
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
