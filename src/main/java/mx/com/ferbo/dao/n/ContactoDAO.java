package mx.com.ferbo.dao.n;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.Contacto;
import mx.com.ferbo.util.InventarioException;

@Named
@ApplicationScoped
public class ContactoDAO extends BaseDAO<Contacto, Integer>{
	private static Logger log = LogManager.getLogger(ContactoDAO.class);

    public ContactoDAO() {
        super(Contacto.class);
    }
    
    @Override
    public synchronized void guardar(Contacto model) throws InventarioException {
		EntityManager em = null;
		
		try {
			log.info("Guardando objeto: {}", model);
			em = getEntityManager();
			em.getTransaction().begin();
			em.persist(model);
			em.flush();
            em.refresh(model);
			em.getTransaction().commit();
			log.info("Objeto guardado correctamente: {}", model);
		} catch(Exception ex) {
			rollback(em);
			log.error("Problema para guardar el objeto: " + model, ex);
			throw new InventarioException("Error al guardar en la base de datos.");
		} finally {
			close(em);
		}
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
    				+ "INNER JOIN FETCH c.mediosContacto mc "
    				+ "WHERE CONCAT("
                    +   "COALESCE(c.nombre, ''), ' ', "
                    +   "COALESCE(c.apellido1, ''), ' ', "
                    +   "COALESCE(c.apellido2, '')"
                    + ") LIKE :query "
                    + "ORDER BY c.nombre ASC, c.apellido1 ASC, c.apellido2 ASC";
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
    
    public Optional<Contacto> cargar(Contacto contacto) {
    	Optional<Contacto> optional = null;
    	Contacto model = null;
    	EntityManager em = null;
    	String sql = null;
    	
    	try {
    		
    		sql = "SELECT c FROM Contacto c "
    				+ "INNER JOIN FETCH c.clienteContactoList cc "
    				+ "INNER JOIN FETCH cc.cliente cte "
    				+ "INNER JOIN c.mediosContacto mc "
    				+ "WHERE c.id = :id";
    		
    		em = this.getEntityManager();
    		model = em.createQuery(sql, this.modelClass)
    				.setParameter("id", contacto.getId())
    				.getSingleResult();
    		
    		model.getMediosContacto().stream().forEach(medioContacto -> {
    			if(medioContacto.getMail() != null)
    				log.info("Mail: {}", medioContacto.getMail().getIdMail());
    			if(medioContacto.getTelefono() != null)
    				log.info("Telefono: {}", medioContacto.getTelefono().getId());
    		});
    		
    		optional = Optional.of(model);
    		
    	} catch(Exception ex) {
    		log.error("Contacto no encontrado...", ex);
    		optional = Optional.empty();
    	} finally {
    		this.close(em);
    	}
    	
    	return optional;
    }
    
}
