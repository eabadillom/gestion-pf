package mx.com.ferbo.dao;

import java.util.List;

import javax.persistence.EntityManager;

import org.jfree.util.Log;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.Domicilios;
import mx.com.ferbo.util.EntityManagerUtil;

public class DomiciliosDAO extends IBaseDAO<Domicilios, Integer> {

	@Override
	public Domicilios buscarPorId(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Domicilios> buscarTodos() {
		List<Domicilios> listado;
		EntityManager em = EntityManagerUtil.getEntityManager();
		listado = em.createNamedQuery("Domicilios.findAll", Domicilios.class).getResultList();
		return listado;
	}

	@Override
	public List<Domicilios> buscarPorCriterios(Domicilios e) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Domicilios buscarPorAsentamiento(Integer idPais, Integer idEstado, Integer idMunicipio, Integer idCiudad, Integer idAsentamiento) {
		Domicilios domicilio = null;
		EntityManager em = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			domicilio = em.createNamedQuery("Domicilios.findByAsentamiento", Domicilios.class)
					.setParameter("paisCve", idPais)
					.setParameter("estadoCve", idEstado)
					.setParameter("municipioCve", idMunicipio)
					.setParameter("ciudadCve", idCiudad)
					.setParameter("asentamientoCve", idAsentamiento)
                            .getSingleResult();
			
		} catch (Exception e) {
			Log.error("Problema al encontrar domicilio", e);
		}finally {
			EntityManagerUtil.close(em);
		}
		
		return domicilio;
	}

	@Override
	public String actualizar(Domicilios dom) {
		// TODO Auto-generated method stub
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.createNativeQuery(
					"update domicilios set asentamiento_cve = :asentamientoCve, ciudad_cve = :ciudadCve, estado_cve = :estadoCve, municipio_cve = :municipioCve, pais_cve = :paisCve,"
                                            + "domicilio_calle = :domicilioCalle, domicilio_fax = :domicilioFax, domicilio_num_ext = :domicilioNumExt, domicilio_num_int = :domicilioNumInt, "
                                            + "domicilio_tel1 = :domicilioTel1, domicilio_tel2= :domicilioTel2, domicilio_tipo_cve = :domicilioTipoCve where dom_cve = :domCve")
					.setParameter("asentamientoCve", dom.getAsentamiento().getAsentamientoHumanoPK().getAsentamientoCve())
                                        .setParameter("ciudadCve", dom.getAsentamiento().getAsentamientoHumanoPK().getCiudades().getCiudadesPK().getCiudadCve())
                                        .setParameter("municipioCve", dom.getAsentamiento().getAsentamientoHumanoPK().getCiudades().getCiudadesPK().getMunicipios().getMunicipiosPK().getMunicipioCve())
                                        .setParameter("estadoCve",dom.getAsentamiento().getAsentamientoHumanoPK().getCiudades().getCiudadesPK().getMunicipios().getMunicipiosPK().getEstados().getEstadosPK().getEstadoCve())
					.setParameter("paisCve", dom.getAsentamiento().getAsentamientoHumanoPK().getCiudades().getCiudadesPK().getMunicipios().getMunicipiosPK().getEstados().getEstadosPK().getPais().getPaisCve())
					.setParameter("domicilioCalle", dom.getDomicilioCalle())
					.setParameter("domicilioFax", dom.getDomicilioFax())
					.setParameter("domicilioNumExt", dom.getDomicilioNumExt())
					.setParameter("domicilioNumInt", dom.getDomicilioNumInt())
					.setParameter("domicilioTel1", dom.getDomicilioTel1())
					.setParameter("domicilioTel2", dom.getDomicilioTel2())
					.setParameter("domicilioTipoCve", dom.getDomicilioTipoCve().getDomicilioTipoCve())
					.setParameter("domCve", dom.getDomCve()).executeUpdate();
			em.getTransaction().commit();
			em.close();
		} catch (Exception e) {
			System.out.println("ERROR" + e.getMessage());
			return "ERROR";
		}
		return null;
	}

	@Override
	public String guardar(Domicilios dom) {
		EntityManager em = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.persist(dom);
			em.getTransaction().commit();
			
		} catch (Exception e) {
			System.out.println("ERROR" + e.getMessage());
			return "ERROR";
		}finally {
			EntityManagerUtil.close(em);
		}
		return null;
	}

	@Override
	public String eliminar(Domicilios dom) {
		// TODO Auto-generated method stub
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.createNativeQuery("DELETE FROM domicilios WHERE (dom_cve = :domCve)")
					.setParameter("domCve", dom.getDomCve()).executeUpdate();
			em.getTransaction().commit();
			em.close();
		} catch (Exception e) {
			System.out.println("ERROR" + e.getMessage());
			return "ERROR";
		}
		return null;
	}

	@Override
	public String eliminarListado(List<Domicilios> listado) {
		// TODO Auto-generated method stub
		return null;
	}

}
