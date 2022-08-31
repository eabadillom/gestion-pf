package mx.com.ferbo.dao;

import java.util.List;

import javax.persistence.EntityManager;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.Aviso;
import mx.com.ferbo.util.EntityManagerUtil;

public class AvisoDAO extends IBaseDAO<Aviso, Integer>{

	@Override
	public Aviso buscarPorId(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Aviso> buscarTodos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Aviso> buscarPorCriterios(Aviso e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String actualizar(Aviso e) {
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.createNativeQuery(
					"UPDATE aviso SET aviso_po = :avisoPo , aviso_pedimento = :avisoPedimento, aviso_sap = :avisoSap, aviso_lote = :avisoLote,aviso_caducidad = :avisoCaducidad,aviso_tarima = :avisoTarima,aviso_otro = :avisoOtro,planta_cve = :plantaCve,categoria_cve = :categoriaCve WHERE (cte_cve = :cteCve) and (aviso_cve = :avisoCve)") 
			.setParameter("avisoCve",e.getAvisoCve())
			.setParameter("avisoPo",e.getAvisoPo())
			.setParameter("avisoPedimento",e.getAvisoPedimento())
			.setParameter("avisoSap",e.getAvisoSap())
			.setParameter("avisoLote",e.getAvisoLote())
			.setParameter("avisoCaducidad",e.getAvisoCaducidad())
			.setParameter("avisoTarima",e.getAvisoTarima())
			.setParameter("avisoOtro",e.getAvisoOtro())
			.setParameter("plantaCve",e.getPlantaCve())
			.setParameter("cteCve",e.getCteCve())
			.setParameter("categoriaCve",e.getCategoriaCve())
					.executeUpdate();
			em.getTransaction().commit();
			em.close();
		} catch (Exception ex) {
			System.out.println("ERROR" + ex.getMessage());
			return "ERROR";
		}
		return null;
	}

	@Override
	public String guardar(Aviso e) {
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.createNativeQuery("INSERT INTO aviso (aviso_cve, aviso_po, aviso_pedimento, aviso_sap,aviso_lote,aviso_caducidad,aviso_tarima,aviso_otro,aviso_temp,aviso_fecha,planta_cve,aviso_observaciones,cte_cve,categoria_cve,aviso_vigencia,aviso_val_seg,aviso_plazo,aviso_tp_facturacion) VALUES (:avisoCve, b:avisoPo, :avisoPedimento, :avisoSap,:avisoLote,:avisoCaducidad,:avisoTarima,:avisoOtro,:avisoTemp,:avisoFecha,:plantaCve,:avisoObservaciones,:cteCve,:categoriaCve,:avisoVigencia,:avisoValSeg,:avisoPlazo,:avisoTpFacturacion)")
			.setParameter("avisoCve",e.getAvisoCve())
			.setParameter("avisoPo",e.getAvisoPo())
			.setParameter("avisoPedimento",e.getAvisoPedimento())
			.setParameter("avisoSap",e.getAvisoSap())
			.setParameter("avisoLote",e.getAvisoLote())
			.setParameter("avisoCaducidad",e.getAvisoCaducidad())
			.setParameter("avisoTarima",e.getAvisoTarima())
			.setParameter("avisoOtro",e.getAvisoOtro())
			.setParameter("avisoTemp",e.getAvisoTemp())
			.setParameter("avisoFecha",e.getAvisoFecha())
			.setParameter("plantaCve",e.getPlantaCve())
			.setParameter("avisoObservaciones",e.getAvisoObservaciones())
			.setParameter("cteCve",e.getCteCve())
			.setParameter("categoriaCve",e.getCategoriaCve())
			.setParameter("avisoVigencia",e.getAvisoVigencia())
			.setParameter("avisoValSeg",e.getAvisoValSeg())
			.setParameter("avisoPlazo",e.getAvisoPlazo())
			.setParameter("avisoTpFacturacion",e.getAvisoTpFacturacion())
			.executeUpdate();
			em.getTransaction().commit();
			em.close();
		} catch (Exception ex) {
			System.out.println("ERROR" + ex.getMessage());
			return "ERROR";
		}
		return null;
	}

	@Override
	public String eliminar(Aviso e) {
		try {
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.createNativeQuery("DELETE FROM aviso WHERE (aviso_cve = :avisoCve)")
					.setParameter("avisoCve", e.getAvisoCve()).executeUpdate();
			em.getTransaction().commit();
			em.close();
		} catch (Exception ex) {
			System.out.println("ERROR" + ex.getMessage());
			return "ERROR";
		}
		return null;
	}
	
	@Override
	public String eliminarListado(List<Aviso> listado) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public List<Aviso> buscarPorCliente(Aviso e){
		EntityManager em = EntityManagerUtil.getEntityManager();
		return em.createNamedQuery("Aviso.findByAvisoCliente", Aviso.class)
				.setParameter("cteCve", e.getCteCve().getCteCve())
				.getResultList();	
		}

}
