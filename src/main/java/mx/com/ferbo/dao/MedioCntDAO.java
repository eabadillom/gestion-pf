package mx.com.ferbo.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.jfree.util.Log;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.MedioCnt;
import mx.com.ferbo.util.EntityManagerUtil;

public class MedioCntDAO extends IBaseDAO<MedioCnt, Integer> {

	@Override
	public MedioCnt buscarPorId(Integer id) {
		MedioCnt medioContacto = null;
		EntityManager em = null;
		Query query = null;
		
		try {
			em = EntityManagerUtil.getEntityManager();
			query = em.createNamedQuery("MedioCnt.findByIdMedio", MedioCnt.class)
					.setParameter("idMedio", id)
					;
			
			medioContacto = (MedioCnt) query.getSingleResult();
		} finally {
			EntityManagerUtil.close(em);
		}
		
		return medioContacto;
	}

	@Override
	public List<MedioCnt> buscarTodos() {
		return null;
		
	}

	@Override
	public List<MedioCnt> buscarPorCriterios(MedioCnt e) {
		
		EntityManager em = null;
		List<MedioCnt> lista = null;
		
		try {
			em = EntityManagerUtil.getEntityManager();
			lista = em.createNamedQuery("MedioCnt.findByIdContacto", MedioCnt.class)
					.setParameter("idContacto", e.getIdContacto().getIdContacto()).getResultList();
		} catch (Exception e2) {
			Log.error("Problema al encontrar registros", e2);
		}finally {
			EntityManagerUtil.close(em);
		}
		
		return lista;
	}

	@Override
	public String actualizar(MedioCnt medio) {
		
		//Implementación que dejó Gabriel
//		EntityManager em = null;
//		try {
//			em = EntityManagerUtil.getEntityManager();
//			em.getTransaction().begin();
//			if (medio.getTpMedio().equalsIgnoreCase("m")) {
//				if (medio.getIdMail().getIdMail() == null) {
//					em.persist(medio.getIdMail());
//				} else {
//					em.merge(medio.getIdMail());
//				}
//				medio.setIdTelefono(null);
//			} else {
//				if (medio.getIdTelefono().getIdTelefono() == null) {
//					em.persist(medio.getIdTelefono());
//				} else {
//					em.merge(medio.getIdTelefono());
//				}
//				medio.setIdMail(null);
//			}
//			em.merge(medio);
//			em.getTransaction().commit();
//		} catch (Exception e) {
//			e.printStackTrace();
//			return "ERROR";
//		}finally {
//			EntityManagerUtil.close(em);
//		}
//		return null;
		
		EntityManager em = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.merge(medio);
			em.getTransaction().commit();
		} catch(Exception ex) {
			ex.printStackTrace();
			return ex.getMessage();
		} finally {
			EntityManagerUtil.close(em);
		}
		return null;
	}

	@Override
	public String guardar(MedioCnt medio) {
		EntityManager em = null;
		
		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.persist(medio);
			em.getTransaction().commit();
		} catch(Exception ex) {
			ex.printStackTrace();
			return ex.getMessage();
		} finally {
			EntityManagerUtil.close(em);
		}
		
		return null;
	}

	@Override
	public String eliminar(MedioCnt medio) {
		EntityManager em = EntityManagerUtil.getEntityManager();
		try {
			em.getTransaction().begin();
			medio = em.merge(medio);
			em.remove(medio);
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
	public String eliminarListado(List<MedioCnt> listado) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public String guardaMedioCnt(MedioCnt medio) {
		EntityManager em = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			if(medio.getTpMedio().equalsIgnoreCase("m")) {
				em.persist(medio.getIdMail());
				medio.setIdTelefono(null);
			}else {
				em.persist(medio.getIdTelefono());
				medio.setIdMail(null);
			}
			em.persist(medio);
			em.getTransaction().commit();
		} catch (Exception e) {
			System.out.println("ERROR" + e.getMessage());
			return "ERROR";
		}finally {
			if(em.isOpen()) {
				try {
					em.close();
				} catch (Exception e) {
					System.out.println("ERROR" + e.getMessage());
					return "ERROR";
				}
			}
		}
		return null;
	}

}
