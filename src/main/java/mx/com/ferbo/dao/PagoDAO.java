package mx.com.ferbo.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.Factura;
import mx.com.ferbo.model.Pago;
import mx.com.ferbo.util.EntityManagerUtil;

public class PagoDAO extends IBaseDAO<Pago, Integer> {

	@Override
	public Pago buscarPorId(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Pago> buscarTodos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Pago> buscarPorCriterios(Pago e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String actualizar(Pago e) {
		// TODO Auto-generated method stub
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.createQuery("UPDATE Pago as p set p.tipo.id = :tipoId , p.banco.id = :bancoId where p.id = :id")
					.setParameter("tipoId", e.getTipo().getId()).setParameter("bancoId", e.getBanco().getId())
					.setParameter("id", e.getId()).executeUpdate();
			em.getTransaction().commit();
			em.close();
		} catch (Exception ex) {
			System.out.println("ERROR" + ex.getMessage());
			return "ERROR";
		}
		return null;
	}

	@Override
	public String guardar(Pago e) {
		// TODO Auto-generated method stub
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.persist(e);
			em.getTransaction().commit();
		} catch (Exception ex) {
			System.out.println("ERROR" + ex.getMessage());
			return "ERROR";
		}
		return null;

	}

	@Override
	public String eliminar(Pago e) {
		// TODO Auto-generated method stub
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.createQuery("DELETE FROM Pago as p where p.id = :id")					
					.setParameter("id", e.getId()).executeUpdate();
			em.getTransaction().commit();
			em.close();
		} catch (Exception ex) {
			System.out.println("ERROR" + ex.getMessage());
			return "ERROR";
		}
		return null;
	
	}

	@Override
	public String eliminarListado(List<Pago> listado) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Pago> buscaPorFactura(Factura f) {
		EntityManager em = EntityManagerUtil.getEntityManager();
		return em.createNamedQuery("Pago.findByFacturaId", Pago.class).setParameter("facturaId", f.getId())
				.getResultList();
	}

	public List<Pago> buscaPorClienteFechas(Cliente c, Date startDate, Date endDate) {
		EntityManager em = EntityManagerUtil.getEntityManager();
		return em.createNamedQuery("Pago.findByClienteFechas", Pago.class).setParameter("cteCve", c.getCteCve())
				.setParameter("startDate", startDate).setParameter("endDate", endDate).getResultList();
	}

}
