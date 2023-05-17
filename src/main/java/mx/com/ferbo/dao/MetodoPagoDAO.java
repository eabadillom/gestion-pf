package mx.com.ferbo.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.MedioPago;
import mx.com.ferbo.model.MetodoPago;
import mx.com.ferbo.util.EntityManagerUtil;

public class MetodoPagoDAO extends IBaseDAO<MetodoPago,String>{
	private static Logger log = Logger.getLogger(MetodoPagoDAO.class);

	@Override
	public MetodoPago buscarPorId(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	

	public MetodoPago buscarPorMetodoPago(String metodoPago) {
		 EntityManager entity = EntityManagerUtil.getEntityManager();
		 MetodoPago metodoP = entity.createNamedQuery("MetodoPago.findByCdMetodoPago", MetodoPago.class).setParameter("cdMetodoPago",metodoPago).getSingleResult();
			return metodoP;
	}
	
	@Override
	public List<MetodoPago> buscarTodos() {
		// TODO Auto-generated method stub
		EntityManager em = EntityManagerUtil.getEntityManager();
		List<MetodoPago> lista = null;
		lista = em.createNamedQuery("MetodoPago.findAll", MetodoPago.class).getResultList();
		
		return lista;
	}
	
	@SuppressWarnings("unchecked")
	public List<MetodoPago> buscarVigentes(Date fecha) {
		List<MetodoPago> list = null;
		EntityManager em = null;
		Query query = null;
		
		try {
			em = EntityManagerUtil.getEntityManager();
			query = em.createNamedQuery("MetodoPago.buscarVigentes", MetodoPago.class)
					.setParameter("fecha", fecha)
					;
			list = query.getResultList();
		} catch(Exception ex) {
			log.error("Problema para obtener información del catálogo metodo_pago", ex);
		} finally {
			EntityManagerUtil.close(em);
		}
		return list;
	}

	@Override
	public List<MetodoPago> buscarPorCriterios(MetodoPago e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String actualizar(MetodoPago e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String guardar(MetodoPago e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminar(MetodoPago e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminarListado(List<MetodoPago> listado) {
		// TODO Auto-generated method stub
		return null;
	}

}
