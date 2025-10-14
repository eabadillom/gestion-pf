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
import mx.com.ferbo.model.EntidadPostal;
import mx.com.ferbo.util.EntityManagerUtil;
import mx.com.ferbo.util.InventarioException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author alberto
 */
@Named
@ApplicationScoped
public class AsentamientoHumanoDAO extends BaseDAO<AsentamientoHumano, Integer> 
{
    Logger log = LogManager.getLogger(AsentamientoHumanoDAO.class);

    public AsentamientoHumanoDAO(Class<AsentamientoHumano> modelClass) {
        super(modelClass);
    }

    public AsentamientoHumanoDAO() {
        super(AsentamientoHumano.class);
    }

    @SuppressWarnings("unchecked")
    public List<AsentamientoHumano> findall() {
        EntityManager entity = null;
        List<AsentamientoHumano> asnHumano = null;
        try {
            entity = EntityManagerUtil.getEntityManager();
            Query sql = entity.createNamedQuery("AsentamientoHumano.findAll", AsentamientoHumano.class);
            asnHumano = sql.getResultList();
        } catch (Exception e) {
            log.error("Problemas para obtener informacion", e);
        } finally {
            EntityManagerUtil.close(entity);
        }
        return asnHumano;
    }

    public AsentamientoHumano buscarPorAsentamiento(Integer paisCve, Integer estadoCve, Integer municipioCve, Integer ciudadCve, Integer asentamientoCve) {
        EntityManager entity = null;
        AsentamientoHumano asn = null;
        try {
            entity = EntityManagerUtil.getEntityManager();
            asn = entity.createNamedQuery("AsentamientoHumano.findAsentamiento", AsentamientoHumano.class)
                    .setParameter("paisCve", paisCve)
                    .setParameter("estadoCve", estadoCve)
                    .setParameter("municipioCve", municipioCve)
                    .setParameter("ciudadCve", ciudadCve)
                    .setParameter("asentamientoCve", asentamientoCve)
                    .getSingleResult();
        } catch (Exception e) {
            log.error("Problemas para obtener informacion", e);
        } finally {
            EntityManagerUtil.close(entity);
        }
        return asn;
    }

    public List<AsentamientoHumano> buscarPorCiudad(Ciudades ciudad) {
        EntityManager entity = null;
        List<AsentamientoHumano> modelList = null;

        try {
            entity = EntityManagerUtil.getEntityManager();
            modelList = entity.createNamedQuery("AsentamientoHumano.findByPaisEstadoMunicipioCiudad", AsentamientoHumano.class)
                    .setParameter("paisCve", ciudad.getCiudadesPK().getMunicipios().getMunicipiosPK().getEstados().getEstadosPK().getPais().getPaisCve())
                    .setParameter("estadoCve", ciudad.getCiudadesPK().getMunicipios().getMunicipiosPK().getEstados().getEstadosPK().getEstadoCve())
                    .setParameter("municipioCve", ciudad.getCiudadesPK().getMunicipios().getMunicipiosPK().getMunicipioCve())
                    .setParameter("ciudadCve", ciudad.getCiudadesPK().getCiudadCve())
                    .getResultList();
        } catch (Exception e) {
            log.error("Problemas para obtener informacion", e);
        } finally {
            EntityManagerUtil.close(entity);
        }

        return modelList;
    }

    public List<AsentamientoHumano> buscarTodos() {
        List<AsentamientoHumano> listado = null;
        EntityManager em = null;
        try {
            em = EntityManagerUtil.getEntityManager();
            listado = em.createNamedQuery("AsentamientoHumano.findAll", AsentamientoHumano.class).getResultList();
        } catch (Exception e) {
            log.error("Problemas para obtener informacion", e);
        } finally {
            EntityManagerUtil.close(em);
        }
        return listado;
    }

    public List<AsentamientoHumano> buscarPorCriterios(AsentamientoHumano a) {
        List<AsentamientoHumano> listado = null;
        EntityManager em = null;
        try {
            em = EntityManagerUtil.getEntityManager();
            listado = em.createNamedQuery("AsentamientoHumano.findByDomicilio", AsentamientoHumano.class)
                    .setParameter("paisCve", a.getAsentamientoHumanoPK().getCiudades().getCiudadesPK().getMunicipios().getMunicipiosPK().getEstados().getEstadosPK().getPais().getPaisCve())
                    .setParameter("estadoCve", a.getAsentamientoHumanoPK().getCiudades().getCiudadesPK().getMunicipios().getMunicipiosPK().getEstados().getEstadosPK().getEstadoCve())
                    .setParameter("municipioCve", a.getAsentamientoHumanoPK().getCiudades().getCiudadesPK().getMunicipios().getMunicipiosPK().getMunicipioCve())
                    .setParameter("ciudadCve", a.getAsentamientoHumanoPK().getCiudades().getCiudadesPK().getCiudadCve())
                    .setParameter("asentamientoCve", a.getAsentamientoHumanoPK().getAsentamientoCve())
                    .getResultList();
        } catch (Exception e) {
            log.error("Problemas para obtener informacion", e);
        } finally {
            EntityManagerUtil.close(em);
        }
        return listado;
    }

    public List<AsentamientoHumano> buscarPorCriterioEspecial(AsentamientoHumano a) {
        List<AsentamientoHumano> listado = null;
        EntityManager em = null;
        try {
            em = EntityManagerUtil.getEntityManager();
            listado = em.createNamedQuery("AsentamientoHumano.findByDomicilioCompleto", AsentamientoHumano.class)
                    .setParameter("paisCve", a.getAsentamientoHumanoPK().getCiudades().getCiudadesPK().getMunicipios().getMunicipiosPK().getEstados().getEstadosPK().getPais().getPaisCve())
                    .setParameter("estadoCve", a.getAsentamientoHumanoPK().getCiudades().getCiudadesPK().getMunicipios().getMunicipiosPK().getEstados().getEstadosPK().getEstadoCve())
                    .setParameter("municipioCve", a.getAsentamientoHumanoPK().getCiudades().getCiudadesPK().getMunicipios().getMunicipiosPK().getMunicipioCve())
                    .setParameter("ciudadCve", a.getAsentamientoHumanoPK().getCiudades().getCiudadesPK().getCiudadCve())
                    .setParameter("tipoasntmntoCve", a.getTipoAsentamiento().getTipoasntmntoCve())
                    .setParameter("entidadpostalCve", a.getEntidadPostal().getEntidadpostalCve())
                    .getResultList();

            for (AsentamientoHumano aux : listado) {
                log.debug("Asentamiento: {}", aux.getAsentamientoDs());
                log.debug("Ciudad: {}", aux.getAsentamientoHumanoPK().getCiudades().getCiudadDs());
                log.debug("Municipio: {}", aux.getAsentamientoHumanoPK().getCiudades().getCiudadesPK().getMunicipios().getMunicipioDs());
                log.debug("Estado: {}", aux.getAsentamientoHumanoPK().getCiudades().getCiudadesPK().getMunicipios().getMunicipiosPK().getEstados().getEstadoDesc());
                log.debug("Pais: {}", aux.getAsentamientoHumanoPK().getCiudades().getCiudadesPK().getMunicipios().getMunicipiosPK().getEstados().getEstadosPK().getPais().getPaisDesc());
            }

        } catch (Exception e) {
            log.error("Problemas para obtener informacion", e);
        } finally {
            EntityManagerUtil.close(em);
        }
        return listado;
    }

    public List<AsentamientoHumano> buscaPorCP(String codigo) {
        List<AsentamientoHumano> listado = null;
        EntityManager em = null;

        try {
            em = EntityManagerUtil.getEntityManager();

            listado = em.createNamedQuery("AsentamientoHumano.findByCp", AsentamientoHumano.class)
                    .setParameter("cp", codigo).getResultList();

            for (AsentamientoHumano aux : listado) {
                log.debug("Asentamiento: {}", aux.getAsentamientoDs());
                log.debug("Ciudad: {}", aux.getAsentamientoHumanoPK().getCiudades().getCiudadDs());
                log.debug("Municipio: {}", aux.getAsentamientoHumanoPK().getCiudades().getCiudadesPK().getMunicipios().getMunicipioDs());
                log.debug("Estado: {}", aux.getAsentamientoHumanoPK().getCiudades().getCiudadesPK().getMunicipios().getMunicipiosPK().getEstados().getEstadoDesc());
                log.debug("Pais: {}", aux.getAsentamientoHumanoPK().getCiudades().getCiudadesPK().getMunicipios().getMunicipiosPK().getEstados().getEstadosPK().getPais().getPaisDesc());
            }
        } catch (Exception ex) {
            log.error("Problemas para obtener informacion", ex);
        } finally {
            EntityManagerUtil.close(em);
        }

        return listado;
    }

    public List<AsentamientoHumano> buscarPorCiudad(AsentamientoHumano e) {
        List<AsentamientoHumano> listado = null;
        EntityManager em = null;

        try {
            em = EntityManagerUtil.getEntityManager();

            if (e.getAsentamientoHumanoPK().getCiudades().getCiudadesPK().getCiudadCve() > 0) {
                TypedQuery<AsentamientoHumano> consEstados = em.createNamedQuery("AsentamientoHumano.findByCiudadCve", AsentamientoHumano.class);
                consEstados.setParameter("ciudadCve", e.getAsentamientoHumanoPK().getCiudades().getCiudadesPK().getCiudadCve());
                listado = consEstados.getResultList();
            } else {
                if (e.getAsentamientoHumanoPK().getAsentamientoCve() > 0) {
                    TypedQuery<AsentamientoHumano> consEstados = em.createNamedQuery("AsentamientoHumano.findByAsentamientoCve", AsentamientoHumano.class);
                    consEstados.setParameter("ciudadCve", e.getAsentamientoHumanoPK().getCiudades().getCiudadesPK().getCiudadCve());
                    listado = consEstados.getResultList();
                    return listado;
                }
                return null;
            }
        } catch (Exception ex) {
            log.error("Problemas para obtener informacion", ex);
        } finally {
            EntityManagerUtil.close(em);
        }

        return listado;
    }

    public List<AsentamientoHumano> buscarPorEntidadPostal(EntidadPostal e) {

        EntityManager em = null;
        List<AsentamientoHumano> lista = null;

        try {

            em = EntityManagerUtil.getEntityManager();
            lista = em.createNamedQuery("AsentamientoHumano.findByEntidadpostalCve", AsentamientoHumano.class)
                    .setParameter("entidadpostalCve", e.getEntidadpostalCve()).getResultList();

        } catch (Exception ex) {
            log.error("Problema al encontrar registros asentamiento", ex);
        } finally {
            EntityManagerUtil.close(em);
        }

        return lista;
    }

    public AsentamientoHumano buscar(AsentamientoHumano a) {
        AsentamientoHumano listado = null;
        EntityManager em = null;

        try {
            em = EntityManagerUtil.getEntityManager();
            listado = em.createNamedQuery("AsentamientoHumano.findAsentamiento", AsentamientoHumano.class)
                    .setParameter("paisCve", a.getAsentamientoHumanoPK().getCiudades().getCiudadesPK().getMunicipios().getMunicipiosPK().getEstados().getEstadosPK().getPais().getPaisCve())
                    .setParameter("estadoCve", a.getAsentamientoHumanoPK().getCiudades().getCiudadesPK().getMunicipios().getMunicipiosPK().getEstados().getEstadosPK().getEstadoCve())
                    .setParameter("municipioCve", a.getAsentamientoHumanoPK().getCiudades().getCiudadesPK().getMunicipios().getMunicipiosPK().getMunicipioCve())
                    .setParameter("ciudadCve", a.getAsentamientoHumanoPK().getCiudades().getCiudadesPK().getCiudadCve())
                    .setParameter("asentamientoCve", a.getAsentamientoHumanoPK().getAsentamientoCve())
                    .getSingleResult();
        } catch (Exception e) {
            log.error("Problemas para obtener informacion", e);
        } finally {
            EntityManagerUtil.close(em);
        }
        return listado;
    }
    
    public AsentamientoHumano buscarUltimoAsentamiento(){
        AsentamientoHumano asentamientoHumano = null;
        EntityManager em = null;

        try {
            em = EntityManagerUtil.getEntityManager();
            TypedQuery<AsentamientoHumano> query = em.createQuery(
                "SELECT ah FROM AsentamientoHumano ah ORDER BY ah.asentamientoHumanoPK.asentamientoCve DESC", AsentamientoHumano.class
            );
            
            query.setMaxResults(1);
            asentamientoHumano = query.getSingleResult();
        } catch(NoResultException ex) {
            log.warn("No se encontraron asentamientos: {} ", ex.getMessage());
        } catch (Exception e) {
            log.error("Problemas para obtener informacion", e);
        } finally {
            EntityManagerUtil.close(em);
        }
        return asentamientoHumano;
    }
    
    @Override
    public synchronized void eliminar(AsentamientoHumano asentamientoHumano) throws InventarioException{
        EntityManager em = null;
        try {
            log.info("Eliminando objeto: {}", asentamientoHumano.toString());
            
            em = getEntityManager();
            em.getTransaction().begin();
            em.createQuery("DELETE FROM AsentamientoHumano ah WHERE ah.asentamientoHumanoPK.asentamientoCve = :asentamientoCve AND ah.cp = :cp")
                .setParameter("asentamientoCve", asentamientoHumano.getAsentamientoHumanoPK().getAsentamientoCve())
                .setParameter("cp", asentamientoHumano.getCp())
                .executeUpdate();
            em.getTransaction().commit();
            
            log.info("Objeto eliminado correctamente: {}", asentamientoHumano.toString());
        } catch (Exception e) {
            this.rollback(em);
            log.error("Problemas para eliminar el objeto: "+ asentamientoHumano.toString(), e);
            throw new InventarioException("Error al eliminar en la basede datos");
        } finally {
            EntityManagerUtil.close(em);
        }
    }

}
