package mx.com.ferbo.business;

import java.io.IOException;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import mx.com.ferbo.response.BiometricoResponse;
import mx.com.ferbo.util.InventarioException;

public class BiometricoSgpApiBL extends AbstractSgpApiBL {
	private static Logger log = LogManager.getLogger(BiometricoSgpApiBL.class);
	
	public BiometricoSgpApiBL() {
		super();
	}
	
	public BiometricoResponse getBiometrico(String numeroEmpleado) throws InventarioException {
		BiometricoResponse respuesta = null;
		
		String sURL = null;
		HttpGet request = null;
        CloseableHttpResponse response = null;
        
        Gson prettyGson   = null;
        String jsonResponse = null;
        
        int httpStatus = -1;
		
		try {
			sURL = basePath + String.format("/sgp-api/gestion/%s/biometrico", numeroEmpleado);
			request = this.createGetRequest(sURL);
			response = httpClient.execute(request);
			httpStatus = response.getCode();
			
			//¿La solicitud está fuera del rango 200?
            if(httpStatus < 200 || httpStatus >= 300)
            	throw new InventarioException("Respuesta no satisfactoria del SGP-API.");
			
            //La solicitud si está en el rango 200
            jsonResponse = this.getResponseBody(response);
            
            prettyGson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'hh:mm:ss").create();
            respuesta = prettyGson.fromJson(jsonResponse, BiometricoResponse.class);
			
            log.info("Respuesta del SGP-API correcta.");
		} catch(InventarioException ex) {
			log.error("Se presentó un problema en la comunicación con Facturama...", ex);
        	String message = this.getErrorMessage(response);
            throw new InventarioException(message);
		} catch(IOException ex) {
			log.error("Se presentó un problema en la comunicación con el SGP-API...", ex);
			throw new InventarioException(ex);
		}
		
		return respuesta;
	}
}
