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
import mx.com.ferbo.ui.ServicioUI;


@Named
@ViewScoped
public class ServiciosBean implements Serializable{

	private static final long serialVersionUID = -4777843305394525276L;

	private List<ServicioUI> servicios;

	private List<ServicioUI> selectedServicios;
	
	private List<TipoCobro> listadoTipoCobro;

	private ServicioUI selectedServicio;
	
	private ServicioDAO servicioDAO;
	private TipoCobroDAO tipoCobroDAO;
	
	//private boolean servicioCheck;
	
	public ServiciosBean() {
		servicioDAO = new ServicioDAO();
		tipoCobroDAO = new TipoCobroDAO();
		selectedServicios = new ArrayList<>();
	}
	
	@PostConstruct
	public void init() {
		List<Servicio> servicios = servicioDAO.buscarTodos();
		listadoTipoCobro = tipoCobroDAO.buscarTodos();
		if(this.servicios == null) {
			this.servicios = new ArrayList<>();
		}
		for(Servicio s:servicios){
			ServicioUI servicioui = new ServicioUI(s);
			this.servicios.add(servicioui);
		}
	}
	
	public void openNew() {
		this.selectedServicio = new ServicioUI();
	}

	public void saveServicio() {
		Servicio servicio = new Servicio();
		
		servicio.setCdUnidad(selectedServicio.getCdUnidad());
		servicio.setServicioCod(selectedServicio.getServicioCod());
		servicio.setServicioCve(selectedServicio.getServicioCve());
		servicio.setServicioDs(selectedServicio.getServicioDs());
		servicio.setUuId(selectedServicio.getUuId());
		servicio.setCobro(selectedServicio.getCobro());
		
		if (this.selectedServicio.getServicioCve() == null) {
			
			
			if (servicioDAO.guardar(servicio) == null) {
				selectedServicio = new ServicioUI(servicio);//usando datos de servicio a guardar
				this.servicios.add(this.selectedServicio);
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Servicio Agregado"));
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error" ,"Ocurrió un error al intentar guardar el Servicio"));
			}

		} else {
			if (servicioDAO.actualizar(servicio) == null) {
				
				
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Servicio Actualizado"));
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error" ,"Ocurrió un error al intentar actualizar el Servicio"));
			}
		}

		PrimeFaces.current().executeScript("PF('manageServicioDialog').hide()");
		PrimeFaces.current().ajax().update("form:messages", "form:dt-servicios");
	}

	public void deleteServicio() {
		Servicio servicio = new Servicio();
		
		servicio.setCdUnidad(selectedServicio.getCdUnidad());
		servicio.setServicioCod(selectedServicio.getServicioCod());
		servicio.setServicioCve(selectedServicio.getServicioCve());
		servicio.setServicioDs(selectedServicio.getServicioDs());
		servicio.setUuId(selectedServicio.getUuId());
		servicio.setCobro(selectedServicio.getCobro());
		
		if (servicioDAO.eliminar(servicio) == null) {
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
		List<Servicio> listaservicio = new ArrayList<>();
		for(ServicioUI s:selectedServicios) {
			Servicio servicio = new Servicio();
			servicio.setCdUnidad(s.getCdUnidad());
			servicio.setServicioCod(s.getServicioCod());
			servicio.setServicioCve(s.getServicioCve());
			servicio.setServicioDs(s.getServicioDs());
			servicio.setUuId(s.getUuId());
			servicio.setCobro(s.getCobro());
			listaservicio.add(servicio);
		}
		
		if (servicioDAO.eliminarListado(listaservicio) == null) {
			this.servicios.removeAll(this.selectedServicios);
			this.selectedServicios = null;
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Servicios Eliminados"));
			PrimeFaces.current().ajax().update("form:messages", "form:dt-servicios");
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error" ,"Ocurrió un error al intentar eliminar los Servicios"));
			PrimeFaces.current().ajax().update("form:messages");
		}
	}
	

	public List<ServicioUI> getServicios() {
		return servicios;
	}

	public void setServicios(List<ServicioUI> servicios) {
		this.servicios = servicios;
	}

	public ServicioUI getSelectedServicio() {
		return selectedServicio;
	}

	public void setSelectedServicio(ServicioUI selectedServicio) {
		this.selectedServicio = selectedServicio;
	}

	public List<ServicioUI> getSelectedServicios() {
		/*if(selectedServicios==null || selectedServicios.isEmpty()) {
			selectedServicios.add(selectedServicio);
		}*/
		
		return selectedServicios;
	}

	public void setSelectedServicios(List<ServicioUI> selectedServicios) {
		this.selectedServicios = selectedServicios;
	}

	public List<TipoCobro> getListadoTipoCobro() {
		return listadoTipoCobro;
	}

	public void setListadoTipoCobro(List<TipoCobro> listadoTipoCobro) {
		this.listadoTipoCobro = listadoTipoCobro;
	}
	
	

}
