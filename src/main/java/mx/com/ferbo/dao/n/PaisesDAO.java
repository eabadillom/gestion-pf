package mx.com.ferbo.dao.n;

import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.Paises;
import mx.com.ferbo.util.EntityManagerUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author alberto
 */
@Named
@ApplicationScoped
public class PaisesDAO extends BaseDAO<Paises, Integer> 
{
    Logger log = LogManager.getLogger(PaisesDAO.class);
    
    public PaisesDAO(Class<Paises> modelClass) {
        super(modelClass);
    }

    public PaisesDAO() {
        super(Paises.class);
    }

    @SuppressWarnings("unchecked")
    public List<Paises> findall() {
        List<Paises> paises = null;
        EntityManager em = null;
        try {
            em = EntityManagerUtil.getEntityManager();
            Query sql = em.createNamedQuery("Paises.findAll", Paises.class);
            paises = sql.getResultList();
        } catch (Exception e) {
            log.error("Problemas para obtener informacion", e);
        } finally {
            EntityManagerUtil.close(em);
        }
        return paises;
    }

    public List<Paises> buscarTodos() {
        List<Paises> listado = null;
        EntityManager em = null;
        try {
            em = EntityManagerUtil.getEntityManager();
            listado = em.createNamedQuery("Paises.findAll", Paises.class).getResultList();
        } catch (Exception e) {
            log.error("Problemas para obtener informacion", e);
        } finally {
            EntityManagerUtil.close(em);
        }
        return listado;
    }

    public List<Paises> buscarPorCriterios(Paises e) {
        // TODO Auto-generated method stub
        return null;
    }

    public List<Paises> buscaPorId(Integer id) {
        List<Paises> listado = null;
        EntityManager em = null;
        try {
            em = EntityManagerUtil.getEntityManager();
            listado = em.createNamedQuery("Paises.findByPaisCve", Paises.class)
                    .setParameter("paisCve", id).getResultList();
        } catch (Exception e) {
            log.error("Problemas para obtener informacion", e);
        } finally {
            EntityManagerUtil.close(em);
        }
        return listado;
    }
    
    public Paises buscarUltimoPais(){
        Paises pais = null;
        EntityManager em = null;
        try {
            em = EntityManagerUtil.getEntityManager();
            TypedQuery<Paises> query = em.createQuery(
                "SELECT p FROM Paises p ORDER BY p.paisCve DESC", Paises.class
            );
            
            query.setMaxResults(1);
            pais = query.getSingleResult();
        } catch(NoResultException ex) {
            log.error("No se encontraron paises: {}", ex.getMessage());
        } catch (Exception e) {
            log.error("Problemas para obtener informacion", e);
        } finally {
            EntityManagerUtil.close(em);
        }
        
        return pais;
    }

}
