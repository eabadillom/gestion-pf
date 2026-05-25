package mx.com.ferbo.dao;

import java.util.List;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.StatusConstanciaSalida;
import mx.com.ferbo.util.EntityManagerUtil;

public class StatusConstanciaSalidaDAO extends IBaseDAO<StatusConstanciaSalida, Integer> {

	private static Logger log = LogManager.getLogger(StatusConstanciaSalidaDAO.class);

	@Override
	public StatusConstanciaSalida buscarPorId(Integer id) {
		StatusConstanciaSalida status = null;
		EntityManager em = null;

		try {
			em = EntityManagerUtil.getEntityManager();
			status = em.find(StatusConstanciaSalida.class, id);
		} catch (Exception ex) {
			log.error("Problema para obtener el status de la constancia de salida...", ex);
		} finally {
			EntityManagerUtil.close(em);
		}

		return status;
	}

	@Override
	public List<StatusConstanciaSalida> buscarTodos() {
		return null;
	}

	@Override
	public List<StatusConstanciaSalida> buscarPorCriterios(StatusConstanciaSalida e) {
		return null;
	}

	@Override
	public String actualizar(StatusConstanciaSalida e) {
		return null;
	}

	@Override
	public String guardar(StatusConstanciaSalida e) {
		return null;
	}

	@Override
	public String eliminar(StatusConstanciaSalida e) {
		return null;
	}

	@Override
	public String eliminarListado(List<StatusConstanciaSalida> listado) {
		return null;
	}

}
