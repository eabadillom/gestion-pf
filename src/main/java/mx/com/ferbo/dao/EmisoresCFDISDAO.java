package mx.com.ferbo.dao;

import static mx.com.ferbo.util.EntityManagerUtil.getEntityManager;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;


import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.EmisoresCFDIS;
import mx.com.ferbo.util.EntityManagerUtil;

public class EmisoresCFDISDAO extends IBaseDAO<EmisoresCFDIS, Integer>{

	@SuppressWarnings("unchecked")
	public List<EmisoresCFDIS> findall() {
		EntityManager entity = getEntityManager();
		List<EmisoresCFDIS> emisores= null;
		Query sql = entity.createNamedQuery("EmisoresCFDIS.findAll", EmisoresCFDIS.class);
		emisores = sql.getResultList();
		return emisores;
	}

	
	@Override
	public EmisoresCFDIS buscarPorId(Integer cd_emisor) {
		/*EmisoresCFDIS emi = null;
		EntityManager em = EntityManagerUtil.getEntityManager();
		emi = em.createNamedQuery("findBycdEmisor", EmisoresCFDIS.class).getSingleResult();*/
		return null;
	}

	@Override
	public List<EmisoresCFDIS> buscarTodos() {
		/*List<EmisoresCFDIS> listaEmisores = null;
		EntityManager em = EntityManagerUtil.getEntityManager();
		listaEmisores = em.createNamedQuery("EmisoresCFDIS.findAll", EmisoresCFDIS.class).getResultList();*/
		return null;
	}

	@Override
	public List<EmisoresCFDIS> buscarPorCriterios(EmisoresCFDIS e) {
		/*List<EmisoresCFDIS> listaEmisores =null;
		EntityManager em = EntityManagerUtil.getEntityManager();
			listaEmisores = em.createNamedQuery("EmisoresCFDIS.findByregimenFiscal", EmisoresCFDIS.class).setParameter("cd_regimen", e.getCd_regimen()).getResultList();*/
		return null;
	}

	@Override
	public String actualizar(EmisoresCFDIS emi) {
		try{
			EntityManager ent = getEntityManager();
			ent.getTransaction().begin();
			ent.merge(emi);
			ent.getTransaction().commit();
			ent.close();
		}catch(Exception e){
			return "Failed!" + e.getMessage();
		}
		return null;
	}

	@Override
	public String guardar(EmisoresCFDIS emi) {
		try {
			EntityManager ent = getEntityManager();
			ent.getTransaction().begin();
			ent.persist(emi);
			ent.getTransaction().commit();
			ent.close();
		}catch(Exception e){
			return "Failed" + e.getMessage();
		}
		return null;
	}

	@Override
	public String eliminar(EmisoresCFDIS emi) {
		try {
			EntityManager ent = getEntityManager();
			ent.getTransaction().begin();
			ent.remove(ent.merge(emi));
			ent.getTransaction().commit();
			ent.close();
		}catch(Exception e) {
			return "Failed!"+e.getMessage();
		}
		return null;
	}

	@Override
	public String eliminarListado(List<EmisoresCFDIS> listado) {
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			for(EmisoresCFDIS emisores : listado) {
				em.remove(em.merge(emisores));
			}
			em.getTransaction().commit();
		}catch(Exception e){
			System.out.println("Error" + e.getMessage());
		}
		return null;
	}

}
