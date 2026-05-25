package mx.com.ferbo.client;

import com.google.gson.Gson;
import java.io.IOException;
import java.util.Map;
import mx.com.ferbo.response.ModelException;
import mx.com.ferbo.util.DataSourceManager;
import org.apache.hc.client5.http.classic.methods.HttpDelete;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author alberto
 */
public abstract class GestionApiClient 
{
    private static Logger log = LogManager.getLogger(GestionApiClient.class);
    
    protected String basePath = null;
    
    protected CloseableHttpClient httpClient = null;

    public GestionApiClient() {
        basePath = DataSourceManager.getJndiParameter("gestion/api");
        
        httpClient = HttpClients.createDefault();
    }
    
    protected String getResponseBody(CloseableHttpResponse response) 
    {
    	String bodyResponse = null;
        HttpEntity entity = null;
        String resultContent;
        
        try {
            entity = response.getEntity();
            resultContent = EntityUtils.toString(entity);
            bodyResponse = new String(resultContent.getBytes(), "UTF-8");
        } catch (ParseException | IOException e) {
            log.error("Problema para obtener la respueta del servlet...", e);
        }
        
        return bodyResponse;
    }
    
    protected String getErrorMessage(CloseableHttpResponse response) 
    {
    	Gson                  gson = null;
    	String                jsonResponse = null;
    	ModelException        failMessage = null;
        Map<String, String[]> mpModelState = null;
        StringBuilder         sbMessage = new StringBuilder();

        jsonResponse = this.getResponseBody(response);
        log.error("Error en servlet: {}",jsonResponse);
        gson = new Gson();
        if(jsonResponse == null || "".equalsIgnoreCase(jsonResponse.trim()))
            return "No hay mensaje de respuesta del servlet.";
        
        failMessage = gson.fromJson(jsonResponse, ModelException.class);
        if(failMessage == null)
            return "No hay mensaje de respuesta del servlet.";
        
        sbMessage.append(failMessage.getMessage());
        sbMessage.append("\r\n");
        mpModelState = failMessage.getModelState();
        
        for(Map.Entry<String, String[]> entry : mpModelState.entrySet()) {
            String[] value = entry.getValue();
            for(String msg : value) {
                    sbMessage.append(msg);
                    sbMessage.append("\r\n");
            }
        }
        return sbMessage.toString();
    }
    
    protected HttpGet createGetRequest(String url) {
    	HttpGet request = null;
    	request = new HttpGet(url);
    	return request;
    }
    
    protected HttpPost createPostRequest(String url) {
    	HttpPost request = null;
    	request = new HttpPost(url);
    	return request;
    }
    
    protected HttpDelete createDeleteRequest(String url) {
    	HttpDelete request = null;
    	request = new HttpDelete(url);
        return request;
    }
    
}
