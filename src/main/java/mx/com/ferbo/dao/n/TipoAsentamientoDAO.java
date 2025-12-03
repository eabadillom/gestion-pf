package mx.com.ferbo.dao.n;

import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.TipoAsentamiento;
import mx.com.ferbo.util.EntityManagerUtil;
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

    public List<TipoAsentamiento> buscarTodos() {
        List<TipoAsentamiento> listado = null;
        EntityManager em = null;
        try {
            em = EntityManagerUtil.getEntityManager();
            listado = em.createNamedQuery("TipoAsentamiento.findAll", TipoAsentamiento.class)
                .getResultList();
        } catch (Exception e) {
            log.error("Error al obtener informacion", e);
        } finally {
            EntityManagerUtil.close(em);
        }

        return listado;
    }

    public List<TipoAsentamiento> buscarPorCriterios(TipoAsentamiento e) {
        // TODO Auto-generated method stub
        return null;
    }
    
    public TipoAsentamiento buscarUltimoTipoAsentamiento(){
        TipoAsentamiento tipoAsentamiento = null;
        EntityManager em = null;
        
        try {
            em = EntityManagerUtil.getEntityManager();
            TypedQuery<TipoAsentamiento> query = em.createQuery(
                "SELECT ta FROM TipoAsentamiento ta ORDER BY ta.tipoasntmntoCve DESC", TipoAsentamiento.class
            );
            
            query.setMaxResults(1);
            tipoAsentamiento = query.getSingleResult();
        } catch(NoResultException ex) {
            log.error("No se encontraron tipos de asentamientos: {}", ex.getMessage());
        } catch (Exception e) {
            log.error("Error al obtener informacion", e);
        } finally {
            EntityManagerUtil.close(em);
        }
        
        return tipoAsentamiento;
    }

}
