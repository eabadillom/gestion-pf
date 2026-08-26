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
import mx.com.ferbo.model.MedioContacto;
import mx.com.ferbo.util.DAOException;

@Named
@ApplicationScoped
public class MedioCntDAO extends BaseDAO<MedioContacto, Integer>{

	private static Logger log = LogManager.getLogger(MedioCntDAO.class);

    public MedioCntDAO(){
        super(MedioContacto.class);
    }

    public List<MedioContacto> buscarPorIdContacto(ClienteContacto clienteContacto) throws DAOException {

		Integer idContacto = clienteContacto.getContacto().getId();

		List<MedioContacto> mediosContacto = null;
		EntityManager em = null;
		
		try {
			em = super.getEntityManager();
			mediosContacto = em.createNamedQuery("MedioCnt.findByIdContacto", MedioContacto.class)
					.setParameter("idContacto", idContacto).
                    getResultList();
			
		} catch (Exception ex) {
			log.error("Error al obtener los medios de contacto del contacto: " + clienteContacto.getContacto().getNombre(), ex);
			throw new DAOException("Ocurrio un error al obtener los medios de contacto del contacto: " + clienteContacto.getContacto().getNombre(), ex);
		} finally {
			super.close(em);
		}
		
		return mediosContacto;
	}

}
