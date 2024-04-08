package mx.com.ferbo.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.Camara;
import mx.com.ferbo.model.Posicion;
import mx.com.ferbo.util.EntityManagerUtil;
import mx.com.ferbo.util.JPAEntity;

public class PosicionCamaraDAO extends IBaseDAO<Posicion, Integer> {
	EntityManager entity = JPAEntity.getEntity().createEntityManager();
	private static Logger log = LogManager.getLogger(PosicionCamaraDAO.class);

	@SuppressWarnings("unchecked")
	public List<Posicion> findAll() {
		List<Posicion> posiciones = null;
		EntityManager em = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			Query sql = entity.createNamedQuery("Posicion.findAll", Posicion.class);
			posiciones = sql.getResultList();
			System.out.println(posiciones + "*****************************************************");
		} catch (Exception e) {
			log.error("Error al obtener informacion", e);
		} finally {
			EntityManagerUtil.close(em);
		}
		return posiciones;
	}

	public String save(Posicion sP) {
		EntityManager entity = null;
		try {
			entity = EntityManagerUtil.getEntityManager();
			entity.getTransaction().begin();
			entity.persist(sP);
			entity.getTransaction().commit();
		} catch (Exception e) {
			log.error("Error al obtener informacion", e);
		} finally {
			EntityManagerUtil.close(entity);
		}
		return null;
	}

	@Override
	public Posicion buscarPorId(Integer id) {
		return null;
	}

	public List<Posicion> buscarPorCamara(Camara c) {
		List<Posicion> listaP = null;
		EntityManager em = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			listaP = em.createNamedQuery("Posicion.findByCamara", Posicion.class)
					.setParameter("camaraCve", c.getCamaraCve()).getResultList();
		} catch (Exception e) {
			log.error("Error al obtener informacion", e);
		} finally {
			EntityManagerUtil.close(em);
		}
		return listaP;
	}

	@Override
	public List<Posicion> buscarTodos() {
		List<Posicion> posiciones = null;
		EntityManager em = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			posiciones = new ArrayList<Posicion>();
			System.out.println(posiciones + "*****************************************************");
		} catch (Exception e) {
			log.error("Error al obtener informacion", e);
		} finally {
			EntityManagerUtil.close(em);
		}
		return posiciones;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Posicion> buscarPorCriterios(Posicion e) {
		List<Posicion> posiciones = null;
		EntityManager entity = null;
		Query query = null;
		try {
			entity = EntityManagerUtil.getEntityManager();
			query = entity.createNamedQuery("Posicion.findByPlantaCamara", Posicion.class)
					.setParameter("plantaCve", e.getPlanta().getPlantaCve())
					.setParameter("camaraCve", e.getCamara().getCamaraCve());
			posiciones = query.getResultList();
		} catch (Exception ex) {
			log.error("Problema para obtener el listado de posiciones por cámara...", ex);
		} finally {
			EntityManagerUtil.close(entity);
		}

		return posiciones;
	}

	@Override
	public String actualizar(Posicion p) {
		EntityManager entity = null;
		try {
			entity = EntityManagerUtil.getEntityManager();
			entity.getTransaction().begin();
			Query actualizar = entity
					.createNativeQuery(" UPDATE posicion SET habilitada  = :habil WHERE id_posicion = :pos ");
			actualizar.setParameter("habil", (p.getHabilitada() == true) ? 1 : 0);
			actualizar.setParameter("pos", p.getIdPosicion());
			actualizar.executeUpdate();
			entity.getTransaction().commit();
		} catch (Exception e) {
			log.error("Error al obtener informacion", e);
		} finally {
			EntityManagerUtil.close(entity);
		}
		return null;
	}

	@Override
	public String guardar(Posicion p) {
		EntityManager entity = null;
		try {
			entity = EntityManagerUtil.getEntityManager();
			entity.getTransaction().begin();
			// entity.persist(sP);
			Query otra = entity.createNativeQuery(
					" insert into posicion(id_planta, id_camara, cod_posicion, desc_posicion, temp_ini, temp_fin, habilitada) "
							+ "values(?,?,?,?,?,?,?); ");
			otra.setParameter(1, p.getPlanta().getPlantaCve());
			otra.setParameter(2, p.getCamara().getCamaraCve());
			otra.setParameter(3, p.getCodPosicion());
			otra.setParameter(4, p.getDescPosicion());
			otra.setParameter(5, p.getTempIni());
			otra.setParameter(6, p.getTempFin());
			otra.setParameter(7, p.getHabilitada());
			otra.executeUpdate();
			entity.getTransaction().commit();
			System.out.println(p + "GURADAR---------------------------------");
		} catch (Exception e) {
			log.error("Error al obtener informacion", e);
		} finally {
			EntityManagerUtil.close(entity);
		}
		return null;
	}

	@Override
	public String eliminar(Posicion e) {
		return null;
	}

	@Override
	public String eliminarListado(List<Posicion> listado) {
		return null;
	};

}
