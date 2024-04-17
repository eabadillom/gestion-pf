package mx.com.ferbo.controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.model.Usuario;

@Named
@ViewScoped
public class SideBarBean implements Serializable {

	private static final long serialVersionUID = 8802717839932668484L;
	private static Logger log = LogManager.getLogger(SideBarBean.class);
	private Usuario usuario;

	private FacesContext faceContext;
	private HttpServletRequest httpServletRequest;
	private HttpSession session;

	@PostConstruct
	public void init() {
		faceContext = FacesContext.getCurrentInstance();
		httpServletRequest = (HttpServletRequest) faceContext.getExternalContext().getRequest();
		session = httpServletRequest.getSession(false);
		this.usuario = (Usuario) httpServletRequest.getSession(true).getAttribute("usuario");
	}

	public void logout() {
		String contextPath = null;
		String fullPath = null;
		try {
			contextPath = faceContext.getExternalContext().getApplicationContextPath();
			fullPath = contextPath + "/login.xhtml";
			this.usuario = (Usuario) session.getAttribute("usuario");
			log.info("El usuario intenta finalizar su sesión: " + this.usuario.getUsuario());
			session.setAttribute("usuario", null);
			session.setAttribute("idCliente", null);
			log.info("Redirigiendo al usuario a {}", fullPath);
			faceContext.getExternalContext().redirect(fullPath);
			session.invalidate();
		} catch (Exception ex) {
			log.error("Problema en el cierre de sesión del usuario...", ex);
		}
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
}