package mx.com.ferbo.dao.n;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.ConstanciaSalida;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Named
@ApplicationScoped
public class ConstanciaSalidaDAO extends BaseDAO <ConstanciaSalida, Integer>
{
    private static Logger log = LogManager.getLogger(ConstanciaSalidaDAO.class);

    public ConstanciaSalidaDAO(){
        super(ConstanciaSalida.class);
    }
    
    public List<ConstanciaSalida> obtenerPorFolioDeposito(Integer folio){
        EntityManager em = null;
        List<ConstanciaSalida> lista = null;
        try {
            em = super.getEntityManager();
            lista = em.createNamedQuery("ConstanciaSalida.findByFolioDeposito", ConstanciaSalida.class)
                .setParameter("folio", folio)
                .getResultList();
        } finally{
            super.close(em);
        }
        
        return lista;
    }
    
    public String buscarPorNumero(String numFolio) {
        ConstanciaSalida constanciaSalida;
        EntityManager em = null;
        String folio = "";

        try {
            em = super.getEntityManager();
            constanciaSalida = em.createNamedQuery("ConstanciaSalida.findByNumero", ConstanciaSalida.class)
                .setParameter("numero", numFolio)
                .getSingleResult();
            
            if(constanciaSalida!=null) {
                folio = constanciaSalida.getNumero();
            }			

        } catch (Exception e) {
            log.error("Error al buscar por numero constancia salida", e.getMessage());
        }finally {
            super.close(em);
        }

        return folio;
    }
    
}
