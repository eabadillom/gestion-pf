package mx.com.ferbo.dao;

import java.util.List;

import javax.persistence.EntityManager;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.AsentamientoHumano;
import mx.com.ferbo.util.EntityManagerUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ColoniasDAO extends IBaseDAO<AsentamientoHumano, Integer> {
        private static Logger log = LogManager.getLogger(ColoniasDAO.class);

	@Override
	public AsentamientoHumano buscarPorId(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AsentamientoHumano> buscarTodos() {
            List<AsentamientoHumano> listado = null;
            EntityManager em = null;
            try {
                em = EntityManagerUtil.getEntityManager();
		listado = em.createNamedQuery("AsentamientoHumano.findAll", AsentamientoHumano.class).getResultList();
            } catch (Exception e) {
                log.error("Problema al eliminar la colonia...", e);
            } finally {
                EntityManagerUtil.close(em);
            }
            return listado;
	}

	@Override
	public List<AsentamientoHumano> buscarPorCriterios(AsentamientoHumano e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String actualizar(AsentamientoHumano e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String guardar(AsentamientoHumano e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminar(AsentamientoHumano e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminarListado(List<AsentamientoHumano> listado) {
		// TODO Auto-generated method stub
		return null;
	}

}
