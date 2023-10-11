package mx.com.ferbo.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.Paises;
import mx.com.ferbo.util.EntityManagerUtil;

public class PaisesDAO extends IBaseDAO<Paises, Integer> {
	Logger log = LogManager.getLogger(PaisesDAO.class);
	
	@SuppressWarnings("unchecked")
	public List<Paises> findall() {
		List<Paises> paises = null;
		EntityManager em = null;
		try {
			 em = EntityManagerUtil.getEntityManager();
			Query sql = em.createNamedQuery("Paises.findAll", Paises.class);
			paises = sql.getResultList();
		}catch(Exception e) {
			log.error("Problemas para obtener informacion",e);
		}finally {
			EntityManagerUtil.close(em);
		}
		return paises;
	}
	
	@Override
	public Paises buscarPorId(Integer id) {
		EntityManager em = null;
		Paises pais = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			Query sql = em.createNamedQuery("Paises.findByPaisCve",Paises.class)
					.setParameter("paisCve", id);
			pais = (Paises) sql.getSingleResult();
		}catch(Exception e) {
			log.error("Problemas para obtener informacion",e);
		}finally {
			EntityManagerUtil.close(em);
		}
		return pais;
	}

	@Override
	public List<Paises> buscarTodos() {
		List<Paises> listado = null;
		EntityManager em = null;
		try {
			 em = EntityManagerUtil.getEntityManager();
			listado = em.createNamedQuery("Paises.findAll", Paises.class).getResultList();			
		}catch(Exception e) {
			log.error("Problemas para obtener informacion",e);
		}finally {
			EntityManagerUtil.close(em);
		}
		return listado;
	}

	@Override
	public List<Paises> buscarPorCriterios(Paises e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String actualizar(Paises paises) {
		EntityManager em = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.merge(paises);
			em.getTransaction().commit();
			em.close();
		} catch (Exception e) {
			System.out.println("ERROR actualizando Países" + e.getMessage());
			return "ERROR";
		}finally {
			EntityManagerUtil.close(em);
		}
		return null;
	}

	@Override
	public String guardar(Paises paises) {
		EntityManager em = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.persist(paises);
			em.getTransaction().commit();
			em.close();
		} catch (Exception e) {
			System.out.println("ERROR al guardar informacion" + e.getMessage());
			return "ERROR";
		}finally {
			EntityManagerUtil.close(em);
		}
		return null;
	}

	@Override
	public String eliminar(Paises paises) {
		EntityManager em = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.remove(em.merge(paises));
			em.getTransaction().commit();
			em.close();
		} catch (Exception e) {
			System.out.println("ERROR" + e.getMessage());
			return "ERROR";
		}finally {
			EntityManagerUtil.close(em);
		}
		return null;
	}

	@Override
	public String eliminarListado(List<Paises> listado) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Paises> buscaPorId(Integer id) {
		EntityManager em = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			return em.createNamedQuery("Paises.findByPaisCve", Paises.class)
					.setParameter("paisCve", id).getResultList();
		}catch(Exception e) {
			log.error("Problemas para obtener informacion",e);
		}finally {
			EntityManagerUtil.close(em);
		}
		return null;
	}

}
