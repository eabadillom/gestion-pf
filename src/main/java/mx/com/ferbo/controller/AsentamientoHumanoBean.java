package mx.com.ferbo.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;

import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.apache.poi.ss.formula.functions.T;
import org.primefaces.PrimeFaces;

import mx.com.ferbo.dao.AsentamientoHumandoDAO;
import mx.com.ferbo.dao.CiudadesDAO;
import mx.com.ferbo.dao.EntidadPostalDAO;
import mx.com.ferbo.dao.EstadosDAO;
import mx.com.ferbo.dao.MunicipiosDAO;
import mx.com.ferbo.dao.PaisesDAO;
import mx.com.ferbo.dao.TipoAsentamientoDAO;
import mx.com.ferbo.model.AsentamientoHumano;
import mx.com.ferbo.model.AsentamientoHumanoPK;
import mx.com.ferbo.model.Ciudades;
import mx.com.ferbo.model.CiudadesPK;
import mx.com.ferbo.model.EntidadPostal;
import mx.com.ferbo.model.Estados;
import mx.com.ferbo.model.EstadosPK;
import mx.com.ferbo.model.Municipios;
import mx.com.ferbo.model.MunicipiosPK;
import mx.com.ferbo.model.Paises;
import mx.com.ferbo.model.TipoAsentamiento;

@Named
@ViewScoped
public class AsentamientoHumanoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<Paises> listaPaises;
	private List<Estados> listaEstados;
	private List<Estados> listaTmpEstados;
	private List<Municipios> listaMunicipios;
	private List<Municipios> listaTmpMunicipios;
	private List<Ciudades> listaCiudades;
	private List<Ciudades> listaTmpCiudades;
	private List<TipoAsentamiento> listaTipoAsentamiento;
	private List<EntidadPostal> listaEntidadPostal;
	private List<AsentamientoHumano> listaAsentamientoHumano;

	private List<Ciudades> listaAsentamientoHumanoSelect;

	private Paises paisSelect;
	private EstadosPK estadoPkSelect;
	private Estados estadoSelect;
	private MunicipiosPK municipioPkSelect;
	private Municipios municipioSelect;
	private CiudadesPK ciudadPKSelect;
	private Ciudades ciudadSelect;
	private TipoAsentamiento tipoAsentamientoSelect;
	private EntidadPostal entidadPostalSelect;
	private AsentamientoHumanoPK asentamientoHumanoPKSelect;
	private AsentamientoHumano asentamientoHumanoSelect;

	private PaisesDAO paisesDao;
	private EstadosDAO estadosDao;
	private MunicipiosDAO municipiosDao;
	private CiudadesDAO ciudadesDao;
	private TipoAsentamientoDAO tipoAsentamientoDao;
	private EntidadPostalDAO entidadPostalDao;
	private AsentamientoHumandoDAO asentamientoHumandoDao;

	private int idPais;
	private int idEstado;
	private int idMunicipio;
	private int idCiudad;
	private short idTipoAsentamiento;
	private int idEntidadPostal;

	public AsentamientoHumanoBean() {
		paisesDao = new PaisesDAO();
		estadosDao = new EstadosDAO();
		municipiosDao = new MunicipiosDAO();
		ciudadesDao = new CiudadesDAO();
		tipoAsentamientoDao = new TipoAsentamientoDAO();
		entidadPostalDao = new EntidadPostalDAO();
		asentamientoHumandoDao = new AsentamientoHumandoDAO();
		
		listaPaises = new ArrayList<>();
		listaEstados = new ArrayList<>();
		listaMunicipios = new ArrayList<>();
		listaCiudades = new ArrayList<>();
		listaTipoAsentamiento = new ArrayList<>();
		listaEntidadPostal = new ArrayList<>();
		listaAsentamientoHumano = new ArrayList<>();
	}

	@PostConstruct
	public void init() {
		listaPaises = paisesDao.buscarTodos();
		listaTipoAsentamiento = tipoAsentamientoDao.buscarTodos();
		listaEntidadPostal = entidadPostalDao.buscarTodos();
		
		this.paisSelect = new Paises();
		this.estadoSelect = new Estados();
		this.estadoPkSelect = new EstadosPK();
		this.municipioSelect = new Municipios();
		this.municipioPkSelect = new MunicipiosPK();
		this.ciudadPKSelect = new CiudadesPK();
		this.ciudadSelect = new Ciudades();
		this.tipoAsentamientoSelect = new TipoAsentamiento();
		this.entidadPostalSelect = new EntidadPostal();
		this.asentamientoHumanoSelect = new AsentamientoHumano();
		this.asentamientoHumanoPKSelect = new AsentamientoHumanoPK();
	}

	public void nuevoAsentamientoHumano() {
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
		this.tipoAsentamientoSelect = new TipoAsentamiento();
		asentamientoHumanoSelect.setTipoAsentamiento(tipoAsentamientoSelect);
		this.entidadPostalSelect = new EntidadPostal();
		asentamientoHumanoSelect.setEntidadPostal(entidadPostalSelect);
		this.asentamientoHumanoPKSelect = new AsentamientoHumanoPK();
		asentamientoHumanoSelect.setAsentamientoHumanoPK(asentamientoHumanoPKSelect);
	}
	
	public void guardarAsentamientoHumano() {
		if (this.asentamientoHumanoSelect.getAsentamientoHumanoPK().getAsentamientoCve() == 0) {
			List<AsentamientoHumano> listaAsentamientoCiudadMunicipioEstadoPais = asentamientoHumandoDao.buscarPorCriterios(asentamientoHumanoSelect);
			int tamanioListaAsentamientoCiudadMunicipioEstadoPais = listaAsentamientoCiudadMunicipioEstadoPais.size() + 1;
			asentamientoHumanoPKSelect.setAsentamientoCve(tamanioListaAsentamientoCiudadMunicipioEstadoPais);
			asentamientoHumanoPKSelect.setPaisCve(idPais);
			asentamientoHumanoPKSelect.setEstadoCve(idEstado);
			asentamientoHumanoPKSelect.setMunicipioCve(idMunicipio);
			asentamientoHumanoPKSelect.setCiudadCve(idCiudad);
			asentamientoHumanoPKSelect.setTipoasntmntoCve(idTipoAsentamiento);
			asentamientoHumanoPKSelect.setEntidadpostalCve(idEntidadPostal);
			asentamientoHumanoSelect.setAsentamientoHumanoPK(asentamientoHumanoPKSelect);
			asentamientoHumanoSelect.getTipoAsentamiento().setTipoasntmntoCve(idTipoAsentamiento);
			asentamientoHumanoSelect.getEntidadPostal().setEntidadpostalCve(idEntidadPostal);
//			this.entidadPostalSelect.setEntidadpostalCve(idEntidadPostal);
//			this.tipoAsentamientoSelect.setTipoasntmntoCve(idTipoAsentamiento);
			
			if(asentamientoHumandoDao.guardar(asentamientoHumanoSelect) == null) {
				this.listaAsentamientoHumano.add(this.asentamientoHumanoSelect);
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Colonia Agregada"));
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Error", "Ocurrió un error al intentar guardar la Colonia"));
			}
		} else {
			if(asentamientoHumandoDao.actualizar(asentamientoHumanoSelect) == null) {
				this.listaAsentamientoHumano.add(this.asentamientoHumanoSelect);
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Colonia Actualizada"));
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Error", "Ocurrió un error al intentar actualizar la Colonia"));
			}
		} 
		PrimeFaces.current().executeScript("PF('nuevoAsentemientoHumanoDialog').hide()");
		PrimeFaces.current().ajax().update("form");
	}

	public void eliminandoAsentamientoHumano() {
		if (asentamientoHumandoDao.eliminar(asentamientoHumanoSelect) == null) {
			this.listaAsentamientoHumano.remove(this.asentamientoHumanoSelect);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Colonia Eliminada"));
			PrimeFaces.current().ajax().update("form:messages", "form:dt-AsentamientoHumano");
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
					"Ocurrió un error al intentar eliminar la Ciudad"));
		}
		PrimeFaces.current().executeScript("PF('deleteAsentamientoHumanoDialog').hide()");
		PrimeFaces.current().ajax().update("form:messages");
	}
	
	public void handleContrySelect() {
		if (this.idPais != -1) {
			this.paisSelect.setPaisCve(idPais);
			estadoSelect.setPaises(paisSelect);
			listaEstados = estadosDao.buscarPorCriteriosEstados(estadoSelect);
		}
	}
	
	public void handleStateSelect() {
		if (this.idEstado != -1) {
			this.municipioPkSelect.setPaisCve(idPais);
			this.municipioPkSelect.setEstadoCve(idEstado);
			this.municipioSelect.setMunicipiosPK(municipioPkSelect);
			listaMunicipios = municipiosDao.buscarPorCriteriosMunicipios(municipioSelect);
		}
	}
	
	public void handleMunicipalitySelect() {
		if (this.idMunicipio != -1) {
			this.ciudadPKSelect.setPaisCve(idPais);
			this.ciudadPKSelect.setEstadoCve(idEstado);
			this.ciudadPKSelect.setMunicipioCve(idMunicipio);
			this.ciudadSelect.setCiudadesPK(ciudadPKSelect);
			listaCiudades = ciudadesDao.buscarPorCriteriosCiudades(ciudadSelect);
		}
	}
	
	public void handleCitySelect() {
		if (this.idCiudad != -1) {
			this.asentamientoHumanoPKSelect.setPaisCve(idPais);
			this.asentamientoHumanoPKSelect.setEstadoCve(idEstado);
			this.asentamientoHumanoPKSelect.setMunicipioCve(idMunicipio);
			this.asentamientoHumanoPKSelect.setCiudadCve(idCiudad);
			if (idEntidadPostal != 0) {
				this.asentamientoHumanoPKSelect.setEntidadpostalCve(idEntidadPostal);
			}
			if (idTipoAsentamiento != 0) {
				this.asentamientoHumanoPKSelect.setTipoasntmntoCve(idTipoAsentamiento);
			}
			this.asentamientoHumanoSelect.setAsentamientoHumanoPK(asentamientoHumanoPKSelect);
//			this.asentamientoHumanoSelect.getTipoAsentamiento().setTipoasntmntoCve(idTipoAsentamiento);
//			this.asentamientoHumanoSelect.getEntidadPostal().setEntidadpostalCve(idEntidadPostal);
			listaAsentamientoHumano = asentamientoHumandoDao.buscarPorCriterios(asentamientoHumanoSelect);
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

	public List<Municipios> getListaTmpMunicipios() {
		return listaTmpMunicipios;
	}

	public void setListaTmpMunicipios(List<Municipios> listaTmpMunicipios) {
		this.listaTmpMunicipios = listaTmpMunicipios;
	}

	public List<Ciudades> getListaCiudades() {
		return listaCiudades;
	}

	public void setListaCiudades(List<Ciudades> listaCiudades) {
		this.listaCiudades = listaCiudades;
	}

	public List<Ciudades> getListaTmpCiudades() {
		return listaTmpCiudades;
	}

	public void setListaTmpCiudades(List<Ciudades> listaTmpCiudades) {
		this.listaTmpCiudades = listaTmpCiudades;
	}

	public List<TipoAsentamiento> getListaTipoAsentamiento() {
		return listaTipoAsentamiento;
	}

	public void setListaTipoAsentamiento(List<TipoAsentamiento> listaTipoAsentamiento) {
		this.listaTipoAsentamiento = listaTipoAsentamiento;
	}

	public List<EntidadPostal> getListaEntidadPostal() {
		return listaEntidadPostal;
	}

	public void setListaEntidadPostal(List<EntidadPostal> listaEntidadPostal) {
		this.listaEntidadPostal = listaEntidadPostal;
	}

	public List<AsentamientoHumano> getListaAsentamientoHumano() {
		return listaAsentamientoHumano;
	}

	public void setListaAsentamientoHumano(List<AsentamientoHumano> listaAsentamientoHumano) {
		this.listaAsentamientoHumano = listaAsentamientoHumano;
	}

	public List<Ciudades> getListaAsentamientoHumanoSelect() {
		return listaAsentamientoHumanoSelect;
	}

	public void setListaAsentamientoHumanoSelect(List<Ciudades> listaAsentamientoHumanoSelect) {
		this.listaAsentamientoHumanoSelect = listaAsentamientoHumanoSelect;
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

	public TipoAsentamiento getTipoAsentamientoSelect() {
		return tipoAsentamientoSelect;
	}

	public void setTipoAsentamientoSelect(TipoAsentamiento tipoAsentamientoSelect) {
		this.tipoAsentamientoSelect = tipoAsentamientoSelect;
	}

	public EntidadPostal getEntidadPostalSelect() {
		return entidadPostalSelect;
	}

	public void setEntidadPostalSelect(EntidadPostal entidadPostalSelect) {
		this.entidadPostalSelect = entidadPostalSelect;
	}

	public AsentamientoHumanoPK getAsentamientoHumanoPKSelect() {
		return asentamientoHumanoPKSelect;
	}

	public void setAsentamientoHumanoPKSelect(AsentamientoHumanoPK asentamientoHumanoPKSelect) {
		this.asentamientoHumanoPKSelect = asentamientoHumanoPKSelect;
	}

	public AsentamientoHumano getAsentamientoHumanoSelect() {
		return asentamientoHumanoSelect;
	}

	public void setAsentamientoHumanoSelect(AsentamientoHumano asentamientoHumanoSelect) {
		this.asentamientoHumanoSelect = asentamientoHumanoSelect;
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

	public TipoAsentamientoDAO getTipoAsentamientoDao() {
		return tipoAsentamientoDao;
	}

	public void setTipoAsentamientoDao(TipoAsentamientoDAO tipoAsentamientoDao) {
		this.tipoAsentamientoDao = tipoAsentamientoDao;
	}

	public EntidadPostalDAO getEntidadPostalDao() {
		return entidadPostalDao;
	}

	public void setEntidadPostalDao(EntidadPostalDAO entidadPostalDao) {
		this.entidadPostalDao = entidadPostalDao;
	}

	public AsentamientoHumandoDAO getAsentamientoHumandoDao() {
		return asentamientoHumandoDao;
	}

	public void setAsentamientoHumandoDao(AsentamientoHumandoDAO asentamientoHumandoDao) {
		this.asentamientoHumandoDao = asentamientoHumandoDao;
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

	public int getIdCiudad() {
		return idCiudad;
	}

	public void setIdCiudad(int idCiudad) {
		this.idCiudad = idCiudad;
	}

	public short getIdTipoAsentamiento() {
		return idTipoAsentamiento;
	}

	public void setIdTipoAsentamiento(short idTipoAsentamiento) {
		this.idTipoAsentamiento = idTipoAsentamiento;
	}

	public int getIdEntidadPostal() {
		return idEntidadPostal;
	}

	public void setIdEntidadPostal(int idEntidadPostal) {
		this.idEntidadPostal = idEntidadPostal;
	}

	

	

}
