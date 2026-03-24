package mx.com.ferbo.dao.n;

import javax.persistence.EntityManager;
import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.Partida;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author alberto
 */
public class PartidaDAO extends BaseDAO<Partida, Integer> 
{
    private static Logger log = LogManager.getLogger(Partida.class);

    public PartidaDAO() {
        super(Partida.class);
    }

    public Partida buscarPorIdConEntrada(Integer partidaCve) {
        EntityManager em = null;
        Partida p = null;
        try {
            em = super.getEntityManager();
            p = em.createNamedQuery("Partida.findByPartidaCve", Partida.class)
                    .setParameter("partidaCve", partidaCve)
                    .getSingleResult();
            
            log.info("Constancia de deposito: {}", p.getFolio().getFolio());
            log.info("Planta: {}", p.getCamaraCve().getPlantaCve().getPlantaAbrev());
            log.info("Producto: {}", p.getUnidadDeProductoCve().getProductoCve().getProductoCve());
            log.info("Unidad: {}", p.getUnidadDeProductoCve().getUnidadDeManejoCve().getUnidadDeManejoCve());

        } catch (Exception ex) {
            log.error("Problema para obtener la partida: " + partidaCve, ex);
        } finally {
            super.close(em);
        }
        return p;
    }
    
}
