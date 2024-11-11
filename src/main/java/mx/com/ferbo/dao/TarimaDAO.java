package mx.com.ferbo.dao;

import java.util.List;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.Tarima;

public class TarimaDAO extends IBaseDAO<Tarima, Integer>{
	
	private static Logger log = LogManager.getLogger(TarimaDAO.class);
	
	public TarimaDAO() {
		this.modelClass = Tarima.class;
	}
	
	public List<Tarima> buscarPorFolio(Integer folio) {
		List<Tarima> tarimas = null;
		EntityManager em = null;
		
		try {
			em = this.getEntityManager();
			tarimas = em.createNamedQuery("Tarima.findByFolio", modelClass)
			.setParameter("folio", folio)
			.getResultList()
			;
		} catch(Exception ex) {
			log.error("Problema para obtener el listado de tarimas por folio...", ex);
		} finally {
			this.close(em);
		}
		
		return tarimas;
	}

	@Override
	public List<Tarima> buscarTodos() {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<Tarima> buscarPorCriterios(Tarima e) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String eliminarListado(List<Tarima> listado) {
		throw new UnsupportedOperationException();
	}

	

}
