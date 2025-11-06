package mx.com.ferbo.dao.n;

import java.util.List;

import javax.persistence.EntityManager;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.Factura;

public class FacturaDAO extends BaseDAO <Factura, Integer>{

    public FacturaDAO(){
        super(Factura.class);
    }

    public List<Factura> obtenerPorFolioDeposito(Integer folio){
        EntityManager em = null;
        List<Factura> lista = null;

        try{
            em = super.getEntityManager();
            lista = em.createNamedQuery("Factura.findByFolioDeposito", Factura.class).setParameter("folio",folio).getResultList();
        } finally {
            close(em);
        }    
        return lista;
    }
    
}
