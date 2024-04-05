package mx.com.ferbo.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.AsentamientoHumano;
import mx.com.ferbo.model.Domicilios;
import mx.com.ferbo.util.EntityManagerUtil;

public class AsentamientoHumanoDAO extends IBaseDAO<AsentamientoHumano, Integer> {

	@Override
	public AsentamientoHumano buscarPorId(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AsentamientoHumano> buscarTodos() {
		List<AsentamientoHumano> listado;
		EntityManager em = EntityManagerUtil.getEntityManager();
		listado = em.createNamedQuery("AsentamientoHumano.findAll", AsentamientoHumano.class).getResultList();
		return listado;
	}

	@Override
	public List<AsentamientoHumano> buscarPorCriterios(AsentamientoHumano e) {
		// TODO Auto-generated method stub
		EntityManager em = EntityManagerUtil.getEntityManager();
		if (e.getAsentamientoHumanoPK().getCiudadCve() > 0) {
			TypedQuery<AsentamientoHumano> consEstados = em.createNamedQuery("AsentamientoHumano.findByCiudadCve",
					AsentamientoHumano.class);
			consEstados.setParameter("ciudadCve", e.getAsentamientoHumanoPK().getCiudadCve());
			List<AsentamientoHumano> listado = consEstados.getResultList();
			return listado;
		} else {
			if (e.getAsentamientoHumanoPK().getAsentamientoCve() > 0) {
				TypedQuery<AsentamientoHumano> consEstados = em
						.createNamedQuery("AsentamientoHumano.findByAsentamientoCve", AsentamientoHumano.class);
				consEstados.setParameter("ciudadCve", e.getAsentamientoHumanoPK().getCiudadCve());
				List<AsentamientoHumano> listado = consEstados.getResultList();
				return listado;
			}
			return null;
		}
	}

	@Override
	public String actualizar(AsentamientoHumano e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String guardar(AsentamientoHumano e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminar(AsentamientoHumano e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminarListado(List<AsentamientoHumano> listado) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<AsentamientoHumano> buscaPorCP(String codigo) {
		EntityManager em = EntityManagerUtil.getEntityManager();
		return em.createNamedQuery("AsentamientoHumano.findByCp", AsentamientoHumano.class).setParameter("cp", codigo)
				.getResultList();

	}

	public List<AsentamientoHumano> buscaPorDomicilio(Domicilios dom) {
		EntityManager em = EntityManagerUtil.getEntityManager();
		return em.createNamedQuery("AsentamientoHumano.findByDomicilio", AsentamientoHumano.class)
				.setParameter("paisCve", dom.getCiudades().getMunicipios().getEstados().getPaises().getPaisCve())
				.setParameter("estadoCve", dom.getCiudades().getMunicipios().getEstados().getEstadosPK().getEstadoCve())
				.setParameter("municipioCve", dom.getCiudades().getMunicipios().getMunicipiosPK().getMunicipioCve())
				.setParameter("ciudadCve", dom.getCiudades().getCiudadesPK().getCiudadCve()).getResultList();
	}
}
