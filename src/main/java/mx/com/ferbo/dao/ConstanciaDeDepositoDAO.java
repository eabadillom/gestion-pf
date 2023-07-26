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
import mx.com.ferbo.model.ConstanciaDeDeposito;
import mx.com.ferbo.model.ConstanciaDepositoDetalle;
import mx.com.ferbo.model.DetalleConstanciaSalida;
import mx.com.ferbo.model.Partida;
import mx.com.ferbo.model.TraspasoPartida;
import mx.com.ferbo.util.EntityManagerUtil;

public class ConstanciaDeDepositoDAO extends IBaseDAO<ConstanciaDeDeposito, Integer> {
	private static Logger log = LogManager.getLogger(ConstanciaDeDepositoDAO.class);

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

	public List<ConstanciaDeDeposito> buscarPorCliente(Integer cteCve){
		EntityManager em = EntityManagerUtil.getEntityManager();
		List<ConstanciaDeDeposito> listado = null;
		listado = em.createNamedQuery("ConstanciaDeDeposito.findByCteCve",ConstanciaDeDeposito.class).setParameter("cteCve", cteCve).getResultList();
		return listado;
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
	public String actualizar(ConstanciaDeDeposito constanciaDeDeposito) {
		// TODO Auto-generated method stub
		
		EntityManager em = null;
		
		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.merge(constanciaDeDeposito);
			em.getTransaction().commit();			
		}catch (Exception e) {
			System.out.println("ERROR" + e.getMessage());
			e.printStackTrace();
			e.getCause();
			return "ERROR";
		}finally {
			EntityManagerUtil.close(em);
		}
		
		
		return null;
	}

	@Override
	public String guardar(ConstanciaDeDeposito constanciaDeDeposito) {
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.persist(constanciaDeDeposito);
			em.getTransaction().commit();
		}catch (Exception e) {
			log.error("Problema al guardar la constancia de depósito...", e);
			EntityManagerUtil.rollback(em);
			return "ERROR";
		} finally {
			EntityManagerUtil.close(em);
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
		List<ConstanciaDeDeposito> lstAux = null;
		EntityManager em = null;
		
		try {
			em = EntityManagerUtil.getEntityManager();
			
			lstAux = em.createNamedQuery("ConstanciaDeDeposito.findByFolioCliente",ConstanciaDeDeposito.class)
					.setParameter("folioCliente",cons.getFolioCliente())
					.getResultList();
			
		} catch(Exception ex) {
			log.error("Problema para obtener la constancia de depósito...", ex);
		} finally {
			EntityManagerUtil.close(em);
		}
			
		return lstAux;
	}
	
	public ConstanciaDeDeposito buscarPorFolioCliente(String folioCliente, boolean isFullInfo) {
		ConstanciaDeDeposito constancia = null;
		EntityManager em = null;
		
		try {
			em = EntityManagerUtil.getEntityManager();
			
			constancia = em.createNamedQuery("ConstanciaDeDeposito.findByFolioCliente",ConstanciaDeDeposito.class)
					.setParameter("folioCliente", folioCliente)
					.getSingleResult();
			
			if(isFullInfo == false)
				return constancia;
			
			Cliente cliente = constancia.getCteCve();
			log.info("Cliente: {}",cliente.getCteCve());
			
			List<ConstanciaDepositoDetalle> constanciaDepositoDetalleList = constancia.getConstanciaDepositoDetalleList();
			log.debug("constancia deposito detalle lista: {}",  constanciaDepositoDetalleList.size());
			for(ConstanciaDepositoDetalle cdet : constanciaDepositoDetalleList) {
				log.debug("Servicio: {}", cdet.getServicioCve().getServicioCod());
			}
			
			List<Partida> partidaList = constancia.getPartidaList();
			for(Partida partida : partidaList) {
				log.debug("Partida: {}",  partida.getPartidaCve());
				log.debug("Planta: {}", partida.getCamaraCve().getPlantaCve().getPlantaCve());
				log.debug("Producto: {}",partida.getUnidadDeProductoCve().getProductoCve().getProductoCve());
				log.debug("Unidad de Manejo: {}",partida.getUnidadDeProductoCve().getUnidadDeManejoCve().getUnidadDeManejoCve());
				log.debug("Unidad de cobro: {}",  partida.getUnidadDeCobro().getUnidadDeManejoCve());
				List<DetalleConstanciaSalida> detalleConstanciaSalidaList = partida.getDetalleConstanciaSalidaList();
				log.debug("detalleConstanciaSalidaList.size(): {}",  detalleConstanciaSalidaList.size());
				for(DetalleConstanciaSalida dcs : detalleConstanciaSalidaList) {
					log.debug("Detalle constancia salida: {}",dcs.getId());
					log.debug("Constancia salida: {}", dcs.getConstanciaCve().getId());
				}
				List<TraspasoPartida> traspasoPartidaList = partida.getTraspasoPartidaList();
				log.debug("Traspaso partida: {}",  partida.getTraspasoPartidaList().size());
				for(TraspasoPartida traspaso : traspasoPartidaList) {
					log.info("Traspaso partida: {}",traspaso.getId());
					log.info("Constancia traspaso: {}", traspaso.getTraspaso().getId());
				}
			}
			
		} catch(Exception ex) {
			log.error("Problema para obtener la constancia de depósito...", ex);
		} finally {
			EntityManagerUtil.close(em);
		}
		
		return constancia;
	}
	
	public ConstanciaDeDeposito buscarPorFolioCliente(String folio) {
		ConstanciaDeDeposito constancia = null;
		EntityManager em = null;
		
		try {
			em = EntityManagerUtil.getEntityManager();
			
			constancia = em.createNamedQuery("ConstanciaDeDeposito.findByFolioCliente",ConstanciaDeDeposito.class)
					.setParameter("folioCliente", folio)
					.getSingleResult();
			
		} catch(Exception ex) {
			log.error("Problema para obtener la constancia de depósito...", ex);
		} finally {
			EntityManagerUtil.close(em);
		}
			
		return constancia;
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
