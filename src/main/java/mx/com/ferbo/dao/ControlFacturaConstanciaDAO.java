package mx.com.ferbo.dao;

import java.util.List;

import javax.persistence.EntityManager;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.ControlFacturaConstanciaDS;
import mx.com.ferbo.model.ControlFacturaConstanciaDsPK;
import mx.com.ferbo.util.EntityManagerUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ControlFacturaConstanciaDAO extends IBaseDAO<ControlFacturaConstanciaDS, ControlFacturaConstanciaDsPK> {
        private static Logger log = LogManager.getLogger(ControlFacturaConstanciaDAO.class);

	@Override
	public ControlFacturaConstanciaDS buscarPorId(ControlFacturaConstanciaDsPK id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ControlFacturaConstanciaDS> buscarTodos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ControlFacturaConstanciaDS> buscarPorCriterios(ControlFacturaConstanciaDS e) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<ControlFacturaConstanciaDS> buscarPorConstancia(Integer folio) {
		EntityManager entity = null;
		List<ControlFacturaConstanciaDS> alFacturasConstancias = null;

		try {
			entity = EntityManagerUtil.getEntityManager();
			alFacturasConstancias = entity
					.createNamedQuery("ControlFacturaConstanciaDS.findByConstancia", ControlFacturaConstanciaDS.class)
					.setParameter("constancia", folio).getResultList();
		} catch (Exception e) {
                        log.error("Problema al buscar la lista de control factura constancia ds...", e);
                } finally {
			EntityManagerUtil.close(entity);
		}

		return alFacturasConstancias;
	}

	@Override
	public String actualizar(ControlFacturaConstanciaDS e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String guardar(ControlFacturaConstanciaDS e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminar(ControlFacturaConstanciaDS e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminarListado(List<ControlFacturaConstanciaDS> listado) {
		// TODO Auto-generated method stub
		return null;
	}

}
