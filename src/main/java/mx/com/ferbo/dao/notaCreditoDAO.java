package mx.com.ferbo.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;

import mx.com.ferbo.util.EntityManagerUtil;
import mx.com.ferbo.util.JPAEntity;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.ConstanciaDeDeposito;
import mx.com.ferbo.model.NotaCredito;

public class notaCreditoDAO {
	
	EntityManager entity = JPAEntity.getEntity().createEntityManager();
	EntityManager em = null;
	
	@SuppressWarnings("unchecked")
	public List<NotaCredito> findAll(){
		List<NotaCredito> notaCredito;
		Query sql = entity.createNamedQuery("NotaCredito.findAll", NotaCredito.class);
		notaCredito = sql.getResultList();
		System.out.println(notaCredito + "*****************************************************");
		return notaCredito;
	}
	
	public void guardar(NotaCredito notaCredito) {
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.persist(notaCredito);
			em.getTransaction().commit();
			em.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
	}
	
	public List<NotaCredito> buscarPorCriterios(Date fechaInico, Date fechaFin, int idCliente) {

		// TODO Auto-generated method stub
		
		
		
		Cliente cliente = new Cliente();//creamos un objeto Cliente
		Map<String, Object> paramaterMap = new HashMap<String, Object>();
		List<String> whereCause = new ArrayList<String>();
		StringBuilder queryBuilder = new StringBuilder();//?? guardar strings para la query
		
		try {
			
			Query q = null;
			queryBuilder.append("SELECT n FROM NotaCredito n");

			if (fechaInico != null && fechaFin != null) {//si ambos parametros son diferente de null entra 
				whereCause.add("(n.fecha BETWEEN :fechaInicio AND :fechaFinal)");//añade a la lista de String que representa los where
				paramaterMap.put("fechaInicio", fechaInico);//seteamos los parametros* 
				paramaterMap.put("fechaFinal", fechaFin);//*
			}
			/*if (folioCliente != null && !"".equalsIgnoreCase(folioCliente.trim())) {//si el folio es diferente de null y diferente de una  cadena "" entra
				whereCause.add("n.folioCliente = :folioCliente");//comparamos folioCliente de constanciadeS de lo que se setea en :folioCliente
				paramaterMap.put("folioCliente", folioCliente);//seteamos folioCliente
			}*/
			if (idCliente != 0) {//si el id es diferente de 0 entra
				//cliente.setCteCve(idCliente);//al objeto instanciado le seteamos el id del parametro idCliente
				whereCause.add("n.idcliente = :idCliente");//comparamos el cliente de constanciadeservicio con lo que se seteo al :idCliente
				paramaterMap.put("idCliente", idCliente);//seteamos idCliente el objeto cliente instanciado
			}

			queryBuilder.append(" WHERE " + StringUtils.join(whereCause, " AND "));//al query le añadimos los wherecause dependiendo en que condiciones entre

			q = em.createQuery(queryBuilder.toString());//a la sesion le pasamos el query creado que se encuentra en queryBuilder y se lo pasamos como String

			for (String key : paramaterMap.keySet()) {//recorremos las llaves que contiene parameterMap
				q.setParameter(key, paramaterMap.get(key));//por cada key le seteamos a q (query) los parametros con la llave (key=string) y con .get el objeto de la key
			}

			List<NotaCredito> listado = (List<NotaCredito>) q.getResultList();//creamos tipo de lista que se retornara y casteamos el resultado de la query
			
			
			return listado;
		} catch (Exception e) {
			System.out.println("ERROR" + e.getMessage());
			return null;
		}
	}

	public EntityManager getEm() {
		return em;
	}

	public void setEm(EntityManager em) {
		this.em = em;
	}
	
	
	
	
	
	 
}