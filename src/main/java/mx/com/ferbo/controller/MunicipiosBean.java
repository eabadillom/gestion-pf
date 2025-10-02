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
import mx.com.ferbo.dao.MunicipiosDAO;
import mx.com.ferbo.dao.PaisesDAO;

import mx.com.ferbo.model.Estados;
import mx.com.ferbo.model.EstadosPK;
import mx.com.ferbo.model.Municipios;
import mx.com.ferbo.model.MunicipiosPK;
import mx.com.ferbo.model.Paises;

@Named
@ViewScoped
public class MunicipiosBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<Paises> listaPaises;
	private List<Estados> listaEstados;
	private List<Estados> listaTmpEstados;
	private List<Municipios> listaMunicipios;

	private List<Municipios> listaMunicipiosSelect;

	private Paises paisSelect;
	private EstadosPK estadoPkSelect;
	private Estados estadoSelect;
	private MunicipiosPK municipioPkSelect;
	private Municipios municipioSelect;

	private PaisesDAO paisesDao;
	private EstadosDAO estadosDao;
	private MunicipiosDAO municipiosDao;

	private int idPais;
	private int idEstado;

	public MunicipiosBean() {
		paisesDao = new PaisesDAO();
		estadosDao = new EstadosDAO();
		municipiosDao = new MunicipiosDAO();
		listaPaises = new ArrayList<>();
		listaEstados = new ArrayList<>();
		listaMunicipios = new ArrayList<>();
	}

	@PostConstruct
	public void init() {
		listaPaises = paisesDao.buscarTodos();
		this.paisSelect = new Paises();
		this.estadoSelect = new Estados();
		this.estadoPkSelect = new EstadosPK();
		this.municipioSelect = new Municipios();
		this.municipioPkSelect = new MunicipiosPK();
	}

	public void nuevoMunicipio() {
		this.paisSelect = new Paises();
		this.estadoSelect = new Estados();
		this.estadoPkSelect = new EstadosPK();
		estadoSelect.setEstadosPK(estadoPkSelect);
		this.municipioSelect = new Municipios();
		this.municipioPkSelect = new MunicipiosPK();
		municipioSelect.setMunicipiosPK(municipioPkSelect);
	}
	
	public void guardarMunicipio() {
		int tamanioListaMunicipioEstadoPais = 0;
		if (this.municipioSelect.getMunicipiosPK().getMunicipioCve() == 0) {
			municipioPkSelect.getEstados().getEstadosPK().getPais().setPaisCve(idPais);
			municipioPkSelect.getEstados().getEstadosPK().setEstadoCve(idEstado);
			List<Municipios> listaMunicipioEstadoPais = municipiosDao.buscarPorPaisEstado(municipioSelect);
			if (municipioSelect.getMunicipiosPK().getEstados().getEstadosPK().getEstadoCve() == 9) {
				tamanioListaMunicipioEstadoPais = listaMunicipioEstadoPais.size() + 2;
			} else {
				tamanioListaMunicipioEstadoPais = listaMunicipioEstadoPais.size() + 1;
			}
			municipioPkSelect.setMunicipioCve(tamanioListaMunicipioEstadoPais);
			municipioSelect.setMunicipiosPK(municipioPkSelect);
			if(municipiosDao.guardar(municipioSelect) == null) {
				this.listaMunicipios.add(this.municipioSelect);
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Municipio Agregado"));
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Error", "Ocurrió un error al intentar guardar el Municipio / Alcaldía"));
			}
		} else {
			if(municipiosDao.actualizar(municipioSelect) == null) {
//				this.listaMunicipios.add(this.municipioSelect);
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Municipio Actualizado"));
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Error", "Ocurrió un error al intentar actualizar el Municipio / Alcaldía"));
			}
		} 
		listaMunicipios = municipiosDao.buscarPorPaisEstado(municipioSelect);
		PrimeFaces.current().executeScript("PF('nuevoMunicipioDialog').hide()");
	}

	public void eliminandoMunicipio() {
		int idMunicipio = this.municipioSelect.getMunicipiosPK().getMunicipioCve();
		municipioSelect = new Municipios();
		municipioPkSelect = new MunicipiosPK();
		municipioPkSelect.getEstados().getEstadosPK().getPais().setPaisCve(idPais);
		municipioPkSelect.getEstados().getEstadosPK().setEstadoCve(idEstado);
		municipioPkSelect.setMunicipioCve(idMunicipio);
		municipioSelect.setMunicipiosPK(municipioPkSelect);
		if (municipiosDao.eliminar(municipioSelect) == null ) {
			this.listaMunicipios.remove(this.municipioSelect);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Municipio Eliminado"));
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error","Ocurrió un error al intentar eliminar el Municipio"));
		}
		PrimeFaces.current().executeScript("PF('deleteMunicipioDialog').hide()");
		PrimeFaces.current().ajax().update("form");
	}

	public void handleContrySelect() {
		if (this.idPais != -1) {
			this.estadoPkSelect.getPais().setPaisCve(idPais);
			this.estadoSelect.setEstadosPK(estadoPkSelect);
			listaEstados = estadosDao.buscarPorCriteriosEstados(estadoSelect);
		}
	}
	
	public void handleStateSelect() {
		if (this.idEstado != -1) {
			this.municipioPkSelect.getEstados().getEstadosPK().getPais().setPaisCve(idPais);
			this.municipioPkSelect.getEstados().getEstadosPK().setEstadoCve(idEstado);
			this.municipioSelect.setMunicipiosPK(municipioPkSelect);
			listaMunicipios = municipiosDao.buscarPorPaisEstado(municipioSelect);
		}
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

	public List<Estados> getListaTmpEstados() {
		return listaTmpEstados;
	}

	public void setListaTmpEstados(List<Estados> listaTmpEstados) {
		this.listaTmpEstados = listaTmpEstados;
	}

	public List<Municipios> getListaMunicipios() {
		return listaMunicipios;
	}

	public void setListaMunicipios(List<Municipios> listaMunicipios) {
		this.listaMunicipios = listaMunicipios;
	}

	public List<Municipios> getListaMunicipiosSelect() {
		return listaMunicipiosSelect;
	}

	public void setListaMunicipiosSelect(List<Municipios> listaMunicipiosSelect) {
		this.listaMunicipiosSelect = listaMunicipiosSelect;
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

	public MunicipiosPK getMunicipioPkSelect() {
		return municipioPkSelect;
	}

	public void setMunicipioPkSelect(MunicipiosPK municipioPkSelect) {
		this.municipioPkSelect = municipioPkSelect;
	}

	public Municipios getMunicipioSelect() {
		return municipioSelect;
	}

	public void setMunicipioSelect(Municipios municipioSelect) {
		this.municipioSelect = municipioSelect;
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

	public MunicipiosDAO getMunicipiosDao() {
		return municipiosDao;
	}

	public void setMunicipiosDao(MunicipiosDAO municipiosDao) {
		this.municipiosDao = municipiosDao;
	}

	public int getIdPais() {
		return idPais;
	}

	public void setIdPais(int idPais) {
		this.idPais = idPais;
	}

	public int getIdEstado() {
		return idEstado;
	}

	public void setIdEstado(int idEstado) {
		this.idEstado = idEstado;
	}
	
}
