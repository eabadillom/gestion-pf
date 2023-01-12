package mx.com.ferbo.dao;

import static mx.com.ferbo.util.EntityManagerUtil.getEntityManager;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.Paises;
import mx.com.ferbo.model.TipoAsentamiento;
import mx.com.ferbo.util.EntityManagerUtil;

public class TipoAsentamientoDAO extends IBaseDAO<TipoAsentamiento, Integer> {

	@SuppressWarnings("unchecked")
	public List<TipoAsentamiento> findall() {
		EntityManager entity = getEntityManager();
		List<TipoAsentamiento> TipoAs= null;
		Query sql = entity.createNamedQuery("TipoAsentamiento.findAll", TipoAsentamiento.class);
		TipoAs = sql.getResultList();
		return TipoAs;
	}
	@Override
	public TipoAsentamiento buscarPorId(Integer id) {
		EntityManager entity = getEntityManager();
		TipoAsentamiento Tasn = null;
		Query sql = entity.createNamedQuery("TipoAsentamiento.findByTipoasntmntoCve",TipoAsentamiento.class)
				.setParameter("tipoasntmntoCve",id.shortValue());
		Tasn = (TipoAsentamiento) sql.getSingleResult();
		return Tasn;
	}

	@Override
	public List<TipoAsentamiento> buscarTodos() {
		List<TipoAsentamiento> listado = null;
		EntityManager em = EntityManagerUtil.getEntityManager();
		listado = em.createNamedQuery("TipoAsentamiento.findAll", TipoAsentamiento.class).getResultList();
		return listado;
	}

	@Override
	public List<TipoAsentamiento> buscarPorCriterios(TipoAsentamiento e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String actualizar(TipoAsentamiento tipoAsentamiento) {
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.merge(tipoAsentamiento);
			em.getTransaction().commit();
			em.close();
		} catch (Exception e) {
			System.out.println("ERROR actualizando Tipo de Asentamiento" + e.getMessage());
			return "ERROR";
		}
		return null;
	}

	@Override
	public String guardar(TipoAsentamiento tipoAsentamiento) {
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.persist(tipoAsentamiento);
			em.getTransaction().commit();
			em.close();
		} catch (Exception e) {
			System.out.println("ERROR guardando Tipo de Asentamiento" + e.getMessage());
			return "ERROR";
		}
		return null;
	}

	@Override
	public String eliminar(TipoAsentamiento tipoAsentamiento) {
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.remove(em.merge(tipoAsentamiento));
			em.getTransaction().commit();
			em.close();
		} catch (Exception e) {
			System.out.println("ERROR" + e.getMessage());
			return "ERROR";
		}
		return null;
	}

	@Override
	public String eliminarListado(List<TipoAsentamiento> listado) {
		// TODO Auto-generated method stub
		return null;
	}

}
