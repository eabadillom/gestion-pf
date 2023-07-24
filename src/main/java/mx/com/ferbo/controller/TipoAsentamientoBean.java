package mx.com.ferbo.controller;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.PrimeFaces;

import mx.com.ferbo.dao.TipoAsentamientoDAO;
import mx.com.ferbo.model.TipoAsentamiento;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named
@ViewScoped
public class TipoAsentamientoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<TipoAsentamiento> listaTipoAsentamiento;
	private List<TipoAsentamiento> listaTipoAsentamientoSelected;

	private TipoAsentamiento tipoAsentamientoSelect;

	private TipoAsentamientoDAO tipoAsentamientoDAO;

	/**
	 * Se inicializan las variables
	 */
	public TipoAsentamientoBean() {
		tipoAsentamientoDAO = new TipoAsentamientoDAO();
		listaTipoAsentamientoSelected = new ArrayList<>();
	}

	/**
	 * En caso de ser necesario se asignan datos
	 */
	@PostConstruct
	public void init() {
		listaTipoAsentamiento = tipoAsentamientoDAO.buscarTodos();
	}

	public void nuevoTipoAsentamiento() {
		this.tipoAsentamientoSelect = new TipoAsentamiento();
	}

	public void guardarTipoAsentamiento() {
		if (this.tipoAsentamientoSelect.getTipoasntmntoCve() == null) {
			List<TipoAsentamiento> listaTmpTipoAsentamiento = tipoAsentamientoDAO.buscarTodos();
			listaTmpTipoAsentamiento.remove(listaTmpTipoAsentamiento.size()-2);
			int tamanioListaTipoAsentamiento = listaTmpTipoAsentamiento.size();
			tipoAsentamientoSelect.setTipoasntmntoCve((short)tamanioListaTipoAsentamiento);
			if (tipoAsentamientoDAO.guardar(tipoAsentamientoSelect) == null) {
				this.listaTipoAsentamiento.add(this.tipoAsentamientoSelect);
				String ab = tipoAsentamientoSelect.getTipoasntmntoDsCorta();
				ab.trim();
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Tipo de Asentamiento Agregado"));
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Error", "Ocurrió un error al intentar guardar el Tipo de Asentamiento"));
			}
		} else {
			if (tipoAsentamientoDAO.actualizar(tipoAsentamientoSelect) == null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Tipo de Asentamiento Actualizado"));
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Error", "Ocurrió un error al intentar actualizar el Tipo de Asentamiento"));
			}
		}
		PrimeFaces.current().executeScript("PF('nuevoTipoAsentamientoDialog').hide()");
		PrimeFaces.current().ajax().update("form");
	}

	public void eliminandoTipoAsentamiento() {
		if (tipoAsentamientoDAO.eliminar(tipoAsentamientoSelect) == null) {
			this.listaTipoAsentamiento.remove(this.tipoAsentamientoSelect);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Tipo de Asentamiento Eliminado"));
			PrimeFaces.current().ajax().update("form:messages", "form:dt-TipoAsentamiento");
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
					"Ocurrió un error al intentar eliminar el Tipo de Asentamiento"));
		}
		PrimeFaces.current().executeScript("PF('deleteTipoAsentamientoDialog').hide()");
		PrimeFaces.current().ajax().update("form:messages");
	}

	public List<TipoAsentamiento> getListaTipoAsentamiento() {
		return listaTipoAsentamiento;
	}

	public void setListaTipoAsentamiento(List<TipoAsentamiento> listaTipoAsentamiento) {
		this.listaTipoAsentamiento = listaTipoAsentamiento;
	}

	public List<TipoAsentamiento> getListaTipoAsentamientoSelected() {
		return listaTipoAsentamientoSelected;
	}

	public void setListaTipoAsentamientoSelected(List<TipoAsentamiento> listaTipoAsentamientoSelected) {
		this.listaTipoAsentamientoSelected = listaTipoAsentamientoSelected;
	}

	public TipoAsentamiento getTipoAsentamientoSelect() {
		return tipoAsentamientoSelect;
	}

	public void setTipoAsentamientoSelect(TipoAsentamiento tipoAsentamientoSelect) {
		this.tipoAsentamientoSelect = tipoAsentamientoSelect;
	}

}
