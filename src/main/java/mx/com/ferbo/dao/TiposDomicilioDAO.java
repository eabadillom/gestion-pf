package mx.com.ferbo.dao;

import java.util.List;

import javax.persistence.EntityManager;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.TiposDomicilio;
import mx.com.ferbo.util.EntityManagerUtil;

@Deprecated
public class TiposDomicilioDAO extends IBaseDAO<TiposDomicilio, Integer> {

	@Override
	public TiposDomicilio buscarPorId(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TiposDomicilio> buscarTodos() {
		List<TiposDomicilio> listado;
		EntityManager em = EntityManagerUtil.getEntityManager();
		listado = em.createNamedQuery("TiposDomicilio.findAll", TiposDomicilio.class).getResultList();
		return listado;
	}

	@Override
	public List<TiposDomicilio> buscarPorCriterios(TiposDomicilio e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String actualizar(TiposDomicilio productoCliente) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String guardar(TiposDomicilio prodCliente) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminar(TiposDomicilio prodCliente) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminarListado(List<TiposDomicilio> listado) {
		// TODO Auto-generated method stub
		return null;
	}

}
