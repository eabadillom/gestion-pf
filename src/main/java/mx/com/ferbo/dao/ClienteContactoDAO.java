package mx.com.ferbo.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.ClienteContacto;
import mx.com.ferbo.model.MedioCnt;
import mx.com.ferbo.util.EntityManagerUtil;

public class ClienteContactoDAO extends IBaseDAO<ClienteContacto, Integer> {

	@Override
	public ClienteContacto buscarPorId(Integer id) {
		// TODO Auto-generated method stub
		return null;
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
			em.merge(clienteContacto.getIdContacto());
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
			em.persist(clienteContacto.getIdContacto());
			em.flush();
			clienteContacto.setFhAlta(new Date());
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
		EntityManager em = null;;
		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			
			
			for (MedioCnt medio : clienteContacto.getIdContacto().getMedioCntList()) {

				em.remove(em.merge(medio));
				
				if (medio.getIdTelefono() != null) {
					em.remove(em.merge(medio.getIdTelefono()));
				}

				if (medio.getIdMail() != null) {
					em.remove(em.merge(medio.getIdMail()));
				}
//				em.createQuery("DELETE MedioCnt m WHERE m.idMedio =:idMedio")
//						.setParameter("idMedio", medio.getIdMedio()).executeUpdate();
			}
//			
//			em.createQuery("DELETE FROM Contacto con WHERE con.idContacto = :idCon")
//					.setParameter("idCon", clienteContacto.getIdContacto().getIdContacto()).executeUpdate();

			em.remove(em.merge(clienteContacto));
			em.remove(em.merge(clienteContacto.getIdContacto()));
//			em.createQuery("DELETE ClienteContacto c WHERE c.id =:clienteContacto")
//					.setParameter("clienteContacto", clienteContacto.getId()).executeUpdate();
			
			em.getTransaction().commit();
		} catch (Exception e) {
			System.out.println("ERROR" + e.getMessage());
			return "ERROR";
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
