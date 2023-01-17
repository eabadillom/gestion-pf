package mx.com.ferbo.facturacion.facturama;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import mx.com.ferbo.db.DataSourceManager;
import mx.com.ferbo.facturacion.facturama.response.BranchOfficeViewModel;
import mx.com.ferbo.facturacion.facturama.response.CfdiInfoModel;
import mx.com.ferbo.facturacion.facturama.response.ClientModelRsp;
import mx.com.ferbo.facturacion.facturama.response.FileViewModel;
import mx.com.ferbo.facturacion.facturama.response.ProductRsp;
import mx.com.ferbo.utils.IOUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class FacturamaBL {
    private static Logger log = Logger.getLogger(FacturamaBL.class);
    
    public FacturamaBL() {
//        File cacertsFile = null;
//        log.info("Estableciendo archivo trustStore...");
//        cacertsFile = new File( getClass().getResource("/resources/cacerts").getFile() );
//        if(cacertsFile.exists() == false)
//            log.warn("El archivo " + cacertsFile.getAbsolutePath() + " no se encuentra.");
//        
//        System.setProperty("javax.net.ssl.trustStore",cacertsFile.getPath());
//        System.setProperty("javax.net.ssl.trustStorePassword","changeit");
        this.infoTrustStorePath();
    }
    
    public void infoTrustStorePath() {
        log.info("java.net.ssl.trustStore: " + System.getProperty("javax.net.ssl.trustStore"));
    }
    
    public List<BranchOfficeViewModel> getSucursales() throws IOException {
        List<BranchOfficeViewModel> beans = null;
        String charset = "UTF-8";
        String basePath = null;
        String user = null;
        String password = null;
        String auth = null;
        byte[] encodedAuth = null;
        String authHeaderValue = null;
        String sURL = null;
        
        URL url = null;
        HttpURLConnection httpConn = null;
        OutputStream output = null;
        InputStream input = null;
        
        try {
            this.infoTrustStorePath();
            log.info("Solicitando información a Facturama...");
            basePath = DataSourceManager.getJndiParameter("facturama/api");
            user = DataSourceManager.getJndiParameter("facturama/user");
            password = DataSourceManager.getJndiParameter("facturama/password");
            
            
            auth = String.format("%s:%s", user, password);
            encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.UTF_8));
            authHeaderValue = "Basic " + new String(encodedAuth);
            
            sURL = basePath + "/api/BranchOffice";
            sURL = String.format(sURL);
            
            url = new URL(sURL);
            httpConn = (HttpURLConnection) url.openConnection();
            log.info("URL solicitada para sucursales: " + sURL);
            httpConn.setRequestProperty("Accept-Charset", charset);
            httpConn.setRequestProperty("Authorization", authHeaderValue);
            httpConn.setRequestMethod("GET");
            httpConn.setDoOutput(false);
            httpConn.setDoInput(true);
            
            input = new BufferedInputStream(httpConn.getInputStream());
            
            String jsonResponse = IOUtils.toString(input, "UTF-8");
            
            log.info("Respuesta de la API Facturama:\n" + jsonResponse);
            
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<BranchOfficeViewModel>>(){}.getType();
            
            beans = gson.fromJson(jsonResponse, listType);
        } finally {
            IOUtil.close(input);
            IOUtil.close(output);
            httpConn.disconnect();
        }
        
        return beans;
    }
    
    public BranchOfficeViewModel getSucursal(String uuid) throws IOException {
        BranchOfficeViewModel bean;
        
        String charset = "UTF-8";
        String basePath = null;
        String user = null;
        String password = null;
        String auth = null;
        byte[] encodedAuth = null;
        String authHeaderValue = null;
        String sURL = null;
        
        URL url = null;
        HttpURLConnection httpConn = null;
        OutputStream output = null;
        InputStream input = null;
        
        try {
            this.infoTrustStorePath();
            log.info("Solicitando información a Facturama...");
            basePath = DataSourceManager.getJndiParameter("facturama/api");
            user = DataSourceManager.getJndiParameter("facturama/user");
            password = DataSourceManager.getJndiParameter("facturama/password");
            
            auth = String.format("%s:%s", user, password);
            encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.UTF_8));
            authHeaderValue = "Basic " + new String(encodedAuth);
            
            sURL = String.format(basePath + "/api/BranchOffice/%s", uuid);
            
            url = new URL(sURL);
            httpConn = (HttpURLConnection) url.openConnection();
            log.info("URL solicitada para sucursales: " + sURL);
            httpConn.setRequestProperty("Accept-Charset", charset);
            httpConn.setRequestProperty("Authorization", authHeaderValue);
            httpConn.setRequestMethod("GET");
            httpConn.setDoOutput(false);
            httpConn.setDoInput(true);
            
            input = new BufferedInputStream(httpConn.getInputStream());
            
            String jsonResponse = IOUtils.toString(input, "UTF-8");
            
            log.info("Respuesta de la API Facturama:\n" + jsonResponse);
            
            Gson gson = new Gson();
            bean = gson.fromJson(jsonResponse, BranchOfficeViewModel.class);
            
        } finally {
            IOUtil.close(input);
            IOUtil.close(output);
            httpConn.disconnect();
        }
        
        return bean;
    }
    
    public BranchOfficeViewModel registra(BranchOffice sucursal) {
        BranchOfficeViewModel respuesta = null;
        
        Gson prettyGson   = null;
        String json = null;
        
        String user = null;
        String password = null;
        String basePath = null;
        String sURL = null;
        String auth = null;
        byte[] encodedAuth = null;
        String authHeaderValue = null;
        
        URL url = null;
        HttpURLConnection conn = null;
        OutputStream output = null;
        byte[] bytes = null;
        
        InputStream input = null;
        
        try {
            
            prettyGson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd HH:mm:ss")
                    .setPrettyPrinting().create();
            json = prettyGson.toJson(sucursal);
            
            log.info("JSON BranchOfficeViewModel Facturama: " + json);
            
            /*------*/
            basePath = DataSourceManager.getJndiParameter("facturama/api");
            user = DataSourceManager.getJndiParameter("facturama/user");
            password = DataSourceManager.getJndiParameter("facturama/password");
            
            auth = String.format("%s:%s", user, password);
            encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.UTF_8));
            authHeaderValue = "Basic " + new String(encodedAuth);
            
            sURL = basePath + "/api/BranchOffice";
            sURL = String.format(sURL);
            
            url = new URL(sURL);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Authorization", authHeaderValue);
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            bytes = json.getBytes("utf-8");
            output = conn.getOutputStream();
            output.write(bytes);
            output.close();
            
            input = new BufferedInputStream(conn.getInputStream());
            String result = IOUtils.toString(input, "UTF-8");
            log.info("Respuesta de la API Facturama:\n" + result);
            Gson gson = new Gson();
            respuesta = gson.fromJson(result,  BranchOfficeViewModel.class);
            
        } catch(Exception ex) {
            log.error("Problema para registrar el cliente en Facturama...", ex);
        } finally {
            IOUtil.close(input);
            IOUtil.close(output);
            conn.disconnect();
        }
        
        return respuesta;
    }
    
    public BranchOfficeViewModel deleteSucursal(String uuid) throws IOException {
        BranchOfficeViewModel bean;
        
        String charset = "UTF-8";
        String basePath = null;
        String user = null;
        String password = null;
        String auth = null;
        byte[] encodedAuth = null;
        String authHeaderValue = null;
        String sURL = null;
        
        URL url = null;
        HttpURLConnection httpConn = null;
        OutputStream output = null;
        InputStream input = null;
        
        try {
            this.infoTrustStorePath();
            log.info("Solicitando información a Facturama...");
            basePath = DataSourceManager.getJndiParameter("facturama/api");
            user = DataSourceManager.getJndiParameter("facturama/user");
            password = DataSourceManager.getJndiParameter("facturama/password");
            
            
            auth = String.format("%s:%s", user, password);
            encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.UTF_8));
            authHeaderValue = "Basic " + new String(encodedAuth);
            
            sURL = String.format(basePath + "/api/BranchOffice/%s", uuid);
            
            url = new URL(sURL);
            httpConn = (HttpURLConnection) url.openConnection();
            log.info("URL solicitada para sucursales: " + sURL);
            httpConn.setRequestProperty("Accept-Charset", charset);
            httpConn.setRequestProperty("Authorization", authHeaderValue);
            httpConn.setRequestMethod("DELETE");
            httpConn.setDoOutput(false);
            httpConn.setDoInput(true);
            
            input = new BufferedInputStream(httpConn.getInputStream());
            
            String jsonResponse = IOUtils.toString(input, "UTF-8");
            
            log.info("Respuesta de la API Facturama:\n" + jsonResponse);
            
            Gson gson = new Gson();
            bean = gson.fromJson(jsonResponse, BranchOfficeViewModel.class);
            
        } finally {
            IOUtil.close(input);
            IOUtil.close(output);
            httpConn.disconnect();
        }
        
        return bean;
    }
    
    public ProductRsp registra(Product servicio){
        ProductRsp respuesta = null;
        
        Gson prettyGson   = null;
        String json = null;
        
        String user = null;
        String password = null;
        String basePath = null;
        String sURL = null;
        String auth = null;
        byte[] encodedAuth = null;
        String authHeaderValue = null;
        
        URL url = null;
        HttpURLConnection httpConn = null;
        OutputStream output = null;
        byte[] bytes = null;
        
        InputStream input = null;
        try {
            this.infoTrustStorePath();
            log.info("Solicitando información a Facturama...");
            basePath = DataSourceManager.getJndiParameter("facturama/api");
            user = DataSourceManager.getJndiParameter("facturama/user");
            password = DataSourceManager.getJndiParameter("facturama/password");
            sURL = basePath + "/api/Product";
            
            /*--------------------------------------------------*/
            
            prettyGson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd HH:mm:ss")
                    .setPrettyPrinting().create();
            json = prettyGson.toJson(servicio);
            
            
            log.info("JSON Producto / Servicio Facturama: " + json);
            System.out.println(json);
            
            auth = String.format("%s:%s", user, password);
            encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.UTF_8));
            authHeaderValue = "Basic " + new String(encodedAuth);
            
            url = new URL(sURL);
            httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            httpConn.setRequestProperty("Accept", "application/json");
            httpConn.setRequestProperty("Authorization", authHeaderValue);
            httpConn.setRequestMethod("POST");            
            httpConn.setDoOutput(true);
            httpConn.setDoInput(true);
            bytes = json.getBytes("utf-8");
            output = httpConn.getOutputStream();
            output.write(bytes);
            output.close();
            
            input = new BufferedInputStream(httpConn.getInputStream());//error
            String result = IOUtils.toString(input, "UTF-8");
            log.info("Respuesta de la API Facturama:\n" + result);
            Gson gson = new Gson();
            respuesta = gson.fromJson(result,  ProductRsp.class);
            
            /*--------------------------------------------------*/
            
            
        } catch(Exception ex) {
            log.error("Problema para registrar producto o servicio en Facturama...", ex);
        } finally {
            IOUtil.close(input);
            IOUtil.close(output);
            httpConn.disconnect();
        }
        
        return respuesta;
    }
    
    public Boolean updateProducto(ProductRsp servicio, String Uuid){
        boolean respuesta = true;
        Gson prettyGson   = null;
        String json = null;
        
        String user = null;
        String password = null;
        String basePath = null;
        String sURL = null;
        String auth = null;
        byte[] encodedAuth = null;
        String authHeaderValue = null;
        
        URL url = null;
        HttpURLConnection httpConn = null;
        OutputStream output = null;
        byte[] bytes = null;
        
        InputStream input = null;
        try {
            this.infoTrustStorePath();
            log.info("Solicitando información a Facturama...");
            basePath = DataSourceManager.getJndiParameter("facturama/api");
            user = DataSourceManager.getJndiParameter("facturama/user");
            password = DataSourceManager.getJndiParameter("facturama/password");
            sURL = basePath + "/api/Product/" + Uuid;
            
            /*--------------------------------------------------*/
            
            prettyGson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd HH:mm:ss")
                    .setPrettyPrinting().create();
            json = prettyGson.toJson(servicio);
            
            
            //log.info("JSON Producto / Servicio Facturama: " + json);
            System.out.println(json);
            
            auth = String.format("%s:%s", user, password);
            encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.UTF_8));
            authHeaderValue = "Basic " + new String(encodedAuth);
            
            url = new URL(sURL);
            httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            httpConn.setRequestProperty("Accept", "application/json");
            httpConn.setRequestProperty("Authorization", authHeaderValue);
            httpConn.setRequestMethod("PUT");   
            httpConn.setDoOutput(true);
            httpConn.setDoInput(false);
            bytes = json.getBytes("utf-8");
            output = httpConn.getOutputStream();
            output.write(bytes);
            output.close();                    
                        
            
            /*--------------------------------------------------*/
            
            respuesta = true;
        } catch(Exception ex) {
            log.error("Problema para actualizar producto o servicio en Facturama...", ex);
            respuesta = false;
        } finally {
            IOUtil.close(input);
            IOUtil.close(output);
            httpConn.disconnect();
        }
        
        return respuesta;
    }
    
    public CfdiInfoModel registra(CFDIInfo cfdi) throws IOException {
        CfdiInfoModel respuesta = null;
        
        Gson prettyGson   = null;
        String json = null;
        
        String user = null;
        String password = null;
        String basePath = null;
        String sURL = null;
        String auth = null;
        byte[] encodedAuth = null;
        String authHeaderValue = null;
        
        URL url = null;
        HttpURLConnection httpConn = null;
        OutputStream output = null;
        byte[] bytes = null;
        
        InputStream input = null;
        
        try {
            this.infoTrustStorePath();
            log.info("Solicitando información a Facturama...");
            basePath = DataSourceManager.getJndiParameter("facturama/api");
            user = DataSourceManager.getJndiParameter("facturama/user");
            password = DataSourceManager.getJndiParameter("facturama/password");
            sURL = basePath + "/2/cfdis";
            
            prettyGson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd HH:mm:ss")
                    .setPrettyPrinting().create();
            json = prettyGson.toJson(cfdi);
            
            log.info("JSON CFDI (Factura) Facturama: " + json);
            auth = String.format("%s:%s", user, password);
            encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.UTF_8));
            authHeaderValue = "Basic " + new String(encodedAuth);
            
            url = new URL(sURL);
            httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            httpConn.setRequestProperty("Accept", "application/json");
            httpConn.setRequestProperty("Authorization", authHeaderValue);
            httpConn.setRequestMethod("POST");
            httpConn.setDoOutput(true);
            httpConn.setDoInput(true);
            bytes = json.getBytes("utf-8");
            output = httpConn.getOutputStream();
            output.write(bytes);
            output.close();
            
            input = new BufferedInputStream(httpConn.getInputStream());
            String jsonResponse = IOUtils.toString(input, "UTF-8");
            log.info("Respuesta de la API Facturama:\n" + jsonResponse);
            Gson gson = new Gson();
            respuesta = gson.fromJson(jsonResponse,  CfdiInfoModel.class);
            
        } catch(Exception ex) {
            input = httpConn.getErrorStream();
            String responseMessage = httpConn.getResponseMessage();
            String errorMessage = String.format("Problema para registrar producto o servicio en Facturama (%s)...", responseMessage);
            log.error(errorMessage, ex);
        } finally {
            IOUtil.close(input);
            IOUtil.close(output);
            httpConn.disconnect();
        }
        
        return respuesta;
    }
    
//    public boolean existenConceptosNoSincronizados() {
//        boolean respuesta = false;
//        Servicio[] servicios = null;
//        List<Servicio> alServicios = null;
//        List<Servicio> alServiciosNoSync = null;
//        ServicioBusinessLogic servicioBO = null;
//        String jndiName = null;
//        
//        jndiName = DataSourceManager.getJniName("");
//        servicioBO = new ServicioBusinessLogic(jndiName);
//        servicios = servicioBO.get();
//        
//        alServicios = Arrays.asList(servicios);
//        
//        alServiciosNoSync = alServicios.stream()
//                .filter(s -> (s.getUUID() == null || "".equalsIgnoreCase(s.getUUID())))
//                .collect(Collectors.toList());
//        
//        if(alServiciosNoSync == null || alServiciosNoSync.size() == 0) {
//            respuesta = false;
//        } else {
//            log.info("Servicios no sincronizados: " + alServiciosNoSync);
//            respuesta = true;
//        }
//        
//        return respuesta;
//    }
    
    /**Obtiene el archivo de la factura en una sucesión de caracteres base64 en el formato deseado.<br>
     * https://www.api.facturama.com.mx/api/Cfdi/{format}/{type}/{id}<br>
     * @param format Formato del archivo a obtener: ( pdf | html | xml ) (requerido)<br>
     * @param type Tipo de comprbante a obtener, puede ser: para facturas de API normal( payroll | received | issued ) y para API Multiemisor ( issuedLite ) (requerido)<br>
     * @param id Identificador unico de la factura (requerido)<br>
     * @return FileViewModel representación del archivo mediante el objeto FileViewModel de Facturama.
     * @throws IOException 
     */
    public FileViewModel getFile(String format, String type, String id) throws IOException {
        FileViewModel respuesta = null;
        
        String charset = "UTF-8";
        String basePath = null;
        String user = null;
        String password = null;
        String auth = null;
        byte[] encodedAuth = null;
        String authHeaderValue = null;
        String sURL = null;
        
        URL url = null;
        HttpURLConnection httpConn = null;
        OutputStream output = null;
        InputStream input = null;
        
        try {
            this.infoTrustStorePath();
            log.info("Solicitando información a Facturama...");
            basePath = DataSourceManager.getJndiParameter("facturama/api");
            user = DataSourceManager.getJndiParameter("facturama/user");
            password = DataSourceManager.getJndiParameter("facturama/password");
            
            //URL original: https://www.api.facturama.com.mx/api/Cfdi/{format}/{type}/{id}
            sURL = basePath + "/api/Cfdi/%s/%s/%s";
            
            auth = String.format("%s:%s", user, password);
            encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.UTF_8));
            authHeaderValue = "Basic " + new String(encodedAuth);
            
            sURL = String.format(sURL, URLEncoder.encode(format,  charset), URLEncoder.encode(type, charset), URLEncoder.encode(id, charset));
            
            url = new URL(sURL);
            httpConn = (HttpURLConnection) url.openConnection();
            log.info("URL solicitada para archivos: " + sURL);
            httpConn.setRequestProperty("Accept-Charset", charset);
            httpConn.setRequestProperty("Authorization", authHeaderValue);
            httpConn.setRequestMethod("GET");
            httpConn.setDoOutput(false);
            httpConn.setDoInput(true);
            
            input = new BufferedInputStream(httpConn.getInputStream());
            
            String jsonResponse = IOUtils.toString(input, "UTF-8");
            
            log.info("Respuesta de la API Facturama:\n" + jsonResponse);
            
            Gson gson = new Gson();
            respuesta = gson.fromJson(jsonResponse, FileViewModel.class);
        } finally {
            IOUtil.close(input);
            IOUtil.close(output);
            httpConn.disconnect();
        }
        
        return respuesta;
    }
    
    public ClientModel getClienteById(String id)throws IOException {
        ClientModel cliente = null;
        
        String charset = "UTF-8";
        String basePath = null;
        String user = null;
        String password = null;
        String auth = null;
        byte[] encodedAuth = null;
        String authHeaderValue = null;
        String sURL = null;
        
        URL url = null;
        HttpURLConnection httpConn = null;
        OutputStream output = null;
        InputStream input = null;
        
        try {
            this.infoTrustStorePath();
            log.info("Solicitando información a Facturama...");
            basePath = DataSourceManager.getJndiParameter("facturama/api");
            user = DataSourceManager.getJndiParameter("facturama/user");
            password = DataSourceManager.getJndiParameter("facturama/password");
            
            
            auth = String.format("%s:%s", user, password);
            encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.UTF_8));
            authHeaderValue = "Basic " + new String(encodedAuth);
            
            sURL = basePath + "/api/Client/%s";
            sURL = String.format(sURL, URLEncoder.encode(id,  charset));
            
            url = new URL(sURL);
            httpConn = (HttpURLConnection) url.openConnection();
            log.info("URL solicitada para archivos: " + sURL);
            httpConn.setRequestProperty("Accept-Charset", charset);
            httpConn.setRequestProperty("Authorization", authHeaderValue);
            httpConn.setRequestMethod("GET");
            httpConn.setDoOutput(false);
            httpConn.setDoInput(true);
            
            input = new BufferedInputStream(httpConn.getInputStream());
            
            String jsonResponse = IOUtils.toString(input, "UTF-8");
            
            log.info("Respuesta de la API Facturama:\n" + jsonResponse);
            
            Gson gson = new Gson();
            cliente = gson.fromJson(jsonResponse, ClientModel.class);
        } finally {
            IOUtil.close(input);
            IOUtil.close(output);
            httpConn.disconnect();
        }
        
        return cliente;
    }
    
    public ClientModelRsp getByRFC(String rfc) {
        List<ClientModelRsp> alClientes = null;
        List<ClientModelRsp> alTmp = null;
        ClientModelRsp respuesta = null;
        
        try {
            alClientes = this.getClientes();
            
            alTmp = alClientes.stream()
                    .filter(c -> c.getRfc().equalsIgnoreCase(rfc))
                    .collect(Collectors.toList());
            
            if(alTmp.size() > 0)
                respuesta = alTmp.get(0);
            
        } catch(Exception ex) {
            log.error("Problema para obtener el cliente con RFC " + rfc, ex);
        }
        
        return respuesta;
    }
    
    public List<ClientModelRsp> getClientes() throws IOException {
        List<ClientModelRsp> beans = null;
        
        String charset = "UTF-8";
        String basePath = null;
        String user = null;
        String password = null;
        String auth = null;
        byte[] encodedAuth = null;
        String authHeaderValue = null;
        String sURL = null;
        
        URL url = null;
        HttpURLConnection httpConn = null;
        OutputStream output = null;
        InputStream input = null;
        
        try {
            this.infoTrustStorePath();
            log.info("Solicitando información a Facturama...");
            basePath = DataSourceManager.getJndiParameter("facturama/api");
            user = DataSourceManager.getJndiParameter("facturama/user");
            password = DataSourceManager.getJndiParameter("facturama/password");
            
            auth = String.format("%s:%s", user, password);
            encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.UTF_8));
            authHeaderValue = "Basic " + new String(encodedAuth);
            
            sURL = basePath + "/api/Client";
            sURL = String.format(sURL);
            
            url = new URL(sURL);
            httpConn = (HttpURLConnection) url.openConnection();
            log.info("URL solicitada para archivos: " + sURL);
            httpConn.setRequestProperty("Accept-Charset", charset);
            httpConn.setRequestProperty("Authorization", authHeaderValue);
            httpConn.setRequestMethod("GET");
            httpConn.setDoOutput(false);
            httpConn.setDoInput(true);
            
            input = new BufferedInputStream(httpConn.getInputStream());
            
            String jsonResponse = IOUtils.toString(input, "UTF-8");
            
            log.info("Respuesta de la API Facturama:\n" + jsonResponse);
            
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<ClientModelRsp>>(){}.getType();
            
            beans = gson.fromJson(jsonResponse, listType);
        } finally {
            IOUtil.close(input);
            IOUtil.close(output);
            httpConn.disconnect();
        }
        
        return beans;
    }
    
    public List<Product> getProductos() throws IOException {
        List<Product> beans = null;
        String charset = "UTF-8";
        String basePath = null;
        String user = null;
        String password = null;
        String auth = null;
        byte[] encodedAuth = null;
        String authHeaderValue = null;
        String sURL = null;
        
        URL url = null;
        HttpURLConnection httpConn = null;
        OutputStream output = null;
        InputStream input = null;
        
        try {
            this.infoTrustStorePath();
            log.info("Solicitando información a Facturama...");
            basePath = DataSourceManager.getJndiParameter("facturama/api");
            user = DataSourceManager.getJndiParameter("facturama/user");
            password = DataSourceManager.getJndiParameter("facturama/password");
            
            
            auth = String.format("%s:%s", user, password);
            encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.UTF_8));
            authHeaderValue = "Basic " + new String(encodedAuth);
            
            sURL = basePath + "/api/Product";
            sURL = String.format(sURL);
            
            url = new URL(sURL);
            httpConn = (HttpURLConnection) url.openConnection();
            log.info("URL solicitada para archivos: " + sURL);
            httpConn.setRequestProperty("Accept-Charset", charset);
            httpConn.setRequestProperty("Authorization", authHeaderValue);
            httpConn.setRequestMethod("GET");
            httpConn.setDoOutput(false);
            httpConn.setDoInput(true);
            
            input = new BufferedInputStream(httpConn.getInputStream());
            
            String jsonResponse = IOUtils.toString(input, "UTF-8");
            
            log.info("Respuesta de la API Facturama:\n" + jsonResponse);
            
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<Product>>(){}.getType();
            
            beans = gson.fromJson(jsonResponse, listType);
        } finally {
            IOUtil.close(input);
            IOUtil.close(output);
            httpConn.disconnect();
        }
        
        return beans;
    }
}
