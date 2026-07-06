package mx.com.ferbo.dao;

import java.util.List;

import javax.persistence.EntityManager;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.CuotaMinima;
import mx.com.ferbo.util.EntityManagerUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CuotaMinimaDAO extends IBaseDAO<CuotaMinima, Integer> {
        private static Logger log = LogManager.getLogger(CuotaMinimaDAO.class);
        
	@Override
	public CuotaMinima buscarPorId(Integer id) {
		return null;
	}

	@Override
	public List<CuotaMinima> buscarTodos() {
            List<CuotaMinima> listado = null;
            EntityManager em = null;
            try {    
                em = EntityManagerUtil.getEntityManager();
		listado = em.createNamedQuery("CuotaMinima.findAll", CuotaMinima.class).getResultList();
            } catch (Exception e) {
                log.error("Problema al buscar la lista de detalle de constancia de salida...", e);
            } finally {
                EntityManagerUtil.close(em);
            }
            return listado;
	}

	@Override
	public List<CuotaMinima> buscarPorCriterios(CuotaMinima e) {
		if (e.getCliente().getCteCve() != null) {
			return this.buscarPorCliente(e);
		}
		return null;
	}

	@Override
	public String actualizar(CuotaMinima e) {
		EntityManager em = null;
                try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.createNativeQuery("UPDATE cuota_minima SET cuota_value = :cuotaValue WHERE (cte_cve = :cteCve)")
					.setParameter("cuotaValue", e.getCuotaValue()).setParameter("cteCve", e.getCliente().getCteCve())
					.executeUpdate();
			em.getTransaction().commit();
			em.close();
		} catch (Exception ex) {
			log.error("Error al actualizar la cuota minima... ", ex);
			return "ERROR";
		} finally {
                        EntityManagerUtil.close(em);
                }
		return null;
	}

	@Override
	public String guardar(CuotaMinima e) {
		EntityManager em = null;
                try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			int cuotaId = this.buscarTodos().size() - 1;
			em.createNativeQuery(
					"INSERT INTO cuota_minima (cte_cve, cuota_id, cuota_enabled, cuota_value) VALUES (:cteCve,:cuotaId, b'1', :cuotaValue)")
					.setParameter("cteCve", e.getCliente().getCteCve()).setParameter("cuotaId", cuotaId + 1)
					.setParameter("cuotaValue", e.getCuotaValue()).executeUpdate();
			em.getTransaction().commit();
			em.close();
		} catch (Exception ex) {
			log.error("Error al guardar la cuota minima...", ex);
			return "ERROR";
		} finally {
                        EntityManagerUtil.close(em);
                }

		return null;
	}

	@Override
	public String eliminar(CuotaMinima e) {
		EntityManager em = null;
                try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.createNativeQuery("DELETE FROM cuota_minima WHERE (cte_cve = :cteCve)")
					.setParameter("cteCve", e.getCliente().getCteCve()).executeUpdate();
			em.getTransaction().commit();
			em.close();
		} catch (Exception ex) {
			log.error("Error al eliminar una cuota minima... ", ex);
			return "ERROR";
		} finally {
                        EntityManagerUtil.close(em);
                }
		return null;
	}

	@Override
	public String eliminarListado(List<CuotaMinima> listado) {
		return null;
	}

	private List<CuotaMinima> buscarPorCliente(CuotaMinima c) {
	    List<CuotaMinima> listCuotaMinima = null;
            EntityManager em = null;
            try {
                em = EntityManagerUtil.getEntityManager();
		listCuotaMinima = em.createNamedQuery("CuotaMinima.findByCteCve", CuotaMinima.class)
                    .setParameter("cteCve", c.getCliente().getCteCve())
                    .getResultList();
            } catch (Exception ex) {
                log.error("Error al obtener la lista de cuota minima... ", ex);
            } finally {
                EntityManagerUtil.close(em);
            }
            return listCuotaMinima;
	}

	public int cuentaRegistros(CuotaMinima e) {
	    Integer totalCuotaMinima = null;
            EntityManager em = null;
            try {
                em = EntityManagerUtil.getEntityManager();
		Number result = (Number) em.createNativeQuery("SELECT count(cuota_id) FROM cuota_minima").getSingleResult();
                totalCuotaMinima = result.intValue();
            } catch (Exception ex) {
                log.error("Error al obtener el total de cuota minima... ", ex);
            } finally {
                EntityManagerUtil.close(em);
            }
            return totalCuotaMinima;
	}

}
