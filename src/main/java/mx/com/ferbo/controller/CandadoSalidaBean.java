package mx.com.ferbo.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.PrimeFaces;

import mx.com.ferbo.dao.CandadoSalidaDAO;
import mx.com.ferbo.model.CandadoSalida;

@Named
@ViewScoped
public class CandadoSalidaBean implements Serializable {

	private static final long serialVersionUID = 1;

	private List<CandadoSalida> lista;
	private CandadoSalida seleccion;

	private CandadoSalidaDAO dao;

	public CandadoSalidaBean() {
		dao = new CandadoSalidaDAO();
		lista = dao.findAll();
		seleccion = new CandadoSalida();
	};

	public void update() {
		PrimeFaces.current().executeScript("PF('dg-modifica').hide()");
		String message = dao.update(seleccion);

		if (message == null) {
			lista.clear();
			lista = dao.findAll();
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Candado Salida modificado", null));
			PrimeFaces.current().ajax().update("form:messages", "form:dtCandado");
		} else {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al modificar", message));
			PrimeFaces.current().ajax().update("form:messages");
		}
		this.seleccion = new CandadoSalida();
	};

	public List<CandadoSalida> getLista() {
		return lista;
	};

	public void setLista(List<CandadoSalida> lista) {
		this.lista = lista;
	};

	public CandadoSalida getSeleccion() {
		return seleccion;
	};

	public void setSeleccion(CandadoSalida seleccion) {
		this.seleccion = seleccion;
	};

}
