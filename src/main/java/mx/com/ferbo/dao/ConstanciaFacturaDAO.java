package mx.com.ferbo.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.ConstanciaFactura;
import mx.com.ferbo.util.EntityManagerUtil;

public class ConstanciaFacturaDAO extends IBaseDAO<ConstanciaFactura, Integer> {

	private static Logger log = LogManager.getLogger(ConstanciaFacturaDAO.class);

	@Override
	public ConstanciaFactura buscarPorId(Integer id) {
		return null;
	}

	@Override
	public List<ConstanciaFactura> buscarTodos() {
		return null;
	}

	@Override
	public List<ConstanciaFactura> buscarPorCriterios(ConstanciaFactura e) {
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<ConstanciaFactura> buscarPorFolioVigencia(Integer folio, Date vigenciaInicio, Date vigenciaFin) {
		List<ConstanciaFactura> list = null;
		EntityManager em = null;
		Query query = null;

		try {
			em = EntityManagerUtil.getEntityManager();
			query = em
					.createNamedQuery("ConstanciaFactura.findByFolioVigenciaInicioVigenciaFin", ConstanciaFactura.class)
					.setParameter("folio", folio).setParameter("vigenciaInicio", vigenciaInicio)
					.setParameter("vigenciaFin", vigenciaFin);
			list = query.getResultList();

		} catch (Exception ex) {
			log.error("Problema para obtener información de facturacion para la constancia folio: " + folio, ex);
		} finally {
			EntityManagerUtil.close(em);
		}

		return list;
	}

	@Override
	public String actualizar(ConstanciaFactura e) {
		return null;
	}

	@Override
	public String guardar(ConstanciaFactura e) {
		return null;
	}

	@Override
	public String eliminar(ConstanciaFactura e) {
		return null;
	}

	@Override
	public String eliminarListado(List<ConstanciaFactura> listado) {
		return null;
	}

}
