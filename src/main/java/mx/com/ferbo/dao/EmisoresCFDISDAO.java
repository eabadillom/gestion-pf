package mx.com.ferbo.dao;

import static mx.com.ferbo.util.EntityManagerUtil.getEntityManager;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.hibernate.validator.internal.util.privilegedactions.GetResource;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.EmisoresCFDIS;
import mx.com.ferbo.model.Usuario;
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
		EmisoresCFDIS emi = null;
		EntityManager em = EntityManagerUtil.getEntityManager();
		emi = em.createNamedQuery("findBycdEmisor", EmisoresCFDIS.class).getSingleResult();
		return emi;
	}

	@Override
	public List<EmisoresCFDIS> buscarTodos() {
		List<EmisoresCFDIS> listaEmisores = null;
		EntityManager em = EntityManagerUtil.getEntityManager();
		listaEmisores = em.createNamedQuery("", EmisoresCFDIS.class).getResultList();
		return listaEmisores;
	}

	@Override
	public List<EmisoresCFDIS> buscarPorCriterios(EmisoresCFDIS e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String actualizar(EmisoresCFDIS e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String guardar(EmisoresCFDIS e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminar(EmisoresCFDIS e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminarListado(List<EmisoresCFDIS> listado) {
		// TODO Auto-generated method stub
		return null;
	}

}
