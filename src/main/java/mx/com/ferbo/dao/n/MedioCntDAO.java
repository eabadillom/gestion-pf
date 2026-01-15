package mx.com.ferbo.dao.n;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.ClienteContacto;
import mx.com.ferbo.model.MedioCnt;
import mx.com.ferbo.util.DAOException;

@Named
@ApplicationScoped
public class MedioCntDAO extends BaseDAO<MedioCnt, Integer>{

	private static Logger log = LogManager.getLogger(MedioCntDAO.class);

    public MedioCntDAO(){
        super(MedioCnt.class);
    }

    public List<MedioCnt> buscarPorIdContacto(ClienteContacto clienteContacto) throws DAOException {

		Integer idContacto = clienteContacto.getIdContacto().getIdContacto();

		List<MedioCnt> mediosContacto = null;
		EntityManager em = null;
		
		try {
			em = super.getEntityManager();
			mediosContacto = em.createNamedQuery("MedioCnt.findByIdContacto", MedioCnt.class)
					.setParameter("idContacto", idContacto).
                    getResultList();
			
		} catch (Exception ex) {
			log.error("Error al obtener los medios de contacto del contacto: " + clienteContacto.getIdContacto().getNbNombre(), ex);
			throw new DAOException("Ocurrio un error al obtener los medios de contacto del contacto: " + clienteContacto.getIdContacto().getNbNombre(), ex);
		} finally {
			super.close(em);
		}
		
		return mediosContacto;
	}

}
