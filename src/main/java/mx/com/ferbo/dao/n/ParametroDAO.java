package mx.com.ferbo.dao.n;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.Parametro;
import mx.com.ferbo.util.DAOException;

@Named
@ApplicationScoped
public class ParametroDAO extends BaseDAO<Parametro, Integer> {
 
    private static final Logger log = LogManager.getLogger(ParametroDAO.class);

    public ParametroDAO(){
        super(Parametro.class);
    }

    public Parametro buscarPorNombre(String nombre) throws DAOException{
        EntityManager em = null;
        Parametro parametro = null;
        try {
            log.info("Inicia proceso para buscar el parametro en base a asu nombre.");
            em = super.getEntityManager();
            parametro = em.createNamedQuery("Parametro.findByNombre", Parametro.class)
				.setParameter("nombre", nombre).getSingleResult();
            log.info("Finaliza proceso para buscar el parametro en base a asu nombre.");
            return parametro;
        } catch (Exception ex) {
            log.warn("Error al obtener el parametro con nombre {}. {}", nombre,ex);
            throw new DAOException("Hubo un problema al obtener el parametro");
        } finally {
            super.close(em);
        }
    }
}
