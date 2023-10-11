package mx.com.ferbo.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.Factura;
import mx.com.ferbo.model.StatusFactura;
import mx.com.ferbo.util.EntityManagerUtil;

public class StatusFacturaDAO extends IBaseDAO<StatusFactura, Integer>{
	private static Logger log = LogManager.getLogger(StatusFacturaDAO.class);
	@Override
	public StatusFactura buscarPorId(Integer id) {
		EntityManager entity = null;
		StatusFactura sf = null;
		try {
			entity = EntityManagerUtil.getEntityManager();
			sf = entity.createNamedQuery("StatusFactura.findById", StatusFactura.class)
					.setParameter("id",id).getSingleResult();
		} catch(Exception ex) {
			log.error("Problema al buscar el status factura...", ex);
		} finally {
			EntityManagerUtil.close(entity);
		}
		
		return sf;
	}
	public List<StatusFactura> buscarPorCliente(Cliente cte, StatusFactura sf){
		EntityManager entity = EntityManagerUtil.getEntityManager();
		List<Factura> listaFactura = new ArrayList<>();
		List<StatusFactura> listaStatusFactura = new ArrayList<>();
		listaFactura = entity.createNamedQuery("Factura.findByCliente.", Factura.class)
				.setParameter("cliente",cte.getCteCve()).setParameter("id", sf.getId()).getResultList();
		entity.close();
		return listaStatusFactura;
		
	}

	@Override
	public List<StatusFactura> buscarTodos() {
		// TODO Auto-generated method stub
		EntityManager em = EntityManagerUtil.getEntityManager();
		return em.createNamedQuery("StatusFactura.findAll", StatusFactura.class).getResultList();
		}

	@Override
	public List<StatusFactura> buscarPorCriterios(StatusFactura e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String actualizar(StatusFactura e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String guardar(StatusFactura e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminar(StatusFactura e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminarListado(List<StatusFactura> listado) {
		// TODO Auto-generated method stub
		return null;
	}

}
