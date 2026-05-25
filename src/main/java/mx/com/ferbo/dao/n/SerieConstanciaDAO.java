package mx.com.ferbo.dao.n;

import java.util.List;
import java.util.Optional;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.SerieConstancia;
import mx.com.ferbo.model.SerieConstanciaPK;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author alberto
 */
@Named
@ApplicationScoped
public class SerieConstanciaDAO extends BaseDAO<SerieConstancia, SerieConstanciaPK> 
{
    private static Logger log = LogManager.getLogger(SerieConstanciaDAO.class);

    public SerieConstanciaDAO() {
        super(SerieConstancia.class);
    }

    public List<SerieConstancia> buscarPorClienteAndPlanta(Integer idCliente, Integer idPlanta) {
        EntityManager em = null;
        Query sql = null;
        List<SerieConstancia> sc = null;
        try {
            em = super.getEntityManager();
            sql = em.createNamedQuery("SerieConstancia.findByClienteAndPlanta", SerieConstancia.class)
                    .setParameter("idCliente", idCliente)
                    .setParameter("idPlanta", idPlanta);
            sc = sql.getResultList();
        } catch (Exception e) {
            log.error("Error al buscar serie...", e);
        } finally {
            super.close(em);
        }
        return sc;
    }

    public SerieConstancia buscarPorClienteAndPlanta(SerieConstancia serieConstancia) {
        EntityManager em = null;
        Query sql = null;
        SerieConstancia sc = null;
        try {
            em = super.getEntityManager();

            sql = em.createNamedQuery("SerieConstancia.findByClienteTpSeriePlanta", SerieConstancia.class)
                    .setParameter("idCliente", serieConstancia.getSerieConstanciaPK().getCliente().getCteCve())
                    .setParameter("idPlanta", serieConstancia.getSerieConstanciaPK().getPlanta().getPlantaCve())
                    .setParameter("tpSerie", serieConstancia.getSerieConstanciaPK().getTpSerie());
            sc = (SerieConstancia) sql.getSingleResult();

        } catch (Exception e) {
            log.error("Error al buscar serie...", e);
        } finally {
            super.close(em);
        }
        return sc;
    }
    
    public Optional<SerieConstancia> buscarPorClienteTipoSerieAndPlanta(Integer idCliente, String tipoSerie, Integer idPlanta) {
        Optional<SerieConstancia> optional = null;
        SerieConstancia model = null;
        EntityManager em = null;

        try {
            em = this.getEntityManager();
            model = em.createNamedQuery("SerieConstancia.findByClienteTpSeriePlanta", modelClass)
                    .setParameter("idCliente", idCliente)
                    .setParameter("tpSerie", tipoSerie)
                    .setParameter("idPlanta", idPlanta)
                    .getSingleResult();

            optional = Optional.of(model);
        } catch (Exception ex) {
            log.error("Problema para obtener la Serie-Constancia solicitada: cteCve = {}, tipoSerie = {}, plantaCve = {}, \n{}", idCliente, tipoSerie, idPlanta, ex);
            optional = Optional.empty();
        } finally {
            this.close(em);
        }

        return optional;
    }
    
    public List<SerieConstancia> buscarPorIdCliente(Integer idCliente) {
        EntityManager em = null;
        List<SerieConstancia> lista = null;

        try {

            em = super.getEntityManager();
            lista = em.createNamedQuery("SerieConstancia.findByIdCliente", SerieConstancia.class)
                    .setParameter("idCliente", idCliente)
                    .getResultList();

        } catch (Exception e2) {
            log.error("Problema no encuentra registros por criterios", e2);
        } finally {
            super.close(em);
        }

        return lista;
    }

    public List<SerieConstancia> buscarPorIdCliente(SerieConstanciaPK serie) {
        EntityManager em = null;
        List<SerieConstancia> lista = null;

        try {

            em = super.getEntityManager();
            lista = em.createNamedQuery("SerieConstancia.findByIdCliente", SerieConstancia.class)
                    .setParameter("idCliente", serie.getCliente().getCteCve())
                    .getResultList();

        } catch (Exception e2) {
            log.error("Problema no encuentra registros por criterios", e2);
        } finally {
            super.close(em);
        }

        return lista;
    }

}
