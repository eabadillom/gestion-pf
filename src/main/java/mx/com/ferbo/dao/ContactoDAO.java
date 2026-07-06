package mx.com.ferbo.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.ClienteContacto;
import mx.com.ferbo.model.Contacto;
import mx.com.ferbo.util.EntityManagerUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ContactoDAO extends IBaseDAO<Contacto, Integer> {
        private static Logger log = LogManager.getLogger(ContactoDAO.class);
        
	@Override
	public Contacto buscarPorId(Integer id) {

		return null;
	}

	@Override
	public List<Contacto> buscarTodos() {
		return null;
	}

	@Override
	public List<Contacto> buscarPorCriterios(Contacto e) {
		return null;
	}

	@Override
	public String actualizar(Contacto contacto) {
		EntityManager em = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.merge(contacto);
			em.getTransaction().commit();
		} catch (Exception e) {
			log.error("Error al actualizar el contacto...", e);
			return "ERROR";
		} finally {
			EntityManagerUtil.close(em);
		}
		return null;
	}

	@Override
	public String guardar(Contacto contacto) {
		return null;
	}

	@Override
	public String eliminar(Contacto contacto) {
		EntityManager em = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.remove(em.merge(contacto));
			em.getTransaction().commit();
		} catch (Exception e) {
			log.error("Error al eliminar el contacto... " + e.getMessage());
			return "ERROR";
		} finally {
			EntityManagerUtil.close(em);
		}
		return null;
	}

	@Override
	public String eliminarListado(List<Contacto> listado) {
		return null;
	}

	public String guardarClienteContacto(Contacto contacto, Cliente cliente) {
		ClienteContactoDAO clienteContactoDAO = new ClienteContactoDAO();
		ClienteContacto clienteContacto = new ClienteContacto();
		String error = null;
		EntityManager em = null;
                try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.persist(contacto);
			em.getTransaction().commit();

			clienteContacto.setIdCliente(cliente);
			clienteContacto.setIdContacto(contacto);
			clienteContacto.setFhAlta(new Date());
			clienteContacto.setStHabilitado(true);
			clienteContacto.setStUsuario("A");
			error = clienteContactoDAO.guardar(clienteContacto) == null ? null : "ERROR";
		} catch (Exception e) {
			log.error("Error al guardar el contacto... ", e);
			error = "ERROR";
		} finally {
			EntityManagerUtil.close(em);
		}
		return error;
	}

}
