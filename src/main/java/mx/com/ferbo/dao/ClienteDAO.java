package mx.com.ferbo.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.ClienteContacto;
import mx.com.ferbo.model.Contacto;
import mx.com.ferbo.model.Mail;
import mx.com.ferbo.model.MedioCnt;
import mx.com.ferbo.model.PrecioServicio;
import mx.com.ferbo.model.ProductoPorCliente;
import mx.com.ferbo.model.SerieConstancia;
import mx.com.ferbo.model.SerieConstanciaPK;
import mx.com.ferbo.model.Telefono;
import mx.com.ferbo.util.EntityManagerUtil;

public class ClienteDAO extends IBaseDAO<Cliente, Integer> {
	private static Logger log = LogManager.getLogger(ClienteDAO.class);

	@SuppressWarnings("unchecked")
	public List<Cliente> findall() {
		List<Cliente> cliente = null;
		EntityManager entity = null;
		try {
			entity = EntityManagerUtil.getEntityManager();
			Query sql = entity.createNamedQuery("Cliente.findAll", Cliente.class);
			cliente = sql.getResultList();
		} catch (Exception ex) {
			log.error("Problema para obtener el listado de clientes...", ex);
		} finally {
			EntityManagerUtil.close(entity);
		}
		return cliente;
	}

	@Override
	public Cliente buscarPorId(Integer id) {
		Cliente cliente = null;
		EntityManager em = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			cliente = em.createNamedQuery("Cliente.findByCteCve", Cliente.class).setParameter("cteCve", id)
					.getSingleResult();
		} catch (Exception ex) {
			log.error("Problema para obtener el cliente: " + id, ex);
		} finally {
			EntityManagerUtil.close(em);
		}

		return cliente;
	}

	public Cliente buscarPorId(Integer id, boolean isFullInfo) {
		Cliente cliente = null;
		EntityManager em = null;
		Query query = null;

		try {
			em = EntityManagerUtil.getEntityManager();
			query = em.createNamedQuery("Cliente.findByCteCve", Cliente.class).setParameter("cteCve", id);
			cliente = (Cliente) query.getSingleResult();
			log.info(cliente.getMetodoPago());
			if (isFullInfo == false)
				return cliente;

			if (cliente.getCandadoSalida() != null)
				cliente.getCandadoSalida().getId();

			List<ClienteContacto> clienteContactoList = cliente.getClienteContactoList();
			log.debug(cliente.getRegimenFiscal().getCd_regimen());
			log.debug(cliente.getUsoCfdi().getUsoCfdi());
			log.debug(cliente.getMetodoPago().getNbMetodoPago());
			log.debug(cliente.getMetodoPago().getCdMetodoPago());

			for (ClienteContacto clienteContacto : clienteContactoList) {

				Contacto contacto = clienteContacto.getIdContacto();

				List<MedioCnt> medioCntList = contacto.getMedioCntList();

				for (MedioCnt medioContacto : medioCntList) {

					Mail idMail = medioContacto.getIdMail();
					Telefono idTelefono = medioContacto.getIdTelefono();

					if (idMail != null)
						idMail.getTpMail().getNbTipo();

					if (idTelefono != null)
						idTelefono.getTpTelefono().getNbTelefono();

				}
			}
		} catch (NoResultException ex) {
			log.error("Problema para obtener la información completa del cliente...", ex);
		} catch (Exception ex) {
			log.error("Problema para obtener la información completa del cliente...", ex);
		} finally {
			EntityManagerUtil.close(em);
		}

		return cliente;
	}

	@Override
	public List<Cliente> buscarTodos() {
		EntityManager em = null;
		List<Cliente> listado = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			listado = em.createNamedQuery("Cliente.findAll", Cliente.class).getResultList();
		} catch (Exception ex) {
			log.error("Problema para obtener el listado de clientes...", ex);
		} finally {
			EntityManagerUtil.close(em);
		}

		return listado;
	}

	@SuppressWarnings("unchecked")
	public List<Cliente> buscarTodos(boolean isFullInfo) {
		List<Cliente> lista = null;
		EntityManager em = null;
		Query query = null;

		try {
			em = EntityManagerUtil.getEntityManager();
			query = em.createNamedQuery("Cliente.findAll", Cliente.class);
			lista = query.getResultList();

			if (isFullInfo == false)
				return lista;

			for (Cliente cliente : lista) {
				if (cliente.getCandadoSalida() != null)
					cliente.getCandadoSalida().getId();

				List<ClienteContacto> clienteContactoList = cliente.getClienteContactoList();
				log.debug("Número de contactos del cliente: {}", clienteContactoList.size());
				log.debug("ClienteContactoList: {}", clienteContactoList.size());
				log.debug(cliente.getRegimenFiscal().getCd_regimen());
				// log.debug(cliente.getUsoCfdi().getUsoCfdi());

				if (cliente.getMetodoPago() != null) {
					log.debug(cliente.getMetodoPago().getNbMetodoPago());
					log.debug(cliente.getMetodoPago().getCdMetodoPago());
				}

				for (ClienteContacto clienteContacto : clienteContactoList) {

					Contacto contacto = clienteContacto.getIdContacto();
					List<MedioCnt> medioCntList = contacto.getMedioCntList();

					for (MedioCnt medioContacto : medioCntList) {

						Mail idMail = medioContacto.getIdMail();
						Telefono idTelefono = medioContacto.getIdTelefono();

						if (idMail != null)
							log.debug(idMail.getTpMail().getNbTipo());

						if (idTelefono != null)
							log.debug(idTelefono.getTpTelefono().getNbTelefono());
					}
				}

				for (SerieConstancia sc : cliente.getSerieConstanciaList()) {
					log.debug("Serie Constancia: idCliente {} - idPlanta {} - TipoSerie {} - Serie {}",
							sc.getSerieConstanciaPK().getCliente().getCteCve(),
							sc.getSerieConstanciaPK().getPlanta().getPlantaCve(),
							sc.getSerieConstanciaPK().getTpSerie(), sc.getNuSerie());
				}
			}
		} catch (Exception ex) {
			log.error("Problema para obtener el listado de clientes...", ex);
		} finally {
			EntityManagerUtil.close(em);
		}
		return lista;
	}

	@Override
	public List<Cliente> buscarPorCriterios(Cliente e) {

		return null;
	}

	@SuppressWarnings("unchecked")
	public List<Cliente> buscarHabilitados(boolean isHabilitado) {
		List<Cliente> lista = null;
		EntityManager em = null;
		Query query = null;

		try {
			em = EntityManagerUtil.getEntityManager();
			query = em.createNamedQuery("Cliente.findByHabilitado", Cliente.class).setParameter("habilitado",
					isHabilitado);
			lista = query.getResultList();
		} catch (Exception ex) {
			log.error("Problema para obtener el listado de clientes...", ex);
		} finally {
			EntityManagerUtil.close(em);
		}
		return lista;
	}

	@SuppressWarnings("unchecked")
	public List<Cliente> buscarHabilitados(boolean isHabilitado, boolean isFullInfo) {
		List<Cliente> lista = null;
		EntityManager em = null;
		Query query = null;

		try {
			em = EntityManagerUtil.getEntityManager();
			query = em.createNamedQuery("Cliente.findByHabilitado", Cliente.class).setParameter("habilitado",
					isHabilitado);
			lista = query.getResultList();

			if (isFullInfo == false)
				return lista;

			for (Cliente cliente : lista) {
				if (cliente.getCandadoSalida() != null)
					cliente.getCandadoSalida().getId();

				List<ClienteContacto> clienteContactoList = cliente.getClienteContactoList();
				log.debug(cliente.getRegimenFiscal().getCd_regimen());
				log.debug(cliente.getUsoCfdi().getUsoCfdi());
				log.debug(cliente.getMetodoPago().getNbMetodoPago());

				for (ClienteContacto clienteContacto : clienteContactoList) {

					Contacto contacto = clienteContacto.getIdContacto();
					List<MedioCnt> medioCntList = contacto.getMedioCntList();

					for (MedioCnt medioContacto : medioCntList) {

						Mail idMail = medioContacto.getIdMail();
						Telefono idTelefono = medioContacto.getIdTelefono();

						if (idMail != null)
							log.debug(idMail.getTpMail().getNbTipo());

						if (idTelefono != null)
							log.debug(idTelefono.getTpTelefono().getNbTelefono());
					}
				}
			}
		} catch (Exception ex) {
			log.error("Problema para obtener el listado de clientes...", ex);
		} finally {
			EntityManagerUtil.close(em);
		}
		return lista;
	}

	public List<Cliente> buscarTodosSerieConstancia() {
		List<Cliente> lista = null;
		EntityManager em = null;

		try {
			em = EntityManagerUtil.getEntityManager();
			lista = em.createNamedQuery("Cliente.findAll", Cliente.class).getResultList();

			for (Cliente c : lista) {

				for (SerieConstancia sc : c.getSerieConstanciaList()) {
					log.debug("Cliente: {} - TipoSerie: {} - Planta: {} - Serie: {}",
							sc.getSerieConstanciaPK().getCliente().getCteCve(), sc.getSerieConstanciaPK().getTpSerie(),
							sc.getSerieConstanciaPK().getPlanta().getPlantaCve(), sc.getNuSerie());
				}
			}
		} catch (Exception ex) {
			log.error("Problema para obtener el listado de clientes...", ex);
		} finally {
			EntityManagerUtil.close(em);
		}

		return lista;
	}

	public Cliente buscarDetalleSerieConstancia(Integer idCliente) {
		Cliente cliente = null;
		EntityManager em = null;

		try {
			em = EntityManagerUtil.getEntityManager();
			cliente = em.find(Cliente.class, idCliente);

			for (SerieConstancia sc : cliente.getSerieConstanciaList()) {
				log.debug("Cliente: {} - TipoSerie: {} - Planta: {} - Serie: {}",
						sc.getSerieConstanciaPK().getCliente().getCteCve(), sc.getSerieConstanciaPK().getTpSerie(),
						sc.getSerieConstanciaPK().getPlanta().getPlantaCve(), sc.getNuSerie());
			}

		} catch (Exception ex) {
			log.error("Problema para obtener el cliente con el detealle de sus series de constancias...", ex);
		} finally {
			EntityManagerUtil.close(em);
		}

		return cliente;
	}

	@Override
	public String actualizar(Cliente cliente) {
		EntityManager em = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.merge(cliente);
			em.getTransaction().commit();
		} catch (Exception e) {
			log.error("Problema en la actualización del cliente: " + cliente.getCteCve(), e);
			return "ERROR";
		} finally {
			EntityManagerUtil.close(em);
		}
		return null;
	}

	@Override
	public String guardar(Cliente cliente) {
		EntityManager em = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.persist(cliente);
			em.getTransaction().commit();
		} catch (Exception e) {
			log.error("Problema al guardar el cliente: " + cliente.getCteCve(), e);
			return "ERROR";
		} finally {
			EntityManagerUtil.close(em);
		}
		return null;
	}

	@Override
	public String eliminar(Cliente cliente) {
		EntityManager em = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();

			for (ClienteContacto ct : cliente.getClienteContactoList()) {
				for (MedioCnt medio : ct.getIdContacto().getMedioCntList()) {
//					em.remove(em.merge(medio));
					em.createQuery("DELETE MedioCnt m WHERE m.idMedio =:idMedio")
							.setParameter("idMedio", medio.getIdMedio()).executeUpdate();
					if (medio.getIdTelefono() != null) {
//						em.remove(em.merge(medio.getIdTelefono()));
						em.createQuery("DELETE FROM Telefono t WHERE t.idTelefono = :idTel")
								.setParameter("idTel", medio.getIdTelefono().getIdTelefono()).executeUpdate();
					}
					if (medio.getIdMail() != null) {
//						em.remove(em.merge(medio.getIdMail()));
						em.createQuery("DELETE FROM Mail m WHERE m.idMail = :idMail")
								.setParameter("idMail", medio.getIdMail().getIdMail()).executeUpdate();
					}
				}
//				em.remove(em.merge(ct));
//				em.remove(em.merge(ct.getIdContacto()));
				em.createQuery("DELETE FROM ClienteContacto ct WHERE ct.id = :clienteCon")
						.setParameter("clienteCon", ct.getId()).executeUpdate();
				em.createQuery("DELETE FROM Contacto con WHERE con.idContacto = :idCon")
						.setParameter("idCon", ct.getIdContacto().getIdContacto()).executeUpdate();
			}
//			em.remove(em.merge(cliente));
			em.createQuery("DELETE FROM Cliente cte WHERE cte.cteCve = :idCliente")
					.setParameter("idCliente", cliente.getCteCve()).executeUpdate();
			em.getTransaction().commit();
		} catch (Exception e) {
			log.error("Problema para eliminar el cliente: " + cliente.getCteCve(), e);
			return "ERROR";
		} finally {
			EntityManagerUtil.close(em);
		}
		return null;
	}

	public String eliminar(Integer cteCve) {
		EntityManager em = null;
		Cliente cliente = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			cliente = em.createNamedQuery("Cliente.findByCteCve", Cliente.class).setParameter("cteCve", cteCve)
					.getSingleResult();
			/*
			 * if(candadoSalida != null) { log.debug(cliente.getCandadoSalida().getId());
			 * cliente.remove(candadoSalida); em.merge(cliente); }
			 */

			em.createQuery("DELETE FROM CandadoSalida cs WHERE cs.cliente.cteCve = :idCliente")
					.setParameter("idCliente", cliente.getCteCve()).executeUpdate();

			SerieConstanciaDAO serieDAO = new SerieConstanciaDAO();
			SerieConstanciaPK seriePK = new SerieConstanciaPK();
			seriePK.setCliente(cliente);

			List<SerieConstancia> lstSerieConstancia = serieDAO.buscarPorIdCliente(seriePK);

			if (lstSerieConstancia.size() > 0) {

				for (SerieConstancia sc : lstSerieConstancia) {
					serieDAO.eliminar(sc);
				}

			}

			List<ClienteContacto> clienteContactoList = cliente.getClienteContactoList();

			for (ClienteContacto ct : clienteContactoList) {
				@SuppressWarnings("unused")
				Contacto contacto = ct.getIdContacto();
				List<MedioCnt> medioCntList = ct.getIdContacto().getMedioCntList();

				for (MedioCnt medio : medioCntList) {
					@SuppressWarnings("unused")
					Telefono telefono = medio.getIdTelefono();
					@SuppressWarnings("unused")
					Mail mail = medio.getIdMail();
				}
			}
			em.createQuery("DELETE FROM Cliente cte WHERE cte.cteCve = :idCliente")
					.setParameter("idCliente", cliente.getCteCve()).executeUpdate();
			em.getTransaction().commit();
		} catch (Exception e) {
			log.error("Problema al eliminar al cliente: " + cliente, e);
			return "ERROR";
		} finally {
			EntityManagerUtil.close(em);
		}
		return null;
	}

	@Override
	public String eliminarListado(List<Cliente> listado) {
		EntityManager em = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			for (Cliente cliente : listado) {
				em.remove(em.merge(cliente));
			}
			em.getTransaction().commit();
			em.close();
		} catch (Exception e) {
			log.error("Problema para eliminar el listado de clientes: " + listado, e);
			return "ERROR";
		} finally {
			EntityManagerUtil.close(em);
		}
		return null;
	}

	public Cliente buscarProductosServicios(Integer idCliente) {
		Cliente cliente = null;
		EntityManager em = null;

		try {
			em = EntityManagerUtil.getEntityManager();
			cliente = em.find(Cliente.class, idCliente);

			log.debug("Productos del cliente: " + cliente.getProductoPorClienteList().size());
			for (ProductoPorCliente ppc : cliente.getProductoPorClienteList()) {
				log.debug("Producto por cliente: {}", ppc.getProdXCteCve());
			}

			log.debug("Servicios del cliente: ", cliente.getPrecioServicioList());
			for (PrecioServicio ps : cliente.getPrecioServicioList()) {
				log.debug("Precio Servicio por cliente: {}", ps.getId());
				log.debug("Aviso: ", ps.getAvisoCve().getAvisoCve());
			}

		} catch (Exception ex) {

		} finally {
			EntityManagerUtil.close(em);
		}

		return cliente;
	}
}
