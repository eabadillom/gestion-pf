package mx.com.ferbo.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.PrimeFaces;

import mx.com.ferbo.dao.UnidadDeManejoDAO;
import mx.com.ferbo.model.UnidadDeManejo;

@Named
@ViewScoped
public class UnidadDeManejoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<UnidadDeManejo> listaUnidadManejo;

	private List<UnidadDeManejo> listaUnidadManejoSelected;

	private UnidadDeManejo unidadManejoSelected;

	private UnidadDeManejoDAO unidadManejoDao;

	public UnidadDeManejoBean() {
		unidadManejoDao = new UnidadDeManejoDAO();
		listaUnidadManejoSelected = new ArrayList<>();
	}

	@PostConstruct
	public void init() {
		listaUnidadManejo = unidadManejoDao.buscarTodos();
	}

	public void nuevaUnidadManejo() {
		this.unidadManejoSelected = new UnidadDeManejo();
	}

	public void guardarUnidadManejo() {
		if (this.unidadManejoSelected.getUnidadDeManejoCve() == null) {
			if (unidadManejoDao.guardar(unidadManejoSelected) == null) {
				this.listaUnidadManejo.add(this.unidadManejoSelected);
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Unidad de Medida Agregada"));
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Error", "Ocurrió un error al intentar guardar la Medida Agregada"));
			}
		} else {
			if (unidadManejoDao.actualizar(unidadManejoSelected) == null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Unidad de Medida Actualizado"));
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Error", "Ocurrió un error al intentar actualizar la Unidad de Medida"));
			}
		}
		PrimeFaces.current().executeScript("PF('nuevaUnidadManejoDialog').hide()");
		PrimeFaces.current().ajax().update("form");
	}

	public void eliminandoUnidadManejo() {
		if (unidadManejoDao.eliminar(unidadManejoSelected) == null) {
			this.listaUnidadManejo.remove(this.unidadManejoSelected);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Unidad de Medida Eliminada"));
			PrimeFaces.current().ajax().update("form:messages", "form:dt-manejo");
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
					"Ocurrió un error al intentar eliminar la Unidad de Medida"));
		}
		PrimeFaces.current().executeScript("PF('deleteUnidadMedidaDialog').hide()");
		PrimeFaces.current().ajax().update("form:messages");
	}

	public List<UnidadDeManejo> getListaUnidadManejo() {
		return listaUnidadManejo;
	}

	public void setListaUnidadManejo(List<UnidadDeManejo> listaUnidadManejo) {
		this.listaUnidadManejo = listaUnidadManejo;
	}

	public List<UnidadDeManejo> getListaUnidadManejoSelected() {
		return listaUnidadManejoSelected;
	}

	public void setListaUnidadManejoSelected(List<UnidadDeManejo> listaUnidadManejoSelected) {
		this.listaUnidadManejoSelected = listaUnidadManejoSelected;
	}

	public UnidadDeManejo getUnidadManejoSelected() {
		return unidadManejoSelected;
	}

	public void setUnidadManejoSelected(UnidadDeManejo unidadManejoSelected) {
		this.unidadManejoSelected = unidadManejoSelected;
	}

	public UnidadDeManejoDAO getUnidadManejoDao() {
		return unidadManejoDao;
	}

	public void setUnidadManejoDao(UnidadDeManejoDAO unidadManejoDao) {
		this.unidadManejoDao = unidadManejoDao;
	}

}
