package mx.com.ferbo.dao.n;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.CuotaMinima;

@Named
@ApplicationScoped
public class CuotaMinimaDAO extends BaseDAO<CuotaMinima, Integer> {

    private static Logger log = LogManager.getLogger(CuotaMinimaDAO.class);

    public CuotaMinimaDAO() {
        super(CuotaMinima.class);
    }

    public List<CuotaMinima> buscarTodos() {
        List<CuotaMinima> list;
        EntityManager em = null;

        try {
            em = super.getEntityManager();
            list = em.createNamedQuery("CuotaMinima.findAll", CuotaMinima.class).getResultList();
        } finally {
            super.close(em);
        }

        return list;
    }
}
