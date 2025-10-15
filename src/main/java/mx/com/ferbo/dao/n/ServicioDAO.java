package mx.com.ferbo.dao.n;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.Servicio;

@Named
@ApplicationScoped
public class ServicioDAO extends BaseDAO<Servicio, Integer> {

    public ServicioDAO() {
        super(Servicio.class);
    }

    public List<Servicio> buscarTodos() {
		EntityManager em = null;
		List<Servicio> lista = null;
		try {
			em = super.getEntityManager();
			lista = em.createNamedQuery("Servicio.findAll", Servicio.class).getResultList();
		} finally {
			super.close(em);
		}
		return lista;
	}
}
