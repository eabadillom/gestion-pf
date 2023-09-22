package mx.com.ferbo.controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;

import mx.com.ferbo.dao.UsuarioDAO;
import mx.com.ferbo.model.Usuario;
import mx.com.ferbo.util.InventarioException;
import mx.com.ferbo.util.SecurityUtil;

@Named(value = "changePwd")
@ViewScoped
public class ChangePasswordBean implements Serializable {

	private static final long serialVersionUID = 7551325508890296828L;
	private static Logger log = LogManager.getLogger(ChangePasswordBean.class);
	
	private FacesContext context;
	private HttpServletRequest request;
	
	private Usuario usuario = null;
	private UsuarioDAO usuarioDAO = null;
	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getCurrentPassword() {
		return currentPassword;
	}

	public void setCurrentPassword(String currentPassword) {
		this.currentPassword = currentPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	private String currentPassword;
	private String newPassword;
	private String confirmPassword;
	private SecurityUtil security;
	
	public ChangePasswordBean() {
		this.security = new SecurityUtil();
	}
	
	@PostConstruct
	public void init() {
		log.info("Entrando a la pantalla (init)...");
		
		try {
			usuarioDAO = new UsuarioDAO();
			context = FacesContext.getCurrentInstance();
			request = (HttpServletRequest) context.getExternalContext().getRequest();
			usuario = (Usuario) request.getSession(false).getAttribute("usuario");
			String mensaje = String.format("Usuario %s ingresa a Registro de orden de salida.", usuario.toString());
			log.info(mensaje);
		} catch(Exception ex) {
			log.error("Problema al ingresar a la pantalla de cambio de contraseña...", ex);
		}
	}
	
	public void changePassword() {
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		
		String currentPasswordSHA512 = null;
		String newPasswordSHA512 = null;
		
		
		try {
			if(this.currentPassword == null || "".equalsIgnoreCase(this.currentPassword.trim()))
				throw new InventarioException("Debe indicar su contraseña actual.");
			
			currentPasswordSHA512 = security.getSHA512(this.currentPassword);
			if(usuario.getPassword().equals(currentPasswordSHA512) == false)
				throw new InventarioException("La contraseña actual indicada es incorrecta.");
			
			if(this.newPassword == null || "".equalsIgnoreCase(this.newPassword.trim()))
				throw new InventarioException("Debe indicar su nueva contraseña");
			
			if(this.confirmPassword == null || "".equalsIgnoreCase(this.confirmPassword.trim()))
				throw new InventarioException("Debe confirmar su nueva contraseña");
			
			security.checkPassword(this.newPassword);
			
			newPasswordSHA512 = security.getSHA512(this.newPassword);
			
			usuario.setPassword(newPasswordSHA512);
			usuario.setStUsuario("A");
			
			usuarioDAO.actualizar(usuario);
			
			log.info("Usuario actualizado");
			
			this.currentPassword = null;
			this.newPassword = null;
			this.confirmPassword = null;
			
			mensaje = "Su contraseña se actualizó correctamente.";
			severity = FacesMessage.SEVERITY_INFO;
		} catch(InventarioException ex) {
			mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_WARN;
		} catch(Exception ex) {
			log.error("Problema con la emisión de salidas...", ex);
			mensaje = "Su solicitud no se pudo generar.\nFavor de comunicarse con el administrador del sistema.";
			severity = FacesMessage.SEVERITY_ERROR;
		} finally {
			message = new FacesMessage(severity, "Ajustes...", mensaje);
	        FacesContext.getCurrentInstance().addMessage(null, message);
	        PrimeFaces.current().ajax().update(":form:messages", ":form:currentPassword", ":form:newPassword", ":form:confirmPassword");
		}
		
	}
	
	public void validateNewPassword() {
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		
		try {
			if(this.newPassword == null)
				throw new InventarioException("Debe indicar una contraseña nueva.");
			
			if(this.confirmPassword == null)
				throw new InventarioException("Debe confirmar su contraseña nueva.");
			
			if(newPassword.equals(confirmPassword) == false)
				throw new InventarioException("Su nueva contraseña no coincide en los dos campos.");
			
			mensaje = "Su nueva contraseña coincide correctamente.";
			severity = FacesMessage.SEVERITY_INFO;
		} catch(InventarioException ex) {
			mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_ERROR;
		} catch (Exception ex) {
			log.error("Problema con la emisión de salidas...", ex);
			mensaje = "Su solicitud no se pudo generar.\nFavor de comunicarse con el administrador del sistema.";
			severity = FacesMessage.SEVERITY_ERROR;
		} finally {
			message = new FacesMessage(severity, "Ajustes...", mensaje);
	        FacesContext.getCurrentInstance().addMessage(null, message);
	        PrimeFaces.current().ajax().update(":form:messages");
		}
	}
	
	

}
