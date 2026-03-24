package mx.com.ferbo.dao.n;

import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.StatusSalida;
import mx.com.ferbo.util.DAOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author alberto
 */
@Named
@ApplicationScoped
public class StatusSalidaDAO extends BaseDAO<StatusSalida, Integer> 
{
    private static Logger log = LogManager.getLogger(StatusSalidaDAO.class);

    public StatusSalidaDAO(Class<StatusSalida> modelClass) {
        super(modelClass);
    }

    public StatusSalidaDAO() {
        super(StatusSalida.class);
    }

    public List<StatusSalida> findAll() throws DAOException {
        List<StatusSalida> lista = null;
        EntityManager em = null;

        try {
            em = super.getEntityManager();
            lista = em.createNamedQuery("StatusSalida.findAll", StatusSalida.class)
                .getResultList();
        } catch (Exception ex) {
            log.error("Problema en la consulta del status salida...", ex);
            throw new DAOException("Problema al obtener el status de salida");
        } finally {
            super.close(em);
        }
        
        return lista;
    }
    
    public StatusSalida findByClave(String clave) throws DAOException {
        StatusSalida model = null;
        
        EntityManager em = null;

        try {
            em = super.getEntityManager();
            model = em.createNamedQuery("StatusSalida.findByClave", StatusSalida.class)
                .setParameter("clave", clave)
                .getSingleResult();
        } catch (Exception ex) {
            log.error("Problema en la consulta del status salida...", ex);
            throw new DAOException("Problema al obtener el status de salida");
        } finally {
            super.close(em);
        }
        
        return model;
    }

}
