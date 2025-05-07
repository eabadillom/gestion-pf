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

	public UsoCfdiDAO() {
		super(UsoCfdi.class);
	}
	
	Logger log = LogManager.getLogger(UsoCfdiDAO.class);

	@Override
	public List<UsoCfdi> buscarTodos() {
		List<UsoCfdi> modelList = null;
		EntityManager em = null;

		try {
			em = this.getEntityManager();

			modelList = em.createNamedQuery("UsoCfdi.findAll", this.modelClass)
				.getResultList();
		} catch(Exception ex) {
			log.error("Problema para obtener el listado de usos de CFDI...", ex);
		} finally {
			this.close(em);
		}

		return modelList;
	}

	@Override
	public List<UsoCfdi> buscarPorCriterios(UsoCfdi e) {
		throw new UnsupportedOperationException("Unimplemented method 'buscarPorCriterios'");
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
		} catch(Exception ex) {
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
		} catch(Exception ex) {
			log.error("Problema para obtener el listado de Usos del CFDI...", ex);
		} finally {
			EntityManagerUtil.close(em);
		}
		
		return list;
	}

	@Override
	public String eliminarListado(List<UsoCfdi> listado) {
		throw new UnsupportedOperationException("Unimplemented method 'eliminarListado'");
	}
}
