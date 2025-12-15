package mx.com.ferbo.dao.n;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.TipoEgreso;
import mx.com.ferbo.util.DAOException;

@Named
@ApplicationScoped
public class TipoEgresoDAO extends BaseDAO<TipoEgreso, Integer> {
    
    private static final Logger log = LogManager.getLogger(TipoEgresoDAO.class);

    public TipoEgresoDAO(){
        super(TipoEgreso.class);
    }

    public List<TipoEgreso> findAll() throws DAOException{
        EntityManager em = null;
        List<TipoEgreso> list = null;
        try {
            log.info("Incia proceso para obtener todos los tipos de egresos.");
            em = super.getEntityManager();
            list = em.createNamedQuery("TipoEgreso.findByAll", TipoEgreso.class).getResultList();
            log.info("Finaliza proceso para obtener todos los tipos de egresos exitosamente.");
            return list;
        } catch(Exception ex) {
            log.warn("Error al obtener los tipos de egreso de la base de datos. {}", ex);
            throw new DAOException("Hubo un problema al obtener los tipos de egreso");
        } finally {
            super.close(em);
        }
    }
}
