package mx.com.ferbo.dao.n;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.CandadoSalida;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author alberto
 */
public class CandadoSalidaDAO extends BaseDAO<CandadoSalida, Integer> 
{
    private static Logger log = LogManager.getLogger(CandadoSalidaDAO.class);
    
    public CandadoSalidaDAO (){
        super(CandadoSalida.class);
    }
    
    public CandadoSalida buscarPorCliente(Integer idCliente) {
        CandadoSalida candado = null;
        EntityManager em = null;

        try {
            em = super.getEntityManager();
            candado = em.createNamedQuery("CandadoSalida.findByCliente", CandadoSalida.class)
                    .setParameter("idCliente", idCliente)
                    .getSingleResult();

        } catch (Exception ex) {
            log.error("Problema para obtener el candado de salida...", ex);
        } finally {
            super.close(em);
        }

        return candado;
    }
    
    public List<CandadoSalida> findAll() {
        EntityManager entity = null;
        List<CandadoSalida> list = null;
        
        try{
            entity = super.getEntityManager();
            Query sql = entity.createNamedQuery("CandadoSalida.findAll", CandadoSalida.class);
            list = sql.getResultList();

            for (CandadoSalida c : list) {
                log.debug(c.toString());
            }
        } catch (Exception ex) {
            log.error("Problema para obtener el listado de candado de salida...", ex);
        } finally {
            super.close(entity);
        }
        return list;
    }
    
}
