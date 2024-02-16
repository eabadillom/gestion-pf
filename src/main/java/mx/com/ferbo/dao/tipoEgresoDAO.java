package mx.com.ferbo.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.CategoriaEgreso;
import mx.com.ferbo.model.SerieFactura;
import mx.com.ferbo.model.TipoEgreso;
import mx.com.ferbo.util.EntityManagerUtil;

public class tipoEgresoDAO  extends IBaseDAO<TipoEgreso, Integer>{
	private static Logger log = LogManager.getLogger(tipoEgresoDAO.class);

	public List<TipoEgreso> findByAll(){
		EntityManager entity = null;
		List<TipoEgreso> list = null;
		
		try {
			entity = EntityManagerUtil.getEntityManager();
			list = entity.createNamedQuery("TipoEgreso.findByAll", TipoEgreso.class).getResultList();
		}catch(Exception e) {
			log.error("Error al obtener informacion",e);
		}finally {
			EntityManagerUtil.close(entity);
		}
		return list;
	};
	
public TipoEgreso findById(Integer idTipo) {
		
		EntityManager em = null;
		TipoEgreso t = null;
		Query query = null;
		
		try {
			em = EntityManagerUtil.getEntityManager();
			query = em.createNamedQuery("SerieFactura.findById", TipoEgreso.class).setParameter("id",idTipo);
			
			t = (TipoEgreso) query.getSingleResult();
			
		} catch (Exception e) {
			log.info("Error al buscar Serie Factura por ID", e.getMessage());
			return null;
		}finally {
			EntityManagerUtil.close(em);
		}
		
		return t;
	}
public String save(TipoEgreso sF) {
	EntityManager entity = null;
	try {
		 entity = EntityManagerUtil.getEntityManager();
		entity.getTransaction().begin();
		entity.persist(sF);
		entity.getTransaction().commit();
		entity.close();
	} catch (Exception e) {
		return e.getMessage();
	}finally {
		EntityManagerUtil.close(entity);
	}
	return null;
}

@Override
public TipoEgreso buscarPorId(Integer id) {
	// TODO Auto-generated method stub
	return null;
}

@Override
public List<TipoEgreso> buscarTodos() {
	// TODO Auto-generated method stub
	return null;
}

@Override
public List<TipoEgreso> buscarPorCriterios(TipoEgreso e) {
	// TODO Auto-generated method stub
	return null;
}

@Override
public String actualizar(TipoEgreso e) {
	// TODO Auto-generated method stub
	return null;
}

@Override
public String guardar(TipoEgreso e) {
	// TODO Auto-generated method stub
	return null;
}

@Override
public String eliminar(TipoEgreso e) {
	// TODO Auto-generated method stub
	return null;
}

@Override
public String eliminarListado(List<TipoEgreso> listado) {
	// TODO Auto-generated method stub
	return null;
};
	
}
