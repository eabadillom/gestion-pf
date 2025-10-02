package mx.com.ferbo.dao.n;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.MedioCnt;

@Named
@ApplicationScoped
public class MedioCntDAO extends BaseDAO<MedioCnt, Integer>{

    public MedioCntDAO(){
        super(MedioCnt.class);
    }

    public List<MedioCnt> buscarPorIdContacto(Integer idContacto) {
		List<MedioCnt> mediosContacto = new ArrayList<>();
		EntityManager em = null;
		
		try {
			em = super.getEntityManager();
			mediosContacto = em.createNamedQuery("MedioCnt.findByIdContacto", MedioCnt.class)
					.setParameter("idContacto", idContacto).
                    getResultList();
			
		} finally {
			super.close(em);
		}
		
		return mediosContacto;
	}

}
