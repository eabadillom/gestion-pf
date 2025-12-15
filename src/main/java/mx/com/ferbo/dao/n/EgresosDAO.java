package mx.com.ferbo.dao.n;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.Egresos;
import mx.com.ferbo.util.DAOException;

@Named
@ApplicationScoped
public class EgresosDAO extends BaseDAO<Egresos, Integer>{
    
    private static final Logger log = LogManager.getLogger(EgresosDAO.class);

    public EgresosDAO(){
        super(Egresos.class);
    }

    public List<Egresos> findAll() throws DAOException {
        EntityManager em = null;
        List<Egresos> list = null;
        try {
            log.info("Inicia proceso para obtener todos los egresos.");
            em = super.getEntityManager();
            list = em.createNamedQuery("Egresos.findByAll", Egresos.class).getResultList();
            log.info("Finaliza proceso para obtener todos los egresos exitosamente.");
            return list;
        } catch (Exception ex) {
            log.warn("Error al obtener todos los egresos. {}",ex);
            throw new DAOException("Hubo un problema al obtener todos los egresos.");
        } finally {
            super.close(em);
        }
    }
}
