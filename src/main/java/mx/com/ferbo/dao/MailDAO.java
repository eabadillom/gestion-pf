package mx.com.ferbo.dao;

import java.util.List;

import javax.persistence.EntityManager;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.Mail;
import mx.com.ferbo.util.EntityManagerUtil;

public class MailDAO extends IBaseDAO<Mail, Integer> {

	@Override
	public Mail buscarPorId(Integer id) {
		return null;
	}

	@Override
	public List<Mail> buscarTodos() {
		return null;
	}

	@Override
	public List<Mail> buscarPorCriterios(Mail e) {
		return null;
	}

	@Override
	public String actualizar(Mail mail) {
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.merge(mail);
			em.getTransaction().commit();
		} catch (Exception e) {
			System.out.println("ERROR" + e.getMessage());
			return "ERROR";
		}
		return null;
	}

	@Override
	public String guardar(Mail mail) {
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.persist(mail);
			em.getTransaction().commit();
		} catch (Exception e) {
			System.out.println("ERROR" + e.getMessage());
			return "ERROR";
		}
		return null;
	}

	@Override
	public String eliminar(Mail e) {
		return null;
	}

	@Override
	public String eliminarListado(List<Mail> listado) {
		return null;
	}

}
