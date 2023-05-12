package mx.com.ferbo.dao;

import java.util.List;
import static mx.com.ferbo.util.EntityManagerUtil.getEntityManager;
import javax.persistence.EntityManager;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.Factura;
import mx.com.ferbo.model.StatusFactura;
import mx.com.ferbo.model.TipoFacturacion;
import mx.com.ferbo.util.EntityManagerUtil;

public class FacturaDAO extends IBaseDAO<Factura, Integer> {

	@Override
	public Factura buscarPorId(Integer id) {
		EntityManager entity = EntityManagerUtil.getEntityManager();
		Factura fact = entity.createNamedQuery("Factura.findById", Factura.class)
				.setParameter("id",id).getSingleResult();
		return fact;
	}

	@Override
	public List<Factura> buscarTodos() {
		// TODO Auto-generated method stub
		EntityManager em = EntityManagerUtil.getEntityManager();
		return em.createNamedQuery("Factura.findAll", Factura.class).getResultList();
	}

	@Override
	public List<Factura> buscarPorCriterios(Factura e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String actualizar(Factura f) {
		try {
			EntityManager entity = getEntityManager();
			entity.getTransaction().begin();
			entity.merge(f);
			entity.getTransaction().commit();
			entity.close();
		}catch (Exception e){
			System.out.println("Error al timbrar " + e.getMessage());
			return "Failed!!";
		}
		return null;
	}

	@Override
	public String guardar(Factura factura) {
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.persist(factura);
			em.getTransaction().commit();
			em.close();
		} catch (Exception e) {
			System.out.println("ERROR guardando Factura" + e.getMessage());
			return "ERROR";
		}
		return null;
	}

	@Override
	public String eliminar(Factura e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminarListado(List<Factura> listado) {
		// TODO Auto-generated method stub
		return null;
	}

	public String actualizaStatus(Factura f) {
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.createNativeQuery("UPDATE factura SET status =:status WHERE id =:id")
					.setParameter("status", f.getStatus()).setParameter("id", f.getId()).executeUpdate();
			em.getTransaction().commit();
			em.close();
		} catch (Exception e) {
			System.out.println("ERROR" + e.getMessage());
			return "ERROR";
		}
		return null;
	}

}
