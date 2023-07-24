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

import mx.com.ferbo.dao.ServicioDAO;
import mx.com.ferbo.dao.TipoCobroDAO;
import mx.com.ferbo.model.Servicio;
import mx.com.ferbo.model.TipoCobro;

@Named
@ViewScoped
public class ServiciosBean implements Serializable{

	private static final long serialVersionUID = -4777843305394525276L;

	private List<Servicio> servicios;

	private List<Servicio> selectedServicios;
	
	private List<TipoCobro> listadoTipoCobro;

	private Servicio selectedServicio;
	
	private ServicioDAO servicioDAO;
	private TipoCobroDAO tipoCobroDAO;
	
	public ServiciosBean() {
		servicioDAO = new ServicioDAO();
		tipoCobroDAO = new TipoCobroDAO();
		selectedServicios = new ArrayList<>();
	}
	
	@PostConstruct
	public void init() {
		servicios = servicioDAO.buscarTodos();
		listadoTipoCobro = tipoCobroDAO.buscarTodos();
	}
	
	public void openNew() {
		this.selectedServicio = new Servicio();
	}

	public void saveServicio() {
		if (this.selectedServicio.getServicioCve() == null) {
			if (servicioDAO.guardar(selectedServicio) == null) {
				this.servicios.add(this.selectedServicio);
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Servicio Agregado"));
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error" ,"Ocurrió un error al intentar guardar el Servicio"));
			}

		} else {
			if (servicioDAO.actualizar(selectedServicio) == null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Servicio Actualizado"));
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error" ,"Ocurrió un error al intentar actualizar el Servicio"));
			}
		}

		PrimeFaces.current().executeScript("PF('manageServicioDialog').hide()");
		PrimeFaces.current().ajax().update("form:messages", "form:dt-servicios");
	}

	public void deleteServicio() {
		if (servicioDAO.eliminar(selectedServicio) == null) {
			this.servicios.remove(this.selectedServicio);
			this.selectedServicio = null;
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Servicio Eliminado"));
			PrimeFaces.current().ajax().update("form:messages", "form:dt-servicios");
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error" ,"Ocurrió un error al intentar eliminar el Servicio"));
			PrimeFaces.current().ajax().update("form:messages");
		}
	}

	public String getDeleteButtonMessage() {
		if (hasSelectedServicios()) {
			int size = this.selectedServicios.size();
			return size > 1 ? size + " servicios seleccionados" : "1 servicio seleccionado";
		}

		return "Eliminar";
	}

	public boolean hasSelectedServicios() {
		return this.selectedServicios != null && !this.selectedServicios.isEmpty();
	}

	public void deleteSelectedServicios() {
		if (servicioDAO.eliminarListado(selectedServicios) == null) {
			this.servicios.removeAll(this.selectedServicios);
			this.selectedServicios = null;
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Servicios Eliminados"));
			PrimeFaces.current().ajax().update("form:messages", "form:dt-servicios");
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error" ,"Ocurrió un error al intentar eliminar los Servicios"));
			PrimeFaces.current().ajax().update("form:messages");
		}
	}

	public List<Servicio> getServicios() {
		return servicios;
	}

	public void setServicios(List<Servicio> servicios) {
		this.servicios = servicios;
	}

	public Servicio getSelectedServicio() {
		return selectedServicio;
	}

	public void setSelectedServicio(Servicio selectedServicio) {
		this.selectedServicio = selectedServicio;
	}

	public List<Servicio> getSelectedServicios() {
		return selectedServicios;
	}

	public void setSelectedServicios(List<Servicio> selectedServicios) {
		this.selectedServicios = selectedServicios;
	}

	public List<TipoCobro> getListadoTipoCobro() {
		return listadoTipoCobro;
	}

	public void setListadoTipoCobro(List<TipoCobro> listadoTipoCobro) {
		this.listadoTipoCobro = listadoTipoCobro;
	}
	
	

}
