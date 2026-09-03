package mx.com.ferbo.dao.n;

import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.StatusSerieComplemento;
import mx.com.ferbo.util.EntityManagerUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Named
@ApplicationScoped
public class StatusSerieComplementoDAO extends BaseDAO<StatusSerieComplemento, Integer>
{
    private Logger log = LogManager.getLogger(AsentamientoHumanoDAO.class);
    
    public StatusSerieComplementoDAO() {
        super(StatusSerieComplemento.class);
    }
    
    public List<StatusSerieComplemento> buscarTodos() {
        EntityManager entity = null;
        List<StatusSerieComplemento> listModel = null;
        
        try {
            entity = EntityManagerUtil.getEntityManager();
            listModel = entity.createNamedQuery("StatusSerieComplemento.findAll", StatusSerieComplemento.class)
                .getResultList();
        } catch (Exception e) {
            log.error("Problemas para obtener informacion", e);
        } finally {
            EntityManagerUtil.close(entity);
        }
        
        return listModel;
    }
    
}
