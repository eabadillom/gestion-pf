package mx.com.ferbo.dao;

import java.util.List;

import javax.persistence.EntityManager;

import org.apache.log4j.Logger;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.Bancos;
import mx.com.ferbo.model.TipoPago;
import mx.com.ferbo.util.EntityManagerUtil;

public class TipoPagoDAO extends IBaseDAO<TipoPago, Integer> {
	private static Logger log = Logger.getLogger(TipoPagoDAO.class);
	@Override
	public TipoPago buscarPorId(Integer id) {
		TipoPago tp = null;
		EntityManager em = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			tp = em.find(TipoPago.class, id);
		}catch(Exception e) {
			log.error("Problema al buscar tipo pago...", e);
			
		}finally {
			EntityManagerUtil.close(em);
		}
		return tp;
	}

	@Override
	public List<TipoPago> buscarTodos() {
		List<TipoPago> listado = null;
		EntityManager em = EntityManagerUtil.getEntityManager();
		listado = em.createNamedQuery("TipoPago.findAll", TipoPago.class).getResultList();
		return listado;
	}

	@Override
	public List<TipoPago> buscarPorCriterios(TipoPago tipoPago) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String actualizar(TipoPago tipoPago) {
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.merge(tipoPago);
			em.getTransaction().commit();
			em.close();
		} catch (Exception e) {
			System.out.println("ERROR actualizando Banco" + e.getMessage());
			return "ERROR";
		}
		return null;
	}

	@Override
	public String guardar(TipoPago tipoPago) {
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.persist(tipoPago);
			em.getTransaction().commit();
			em.close();
		} catch (Exception e) {
			System.out.println("ERROR guardando Banco" + e.getMessage());
			return "ERROR";
		}
		return null;
	}

	@Override
	public String eliminar(TipoPago tipoPago) {
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.remove(em.merge(tipoPago));
			em.getTransaction().commit();
			em.close();
		} catch (Exception e) {
			System.out.println("ERROR" + e.getMessage());
			return "ERROR";
		}
		return null;
	}

	@Override
	public String eliminarListado(List<TipoPago> listado) {
		// TODO Auto-generated method stub
		return null;
	}

	
}
