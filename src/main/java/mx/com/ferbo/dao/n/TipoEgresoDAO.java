package mx.com.ferbo.dao.n;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.n.TipoEgreso;
import mx.com.ferbo.util.DAOException;

@Named
@ApplicationScoped
public class TipoEgresoDAO extends BaseDAO <TipoEgreso, Integer> {

    private static final Logger log = LogManager.getLogger(TipoEgresoDAO.class);

    public TipoEgresoDAO(){
        super(TipoEgreso.class);
    }

    public List<TipoEgreso> buscarActivos(boolean activo) throws DAOException{
        EntityManager em = null;
        List<TipoEgreso> lista = null;
        try{
            em = super.getEntityManager();
            if ( activo){
                lista = em.createQuery("TipoEgreso.findAllAcivos", TipoEgreso.class).getResultList();
            } else {
                lista = em.createQuery("TipoEgreso.findAllNoAcivos", TipoEgreso.class).getResultList();
            }
            return lista;
        } catch (Exception ex) {
            String estado = activo ? "activos" : "no activos";
            log.error("Error no se pudieron obtener los tipo de egreso {}. {}", estado, ex);
            throw new DAOException("Hubo un problema al buscar los tipo de egreso " + estado);
        } finally {
            super.close(em);
        }
    }
}
