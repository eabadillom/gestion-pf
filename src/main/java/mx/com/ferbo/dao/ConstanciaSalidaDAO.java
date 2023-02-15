package mx.com.ferbo.dao;

import java.util.List;

import javax.persistence.EntityManager;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.ConstanciaSalida;
import mx.com.ferbo.util.EntityManagerUtil;

public class ConstanciaSalidaDAO extends IBaseDAO<ConstanciaSalida, Integer> {

	@Override
	public ConstanciaSalida buscarPorId(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ConstanciaSalida> buscarTodos() {
		EntityManager em = EntityManagerUtil.getEntityManager();
		List<ConstanciaSalida> lista = null;
		lista = em.createNamedQuery("ConstanciaSalida.findAll",ConstanciaSalida.class).getResultList();
		
		return lista;
	}

	@Override
	public List<ConstanciaSalida> buscarPorCriterios(ConstanciaSalida e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String actualizar(ConstanciaSalida e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String guardar(ConstanciaSalida e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminar(ConstanciaSalida e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminarListado(List<ConstanciaSalida> listado) {
		// TODO Auto-generated method stub
		return null;
	}

}
