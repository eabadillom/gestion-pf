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

		EntityManager em = EntityManagerUtil.getEntityManager();
		List<DetalleConstanciaSalida> listado = null;

		listado = em.createNamedQuery("DetalleConstanciaSalida.findAll", DetalleConstanciaSalida.class).getResultList();

		return listado;
	}

	@Override
	public List<DetalleConstanciaSalida> buscarPorCriterios(DetalleConstanciaSalida e) {
		return null;
	}

	public List<DetalleConstanciaSalida> buscarPorPartidaCve(Partida partida) {

		EntityManager em = EntityManagerUtil.getEntityManager();
		List<DetalleConstanciaSalida> lista = null;
		lista = em.createNamedQuery("DetalleConstanciaSalida.findByPartidaCve", DetalleConstanciaSalida.class)
				.setParameter("partidaCve", partida.getPartidaCve()).getResultList();

		return lista;
	}

	public List<DetalleConstanciaSalida> buscarPorPartidaCve(Partida partida, boolean isFullInfo) {

		EntityManager em = EntityManagerUtil.getEntityManager();
		List<DetalleConstanciaSalida> lista = null;
		lista = em.createNamedQuery("DetalleConstanciaSalida.findByPartidaCve", DetalleConstanciaSalida.class)
				.setParameter("partidaCve", partida.getPartidaCve()).getResultList();
		if (isFullInfo == false)
			return lista;

		for (DetalleConstanciaSalida dcs : lista) {
			log.debug("PartidaCve: {}", dcs.getPartidaCve().getPartidaCve());
		}

		return lista;
	}

	@Override
	public String actualizar(DetalleConstanciaSalida e) {
		return null;
	}

	@Override
	public String guardar(DetalleConstanciaSalida detalleConstanciaSalida) {
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.persist(detalleConstanciaSalida);
			em.getTransaction().commit();
			em.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return "ERROR";
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
		EntityManager em = EntityManagerUtil.getEntityManager();
		return em.createNamedQuery("DetalleConstanciaSalida.findByParams", DetalleConstanciaSalida.class)
				.setParameter("partidaCve", p.getPartidaCve().intValue())
				.setParameter("folioEntrada", cDepSel.getFolioCliente())
				.setParameter("producto", p.getUnidadDeProductoCve().getProductoCve().getProductoDs()).getResultList();
	}

}
