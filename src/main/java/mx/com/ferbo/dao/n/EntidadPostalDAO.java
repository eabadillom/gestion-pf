package mx.com.ferbo.dao.n;

import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.EntidadPostal;
import mx.com.ferbo.util.EntityManagerUtil;
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

    public List<EntidadPostal> buscarTodos() {
        List<EntidadPostal> listado = null;
        EntityManager em = null;
        
        try {
            em = EntityManagerUtil.getEntityManager();
            listado = em.createNamedQuery("EntidadPostal.findAll", EntidadPostal.class).getResultList();
        } catch (Exception e) {
            log.error("Error al obtener informacion", e);
        } finally {
            EntityManagerUtil.close(em);
        }
        
        return listado;
    }
    
    public EntidadPostal buscarUltimaEntidadPostal(){
        EntidadPostal entidadPostal = null;
        EntityManager em = null;
        
        try {
            em = EntityManagerUtil.getEntityManager();
            TypedQuery<EntidadPostal> query = em.createQuery(
                "SELECT ep FROM EntidadPostal ep ORDER BY ep.entidadpostalCve DESC", EntidadPostal.class
            );
            
            query.setMaxResults(1);
            entidadPostal = query.getSingleResult();
        } catch(NoResultException ex) {
            log.warn("No se encontraron entidades postales: {}", ex.getMessage());
        } catch (Exception e) {
            log.error("Error al obtener informacion", e);
        } finally {
            EntityManagerUtil.close(em);
        }
        
        return entidadPostal;
    }

}
