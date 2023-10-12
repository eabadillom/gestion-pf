package mx.com.ferbo.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.OrdenSalida;
import mx.com.ferbo.util.EntityManagerUtil;

public class OrdenSalidaDAO extends IBaseDAO<OrdenSalida, Integer>{
	private static Logger log = LogManager.getLogger(OrdenSalidaDAO.class);

	@SuppressWarnings("unchecked")
	public List<OrdenSalida> findall() {
		List<OrdenSalida> ordenSalida = null;
		EntityManager entity = null;
		try {
			entity = EntityManagerUtil.getEntityManager();
			Query sql = entity.createNamedQuery("OrdenSalida.findAll", OrdenSalida.class);
			ordenSalida = sql.getResultList();
		} catch(Exception ex) {
			log.error("Problema para obtener el listado de de salidas...", ex);
		} finally {
			EntityManagerUtil.close(entity);
		}
		return ordenSalida;
	}	
	
	@Override
	public OrdenSalida buscarPorId(Integer id) {
		OrdenSalida os = null;
		EntityManager  em = null;
		try {
			em = EntityManagerUtil.getEntityManager();		
			os = em.createNamedQuery("OrdenSalida.findByidPreSalida", OrdenSalida.class).
					setParameter("idPreSalida", id)
					.getSingleResult();
		} catch(Exception ex) {
			log.error("Problema para obtener la orden de salida: " + id, ex );
		} finally {
			EntityManagerUtil.close(em);
		}
		return os;
	}

	@Override
	public List<OrdenSalida> buscarTodos() {
		EntityManager em = null;
		List<OrdenSalida> listadoSalidas = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			listadoSalidas = em.createNamedQuery("OrdenSalida.findAll", OrdenSalida.class)
					.getResultList()
					;
		} catch(Exception ex) {
			log.error("Problema para obtener el listado de clientes...", ex);
		} finally {
			EntityManagerUtil.close(em);
		}
		
		return listadoSalidas;
	}

	@Override
	public List<OrdenSalida> buscarPorCriterios(OrdenSalida e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String actualizar(OrdenSalida e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String guardar(OrdenSalida os) {
		EntityManager em = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.persist(os);
			em.getTransaction().commit();
		} catch (Exception e) {
			log.error("Problema al guardar el cliente: " + os.getIdPreSalida(), e);
			return "ERROR";
		}finally {
			EntityManagerUtil.close(em);
		}
		return null;
	}


	@Override
	public String eliminar(OrdenSalida e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminarListado(List<OrdenSalida> listado) {
		// TODO Auto-generated method stub
		return null;
	}



}
