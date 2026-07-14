package mx.com.ferbo.bitacora.dao;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ferbo.tools.exception.SystemException;

import mx.com.ferbo.bitacora.model.EventoBitacora;
import mx.com.ferbo.bitacora.model.FiltroBitacora;
import mx.com.ferbo.commons.dao.BaseDAO;

@Named
@ApplicationScoped
public class BitacoraDAO extends BaseDAO<EventoBitacora, Long> {

    private static final Logger log = LogManager.getLogger(BitacoraDAO.class);

    public BitacoraDAO() {
        super(EventoBitacora.class);
    }

    @SuppressWarnings("unchecked")
    public List<Object[]> buscarGruposPorFiltros(FiltroBitacora filtros) {

        EntityManager em = null;
        List<Object[]> resultados;

        try {
            em = getEntityManager();

            resultados = em.createNamedQuery(
                    "EventoBitacora.findResumenByFiltros")
                    .setParameter("inicio", filtros.getInicio())
                    .setParameter("fin", filtros.getFin())
                    .setParameter("idUsuario", filtros.getIdUsuario())
                    .setParameter("nombrePantalla",
                            (filtros.getNombrePantalla() == null) ? null
                                    : filtros.getNombrePantalla().toString())
                    .setParameter("tipoPantalla",
                            (filtros.getTipoPantalla() == null) ? null
                                    : filtros.getTipoPantalla().toString())
                    .getResultList();

            return resultados;

        } catch (Exception ex) {
            log.warn("Error al buscar grupos: {}", ex.getMessage(), ex);
            throw new SystemException("Problema al buscar grupos de bitácora");

        } finally {
            close(em);
        }
    }

    public List<EventoBitacora> buscarPorFiltros(FiltroBitacora filtros) {
        EntityManager em = null;
        List<EventoBitacora> eventos;
        try {
            em = getEntityManager();
            eventos = em.createNamedQuery("EventoBitacora.findByFiltros", EventoBitacora.class)
                    .setParameter("idUsuario", filtros.getIdUsuario())
                    .setParameter("inicio", filtros.getInicio())
                    .setParameter("fin", filtros.getFin())
                    .setParameter("nombrePantalla", filtros.getNombrePantalla())
                    .setParameter("tipoPantalla", filtros.getTipoPantalla())
                    .setParameter("documento", filtros.getDocumento())
                    .getResultList();
            for (EventoBitacora evento : eventos) {
                log.debug("Usuario del evento: {}", evento.getUsuario());
            }
            return eventos;
        } catch (Exception ex) {
            log.warn("Error al buscar eventos de la bitácora por filtros: {}", ex.getMessage(), ex);
            throw new SystemException(
                    "Hubo un problema al buscar los eventos de la bitacora con los filtros seleccionados");
        } finally {
            close(em);
        }
    }
}
