package mx.com.ferbo.pagos.dao;

import java.util.Date;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ferbo.tools.exception.SystemException;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.MetodoPago;

@Named
@ApplicationScoped
public class MetodoPagoDAO extends BaseDAO<MetodoPago, Integer> {

    private static Logger log = LogManager.getLogger(MetodoPago.class);

    private EntityManager em;

    public MetodoPagoDAO() {
        super(MetodoPago.class);
    }

    public List<MetodoPago> buscarVigentes(Date fecha) throws SystemException {
        try {
            em = getEntityManager();
            return em.createNamedQuery("MetodoPago.buscarVigentes", MetodoPago.class)
                    .setParameter("fecha", fecha).getResultList();
        } catch(Exception ex) {
            log.error("Error al obtener los métodos de pago", ex);
            throw new SystemException("Ocurrió un problema al obtener los métodos de pago vigentes hasta la fecha: " + fecha);
        } finally {
            close(em);
        }
    }

}
