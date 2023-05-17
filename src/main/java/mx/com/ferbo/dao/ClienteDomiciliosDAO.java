package mx.com.ferbo.dao;

import java.util.List;

import javax.persistence.EntityManager;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.ClienteDomicilios;
import mx.com.ferbo.util.EntityManagerUtil;

public class ClienteDomiciliosDAO extends IBaseDAO<ClienteDomicilios, Integer> {

	@Override
	public ClienteDomicilios buscarPorId(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ClienteDomicilios> buscarTodos() {
		List<ClienteDomicilios> listado;
		EntityManager em = EntityManagerUtil.getEntityManager();
		listado = em.createNamedQuery("ClienteDomicilios.findAll", ClienteDomicilios.class).getResultList();
		return listado;
	}

	@Override
	public List<ClienteDomicilios> buscarPorCriterios(ClienteDomicilios e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String actualizar(ClienteDomicilios clienteDomicilio) {
		// TODO Auto-generated method stub
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.createNativeQuery(
					"UPDATE cliente_domicilios SET CTE_CVE = :cteCve, domicilio_tipo_cve = :domicilioTipoCve WHERE (id = :id)")
					.setParameter("cteCve", clienteDomicilio.getCteCve().getCteCve())
					.setParameter("domicilioTipoCve",
							clienteDomicilio.getDomicilios().getDomicilioTipoCve().getDomicilioTipoCve())
					.setParameter("domCve", clienteDomicilio.getDomicilios().getDomCve())
					.setParameter("id", clienteDomicilio.getId())
					.executeUpdate();
			em.getTransaction().commit();
			em.close();
		} catch (Exception e) {
			System.out.println("ERROR" + e.getMessage());
			return "ERROR";
		}
		return null;
	}

	@Override
	public String guardar(ClienteDomicilios clienteDomicilio) {
		// TODO Auto-generated method stub
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.persist(clienteDomicilio);
			em.getTransaction().commit();
			em.close();
		} catch (Exception e) {
			System.out.println("ERROR" + e.getMessage());
			return "ERROR";
		}
		return null;
	}

	@Override
	public String eliminar(ClienteDomicilios clienteDom) {
		// TODO Auto-generated method stub
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.createNativeQuery("DELETE FROM CLIENTE_DOMICILIOS WHERE (id = :id)")
					.setParameter("id", clienteDom.getId()).executeUpdate();
			em.getTransaction().commit();
			em.close();
		} catch (Exception e) {
			System.out.println("ERROR" + e.getMessage());
			return "ERROR";
		}
		return null;
	}

	@Override
	public String eliminarListado(List<ClienteDomicilios> listado) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<ClienteDomicilios> buscaPorCliente(Cliente c) {
		EntityManager em = EntityManagerUtil.getEntityManager();
		return em.createNamedQuery("ClienteDomicilios.findByCliente", ClienteDomicilios.class)
				.setParameter("cteCve", c.getCteCve()).getResultList();
	}

}
