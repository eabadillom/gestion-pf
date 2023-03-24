package mx.com.ferbo.dao;

import java.util.List;

import javax.persistence.EntityManager;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.ConstanciaTraspaso;
import mx.com.ferbo.util.EntityManagerUtil;

public class ConstanciaTraspasoDAO extends IBaseDAO<ConstanciaTraspaso, Integer>{

	@Override
	public ConstanciaTraspaso buscarPorId(Integer id) {
		// TODO Auto-generated method stub
		EntityManager em = EntityManagerUtil.getEntityManager();
		return em.createNamedQuery("ConstanciaTraspaso.findById", ConstanciaTraspaso.class).
				setParameter("id", id).getSingleResult();		
	}

	public List<ConstanciaTraspaso> buscarporNumero(String numero){
		EntityManager em = EntityManagerUtil.getEntityManager();
		return em.createNamedQuery("ConstanciaTraspaso.findByNumero", ConstanciaTraspaso.class).setParameter("numero", numero).getResultList();
	}
	@Override
	public List<ConstanciaTraspaso> buscarTodos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ConstanciaTraspaso> buscarPorCriterios(ConstanciaTraspaso e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String actualizar(ConstanciaTraspaso constancia) {
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.persist(constancia);
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
	public String guardar(ConstanciaTraspaso constancia) {
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.persist(constancia);
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
	public String eliminar(ConstanciaTraspaso e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminarListado(List<ConstanciaTraspaso> listado) {
		// TODO Auto-generated method stub
		return null;
	}
	

}
