package mx.com.ferbo.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.log4j.Logger;

import mx.com.ferbo.ui.RepEstadoCuenta;
import mx.com.ferbo.util.EntityManagerUtil;

public class RepEstadoCuentaDAO {
private static Logger log = Logger.getLogger(RepEstadoCuentaDAO.class);
	
	@SuppressWarnings("unchecked")
	public List<RepEstadoCuenta> listaEstadoCuenta( Date fecha, String ventas, String pagos, String saldoInicial, String emisor){
		EntityManager em = null;
		List<RepEstadoCuenta> listaEstadoCuenta = null;
		String sql = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			
			listaEstadoCuenta = new ArrayList<RepEstadoCuenta>();
			
		} catch (Exception e) {
			
		}
		
		
		return listaEstadoCuenta;
		
	}
	
}
