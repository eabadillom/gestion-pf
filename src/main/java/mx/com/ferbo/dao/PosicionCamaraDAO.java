package mx.com.ferbo.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import mx.com.ferbo.util.EntityManagerUtil;
import mx.com.ferbo.util.JPAEntity;
import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.Camara;
import mx.com.ferbo.model.Planta;

import mx.com.ferbo.util.JPAEntity;
import mx.com.ferbo.model.Posicion;

public class PosicionCamaraDAO extends IBaseDAO<Posicion, Integer>{
	
	EntityManager entity = JPAEntity.getEntity().createEntityManager();
	
	@SuppressWarnings("unchecked")
	public List<Posicion> findAll(){
		List<Posicion> posiciones;
		Query sql = entity.createNamedQuery("Posicion.findAll", Posicion.class);
		posiciones = sql.getResultList();
		System.out.println(posiciones + "*****************************************************");
		return posiciones;
	}
	
	public String save(Posicion sP) {
		try {
			EntityManager entity = EntityManagerUtil.getEntityManager();
			entity.getTransaction().begin();
			entity.persist(sP);
			entity.getTransaction().commit();
			entity.close();
		} catch (Exception e) {
			return e.getMessage();
		}
		return null;
	}

	@Override
	public Posicion buscarPorId(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<Posicion> buscarPorCamara(Camara c) {
	List<Posicion> listaP = null;
	EntityManager em = EntityManagerUtil.getEntityManager();
	Query qr = entity.createNamedQuery("SELECT * FROM posicion where id_camara = :idCamara ")
			.setParameter("id_camara", c.getCamaraCve());
	listaP = qr.getResultList();
	return listaP;
	
	}
	@Override
	public List<Posicion> buscarTodos() {
		List<Posicion> posiciones = new ArrayList<Posicion>();
		//Query sql = entity.createNamedQuery("Posicion.findAll", Posicion.class);
//		try {
//		EntityManager entity = EntityManagerUtil.getEntityManager();
//		entity.getTransaction().begin();
//		Query qr = entity.createNamedQuery(" SELECT * FROM gestiondb.posicion  ");
//		List<Object[]> resultQuery = qr.getResultList();
//		System.out.println(resultQuery + "--------------------------------------");
//		}catch (Exception e) {
//			// TODO: handle exception
//		}
		//posiciones = sql.getResultList();
		System.out.println(posiciones + "*****************************************************");
		return posiciones;
	}

	@Override
	public List<Posicion> buscarPorCriterios(Posicion e) {
		List<Posicion> posiciones = new ArrayList<Posicion>();
		//Query sql = entity.createNamedQuery("Posicion.findAll", Posicion.class);
		try {
		EntityManager entity = EntityManagerUtil.getEntityManager();
		entity.getTransaction().begin();
		Query qr = entity.createNamedQuery(" SELECT * FROM posicion where id_planta = :idPlanta and id_camara = :idCamara ").setParameter("id_planta", e.getPlanta().getPlantaCve()).setParameter("id_camara", e.getCamara().getCamaraCve());
		List<Object[]> resultQuery = qr.getResultList();
		System.out.println(resultQuery + "--------------------------------------");
		}catch (Exception eBp) {
			// TODO: handle exception
		}
		return posiciones;
	}

	@SuppressWarnings("unused")
	@Override
	public String actualizar(Posicion e) {
		System.out.println(e + "ACTUALIZAR+++++++++++++++++++++++++++++++++++++++++++++++++++++");
		try {
			EntityManager entity = EntityManagerUtil.getEntityManager();
			entity.getTransaction().begin();
			Query actualizar = entity.createNativeQuery(" UPDATE posicion SET habilitada  = :habil WHERE id_posicion = :pos ") ;
			actualizar.setParameter("habil", (e.getHabilitada() == true) ? 1 : 0);
			actualizar.setParameter("pos", e.getIdPosicion());
			actualizar.executeUpdate();
			entity.getTransaction().commit();
			entity.close();
		} catch (Exception e2) {
			return e2.getMessage();
		}
		return null;
	}

	@Override
	public String guardar(Posicion sP) {
		System.out.println(sP + "GURADAR---------------------------------");
		try {
			EntityManager entity = EntityManagerUtil.getEntityManager();
			entity.getTransaction().begin();
			//entity.persist(sP);
			Query otra = entity.createNativeQuery(" insert into posicion(id_planta, id_camara, cod_posicion, desc_posicion, temp_ini, temp_fin, habilitada) "
					+ "values(?,?,?,?,?,?,?); "); 
			otra.setParameter(1, sP.getPlanta().getPlantaCve());
			otra.setParameter(2, sP.getCamara().getCamaraCve());
			otra.setParameter(3, sP.getCodPosicion());
			otra.setParameter(4, sP.getDescPosicion());
			otra.setParameter(5, sP.getTempIni());
			otra.setParameter(6, sP.getTempFin());
			otra.setParameter(7, sP.getHabilitada());
			otra.executeUpdate();
			entity.getTransaction().commit();
			entity.close();
		} catch (Exception e) {
			return e.getMessage();
		}
		return null;
	}

	@Override
	public String eliminar(Posicion e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminarListado(List<Posicion> listado) {
		// TODO Auto-generated method stub
		return null;
	};
	
	 
}