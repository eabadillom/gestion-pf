package mx.com.ferbo.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**Esta clase maneja la carga de archivos con extensión .properties. Sus métodos se
 * pueden heredar para que otras clases también tengan la capacidad de invocación
 * a archivos .properties y cargar en un objeto {@link Properties} o {@link Map}
 * las propiedades que sean necesarias para la lectura de los registros ubicados
 * en los archivos .properties.
 * @author Esteban Antonio Badillo Martínez
 * @version 1.0
 */
public abstract class LoadProperties{
	
	private static final String LOG_NAME = "com.bancomer.scrittura.Default";
	
	private static Logger log = Logger.getLogger(LoadProperties.class);
	
	/**Carga el archivo de propiedades filename
	 * @param filename Nombre del archivo de configuracion
	 * @return {@link Properties} Configuracion leida
	 * @throws MigracionException Excepcion enviada en caso de error
	 */
	public static Properties readConfiguration(String filename)
	throws FileNotFoundException, IOException {

		Properties prop = new Properties();
		FileInputStream fis = null;
		
		try {
			fis = new FileInputStream(filename);
			prop.load(fis);
		} catch (FileNotFoundException exc) {
			throw exc;
		} catch (IOException exc) {
			throw exc;
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException ex) {
					log.error(ex);
				}
			}
		}

		return prop;
	}

	/**Metodo para cargar en un HashMap un conjunto de propiedades
	 * del archivo Properties de Configuración para esta clase (DateUtil).
	 * @param mapName Prefijo del property a cargar.
	 * @param prop {@link Properties} Objeto con las opciones de configuración del .properties.
	 * @return {@link Map} HashMap con las propiedades de configuración.
	 */
	  public static Map loadMappers(String mapName, Properties prop) {

		Map map = new Hashtable();
		String propName;
		String keyName;

		Enumeration em = prop.keys();
		while (em.hasMoreElements()) {
			propName = (String) em.nextElement();
			if (!propName.startsWith(mapName)) {
				continue;
			}

			keyName = propName.substring(mapName.length() + 1);
			map.put(keyName, prop.getProperty(propName));
		}

		return map;
	}
	  
	  /**Devuelve el Logger configurado
	   * @return {@link Logger}
	   */
	  public static Logger logger() {
	    if (log == null) {
	      Properties prop = new Properties();
	      prop.put("log4j.rootLogger", "debug, console");
	      prop.put("log4j.appender.console", "org.apache.log4j.ConsoleAppender");
	      prop.put("log4j.appender.console.layout", "org.apache.log4j.SimpleLayout");

	      log = inicializaLogger(prop, LOG_NAME);
	    }

	    return log;
	  }
	  
	  private static Logger inicializaLogger(Properties config, String name) {
		    PropertyConfigurator.configure(config);
		    return Logger.getLogger(name);
		  }
}