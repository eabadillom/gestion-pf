
package mx.com.ferbo.dao.n;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.TipoTelefono;
import mx.com.ferbo.util.DAOException;

/**
 *
 * @author julio
 */
@Named
@ApplicationScoped
public class TipoTelefonoDAO extends BaseDAO<TipoTelefono, Integer> {

    private static Logger log = LogManager.getLogger(TipoTelefonoDAO.class);

    public TipoTelefonoDAO() {
        super(TipoTelefono.class);
    }

    public List<TipoTelefono> buscarTodos() throws DAOException {
        EntityManager em = super.getEntityManager();
        List<TipoTelefono> list = new ArrayList<>();
        try {
            list = em.createNamedQuery("TipoTelefono.findAll", TipoTelefono.class).getResultList();
        } catch (Exception ex) {
            log.error("Error al optones los tipos de telefono", ex);
            throw new DAOException("Ocurrió un problema al obtener los tipos de teléfono", ex);
        } finally {
            super.close(em);
        }
        return list;
    }
}
