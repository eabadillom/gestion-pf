package mx.com.ferbo.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;
import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import mx.com.ferbo.business.ClienteBL;
import mx.com.ferbo.dao.UsuarioDAO;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.Usuario;
import mx.com.ferbo.response.DetBiometricoResponse;
import mx.com.ferbo.util.SecurityUtil;


@Named
@ViewScoped
public class LoginBean implements Serializable  {

	private static final long serialVersionUID = 491768169161736335L;
	private static final Logger log = LogManager.getLogger(LoginBean.class);

	private String username;
	private String password;
	private UsuarioDAO usuarioDAO;
	private Usuario usuario;
	private FacesContext faceContext;
	private HttpServletRequest httpServletRequest;
	
	private List<Cliente> clientesList = null;
    private List<Cliente> clientesActivosList = null;
    private ClienteBL clienteBL = null;
  
	private SecurityUtil securityUtil = null;

	public LoginBean() {
		usuarioDAO = new UsuarioDAO();
	}

	@PostConstruct
	public void init() {
		securityUtil = new SecurityUtil();
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

		String shaPassword = null;
		String nextPage = null;

		try {
			if (this.username == null) {
				message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Usuario o contraseña incorrecto.", null);
				log.warn("Inicio de sesión incorrecto (usuario incorrecto).");
				FacesContext.getCurrentInstance().addMessage(null, message);
				this.espera();
				return;
			}
			if (this.password == null) {
				message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Usuario o contraseña incorrecto.", null);
				log.warn("Inicio de sesión incorrecto (contraseña incorrecta).");
				FacesContext.getCurrentInstance().addMessage(null, message);
				this.espera();
				return;
			}

			Usuario usr = usuarioDAO.buscarPorUsuario(this.username);
			if (usr == null) {
				message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Usuario o contraseña incorrecto.", null);
				log.warn("Inicio de sesión incorrecto (usuario/contraseña no encontrado).");
				FacesContext.getCurrentInstance().addMessage(null, message);
				this.espera();
				return;
			}

			if (usr.getUsuario().equals(this.username) == false) {
				message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Usuario o contraseña incorrecto.", null);
				log.warn("Inicio de sesión incorrecto (usuario con status BAJA {}).", usr.getUsuario());
				FacesContext.getCurrentInstance().addMessage(null, message);
				this.espera();
				return;
			}

			if ("B".equalsIgnoreCase(usr.getStUsuario())) {
				message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Usuario o contraseña incorrecto.", null);
				log.warn("Inicio de sesión incorrecto (usuario bloqueado).");
				FacesContext.getCurrentInstance().addMessage(null, message);
				this.espera();
				return;
			}

			if ("L".equalsIgnoreCase(usr.getStUsuario())) {
				message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Usuario o contraseña incorrecto.", null);
				log.warn("Inicio de sesión incorrecto (usuario con status BLOQUEADO: {}).", usr.getUsuario());
				FacesContext.getCurrentInstance().addMessage(null, message);
				this.espera();
				return;
			}

			// Por seguridad, se salan las contraseñas.
			shaPassword = securityUtil.getSHA512(this.password + usr.getUsuario());

			if (usr.getPassword().equals(shaPassword) == false) {
				message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Usuario o contraseña incorrecto.", null);
				log.warn("Inicio de sesión incorrecto (contraseña incorrecta).");
				FacesContext.getCurrentInstance().addMessage("login_form:growl", message);
				this.espera();
				return;
			}

			faceContext = FacesContext.getCurrentInstance();
			httpServletRequest = (HttpServletRequest) faceContext.getExternalContext().getRequest();

			if (usr.getHuella()) {

				// ---- PETICION GET ----- para mi Microservicio Biometrico
				String numeroEmpleado = usr.getNumEmpleado();

				Properties prop = new Properties();
				InputStream in = getClass().getResourceAsStream("/config/gestion.properties");
				prop.load(in);
				log.info(prop.get("sgp.url"));
				String ip = prop.getProperty("sgp.url");

				// le mandamos la url de nuestro microservicio
				url = ip + "/detEmpleado/empleadoBio?numEmp=" + numeroEmpleado;
				url = String.format(url);
				log.info("Realizando la peticion GET al microServicio: " + url);

				// Hacemos la peticion GET por HTTP
				request = new HttpGet(url);
				request.addHeader("Accept-Charset", "UTF-8");
				httpClient = HttpClients.createDefault();
				response = httpClient.execute(request);
				httpStatus = response.getCode();

				log.info("La respuesta de la peticion get es: " + httpStatus);

				if (httpStatus < 200 || httpStatus >= 300)// es erronea la respuesta por el microservicio ?
					throw new Exception("Respuesta no satisfactoria del MicroServicio.");

				// status correcto
				// recuperando el body de la respuesta
				entity = response.getEntity();
				resultContent = EntityUtils.toString(entity);
				bodyResponse = new String(resultContent.getBytes(), "UTF-8");

				jsonResponse = bodyResponse;

				// conviertiendo a formato json
				log.info("Respuesta del MicroServicio:\n" + jsonResponse);
				prettyJson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'hh:mm:ss").create();

				type = new TypeToken<DetBiometricoResponse>() {
				}.getType();

				bean = prettyJson.fromJson(jsonResponse, type);

				log.info("Su primer bio es: " + bean.getHuella());

				log.info("json:\n" + bean);

				httpServletRequest.getSession(true).setAttribute("json", bean);

			}

			httpServletRequest.getSession(true).setAttribute("usuario", usr);

			this.setUsuario(usr);
			message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Acceso correcto", null);
			FacesContext.getCurrentInstance().addMessage(null, message);

			if ("R".equals(this.usuario.getStUsuario()))
				nextPage = "changePassword.xhtml";
			else {

				if (usuario.getHuella() == true) {
					nextPage = "ValidacionHuella.jsp";
				} else {
					nextPage = "dashboard.xhtml";
				}

			}
			
			clienteBL = new ClienteBL(this.usuario, this.clientesList, this.clientesActivosList, httpServletRequest.getSession());
	        clienteBL.start();

			faceContext.getExternalContext().redirect(nextPage);

		} catch (IOException ex) {
			log.error("Problema en la autenticación del usuario...", ex);
		} catch (Exception ex) {
			log.error("Problema en la autenticación del usuario...", ex);
		} finally {
			this.username = null;
			this.password = null;
		}
	}

	public void espera() throws InterruptedException {
		TimeUnit.SECONDS.sleep(3);
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
	
	public List<Cliente> getClientesList() {
		return clientesList;
	}
	public void setClientesList(List<Cliente> clientesList) {
		this.clientesList = clientesList;
	}
	public List<Cliente> getClientesActivosList() {
		return clientesActivosList;
	}
	public void setClientesActivosList(List<Cliente> clientesActivosList) {
		this.clientesActivosList = clientesActivosList;
	}
}
