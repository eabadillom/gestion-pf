package mx.com.ferbo.dao.n.egresos;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.n.egresos.ActivoFijo;
import mx.com.ferbo.util.DAOException;

@Named
@ApplicationScoped
public class ActivoFijoDAO extends BaseDAO <ActivoFijo, Integer> {

    private static final Logger log = LogManager.getLogger(ActivoFijoDAO.class);

    public ActivoFijoDAO(){
        super(ActivoFijo.class);
    }

    public ActivoFijo buscarPorEgreso(Integer id) throws DAOException{
        ActivoFijo activo = null;
        EntityManager em = null;
        try {
           em = super.getEntityManager();
           activo = em.createQuery("ActivoFijo.findByEgreso", ActivoFijo.class).setParameter("id", id).getSingleResult();
           return activo;
        } catch (Exception ex) {
            log.error("Error al buscar el activo fijo asociado al egreso con id {}. {}", id, ex);
            throw new DAOException("Hubo un problema al buscar el activo fijo asociado al egreso.");
        } finally {
            super.close(em);
        }
    }
}
