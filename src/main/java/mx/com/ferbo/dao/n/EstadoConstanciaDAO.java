package mx.com.ferbo.dao.n;

import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.EstadoConstancia;
import mx.com.ferbo.util.DAOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Named
@ApplicationScoped
public class EstadoConstanciaDAO extends BaseDAO <EstadoConstancia, Integer>
{
    private static Logger log = LogManager.getLogger(EstadoConstanciaDAO.class);

    public EstadoConstanciaDAO(){
        super(EstadoConstancia.class);
    }
    
    public List<EstadoConstancia> buscarTodos() throws DAOException {
        EntityManager entity = null;
        List<EstadoConstancia> alEstados = null;
        try {
            entity = super.getEntityManager();
            alEstados = entity.createNamedQuery("EstadoConstancia.findAll", EstadoConstancia.class)
                    .getResultList();
        } catch (Exception ex) {
            log.error("Problema en la consulta del estado constancia...", ex);
            throw new DAOException("Problema al obtener el estado constancia");
        } finally {
            super.close(entity);
        }
        return alEstados;
    }
    
}
