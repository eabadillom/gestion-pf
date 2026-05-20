package mx.com.ferbo.egresos.dao;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ferbo.tools.exception.SystemException;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.egresos.model.ArchivoEgreso;
import mx.com.ferbo.egresos.model.Egreso;

@Named
@ApplicationScoped
public class ArchivoEgresoDAO extends BaseDAO<ArchivoEgreso, Long> {

    private static final Logger log = LogManager.getLogger(ArchivoEgresoDAO.class);

    private EntityManager em;

    public ArchivoEgresoDAO() {
        super(ArchivoEgreso.class);
    }

    public ArchivoEgreso buscarPorEgreso(Egreso egreso) {
        ArchivoEgreso archivoEgreso = null;
        try {
            em = getEntityManager();
            archivoEgreso = em.createNamedQuery("ArchivoEgreso.findByEgreso", ArchivoEgreso.class)
                    .setParameter("egreso", egreso).getSingleResult();
            return archivoEgreso;
        } catch (Exception ex) {
            log.error("Error al momento de buscar el arhivo por egreso: {}. {}", egreso, ex);
            throw new SystemException("Hubo un problema al momento de buscar el arhivo del egreso seleccionado.");
        } finally {
            close(em);
        }
    }
}
