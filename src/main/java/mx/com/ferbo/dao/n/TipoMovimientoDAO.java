package mx.com.ferbo.dao.n;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.TipoMovimiento;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author alberto
 */
public class TipoMovimientoDAO extends BaseDAO<TipoMovimiento, Integer> 
{
    private static Logger log = LogManager.getLogger(TipoMovimientoDAO.class);
    
    public TipoMovimientoDAO (){
        super(TipoMovimiento.class);
    }
    
    public List<TipoMovimiento> buscarTodos() {
        List<TipoMovimiento> lista = null;
        EntityManager em = null;
        Query query = null;

        try {
            em = super.getEntityManager();
            query = em.createNamedQuery("TipoMovimiento.findAll", TipoMovimiento.class);
            lista = query.getResultList();
        } catch (Exception ex) {
            log.error("Problema para obtener los tipos de movimiento...", ex);
        } finally {
            super.close(em);
        }
        return lista;
    }
    
}
