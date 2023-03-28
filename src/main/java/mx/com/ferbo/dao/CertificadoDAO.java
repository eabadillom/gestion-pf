package mx.com.ferbo.dao;

import java.util.List;

import javax.persistence.EntityManager;
import static mx.com.ferbo.util.EntityManagerUtil.getEntityManager;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.AsentamientoHumano;
import mx.com.ferbo.model.TipoAsentamiento;
import mx.com.ferbo.util.EntityManagerUtil;


import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.Certificado;

public class CertificadoDAO extends IBaseDAO<Certificado, Integer>{

		@SuppressWarnings("unchecked")
		public List<Certificado> findAll(){
			EntityManager entity = getEntityManager();
			List<Certificado> certi = null;
			Query sql = entity.createNamedQuery("Certificado.findAll", Certificado.class);
			certi = sql.getResultList();
			return certi;
			
		}
	@Override
	public Certificado buscarPorId(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Certificado> buscarTodos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Certificado> buscarPorCriterios(Certificado e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String actualizar(Certificado e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String guardar(Certificado e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminar(Certificado e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminarListado(List<Certificado> listado) {
		// TODO Auto-generated method stub
		return null;
	}

}
