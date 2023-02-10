package mx.com.ferbo.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.ConstanciaDeServicio;
import mx.com.ferbo.util.EntityManagerUtil;

public class ConstanciaServicioDAO extends IBaseDAO<ConstanciaDeServicio, Integer> {
	
	public EntityManager em = null;
	
	public ConstanciaServicioDAO() {
	}
	
	public ConstanciaServicioDAO(EntityManager em) {
		this.em = em;
	}

	@Override
	public ConstanciaDeServicio buscarPorId(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public List<ConstanciaDeServicio> buscarPorFolioCliente(String folioCliente) {
		List<ConstanciaDeServicio> alConstancias = null;
		EntityManager manager = null;
		
		try {
			manager = EntityManagerUtil.getEntityManager();
			manager.getTransaction().begin();
			alConstancias = manager.createNamedQuery("ConstanciaDeServicio.findByFolioCliente", ConstanciaDeServicio.class)
					.setParameter("folioCliente", folioCliente)
					.getResultList();
			
			manager.getTransaction().commit();
		} catch(Exception ex) {
			manager.getTransaction().rollback();
		} finally {
			if(manager != null)
				manager.close();
		}
		
		return alConstancias;
	}

	@Override
	public List<ConstanciaDeServicio> buscarTodos() {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<ConstanciaDeServicio> buscarPorCriterio(String folioCliente, Date fechaInico, Date fechaFin, int idCliente) {
		Cliente cliente = new Cliente();
		Map<String, Object> paramaterMap = new HashMap<String, Object>();
		List<String> whereCause = new ArrayList<String>();
		StringBuilder queryBuilder = new StringBuilder();
		
		try {
			Query q = null;
			queryBuilder.append("SELECT cds FROM ConstanciaDeServicio cds");

			if (fechaInico != null && fechaFin != null) {
				whereCause.add("(cds.fecha BETWEEN :fechaInicio AND :fechaFinal)");
				paramaterMap.put("fechaInicio", fechaInico);
				paramaterMap.put("fechaFinal", fechaFin);
			}
			if (folioCliente != null && !"".equalsIgnoreCase(folioCliente.trim())) {
				whereCause.add("cds.folioCliente = :folioCliente");
				paramaterMap.put("folioCliente", folioCliente);
			}
			if (idCliente != 0) {
				cliente.setCteCve(idCliente);
				whereCause.add("cds.cteCve = :idCliente");
				paramaterMap.put("idCliente", cliente);
			}

			queryBuilder.append(" WHERE " + StringUtils.join(whereCause, " AND "));

			q = em.createQuery(queryBuilder.toString());

			for (String key : paramaterMap.keySet()) {
				q.setParameter(key, paramaterMap.get(key));
			}

			List<ConstanciaDeServicio> listado = (List<ConstanciaDeServicio>) q.getResultList();

			return listado;
		} catch (Exception e) {
			System.out.println("ERROR" + e.getMessage());
			return null;
		}
	}

	@Override
	public List<ConstanciaDeServicio> buscarPorCriterios(ConstanciaDeServicio constanciaDeServicio) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String actualizar(ConstanciaDeServicio e) {
		EntityManager entity = null;
		try {
			entity = EntityManagerUtil.getEntityManager();
			entity.getTransaction().begin();
			entity.merge(e);
			entity.getTransaction().commit();
		} catch(Exception ex) {
			entity.getTransaction().rollback();
			ex.printStackTrace();
		}finally {
			entity.close();
		}
	
		return null;
	}

	@Override
	public String guardar(ConstanciaDeServicio e) {
		EntityManager entity = null;
		try {
			entity = EntityManagerUtil.getEntityManager();
			entity.getTransaction().begin();
			entity.persist(e);
			entity.getTransaction().commit();
		} catch(Exception ex) {
			entity.getTransaction().rollback();
			ex.printStackTrace();
		} finally {
			if(entity != null)
				entity.close();
		}
		return null;
	}

	@Override
	public String eliminar(ConstanciaDeServicio e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminarListado(List<ConstanciaDeServicio> listado) {
		// TODO Auto-generated method stub
		return null;
	}

	public EntityManager getEntityManager() {
		return em;
	}

	public void setEntityManager(EntityManager em) {
		this.em = em;
	}
}
