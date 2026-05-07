package mx.com.ferbo.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.PrimeFaces;

import mx.com.ferbo.dao.SerieNotaDAO;
import mx.com.ferbo.model.SerieNota;
import mx.com.ferbo.model.StatusSerie;
import mx.com.ferbo.util.InventarioException;

@Named
@ViewScoped
public class SerieNotaBean implements Serializable {

	private static final long serialVersionUID = 1;

	private List<SerieNota> listSerie;
	private List<StatusSerie> status;

	private SerieNota nuevo;
	private SerieNota seleccion;

	private SerieNotaDAO daoSerie;

	public SerieNotaBean() {
		daoSerie = new SerieNotaDAO();
		listSerie = daoSerie.findAll();
		status = daoSerie.findStatus();
		seleccion = new SerieNota();
		nuevo = new SerieNota();
	}

	public void openNew() {
		nuevo = new SerieNota();
	}

	public void save() {
		FacesMessage message = null;
		Severity severity = null;
		String titulo = null;
		String mensaje = null;
		
		try {
			daoSerie.guardar(nuevo);
			listSerie.clear();
			listSerie = daoSerie.findAll();
			nuevo = new SerieNota();
			PrimeFaces.current().executeScript("PF('dg-agrega').hide()");
			titulo = "Serie registrada";
			mensaje = String.format("Folio %s", nuevo.getNumeroActual());
			severity = FacesMessage.SEVERITY_INFO;
		} catch (InventarioException ex) {
			titulo = "Información incorrecta";
			mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_ERROR;
		} catch (Exception ex) {
			titulo = "Error al guardar la serie";
			mensaje = "Ha ocurrido un error en el sistema. Intente nuevamente.\nSi el problema persiste, comuniquese con su administrador del sistema.";
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
		String titulo = null;
		String mensaje = null;
		
		try {
			daoSerie.actualizar(seleccion);
			listSerie.clear();
			listSerie = daoSerie.findAll();
			PrimeFaces.current().executeScript("PF('dg-modifica').hide()");
			mensaje = String.format("Folio %s", seleccion.getNumeroActual());
			this.seleccion = new SerieNota();
			titulo = "Serie actualizada";
			severity = FacesMessage.SEVERITY_INFO;
		} catch (InventarioException ex) {
			titulo = "Información incorrecta";
			mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_ERROR;
		} catch (Exception ex) {
			titulo = "Error al guardar la serie";
			mensaje = "Ha ocurrido un error en el sistema. Intente nuevamente.\nSi el problema persiste, comuniquese con su administrador del sistema.";
			severity = FacesMessage.SEVERITY_ERROR;
		} finally {
			message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
			PrimeFaces.current().ajax().update("form:messages", "form:dtSerieFac");
		}
	}

	public void cancelar() {
		FacesMessage message = null;
		Severity severity = null;
		String titulo = null;
		String mensaje = null;
		
		StatusSerie cancelada = null;
		
		try {
			cancelada = status.stream().filter(item -> item.getId().equals(new Integer(3)))
					.findFirst().orElseThrow(() -> new InventarioException("Status incorrecto."));
			seleccion.setStatusSerie(cancelada);
			daoSerie.actualizar(seleccion);
			listSerie.clear();
			listSerie = daoSerie.findAll();
			seleccion = new SerieNota();
			
			PrimeFaces.current().executeScript("PF('dg-delete').hide()");
			titulo = "Serie cancelada";
			mensaje = String.format("Folio %s", seleccion.getNumeroActual());
			severity = FacesMessage.SEVERITY_INFO;
		} catch (InventarioException ex) {
			titulo = "Información incorrecta";
			mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_ERROR;
		} catch (Exception ex) {
			titulo = "Error al cancelar la serie";
			mensaje = "Ha ocurrido un error en el sistema. Intente nuevamente.\nSi el problema persiste, comuniquese con su administrador del sistema.";
			severity = FacesMessage.SEVERITY_ERROR;
		} finally {
			message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
			PrimeFaces.current().ajax().update("form:messages", "form:dtSerieFac");
		}

	}

	public List<SerieNota> getListSerie() {
		return listSerie;
	}

	public void setListSerie(List<SerieNota> listSerie) {
		this.listSerie = listSerie;
	}

	public SerieNota getNuevo() {
		return nuevo;
	}

	public void setNuevo(SerieNota nuevo) {
		this.nuevo = nuevo;
	}

	public List<StatusSerie> getStatus() {
		return status;
	}

	public SerieNota getSeleccion() {
		return seleccion;
	}

	public void setSeleccion(SerieNota seleccion) {
		this.seleccion = seleccion;
	}
}
