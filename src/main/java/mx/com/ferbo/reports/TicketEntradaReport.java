package mx.com.ferbo.reports;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.util.InventarioException;
import mx.com.ferbo.utils.FileUtils;

public class TicketEntradaReport extends ReportBL {
	
	private static Logger log = LogManager.getLogger(TicketEntradaReport.class);
	
	private static final String LOGO_PATH  = "/images/logo.jpeg";
	private static final String JRXML_PATH = "/jasper/GestionReport.jrxml";
	
	public static synchronized byte[] getPDF(String folioCliente) {
		byte[] bytes = null;
		
		Map<String, Object> parameters = null;
		String logoPath = null;
		String jrxmlPath = null;
		
		try {
			logoPath = FileUtils.getFullPath(LOGO_PATH)
					.orElseThrow(() -> new InventarioException("La ruta al logo es incorrecta."));
			
			jrxmlPath = FileUtils.getFullPath(JRXML_PATH)
					.orElseThrow(() -> new InventarioException("La ruta al archivo del ticket es incorrecta."));
			
			parameters = getParameters();
			parameters.put("FOLIO", folioCliente);
			parameters.put("LogoPath", logoPath);
			
			bytes = createPDF(parameters, jrxmlPath);
			
		} catch(Exception ex) {
			log.error("Problema para generar el ticket de la constancia de depósito...", ex);
		} finally {
			close(parameters);
		}
		
		return bytes;
	}
	
	public static synchronized byte[] getXLSX(String folioCliente) {
		byte[] bytes = null;
		Map<String, Object> parameters = null;
		String logoPath = null;
		String jrxmlPath = null;
		
		try {
			logoPath = FileUtils.getFullPath(LOGO_PATH)
					.orElseThrow(() -> new InventarioException("La ruta al logo es incorrecta."));
			
			jrxmlPath = FileUtils.getFullPath(JRXML_PATH)
					.orElseThrow(() -> new InventarioException("La ruta al archivo del ticket es incorrecta."));
			
			parameters = getParameters();
			parameters.put("FOLIO", folioCliente);
			parameters.put("LogoPath", logoPath);
			
			bytes = createPDF(parameters, jrxmlPath);
			
		} catch(Exception ex) {
			log.error("Problema para generar el ticket de la constancia de depósito...", ex);
		} finally {
			close(parameters);
		}
	
		return bytes;
	}
	
	
}
