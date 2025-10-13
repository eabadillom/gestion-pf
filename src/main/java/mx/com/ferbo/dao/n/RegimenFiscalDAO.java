package mx.com.ferbo.dao.n;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.RegimenFiscal;
import mx.com.ferbo.util.InventarioException;

@Named
@ApplicationScoped
public class RegimenFiscalDAO extends BaseDAO<RegimenFiscal, Integer> {

    private static Logger log = LogManager.getLogger(RegimenFiscalDAO.class);

    public RegimenFiscalDAO() {
        super(RegimenFiscal.class);
    }

    public List<RegimenFiscal> buscarPorTipoPersona(String tipoPersona) throws InventarioException {
        EntityManager em = null;
        List<RegimenFiscal> listaRegimen = new ArrayList<>();

        try {
            em = super.getEntityManager();

            String namedQuery;
            if ("F".equalsIgnoreCase(tipoPersona)) {
                namedQuery = "RegimenFiscal.findByst_per_fisica";
            } else if ("M".equalsIgnoreCase(tipoPersona)) {
                namedQuery = "RegimenFiscal.findByst_per_moral";
            } else {
                throw new IllegalStateException("Tipo de persona no válido: " + tipoPersona);
            }

            listaRegimen = em.createNamedQuery(namedQuery, RegimenFiscal.class).getResultList();

        } catch (IllegalStateException ex) {
            log.warn(ex.getMessage(), ex);
            throw new InventarioException(ex.getMessage());
        } catch (Exception ex) {
            log.error("Problema al buscar régimen fiscal para tipo persona: " + tipoPersona, ex);
            throw new InventarioException("Problema al buscar régimen fiscal para tipo persona.");
        } finally {
            super.close(em);
        }

        return listaRegimen;
    }

}
