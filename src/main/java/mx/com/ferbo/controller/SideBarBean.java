package mx.com.ferbo.controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import mx.com.ferbo.model.Usuario;

@Named
@ViewScoped
public class SideBarBean implements Serializable {

	private static final long serialVersionUID = 8802717839932668484L;
	private Usuario usuario;
	
	private FacesContext faceContext;
    private HttpServletRequest httpServletRequest;
	
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	@PostConstruct
	public void init() {
		faceContext = FacesContext.getCurrentInstance();
        httpServletRequest = (HttpServletRequest) faceContext.getExternalContext().getRequest();
        this.usuario = (Usuario) httpServletRequest.getSession(true).getAttribute("usuario");    
	}
}
