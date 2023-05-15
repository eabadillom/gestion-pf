package mx.com.ferbo.dao;

import static mx.com.ferbo.util.EntityManagerUtil.getEntityManager;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.StatusFactura;
import mx.com.ferbo.model.TipoAsentamiento;
import mx.com.ferbo.model.TipoFacturacion;
import mx.com.ferbo.util.EntityManagerUtil;

public class StatusFacturaDAO extends IBaseDAO<StatusFactura, Integer>{

	@Override
	public StatusFactura buscarPorId(Integer id) {
		EntityManager entity = EntityManagerUtil.getEntityManager();
		StatusFactura sf = entity.createNamedQuery("StatusFactura.findById", StatusFactura.class)
				.setParameter("id",id).getSingleResult();
		return sf;
	}

	@Override
	public List<StatusFactura> buscarTodos() {
		// TODO Auto-generated method stub
		EntityManager em = EntityManagerUtil.getEntityManager();
		return em.createNamedQuery("StatusFactura.findAll", StatusFactura.class).getResultList();
		}

	@Override
	public List<StatusFactura> buscarPorCriterios(StatusFactura e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String actualizar(StatusFactura e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String guardar(StatusFactura e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminar(StatusFactura e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminarListado(List<StatusFactura> listado) {
		// TODO Auto-generated method stub
		return null;
	}

}
