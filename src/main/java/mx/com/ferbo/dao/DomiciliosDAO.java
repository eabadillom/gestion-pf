package mx.com.ferbo.dao;

import java.util.List;

import javax.persistence.EntityManager;

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
		EntityManager em = EntityManagerUtil.getEntityManager();
		
		domicilio = em.createNamedQuery("Domicilios.findByAsentamiento", Domicilios.class)
				.setParameter("paisCve", idPais)
				.setParameter("estadoCve", idEstado)
				.setParameter("municipioCve", idMunicipio)
				.setParameter("ciudadCve", idCiudad)
				.setParameter("domicilioColonia", idAsentamiento).getSingleResult();
		
		return domicilio;
	}

	@Override
	public String actualizar(Domicilios dom) {
		// TODO Auto-generated method stub
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.createNativeQuery(
					"update DOMICILIOS set ciudad_cve = :ciudadCve, estado_cve = :estadoCve, municipio_cve = :municipioCve, pais_cve = :paisCve,"
							+ "domicilio_calle= :domicilioCalle, domicilio_colonia=:domicilioColonia, domicilio_cp=:domicilioCp, domicilio_fax=:domicilioFax"
							+ ", domicilio_num_ext = :domicilioNumExt , domicilio_num_int = :domicilioNumInt , domicilio_tel1 = :domicilioTel1,"
							+ "domicilio_tel2= :domicilioTel2, domicilio_tipo_cve = :domicilioTipoCve where dom_cve = :domCve")
					.setParameter("ciudadCve", dom.getCiudades().getCiudadesPK().getCiudadCve())
					.setParameter("estadoCve",
							dom.getCiudades().getMunicipios().getEstados().getEstadosPK().getEstadoCve())
					.setParameter("municipioCve", dom.getCiudades().getMunicipios().getMunicipiosPK().getMunicipioCve())
					.setParameter("paisCve", dom.getPaisCved().getPaisCve())
					.setParameter("domicilioCalle", dom.getDomicilioCalle())
					.setParameter("domicilioColonia", dom.getDomicilioColonia())
					.setParameter("domicilioCp", dom.getDomicilioCp())
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
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.persist(dom);
			em.getTransaction().commit();
			em.close();
		} catch (Exception e) {
			System.out.println("ERROR" + e.getMessage());
			return "ERROR";
		}
		return null;
	}

	@Override
	public String eliminar(Domicilios dom) {
		// TODO Auto-generated method stub
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.createNativeQuery("DELETE FROM DOMICILIOS WHERE (dom_cve = :domCve)")
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
