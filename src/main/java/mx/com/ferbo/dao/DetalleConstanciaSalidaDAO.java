package mx.com.ferbo.dao;

import java.util.List;

import javax.persistence.EntityManager;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.ConstanciaDeDeposito;
import mx.com.ferbo.model.DetalleConstanciaSalida;
import mx.com.ferbo.model.Partida;
import mx.com.ferbo.util.EntityManagerUtil;

public class DetalleConstanciaSalidaDAO extends IBaseDAO<DetalleConstanciaSalida, Integer>{

	@Override
	public DetalleConstanciaSalida buscarPorId(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DetalleConstanciaSalida> buscarTodos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DetalleConstanciaSalida> buscarPorCriterios(DetalleConstanciaSalida e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String actualizar(DetalleConstanciaSalida e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String guardar(DetalleConstanciaSalida e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminar(DetalleConstanciaSalida e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminarListado(List<DetalleConstanciaSalida> listado) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public List<DetalleConstanciaSalida> buscarPorParams(Partida p, ConstanciaDeDeposito cDepSel) {
	EntityManager em = EntityManagerUtil.getEntityManager();		
		return em.createNamedQuery("DetalleConstanciaSalida.findByParams", DetalleConstanciaSalida.class)
				.setParameter("partidaCve", p.getPartidaCve().intValue())
				.setParameter("folioEntrada", cDepSel.getFolioCliente())
				.setParameter("producto", p.getUnidadDeProductoCve().getProductoCve().getProductoDs())
				.getResultList();		
	}

}
