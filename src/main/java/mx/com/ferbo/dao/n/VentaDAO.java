package mx.com.ferbo.dao.n;

import java.util.Date;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.Ventas;
import mx.com.ferbo.util.EntityManagerUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author alberto
 */
@Named
@ApplicationScoped
public class VentaDAO extends BaseDAO<Ventas, Integer> 
{
    private static Logger log = LogManager.getLogger(VentaDAO.class);
    
    public VentaDAO(Class<Ventas> modelClass) {
        super(modelClass);
    }
    
    public VentaDAO(){
        super(Ventas.class);
    }
    
    public List<Ventas> buscarTodos() 
    {
        EntityManager em = null;
        Query query = null;
        List<Ventas> list = null;

        try {
            em = EntityManagerUtil.getEntityManager();
            query = em.createNamedQuery("Ventas.findByAll", Ventas.class);

            list = query.getResultList();

        } catch (Exception e) {
            log.error("Error al buscar las ventas: ", e);
            return null;
        } finally {
            close(em);
        }

        return list;
    }
    
    public List<Ventas> buscarPorParametro(Integer idCliente, Integer idEmisor, Date fechaIni, Date fechaFin)
    {
        EntityManager em = null;
        List<Ventas> listModel = null;
        
        try {
            em = EntityManagerUtil.getEntityManager();
            
            listModel = em.createNamedQuery("Ventas.findByParametros", Ventas.class)
                .setParameter("idCliente", idCliente)
                .setParameter("idEmisor", idEmisor)
                .setParameter("fechaIni", fechaIni)
                .setParameter("fechaFin", fechaFin)
                .getResultList();
            
            for(Ventas venta : listModel){
                log.debug("DetalleVenta: {}", venta.getDetalles().toString());
            }
            
        } catch (Exception e) {
            log.error("Error al buscar las ventas: ", e);
            return null;
        } finally {
            close(em);
        }
        
        return listModel;
    }
    
}
