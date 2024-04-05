package mx.com.ferbo.dao;

import java.util.List;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.TipoMail;
import mx.com.ferbo.util.EntityManagerUtil;

public class TipoMailDAO extends IBaseDAO<TipoMail, Integer> {

	private static Logger log = LogManager.getLogger(TipoMailDAO.class);

	@Override
	public TipoMail buscarPorId(Integer id) {
		TipoMail tipo = null;
		EntityManager em = null;

		try {
			em = EntityManagerUtil.getEntityManager();
			tipo = em.find(TipoMail.class, id);
		} catch (Exception ex) {
			log.error("Problema para obtener el tipo de mail: {}", id);
		} finally {
			EntityManagerUtil.close(em);
		}

		return tipo;
	}

	@Override
	public List<TipoMail> buscarTodos() {
		EntityManager em = EntityManagerUtil.getEntityManager();
		List<TipoMail> listado = null;
		listado = em.createNamedQuery("TipoMail.findAll", TipoMail.class).getResultList();
		return listado;
	}

	@Override
	public List<TipoMail> buscarPorCriterios(TipoMail e) {
		return null;
	}

	@Override
	public String actualizar(TipoMail e) {
		return null;
	}

	@Override
	public String guardar(TipoMail e) {
		return null;
	}

	@Override
	public String eliminar(TipoMail e) {
		return null;
	}

	@Override
	public String eliminarListado(List<TipoMail> listado) {
		return null;
	}

}
