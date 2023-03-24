package mx.com.ferbo.dao;

import java.util.List;

import javax.persistence.EntityManager;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.EstadoConstancia;
import mx.com.ferbo.util.EntityManagerUtil;

public class EstadoConstanciaDAO extends IBaseDAO<EstadoConstancia, Integer> {

	@Override
	public EstadoConstancia buscarPorId(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<EstadoConstancia> buscarTodos() {
		EntityManager entity = null;
		List<EstadoConstancia> alEstados = null;
		try {
			entity = EntityManagerUtil.getEntityManager();
			alEstados = entity.createNamedQuery("EstadoConstancia.findAll", EstadoConstancia.class)
					.getResultList();
		} finally {
			entity.close();
		}
		return alEstados;
	}

	@Override
	public List<EstadoConstancia> buscarPorCriterios(EstadoConstancia e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String actualizar(EstadoConstancia e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String guardar(EstadoConstancia e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminar(EstadoConstancia e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminarListado(List<EstadoConstancia> listado) {
		// TODO Auto-generated method stub
		return null;
	}

}
