package mx.com.ferbo.dao.n;

import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.AsentamientoHumano;
import mx.com.ferbo.model.Estados;
import mx.com.ferbo.model.EstadosPK;
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
public class EstadosDAO extends BaseDAO<Estados, Integer> 
{
    private static Logger log = LogManager.getLogger(EstadosDAO.class);

    public EstadosDAO() {
        super(Estados.class);
    }

    public List<Estados> findall() {
        EntityManager em = null;
        List<Estados> estados = null;
        
        try {
            em = getEntityManager();
            Query sql = em.createNamedQuery("Estados.findAll", Estados.class);
            estados = sql.getResultList();
            
        } catch (Exception ex) {
            log.warn("Problema para obtener la información del Estado...", ex);
        } finally {
            EntityManagerUtil.close(em);
        }
        
        return estados;
    }

    public Estados buscarPorId(Paises idPais, Integer idEstado) {
        Estados model = null;
        EntityManager em = null;

        try {
            em = getEntityManager();
            model = em.find(Estados.class, new EstadosPK(idPais, idEstado));
        } catch (Exception ex) {
            log.warn("Problema para obtener la información del Estado...", ex);
        } finally {
            EntityManagerUtil.close(em);
        }

        return model;
    }

    public List<Estados> buscarTodos() {
        List<Estados> listado = null;
        EntityManager em = null;
        
        try{
            em = EntityManagerUtil.getEntityManager();
            listado = em.createNamedQuery("Estados.findAll", Estados.class)
                .getResultList();
        } catch (Exception ex) {
            log.warn("Problema para obtener la información del Estado...", ex);
        } finally {
            EntityManagerUtil.close(em);
        }
        
        return listado;
    }

    public List<Estados> buscarPorCriteriosEstados(Estados e) {
        List<Estados> listado = null;
        EntityManager em = null;
        
        try{
            em = EntityManagerUtil.getEntityManager();
            listado = em.createNamedQuery("Estados.findByPaisCve", Estados.class)
                .setParameter("paisCve", e.getEstadosPK().getPais().getPaisCve())
                .getResultList();
        } catch (Exception ex) {
            log.warn("Problema para obtener la información del Estado...", ex);
        } finally {
            EntityManagerUtil.close(em);
        }
        
        return listado;
    }

    public List<Estados> buscarPorCriterios(Estados e) {
        // TODO Auto-generated method stub
        EntityManager em = EntityManagerUtil.getEntityManager();
        if (e.getEstadosPK().getPais().getPaisCve() != null) {
            TypedQuery<Estados> consEstados = em.createNamedQuery("Estados.findByPaisCve", Estados.class);
            consEstados.setParameter("paisCve", e.getEstadosPK().getPais().getPaisCve());
            List<Estados> listado = consEstados.getResultList();
            return listado;
        } else {
            return null;
        }
    }

    public List<Estados> buscaPorId(Integer id) {
        List<Estados> listado = null;
        EntityManager em = null;
        
        try{
            em= EntityManagerUtil.getEntityManager();
            listado = em.createNamedQuery("Estados.findByEstadoCve", Estados.class)
                .setParameter("estadoCve", id).getResultList();
        } catch (Exception ex) {
            log.warn("Problema para obtener la información del Estado...", ex);
        } finally {
            EntityManagerUtil.close(em);
        }
        
        return listado;
    }

    public List<Estados> buscaPorAsentamiento(AsentamientoHumano as) {
        List<Estados> listado = null;
        EntityManager em = null;
        
        try{
            em = EntityManagerUtil.getEntityManager();
            em.createNamedQuery("Estados.findByCriterios", Estados.class)
                .setParameter("paisCve", as.getAsentamientoHumanoPK().getCiudades().getCiudadesPK().getMunicipios().getMunicipiosPK().getEstados().getEstadosPK().getPais().getPaisCve())
                .setParameter("estadoCve", as.getAsentamientoHumanoPK().getCiudades().getCiudadesPK().getMunicipios().getMunicipiosPK().getEstados().getEstadosPK().getEstadoCve())
                .getResultList();
        } catch (Exception ex) {
            log.warn("Problema para obtener la información del Estado...", ex);
        } finally {
            EntityManagerUtil.close(em);
        }
        
        return listado;
    }

    public List<Estados> buscarPorPais(Paises pais) {
        EntityManager em = null;
        List<Estados> list = null;

        try {
            em = EntityManagerUtil.getEntityManager();
            list = em.createNamedQuery("Estados.findByPaisCve", Estados.class)
                    .setParameter("paisCve", pais.getPaisCve())
                    .getResultList();
        } catch (Exception ex) {
            log.error("Problema al obtener la información de estados del pais " + pais.getPaisCve(), ex);
        } finally {
            EntityManagerUtil.close(em);
        }

        return list;
    }

}
