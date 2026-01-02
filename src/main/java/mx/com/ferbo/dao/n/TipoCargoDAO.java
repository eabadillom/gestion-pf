package mx.com.ferbo.dao.n;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.TipoCargo;
import mx.com.ferbo.util.DAOException;

@Named
@ApplicationScoped
public class TipoCargoDAO extends BaseDAO<TipoCargo, Integer>{

    private static final Logger log = LogManager.getLogger(TipoCargoDAO.class);

    public TipoCargoDAO(){
        super(TipoCargo.class);
    }

    public TipoCargo buscarPorNombre(String nombre) throws DAOException{
        TipoCargo tipo = null;
        EntityManager em = null;
        try {
            em = super.getEntityManager();
            tipo = em.createQuery("TipoCargo.findByNombre", TipoCargo.class).setParameter("nombre", nombre).getSingleResult();
            return tipo;
        } catch (Exception ex) {
            log.error("Error al buscar el tipo de cargo con nombre {}. {}", nombre, ex);
            throw new DAOException("Hubo un problema al buscar el tipo de cargo por nombre.");
        } finally {
            super.close(em);
        }
    }

    public List<TipoCargo> buscarActivos(boolean activo) throws DAOException{
        List<TipoCargo> lista = null;
        EntityManager em = null;
        try {
            em = super.getEntityManager();
            if (activo) {
                lista = em.createQuery("TipoCargo.findAllActivos", TipoCargo.class).getResultList();
            } else {
                lista = em.createQuery("TipoCargo.findAllNoActivos", TipoCargo.class).getResultList();
            }
            return lista;
        } catch (Exception ex) {
            String estado = activo ? "activos" : "no activos";
            log.error("Error al buscar buscar todos los tipo de cargo {}. {}", estado, ex);
            throw new DAOException("Hubo un problema al buscar todos los tipos de cargo " + estado);
        } finally {

        }
    }
}
