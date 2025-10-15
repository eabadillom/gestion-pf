package mx.com.ferbo.dao.n;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.UsoCfdi;
import mx.com.ferbo.util.InventarioException;

public class UsoCfdiDAO extends BaseDAO<UsoCfdi, Integer>{

    private static Logger log = LogManager.getLogger(UsoCfdiDAO.class);

    public UsoCfdiDAO(){
        super(UsoCfdi.class);
    }

    public List<UsoCfdi> buscarUsoCfdiPorTipoPersona(String tipoPersona) throws InventarioException {
    List<UsoCfdi> list = new ArrayList<>();
    EntityManager em = null;

    try {
        em = super.getEntityManager();

        String namedQuery;
        if ("F".equalsIgnoreCase(tipoPersona)) {
            namedQuery = "UsoCfdi.findByPersonaFisica";
        } else if ("M".equalsIgnoreCase(tipoPersona)) {
            namedQuery = "UsoCfdi.findByPersonaMoral";
        } else {
            throw new IllegalStateException("Tipo de persona no válido: " + tipoPersona);
        }

        list = em.createNamedQuery(namedQuery, UsoCfdi.class).getResultList();

    
    } catch (IllegalStateException ex) {
        log.warn(ex.getMessage(), ex);
        throw new InventarioException(ex.getMessage());
    }catch (Exception ex) {
        log.error("Problema para obtener el listado de Usos del CFDI para tipo " + tipoPersona, ex);
        throw new InventarioException("Problema para obtener el listado de Usos del CFDI");
    } finally {
        super.close(em);
    }

    return list;
}
}
