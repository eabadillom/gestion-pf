package mx.com.ferbo.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.CategoriaEgreso;
import mx.com.ferbo.util.EntityManagerUtil;

/**
 *
 * @author alberto
 */
@Deprecated
public class CategoriaEgresosDAO extends IBaseDAO<CategoriaEgreso, Integer>
{
    private static Logger log = LogManager.getLogger(CategoriaEgresosDAO.class);

    public List<CategoriaEgreso> findByAll() 
    {
        EntityManager entity = null;
        List<CategoriaEgreso> list = null;

        try {
            entity = EntityManagerUtil.getEntityManager();
            list = entity.createNamedQuery("CategoriaEgreso.findByAll", CategoriaEgreso.class).getResultList();
        } catch (Exception e) {
            log.error("Error al obtener informacion", e);
        } finally {
            EntityManagerUtil.close(entity);
        }
        return list;
    }

    public CategoriaEgreso findById(Integer idEgreso) 
    {
        EntityManager em = null;
        CategoriaEgreso catEgreso = null;
        Query query = null;

        try {
            em = EntityManagerUtil.getEntityManager();
            query = em.createNamedQuery("CategoriaEgreso.findById", CategoriaEgreso.class).setParameter("idCategoria", idEgreso);

            catEgreso = (CategoriaEgreso) query.getSingleResult();

        } catch (Exception e) {
            log.info("Error al buscar Serie Factura por ID", e.getMessage());
            return null;
        } finally {
            EntityManagerUtil.close(em);
        }
        return catEgreso;
    }

    public String save(CategoriaEgreso c) 
    {
        EntityManager entity = null;
        try {
            entity = EntityManagerUtil.getEntityManager();
            entity.getTransaction().begin();
            entity.persist(c);
            entity.getTransaction().commit();
            entity.close();
        } catch (Exception e) {
            return e.getMessage();
        } finally {
            EntityManagerUtil.close(entity);
        }
        return null;
    }

    public String update(CategoriaEgreso c) 
    {
        EntityManager entity = null;
        try {
            entity = EntityManagerUtil.getEntityManager();
            entity.getTransaction().begin();
            entity.merge(c);
            entity.getTransaction().commit();
        } catch (Exception e) {
            return e.getMessage();
        } finally {
            EntityManagerUtil.close(entity);
        }
        return null;
    }

    @Override
    public CategoriaEgreso buscarPorId(Integer id) {
        return null;
    }

    @Override
    public List<CategoriaEgreso> buscarTodos() {
        return null;
    }

    @Override
    public List<CategoriaEgreso> buscarPorCriterios(CategoriaEgreso e) {
        return null;
    }

    @Override
    public String actualizar(CategoriaEgreso e) {
        return null;
    }

    @Override
    public String guardar(CategoriaEgreso e) {
        return null;
    }

    @Override
    public String eliminar(CategoriaEgreso e) {
        return null;
    }

    @Override
    public String eliminarListado(List<CategoriaEgreso> listado) {
        return null;
    }
    
}
