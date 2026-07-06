package mx.com.ferbo.dao;

import java.util.List;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.ConstanciaDeDeposito;
import mx.com.ferbo.model.DetalleConstanciaSalida;
import mx.com.ferbo.model.Partida;
import mx.com.ferbo.util.EntityManagerUtil;

public class DetalleConstanciaSalidaDAO extends IBaseDAO<DetalleConstanciaSalida, Integer> {
	private static Logger log = LogManager.getLogger(DetalleConstanciaSalidaDAO.class);

	@Override
	public DetalleConstanciaSalida buscarPorId(Integer id) {
		return null;
	}

	@Override
	public List<DetalleConstanciaSalida> buscarTodos() {
            List<DetalleConstanciaSalida> listado = null;
            EntityManager em = null;
            try {
                em = EntityManagerUtil.getEntityManager();

		listado = em.createNamedQuery("DetalleConstanciaSalida.findAll", DetalleConstanciaSalida.class)
                    .getResultList();
            } catch (Exception e) {
                log.error("Problema al buscar la lista de detalle de constancia de salida...", e);
            } finally {
                EntityManagerUtil.close(em);
            }
            return listado;
	}

	@Override
	public List<DetalleConstanciaSalida> buscarPorCriterios(DetalleConstanciaSalida e) {
		return null;
	}

	public List<DetalleConstanciaSalida> buscarPorPartidaCve(Partida partida) {
            List<DetalleConstanciaSalida> lista = null;
            EntityManager em = null;
            try {
                em = EntityManagerUtil.getEntityManager();
                lista = em.createNamedQuery("DetalleConstanciaSalida.findByPartidaCve", DetalleConstanciaSalida.class)
				.setParameter("partidaCve", partida.getPartidaCve()).getResultList();
            } catch (Exception e) {
                log.error("Problema al buscar la lista de detalle de constancia de salida...", e);
            } finally {
                EntityManagerUtil.close(em);
            }
            return lista;
	}

	public List<DetalleConstanciaSalida> buscarPorPartidaCve(Partida partida, boolean isFullInfo) {
            List<DetalleConstanciaSalida> lista = null;
            EntityManager em = null;
            try {
                em = EntityManagerUtil.getEntityManager();
		lista = em.createNamedQuery("DetalleConstanciaSalida.findByPartidaCve", DetalleConstanciaSalida.class)
				.setParameter("partidaCve", partida.getPartidaCve()).getResultList();
		if (isFullInfo == false)
			return lista;

		for (DetalleConstanciaSalida dcs : lista) {
			log.debug("PartidaCve: {}", dcs.getPartidaCve().getPartidaCve());
		}
            } catch (Exception e) {
                log.error("Problema al buscar la lista de detalle de constancia de salida...", e);
            } finally {
                EntityManagerUtil.close(em);
            }
            return lista;
	}

	@Override
	public String actualizar(DetalleConstanciaSalida e) {
		return null;
	}

	@Override
	public String guardar(DetalleConstanciaSalida detalleConstanciaSalida) {
		EntityManager em = null;
                try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.persist(detalleConstanciaSalida);
			em.getTransaction().commit();
			em.close();
		} catch (Exception e) {
			log.error("Error al guardar el detalle de constancia salida...", e);
			return "ERROR";
		} finally {
                    EntityManagerUtil.close(em);
                }
		return null;
	}

	@Override
	public String eliminar(DetalleConstanciaSalida e) {
		return null;
	}

	@Override
	public String eliminarListado(List<DetalleConstanciaSalida> listado) {
		return null;
	}

	public List<DetalleConstanciaSalida> buscarPorParams(Partida p, ConstanciaDeDeposito cDepSel) {
            List<DetalleConstanciaSalida> lista = null;
            EntityManager em = null;
            try {
                em = EntityManagerUtil.getEntityManager();
		lista = em.createNamedQuery("DetalleConstanciaSalida.findByParams", DetalleConstanciaSalida.class)
                    .setParameter("partidaCve", p.getPartidaCve().intValue())
                    .setParameter("folioEntrada", cDepSel.getFolioCliente())
                    .setParameter("producto", p.getUnidadDeProductoCve().getProductoCve().getProductoDs())
                    .getResultList();
            } catch (Exception e) {
                log.error("Problema al eliminar la lista de detalle de constancia de salida...", e);
            } finally {
                EntityManagerUtil.close(em);
            }
            return lista;
	}

}
