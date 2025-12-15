package mx.com.ferbo.dao.n;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.EmisoresCFDIS;
import mx.com.ferbo.util.DAOException;

@Named
@ApplicationScoped
public class EmisoresCFDISDAO extends BaseDAO<EmisoresCFDIS, Integer> {

    private static final Logger log = LogManager.getLogger(EmisoresCFDISDAO.class);

    public EmisoresCFDISDAO() {
        super(EmisoresCFDIS.class);
    }

    public List<EmisoresCFDIS> findAll() throws DAOException {
        EntityManager em = null;
        List<EmisoresCFDIS> list = null;
        try {
            log.info("Inicia proceso para obtener todos los emisores de CFDIs.");
            em = super.getEntityManager();
            list = em.createNamedQuery("EmisoresCFDIS.findAll", EmisoresCFDIS.class).getResultList();
            log.info("Finaliza proceso para obtener todos los emisores de CFDIs exitosamente.");
            return list;
        } catch (Exception ex) {
            log.warn("Error al obtener todos los emisores de CFDIs. {}", ex);
            throw new DAOException("Hubo un problema al obtener todos los emisores de CFDIs.");
        } finally {
            super.close(em);
        }
    }
}
