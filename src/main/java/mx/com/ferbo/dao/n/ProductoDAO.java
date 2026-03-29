package mx.com.ferbo.dao.n;

import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.Producto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author alberto
 */
@Named
@ApplicationScoped
public class ProductoDAO extends BaseDAO<Producto, Integer>
{
    private static Logger log = LogManager.getLogger(ProductoDAO.class);
    
    public ProductoDAO(){
        super(Producto.class);
    }
    
    public List<Producto> buscarPorCliente(Integer idCliente) {
        List<Producto> modelList = null;
        EntityManager em = null;

        try {
            em = super.getEntityManager();
            modelList = em.createNamedQuery("Producto.findByCliente", Producto.class)
                    .setParameter("idCliente", idCliente)
                    .getResultList();
        } catch (Exception ex) {
            log.error("Problema para obtener el listado de productos por cliente...", ex);
        } finally {
            super.close(em);
        }

        return modelList;
    }
    
}
