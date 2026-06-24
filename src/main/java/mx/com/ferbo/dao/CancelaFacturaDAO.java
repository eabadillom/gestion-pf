package mx.com.ferbo.dao;

import java.util.List;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.CancelaFactura;
import mx.com.ferbo.util.EntityManagerUtil;

public class CancelaFacturaDAO extends IBaseDAO<CancelaFactura, Integer> {
	private static Logger log = LogManager.getLogger(CancelaFacturaDAO.class);

	@Override
	public CancelaFactura buscarPorId(Integer id) {
		//  Auto-generated method stub
		return null;
	}

	@Override
	public List<CancelaFactura> buscarTodos() {
		//  Auto-generated method stub
		return null;
	}

	@Override
	public List<CancelaFactura> buscarPorCriterios(CancelaFactura e) {
		//  Auto-generated method stub
		return null;
	}

	@Override
	public String actualizar(CancelaFactura e) {
		EntityManager em = null;

		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			e = em.merge(e);
			em.getTransaction().commit();
		} catch (Exception ex) {
			log.error("Problema al guardar la cancelación de la factura...", ex);
			return "Problema al guardar la cancelación de la factura.";
		} finally {
			EntityManagerUtil.close(em);
		}

		return null;
	}

	@Override
	public String guardar(CancelaFactura e) {
		EntityManager em = null;

		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.persist(e);
			em.getTransaction().commit();
		} catch (Exception ex) {
			log.error("Problema al guardar la cancelación de la factura...", ex);
			return "Problema al guardar la cancelación de la factura.";
		} finally {
			EntityManagerUtil.close(em);
		}

		return null;
	}

	@Override
	public String eliminar(CancelaFactura e) {
		//  Auto-generated method stub
		return null;
	}

	@Override
	public String eliminarListado(List<CancelaFactura> listado) {
		//  Auto-generated method stub
		return null;
	}

}
