package mx.com.ferbo.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import mx.com.ferbo.dao.CiudadesDAO;
import mx.com.ferbo.dao.EstadosDAO;
import mx.com.ferbo.dao.MunicipiosDAO;
import mx.com.ferbo.dao.PaisesDAO;
import mx.com.ferbo.model.Ciudades;
import mx.com.ferbo.model.Estados;
import mx.com.ferbo.model.Municipios;
import mx.com.ferbo.model.Paises;

@Named
@ViewScoped
public class LocalizacionBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Paises pais;
	private Estados estado;
	private Municipios municipio;
	private Ciudades ciudad;

	private List<Paises> listaPaises;
	private List<Estados> listaEstados;
	private List<Municipios> listaMunicipios;
	private List<Ciudades> listaCiudades;

	private PaisesDAO paisesDao;
	private EstadosDAO estadosDao;
	private MunicipiosDAO municipiosDao;
	private CiudadesDAO ciudadesDao;

	public LocalizacionBean() {
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
		listaEstados = estadosDao.buscarTodos();
		listaMunicipios = municipiosDao.buscarTodos();
		listaCiudades = ciudadesDao.buscarTodos();
	}

	public Paises getPais() {
		return pais;
	}

	public void setPais(Paises pais) {
		this.pais = pais;
	}

	public Estados getEstado() {
		return estado;
	}

	public void setEstado(Estados estado) {
		this.estado = estado;
	}

	public Municipios getMunicipio() {
		return municipio;
	}

	public void setMunicipio(Municipios municipio) {
		this.municipio = municipio;
	}

	public Ciudades getCiudad() {
		return ciudad;
	}

	public void setCiudad(Ciudades ciudad) {
		this.ciudad = ciudad;
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

}
