package mx.com.ferbo.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.ConstanciaDeDeposito;
import mx.com.ferbo.model.DetalleConstanciaSalida;
import mx.com.ferbo.model.DetallePartida;
import mx.com.ferbo.model.Partida;
import mx.com.ferbo.util.EntityManagerUtil;

public class PartidaDAO extends IBaseDAO<Partida, Integer>{
	private static Logger log = LogManager.getLogger(PartidaDAO.class);
	@SuppressWarnings("unchecked")
	public List<Partida> findall() {
		EntityManager entity = EntityManagerUtil.getEntityManager();
		List<Partida> partida= null;
		Query sql = entity.createNamedQuery("Partida.findAll", Partida.class);
		partida = sql.getResultList();
		return partida;
	}
	
	@Override
	public Partida buscarPorId(Integer partidaClave) {
		EntityManager em = null;
		Partida p = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			p = em.createNamedQuery("Partida.findByPartidaCve", Partida.class).setParameter("partidaCve", partidaClave).getSingleResult();
		} catch(Exception ex) {
			log.error("Problema para obtener la partida: " + partidaClave, ex);
		} finally {
			EntityManagerUtil.close(em);
		}
		return p;
	}
	
	public Partida buscarPorId(Integer partidaCve, boolean isFullInfo) {
		EntityManager em = null;
		Partida p = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			p = em.find(Partida.class, partidaCve);
			
			if(isFullInfo == false)
				return p;
			
			
			log.debug("DetalleConstanciaSalidaList: {}", p.getDetalleConstanciaSalidaList().size());
			for(DetalleConstanciaSalida dcs : p.getDetalleConstanciaSalidaList()) {
				log.debug("DetalleconstanciaSalida: {}", dcs.getId());
			}
			
			List<DetallePartida> detallePartidaList = p.getDetallePartidaList();
			log.debug("Lista detallePartida: {}", detallePartidaList.size());
			for(DetallePartida detallePartida : detallePartidaList) {
				log.debug("DetallePartida: {}, {}", detallePartida.getDetallePartidaPK().getPartidaCve(), detallePartida.getDetallePartidaPK().getDetPartCve());
//				log.debug("Estado Inventario: {}", detallePartida.getEdoInvCve().getEdoInvCve());
				log.debug("Unidad de Medida: {}", detallePartida.getUMedidaCve());
//				log.debug("DetalleconstanciaSalidaList: {}", detallePartida.getDetalleConstanciaSalida().getId());
			}
			
		} catch(Exception ex) {
			log.error("Problema para obtener la partida: " + partidaCve, ex);
		} finally {
			EntityManagerUtil.close(em);
		}
		return p;
	}

	@Override
	public List<Partida> buscarTodos() {
		EntityManager em = EntityManagerUtil.getEntityManager();
		return em.createNamedQuery("Partida.findAll", Partida.class).getResultList();
	}

	@Override
	public List<Partida> buscarPorCriterios(Partida e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String actualizar(Partida partida) {
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.merge(partida);
			em.getTransaction().commit();
			em.close();
		} catch (Exception e) {
			System.out.println("ERROR" + e.getMessage());
			return "ERROR";
		}
		return null;
	}

	@Override
	public String guardar(Partida partida) {
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.persist(partida);
			em.getTransaction().commit();
			em.close();
		} catch (Exception e) {
			System.out.println("ERROR" + e.getMessage());
			return "ERROR";
		}
		return null;
	}

	@Override
	public String eliminar(Partida e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminarListado(List<Partida> listado) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public List<Partida> buscarPorConstanciaDeposito(ConstanciaDeDeposito cons){
		EntityManager em = EntityManagerUtil.getEntityManager();
		List<Partida> buscaPartidas = new ArrayList<>();
		buscaPartidas=em.createNamedQuery("Partida.findByConstanciaDeDeposito",Partida.class)
				.setParameter("folioCliente", cons.getFolioCliente()).getResultList();
		return buscaPartidas;

		
	}

}
