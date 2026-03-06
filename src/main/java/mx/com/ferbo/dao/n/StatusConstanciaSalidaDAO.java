package mx.com.ferbo.dao.n;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.StatusConstanciaSalida;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author alberto
 */
@Named
@ApplicationScoped
public class StatusConstanciaSalidaDAO extends BaseDAO<StatusConstanciaSalida, Integer>
{
    private static Logger log = LogManager.getLogger(StatusConstanciaSalidaDAO.class);
    
    public StatusConstanciaSalidaDAO(){
        super(StatusConstanciaSalida.class);
    }
    
    public StatusConstanciaSalida buscarConstanciaPorId(Integer id) {
        StatusConstanciaSalida status = null;
        EntityManager em = null;

        try {
            em = super.getEntityManager();
            status = em.createNamedQuery("StatusConstanciaSalida.findById", StatusConstanciaSalida.class)
                .setParameter("id", id)
                .getSingleResult();
        } catch(Exception ex) {
            log.error("Problema para obtener el status de la constancia de salida...", ex);
        } finally {
            super.close(em);
        }

        return status;
    }
    
}
