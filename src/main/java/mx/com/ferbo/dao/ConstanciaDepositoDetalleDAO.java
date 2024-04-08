package mx.com.ferbo.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.ConstanciaDeDeposito;
import mx.com.ferbo.model.ConstanciaDepositoDetalle;
import mx.com.ferbo.util.EntityManagerUtil;

public class ConstanciaDepositoDetalleDAO extends IBaseDAO<ConstanciaDepositoDetalle, Integer> {

	@Override
	public ConstanciaDepositoDetalle buscarPorId(Integer id) {
		return null;
	}

	@Override
	public List<ConstanciaDepositoDetalle> buscarTodos() {
		EntityManager em = EntityManagerUtil.getEntityManager();
		List<ConstanciaDepositoDetalle> listado = null;
		listado = em.createNamedQuery("ConstanciaDepositoDetalle.findAll", ConstanciaDepositoDetalle.class)
				.getResultList();
		return listado;
	}

	@Override
	public List<ConstanciaDepositoDetalle> buscarPorCriterios(ConstanciaDepositoDetalle e) {
		
		return null;
	}

	public List<ConstanciaDepositoDetalle> buscarPorFolio(ConstanciaDeDeposito constanciaDeDeposito) {

		List<ConstanciaDepositoDetalle> lista = new ArrayList<>();

		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			lista = em.createNamedQuery("ConstanciaDepositoDetalle.findFolio", ConstanciaDepositoDetalle.class)
					.setParameter("folio", constanciaDeDeposito.getFolio()).getResultList();

		} catch (Exception e) {
		}

		return lista;
	}

	@Override
	public String actualizar(ConstanciaDepositoDetalle e) {
		
		return null;
	}

	@Override
	public String guardar(ConstanciaDepositoDetalle constanciaDD) {
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.persist(constanciaDD);
			em.getTransaction().commit();
			em.close();
		} catch (Exception e) {
			System.out.println("ERROR" + e.getMessage());
			return "ERROR";
		}
		return null;
	}

	@Override
	public String eliminar(ConstanciaDepositoDetalle constanciaDepositoDetalle) {

		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			ConstanciaDepositoDetalle constancia = em.find(ConstanciaDepositoDetalle.class,
					constanciaDepositoDetalle.getConstanciaDepositoDetalleCve());
			em.remove(constancia);
			em.getTransaction().commit();
			em.close();
		} catch (Exception e) {
			System.out.println("ERROR" + e.getMessage());
			return "ERROR";
		}

		return null;
	}

	@Override
	public String eliminarListado(List<ConstanciaDepositoDetalle> listado) {
		return null;
	}

}
