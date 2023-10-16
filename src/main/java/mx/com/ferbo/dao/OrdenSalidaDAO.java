package mx.com.ferbo.dao;

import java.math.BigDecimal;
import java.sql.Time;
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
import mx.com.ferbo.ui.RepEstadoCuenta;
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
	public List<OrdenSalida> buscarFolioPorCliente(Cliente cliente, Date fecha) {
		 List<OrdenSalida> listaOrdenSalida = null;
		 EntityManager em = null;
		 String sql = null;
		 try {
			 sql= "SELECT "
					 +"distinct  "
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
					 +"WHERE ps.st_estado = 'A' AND ps.fh_salida = :fecha AND cdd.CTE_CVE = :idCliente";
				em = EntityManagerUtil.getEntityManager();
			    @SuppressWarnings("unchecked")
				List<Object[]> results = em.createNativeQuery(sql)
			    		.setParameter("idCliente", cliente.getCteCve())
			    		.setParameter("fecha", fecha)
						.getResultList()
						;
			    listaOrdenSalida = new ArrayList<OrdenSalida>();
				for(Object[] o : results) {
					 OrdenSalida os = new OrdenSalida();
					int idx = 0 ;
					os.setFechaSalida((Date) o[idx++]);
					os.setFolioSalida((String) o[idx++]);
					os.setTmSalida((Time) o[idx++]);
					os.setNombrePlacas(	(String) o[idx++]);
					os.setNombreOperador((String) o[idx++]);
					listaOrdenSalida.add(os);
				}
		 }catch(Exception e) {
			 
		 }finally {
			 
		 }
		return null;
		
	}
	
	@Override
	public List<OrdenSalida> buscarPorCriterios(OrdenSalida e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String actualizar(OrdenSalida e) {
		// TODO Auto-generated method stub
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
