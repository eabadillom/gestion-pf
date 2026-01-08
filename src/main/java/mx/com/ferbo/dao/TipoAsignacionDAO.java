package mx.com.ferbo.dao;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.n.catalogos.TipoAsignacion;
import mx.com.ferbo.util.DAOException;

@Named
@ApplicationScoped
public class TipoAsignacionDAO extends BaseDAO<TipoAsignacion, Integer> {

    private static final Logger log = LogManager.getLogger(TipoAsignacionDAO.class);

    public TipoAsignacionDAO(){
        super(TipoAsignacion.class);
    }

    public List<TipoAsignacion> buscarTiposAsignacion(boolean activo) throws DAOException {
        List<TipoAsignacion> lista = null;
        EntityManager em = null;
        try {
            em = super.getEntityManager();
            if (activo) {
                lista =  em.createQuery("TipoAsignacion.findAllActivos", TipoAsignacion.class).getResultList();
            } else {
                lista =  em.createQuery("TipoAsignacion.findAllNoActivos", TipoAsignacion.class).getResultList();
            }
            return lista;
        } catch (Exception ex) {
            String estado = activo ? "activos" : "no activos";
            log.error("Error al buscar la lista de tipos de asignacion {}. {}", estado, ex);
            throw new DAOException("Hubo un problema al buscar la lista de tipos de asignación" + estado);
        } finally {
            super.close(em);
        }
    }
}
