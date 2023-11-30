package mx.com.ferbo.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.IngresoServicio;
import mx.com.ferbo.util.EntityManagerUtil;

public class IngresoServicioDAO extends IBaseDAO<IngresoServicio , Integer>{
	
	private static Logger log = LogManager.getLogger(IngresoServicioDAO.class);

	@Override
	public IngresoServicio buscarPorId(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<IngresoServicio> buscarTodos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<IngresoServicio> buscarPorCriterios(IngresoServicio e) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<IngresoServicio> buscarPorIngreso(Integer idIngreso){
		
		List<IngresoServicio> lista = new ArrayList<IngresoServicio>();
		EntityManager em = null;
		String query = null;
		
		try {
			
			em = EntityManagerUtil.getEntityManager();
			
			//query = "SELECT ingS.* FROM ingreso_servicio ingS WHERE ingS.id_ingreso  = ? ";
			
			Query q = em.createNamedQuery("IngresoServicio.findByIngreso",IngresoServicio.class)
						.setParameter("idIngreso", idIngreso);
			
			lista = q.getResultList();
			
		} catch (Exception e) {
			log.info("Error al encontrar ingresos servicios del ingreso" + e.getMessage());
		}finally {
			EntityManagerUtil.close(em);
		}
		
		return lista;
	}

	@Override
	public String actualizar(IngresoServicio e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String guardar(IngresoServicio e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminar(IngresoServicio e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminarListado(List<IngresoServicio> listado) {
		// TODO Auto-generated method stub
		return null;
	}

}
