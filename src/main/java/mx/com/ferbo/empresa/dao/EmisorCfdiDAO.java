package mx.com.ferbo.empresa.dao;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.transaction.SystemException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.EmisoresCFDIS;

@Named
@ApplicationScoped
public class EmisorCfdiDAO extends BaseDAO<EmisoresCFDIS, Integer> {

    private static final Logger log = LogManager.getLogger(EmisorCfdiDAO.class);

    private EntityManager em;

    public EmisorCfdiDAO() {
        super(EmisoresCFDIS.class);
    }

    public EmisorCfdiDAO(Class<EmisoresCFDIS> modelClass) {
        super(modelClass);
    }

    public List<EmisoresCFDIS> obtenerTodos() throws SystemException {
        try {
            em = getEntityManager();
            return em.createNamedQuery("EmisoresCFDIS.findAll", EmisoresCFDIS.class).getResultList();
        } catch (Exception ex) {
            log.error("Error al momento de buscar todos los emisores de cfdi.");
            throw new SystemException("Hubo un problema al momento de buscar a todos los emisores de CFDI");
        } finally {
            close(em);
        }
    }
}
