package mx.com.ferbo.dao.n;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.ConstanciaSalida;

@Named
@ApplicationScoped
public class ConstanciaSalidaDAO extends BaseDAO <ConstanciaSalida, Integer>{

    public ConstanciaSalidaDAO(){
        super(ConstanciaSalida.class);
    }
    
    public List<ConstanciaSalida> obtenerPorFolioDeposito(Integer folio){
        EntityManager em = null;
        List<ConstanciaSalida> lista = null;
		try {
			em = super.getEntityManager();
            lista = em.createNamedQuery("ConstanciaSalida.findByFolioDeposito", ConstanciaSalida.class).setParameter("folio", folio).getResultList();
		} finally{
            super.close(em);
        }
        return lista;
	}
}
