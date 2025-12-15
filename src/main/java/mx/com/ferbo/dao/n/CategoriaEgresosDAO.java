package mx.com.ferbo.dao.n;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.CategoriaEgreso;
import mx.com.ferbo.util.DAOException;

@Named
@ApplicationScoped
public class CategoriaEgresosDAO extends BaseDAO<CategoriaEgreso, Integer> {
    
    private static final Logger log = LogManager.getLogger(CategoriaEgresosDAO.class);

    public CategoriaEgresosDAO(){
        super(CategoriaEgreso.class);
    }

    public List<CategoriaEgreso> findAll() throws DAOException{
        EntityManager em = null;
        List<CategoriaEgreso> list = null;
        
        try{
            log.info("Inicia proceso para obtener todos las categorias de egresos.");
            em = super.getEntityManager();
            list = em.createNamedQuery("CategoriaEgreso.findByAll", CategoriaEgreso.class).getResultList();
            log.info("Finaliza proceso para obtener todos las categorias de egresos exitosamente.");
            return list;
        } catch (Exception ex){
            log.warn("Error al obtener todas las categorias de egreso. {}", ex);
            throw new DAOException("Hubo un problema al obtener todas las categorias de egreso"); 
        } finally {
          super.close(em);   
        }
    }
}
