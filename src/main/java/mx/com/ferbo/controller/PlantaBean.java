package mx.com.ferbo.controller;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.PrimeFaces;

import mx.com.ferbo.dao.PlantaDAO;
import mx.com.ferbo.model.Planta;
import mx.com.ferbo.model.Usuario;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Named
@ViewScoped
public class PlantaBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private PlantaDAO result;
	private List<Planta> principal;
	private Planta nuevo;
	private Planta seleccion;

	private List<SelectItem> listaUsuario;

	public PlantaBean() {
		this.result = new PlantaDAO();
		this.principal = result.findall();
		this.nuevo = new Planta();
		this.nuevo.setIdUsuario(new Usuario());
		this.seleccion = new Planta();
		this.seleccion.setIdUsuario(new Usuario());
	};

	public void openNew() {
		this.nuevo = new Planta();
		this.nuevo.setIdUsuario(new Usuario());
	};

	public void save() {
		PrimeFaces.current().executeScript("PF('dg-agrega').hide()");
		String message = result.save(nuevo);

		if (message == null) {
			principal.clear();
			principal = result.findall();
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Planta Agregada " + nuevo.getPlantaDs(), null));
			PrimeFaces.current().ajax().update("form:messages", "form:dt-planta");
		} else {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al agregar " + nuevo.getPlantaDs(), message));
			PrimeFaces.current().ajax().update("form:messages");
		}
		this.nuevo = new Planta();
		this.nuevo.setIdUsuario(new Usuario());
	};

	public void update() {
		PrimeFaces.current().executeScript("PF('dg-modifica').hide()");
		String message = result.update(seleccion);

		if (message == null) {
			principal.clear();
			principal = result.findall();
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Planta Moficada  " + seleccion.getPlantaDs(), null));
			PrimeFaces.current().ajax().update("form:messages", "form:dt-planta");
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error al modificar " + seleccion.getPlantaDs(), message));
			PrimeFaces.current().ajax().update("form:messages");
		}
		this.seleccion = new Planta();
		this.seleccion.setIdUsuario(new Usuario());
	};

	public void delete() {
		PrimeFaces.current().executeScript("PF('dg-delete').hide()");
		String message = result.delete(seleccion);

		if (message == null) {
			principal.remove(this.seleccion);
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Planta Eliminado " + seleccion.getPlantaDs(), null));
			PrimeFaces.current().ajax().update("form:messages", "form:dt-planta");
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error al eliminar " + seleccion.getPlantaDs(), message));
			PrimeFaces.current().ajax().update("form:messages");
		}
		this.nuevo = new Planta();
		this.nuevo.setIdUsuario(new Usuario());
	};

	public List<Planta> getPrincipal() {
		return principal;
	};

	public void setPrincipal(List<Planta> principal) {
		this.principal = principal;
	};

	public Planta getNuevo() {
		return nuevo;
	};

	public void setNuevo(Planta nuevo) {
		this.nuevo = nuevo;
	};

	public List<SelectItem> getListaUsuario() {
		this.listaUsuario = new ArrayList<SelectItem>();
		List<Usuario> u = result.getUsuarios();

		listaUsuario.clear();

		for (Usuario user : u) {
			if (user.getPerfil() == 1 || user.getPerfil() == 4) {
				SelectItem temp = new SelectItem(user.getId(), user.getUsuario());
				this.listaUsuario.add(temp);
			}
		}
		return listaUsuario;
	};

	public void setListaUsuario(List<SelectItem> listaUsuario) {
		this.listaUsuario = listaUsuario;
	};

	public Planta getSeleccion() {
		return seleccion;
	};

	public void setSeleccion(Planta seleccion) {
		this.seleccion = seleccion;

		if (Objects.isNull(seleccion.getIdUsuario())) {
			seleccion.setIdUsuario(new Usuario());
		}

		PrimeFaces.current().ajax().update("form:frm-modifica");
	};

}
