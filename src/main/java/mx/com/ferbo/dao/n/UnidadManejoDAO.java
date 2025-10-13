package mx.com.ferbo.dao.n;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.UnidadDeManejo;

@Named
@ApplicationScoped
public class UnidadManejoDAO extends BaseDAO<UnidadDeManejo, Integer>{

    public UnidadManejoDAO() {
        super(UnidadDeManejo.class);
    }

    public List<UnidadDeManejo> buscarTodos() {
		List<UnidadDeManejo> lista = null;
		EntityManager em = null;

		try {
			em = super.getEntityManager();
			lista = em.createNamedQuery("UnidadDeManejo.findAll", UnidadDeManejo.class).getResultList();
		}  finally {
			super.close(em);
		}
		
		return lista;
	}

}
