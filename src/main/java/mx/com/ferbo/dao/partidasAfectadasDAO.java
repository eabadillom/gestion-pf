package mx.com.ferbo.dao;

import java.util.List;
import static mx.com.ferbo.util.EntityManagerUtil.getEntityManager;
import javax.persistence.EntityManager;
import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.util.EntityManagerUtil;
import mx.com.ferbo.model.PartidasAfectadas;

public class partidasAfectadasDAO extends IBaseDAO<PartidasAfectadas, Integer>{

	@Override
	public PartidasAfectadas buscarPorId(Integer id) {
		PartidasAfectadas pa = null;
		EntityManager em = EntityManagerUtil.getEntityManager();
		pa = em.createNamedQuery("findByID", PartidasAfectadas.class).getSingleResult();
		return pa;
	}

	@Override
	public List<PartidasAfectadas> buscarTodos() {
		List<PartidasAfectadas> listapa = null;
		EntityManager em = EntityManagerUtil.getEntityManager();
		listapa = em.createNamedQuery("",PartidasAfectadas.class).getResultList();
		return listapa;
	}

	@Override
	public List<PartidasAfectadas> buscarPorCriterios(PartidasAfectadas e) {
		return null;
	}

	@Override
	public String actualizar(PartidasAfectadas p) {
		try {
			EntityManager entity = getEntityManager();
			entity.getTransaction().begin();
			entity.merge(p);
			entity.getTransaction().commit();
			entity.close();
			System.out.println(p);
		}catch(Exception e){
			return "Failed!" + e.getMessage();
		}
		return null;
	}

	@Override
	public String guardar(PartidasAfectadas p) {
		try {
			EntityManager entity= getEntityManager();
			entity.getTransaction().begin();
			entity.persist(p);
			entity.getTransaction().commit();
			entity.close();
		}catch(Exception e){
			return "Failed!! " + e.getMessage();
		} return null;
	}

	@Override
	public String eliminar(PartidasAfectadas p) {
		try {
			EntityManager entity = getEntityManager();
			entity.getTransaction().begin();
			entity.remove(entity.merge(p));
			entity.getTransaction().commit();
			entity.close();
		}catch(Exception e){
			return "Failed" + e.getMessage();
		}
		return null;
	}

	@Override
	public String eliminarListado(List<PartidasAfectadas> listado) {
		
		return null;
	}

}
