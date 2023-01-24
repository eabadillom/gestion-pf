package mx.com.ferbo.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.primefaces.PrimeFaces;

import mx.com.ferbo.dao.PerfilDAO;
import mx.com.ferbo.dao.PlantaDAO;
import mx.com.ferbo.dao.TipoMailDAO;
import mx.com.ferbo.dao.UsuarioDAO;
import mx.com.ferbo.model.Mail;
import mx.com.ferbo.model.Perfil;
import mx.com.ferbo.model.Planta;
import mx.com.ferbo.model.TipoMail;
import mx.com.ferbo.model.Usuario;
import mx.com.ferbo.util.SecurityUtil;

@Named
@ViewScoped
public class UsuariosBean implements Serializable {
	private static final long serialVersionUID = 8438449261015571241L;
	private UsuarioDAO usuarioDAO;
	private PlantaDAO plantaDAO;
	private PerfilDAO perfilDAO;
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
	

	public UsuariosBean() {
		plantaDAO = new PlantaDAO();
		perfilDAO = new PerfilDAO();
		usuarioDAO= new UsuarioDAO();
		usuario = new Usuario();
}

	@PostConstruct
	public void init() {
		usuarios = usuarioDAO.getUsuarios();
		lstPerfil = usuarioDAO.getPerfil();
		lstPlanta = plantaDAO.findall();
	}
	
	public void openNew() {
		this.usuario= new Usuario();
	};

	public void guardar() {
		PrimeFaces.current().executeScript("PF('dialogCliente').hide()");
		System.out.println(usuario);
		usuario.setIdPlanta(this.idplanta);
		usuario.setStUsuario(this.idstatus);
		usuario.setPerfil(this.idperfil);
		String user = usuarioDAO.guardar(usuario);
		if (user == null) {
			usuarios.clear();
			usuarios = usuarioDAO.findall();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Usuario agregado " + usuario.getUsuario(), null));
			PrimeFaces.current().ajax().update("form:messages", "form:dt-usuario");
		} else {
			FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR, "Usuario no agregado " + usuario.getUsuario(), user));
			PrimeFaces.current().ajax().update("form:messages");
		}
		this.usuario = new Usuario();
	}

	public void eliminarUsuario() {
		
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

}
