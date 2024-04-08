package mx.com.ferbo.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.UsoCfdi;
import mx.com.ferbo.util.EntityManagerUtil;

public class UsoCfdiDAO extends IBaseDAO<UsoCfdi, String> {

	Logger log = LogManager.getLogger(UsoCfdiDAO.class);

	@Override
	public UsoCfdi buscarPorId(String id) {
		return null;
	}

	@Override
	public List<UsoCfdi> buscarTodos() {
		return null;
	}

	@Override
	public List<UsoCfdi> buscarPorCriterios(UsoCfdi e) {
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<UsoCfdi> buscaPorPersonaFisica() {
		List<UsoCfdi> list = null;
		EntityManager em = null;
		Query query = null;

		try {
			em = EntityManagerUtil.getEntityManager();
			query = em.createNamedQuery("UsoCfdi.findByPersonaFisica", UsoCfdi.class);
			list = query.getResultList();
		} catch (Exception ex) {
			log.error("Problema para obtener el listado de Usos del CFDI...", ex);
		} finally {
			EntityManagerUtil.close(em);
		}

		return list;
	}

	@SuppressWarnings("unchecked")
	public List<UsoCfdi> buscaPorPersonaMoral() {
		List<UsoCfdi> list = null;
		EntityManager em = null;
		Query query = null;

		try {
			em = EntityManagerUtil.getEntityManager();
			query = em.createNamedQuery("UsoCfdi.findByPersonaMoral", UsoCfdi.class);
			list = query.getResultList();
		} catch (Exception ex) {
			log.error("Problema para obtener el listado de Usos del CFDI...", ex);
		} finally {
			EntityManagerUtil.close(em);
		}

		return list;
	}

	@Override
	public String actualizar(UsoCfdi e) {
		return null;
	}

	@Override
	public String guardar(UsoCfdi e) {
		return null;
	}

	@Override
	public String eliminar(UsoCfdi e) {
		return null;
	}

	@Override
	public String eliminarListado(List<UsoCfdi> listado) {
		return null;
	}

}
