package mx.com.ferbo.business;

import java.io.IOException;
import mx.com.ferbo.client.GestionApiClient;
import mx.com.ferbo.util.InventarioException;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author alberto
 */
public class GestionApiClientBL extends GestionApiClient
{
    private static Logger log = LogManager.getLogger(GestionApiClientBL.class);

    public GestionApiClientBL() 
    {
        super();
    }
    
    public void enviarCorreoCliente(String numeroCliente) throws InventarioException
    {
        String sURL = null;
        
        HttpPost request = null;
        CloseableHttpResponse response = null;
        
        int httpStatus = -1;
        try {
            sURL = basePath + String.format("/gestion-cron/reporte-inventario?numeroCliente=%s", numeroCliente);
            sURL = String.format(sURL);
            
            request = this.createPostRequest(sURL);
            response = httpClient.execute(request);
            httpStatus = response.getCode();
            
            if(httpStatus < 200 || httpStatus >= 300)
            	throw new InventarioException("Respuesta no satisfactoria del envio del correo.");
            
            log.info("Se envió la petición del correo al cliente correctamente.");
        } catch(IOException ex){
            log.error("Se presentó un problema en la comunicación con el servlet...", ex);
            throw new InventarioException("Ha ocurrido un error en el sistema, por favor comuniquese con su administrador del sistema.");
        }
    }
    
}
