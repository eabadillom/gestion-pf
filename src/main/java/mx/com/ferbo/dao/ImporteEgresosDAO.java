package mx.com.ferbo.dao;

import java.util.List;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.ImporteEgreso;
import mx.com.ferbo.model.egresos;
import mx.com.ferbo.util.EntityManagerUtil;

public class ImporteEgresosDAO extends IBaseDAO<ImporteEgreso, Integer>{
	
	private static final long serialVersionUID = -586280005718635555L;
	private static Logger log = LogManager.getLogger(ImporteEgresosDAO.class);
	
	
	@Override
	public ImporteEgreso buscarPorId(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<ImporteEgreso> buscarTodos() {
		List<ImporteEgreso> listaEgresos = null;
		EntityManager entity = null;
		
		try {
			entity = EntityManagerUtil.getEntityManager();
			listaEgresos = entity.createNamedQuery("ImporteEgreso.findByAll", ImporteEgreso.class).getResultList();
		}catch(Exception e) {
			log.error("Error al obtener informacion",e);
		}finally {
			EntityManagerUtil.close(entity);
		}
		return listaEgresos;
	}
	
	@Override
	public List<ImporteEgreso> buscarPorCriterios(ImporteEgreso e) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String actualizar(ImporteEgreso e) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String guardar(ImporteEgreso e) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String eliminar(ImporteEgreso e) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String eliminarListado(List<ImporteEgreso> listado) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	
}