package mx.com.ferbo.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.ClienteContacto;
import mx.com.ferbo.model.MedioCnt;
import mx.com.ferbo.util.EntityManagerUtil;

public class ClienteContactoDAO extends IBaseDAO<ClienteContacto, Integer> {

	private static Logger log = LogManager.getLogger(ClienteContactoDAO.class);

	@Override
	public ClienteContacto buscarPorId(Integer id) {
		ClienteContacto clienteContacto = null;
		EntityManager em = null;
		Query query = null;

		try {
			em = EntityManagerUtil.getEntityManager();
			query = em.createNamedQuery("ClienteContacto.findById", ClienteContacto.class).setParameter("id", id);
			clienteContacto = (ClienteContacto) query.getSingleResult();
		} catch (Exception ex) {
			log.error("Problema al buscar el contacto del cliente por el identificador {}... ", id, ex);
		} finally {
			EntityManagerUtil.close(em);
		}

		return clienteContacto;
	}

	public ClienteContacto buscarPorId(Integer id, boolean isAllInfo) {
		ClienteContacto clienteContacto = null;
		EntityManager em = null;
		Query query = null;

		try {
			em = EntityManagerUtil.getEntityManager();
			query = em.createNamedQuery("ClienteContacto.findById", ClienteContacto.class).setParameter("id", id);
			clienteContacto = (ClienteContacto) query.getSingleResult();

			if (isAllInfo == false)
				return clienteContacto;

			for (MedioCnt medioContacto : clienteContacto.getIdContacto().getMedioCntList()) {
				log.debug("Tipo mail: " + medioContacto.getIdMail().getTpMail().getTpMail());
				log.debug("Tipo Telefono: " + medioContacto.getIdTelefono().getTpTelefono().getTpTelefono());
			}
		} catch (Exception ex) {
			log.error("Problema al buscar el contacto del cliente por el identificador...",ex);
		} finally {
			EntityManagerUtil.close(em);
		}

		return clienteContacto;
	}

	@Override
	public List<ClienteContacto> buscarTodos() {
            List<ClienteContacto> listado = null;
            EntityManager em = null;
            try {
                em = EntityManagerUtil.getEntityManager();
		listado = em.createNamedQuery("ClienteContacto.findAll", ClienteContacto.class).getResultList();
            } catch (Exception ex) {
                log.error("Problema al buscar el listado del contacto del cliente...",ex);
            } finally {
                EntityManagerUtil.close(em);
            }
            return listado;
	}

	@Override
	public List<ClienteContacto> buscarPorCriterios(ClienteContacto e) {
		
		return null;
	}

	@Override
	public String actualizar(ClienteContacto clienteContacto) {
		EntityManager em = null;
                try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
//			em.merge(clienteContacto.getIdContacto());
			em.merge(clienteContacto);
			em.getTransaction().commit();
		} catch (Exception e) {
			log.error("Problema al actualizar el contacto del cliente... ", e);
			return "ERROR";
		} finally {
                        EntityManagerUtil.close(em);
                }
		return null;
	}

	@Override
	public String guardar(ClienteContacto clienteContacto) {
		EntityManager em = null;
                try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.persist(clienteContacto);
			em.getTransaction().commit();
		} catch (Exception e) {
			log.error("Problema al guardar el cliente contacto... ", e);
			return "ERROR";
		} finally {
                        EntityManagerUtil.close(em);
                }
		return null;
	}

	@Override
	public String eliminar(ClienteContacto clienteContacto) {
		EntityManager em = null;
		Query query = null;
		ClienteContacto tmpObj = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();

			query = em.createNamedQuery("ClienteContacto.findById", ClienteContacto.class).setParameter("id",
					clienteContacto.getId());
			tmpObj = (ClienteContacto) query.getSingleResult();
			em.remove(tmpObj);
			em.getTransaction().commit();
		} catch (Exception e) {
			log.error("Problema al eliminar el contacto del cliente... ", e);
		} finally {
			EntityManagerUtil.close(em);
		}
		return null;
	}

	@Override
	public String eliminarListado(List<ClienteContacto> listado) {
		
		return null;
	}

}
