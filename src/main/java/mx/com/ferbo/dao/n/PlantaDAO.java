package mx.com.ferbo.dao.n;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.Planta;
import mx.com.ferbo.model.SerieConstancia;

@Named
@ApplicationScoped
public class PlantaDAO extends BaseDAO <Planta, Integer> {

    private static Logger log = LogManager.getLogger(PlantaDAO.class);

    public PlantaDAO(){
        super(Planta.class);
    }

    public List<Planta> buscarTodos(Boolean isFullInfo) {
		List<Planta> list = null;
		EntityManager em = null;
		try {
			em = super.getEntityManager();
			list = em.createNamedQuery("Planta.findAll", Planta.class).getResultList();

			if (!isFullInfo){
				return list;
			}
			
			for(Planta p : list) {
				log.debug(p.getIdUsuario().getUsuario());//ERROR lazy 
				
				log.debug(p.getIdEmisoresCFDIS().getNb_emisor()); //no tienen notacion lazy
				log.debug(p.getIdEmisoresCFDIS().getNb_rfc());
				log.debug(p.getIdEmisoresCFDIS().getNb_emisor());
				for(SerieConstancia serieConstancia : p.getSerieConstanciaList()) {
					log.debug(serieConstancia.getSerieConstanciaPK().getPlanta().getPlantaCve());
				}
			}

		}  finally {
			super.close(em);
		}
		return list;
	}
}
