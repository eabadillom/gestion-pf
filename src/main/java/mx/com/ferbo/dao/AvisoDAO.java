package mx.com.ferbo.dao;

import java.util.List;

import javax.persistence.EntityManager;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.Aviso;
import mx.com.ferbo.util.EntityManagerUtil;

public class AvisoDAO extends IBaseDAO<Aviso, Integer>{

	@Override
	public Aviso buscarPorId(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Aviso> buscarTodos() {
		// TODO Auto-generated method stub
		return null;
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
	
	public List<Aviso> buscarPorCliente(Aviso e){
		EntityManager em = EntityManagerUtil.getEntityManager();
		return em.createNamedQuery("Aviso.findByAvisoCliente", Aviso.class)
				.setParameter("cteCve", e.getCteCve().getCteCve())
				.getResultList();	
		}

}
