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
			query = em.createNamedQuery("ClienteContacto.findById", ClienteContacto.class)
					.setParameter("id", id);
			clienteContacto = (ClienteContacto) query.getSingleResult();
		} catch(Exception ex) {
			ex.printStackTrace();
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
			query = em.createNamedQuery("ClienteContacto.findById", ClienteContacto.class)
					.setParameter("id", id);
			clienteContacto = (ClienteContacto) query.getSingleResult();
			
			if(isAllInfo == false)
				return clienteContacto;
			
			for(MedioCnt medioContacto : clienteContacto.getIdContacto().getMedioCntList()) {
				log.debug("Tipo mail: " + medioContacto.getIdMail().getTpMail().getTpMail() );
				log.debug("Tipo Telefono: " + medioContacto.getIdTelefono().getTpTelefono().getTpTelefono());
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		} finally {
			EntityManagerUtil.close(em);
		}
		
		return clienteContacto;
	}

	@Override
	public List<ClienteContacto> buscarTodos() {
		List<ClienteContacto> listado;
		EntityManager em = EntityManagerUtil.getEntityManager();
		listado = em.createNamedQuery("ClienteContacto.findAll", ClienteContacto.class).getResultList();
		return listado;
	}

	@Override
	public List<ClienteContacto> buscarPorCriterios(ClienteContacto e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String actualizar(ClienteContacto clienteContacto) {
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
//			em.merge(clienteContacto.getIdContacto());
			em.merge(clienteContacto);
			em.getTransaction().commit();
			em.close();
		} catch (Exception e) {
			System.out.println("ERROR" + e.getMessage());
			return "ERROR";
		}
		return null;
	}

	@Override
	public String guardar(ClienteContacto clienteContacto) {
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.persist(clienteContacto);
			em.getTransaction().commit();
			em.close();
		} catch (Exception e) {
			System.out.println("ERROR" + e.getMessage());
			return "ERROR";
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
			
			query = em.createNamedQuery("ClienteContacto.findById", ClienteContacto.class)
					.setParameter("id", clienteContacto.getId());
			tmpObj = (ClienteContacto) query.getSingleResult();
			em.remove(tmpObj);
			em.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			EntityManagerUtil.close(em);
		}
		return null;
	}
	
	

	@Override
	public String eliminarListado(List<ClienteContacto> listado) {
		// TODO Auto-generated method stub
		return null;
	}

}
