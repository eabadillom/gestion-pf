package mx.com.ferbo.dao;

import java.util.List;

import javax.persistence.EntityManager;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.Mail;
import mx.com.ferbo.util.EntityManagerUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MailDAO extends IBaseDAO<Mail, Integer> {
        private static Logger log = LogManager.getLogger(MailDAO.class);

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
	    EntityManager em = null;
            try {
                em = EntityManagerUtil.getEntityManager();
                em.getTransaction().begin();
                em.merge(mail);
                em.getTransaction().commit();
            } catch (Exception e) {
                log.error("Error al guardar el mail", e);
                return "ERROR";
            } finally {
                EntityManagerUtil.close(em);
            }
            return null;
	}

	@Override
	public String guardar(Mail mail) {
            EntityManager em = null;
            try {
                em = EntityManagerUtil.getEntityManager();
                em.getTransaction().begin();
                em.persist(mail);
                em.getTransaction().commit();
            } catch (Exception e) {
                log.error("Error al actualizar el mail", e);
                return "ERROR";
            } finally {
                EntityManagerUtil.close(em);
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
