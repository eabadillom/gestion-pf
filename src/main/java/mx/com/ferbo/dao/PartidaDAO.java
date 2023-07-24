package mx.com.ferbo.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.jfree.util.Log;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.ConstanciaDeDeposito;
import mx.com.ferbo.model.Partida;
import mx.com.ferbo.model.PartidaServicio;
import mx.com.ferbo.util.EntityManagerUtil;

public class PartidaDAO extends IBaseDAO<Partida, Integer>{
	@SuppressWarnings("unchecked")
	public List<Partida> findall() {
		
		EntityManager entity = null;
		List<Partida> partida= null;
		
		try {
			entity = EntityManagerUtil.getEntityManager();
			Query sql = entity.createNamedQuery("Partida.findAll", Partida.class);
			partida = sql.getResultList();
		} catch (Exception e) {
			Log.error("Problema para traer list de partida", e);
		}finally {
			EntityManagerUtil.close(entity);
		}
		
		
		return partida;
	}
	
	@Override
	public Partida buscarPorId(Integer partidaClave) {
		
		EntityManager em = null;
		Partida p = null;
		
		try {
			em = EntityManagerUtil.getEntityManager();
			p = em.createNamedQuery("Partida.findByPartidaCve", Partida.class).setParameter("partidaCve", partidaClave).getSingleResult();
		} catch (Exception e) {
			EntityManagerUtil.close(em);
		}finally {
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
		
		EntityManager em = null;
		List<Partida> buscaPartidas = new ArrayList<>();
		
		try {
			em = EntityManagerUtil.getEntityManager();		
			buscaPartidas=em.createNamedQuery("Partida.findByConstanciaDeDeposito",Partida.class)
					.setParameter("folioCliente", cons.getFolioCliente()).getResultList();
		} catch (Exception e) {
			Log.error("Problema al buscar constancias de deposito", e);
		}finally {
			EntityManagerUtil.close(em);
		}
		
		return buscaPartidas;

		
	}

}
