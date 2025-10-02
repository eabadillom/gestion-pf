
package mx.com.ferbo.dao.n;

import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.TipoTelefono;

/**
 *
 * @author julio
 */
@Named
@ApplicationScoped
public class TipoTelefonoDAO extends BaseDAO {
    
    public TipoTelefonoDAO() {
        super(TipoTelefono.class);
    }
    
    public List<TipoTelefono> buscarTodos() {
		EntityManager em = super.getEntityManager();
		List<TipoTelefono> listado = new ArrayList<>();
                try{
		listado = em.createNamedQuery("TipoTelefono.findAll", TipoTelefono.class).getResultList();
                }
                finally{
                    super.close(em);
                }
		return listado;
	}
}
