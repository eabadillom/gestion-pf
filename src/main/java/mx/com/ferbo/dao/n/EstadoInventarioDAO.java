package mx.com.ferbo.dao.n;

import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.EstadoInventario;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author alberto
 */
@Named
@ApplicationScoped
public class EstadoInventarioDAO extends BaseDAO<EstadoInventario, Integer>
{
    private static Logger log = LogManager.getLogger(EstadoInventarioDAO.class);
    
    public EstadoInventarioDAO(){
        super(EstadoInventario.class);
    }
    
    public List<EstadoInventario> buscarTodos() {
        List<EstadoInventario> lista = null;
        EntityManager em = null;
        Query query = null;

        try {
            em = super.getEntityManager();
            query = em.createNamedQuery("EstadoInventario.findAll", EstadoInventario.class);
            lista = query.getResultList();
        } catch (Exception ex) {
            log.error("Problema para obtener los tipos de movimiento...", ex);
        } finally {
            super.close(em);
        }
        return lista;
    }
    
}
