package mx.com.ferbo.dao.n;

import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.Certificado;
import mx.com.ferbo.model.EmisoresCFDIS;
import mx.com.ferbo.util.EntityManagerUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author alberto
 */
@Named
@ApplicationScoped
public class EmisoresCFDISDAO extends BaseDAO<EmisoresCFDIS, Integer>
{
    private static Logger log = LogManager.getLogger(EmisoresCFDISDAO.class);

    public EmisoresCFDISDAO(Class<EmisoresCFDIS> modelClass) {
        super(modelClass);
    }
    
    public EmisoresCFDISDAO(){
        super(EmisoresCFDIS.class);
    }
    
    public List<EmisoresCFDIS> buscarTodos() 
    {
        List<EmisoresCFDIS> listaEmisores = null;
        EntityManager em = null;

        try {
            em = EntityManagerUtil.getEntityManager();
            
            listaEmisores = em.createNamedQuery("EmisoresCFDIS.findAll", EmisoresCFDIS.class)
                .getResultList();
            
            
        } catch (Exception e) {
            log.error("Problema al recuperar lista emisoresCFDIS", e);
        } finally {
            EntityManagerUtil.close(em);
        }

        return listaEmisores;
    }
    
    public List<EmisoresCFDIS> buscarTodos(boolean isFullInfo) 
    {
        List<EmisoresCFDIS> modelList = null;
        EntityManager em = null;

        try {
            em = EntityManagerUtil.getEntityManager();
            modelList = em.createNamedQuery("EmisoresCFDIS.findAll", EmisoresCFDIS.class)
                .getResultList();
            
            if (isFullInfo == false) {
                return modelList;
            }

            for (EmisoresCFDIS e : modelList) {
                log.debug(e.getCd_regimen().getNb_regimen());
                for (Certificado c : e.getListaCertificado()) {
                    log.debug("Certificado: {}", c.getCdCertificado());
                }
            }

        } catch (Exception ex) {
            log.error("Problema al obtener la lista de emisoresCFDIS ", ex);
        } finally {
            EntityManagerUtil.close(em);
        }

        return modelList;
    }
    
    public List<EmisoresCFDIS> buscarPorCriterios(EmisoresCFDIS e) {

        EntityManager em = null;
        List<EmisoresCFDIS> listaEmisores = null;

        try {

            em = EntityManagerUtil.getEntityManager();
            listaEmisores = em.createNamedQuery("EmisoresCFDIS.findByregimenFiscal", EmisoresCFDIS.class)
                .setParameter("cd_regimen", e.getCd_regimen())
                .getResultList();

        } catch (Exception ex) {
            log.error("Problema para buscar emisor por criterio", ex);
        } finally {
            EntityManagerUtil.close(em);
        }

        return listaEmisores;
    }
    
}
