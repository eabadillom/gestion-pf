package mx.com.ferbo.dao;

import static mx.com.ferbo.util.EntityManagerUtil.getEntityManager;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.Certificado;
import mx.com.ferbo.util.EntityManagerUtil;

public class CertificadoDAO extends IBaseDAO<Certificado, Integer> {

	@SuppressWarnings("unchecked")
	public List<Certificado> findAll() {
		EntityManager entity = getEntityManager();
		List<Certificado> certi = null;
		Query sql = entity.createNamedQuery("Certificado.findAll", Certificado.class);
		certi = sql.getResultList();
		return certi;
	}

	@Override
	public Certificado buscarPorId(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	public Certificado buscarporFecha() {
		EntityManager entity = getEntityManager();
		Certificado dtCertificado = null;
		Query sql = entity.createNamedQuery("Certificado.findByFecha", Certificado.class);
		dtCertificado = (Certificado) sql.getSingleResult();
		return dtCertificado;
	}

	public List<Certificado> buscarporcdEmisor(Integer emisor) {
		List<Certificado> listaCertificado = null;
		EntityManager entity = getEntityManager();
		Query sql = entity.createNamedQuery("Certificado.findByemisor", Certificado.class).setParameter("emisor",
				emisor);
		listaCertificado = sql.getResultList();
		return listaCertificado;
	}

	@Override
	public List<Certificado> buscarTodos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Certificado> buscarPorCriterios(Certificado e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String actualizar(Certificado c) {
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.merge(c);
			em.getTransaction().commit();
			em.close();
		} catch (Exception e) {
			System.out.println("Error al guardar datos" + e.getMessage());
			return "ERROR";
		}
		return null;
	}

	@Override
	public String guardar(Certificado e) {
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.persist(e);
			em.getTransaction().commit();
			em.close();
		} catch (Exception ex) {
			System.out.println("Error al guardar datos" + ex.getMessage());
			return "ERROR";
		}
		return null;
	}

	@Override
	public String eliminar(Certificado e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminarListado(List<Certificado> listado) {
		// TODO Auto-generated method stub
		return null;
	}

}
