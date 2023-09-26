package mx.com.ferbo.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;

import mx.com.ferbo.dao.PlantaDAO;
import mx.com.ferbo.dao.UsuarioDAO;
import mx.com.ferbo.model.Perfil;
import mx.com.ferbo.model.Planta;
import mx.com.ferbo.model.TipoMail;
import mx.com.ferbo.model.Usuario;
import mx.com.ferbo.util.InventarioException;
import mx.com.ferbo.util.SecurityUtil;

@Named
@ViewScoped
public class UsuariosBean implements Serializable {
	private static final long serialVersionUID = 8438449261015571241L;
	private static Logger log = LogManager.getLogger(UsuariosBean.class);
	
	private UsuarioDAO usuarioDAO;
	private PlantaDAO plantaDAO;
	
	private List<Usuario> usuarios;
	private List<TipoMail> lstTipoMail;
	private List<Planta> lstPlanta;
	private List<Perfil> lstPerfil;

	private Usuario usuario;
	private Perfil perfil;
	private int idplanta;
	private int idperfil;
	private String idstatus;
	private String nombre;
	private String apellido1;
	private String apellido2;
	private String mail;
	private String descripcion;
	private String login;
	private boolean showPassword = false;
	private String newPassword = null;
	

	public UsuariosBean() {
		plantaDAO = new PlantaDAO();
		usuarioDAO= new UsuarioDAO();
		usuario = new Usuario();
	}

	@PostConstruct
	public void init() {
		usuarios = usuarioDAO.getUsuarios();
		lstPerfil = usuarioDAO.getPerfil();
		lstPlanta = plantaDAO.findall();
		this.showPassword = false;
	}
	
	public void openNew() {
		this.usuario= new Usuario();
		this.usuario.setStUsuario("R");
		this.showPassword = false;
		this.idstatus = "R";
	};
	
	public void cargaUsuario() {
		log.debug("Usuario: {}, Status: {}, StNtfSrvExt: {}", this.usuario.getUsuario(), this.usuario.getStUsuario(), this.usuario.isStNtfSrvExt());
	}

	public void guardar() {
		String sha512Password = null;
		SecurityUtil securityBO = null;
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		String titulo = "Usuario";
		
		try {
			securityBO = new SecurityUtil();
			
			if(usuario.getId() == null) {
				//Por seguridad, se salan las contraseñas.
				sha512Password = securityBO.getSHA512("temporal" + usuario.getUsuario());
				usuario.setPassword(sha512Password);
				usuario.setStUsuario("R");
			}
			
			log.debug("Usuario: {}", usuario);
			usuario.setIdPlanta(this.idplanta);
			String user = usuarioDAO.actualizar(usuario);
			
			if(user != null)
				throw new InventarioException("Ocurrió un problema al guardar el usuario.\r\nIntente nuevamente.\r\nSi el problema persiste, informe a su administrador de sistemas.");
			
			usuarios.clear();
			usuarios = usuarioDAO.findall();
			this.showPassword = false;
			this.usuario = new Usuario();
			
			mensaje = "El usuario se registró correctamente.";
			severity = FacesMessage.SEVERITY_INFO;
			PrimeFaces.current().executeScript("PF('dialogCliente').hide()");
		} catch (InventarioException ex) {
			mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_WARN;
		} catch (Exception ex) {
			log.error("Problema para actualizar el usuario...", ex);
			mensaje = "Ha ocurrido un error en el sistema. Intente nuevamente.\nSi el problema persiste, por favor comuniquese con su administrador del sistema.";
			severity = FacesMessage.SEVERITY_ERROR;
		} finally {
			message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
			PrimeFaces.current().ajax().update("form:messages", "form:dt-usuario");
		}
	}
	
	public boolean isShowPassword() {
		return showPassword;
	}

	public void setShowPassword(boolean showPassword) {
		this.showPassword = showPassword;
	}
	
	public void resetPassword() {
		this.newPassword = null;
	}

	public void guardarPassword() {
		String sha512Password = null;
		SecurityUtil securityBO = null;
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		String titulo = "Actualizar contraseña";
		
		try {
			securityBO = new SecurityUtil();
			securityBO.checkPassword(this.newPassword);
			
			//Por seguridad, se salan las contraseñas.
			sha512Password = securityBO.getSHA512(this.newPassword + usuario.getUsuario());
			this.usuario.setPassword(sha512Password);
			
			String user = this.usuarioDAO.actualizar(usuario);
			if (user != null) {
				throw new InventarioException("La contraseña no se actualizó.\r\nIntente nuevamente.\r\nSi el problema persiste, informe a su administrador de sistemas.");
			}
			
			this.usuarios.clear();
			this.usuarios = this.usuarioDAO.findall();
			this.usuario = new Usuario();
			mensaje = "La contraseña se actualizó correctamente.";
			severity = FacesMessage.SEVERITY_INFO;
			PrimeFaces.current().executeScript("PF('dialogPassword').hide()");
		} catch (InventarioException ex) {
			mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_WARN;
		} catch (Exception ex) {
			log.error("Problema para actualizar el usuario...", ex);
			mensaje = "Ha ocurrido un error en el sistema. Intente nuevamente.\nSi el problema persiste, por favor comuniquese con su administrador del sistema.";
			severity = FacesMessage.SEVERITY_ERROR;
		} finally {
			message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
			PrimeFaces.current().ajax().update("form:messages", "form:dt-usuario");
		}
	}
	
	public void eliminar() {
		PrimeFaces.current().executeScript("PF('deleteClienteDialog').hide()");
		String user= usuarioDAO.eliminar(usuario);
		if (user == null) {
			usuarios.remove(this.usuario);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Usuario Eliminado " + usuario.getUsuario(), null));
			PrimeFaces.current().ajax().update("form:messages", "form:dt-usuario");
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al eliminar " , "El usuario no puede ser eliminado por dependecia con una planta"));
			PrimeFaces.current().ajax().update("form:messages");
		}
	}
	
	public List<TipoMail> getLstTipoMail() {
		return lstTipoMail;
	}

	public void setLstTipoMail(List<TipoMail> lstTipoMail) {
		this.lstTipoMail = lstTipoMail;
	}

	public List<Planta> getLstPlanta() {
		return lstPlanta;
	}

	public void setLstPlanta(List<Planta> lstPlanta) {
		this.lstPlanta = lstPlanta;
	}

	public List<Perfil> getLstPerfil() {
		return lstPerfil;
	}

	public void setLstPerfil(List<Perfil> lstPerfil) {
		this.lstPerfil = lstPerfil;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public int getIdplanta() {
		return idplanta;
	}

	public void setIdplanta(int idplanta) {
		this.idplanta = idplanta;
	}

	public int getIdperfil() {
		return idperfil;
	}

	public void setIdperfil(int idperfil) {
		this.idperfil = idperfil;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido1() {
		return apellido1;
	}

	public void setApellido1(String apellido1) {
		this.apellido1 = apellido1;
	}

	public String getApellido2() {
		return apellido2;
	}

	public void setApellido2(String apellido2) {
		this.apellido2 = apellido2;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Perfil getPerfil() {
		return perfil;
	}

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}

	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public String getIdstatus() {
		return idstatus;
	}

	public void setIdstatus(String idstatus) {
		this.idstatus = idstatus;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}
	
	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

}
