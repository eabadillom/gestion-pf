package mx.com.ferbo.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.UsoCfdi;
import mx.com.ferbo.util.EntityManagerUtil;

public class UsoCfdiDAO extends IBaseDAO<UsoCfdi, String> {
	
	Logger log = Logger.getLogger(UsoCfdiDAO.class);

	@Override
	public UsoCfdi buscarPorId(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UsoCfdi> buscarTodos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UsoCfdi> buscarPorCriterios(UsoCfdi e) {
		// TODO Auto-generated method stub
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
	public String actualizar(UsoCfdi e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String guardar(UsoCfdi e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminar(UsoCfdi e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminarListado(List<UsoCfdi> listado) {
		// TODO Auto-generated method stub
		return null;
	}

	

}
