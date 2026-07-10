package mx.com.ferbo.dao.n;

import java.util.Date;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.ComplementoPago;
import mx.com.ferbo.util.DAOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Named
@ApplicationScoped
public class ComplementoPagoDAO extends BaseDAO<ComplementoPago, Integer>
{
    private static Logger log = LogManager.getLogger(ComplementoPagoDAO.class);

    public ComplementoPagoDAO() {
        super(ComplementoPago.class);
    }
    
    public List<ComplementoPago> buscarPorPeriodoRegistro(Date inicio, Date fin) throws DAOException
    {
        List<ComplementoPago> listComplementoPago = null;
        EntityManager em = null;
        try {
            em = super.getEntityManager();
            listComplementoPago = em.createNamedQuery("ComplementoPago.findByRegistro", ComplementoPago.class)
                .setParameter("inicio", inicio)
                .setParameter("fin", fin)
                .getResultList();
        } catch (Exception ex) {
            log.error("Problema al obtener la lista de complementos de pago...", ex);
            throw new DAOException("Problema al obtener los complementos de pagos");
        } finally {
            super.close(em);
        }
        return listComplementoPago;
    }
    
    public List<ComplementoPago> buscarPorPeriodoTimbrado(Date inicio, Date fin) throws DAOException 
    {
        List<ComplementoPago> listComplementoPago = null;
        EntityManager em = null;
        try {
            em = super.getEntityManager();
            listComplementoPago = em.createNamedQuery("ComplementoPago.findByTimbrado", ComplementoPago.class)
                .setParameter("inicio", inicio)
                .setParameter("fin", fin)
                .getResultList();
        } catch (Exception ex) {
            log.error("Problema al obtener la lista de complementos de pago...", ex);
            throw new DAOException("Problema al obtener los complementos de pagos");
        } finally {
            super.close(em);
        }
        return listComplementoPago;
    }
    
}
