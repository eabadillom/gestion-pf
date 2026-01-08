package mx.com.ferbo.dao.n.egresos;

import java.util.Date;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.n.egresos.ImporteEgreso;
import mx.com.ferbo.util.DAOException;

@Named
@ApplicationScoped
public class ImporteEgresoDAO extends BaseDAO<ImporteEgreso, Integer> {

    private static final Logger log = LogManager.getLogger(ImporteEgresoDAO.class);

    public ImporteEgresoDAO() {
        super(ImporteEgreso.class);
    }

    public List<ImporteEgreso> buscarTodosPorPeriodo(Date inicio, Date fin) throws DAOException {
        List<ImporteEgreso> lista = null;
        EntityManager em = null;
        try {
            em = super.getEntityManager();
            lista = em.createQuery("ImporteEgreso.findAllByMes", ImporteEgreso.class).setParameter("inicio", inicio)
                    .setParameter("fin", fin).getResultList();
            return lista;
        } catch (Exception ex) {
            log.error("Error al obtener todos los importes de egreso desde {} hasta {}. {}", inicio, fin, ex);
            throw new DAOException("Hubo un problema al buscar todos los importes de egreso del mes dado.");
        } finally {
            super.close(em);
        }
    }

    public List<ImporteEgreso> buscarTodosPorConcepto(String concepto) throws DAOException {
        List<ImporteEgreso> lista = null;
        EntityManager em = null;
        try {
            em = super.getEntityManager();
            lista = em.createQuery("ImporteEgreso.findAllByConcepto", ImporteEgreso.class).setParameter("concepto", concepto).getResultList();
            return lista;
        } catch (Exception ex) {
            log.error("Error al buscar todos los importe de egreso asociado al concepto {}. {}", concepto, ex);
            throw new DAOException("Hubo un problema al buscar la lista de importe de egresos asociados al concepto dado");
        } finally {
            super.close(em);
        }
    }
}
