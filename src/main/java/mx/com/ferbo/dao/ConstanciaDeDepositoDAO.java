package mx.com.ferbo.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.ConstanciaDeDeposito;
import mx.com.ferbo.util.EntityManagerUtil;

public class ConstanciaDeDepositoDAO extends IBaseDAO<ConstanciaDeDeposito, Integer> {

	@Override
	public ConstanciaDeDeposito buscarPorId(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ConstanciaDeDeposito> buscarTodos() {
		EntityManager em = EntityManagerUtil.getEntityManager();
		return em.createNamedQuery("ConstanciaDeDeposito.findAll", ConstanciaDeDeposito.class)
				.getResultList();
	}

	public List<ConstanciaDeDeposito> buscarPorCliente(Integer cteCve){
		EntityManager em = EntityManagerUtil.getEntityManager();
		List<ConstanciaDeDeposito> listado = null;
		listado = em.createNamedQuery("ConstanciaDeDeposito.findByCteCve",ConstanciaDeDeposito.class).setParameter("cteCve", cteCve).getResultList();
		return listado;
	}
	
	@Override
	public List<ConstanciaDeDeposito> buscarPorCriterios(ConstanciaDeDeposito e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String actualizar(ConstanciaDeDeposito e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String guardar(ConstanciaDeDeposito constanciaDeDeposito) {
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.persist(constanciaDeDeposito);
			em.getTransaction().commit();
			em.close();
		}catch (Exception e) {
			System.out.println("ERROR" + e.getMessage());
			e.printStackTrace();
			e.getCause();
			return "ERROR";
		}
		
		return null;
	}

	@Override
	public String eliminar(ConstanciaDeDeposito e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminarListado(List<ConstanciaDeDeposito> listado) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<ConstanciaDeDeposito> buscarPorFolioCliente(ConstanciaDeDeposito cons) {
		EntityManager em = EntityManagerUtil.getEntityManager();
		List<ConstanciaDeDeposito> lstAux = new ArrayList<>();
		lstAux = em.createNamedQuery("ConstanciaDeDeposito.findByFolioCliente",ConstanciaDeDeposito.class)
				.setParameter("folioCliente",cons.getFolioCliente())
				.getResultList();	
		System.out.println(lstAux);
		return lstAux;
	}

}
