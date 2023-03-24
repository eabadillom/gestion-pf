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

import mx.com.ferbo.dao.PaisesDAO;
import mx.com.ferbo.model.Paises;

@Named
@ViewScoped
public class PaisesBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private List<Paises> listaPaises;
	
	private List<Paises> listaPaisesSelect;
	
	private Paises paisSelect;
	
	private PaisesDAO paisesDao;
	
	/**
	 * Se inicializan las variables
	 */
	public PaisesBean() {
		paisesDao = new PaisesDAO();
		listaPaisesSelect = new ArrayList<>();
	}
	
	/**
	 * En caso de ser necesario se asignan datos
	 */
	@PostConstruct
	public void init() {
		listaPaises = paisesDao.buscarTodos();
	}

	public void nuevoPais() {
		this.paisSelect = new Paises();
	}
	
	public void guardarPais() {
		if (this.paisSelect.getPaisCve() == null) {
			int tamanioListaPaises = listaPaises.size()+1;
			paisSelect.setPaisCve(tamanioListaPaises);
			if (paisesDao.guardar(paisSelect) == null) {
				this.listaPaises.add(this.paisSelect);
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("País Agregado"));
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Error", "Ocurrió un error al intentar guardar el País"));
			}
		} else {
			if (paisesDao.actualizar(paisSelect) == null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("País Actualizado"));
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Error", "Ocurrió un error al intentar actualizar el País"));
			}
		}
		PrimeFaces.current().executeScript("PF('nuevoPaisDialog').hide()");
		PrimeFaces.current().ajax().update("form");
	}
	
	public void eliminandoPais() {
		if (paisesDao.eliminar(paisSelect) == null) {
			this.listaPaises.remove(this.paisSelect);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Pais Eliminado"));
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
					"Ocurrió un error al intentar eliminar el Pais"));
		}
		PrimeFaces.current().executeScript("PF('deletePaiesDialog').hide()");
		PrimeFaces.current().ajax().update("form");
	}

	public List<Paises> getListaPaises() {
		return listaPaises;
	}

	public void setListaPaises(List<Paises> listaPaises) {
		this.listaPaises = listaPaises;
	}

	public List<Paises> getListaPaisesSelect() {
		return listaPaisesSelect;
	}

	public void setListaPaisesSelect(List<Paises> listaPaisesSelect) {
		this.listaPaisesSelect = listaPaisesSelect;
	}

	public Paises getPaisSelect() {
		return paisSelect;
	}

	public void setPaisSelect(Paises paisSelect) {
		this.paisSelect = paisSelect;
	}

	public PaisesDAO getPaisesDao() {
		return paisesDao;
	}

	public void setPaisesDao(PaisesDAO paisesDao) {
		this.paisesDao = paisesDao;
	}
}
