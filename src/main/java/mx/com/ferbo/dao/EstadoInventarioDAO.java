package mx.com.ferbo.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.EstadoInventario;
import mx.com.ferbo.util.EntityManagerUtil;

public class EstadoInventarioDAO extends IBaseDAO<EstadoInventario, Integer> {
	
	private static Logger log = LogManager.getLogger(EstadoInventarioDAO.class);

	@Override
	public EstadoInventario buscarPorId(Integer id) {
		EstadoInventario estado = null;
		EntityManager em = null;
		
		try {
			em = EntityManagerUtil.getEntityManager();
			estado = em.find(EstadoInventario.class, id);
		} catch(Exception ex) {
			log.error("Problema para obtener el registro de estado de inventario...", ex);
		} finally {
			EntityManagerUtil.close(em);
		}
		
		return estado;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<EstadoInventario> buscarTodos() {
		List<EstadoInventario> lista = null;
		EntityManager em = null;
		Query query = null;
		
		try {
			em = EntityManagerUtil.getEntityManager();
			query = em.createNamedQuery("TipoMovimiento.findAll", EstadoInventario.class);
			lista = query.getResultList();
		} catch(Exception ex) {
			log.error("Problema para obtener los tipos de movimiento...", ex);
		} finally {
			EntityManagerUtil.close(em);
		}
		return lista;
	}

	@Override
	public List<EstadoInventario> buscarPorCriterios(EstadoInventario e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String actualizar(EstadoInventario e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String guardar(EstadoInventario e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminar(EstadoInventario e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminarListado(List<EstadoInventario> listado) {
		// TODO Auto-generated method stub
		return null;
	}

}
