package mx.com.ferbo.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.Aviso;
import mx.com.ferbo.model.PrecioServicio;
import mx.com.ferbo.util.EntityManagerUtil;

public class AvisoDAO extends IBaseDAO<Aviso, Integer> {

	private Logger log = LogManager.getLogger(AvisoDAO.class);

	@SuppressWarnings("unchecked")
	public List<Aviso> findall() {
		EntityManager em = null;
		List<Aviso> aviso = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			Query sql = em.createNamedQuery("Aviso.findAll", Aviso.class);
			aviso = sql.getResultList();
		} catch (Exception e) {
			log.error("Problemas para obtener informacion", e);
		} finally {
			EntityManagerUtil.close(em);
		}
		return aviso;
	}

	@Override
	public Aviso buscarPorId(Integer id) {
		Aviso aviso = null;
		EntityManager em = null;
		Query query = null;

		try {
			em = EntityManagerUtil.getEntityManager();
			query = em.createNamedQuery("Aviso.findByAvisoCve", Aviso.class).setParameter("avisoCve", id);
			aviso = (Aviso) query.getSingleResult();
		} catch (Exception ex) {
			log.error("Problema para obtener el aviso " + id, ex);
		} finally {
			EntityManagerUtil.close(em);
		}
		return aviso;
	}

	public Aviso buscarPorId(Integer id, boolean isFullInfo) {
		Aviso aviso = null;
		EntityManager em = null;

		try {
			em = EntityManagerUtil.getEntityManager();
			aviso = em.find(Aviso.class, id);

			if (isFullInfo == false)
				return aviso;
			log.debug(aviso.getPlantaCve().getPlantaCve());
			log.debug(aviso.getCteCve().getCteCve());
			log.debug(aviso.getCategoriaCve().getCategoriaCve());
			log.debug(aviso.getPrecioServicioList().size());
			for (PrecioServicio ps : aviso.getPrecioServicioList()) {
				log.debug("ID Precio Servicio: " + ps.getId());
			}

		} catch (Exception ex) {
			log.error("Problema para obtener el aviso " + id, ex);
		} finally {
			EntityManagerUtil.close(em);
		}
		return aviso;
	}

	@Override
	public List<Aviso> buscarTodos() {
		EntityManager em = null;
		List<Aviso> lista = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			lista = em.createNamedQuery("Aviso.findAll", Aviso.class).getResultList();
		} catch (Exception ex) {
			log.error("Problema para obtener el listado de avisos...", ex);
		} finally {
			EntityManagerUtil.close(em);
		}

		return lista;
	}

	@SuppressWarnings("unchecked")
	public List<Aviso> buscarTodos(boolean isFullInfo) {
		List<Aviso> lista = null;
		EntityManager em = null;
		Query query = null;

		try {
			em = EntityManagerUtil.getEntityManager();
			query = em.createNamedQuery("Aviso.findAll", Aviso.class);
			lista = query.getResultList();
			if (isFullInfo == false)
				return lista;

			for (Aviso aviso : lista) {
				aviso.getCategoriaCve().getCategoriaCve();
			}

		} catch (Exception ex) {
			log.error("Problema para obtener el listado de avisos...", ex);
		} finally {
			EntityManagerUtil.close(em);
		}

		return lista;
	}

	@Override
	public List<Aviso> buscarPorCriterios(Aviso e) {
		//  Auto-generated method stub
		return null;
	}

	@Override
	public String actualizar(Aviso aviso) {
		EntityManager em = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.merge(aviso);
			em.getTransaction().commit();
		} catch (Exception e) {
			System.out.println("ERROR" + e.getMessage());
			return "ERROR";
		} finally {
			EntityManagerUtil.close(em);
		}
		return null;
	}

	@Override
	public String guardar(Aviso e) {
		EntityManager em = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.createNativeQuery(
					"INSERT INTO aviso (aviso_cve, aviso_po, aviso_pedimento, aviso_sap,aviso_lote,aviso_caducidad,aviso_tarima,aviso_otro,aviso_temp,aviso_fecha,planta_cve,aviso_observaciones,cte_cve,categoria_cve,aviso_vigencia,aviso_val_seg,aviso_plazo,aviso_tp_facturacion) VALUES (:avisoCve, :avisoPo, :avisoPedimento, :avisoSap,:avisoLote,:avisoCaducidad,:avisoTarima,:avisoOtro,:avisoTemp,:avisoFecha,:plantaCve,:avisoObservaciones,:cteCve,:categoriaCve,:avisoVigencia,:avisoValSeg,:avisoPlazo,:avisoTpFacturacion)")
					.setParameter("avisoCve", e.getAvisoCve()).setParameter("avisoPo", e.getAvisoPo())
					.setParameter("avisoPedimento", e.getAvisoPedimento()).setParameter("avisoSap", e.getAvisoSap())
					.setParameter("avisoLote", e.getAvisoLote()).setParameter("avisoCaducidad", e.getAvisoCaducidad())
					.setParameter("avisoTarima", e.getAvisoTarima()).setParameter("avisoOtro", e.getAvisoOtro())
					.setParameter("avisoTemp", e.getAvisoTemp()).setParameter("avisoFecha", e.getAvisoFecha())
					.setParameter("plantaCve", e.getPlantaCve())
					.setParameter("avisoObservaciones", e.getAvisoObservaciones()).setParameter("cteCve", e.getCteCve())
					.setParameter("categoriaCve", e.getCategoriaCve())
					.setParameter("avisoVigencia", e.getAvisoVigencia()).setParameter("avisoValSeg", e.getAvisoValSeg())
					.setParameter("avisoPlazo", e.getAvisoPlazo())
					.setParameter("avisoTpFacturacion", e.getAvisoTpFacturacion()).executeUpdate();
			em.getTransaction().commit();
		} catch (Exception ex) {
			System.out.println("ERROR" + ex.getMessage());
			return "ERROR";
		} finally {
			EntityManagerUtil.close(em);
		}
		return null;
	}

	@Override
	public String eliminar(Aviso e) {
		EntityManager em = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.createNativeQuery("DELETE FROM aviso WHERE (aviso_cve = :avisoCve)")
					.setParameter("avisoCve", e.getAvisoCve()).executeUpdate();
			em.getTransaction().commit();
			em.close();
		} catch (Exception ex) {
			System.out.println("ERROR" + ex.getMessage());
			return "ERROR";
		} finally {
			EntityManagerUtil.close(em);
		}
		return null;
	}

	@Override
	public String eliminarListado(List<Aviso> listado) {
		//  Auto-generated method stub
		return null;
	}

	public List<Aviso> buscarPorCliente(Aviso a) {
		EntityManager em = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			return em.createNamedQuery("Aviso.findByAvisoCliente", Aviso.class)
					.setParameter("cteCve", a.getCteCve().getCteCve()).getResultList();
		} catch (Exception e) {
			log.error("Error al obtener informacion", e);
		} finally {
			EntityManagerUtil.close(em);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<Aviso> buscarPorCliente(Integer cteCve) {
		List<Aviso> lista = null;
		EntityManager em = null;
		Query query = null;

		try {
			em = EntityManagerUtil.getEntityManager();
			query = em.createNamedQuery("Aviso.findByAvisoCliente", Aviso.class).setParameter("cteCve", cteCve);
			lista = query.getResultList();
		} catch (Exception ex) {
			log.error("Problema en la consulta de los avisos del cliente " + cteCve, ex);
		} finally {
			EntityManagerUtil.close(em);
		}
		return lista;
	}

	public int conteoConstanciaDeDeposito(Aviso aviso) {

		EntityManager em = null;
		int count = 0;
		Query query = null;

		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			query = em.createNativeQuery(
					"SELECT COUNT(cdd.aviso_cve) FROM constancia_de_deposito cdd WHERE cdd.aviso_cve = :avisoCve");
			query.setParameter("avisoCve", aviso.getAvisoCve());
			count = Integer.parseInt(query.getSingleResult().toString());
			em.getTransaction().commit();
		} catch (Exception e) {
			log.error("Problema al contar registros", e);
		} finally {
			EntityManagerUtil.close(em);
		}
		return count;
	}

	public int conteoPrecioServicio(Aviso aviso) {

		EntityManager em = null;
		Query query = null;
		int count = 0;

		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			query = em.createNativeQuery(
					"SELECT COUNT(ps.aviso_cve) FROM precio_servicio ps  WHERE ps.aviso_cve = :avisoCve");
			query.setParameter("avisoCve", aviso.getAvisoCve());
			count = Integer.parseInt(query.getSingleResult().toString());
			em.getTransaction().commit();
		} catch (Exception e) {
			log.error("Problema al contar registros");
		} finally {
			EntityManagerUtil.close(em);
		}

		return count;
	}

}
