package mx.com.ferbo.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.IngresoProducto;
import mx.com.ferbo.model.Producto;
import mx.com.ferbo.util.EntityManagerUtil;

public class IngresoProductoDAO extends IBaseDAO<IngresoProducto, Integer> {

	private static Logger log = LogManager.getLogger(FacturaDAO.class);

	@Override
	public IngresoProducto buscarPorId(Integer id) {

		return null;
	}

	@SuppressWarnings("unchecked")
	public List<IngresoProducto> buscarIngresosProductoPorId(Integer idIngreso) {

		List<IngresoProducto> listaIngresoProducto = new ArrayList<IngresoProducto>();
		EntityManager em = null;
		String query = null;

		try {

			em = EntityManagerUtil.getEntityManager();

			query = "SELECT ip.* FROM ingreso_producto ip WHERE ip.id_ingreso = ? ";

			Query q = em.createNativeQuery(query, IngresoProducto.class).setParameter(1, idIngreso);

			listaIngresoProducto = q.getResultList();

			for (IngresoProducto ip : listaIngresoProducto) {

				Producto prod = ip.getProducto();
				System.out.println(prod);

			}

		} catch (Exception e) {
			log.info("Error al encontrar Ingresos Productos por idIngreso" + e.getMessage());
		}

		return listaIngresoProducto;
	}

	@Override
	public List<IngresoProducto> buscarTodos() {

		return null;
	}

	@Override
	public List<IngresoProducto> buscarPorCriterios(IngresoProducto e) {
		return null;
	}

	@Override
	public String actualizar(IngresoProducto e) {
		return null;
	}

	@Override
	public String guardar(IngresoProducto e) {
		return null;
	}

	@Override
	public String eliminar(IngresoProducto e) {
		return null;
	}

	@Override
	public String eliminarListado(List<IngresoProducto> listado) {
		return null;
	}

}
