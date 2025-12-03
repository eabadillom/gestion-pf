package mx.com.ferbo.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.Pais;
import mx.com.ferbo.util.EntityManagerUtil;

@Deprecated
public class PaisDAO extends IBaseDAO<Pais, Integer> {

	@Override
	public Pais buscarPorId(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Pais> buscarTodos() {
		List<Pais> listado;
		EntityManager em = EntityManagerUtil.getEntityManager();
		listado = em.createNamedQuery("Pais.findAll", Pais.class).getResultList();
		return listado;
	}

	@Override
	public List<Pais> buscarPorCriterios(Pais e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String actualizar(Pais e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String guardar(Pais e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminar(Pais e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminarListado(List<Pais> listado) {
		// TODO Auto-generated method stub
		return null;
	}

	}
