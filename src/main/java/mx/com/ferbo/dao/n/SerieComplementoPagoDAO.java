package mx.com.ferbo.dao.n;

import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.SerieComplementoPago;
import mx.com.ferbo.util.DAOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Named
@ApplicationScoped
public class SerieComplementoPagoDAO extends BaseDAO<SerieComplementoPago, Integer>
{
    private static Logger log = LogManager.getLogger(SerieComplementoPagoDAO.class);

    public SerieComplementoPagoDAO() {
        super(SerieComplementoPago.class);
    }
    
    public SerieComplementoPago buscarPorEmisor(Integer idEmisor) throws DAOException 
    {
        SerieComplementoPago serieComplemento = null;
        EntityManager em = null;
        try {
            em = super.getEntityManager();
            serieComplemento = em.createNamedQuery("SerieComplementoPago.findByEmisor", SerieComplementoPago.class)
                .setParameter("emisor", idEmisor)
                .getSingleResult();
        } catch (Exception ex) {
            log.error("Problema en la consulta de serie complemento pago...", ex);
            throw new DAOException("Problema al obtener la serie complemento pago");
        } finally {
            super.close(em);
        }
        
        return serieComplemento;
    }
    
    public List<SerieComplementoPago> buscarTodos() throws DAOException
    {
        List<SerieComplementoPago> listSerieComplemento = null;
        EntityManager em = null;
        try {
            em = super.getEntityManager();
            listSerieComplemento = em.createNamedQuery("SerieComplementoPago.findAll", SerieComplementoPago.class)
                .getResultList();
        } catch (Exception ex) {
            log.error("Problema en la consulta de serie complemento pago...", ex);
            throw new DAOException("Problema al obtener la serie complemento pago");
        } finally {
            super.close(em);
        }
        
        return listSerieComplemento;
    }
    
    public List<SerieComplementoPago> buscarSeriesPorEmisor(Integer idEmisor) throws DAOException 
    {
        List<SerieComplementoPago> listSerieComplemento = null;
        EntityManager em = null;
        try {
            em = super.getEntityManager();
            listSerieComplemento = em.createNamedQuery("SerieComplementoPago.findByEmisor", SerieComplementoPago.class)
                .setParameter("emisor", idEmisor)
                .getResultList();
        } catch (Exception ex) {
            log.error("Problema en la consulta de serie complemento pago...", ex);
            throw new DAOException("Problema al obtener la serie complemento pago");
        } finally {
            super.close(em);
        }
        
        return listSerieComplemento;
    }
    
}
