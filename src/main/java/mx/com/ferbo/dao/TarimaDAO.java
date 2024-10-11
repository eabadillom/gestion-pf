package mx.com.ferbo.dao;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.Tarima;

public class TarimaDAO extends IBaseDAO<Tarima, Integer>{
	
	private static Logger log = LogManager.getLogger(TarimaDAO.class);
	
	public TarimaDAO() {
		this.modelClass = Tarima.class;
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
