package mx.com.ferbo.dao.n;

import java.util.List;
import javax.persistence.EntityManager;
import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.DetallePartida;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author alberto
 */
public class DetallePartidaDAO extends BaseDAO<DetallePartida, Integer> {

    private static Logger log = LogManager.getLogger(DetallePartidaDAO.class);

    public DetallePartidaDAO() {
        super(DetallePartida.class);
    }

    public List<DetallePartida> buscarPorPartida(Integer partidaCve) {
        List<DetallePartida> list = null;
        EntityManager em = null;
        try {
            em = super.getEntityManager();
            list = em.createNamedQuery("DetallePartida.findByPartidaCve", DetallePartida.class)
                    .setParameter("partidaCve", partidaCve)
                    .getResultList();
        } catch (Exception ex) {
            log.error("Problema para obtener el listado de detalle partida...", ex);
        } finally {
            super.close(em);
        }
        return list;
    }

}
