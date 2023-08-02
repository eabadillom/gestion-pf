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

import mx.com.ferbo.dao.CamaraDAO;
import mx.com.ferbo.dao.PlantaDAO;
import mx.com.ferbo.model.Camara;
import mx.com.ferbo.model.Planta;

@Named
@ViewScoped
public class CamarasBean implements Serializable {

	private static final long serialVersionUID = -1785488265380235016L;

	private Camara camaraSelect;
	private List<Camara> lstCamaras;
	private List<Camara> lstCamarasSelected;
	private List<Planta> lstPlantas;
	private CamaraDAO camaraDAO;
	private PlantaDAO plantaDAO;

	/**
	 * Se inicializan las variables
	 */
	public CamarasBean() {
		camaraDAO = new CamaraDAO();
		plantaDAO = new PlantaDAO();
		lstCamarasSelected = new ArrayList<>();
	}

	/**
	 * En caso de ser necesario se asignan datos
	 */
	@PostConstruct
	public void init() {
		lstCamaras = camaraDAO.findall(true);
		lstPlantas = plantaDAO.findall();
	}

	/**
	 * Método para inicializar objeto tipo Camara
	 */
	public void nuevaCamara() {
		this.camaraSelect = new Camara();
	}

	/**
	 * Método para guardar/actualizar objeto tipo Camara
	 */
	public void guardarCamara() {
		if (this.camaraSelect.getCamaraCve() == null) {
			if (camaraDAO.guardar(camaraSelect) == null) {
				this.lstCamaras.add(this.camaraSelect);
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Cámara Agregada"));
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Error", "Ocurrió un error al intentar guardar la Cámara"));
			}

		} else {
			if (camaraDAO.actualizar(camaraSelect) == null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Cámara Actualizada"));
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Error", "Ocurrió un error al intentar actualizar la Cámara"));
			}
		}

		PrimeFaces.current().executeScript("PF('camaraDialog').hide()");
		PrimeFaces.current().ajax().update("form:messages", "form:dt-camaras");
	}

	/**
	 * Método para eliminar objeto tipo Camara
	 */
	public void eliminarCamara() {
		if (camaraDAO.eliminar(camaraSelect) == null) {
			this.lstCamaras.remove(this.camaraSelect);
			this.camaraSelect = null;
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Cámara Eliminada"));
			PrimeFaces.current().ajax().update("form:messages", "form:dt-camaras");
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
					"Ocurrió un error al intentar eliminar la Cámara"));
			PrimeFaces.current().ajax().update("form:messages");
		}

	}

	/**
	 * Método para consultar mensaje de eliminación para botón eliminar varios
	 */
	public String getConsultaMensajeBtn() {
		if (camaraSeleccionada()) {
			int size = this.lstCamarasSelected.size();
			return size > 1 ? size + " cámaras seleccionadas" : "1 cámara seleccionada";
		}

		return "Eliminar";
	}

	/**
	 * Método para validar si se ha seleccionado uno o varios objetos tipo Camara
	 */
	public boolean camaraSeleccionada() {
		return this.lstCamarasSelected != null && !this.lstCamarasSelected.isEmpty();
	}

	/**
	 * Método para eliminar listado de objetos tipo Camara
	 */
	public void eliminarCamarasSeleccionadas() {
		if (camaraDAO.eliminarListado(lstCamarasSelected) == null) {
			this.lstCamarasSelected.removeAll(this.lstCamarasSelected);
			this.camaraSelect = null;
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Cámaras Eliminadas"));
			PrimeFaces.current().ajax().update("form:messages", "form:dt-camaras");
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
					"Ocurrió un error al intentar eliminar las Cámaras"));
			PrimeFaces.current().ajax().update("form:messages");
		}
	}

	/**
	 * Gettes & Setters
	 */

	public Camara getCamaraSelect() {
		return camaraSelect;
	}

	public void setCamaraSelect(Camara camaraSelect) {
		this.camaraSelect = camaraSelect;
	}

	public List<Camara> getLstCamaras() {
		return lstCamaras;
	}

	public List<Camara> getLstCamarasSelected() {
		return lstCamarasSelected;
	}

	public void setLstCamarasSelected(List<Camara> lstCamarasSelected) {
		this.lstCamarasSelected = lstCamarasSelected;
	}

	public List<Planta> getLstPlantas() {
		return lstPlantas;
	}

}
