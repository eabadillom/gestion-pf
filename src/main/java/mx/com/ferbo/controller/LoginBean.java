package mx.com.ferbo.controller;

import java.io.IOException;
import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import mx.com.ferbo.dao.UsuarioDAO;
import mx.com.ferbo.model.Usuario;

@Named
@ViewScoped
public class LoginBean implements Serializable {

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
	
	public void login() {
		FacesMessage message = null;
		
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
			
			//en caso de que todas las validaciones se encuentren correctas, se procederá a registrar
			//el usuario en sesión y redirigir a la página de bienvenida.
			faceContext = FacesContext.getCurrentInstance();
	        httpServletRequest = (HttpServletRequest) faceContext.getExternalContext().getRequest();
	        httpServletRequest.getSession(true).setAttribute("usuario", usr);                
	        this.setUsuario(usr);
	        message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Acceso correcto", null);
	        FacesContext.getCurrentInstance().addMessage(null, message);
				faceContext.getExternalContext().redirect("dashboard.xhtml");
		} catch (IOException ex) {
			ex.printStackTrace();
			//log.error("Problema en la autenticación del usuario...", ex);
		} catch (Exception ex) {
			ex.printStackTrace();
			//log.error("Problema en la autenticación del usuario...", ex);
		}
	}
}
