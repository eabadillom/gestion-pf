package mx.com.ferbo.db;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

public abstract class DataSourceManager {

	protected static Logger log = Logger.getLogger(DataSourceManager.class);
	protected static DataSource ds = null;
	protected static DataSourceManager dsm = null;
	protected String jniName = null;

	protected static boolean logIsolationType = true;

	public void init(String jniName) {

		if (ds != null)
			return;

		Context initContext;

		try {
			initContext = new InitialContext();
			Context envContext = (Context) initContext.lookup("java:/comp/env");
			ds = (DataSource) envContext.lookup(jniName);
		} catch (NamingException ne) {
			try {
				initContext = new InitialContext();
				Context envContext = (Context) initContext.lookup("java:comp/env");
				ds = (DataSource) envContext.lookup(jniName);
			} catch (NamingException nexc) {
				try {
					initContext = new InitialContext();
					ds = (DataSource) initContext.lookup(jniName);
				} catch (NamingException exc) {
					ne.printStackTrace();
					exc.printStackTrace();
					throw new RuntimeException("No se encontro la fuente '"	+ jniName + "'");
				}
			}
		}
	}
	
	public static String getJniName(String name){
		InitialContext initContext = null;
		String jniName = null;
			
		try {
			initContext = new InitialContext();
			jniName = (String) initContext.lookup(name);
		}
		catch(NamingException ex) {
			log.error(ex);
		}
		
		return jniName;
	}
	
	public static String getJndiParameter(String name) {
//	    Context initContext = null;
		String parameter = null;
//	    try {
//            initContext = new InitialContext();
//            parameter = (String) initContext.lookup(name);
//            
//        } catch (NamingException ex) {
//            try {
//                Context envContext = (Context) initContext.lookup("java:/comp/env");
//                parameter = (String) envContext.lookup(name);
//            } catch(NamingException inEx) {
//                log.error("Problema para obtener el valor JNDI: " + name, inEx);
//            }
//        }
//	    
//	    return parameter;
		
		if("facturama/api".equals(name)) {
			parameter = "https://apisandbox.facturama.mx";
		} else if( ("facturama/user").equals(name) ) {
			parameter = "ebadillom";
		} else if ( "facturama/password".equals(name) ) {
			parameter = "xpafBdmFrb01";
		}
		
		return parameter;
	}
	
	public Connection getConnection() throws SQLException {

		boolean changed = false;
		Connection conn = ds.getConnection();
		
		String threadName = Thread.currentThread().getName();
		
		log.debug("Thread Name: " + threadName);
		
		if (logIsolationType)
			log.info("Defaults: Autocommit = "
					+ conn.getAutoCommit()
					+ ", TransactionIsolation = "
					+ transactionIsolationToString(conn
							.getTransactionIsolation()));
		
		if (conn.getAutoCommit() == true) {
			conn.setAutoCommit(false);
			changed = true;
			log.debug("Se establecio AutoCommit en False");
		}
		
		if (conn.getTransactionIsolation() != Connection.TRANSACTION_READ_COMMITTED) {
			conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			changed = true;
			log.debug("Se establecio TransactionIsolation en TRANSACTION_READ_COMMITED");
		}

		if (logIsolationType && changed) {
			logIsolationType = false;
			
			log.info("IRESA: Autocommit = "
					+ conn.getAutoCommit()
					+ ", TransactionIsolation = "
					+ transactionIsolationToString(conn
							.getTransactionIsolation()));
		}

		return conn;
	}

	public static Connection getConnection(String jndiName) throws SQLException {
		if (dsm == null)
			dsm = new DataSourceManager() {
			};

		if (ds == null)
			dsm.init(jndiName);
		
		return dsm.getConnection();
	}
	
	

	protected String transactionIsolationToString(int transactionType) {

		String s = "TRANSACTION_UNKNOW";

		switch (transactionType) {
		case Connection.TRANSACTION_READ_UNCOMMITTED:
			s = "TRANSACTION_READ_UNCOMMITTED";
			break;
		case Connection.TRANSACTION_READ_COMMITTED:
			s = "TRANSACTION_READ_COMMITTED";
			break;
		case Connection.TRANSACTION_REPEATABLE_READ:
			s = "TRANSACTION_REPEATABLE_READ";
			break;
		case Connection.TRANSACTION_SERIALIZABLE:
			s = "TRANSACTION_SERIALIZABLE";
			break;
		case Connection.TRANSACTION_NONE:
			s = "TRANSACTION_NONE";
			break;
		}

		return s;
	}
	
	/**Cierra un objeto de conexion a Base de Datos.
	 * @param conn Objeto de Conexion a Base de Datos.
	 */
	public static void close(Connection conn){
		try{
			if(conn != null){
				if(!conn.isClosed())
					conn.close();
			}
			conn = null;
			log.debug("El objeto Connection se cerro satisfactoriamente.");
		}catch(SQLException ex){
			log.error("Error al cerrar la conexion a BD.", ex);
		}
	}
	
	public static void close(Statement st){
		try{
			if(st != null)
				st.close();
			st = null;
			log.debug("El objeto Statement se cerro satisfactoriamente.");
		} catch(SQLException ex) {
			log.error("Error al cerrar un Statement: ", ex);
		}
	}
	
	/**Cierra un objeto de sentencia SQL PreparedStatement.
	 * @param ps Objeto PreparedStatement.
	 */
	public static void close(PreparedStatement ps){
		try{
			if(ps != null)
				ps.close();
			ps = null;
			log.debug("El objeto PreparedStatement se cerro satisfactoriamente.");
		} catch(SQLException ex) {
			log.error("Error al cerrar un PreparedStatement: ", ex);
		}
	}
	
	/**Cierra un objeto de sentencia SQL CallableStatement
	 * @param cs Objeto CallableStatement
	 */
	public static void close(CallableStatement cs){
		try{
			if(cs != null)
				cs.close();
			cs = null;
			log.debug("El objeto CallableStatement se cerro satisfactoriamente.");
		} catch(SQLException ex) {
			log.error("Error al cerrar un CallableStatement: ", ex);
		}
	}
	
	/**Cierra un objeto de datos ResultSet.
	 * @param rs Objeto ResultSet.
	 */
	public static void close(ResultSet rs){
		try{
			if(rs != null)
				rs.close();
			rs = null;
			log.debug("El objeto ResultSet se cerro satisfactoriamente");
		} catch(SQLException ex) {
			log.error("Error al cerrar un ResultSet: ", ex);
		}
	}
	
	public static void rollback(Connection conn) {
		try {
			if(conn != null)
				conn.rollback();
			log.debug("El objeto Connection realizo correctamente la operacion de Rollback.");
		} catch(SQLException ex) {
			log.error("Problemas para realizar la operacion de rollback.", ex);
		}
	}
}
