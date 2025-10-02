package mx.com.ferbo.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;

import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.PrimeFaces;

import mx.com.ferbo.dao.CiudadesDAO;
import mx.com.ferbo.dao.EstadosDAO;
import mx.com.ferbo.dao.MunicipiosDAO;
import mx.com.ferbo.dao.PaisesDAO;

import mx.com.ferbo.model.Ciudades;
import mx.com.ferbo.model.CiudadesPK;
import mx.com.ferbo.model.Estados;
import mx.com.ferbo.model.EstadosPK;
import mx.com.ferbo.model.Municipios;
import mx.com.ferbo.model.MunicipiosPK;
import mx.com.ferbo.model.Paises;

@Named
@ViewScoped
public class CiudadesBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<Paises> listaPaises;
	private List<Estados> listaEstados;
	private List<Municipios> listaMunicipios;
	private List<Ciudades> listaCiudades;

	private List<Ciudades> listaCiudadesSelect;

	private Paises paisSelect;
	private EstadosPK estadoPkSelect;
	private Estados estadoSelect;
	private MunicipiosPK municipioPkSelect;
	private Municipios municipioSelect;
	private CiudadesPK ciudadPKSelect;
	private Ciudades ciudadSelect;

	private PaisesDAO paisesDao;
	private EstadosDAO estadosDao;
	private MunicipiosDAO municipiosDao;
	private CiudadesDAO ciudadesDao;

	private int idPais;
	private int idEstado;
	private int idMunicipio;

	public CiudadesBean() {
		paisesDao = new PaisesDAO();
		estadosDao = new EstadosDAO();
		municipiosDao = new MunicipiosDAO();
		ciudadesDao = new CiudadesDAO();
		listaPaises = new ArrayList<>();
		listaEstados = new ArrayList<>();
		listaMunicipios = new ArrayList<>();
		listaCiudades = new ArrayList<>();
	}

	@PostConstruct
	public void init() {
		listaPaises = paisesDao.buscarTodos();
		this.paisSelect = new Paises();
		this.estadoSelect = new Estados();
		this.estadoPkSelect = new EstadosPK();
		this.municipioSelect = new Municipios();
		this.municipioPkSelect = new MunicipiosPK();
		this.ciudadPKSelect = new CiudadesPK();
		this.ciudadSelect = new Ciudades();
	}

	public void nuevaCiudad() {
		this.paisSelect = new Paises();
		this.estadoSelect = new Estados();
		this.estadoPkSelect = new EstadosPK();
		estadoSelect.setEstadosPK(estadoPkSelect);
		this.municipioSelect = new Municipios();
		this.municipioPkSelect = new MunicipiosPK();
		municipioSelect.setMunicipiosPK(municipioPkSelect);
		this.ciudadSelect = new Ciudades();
		this.ciudadPKSelect = new CiudadesPK();
		ciudadSelect.setCiudadesPK(ciudadPKSelect);
		
		if(idPais!=0 || idEstado !=0 || idMunicipio !=0) {
			handleMunicipalitySelect();
		}
		
	}
	
	public void guardarCiudad() {
		if (this.ciudadSelect.getCiudadesPK().getCiudadCve() == 0) {
			List<Ciudades> listaCiudadMunicipioEstadoPais = ciudadesDao.buscarPorCriteriosCiudades(ciudadSelect);
			int tamanioListaCiudadMunicipioEstadoPais = listaCiudadMunicipioEstadoPais.size() + 1;
			ciudadPKSelect.setCiudadCve(tamanioListaCiudadMunicipioEstadoPais);
			ciudadSelect.setCiudadesPK(ciudadPKSelect);
			
			this.municipioPkSelect.getEstados().getEstadosPK().getPais().setPaisCve(idPais);
			this.municipioPkSelect.getEstados().getEstadosPK().setEstadoCve(idEstado);
			this.municipioSelect.setMunicipiosPK(municipioPkSelect);			
			List<Municipios> listaMunicipiosTmp = municipiosDao.buscarPorPaisEstado(municipioSelect);
			List<Municipios> municipioTmp = listaMunicipiosTmp.stream().filter(m ->m.getMunicipiosPK().getMunicipioCve()==idMunicipio).collect(Collectors.toList());
			Municipios municipio = municipioTmp.get(0);
			ciudadSelect.getCiudadesPK().setMunicipios(municipio);
			if(ciudadesDao.guardar(ciudadSelect) == null) {
				this.listaCiudades.add(this.ciudadSelect);
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Ciudad Agregada"));
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Error", "Ocurrió un error al intentar guardar la Ciudad"));
			}
		} else {
			if(ciudadesDao.actualizar(ciudadSelect) == null) {
//				this.listaCiudades.add(this.ciudadSelect);
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Ciudad Actualizada"));
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Error", "Ocurrió un error al intentar actualizar la Ciudad"));
			}
		} 
		listaCiudades = ciudadesDao.buscarPorCriterios(ciudadSelect);
		PrimeFaces.current().executeScript("PF('nuevaCiudadDialog').hide()");
		PrimeFaces.current().ajax().update("form:dt-Ciudades");
	}
	
	
	public void eliminandoCiudad() {
		if (ciudadesDao.eliminar(ciudadSelect) == null) {
			this.listaCiudades.remove(this.ciudadSelect);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Ciudad Eliminada"));
			PrimeFaces.current().ajax().update("form:messages", "form:dt-Ciudades");
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
					"Ocurrió un error al intentar eliminar la Ciudad"));
		}
		PrimeFaces.current().executeScript("PF('deleteCiudadDialog').hide()");
		PrimeFaces.current().ajax().update("form:messages");
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
	
	public void handleMunicipalitySelect() {
		if (this.idMunicipio != -1) {
			this.ciudadPKSelect.getMunicipios().getMunicipiosPK().getEstados().getEstadosPK().getPais().setPaisCve(idPais);
			this.ciudadPKSelect.getMunicipios().getMunicipiosPK().getEstados().getEstadosPK().setEstadoCve(idEstado);
			this.ciudadPKSelect.getMunicipios().getMunicipiosPK().setMunicipioCve(idMunicipio);
			this.ciudadSelect.setCiudadesPK(ciudadPKSelect);
			listaCiudades = ciudadesDao.buscarPorCriteriosCiudades(ciudadSelect);
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

	public List<Municipios> getListaMunicipios() {
		return listaMunicipios;
	}

	public void setListaMunicipios(List<Municipios> listaMunicipios) {
		this.listaMunicipios = listaMunicipios;
	}
	
	public List<Ciudades> getListaCiudades() {
		return listaCiudades;
	}

	public void setListaCiudades(List<Ciudades> listaCiudades) {
		this.listaCiudades = listaCiudades;
	}

	public List<Ciudades> getListaCiudadesSelect() {
		return listaCiudadesSelect;
	}

	public void setListaCiudadesSelect(List<Ciudades> listaCiudadesSelect) {
		this.listaCiudadesSelect = listaCiudadesSelect;
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

	public CiudadesPK getCiudadPKSelect() {
		return ciudadPKSelect;
	}

	public void setCiudadPKSelect(CiudadesPK ciudadPKSelect) {
		this.ciudadPKSelect = ciudadPKSelect;
	}

	public Ciudades getCiudadSelect() {
		return ciudadSelect;
	}

	public void setCiudadSelect(Ciudades ciudadSelect) {
		this.ciudadSelect = ciudadSelect;
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

	public CiudadesDAO getCiudadesDao() {
		return ciudadesDao;
	}

	public void setCiudadesDao(CiudadesDAO ciudadesDao) {
		this.ciudadesDao = ciudadesDao;
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

	public int getIdMunicipio() {
		return idMunicipio;
	}

	public void setIdMunicipio(int idMunicipio) {
		this.idMunicipio = idMunicipio;
	}

}
