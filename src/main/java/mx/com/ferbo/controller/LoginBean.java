package mx.com.ferbo.controller;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.log4j.Logger;
import org.primefaces.model.ResponsiveOption;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import mx.com.ferbo.dao.UsuarioDAO;
import mx.com.ferbo.model.Usuario;
import mx.com.ferbo.response.DetBiometricoResponse;

@Named
@ViewScoped
public class LoginBean implements Serializable  {

	private static final long serialVersionUID = 491768169161736335L;
	private static final Logger log = Logger.getLogger(LoginBean.class);
	
	private String username;
	private String password;
	private UsuarioDAO usuarioDAO;
	private Usuario usuario;
	private FacesContext faceContext;
    private HttpServletRequest httpServletRequest;

	public LoginBean() {
		usuarioDAO = new UsuarioDAO();

	}
	
	public void login() {
		
		FacesMessage message = null;
		DetBiometricoResponse bean = null;
		HttpGet request = null;
		String url = null;
		CloseableHttpResponse response = null;
		CloseableHttpClient httpClient = null;
		int httpStatus;
		String jsonResponse = null;
		String bodyResponse = null;
		        HttpEntity entity = null;
		        String resultContent;
		        Gson prettyJson = null;
		        Type type = null;
		
		try {
			if(this.username == null) {
				message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Usuario o contraseña incorrecto.", null);
				log.warn("Inicio de sesión incorrecto (usuario incorrecto).");
				FacesContext.getCurrentInstance().addMessage(null, message);
				return;
			}
			if(this.password == null) {
				message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Usuario o contraseña incorrecto.", null);
				log.warn("Inicio de sesión incorrecto (contraseña incorrecta).");
				FacesContext.getCurrentInstance().addMessage(null, message);
				return;
			}
				
			Usuario usr = usuarioDAO.buscarPorUsuario(this.username);
			if(usr == null) {
				message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Usuario o contraseña incorrecto.", null);
				log.warn("Inicio de sesión incorrecto (usuario/contraseña no encontrado).");
				FacesContext.getCurrentInstance().addMessage(null, message);
				return;
			}
			
			if(usr.getUsuario().equals(this.username) == false) {
				message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Usuario o contraseña incorrecto.", null);
				log.warn("Inicio de sesión incorrecto (usuario incorrecto).");
				FacesContext.getCurrentInstance().addMessage(null, message);
				return;
			}
			
			if(usr.getPassword().equals(this.password) == false) {
				message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Usuario o contraseña incorrecto.", null);
				log.warn("Inicio de sesión incorrecto (contraseña incorrecta).");
				FacesContext.getCurrentInstance().addMessage("login_form:growl", message);
				return;
			}
	
			//---- PETICION GET ----- para mi Microservicio Biometrico
			String numeroEmpleado = usr.getNumEmpleado();

			//le mandamos la url de nuestro microservicio
			url = "http://192.168.1.120:8090/detEmpleado/empleadoBio?numEmp="+numeroEmpleado;
			url = String.format(url);
			log.info("Realizando la peticion GET al microServicio: "+url);

			//Hacemos la peticion GET por HTTP
			request = new HttpGet(url);
			request.addHeader("Accept-Charset", "UTF-8");
			httpClient = HttpClients.createDefault();
			response = httpClient.execute(request);
			httpStatus = response.getCode();

			log.info("La respuesta de la peticion get es: " + httpStatus);

			if(httpStatus < 200 || httpStatus >= 300)//es erronea la respuesta por el microservicio ?
			            throw new Exception("Respuesta no satisfactoria del MicroServicio.");

			//status correcto
			//recuperando el body de la respuesta
			entity = response.getEntity();
			resultContent = EntityUtils.toString(entity);
			bodyResponse = new String(resultContent.getBytes(), "UTF-8");

			jsonResponse = bodyResponse;

			//conviertiendo a formato json
			log.info("Respuesta del MicroServicio:\n" + jsonResponse);
			prettyJson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'hh:mm:ss").create();

			type = new TypeToken<DetBiometricoResponse>(){}.getType();

			bean = prettyJson.fromJson(jsonResponse, type);

			log.info("Su primer bio es: " + bean.getHuella());

			log.info("json:\n" + bean);

			faceContext = FacesContext.getCurrentInstance();
			       httpServletRequest = (HttpServletRequest) faceContext.getExternalContext().getRequest();
			       httpServletRequest.getSession(true).setAttribute("usuario", usr);
			       httpServletRequest.getSession(true).setAttribute("json", bean);
			       
			       this.setUsuario(usr);
			       message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Acceso correcto", null);
			       FacesContext.getCurrentInstance().addMessage(null, message);
			faceContext.getExternalContext().redirect("ValidacionHuella.jsp");


		} catch (IOException ex) {
			ex.printStackTrace();
			//log.error("Problema en la autenticación del usuario...", ex);
		} catch (Exception ex) {
			ex.printStackTrace();
			//log.error("Problema en la autenticación del usuario...", ex);
		}
	}
	
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}
