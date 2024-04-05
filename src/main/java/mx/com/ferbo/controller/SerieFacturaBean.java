package mx.com.ferbo.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.PrimeFaces;

import mx.com.ferbo.dao.PlantaDAO;
import mx.com.ferbo.dao.SerieFacturaDAO;
import mx.com.ferbo.model.Planta;
import mx.com.ferbo.model.SerieFactura;
import mx.com.ferbo.model.StatusSerie;

@Named
@ViewScoped
public class SerieFacturaBean implements Serializable {

	private static final long serialVersionUID = 1;

	private List<SerieFactura> listSerie;
	private List<StatusSerie> status;
	private List<Planta> listaPlanta;

	private SerieFactura nuevo;
	private SerieFactura seleccion;

	private SerieFacturaDAO daoSerie;
	private PlantaDAO plantaDAO;

	public SerieFacturaBean() {
		daoSerie = new SerieFacturaDAO();
		listSerie = daoSerie.findAll();
		status = daoSerie.findStatus();
		seleccion = new SerieFactura();
		plantaDAO = new PlantaDAO();
		listaPlanta = plantaDAO.findall();
	};

	public void openNew() {
		nuevo = new SerieFactura();
	};

	public void save() {
		PrimeFaces.current().executeScript("PF('dg-agrega').hide()");
		String message = daoSerie.save(nuevo);

		if (message == null) {
			listSerie.clear();
			listSerie = daoSerie.findAll();
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Serie Factura Agregada ", null));
			PrimeFaces.current().ajax().update("form:messages", "form:dtSerieFac");
		} else {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al agregar serie", message));
			PrimeFaces.current().ajax().update("form:messages");
		}
		nuevo = new SerieFactura();
	};

	public void update() {
		PrimeFaces.current().executeScript("PF('dg-modifica').hide()");
		String message = daoSerie.update(seleccion);

		if (message == null) {
			listSerie.clear();
			listSerie = daoSerie.findAll();
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Serie Modificada " + seleccion.getNomSerie(), null));
			PrimeFaces.current().ajax().update("form:messages", "form:dtSerieFac");
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error al modificar " + seleccion.getNomSerie(), message));
			PrimeFaces.current().ajax().update("form:messages");
		}
		this.seleccion = new SerieFactura();
	};

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
	};

	public List<SerieFactura> getListSerie() {
		return listSerie;
	};

	public void setListSerie(List<SerieFactura> listSerie) {
		this.listSerie = listSerie;
	};

	public SerieFactura getNuevo() {
		return nuevo;
	};

	public void setNuevo(SerieFactura nuevo) {
		this.nuevo = nuevo;
	};

	public List<StatusSerie> getStatus() {
		return status;
	};

	public SerieFactura getSeleccion() {
		return seleccion;
	};

	public void setSeleccion(SerieFactura seleccion) {
		this.seleccion = seleccion;
	}

	public List<Planta> getListaPlanta() {
		return listaPlanta;
	}

	public void setListaPlanta(List<Planta> listaPlanta) {
		this.listaPlanta = listaPlanta;
	};

}
