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
import mx.com.ferbo.model.ConstanciaDeDeposito;
import mx.com.ferbo.model.ConstanciaDepositoDetalle;
import mx.com.ferbo.model.ConstanciaSalida;
import mx.com.ferbo.util.EntityManagerUtil;

public class ConstanciaSalidaDAO extends IBaseDAO<ConstanciaSalida, Integer> {

	public EntityManager em = null;
	
	@Override
	public ConstanciaSalida buscarPorId(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ConstanciaSalida> buscarTodos() {
		EntityManager em = EntityManagerUtil.getEntityManager();
		List<ConstanciaSalida> lista = null;
		lista = em.createNamedQuery("ConstanciaSalida.findAll",ConstanciaSalida.class).getResultList();
		
		return lista;
	}

	@Override
	public List<ConstanciaSalida> buscarPorCriterios(ConstanciaSalida e) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<ConstanciaSalida> buscarPorCriterios(String folioCliente, Date fechaInico, Date fechaFin, int idCliente) {

		// TODO Auto-generated method stub
		
		Cliente cliente = new Cliente();
		Map<String, Object> paramaterMap = new HashMap<String, Object>();
		List<String> whereCause = new ArrayList<String>();
		StringBuilder queryBuilder = new StringBuilder();
		
		try {
			
			Query q = null;
			queryBuilder.append("SELECT c FROM ConstanciaSalida c");

			if (fechaInico != null && fechaFin != null) {
				whereCause.add("(c.fecha BETWEEN :fechaInicio AND :fechaFinal)");
				paramaterMap.put("fechaInicio", fechaInico);
				paramaterMap.put("fechaFinal", fechaFin);
			}
			if (folioCliente != null && !"".equalsIgnoreCase(folioCliente.trim())) {
				whereCause.add("c.numero = :folioCliente");
				paramaterMap.put("folioCliente", folioCliente);
			}
			if (idCliente != 0) {
				cliente.setCteCve(idCliente);
				whereCause.add("c.clienteCve = :idCliente");
				paramaterMap.put("idCliente", cliente);
			}

			queryBuilder.append(" WHERE " + StringUtils.join(whereCause, " AND "));

			q = em.createQuery(queryBuilder.toString());

			for (String key : paramaterMap.keySet()) {
				q.setParameter(key, paramaterMap.get(key));
			}

			List<ConstanciaSalida> listado = (List<ConstanciaSalida>) q.getResultList();
			
			
			return listado;
		} catch (Exception e) {
			System.out.println("ERROR" + e.getMessage());
			return null;
		}
	}

	@Override
	public String actualizar(ConstanciaSalida constanciaSalida) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public String actualizarStatus(ConstanciaSalida constanciaSalida) {
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			Query actualizar = em.createNativeQuery(" UPDATE CONSTANCIA_SALIDA SET STATUS  = :status WHERE ID = :id ") ;
			actualizar.setParameter("status",(constanciaSalida.getStatus()==null) ? 1:2);//si constancia de salida status es null colocar 1 en otro caso colocar 2
			actualizar.setParameter("id", constanciaSalida.getId());
			actualizar.executeUpdate();
			em.getTransaction().commit();
			em.close();
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("ERROR "+e.getMessage());
			return "ERROR";
		}
		
		return null;
	}

	@Override
	public String guardar(ConstanciaSalida constanciaSalida) {
		// TODO Auto-generated method stub
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.persist(constanciaSalida);//guarda el servicio dado (NO es gestionado)
			em.getTransaction().commit();
			em.close();
		} catch (Exception e) {
			System.out.println("ERROR" + e.getMessage());
			return "ERROR";
		}
		
		return null;
	}
	
	public EntityManager getEm() {
		return em;
	}

	public void setEm(EntityManager em) {
		this.em = em;
	}

	@Override
	public String eliminar(ConstanciaSalida e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminarListado(List<ConstanciaSalida> listado) {
		// TODO Auto-generated method stub
		return null;
	}

}
