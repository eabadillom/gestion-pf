package mx.com.ferbo.dao;

import java.util.List;

import javax.persistence.EntityManager;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.Bancos;
import mx.com.ferbo.model.TipoPago;
import mx.com.ferbo.util.EntityManagerUtil;

public class TipoPagoDAO extends IBaseDAO<TipoPago, Integer> {

	@Override
	public TipoPago buscarPorId(Integer id) {
		// TODO Auto-generated method stub
		return null;
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
