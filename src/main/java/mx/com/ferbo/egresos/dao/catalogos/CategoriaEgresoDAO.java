package mx.com.ferbo.egresos.dao.catalogos;

import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ferbo.tools.exception.SystemException;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.egresos.model.calogos.CategoriaEgreso;

@Named
@ApplicationScoped
public class CategoriaEgresoDAO extends BaseDAO<CategoriaEgreso, Long> {

    private final static Logger log = LogManager.getLogger(CategoriaEgresoDAO.class);

    private EntityManager em;

    public CategoriaEgresoDAO() {
        super(CategoriaEgreso.class);
    }

    public CategoriaEgreso buscarPorClave(String clave) {
        try {
            em = getEntityManager();
            return em.createNamedQuery("CategoriaEgreso.findByClave", CategoriaEgreso.class)
                    .setParameter("clave", clave).getSingleResult();
        } catch (Exception ex) {
            log.error("Error al momento de buscar las categorias de egreso por clave. {}", ex);
            throw new SystemException(
                    "Hubo un problema al momento de buscar las categorias de egresos con la clave: " + clave);
        } finally {
            close(em);
        }
    }

    public List<CategoriaEgreso> buscarActivosOInactivos(Boolean activo) {
        try {
            em = getEntityManager();
            return em.createNamedQuery("CategoriaEgreso.findActivosOInactivos", CategoriaEgreso.class)
                    .setParameter("activo", activo).getResultList();
        } catch (Exception ex) {
            String estado = (activo) ? "activas" : "inactivas";
            log.error("Error al momento de buscar las categorias de egreso {}. {}", estado, ex);
            throw new SystemException("Hubo un problema al momento de buscar las categorias de egreso " + estado + ".");
        } finally {

        }
    }

}
