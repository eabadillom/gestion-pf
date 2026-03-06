package mx.com.ferbo.dao.n;

import java.util.List;
import javax.persistence.EntityManager;
import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.ServiciosSalida;
import mx.com.ferbo.util.DAOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author alberto
 */
public class ServiciosSalidaDAO extends BaseDAO<ServiciosSalida, Integer>
{
    private static Logger log = LogManager.getLogger(ServiciosSalidaDAO.class);
    
    public ServiciosSalidaDAO(Class<ServiciosSalida> modelClass) {
        super(modelClass);
    }
    
    public ServiciosSalidaDAO(){
        super(ServiciosSalida.class);
    }
    
    public List<ServiciosSalida> buscarPorFolioSalida(String folioSalida) throws DAOException {
        List<ServiciosSalida> listModel = null;
        EntityManager em = null;
        
        try {
            em = super.getEntityManager();
            
            listModel = em.createNamedQuery("ServiciosSalida.findByFolioSalida", ServiciosSalida.class)
                .setParameter("folioSalida", folioSalida)
                .getResultList();
        } catch (Exception ex) {
            log.error("Problema en la consulta de las salidas...", ex);
            throw new DAOException("Problema al obtener las salida");
        } finally {
            super.close(em);
        }
        
        return listModel;
    }
    
}
