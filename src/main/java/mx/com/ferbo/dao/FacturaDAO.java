package mx.com.ferbo.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.ConstanciaFactura;
import mx.com.ferbo.model.ConstanciaFacturaDs;
import mx.com.ferbo.model.Factura;
import mx.com.ferbo.model.FacturaMedioPago;
import mx.com.ferbo.model.ProductoConstancia;
import mx.com.ferbo.model.ProductoConstanciaDs;
import mx.com.ferbo.model.ServicioConstancia;
import mx.com.ferbo.model.ServicioConstanciaDs;
import mx.com.ferbo.model.ServicioFactura;
import mx.com.ferbo.model.StatusFactura;
import mx.com.ferbo.util.EntityManagerUtil;

public class FacturaDAO extends IBaseDAO<Factura, Integer> {
	
	private static Logger log = LogManager.getLogger(FacturaDAO.class);

	@SuppressWarnings("unchecked")
	public List<Factura> findall() {
		EntityManager entity = EntityManagerUtil.getEntityManager();
		List<Factura> fact = null;
		Query sql = entity.createNamedQuery("Factura.findAll", Factura.class);
		fact = sql.getResultList();
		return fact;
	}
	
	
	@Override
	public Factura buscarPorId(Integer id) {
		EntityManager entity = EntityManagerUtil.getEntityManager();
		Factura fact = entity.createNamedQuery("Factura.findById", Factura.class)
				.setParameter("id",id).getSingleResult();
		return fact;
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
			
			log.debug("Planta: {}", factura.getPlanta().getPlantaCve());
			
			List<FacturaMedioPago> facturaMedioPagoList = factura.getFacturaMedioPagoList();
			for(FacturaMedioPago medio : facturaMedioPagoList) {
				log.debug("Medio de pago (Forma de pago SAT): {}", medio.getMpId().getMpId());
			}
			
			List<ServicioFactura> servicioFacturaList = factura.getServicioFacturaList();
			for(ServicioFactura s : servicioFacturaList) {
				log.debug("Tipo cobro: {}", s.getTipoCobro().getId());
			}
			
			List<ConstanciaFacturaDs> constanciaFacturaDsList = factura.getConstanciaFacturaDsList();
			for(ConstanciaFacturaDs cf : constanciaFacturaDsList) {
				List<ProductoConstanciaDs> productoConstanciaDsList = cf.getProductoConstanciaDsList();
				for(ProductoConstanciaDs pc : productoConstanciaDsList) {
					log.debug("Producto constancia DS: {}", pc.getId());
				}
				
				List<ServicioConstanciaDs> servicioConstanciaDsList = cf.getServicioConstanciaDsList();
				for(ServicioConstanciaDs sc : servicioConstanciaDsList) {
					log.debug("Servicio constancia DS: {}", sc.getId());
				}
			}
			
			List<ConstanciaFactura> constanciaFacturaList = factura.getConstanciaFacturaList();
			for(ConstanciaFactura cf : constanciaFacturaList) {
				List<ProductoConstancia> productoConstanciaList = cf.getProductoConstanciaList();
				for(ProductoConstancia pc : productoConstanciaList) {
					log.debug("Producto constancia: {}", pc.getId());
				}
				
				List<ServicioConstancia> servicioConstanciaList = cf.getServicioConstanciaList();
				for(ServicioConstancia sc : servicioConstanciaList) {
					log.debug("Servicio constancia: {}", sc.getId());
				}
			}
			
		} catch(Exception ex) {
			log.error("Problema para obtener la información de la factura id:{}", ex, id);
		} finally {
			EntityManagerUtil.close(em);
		}
		
		return factura;
	}
	
	public List<Factura> buscarPorCliente (Cliente cte){
	EntityManager entity = EntityManagerUtil.getEntityManager();
	return entity.createNamedQuery("Factura.findByCliente", Factura.class).getResultList();
	}
	
	public List<Factura> buscarPorCteStatus(StatusFactura sf, Cliente cte){
		EntityManager entity = EntityManagerUtil.getEntityManager();
		return entity.createNamedQuery("Factura.findByClienteStatusFactura", Factura.class)
		.setParameter("clienteCve", cte.getCteCve()).setParameter("status", sf.getId()).getResultList();
		}

	@Override
	public List<Factura> buscarTodos() {
		// TODO Auto-generated method stub
		EntityManager em = EntityManagerUtil.getEntityManager();
		return em.createNamedQuery("Factura.findAll", Factura.class).getResultList();
	}

	@Override
	public List<Factura> buscarPorCriterios(Factura f) {
		List<Factura> listaFac = null;
		EntityManager em = EntityManagerUtil.getEntityManager();
		listaFac = em.createNamedQuery("Factura.findById", Factura.class).setParameter("id", f.getId()).getResultList();
		return listaFac;
	}

	@Override
	public String actualizar(Factura f) {
		EntityManager entity = null;
		try {
			entity = EntityManagerUtil.getEntityManager();
			entity.getTransaction().begin();
			entity.merge(f); 
			entity.getTransaction().commit();
		}catch (Exception e){
			log.error("Problema al actualizar la factura: {}", e, f);
			return "Failed!!";
		} finally {
			EntityManagerUtil.close(entity);
		}
		return null;
	}

	@Override
	public String guardar(Factura factura) {
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.persist(factura);
			em.getTransaction().commit();
			em.close();
		} catch (Exception e) {
			System.out.println("ERROR guardando Factura" + e.getStackTrace());
			System.out.println("ERROR guardando Factura" + e.getLocalizedMessage());
			
			return "ERROR";
		}
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

	public String actualizaStatus(Factura f) {
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.createNativeQuery("UPDATE factura SET status =:status WHERE id =:id")
					.setParameter("status", f.getStatus()).setParameter("id", f.getId()).executeUpdate();
			em.getTransaction().commit();
			em.close();
		} catch (Exception e) {
			System.out.println("ERROR" + e.getMessage());
			return "ERROR";
		}
		return null;
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
				
				if(factura.getCancelaFactura() != null)
					log.debug("CancelaFactura: id={}",factura.getCancelaFactura().getId());
			}
			
		} catch(Exception ex) {
			log.error("Problema para obtener el listado de facturas...", ex);
		} finally {
			EntityManagerUtil.close(entity);
		}
		
		return list;
	}

}
