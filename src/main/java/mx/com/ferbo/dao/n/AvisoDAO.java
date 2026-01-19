package mx.com.ferbo.dao.n;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.Aviso;
import mx.com.ferbo.util.DAOException;

@Named
@ApplicationScoped
public class AvisoDAO extends BaseDAO <Aviso, Integer> {

    private static Logger log = LogManager.getLogger(AvisoDAO.class);

    public AvisoDAO(){
        super(Aviso.class);
    }

    public List<Aviso> buscarPorCliente(Integer cteCve) throws DAOException {
		List<Aviso> lista = null;
		EntityManager em = null;
		
		try {
			em = super.getEntityManager();
			lista = em.createNamedQuery("Aviso.findByAvisoCliente", Aviso.class)
					.setParameter("cteCve", cteCve).getResultList();
		} catch(Exception ex) {
			log.error("Problema en la consulta de los avisos del cliente " + cteCve, ex);
            throw new DAOException("Problema al obtener los avisos del cliente");
		} finally {
			super.close(em);;
		}
		return lista;
	}

    public int conteoConstanciaDeDeposito(Aviso aviso) throws DAOException {
		
		EntityManager em = null;
		int count = 0;
		Query query = null;
		
		try {
			em = super.getEntityManager();
			query =  em.createNativeQuery("SELECT COUNT(cdd.aviso_cve) FROM constancia_de_deposito cdd WHERE cdd.aviso_cve = :avisoCve");
			query.setParameter("avisoCve", aviso.getAvisoCve());	
			count =  Integer.parseInt(query.getSingleResult().toString());
		} catch (Exception ex) {
			log.error("Problema al contar registros",ex);
            throw new DAOException("Problema al contar las constancias de deposito");
		}finally {
			super.close(em);
		}
		return count;
	}
	
    public int conteoPrecioServicio(Aviso aviso) throws DAOException {
		
		EntityManager em = null;
		Query query = null;
		int count = 0;
		
		try {
			em = super.getEntityManager();
			query =  em.createNativeQuery("SELECT COUNT(ps.aviso_cve) FROM precio_servicio ps  WHERE ps.aviso_cve = :avisoCve");
			query.setParameter("avisoCve", aviso.getAvisoCve());	
			count =  Integer.parseInt(query.getSingleResult().toString());
		} catch (Exception ex) {
			log.error("Problema al contar registros", ex);
            throw new DAOException("Problema al contar los precios de servicios");
		}finally {
			super.close(em);
		}
		
		return count;
	}
}
