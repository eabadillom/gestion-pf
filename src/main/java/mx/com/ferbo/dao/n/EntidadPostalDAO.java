package mx.com.ferbo.dao.n;

import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.EntidadPostal;
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
public class EntidadPostalDAO extends BaseDAO<EntidadPostal, Integer> {

    Logger log = LogManager.getLogger(EntidadPostal.class);

    public EntidadPostalDAO() {
        super(EntidadPostal.class);
    }

    public List<EntidadPostal> buscarTodos() throws InventarioException {
        List<EntidadPostal> listado = null;
        EntityManager em = null;
        
        try {
            em = EntityManagerUtil.getEntityManager();
            listado = em.createNamedQuery("EntidadPostal.findAll", EntidadPostal.class).getResultList();
        } catch (Exception e) {
            log.error("Error al obtener informacion", e);
            throw new InventarioException("Error al obtener información en la base de datos.");
        } finally {
            EntityManagerUtil.close(em);
        }
        
        return listado;
    }

}
