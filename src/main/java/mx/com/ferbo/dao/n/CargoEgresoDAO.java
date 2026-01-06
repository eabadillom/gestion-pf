package mx.com.ferbo.dao.n;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.CargoEgreso;
import mx.com.ferbo.util.DAOException;

@Named
@ApplicationScoped
public class CargoEgresoDAO extends BaseDAO <CargoEgreso, Integer> {

    private static final Logger log = LogManager.getLogger();

    public CargoEgresoDAO(){
        super(CargoEgreso.class);
    }

    public List<CargoEgreso> buscarTodosPorImporteEgreso(Integer id) throws DAOException{
        List<CargoEgreso> lista = null;
        EntityManager em = null;
        try {
            em = super.getEntityManager();
            lista = em.createQuery("CargoEgreso.findAllByImporteEgreso", CargoEgreso.class).setParameter("id", id).getResultList();
            return lista;
        } catch (Exception ex) {
            log.error("Error al buscar todos los cargos asociados al importe de egreso con id {}. {}", id, ex);
            throw new DAOException("Hubo un problema al buscar los cargos asociado al importe de egreso por id.");
        } finally {
            super.close(em);
        }
    }
}
