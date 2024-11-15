package mx.com.ferbo.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.Aviso;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.ConstanciaDeDeposito;
import mx.com.ferbo.model.ConstanciaDepositoDetalle;
import mx.com.ferbo.model.DetalleConstanciaSalida;
import mx.com.ferbo.model.DetallePartida;
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
		EntityManager em =null;
		try {
			em = EntityManagerUtil.getEntityManager();
			return em.createNamedQuery("ConstanciaDeDeposito.findAll", ConstanciaDeDeposito.class)
					.getResultList();			
		}catch(Exception e) {
				log.error("Error al obtener informacion",e);
		}finally {
			EntityManagerUtil.close(em);
		}
		return null;
	}

	public List<ConstanciaDeDeposito> buscarPorCliente(Integer cteCve){
		EntityManager em = null;
		List<ConstanciaDeDeposito> listado = null;
		try {
			 em = EntityManagerUtil.getEntityManager();
			listado = em.createNamedQuery("ConstanciaDeDeposito.findByCteCve",ConstanciaDeDeposito.class).setParameter("cteCve", cteCve).getResultList();
		}catch(Exception e) {
			log.error("Error al obtener informacion",e);
		}finally {
			EntityManagerUtil.close(em);
		}
		return listado;
	}
	
	@SuppressWarnings("unchecked")
	public List<ConstanciaDeDeposito> buscarPorCriterios(String folioCliente, Date fechaInico, Date fechaFin, int idCliente) {		
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
	
	@SuppressWarnings("unchecked")
	public List<ConstanciaDeDeposito> buscarPor(String folioCliente, Integer idCliente, Date fechaInicio, Date fechaFin) {
		EntityManager em = null;
		List<ConstanciaDeDeposito> listado = null;
		
		try {
			if(folioCliente != null && folioCliente.contains("%") == false)
				folioCliente = "%".concat(folioCliente).concat("%");
			
			em = EntityManagerUtil.getEntityManager();
			listado = em.createNativeQuery("SELECT * FROM (\n"
					+ "	SELECT * FROM (\n"
					+ "		SELECT * FROM constancia_de_deposito cdd \n"
					+ "		WHERE (:idCliente IS NULL OR cdd.CTE_CVE = :idCliente)\n"
					+ "	) cdd2 WHERE ((cdd2.FECHA_INGRESO BETWEEN :fechaInicio AND :fechaFin) OR (:fechaInicio IS NULL OR :fechaFin IS NULL))\n"
					+ ") cs3 WHERE (:folioCliente IS NULL OR cs3.FOLIO_CLIENTE LIKE :folioCliente)", ConstanciaDeDeposito.class)
					.setParameter("idCliente", idCliente)
					.setParameter("folioCliente", folioCliente)
					.setParameter("fechaInicio", fechaInicio)
					.setParameter("fechaFin", fechaFin)
					.getResultList()
					;
		} catch(Exception e) {
			log.error("Error al obtener informacion",e);
		} finally {
			EntityManagerUtil.close(em);
		}
		return listado;
	}

	@Override
	public String actualizar(ConstanciaDeDeposito constanciaDeDeposito) {
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
		EntityManager em = null;
		try {
			em = EntityManagerUtil.getEntityManager();
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
			
		} catch(NoResultException ex) {
			log.warn("No se encontró la constancia solicitada (folio_cliente): {}", cons.getFolioCliente());
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
			
			Aviso aviso = constancia.getAvisoCve();
			log.info("Aviso: {}", aviso.getAvisoCve());
			
			List<Partida> partidaList = constancia.getPartidaList();
			for(Partida partida : partidaList) {
				log.debug("Partida: {}",  partida.getPartidaCve());
				log.debug("Planta: {}", partida.getCamaraCve().getPlantaCve().getPlantaAbrev());
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
				List<DetallePartida> detallePartidaList = partida.getDetallePartidaList();
				for(DetallePartida dp : detallePartidaList) {
					log.info("Detalle Partida: {} - {}", dp.getDetallePartidaPK().getPartidaCve(), dp.getDetallePartidaPK().getDetPartCve());
				}
			}
			
		} catch(NoResultException ex) {
			log.warn("No se encontró la constancia solicitada (folio_cliente): {}", folioCliente);
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
			
		} catch(NoResultException ex) {
			log.warn("No se encontró la constancia solicitada (folio_cliente): {}", folio);
		} catch(Exception ex) {
			log.error("Problema para obtener la constancia de depósito...", ex);
		} finally {
			EntityManagerUtil.close(em);
		}
			
		return constancia;
	}
	
	@SuppressWarnings("unchecked")
	public boolean tieneSalidas(Integer folio) {
		boolean respuesta = false;
		EntityManager em = null;
		String sql = null;
		List<Object[]> results = null;
		
		try {
			sql = "select cdd.FOLIO, dcs.ID "
					+ "from constancia_de_deposito cdd "
					+ "inner join partida p ON cdd.FOLIO = p.FOLIO "
					+ "left outer join detalle_constancia_salida dcs ON dcs.PARTIDA_CVE = p.PARTIDA_CVE "
					+ "where cdd.FOLIO = :folio "
					+ "GROUP BY cdd.FOLIO, dcs.ID "
					;
			
			em = EntityManagerUtil.getEntityManager();
			results = em.createNativeQuery(sql)
					.setParameter("folio", folio)
					.getResultList()
					;
			
			for(Object[] result : results) {
					
				if(result[1] != null) {
					respuesta = true;
					return respuesta;
				}
			}
			
			respuesta = false;
		} catch(Exception ex) {
			log.error("Problema para ejecutar la consulta...", ex);
		} finally {
			EntityManagerUtil.close(em);
		}
		
		return respuesta;
	}
	
	
	@SuppressWarnings("unchecked")
	public boolean tieneFacturas(Integer folio) {
		boolean respuesta = false;
		EntityManager em = null;
		List<Object[]> results = null;
		String sql = null;
		
		try {
			sql = "select cdd.FOLIO, cf.id "
					+ "from constancia_de_deposito cdd "
					+ "left outer join constancia_factura cf ON cf.folio = cdd.FOLIO "
					+ "where cdd.FOLIO = :folio "
					+ "GROUP BY cdd.FOLIO, cf.id "
					;
			
			em = EntityManagerUtil.getEntityManager();
			results = em.createNativeQuery(sql)
					.setParameter("folio", folio)
					.getResultList()
					;
			
			for(Object[] result : results) {
				if(result[1] != null) {
					respuesta = true;
					return respuesta;
				}
			}
			
			respuesta = false;
		} catch(Exception ex) {
			log.error("Problema para ejecutar la consulta...", ex);
		} finally {
			EntityManagerUtil.close(em);
		}
		
		return respuesta;
	}

	@Override
	public List<ConstanciaDeDeposito> buscarPorCriterios(ConstanciaDeDeposito e) {
		
		return null;
	}

	public EntityManager getEm() {
		return em;
	}

	public void setEm(EntityManager em) {
		this.em = em;
	}
        
    public List<ConstanciaDeDeposito> buscarPorProducto(String nombreProducto) {
		EntityManager em = null;
		List<ConstanciaDeDeposito> constancias = null;
		String parametro = null;
		
		try {
			parametro = "%"+nombreProducto+"%";
			em = EntityManagerUtil.getEntityManager();
			constancias = em.createNamedQuery("ConstanciaDeDeposito.findByProducto",ConstanciaDeDeposito.class)
					.setParameter("nombreProducto",parametro)
					.getResultList()
					;
			for(ConstanciaDeDeposito c : constancias) {
				log.debug("Cliente: {}", c.getCteCve().getCteCve());
			}
		}catch(Exception e) {
			log.error("Error al obtener informacion",e);
		}finally {
			EntityManagerUtil.close(em);
		}
		
		return constancias;
	}
}
