package mx.com.ferbo.dao;

import java.util.List;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.ClienteDomicilios;
import mx.com.ferbo.util.EntityManagerUtil;

public class ClienteDomiciliosDAO extends IBaseDAO<ClienteDomicilios, Integer> {

	private static Logger log = LogManager.getLogger(ClienteDomiciliosDAO.class);

	@Override
	public ClienteDomicilios buscarPorId(Integer id) {
		
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
		return null;
	}

	public List<ClienteDomicilios> buscarPorCliente(Integer idCliente) {
		List<ClienteDomicilios> lista = null;
		EntityManager em = null;

		try {
			em = EntityManagerUtil.getEntityManager();
			lista = em.createNamedQuery("ClienteDomicilios.findByCliente", ClienteDomicilios.class)
					.setParameter("cteCve", idCliente).getResultList();
		} catch (Exception ex) {
			log.error("Problema para obtener el listado de domicilios por cliente...", ex);
		} finally {

		}

		return lista;
	}

	public List<ClienteDomicilios> buscarDomicilioFiscalPorCliente(Integer idCliente, boolean isFullInfo) {
		List<ClienteDomicilios> listado = null;
		EntityManager em = null;

		try {
			em = EntityManagerUtil.getEntityManager();
			listado = em.createNamedQuery("ClienteDomicilios.findByClienteDomFiscal", ClienteDomicilios.class)
					.setParameter("cteCve", idCliente).getResultList();

			if (isFullInfo == false)
				return listado;

			for (ClienteDomicilios cd : listado) {
				log.debug("Domicilio cve: {}", cd.getDomicilios().getDomCve());
				log.debug("PaisCve: {}",
						cd.getDomicilios().getCiudades().getMunicipios().getEstados().getPaises().getPaisCve());
			}

		} catch (Exception ex) {
			log.error("Problema para obtener el listado de domicilios por cliente...", ex);
		} finally {
			EntityManagerUtil.close(em);
		}

		return listado;
	}

	@Override
	public String actualizar(ClienteDomicilios clienteDomicilio) {
		
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.createNativeQuery(
					"UPDATE cliente_domicilios SET CTE_CVE = :cteCve, domicilio_tipo_cve = :domicilioTipoCve WHERE (id = :id)")
					.setParameter("cteCve", clienteDomicilio.getCteCve().getCteCve())
					.setParameter("domicilioTipoCve",
							clienteDomicilio.getDomicilios().getDomicilioTipoCve().getDomicilioTipoCve())
					.setParameter("domCve", clienteDomicilio.getDomicilios().getDomCve())
					.setParameter("id", clienteDomicilio.getId()).executeUpdate();
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
		
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.createNativeQuery("DELETE FROM cliente_domicilios WHERE (id = :id)")
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
		
		return null;
	}

	public List<ClienteDomicilios> buscaPorCliente(Cliente c) {
		EntityManager em = EntityManagerUtil.getEntityManager();
		return em.createNamedQuery("ClienteDomicilios.findByCliente", ClienteDomicilios.class)
				.setParameter("cteCve", c.getCteCve()).getResultList();
	}

}
