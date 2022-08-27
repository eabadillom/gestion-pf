package mx.com.ferbo.controller;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.PrimeFaces;

import mx.com.ferbo.dao.PlantaDAO;
import mx.com.ferbo.model.Planta;
import mx.com.ferbo.model.Usuario;

import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class PlantaBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private PlantaDAO daoPlanta;
	private List<Planta> list;
	private List<Usuario> usuarios;

	private Planta planta;
	private Planta seleccion;

	public PlantaBean() {
		daoPlanta = new PlantaDAO();
		list = daoPlanta.findall();
		usuarios = daoPlanta.getUsuarios();
		planta = new Planta();
		seleccion = new Planta();
	};

	public void openNew() {
		planta = new Planta();
	};

	public void save() {
		PrimeFaces.current().executeScript("PF('dg-agrega').hide()");
		String message = daoPlanta.save(planta);

		if (message == null) {
			list.clear();
			list = daoPlanta.findall();
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Planta Agregada " + planta.getPlantaDs(), null));
			PrimeFaces.current().ajax().update("form:messages", "form:dt-planta");
		} else {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al agregar " + planta.getPlantaDs(), message));
			PrimeFaces.current().ajax().update("form:messages");
		}
		this.planta = new Planta();
	};

	public void update() {
		PrimeFaces.current().executeScript("PF('dg-modifica').hide()");
		String message = daoPlanta.update(seleccion);

		if (message == null) {
			list.clear();
			list = daoPlanta.findall();
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Planta Moficada  " + seleccion.getPlantaDs(), null));
			PrimeFaces.current().ajax().update("form:messages", "form:dt-planta");
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error al modificar " + seleccion.getPlantaDs(), message));
			PrimeFaces.current().ajax().update("form:messages");
		}
		this.seleccion = new Planta();
	};

	public void delete() {
		PrimeFaces.current().executeScript("PF('dg-delete').hide()");
		String message = daoPlanta.delete(seleccion);

		if (message == null) {
			list.remove(this.seleccion);
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Planta Eliminado " + seleccion.getPlantaDs(), null));
			PrimeFaces.current().ajax().update("form:messages", "form:dt-planta");
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error al eliminar " + seleccion.getPlantaDs(), message));
			PrimeFaces.current().ajax().update("form:messages");
		}
	};

	public List<Planta> getList() {
		return list;
	};

	public void setList(List<Planta> list) {
		this.list = list;
	};

	public List<Usuario> getUsuarios() {
		return usuarios;
	};

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	};

	public Planta getPlanta() {
		return planta;
	};

	public void setPlanta(Planta planta) {
		this.planta = planta;
	};

	public Planta getSeleccion() {
		return seleccion;
	};

	public void setSeleccion(Planta seleccion) {
		this.seleccion = seleccion;
	};

}
