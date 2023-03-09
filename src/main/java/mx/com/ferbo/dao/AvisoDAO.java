package mx.com.ferbo.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.Aviso;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.util.EntityManagerUtil;

public class AvisoDAO extends IBaseDAO<Aviso,Integer>{

	@SuppressWarnings("unchecked")
	public List<Aviso> findall() {
		EntityManager entity = EntityManagerUtil.getEntityManager();
		List<Aviso> aviso= null;
		Query sql = entity.createNamedQuery("Aviso.findAll", Aviso.class);
		aviso = sql.getResultList();
		return aviso;
	}	
	
	@Override
	public Aviso buscarPorId(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Aviso> buscarTodos() {
		EntityManager em = EntityManagerUtil.getEntityManager();
		List<Aviso> lista = null;
		lista = em.createNamedQuery("Aviso.findAll", Aviso.class).getResultList();

		return lista;
	}

	@Override
	public List<Aviso> buscarPorCriterios(Aviso e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String actualizar(Aviso e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String guardar(Aviso e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminar(Aviso e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminarListado(List<Aviso> listado) {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
