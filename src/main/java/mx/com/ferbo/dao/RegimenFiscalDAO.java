package mx.com.ferbo.dao;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.RegimenFiscal;
import mx.com.ferbo.util.EntityManagerUtil;

public class RegimenFiscalDAO extends IBaseDAO<RegimenFiscal, String>{

	
	public List<RegimenFiscal> findAll(){
		EntityManager entity = EntityManagerUtil.getEntityManager();
		List<RegimenFiscal> listaRegimen = null;
		Query sql = (Query) entity.createNamedQuery("RegimenFiscal.findAll", RegimenFiscal.class);
		listaRegimen = (List<RegimenFiscal>) sql.getResultList();
		return listaRegimen;
	}
	
	@Override
	public RegimenFiscal buscarPorId(String cd_regimen) {
		EntityManager entity = EntityManagerUtil.getEntityManager();
		RegimenFiscal rf = null;
		Query sql = (Query) entity.createNamedQuery("RegimenFiscal.findBycdRegimen", RegimenFiscal.class)
				.setParameter("cd_regimen", cd_regimen);
		rf = (RegimenFiscal) sql.getSingleResult();
		return rf;
	}

	@Override
	public List<RegimenFiscal> buscarTodos() {
		List<RegimenFiscal> listaRegimen = null;
		EntityManager em = EntityManagerUtil.getEntityManager();
		listaRegimen = em.createNamedQuery("RegimenFiscal.findAll", RegimenFiscal.class).getResultList();
		return listaRegimen;
	}

	@Override
	public List<RegimenFiscal> buscarPorCriterios(RegimenFiscal r) {
		List<RegimenFiscal> listaRegimen = null;
		EntityManager em = EntityManagerUtil.getEntityManager();
		listaRegimen = em.createNamedQuery("RegimenFiscal.findBycdRegimen", RegimenFiscal.class).setParameter("cd_regimen", r.getCd_regimen()).getResultList();
		return listaRegimen;
	}

	@Override
	public String actualizar(RegimenFiscal r) {
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.merge(r);
			em.getTransaction().commit();
		}catch (Exception e){
			System.out.println("Error" + e.getMessage());
			return "Error";
		}
		return null;
	}

	@Override
	public String guardar(RegimenFiscal r) {
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.persist(r);
			em.getTransaction().commit();
		}catch(Exception e) {
			System.out.println("Error" + e.getMessage());
			return "Error";
		}
		return null;
	}

	@Override
	public String eliminar(RegimenFiscal r) {
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.remove(em.merge(r));
			em.getTransaction().commit();
		}catch(Exception e){
			System.out.println("Error" + e.getMessage());
			return "Error";
		}
		return null;
	}

	@Override
	public String eliminarListado(List<RegimenFiscal> listado) {
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			for(RegimenFiscal rf : listado) {
				em.remove(em.merge(rf));
			}
			em.getTransaction().commit();
		}catch(Exception e) {
			System.out.println("Error" + e.getMessage());
		}
		return null;
	}

}
