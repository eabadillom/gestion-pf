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
import mx.com.ferbo.model.ConstanciaTraspaso;
import mx.com.ferbo.util.EntityManagerUtil;

public class ConstanciaTraspasoDAO extends IBaseDAO<ConstanciaTraspaso, Integer>{

	public EntityManager em = null;
	@Override
	public ConstanciaTraspaso buscarPorId(Integer id) {
		// TODO Auto-generated method stub
		EntityManager em = EntityManagerUtil.getEntityManager();
		return em.createNamedQuery("ConstanciaTraspaso.findById", ConstanciaTraspaso.class).
				setParameter("id", id).getSingleResult();		
	}

	public List<ConstanciaTraspaso> buscarporNumero(String numero){
		EntityManager em = EntityManagerUtil.getEntityManager();
		return em.createNamedQuery("ConstanciaTraspaso.findByNumero", ConstanciaTraspaso.class).
				setParameter("numero", numero).getResultList();
	}
	@Override
	public List<ConstanciaTraspaso> buscarTodos() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public List<ConstanciaTraspaso> buscarPorCriterios(String numero, Date fechaInico, Date fechaFin, int idCliente) {
		EntityManager em = EntityManagerUtil.getEntityManager();	
		Cliente cliente = new Cliente();
			Map<String, Object> paramaterMap = new HashMap<String, Object>();
			List<String> whereCause = new ArrayList<String>();
			StringBuilder queryBuilder = new StringBuilder();
			
			try {
				Query q = null;
				queryBuilder.append("SELECT ct FROM ConstanciaTraspaso ct");
				/*/
				if (fechaInico != null && fechaFin != null) {
					whereCause.add("(ct.fecha BETWEEN :fechaInicio AND :fechaFinal)");
					paramaterMap.put("fechaInicio", fechaInico);
					paramaterMap.put("fechaFinal", fechaFin);
				}*/
				if (numero != null && !"".equalsIgnoreCase(numero.trim())) {
					whereCause.add("ct.numero = :folioCliente");
					paramaterMap.put("numero", numero);
				}
				if (idCliente != 0) {
					cliente.setCteCve(idCliente);
					whereCause.add("ct.cliente.cteCve = :cteCve");
					paramaterMap.put("cteCve", idCliente);
				}

				queryBuilder.append(" WHERE " + StringUtils.join(whereCause, " AND "));

				q = em.createQuery(queryBuilder.toString());

				for (String key : paramaterMap.keySet()) {
					q.setParameter(key, paramaterMap.get(key));
				}

				List<ConstanciaTraspaso > listado = (List<ConstanciaTraspaso >) q.getResultList();

				return listado;
			} catch (Exception e) {
				System.out.println("ERROR" + e.getMessage());
				return null;
			}
	}

	@Override
	public String actualizar(ConstanciaTraspaso constancia) {
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.persist(constancia);
			em.getTransaction().commit();
			em.close();
		}catch (Exception e) {
			System.out.println("ERROR" + e.getMessage());
			e.printStackTrace();
			e.getCause();
			return "ERROR";
		}
		
		return null;
	}

	@Override
	public String guardar(ConstanciaTraspaso constancia) {
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.persist(constancia);
			em.getTransaction().commit();
			em.close();
		}catch (Exception e) {
			System.out.println("ERROR" + e.getMessage());
			e.printStackTrace();
			e.getCause();
			return "ERROR";
		}
		
		return null;
	}

	@Override
	public String eliminar(ConstanciaTraspaso e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminarListado(List<ConstanciaTraspaso> listado) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ConstanciaTraspaso> buscarPorCriterios(ConstanciaTraspaso e) {
		// TODO Auto-generated method stub
		return null;
	}

	public EntityManager getEm() {
		return em;
	}

	public void setEm(EntityManager em) {
		this.em = em;
	}
	

}
