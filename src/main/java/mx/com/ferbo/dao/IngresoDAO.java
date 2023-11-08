package mx.com.ferbo.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jfree.util.Log;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.Ingreso;
import mx.com.ferbo.util.EntityManagerUtil;

public class IngresoDAO extends IBaseDAO<Ingreso, Integer> {
	
	private static Logger log = LogManager.getLogger(FacturaDAO.class);

	@Override
	public Ingreso buscarPorId(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Ingreso> buscarTodos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Ingreso> buscarPorCriterios(Ingreso e) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<Ingreso> buscarPorFechaCtePlanta(Date fechaActualIni,Date fechaActualFin, Integer idCliente, Integer idPlanta ){
		
		List<Ingreso> listaIngresos = new ArrayList<Ingreso>();
		EntityManager em = null;
		String query = null;
		
		try {
			
			em = EntityManagerUtil.getEntityManager();
			
			query = "SELECT i.* FROM ingreso i "
					+ "INNER JOIN ingreso_producto ip ON ip.id_ingreso = i.id_ingreso "
					+ "WHERE i.fecha_hora BETWEEN :fechaActualIni AND :fechaActualFin AND i.id_cliente = :idCliente AND ip.id_planta = :idPlanta ";
			
			Query sql = em.createNativeQuery(query, Ingreso.class)
					.setParameter("fechaActualIni", fechaActualIni)
					.setParameter("fechaActualFin", fechaActualFin)
					.setParameter("idCliente", idCliente)
					.setParameter("idPlanta", idPlanta);
			
			listaIngresos = sql.getResultList();
			
			
		} catch (Exception e) {
			log.info("Error al encontrar ordenes de entrada" + e.getMessage());
		}finally {
			EntityManagerUtil.close(em);
		}
		
		return listaIngresos;
		
	}
	
	@Override
	public String actualizar(Ingreso e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String guardar(Ingreso e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminar(Ingreso e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminarListado(List<Ingreso> listado) {
		// TODO Auto-generated method stub
		return null;
	}

}
