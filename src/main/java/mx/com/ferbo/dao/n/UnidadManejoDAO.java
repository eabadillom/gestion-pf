package mx.com.ferbo.dao.n;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.UnidadDeManejo;
import mx.com.ferbo.util.DAOException;

@Named
@ApplicationScoped
public class UnidadManejoDAO extends BaseDAO<UnidadDeManejo, Integer> {

	private static Logger log = LogManager.getLogger(UnidadManejoDAO.class);

	public UnidadManejoDAO() {
		super(UnidadDeManejo.class);
	}

	public List<UnidadDeManejo> buscarTodos() throws DAOException {
		EntityManager em = null;
		try {
			em = super.getEntityManager();
			return em.createNamedQuery("UnidadDeManejo.findAll", UnidadDeManejo.class)
					.getResultList();
		} catch (Exception ex) {
			log.error("Error al listar unidades de manejo", ex);
			throw new DAOException("No fue posible listar las unidades de manejo", ex);
		} finally {
			super.close(em);
		}
	}

}