package mx.com.ferbo.dao;

import java.math.BigDecimal;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.ConstanciaDeDeposito;
import mx.com.ferbo.model.ConstanciaDeServicio;
import mx.com.ferbo.model.OrdenSalida;
import mx.com.ferbo.model.Partida;
import mx.com.ferbo.ui.OrdenDeSalidas;
import mx.com.ferbo.ui.RepEstadoCuenta;
import mx.com.ferbo.util.DateUtil;
import mx.com.ferbo.util.EntityManagerUtil;

public class OrdenSalidaDAO extends IBaseDAO<OrdenSalida, Integer>{
	private static Logger log = LogManager.getLogger(OrdenSalidaDAO.class);
	
	public EntityManager em = null;

	@SuppressWarnings("unchecked")
	public List<OrdenSalida> findall() {
		List<OrdenSalida> ordenSalida = null;
		EntityManager entity = null;
		try {
			entity = EntityManagerUtil.getEntityManager();
			Query sql = entity.createNamedQuery("OrdenSalida.findAll", OrdenSalida.class);
			ordenSalida = sql.getResultList();
		} catch(Exception ex) {
			log.error("Problema para obtener el listado de de salidas...", ex);
		} finally {
			EntityManagerUtil.close(entity);
		}
		return ordenSalida;
	}	
	
	@Override
	public OrdenSalida buscarPorId(Integer id) {
		OrdenSalida os = null;
		EntityManager  em = null;
		try {
			em = EntityManagerUtil.getEntityManager();		
			os = em.createNamedQuery("OrdenSalida.findByidPreSalida", OrdenSalida.class).
					setParameter("idPreSalida", id)
					.getSingleResult();
		} catch(Exception ex) {
			log.error("Problema para obtener la orden de salida: " + id, ex );
		} finally {
			EntityManagerUtil.close(em);
		}
		return os;
	}

		public OrdenSalida buscarPorFolioOrdenSalida(String folioOrden) {
		OrdenSalida os = null;
		EntityManager  em = null;
		try {
			em = EntityManagerUtil.getEntityManager();		
			os = em.createNamedQuery("OrdenSalida.findByfolio", OrdenSalida.class).
					setParameter("folio", folioOrden)
					.getSingleResult();
		} catch(Exception ex) {
			log.error("Problema para obtener la orden de salida: " + folioOrden, ex );
		} finally {
			EntityManagerUtil.close(em);
		}
		return os;
	}
		
		/*findBypartidaClave*/
		public OrdenSalida buscarPorPartida(Integer partida) {
			OrdenSalida os = null;
			EntityManager  em = null;
			try {
				em = EntityManagerUtil.getEntityManager();		
				os = em.createNamedQuery("OrdenSalida.partidaClave", OrdenSalida.class).
						setParameter("partidaClave", partida)
						.getSingleResult();
			} catch(Exception ex) {
				log.error("Problema para obtener la orden de salida: " + partida, ex );
			} finally {
				EntityManagerUtil.close(em);
			}
			return os;
		}
	
		public List<OrdenSalida> listaPorPartida(Integer partida) {
			List<OrdenSalida> listaPartida= null;
			EntityManager em = null;
			
			try {
				em = EntityManagerUtil.getEntityManager();
				em.getTransaction().begin();
				listaPartida = em.createNamedQuery("OrdenSalida.findBypartidaClave", OrdenSalida.class)
						.setParameter("partidaClave", partida)
						.getResultList();
				
				em.getTransaction().commit();
			} catch(Exception ex) {
				EntityManagerUtil.rollback(em);
			} finally {
				EntityManagerUtil.close(em);
			}
			
			return listaPartida;
		}
			
		
		
	
	@Override
	public List<OrdenSalida> buscarTodos() {
		EntityManager em = null;
		List<OrdenSalida> listadoSalidas = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			listadoSalidas = em.createNamedQuery("OrdenSalida.findAll", OrdenSalida.class)
					.getResultList()
					;
		} catch(Exception ex) {
			log.error("Problema para obtener el listado de clientes...", ex);
		} finally {
			EntityManagerUtil.close(em);
		}
		
		return listadoSalidas;
	}
	
	public List<OrdenSalida> buscarPorstatusFecha(Date fecha) {
		List<OrdenSalida> listaStatusFecha = null;
		EntityManager em = null;
		
		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			listaStatusFecha = em.createNamedQuery("OrdenSalida.findByfechaSalida", OrdenSalida.class)
					.setParameter("fechaSalida", fecha)
					.getResultList();
			
			em.getTransaction().commit();
		} catch(Exception ex) {
			EntityManagerUtil.rollback(em);
		} finally {
			EntityManagerUtil.close(em);
		}
		
		return listaStatusFecha;
	}
	
	@SuppressWarnings("unchecked")
	public List<OrdenDeSalidas> buscarpoPlanta(String folioSalida, Date fecha) {
		 List<OrdenDeSalidas> listaOrdenDeSalidas = null;
		 EntityManager em = null;
		 String sql = null;
		 try {
			sql = "SELECT "
					+"DISTINCT "
					+"cd_folio_salida, "
					+"ps.folio, "
					+ "st_estado, "
					+ "fh_salida, "
					+ "tm_salida, "
					+ " p.PARTIDA_CVE, "
					+ "p.CANTIDAD_TOTAL, "
					+ "p.PESO_TOTAL, "
					+ " dp.dtp_codigo, "
					+ "dp.dtp_lote, "
					+ " dp.dtp_caducidad, "
					+ "dp.dtp_SAP, "
					+ "dp.dtp_pedimento, "
					+ " dcs.TEMPERATURA, "
					+ "udm.UNIDAD_DE_MANEJO_DS,"
					+ " p2.NUMERO_PROD , "
					+ "p2.PRODUCTO_DS, "
					+ " c.CAMARA_DS, "
					+ "p3.PLANTA_DS "
					+ "FROM pre_salida ps "
					+ "INNER JOIN PARTIDA p ON ps.partida_cve = p.PARTIDA_CVE "
					+ "INNER JOIN CAMARA c on p.CAMARA_CVE = c.CAMARA_CVE "
					+ "INNER JOIN PLANTA p3 ON c.PLANTA_CVE = p3.PLANTA_CVE "
					+ "INNER JOIN DETALLE_PARTIDA dp on p.PARTIDA_CVE = dp.PARTIDA_CVE "
					+ "INNER JOIN DETALLE_CONSTANCIA_SALIDA dcs ON dcs.PARTIDA_CVE = ps.partida_cve "
					+ "INNER JOIN UNIDAD_DE_PRODUCTO udp ON p.UNIDAD_DE_PRODUCTO_CVE = udp.UNIDAD_DE_PRODUCTO_CVE "
					+ "INNER JOIN UNIDAD_DE_MANEJO udm ON udp.UNIDAD_DE_MANEJO_CVE  = udm.UNIDAD_DE_MANEJO_CVE "
					+ "INNER JOIN PRODUCTO p2 ON udp.PRODUCTO_CVE = p2.PRODUCTO_CVE "
					+ "WHERE ps.st_estado = 'A' AND ps.cd_folio_salida  = :folioSalida AND ps.fh_salida = :fecha";
			em = EntityManagerUtil.getEntityManager();
			SimpleDateFormat formatoSimple = new SimpleDateFormat("yyyy-MM-dd");
			String fech = formatoSimple.format(fecha);
			Query query = em.createNativeQuery(sql)
					.setParameter("fecha",fech)
					.setParameter("folioSalida",folioSalida )						
					;
			List<Object[]> results = query.getResultList();
			listaOrdenDeSalidas = new ArrayList<OrdenDeSalidas>();
			for(Object[] o : results) {
				OrdenDeSalidas ods = new OrdenDeSalidas();
				int idx = 0 ;
				ods.setFolioSalida((String) o[idx++]);
				ods.setFolioOrdenSalida((Integer) o[idx++]);
				ods.setStatus((String) o[idx++]);
				ods.setFechaSalida((Date) o[idx++]);
				ods.setHoraSalia((Time) o[idx++]);
				ods.setPartidaCve((Integer) o[idx++]);
				ods.setCantidad((Integer) o[idx++]);
				ods.setPeso((BigDecimal) o[idx++]);
				ods.setCodigo((String) o[idx++]);
				ods.setLote((String) o[idx++]);
				ods.setFechaCaducidad((Date) o[idx++]);
				ods.setSAP((String) o[idx++]);
				ods.setPedimento((String) o[idx++]);
				ods.setTemperatura((String) o[idx++]);
				ods.setUnidadManejo((String) o[idx++]);
				ods.setCodigoProducto((String) o[idx++]);
				ods.setNombreProducto((String) o[idx++]);
				ods.setNombreCamara((String) o[idx++]);
				ods.setNombrePlanta((String) o[idx++]);
				listaOrdenDeSalidas.add(ods);
			}
		} catch (Exception e) {
			log.error("Problemas para obtener la informacion requerida", e);
		}finally {
			EntityManagerUtil.close(em);
		}
		return listaOrdenDeSalidas;
		
	}
	
	public List<String> buscaFolios(Cliente cliente, Date fecha) {
		List<String> listaFolios = null;
		EntityManager em = null;
		String sql = null;
		try {
			 sql = "SELECT "
					 +"distinct "
					 +"cd_folio_salida, " 
					 +"fh_salida, "
					 +"tm_salida, "
					 +"nb_placa_tte, "
					 +"nb_operador_tte "
					 +"FROM "
					 +"pre_salida ps "
					 +"INNER JOIN "
					 +"PARTIDA p "
					 +"ON "
					 +"ps.partida_cve = p.PARTIDA_CVE "
					 +"INNER JOIN "
					 +"CONSTANCIA_DE_DEPOSITO cdd ON p.FOLIO = cdd.FOLIO "
					 +"WHERE ps.st_estado = 'A' AND ps.fh_salida = :fecha AND cdd.CTE_CVE = :idCliente ";
			em = EntityManagerUtil.getEntityManager();
			SimpleDateFormat formatoSimple = new SimpleDateFormat("yyyy-MM-dd");
			String fech = formatoSimple.format(fecha);
			Query query = em.createNativeQuery(sql)
					.setParameter("fecha", fech)
					.setParameter("idCliente", cliente.getCteCve())						
					;
			@SuppressWarnings("unchecked")
			List<Object[]> results = query.getResultList();
		    listaFolios = new ArrayList<String>();
			for(Object[] o : results) {
				listaFolios.add((String) o[0]);
			}
			
		} catch(Exception ex) {
			log.error("Problema para obtener el listado de folios...", ex);
		} finally {
			EntityManagerUtil.close(em);
		}
		
		
		return listaFolios;
	}
	
	@SuppressWarnings("unchecked")
	public List<OrdenSalida> buscaFolios(String folio) {
		List<OrdenSalida> listaFolios = null;
		EntityManager em = null;
		Query query = null;
		String sql = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			query = em.createNamedQuery("OrdenSalida.findByFolioSalida", OrdenSalida.class)
					.setParameter("FolioSalida",folio)
					;
			listaFolios = query.getResultList();
		} catch(Exception ex) {
			log.error("Problema para obtener el listado de folios...", ex);
		} finally {
			EntityManagerUtil.close(em);
		}
		
		
		return listaFolios;
	}
	
	
	@SuppressWarnings({ "unchecked", "deprecation" })
	public List<OrdenSalida> buscarFolioPorCliente(Cliente cliente, Date fecha) {
		 List<OrdenSalida> listaOrdenSalida = null;
		 EntityManager em = null;
		 String sql = null;
		 
		 try {
			 sql = "SELECT "
					 +"distinct "
					 +"cd_folio_salida, " 
					 +"fh_salida, "
					 +"tm_salida, "
					 +"nb_placa_tte, "
					 +"nb_operador_tte "
					 +"FROM "
					 +"pre_salida ps "
					 +"INNER JOIN "
					 +"PARTIDA p "
					 +"ON "
					 +"ps.partida_cve = p.PARTIDA_CVE "
					 +"INNER JOIN "
					 +"CONSTANCIA_DE_DEPOSITO cdd ON p.FOLIO = cdd.FOLIO "
					 +"WHERE ps.st_estado = 'A' AND ps.fh_salida = :fecha AND cdd.CTE_CVE = :idCliente ";
				em = EntityManagerUtil.getEntityManager();
				SimpleDateFormat formatoSimple = new SimpleDateFormat("yyyy-MM-dd");
				String fech = formatoSimple.format(fecha);
				Query query = em.createNativeQuery(sql)
						.setParameter("fecha",fech)
						.setParameter("idCliente", cliente.getCteCve())						
						;
				
				List<Object[]> results = query.getResultList();
			    listaOrdenSalida = new ArrayList<OrdenSalida>();
				for(Object[] o : results) {
					 OrdenSalida os = new OrdenSalida();
					int idx = 0 ;
					os.setFolioSalida( (String) o[idx++]);
					os.setFechaSalida( (Date) o[idx++]);
					os.setTmSalida((Time) o[idx++]);
					os.setNombrePlacas(	(String) o[idx++]);
					os.setNombreOperador((String) o[idx++]);
					listaOrdenSalida.add(os);
				}
		 }catch(Exception e) {
			 log.error("Problemas para obtener informacion", e);
		 }finally {
			 EntityManagerUtil.close(em);
		 }
		return listaOrdenSalida;
	}
	
	@Override
	public List<OrdenSalida> buscarPorCriterios(OrdenSalida e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String actualizar(OrdenSalida os) {
		EntityManager em = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.merge(os);
			em.getTransaction().commit();
		} catch (Exception e) {
			log.error("Problema en la actualización de la orden: " + os.getFolioSalida(), e);
			return "ERROR";
		}finally {
			EntityManagerUtil.close(em);
		}
		return null;
	}

	@Override
	public String guardar(OrdenSalida os) {
		EntityManager em = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.persist(os);
			em.getTransaction().commit();
		} catch (Exception e) {
			log.error("Problema al guardar el cliente: " + os.getIdPreSalida(), e);
			return "ERROR";
		}finally {
			EntityManagerUtil.close(em);
		}
		return null;
	}


	@Override
	public String eliminar(OrdenSalida e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminarListado(List<OrdenSalida> listado) {
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
