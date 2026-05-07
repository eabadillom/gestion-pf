package mx.com.ferbo.egresos.dao;

import java.time.LocalDateTime;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ferbo.tools.exception.SystemException;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.egresos.model.Egreso;
import mx.com.ferbo.egresos.model.calogos.CategoriaEgreso;
import mx.com.ferbo.egresos.model.calogos.StatusEgreso;

@Named
@ApplicationScoped
public class EgresoDAO extends BaseDAO<Egreso, Long> {

    private static final Logger log = LogManager.getLogger(EgresoDAO.class);

    private EntityManager em;

    public EgresoDAO() {
        super(Egreso.class);
    }

    public List<Egreso> buscarTodos() throws SystemException {
        try {
            em = getEntityManager();
            return em.createNamedQuery("Egreso.findAll", Egreso.class).getResultList();
        } catch (Exception ex) {
            log.error("Error al momento de buscar los todos los egresos. {}");
            throw new SystemException("Hubo un problema al momento de buscar todos los egresos.");
        } finally {
            close(em);
        }
    }

    public List<Egreso> buscarPorRangoFecha(LocalDateTime inicio, LocalDateTime fin) throws SystemException {
        try {
            em = getEntityManager();
            return em.createNamedQuery("Egreso.findByFechaBetween", Egreso.class).setParameter("inicio", inicio)
                    .setParameter("fin", fin).getResultList();
        } catch (Exception ex) {
            log.error("Error al momento de buscar los egresos desde {} hasta {}. {}", inicio, fin, ex);
            throw new SystemException(
                    "Hubo un problema al momento de buscar los egresos dentro de un rango de fechas.");
        } finally {
            close(em);
        }
    }

    public List<Egreso> buscarPorCategoria(CategoriaEgreso categoria) throws SystemException {
        try {
            em = getEntityManager();
            return em.createNamedQuery("Egreso.findByCategoria", Egreso.class).setParameter("categoria", categoria)
                    .getResultList();
        } catch (Exception ex) {
            log.error("Error al momento de buscar los egresos con la categoría: {}. {}", categoria.getNombre(), ex);
            throw new SystemException("Hubo un problema al momento de buscar los egresos por categoría.");
        } finally {
            close(em);
        }
    }

    public List<Egreso> buscarPorStatus(StatusEgreso status) throws SystemException {
        try {
            em = getEntityManager();
            return em.createNamedQuery("Egreso.findByStatus", Egreso.class).setParameter("status", status)
                    .getResultList();
        } catch (Exception ex) {
            log.error("Error al momento de buscar los egresos con el status: {}. {}", status.getNombre(), ex);
            throw new SystemException("Hubo un problema al momento de buscar los egresos por status.");
        } finally {
            close(em);
        }
    }

    public List<Egreso> buscarPorConcepto(String concepto) throws SystemException {
        try {
            em = getEntityManager();
            return em.createNamedQuery("Egreso.searchByConcepto", Egreso.class).setParameter("concepto", concepto)
                    .getResultList();
        } catch (Exception ex) {
            log.error("Error al momento de buscar los egresos con el concepto: {}. {}", concepto, ex);
            throw new SystemException("Hubo un problema al momento de buscar los egresos por concepto.");
        } finally {
            close(em);
        }
    }

    public List<Egreso> buscarPorFiltros(LocalDateTime inicio, LocalDateTime fin, CategoriaEgreso categoria,
            StatusEgreso status,
            String concepto) throws SystemException {
        try {
            em = getEntityManager();
            return em.createNamedQuery("Egreso.findWithFilters", Egreso.class).setParameter("inicio", inicio)
                    .setParameter("fin", fin)
                    .setParameter("categoria", categoria).setParameter("status", status)
                    .setParameter("concepto", concepto).getResultList();
        } catch (Exception ex) {
            log.error(
                    "Error al momento de buscar los egresos con los los filtros: inicio={}, fin={}, categoria={}, status={}, concepto={}. {}",
                    inicio, fin, categoria, status, concepto, ex);
            throw new SystemException("Hubo un problema al momento de buscar los egresos por filtros.");
        } finally {
            close(em);
        }
    }
}
