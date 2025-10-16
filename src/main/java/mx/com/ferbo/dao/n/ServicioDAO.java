package mx.com.ferbo.dao.n;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.Servicio;
import mx.com.ferbo.util.InventarioException;

@Named
@ApplicationScoped
public class ServicioDAO extends BaseDAO<Servicio, Integer> {

    public ServicioDAO() {
        super(Servicio.class);
    }

    public List<Servicio> buscarTodos() throws InventarioException {
		EntityManager em = null;
		List<Servicio> lista = null;
		try {
			em = super.getEntityManager();
			lista = em.createNamedQuery("Servicio.findAll", Servicio.class).getResultList();
		}catch(Exception e) {
			throw new InventarioException("No hay datos en servicio");
		}finally {
			super.close(em);
		}
		return lista;
	}
}