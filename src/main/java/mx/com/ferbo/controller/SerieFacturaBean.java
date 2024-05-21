package mx.com.ferbo.controller;

import java.io.Serializable;
import java.util.List;

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

import mx.com.ferbo.dao.EmisoresCFDISDAO;
import mx.com.ferbo.dao.SerieFacturaDAO;
import mx.com.ferbo.model.EmisoresCFDIS;
import mx.com.ferbo.model.SerieFactura;
import mx.com.ferbo.model.StatusSerie;
import mx.com.ferbo.model.Usuario;
import mx.com.ferbo.util.InventarioException;

@Named
@ViewScoped
public class SerieFacturaBean implements Serializable {

	private static final long serialVersionUID = 1;
	private static final Logger log = LogManager.getLogger(SerieFacturaBean.class);

	private List<SerieFactura> listSerie;
	private List<StatusSerie> status;
	private List<EmisoresCFDIS> emisores;
	
	private Usuario usuario;
	private FacesContext faceContext;
	private HttpServletRequest httpServletRequest;

	public List<EmisoresCFDIS> getEmisores() {
		return emisores;
	}

	public void setEmisores(List<EmisoresCFDIS> emisores) {
		this.emisores = emisores;
	}

	private SerieFactura nuevo;
	private SerieFactura seleccion;

	private SerieFacturaDAO daoSerie;
	private EmisoresCFDISDAO emisorDAO;

	public SerieFacturaBean() {
		daoSerie = new SerieFacturaDAO();
		listSerie = daoSerie.findAll();
		status = daoSerie.findStatus();
		seleccion = new SerieFactura();
		emisorDAO = new EmisoresCFDISDAO();
	}
	
	@PostConstruct
	public void init() {
		faceContext = FacesContext.getCurrentInstance();
		httpServletRequest = (HttpServletRequest) faceContext.getExternalContext().getRequest();
		usuario = (Usuario) httpServletRequest.getSession(false).getAttribute("usuario");
		
		emisores = emisorDAO.findall();
		log.info("El usuario {} entra a Facturación / Catálogos / Series / Series de Facturas...", this.usuario.getUsuario());
	}

	public void openNew() {
		nuevo = new SerieFactura();
	}

	public void save() {
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		String titulo = "Serie de factura";
		
		String result = null;
		
		try {
			result = daoSerie.save(nuevo);
			
			if(result != null)
				throw new InventarioException("Ocurrió un problema al guardar la información.");
			
			log.info("El usuario {} guarda la serie factura {}", this.usuario.getUsuario(), this.nuevo);
			
			listSerie.clear();
			listSerie = daoSerie.findAll();
			nuevo = new SerieFactura();
			
			PrimeFaces.current().executeScript("PF('dg-agrega').hide()");
			
			mensaje = "La información se guardó correctamente.";
			severity = FacesMessage.SEVERITY_INFO;
			
			
		} catch (InventarioException ex) {
			mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_WARN;
		} catch (Exception ex) {
			log.error("Problema para cargar la información del cliente...", ex);
			mensaje = "Ocurrió un problema al guardar la información.";
			severity = FacesMessage.SEVERITY_ERROR;
		} finally {
			message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
			PrimeFaces.current().ajax().update("form:messages", "form:dtSerieFac");
		} 
	}

	public void update() {
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		String titulo = "Serie de factura";
		
		String result = null;
		
		try {
			result = daoSerie.update(seleccion);
			
			if(result != null)
				throw new InventarioException("Ocurrió un problema al guardar la información.");
			
			log.info("El usuario {} actualiza la serie factura {}", this.usuario.getUsuario(), this.seleccion);
			
			listSerie.clear();
			listSerie = daoSerie.findAll();
			this.seleccion = new SerieFactura();
			
			PrimeFaces.current().executeScript("PF('dg-modifica').hide()");
			
			mensaje = "La información se guardó correctamente.";
			severity = FacesMessage.SEVERITY_INFO;
			
			
		} catch (InventarioException ex) {
			mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_WARN;
		} catch (Exception ex) {
			log.error("Problema para cargar la información del cliente...", ex);
			mensaje = "Ocurrió un problema al guardar la información.";
			severity = FacesMessage.SEVERITY_ERROR;
		} finally {
			message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
			PrimeFaces.current().ajax().update("form:messages", "form:dtSerieFac");
		} 
		
		
		
		
		
		
		
//		PrimeFaces.current().executeScript("PF('dg-modifica').hide()");
//		String message = daoSerie.update(seleccion);
//
//		if (message == null) {
//			listSerie.clear();
//			listSerie = daoSerie.findAll();
//			FacesContext.getCurrentInstance().addMessage(null,
//					new FacesMessage(FacesMessage.SEVERITY_INFO, "Serie Modificada " + seleccion.getNomSerie(), null));
//			PrimeFaces.current().ajax().update("form:messages", "form:dtSerieFac");
//		} else {
//			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
//					"Error al modificar " + seleccion.getNomSerie(), message));
//			PrimeFaces.current().ajax().update("form:messages");
//		}
//		this.seleccion = new SerieFactura();
	}
	
	public void cancelar() {
		PrimeFaces.current().executeScript("PF('dg-delete').hide()");
		String message = daoSerie.cancelar(seleccion.getId());
		
		if (message == null) {
			listSerie.clear();
			listSerie = daoSerie.findAll();
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Serie Cancelada " + seleccion.getNomSerie(), null));
			PrimeFaces.current().ajax().update("form:messages", "form:dtSerieFac");
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error al cancelar " + seleccion.getNomSerie(), message));
			PrimeFaces.current().ajax().update("form:messages");
		}
		this.seleccion = new SerieFactura();
	}

	public List<SerieFactura> getListSerie() {
		return listSerie;
	}

	public void setListSerie(List<SerieFactura> listSerie) {
		this.listSerie = listSerie;
	}

	public SerieFactura getNuevo() {
		return nuevo;
	}

	public void setNuevo(SerieFactura nuevo) {
		this.nuevo = nuevo;
	}

	public List<StatusSerie> getStatus() {
		return status;
	}

	public SerieFactura getSeleccion() {
		return seleccion;
	}

	public void setSeleccion(SerieFactura seleccion) {
		this.seleccion = seleccion;
	}
}
