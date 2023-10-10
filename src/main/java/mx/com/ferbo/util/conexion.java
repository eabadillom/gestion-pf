package mx.com.ferbo.util;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.activation.DataSource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class conexion {
	private static Logger logger = LogManager.getLogger(conexion.class);

	public static synchronized Connection dsConexion() throws SQLException {
		Connection conn = null;
		Context initContext = null;
		Context envContext = null;
		DataSource ds = null;

		try {

			try {
				initContext = (Context) new InitialContext();
				ds = (DataSource) ((InitialContext) initContext).lookup("jdbc/iresa");
			} catch (NameNotFoundException inEx) {
				envContext = (Context) ((InitialContext) initContext).lookup("java:/comp/env");
				ds = (DataSource) ((InitialContext) envContext).lookup("jdbc/iresa");
			} catch (NamingException e) {
				logger.error(e.getLocalizedMessage(), e);
			}

			if (ds != null) {
				conn = ((conexion) ds).getConnection();
				conn.setAutoCommit(false);
			}
		} catch (NamingException e) {
			logger.error(e.getLocalizedMessage(), e);
		}

		return conn;
	}

	public static Connection getConnection() throws SQLException {
		Connection con = null;
		con = dsConexion();
		return con;
	}

	@Deprecated
	public static Properties getProperties() {
		String user = null;
		String password = null;
		String driver = null;
		String dburl = null;
		Properties pr = new Properties();
		ResourceBundle paramBD = ResourceBundle.getBundle("com.mx.clientes.util.Conexion");

		user = paramBD.getString("user");
		password = paramBD.getString("password");
		driver = paramBD.getString("driver");
		dburl = paramBD.getString("dburl");

		pr.setProperty("user", user);
		pr.setProperty("password", password);
		pr.setProperty("driver", driver);
		pr.setProperty("dburl", dburl);

		pr.put("user", user);
		pr.put("password", password);

		return pr;
	}

	public static void close(ResultSet resultset) {
		try {
			if (resultset != null)
				resultset.close();
			resultset = null;
			logger.debug("El objeto ResultSet se cerro satisfactoriamente");
		} catch (SQLException ex) {
			logger.error("Error al cerrar un ResultSet: ", ex);
		}
	}

	public static void close(Statement statement) {
		try {
			if (statement != null)
				statement.close();
			statement = null;
			logger.debug("El objeto Statement se cerro satisfactoriamente.");
		} catch (SQLException ex) {
			logger.error("Error al cerrar un Statement: ", ex);
		}
	}

	public static void close(PreparedStatement statement) {
		try {
			if (statement != null)
				statement.close();
			statement = null;
			logger.debug("El objeto PreparedStatement se cerro satisfactoriamente.");
		} catch (SQLException ex) {
			logger.error("Error al cerrar un PreparedStatement: ", ex);
		}
	}

	public static synchronized void close(Connection connection) {
		try {
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
		} catch (SQLException ex) {
			logger.error("Problema al cerrar el objeto Connection.", ex);
		} catch (Exception ex) {
			logger.error("Problema general al cerrar el objeto Connection.", ex);
		} finally {
			connection = null;
		}
	}

	public static void rollback(Connection conn) {
		try {
			if (conn != null)
				conn.rollback();
		} catch (SQLException ex) {
			logger.error("Problema al realizar rollback de la conexion.", ex);
		}
	}

}
