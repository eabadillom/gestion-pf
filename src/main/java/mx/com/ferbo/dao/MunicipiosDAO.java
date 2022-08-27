package mx.com.ferbo.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.AsentamientoHumano;
//import mx.com.ferbo.model.Estados;
import mx.com.ferbo.model.Municipios;
import mx.com.ferbo.util.EntityManagerUtil;

public class MunicipiosDAO extends IBaseDAO<Municipios, Integer> {

	@Override
	public Municipios buscarPorId(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Municipios> buscarTodos() {
		List<Municipios> listado = null;
		EntityManager em = EntityManagerUtil.getEntityManager();
		listado = em.createNamedQuery("Municipios.findAll", Municipios.class).getResultList();
		return listado;
	}

	public List<Municipios> buscarPorCriteriosMunicipios(Municipios e) {
		List<Municipios> listado = null;
		EntityManager em = EntityManagerUtil.getEntityManager();
		listado = em.createNamedQuery("Municipios.findByPaisCveEstadoCve", Municipios.class).setParameter("estadoCve", e.getMunicipiosPK().getEstadoCve()).setParameter("paisCve", e.getMunicipiosPK().getPaisCve()).getResultList();
		return listado;
	}

	@Override
	public List<Municipios> buscarPorCriterios(Municipios e) {
		List<Municipios> listado = null;
		EntityManager em = EntityManagerUtil.getEntityManager();
		if (e.getEstados().getEstadosPK().getEstadoCve() > 0) {
			TypedQuery<Municipios> consEstados = em.createNamedQuery("Municipios.findByEstadoCve", Municipios.class);
			consEstados.setParameter("estadoCve", e.getEstados().getEstadosPK().getEstadoCve());
			listado = consEstados.getResultList();
			return listado;
		} else if(e.getMunicipiosPK().getEstadoCve() != -1 && e.getMunicipiosPK().getPaisCve() != -1){
			listado = em.createNamedQuery("Municipios.findByPaisCveEstadoCve", Municipios.class).setParameter("estadoCve", e.getMunicipiosPK().getEstadoCve()).setParameter("paisCve", e.getMunicipiosPK().getPaisCve()).getResultList();
			return listado;
		} else {
			return null;
		}
	}

	@Override
	public String actualizar(Municipios municipios) {
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.merge(municipios);
			em.getTransaction().commit();
			em.close();
		} catch (Exception e) {
			System.out.println("ERROR guardando Municipio" + e.getMessage());
			return "ERROR";
		}
		return null;
	}

	@Override
	public String guardar(Municipios municipios) {
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.persist(municipios);
			em.getTransaction().commit();
			em.close();
		} catch (Exception e) {
			System.out.println("ERROR guardando Municipio" + e.getMessage());
			return "ERROR";
		}
		return null;
	}

	@Override
	public String eliminar(Municipios municipios) {
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
//			em.remove(em.merge(municipios));
//			DELETE FROM `gestiondb`.`municipios` WHERE (`pais_cve` = '3') and (`estado_cve` = '9') and (`municipio_cve` = '18');
			em.createQuery("DELETE FROM Municipios m WHERE m.municipiosPK.paisCve =:paisCve and m.municipiosPK.estadoCve =:estadoCve and m.municipiosPK.municipioCve =:municipioCve")
			.setParameter("paisCve", municipios.getMunicipiosPK().getPaisCve())
			.setParameter("estadoCve", municipios.getMunicipiosPK().getEstadoCve())
			.setParameter("municipioCve", municipios.getMunicipiosPK().getMunicipioCve()).executeUpdate();
			em.getTransaction().commit();
			em.close();
		} catch (Exception e) {
			System.out.println("ERROR" + e.getMessage());
			return "ERROR";
		}
		return null;
	}

	@Override
	public String eliminarListado(List<Municipios> listado) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Municipios> buscaPorId(Integer id) {
		EntityManager em = EntityManagerUtil.getEntityManager();
		return em.createNamedQuery("Municipios.findByMunicipioCve", Municipios.class)
				.setParameter("municipioCve", id).getResultList();
	}
	
	public List<Municipios> buscaPorAsentamiento(AsentamientoHumano as) {
		EntityManager em = EntityManagerUtil.getEntityManager();
		return em.createNamedQuery("Municipios.findByTodo", Municipios.class)
				.setParameter("municipioCve", as.getAsentamientoHumanoPK().getMunicipioCve())
				.setParameter("estadoCve", as.getAsentamientoHumanoPK().getEstadoCve())
				.setParameter("paisCve", as.getAsentamientoHumanoPK().getPaisCve())
				.getResultList();
	}

}