package mx.com.ferbo.dao.egresos;

import java.util.Date;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.egresos.ImporteEgreso;
import mx.com.ferbo.util.DAOException;

@Named
@ApplicationScoped
public class ImporteEgresoDAO extends BaseDAO<ImporteEgreso, Integer> {

    private static final Logger log = LogManager.getLogger(ImporteEgresoDAO.class);

    public ImporteEgresoDAO() {
        super(ImporteEgreso.class);
    }

    public List<ImporteEgreso> buscarPorFiltros(Date inicio, Date fin,
            Integer idConcepto, Integer idEmisor)
            throws DAOException {
        EntityManager em = null;
        try {
            em = super.getEntityManager();
            return em.createNamedQuery("ImporteEgreso.findByFiltros", ImporteEgreso.class)
                    .setParameter("inicio", inicio)
                    .setParameter("fin", fin)
                    .setParameter("idConcepto", idConcepto)
                    .setParameter("idEmisor", idEmisor)
                    .getResultList();

        } catch (Exception ex) {
            log.error("Error al obtener importes de egreso. {}", ex);
            throw new DAOException("Hubo un problema al buscar los importes de egreso.");
        } finally {
            super.close(em);
        }
    }
}
