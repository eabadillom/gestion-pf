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
import mx.com.ferbo.model.Ciudades;
import mx.com.ferbo.model.CiudadesPK;
import mx.com.ferbo.model.Municipios;
import mx.com.ferbo.util.EntityManagerUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author alberto
 */
@Named
@ApplicationScoped
public class CiudadesDAO extends BaseDAO<Ciudades, Integer> 
{
    private static Logger log = LogManager.getLogger(CiudadesDAO.class);
    
    public CiudadesDAO(Class<Ciudades> modelClass) {
        super(modelClass);
    }

    public CiudadesDAO() {
        super(Ciudades.class);
    }

    public List<Ciudades> findall() {
        EntityManager entity = null;
        List<Ciudades> ciudades = null;

        try {
            entity = getEntityManager();
            Query sql = entity.createNamedQuery("Ciudades.findAll", Ciudades.class);
            ciudades = sql.getResultList();
        } catch (Exception e) {
            log.error("Problema al recuperar ciudades", e);
        } finally {
            EntityManagerUtil.close(entity);
        }

        return ciudades;
    }

    public Ciudades buscarPorId(CiudadesPK ciudadPK) {
        Ciudades model = null;
        EntityManager em = null;

        try {
            em = EntityManagerUtil.getEntityManager();
            model = em.find(Ciudades.class, ciudadPK);
        } catch (Exception ex) {
            log.warn("Problema para obtener la información de la ciudad...", ex);
        } finally {
            EntityManagerUtil.close(em);
        }

        return model;
    }

    public List<Ciudades> buscarTodos() {
        EntityManager em = null;
        List<Ciudades> listado = null;

        try {
            em = EntityManagerUtil.getEntityManager();
            listado = em.createNamedQuery("Ciudades.findAll", Ciudades.class).getResultList();
        } catch (Exception e) {
            log.error("Problema al recuperar ciudades", e);
        } finally {
            EntityManagerUtil.close(em);
        }

        return listado;
    }

    public List<Ciudades> buscarPorCriteriosCiudades(Ciudades e) {
        EntityManager em = null;
        List<Ciudades> listado = null;

        try {
            em = EntityManagerUtil.getEntityManager();
            listado = em.createNamedQuery("Ciudades.findByPaisCveEstadoCveMunicipioCve", Ciudades.class)
                    .setParameter("paisCve", e.getCiudadesPK().getMunicipios().getMunicipiosPK().getEstados().getEstadosPK().getPais().getPaisCve())
                    .setParameter("estadoCve", e.getCiudadesPK().getMunicipios().getMunicipiosPK().getEstados().getEstadosPK().getEstadoCve())
                    .setParameter("municipioCve", e.getCiudadesPK().getMunicipios().getMunicipiosPK().getMunicipioCve())
                    .getResultList();
        } catch (Exception ex) {
            log.error("Problema para obtener el listado de ciudades...", ex);
        } finally {
            EntityManagerUtil.close(em);
        }

        return listado;
    }

    public List<Ciudades> buscarPorPaisEstadoMunicipio(Municipios municipio) {
        EntityManager em = null;
        List<Ciudades> listado = null;

        try {

            em = EntityManagerUtil.getEntityManager();
            listado = em.createNamedQuery("Ciudades.findByPaisCveEstadoCveMunicipioCve", Ciudades.class)
                    .setParameter("paisCve", municipio.getMunicipiosPK().getEstados().getEstadosPK().getPais().getPaisCve())
                    .setParameter("estadoCve", municipio.getMunicipiosPK().getEstados().getEstadosPK().getEstadoCve())
                    .setParameter("municipioCve", municipio.getMunicipiosPK().getMunicipioCve())
                    .getResultList();

        } catch (Exception ex) {
            log.error("Problema para obtener el listado de ciudades...", ex);
        } finally {
            EntityManagerUtil.close(em);
        }

        return listado;
    }

    public List<Ciudades> buscarPorCriterios(Ciudades e) {//verificar que funcione correctamente
        // TODO Auto-generated method stub
        EntityManager em = null;
        List<Ciudades> listado = null;

        try {
            em = EntityManagerUtil.getEntityManager();
            if (e.getCiudadesPK().getMunicipios().getMunicipiosPK().getMunicipioCve() > 0) {
                TypedQuery<Ciudades> consEstados = em.createNamedQuery("Ciudades.findByEstadoMunicipioCve", Ciudades.class);
                consEstados.setParameter("municipioCve", e.getCiudadesPK().getMunicipios().getMunicipiosPK().getMunicipioCve())
                        .setParameter("estadoCve", e.getCiudadesPK().getMunicipios().getMunicipiosPK().getEstados().getEstadosPK().getEstadoCve());
                listado = consEstados.getResultList();
            }
            if (e.getCiudadesPK().getMunicipios().getMunicipiosPK().getEstados().getEstadosPK().getPais().getPaisCve() != -1 && e.getCiudadesPK().getMunicipios().getMunicipiosPK().getEstados().getEstadosPK().getEstadoCve() != -1 && e.getCiudadesPK().getMunicipios().getMunicipiosPK().getMunicipioCve() != -1) {
                listado = em.createNamedQuery("Ciudades.findByPaisCveEstadoCveMunicipioCve", Ciudades.class)
                        .setParameter("paisCve", e.getCiudadesPK().getMunicipios().getMunicipiosPK().getEstados().getEstadosPK().getPais().getPaisCve())
                        .setParameter("estadoCve", e.getCiudadesPK().getMunicipios().getMunicipiosPK().getEstados().getEstadosPK().getEstadoCve())
                        .setParameter("municipioCve", e.getCiudadesPK().getMunicipios().getMunicipiosPK().getMunicipioCve())
                        .getResultList();
            }

        } catch (Exception ex) {
            log.error("Problema para obtener el listado de ciudades...", ex);
        } finally {
            EntityManagerUtil.close(em);
        }

        return listado;
    }

    public List<Ciudades> buscaPorId(Integer id) {
        List<Ciudades> listado = null;
        EntityManager em = null;
        
        try {
            em = EntityManagerUtil.getEntityManager();
            listado = em.createNamedQuery("Ciudades.findByCiudadCve", Ciudades.class)
                .setParameter("ciudadCve", id)
                .getResultList();
        } catch (Exception ex) {
            log.error("Problema para obtener el listado de ciudades...", ex);
        } finally {
            EntityManagerUtil.close(em);
        }
        
        return listado;
    }

    public List<Ciudades> buscaPorAsentamiento(AsentamientoHumano as) {
        List<Ciudades> listado = null;
        EntityManager em = null;
        
        try { 
            em = EntityManagerUtil.getEntityManager();
            listado = em.createNamedQuery("Ciudades.findByTodo", Ciudades.class)
                .setParameter("municipioCve", as.getAsentamientoHumanoPK().getCiudades().getCiudadesPK().getMunicipios().getMunicipiosPK().getMunicipioCve())
                .setParameter("estadoCve", as.getAsentamientoHumanoPK().getCiudades().getCiudadesPK().getMunicipios().getMunicipiosPK().getEstados().getEstadosPK().getEstadoCve())
                .setParameter("ciudadCve", as.getAsentamientoHumanoPK().getCiudades().getCiudadesPK().getCiudadCve())
                .getResultList();
        } catch (Exception ex) {
            log.error("Problema para obtener el listado de ciudades...", ex);
        } finally {
            EntityManagerUtil.close(em);
        }
        
        return listado;
    }
    
    public Ciudades buscarUltimaCidad(Municipios municipio){
        Ciudades ciudad = null;
        EntityManager em = null;
        
        try {
            em = EntityManagerUtil.getEntityManager();
            
            TypedQuery<Ciudades> query = em.createQuery(
                "SELECT c FROM Ciudades c WHERE c.ciudadesPK.municipios.municipiosPK.estados.estadosPK.pais.paisCve = :idPais AND c.ciudadesPK.municipios.municipiosPK.estados.estadosPK.estadoCve = :idEstado AND c.ciudadesPK.municipios.municipiosPK.municipioCve = :idMunicipio AND c.ciudadesPK.ciudadCve < 998 ORDER BY c.ciudadesPK.ciudadCve DESC", Ciudades.class
            );
            query.setParameter("idPais", municipio.getMunicipiosPK().getEstados().getEstadosPK().getPais().getPaisCve());
            query.setParameter("idEstado", municipio.getMunicipiosPK().getEstados().getEstadosPK().getEstadoCve());
            query.setParameter("idMunicipio", municipio.getMunicipiosPK().getMunicipioCve());
            query.setMaxResults(1);
            
            ciudad = query.getSingleResult();
        } catch(NoResultException ex) {
            log.warn("No se encontraron ciudades asociados al municipio ({}): {}", municipio.getMunicipioDs(), ex.getMessage());
        } catch (Exception ex) {
            log.error("Problema para obtener la ciudad...", ex);
        } finally {
            EntityManagerUtil.close(em);
        }
        
        return ciudad;
    }

}
