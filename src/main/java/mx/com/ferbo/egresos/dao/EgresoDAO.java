package mx.com.ferbo.egresos.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.SystemException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.egresos.model.Egreso;
import mx.com.ferbo.egresos.model.calogos.CategoriaEgreso;
import mx.com.ferbo.egresos.model.calogos.StatusEgreso;

public class EgresoDAO extends BaseDAO<Egreso, Long> {

    private static final Logger log = LogManager.getLogger(EgresoDAO.class);

    public EgresoDAO() {
        super(Egreso.class);
    }

    public EgresoDAO(Class<Egreso> modelClass) {
        super(modelClass);
    }

    public List<Egreso> buscarTodos() throws SystemException {
        EntityManager em = null;
        List<Egreso> egresos = null;
        try {
            em = super.getEntityManager();
            egresos = em.createNamedQuery("Egreso.findAll", Egreso.class).getResultList();
            return egresos;
        } catch (Exception ex) {
            log.error("Error al momento de buscar todos los egresos.");
            throw new SystemException("Hubo un problema al buscar todos los egresos");
        } finally {
            super.close(em);
        }
    }

    public List<Egreso> buscarPorRangoFecha(Date inicio, Date fin) throws SystemException {
        EntityManager em = null;
        List<Egreso> egresos = null;
        try {
            em = super.getEntityManager();
            egresos = em.createNamedQuery("Egreso.findByFechaBetween", Egreso.class).setParameter("inicio", inicio)
                    .setParameter("fin", fin).getResultList();
            return egresos;
        } catch (Exception ex) {
            log.error("Error al momento de buscar los egresos entre el rango de fechas desde {} hasta {}. {}", inicio,
                    fin, ex);
            throw new SystemException("Hubo un problema al buscar los egresos entre el rango de fechas.");
        } finally {
            super.close(em);
        }
    }

    public List<Egreso> buscarPorCategoria(CategoriaEgreso categoria) throws SystemException {
        EntityManager em = null;
        List<Egreso> egresos = null;
        try {
            em = super.getEntityManager();
            egresos = em.createNamedQuery("Egreso.findByCategoria", Egreso.class).setParameter("categoria", categoria)
                    .getResultList();
            return egresos;
        } catch (Exception ex) {
            log.info("Error al momento de buscar los egresos con la categoria: {}. {}", categoria.getNombre(), ex);
            throw new SystemException("Hubo un problema al buscar los egresos asociado a la categoría.");
        } finally {
            super.close(em);
        }
    }

    public List<Egreso> buscarPorStatus(StatusEgreso status) throws SystemException {
        EntityManager em = null;
        List<Egreso> egresos = null;
        try {
            em = super.getEntityManager();
            egresos = em.createNamedQuery("Egreso.findByStatus", Egreso.class).setParameter("status", status)
                    .getResultList();
            return egresos;
        } catch (Exception ex) {
            log.error("Error al momento de buscar los egresos con el status {}. {}", status.getNombre(), ex);
            throw new SystemException("Hubo un problema al buscar los egresos por status");
        } finally {
            super.close(em);
        }
    }

    public List<Egreso> buscarPorConcepto(String concepto) throws SystemException {
        EntityManager em = null;
        List<Egreso> egresos = null;
        try {
            em = super.getEntityManager();
            egresos = em.createNamedQuery("Egreso.searchByConcepto", Egreso.class).setParameter("concepto", concepto)
                    .getResultList();
            return egresos;
        } catch (Exception ex) {
            log.error("Error al momento de buscar los egresos asociados con el concepto: {}. {}", concepto, ex);
            throw new SystemException("Hubo un problema al buscar los egresos por concepto.");
        } finally {
            super.close(em);
        }
    }

    public List<Egreso> buscarPorFiltros(Date inicio, Date fin, CategoriaEgreso categoria, StatusEgreso status,
            String concepto) throws SystemException {
        EntityManager em = null;
        List<Egreso> egresos = null;
        try {
            em = super.getEntityManager();
            egresos = em.createNamedQuery("", Egreso.class).setParameter("inicio", inicio).setParameter("fin", fin)
                    .setParameter("categoria", categoria).setParameter("status", status)
                    .setParameter("concepto", concepto).getResultList();
            return egresos;
        } catch (Exception ex) {
            log.error(
                    "Error al buscar egresos con filtros: inicio={}, fin={}, categoriaId={}, statusId={}, concepto={}. {}",
                    inicio,
                    fin,
                    categoria.getNombre(),
                    status.getNombre(),
                    concepto,
                    ex);
            throw new SystemException("Hubo un problema al buscar los egresos por filtros.");
        } finally {
            super.close(em);
        }
    }
}
