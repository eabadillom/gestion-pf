package mx.com.ferbo.db;

/**
 * @author Esteban
 *
 */
public class ConnectionBD extends DataSourceManager {
	public ConnectionBD(String jniName)
	{
		super.init(jniName);
	}

}
