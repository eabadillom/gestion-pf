package mx.com.ferbo.dao.n;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.TipoMail;

/**
 *
 * @author julio
 */
@Named
@ApplicationScoped
public class TipoMailDAO extends BaseDAO <TipoMail, Integer>{

    public TipoMailDAO() {
        super(TipoMail.class);
    }

    public List<TipoMail> buscarTodos() {
        EntityManager em = super.getEntityManager();
        List<TipoMail> listado = new ArrayList<>();
        try{
        listado = em.createNamedQuery("TipoMail.findAll", TipoMail.class).getResultList();
        } finally {
            super.close(em);
        }
        return listado;
    }
    
    public Optional<TipoMail> buscarPorId(Integer id){
        return super.buscarPorId(id);
    }
}
