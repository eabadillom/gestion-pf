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

import mx.com.ferbo.dao.EstadosDAO;
import mx.com.ferbo.dao.PaisesDAO;
import mx.com.ferbo.model.Estados;
import mx.com.ferbo.model.EstadosPK;
import mx.com.ferbo.model.Pais;
import mx.com.ferbo.model.Paises;

@Named
@ViewScoped
public class EstadosBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<Paises> listaPaises;
	private List<Estados> listaEstados;

	private List<Estados> listaEstadosSelect;

	private Paises paisSelect;
	private EstadosPK estadoPkSelect;
	private Estados estadoSelect;

	private PaisesDAO paisesDao;
	private EstadosDAO estadosDao;

	private int idPais;

	public EstadosBean() {
		paisesDao = new PaisesDAO();
		estadosDao = new EstadosDAO();
		listaPaises = new ArrayList<>();
		listaEstados = new ArrayList<>();
	}

	@PostConstruct
	public void init() {
		listaPaises = paisesDao.buscarTodos();
		this.paisSelect = new Paises();
		this.estadoSelect = new Estados();
		this.estadoPkSelect = new EstadosPK();
	}

	public void nuevoEstado() {
		this.paisSelect = new Paises();
		this.estadoSelect = new Estados();
		this.estadoPkSelect = new EstadosPK();
		this.estadoPkSelect.setPais(new Paises());
                estadoSelect.setEstadosPK(estadoPkSelect);
	}

	public void guardarEstado() {
		if (this.estadoSelect.getEstadosPK().getEstadoCve() == 0) {
			this.estadoPkSelect.getPais().setPaisCve(idPais);
			this.estadoSelect.setEstadosPK(estadoPkSelect);
			List<Estados> listaEstadosPais = estadosDao.buscarPorCriteriosEstados(estadoSelect);
			int tamanioListaEstadosPais = listaEstadosPais.size() + 1;
			this.estadoPkSelect.setEstadoCve(tamanioListaEstadosPais);
			this.estadoSelect.setEstadosPK(estadoPkSelect);
			this.paisSelect.setPaisCve(idPais);
			this.estadoSelect.getEstadosPK().getPais().setPaisCve(paisSelect.getPaisCve());
			if (estadosDao.guardar(estadoSelect) == null) {
				listaEstados = estadosDao.buscarPorCriteriosEstados(estadoSelect);
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Estado Agregado"));
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Error", "Ocurrió un error al intentar guardar el Estado"));
			}
		} else {
			int idEstado = this.estadoSelect.getEstadosPK().getEstadoCve();
			this.estadoPkSelect = new EstadosPK();
			this.estadoPkSelect.getPais().setPaisCve(idPais);
			this.estadoPkSelect.setEstadoCve(idEstado);
			this.estadoSelect.setEstadosPK(estadoPkSelect);
			if (estadosDao.actualizar(estadoSelect) == null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Estado Actualizado"));
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Error", "Ocurrió un error al intentar actualizar el Estado"));
			}
		}
		PrimeFaces.current().executeScript("PF('nuevoEstadoDialog').hide()");
		PrimeFaces.current().ajax().update("form");
	}

	public void eliminandoEstado() {
		int idEstado = this.estadoSelect.getEstadosPK().getEstadoCve();
		estadoSelect = new Estados();
		estadoPkSelect = new EstadosPK();
		estadoPkSelect.setEstadoCve(idEstado);
		estadoPkSelect.getPais().setPaisCve(idPais);
		estadoSelect.setEstadosPK(estadoPkSelect);
		if (estadosDao.eliminar(estadoSelect) == null) {
			this.listaEstados.remove(this.estadoSelect);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Estado Eliminado"));
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
					"Ocurrió un error al intentar eliminar el Estado"));
		}
		PrimeFaces.current().executeScript("PF('deleteEstadoDialog').hide()");
		PrimeFaces.current().ajax().update("form");
	}

	public void handleContrySelect() {
		if (this.idPais != -1) {
			this.estadoPkSelect.getPais().setPaisCve(idPais);
			this.estadoSelect.setEstadosPK(estadoPkSelect);
			listaEstados = estadosDao.buscarPorCriteriosEstados(estadoSelect);
		}
		PrimeFaces.current().ajax().update("form");
	}

	public List<Paises> getListaPaises() {
		return listaPaises;
	}

	public void setListaPaises(List<Paises> listaPaises) {
		this.listaPaises = listaPaises;
	}

	public List<Estados> getListaEstados() {
		return listaEstados;
	}

	public void setListaEstados(List<Estados> listaEstados) {
		this.listaEstados = listaEstados;
	}

	public List<Estados> getListaEstadosSelect() {
		return listaEstadosSelect;
	}

	public void setListaEstadosSelect(List<Estados> listaEstadosSelect) {
		this.listaEstadosSelect = listaEstadosSelect;
	}

	public Paises getPaisSelect() {
		return paisSelect;
	}

	public void setPaisSelect(Paises paisSelect) {
		this.paisSelect = paisSelect;
	}

	public EstadosPK getEstadoPkSelect() {
		return estadoPkSelect;
	}

	public void setEstadoPkSelect(EstadosPK estadoPkSelect) {
		this.estadoPkSelect = estadoPkSelect;
	}

	public Estados getEstadoSelect() {
		return estadoSelect;
	}

	public void setEstadoSelect(Estados estadoSelect) {
		this.estadoSelect = estadoSelect;
	}

	public PaisesDAO getPaisesDao() {
		return paisesDao;
	}

	public void setPaisesDao(PaisesDAO paisesDao) {
		this.paisesDao = paisesDao;
	}

	public EstadosDAO getEstadosDao() {
		return estadosDao;
	}

	public void setEstadosDao(EstadosDAO estadosDao) {
		this.estadosDao = estadosDao;
	}

	public int getIdPais() {
		return idPais;
	}

	public void setIdPais(int idPais) {
		this.idPais = idPais;
	}

}
