package mx.com.ferbo.dao.n;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.TipoDocumento;
import mx.com.ferbo.util.DAOException;

@Named
@ApplicationScoped
public class TipoDocumentoDAO extends BaseDAO <TipoDocumento, Integer> {

    private static final Logger log = LogManager.getLogger(TipoDocumentoDAO.class);

    public TipoDocumentoDAO(){
        super(TipoDocumento.class);
    }

    public TipoDocumento buscarPorNombre(String nombre) throws DAOException{
        TipoDocumento tipo = null;
        EntityManager em = null;
        try {
            em = super.getEntityManager();
            tipo = em.createQuery("TipoDocumento.findByNombre", TipoDocumento.class).setParameter("nombre", nombre).getSingleResult();
            return tipo;
        } catch (Exception ex) {
            log.error("Error al buscar el tipo de de cocumento con nombre {}. {}", nombre, ex);
            throw new DAOException("Hubo un problema la buscar el tipo de documento.");
        } finally {
            super.close(em);
        }
    }

    public List<TipoDocumento> buscarActivos(boolean activo) throws DAOException{
        List<TipoDocumento> lista = null;
        EntityManager em = null;
        try {
            em = super.getEntityManager();
            if (activo){
            lista = em.createQuery("TipoDocumento.findAllActivos", TipoDocumento.class).getResultList();
            }
            else {
                lista = em.createQuery("TipoDocumento.findAllNoActivos", TipoDocumento.class).getResultList();
            }
            return lista;
        } catch (Exception ex) {
            String estado = activo ? "activos" : "no activos";
            log.error("Error al buscar los tipos de documento {}. {}", estado, ex);
            throw new DAOException("Hubo un problema al buscar los tipos de documento " + estado);
        } finally {
            super.close(em);
        }
    }
}
