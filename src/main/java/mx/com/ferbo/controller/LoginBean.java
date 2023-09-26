package mx.com.ferbo.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.dao.UsuarioDAO;
import mx.com.ferbo.model.Usuario;
import mx.com.ferbo.util.SecurityUtil;

@Named
@ViewScoped
public class LoginBean implements Serializable {

	private static final long serialVersionUID = 491768169161736335L;
	private static final Logger log = LogManager.getLogger(LoginBean.class);
	
	private String username;
	private String password;
	private UsuarioDAO usuarioDAO;
	private Usuario usuario;
	private FacesContext faceContext;
    private HttpServletRequest httpServletRequest;
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
		String shaPassword = null;
		String nextPage = null;
		
		try {
			if(this.username == null) {
				message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Usuario o contraseña incorrecto.", null);
				log.warn("Inicio de sesión incorrecto (usuario incorrecto).");
				FacesContext.getCurrentInstance().addMessage(null, message);
				this.espera();
				return;
			}
			if(this.password == null) {
				message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Usuario o contraseña incorrecto.", null);
				log.warn("Inicio de sesión incorrecto (contraseña incorrecta).");
				FacesContext.getCurrentInstance().addMessage(null, message);
				this.espera();
				return;
			}
				
			Usuario usr = usuarioDAO.buscarPorUsuario(this.username);
			if(usr == null) {
				message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Usuario o contraseña incorrecto.", null);
				log.warn("Inicio de sesión incorrecto (usuario/contraseña no encontrado).");
				FacesContext.getCurrentInstance().addMessage(null, message);
				this.espera();
				return;
			}
			
			if(usr.getUsuario().equals(this.username) == false) {
				message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Usuario o contraseña incorrecto.", null);
				log.warn("Inicio de sesión incorrecto (usuario con status BAJA {}).", usr.getUsuario());
				FacesContext.getCurrentInstance().addMessage(null, message);
				this.espera();
				return;
			}
			
			if("B".equalsIgnoreCase(usr.getStUsuario())) {
				message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Usuario o contraseña incorrecto.", null);
				log.warn("Inicio de sesión incorrecto (usuario bloqueado).");
				FacesContext.getCurrentInstance().addMessage(null, message);
				this.espera();
				return;
			}
			
			if("L".equalsIgnoreCase(usr.getStUsuario())) {
				message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Usuario o contraseña incorrecto.", null);
				log.warn("Inicio de sesión incorrecto (usuario con status BLOQUEADO: {}).", usr.getUsuario());
				FacesContext.getCurrentInstance().addMessage(null, message);
				this.espera();
				return;
			}
			
			shaPassword = securityUtil.getSHA512(this.password);
			
			if(usr.getPassword().equals(shaPassword) == false) {
				message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Usuario o contraseña incorrecto.", null);
				log.warn("Inicio de sesión incorrecto (contraseña incorrecta).");
				FacesContext.getCurrentInstance().addMessage("login_form:growl", message);
				this.espera();
				return;
			}
			
			//en caso de que todas las validaciones se encuentren correctas, se procederá a registrar
			//el usuario en sesión y redirigir a la página de bienvenida.
			faceContext = FacesContext.getCurrentInstance();
	        httpServletRequest = (HttpServletRequest) faceContext.getExternalContext().getRequest();
	        httpServletRequest.getSession(true).setAttribute("usuario", usr);                
	        this.setUsuario(usr);
	        message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Acceso correcto", null);
	        FacesContext.getCurrentInstance().addMessage(null, message);
	        
	        if( "R".equals(usuario.getStUsuario()) )
	        	nextPage = "changePassword.xhtml";
	        else
	        	nextPage = "dashboard.xhtml";
	        
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
	
	
}
