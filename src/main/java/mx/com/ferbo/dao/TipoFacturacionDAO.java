package mx.com.ferbo.dao;

import java.util.List;

import javax.persistence.EntityManager;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.TipoFacturacion;
import mx.com.ferbo.util.EntityManagerUtil;

public class TipoFacturacionDAO extends IBaseDAO<TipoFacturacion, Integer> {
	{

	}

	@Override
	public TipoFacturacion buscarPorId(Integer id) {
		EntityManager entity = EntityManagerUtil.getEntityManager();
		TipoFacturacion TP = entity.createNamedQuery("TipoFacturacion.findById", TipoFacturacion.class)
				.setParameter("id", id).getSingleResult();
		return TP;
	}

	@Override
	public List<TipoFacturacion> buscarTodos() {
		return null;
	}

	@Override
	public List<TipoFacturacion> buscarPorCriterios(TipoFacturacion e) {
		return null;
	}

	@Override
	public String actualizar(TipoFacturacion e) {
		return null;
	}

	@Override
	public String guardar(TipoFacturacion e) {
		return null;
	}

	@Override
	public String eliminar(TipoFacturacion e) {
		return null;
	}

	@Override
	public String eliminarListado(List<TipoFacturacion> listado) {
		return null;
	}

}
