package mx.com.ferbo.egresos.dao.catalogos;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.transaction.SystemException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.egresos.model.calogos.CategoriaEgreso;

@Named
@ApplicationScoped
public class CategoriaEgresoDAO extends BaseDAO<CategoriaEgreso, Long> {

    private static final Logger log = LogManager.getLogger(CategoriaEgresoDAO.class);

    public CategoriaEgresoDAO() {
        super(CategoriaEgreso.class);
    }

    public CategoriaEgresoDAO(Class<CategoriaEgreso> modelClass) {
        super(modelClass);
    }

    public CategoriaEgreso buscarPorClave(String clave) throws SystemException {
        EntityManager em = null;
        CategoriaEgreso categoria = null;

        try {
            em = super.getEntityManager();
            categoria = em.createNamedQuery("CategoriaEgreso.findByClave", CategoriaEgreso.class)
                    .setParameter("clave", clave).getSingleResult();
            return categoria;
        } catch (Exception ex) {
            log.error("Error al momento de buscar la categoria de egreso con clave: {}. {}", clave, ex);
            throw new SystemException("Hubo un problema al buscar la categoria de egreso.");
        } finally {
            super.close(em);
        }

    }

    public List<CategoriaEgreso> buscarActivosOInactivos(Boolean activo) throws SystemException {
        EntityManager em = null;
        List<CategoriaEgreso> categorias = null;
        try {
            em = super.getEntityManager();
            categorias = em.createNamedQuery("CategoriaEgreso.findActivosOInactivos", CategoriaEgreso.class)
                    .setParameter("activo", activo).getResultList();
            return categorias;
        } catch (Exception ex) {
            String estado = (activo) ? "activas" : "inactivas";
            log.error("Error al momento de buscar las categoria de egreso {}. {}", estado, ex);
            throw new SystemException("Hubo un problema al buscar las categorias de egreso " + estado + ".");
        } finally {
            super.close(em);
        }
    }

}
