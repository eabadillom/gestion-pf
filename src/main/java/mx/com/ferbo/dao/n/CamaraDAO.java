package mx.com.ferbo.dao.n;

import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.Camara;
import mx.com.ferbo.util.EntityManagerUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author alberto
 */
@Named
@ApplicationScoped
public class CamaraDAO extends BaseDAO <Camara, Integer>
{
    private static Logger log = LogManager.getLogger(CamaraDAO.class);
    
    public CamaraDAO(Class<Camara> modelClass) {
        super(modelClass);
    }
    
    public CamaraDAO()
    {
        super(Camara.class);
    }
    
    public List<Camara> findall() 
    {
        List<Camara> camara = null;
        Query sql = null;
        EntityManager entity = null;
        
        try {
                entity = getEntityManager();
                sql = entity.createNamedQuery("Camara.findAll", Camara.class);
                camara = sql.getResultList();
        } catch(Exception ex) {
                log.error("Problema para obtener el listado de cámaras...", ex);
        } finally {
                EntityManagerUtil.close(entity);
        }
        
        return camara;
    }
    
    public List<Camara> findById(Integer idPlanta)
    {
        List<Camara> listCamaras = null;
        Query sql = null;
        EntityManager entity = null;
        
        try {
                entity = getEntityManager();
                
                sql = entity.createNamedQuery("Camara.findByPlantaCve", Camara.class)
                    .setParameter("plantaCve", idPlanta);                
                
                listCamaras = sql.getResultList();
        } catch(Exception ex) {
                log.error("Problema para obtener el listado de cámaras...", ex);
        } finally {
                EntityManagerUtil.close(entity);
        }
        
        return listCamaras;
    }
    
}
