package mx.com.ferbo.dao.n;

import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.ConstanciaDeServicio;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author alberto
 */
@Named
@ApplicationScoped
public class ConstanciaServicioDAO extends BaseDAO<ConstanciaDeServicio, Integer> {

    private static Logger log = LogManager.getLogger(ConstanciaServicioDAO.class);

    public ConstanciaServicioDAO() {
        super(ConstanciaDeServicio.class);
    }

    public List<ConstanciaDeServicio> buscarPorFolioCliente(String folioCliente) {
        List<ConstanciaDeServicio> alConstancias = null;
        EntityManager em = null;

        try {
            em = super.getEntityManager();
            em.getTransaction().begin();
            alConstancias = em.createNamedQuery("ConstanciaDeServicio.findByFolioCliente", ConstanciaDeServicio.class)
                    .setParameter("folioCliente", folioCliente)
                    .getResultList();

            em.getTransaction().commit();
        } catch (Exception ex) {
            super.rollback(em);
        } finally {
            super.close(em);
        }

        return alConstancias;
    }

}
