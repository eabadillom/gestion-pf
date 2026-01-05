package mx.com.ferbo.dao;

import java.sql.SQLException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.FacturaMedioPago;
import mx.com.ferbo.util.EntityManagerUtil;

public class FacturaMedioPagoDAO extends IBaseDAO<FacturaMedioPago, Integer > {

	private static Logger log = LogManager.getLogger(FacturaMedioPagoDAO.class);
	
	@Override
	public FacturaMedioPago buscarPorId(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<FacturaMedioPago> buscarTodos() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public FacturaMedioPago buscarPorFactura(Integer facturaId) throws SQLException {
		
		FacturaMedioPago fmp = null;
		EntityManager em = null;
		Query query = null;
		
		try {			
			em = EntityManagerUtil.getEntityManager();
			query = em.createNativeQuery("SELECT fmp.factura_id, fmp.fmp_id, fmp.mp_id, fmp.mp_descripcion, fmp.fmp_porcentaje, fmp.fmp_referencia, fmp.fmp_forma_pago "
					+ "FROM factura_medio_pago fmp WHERE fmp.factura_id = :facturaId ", FacturaMedioPago.class)
					.setParameter("facturaId", facturaId);
					
			fmp = (FacturaMedioPago) query.getSingleResult();
			
			
		}catch(Exception e){
			log.info("Error al buscar por factura id una factura medio pago",e.getMessage());
		}finally {
			EntityManagerUtil.close(em);
		}
		
		return fmp;
	}

	@Override
	public List<FacturaMedioPago> buscarPorCriterios(FacturaMedioPago e) {
		// TODO Auto-generated method stub
		return null;
	}

//	@Override
//	public String actualizar(FacturaMedioPago e) {
//		
//		EntityManager em = null;
//		
//		try {
//			em = EntityManagerUtil.getEntityManager();
//			em.getTransaction().begin();
//			em.createNativeQuery("UPDATE factura_medio_pago fmp SET fmp.mp_id =:mpId, fmp.mp_descripcion = :mpDescripcion WHERE fmp.factura_id = :facturaId",FacturaMedioPago.class)
//			.setParameter("mpId", e.getMpId())
//			.setParameter("mpDescripcion", e.getMpDescripcion())
//			.setParameter("facturaId", e.getFactura().getId()).executeUpdate();			
//			
//			em.getTransaction().commit();			
//		} catch (Exception e2) {
//			log.info("Error al actualizar factira medio pago", e2.getMessage());
//		}finally {
//			EntityManagerUtil.close(em);
//		}
//		
//		return null;
//	}

	@Override
	public String guardar(FacturaMedioPago e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminar(FacturaMedioPago e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminarListado(List<FacturaMedioPago> listado) {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
