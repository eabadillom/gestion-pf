package mx.com.ferbo.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.ConstanciaDeDeposito;
import mx.com.ferbo.model.Partida;
import mx.com.ferbo.util.EntityManagerUtil;

public class PartidaDAO extends IBaseDAO<Partida, Integer>{

	@Override
	public Partida buscarPorId(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Partida> buscarTodos() {
		EntityManager em = EntityManagerUtil.getEntityManager();
		return em.createNamedQuery("Partida.findAll", Partida.class).getResultList();
	}

	@Override
	public List<Partida> buscarPorCriterios(Partida e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String actualizar(Partida e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String guardar(Partida e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminar(Partida e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminarListado(List<Partida> listado) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public List<Partida> buscarPorConstanciaDeposito(ConstanciaDeDeposito cons){
		EntityManager em = EntityManagerUtil.getEntityManager();
		List<Partida> buscaPartidas = new ArrayList<>();
		buscaPartidas=em.createNamedQuery("Partida.findByConstanciaDeDeposito",Partida.class)
				.setParameter("folio", cons.getFolio()).getResultList();
		return buscaPartidas;

		
	}

}
