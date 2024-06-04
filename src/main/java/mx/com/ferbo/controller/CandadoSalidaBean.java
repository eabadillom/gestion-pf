package mx.com.ferbo.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;

import mx.com.ferbo.dao.CandadoSalidaDAO;
import mx.com.ferbo.model.CandadoSalida;
import mx.com.ferbo.model.Usuario;

@Named
@ViewScoped
public class CandadoSalidaBean implements Serializable {

	private static final long serialVersionUID = 1;
	private static Logger log = LogManager.getLogger(CandadoSalidaBean.class);

	private List<CandadoSalida> lista;
	private CandadoSalida seleccion;
	private CandadoSalidaDAO dao;
	
	private Usuario usuario;
	private FacesContext faceContext;
	private HttpServletRequest httpServletRequest;

	public CandadoSalidaBean() {
		dao = new CandadoSalidaDAO();
		lista = dao.findAll();
		seleccion = new CandadoSalida();
	};
	
	@PostConstruct
	public void init() {
		faceContext = FacesContext.getCurrentInstance();
		httpServletRequest = (HttpServletRequest) faceContext.getExternalContext().getRequest();
		usuario = (Usuario) httpServletRequest.getSession(false).getAttribute("usuario");
		
		log.info("El usuario {} ingresa al mantenimiento de candados de salida...", this.usuario.getUsuario());
	}

	public void update() {
		PrimeFaces.current().executeScript("PF('dg-modifica').hide()");
		String message = dao.update(seleccion);

		if (message == null) {
			lista.clear();
			lista = dao.findAll();
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Candado Salida modificado", null));
			PrimeFaces.current().ajax().update("form:messages", "form:dtCandado");
		} else {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al modificar", message));
			PrimeFaces.current().ajax().update("form:messages");
		}
		this.seleccion = new CandadoSalida();
	};

	public List<?> getLista() {
		return lista;
	};

	public void setLista(List<CandadoSalida> lista) {
		this.lista = lista;
	};

	public CandadoSalida getSeleccion() {
		return seleccion;
	};

	public void setSeleccion(CandadoSalida seleccion) {
		this.seleccion = seleccion;
	};

}
