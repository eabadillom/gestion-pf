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
import mx.com.ferbo.util.EntityManagerUtil;

public class ConstanciaDeDepositoDAO extends IBaseDAO<ConstanciaDeDeposito, Integer> {

	public EntityManager em = null;
	
	@Override
	public ConstanciaDeDeposito buscarPorId(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ConstanciaDeDeposito> buscarTodos() {
		EntityManager em = EntityManagerUtil.getEntityManager();
		return em.createNamedQuery("ConstanciaDeDeposito.findAll", ConstanciaDeDeposito.class)
				.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<ConstanciaDeDeposito> buscarPorCriterios(String folioCliente, Date fechaInico, Date fechaFin, int idCliente) {
		// TODO Auto-generated method stub
		
		
		
		Cliente cliente = new Cliente();//creamos un objeto Cliente
		Map<String, Object> paramaterMap = new HashMap<String, Object>();
		List<String> whereCause = new ArrayList<String>();
		StringBuilder queryBuilder = new StringBuilder();//?? guardar strings para la query
		
		try {
			
			Query q = null;
			queryBuilder.append("SELECT cdd FROM ConstanciaDeDeposito cdd");

			if (fechaInico != null && fechaFin != null) {//si ambos parametros son diferente de null entra 
				whereCause.add("(cdd.fechaIngreso BETWEEN :fechaInicio AND :fechaFinal)");//añade a la lista de String que representa los where
				paramaterMap.put("fechaInicio", fechaInico);//seteamos los parametros* 
				paramaterMap.put("fechaFinal", fechaFin);//*
			}
			if (folioCliente != null && !"".equalsIgnoreCase(folioCliente.trim())) {//si el folio es diferente de null y diferente de una  cadena "" entra
				whereCause.add("cdd.folioCliente = :folioCliente");//comparamos folioCliente de constanciadeS de lo que se setea en :folioCliente
				paramaterMap.put("folioCliente", folioCliente);//seteamos folioCliente
			}
			if (idCliente != 0) {//si el id es diferente de 0 entra
				cliente.setCteCve(idCliente);//al objeto instanciado le seteamos el id del parametro idCliente
				whereCause.add("cdd.cteCve = :idCliente");//comparamos el cliente de constanciadeservicio con lo que se seteo al :idCliente
				paramaterMap.put("idCliente", cliente);//seteamos idCliente el objeto cliente instanciado
			}

			queryBuilder.append(" WHERE " + StringUtils.join(whereCause, " AND "));//al query le añadimos los wherecause dependiendo en que condiciones entre

			q = em.createQuery(queryBuilder.toString());//a la sesion le pasamos el query creado que se encuentra en queryBuilder y se lo pasamos como String

			for (String key : paramaterMap.keySet()) {//recorremos las llaves que contiene parameterMap
				q.setParameter(key, paramaterMap.get(key));//por cada key le seteamos a q (query) los parametros con la llave (key=string) y con .get el objeto de la key
			}

			List<ConstanciaDeDeposito> listado = (List<ConstanciaDeDeposito>) q.getResultList();//creamos tipo de lista que se retornara y casteamos el resultado de la query
			
			
			return listado;
		} catch (Exception e) {
			System.out.println("ERROR" + e.getMessage());
			return null;
		}
	}

	@Override
	public String actualizar(ConstanciaDeDeposito e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String guardar(ConstanciaDeDeposito constanciaDeDeposito) {
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.persist(constanciaDeDeposito);
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
	public String eliminar(ConstanciaDeDeposito e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminarListado(List<ConstanciaDeDeposito> listado) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<ConstanciaDeDeposito> buscarPorFolioCliente(ConstanciaDeDeposito cons) {
		EntityManager em = EntityManagerUtil.getEntityManager();
		List<ConstanciaDeDeposito> lstAux = new ArrayList<>();
		lstAux = em.createNamedQuery("ConstanciaDeDeposito.findByFolioCliente",ConstanciaDeDeposito.class)
				.setParameter("folioCliente",cons.getFolioCliente())
				.getResultList();	
		System.out.println(lstAux);
		return lstAux;
	}

	@Override
	public List<ConstanciaDeDeposito> buscarPorCriterios(ConstanciaDeDeposito e) {
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
