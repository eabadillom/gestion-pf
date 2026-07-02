package mx.com.ferbo.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.Certificado;
import mx.com.ferbo.util.EntityManagerUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CertificadoDAO extends IBaseDAO<Certificado, Integer> {
        private static Logger log = LogManager.getLogger(CertificadoDAO.class);

	@SuppressWarnings("unchecked")
	public List<Certificado> findAll() {
            EntityManager entity = null;
            List<Certificado> certi = null;
            try {
                entity = EntityManagerUtil.getEntityManager();
		Query sql = entity.createNamedQuery("Certificado.findAll", Certificado.class);
		certi = sql.getResultList();
            } catch(Exception ex) {
                    log.warn("Problema para obtener el listado de certificados...", ex);
            } finally {
                EntityManagerUtil.close(entity);
            }
            return certi;
	}

	@Override
	public Certificado buscarPorId(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	public Certificado buscarporFecha() {
            Certificado dtCertificado = null;
            EntityManager entity = null;
            try {
                entity = EntityManagerUtil.getEntityManager();
		
		Query sql = entity.createNamedQuery("Certificado.findByFecha", Certificado.class);
		dtCertificado = (Certificado) sql.getSingleResult();
            } catch(Exception ex) {
                log.warn("Problema para obtener información del certificado...", ex);
            } finally {
                EntityManagerUtil.close(entity);
            }
            return dtCertificado;
	}

	public List<Certificado> buscarporcdEmisor(Integer emisor) {
            List<Certificado> listaCertificado = null;
            EntityManager entity = null;
            try {    
                entity = EntityManagerUtil.getEntityManager();
		Query sql = entity.createNamedQuery("Certificado.findByemisor", Certificado.class)
                    .setParameter("emisor", emisor);
		listaCertificado = sql.getResultList();
            } catch(Exception ex) {
                    log.warn("Problema para obtener el listado de certificados...", ex);
            } finally {
                EntityManagerUtil.close(entity);
            }   
            return listaCertificado;
	}

	@Override
	public List<Certificado> buscarTodos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Certificado> buscarPorCriterios(Certificado e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String actualizar(Certificado c) {
                EntityManager em = null;
                try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.merge(c);
			em.getTransaction().commit();
		} catch (Exception e) {
			log.error("Error al actualizar el certificado... ", e);
			return "ERROR";
		} finally {
                    EntityManagerUtil.close(em);
                }
		return null;
	}

	@Override
	public String guardar(Certificado e) {
		EntityManager em = null;
                try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.persist(e);
			em.getTransaction().commit();
		} catch (Exception ex) {
			log.error("Error al guardar el certificado", ex);
			return "ERROR";
		} finally {
                    EntityManagerUtil.close(em);
                }
		return null;
	}

	@Override
	public String eliminar(Certificado e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminarListado(List<Certificado> listado) {
		// TODO Auto-generated method stub
		return null;
	}

}
