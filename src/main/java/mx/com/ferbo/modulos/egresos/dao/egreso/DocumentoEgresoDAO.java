package mx.com.ferbo.modulos.egresos.dao.egreso;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;

import mx.com.ferbo.modulos.egresos.dao.EgresoBaseDAO;
import mx.com.ferbo.modulos.egresos.model.catsecundarios.StatusEgreso;
import mx.com.ferbo.modulos.egresos.model.egreso.DocumentoEgreso;
import mx.com.ferbo.util.DAOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Named
@ApplicationScoped
public class DocumentoEgresoDAO extends EgresoBaseDAO<DocumentoEgreso> {
    
    private static final Logger log = LogManager.getLogger(DocumentoEgresoDAO.class);

    public DocumentoEgresoDAO() {
        super(DocumentoEgreso.class);
    }

    @Override
    protected Class<DocumentoEgreso> getEntityClass() {
        return DocumentoEgreso.class;
    }

    public DocumentoEgreso bucarPorPago(Integer idPago) throws DAOException {
        EntityManager em = null;
        DocumentoEgreso documento = null;
        try {
            em = super.getEntityManager();
            documento = em.createNamedQuery("DocumentoEgreso.findByPago", DocumentoEgreso.class).setParameter("idPago", idPago).getSingleResult();
            return documento;
        } catch (Exception ex) {
            log.error("Error al buscar el documento asociado con el pago de id: {}. {}", idPago, ex);
            throw new DAOException("Hubo en problema al buscar el documento del pago seleccionado.");
        } finally {
            super.close(em);
        }
    }
}
