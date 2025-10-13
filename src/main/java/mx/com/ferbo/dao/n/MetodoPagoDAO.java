package mx.com.ferbo.dao.n;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.MetodoPago;
import mx.com.ferbo.util.InventarioException;

@Named
@ApplicationScoped
public class MetodoPagoDAO extends BaseDAO<MetodoPago, Integer> {

    private static Logger log = LogManager.getLogger(MetodoPago.class);

    public MetodoPagoDAO() {
        super(MetodoPago.class);
    }

    public List<MetodoPago> buscarVigentes(Date fecha) throws InventarioException {
        List<MetodoPago> list = new ArrayList<>();
        EntityManager em = null;

        try {
            em = super.getEntityManager();
            list = em.createNamedQuery("MetodoPago.buscarVigentes", MetodoPago.class)
                    .setParameter("fecha", fecha).getResultList();
        } catch (Exception ex) {
            log.error("Problema para obtener información del catálogo metodo_pago", ex);
            throw new InventarioException("Problema para obtener los métodos de pago.");
        } finally {
            super.close(em);
        }
        return list;
    }

}
