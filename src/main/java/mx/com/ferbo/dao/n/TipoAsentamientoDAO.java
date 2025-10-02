package mx.com.ferbo.dao.n;

import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.TipoAsentamiento;
import mx.com.ferbo.util.EntityManagerUtil;
import mx.com.ferbo.util.InventarioException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author alberto
 */
@Named
@ApplicationScoped
public class TipoAsentamientoDAO extends BaseDAO<TipoAsentamiento, Integer> 
{
    Logger log = LogManager.getLogger(TipoAsentamiento.class);

    public TipoAsentamientoDAO() {
        super(TipoAsentamiento.class);
    }

    public List<TipoAsentamiento> buscarTodos() throws InventarioException {
        List<TipoAsentamiento> listado = null;
        EntityManager em = null;
        try {
            em = EntityManagerUtil.getEntityManager();
            listado = em.createNamedQuery("TipoAsentamiento.findAll", TipoAsentamiento.class)
                .getResultList();
        } catch (Exception e) {
            log.error("Error al obtener informacion", e);
            throw new InventarioException("Error al obtener información en la base de datos.");
        } finally {
            EntityManagerUtil.close(em);
        }

        return listado;
    }

    public List<TipoAsentamiento> buscarPorCriterios(TipoAsentamiento e) {
        // TODO Auto-generated method stub
        return null;
    }

}
