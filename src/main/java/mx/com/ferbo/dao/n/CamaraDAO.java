package mx.com.ferbo.dao.n;

import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.Camara;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author alberto
 */
@Named
@ApplicationScoped
public class CamaraDAO extends BaseDAO<Camara, Integer> 
{
    private static Logger log = LogManager.getLogger(CamaraDAO.class);
    
    public CamaraDAO (){
        super(Camara.class);
    }
    
    public List<Camara> findall() {
        List<Camara> camara = null;
        Query sql = null;
        EntityManager entity = null;
        try {
            entity = super.getEntityManager();
            sql = entity.createNamedQuery("Camara.findAll", Camara.class);
            camara = sql.getResultList();
        } catch (Exception ex) {
            log.error("Problema para obtener el listado de cámaras...", ex);
        } finally {
            super.close(entity);
        }
        return camara;
    }

    public List<Camara> findall(Boolean isFullInfo) {
        List<Camara> camara = null;
        EntityManager entity = null;
        try {
            entity = super.getEntityManager();
            camara = entity.createNamedQuery("Camara.findAll", Camara.class).getResultList();

            if (isFullInfo == false) {
                return camara;
            }

            for (Camara c : camara) {
                log.debug(c.getPlantaCve().getPlantaDs());
            }

        } catch (Exception ex) {
            log.error("Problema para obtener el listado de cámaras...", ex);
        } finally {
            super.close(entity);
        }
        return camara;
    }
    
}
