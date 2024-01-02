package mx.com.ferbo.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.ConstanciaTraspaso;
import mx.com.ferbo.model.TraspasoPartida;
import mx.com.ferbo.model.TraspasoServicio;
import mx.com.ferbo.util.EntityManagerUtil;

public class ConstanciaTraspasoDAO extends IBaseDAO<ConstanciaTraspaso, Integer>{
	private static Logger log = LogManager.getLogger(ConstanciaTraspasoDAO.class);

	public EntityManager em = null;
	@Override
	public ConstanciaTraspaso buscarPorId(Integer id) {
		EntityManager em = null;
		ConstanciaTraspaso constancia = null;
		
		try {
			em = EntityManagerUtil.getEntityManager();
			constancia = em.createNamedQuery("ConstanciaTraspaso.findById", ConstanciaTraspaso.class).
					setParameter("id", id).getSingleResult();
		} catch(Exception ex) {
			log.error("Problema para obtener la constancia de traspaso id: " + id,  ex);
		} finally {
			EntityManagerUtil.close(em);
		}
		
		return constancia;
	}
	
	public ConstanciaTraspaso buscarPorId(Integer id, boolean isFullInfo) {
		EntityManager em = null;
		ConstanciaTraspaso constancia = null;
		
		try {
			em = EntityManagerUtil.getEntityManager();
			constancia = em.createNamedQuery("ConstanciaTraspaso.findById", ConstanciaTraspaso.class).
					setParameter("id", id).getSingleResult();
			if(isFullInfo == false)
				return constancia;
			
			log.debug("Cliente: {}", constancia.getCliente().getCteCve());
			
			List<TraspasoPartida> tpList = constancia.getTraspasoPartidaList();
			for(TraspasoPartida tp : tpList) {
				log.debug("TraspasoPartida: {}", tp.getId());
			}
			
			List<TraspasoServicio> tsList = constancia.getTraspasoServicioList();
			for(TraspasoServicio ts : tsList) {
				log.debug("TraspasoServicio: {}", ts.getId());
			}
			
		} catch(Exception ex) {
			log.error("Problema para obtener la constancia de traspaso id: " + id,  ex);
		} finally {
			EntityManagerUtil.close(em);
		}
		
		return constancia;
	}

	public List<ConstanciaTraspaso> buscarporNumero(String numero){
		EntityManager em = null;
		List<ConstanciaTraspaso> lista = null;
		
		try {
			em = EntityManagerUtil.getEntityManager();
			lista = em.createNamedQuery("ConstanciaTraspaso.findByNumero", ConstanciaTraspaso.class).
					setParameter("numero", numero).getResultList();
		} catch(Exception ex) {
			log.error("Problema para obtener la lista de constancias de traspaso Numero: " + numero, ex);
		} finally {
			EntityManagerUtil.close(em);
		}
		return lista; 
	}
	@Override
	public List<ConstanciaTraspaso> buscarTodos() {
		// TODO Auto-generated method stub
		return null;
	}

	
	@SuppressWarnings("unchecked")
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
	
	
	
	
	
	public List<ConstanciaTraspaso> buscar(Date fechaInicio, Date fechaFin, Integer idCliente, String folioCliente) {
		List<ConstanciaTraspaso> resultList = null;
		EntityManager em = null;
		
		try {
			em = EntityManagerUtil.getEntityManager();
			resultList = em.createNamedQuery("ConstanciaTraspaso.findByPeriodoClienteNumero", ConstanciaTraspaso.class)
					.setParameter("fechaInicio", fechaInicio)
					.setParameter("fechaFin", fechaFin)
					.setParameter("idCliente", idCliente)
					.setParameter("folioCliente", folioCliente)
					.getResultList()
					;
		} catch(Exception ex) {
			log.error("Problema para consultar las constancias de salida...", ex);
		} finally {
			EntityManagerUtil.close(em);
		}
		
		
		return resultList;
	}

	@Override
	public String actualizar(ConstanciaTraspaso constancia) {
		EntityManager em = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.merge(constancia);
			em.getTransaction().commit();
			
		}catch (Exception e) {
			log.error("Problema para actualizar la constancia de traspaso: " + constancia, e);
			return "ERROR";
		} finally {
			EntityManagerUtil.close(em);
		}
		
		return null;
	}

	@Override
	public String guardar(ConstanciaTraspaso constancia) {
		EntityManager em = null;
		
		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.persist(constancia);
			em.getTransaction().commit();
		}catch (Exception e) {
			log.error("Problema para guardar la constancia de traspaso: " + constancia,  e);
			return "ERROR";
		} finally {
			EntityManagerUtil.close(em);
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
