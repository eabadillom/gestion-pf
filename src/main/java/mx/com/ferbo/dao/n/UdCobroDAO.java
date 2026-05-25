package mx.com.ferbo.dao.n;

import java.util.List;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.UdCobro;

public class UdCobroDAO extends BaseDAO <UdCobro, Integer> {

    private static Logger log = LogManager.getLogger(UdCobroDAO.class);

    public UdCobroDAO (){
        super(UdCobro.class);
    }

    public List<UdCobro> buscarTodos() {
		List<UdCobro> list = null;
		EntityManager em = null;
		try {
			em = super.getEntityManager();
			list = em.createNamedQuery("UdCobro.findAll", UdCobro.class).getResultList();
		} finally {
			super.close(em);
		}
		return list;
	}
}
