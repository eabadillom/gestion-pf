package mx.com.ferbo.dao.n;

import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.AsentamientoHumano;
import mx.com.ferbo.model.Estados;
import mx.com.ferbo.model.Municipios;
import mx.com.ferbo.model.MunicipiosPK;
import mx.com.ferbo.util.EntityManagerUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author alberto
 */
@Named
@ApplicationScoped
public class MunicipiosDAO extends BaseDAO<Municipios, Integer> 
{
    Logger log = LogManager.getLogger(MunicipiosDAO.class);
    
    public MunicipiosDAO(Class<Municipios> modelClass) {
        super(modelClass);
    }

    public MunicipiosDAO() {
        super(Municipios.class);
    }

    @SuppressWarnings("unchecked")
    public List<Municipios> findall() {
        EntityManager entity = null;
        List<Municipios> municipios = null;
        try {
            entity = EntityManagerUtil.getEntityManager();
            Query sql = entity.createNamedQuery("Municipios.findAll", Municipios.class);
            municipios = sql.getResultList();
        } catch (Exception e) {
            log.error("Problemas para obtener informacion", e);
        } finally {
            EntityManagerUtil.close(entity);
        }
        return municipios;
    }

    public Municipios buscarPorId(MunicipiosPK municipioPK) {
        Municipios model = null;
        EntityManager em = null;

        try {
            em = EntityManagerUtil.getEntityManager();
            model = em.find(Municipios.class, municipioPK);

        } catch (Exception ex) {
            log.warn("Problema para obtener la información del municipio...", ex);
        } finally {
            EntityManagerUtil.close(em);
        }

        return model;
    }

    public List<Municipios> buscarTodos() {
        List<Municipios> listado = null;
        EntityManager em = null;
        try {
            em = EntityManagerUtil.getEntityManager();
            listado = em.createNamedQuery("Municipios.findAll", Municipios.class).getResultList();
        } catch (Exception e) {
            log.error("Problemas para obtener informacion", e);
        } finally {
            EntityManagerUtil.close(em);
        }
        return listado;
    }

    public List<Municipios> buscarPorPaisEstado(Estados estado) {
        List<Municipios> modelList = null;
        EntityManager em = null;

        try {
            em = EntityManagerUtil.getEntityManager();
            modelList = em.createNamedQuery("Municipios.findByPaisCveEstadoCve", Municipios.class)
                    .setParameter("paisCve", estado.getEstadosPK().getPais().getPaisCve())
                    .setParameter("estadoCve", estado.getEstadosPK().getEstadoCve())
                    .getResultList();
        } catch (Exception ex) {
            log.warn("Problema para obtener la lista de municipios...", ex);
        } finally {
            EntityManagerUtil.close(em);
        }

        return modelList;
    }

    public List<Municipios> buscarPorPaisEstado(Municipios m) {
        List<Municipios> listado = null;
        EntityManager em = null;
        try {
            em = EntityManagerUtil.getEntityManager();
            listado = em.createNamedQuery("Municipios.findByPaisCveEstadoCve", Municipios.class)
                    .setParameter("estadoCve", m.getMunicipiosPK().getEstados().getEstadosPK().getEstadoCve())
                    .setParameter("paisCve", m.getMunicipiosPK().getEstados().getEstadosPK().getPais().getPaisCve())
                    .getResultList();

        } catch (Exception e) {
            log.error("Problemas para obtener informacion", e);
        } finally {
            EntityManagerUtil.close(em);
        }
        return listado;
    }

    public List<Municipios> buscarPorCriterios(Municipios m) {
        EntityManager em = null;
        List<Municipios> listado = null;
        try {
            em = EntityManagerUtil.getEntityManager();
            if (m.getMunicipiosPK().getEstados().getEstadosPK().getEstadoCve() > 0) {
                TypedQuery<Municipios> consEstados = em.createNamedQuery("Municipios.findByEstadoCve", Municipios.class);
                consEstados.setParameter("estadoCve", m.getMunicipiosPK().getEstados().getEstadosPK().getEstadoCve());
                listado = consEstados.getResultList();
                return listado;
            } else if (m.getMunicipiosPK().getEstados().getEstadosPK().getEstadoCve() != -1 && m.getMunicipiosPK().getEstados().getEstadosPK().getPais().getPaisCve() != -1) {
                listado = em.createNamedQuery("Municipios.findByPaisCveEstadoCve", Municipios.class)
                        .setParameter("estadoCve", m.getMunicipiosPK().getEstados().getEstadosPK().getEstadoCve())
                        .setParameter("paisCve", m.getMunicipiosPK().getEstados().getEstadosPK().getPais().getPaisCve())
                        .getResultList();
                return listado;
            }
        } catch (Exception e) {
            log.error("Problemas para obtener informacion", e);
        } finally {
            EntityManagerUtil.close(em);
        }
        return listado;
    }

    public List<Municipios> buscaPorId(Integer id) {
        List<Municipios> listado = null;
        EntityManager em = null;
        try {
            em = EntityManagerUtil.getEntityManager();
            listado = em.createNamedQuery("Municipios.findByMunicipioCve", Municipios.class)
                    .setParameter("municipioCve", id).getResultList();
        } catch (Exception e) {
            log.error("Problemas para obtener informacion", e);
        } finally {
            EntityManagerUtil.close(em);
        }
        return listado;
    }

    public List<Municipios> buscaPorAsentamiento(AsentamientoHumano as) {
        List<Municipios> listado = null;
        EntityManager em = null;
        try {
            em = EntityManagerUtil.getEntityManager();
            listado = em.createNamedQuery("Municipios.findByTodo", Municipios.class)
                    .setParameter("municipioCve", as.getAsentamientoHumanoPK().getCiudades().getCiudadesPK().getMunicipios().getMunicipiosPK().getMunicipioCve())
                    .setParameter("estadoCve", as.getAsentamientoHumanoPK().getCiudades().getCiudadesPK().getMunicipios().getMunicipiosPK().getEstados().getEstadosPK().getEstadoCve())
                    .setParameter("paisCve", as.getAsentamientoHumanoPK().getCiudades().getCiudadesPK().getMunicipios().getMunicipiosPK().getEstados().getEstadosPK().getPais().getPaisCve())
                    .getResultList();
        } catch (Exception e) {
            log.error("Problemas para obtener informacion", e);
        } finally {
            EntityManagerUtil.close(em);
        }
        return listado;
    }
    
    public Municipios buscarUltimoMunicipio(Estados estado){
        Municipios municipio = null;
        EntityManager em = null;
        
        try {
            em = EntityManagerUtil.getEntityManager();
            
            TypedQuery<Municipios> query = em.createQuery(
                "SELECT m FROM Municipios m WHERE m.municipiosPK.estados.estadosPK.pais.paisCve = :idPais AND m.municipiosPK.estados.estadosPK.estadoCve = :idEstado AND m.municipiosPK.municipioCve < 998 ORDER BY m.municipiosPK.municipioCve DESC", Municipios.class
            );
            query.setParameter("idPais", estado.getEstadosPK().getPais().getPaisCve());
            query.setParameter("idEstado", estado.getEstadosPK().getEstadoCve());
            query.setMaxResults(1);
            
            municipio = query.getSingleResult();
        } catch(NoResultException ex) {
            log.warn("No se encontraron municipios asociados al estado ({}): {}", estado.getEstadoDesc(), ex.getMessage());
        } catch (Exception e) {
            log.error("Problemas para obtener informacion", e);
        } finally {
            EntityManagerUtil.close(em);
        }
        
        return municipio;
    }

}
