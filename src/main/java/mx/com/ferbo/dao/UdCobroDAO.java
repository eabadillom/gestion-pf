package mx.com.ferbo.dao;

import java.util.List;

import javax.persistence.EntityManager;

import org.apache.log4j.Logger;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.UdCobro;
import mx.com.ferbo.util.EntityManagerUtil;

public class UdCobroDAO extends IBaseDAO<UdCobro, Integer>{
	private static Logger log = Logger.getLogger(UdCobroDAO.class);

	@Override
	public UdCobro buscarPorId(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UdCobro> buscarTodos() {
		List<UdCobro> lista = null;
		EntityManager em = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			lista = em.createNamedQuery("UdCobro.findAll", UdCobro.class).getResultList();
		} catch(Exception ex) {
			log.error("Problema para obtener el listado de unidades de cobro...", ex);
		} finally {
			EntityManagerUtil.close(em);
		}
		return lista;
	}

	@Override
	public List<UdCobro> buscarPorCriterios(UdCobro e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String actualizar(UdCobro e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String guardar(UdCobro e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminar(UdCobro e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminarListado(List<UdCobro> listado) {
		// TODO Auto-generated method stub
		return null;
	}

}
