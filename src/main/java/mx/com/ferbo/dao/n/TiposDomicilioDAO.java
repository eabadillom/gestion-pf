package mx.com.ferbo.dao.n;

import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.TiposDomicilio;
import mx.com.ferbo.util.EntityManagerUtil;
import mx.com.ferbo.util.InventarioException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author alberto
 */
@Named
@ApplicationScoped
public class TiposDomicilioDAO extends BaseDAO<TiposDomicilio, Integer> 
{
    Logger log = LogManager.getLogger(TiposDomicilio.class);

    public TiposDomicilioDAO(Class<TiposDomicilio> modelClass) {
        super(modelClass);
    }

    public TiposDomicilioDAO() {
        super(TiposDomicilio.class);
    }

    public List<TiposDomicilio> buscarTodos() {
        List<TiposDomicilio> listado = null;
        EntityManager em = null;
        
        try {
            em = EntityManagerUtil.getEntityManager();
            listado = em.createNamedQuery("TiposDomicilio.findAll", TiposDomicilio.class).getResultList();
        } catch (Exception e) {
            log.error("Error al obtener informacion", e);
        } finally {
            EntityManagerUtil.close(em);
        }
        
        return listado;
    }

}
