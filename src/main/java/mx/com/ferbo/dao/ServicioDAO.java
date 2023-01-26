package mx.com.ferbo.dao;


import java.util.List;

import javax.persistence.EntityManager;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.Servicio;
import mx.com.ferbo.util.EntityManagerUtil;

public class ServicioDAO extends IBaseDAO<Servicio, Integer> {
	
	@Override
	public Servicio buscarPorId(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Servicio> buscarTodos() {
		EntityManager em = EntityManagerUtil.getEntityManager();//Abre (sesion) conexion entre Hibernate y la BD
		List<Servicio> listado = null;
		listado = em.createNamedQuery("Servicio.findAll", Servicio.class).getResultList();//crea la consulta pasando el nombre de esta y el tipo
		return listado;
	}

	@Override
	public List<Servicio> buscarPorCriterios(Servicio e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String actualizar(Servicio servicio) {
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();//comienza transaccion a ejecutar
			em.merge(servicio);//modifica el servicio dado (es gestionado popr entity...)
			em.getTransaction().commit();//realiza el cambio en la BD
			em.close();//cierra la sesion
		} catch (Exception e) {
			System.out.println("ERROR" + e.getMessage());
			return "ERROR";
		}
		return null;
	}

	@Override
	public String guardar(Servicio servicio) {
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.persist(servicio);//guarda el servicio dado (NO es gestionado)
			em.getTransaction().commit();
			em.close();
		} catch (Exception e) {
			System.out.println("ERROR" + e.getMessage());
			return "ERROR";
		}
		return null;
	}

	@Override
	public String eliminar(Servicio servicio) {
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.remove(em.merge(servicio));//remueve o elimina el servicio dado
			em.getTransaction().commit();
			em.close();
		} catch (Exception e) {
			System.out.println("ERROR" + e.getMessage());
			return "ERROR";
		}
		return null;
	}

	@Override
	public String eliminarListado(List<Servicio> listado) {
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			for (Servicio servicio : listado) {
				em.remove(em.merge(servicio));
			}
			em.getTransaction().commit();
			em.close();
		} catch (Exception e) {
			System.out.println("ERROR" + e.getMessage());
			return "ERROR";
		}
		return null;
	}


}
