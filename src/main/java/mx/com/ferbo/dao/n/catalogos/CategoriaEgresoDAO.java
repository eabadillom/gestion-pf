package mx.com.ferbo.dao.n.catalogos;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.n.catalogos.CategoriaEgreso;
import mx.com.ferbo.util.DAOException;

@Named
@ApplicationScoped
public class CategoriaEgresoDAO extends BaseDAO<CategoriaEgreso, Integer> {

    private static final Logger log = LogManager.getLogger(CategoriaEgresoDAO.class);

    public CategoriaEgresoDAO(){
        super(CategoriaEgreso.class);
    }

    public List<CategoriaEgreso> buscarTodosPorTipoEgreso(Integer id) throws DAOException{
        List<CategoriaEgreso> lista = null;
        EntityManager em = null;
        try {
            em = super.getEntityManager();
            lista = em.createNamedQuery("CategoriaEgreso.findAllByTipoEgreso", CategoriaEgreso.class).setParameter("id", id).getResultList();
            return lista;
        } catch (Exception ex) {
            log.error("Error al buscar las tegorias por el tipo de egreso con id {}. {}", id, ex);
            throw new DAOException("Hubo un problema al obtener las categorias por el id de tipo de egreo");
        } finally {
            super.close(em);
        }
    }

    public List<CategoriaEgreso> buscarTodosPorTipoEgresoYActivo(Integer id, boolean vigente) throws DAOException {
        List<CategoriaEgreso> lista = null;
        EntityManager em = null;
        try {
            em = super.getEntityManager();
            lista = em.createNamedQuery("CategoriaEgreso.findAllByTipoEgresoYVigenteONoVigente", CategoriaEgreso.class).setParameter("id", id).setParameter("vigente", vigente).getResultList();
            return lista;
        } catch (Exception ex) {
            String estado = vigente ? "activo" : "no activo";
            log.error("Error al buscar todas las categorias del tipo de egreso con id {} y estado {}. {}", id, estado, ex);
            throw new DAOException("Hubo un problema al buscar el listado de categoria en base al tipo y estado");
        } finally {
            super.close(em);
        }
    }
}
