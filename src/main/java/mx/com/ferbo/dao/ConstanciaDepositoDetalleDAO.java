package mx.com.ferbo.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.ConstanciaDeDeposito;
import mx.com.ferbo.model.ConstanciaDepositoDetalle;
import mx.com.ferbo.util.EntityManagerUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConstanciaDepositoDetalleDAO extends IBaseDAO<ConstanciaDepositoDetalle, Integer> {
        private static Logger log = LogManager.getLogger(ConstanciaDepositoDetalleDAO.class);

	@Override
	public ConstanciaDepositoDetalle buscarPorId(Integer id) {
		return null;
	}

	@Override
	public List<ConstanciaDepositoDetalle> buscarTodos() {
            List<ConstanciaDepositoDetalle> listado = null;
            EntityManager em = null;
            try {
		em = EntityManagerUtil.getEntityManager();
                listado = em.createNamedQuery("ConstanciaDepositoDetalle.findAll", ConstanciaDepositoDetalle.class)
				.getResultList();
            } catch (Exception e) {
                log.error("Problema para obtener la constancia de deposito detalle... ", e);
            } finally {
                EntityManagerUtil.close(em);
            }
            return listado;
	}

	@Override
	public List<ConstanciaDepositoDetalle> buscarPorCriterios(ConstanciaDepositoDetalle e) {
		
		return null;
	}

	public List<ConstanciaDepositoDetalle> buscarPorFolio(ConstanciaDeDeposito constanciaDeDeposito) {

		List<ConstanciaDepositoDetalle> lista = new ArrayList<>();
                EntityManager em = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			lista = em.createNamedQuery("ConstanciaDepositoDetalle.findFolio", ConstanciaDepositoDetalle.class)
					.setParameter("folio", constanciaDeDeposito.getFolio()).getResultList();

		} catch (Exception e) {
                    log.error("Problema para obtener la constancia de deposito detalle... ", e);
		} finally {
                    EntityManagerUtil.close(em);
                }

		return lista;
	}

	@Override
	public String actualizar(ConstanciaDepositoDetalle e) {
		
		return null;
	}

	@Override
	public String guardar(ConstanciaDepositoDetalle constanciaDD) {
		EntityManager em = null;
                try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.persist(constanciaDD);
			em.getTransaction().commit();
		} catch (Exception e) {
			log.error("Problema al guardar la constancia de deposito detalle...", e);
			return "ERROR";
		} finally {
                    EntityManagerUtil.close(em);
                }
		return null;
	}

	@Override
	public String eliminar(ConstanciaDepositoDetalle constanciaDepositoDetalle) {
                EntityManager em = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			ConstanciaDepositoDetalle constancia = em.find(ConstanciaDepositoDetalle.class,
					constanciaDepositoDetalle.getConstanciaDepositoDetalleCve());
			em.remove(constancia);
			em.getTransaction().commit();
		} catch (Exception e) {
			log.error("Problema al eliminar la constancia de deposito detalle...", e);
			return "ERROR";
		} finally {
                    EntityManagerUtil.close(em);
                }

		return null;
	}

	@Override
	public String eliminarListado(List<ConstanciaDepositoDetalle> listado) {
		return null;
	}

}
