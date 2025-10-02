package mx.com.ferbo.dao.n;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.UnidadDeManejo;
import mx.com.ferbo.util.EntityManagerUtil;
import mx.com.ferbo.util.InventarioException;

@Named
@ApplicationScoped
public class UnidadManejoDAO extends BaseDAO<UnidadDeManejo, Integer>{

    public UnidadManejoDAO() {
        super(UnidadDeManejo.class);
    }

    public List<UnidadDeManejo> buscarTodos() throws InventarioException {
		List<UnidadDeManejo> lista = null;
		EntityManager em = null;

		try {
			em = super.getEntityManager();
			lista = em.createNamedQuery("UnidadDeManejo.findAll", UnidadDeManejo.class).getResultList();
		} catch(Exception ex) {
			throw new InventarioException("No existen datos en unidad de manejo");
		} finally {
			EntityManagerUtil.close(em);
		}
		
		return lista;
	}

}
