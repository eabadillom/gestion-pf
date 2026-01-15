package mx.com.ferbo.dao.n;

import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.Pais;
import mx.com.ferbo.util.EntityManagerUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author alberto
 */
@Named
@ApplicationScoped
public class PaisDAO extends BaseDAO<Pais, Integer> 
{
    private static Logger log = LogManager.getLogger(PaisDAO.class);
    
    public PaisDAO(Class<Pais> modelClass) {
        super(modelClass);
    }
    
    public PaisDAO(){
        super(Pais.class);
    }
    
    public List<Pais> buscarTodos() {
        List<Pais> listado = null;
        EntityManager em = null;
        
        try {
            em = EntityManagerUtil.getEntityManager();
            listado = em.createNamedQuery("Pais.findAll", Pais.class)
                .getResultList();
        } catch (Exception ex) {
            log.error("Problema al obtener la información del pais ", ex);
        } finally {
            EntityManagerUtil.close(em);
        }
        
        return listado;
    }
    
}
