package mx.com.ferbo.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.CategoriaEgreso;
import mx.com.ferbo.model.EmisoresCFDIS;
import mx.com.ferbo.model.egresos;
import mx.com.ferbo.util.EntityManagerUtil;

public class egresosDAO extends IBaseDAO<egresos, Integer>{
	private static final long serialVersionUID = -586280005718635555L;
	private static Logger log = LogManager.getLogger(egresosDAO.class);
	
	@SuppressWarnings("unchecked")
		
	public List<egresos> findByAll() {
		List<egresos> listaEgresos = null;
		EntityManager entity = null;
		
		try {
			entity = EntityManagerUtil.getEntityManager();
			listaEgresos = entity.createNamedQuery("egresos.findByAll", egresos.class).getResultList();
		}catch(Exception e) {
			log.error("Error al obtener informacion",e);
		}finally {
			EntityManagerUtil.close(entity);
		}
		return listaEgresos;
	}

	@Override
	public egresos buscarPorId(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<egresos> buscarTodos() {
		List<egresos> listaEgresos = null;
		EntityManager entity = null;
		
		try {
			entity = EntityManagerUtil.getEntityManager();
			listaEgresos = entity.createNamedQuery("egresos.findByAll", egresos.class).getResultList();
		}catch(Exception e) {
			log.error("Error al obtener informacion",e);
		}finally {
			EntityManagerUtil.close(entity);
		}
		return listaEgresos;
	}

	@Override
	public List<egresos> buscarPorCriterios(egresos e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String actualizar(egresos e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String guardar(egresos es) {
		EntityManager entity = null;
		try {
			 entity = EntityManagerUtil.getEntityManager();
			entity.getTransaction().begin();
			entity.persist(es);
			entity.getTransaction().commit();
			entity.close();
		} catch (Exception e) {
			return e.getMessage();
		}finally {
			EntityManagerUtil.close(entity);
		}
		return null;
	};

	@Override
	public String eliminar(egresos e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminarListado(List<egresos> listado) {
		// TODO Auto-generated method stub
		return null;
	};
	
}
