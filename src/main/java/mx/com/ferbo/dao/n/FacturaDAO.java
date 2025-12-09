package mx.com.ferbo.dao.n;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.ConstanciaFactura;
import mx.com.ferbo.model.ConstanciaFacturaDs;
import mx.com.ferbo.model.Factura;
import mx.com.ferbo.model.FacturaMedioPago;
import mx.com.ferbo.model.NotaPorFactura;
import mx.com.ferbo.model.Pago;
import mx.com.ferbo.model.ProductoConstancia;
import mx.com.ferbo.model.ProductoConstanciaDs;
import mx.com.ferbo.model.ServicioConstancia;
import mx.com.ferbo.model.ServicioConstanciaDs;
import mx.com.ferbo.model.ServicioFactura;
import mx.com.ferbo.util.EntityManagerUtil;

public class FacturaDAO extends BaseDAO <Factura, Integer>{
	
	private static Logger log = LogManager.getLogger(FacturaDAO.class);

    public FacturaDAO(){
        super(Factura.class);
    }
    
    public Factura buscarPorId(Integer id, boolean isFullInfo) {
		Factura factura = null;
		EntityManager em = null;
		
		try {
			em = EntityManagerUtil.getEntityManager();
			factura = em.createNamedQuery("Factura.findById", Factura.class)
					.setParameter("id",id).getSingleResult();

			if(isFullInfo == false) {
				return factura;
			}
			
			log.info("Planta: {}", factura.getPlanta().getPlantaCve());
			
			List<FacturaMedioPago> facturaMedioPagoList = factura.getFacturaMedioPagoList();
			for(FacturaMedioPago medio : facturaMedioPagoList) {
				log.info("Medio de pago (Forma de pago SAT): {}", medio.getMpId().getMpId());
			}
			
			List<ServicioFactura> servicioFacturaList = factura.getServicioFacturaList();
			for(ServicioFactura s : servicioFacturaList) {
				log.info("Tipo cobro: {}", s.getTipoCobro().getId());
			}
			
			List<ConstanciaFacturaDs> constanciaFacturaDsList = factura.getConstanciaFacturaDsList();
			for(ConstanciaFacturaDs cf : constanciaFacturaDsList) {
				List<ProductoConstanciaDs> productoConstanciaDsList = cf.getProductoConstanciaDsList();
				for(ProductoConstanciaDs pc : productoConstanciaDsList) {
					log.info("Producto constancia DS: {}", pc.getId());
				}
				
				List<ServicioConstanciaDs> servicioConstanciaDsList = cf.getServicioConstanciaDsList();
				for(ServicioConstanciaDs sc : servicioConstanciaDsList) {
					log.info("Servicio constancia DS: {}", sc.getId());
				}
			}
			
			List<ConstanciaFactura> constanciaFacturaList = factura.getConstanciaFacturaList();
			for(ConstanciaFactura cf : constanciaFacturaList) {
				List<ProductoConstancia> productoConstanciaList = cf.getProductoConstanciaList();
				for(ProductoConstancia pc : productoConstanciaList) {
					log.info("Producto constancia: {}", pc.getId());
				}
				
				List<ServicioConstancia> servicioConstanciaList = cf.getServicioConstanciaList();
				for(ServicioConstancia sc : servicioConstanciaList) {
					log.info("Servicio constancia: {}", sc.getId());
				}
			}
			
			List<Pago> pagoList = factura.getPagoList();
			for(Pago p : pagoList) {
				log.info("Pago: {}", p.getId());
				log.info("Tipo pago: {}", p.getTipo().getId());
				log.info("Factura: {}", p.getFactura().getId());
				log.info("Status factura: {}", p.getFactura().getStatus().getId());
			}
			
			List<NotaPorFactura> notaPorFacturaList= factura.getNotaFacturaList();
			for(NotaPorFactura nf : notaPorFacturaList) {
				log.debug("Nota: {}", nf.getNotaPorFacturaPK().getNota().getId());
				log.debug("Status nota: {}", nf.getNotaPorFacturaPK().getNota().getStatus().getId());
				log.debug("Factura: {}", nf.getNotaPorFacturaPK().getFactura().getId());
				log.debug("Status factura: {}", nf.getNotaPorFacturaPK().getFactura().getStatus().getId());
				log.debug("Cantidad NxF: {}",nf.getCantidad());
			}
		} catch(Exception ex) {
			log.error("Problema para obtener la información de la factura id:{}", ex, id);
		} finally {
			EntityManagerUtil.close(em);
		}
		
		return factura;
	}

    public List<Factura> obtenerPorFolioDeposito(Integer folio){
        EntityManager em = null;
        List<Factura> lista = null;

        try{
            em = super.getEntityManager();
            lista = em.createNamedQuery("Factura.findByFolioDeposito", Factura.class).setParameter("folio",folio).getResultList();
        } finally {
            close(em);
        }    
        return lista;
    }
    
    public List<Factura> buscaFacturas(Cliente c, Date fechaInicio, Date fechaFin, boolean isFullInfo) {
		EntityManager entity = null;
		List<Factura> list = null;
		try {
			entity = EntityManagerUtil.getEntityManager();
			list = entity.createNamedQuery("Factura.findByClientePeriodo", Factura.class)
					.setParameter("cliente", c)
					.setParameter("fechaInicio", fechaInicio)
					.setParameter("fechaFin", fechaFin)
					.getResultList();
			
			if(isFullInfo == false)
				return list;
			
			for(Factura factura : list) {
				log.debug("Status id: {}", factura.getStatus().getId());
			}
			
		} catch(Exception ex) {
			log.error("Problema para obtener el listado de facturas...", ex);
		} finally {
			EntityManagerUtil.close(entity);
		}
		
		return list;
	}
	
	public List<Factura> buscaFacturas(Date fechaInicio, Date fechaFin, boolean isFullInfo) {
		EntityManager entity = null;
		List<Factura> list = null;
		try {
			entity = EntityManagerUtil.getEntityManager();
			list = entity.createNamedQuery("Factura.findByPeriodo", Factura.class)
					.setParameter("fechaInicio", fechaInicio)
					.setParameter("fechaFin", fechaFin)
					.getResultList();
			
			if(isFullInfo == false)
				return list;
			
			for(Factura factura : list) {
				log.debug("Status id: {}", factura.getStatus().getId());
			}
			
		} catch(Exception ex) {
			log.error("Problema para obtener el listado de facturas...", ex);
		} finally {
			EntityManagerUtil.close(entity);
		}
		
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<Factura> consultaFacturas(Integer idCliente, Date fechaInicio, Date fechaFin) {
		List<Factura> facturas = null;
		EntityManager em = null;
		String query = null;
		try {
			query = "SELECT f.* FROM factura f\n"
					+ "LEFT OUTER JOIN cfdi c ON f.id = c.id_factura\n"
					+ "WHERE (c.cfdi_id IS NULL)\n"
					+ "AND (f.uuid IS NOT NULL)\n"
					+ "AND (f.fecha BETWEEN :fechaInicio AND :fechaFin)\n"
					+ "AND (f.cliente = :idCliente OR :idCliente IS NULL)";
			log.info("Query: {}", query);
			em = this.getEntityManager();
			facturas = em.createNativeQuery(query, modelClass)
					.setParameter("idCliente", idCliente)
					.setParameter("fechaInicio", fechaInicio)
					.setParameter("fechaFin", fechaFin)
					.getResultList();
					
		} catch(Exception ex) {
			log.error("Problema para obtener la lista de facturas...", ex);
		} finally {
			this.close(em);
		}
		
		return facturas;
	}
    
}
