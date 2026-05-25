package mx.com.ferbo.dao.n;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.TipoMail;
import mx.com.ferbo.util.DAOException;

/**
 *
 * @author julio
 */
@Named
@ApplicationScoped
public class TipoMailDAO extends BaseDAO <TipoMail, Integer>{

    private static Logger log = LogManager.getLogger(TipoMailDAO.class);

    public TipoMailDAO() {
        super(TipoMail.class);
    }

    public List<TipoMail> buscarTodos() throws DAOException {
        EntityManager em = super.getEntityManager();
        List<TipoMail> list = new ArrayList<>();
        try{
        list = em.createNamedQuery("TipoMail.findAll", TipoMail.class).getResultList();
        } catch (Exception ex) {
            log.error("Error al obtener los tipos de email", ex);
            throw new DAOException("Ocurrrió un problema al obtener los tipos de email", ex);
        } finally {
            super.close(em);
        }
        return list;
    }
    
    public Optional<TipoMail> buscarPorId(Integer id){
        return super.buscarPorId(id);
    }
}
