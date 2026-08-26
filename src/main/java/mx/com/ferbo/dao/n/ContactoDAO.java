package mx.com.ferbo.dao.n;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.Contacto;

@Named
@ApplicationScoped
public class ContactoDAO extends BaseDAO<Contacto, Integer>{
	private static Logger log = LogManager.getLogger(ContactoDAO.class);

    public ContactoDAO() {
        super(Contacto.class);
    }
    
    public List<Contacto> buscar(String query) {
    	List<Contacto> modelList;
    	EntityManager em = null;
    	String sql = null;
    	
    	String prmQuery = null;
    	
    	try {
    		prmQuery = new String(query);
			if(prmQuery.startsWith("%") == false)
				prmQuery = "%".concat(prmQuery);
			if(prmQuery.endsWith("%") == false)
				prmQuery = prmQuery.concat("%");
    		
    		sql = "SELECT c FROM Contacto c "
    				+ "INNER JOIN FETCH c.clienteContactoList cc "
    				+ "INNER JOIN FETCH cc.cliente cte "
    				+ "INNER JOIN FETCH c.mediosContacto mc"
    				+ "WHERE c.nombre LIKE :query OR c.apellido1 LIKE :query OR c.apellido2 LIKE :query";
    		em = this.getEntityManager();
    		modelList = em.createQuery(sql, Contacto.class)
    				.setParameter("query", prmQuery)
    				.getResultList();
    	} catch(Exception ex) {
    		log.error("Problema para obtener la lista de contactos con base en la consulta: query={}\n{}", query, ex);
    		modelList = new ArrayList<Contacto>();
    	} finally {
    		this.close(em);
    	}
    	
    	return modelList;
    }
    
}
