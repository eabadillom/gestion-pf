package mx.com.ferbo.dao.n;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.AsignacionEgreso;
import mx.com.ferbo.util.DAOException;

@Named
@ApplicationScoped
public class AsignacionEgresoDAO extends BaseDAO <AsignacionEgreso, Integer> {

    private static final Logger log = LogManager.getLogger(AsignacionEgresoDAO.class);

    public AsignacionEgresoDAO(){
        super(AsignacionEgreso.class);
    }

    public List<AsignacionEgreso> buscarTodosPorEgreso(Integer id) throws DAOException{
        List<AsignacionEgreso> lista = null;
        EntityManager em = null;
        try {
            em = super.getEntityManager();
            lista = em.createQuery("AsignacionEgreso.findAllByEgreso", AsignacionEgreso.class).setParameter("id", id).getResultList();
            return lista;
        } catch (Exception ex) {
            log.error("Error al buscar todas las asignaciones del egreso con id {}. {}", id, ex);
            throw new DAOException("Hubo un problema al buscar todas las asignaciones del egreso por id.");
        } finally { 
            super.close(em);
        }
    }
}
