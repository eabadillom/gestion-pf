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

import mx.com.ferbo.dao.TipoPagoDAO;
import mx.com.ferbo.model.TipoPago;

@Named
@ViewScoped
public class TipoPagoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<TipoPago> listaTipoPago;

	private List<TipoPago> listaTipoPagoSelected;

	private TipoPago tipoPagoSelected;

	private TipoPagoDAO tipoPagoDao;

	public TipoPagoBean() {
		tipoPagoDao = new TipoPagoDAO();
		listaTipoPagoSelected = new ArrayList<>();
	}

	@PostConstruct
	public void init() {
		listaTipoPago = tipoPagoDao.buscarTodos();
	}

	public void nuevoTipoPago() {
		this.tipoPagoSelected = new TipoPago();
	}

	public void guardarTipoPago() {
		if (this.tipoPagoSelected.getId() == null) {
			if (tipoPagoDao.guardar(tipoPagoSelected) == null) {
				this.listaTipoPago.add(this.tipoPagoSelected);
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Tipo de pago agregado"));
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Error", "Ocurrió un error al intentar guardar la Medida Agregada"));
			}
		} else {
			if (tipoPagoDao.actualizar(tipoPagoSelected) == null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Tipo de pago actualizado"));
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Error", "Ocurrió un error al intentar actualizar el tipo de pago."));
			}
		}
		PrimeFaces.current().executeScript("PF('nuevaTipoPagoDialog').hide()");
		PrimeFaces.current().ajax().update("form");
	}

	public void eliminandoTipoPago() {
		if (tipoPagoDao.eliminar(tipoPagoSelected) == null) {
			this.listaTipoPago.remove(this.tipoPagoSelected);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Tipo de pago eliminado"));
			PrimeFaces.current().ajax().update("form:messages", "form:dt-tipoPago");
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
					"Ocurrió un error al intentar eliminar el tipo de pago."));
		}
		PrimeFaces.current().executeScript("PF('deleteUnidadMedidaDialog').hide()");
		PrimeFaces.current().ajax().update("form:messages");
	}

	public List<TipoPago> getListaTipoPago() {
		return listaTipoPago;
	}

	public void setListaTipoPago(List<TipoPago> listaTipoPago) {
		this.listaTipoPago = listaTipoPago;
	}

	public List<TipoPago> getListaTipoPagoSelected() {
		return listaTipoPagoSelected;
	}

	public void setListaTipoPagoSelected(List<TipoPago> listaTipoPagoSelected) {
		this.listaTipoPagoSelected = listaTipoPagoSelected;
	}

	public TipoPago getTipoPagoSelected() {
		return tipoPagoSelected;
	}

	public void setTipoPagoSelected(TipoPago TipoPagoSelected) {
		this.tipoPagoSelected = TipoPagoSelected;
	}

	public TipoPagoDAO getTipoPagoDao() {
		return tipoPagoDao;
	}

	public void setTipoPagoDao(TipoPagoDAO TipoPagoDao) {
		this.tipoPagoDao = TipoPagoDao;
	}

}
