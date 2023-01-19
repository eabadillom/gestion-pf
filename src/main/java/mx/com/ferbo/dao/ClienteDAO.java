package mx.com.ferbo.dao;

import java.util.List;

import javax.persistence.EntityManager;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.ClienteContacto;
import mx.com.ferbo.model.MedioCnt;
import mx.com.ferbo.util.EntityManagerUtil;

public class ClienteDAO extends IBaseDAO<Cliente, Integer> {

	@Override
	public Cliente buscarPorId(Integer id) {
		// TODO Auto-generated method stub
		EntityManager em = EntityManagerUtil.getEntityManager();		
		return em.createNamedQuery("Cliente.findByCteCve", Cliente.class).
				setParameter("cteCve", id)
				.getSingleResult();
	}

	@Override
	public List<Cliente> buscarTodos() {
		EntityManager em = EntityManagerUtil.getEntityManager();
		List<Cliente> listado = null;
		listado = em.createNamedQuery("Cliente.findAll", Cliente.class).getResultList();		
		return listado;
	}

	@Override
	public List<Cliente> buscarPorCriterios(Cliente e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String actualizar(Cliente cliente) {
		EntityManager em = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.merge(cliente);
			em.getTransaction().commit();
			em.close();
		} catch (Exception e) {
			System.out.println("ERROR" + e.getMessage());
			return "ERROR";
		}finally {
			if(em.isOpen()) {
				try {
					em.close();
				}catch (Exception e) {
					System.out.println("ERROR" + e.getMessage());
					return "ERROR";
				}
			}
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
			em.close();
		} catch (Exception e) {
			System.out.println("ERROR" + e.getMessage());
			return "ERROR";
		}finally {
			if(em.isOpen()) {
				try {
					em.close();
				}catch (Exception e) {
					System.out.println("ERROR" + e.getMessage());
					return "ERROR";
				}
			}
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
			System.out.println("ERROR" + e.getMessage());
			return "ERROR";
		}finally {
			if(em.isOpen()) {
				try {
					em.close();
				}catch (Exception e) {
					System.out.println("ERROR" + e.getMessage());
					return "ERROR";
				}
			}
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
			System.out.println("ERROR" + e.getMessage());
			return "ERROR";
		}finally {
			if(em.isOpen()) {
				try {
					em.close();
				}catch (Exception e) {
					System.out.println("ERROR" + e.getMessage());
					return "ERROR";
				}
			}
		}
		return null;
	}

}
