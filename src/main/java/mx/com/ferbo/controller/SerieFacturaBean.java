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

	private List<SerieFactura> series;
	private List<StatusSerie> status;
	private List<EmisoresCFDIS> emisores;
	
	private Usuario usuario;
	private FacesContext faceContext;
	private HttpServletRequest httpServletRequest;

	private SerieFactura nuevo;
	private SerieFactura seleccion;
	private EmisoresCFDIS emisor;

	private SerieFacturaDAO daoSerie;
	private EmisoresCFDISDAO emisorDAO;

	public SerieFacturaBean() {
		daoSerie = new SerieFacturaDAO();
		seleccion = new SerieFactura();
		emisorDAO = new EmisoresCFDISDAO();
	}
	
	@PostConstruct
	public void init() {
		faceContext = FacesContext.getCurrentInstance();
		httpServletRequest = (HttpServletRequest) faceContext.getExternalContext().getRequest();
		usuario = (Usuario) httpServletRequest.getSession(false).getAttribute("usuario");
		
		series = daoSerie.findAll(true);
		status = daoSerie.findStatus();
		emisores = emisorDAO.buscarTodos(true);
		log.info("El usuario {} entra a Facturación / Catálogos / Series / Series de Facturas...", this.usuario.getUsuario());
	}

	public void openNew() {
		nuevo = new SerieFactura();
		nuevo.setEmisor(this.emisor);
	}
	
	public void filtraSeries () {
		log.info("Emisor seleccionado: {}", this.emisor);
		if(this.emisor == null)
			this.series = daoSerie.findAll();
		else
			this.series = daoSerie.buscarPorEmisor(emisor, true);
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
			
			series.clear();
			series = daoSerie.findAll();
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
			daoSerie.actualizar(seleccion);
			
			log.info("El usuario {} actualiza la serie factura {}", this.usuario.getUsuario(), this.seleccion);
			
			series.clear();
			series = daoSerie.findAll();
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
	}
	
	public void cancelar() {
		PrimeFaces.current().executeScript("PF('dg-delete').hide()");
		String message = daoSerie.cancelar(seleccion.getId());
		
		if (message == null) {
			series.clear();
			series = daoSerie.findAll();
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

	public List<SerieFactura> getSeries() {
		return series;
	}

	public void setSeries(List<SerieFactura> series) {
		this.series = series;
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
	
	public List<EmisoresCFDIS> getEmisores() {
		return emisores;
	}

	public void setEmisores(List<EmisoresCFDIS> emisores) {
		this.emisores = emisores;
	}

	public EmisoresCFDIS getEmisor() {
		return emisor;
	}

	public void setEmisor(EmisoresCFDIS emisor) {
		this.emisor = emisor;
	}
}
