package mx.com.ferbo.dao;

import java.util.List;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.CategoriaEgreso;
import mx.com.ferbo.model.EmisoresCFDIS;
import mx.com.ferbo.model.Egresos;
import mx.com.ferbo.util.EntityManagerUtil;

/**
 *
 * @author alberto
 */
@Deprecated
public class EgresosDAO extends IBaseDAO<Egresos, Integer>
{
    private static Logger log = LogManager.getLogger(EgresosDAO.class);

    @SuppressWarnings("unchecked")
    public List<Egresos> findByAll() 
    {
        List<Egresos> listaEgresos = null;
        EntityManager entity = null;

        try {
            entity = EntityManagerUtil.getEntityManager();
            listaEgresos = entity.createNamedQuery("Egresos.findByAll", Egresos.class).getResultList();
        } catch (Exception e) {
            log.error("Error al obtener informacion", e);
        } finally {
            EntityManagerUtil.close(entity);
        }
        return listaEgresos;
    }

    @Override
    public Egresos buscarPorId(Integer id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Egresos> buscarTodos() 
    {
        List<Egresos> listaEgresos = null;
        EntityManager entity = null;

        try {
            entity = EntityManagerUtil.getEntityManager();
            listaEgresos = entity.createNamedQuery("Egresos.findByAll", Egresos.class).getResultList();
        } catch (Exception e) {
            log.error("Error al obtener informacion", e);
        } finally {
            EntityManagerUtil.close(entity);
        }
        return listaEgresos;
    }

    @Override
    public List<Egresos> buscarPorCriterios(Egresos e) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String actualizar(Egresos e) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String guardar(Egresos es) 
    {
        EntityManager entity = null;
        try {
            entity = EntityManagerUtil.getEntityManager();
            entity.getTransaction().begin();
            entity.persist(es);
            entity.getTransaction().commit();
            entity.close();
        } catch (Exception e) {
            return e.getMessage();
        } finally {
            EntityManagerUtil.close(entity);
        }
        return null;
    };

    @Override
    public String eliminar(Egresos e) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String eliminarListado(List<Egresos> listado) {
        // TODO Auto-generated method stub
        return null;
    }
    
}
