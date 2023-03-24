package mx.com.ferbo.utils;

import java.io.File;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Base64.Encoder;

import org.apache.log4j.Logger;

public class FileUtils {
	
	private static Logger log = Logger.getLogger(FileUtils.class);
	
	public static synchronized byte[] getBinContent(String fileName){
		byte[]                contenido = null;
		byte[]                buffer    = null;
		int                   length = 0;
		File                  f = null;
		FileInputStream       fis = null;
		DataInputStream       in = null;
		ByteArrayOutputStream out = null;;
		
		f = new File(fileName);
		buffer = new byte[4 * 1024]; // 4K buffer
		
		try {
			log.debug("Comenzando la lectura binaria del archivo " + fileName);
			fis = new FileInputStream(f);
			
			log.debug("Archivo abierto correctamente: " + fileName);
			in = new DataInputStream(fis);
			out = new ByteArrayOutputStream();
			
			log.debug("Se inicia lectura del archivo en bloques de 4k: " + fileName);
			while ((in != null) && ((length = in.read(buffer)) != -1)) {
				out.write(buffer, 0, length);
			}
			
			contenido = out.toByteArray();
			log.debug("Terminando la lectura binaria del archivo " + fileName);
			
		} catch (FileNotFoundException ex) {
			log.error(ex);
		} catch (IOException ex) {
			log.error(ex);
		} finally {
			close(fis);
			close(in);
			close(out);
		}
		
		return contenido;
	}
	
	public static synchronized void close(DataInputStream in){
		try {
			if(in != null)
				in.close();
			log.debug("Objeto DataInputStream cerrado satisfactoriamente.");
		} catch (IOException ex) {
			log.error("Error al cerrar el objeto DataInputStream", ex);
		} finally {
			in = null;
		}
	}
	
	public static synchronized void close(FileInputStream in){
		try {
			if(in != null)
				in.close();
			log.debug("Objeto FileInputStream cerrado satisfactoriamente.");
		} catch (IOException ex) {
			log.error("Error al cerrar el objeto FileInputStream", ex);
		} finally {
			in = null;
		}
	}
	
	public static synchronized void close(ByteArrayOutputStream out){
		try{
			if(out != null)
				out.close();
			log.debug("Objeto ByteArrayOutputStream cerrado satisfactoriamente.");
		} catch (IOException ex) {
			log.error("Error al cerrar el objeto ByteArrayOutputStream", ex);
		} finally {
			out = null;
		}
	}
	
	
	
	public static synchronized String getBase64String(byte[] bytes) {
		String sOutput = null;
		Encoder encoder =  Base64.getEncoder();
		sOutput = encoder.encodeToString(bytes);
		return sOutput;
	}
}