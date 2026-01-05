package mx.com.ferbo.dao.n;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.DocumentoEgreso;
import mx.com.ferbo.util.DAOException;

@Named
@ApplicationScoped
public class DocumentoEgresoDAO extends BaseDAO <DocumentoEgreso, Integer> {

    private static final Logger log = LogManager.getLogger(DocumentoEgresoDAO.class);

    public DocumentoEgresoDAO(){
        super(DocumentoEgreso.class);
    }
    
    public DocumentoEgreso buscarPorPago(Integer id) throws DAOException {
        DocumentoEgreso docuento = null;
        EntityManager em = null;
        try {
            em = super.getEntityManager();
            docuento = em.createQuery("DocumentoEgreso.findByPago", DocumentoEgreso.class).setParameter("id", id).getSingleResult();
            return docuento;
        } catch (Exception ex) {
            log.error("Error al buscar el documento del pago con id {}. {}", id, ex);
            throw new DAOException("Hubo un problema al buscar el documento del pago por id.");
        } finally {
            super.close(em);
        }
    }
}
