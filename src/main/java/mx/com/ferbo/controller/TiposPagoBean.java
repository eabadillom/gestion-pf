package mx.com.ferbo.controller;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.PrimeFaces;

import mx.com.ferbo.dao.TiposPagoDAO;
import mx.com.ferbo.model.MedioPago;

@Named
@ViewScoped
public class TiposPagoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private TiposPagoDAO result;
	private List<MedioPago> principal;
	private MedioPago nuevo;
	private MedioPago seleccion;

	public TiposPagoBean() {
		this.result = new TiposPagoDAO();
		this.principal = result.findAll();
		this.seleccion = new MedioPago();
	};

	public void openNew() {
		this.nuevo = new MedioPago();
		this.nuevo.setVigenciaInicio(new Date());
	};

	public void save() {
		PrimeFaces.current().executeScript("PF('dg-agrega').hide()");
		String message = result.save(this.nuevo);

		if (message == null) {
			principal.add(this.nuevo);
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Nuevo Tipo de Pago Agregado ", null));
			PrimeFaces.current().ajax().update("form:messages", "form:dt-tipos");
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error al agregar: " + nuevo.getMpDescripcion(), message));
			PrimeFaces.current().ajax().update("form:messages");
		}
		this.nuevo = null;
	};

	public void updateTipo() {
		PrimeFaces.current().executeScript("PF('dg-modifica').hide()");
		String message = result.update(this.seleccion);

		if (message == null) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Tipo de Pago Editado", null));
			PrimeFaces.current().ajax().update("form:messages", "form:dt-tipos");
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error al editar: " + nuevo.getMpDescripcion(), message));
			PrimeFaces.current().ajax().update("form:messages");
		}
		this.seleccion = new MedioPago();
	};

	public List<MedioPago> getPrincipal() {
		return principal;
	};

	public void setPrincipal(List<MedioPago> principal) {
		this.principal = principal;
	};

	public MedioPago getNuevo() {
		return nuevo;
	};

	public void setNuevo(MedioPago nuevo) {
		this.nuevo = nuevo;
	}

	public MedioPago getSeleccion() {
		return seleccion;
	}

	public void setSeleccion(MedioPago seleccion) {
		this.seleccion = seleccion;
	};

}
