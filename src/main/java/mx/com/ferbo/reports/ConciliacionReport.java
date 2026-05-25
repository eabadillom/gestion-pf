package mx.com.ferbo.reports;

import java.util.Date;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.util.InventarioException;
import mx.com.ferbo.utils.FileUtils;

public class ConciliacionReport extends ReportBL {
	
	private static Logger log = LogManager.getLogger(ConciliacionReport.class);
	
	private static final String LOGO_PATH  = "/images/logoF.png";
	private static final String JRXML_PATH = "/jasper/reportConciliacion.jrxml";
	
	public static synchronized byte[] getPDF(Date periodoInicio, Date periodoFin, Integer idCliente) {
		byte[] bytes = null;
		
		Map<String, Object> parameters = null;
		String logoPath = null;
		String jrxmlPath = null;
		
		try {
			logoPath = FileUtils.getFullPath(LOGO_PATH)
					.orElseThrow(() -> new InventarioException("La ruta al logo es incorrecta."));
			
			jrxmlPath = FileUtils.getFullPath(JRXML_PATH)
					.orElseThrow(() -> new InventarioException("La ruta al archivo del reporte de conciliación es incorrecta."));
			
			parameters = getParameters();
			parameters.put("idCliente", idCliente );
			parameters.put("fechaInicio", periodoInicio);
			parameters.put("fechaFin", periodoFin);
			parameters.put("imagen", logoPath);
			
			bytes = createPDF(parameters, jrxmlPath);
			
		} catch(Exception ex) {
			log.error("Problema para generar el reporte de conciliación...", ex);
		} finally {
			close(parameters);
		}
		
		return bytes;
	}
	
	public static synchronized byte[] getXLSX(Date periodoInicio, Date periodoFin, Integer idCliente) {
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
			parameters.put("idCliente", idCliente );
			parameters.put("fechaInicio", periodoInicio);
			parameters.put("fechaFin", periodoFin);
			parameters.put("imagen", logoPath);
			
			bytes = createXLSX(parameters, jrxmlPath);
			
		} catch(Exception ex) {
			log.error("Problema para generar el ticket de la constancia de depósito...", ex);
		} finally {
			close(parameters);
		}
	
		return bytes;
	}

}
